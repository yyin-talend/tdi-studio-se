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

import java.io.File;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.talend.commons.ui.image.ImageProvider;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.ui.images.ECoreImage;
import org.talend.core.ui.images.OverlayImageProvider;
import org.talend.repository.i18n.Messages;
import org.talend.repository.model.RepositoryNode;
import org.talend.repository.model.RepositoryNode.EProperties;
import org.talend.repository.ui.wizards.metadata.connection.genericshema.ImportSchemaFileWizard;

/**
 * DOC ggu class global comment. Detailled comment <br/>
 * 
 */
public class ImportSchemaFileAction extends CreateGenericSchemaAction {

    private static final String LABEL = Messages.getString("ImportSchemaFileAction.Label"); //$NON-NLS-1$

    private File file = null;

    /**
     * DOC ggu ImportSchemaFileAction constructor comment.
     */
    public ImportSchemaFileAction() {
        this.setText(LABEL);
        this.setToolTipText(LABEL);
        this.setImageDescriptor(OverlayImageProvider.getImageWithNew(ImageProvider
                .getImage(ECoreImage.METADATA_FILE_LDIF_ICON)));

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.ui.actions.metadata.CreateGenericSchemaAction#getClassForDoubleClick()
     */
    @Override
    public Class getClassForDoubleClick() {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.ui.actions.metadata.CreateGenericSchemaAction#init(org.talend.repository.model.RepositoryNode)
     */
    @Override
    protected void init(RepositoryNode node) {

        ERepositoryObjectType nodeType = (ERepositoryObjectType) node.getProperties(EProperties.CONTENT_TYPE);
        if (nodeType == null) {
            return;
        }
        if (nodeType != ERepositoryObjectType.METADATA_GENERIC_SCHEMA) {
            return;
        }

        switch (node.getType()) {
        case SIMPLE_FOLDER:
        case SYSTEM_FOLDER:
            collectChildNames(node);
            setEnabled(true);
            break;
        default:
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
        file = openImportFileDialog();
        if (file == null) {
            return;
        }

        openImportWizard();
    }

    private File openImportFileDialog() {
        FileDialog dial = new FileDialog(PlatformUI.getWorkbench().getDisplay().getActiveShell(), SWT.OPEN);
        dial.setFilterExtensions(new String[] { "*.xml" }); //$NON-NLS-1$
        String fileName = dial.open();
        if ((fileName != null) && (!fileName.equals(""))) { //$NON-NLS-1$
            return new File(fileName);
        } else {
            return null;
        }
    }

    private void openImportWizard() {
        ISelection selection = getSelection();
        ImportSchemaFileWizard wizard = new ImportSchemaFileWizard(PlatformUI.getWorkbench(), selection,
                getExistingNames(), file);
        if (!wizard.isInitOK()) {
            wizard.dispose();
            return;
        }
        WizardDialog wizardDialog = new WizardDialog(new Shell(), wizard);
        wizardDialog.setPageSize(WIZARD_WIDTH, WIZARD_HEIGHT);
        wizardDialog.create();
        wizardDialog.open();
        refresh(((IStructuredSelection) selection).getFirstElement());
    }

}
