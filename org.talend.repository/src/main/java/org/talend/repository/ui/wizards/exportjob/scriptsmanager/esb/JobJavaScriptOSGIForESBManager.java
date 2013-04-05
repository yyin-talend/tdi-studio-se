// ============================================================================
//
// Copyright (C) 2006-2013 Talend Inc. - www.talend.com
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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.jar.Manifest;

import org.apache.commons.collections.map.MultiKeyMap;
import org.apache.commons.lang.StringUtils;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.EMap;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.osgi.framework.Bundle;
import org.talend.commons.exception.PersistenceException;
import org.talend.commons.ui.runtime.exception.ExceptionHandler;
import org.talend.commons.utils.generation.JavaUtils;
import org.talend.commons.utils.io.FilesUtils;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.IOsgiDependenciesService;
import org.talend.core.model.general.ModuleNeeded;
import org.talend.core.model.general.Project;
import org.talend.core.model.process.ElementParameterParser;
import org.talend.core.model.process.IProcess;
import org.talend.core.model.properties.Item;
import org.talend.core.model.properties.ProcessItem;
import org.talend.core.model.properties.Property;
import org.talend.core.model.properties.RoutineItem;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.core.model.utils.JavaResourcesHelper;
import org.talend.core.repository.constants.FileConstants;
import org.talend.core.repository.model.ProxyRepositoryFactory;
import org.talend.core.ui.branding.IBrandingService;
import org.talend.designer.core.ICamelDesignerCoreService;
import org.talend.designer.core.IDesignerCoreService;
import org.talend.designer.core.model.utils.emf.component.IMPORTType;
import org.talend.designer.core.model.utils.emf.talendfile.ConnectionType;
import org.talend.designer.core.model.utils.emf.talendfile.ElementParameterType;
import org.talend.designer.core.model.utils.emf.talendfile.ElementValueType;
import org.talend.designer.core.model.utils.emf.talendfile.NodeType;
import org.talend.designer.runprocess.IProcessor;
import org.talend.designer.runprocess.ItemCacheManager;
import org.talend.designer.runprocess.LastGenerationInfo;
import org.talend.designer.runprocess.ProcessorException;
import org.talend.designer.runprocess.ProcessorUtilities;
import org.talend.repository.ProjectManager;
import org.talend.repository.RepositoryPlugin;
import org.talend.repository.documentation.ExportFileResource;
import org.talend.repository.ui.wizards.exportjob.scriptsmanager.JobJavaScriptsManager;
import org.talend.repository.utils.EmfModelUtils;
import org.talend.repository.utils.TemplateProcessor;

import aQute.bnd.osgi.Analyzer;
import aQute.bnd.osgi.FileResource;
import aQute.bnd.osgi.Jar;

/**
 * DOC ycbai class global comment. Detailled comment
 */
public class JobJavaScriptOSGIForESBManager extends JobJavaScriptsManager {

    public JobJavaScriptOSGIForESBManager(Map<ExportChoice, Object> exportChoiceMap, String contextName, String launcher,
            int statisticPort, int tracePort) {
        super(exportChoiceMap, contextName, launcher, statisticPort, tracePort);
    }

    private static final String PACKAGE_SEPARATOR = "."; //$NON-NLS-1$

    private static final String JAVA = "java"; //$NON-NLS-1$

    private static final String ROUTE = "route"; //$NON-NLS-1$

    private static final String JOB = "job"; //$NON-NLS-1$

    private static final String OSGI_INF = "OSGI-INF"; //$NON-NLS-1$

    private static final String BLUEPRINT = "blueprint"; //$NON-NLS-1$

    private static final String SPRING = "spring"; //$NON-NLS-1$

    private MultiKeyMap requireBundleModules = new MultiKeyMap();

    private String jobName;

    private String jobClassName;

    private String itemType = null;

    private final File classesLocation = new File(getTmpFolder() + File.separator + "classes"); //$NON-NLS-1$;

