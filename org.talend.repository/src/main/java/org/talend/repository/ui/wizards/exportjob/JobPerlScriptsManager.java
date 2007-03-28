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
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.common.util.EList;
import org.eclipse.jface.preference.IPreferenceStore;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.core.CorePlugin;
import org.talend.core.context.Context;
import org.talend.core.context.RepositoryContext;
import org.talend.core.language.ECodeLanguage;
import org.talend.core.model.general.ILibrariesService;
import org.talend.core.model.general.Project;
import org.talend.core.model.properties.ProcessItem;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.prefs.ITalendCorePrefConstants;
import org.talend.designer.core.model.utils.emf.talendfile.ContextType;
import org.talend.designer.core.model.utils.emf.talendfile.JobType;
import org.talend.designer.core.model.utils.emf.talendfile.NodeType;
import org.talend.designer.runprocess.ProcessorException;
import org.talend.designer.runprocess.ProcessorUtilities;
import org.talend.repository.RepositoryPlugin;
import org.talend.repository.i18n.Messages;
import org.talend.repository.model.ResourceModelUtils;
import org.talend.repository.ui.wizards.exportjob.JobScriptsExportWizardPage.ExportChoice;

/**
 * Manages the job scripts to be exported. <br/>
 * 
 * $Id: JobScriptsManager.java 1 2006-12-14 下午05:06:49 ftang
 * 
 */
public class JobPerlScriptsManager extends JobScriptsManager {

    /**
     * 
     */
    private static final String LIBRARY_FOLDER_NAME = "lib"; //$NON-NLS-1$

    private static final String ALL_PERL_INTERPRETERS = Messages.getString("JobPerlScriptsManager.allInterpreter"); //$NON-NLS-1$

    private static final String PERL_INTERPRETER_UNIX = "Unix"; //$NON-NLS-1$

    private static final String PERL_INTERPRETER_WINDOWS = "Windows"; //$NON-NLS-1$

    private static final String JOB_SOURCE_FOLDER_NAME = "src"; //$NON-NLS-1$

    private static final String SYSTEM_ROUTINES_FOLDER_NAME = "system";

    /**
     * Gets the export resources.
     * 
     * @param process
     * @param needLauncher
     * @param needSystemRoutine
     * @param needUserRoutine
     * @param needModule
     * @param needJob
     * @param needContext
     * @return
     */
    public List<ExportFileResource> getExportResources(ExportFileResource[] process,
            Map<ExportChoice, Boolean> exportChoice, String contextName, String launcher) {

        ProcessorUtilities.setExportConfig("perl", "", LIBRARY_FOLDER_NAME);

        for (int i = 0; i < process.length; i++) {
            ProcessItem processItem = process[i].getProcess();
            generatePerlFiles(processItem, contextName);
            List<URL> resources = new ArrayList<URL>();
            resources.addAll(getLauncher(exportChoice.get(ExportChoice.needLauncher), processItem,
                    escapeSpace(contextName), escapeSpace(launcher)));

            if (i == 0) { // i = 0 to export only once
                // Gets system routines.
                List<URL> systemRoutineList = getSystemRoutine(exportChoice.get(ExportChoice.needSystemRoutine));
                if (systemRoutineList.size() > 0) {
                    process[i].addResources(LIBRARY_FOLDER_NAME + File.separatorChar
                            + ILibrariesService.SOURCE_PERL_ROUTINES_FOLDER + File.separatorChar
                            + SYSTEM_ROUTINES_FOLDER_NAME, systemRoutineList);
                }
                // Gets user routines.
                try {
                    List<URL> userRoutineList = getUserRoutine(exportChoice.get(ExportChoice.needUserRoutine));
                    if (userRoutineList.size() > 0) {
                        process[i].addResources(LIBRARY_FOLDER_NAME + File.separatorChar
                                + ILibrariesService.SOURCE_PERL_ROUTINES_FOLDER + File.separatorChar
                                + this.getCurrentProjectName(), userRoutineList);
                    }
                } catch (MalformedURLException e) {
                    ExceptionHandler.process(e);
                }

                List<URL> talendLibraries = getTalendLibraries(exportChoice.get(ExportChoice.needTalendLibraries));
                if (talendLibraries.size() > 0) {
                    process[i].addResources(LIBRARY_FOLDER_NAME + File.separatorChar + "talend", talendLibraries);
                }
            }
            resources.addAll(getJobScripts(processItem, exportChoice.get(ExportChoice.needJob)));
            resources.addAll(getContextScripts(processItem, exportChoice.get(ExportChoice.needContext)));
            boolean needChildren = exportChoice.get(ExportChoice.needJob) && exportChoice.get(ExportChoice.needContext);
            addChildrenResources(processItem, needChildren, process[i], exportChoice);
            process[i].addResources(resources);
        }
        return Arrays.asList(process);
    }

