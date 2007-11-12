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
package org.talend.repository.ui.actions.importproject;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.talend.repository.i18n.Messages;
import org.talend.repository.ui.wizards.newproject.copyfromeclipse.TalendZipFileExportWizard;

/**
 * Action used to refresh a repository view.<br/>
 * 
 * $Id: RefreshAction.java 824 2006-12-01 15:49:55 +0000 (ven., 01 déc. 2006) smallet $
 * 
 */
public class ExportProjectsAsAction extends Action implements IWorkbenchWindowActionDelegate {

    public ExportProjectsAsAction() {
        super();
    }

    public void run() {
        Shell activeShell = Display.getCurrent().getActiveShell();
        TalendZipFileExportWizard docWizard = new TalendZipFileExportWizard();
        WizardDialog dialog = new WizardDialog(activeShell, docWizard);
        docWizard.setWindowTitle(Messages.getString("ExportProjectsAsAction.actionTitle"));
        dialog.create();
        dialog.open();
    }

    public void dispose() {
    }

    public void init(IWorkbenchWindow window) {
    }

    public void run(IAction action) {
        run();
    }

    public void selectionChanged(IAction action, ISelection selection) {
    }
}
