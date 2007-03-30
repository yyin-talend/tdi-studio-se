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
package org.talend.repository.ui.wizards.exportjob;

import java.io.File;
import java.io.FilenameFilter;
import java.io.PipedReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.utils.generation.JavaUtils;
import org.talend.commons.utils.io.FilesUtils;
import org.talend.core.CorePlugin;
import org.talend.core.context.Context;
import org.talend.core.context.RepositoryContext;
import org.talend.core.language.ECodeLanguage;
import org.talend.core.model.general.ILibrariesService;
import org.talend.core.model.general.Project;
import org.talend.core.model.properties.ProcessItem;
import org.talend.designer.core.model.utils.emf.talendfile.ContextType;
import org.talend.designer.runprocess.ProcessorException;
import org.talend.designer.runprocess.ProcessorUtilities;
import org.talend.repository.RepositoryPlugin;
import org.talend.repository.ui.wizards.exportjob.JobScriptsManager.ExportChoice;

/**
 * Manages the job scripts to be exported. <br/>
 * 
 * $Id: JobScriptsManager.java 1 2006-12-14 下午05:06:49 bqian
 * 
 */
public class JobJavaScriptsManager extends JobScriptsManager {

    /**
     * 
     */
    private static final String USER_ROUTINES_PATH = "routines";

    /**
     * 
     */
    private static final String SYSTEM_ROUTINES_PATH = "routines/system";

    private static final String JOB_CONTEXT_FOLDER = "contexts";

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.ui.wizards.exportjob.JobScriptsManager#getExportResources(org.talend.core.model.properties.ProcessItem[],
     * boolean, boolean, boolean, boolean, boolean, boolean, boolean, java.lang.String)
     */
    @Override
    public List<ExportFileResource> getExportResources(ExportFileResource[] process, Map<ExportChoice, Boolean> exportChoice,
            String contextName, String launcher) {

        ProcessorUtilities.setExportConfig("java", "", LIBRARY_FOLDER_NAME);

        for (int i = 0; i < process.length; i++) {
            ProcessItem processItem = process[i].getProcess();

            generateJobFiles(processItem, contextName);
            List<URL> resources = new ArrayList<URL>();
            resources.addAll(getLauncher(exportChoice.get(ExportChoice.needLauncher), processItem, escapeSpace(contextName),
                    escapeSpace(launcher)));

            resources.addAll(getJobScripts(processItem, exportChoice.get(ExportChoice.needJob), exportChoice
                    .get(ExportChoice.needContext)));
            // boolean needChildren = exportChoice.get(ExportChoice.needJob) &&
            // exportChoice.get(ExportChoice.needContext);
            // addChildrenResources(processItem, needChildren, process[i], exportChoice);
            process[i].addResources(resources);

            // Gets job designer resouce
            List<URL> srcList = getSource(processItem, exportChoice.get(ExportChoice.needSource));
            process[i].addResources(JOB_SOURCE_FOLDER_NAME, srcList);
        }

        // Exports the system libs
        List<ExportFileResource> list = new ArrayList<ExportFileResource>(Arrays.asList(process));
        // Add the java system libraries
        ExportFileResource rootResource = new ExportFileResource(null, LIBRARY_FOLDER_NAME);
        list.add(rootResource);
        // Gets system routines
        List<URL> systemRoutineList = getSystemRoutine(exportChoice.get(ExportChoice.needSystemRoutine));
        rootResource.addResources(systemRoutineList);
        // Gets user routines
        List<URL> userRoutineList = getUserRoutine(exportChoice.get(ExportChoice.needUserRoutine));
        rootResource.addResources(userRoutineList);

        // Gets talend libraries
        List<URL> talendLibraries = getExternalLibraries(exportChoice.get(ExportChoice.needTalendLibraries));
        rootResource.addResources(talendLibraries);

        return list;
    }

    /**
     * Gets required java jars.
     * 
     * @param boolean1
     * @return
     */
    private List<URL> getExternalLibraries(boolean needLibraries) {
        List<URL> list = new ArrayList<URL>();
        if (!needLibraries) {
            return list;
        }
        ILibrariesService librariesService = CorePlugin.getDefault().getLibrariesService();
        String path = librariesService.getLibrariesPath();
        File file = new File(path);
        // Lists all the jar files
        File[] files = file.listFiles(new FilenameFilter() {

            public boolean accept(File dir, String name) {
                return name.toLowerCase().endsWith(".jar") ? true : false;
            }
        });
        for (int i = 0; i < files.length; i++) {
            File tempFile = files[i];
            try {
                list.add(tempFile.toURL());
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
    private List<URL> getJobScripts(ProcessItem process, boolean needJob, boolean needContext) {

        List<URL> list = new ArrayList<URL>(1);
        if (!(needJob || needContext)) {
            return list;
        }
        String projectName = getCurrentProjectName();
        String jobName = escapeFileNameSpace(process);

        try {
            String classRoot = getClassRootLocation();
            String jarPath = getTmpFolder() + File.separatorChar + jobName + ".jar";
            // Exports the jar file
            JarBuilder jarbuilder = new JarBuilder(classRoot, jarPath);

            // builds the jar file of the job classes,needContext specifies whether inclucdes the context.
            if (needJob) {
                String jobPath = projectName + File.separatorChar + jobName;
                String contextPaht = jobPath + File.separatorChar + JOB_CONTEXT_FOLDER;
                List<String> include = new ArrayList<String>();
                include.add(jobPath);
                jarbuilder.setIncludeDir(include);
                if (!needContext) {
                    List<String> excludes = new ArrayList<String>(1);
                    excludes.add(contextPaht);
                    jarbuilder.setExcludeDir(excludes);
                }
            } else {
                String jobPath = projectName + File.separatorChar + jobName;
                String contextPaht = jobPath + File.separatorChar + JOB_CONTEXT_FOLDER;
                List<String> include = new ArrayList<String>();
                include.add(contextPaht);
                jarbuilder.setIncludeDir(include);
            }

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
    private String getClassRootLocation() throws Exception {
        IProject project = RepositoryPlugin.getDefault().getRunProcessService().getProject(ECodeLanguage.JAVA);

        IJavaProject javaProject = JavaCore.create(project);
        IPath binPath = javaProject.getOutputLocation();

        IPath root = project.getParent().getLocation();

        binPath = root.append(binPath);

        URL url = binPath.toFile().toURL();
        return url.getPath();
    }

    /**
     * Gets current project name.
     * 
     * @return
     */
    private String getCurrentProjectName() {
        RepositoryContext repositoryContext = (RepositoryContext) CorePlugin.getContext().getProperty(
                Context.REPOSITORY_CONTEXT_KEY);
        Project project = repositoryContext.getProject();

        String name = project.getLabel();
        name = name.replaceAll(" ", "_"); //$NON-NLS-1$ //$NON-NLS-2$
        name = name.toLowerCase();

        return name;
    }

    /**
     * Gets system routine.
     * 
     * @param needSystemRoutine
     * @return
     */
    private List<URL> getSystemRoutine(boolean needSystemRoutine) {
        List<URL> list = new ArrayList<URL>();
        if (!needSystemRoutine) {
            return list;
        }
        try {
            String classRoot = getClassRootLocation();
            List<String> include = new ArrayList<String>();
            include.add(SYSTEM_ROUTINES_PATH);

            String jarPath = getTmpFolder() + File.separatorChar + "systemRoutines.jar";

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
    private List<URL> getUserRoutine(boolean needUserRoutine) {
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

            String jarPath = getTmpFolder() + File.separatorChar + "userRoutines.jar";

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
