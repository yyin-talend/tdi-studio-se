// ============================================================================
//
// Copyright (C) 2006-2021 Talend Inc. - www.talend.com
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
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.Optional;
import java.util.Properties;
import java.util.Set;
import java.util.jar.Attributes;
import java.util.jar.Manifest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.map.MultiKeyMap;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.common.util.EList;
import org.eclipse.jdt.internal.compiler.batch.Main;
import org.osgi.framework.Bundle;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.exception.PersistenceException;
import org.talend.commons.utils.generation.JavaUtils;
import org.talend.commons.utils.io.FilesUtils;
import org.talend.core.CorePlugin;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.PluginChecker;
import org.talend.core.model.components.ComponentUtilities;
import org.talend.core.model.components.IComponent;
import org.talend.core.model.general.ModuleNeeded;
import org.talend.core.model.general.Project;
import org.talend.core.model.process.IProcess;
import org.talend.core.model.process.JobInfo;
import org.talend.core.model.properties.Item;
import org.talend.core.model.properties.ProcessItem;
import org.talend.core.model.properties.Property;
import org.talend.core.model.properties.RoutineItem;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.core.model.utils.JavaResourcesHelper;
import org.talend.core.repository.constants.FileConstants;
import org.talend.core.repository.model.ProxyRepositoryFactory;
import org.talend.core.runtime.maven.MavenArtifact;
import org.talend.core.runtime.maven.MavenConstants;
import org.talend.core.runtime.process.ITalendProcessJavaProject;
import org.talend.core.runtime.process.LastGenerationInfo;
import org.talend.core.runtime.process.TalendProcessOptionConstants;
import org.talend.core.runtime.repository.build.BuildExportManager;
import org.talend.core.service.ITaCoKitDependencyService;
import org.talend.core.ui.branding.IBrandingService;
import org.talend.core.utils.CodesJarResourceCache;
import org.talend.designer.core.ICamelDesignerCoreService;
import org.talend.designer.core.IDesignerCoreService;
import org.talend.designer.core.model.utils.emf.component.IMPORTType;
import org.talend.designer.core.model.utils.emf.talendfile.ElementParameterType;
import org.talend.designer.core.model.utils.emf.talendfile.ElementValueType;
import org.talend.designer.core.model.utils.emf.talendfile.NodeType;
import org.talend.designer.core.model.utils.emf.talendfile.RoutinesParameterType;
import org.talend.designer.maven.model.TalendMavenConstants;
import org.talend.designer.maven.utils.PomIdsHelper;
import org.talend.designer.maven.utils.PomUtil;
import org.talend.designer.runprocess.IProcessor;
import org.talend.designer.runprocess.IRunProcessService;
import org.talend.designer.runprocess.ProcessorException;
import org.talend.designer.runprocess.ProcessorUtilities;
import org.talend.repository.ProjectManager;
import org.talend.repository.RepositoryPlugin;
import org.talend.repository.documentation.ExportFileResource;
import org.talend.repository.ui.wizards.exportjob.scriptsmanager.JobJavaScriptsManager;
import org.talend.repository.utils.EmfModelUtils;
import org.talend.repository.utils.EsbConfigUtils;
import org.talend.repository.utils.TemplateProcessor;

import aQute.bnd.header.Parameters;
import aQute.bnd.osgi.Analyzer;
import aQute.bnd.osgi.Clazz;
import aQute.bnd.osgi.Domain;
import aQute.bnd.osgi.FileResource;
import aQute.bnd.osgi.Jar;

/**
 * DOC ycbai class global comment. Detailled comment
 */
public class JobJavaScriptOSGIForESBManager extends JobJavaScriptsManager {


    protected static final char MANIFEST_ITEM_SEPARATOR = ',';

    protected static final String OSGI_EXCLUDE_PROP_FILENAME = "osgi-exclude.properties"; ////$NON-NLS-1$

    private boolean ENABLE_CACHE = StringUtils.equals(System.getProperty("enable.manifest.cache", "false"), "true");

    private static final Collection<String> EXCLUDED_MODULES = new ArrayList<String>();

    private Bundle esbBundle = Platform.getBundle(PluginChecker.ESBSE_PLUGIN_ID);

    File cacheFile = null;

