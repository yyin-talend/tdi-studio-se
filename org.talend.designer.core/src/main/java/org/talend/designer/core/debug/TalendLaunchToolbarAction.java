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
package org.talend.designer.core.debug;

import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.internal.ui.DebugUIPlugin;
import org.eclipse.debug.internal.ui.contextlaunching.ContextRunner;
import org.eclipse.debug.internal.ui.contextlaunching.LaunchingResourceManager;
import org.eclipse.debug.ui.DebugUITools;
import org.eclipse.debug.ui.IDebugUIConstants;
import org.eclipse.debug.ui.actions.AbstractLaunchToolbarAction;
import org.eclipse.debug.ui.actions.LaunchAction;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.PlatformUI;
import org.talend.commons.exception.ExceptionHandler;

/**
 * DOC bqian class global comment. Detailled comment
 */
public class TalendLaunchToolbarAction extends AbstractLaunchToolbarAction {

    /**
     * DOC bqian TalendLaunchToolbarAction constructor comment.
     * 
     * @param launchGroupIdentifier
     */
    public TalendLaunchToolbarAction() {
        super(IDebugUIConstants.ID_RUN_LAUNCH_GROUP);
    }

    /**
     * Fills the drop-down menu with favorites and launch history
     * 
     * @param menu the menu to fill
     */
    protected void fillMenu(Menu menu) {
        ILaunchConfiguration[] historyList = getHistory();
        ILaunchConfiguration[] favoriteList = getFavorites();

        // Add favorites
        int accelerator = 1;
        for (int i = 0; i < favoriteList.length; i++) {
            ILaunchConfiguration launch = favoriteList[i];
            try {
                if (launch.getType().getIdentifier().equals(JobLaunchShortcut.JOB_DEBUG_LAUNCH_CONFIGURATION_TYPE)) {
                    LaunchAction action = new LaunchAction(launch, getMode());
                    addToMenu(menu, action, accelerator);
                    accelerator++;
                }
            } catch (Exception e) {
                ExceptionHandler.process(e);
                continue;
            }
        }

        // Separator between favorites and history
        if (favoriteList.length > 0 && historyList.length > 0) {
            addSeparator(menu);
        }

        // Add history launches next
        for (int i = 0; i < historyList.length; i++) {
            ILaunchConfiguration launch = historyList[i];
            try {
                if (launch.getType().getIdentifier().equals(JobLaunchShortcut.JOB_DEBUG_LAUNCH_CONFIGURATION_TYPE)) {
                    LaunchAction action = new LaunchAction(launch, getMode());
                    addToMenu(menu, action, accelerator);
                    accelerator++;
                }
            } catch (Exception e) {
                ExceptionHandler.process(e);
                continue;
            }
        }
    }

    /**
     * Launch the last launch, or open the launch config dialog if none.
     * 
     * @see org.eclipse.ui.IActionDelegate#run(org.eclipse.jface.action.IAction)
     */
    public void run(IAction action) {
        // always ignore external tools during context launching
        // if (LaunchingResourceManager.isContextLaunchEnabled()
        // && !getLaunchGroupIdentifier().equals("org.eclipse.ui.externaltools.launchGroup")) { //$NON-NLS-1$
        // ContextRunner.getDefault().launch(
        // DebugUIPlugin.getDefault().getLaunchConfigurationManager().getLaunchGroup(getLaunchGroupIdentifier()));
        // } else {
        ILaunchConfiguration configuration = getLastLaunch();
        if (configuration == null) {
            MessageDialog.openInformation(DebugUIPlugin.getShell(), "Infomation", "There is no running item available.");
            // DebugUITools.openLaunchConfigurationDialogOnGroup(DebugUIPlugin.getShell(), new StructuredSelection(),
            // getLaunchGroupIdentifier());
        } else {
            DebugUITools.launch(configuration, getMode());
        }
    }
    // }
}
