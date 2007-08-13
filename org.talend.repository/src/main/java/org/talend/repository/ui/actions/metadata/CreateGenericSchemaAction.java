// ============================================================================
//
// Talend Community Edition
//
// Copyright (C) 2006-2007 Talend - www.talend.com
//
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either
// version 2.1 of the License.
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

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.talend.commons.ui.image.ImageProvider;
import org.talend.core.model.properties.DelimitedFileConnectionItem;
import org.talend.core.model.properties.GenericSchemaConnectionItem;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.ui.images.ECoreImage;
import org.talend.core.ui.images.OverlayImageProvider;
import org.talend.repository.model.IProxyRepositoryFactory;
import org.talend.repository.model.ProxyRepositoryFactory;
import org.talend.repository.model.RepositoryNode;
import org.talend.repository.model.RepositoryNode.EProperties;
import org.talend.repository.ui.wizards.metadata.connection.files.delimited.DelimitedFileWizard;
import org.talend.repository.ui.wizards.metadata.connection.genericshema.GenericSchemaWizard;
import org.talend.repository.ui.wizards.metadata.table.database.DatabaseTableWizard;

/**
 * Action used to create a new "file delimited" metadata.<br/>
 * 
 * $Id: CreateFileDelimitedAction.java 3351 2007-05-04 12:14:00Z plegall $
 * 
 */
public class CreateGenericSchemaAction extends AbstractCreateAction {

    private String editLabel; //$NON-NLS-1$

    private String createLabel;

    private String openLabel;

    protected static final int WIZARD_WIDTH = 800;

    protected static final int WIZARD_HEIGHT = 475;

    private boolean creation = false;

    private ImageDescriptor defaultImage, createImage;

    private ERepositoryObjectType currentNodeType;

    public CreateGenericSchemaAction() {
        super();
        createLabel = "Create generic schema";
        editLabel = "Edit generic schema";
        openLabel = "Open generic schema";

        defaultImage = ImageProvider.getImageDesc(ECoreImage.METADATA_FILE_LDIF_ICON);
        createImage = OverlayImageProvider.getImageWithNew(ImageProvider.getImage(ECoreImage.METADATA_FILE_LDIF_ICON));

        setText(createLabel);
        setToolTipText(createLabel);
        setImageDescriptor(defaultImage);

        currentNodeType = ERepositoryObjectType.METADATA_GENERIC_SCHEMA;
    }

    public void run() {
        ISelection selection = getSelection();
        WizardDialog wizardDialog = new WizardDialog(new Shell(), new GenericSchemaWizard(PlatformUI.getWorkbench(),
                creation, selection, getExistingNames(),false));
        wizardDialog.setPageSize(WIZARD_WIDTH, WIZARD_HEIGHT);
        wizardDialog.create();

        wizardDialog.open();
        refresh(((IStructuredSelection) selection).getFirstElement());
    }

    protected void init(RepositoryNode node) {
        ERepositoryObjectType nodeType = (ERepositoryObjectType) node.getProperties(EProperties.CONTENT_TYPE);
        if (!currentNodeType.equals(nodeType)) {
            return;
        }

        switch (node.getType()) {
        case SIMPLE_FOLDER:
        case SYSTEM_FOLDER:
            this.setText(createLabel);
            collectChildNames(node);
            this.setImageDescriptor(createImage);
            creation = true;
            break;
        case REPOSITORY_ELEMENT:
            IProxyRepositoryFactory factory = ProxyRepositoryFactory.getInstance();
            if (factory.isPotentiallyEditable(node.getObject())) {
                this.setText(editLabel);
                this.setImageDescriptor(defaultImage);
                collectSiblingNames(node);
            } else {
                this.setText(openLabel);
                this.setImageDescriptor(defaultImage);
            }
            collectSiblingNames(node);
            creation = false;
            break;
        default:
            return;
        }
        setEnabled(true);
    }

    public Class getClassForDoubleClick() {
        return GenericSchemaConnectionItem.class;
    }

}
