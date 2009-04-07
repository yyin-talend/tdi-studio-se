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
package org.talend.designer.components;

import java.io.File;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.talend.commons.exception.BusinessException;
import org.talend.core.language.ECodeLanguage;
import org.talend.designer.components.model.ComponentFileChecker;
import org.talend.designer.components.ui.ComponenttRunJobPreferencePage;

/**
 * DOC Administrator class global comment. Detailled comment <br/>
 * 
 * @author ftang, 17/08, 2007
 * 
 */
public class ComponentsLocalProviderService implements IComponentsLocalProviderService {

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.components.IComponentsLocalProviderService#isAvoidToShowJobAfterDoubleClick()
     */
    public boolean isAvoidToShowJobAfterDoubleClick() {
        return ComponentsLocalProviderPlugin.getDefault().getPreferenceStore()
                .getBoolean(ComponenttRunJobPreferencePage.IS_AVOID);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.components.IComponentsLocalProviderService#getPreferenceStore()
     */
    public IPreferenceStore getPreferenceStore() {
        return ComponentsLocalProviderPlugin.getDefault().getPreferenceStore();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.components.IComponentsLocalProviderService#getPlugin()
     */
    public AbstractUIPlugin getPlugin() {
        return ComponentsLocalProviderPlugin.getDefault();
    }

    public boolean validateComponent(String componentFolder, ECodeLanguage language) {
        if (componentFolder != null && language != null) {
            File folder = new File(componentFolder);
            if (folder.exists() && folder.isDirectory()) {
                try {
                    ComponentFileChecker.checkComponentFolder(folder, language.getName().toLowerCase());
                    return true; // It's ok
                } catch (BusinessException e) {
                    final BusinessException tempE = e;
                    Display.getDefault().syncExec(new Runnable() {

                        public void run() {
                            Status status = new Status(IStatus.ERROR, ComponentsLocalProviderPlugin.PLUGIN_ID, 1, tempE
                                    .getMessage(), tempE.getCause());
                            ErrorDialog dlg = new ErrorDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
                                    "Talend Exchange Error", "Component load error", status, IStatus.ERROR);
                            dlg.open();
                        }

                    });

                }
            }
        }
        return false;
    }
}
