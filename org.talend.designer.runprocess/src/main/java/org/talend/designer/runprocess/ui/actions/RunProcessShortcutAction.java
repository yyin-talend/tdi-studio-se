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
package org.talend.designer.runprocess.ui.actions;

import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.debug.internal.ui.DebugUIPlugin;
import org.eclipse.debug.internal.ui.launchConfigurations.LaunchConfigurationManager;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.talend.core.CorePlugin;
import org.talend.designer.core.debug.TalendDebugUIConstants;
import org.talend.designer.core.ui.MultiPageTalendEditor;

/**
 * DOC smallet class global comment. Detailled comment <br/>
 * 
 * $Id: ShowRunProcessViewAction.java 219 2006-10-24 13:45:54 +0000 (mar., 24 oct. 2006) smallet $
 * 
 */
public class RunProcessShortcutAction extends RunProcessAction {

    public RunProcessShortcutAction() {
        super();
        this.setActionDefinitionId("showAndRunProcess"); //$NON-NLS-1$
    }

    @SuppressWarnings("restriction")
    public void run() {
        IWorkbench workbench = PlatformUI.getWorkbench();
        IWorkbenchPage page = workbench.getActiveWorkbenchWindow().getActivePage();
        IEditorPart activeEditor = page.getActiveEditor();
        if (CorePlugin.getDefault().getDesignerCoreService().isTalendEditor(activeEditor)) {
            getLaunchConfigurationManager().getLaunchShortcut(TalendDebugUIConstants.TALEND_JOB_LAUNCH_SHORTCUT_ID).launch(
                    activeEditor, ILaunchManager.RUN_MODE);
        } else {
            new RunProcessAction().run();
        }
    }

    @SuppressWarnings("restriction")
    private LaunchConfigurationManager getLaunchConfigurationManager() {
        return DebugUIPlugin.getDefault().getLaunchConfigurationManager();
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