    /**
     * Gets user routine.
     * 
     * @param needUserRoutine
     * @return
     * @throws MalformedURLException
     */
    private List<URL> getUserRoutine(boolean needUserRoutine) throws MalformedURLException {
        List<URL> list = new ArrayList<URL>();
        if (!needUserRoutine) {
            return list;
        }
        ILibrariesService librariesService = CorePlugin.getDefault().getLibrariesService();
        String folderPath = librariesService.getLibrariesPath() + File.separatorChar
                + ILibrariesService.SOURCE_PERL_ROUTINES_FOLDER + File.separatorChar + this.getCurrentProjectName();
        File file = new File(folderPath);
        File[] files = file.listFiles();
        if (files != null) {
            for (int i = 0; i < files.length; i++) {
                File tempFile = files[i];
                list.add(tempFile.toURL());
            }
        }

        return list;
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
        if (needSystemRoutine) {
            ILibrariesService librariesService = CorePlugin.getDefault().getLibrariesService();
            String path = librariesService.getLibrariesPath() + File.separatorChar
                    + ILibrariesService.SOURCE_PERL_ROUTINES_FOLDER + File.separatorChar + SYSTEM_ROUTINES_FOLDER_NAME;
            File file = new File(path);
            File[] files = file.listFiles();
            for (int i = 0; i < files.length; i++) {
                File tempFile = files[i];
                try {
                    list.add(tempFile.toURL());
                } catch (MalformedURLException e) {
                    ExceptionHandler.process(e);
                }
            }

        }
        return list;
    }

    private void addComponentModules(ProcessItem processItem, ExportFileResource resource) {
        EList nList = processItem.getProcess().getNode();
        Set set = new HashSet(nList);
        for (Iterator iter = set.iterator(); iter.hasNext();) {
            NodeType nType = (NodeType) iter.next();
            String componentName = nType.getComponentName();

            List<URL> modules = getComponentModules(componentName); //$NON-NLS-1$
            resource.addResources(LIBRARY_FOLDER_NAME + File.separatorChar + componentName, modules);
        }
    }

    private List<URL> getComponentModules(String componentName) {
        List<URL> modules = new ArrayList<URL>();
        ILibrariesService librariesService = CorePlugin.getDefault().getLibrariesService();
        String path = librariesService.getLibrariesPath() + File.separatorChar + componentName;
        File file = new File(path);
        if (file.exists()) {
            File[] files = file.listFiles();
            for (int i = 0; i < files.length; i++) {
                File tempFile = files[i];
                try {
                    modules.add(tempFile.toURL());
                } catch (MalformedURLException e) {
                    ExceptionHandler.process(e);
                }
            }
        }
        return modules;
    }

    private List<URL> getSource(ProcessItem processItem, boolean needChoice) {
        List<String> list = new ArrayList<String>();
        if (needChoice) {
            try {
                String name = processItem.getProperty().getLabel() + "_" + processItem.getProperty().getVersion(); //$NON-NLS-1$
                name = name != null ? name.replace(" ", "") : ""; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                list.add(name + ".item"); //$NON-NLS-1$
                list.add(name + ".properties"); //$NON-NLS-1$

            } catch (Exception e) {
                ExceptionHandler.process(e);
            }

        }

        IResource[] resources = this.getAllSourceFiles();
        return getResourcesURL(resources, list);
    }

    /**
     * Generates the perl files.
     * 
     * @param needGenerateCode
     * @param contextName
     * @param process
     */
    private void generatePerlFiles(ProcessItem process, String contextName) {
        ProcessorUtilities.generateCode(process.getProperty().getLabel(), contextName); //$NON-NLS-1$
    }