    @Override
    public List<ExportFileResource> getExportResources(ExportFileResource[] processes, String... codeOptions)
            throws ProcessorException {
        List<ExportFileResource> list = new ArrayList<ExportFileResource>();

        ExportFileResource osgiResource = new ExportFileResource(null, ""); //$NON-NLS-1$;
        ExportFileResource jobScriptResource = new ExportFileResource(null, ""); //$NON-NLS-1$

        List<ProcessItem> itemToBeExport = new ArrayList<ProcessItem>();

        list.add(osgiResource);
        list.add(jobScriptResource);

        // set export config mode now only to be sure that the libraries will be
        // setup for an export mode, and not
        // editor mode.
        ProcessorUtilities.setExportConfig(JAVA, "", ""); //$NON-NLS-1$

        try {
            for (ExportFileResource process : processes) {
                ProcessItem processItem = (ProcessItem) process.getItem();
                if (processItem.eIsProxy() || processItem.getProcess().eIsProxy()) {
                    try {
                        Property property = ProxyRepositoryFactory.getInstance().getUptodateProperty(processItem.getProperty());
                        processItem = (ProcessItem) property.getItem();
                    } catch (PersistenceException e) {
                        throw new ProcessorException(e);
                    }
                }
                itemToBeExport.add(processItem);
                jobName = processItem.getProperty().getLabel();
                jobClassName = getPackageName(processItem) + PACKAGE_SEPARATOR + jobName;

                String jobVersion = processItem.getProperty().getVersion();
                if (!isMultiNodes() && getSelectedJobVersion() != null) {
                    jobVersion = getSelectedJobVersion();
                }
                ERepositoryObjectType type = ERepositoryObjectType.getItemType(processItem);
                if (type.equals(ERepositoryObjectType.PROCESS)) {
                    itemType = JOB;
                } else {
                    itemType = ROUTE;
                }

                // generate the source files
                String libPath = calculateLibraryPathFromDirectory(process.getDirectoryName());
                // use character @ as temporary classpath separator, this one will
                // be replaced during the export.
                String standardJars = libPath + PATH_SEPARATOR + SYSTEMROUTINE_JAR
                        + ProcessorUtilities.TEMP_JAVA_CLASSPATH_SEPARATOR + libPath + PATH_SEPARATOR + USERROUTINE_JAR
                        + ProcessorUtilities.TEMP_JAVA_CLASSPATH_SEPARATOR + PACKAGE_SEPARATOR;

                ProcessorUtilities.setExportConfig(JAVA, standardJars, libPath);

                String processId = processItem.getProperty().getId();
                if (!isOptionChoosed(ExportChoice.doNotCompileCode)) {
                    generateJobFiles(processItem, contextName, jobVersion, statisticPort != IProcessor.NO_STATISTICS,
                            tracePort != IProcessor.NO_TRACES, isOptionChoosed(ExportChoice.applyToChildren),
                            true /* isExportAsOSGI */, progressMonitor);
                    analysisModules(processId, jobVersion);
                } else {
                    LastGenerationInfo.getInstance().setModulesNeededWithSubjobPerJob(processId, jobVersion,
                            Collections.<ModuleNeeded> emptySet());
                    LastGenerationInfo.getInstance().setLastMainJob(null);
                }

                analysisMavenModule(processItem);

                // generate jar file for job
                getJobScriptsUncompressed(jobScriptResource, processItem);

                // dynamic db xml mapping
                addXmlMapping(process, isOptionChoosed(ExportChoice.needSourceCode));

                // restJob
                if (JOB.equals(itemType) && (null != getRESTRequestComponent(processItem))) {
                    osgiResource.addResources(getMetaInfSpringFolder(),
                            Collections.singletonList(generateRestJobSpringConfig(processItem)));
                } else {
                    osgiResource
                            .addResources(getOSGIInfFolder(), Collections.singletonList(generateBlueprintConfig(processItem)));
                }

                // Add Route Resource http://jira.talendforge.org/browse/TESB-6227
                if (ROUTE.equals(itemType)) {
                    addOSGIRouteResources(osgiResource, processItem);
                }
            }

            ExportFileResource libResource = getCompiledLibExportFileResource(processes);
            list.add(libResource);


            ExportFileResource providedLibResources = getProvidedLibExportFileResource(processes);
            if (providedLibResources != null) {
                list.add(providedLibResources);
            }

            // generate the META-INFO folder
            ExportFileResource metaInfoFolder = genMetaInfoFolder(libResource, itemToBeExport);
            list.add(0, metaInfoFolder);
        } catch (Exception e) {
            throw new ProcessorException(e);
        }

        return list;
    }

    @Override
    protected void analysisModules(String processId, String processVersion) {
        Set<ModuleNeeded> neededModules = LastGenerationInfo.getInstance().getModulesNeededWithSubjobPerJob(processId,
                processVersion);
        for (ModuleNeeded module : neededModules) {
            if (isCompiledLib(module)) {
                addModuleNeededsInMap(getCompiledModules(), processId, processVersion, module);
            } else if (isRequireBundleLib(module)) {
                addModuleNeededsInMap(getRequireBundleModules(), processId, processVersion, module);
            } else {
                addModuleNeededsInMap(getExcludedModules(), processId, processVersion, module);
            }
        }
    }

    @Override
    protected ExportFileResource getCompiledLibExportFileResource(ExportFileResource[] processes) {
        ExportFileResource libResource = new ExportFileResource(null, LIBRARY_FOLDER_NAME);
        // Gets talend libraries
        List<URL> talendLibraries = getExternalLibraries(true, processes, getCompiledModuleNames());
        if (talendLibraries != null) {
            libResource.addResources(talendLibraries);
        }
        addRoutinesResources(processes, libResource);
        return libResource;
    }

    @Override
    protected void addRoutinesResources(ExportFileResource[] processes, ExportFileResource libResource) {
        // Gets system routines
        List<URL> systemRoutineList = getSystemRoutine(processes);
        libResource.addResources(systemRoutineList);
        // Gets user routines
        List<URL> userRoutineList = getUserRoutine(processes);
        libResource.addResources(userRoutineList);
        // pigudf
        // libResource.addResource(getPigudf(processes))
    }

    protected ExportFileResource getProvidedLibExportFileResource(ExportFileResource[] processes) {
        return null; // default, no provided lib for osgi bundle
    }

    /**
     *
     * This should be same as @see isIncludedLib. But, there are some special jar to exclude temp.
     */
    @Override
    protected boolean isCompiledLib(ModuleNeeded module) {
        if (module != null) {
            /*
             * If null, will add the lib always.
             *
             * If empty, nothing will be added.
             *
             * Else, add the bundle id in "Require-Bundle", but don't add the lib.
             */
            if (isIncludedLib(module)) {
                return true;
            }
        }
        return false;
    }

    protected boolean isSpecialLib(String libName) {
        if (libName != null) {
            // temp workaround for https://jira.talendforge.org/browse/TDI-22934
            if (libName.startsWith("camel-core-") //$NON-NLS-1$
                    || libName.startsWith("dom4j-") //$NON-NLS-1$
                    // temp workaround for https://jira.talendforge.org/browse/TESB-7271
                    || libName.startsWith("ojdbc")) { //$NON-NLS-1$
                return true;
            }
        }
        return false;
    }

