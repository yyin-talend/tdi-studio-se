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
package org.talend.repository.ui.actions;

import java.util.List;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbench;
import org.talend.commons.ui.image.EImage;
import org.talend.commons.ui.image.ImageProvider;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.repository.i18n.Messages;
import org.talend.repository.model.RepositoryNode;
import org.talend.repository.model.RepositoryNode.EProperties;
import org.talend.repository.ui.wizards.genHTMLDoc.GenerateDocAsHTMLWizard;

/**
 * This class is a action used for generation job information as HTML file.
 * 
 * $Id: GenerateDocAsHTMLAction.java 2007-3-1,下午03:06:53 ftang $
 * 
 */
public class GenerateDocAsHTMLAction extends AContextualAction {

    private static final String GENERATE_DOC_AS_HTML = Messages.getString("GenerateDocAsHTMLAction.GenerateDocAsHTML");

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.ui.actions.ITreeContextualAction#init(org.eclipse.jface.viewers.TreeViewer,
     * org.eclipse.jface.viewers.IStructuredSelection)
     */
    public void init(TreeViewer viewer, IStructuredSelection selection) {
        boolean canWork = false;
        List<RepositoryNode> nodes = (List<RepositoryNode>) selection.toList();
        for (RepositoryNode node : nodes) {
            if (node.getProperties(EProperties.CONTENT_TYPE) == ERepositoryObjectType.PROCESS) {
                canWork = true;
                break;
            }
        }
        setEnabled(canWork);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.ui.actions.AContextualAction#isVisible()
     */
    public boolean isVisible() {
        return isEnabled();
    }

    /**
     * Default constructor.
     */
    public GenerateDocAsHTMLAction() {
        super();
        this.setText(GENERATE_DOC_AS_HTML);
        this.setToolTipText(GENERATE_DOC_AS_HTML);
        this.setImageDescriptor(ImageProvider.getImageDesc(EImage.EXPORT_ICON));
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.action.Action#run()
     */
    public void run() {
        GenerateDocAsHTMLWizard processWizard = new GenerateDocAsHTMLWizard();
        IWorkbench workbench = this.getViewPart().getViewSite().getWorkbenchWindow().getWorkbench();
        processWizard.setWindowTitle(GENERATE_DOC_AS_HTML);
        processWizard.init(workbench, (IStructuredSelection) this.getSelection());

        Shell activeShell = Display.getCurrent().getActiveShell();
        WizardDialog dialog = new WizardDialog(activeShell, processWizard);
        dialog.open();
    }
}
