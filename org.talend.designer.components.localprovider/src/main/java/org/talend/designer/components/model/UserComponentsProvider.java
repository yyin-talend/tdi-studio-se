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
import java.io.FileFilter;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.preference.IPreferenceStore;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.utils.io.FilesUtils;
import org.talend.core.PluginChecker;
import org.talend.core.i18n.Messages;
import org.talend.core.model.components.AbstractComponentsProvider;
import org.talend.core.model.general.Project;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.designer.components.ComponentsLocalProviderPlugin;
import org.talend.designer.components.ui.IComponentPreferenceConstant;
import org.talend.repository.ProjectManager;

/***/
public class UserComponentsProvider extends AbstractComponentsProvider {

    private static Logger logger = Logger.getLogger(UserComponentsProvider.class);

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

    public void preComponentsLoad() throws IOException {
        File installationFolder = getInstallationFolder();
        if (installationFolder.exists()) {
            FilesUtils.removeFolder(installationFolder, true);
        }
        FilesUtils.createFoldersIfNotExists(installationFolder.getAbsolutePath(), false);
        FileFilter ff = new FileFilter() {

            public boolean accept(File pathname) {
                if (pathname.getName().equals(".svn")) {
                    return false;
                }
                return true;
            }

        };

        // synchroniz shared custom component
        if (PluginChecker.isTIS()) {
            Project currentProject = ProjectManager.getInstance().getCurrentProject();
            String projectLabel = currentProject.getTechnicalLabel();
            String sourcePath = new Path(Platform.getInstanceLocation().getURL().getPath()).toFile().getPath()
                    + File.separatorChar + projectLabel + File.separatorChar
                    + ERepositoryObjectType.getFolderName(ERepositoryObjectType.COMPONENTS);
            File source = new File(sourcePath);
            if (source.exists()) {
                for (File file : source.listFiles(ff)) {
                    FilesUtils.copyFolder(file, new File(installationFolder.getAbsolutePath() + File.separator + file.getName()),
                            true, ff, null, true, false);
                }
            }
        }

        // if components in user component path include some shared components , replace it
        File externalComponentsLocation = getExternalComponentsLocation();
        if (externalComponentsLocation != null) {
            if (externalComponentsLocation.exists()) {
                try {
                    FilesUtils.copyFolder(externalComponentsLocation, installationFolder, false, ff, null, true, false);
                } catch (IOException e) {
                    ExceptionHandler.process(e);
                }

            } else {
                logger.warn(Messages
                        .getString("AbstractComponentsProvider.folderNotExist", externalComponentsLocation.toString())); //$NON-NLS-1$
            }
        }
    }
}