    public JobJavaScriptOSGIForESBManager(Map<ExportChoice, Object> exportChoiceMap, String contextName, String launcher,
            int statisticPort, int tracePort) {
        super(exportChoiceMap, contextName, launcher, statisticPort, tracePort);

        if (esbBundle != null) {
            IPath stateLocationPath = Platform.getStateLocation(esbBundle);

            cacheFile = new File(stateLocationPath.toFile().getAbsolutePath() + File.separatorChar + "dependencyMap.cache");

            FileInputStream fi = null;
            ObjectInputStream oi = null;
            try {
                if (!cacheFile.exists()) {
                    cacheFile.createNewFile();
                } else {
                    if (cacheFile.length() > 0) {
                        fi = new FileInputStream(cacheFile);
                        oi = new ObjectInputStream(fi);
                        dependencyCacheMap = (Map) oi.readObject();
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (oi != null) {
                        oi.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    Map<String, List<Object>> dependencyCacheMap = null;

    protected static final char PACKAGE_SEPARATOR = '.';

    private static final String JAVA = "java"; //$NON-NLS-1$

    private static final String JAVA_VERSION = "java.version";

    private static File routineClassRootFolder = null;

    private static File beanClassRootFolder = null;

    private MultiKeyMap requireBundleModules = new MultiKeyMap();

//    private String itemType = null;

    private final File classesLocation = new File(getTmpFolder() + File.separator + "classes"); //$NON-NLS-1$;

    private static final String DLL_FILE = ".dll"; //$NON-NLS-1$

    private static final String SO_FILE = ".so"; //$NON-NLS-1$

    private static final String OSGI_OS_CODE = ';' + "osname=" + System.getProperty("osgi.os") + ';' + "processor=" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            + System.getProperty("osgi.arch") + ','; //$NON-NLS-1$
    
    private static final String RESOLUTION_OPTIONAL = ";resolution:=optional";

    private static final String COMPILER_LOG_DELIMITER = "----------";

    private static final String COMPILER_LOG_REGEX = "(^[a-z_0-9\\.]+)\\.";

    private static final String COMPILER_LOG_ERROR_1 = "cannot be resolved to a type";

    private static final String COMPILER_LOG_ERROR_2 = "cannot be resolved";
    
    private static final String COMPILER_LOG_ERROR_3 = "cannot be resolved to a variable";
    
    private static String complianceLevel = "1.8";
    
    private static String complianceParameter;

    static {
        String javaVersion = System.getProperty(JAVA_VERSION);
        if (javaVersion != null) {
            if (javaVersion.startsWith("1.7")) {
                complianceLevel = "1.7";
            } else if (javaVersion.startsWith("1.8")) {
                complianceLevel = "1.8";
            } else {
                Matcher m = Pattern.compile("([0-9]+).*").matcher(javaVersion);
                if (m.find()) {
                    complianceLevel = m.group(1);
                }
    	    }
        }
        complianceParameter = " -" + complianceLevel + " -maxProblems 100000 -nowarn";
        
        
        try {
            File propFile = null;
            File esbConfigurationLocation = EsbConfigUtils.getEclipseEsbFolder();

            if (esbConfigurationLocation != null && esbConfigurationLocation.exists() && esbConfigurationLocation.isDirectory()) {
                propFile = new File(esbConfigurationLocation.getAbsolutePath(), OSGI_EXCLUDE_PROP_FILENAME);
            }
            
            InputStream is = null;
            if (propFile != null && propFile.exists() && propFile.isFile()) {
                is = new FileInputStream(propFile);
            } else {
                is = RepositoryPlugin.getDefault().getBundle().getEntry("/resources/" + OSGI_EXCLUDE_PROP_FILENAME)
                        .openStream();
            }
            final Properties p = new Properties();
            p.load(is);
            for (Enumeration<?> e = p.propertyNames(); e.hasMoreElements();) {
                EXCLUDED_MODULES.add((String) e.nextElement());
            }
            if ("1.8".equals(complianceLevel) && null != p.getProperty("java8.excludes")) {
                EXCLUDED_MODULES.addAll(Arrays.asList(p.getProperty("java8.excludes").split(",")));
            }
        } catch (IOException e) {
            RepositoryPlugin.getDefault().getLog()
                    .log(new Status(Status.ERROR, RepositoryPlugin.PLUGIN_ID, "Unable to load OSGi excludes", e));
        }
    }

    @Override
    public List<ExportFileResource> getExportResources(ExportFileResource[] processes, String... codeOptions)
            throws ProcessorException {

        List<ExportFileResource> list = new ArrayList<ExportFileResource>();

        ExportFileResource osgiResource = new ExportFileResource(null, ""); //$NON-NLS-1$;
        ExportFileResource jobScriptResource = new ExportFileResource(null, ""); //$NON-NLS-1$

        list.add(osgiResource);
        list.add(jobScriptResource);

        // set export config mode now only to be sure that the libraries will be
        // setup for an export mode, and not
        // editor mode.
        ProcessorUtilities.setExportConfig(JAVA, "", ""); //$NON-NLS-1$
        // set export type as osgi
        ProcessorUtilities.setExportAsOSGI(true);

        try {
            ProcessItem processItem = null;
            for (ExportFileResource process : processes) {
                processItem = (ProcessItem) process.getItem();
                if (processItem.eIsProxy() || processItem.getProcess().eIsProxy()) {
//                    try {
//                        Property property = ProxyRepositoryFactory.getInstance().getUptodateProperty(processItem.getProperty());
//                        processItem = (ProcessItem) property.getItem();
//                    } catch (PersistenceException e) {
//                        throw new ProcessorException(e);
//                    }
                }

                String jobVersion = processItem.getProperty().getVersion();
                if (!isMultiNodes() && getSelectedJobVersion() != null) {
                    jobVersion = getSelectedJobVersion();
                }

                ProcessorUtilities.setExportConfig(process.getDirectoryName(), true);

                String processId = processItem.getProperty().getId();

                if (null == contextName) {
                    contextName = processItem.getProcess().getDefaultContext();
                }

                IProcess iProcess = generateJobFiles(processItem, contextName, jobVersion,
                        statisticPort != IProcessor.NO_STATISTICS, tracePort != IProcessor.NO_TRACES,
                        isOptionChoosed(ExportChoice.applyToChildren), progressMonitor);
                analysisModules(processId, jobVersion);

                analysisMavenModule(processItem);

                // generate jar file for job
                getJobScriptsUncompressed(jobScriptResource, processItem);

                // dynamic DB XML mapping
                addXmlMapping(process, true);// isOptionChoosed(ExportChoice.needSourceCode)

                if (CollectionUtils.isNotEmpty(process.getAllResources())) {
                    ExportFileResource xm = new ExportFileResource(null, JavaUtils.JAVA_XML_MAPPING);
                    Set<URL> urls = process
                            .getResourcesByRelativePath(JOB_SOURCE_FOLDER_NAME + PATH_SEPARATOR + JavaUtils.JAVA_XML_MAPPING);

                    if (CollectionUtils.isNotEmpty(urls)) {
                        xm.addResources(new ArrayList<URL>(urls));
                        list.add(xm);
                    }
                }

                generateConfig(osgiResource, processItem, iProcess);

                addResources(osgiResource, processItem);

                /*
                 * export current item's dependencies. this used for TDM components specially and need more discussion
                 * about then
                 */
                // TCOMP-1681: feature uses this call for feeding a component-runtime compliant MAVEN-INF/repository and
                // TALEND-INF/plugins.properties.
                BuildExportManager.getInstance().exportOSGIDependencies(osgiResource, processItem);

            }

            ExportFileResource libResource = getCompiledLibExportFileResource(processes);
            list.add(libResource);

            ExportFileResource libResourceSelected = new ExportFileResource(null, LIBRARY_FOLDER_NAME);

            if (GlobalServiceRegister.getDefault().isServiceRegistered(ICamelDesignerCoreService.class)) {
                ICamelDesignerCoreService camelService = (ICamelDesignerCoreService) GlobalServiceRegister.getDefault().getService(
                        ICamelDesignerCoreService.class);
                Map<String, String> nameMavenUriMap = getNameMavenUriMap();
                Collection<String> unselectList = camelService.getUnselectDependenciesBundle(processItem);
                List<URL> libListURLs = new ArrayList<>();

                for(Set<URL> set:libResource.getAllResources()) {
                    for (URL url : set) {
                        String libName = new File(new File(url.getFile()).toURI()).getName();
                        String libMavenUri = nameMavenUriMap.get(libName);
                        boolean isUnselectLib = false;
                        if(unselectList.contains(libMavenUri)) {
                            isUnselectLib = true;
                        }

                        if (!isUnselectLib) {
                            libListURLs.add(url);
                        }
                    }
                }
            
                libResourceSelected.addResources(libListURLs);
            }

            // generate the META-INFO folder
            ExportFileResource metaInfoFolder = genMetaInfoFolder(libResourceSelected, processItem);
            list.add(0, metaInfoFolder);

            ExportFileResource providedLibResources = getProvidedLibExportFileResource(processes);
            if (providedLibResources != null) {
                list.add(providedLibResources);
            }

            // TCOMP-1681: for class isolation and avoid clashes, we remove tacokit dependencies to main classpath.
            // However, they're in the scope of ComponentManager.
            if (GlobalServiceRegister.getDefault().isServiceRegistered(ITaCoKitDependencyService.class)) {
                ITaCoKitDependencyService tckService = (ITaCoKitDependencyService) GlobalServiceRegister.getDefault()
                        .getService(ITaCoKitDependencyService.class);
                if (tckService != null && tckService.hasTaCoKitComponents(tckService.getJobComponents(processItem))) {
                    list = cleanupResources(tckService.getJobComponents(processItem), list, tckService);
                }
            }

        } catch (ProcessorException e) {
            throw e;
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
            if (isCompiledLib(module) || isRESTRequestNeededLibs(module)) {
                addModuleNeededsInMap(getCompiledModules(), processId, processVersion, module);
            } else if (isRequireBundleLib(module)) {
                addModuleNeededsInMap(getRequireBundleModules(), processId, processVersion, module);
            } else {
                addModuleNeededsInMap(getExcludedModules(), processId, processVersion, module);
            }
        }
    }

    private boolean isRESTRequestNeededLibs(ModuleNeeded module) {
        return module.getModuleName().startsWith("delight-rhino-sandbox") || module.getModuleName().startsWith("rhino");
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

    private List<ExportFileResource> cleanupResources(Stream<IComponent> components, List<ExportFileResource> resources, ITaCoKitDependencyService service) {
        Set<String> tckOnly = service.getTaCoKitOnlyDependencies(components);
        final List<ExportFileResource> rmResources = new ArrayList<>();
        //This code is nicer but have to reiterate after, so not so efficient
//        List<URL> rmDeps = resources.stream()
//                .filter(rf -> "lib".equals(rf.getDirectoryName()))
//                .map(resource -> resource.getResourcesByRelativePath(""))
//                .flatMap(urls -> urls.stream())
//                .filter(url -> tckOnly.stream().anyMatch(tck -> url.toString().endsWith(tck)))
//                .collect(Collectors.toList());
        for (ExportFileResource resource : resources) {
            if (!("lib".equals(resource.getDirectoryName()) || "lib-provided".equals(resource.getDirectoryName()))) {
                continue;
            }
            List<URL> rmDeps = resource.getResourcesByRelativePath("").stream()
                    .filter(url -> tckOnly.stream().anyMatch(tck -> url.toString().endsWith(tck)))
                    .collect(Collectors.toList());
            rmDeps.stream().forEach(dep -> resource.removeResources("", dep));
        }
        return resources;
    }

    protected String getIncludeRoutinesPath() {
        return USER_ROUTINES_PATH;
    }

    protected Collection<String> getRoutinesPaths() {
        return Collections.singletonList(getIncludeRoutinesPath());
    }

    @Override
    protected void addRoutinesResources(ExportFileResource[] processes, ExportFileResource libResource) {
        // codes jars
        ProcessItem item = (ProcessItem) processes[0].getItem();
        EList<RoutinesParameterType> routinesParameter = item.getProcess().getParameters().getRoutinesParameter();
        List<URL> codesjarM2Files = new ArrayList<>();
        if (routinesParameter != null) {
            routinesParameter.stream().filter(r -> r.getType() != null).map(r -> CodesJarResourceCache.getCodesJarById(r.getId()))
                    .filter(info -> info != null).forEach(info -> {
                        MavenArtifact artifact = new MavenArtifact();
                        artifact.setGroupId(PomIdsHelper.getCodesJarGroupId(info));
                        artifact.setArtifactId(info.getLabel().toLowerCase());
                        artifact.setVersion(PomIdsHelper.getCodesJarVersion(info.getProjectTechName()));
                        artifact.setType(MavenConstants.TYPE_JAR);
                        try {
                            codesjarM2Files.add(new File(PomUtil.getArtifactFullPath(artifact)).toURI().toURL());
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        }
                    });
        }

        String projectTechName = ProjectManager.getInstance().getProject(item).getTechnicalLabel();

        // routines.jar
        MavenArtifact routinesArtifact = new MavenArtifact();
        routinesArtifact.setGroupId(PomIdsHelper.getCodesGroupId(projectTechName, TalendMavenConstants.DEFAULT_CODE));
        routinesArtifact.setArtifactId(TalendMavenConstants.DEFAULT_ROUTINES_ARTIFACT_ID);
        routinesArtifact.setVersion(PomIdsHelper.getCodesVersion(projectTechName));
        routinesArtifact.setType(MavenConstants.TYPE_JAR);
        try {
            codesjarM2Files.add(new File(PomUtil.getArtifactFullPath(routinesArtifact)).toURI().toURL());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        // beans.jar
        MavenArtifact beansArtifact = new MavenArtifact();
        beansArtifact.setGroupId(PomIdsHelper.getCodesGroupId(projectTechName, TalendMavenConstants.DEFAULT_BEAN));
        beansArtifact.setArtifactId(TalendMavenConstants.DEFAULT_BEANS_ARTIFACT_ID);
        beansArtifact.setVersion(PomIdsHelper.getCodesVersion(projectTechName));
        beansArtifact.setType(MavenConstants.TYPE_JAR);
        try {
            codesjarM2Files.add(new File(PomUtil.getArtifactFullPath(beansArtifact)).toURI().toURL());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        libResource.addResources(codesjarM2Files);
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
        /*
         * If null, will add the lib always.
         * If empty, nothing will be added.
         * Else, add the bundle id in "Require-Bundle", but don't add the lib.
         */
        return module != null && isIncludedLib(module);
    }

    /**
     * If null, will add the lib always. @see isIncludedLib
     * If empty, nothing will be added. @see isExcludedLib
     * Else, add the bundle id in "Require-Bundle", but don't add the lib. @see isIncludedInRequireBundle
     */
    protected boolean isIncludedLib(ModuleNeeded module) {
        return module != null && module.getBundleName() == null && !isExcluded(module.getModuleName());
    }

    private static boolean isExcluded(String moduleName) {
        for (String exclude : EXCLUDED_MODULES) {
            if (moduleName.startsWith(exclude)) {
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
    protected void addResources(ExportFileResource osgiResource, ProcessItem processItem) throws Exception {
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
                String classRootLocation = getJobClassRootLocation(process.getProperty()) + projectName + File.separator;
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

    private static boolean isTalendESBJob(ProcessItem processItem) {
        return null != EmfModelUtils.getComponentByName(processItem, "tESBProviderRequest", "tESBConsumer", "tRouteInput");
    }

    private static boolean isTalendESBJobFactory(ProcessItem processItem) {
        return null != EmfModelUtils.getComponentByName(processItem, "tESBProviderRequest", "tRouteInput");
    }

    private static boolean isTalendStepTemplate(ProcessItem processItem) {
        return null != EmfModelUtils.getComponentByName(processItem, "tActionInput", "tActionOutput", "tActionReject");
    }

    private static NodeType getRESTRequestComponent(ProcessItem processItem) {
        return EmfModelUtils.getComponentByName(processItem, "tRESTRequest");
    }
    
    private static NodeType getMDMComponent(ProcessItem processItem) {
        NodeType nodeType = EmfModelUtils.getComponentByName(processItem, "tMDMConnection", "tMDMInput", "tMDMOutput");
        if (nodeType != null) {
            return nodeType;
        } 
        for (JobInfo subjobInfo : ProcessorUtilities.getChildrenJobInfo(processItem)) {
            nodeType = EmfModelUtils.getComponentByName(subjobInfo.getProcessItem(), "tMDMConnection", "tMDMInput", "tMDMOutput");
            if (nodeType != null) {
                return nodeType;
            }
        }
        return null;
    }

    private static NodeType getRESTClientComponent(ProcessItem processItem) {
        NodeType nodeType = EmfModelUtils.getComponentByName(processItem, "tRESTClient");
        if (nodeType != null) {
            return nodeType;
        }
        for (JobInfo subjobInfo : ProcessorUtilities.getChildrenJobInfo(processItem)) {
            nodeType = EmfModelUtils.getComponentByName(subjobInfo.getProcessItem(), "tRESTClient");
            if (nodeType != null) {
                return nodeType;
            }
        }
        return null;
    }

    protected static String getPackageName(ProcessItem processItem) {
        return JavaResourcesHelper.getProjectFolderName(processItem)
                + PACKAGE_SEPARATOR
                + JavaResourcesHelper.getJobFolderName(processItem.getProperty().getLabel(), processItem.getProperty()
                        .getVersion());
    }

    private Map<String, Object> collectJobInfo(ProcessItem processItem, IProcess process) {
        // velocity template context
        Map<String, Object> jobInfo = new HashMap<String, Object>();

        String name = processItem.getProperty().getLabel();
        String className = getPackageName(processItem) + PACKAGE_SEPARATOR + name;

        boolean isTalendStepTemplate = isTalendStepTemplate(processItem);
        // job name and class name
        String jobName = name;
        if (!EmfModelUtils.getComponentsByName(processItem, "tRouteInput").isEmpty()) { //$NON-NLS-1$
            jobName = className;
        }
        jobInfo.put("name", jobName);
        jobInfo.put("version", getBundleVersion()); //$NON-NLS-1$
        jobInfo.put("className", className); //$NON-NLS-1$

        // additional Talend job interfaces (ESB related)
        boolean isESBJob = isTalendESBJob(processItem);
        jobInfo.put("isESBJob", isESBJob); //$NON-NLS-1$
        jobInfo.put("isESBJobFactory", isESBJob && isTalendESBJobFactory(processItem)); //$NON-NLS-1$
        jobInfo.put("isTalendStepTemplate", isTalendStepTemplate); //$NON-NLS-1$

        // job components use SAM / use SAML
        boolean useSAM = false;
        for (NodeType node : EmfModelUtils.getComponentsByName(processItem, "tRESTClient")) { //$NON-NLS-1$
            if (!useSAM && EmfModelUtils.computeCheckElementValue("SERVICE_ACTIVITY_MONITOR", node)) { //$NON-NLS-1$
                useSAM = true;
                break;
            }
        }
        jobInfo.put("useSAM", useSAM); //$NON-NLS-1$

        // job OSGi DataSources
        jobInfo.put("dataSources", DataSourceConfig.getAliases(process)); //$NON-NLS-1$

        jobInfo.put("hasDestroyMethod", ERepositoryObjectType.PROCESS_MR != ERepositoryObjectType.getItemType(processItem));

        return jobInfo;
    }

    private static Map<String, Object> collectRestEndpointInfo(ProcessItem processItem) {
        Map<String, Object> endpointInfo = new HashMap<String, Object>();

        NodeType restRequestComponent = getRESTRequestComponent(processItem);

        if (null == restRequestComponent) {
            return endpointInfo;
        }

        // REST endpoint address
        final String runtimeServicesContext = "/services"; //$NON-NLS-1$
        final String runtimeServicesContextFull = runtimeServicesContext + '/';
        String endpointUri = EmfModelUtils.computeTextElementValue("REST_ENDPOINT", restRequestComponent); //$NON-NLS-1$
        if (endpointUri.contains("context.") || endpointUri.contains("(") || endpointUri.contains("+")) { //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            // Since TESB-24998, endpointUri could be an expression with variables, does not need unquote
            endpointUri = EmfModelUtils.computeTextElementValueWithoutUnquote("REST_ENDPOINT", restRequestComponent); //$NON-NLS-1$
        }
        if (endpointUri.contains("context.")) {
            // TESB-24998 Add context bean in blueprint
            endpointInfo.put("useContextBean", true); //$NON-NLS-1$
            endpointInfo.put("defaultContext", processItem.getProcess().getDefaultContext()); //$NON-NLS-1$
        } else if (!endpointUri.contains("://") && !endpointUri.startsWith("/")) { //$NON-NLS-1$ //$NON-NLS-2$
            endpointUri = '/' + (endpointUri.isEmpty() ? processItem.getProperty().getLabel() : endpointUri);
        }

        // TESB-5916: Rest service can't be deployed in the Runtime on the port said in the studio
        // if (endpointUri.contains("://")) {
        // endpointUri = new URL(endpointUri).getPath();
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

        // Attributes to Elements
        endpointInfo.put("attributesToElements", //$NON-NLS-1$
                EmfModelUtils.computeCheckElementValue("ATTRIBUTES_TO_ELEMENTS", restRequestComponent)); //$NON-NLS-1$
        
        // Convert JSON to string (big double values)
        endpointInfo.put("convertTypesToStrings", //$NON-NLS-1$
                EmfModelUtils.computeCheckElementValue("CONVERT_JSON_VALUES_TO_STRING", restRequestComponent)); //$NON-NLS-1$

        // use Authentication & authentication type
        endpointInfo.put("useAuthentication", //$NON-NLS-1$
                EmfModelUtils.computeCheckElementValue("NEED_AUTH", restRequestComponent)); //$NON-NLS-1$
        endpointInfo.put("authenticationType", //$NON-NLS-1$
                EmfModelUtils.computeTextElementValue("AUTH_TYPE", restRequestComponent)); //$NON-NLS-1$

        // use Service Activity Monitoring
        endpointInfo.put("useSAM", //$NON-NLS-1$
                EmfModelUtils.computeCheckElementValue("SERVICE_ACTIVITY_MONITOR", restRequestComponent)); //$NON-NLS-1$

        // use Service Locator
        endpointInfo.put("useSL", //$NON-NLS-1$
                EmfModelUtils.computeCheckElementValue("SERVICE_LOCATOR", restRequestComponent)); //$NON-NLS-1$

        // use Authorization
        if (isStudioEEVersion()) {
            if (EmfModelUtils.computeCheckElementValue("NEED_AUTH", restRequestComponent)) {
                endpointInfo.put("useAuthorization", //$NON-NLS-1$
                        EmfModelUtils.computeCheckElementValue("NEED_AUTHORIZATION", restRequestComponent)); //$NON-NLS-1$
            }
        } else {
            endpointInfo.put("useAuthorization", false); //$NON-NLS-1$
        }

        // Service Locator custom properties
        Map<String, String> slCustomProperties = new HashMap<String, String>();
        ElementParameterType customPropsType = EmfModelUtils.findElementParameterByName(
                "SERVICE_LOCATOR_CUSTOM_PROPERTIES", restRequestComponent); //$NON-NLS-1$
        if (null != customPropsType) {
            List<?> elementValues = customPropsType.getElementValue();
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

        Map<String, String> samCustomProperties = new HashMap<String, String>();
        customPropsType = EmfModelUtils.findElementParameterByName("SERVICE_ACTIVITY_MONITOR_CUSTOM_PROPERTIES", //$NON-NLS-1$
                restRequestComponent);
        if (null != customPropsType) {
            List<?> elementValues = customPropsType.getElementValue();
            final int size = elementValues.size();
            for (int i = 0; i < size; i += 2) {
                if (size <= i + 1) {
                    break;
                }
                ElementValueType name = (ElementValueType) elementValues.get(i);
                ElementValueType value = (ElementValueType) elementValues.get(i + 1);
                if (null != name && null != value) {
                    if (null != name.getValue() && null != value.getValue()) {
                        samCustomProperties.put(unquote(name.getValue()), unquote(value.getValue()));
                    }
                }
            }
        }

        endpointInfo.put("slCustomProps", slCustomProperties); //$NON-NLS-1$
        endpointInfo.put("samCustomProps", samCustomProperties); //$NON-NLS-1$

        // service name & namespace
        endpointInfo.put("serviceName", //$NON-NLS-1$
                EmfModelUtils.computeTextElementValue("SERVICE_NAME", restRequestComponent)); //$NON-NLS-1$
        endpointInfo.put("serviceNamespace", //$NON-NLS-1$
                EmfModelUtils.computeTextElementValue("SERVICE_NAMESPACE", restRequestComponent)); //$NON-NLS-1$

        // correlation id
        endpointInfo.put("useBusinesCorrelation", //$NON-NLS-1$
                EmfModelUtils.computeCheckElementValue("USE_BUSINESS_CORRELATION", restRequestComponent)); //$NON-NLS-1$

        return endpointInfo;
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

    protected void generateConfig(ExportFileResource osgiResource, ProcessItem processItem, IProcess process) throws IOException {
        final File targetFile = new File(getTmpFolder() + PATH_SEPARATOR + "job.xml"); //$NON-NLS-1$
        // restJob
        if (null != getRESTRequestComponent(processItem)) {
            generateRestJobBlueprintConfig(processItem, process, targetFile);
        } else {
            createJobBundleBlueprintConfig(processItem, process, targetFile);
        }
        osgiResource.addResource(FileConstants.BLUEPRINT_FOLDER_NAME, targetFile.toURI().toURL());
    }

    private static final String TEMPLATE_BLUEPRINT_JOB_REST = "/resources/job-rest-template.xml"; //$NON-NLS-1$

    private void generateRestJobBlueprintConfig(ProcessItem processItem, IProcess process, File targetFile) throws IOException {
        // velocity template context
        Map<String, Object> contextParams = new HashMap<String, Object>();
        contextParams.put("endpoint", collectRestEndpointInfo(processItem)); //$NON-NLS-1$
        contextParams.put("job", collectJobInfo(processItem, process)); //$NON-NLS-1$

        TemplateProcessor.processTemplate("REST_JOB_BLUEPRINT_CONFIG", //$NON-NLS-1$
                contextParams, targetFile, JobJavaScriptOSGIForESBManager.class.getResourceAsStream(TEMPLATE_BLUEPRINT_JOB_REST));
    }

    private static final String TEMPLATE_BLUEPRINT_JOB = "/resources/job-template.xml"; //$NON-NLS-1$

    private void createJobBundleBlueprintConfig(ProcessItem processItem, IProcess process, File targetFile) throws IOException {
        TemplateProcessor.processTemplate("JOB_BLUEPRINT_CONFIG", //$NON-NLS-1$
                collectJobInfo(processItem, process), targetFile, JobJavaScriptOSGIForESBManager.class.getClassLoader()
                        .getResourceAsStream(TEMPLATE_BLUEPRINT_JOB));
    }

    private ExportFileResource genMetaInfoFolder(ExportFileResource libResource, ProcessItem processItem) throws IOException {

        ExportFileResource metaInfoResource = new ExportFileResource(null, FileConstants.META_INF_FOLDER_NAME);

        // generate the MANIFEST.MF file in the temp folder
        File manifestFile = new File(getTmpFolder() + PATH_SEPARATOR + FileConstants.MANIFEST_MF_FILE_NAME);

        FileOutputStream fos = null;
        try {
            Manifest manifest = getManifest(libResource, processItem);
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

    private Manifest getManifest(ExportFileResource libResource, ProcessItem processItem) throws IOException {

        Analyzer analyzer = createAnalyzer(libResource, processItem);

        Set<String> imports = null;

        if (GlobalServiceRegister.getDefault().isServiceRegistered(IRunProcessService.class)) {
            IRunProcessService service = (IRunProcessService) GlobalServiceRegister.getDefault()
                    .getService(IRunProcessService.class);
            ITalendProcessJavaProject talendProcessJavaProject = service.getTalendJobJavaProject(processItem.getProperty());
            if (talendProcessJavaProject != null) {
                imports = importCompiler(service, processItem);
                String[] defaultPackages = analyzer.getProperty(Analyzer.IMPORT_PACKAGE).split(",");
                // JDK upgrade to 11
                imports.add("org.osgi.framework");

                for (String dp : defaultPackages) {
                    if (!imports.contains(dp) && !imports.contains(dp + RESOLUTION_OPTIONAL)) {
                        imports.add(dp);
                    }
                }
                imports.remove("*;resolution:=optional");
                imports.remove("routines.system");
                imports.remove("routines.system" + RESOLUTION_OPTIONAL);

                if (!ENABLE_CACHE) {
                    // TESB-24730 set specific version for "javax.annotation"
                    imports.remove("javax.annotation");
                    imports.remove("javax.annotation" + RESOLUTION_OPTIONAL);
                    imports.add("javax.annotation;version=\"[1.3,2)\"" + RESOLUTION_OPTIONAL);
                }

                analyzer.setProperty(Analyzer.IMPORT_PACKAGE, String.join(",", imports) + ",*;resolution:=optional");
            }
        }

        // Calculate the manifest
        Manifest manifest = null;
        try {
            manifest = analyzer.calcManifest();
            filterPackagesCache(manifest, imports);

        } catch (IOException e) {
            throw e;
        } catch (Exception e) {
            ExceptionHandler.process(e);
        } finally {
            analyzer.close();
        }

        // In some cases of Java8, if user compiled some classes with newer(Java 11) JDK versions, the
        // Require-Capability will use the last version(Java 11)
        // https://github.com/bndtools/bnd/blob/master/biz.aQute.bndlib/src/aQute/bnd/osgi/Analyzer.java#L975
        if ("1.8".equals(complianceLevel)) { //$NON-NLS-1$
            String requireCapability = manifest.getMainAttributes().getValue(Analyzer.REQUIRE_CAPABILITY);
            if (StringUtils.isNotBlank(requireCapability)) {
                // set back to 1.8, version from:
                // https://github.com/bndtools/bnd/blob/master/biz.aQute.bndlib/src/aQute/bnd/osgi/Clazz.java#L141
                requireCapability = requireCapability.replace(Clazz.JAVA.OpenJDK9.getFilter(), Clazz.JAVA.OpenJDK8.getFilter())
                        .replace(Clazz.JAVA.OpenJDK10.getFilter(), Clazz.JAVA.OpenJDK8.getFilter())
                        .replace(Clazz.JAVA.OpenJDK11.getFilter(), Clazz.JAVA.OpenJDK8.getFilter());
                manifest.getMainAttributes().put(new Attributes.Name(Analyzer.REQUIRE_CAPABILITY), requireCapability);
            }
        }

        return manifest;
    }
    
    private boolean cacheManifest(URL url, String relativePath) {
        boolean result = false;
        ObjectOutputStream o = null;
        Analyzer al = new Analyzer();
        try {
            File jarFile = new File(url.toURI());

            try (InputStream is = RepositoryPlugin.getDefault().getBundle()
                    .getEntry("/resources/osgi-ignore-calmanifest.properties").openStream();
                    BufferedReader br = new BufferedReader(new InputStreamReader(is))) {

                Optional<String> rs = br.lines()
                        .filter(fn -> StringUtils.equals(jarFile.getName(), fn) || StringUtils.equals(fn, "*")).findFirst();
                result = rs.orElse(null) != null;
            }

            if (result) {

                String key = jarFile.length() + jarFile.getName();

                if (dependencyCacheMap == null || dependencyCacheMap.get(key) == null) {
                    Jar bin = new Jar(jarFile);
                    al.setJar(bin);
                    // al.setProperty(Analyzer.IMPORT_PACKAGE, "*;resolution:=optional");
                    // bin.putResource(relativePath, new FileResource(jarFile));
                    Manifest manifest = al.calcManifest();
                    String requireCapabilityString = manifest.getMainAttributes().getValue(Analyzer.REQUIRE_CAPABILITY);

                    // Todo Handle requireCapabilityString to the MANIFEST.MF file ?
//                    if (requireCapabilityString != null) {
//
//                    }
                    Domain d = Domain.domain(manifest);
                    Parameters imports = d.getImportPackage();
                    Parameters privates = d.getPrivatePackage();

                    String privatePackageString = manifest.getMainAttributes().getValue(Analyzer.PRIVATE_PACKAGE);
                    String importPackageString = manifest.getMainAttributes().getValue(Analyzer.IMPORT_PACKAGE);

                    if (StringUtils.isBlank(privatePackageString) || StringUtils.isBlank(importPackageString)) {
                        bundleClasspathKeys.remove(key);
                        return false;
                    }

                    List<Object> infos = new ArrayList<>();
                    infos.add(imports.keyList());
                    infos.add(privates.keyList());
                    infos.add(relativePath);

                    if (dependencyCacheMap == null) {
                        dependencyCacheMap = new HashMap<>();
                    }

                    dependencyCacheMap.put(key, infos);

                    if (cacheFile != null && cacheFile.exists()) {
                        FileOutputStream f = new FileOutputStream(cacheFile);
                        o = new ObjectOutputStream(f);
                        o.writeObject(dependencyCacheMap);
                    }
                } else {
                    if (dependencyCacheMap.get(key).get(0) == null || dependencyCacheMap.get(key).get(1) == null) {
                        dependencyCacheMap.remove(key);
                        bundleClasspathKeys.remove(key);
                    }

                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (o != null) {
                    o.close();
                }
                if (al != null) {
                    al.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return result;

    }

    private List<String> bundleClasspathKeys = new ArrayList<>();

    @SuppressWarnings("unchecked")
    private void filterPackagesCache(Manifest manifest, Set<String> imports) {

        if (!ENABLE_CACHE) {
            bundleClasspathKeys.clear();
        }

        Domain domain = Domain.domain(manifest);
        Parameters calculatedImports = domain.getImportPackage();
        Parameters calculatedPrivates = domain.getPrivatePackage();

        Set<String> privateNonRepetitivePackages = new HashSet<>(calculatedPrivates.keySet());

        Set<String> importNonRepetitivePackages = new HashSet<>(calculatedImports.keySet());

        importNonRepetitivePackages.addAll(imports);

        importNonRepetitivePackages = importNonRepetitivePackages.stream().map(v -> {
            if (!StringUtils.endsWith(v, RESOLUTION_OPTIONAL)) {
                return v + RESOLUTION_OPTIONAL;
            }
            return v;
        }).collect(Collectors.toSet());

        int size = bundleClasspathKeys.size();

        for (int i = 0; i < size; i++) {
            List<Object> infos = dependencyCacheMap.get(bundleClasspathKeys.get(i));
            if (infos == null) {
                continue;
            }
            List<String> parameters = (List<String>) infos.get(0);

            importNonRepetitivePackages.addAll(parameters.stream().map(v -> {
                if (!StringUtils.endsWith(v, RESOLUTION_OPTIONAL)) {
                    return v + RESOLUTION_OPTIONAL;
                }
                return v;
            }).collect(Collectors.toSet()));

            privateNonRepetitivePackages.addAll((List<String>) infos.get(1));
        }

        importNonRepetitivePackages.remove("routines.system");
        importNonRepetitivePackages.remove("routines.system" + RESOLUTION_OPTIONAL);

        // TESB-24730 set specific version for "javax.annotation"
        importNonRepetitivePackages.remove("javax.annotation");
        importNonRepetitivePackages.remove("javax.annotation" + RESOLUTION_OPTIONAL);
        importNonRepetitivePackages.add("javax.annotation;version=\"[1.3,2)\"" + RESOLUTION_OPTIONAL);
        
        // TESB-32507 make  org.talend.esb.authorization.xacml.rt.pep not optional
        if (importNonRepetitivePackages.contains("org.talend.esb.authorization.xacml.rt.pep" + RESOLUTION_OPTIONAL))  {
            importNonRepetitivePackages.remove("org.talend.esb.authorization.xacml.rt.pep" + RESOLUTION_OPTIONAL);
            importNonRepetitivePackages.add("org.talend.esb.authorization.xacml.rt.pep");
        }

        Set<String> fileterdImportPackage = new HashSet<>();

        for (String p : importNonRepetitivePackages) {
            if (!privateNonRepetitivePackages.contains(p.replace(RESOLUTION_OPTIONAL, "")) || p.startsWith("routines")) {
                fileterdImportPackage.add(p);
            }
        }

        manifest.getMainAttributes().putValue(Analyzer.PRIVATE_PACKAGE, String.join(",", privateNonRepetitivePackages));
        manifest.getMainAttributes().putValue(Analyzer.IMPORT_PACKAGE, String.join(",", fileterdImportPackage));
    }

    protected Analyzer createAnalyzer(ExportFileResource libResource, ProcessItem processItem) throws IOException {
        Analyzer analyzer = new Analyzer();
        Jar bin = new Jar(classesLocation);
        analyzer.setJar(bin);

        final String bundleName = processItem.getProperty().getLabel();
        String symbolicName = bundleName;
        // http://jira.talendforge.org/browse/TESB-5382 LiXiaopeng
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
        analyzer.setProperty(Analyzer.BUNDLE_VENDOR, brandingService.getFullProductName() + " (" //$NON-NLS-1$
                + brandingService.getAcronym() + '_' + RepositoryPlugin.getDefault().getBundle().getVersion().toString() + ")"); //$NON-NLS-1$

        addOsgiDependencies(analyzer, libResource, processItem);

        final StringBuilder bundleClasspath = new StringBuilder("."); //$NON-NLS-1$
        final StringBuilder bundleNativeCode = new StringBuilder();
        Set<String> relativePathList = libResource.getRelativePathList();
        for (String path : relativePathList) {
            Set<URL> resources = libResource.getResourcesByRelativePath(path);
            for (URL url : resources) {
                // TESB-21804:Fail to deploy cMessagingEndpoint with quartz component in runtime for ClassCastException
                String urlStr = url.getPath().replace("\\", "/");
                if (urlStr.matches("(.*)camel-(.*)-alldep-(.*)$") 
                        || urlStr.matches("(.*)activemq-all-[\\d\\.]*.jar$")
                        || urlStr.matches("(.*)/jms[\\d\\.-]*.jar$")
                        || urlStr.matches("(.*)tdm-lib-di-[\\d\\.-]*.jar$")
                        || urlStr.matches("(.*)dom4j-[\\d\\.-]*.jar$")) {
                    continue;
                }

                File dependencyFile = new File(FilesUtils.getFileRealPath(url.getPath()));
                String relativePath = libResource.getDirectoryName() + PATH_SEPARATOR + dependencyFile.getName();

                bundleClasspathKeys.add(dependencyFile.length() + dependencyFile.getName());
                bundleClasspath.append(MANIFEST_ITEM_SEPARATOR).append(relativePath);

                // analyzer.addClasspath(new File(url.getPath()));
                // Add dynamic library declaration in manifest
                if (relativePath.toLowerCase().endsWith(DLL_FILE) || relativePath.toLowerCase().endsWith(SO_FILE)) {
                    bundleNativeCode.append(libResource.getDirectoryName() + PATH_SEPARATOR + dependencyFile.getName()).append(
                            OSGI_OS_CODE);
                }

                // If don't want to enable this feature just comment out
                if (ENABLE_CACHE && cacheManifest(url, relativePath)) {
                    continue;
                }

                bin.putResource(relativePath, new FileResource(dependencyFile));
            }
        }
        analyzer.setProperty(Analyzer.BUNDLE_CLASSPATH, bundleClasspath.toString());

        // TESB-15680: Add Bundle-NativeCode in manifest
        if (bundleNativeCode.length() > 0) {
            bundleNativeCode.setLength(bundleNativeCode.length() - 1);
            analyzer.setProperty(Analyzer.BUNDLE_NATIVECODE, bundleNativeCode.toString());
        }

        return analyzer;
    }

    private boolean usesMssql(ProcessItem processItem) {
        IDesignerCoreService service = CorePlugin.getDefault().getDesignerCoreService();
        return service.getProcessFromProcessItem(processItem, false)
                .getNeededLibraries(TalendProcessOptionConstants.MODULES_DEFAULT).stream()
                .anyMatch(lib -> lib.matches("mssql-jdbc.jar"));
    }
    
    protected void addOsgiDependencies(Analyzer analyzer, ExportFileResource libResource, ProcessItem processItem)
            throws IOException {
        analyzer.setProperty(Analyzer.EXPORT_SERVICE, "routines.system.api.TalendJob;name=" + processItem.getProperty().getLabel() + ";type=" + "job");
        analyzer.setProperty(Analyzer.EXPORT_PACKAGE, getPackageName(processItem));

        final Collection<String> importPackages = new HashSet<String>();
        String requireBundle = null;

        NodeType restRequestComponent = getRESTRequestComponent(processItem);
        if (null != restRequestComponent) {
            importPackages.add("org.apache.cxf.metrics");
            
            if (EmfModelUtils.computeCheckElementValue("NEED_AUTH", restRequestComponent)) { //$NON-NLS-1$
                String authType = EmfModelUtils.computeTextElementValue("AUTH_TYPE", restRequestComponent); //$NON-NLS-1$
                if ("BASIC".equals(authType)) { //$NON-NLS-1$
                    importPackages.add("org.apache.cxf.jaxrs.security"); //$NON-NLS-1$
                } else if ("SAML".equals(authType)) { //$NON-NLS-1$
                    importPackages.add("org.apache.cxf.interceptor.security"); //$NON-NLS-1$
                    importPackages.add("org.apache.cxf.rs.security.saml"); //$NON-NLS-1$
                    if (EmfModelUtils.computeCheckElementValue("NEED_AUTHORIZATION", restRequestComponent)) { //$NON-NLS-1$
                        importPackages.add("org.talend.esb.authorization.xacml.rt.pep"); //$NON-NLS-1$
                    }
                }
            }
        }
        
        //https://jira.talendforge.org/browse/APPINT-32593
        NodeType mdmComponent = getMDMComponent(processItem);
        if (null != mdmComponent) {
            importPackages.add("org.apache.cxf.message");
        }

        for (NodeType node : EmfModelUtils.getComponentsByName(processItem, "tESBConsumer")) { //$NON-NLS-1$
            // https://jira.talendforge.org/browse/TESB-9574
            if (requireBundle == null && EmfModelUtils.computeCheckElementValue("USE_SR", node)) { //$NON-NLS-1$
                requireBundle = "tesb-xacml-rt"; //$NON-NLS-1$
            }
        }

        // https://jira.talendforge.org/browse/APPINT-34077
        if (ERepositoryObjectType.getType(processItem.getProperty()).equals(ERepositoryObjectType.PROCESS)) {
            for (JobInfo subjobInfo : ProcessorUtilities.getChildrenJobInfo(processItem)) {
                if (EmfModelUtils.getComponentByName(subjobInfo.getProcessItem(), "tESBConsumer") != null) { //$NON-NLS-1$
                    importPackages.add("org.apache.cxf.databinding"); //$NON-NLS-1$
                    break;
                }
            }
        }

        // https://jira.talendforge.org/browse/APPINT-34443
        NodeType restClientComponent = getRESTClientComponent(processItem);
        if (null != restClientComponent) {
            importPackages.add("org.apache.cxf.endpoint");
            importPackages.add("org.apache.cxf.service.model");
        }

        if (ERepositoryObjectType.PROCESS_MR == ERepositoryObjectType.getItemType(processItem)) {
            importPackages.add("org.talend.cloud"); //$NON-NLS-1$
        }

        if(usesMssql(processItem)) {
            importPackages.add("com.microsoft.sqlserver.jdbc");
        }
    
        if (requireBundle != null && !requireBundle.isEmpty()) {
            requireBundle += (", org.ops4j.pax.logging.pax-logging-api");
        } else {
            requireBundle = "org.ops4j.pax.logging.pax-logging-api";
        }

        if (requireBundle != null) {
            analyzer.setProperty(Analyzer.REQUIRE_BUNDLE, requireBundle);
        }

        StringBuilder importPackage = new StringBuilder("routines.system.api,"); //$NON-NLS-1$
        for (String ip : importPackages) {
            importPackage.append(ip).append(',');
        }
        importPackage.append("*;resolution:=optional"); //$NON-NLS-1$
        analyzer.setProperty(Analyzer.IMPORT_PACKAGE, importPackage.toString());
    }

    protected static boolean isStudioEEVersion() {
        return PluginChecker.isTIS();
    }

    @Override
    protected List<URL> getExternalLibraries(boolean needLibraries, ExportFileResource[] process, Set<String> neededLibraries) {
        if (!needLibraries) {
            return Collections.emptyList();
        }
        // Lists all the needed jar files
        final Set<String> listModulesReallyNeeded = new HashSet<String>(neededLibraries);

        // jar from routines
        for (IRepositoryViewObject object : collectRoutines(process, getIncludeRoutinesPath())) {
            Item item = object.getProperty().getItem();
            if (item instanceof RoutineItem) {
                @SuppressWarnings("unchecked")
                List<IMPORTType> imports = ((RoutineItem) item).getImports();
                for (IMPORTType type : imports) {
                    if (type.isREQUIRED()) {
                        listModulesReallyNeeded.add(type.getMODULE());
                    }
                }
            }
        }

        return getNeededModuleURLs(listModulesReallyNeeded);
    }

    protected List<URL> getNeededModuleURLs(Set<String> neededModules) {
        List<URL> list = new ArrayList<URL>();
        if (GlobalServiceRegister.getDefault().isServiceRegistered(IRunProcessService.class)) {
            IRunProcessService processService = (IRunProcessService) GlobalServiceRegister.getDefault().getService(
                    IRunProcessService.class);
            IFolder libFolder = processService.getJavaProjectLibFolder();
            File file = libFolder.getLocation().toFile();
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
    public Set<ModuleNeeded> getExcludedModuleNeededs() {
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

    @Override
    protected IProcess generateJobFiles(ProcessItem process, String contextName, String version, boolean statistics,
            boolean trace, boolean applyContextToChildren, IProgressMonitor monitor) throws ProcessorException {
        IDesignerCoreService service = CorePlugin.getDefault().getDesignerCoreService();
        IProcess currentProcess = service.getProcessFromProcessItem(process);
        IProcessor processor = ProcessorUtilities.getProcessor(currentProcess, process.getProperty());
        return processor.getProcess();
    }

    private Set<String> importCompiler(IRunProcessService service, ProcessItem processItem) {

        Set<String> imports = new HashSet<String>();
        if (processItem != null && service != null && processItem.getProperty() != null) {
            ITalendProcessJavaProject talendProcessJavaProject = service.getTalendJobJavaProject(processItem.getProperty());
            if (talendProcessJavaProject != null) {
                String src = JavaResourcesHelper.getJobClassFilePath(processItem, true);
                IFile srcFile = talendProcessJavaProject.getSrcFolder().getFile(src);
                imports.addAll(importCompiler(srcFile.getLocation().toString()));
                // include imports from child jobs
                if (ERepositoryObjectType.getType(processItem.getProperty()).equals(ERepositoryObjectType.PROCESS)) {
                    for (JobInfo subjobInfo : ProcessorUtilities.getChildrenJobInfo(processItem)) {
                        imports.addAll(importCompiler(service, subjobInfo.getProcessItem()));
                    }
                }
            }
        }
        return imports;
    }
    
    private Set<String> importCompiler(String src) {
        Set<String> imports = new HashSet<String>();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayOutputStream err = new ByteArrayOutputStream();
        try {
            Compiler c = new Compiler(new PrintWriter(out), new PrintWriter(err));
            c.compile(Locale.forLanguageTag("und"), c.tokenize(src.concat(complianceParameter)));
           
            String errString = new String(err.toByteArray());
            String[] errBlocks = errString.split(COMPILER_LOG_DELIMITER);
            String reg = COMPILER_LOG_REGEX;
            Pattern pattern = Pattern.compile(reg);
            for (String errBlock : errBlocks) {
                String[] lines = errBlock.trim().replaceAll("\r", "").split("\n");
                if (lines.length == 4) {
                    if (lines[3].endsWith(COMPILER_LOG_ERROR_1) || lines[3].endsWith(COMPILER_LOG_ERROR_2) || lines[3].endsWith(COMPILER_LOG_ERROR_3)) {
                        int markerPos = lines[2].indexOf('^');
                        Matcher m = pattern.matcher(lines[1].substring(markerPos));
                        if (m.find()) {
                            if (m.groupCount() == 1 && m.group(1).indexOf('.') > 0) {
                                imports.add(m.group(1) + RESOLUTION_OPTIONAL);
                            }
                        }
                    }
                }
            }
            out.close();
            err.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return imports;
    }
    
    public Map<String, String> getNameMavenUriMap() {
        Map<String, String> map = new HashMap<String, String>();

        for (ModuleNeeded module : getCompiledModuleNeededs()) {
            map.put(module.getModuleName(), module.getMavenUri());
        }
        return map;
    }
    
    @SuppressWarnings("restriction")
    private class Compiler extends org.eclipse.jdt.internal.compiler.batch.Main {

        public boolean compile(Locale locale, String[] commandLineArguments) {
            relocalize(locale);
            return super.compile(commandLineArguments);
    	}
    	
        private void relocalize(Locale locale) {
            this.compilerLocale = locale;
            try {
                this.bundle = ResourceBundleFactory.getBundle(locale);
	        } catch(MissingResourceException e) {
	            System.out.println("Missing resource : " + Main.bundleName.replace('.', '/') + ".properties for locale " + locale); //$NON-NLS-1$//$NON-NLS-2$
	            throw e;
	        }
        }
    	
    	public Compiler (PrintWriter outWriter, PrintWriter errWriter) {
            super(outWriter, errWriter, false, null, null);
    	}
    }
}