    /**
     * If null, will add the lib always. @see isIncludedLib
     *
     * If empty, nothing will be added. @see isExcludedLib
     *
     * Else, add the bundle id in "Require-Bundle", but don't add the lib. @see isIncludedInRequireBundle
     */
    protected boolean isIncludedLib(ModuleNeeded module) {
        if (module != null) {
            if (module.getBundleName() == null) {
                return true;
            }
        }
        return false;
    }

    protected boolean isProvidedLib(ModuleNeeded module) {
        return isRequireBundleLib(module) || isExcludedLib(module);
    }

    /**
     * nothing will be added.
     */
    @Override
    protected boolean isExcludedLib(ModuleNeeded module) {
        if (module != null) {
            if (module.getBundleName() != null && "".equals(module.getBundleName().trim())) { //$NON-NLS-1$
                return true;
            }
        }
        return false;
    }

    /**
     * check for the lib for "Require-Bundle"
     */
    protected boolean isRequireBundleLib(ModuleNeeded module) {
        if (module != null) {
            if (module.getBundleName() != null && !"".equals(module.getBundleName().trim())) { //$NON-NLS-1$
                return true;
            }
        }
        return false;
    }

    /**
     * Get all route resource needed.
     *
     * @param osgiResource
     * @param processItem
     * @throws MalformedURLException
     */
    protected void addOSGIRouteResources(ExportFileResource osgiResource, ProcessItem processItem) throws Exception {
        IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(JavaUtils.JAVA_PROJECT_NAME);
        IFolder srcFolder = project.getFolder(JavaUtils.JAVA_SRC_DIRECTORY);
        IPath srcPath = srcFolder.getLocation();

        // http://jira.talendforge.org/browse/TESB-6437
        // https://jira.talendforge.org/browse/TESB-7893
        ICamelDesignerCoreService camelService = (ICamelDesignerCoreService) GlobalServiceRegister.getDefault().getService(
                ICamelDesignerCoreService.class);
        if (camelService != null) {
            List<IPath> paths = camelService.synchronizeRouteResource(processItem);
            for (IPath path : paths) {
                osgiResource.addResource(path.removeLastSegments(1).makeRelativeTo(srcPath).toString(), path.toFile().toURI()
                        .toURL());
            }
        }
    }

    /**
     * DOC ycbai Comment method "getJobScriptsUncompressed".
     *
     * @param resource
     * @param process
     * @throws IOException
     */
    private void getJobScriptsUncompressed(ExportFileResource resource, ProcessItem process) throws IOException {
        String projectName = getCorrespondingProjectName(process);
        final URI classRootURI = classesLocation.toURI();
        List<String> jobFolderNames = getRelatedJobFolderNames(process);
        try {
            final String classRootLocation = getClassRootLocation() + projectName + File.separator;
            for (String jobFolderName : jobFolderNames) {
                String classRoot = FilesUtils.getFileRealPath(classRootLocation + jobFolderName);
                String targetPath = FilesUtils.getFileRealPath(classesLocation + File.separator + projectName + File.separator
                        + jobFolderName);
                File sourceFile = new File(classRoot);
                File targetFile = new File(targetPath);
                FilesUtils.copyFolder(sourceFile, targetFile, true, null, null, true, false);

                List<URL> fileURLs = FilesUtils.getFileURLs(targetFile);
                for (URL url : fileURLs) {
                    resource.addResource(classRootURI.relativize(new File(url.toURI()).getParentFile().toURI()).toString(), url);
                }
            }
        } catch (IOException e) {
            throw e;
        } catch (Exception e) {
            ExceptionHandler.process(e);
        }
    }

    private static boolean isTalendESBJob(ProcessItem processItem) {
        return null != EmfModelUtils.getComponentByName(processItem, "tESBProviderRequest", "tESBConsumer", "tRouteInput");
    }

    private static boolean isTalendESBJobFactory(ProcessItem processItem) {
        return null != EmfModelUtils.getComponentByName(processItem, "tESBProviderRequest", "tRouteInput");
    }

    private static NodeType getRESTRequestComponent(ProcessItem processItem) {
        return EmfModelUtils.getComponentByName(processItem, "tRESTRequest");
    }

    private static String getPackageName(ProcessItem processItem) {
        return JavaResourcesHelper.getProjectFolderName(processItem)
                + PACKAGE_SEPARATOR
                + JavaResourcesHelper.getJobFolderName(processItem.getProperty().getLabel(),
                        processItem.getProperty().getVersion());
    }

    private URL generateBlueprintConfig(ProcessItem processItem) throws IOException {
        if (itemType == null) {
            itemType = JOB;
        }

        File targetFile = new File(getTmpFolder() + PATH_SEPARATOR + "job.xml"); //$NON-NLS-1$

        if (ROUTE.equals(itemType)) {
            String inputFile = getPluginResourceUri("resources/route-template.xml"); //$NON-NLS-1$
            createRouteBundleBlueprintConfig(processItem, inputFile, targetFile, jobName, jobClassName, itemType);
        } else {
            createJobBundleBlueprintConfig(processItem, targetFile, jobName, jobClassName);
        }

        return targetFile.toURI().toURL();
    }

    private String getPluginResourceUri(String resourcePath) throws IOException {
        final Bundle b = Platform.getBundle(RepositoryPlugin.PLUGIN_ID);
        return FileLocator.toFileURL(FileLocator.find(b, new Path(resourcePath), null)).getFile();
    }

    private static final String TEMPLATE_SPRING_JOB_REST = "/resources/job-rest-beans-template.xml"; //$NON-NLS-1$

