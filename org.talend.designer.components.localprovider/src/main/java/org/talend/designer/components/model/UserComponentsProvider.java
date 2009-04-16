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
}