    /**
     * Gets perl intepreter.
     * 
     * @param needLauncher
     * @param process
     * @return
     */
    private List<URL> getLauncher(boolean needLauncher, ProcessItem process, String contextName, String launcher) {

        List<URL> list = new ArrayList<URL>();
        if (!needLauncher) {
            return list;
        }
        String cmd = getCommandByTalendJob(escapeFileNameSpace(process), contextName);
        String tmpFold = getTmpFolder();
        File fileTemp = new File(tmpFold);
        if (!fileTemp.exists()) {
            fileTemp.mkdir();
        }
        if (launcher.equals(ALL_PERL_INTERPRETERS)) {
            createLauncherFile(process, list, cmd, UNIX_LAUNCHER, tmpFold);
            createLauncherFile(process, list, cmd, WINDOWS_LAUNCHER, tmpFold);
        } else if (launcher.equals(PERL_INTERPRETER_UNIX)) {
            createLauncherFile(process, list, cmd, UNIX_LAUNCHER, tmpFold);
        } else if (launcher.equals(PERL_INTERPRETER_WINDOWS)) {
            createLauncherFile(process, list, cmd, WINDOWS_LAUNCHER, tmpFold);
        }
        return list;
    }

    /**
     * DOC Administrator Comment method "createLauncherFile".
     * 
     * @param process
     * @param list
     * @param cmdPrimary
     * @param cmdSecondary
     * @param tmpFold
     */
    private void createLauncherFile(ProcessItem process, List<URL> list, String cmdPrimary, String fileName,
            String tmpFold) {
        PrintWriter pw = null;
        try {

            File file = new File(tmpFold, process.getProperty().getLabel() + "_" + fileName); //$NON-NLS-1$
            file.createNewFile();
            pw = new PrintWriter(new FileOutputStream(file));
            pw.print(cmdPrimary);
            pw.flush();
            list.add(file.toURL());
            pw.close();
        } catch (Exception e) {
            ExceptionHandler.process(e);
        } finally {
            try {
                if (pw != null) {
                    pw.close();
                }
            } catch (Exception e) {
                // do nothing here
            }
        }
    }

    private String getCommandByTalendJob(String jobName, String context) {
        String[] cmd = new String[] {};
        try {
            cmd = ProcessorUtilities.getCommandLine(true, jobName, context, new String[] {});
        } catch (ProcessorException e) {
            e.printStackTrace();
        }
        StringBuffer sb = new StringBuffer();
        sb.append(""); //$NON-NLS-1$
        for (String s : cmd) {
            sb.append(s).append(' ');
        }
        return sb.toString();
    }

    /**
     * Gets the perl launcher location.
     * 
     * @return
     */
    public String[] getLauncher() {
        String[] launchers = { ALL_PERL_INTERPRETERS, PERL_INTERPRETER_UNIX, PERL_INTERPRETER_WINDOWS };
        return launchers;
    }

    /**
     * Gets resources' URL.
     * 
     * @param resources
     * @param fileNames
     * @return
     */
    private List<URL> getResourcesURL(IResource[] resources, List<String> fileNames) {
        List<URL> list = new ArrayList<URL>();

        for (Iterator<String> iter = fileNames.iterator(); iter.hasNext();) {
            String name = iter.next();
            for (int i = 0; i < resources.length; i++) {
                IResource resource = resources[i];
                if (resource.getFullPath().toOSString().endsWith(name)) {
                    try {
                        URL url = resource.getLocation().toFile().toURL();
                        list.add(FileLocator.toFileURL(url));
                        break;
                    } catch (Exception e) {
                        ExceptionHandler.process(e);
                    }
                }
            }
        }
        return list;
    }

    IResource[] resouces = null;

    /**
     * Gets all the perl files in the project .Perl.
     * 
     * @return
     */
    private IResource[] getAllPerlFiles() {
        if (resouces == null) {
            try {
                IProject perlProject = RepositoryPlugin.getDefault().getRunProcessService().getProject(
                        ECodeLanguage.PERL);
                resouces = perlProject.members();
            } catch (Exception e) {
                ExceptionHandler.process(e);
                resouces = new IResource[0];
            }
        }
        return resouces;
    }

    private IResource[] sourceResouces = null;

