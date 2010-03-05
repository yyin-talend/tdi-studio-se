// ============================================================================
//
// Copyright (C) 2006-2010 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.designer.core.ui.action;

import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.IWizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorPart;
import org.talend.commons.ui.image.ImageProvider;
import org.talend.core.ui.IUIRefresher;
import org.talend.core.ui.images.ECoreImage;
import org.talend.designer.core.i18n.Messages;
import org.talend.designer.core.ui.wizards.OpenExistVersionProcessWizard;
import org.talend.repository.model.RepositoryNode;
import org.talend.repository.model.RepositoryNodeUtilities;
import org.talend.repository.ui.actions.EditPropertiesAction;

/**
 * DOC xye class global comment. Detailled comment
 */
public class OpenExistVersionProcessAction extends EditPropertiesAction {

    private static final String ACTION_LABEL = Messages.getString("OpenExistVersionProcess.open"); //$NON-NLS-1$

    public OpenExistVersionProcessAction() {
        super();

        this.setText(ACTION_LABEL);
        this.setToolTipText(ACTION_LABEL);
        this.setImageDescriptor(ImageProvider.getImageDesc(ECoreImage.PROCESS_ICON));
    }

    @Override
    protected void doRun() {
        ISelection selection = getSelection();
        Object obj = ((IStructuredSelection) selection).getFirstElement();
        RepositoryNode node = (RepositoryNode) obj;

        IPath path = RepositoryNodeUtilities.getPath(node);
        String originalName = node.getObject().getLabel();

        OpenExistVersionProcessWizard wizard = new OpenExistVersionProcessWizard(node.getObject());
        PropertyManagerWizardDialog dialog = new PropertyManagerWizardDialog(Display.getCurrent().getActiveShell(), wizard);
        dialog.setPageSize(300, 250);
        dialog.setTitle(Messages.getString("OpenExistVersionProcess.open.dialog")); //$NON-NLS-1$
        if (dialog.open() == Dialog.OK) {
            refresh(node);
            // refresh the corresponding editor's name
            IEditorPart part = getCorrespondingEditor(node);
            if (part != null && part instanceof IUIRefresher) {
                ((IUIRefresher) part).refreshName();
            } else {
                processRoutineRenameOperation(originalName, node, path);
            }
        }
    }

    public class PropertyManagerWizardDialog extends WizardDialog {

        /**
         * DOC xye PropertyManagerWizardDialog constructor comment.
         * 
         * @param parentShell
         * @param newWizard
         */
        public PropertyManagerWizardDialog(Shell parentShell, IWizard newWizard) {
            super(parentShell, newWizard);
        }

        public Button getFinishButton() {
            return getButton(IDialogConstants.FINISH_ID);
        }

    }

}