    private URL generateRestJobSpringConfig(ProcessItem processItem) throws IOException {
        File targetFile = new File(getTmpFolder() + PATH_SEPARATOR + "beans.xml"); //$NON-NLS-1$

        NodeType restRequestComponent = getRESTRequestComponent(processItem);

        // velocity template context
        Map<String, Object> endpointInfo = new HashMap<String, Object>();

        // job name and class name
        endpointInfo.put("jobName", jobName); //$NON-NLS-1$
        endpointInfo.put("jobClassName", jobClassName); //$NON-NLS-1$

        // REST endpoint address
        final String runtimeServicesContext = "/services"; //$NON-NLS-1$
        final String runtimeServicesContextFull = runtimeServicesContext + "/"; //$NON-NLS-1$
        String endpointUri = EmfModelUtils.computeTextElementValue("REST_ENDPOINT", restRequestComponent); //$NON-NLS-1$
        if (!endpointUri.isEmpty() && !endpointUri.contains("://") && !endpointUri.startsWith("/")) { //$NON-NLS-1$ //$NON-NLS-2$
            endpointUri = "/" + endpointUri; //$NON-NLS-1$
        }
        // TESB-5916: Rest service can't be deployed in the Runtime on the port said in the studio
        // if (endpointUri.contains("://")) {
        //     endpointUri = new URL(endpointUri).getPath();
        // }
        if (runtimeServicesContextFull.equals(endpointUri) || runtimeServicesContext.equals(endpointUri)) {
            // pass as is
        } else if (endpointUri.startsWith(runtimeServicesContextFull)) {
            // remove forwarding "/services/" context as required by runtime (but leave forwarding slash)
            endpointUri = endpointUri.substring(runtimeServicesContextFull.length() - 1);
        }
        endpointInfo.put("address", endpointUri); //$NON-NLS-1$

        // log messages
        endpointInfo.put("logMessages", //$NON-NLS-1$
                EmfModelUtils.computeCheckElementValue("LOG_MESSAGES", restRequestComponent)); //$NON-NLS-1$

        // wrap JSON request
        endpointInfo.put("wrapJsonRequest", //$NON-NLS-1$
                EmfModelUtils.computeCheckElementValue("WRAP_JSON_REQUEST", restRequestComponent)); //$NON-NLS-1$

        // unwrap JSON response (drop root element)
        endpointInfo.put("unwrapJsonResponse", //$NON-NLS-1$
                EmfModelUtils.computeCheckElementValue("UNWRAP_JSON_RESPONSE", restRequestComponent)); //$NON-NLS-1$

        // use HTTP Basic Authentication
        endpointInfo.put("useHttpBasicAuth", //$NON-NLS-1$
                EmfModelUtils.computeCheckElementValue("HTTP_BASIC_AUTH", restRequestComponent)); //$NON-NLS-1$

        // use Service Activity Monitoring
        endpointInfo.put("useSAM", //$NON-NLS-1$
                EmfModelUtils.computeCheckElementValue("SERVICE_ACTIVITY_MONITOR", restRequestComponent)); //$NON-NLS-1$

        // use Service Locator
        endpointInfo.put("useSL", //$NON-NLS-1$
                EmfModelUtils.computeCheckElementValue("SERVICE_LOCATOR", restRequestComponent)); //$NON-NLS-1$

        // Service Locator custom properties
        Map<String, String> slCustomProperties = new HashMap<String, String>();
        ElementParameterType customPropsType = EmfModelUtils.findElementParameterByName(
                "SERVICE_LOCATOR_CUSTOM_PROPERTIES", restRequestComponent); //$NON-NLS-1$
        if (null != customPropsType) {
            EList<?> elementValues = customPropsType.getElementValue();
            final int size = elementValues.size();
            for (int i = 0; i < size; i += 2) {
                if (size <= i + 1) {
                    break;
                }
                ElementValueType name = (ElementValueType) elementValues.get(i);
                ElementValueType value = (ElementValueType) elementValues.get(i + 1);
                if (null != name && null != value) {
                    if (null != name.getValue() && null != value.getValue()) {
                        slCustomProperties.put(unquote(name.getValue()), unquote(value.getValue()));
                    }
                }
            }
        }
        endpointInfo.put("slCustomProps", slCustomProperties); //$NON-NLS-1$

        // service name & namespace
        endpointInfo.put("serviceName", //$NON-NLS-1$
                EmfModelUtils.computeTextElementValue("SERVICE_NAME", restRequestComponent)); //$NON-NLS-1$
        endpointInfo.put("serviceNamespace", //$NON-NLS-1$
                EmfModelUtils.computeTextElementValue("SERVICE_NAMESPACE", restRequestComponent)); //$NON-NLS-1$

        // job OSGi DataSources
        endpointInfo.put("jobDataSources", DataSourceConfig.getAliases(processItem)); //$NON-NLS-1$

        // velocity template context
        Map<String, Object> contextParams = new HashMap<String, Object>();
        contextParams.put("endpoint", endpointInfo); //$NON-NLS-1$

        TemplateProcessor.processTemplate("REST_JOB_SPRING_CONFIG", contextParams, targetFile,
                new InputStreamReader(this.getClass().getResourceAsStream(TEMPLATE_SPRING_JOB_REST)));

        return targetFile.toURI().toURL();
    }

    /**
    *
    * Ensure that the value is not surrounded by quotes.
    *
    * @param value
    * @return
    */
    private static final String unquote(String value) {
        String result = value;
        if (null != value && 1 < value.length()) {
            if (value.startsWith("\"") && value.endsWith("\"")) {
                result = value.substring(1, value.length() - 1);
            }
        }
        return result;
    }

    private static final String TEMPLATE_BLUEPRINT_JOB = "/resources/job-template.xml"; //$NON-NLS-1$