    private IResource[] getAllSourceFiles() {
        if (sourceResouces == null) {
            try {
                List<IResource> sourceFile = new ArrayList<IResource>();
                Project project = ((RepositoryContext) CorePlugin.getContext().getProperty(
                        Context.REPOSITORY_CONTEXT_KEY)).getProject();
                IProject prj = ResourceModelUtils.getProject(project);
                IFolder folder = prj.getFolder(ERepositoryObjectType.getFolderName(ERepositoryObjectType.PROCESS));
                addNodeToResource(folder.members(), sourceFile);
                sourceResouces = sourceFile.toArray(new IResource[sourceFile.size()]);
            } catch (Exception e) {
                ExceptionHandler.process(e);
                sourceResouces = new IResource[0];
            }
        }
        return sourceResouces;
    }

    private void addNodeToResource(IResource[] resources, List<IResource> sourceFile) throws CoreException {

        for (IResource resource : resources) {
            if (resource instanceof IFolder) {
                IFolder folder = (IFolder) resource;
                addNodeToResource(folder.members(), sourceFile);
            }
            if (resource instanceof IFile) {
                sourceFile.add(resource);
            }

        }
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
        name = name.toUpperCase();

        return name;
    }

    /**
     * Gets required perl libraries.
     * 
     * @param needModel
     * @return
     */
    private List<URL> getTalendLibraries(boolean needLibraries) {
        List<URL> libraries = new ArrayList<URL>();
        if (needLibraries) {
            try {
                ILibrariesService service = CorePlugin.getDefault().getLibrariesService();
                libraries = service.getTalendRoutines();
            } catch (Exception e) {
                ExceptionHandler.process(e);
            }
        }
        return libraries;
    }

    /**
     * Gets Job Scripts.
     * 
     * @param process
     * @param needJob
     * @return
     */
    private List<URL> getJobScripts(ProcessItem process, boolean needJob) {
        List<String> list = new ArrayList<String>();
        if (needJob) {
            String projectName = getCurrentProjectName();
            try {
                String name = escapeFileNameSpace(process);
                name = projectName + ".job_" + name + ".pl"; //$NON-NLS-1$ //$NON-NLS-2$
                list.add(name);
            } catch (Exception e) {
                ExceptionHandler.process(e);
            }

        }

        IResource[] resources = this.getAllPerlFiles();
        return getResourcesURL(resources, list);
    }

    private void addChildrenResources(ProcessItem process, boolean needChildren, ExportFileResource resource,
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

        IResource[] resources = this.getAllPerlFiles();
        resource.addResources(getResourcesURL(resources, list));
    }

    private void getChildrenJobAndContextName(String rootName, List<String> list, ProcessItem process,
            String projectName, List<ProcessItem> processedJob, ExportFileResource resource,
            Map<ExportChoice, Boolean> exportChoice) {
        if (processedJob.contains(process)) {
            // prevent circle
            return;
        }
        processedJob.add(process);
        addComponentModules(process, resource);
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

            String processName = escapeSpace(processLabel);
            String jobScriptName = projectName + ".job_" + processName + ".pl"; //$NON-NLS-1$
            String contextName = escapeSpace(jType.getContext());
            String contextFullName = projectName + ".job_" + processName + "_" + contextName + ".pl"; //$NON-NLS-1$ 

            addToList(list, jobScriptName);
            addToList(list, contextFullName);

            ProcessItem childProcess = findProcess(processLabel);
            if (childProcess == null) {
                return;
            }
            getChildrenJobAndContextName(rootName, list, childProcess, projectName, processedJob, resource,
                    exportChoice);
        }
    }

    private ProcessItem findProcess(String name) {
        return ProcessorUtilities.getProcessItem(name);
    }

    private void addToList(List<String> list, String o) {
        if (!list.contains(o)) {
            list.add(o);
        }
    }

    /**
     * Gets context scripts.
     * 
     * @param process
     * @param needContext
     * @return
     */
    private List<URL> getContextScripts(ProcessItem process, boolean needContext) {
        List<String> list = new ArrayList<String>();
        if (needContext) {
            List<String> contexts = getJobContexts(process);
            // String processName = process[0].getProperty().getLabel();
            String processName = escapeFileNameSpace(process);
            String projectName = getCurrentProjectName();
            for (Iterator<String> iter = contexts.iterator(); iter.hasNext();) {
                String contextName = iter.next();
                contextName = escapeSpace(contextName);
                String contextFullName = projectName + ".job_" + processName + "_" //$NON-NLS-1$ //$NON-NLS-2$
                        + contextName + ".pl"; //$NON-NLS-3$
                list.add(contextFullName);
            }
        }

        IResource[] resources = this.getAllPerlFiles();
        return getResourcesURL(resources, list);
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
