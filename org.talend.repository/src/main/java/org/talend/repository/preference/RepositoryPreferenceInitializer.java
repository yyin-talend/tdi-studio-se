// ============================================================================
//
// Copyright (C) 2006-2012 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.repository.preference;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;
import org.talend.commons.ui.runtime.exception.ExceptionHandler;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.model.repository.IRepositoryPrefConstants;
import org.talend.repository.RepositoryPlugin;
import org.talend.repository.constants.ExportJobConstants;
import org.talend.resource.IResourceService;

/**
 * ggu class global comment. Detailled comment
 */
public class RepositoryPreferenceInitializer extends AbstractPreferenceInitializer {

    private static final String EMPTY_STR = ""; //$NON-NLS-1$

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer#initializeDefaultPreferences()
     */
    @Override
    public void initializeDefaultPreferences() {
        final IPreferenceStore preferenceStore = RepositoryPlugin.getDefault().getPreferenceStore();
        preferenceStore.setDefault(IRepositoryPrefConstants.MERGE_REFERENCE_PROJECT, true);

        preferenceStore.setDefault(IRepositoryPrefConstants.ANT_SCRIPT_TEMPLATE, getAntScriptXmlString());
        preferenceStore.setDefault(IRepositoryPrefConstants.MAVEN_SCRIPT_AUTONOMOUSJOB_TEMPLATE, getMavenScriptXmlString("job/" //$NON-NLS-1$
                + ExportJobConstants.MAVEN_BUILD_FILE_NAME));
        preferenceStore.setDefault(IRepositoryPrefConstants.MAVEN_SCRIPT_AUTONOMOUSJOB_ASSEMBLY_TEMPLATE,
                getMavenScriptXmlString("job/" + ExportJobConstants.MAVEN_ASSEMBLY_FILE_NAME)); //$NON-NLS-1$

        preferenceStore.setDefault(IRepositoryPrefConstants.MAVEN_OSGI_SCRIPT_TEMPLATE, getMavenScriptXmlString("osgi/" //$NON-NLS-1$
                + ExportJobConstants.MAVEN_BUILD_FILE_NAME));

    }

    private String getAntScriptXmlString() {
        IResourceService resourceService = (IResourceService) GlobalServiceRegister.getDefault().getService(
                IResourceService.class);
        if (resourceService == null) {
            return EMPTY_STR;
        }
        File templateScriptFile = new File(resourceService.getAntScriptFilePath());

        return getScriptXmlString(templateScriptFile);
    }

    private String getMavenScriptXmlString(String pathWithPom) {
        IResourceService resourceService = (IResourceService) GlobalServiceRegister.getDefault().getService(
                IResourceService.class);
        if (resourceService == null) {
            return EMPTY_STR;
        }
        File templateScriptFile = new File(resourceService.getMavenScriptFilePath(pathWithPom));

        return getScriptXmlString(templateScriptFile);
    }

    private String getScriptXmlString(File templateScriptFile) {
        if (templateScriptFile != null && templateScriptFile.exists()) {
            try {
                return new Scanner(templateScriptFile).useDelimiter("\\A").next(); //$NON-NLS-1$
            } catch (FileNotFoundException e) {
                ExceptionHandler.process(e);
            }
        }
        return ""; //$NON-NLS-1$
    }
}