    private void createJobBundleBlueprintConfig(ProcessItem processItem, File targetFile,
            String jobName, String jobClassName) throws IOException {

        // velocity template context
        Map<String, Object> jobInfo = new HashMap<String, Object>();

        // job name and class name
        jobInfo.put("name", jobName); //$NON-NLS-1$
        jobInfo.put("className", jobClassName); //$NON-NLS-1$

        // additional Talend job interfaces (ESB related)
        boolean isESBJob = isTalendESBJob(processItem);
        jobInfo.put("isESBJob", isESBJob); //$NON-NLS-1$
        jobInfo.put("isESBJobFactory", isESBJob && isTalendESBJobFactory(processItem)); //$NON-NLS-1$

        // job components use SAM
        boolean useSAM = false;
        for (NodeType node : EmfModelUtils.getComponentsByName(processItem, "tRESTClient")) { //$NON-NLS-1$
            if (EmfModelUtils.computeCheckElementValue("SERVICE_ACTIVITY_MONITOR", node)) { //$NON-NLS-1$
                useSAM = true;
                break;
            }
        }
        jobInfo.put("useSAM", useSAM); //$NON-NLS-1$

        // job OSGi DataSources
        jobInfo.put("dataSources", DataSourceConfig.getAliases(processItem)); //$NON-NLS-1$


        // velocity template context
        Map<String, Object> contextParams = new HashMap<String, Object>();
        contextParams.put("job", jobInfo); //$NON-NLS-1$

        TemplateProcessor.processTemplate("JOB_BLUEPRINT_CONFIG", contextParams, targetFile,
                new InputStreamReader(this.getClass().getResourceAsStream(TEMPLATE_BLUEPRINT_JOB)));
    }

    private void createRouteBundleBlueprintConfig(ProcessItem processItem, String inputFile, File targetFile, String jobName,
            String jobClassName, String itemType) throws IOException {

        String additionalJobInterfaces = "";
        String additionalServiceProps = "";
        String additionalJobBundleConfig = "";
        String additionalJobBeanParams = "";

        boolean hasSAM = false;

        boolean hasCXFSamlConsumer = false;
        boolean hasCXFSamlProvider = false;
        for (NodeType node : EmfModelUtils.getComponentsByName(processItem, "cCXF")) { //$NON-NLS-1$
            // http://jira.talendforge.org/browse/TESB-3850
            String format = EmfModelUtils.computeTextElementValue("DATAFORMAT", node); //$NON-NLS-1$
            if (!"RAW".equals(format) && !hasSAM) { //$NON-NLS-1$
                hasSAM = EmfModelUtils.computeCheckElementValue("ENABLE_SAM", node);
            }
            if("CXF_MESSAGE".equals(format) || "RAW".equals(format)){
                continue;
            }
            if(hasCXFSamlConsumer && hasCXFSamlProvider){
                continue;
            }
            if(!EmfModelUtils.computeCheckElementValue("ENABLE_SECURITY", node)){
                continue;
            }
            String securityType = EmfModelUtils.computeTextElementValue("SECURITY_TYPE", node);
            if(!"SAML".equals(securityType)){
                continue;
            }

            String uniquename = ElementParameterParser.getUNIQUENAME(node);
            EList connections = processItem.getProcess().getConnection();
            boolean found = false;
            for(Object c:connections){
                String target = ((ConnectionType)c).getTarget();
                if(uniquename.equals(target)){
                    hasCXFSamlConsumer = true;
                    found = true;
                    break;
                }
            }
            if(!found){
                hasCXFSamlProvider = true;
            }
        }

        if(hasCXFSamlConsumer || hasCXFSamlProvider ){
            additionalJobBeanParams += readFileContent("ccxf_saml_bean.txt");
            additionalJobBundleConfig += readFileContent("ccxf_saml_bundle.txt");
        }
        if(hasCXFSamlProvider){
            additionalJobBeanParams += readFileContent("ccxf_saml_provider_bean.txt");
            additionalJobBundleConfig += readFileContent("ccxf_saml_provider_bundle.txt");
        }
        if(hasCXFSamlConsumer){
            additionalJobBeanParams += readFileContent("ccxf_saml_consumer_bean.txt");
            additionalJobBundleConfig += readFileContent("ccxf_saml_consumer_bundle.txt");
        }

        if (hasSAM) {
            additionalJobBeanParams += "<property name=\"eventFeature\" ref=\"eventFeature\"/>";
            additionalJobBundleConfig = "<reference id=\"eventFeature\"  xmlns:ext=\"http://aries.apache.org/blueprint/xmlns/blueprint-ext/v1.0.0\" "
                    + "ext:proxy-method=\"classes\" interface=\"org.talend.esb.sam.agent.feature.EventFeature\"/>";
        }

        // OSGi DataSource
        additionalJobBeanParams += DataSourceConfig.getAdditionalJobBeanParams(processItem, false);

        BufferedReader br = null;
        BufferedWriter bw = null;
        try {
            br = new BufferedReader(new FileReader(inputFile));
            bw = new BufferedWriter(new FileWriter(targetFile));

            String line = br.readLine();
            while (line != null) {
                line = line.replace("@JOBNAME@", jobName) //$NON-NLS-1$
                        .replace("@TYPE@", itemType) //$NON-NLS-1$
                        .replace("@JOBCLASSNAME@", jobClassName) //$NON-NLS-1$
                        .replace("@ADDITIONAL_JOB_INTERFACE@", additionalJobInterfaces) //$NON-NLS-1$
                        .replace("@ADDITIONAL_JOB_BEAN_PARAMS@", additionalJobBeanParams) //$NON-NLS-1$
                        .replace("@ADDITIONAL_JOB_BUNDLE_CONFIG@", additionalJobBundleConfig) //$NON-NLS-1$
                        .replace("@ADDITIONAL_SERVICE_PROPERTIES@", additionalServiceProps); //$NON-NLS-1$

                bw.write(line);
                bw.newLine();
                line = br.readLine();
            }
            bw.flush();
        } finally {
            if (null != br) {
                br.close();
            }
            if (null != bw) {
                bw.close();
            }
        }
    }

