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
package org.talend.repository.ui.wizards.exportjob.scriptsmanager.esb;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.talend.commons.ui.runtime.exception.ExceptionHandler;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.model.general.ModuleNeeded;
import org.talend.core.model.properties.ProcessItem;
import org.talend.core.model.repository.IRepositoryPrefConstants;
import org.talend.designer.runprocess.LastGenerationInfo;
import org.talend.repository.RepositoryPlugin;
import org.talend.repository.constants.ExportJobConstants;
import org.talend.resource.IResourceService;

/**
 * created by nrousseau on Sep 25, 2012 Detailled comment
 * 
 */
public class OSGIJavaScriptForESBWithMavenManager extends JavaScriptForESBWithMavenManager {

    /**
     * DOC nrousseau JobJavaScriptOSGIForESBWithMavenManager constructor comment.
     * 
     * @param exportChoiceMap
     * @param contextName
     * @param launcher
     * @param statisticPort
     * @param tracePort
     */
    public OSGIJavaScriptForESBWithMavenManager(Map<ExportChoice, Object> exportChoiceMap, String contextName,
            String launcher, int statisticPort, int tracePort) {
        super(exportChoiceMap, contextName, launcher, statisticPort, tracePort);
    }

    @Override
    protected void addMavenBuildScripts(List<URL> scriptsUrls, ProcessItem processItem, String selectedJobVersion,
            Map<String, String> mavenPropertiesMap) {
        String mavenScript = RepositoryPlugin.getDefault().getPreferenceStore()
                .getString(IRepositoryPrefConstants.MAVEN_OSGI_SCRIPT_TEMPLATE);
        if (mavenScript == null) {
            return;
        }
        IResourceService resourceService = (IResourceService) GlobalServiceRegister.getDefault().getService(
                IResourceService.class);
        if (resourceService == null) {
            return;
        }
        Set<ModuleNeeded> neededModules = LastGenerationInfo.getInstance().getModulesNeededWithSubjobPerJob(
                processItem.getProperty().getId(), selectedJobVersion);

        File mavenBuildFile = new File(getTmpFolder() + PATH_SEPARATOR + ExportJobConstants.MAVEN_BUILD_FILE_NAME);
        try {
            FileOutputStream mavenBuildFileOutputStream = null;
            try {
                mavenBuildFileOutputStream = new FileOutputStream(mavenBuildFile);
                mavenBuildFileOutputStream.write(mavenScript.getBytes());
            } finally {
                if (mavenBuildFileOutputStream != null) {
                    mavenBuildFileOutputStream.close();
                }
            }
            updateMavenBuildFileContent(mavenBuildFile, mavenPropertiesMap, neededModules, MAVEN_PROP_LIB_PATH);
            scriptsUrls.add(mavenBuildFile.toURL());
        } catch (Exception e) {
            ExceptionHandler.process(e);
        }
    }
}
