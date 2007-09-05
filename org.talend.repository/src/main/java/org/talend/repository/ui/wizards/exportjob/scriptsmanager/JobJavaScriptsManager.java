// ============================================================================
//
// Talend Community Edition
//
// Copyright (C) 2006-2007 Talend - www.talend.com
//
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either
// version 2.1 of the License.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
// Lesser General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with this program; if not, write to the Free Software
// Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
//
// ============================================================================
package org.talend.repository.ui.wizards.exportjob.scriptsmanager;

import java.io.File;
import java.io.FilenameFilter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.BooleanUtils;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.emf.common.util.EList;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.core.CorePlugin;
import org.talend.core.language.ECodeLanguage;
import org.talend.core.model.general.ILibrariesService;
import org.talend.core.model.general.ModuleNeeded;
import org.talend.core.model.process.IProcess;
import org.talend.core.model.properties.ProcessItem;
import org.talend.designer.core.IDesignerCoreService;
import org.talend.designer.core.model.utils.emf.talendfile.ContextType;
import org.talend.designer.core.model.utils.emf.talendfile.JobType;
import org.talend.designer.runprocess.IProcessor;
import org.talend.designer.runprocess.ProcessorUtilities;
import org.talend.librariesmanager.model.ModulesNeededProvider;
import org.talend.repository.RepositoryPlugin;
import org.talend.repository.ui.utils.JavaResourcesHelper;
import org.talend.repository.ui.wizards.exportjob.ExportFileResource;

/**
 * Manages the job scripts to be exported. <br/>
 * 
 * $Id: JobScriptsManager.java 1 2006-12-14 下午05:06:49 bqian
 * 
 */
public class JobJavaScriptsManager extends JobScriptsManager {

    private static final String USER_ROUTINES_PATH = "routines";

    private static final String SYSTEM_ROUTINES_PATH = "routines/system";

    public static final String JOB_CONTEXT_FOLDER = "contexts";

    protected static final String SYSTEMROUTINE_JAR = "systemRoutines.jar";