    private String readFileContent(String path) throws IOException{
        InputStream is = getClass().getResourceAsStream(path);
        String s = new Scanner(is).useDelimiter("\\A").next();
        is.close();
        return s;
    }

    private static String getOSGIInfFolder() {
        return OSGI_INF.concat(PATH_SEPARATOR).concat(BLUEPRINT);
    }

    private static String getMetaInfSpringFolder() {
        return FileConstants.META_INF_FOLDER_NAME.concat(PATH_SEPARATOR).concat(SPRING);
    }

    private ExportFileResource genMetaInfoFolder(ExportFileResource libResource, List<ProcessItem> itemToBeExport)
            throws IOException {
        ExportFileResource metaInfoResource = new ExportFileResource(null, FileConstants.META_INF_FOLDER_NAME);

        // generate the MANIFEST.MF file in the temp folder
        File manifestFile = new File(getTmpFolder() + PATH_SEPARATOR + FileConstants.MANIFEST_MF_FILE_NAME);

        FileOutputStream fos = null;
        try {
            Manifest manifest = getManifest(libResource, itemToBeExport, jobName);
            fos = new FileOutputStream(manifestFile);
            manifest.write(fos);
        } finally {
            if (fos != null) {
                fos.close();
            }
        }

        metaInfoResource.addResources(Collections.singletonList(manifestFile.toURI().toURL()));

        return metaInfoResource;
    }

    private Manifest getManifest(ExportFileResource libResource, List<ProcessItem> itemToBeExport, String bundleName)
            throws IOException {
        Analyzer analyzer = new Analyzer();
        Jar bin = new Jar(classesLocation);
        analyzer.setJar(bin);

        // http://jira.talendforge.org/browse/TESB-5382 LiXiaopeng
        String symbolicName = bundleName;
        Project project = ProjectManager.getInstance().getCurrentProject();
        if (project != null) {
            String proName = project.getLabel();
            if (proName != null) {
                symbolicName = proName.toLowerCase() + '.' + symbolicName;
            }
        }
        analyzer.setProperty(Analyzer.BUNDLE_NAME, bundleName);
        analyzer.setProperty(Analyzer.BUNDLE_SYMBOLICNAME, symbolicName);
        analyzer.setProperty(Analyzer.BUNDLE_VERSION, getBundleVersion());
        IBrandingService brandingService = (IBrandingService) GlobalServiceRegister.getDefault().getService(
                IBrandingService.class);
        analyzer.setProperty(Analyzer.BUNDLE_VENDOR, brandingService.getFullProductName() + " (" + brandingService.getAcronym()
                + "_" + RepositoryPlugin.getDefault().getBundle().getVersion().toString() + ")");

        String importPackages = "";
        StringBuilder sb = new StringBuilder();
        String delim = "";
        for (ProcessItem pi : itemToBeExport) {
            sb.append(delim).append(getPackageName(pi));
            delim = ",";
            // Add Route Resource Export packages
            // http://jira.talendforge.org/browse/TESB-6227
            if (ROUTE.equals(itemType)) {
                String routeResourcePackages = addRouteResourcePackages(pi);
                if (!routeResourcePackages.isEmpty()) {
                    sb.append(delim).append(routeResourcePackages);
                }
            } else { // JOB
                NodeType restRequestComponent = getRESTRequestComponent(pi);
                if (null != restRequestComponent && "".equals(importPackages)) {
                    if (EmfModelUtils.computeCheckElementValue("HTTP_BASIC_AUTH", restRequestComponent)) {
                        importPackages = "org.apache.cxf.jaxrs.security,";
                    }
                    if (EmfModelUtils.computeCheckElementValue("SERVICE_LOCATOR", restRequestComponent)) {
                        importPackages += "org.talend.esb.servicelocator.cxf,";
                    }
                }
                if (null != restRequestComponent
                        && EmfModelUtils.computeCheckElementValue("SERVICE_ACTIVITY_MONITOR", restRequestComponent)) {
                    importPackages += "org.talend.esb.sam.agent.feature,";
                    analyzer.setProperty(Analyzer.REQUIRE_BUNDLE,
                            "org.apache.cxf.bundle,org.springframework.beans,org.springframework.context,org.springframework.osgi.core,sam-agent,sam-common");
                } else {
                    Collection<NodeType> tRESTClientComps = EmfModelUtils.getComponentsByName(pi, "tRESTClient");
                    for (NodeType node : tRESTClientComps) {
                        // https://jira.talendforge.org/browse/TESB-8066
                        if (EmfModelUtils.computeCheckElementValue("SERVICE_ACTIVITY_MONITOR", node)) { //$NON-NLS-1$
                            importPackages += "org.talend.esb.sam.agent.feature,";
                            analyzer.setProperty(Analyzer.REQUIRE_BUNDLE,
                                    "org.apache.cxf.bundle,org.springframework.beans,org.springframework.context,org.springframework.osgi.core,sam-agent,sam-common");
                            break;
                        }
                    }
                }
            }
        }
        analyzer.setProperty(Analyzer.EXPORT_PACKAGE, sb.toString());

        if (ROUTE.equals(itemType)) {
            addRouteOsgiDependencies(analyzer, libResource, itemToBeExport);
        } else {
            importPackages += "routines.system.api,org.apache.cxf.management.counters,*;resolution:=optional";
            analyzer.setProperty(Analyzer.IMPORT_PACKAGE, importPackages);
        }

        StringBuilder bundleClasspath = new StringBuilder(".");
        Set<String> relativePathList = libResource.getRelativePathList();
        for (String path : relativePathList) {
            Set<URL> resources = libResource.getResourcesByRelativePath(path);
            for (URL url : resources) {
                File dependencyFile = new File(FilesUtils.getFileRealPath(url.getPath()));
                String relativePath = libResource.getDirectoryName() + PATH_SEPARATOR + dependencyFile.getName();
                bundleClasspath.append(',').append(relativePath);
                bin.putResource(relativePath, new FileResource(dependencyFile));
                // analyzer.addClasspath(new File(url.getPath()));
            }
        }
        analyzer.setProperty(Analyzer.BUNDLE_CLASSPATH, bundleClasspath.toString());

        // } else {
        //            String additionalImports = ""; //$NON-NLS-1$
        // for (ProcessItem processItem : itemToBeExport) {
        // if (DataSourceConfig.isDBConnectionJob(processItem)) {
        // additionalImports = ",org.apache.commons.dbcp.datasources";
        // }
        // }
        //            a.put(new Attributes.Name("Import-Package"), //$NON-NLS-1$
        //                    "routines.system.api;resolution:=optional" //$NON-NLS-1$
        //                    + ",org.w3c.dom;resolution:=optional" //$NON-NLS-1$
        //                    + ",javax.xml.namespace;resolution:=optional" //$NON-NLS-1$
        //                    + ",javax.xml.soap;resolution:=optional" //$NON-NLS-1$
        //                    + ",javax.xml.ws;resolution:=optional" //$NON-NLS-1$
        //                    + ",javax.xml.ws.soap;resolution:=optional" //$NON-NLS-1$
        //                    + ",javax.xml.transform;resolution:=optional" //$NON-NLS-1$
        //                    + ",org.apache.cxf.management.counters;resolution:=optional" //$NON-NLS-1$
        // + additionalImports);
        // if (itemToBeExport != null && !itemToBeExport.isEmpty()) {
        // for (ProcessItem pi : itemToBeExport) {
        // /*
        // * need to fill bundle depedence informations for every component,feature 0023460
        // */
        // String requiredBundles = caculateDependenciesBundles(pi);
        // requiredBundles = addAdditionalRequiredBundles(pi, requiredBundles);
        // if (requiredBundles != null && !"".equals(requiredBundles)) {
        // a.put(new Attributes.Name("Require-Bundle"), requiredBundles);
        // }
        // }
        // }
        // if (!libResource.getAllResources().isEmpty()) {
        //                a.put(new Attributes.Name("Bundle-ClassPath"), getClassPath(libResource)); //$NON-NLS-1$
        // }
        // }
        analyzer.setProperty(Analyzer.EXPORT_SERVICE, "routines.system.api.TalendJob;name=" + bundleName + ";type=" + itemType);

        // Calculate the manifest
        Manifest manifest = null;
        try {
            manifest = analyzer.calcManifest();
        } catch (IOException e) {
            throw e;
        } catch (Exception e) {
            ExceptionHandler.process(e);
        } finally {
            analyzer.close();
        }

        return manifest;
    }

