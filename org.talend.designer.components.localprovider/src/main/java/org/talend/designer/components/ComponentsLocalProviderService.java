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
package org.talend.designer.components;

import org.eclipse.jface.preference.IPreferenceStore;
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

}
