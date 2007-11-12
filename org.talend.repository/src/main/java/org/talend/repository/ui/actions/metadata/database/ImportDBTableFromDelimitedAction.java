// ============================================================================
//
// Copyright (C) 2006-2007 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the  agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//   
// ============================================================================
package org.talend.repository.ui.actions.metadata.database;

import org.apache.log4j.Logger;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;
import org.talend.commons.ui.image.ImageProvider;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.ui.images.ECoreImage;
import org.talend.core.ui.images.OverlayImageProvider;
import org.talend.repository.RepositoryPlugin;
import org.talend.repository.i18n.Messages;
import org.talend.repository.model.RepositoryNode;
import org.talend.repository.model.RepositoryNode.EProperties;
import org.talend.repository.ui.actions.metadata.AbstractCreateTableAction;
import org.talend.repository.ui.actions.metadata.database.wizard.ImportDBTableWizard;

/**
 * DOC ggu class global comment. Detailled comment <br/>
 * 
 */
public class ImportDBTableFromDelimitedAction extends AbstractCreateTableAction {

    protected static Logger log = Logger.getLogger(ImportDBTableFromDelimitedAction.class);

    protected static final String PID = RepositoryPlugin.PLUGIN_ID;

    private static final String LABEL = Messages.getString("ImportDBTableFromDelimitedAction.Label"); //$NON-NLS-1$

    /**
     * DOC ggu ImportConnectionTableFromCSV constructor comment.
     */
    public ImportDBTableFromDelimitedAction() {
        this.setText(LABEL);
        this.setToolTipText(LABEL);
        this
                .setImageDescriptor(OverlayImageProvider.getImageWithNew(ImageProvider
                        .getImage(ECoreImage.METADATA_CONNECTION_ICON)));
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
        if (nodeType != ERepositoryObjectType.METADATA_CONNECTIONS) {
            return;
        }

        switch (node.getType()) {
        case SIMPLE_FOLDER:
        case SYSTEM_FOLDER:
            collectChildNames(node);
            setEnabled(true);
            break;
        default:
            break;
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
