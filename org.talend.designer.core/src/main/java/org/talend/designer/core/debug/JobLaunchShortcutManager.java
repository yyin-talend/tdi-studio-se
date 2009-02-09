// ============================================================================
//
// Copyright (C) 2006-2009 Talend Inc. - www.talend.com
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

import java.util.List;

import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.debug.internal.ui.DebugUIPlugin;
import org.eclipse.debug.internal.ui.launchConfigurations.LaunchShortcutExtension;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IEditorPart;

/**
 * Added for compatibility with eclipse 3.4.
 */
public class JobLaunchShortcutManager {

    public static void run(ISelection selection) {
        List<LaunchShortcutExtension> launchShortcuts = DebugUIPlugin.getDefault().getLaunchConfigurationManager()
                .getLaunchShortcuts();

        for (LaunchShortcutExtension lse : launchShortcuts) {
            if (lse.getId().equals(TalendDebugUIConstants.TALEND_JOB_LAUNCH_SHORTCUT_ID)) {
                lse.launch(selection, ILaunchManager.RUN_MODE);
                return;
            }
        }
    }

    public static void run(IEditorPart editor) {
        List<LaunchShortcutExtension> launchShortcuts = DebugUIPlugin.getDefault().getLaunchConfigurationManager()
                .getLaunchShortcuts();

        for (LaunchShortcutExtension lse : launchShortcuts) {
            if (lse.getId().equals(TalendDebugUIConstants.TALEND_JOB_LAUNCH_SHORTCUT_ID)) {
                lse.launch(editor, ILaunchManager.RUN_MODE);
                return;
            }
        }
    }

    // debug not used yet, so code is disabled

    // public static void debug(ISelection selection) {
    // List<LaunchShortcutExtension> launchShortcuts = DebugUIPlugin.getDefault().getLaunchConfigurationManager()
    // .getLaunchShortcuts();
    //
    // for (LaunchShortcutExtension lse : launchShortcuts) {
    // if (lse.getId().equals(TalendDebugUIConstants.TALEND_JOB_LAUNCH_SHORTCUT_ID)) {
    // lse.launch(selection, ILaunchManager.DEBUG_MODE);
    // return;
    // }
    // }
    // }
    //
    // public static void debug(IEditorPart editor) {
    // List<LaunchShortcutExtension> launchShortcuts = DebugUIPlugin.getDefault().getLaunchConfigurationManager()
    // .getLaunchShortcuts();
    //
    // for (LaunchShortcutExtension lse : launchShortcuts) {
    // if (lse.getId().equals(TalendDebugUIConstants.TALEND_JOB_LAUNCH_SHORTCUT_ID)) {
    // lse.launch(editor, ILaunchManager.DEBUG_MODE);
    // return;
    // }
    // }
    // }
}
