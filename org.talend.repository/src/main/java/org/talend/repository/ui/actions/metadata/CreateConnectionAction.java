// ============================================================================
//
// Talend Community Edition
//
// Copyright (C) 2006 Talend - www.talend.com
//
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either
// version 2.1 of the License, or (at your option) any later version.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
// Lesser General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with this program; if not, write to the Free Software
// Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
//
// ============================================================================
package org.talend.repository.ui.actions.metadata;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;
import org.talend.commons.ui.image.ImageProvider;
import org.talend.core.model.metadata.builder.connection.ConnectionFactory;
import org.talend.core.model.metadata.builder.connection.DatabaseConnection;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.core.model.properties.DatabaseConnectionItem;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.ui.images.OverlayImageProvider;
import org.talend.core.ui.images.ECoreImage;
import org.talend.repository.RepositoryPlugin;
import org.talend.repository.i18n.Messages;
import org.talend.repository.model.IProxyRepositoryFactory;
import org.talend.repository.model.ProxyRepositoryFactory;
import org.talend.repository.model.RepositoryNode;
import org.talend.repository.model.RepositoryNodeUtilities;
import org.talend.repository.model.RepositoryNode.EProperties;
import org.talend.repository.ui.wizards.metadata.connection.database.DatabaseWizard;

/**
 * Action used to create a new connection.<br/>
 * 
 * $Id$
 * 
 */
public class CreateConnectionAction extends AbstractCreateAction {

    protected static Logger log = Logger.getLogger(CreateConnectionAction.class);

    protected static final String PID = RepositoryPlugin.PLUGIN_ID;

    private static final String EDIT_LABEL = Messages.getString("CreateConnectionAction.action.editTitle"); //$NON-NLS-1$

    private static final String OPEN_LABEL = Messages.getString("CreateConnectionAction.action.openTitle"); //$NON-NLS-1$

    private static final String CREATE_LABEL = Messages.getString("CreateConnectionAction.action.createTitle"); //$NON-NLS-1$

    ImageDescriptor defaultImage = ImageProvider.getImageDesc(ECoreImage.METADATA_CONNECTION_ICON);

    ImageDescriptor createImage = OverlayImageProvider.getImageWithNew(ImageProvider
            .getImage(ECoreImage.METADATA_CONNECTION_ICON));

    public CreateConnectionAction() {
        super();

        this.setText(CREATE_LABEL);
        this.setToolTipText(CREATE_LABEL);
        this.setImageDescriptor(defaultImage);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.action.Action#run()
     */
    public void run() {
        RepositoryNode metadataNode = getViewPart().getRoot().getChildren().get(5);
        RepositoryNode dbConnectionNode = metadataNode.getChildren().get(0);

        // Force focus to the repositoryView and open Metadata and DbConnection nodes
        getViewPart().setFocus();
        getViewPart().expand(metadataNode, true);
        getViewPart().expand(dbConnectionNode, true);

        DatabaseConnection connection = null;
        IPath pathToSave = null;

        // Define the RepositoryNode, by default Metadata/DbConnection
        RepositoryNode node = dbConnectionNode;

        // When the userSelection is an element of metadataNode, use it !
        Object userSelection = ((IStructuredSelection) getSelection()).getFirstElement();
        if (userSelection instanceof RepositoryNode) {
            switch (((RepositoryNode) userSelection).getType()) {
            case REPOSITORY_ELEMENT:
            case SIMPLE_FOLDER:
            case SYSTEM_FOLDER:
                node = (RepositoryNode) userSelection;
                break;
            }
        }
        ISelection selection = getSelection();
        boolean creation = false;
        // Define the repositoryObject DatabaseConnection and his pathToSave
        switch (node.getType()) {
        case REPOSITORY_ELEMENT:
            // pathToSave = null;
            connection = (DatabaseConnection) ((ConnectionItem) node.getObject().getProperty().getItem()).getConnection();
            creation = false;
            break;
        case SIMPLE_FOLDER:
            pathToSave = RepositoryNodeUtilities.getPath(node);
            connection = ConnectionFactory.eINSTANCE.createDatabaseConnection();
            creation = true;
            break;
        case SYSTEM_FOLDER:
            pathToSave = new Path(""); //$NON-NLS-1$
            connection = ConnectionFactory.eINSTANCE.createDatabaseConnection();
            creation = true;
            break;
        }

        // Init the content of the Wizard
        init(node);
        DatabaseWizard databaseWizard = new DatabaseWizard(PlatformUI.getWorkbench(), creation, selection, getExistingNames());

        // Open the Wizard
        WizardDialog wizardDialog = new WizardDialog(Display.getCurrent().getActiveShell(), databaseWizard);
        wizardDialog.setPageSize(600, 475);
        wizardDialog.create();
        wizardDialog.open();
        refresh(((IStructuredSelection) getSelection()).getFirstElement());
    }

    protected void init(RepositoryNode node) {
        ERepositoryObjectType nodeType = (ERepositoryObjectType) node.getProperties(EProperties.CONTENT_TYPE);
        if (!ERepositoryObjectType.METADATA_CONNECTIONS.equals(nodeType)) {
            return;
        }
        switch (node.getType()) {
        case SIMPLE_FOLDER:
        case SYSTEM_FOLDER:
            this.setText(CREATE_LABEL);
            this.setImageDescriptor(createImage);
            collectChildNames(node);
            break;
        case REPOSITORY_ELEMENT:
            IProxyRepositoryFactory factory = ProxyRepositoryFactory.getInstance();
            if (factory.isPotentiallyEditable(node.getObject())) {
                this.setText(EDIT_LABEL);
                this.setImageDescriptor(defaultImage);
                collectSiblingNames(node);
            } else {
                this.setText(OPEN_LABEL);
                this.setImageDescriptor(defaultImage);
            }
            break;
        default:
            return;
        }
        setEnabled(true);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.ui.actions.AContextualView#getClassForDoubleClick()
     */
    @Override
    public Class getClassForDoubleClick() {
        return DatabaseConnectionItem.class;
    }
}
