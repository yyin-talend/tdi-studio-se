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
package org.talend.repository.ui.actions.documentation;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbench;
import org.talend.commons.ui.image.ImageProvider;
import org.talend.core.ui.images.ECoreImage;
import org.talend.repository.ui.wizards.genHTMLDoc.GenerateDocAsHTMLWizard;

/**
 * DOC ftang class global comment. Detailed comment <br/>
 * 
 */
public class ExportHTMLDocumentationAction extends HTMLDocumentationAction {

    protected static final String GENERATE_DOC_AS_HTML = "Export HTML Documentation";

    /**
     * Constructs a new OpenDocumentationAction.
     */
    public ExportHTMLDocumentationAction() {
        super();

        setText("Export HTML Documentation"); //$NON-NLS-1$
        setToolTipText("Export HTML Documentation"); //$NON-NLS-1$
        setImageDescriptor(ImageProvider.getImageDesc(ECoreImage.DOCUMENTATION_ICON));
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
        processWizard.init(workbench, (IStructuredSelection) this.getSelection(), getCurrentJobNode());
        Shell activeShell = Display.getCurrent().getActiveShell();
        WizardDialog dialog = new WizardDialog(activeShell, processWizard);
        dialog.open();
    }

}