    /**
     * Add route resource packages.
     */
    private String addRouteResourcePackages(ProcessItem item) {
        Set<String> pkgs = new HashSet<String>();
        EMap additionalProperties = item.getProperty().getAdditionalProperties();
        if (additionalProperties == null) {
            return "";
        }
        Object resourcesObj = additionalProperties.get("ROUTE_RESOURCES_PROP");
        if (resourcesObj == null) {
            return "";
        }

        String[] resourceIds = resourcesObj.toString().split(",");
        String exportPkg = "";
        for (String id : resourceIds) {
            try {
                IRepositoryViewObject rvo = ProxyRepositoryFactory.getInstance().getLastVersion(id);
                if (rvo != null) {

                    Item it = rvo.getProperty().getItem();
                    String path = it.getState().getPath();
                    if (path != null && !path.isEmpty()) {
                        exportPkg = "route_resources." + path.replace("/", ".");
                    } else {
                        exportPkg = "route_resources";
                    }
                    pkgs.add(exportPkg);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return StringUtils.join(pkgs.toArray(), ",");
    }

    private static void addRouteOsgiDependencies(Analyzer analyzer, ExportFileResource libResource,
            List<ProcessItem> itemToBeExport) throws IOException {
        IPath libPath = ResourcesPlugin.getWorkspace().getRoot().getProject(JavaUtils.JAVA_PROJECT_NAME).getLocation()
                .append(JavaUtils.JAVA_LIB_DIRECTORY);
        for (ProcessItem pi : itemToBeExport) {
            IOsgiDependenciesService dependenciesService = (IOsgiDependenciesService) GlobalServiceRegister.getDefault()
                    .getService(IOsgiDependenciesService.class);
            if (dependenciesService != null) {
                Map<String, String> bundleDependences = dependenciesService.getBundleDependences(pi, pi.getProperty()
                        .getAdditionalProperties());
                // process external libs
                String externalLibs = bundleDependences.get(IOsgiDependenciesService.BUNDLE_CLASSPATH);
                String[] libs = externalLibs.split(IOsgiDependenciesService.ITEM_SEPARATOR);
                Set<URL> list = new HashSet<URL>();
                for (String s : libs) {
                    if (s.isEmpty()) {
                        continue;
                    }
                    IPath path = libPath.append(s);
                    URL url = path.toFile().toURI().toURL();
                    list.add(url);
                }
                libResource.addResources(new ArrayList<URL>(list));

                // add manifest items
                String requireBundles = bundleDependences.get(IOsgiDependenciesService.REQUIRE_BUNDLE);
                if (requireBundles != null && !"".equals(requireBundles)) {
                    analyzer.setProperty(Analyzer.REQUIRE_BUNDLE, requireBundles);
                }
                String importPackages = bundleDependences.get(IOsgiDependenciesService.IMPORT_PACKAGE);
                if (importPackages != null && !"".equals(importPackages)) {
                    analyzer.setProperty(Analyzer.IMPORT_PACKAGE, importPackages + ",*;resolution:=optional"); //$NON-NLS-1$
                }
                String exportPackages = bundleDependences.get(IOsgiDependenciesService.EXPORT_PACKAGE);
                if (exportPackages != null && !"".equals(exportPackages)) {
                    analyzer.setProperty(Analyzer.EXPORT_PACKAGE, exportPackages);
                }
            }
        }
    }

    @Override
    protected List<URL> getExternalLibraries(boolean needLibraries, ExportFileResource[] process, Set<String> neededLibraries) {
        if (!needLibraries) {
            return Collections.emptyList();
        }
        // jar from routines
        List<IRepositoryViewObject> collectRoutines = new ArrayList<IRepositoryViewObject>();
        boolean useBeans = false;
        if (GlobalServiceRegister.getDefault().isServiceRegistered(ICamelDesignerCoreService.class)) {
            ICamelDesignerCoreService camelService = (ICamelDesignerCoreService) GlobalServiceRegister.getDefault().getService(
                    ICamelDesignerCoreService.class);
            if (camelService.isInstanceofCamel(process[0].getItem())) {
                useBeans = true;
            }
        }
        // Lists all the needed jar files
        Set<String> listModulesReallyNeeded = new HashSet<String>();

        if (!useBeans || isOptionChoosed(ExportChoice.needMavenScript)) {
            // Gets all the jar files
            /*
             * FIXME, I think, this should be never executed in osgi manager, should remove it.
             */
            if (neededLibraries == null) {
                // in case export as been done with option "not recompile", then
                // libraires can't be retrieved when
                // build.
                IDesignerCoreService designerService = RepositoryPlugin.getDefault().getDesignerCoreService();
                for (ExportFileResource resource : process) {
                    ProcessItem item = (ProcessItem) resource.getItem();
                    String version = item.getProperty().getVersion();
                    if (!isMultiNodes() && this.getSelectedJobVersion() != null) {
                        version = this.getSelectedJobVersion();
                    }
                    ProcessItem selectedProcessItem;
                    if (resource.getNode() != null) {
                        selectedProcessItem = ItemCacheManager.getProcessItem(resource.getNode().getRoot().getProject(), item
                                .getProperty().getId(), version);
                    } else {
                        // if no node given, take in the current project only
                        selectedProcessItem = ItemCacheManager.getProcessItem(item.getProperty().getId(), version);
                    }
                    IProcess iProcess = designerService.getProcessFromProcessItem(selectedProcessItem);
                    neededLibraries = iProcess.getNeededLibraries(true);
                    if (neededLibraries != null) {
                        listModulesReallyNeeded.addAll(neededLibraries);
                    }
                }
            } else {
                listModulesReallyNeeded.addAll(neededLibraries);
            }
        }
        String includePath;
        if (useBeans) {
            includePath = "beans"; //$NON-NLS-1$
        } else {
            includePath = "routines"; //$NON-NLS-1$
        }
        collectRoutines.addAll(collectRoutines(process, includePath));

        for (IRepositoryViewObject object : collectRoutines) {
            Item item = object.getProperty().getItem();
            if (item instanceof RoutineItem) {
                RoutineItem routine = (RoutineItem) item;
                EList imports = routine.getImports();
                for (Object o : imports) {
                    IMPORTType type = (IMPORTType) o;
                    listModulesReallyNeeded.add(type.getMODULE());
                }
            }
        }

        return getNeededModuleURLs(listModulesReallyNeeded);

    }

    protected List<URL> getNeededModuleURLs(Set<String> neededModules) {
        List<URL> list = new ArrayList<URL>();
        IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
        IProject prj = root.getProject(JavaUtils.JAVA_PROJECT_NAME);
        IJavaProject project = JavaCore.create(prj);
        IPath libPath = project.getResource().getLocation().append(JavaUtils.JAVA_LIB_DIRECTORY);
        File file = libPath.toFile();
        File[] files = file.listFiles(FilesUtils.getAcceptModuleFilesFilter());
        for (File tempFile : files) {
            try {
                if (neededModules.contains(tempFile.getName())) {
                    list.add(tempFile.toURI().toURL());
                }
            } catch (MalformedURLException e) {
                ExceptionHandler.process(e);
            }
        }
        return list;
    }

    @Override
    public void setTopFolder(List<ExportFileResource> resourcesToExport) {
        return;
    }

    @Override
    public String getOutputSuffix() {
        return FileConstants.JAR_FILE_SUFFIX;
    }

    /**
     * Getter for requireBundleModules.
     *
     * @return the requireBundleModules
     */
    protected MultiKeyMap getRequireBundleModules() {
        return this.requireBundleModules;
    }

    protected Set<ModuleNeeded> getRequireBundleModuleNeededs() {
        return getModuleNeededs(getRequireBundleModules());
    }

    @Override
    protected Set<ModuleNeeded> getExcludedModuleNeededs() {
        Set<ModuleNeeded> excludedModuleNeededs = super.getExcludedModuleNeededs();

        excludedModuleNeededs.addAll(getRequireBundleModuleNeededs());
        return excludedModuleNeededs;
    }

    @Override
    protected Set<String> getExcludedModuleNames() {
        Set<String> providedModulesSet = super.getExcludedModuleNames();

        for (ModuleNeeded module : getRequireBundleModuleNeededs()) {
            providedModulesSet.add(module.getModuleName());
        }
        return providedModulesSet;
    }
}
