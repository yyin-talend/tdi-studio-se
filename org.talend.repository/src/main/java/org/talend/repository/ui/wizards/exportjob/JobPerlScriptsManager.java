// ============================================================================
//
// Talend Community Edition
//
// Copyright (C) 2006 Talend - www.talend.com
//
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either
// version 2.1 of the License, or (at your option) any later version.
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
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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
import org.talend.commons.exception.PersistenceException;
import org.talend.core.CorePlugin;
import org.talend.core.context.Context;
import org.talend.core.context.RepositoryContext;
import org.talend.core.model.general.Project;
import org.talend.core.model.properties.ProcessItem;
import org.talend.core.model.properties.RoutineItem;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.IRepositoryObject;
import org.talend.core.prefs.ITalendCorePrefConstants;
import org.talend.designer.codegen.IModuleService;
import org.talend.designer.core.model.utils.emf.talendfile.ContextType;
import org.talend.designer.core.model.utils.emf.talendfile.JobType;
import org.talend.designer.runprocess.ProcessorUtilities;
import org.talend.repository.RepositoryPlugin;
import org.talend.repository.i18n.Messages;
import org.talend.repository.model.ProxyRepositoryFactory;
import org.talend.repository.model.ResourceModelUtils;
import org.talend.repository.ui.wizards.exportjob.JobScriptsExportWizardPage.ExportChoice;

/**
 * Manages the job scripts to be exported. <br/>
 * 
 * $Id: JobScriptsManager.java 1 2006-12-14 下午05:06:49 bqian
 * 
 */
public class JobPerlScriptsManager extends JobScriptsManager {

    private static final String ALL_PERL_INTERPRETERS = Messages.getString("JobPerlScriptsManager.allInterpreter"); //$NON-NLS-1$

    private static final String JOB_SOURCE_FOLDER_NAME = "src"; //$NON-NLS-1$

