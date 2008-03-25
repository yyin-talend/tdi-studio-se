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

import org.apache.log4j.Logger;
import org.eclipse.jface.preference.IPreferenceStore;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.utils.io.FilesUtils;
import org.talend.core.model.components.AbstractComponentsProvider;
import org.talend.designer.components.ComponentsLocalProviderPlugin;
import org.talend.designer.components.ui.ComponentsPreferencePage;

/***/
public class UserComponentsProvider extends AbstractComponentsProvider {

    private static Logger logger = Logger.getLogger(UserComponentsProvider.class);

    /***/
    public UserComponentsProvider() {
    }

    public void preComponentsLoad() throws IOException {
        File installationFolder = getInstallationFolder();

        // clean folder
        if (installationFolder.exists()) {
            FilesUtils.removeFolder(installationFolder, true);
        }
        FilesUtils.createFoldersIfNotExists(installationFolder.getAbsolutePath(), false);

        File externalComponentsLocation = getExternalComponentsLocation();
        if (externalComponentsLocation != null) {
            if (externalComponentsLocation.exists()) {
                try {
                    FilesUtils.copyFolder(externalComponentsLocation, installationFolder, false, null, null, true);
                } catch (IOException e) {
                    ExceptionHandler.process(e);
                }
            } else {
                logger.warn("Folder " + externalComponentsLocation.toString() + " does not exist.");
            }
        }
    }

    private File getExternalComponentsLocation() {
        IPreferenceStore prefStore = ComponentsLocalProviderPlugin.getDefault().getPreferenceStore();
        String path = prefStore.getString(ComponentsPreferencePage.USER_COMPONENTS_FOLDER);
        return (path == null || path.length() == 0 ? null : new File(path));
    }
}