    protected static final String USERROUTINE_JAR = "userRoutines.jar";

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.ui.wizards.exportjob.JobScriptsManager#getExportResources(org.talend.core.model.properties.ProcessItem[],
     * boolean, boolean, boolean, boolean, boolean, boolean, boolean, java.lang.String)
     */
    @Override
    public List<ExportFileResource> getExportResources(ExportFileResource[] process,
            Map<ExportChoice, Boolean> exportChoice, String contextName, String launcher, int statisticPort,
            int tracePort, String... codeOptions) {

        for (int i = 0; i < process.length; i++) {
            ProcessItem processItem = process[i].getProcess();

            String libPath = calculateLibraryPathFromDirectory(process[i].getDirectoryName());
            // use character @ as temporary classpath separator, this one will be replaced during the export.
            String standardJars = libPath + PATH_SEPARATOR + SYSTEMROUTINE_JAR
                    + ProcessorUtilities.TEMP_JAVA_CLASSPATH_SEPARATOR + libPath + PATH_SEPARATOR + USERROUTINE_JAR
                    + ProcessorUtilities.TEMP_JAVA_CLASSPATH_SEPARATOR + ".";
            ProcessorUtilities.setExportConfig("java", standardJars, libPath);

            generateJobFiles(processItem, contextName, statisticPort != IProcessor.NO_STATISTICS,
                    tracePort != IProcessor.NO_TRACES, BooleanUtils.isTrue(exportChoice
                            .get(ExportChoice.applyToChildren)));
            List<URL> resources = new ArrayList<URL>();
            resources.addAll(getLauncher(BooleanUtils.isTrue(exportChoice.get(ExportChoice.needLauncher)), processItem,
                    escapeSpace(contextName), escapeSpace(launcher), statisticPort, tracePort, codeOptions));

            List<URL> srcList = getSource(processItem, BooleanUtils.isTrue(exportChoice.get(ExportChoice.needSource)));
            process[i].addResources(JOB_SOURCE_FOLDER_NAME, srcList);

            resources.addAll(getJobScripts(processItem, BooleanUtils.isTrue(exportChoice.get(ExportChoice.needJob))));

            addContextScripts(process[i], BooleanUtils.isTrue(exportChoice.get(ExportChoice.needContext)));

            // add children jobs
            boolean needChildren = BooleanUtils.isTrue(exportChoice.get(ExportChoice.needJob))
                    && BooleanUtils.isTrue(exportChoice.get(ExportChoice.needContext));
            List<URL> childrenList = addChildrenResources(processItem, needChildren, process[i], exportChoice);
            resources.addAll(childrenList);
            process[i].addResources(resources);

            // Gets job designer resouce
            // List<URL> srcList = getSource(processItem, exportChoice.get(ExportChoice.needSource));
            // process[i].addResources(JOB_SOURCE_FOLDER_NAME, srcList);
        }

        // Exports the system libs
        List<ExportFileResource> list = new ArrayList<ExportFileResource>(Arrays.asList(process));
        // Add the java system libraries
        ExportFileResource rootResource = new ExportFileResource(null, LIBRARY_FOLDER_NAME);
        list.add(rootResource);
        // Gets system routines
        List<URL> systemRoutineList = getSystemRoutine(BooleanUtils.isTrue(exportChoice
                .get(ExportChoice.needSystemRoutine)));
        rootResource.addResources(systemRoutineList);
        // Gets user routines
        List<URL> userRoutineList = getUserRoutine(BooleanUtils.isTrue(exportChoice.get(ExportChoice.needUserRoutine)));
        rootResource.addResources(userRoutineList);

        // Gets talend libraries
        List<URL> talendLibraries = getExternalLibraries(BooleanUtils.isTrue(exportChoice
                .get(ExportChoice.needTalendLibraries)), process);
        rootResource.addResources(talendLibraries);

        return list;
    }

    /**
     * DOC acer Comment method "addContextScripts".
     * 
     * @param resource
     * @param boolean1
     */
    protected void addContextScripts(ExportFileResource resource, Boolean needContext) {
        addContextScripts(escapeFileNameSpace(resource.getProcess()), resource, needContext);
    }

    /**
     * DOC acer Comment method "addContextScripts".
     * 
     * @param resource
     * @param boolean1
     */
    protected void addContextScripts(String jobName, ExportFileResource resource, Boolean needContext) {
        if (!needContext) {
            return;
        }
        List<URL> list = new ArrayList<URL>(1);
        String projectName = getCurrentProjectName();
        jobName = JavaResourcesHelper.getJobFolderName(jobName);
        try {
            IPath classRoot = getClassRootPath();
            classRoot = classRoot.append(projectName).append(jobName).append(JOB_CONTEXT_FOLDER);
            File contextDir = classRoot.toFile();
            if (contextDir.isDirectory()) {
                for (File contextFile : classRoot.toFile().listFiles()) {
                    list.add(contextFile.toURL());
                }
            }

            // list.add(classRoot.toFile().toURL());

            String jobPackagePath = projectName + PATH_SEPARATOR + jobName + PATH_SEPARATOR + JOB_CONTEXT_FOLDER;
            resource.addResources(jobPackagePath, list);
        } catch (Exception e) {
            ExceptionHandler.process(e);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.ui.wizards.exportjob.scriptsmanager.JobScriptsManager#getSource(org.talend.core.model.properties.ProcessItem,
     * boolean)
     */
    @Override
    protected List<URL> getSource(ProcessItem processItem, boolean needSource) {
        List<URL> urls = super.getSource(processItem, needSource);
        if (!needSource) {
            return urls;
        }
        // Get java src
        try {
            String projectName = getCurrentProjectName();
            String jobName = processItem.getProperty().getLabel();
            String jobFolderName = JavaResourcesHelper.getJobFolderName(escapeFileNameSpace(processItem));

            IPath path = getSrcRootLocation();
            path = path.append(projectName).append(jobFolderName).append(jobName + ".java");
            urls.add(FileLocator.toFileURL(path.toFile().toURL()));
        } catch (Exception e) {
            ExceptionHandler.process(e);
        }
        return urls;
    }

    protected String calculateLibraryPathFromDirectory(String directory) {
        int nb = directory.split(PATH_SEPARATOR).length - 1;
        String path = "../";
        for (int i = 0; i < nb; i++) {
            path = path.concat("../");
        }
        return path + LIBRARY_FOLDER_NAME;
    }

    private List<URL> addChildrenResources(ProcessItem process, boolean needChildren, ExportFileResource resource,
            Map<ExportChoice, Boolean> exportChoice) {
        List<String> list = new ArrayList<String>();
        if (needChildren) {
            String projectName = getCurrentProjectName();
            try {
                List<ProcessItem> processedJob = new ArrayList<ProcessItem>();
                getChildrenJobAndContextName(process.getProperty().getLabel(), list, process, projectName,
                        processedJob, resource, exportChoice);
            } catch (Exception e) {
                ExceptionHandler.process(e);
            }

        }

        List<URL> allJobScripts = new ArrayList<URL>();
        for (Iterator<String> iter = list.iterator(); iter.hasNext();) {
            String jobName = iter.next();
            allJobScripts.addAll(getJobScripts(jobName, exportChoice.get(ExportChoice.needJob)));
            addContextScripts(jobName, resource, exportChoice.get(ExportChoice.needContext));
        }

        return allJobScripts;
    }

    private void getChildrenJobAndContextName(String rootName, List<String> list, ProcessItem process,
            String projectName, List<ProcessItem> processedJob, ExportFileResource resource,
            Map<ExportChoice, Boolean> exportChoice) {
        if (processedJob.contains(process)) {
            // prevent circle
            return;
        }
        processedJob.add(process);
        // addComponentModules(process, resource);
        List<URL> srcList = getSource(process, exportChoice.get(ExportChoice.needSource));
        resource.addResources(JOB_SOURCE_FOLDER_NAME, srcList);
        if (process.getProcess().getRequired() == null) {
            return;
        }
        EList jobList = process.getProcess().getRequired().getJob();
        for (int j = 0; j < jobList.size(); j++) {
            JobType jType = (JobType) jobList.get(j);
            String processLabel = jType.getName();
            if (processLabel.equals(rootName)) {
                continue;
            }

            String jobName = escapeSpace(processLabel);

            addToList(list, jobName);

            ProcessItem childProcess = findProcess(processLabel);
            if (childProcess == null) {
                return;
            }
            getChildrenJobAndContextName(rootName, list, childProcess, projectName, processedJob, resource,
                    exportChoice);
        }
    }

    /**
     * Gets required java jars.
     * 
     * @param process
     * 
     * @param boolean1
     * @return
     */
    protected List<URL> getExternalLibraries(boolean needLibraries, ExportFileResource[] process) {
        List<URL> list = new ArrayList<URL>();
        if (!needLibraries) {
            return list;
        }
        ILibrariesService librariesService = CorePlugin.getDefault().getLibrariesService();
        String path = librariesService.getLibrariesPath();
        // Gets all the jar files
        File file = new File(path);
        File[] files = file.listFiles(new FilenameFilter() {

            public boolean accept(File dir, String name) {
                return name.toLowerCase().endsWith(".jar") || name.toLowerCase().endsWith(".properties")
                        || name.toLowerCase().endsWith(".zip") ? true : false;
            }
        });
        // Lists all the needed jar files
        Set<String> listModulesReallyNeeded = new HashSet<String>();
        IDesignerCoreService designerService = RepositoryPlugin.getDefault().getDesignerCoreService();
        for (int i = 0; i < process.length; i++) {
            ExportFileResource resource = process[i];
            IProcess iProcess = designerService.getProcessFromProcessItem(resource.getProcess());
            listModulesReallyNeeded.addAll(iProcess.getNeededLibraries(true));
        }

        for (ModuleNeeded moduleNeeded : ModulesNeededProvider.getModulesNeededForRoutines()) {
            listModulesReallyNeeded.add(moduleNeeded.getModuleName());
        }

        for (int i = 0; i < files.length; i++) {
            File tempFile = files[i];
            try {
                if (listModulesReallyNeeded.contains(tempFile.getName())) {
                    list.add(tempFile.toURL());
                }
            } catch (MalformedURLException e) {
                ExceptionHandler.process(e);
            }
        }

        return list;
        // List<URL> libraries = new ArrayList<URL>();
        // if (needLibraries) {
        // try {
        // ILibrariesService service = CorePlugin.getDefault().getLibrariesService();
        // libraries = service.getTalendRoutines();
        // } catch (Exception e) {
        // ExceptionHandler.process(e);
        // }
        // }
        // return libraries;
    }

    /**
     * Gets Job Scripts.
     * 
     * @param process
     * @param needJob
     * @param needContext
     * @return
     */
    protected List<URL> getJobScripts(ProcessItem process, boolean needJob) {
        return this.getJobScripts(escapeFileNameSpace(process), needJob);
    }

    /**
     * Gets Job Scripts.
     * 
     * @param process
     * @param needJob
     * @param needContext
     * @return
     */
    private List<URL> getJobScripts(String jobName, boolean needJob) {
        List<URL> list = new ArrayList<URL>(1);
        if (!needJob) {
            return list;
        }
        String projectName = getCurrentProjectName();
        jobName = JavaResourcesHelper.getJobFolderName(jobName);

        try {
            String classRoot = getClassRootLocation();
            String jarPath = getTmpFolder() + PATH_SEPARATOR + jobName + ".jar";
            // Exports the jar file
            JarBuilder jarbuilder = new JarBuilder(classRoot, jarPath);

            // builds the jar file of the job classes,needContext specifies whether inclucdes the context.
            // add the job
            String jobPath = projectName + PATH_SEPARATOR + jobName;
            List<String> include = new ArrayList<String>();
            include.add(jobPath);
            jarbuilder.setIncludeDir(include);
            // filter the context
            String contextPaht = jobPath + PATH_SEPARATOR + JOB_CONTEXT_FOLDER;
            List<String> excludes = new ArrayList<String>(1);
            excludes.add(contextPaht);
            jarbuilder.setExcludeDir(excludes);

            jarbuilder.buildJar();

            File jarFile = new File(jarPath);
            URL url = jarFile.toURL();
            list.add(url);
        } catch (Exception e) {
            ExceptionHandler.process(e);
        }
        return list;
    }

    /**
     * Gets all the perl files in the project .Perl.
     * 
     * @param name
     * @param projectName
     * 
     * @return
     */
    protected String getClassRootLocation() throws Exception {
        IPath binPath = getClassRootPath();
        URL url = binPath.toFile().toURL();
        return url.getPath();
    }

    private IPath getClassRootPath() throws Exception {
        IProject project = RepositoryPlugin.getDefault().getRunProcessService().getProject(ECodeLanguage.JAVA);

        IJavaProject javaProject = JavaCore.create(project);
        IPath binPath = javaProject.getOutputLocation();

        IPath root = project.getParent().getLocation();
        binPath = root.append(binPath);
        return binPath;
    }

    /**
     * Get the path of .JAVA/src
     * 
     * @throws Exception
     */
    protected IPath getSrcRootLocation() throws Exception {
        IProject project = RepositoryPlugin.getDefault().getRunProcessService().getProject(ECodeLanguage.JAVA);

        IJavaProject javaProject = JavaCore.create(project);
        IPackageFragmentRoot[] pp = javaProject.getAllPackageFragmentRoots();
        IPackageFragmentRoot src = null;
        for (IPackageFragmentRoot root : pp) {
            if (root.getKind() == IPackageFragmentRoot.K_SOURCE) {
                src = root;
                break;
            }
        }

        IPath root = project.getParent().getLocation();
        root = root.append(src.getPath());
        return root;
    }

    /**
     * Gets current project name.
     * 
     * @return
     */
    protected String getCurrentProjectName() {
        return JavaResourcesHelper.getCurrentProjectName();
    }

    /**
     * Gets system routine.
     * 
     * @param needSystemRoutine
     * @return
     */
    protected List<URL> getSystemRoutine(boolean needSystemRoutine) {
        List<URL> list = new ArrayList<URL>();
        if (!needSystemRoutine) {
            return list;
        }
        try {
            String classRoot = getClassRootLocation();
            List<String> include = new ArrayList<String>();
            include.add(SYSTEM_ROUTINES_PATH);

            String jarPath = getTmpFolder() + PATH_SEPARATOR + SYSTEMROUTINE_JAR;

            // make a jar file of system routine classes
            JarBuilder jarbuilder = new JarBuilder(classRoot, jarPath);
            jarbuilder.setIncludeDir(include);

            jarbuilder.buildJar();

            File jarFile = new File(jarPath);
            URL url = jarFile.toURL();
            list.add(url);
        } catch (Exception e) {
            ExceptionHandler.process(e);
        }
        return list;
    }

    /**
     * Gets system routine.
     * 
     * @param needSystemRoutine
     * @return
     */
    protected List<URL> getUserRoutine(boolean needUserRoutine) {
        List<URL> list = new ArrayList<URL>();
        if (!needUserRoutine) {
            return list;
        }
        try {
            String classRoot = getClassRootLocation();
            List<String> include = new ArrayList<String>();
            include.add(USER_ROUTINES_PATH);

            List<String> excludes = new ArrayList<String>();
            excludes.add(SYSTEM_ROUTINES_PATH);

            String jarPath = getTmpFolder() + PATH_SEPARATOR + USERROUTINE_JAR;

            // make a jar file of system routine classes
            JarBuilder jarbuilder = new JarBuilder(classRoot, jarPath);
            jarbuilder.setIncludeDir(include);
            jarbuilder.setExcludeDir(excludes);

            jarbuilder.buildJar();

            File jarFile = new File(jarPath);
            URL url = jarFile.toURL();
            list.add(url);
        } catch (Exception e) {
            ExceptionHandler.process(e);
        }
        return list;
    }

    /**
     * 
     * Gets the set of current job's context.
     * 
     * @return a List of context names.
     * 
     */
    public List<String> getJobContexts(ProcessItem processItem) {
        List<String> contextNameList = new ArrayList<String>();
        for (Object o : processItem.getProcess().getContext()) {
            if (o instanceof ContextType) {
                ContextType context = (ContextType) o;
                if (contextNameList.contains(context.getName())) {
                    continue;
                }
                contextNameList.add(context.getName());
            }
        }
        return contextNameList;
    }
}