    /**
     * qian Gets the export resources.
     * 
     * @param process
     * @param needLauncher
     * @param needSystemRoutine
     * @param needUserRoutine
     * @param needModel
     * @param needJob
     * @param needContext
     * @return
     */
    public List<ExportFileResource> getExportResources(ExportFileResource[] process, Map<ExportChoice, Boolean> exportChoice,
            String contextName, String launcher) {
        for (int i = 0; i < process.length; i++) {
            ProcessItem processItem = process[i].getProcess();
            generatePerlFiles(exportChoice.get(ExportChoice.needGenerateCode), processItem, contextName);
            List<URL> resources = new ArrayList<URL>();
            resources.addAll(getLauncher(exportChoice.get(ExportChoice.needLauncher), processItem, escapeSpace(contextName),
                    escapeSpace(launcher)));
            resources.addAll(getSystemRoutine(exportChoice.get(ExportChoice.needSystemRoutine)));
            resources.addAll(getUserRoutine(exportChoice.get(ExportChoice.needUserRoutine)));
            resources.addAll(getModel(exportChoice.get(ExportChoice.needModel)));
            resources.addAll(getJobScripts(processItem, exportChoice.get(ExportChoice.needJob)));

            List<URL> srcList = getSource(processItem, exportChoice.get(ExportChoice.needSource));
            process[i].addResources(JOB_SOURCE_FOLDER_NAME, srcList);
            resources.addAll(getContextScripts(processItem, exportChoice.get(ExportChoice.needContext)));
            boolean needChildren = exportChoice.get(ExportChoice.needJob) && exportChoice.get(ExportChoice.needContext);
            resources.addAll(getChildrenScripts(processItem, needChildren));
            process[i].addResources(resources);
        }
        return Arrays.asList(process);
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
    private void generatePerlFiles(boolean needGenerateCode, ProcessItem process, String contextName) {
        if (!needGenerateCode) {
            return;
        }
        ProcessItem item = process;
        ProcessorUtilities.generateCode(item.getProperty().getLabel(), contextName);
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
        IPreferenceStore prefStore = CorePlugin.getDefault().getPreferenceStore();
        String primaryLauncher = prefStore.getString(ITalendCorePrefConstants.PERL_INTERPRETER);
        String cmdPrimary = getCommandByTalendJob(getCurrentProjectName(), escapeFileNameSpace(process), contextName,
                primaryLauncher);
        String secondaryLauncher = prefStore.getString(ITalendCorePrefConstants.PERL_SECONDARY_INTERPRETER);
        String cmdSecondary = getCommandByTalendJob(getCurrentProjectName(), escapeFileNameSpace(process), contextName,
                secondaryLauncher);

        String tmpFold = getTmpFolder();
        File fileTemp = new File(tmpFold);
        if (!fileTemp.exists()) {
            fileTemp.mkdir();
        }
        if (launcher.equals(ALL_PERL_INTERPRETERS)) {
            createLauncherFile(process, list, cmdPrimary, getPrimaryFileName(), tmpFold);
            createLauncherFile(process, list, cmdSecondary, getSecondaryFileName(), tmpFold);
        } else if (launcher.equals(primaryLauncher)) {
            createLauncherFile(process, list, cmdPrimary, getPrimaryFileName(), tmpFold);
        } else if (launcher.equals(secondaryLauncher)) {
            createLauncherFile(process, list, cmdSecondary, getSecondaryFileName(), tmpFold);
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
    private void createLauncherFile(ProcessItem process, List<URL> list, String cmdPrimary, String fileName, String tmpFold) {
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

    private String getPrimaryFileName() {
        if (Platform.getOS().equals(Platform.OS_WIN32)) {
            return launcherPerlScriptFile[1];
        } else {
            return launcherPerlScriptFile[0];
        }
    }

    private String getSecondaryFileName() {
        if (Platform.getOS().equals(Platform.OS_WIN32)) {
            return launcherPerlScriptFile[0];
        } else {
            return launcherPerlScriptFile[1];
        }
    }

    // FIXME to reuse the exstentent code of this implementation
    private String getCommandByTalendJob(String project, String jobName, String context, String perlInterpreter) {
        String contextArg = "--context="; //$NON-NLS-1$

        String projectSeparator = ".job"; //$NON-NLS-1$

        String wordSeparator = "_"; //$NON-NLS-1$

        String perlExt = ".pl"; //$NON-NLS-1$

        if (perlInterpreter == null || perlInterpreter.length() == 0) {
            // throw new
            // ProcessorException(Messages.getString("Processor.configurePerl"));
            // //$NON-NLS-1$
        }

        String perlCode = project + projectSeparator + wordSeparator + jobName + perlExt;

        String contextCode = project + projectSeparator + wordSeparator + jobName + wordSeparator + context + perlExt;

        String[] cmd = new String[] { perlInterpreter, perlCode, contextArg + contextCode };

        StringBuffer sb = new StringBuffer();
        sb.append(""); //$NON-NLS-1$
        for (String s : cmd) {
            sb.append(s).append(' ');
        }
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }

    /**
     * Gets the perl launcher location.
     * 
     * @return
     */
    public String[] getLauncher() {
        String perlIntepreter = CorePlugin.getDefault().getPreferenceStore().getString(ITalendCorePrefConstants.PERL_INTERPRETER);
        String secondaryPerlIntepreter = CorePlugin.getDefault().getPreferenceStore().getString(
                ITalendCorePrefConstants.PERL_SECONDARY_INTERPRETER);
        String[] launchers = { ALL_PERL_INTERPRETERS, perlIntepreter, secondaryPerlIntepreter };
        return launchers;
    }

    /**
     * Gets system routines.
     * 
     * @param needSystemRoutine
     * @return
     */
    private List<URL> getSystemRoutine(boolean needSystemRoutine) {
        List list = new ArrayList();
        if (needSystemRoutine) {
            getRoutineNames(list, true);
        }
        return getResourcesURL(getAllPerlFiles(), list);
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
                IProject perlProject = RepositoryPlugin.getDefault().getRunProcessService().getPerlProject();
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
                // IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
                // IProject prj = root.getProject(getCurrentProjectName());
                Project project = ((RepositoryContext) CorePlugin.getContext().getProperty(Context.REPOSITORY_CONTEXT_KEY))
                        .getProject();
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
     * Gets routine names.
     * 
     * @param list
     * @param isBuildin
     */
    private void getRoutineNames(List list, boolean isBuildin) {
        String projectName = getCurrentProjectName();
        try {
            List<IRepositoryObject> routines = ProxyRepositoryFactory.getInstance().getAll(ERepositoryObjectType.ROUTINES);
            for (int i = 0; i < routines.size(); i++) {
                RoutineItem routine = (RoutineItem) routines.get(i).getProperty().getItem();
                if (routine.isBuiltIn() == isBuildin) {
                    String name = routine.getProperty().getLabel();
                    name = projectName + "__" + name + ".pm"; //$NON-NLS-1$ //$NON-NLS-2$
                    list.add(name);
                }
            }
        } catch (PersistenceException e) {
            ExceptionHandler.process(e);
        }
    }

    /**
     * Gets user routine.
     * 
     * @param needModel
     * @return
     */
    private List<URL> getUserRoutine(boolean needModel) {
        List list = new ArrayList();
        if (needModel) {
            getRoutineNames(list, false);
        }
        return getResourcesURL(getAllPerlFiles(), list);
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
     * Gets required perl models.
     * 
     * @param needModel
     * @return
     */
    private List<URL> getModel(boolean needModel) {
        List<URL> list = null;
        if (needModel) {
            try {
                IModuleService service = RepositoryPlugin.getDefault().getModuleService();
                list = service.getModule();
            } catch (Exception e) {
                ExceptionHandler.process(e);
            }
        }
        if (list == null) {
            list = Collections.<URL> emptyList();
        }
        return list;
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

    private List<URL> getChildrenScripts(ProcessItem process, boolean needChildren) {
        List<String> list = new ArrayList<String>();
        if (needChildren) {
            String projectName = getCurrentProjectName();
            try {
                List<ProcessItem> processedJob = new ArrayList<ProcessItem>();
                getChildrenJobAndContextName(process.getProperty().getLabel(), list, process, projectName, processedJob);
            } catch (Exception e) {
                ExceptionHandler.process(e);
            }

        }

        IResource[] resources = this.getAllPerlFiles();
        return getResourcesURL(resources, list);
    }

    private void getChildrenJobAndContextName(String rootName, List<String> list, ProcessItem process, String projectName,
            List<ProcessItem> processedJob) {
        if (processedJob.contains(process)) {
            // pretent circle
            return;
        }
        processedJob.add(process);
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
            String jobScriptName = projectName + ".job_" + processName + ".pl"; //$NON-NLS-1$ //$NON-NLS-2$
            String contextName = escapeSpace(jType.getContext());
            String contextFullName = projectName + ".job_" + processName + "_" + contextName + ".pl"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

            addToList(list, jobScriptName);
            addToList(list, contextFullName);

            ProcessItem childProcess = findProcess(processLabel);
            if (childProcess == null) {
                return;
            }
            getChildrenJobAndContextName(rootName, list, childProcess, projectName, processedJob);
        }
    }

    private ProcessItem findProcess(String name) {
        return ProcessorUtilities.getProcessItem(name);
    }

    private void addToList(List list, Object o) {
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
                String contextFullName = projectName + ".job_" + processName + "_" + contextName + ".pl"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
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
