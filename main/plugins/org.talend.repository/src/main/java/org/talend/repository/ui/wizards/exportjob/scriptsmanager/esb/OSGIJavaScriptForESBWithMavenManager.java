// ============================================================================
//
// Copyright (C) 2006-2015 Talend Inc. - www.talend.com
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

import org.eclipse.core.runtime.Path;
import org.talend.commons.ui.runtime.exception.ExceptionHandler;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.IOsgiDependenciesService;
import org.talend.repository.constants.IExportJobConstants;
import org.talend.repository.preference.constants.IExportJobPrefConstants;
import org.talend.resource.IExportJobResourcesService;
import org.talend.resources.util.EMavenBuildScriptProperties;

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
    public OSGIJavaScriptForESBWithMavenManager(Map<ExportChoice, Object> exportChoiceMap, String contextName, String launcher,
            int statisticPort, int tracePort) {
        super(exportChoiceMap, contextName, launcher, statisticPort, tracePort);
    }

    @Override
    protected void addMavenBuildScripts(List<URL> scriptsUrls, Map<String, String> mavenPropertiesMap) {
        IExportJobResourcesService resourcesService = null;
        if (GlobalServiceRegister.getDefault().isServiceRegistered(IExportJobResourcesService.class)) {
            resourcesService = (IExportJobResourcesService) GlobalServiceRegister.getDefault().getService(
                    IExportJobResourcesService.class);
        }
        if (resourcesService == null) {
            return;
        }
        String mavenScript = resourcesService.getScriptFromPreferenceStore(IExportJobPrefConstants.MAVEN_OSGI_SCRIPT_TEMPLATE);
        if (mavenScript == null) {
            return;
        }

        File mavenBuildFile = new File(getTmpFolder() + PATH_SEPARATOR + IExportJobConstants.MAVEN_BUILD_FILE_NAME);
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
            updateMavenBuildFileContent(mavenBuildFile, mavenPropertiesMap, true, false);
            scriptsUrls.add(mavenBuildFile.toURL());
        } catch (Exception e) {
            ExceptionHandler.process(e);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.ui.wizards.exportjob.scriptsmanager.esb.JavaScriptForESBWithMavenManager#
     * getMenifestMavenProperties(java.net.URL, java.util.Map)
     */
    @Override
    protected void getMenifestMavenProperties(URL manifestURL, Map<String, String> mavenPropertiesMap) {
        super.getMenifestMavenProperties(manifestURL, mavenPropertiesMap);
        Set<String> theProvidedModules = getExcludedModuleNames();

        // reset the Bundle-Classpath to remove the routines jar
        String bundleClasspathName = EMavenBuildScriptProperties.BundleConfigBundleClasspath.getVarScript();
        String bundleClasspath = mavenPropertiesMap.get(bundleClasspathName);
        if (bundleClasspath == null) {
            return; //
        }
        String[] classpathes = bundleClasspath.split(IOsgiDependenciesService.ITEM_SEPARATOR);
        StringBuffer sb = new StringBuffer(200);
        for (int i = 0; i < classpathes.length; i++) {
            String path = classpathes[i];
            Path libPath = new Path(path);
            String libName = libPath.lastSegment(); // lib name with jar extension
            // because export the routine/bean resource codes to build by maven, so no need routine/ben jar.
            if (isRoutines(path) || isBeans(path) || theProvidedModules.contains(libName)) {
                continue;
            }
            sb.append(path);
            if (i < classpathes.length - 1) { // the last one don't add it
                sb.append(IOsgiDependenciesService.ITEM_SEPARATOR);
            }
        }
        mavenPropertiesMap.put(bundleClasspathName, sb.toString());
    }

    protected boolean isRoutines(String value) {
        if (value != null && (value.endsWith(SYSTEMROUTINE_JAR) || value.endsWith(USERROUTINE_JAR))) {
            return true;
        }
        return false;
    }

    protected boolean isBeans(String value) {
        if (value != null && value.endsWith(USERBEANS_JAR)) {
            return true;
        }
        return false;
    }
}
