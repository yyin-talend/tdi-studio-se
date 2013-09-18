// ============================================================================
//
// Copyright (C) 2006-2013 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.designer.core.utils;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.widgets.Shell;
import org.talend.core.model.components.IComponent;
import org.talend.core.model.general.ModuleNeeded;
import org.talend.designer.core.i18n.Messages;
import org.talend.librariesmanager.ui.LibManagerUiPlugin;
import org.talend.librariesmanager.ui.dialogs.ComponentExternalModulesDialog;
import org.talend.librariesmanager.ui.dialogs.ExternalModulesInstallDialog;

/**
 * DOC ggu class global comment. Detailled comment
 */
public class ModulesInstallerUtil {

    public static void installModules(Shell shell, IComponent component) {
        if (!LibManagerUiPlugin.getDefault().getPreferenceStore()
                .getBoolean(ExternalModulesInstallDialog.DO_NOT_SHOW_EXTERNALMODULESINSTALLDIALOG)) {
            forceInstallModules(shell, component, component.getModulesNeeded());
        }
    }

    public static void forceInstallModules(Shell shell, IComponent component, List<ModuleNeeded> modules) {
        String text = Messages.getString("ModulesInstaller_text1", component.getName());//$NON-NLS-1$
        String title = Messages.getString("ModulesInstaller_title1") + component.getName(); //$NON-NLS-1$
        if (!modules.isEmpty()) {
            ComponentExternalModulesDialog dialog = new ComponentExternalModulesDialog(shell, modules, text, title);
            dialog.openDialog();
        }
    }

    public static void installModules(Shell shell, List<IComponent> components) {
        if (!LibManagerUiPlugin.getDefault().getPreferenceStore()
                .getBoolean(ExternalModulesInstallDialog.DO_NOT_SHOW_EXTERNALMODULESINSTALLDIALOG)) {
            String text = Messages.getString("ModulesInstaller_text2"); //$NON-NLS-1$
            String title = Messages.getString("ModulesInstaller_title2"); //$NON-NLS-1$
            List<ModuleNeeded> needed = new ArrayList<ModuleNeeded>();
            for (IComponent component : components) {
                needed.addAll(component.getModulesNeeded());
            }
            if (!needed.isEmpty()) {
                ComponentExternalModulesDialog dialog = new ComponentExternalModulesDialog(shell, needed, text, title);
                dialog.openDialog();
            }
        }
    }

}
