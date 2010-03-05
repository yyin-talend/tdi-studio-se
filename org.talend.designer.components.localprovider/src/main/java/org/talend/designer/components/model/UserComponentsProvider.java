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
package org.talend.designer.components.model;

import java.io.File;

import org.eclipse.jface.preference.IPreferenceStore;
import org.talend.core.model.components.AbstractComponentsProvider;
import org.talend.designer.components.ComponentsLocalProviderPlugin;
import org.talend.designer.components.ui.IComponentPreferenceConstant;

/***/
public class UserComponentsProvider extends AbstractComponentsProvider {

    /***/
    public UserComponentsProvider() {
    }

    protected File getExternalComponentsLocation() {
        IPreferenceStore prefStore = ComponentsLocalProviderPlugin.getDefault().getPreferenceStore();
        String path = prefStore.getString(IComponentPreferenceConstant.USER_COMPONENTS_FOLDER);
        return (path == null || path.length() == 0 ? null : new File(path));
    }

    public void setUserComponentPath(String path) {
        if (path == null) {
            path = ""; //$NON-NLS-1$
        }
        IPreferenceStore prefStore = ComponentsLocalProviderPlugin.getDefault().getPreferenceStore();
        File userPath = new File(path.trim());
        if (userPath.exists()) {
            prefStore.setValue(IComponentPreferenceConstant.USER_COMPONENTS_FOLDER, path);
        } else {
            prefStore.setValue(IComponentPreferenceConstant.USER_COMPONENTS_FOLDER, ""); //$NON-NLS-1$
        }
    }

    public String getUserComponentPath() {
        File path = getExternalComponentsLocation();
        if (path == null) {
            return ""; //$NON-NLS-1$
        }
        return path.toString();
    }
}
