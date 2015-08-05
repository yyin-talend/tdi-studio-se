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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
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
                            Collections.singletonList(generateRestJobSpringFiles(processItem)));
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
            if (libResource != null) {
                list.add(libResource);
            }
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
        ICamelDesignerCoreService camelService = (ICamelDesignerCoreService) GlobalServiceRegister.getDefault().getService(
                ICamelDesignerCoreService.class);
        List<IPath> paths = camelService.synchronizeRouteResource(processItem);

        IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(JavaUtils.JAVA_PROJECT_NAME);
        IFolder srcFolder = project.getFolder(JavaUtils.JAVA_SRC_DIRECTORY);
        IPath srcPath = srcFolder.getLocation();

        for (IPath path : paths) {
            // http://jira.talendforge.org/browse/TESB-6437
            osgiResource
                    .addResource(path.removeLastSegments(1).makeRelativeTo(srcPath).toString(), path.toFile().toURI().toURL());
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
        final URI classRootURI = classesLocation.toURI();
        List<String> jobFolderNames = getRelatedJobFolderNames(process);
        try {
            for (String jobFolderName : jobFolderNames) {
                String[] jf = jobFolderName.split(":"); //$NON-NLS-1$
                String projectName = jf[0];
                String folderName = jf[1];
                String classRootLocation = getClassRootLocation() + projectName + File.separator;
                String classRoot = FilesUtils.getFileRealPath(classRootLocation + folderName);
                String targetPath = FilesUtils.getFileRealPath(classesLocation + File.separator + projectName + File.separator
                        + folderName);
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

    /**
     * This method will return <code>true</code> if given job contains tESBProviderRequest or tESBConsumer component
     * 
     * @param processItem
     * @author rzubairov
     * @return
     */
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
                + JavaResourcesHelper.getJobFolderName(processItem.getProperty().getLabel(), processItem.getProperty()
                        .getVersion());
    }

    private URL generateBlueprintConfig(ProcessItem processItem) throws IOException {
        if (itemType == null) {
            itemType = JOB;
        }

        String inputFile = getPluginResourceUri("resources/" + itemType + "-template.xml"); //$NON-NLS-1$ //$NON-NLS-2$
        File targetFile = new File(getTmpFolder() + PATH_SEPARATOR + "job.xml"); //$NON-NLS-1$

        createJobBundleBlueprintConfig(processItem, inputFile, targetFile, jobName, jobClassName, itemType);

        return targetFile.toURI().toURL();
    }

    private URL generateRestJobSpringFiles(ProcessItem processItem) throws IOException {
        String inputFile = getPluginResourceUri("resources/job-rest-beans-template.xml"); //$NON-NLS-1$
        File targetFile = new File(getTmpFolder() + PATH_SEPARATOR + "beans.xml"); //$NON-NLS-1$

        createRestJobBundleSpringConfig(processItem, inputFile, targetFile, jobName, jobClassName);

        return targetFile.toURI().toURL();
    }

    private String getPluginResourceUri(String resourcePath) throws IOException {
        final Bundle b = Platform.getBundle(RepositoryPlugin.PLUGIN_ID);
        return FileLocator.toFileURL(FileLocator.find(b, new Path(resourcePath), null)).getFile();
    }

    private void createRestJobBundleSpringConfig(ProcessItem processItem, String inputFile, File targetFile, String jobName,
            String jobClassName) throws IOException {

        NodeType restRequestComponent = getRESTRequestComponent(processItem);

        String endpointUri = EmfModelUtils.computeTextElementValue("REST_ENDPOINT", restRequestComponent);
        if (!endpointUri.isEmpty() && !endpointUri.contains("://") && !endpointUri.startsWith("/")) {
            endpointUri = "/" + endpointUri;
        }
        // TESB-5916: Rest service can't be deployed in the Runtime on the port said in the studio
        // if (endpointUri.contains("://")) {
        // endpointUri = new URL(endpointUri).getPath();
        // }
        if (endpointUri.equals("/services/") || endpointUri.equals("/services")) {
            // pass as is
        } else if (endpointUri.startsWith("/services/")) {
            // remove forwarding "/services/" context as required by runtime
            endpointUri = endpointUri.substring("/services/".length() - 1); // leave forwarding slash
        }

        String jaxrsServiceProviders = "";
        String additionalBeansConfig = "";
        String additionalJobBeanParams = "";
        String additionalJsonProviderBeanParams = "";
        boolean useHttpBasicAuth = EmfModelUtils.computeCheckElementValue("HTTP_BASIC_AUTH", restRequestComponent);
        if (useHttpBasicAuth) {
            jaxrsServiceProviders = "<ref bean=\"authenticationFilter\"/>";
            additionalBeansConfig = "\t<bean id=\"authenticationFilter\" class=\"org.apache.cxf.jaxrs.security.JAASAuthenticationFilter\">"
                    + "\n\t\t<property name=\"contextName\" value=\"karaf\"/>\n\t</bean>";
        }
        boolean wrapJsonRequest = EmfModelUtils.computeCheckElementValue("WRAP_JSON_REQUEST", restRequestComponent);
        if (wrapJsonRequest) {
            additionalJsonProviderBeanParams = "<property name=\"supportUnwrapped\" value=\"true\"/>"
                    + "\n\t\t<property name=\"wrapperName\" value=\"root\"/>";
        }
        // OSGi DataSource
        additionalJobBeanParams += DataSourceConfig.getAdditionalJobBeanParams(processItem, true);

        BufferedReader br = null;
        BufferedWriter bw = null;
        try {
            br = new BufferedReader(new FileReader(inputFile));
            bw = new BufferedWriter(new FileWriter(targetFile));

            String line = br.readLine();
            while (line != null) {
                line = line.replace("@ENDPOINT_URI@", endpointUri) //$NON-NLS-1$
                        .replace("@JOBNAME@", jobName) //$NON-NLS-1$
                        .replace("@JOBCLASSNAME@", jobClassName) //$NON-NLS-1$
                        .replace("@JAXRS_SERVICE_PROVIDERS@", jaxrsServiceProviders) //$NON-NLS-1$
                        .replace("@ADDITIONAL_BEANS_CONFIG@", additionalBeansConfig) //$NON-NLS-1$
                        .replace("@ADDITIONAL_JOB_BEAN_PARAMS@", additionalJobBeanParams) //$NON-NLS-1$
                        .replace("@ADDITIONAL_JSON_PROVIDER_BEAN_PARAMS@", additionalJsonProviderBeanParams); //$NON-NLS-1$

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

    /**
     * Created OSGi Blueprint configuration for job bundle.
     * 
     * @param processItem
     * @param inputFile
     * @param targetFile
     * @param jobName
     * @param jobClassName
     * @param itemType
     * @param isESBJob
     * @throws IOException
     */
    private void createJobBundleBlueprintConfig(ProcessItem processItem, String inputFile, File targetFile, String jobName,
            String jobClassName, String itemType) throws IOException {

        String additionalJobInterfaces = "";
        String additionalServiceProps = "";
        String additionalJobBundleConfig = "";
        String additionalJobBeanParams = "";

        // http://jira.talendforge.org/browse/TESB-3677
        if (ROUTE.equals(itemType)) {
            for (NodeType node : EmfModelUtils.getComponentsByName(processItem, "cCXF")) { //$NON-NLS-1$
                // http://jira.talendforge.org/browse/TESB-3850
                String format = EmfModelUtils.computeTextElementValue("DATAFORMAT", node); //$NON-NLS-1$
                if (!"RAW".equals(format)) { //$NON-NLS-1$
                    if (EmfModelUtils.computeCheckElementValue("ENABLE_SAM", node)) { //$NON-NLS-1$
                        // SAM
                        additionalJobBeanParams = "<property name=\"eventFeature\" ref=\"eventFeature\"/>";
                        additionalJobBundleConfig = "<reference id=\"eventFeature\"  xmlns:ext=\"http://aries.apache.org/blueprint/xmlns/blueprint-ext/v1.0.0\" "
                                + "ext:proxy-method=\"classes\" interface=\"org.talend.esb.sam.agent.feature.EventFeature\"/>";
                        break;
                    }
                }
            }
        } else { // JOB
            if (isTalendESBJob(processItem)) {
                additionalJobInterfaces = "<value>routines.system.api.TalendESBJob</value>"; //$NON-NLS-1$
                if (isTalendESBJobFactory(processItem)) {

                    additionalJobInterfaces += "\n\t\t\t<value>routines.system.api.TalendESBJobFactory</value>"; //$NON-NLS-1$
                    additionalServiceProps = "<entry key=\"multithreading\" value=\"true\" />"; //$NON-NLS-1$
                }
            }
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
                if (null != restRequestComponent && "".equals(importPackages)
                        && EmfModelUtils.computeCheckElementValue("HTTP_BASIC_AUTH", restRequestComponent)) {
                    importPackages = "org.apache.cxf.jaxrs.security,";
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

    // private String addAdditionalRequiredBundles(ProcessItem pi, String requiredBundles) {
    // if (isRESTClientJob(pi) || isRESTProviderJob(pi)) {
    // String bundlesToAdd = "org.apache.cxf.cxf-rt-frontend-jaxrs" + ",org.apache.cxf.cxf-rt-rs-extension-providers";
    // // check if we need add ',' after already existing bundles
    // requiredBundles = (requiredBundles != null && !"".equals(requiredBundles)) ? requiredBundles + "," : "";
    // requiredBundles = requiredBundles + bundlesToAdd;
    // }
    //
    // return requiredBundles;
    // }

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

        collectRoutines.addAll(collectRoutines(process, useBeans));

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
