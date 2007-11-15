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
package org.talend.designer.components.model;

import java.io.File;
import java.io.IOException;

import org.eclipse.jface.preference.IPreferenceStore;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.utils.io.FilesUtils;
import org.talend.designer.components.ComponentsLocalProviderPlugin;
import org.talend.designer.components.ui.ComponentsPreferencePage;

/**
 * This class aim is to retrieve components from a file system folder to the location they must be for loading.<br/>
 * 
 * $Id: talend.epf 1 2006-09-29 17:06:40 +0000 (ven., 29 sept. 2006) nrousseau $
 * 
 */
public class ComponentsRetriever {

    public static void retrieveComponents(File target) {
        File externalComponentsLocation = getExternalComponentsLocation();
        if (externalComponentsLocation != null) {
            try {
                FilesUtils.copyFolder(externalComponentsLocation, target, true, null, null, true);
            } catch (IOException e) {
                ExceptionHandler.process(e);
            }
        }
    }

    private static File getExternalComponentsLocation() {
        IPreferenceStore prefStore = ComponentsLocalProviderPlugin.getDefault().getPreferenceStore();
        String path = prefStore.getString(ComponentsPreferencePage.USER_COMPONENTS_FOLDER);
        return (path == null || path.length() == 0 ? null : new File(path));
    }
}
