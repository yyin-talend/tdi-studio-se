// ============================================================================
//
// Copyright (C) 2006-2014 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.repository.utils;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.MessageDialogWithToggle;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.widgets.Shell;
import org.talend.core.ui.CoreUIPlugin;
import org.talend.repository.i18n.Messages;

/**
 * DOC hcyi class global comment. Detailled comment
 */
public class StudioConfigUtils {

    /**
     * To show that the studio does not fully support java 8 yet
     */
    public static final boolean checkUnSupportJavaVersion(Shell shell) {
        String javaVersion = System.getProperty("java.version");
        if (javaVersion != null) {
            org.talend.commons.utils.Version v = new org.talend.commons.utils.Version(javaVersion);
            if (v.getMajor() == 1 && v.getMinor() > 7) { // more than JDK 1.7
                IPreferenceStore preferenceStore = CoreUIPlugin.getDefault().getPreferenceStore();
                String preKey = "StudioConfigUtils.doNotSupportJavaVersionYet"; //$NON-NLS-1$
                if (!preferenceStore.getBoolean(preKey)) {
                    MessageDialogWithToggle dialog = new MessageDialogWithToggle(
                            shell,
                            "", shell.getBackgroundImage(), //$NON-NLS-1$
                            Messages.getString("StudioConfigUtils.doNotSupportJavaVersionYet"),
                            MessageDialog.WARNING,
                            new String[] { IDialogConstants.CANCEL_LABEL, "Continue" }, 0, Messages.getString("StudioConfigUtils.doNotShowMessage"), //$NON-NLS-1$
                            false);
                    dialog.open();
                    if (dialog.getReturnCode() == IDialogConstants.CANCEL_ID) {
                        return false;
                    }
                    dialog.setPrefKey(preKey); //$NON-NLS-1$
                    dialog.setPrefStore(preferenceStore);
                    preferenceStore.setValue(preKey, dialog.getToggleState());
                }
            }
        }
        return true;
    }
}
