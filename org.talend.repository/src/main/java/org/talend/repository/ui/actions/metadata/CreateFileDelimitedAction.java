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

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.talend.core.model.properties.DelimitedFileConnectionItem;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.ui.images.EImage;
import org.talend.core.ui.images.ImageProvider;
import org.talend.repository.i18n.Messages;
import org.talend.repository.model.ProxyRepositoryFactory;
import org.talend.repository.model.RepositoryNode;
import org.talend.repository.model.RepositoryNode.EProperties;
import org.talend.repository.ui.wizards.metadata.connection.files.delimited.DelimitedFileWizard;

/**
 * Action used to create a new "file delimited" metadata.<br/>
 * 
 * $Id$
 * 
 */
public class CreateFileDelimitedAction extends AbstractCreateAction {

    private static final String EDIT_LABEL = Messages.getString("CreateFileDelimitedAction.action.editTitle"); //$NON-NLS-1$

    private static final String CREATE_LABEL = Messages.getString("CreateFileDelimitedAction.action.createTitle"); //$NON-NLS-1$

    private static final String OPEN_LABEL = Messages.getString("CreateFileDelimitedAction.action.openTitle"); //$NON-NLS-1$

    protected static final int WIZARD_WIDTH = 800;

    protected static final int WIZARD_HEIGHT = 475;

    private boolean creation = false;
    
    public CreateFileDelimitedAction() {
        super();

        this.setText(CREATE_LABEL);
        this.setToolTipText(CREATE_LABEL);
        this.setImageDescriptor(ImageProvider.getImageDesc(EImage.METADATA_FILE_DELIMITED_ICON));
    }

    public void run() {
        ISelection selection = getSelection();
        WizardDialog wizardDialog = new WizardDialog(new Shell(), new DelimitedFileWizard(PlatformUI.getWorkbench(), creation, selection,
                getExistingNames()));
        wizardDialog.setPageSize(WIZARD_WIDTH, WIZARD_HEIGHT);
        wizardDialog.create();
        wizardDialog.open();
        refresh(((IStructuredSelection) selection).getFirstElement());
    }

    protected void init(RepositoryNode node) {
        ERepositoryObjectType nodeType = (ERepositoryObjectType) node.getProperties(EProperties.CONTENT_TYPE);
        if (!ERepositoryObjectType.METADATA_FILE_DELIMITED.equals(nodeType)) {
            return;
        }

        switch (node.getType()) {
        case SIMPLE_FOLDER:
        case SYSTEM_FOLDER:
            this.setText(CREATE_LABEL);
            collectChildNames(node);
            creation = true;
            break;
        case REPOSITORY_ELEMENT:
            ProxyRepositoryFactory factory = ProxyRepositoryFactory.getInstance();
            if (factory.isPotentiallyEditable(node.getObject())) {
                this.setText(EDIT_LABEL);
                collectSiblingNames(node);
            } else {
                this.setText(OPEN_LABEL);
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
        return DelimitedFileConnectionItem.class;
    }

}
