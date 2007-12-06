// ============================================================================
//
// Copyright (C) 2006-2007 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.repository.ui.actions.metadata.importing;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;
import org.talend.commons.ui.image.ImageProvider;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.ui.images.ECoreImage;
import org.talend.core.ui.images.OverlayImageProvider;
import org.talend.repository.i18n.Messages;
import org.talend.repository.model.RepositoryNode;
import org.talend.repository.model.RepositoryNode.EProperties;
import org.talend.repository.ui.actions.metadata.AbstractCreateAction;
import org.talend.repository.ui.actions.metadata.database.wizard.ImportDBTableWizard;

/**
 * DOC ggu class global comment. Detailled comment
 */
public class ImportTablesFromDelimitedAction extends AbstractCreateAction {

    private static final String LABEL = Messages.getString("ImportTablesFromDelimitedAction.Label"); //$NON-NLS-1$

    public ImportTablesFromDelimitedAction() {
        this.setText(LABEL);
        this.setToolTipText(LABEL);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.ui.actions.metadata.AbstractCreateAction#init(org.talend.repository.model.RepositoryNode)
     */
    @Override
    protected void init(RepositoryNode node) {
        ERepositoryObjectType nodeType = (ERepositoryObjectType) node.getProperties(EProperties.CONTENT_TYPE);
        if (nodeType == null) {
            return;
        }
        switch (nodeType) {
        default:
            // unenabled
            return;
        case METADATA_CONNECTIONS:
            this.setImageDescriptor(OverlayImageProvider.getImageWithNew(ImageProvider
                    .getImage(ECoreImage.METADATA_CONNECTION_ICON)));
            break;
        case METADATA_FILE_DELIMITED:
            this.setImageDescriptor(OverlayImageProvider.getImageWithNew(ImageProvider
                    .getImage(ECoreImage.METADATA_FILE_DELIMITED_ICON)));
            break;
        // case METADATA_FILE_POSITIONAL:
        // case METADATA_FILE_REGEXP:
        // case METADATA_FILE_XML:
        // case METADATA_FILE_LDIF:
        // case METADATA_GENERIC_SCHEMA:
        // case METADATA_LDAP_SCHEMA:

        }

        switch (node.getType()) {
        case SIMPLE_FOLDER:
        case SYSTEM_FOLDER:
            collectChildNames(node);
            setEnabled(true);
            break;
        default:
            // not enabled
            return;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.action.Action#run()
     */
    @Override
    public void run() {
        expandNode();
        showOptionDialog();
    }

    private void expandNode() {
        RepositoryNode node = null;

        Object userSelection = ((IStructuredSelection) getSelection()).getFirstElement();
        if (userSelection instanceof RepositoryNode) {

            switch (((RepositoryNode) userSelection).getType()) {
            case REPOSITORY_ELEMENT:
            case SIMPLE_FOLDER:
            case SYSTEM_FOLDER:
                node = (RepositoryNode) userSelection;
                break;
            default:
                return;
            }
        }
        getViewPart().setFocus();
        getViewPart().expand(node, true);
    }

    private void showOptionDialog() {

        ImportDBTableWizard importWizard = new ImportDBTableWizard(PlatformUI.getWorkbench(), getSelection());

        // Open the Wizard
        WizardDialog wizardDialog = new WizardDialog(Display.getCurrent().getActiveShell(), importWizard);
        wizardDialog.setPageSize(200, 120);
        wizardDialog.create();
        wizardDialog.open();
        refresh(((IStructuredSelection) getSelection()).getFirstElement());
    }

}
