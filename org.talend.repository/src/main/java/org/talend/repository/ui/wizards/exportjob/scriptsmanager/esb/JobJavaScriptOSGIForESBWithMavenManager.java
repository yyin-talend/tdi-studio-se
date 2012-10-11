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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.jar.Manifest;

import org.talend.commons.ui.runtime.exception.ExceptionHandler;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.model.general.ModuleNeeded;
import org.talend.core.model.process.IProcess;
import org.talend.core.model.properties.ProcessItem;
import org.talend.core.model.repository.IRepositoryPrefConstants;
import org.talend.core.model.repository.RepositoryManager;
import org.talend.designer.runprocess.LastGenerationInfo;
import org.talend.designer.runprocess.ProcessorException;
import org.talend.repository.constants.ExportJobConstants;
import org.talend.repository.documentation.ExportFileResource;
import org.talend.resource.IResourceService;
import org.talend.resources.util.EMavenBuildScriptProperties;

/**
 * created by nrousseau on Sep 25, 2012 Detailled comment
 * 
 */
public class JobJavaScriptOSGIForESBWithMavenManager extends JobJavaScriptOSGIForESBManager {

    /**
     * DOC nrousseau JobJavaScriptOSGIForESBWithMavenManager constructor comment.
     * 
     * @param exportChoiceMap
     * @param contextName
     * @param launcher
     * @param statisticPort
     * @param tracePort
     */
    public JobJavaScriptOSGIForESBWithMavenManager(Map<ExportChoice, Object> exportChoiceMap, String contextName,
            String launcher, int statisticPort, int tracePort) {
        super(exportChoiceMap, contextName, launcher, statisticPort, tracePort);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.repository.ui.wizards.exportjob.scriptsmanager.esb.JobJavaScriptOSGIForESBManager#getExportResources
     * (org.talend.repository.documentation.ExportFileResource[], java.lang.String[])
     */
    @Override
    public List<ExportFileResource> getExportResources(ExportFileResource[] processes, String... codeOptions)
            throws ProcessorException {
        List<ExportFileResource> list = new ArrayList<ExportFileResource>();
        list = super.getExportResources(processes, codeOptions);

        String privatePackage = "", exportService = ""; //$NON-NLS-1$ //$NON-NLS-2$

        Iterator<ExportFileResource> it = list.iterator();
        while (it.hasNext()) {
            ExportFileResource resource = it.next();
            if ("".equals(resource.getDirectoryName())) { //$NON-NLS-1$
                Map<String, Set<URL>> newResourcesMap = new HashMap<String, Set<URL>>();
                for (String path : resource.getRelativePathList()) {
                    if (path.startsWith("OSGI")) { //$NON-NLS-1$
                        Set<URL> urls = resource.getResourcesByRelativePath(path);
                        // put OSGI_INF to /src/main/resources/
                        newResourcesMap.put("src/main/resources/" + path, urls); //$NON-NLS-1$
                    }
                }
                resource.removeAllMap();
                for (String path : newResourcesMap.keySet()) {
                    resource.addResources(path, new ArrayList<URL>(newResourcesMap.get(path)));
                }
            } else if (LIBRARY_FOLDER_NAME.equals(resource.getDirectoryName())) {
                resource.setDirectoryName("src/main/resources/lib/"); //$NON-NLS-1$
            } else if (META_INF.equals(resource.getDirectoryName())) {
                it.remove();
                if (!resource.getAllResources().isEmpty()) {
                    Set<URL> urls = resource.getAllResources().iterator().next();
                    if (!urls.isEmpty()) {
                        URL manifestURL = urls.iterator().next();
                        try {
                            File manifestFile = new File(manifestURL.toURI());
                            FileInputStream fis = new FileInputStream(manifestFile);
                            Manifest manifest = new Manifest(fis);
                            exportService = manifest.getMainAttributes().getValue("Export-Service"); //$NON-NLS-1$
                            privatePackage = manifest.getMainAttributes().getValue("Private-Package"); //$NON-NLS-1$
                            fis.close();
                        } catch (URISyntaxException e) {
                            ExceptionHandler.process(e);
                        } catch (FileNotFoundException e) {
                            ExceptionHandler.process(e);
                        } catch (IOException e) {
                            ExceptionHandler.process(e);
                        }
                    }
                }
            }
        }

        addSourceCodeToExport(list, processes, codeOptions);

        addMavenScriptToExport(list, processes, privatePackage, exportService);

        return list;
    }

    /**
     * DOC nrousseau Comment method "addMavenScriptToExport".
     * 
     * @param list
     * @param processes
     * @param codeOptions
     */
    private void addMavenScriptToExport(List<ExportFileResource> list, ExportFileResource[] processes, String privatePackage,
            String exportService) {
        List<URL> scriptsUrls = new ArrayList<URL>();
        addMavenBuildScripts(scriptsUrls, (ProcessItem) processes[0].getItem(),
                processes[0].getItem().getProperty().getVersion(), privatePackage, exportService);
        ExportFileResource mavenResource = new ExportFileResource(null, ""); //$NON-NLS-1$
        mavenResource.addResources("", scriptsUrls); //$NON-NLS-1$
        list.add(mavenResource);
    }

    /**
     * DOC nrousseau Comment method "addSourceCodeToExport".
     * 
     * @param list
     * @param processes
     */
    private void addSourceCodeToExport(List<ExportFileResource> list, ExportFileResource[] processes, String... codeOptions) {
        List<URL> noUseJustForSimplifyChange = new ArrayList<URL>();
        exportChoice.put(ExportChoice.needSourceCode, Boolean.TRUE);
        exportChoice.put(ExportChoice.needContext, Boolean.TRUE);
        exportChoice.put(ExportChoice.needMavenScript, Boolean.FALSE);
        processes[0].removeAllMap();
        posExportResource(processes, exportChoice, contextName, launcher, statisticPort, tracePort, 0, (IProcess) null,
                (ProcessItem) processes[0].getItem(), processes[0].getItem().getProperty().getVersion(),
                noUseJustForSimplifyChange, codeOptions);
        Map<String, Set<URL>> newResourcesMap = new HashMap<String, Set<URL>>();
        for (String path : processes[0].getRelativePathList()) {
            Set<URL> urls = processes[0].getResourcesByRelativePath(path);
            // put OSGI_INF to /src/main/resources/
            if (path.startsWith("src/")) { //$NON-NLS-1$
                newResourcesMap.put(path.replace("src/", "src/main/java/"), urls); //$NON-NLS-1$//$NON-NLS-2$
            } else {
                newResourcesMap.put("src/main/resources/" + path, urls); //$NON-NLS-1$
            }
        }
        ExportFileResource sourceCodeResource = new ExportFileResource(null, ""); //$NON-NLS-1$
        for (String path : newResourcesMap.keySet()) {
            sourceCodeResource.addResources(path, new ArrayList<URL>(newResourcesMap.get(path)));
        }
        list.add(sourceCodeResource);
    }

    @SuppressWarnings("rawtypes")
    private void addMavenBuildScripts(List<URL> scriptsUrls, ProcessItem processItem, String selectedJobVersion,
            String privatePackage, String exportService) {
        String mavenScript = RepositoryManager.getPreferenceStore()
                .getString(IRepositoryPrefConstants.MAVEN_OSGI_SCRIPT_TEMPLATE);
        if (mavenScript == null) {
            return;
        }
        IResourceService resourceService = (IResourceService) GlobalServiceRegister.getDefault().getService(
                IResourceService.class);
        if (resourceService == null) {
            return;
        }

        String projectName = getCorrespondingProjectName(processItem);
        String jobName = processItem.getProperty().getLabel();
        String jobVersion = processItem.getProperty().getVersion();

        // set the maven properties
        final Map<String, String> mavenPropertiesMap = new HashMap<String, String>();
        mavenPropertiesMap.put(EMavenBuildScriptProperties.ItemProjectName.getVarScript(), projectName);
        mavenPropertiesMap.put(EMavenBuildScriptProperties.ItemName.getVarScript(), jobName);
        mavenPropertiesMap.put(EMavenBuildScriptProperties.ItemVersion.getVarScript(), jobVersion);
        mavenPropertiesMap.put(EMavenBuildScriptProperties.BundleConfigPrivatePackage.getVarScript(), privatePackage);
        mavenPropertiesMap.put(EMavenBuildScriptProperties.BundleConfigExportService.getVarScript(), exportService);

        Set<ModuleNeeded> neededModules = LastGenerationInfo.getInstance().getModulesNeededWithSubjobPerJob(
                processItem.getProperty().getId(), selectedJobVersion);

        File mavenBuildFile = new File(getTmpFolder() + PATH_SEPARATOR + ExportJobConstants.MAVEN_BUILD_FILE_NAME);
        File mavenAssemblyFile = new File(getTmpFolder() + PATH_SEPARATOR + ExportJobConstants.MAVEN_ASSEMBLY_FILE_NAME);
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
            updateMavenBuildFileContent(mavenBuildFile, mavenPropertiesMap, neededModules, "${lib.path}/");
            scriptsUrls.add(mavenBuildFile.toURL());
            scriptsUrls.add(mavenAssemblyFile.toURL());
        } catch (Exception e) {
            ExceptionHandler.process(e);
        }
    }
}
