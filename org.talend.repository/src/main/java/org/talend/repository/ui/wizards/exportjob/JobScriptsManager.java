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
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.internal.resources.Workspace;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.FileLocator;
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
import org.talend.designer.codegen.perlmodule.IPerlModuleService;
import org.talend.designer.core.model.utils.emf.talendfile.ContextType;
import org.talend.repository.RepositoryPlugin;
import org.talend.repository.model.ProxyRepositoryFactory;

/**
 * Manages the job scripts to be exported. <br/>
 * 
 * $Id: JobScriptsManager.java 1 2006-12-14 下午05:06:49 bqian
 * 
 */
public class JobScriptsManager {

    private String[] launcherPerlScriptFile = { "run.sh", "run.bat" };

    /**
     * DOC qian Gets the export resources.
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
    public List<URL> getExportResources(ProcessItem[] process, boolean needLauncher, boolean needSystemRoutine,
            boolean needUserRoutine, boolean needModel, boolean needJob, boolean needContext, String contextName) {

        List<URL> resources = new ArrayList<URL>();

        resources.addAll(getLauncher(needLauncher, process, escapeSpace(contextName)));

        resources.addAll(getSystemRoutine(needSystemRoutine));
        resources.addAll(getUserRoutine(needUserRoutine));
        resources.addAll(getModel(needModel));
        resources.addAll(getJobScripts(process, needJob));
        resources.addAll(getContextScripts(process, needContext));

        return resources;
    }

    private String getTmpFolder() {
        String tmpFold = System.getProperty("user.dir");
        return tmpFold;
    }

    /**
     * DOC qian Deletes the temporary files.
     */
    public void deleteTempFiles() {
        String tmpFold = getTmpFolder();
        for (int i = 0; i < launcherPerlScriptFile.length; i++) {
            String fileName = launcherPerlScriptFile[i];
            File file = new File(tmpFold, fileName);
            if (file.exists()) {
                file.delete();
            }
        }
    }

    /**
     * DOC qian Gets perl intepreter.
     * 
     * @param needLauncher
     * @param process
     * @return
     */
    private List<URL> getLauncher(boolean needLauncher, ProcessItem[] process, String contextName) {

        List<URL> list = new ArrayList<URL>();
        if (!needLauncher) {
            return list;
        }

        String cmd = getCommandByTalendJob(getCurrentProjectName(), escapeFileNameSpace(process[0]), contextName);

        String tmpFold = getTmpFolder();
        PrintWriter pw = null;
        try {
            for (int i = 0; i < launcherPerlScriptFile.length; i++) {
                String fileName = launcherPerlScriptFile[i];
                File file = new File(tmpFold, fileName);
                file.createNewFile();
                pw = new PrintWriter(new FileOutputStream(file));
                pw.print(cmd);
                pw.flush();

                list.add(file.toURL());
                pw.close();
            }
        } catch (Exception e) {
            ExceptionHandler.process(e);
        } finally {
            try {
                if (pw != null) {
                    pw.close();
                }
            } catch (Exception e) {
                // TODO: handle exception
            }
        }

        return list;
    }

    // FIXME to reuse the exstentent code of this implementation
    private String getCommandByTalendJob(String project, String jobName, String context) {
        String contextArg = "--context=";

        String projectSeparator = ".job";

        String wordSeparator = "_";

        String perlExt = ".pl";
        IPreferenceStore prefStore = CorePlugin.getDefault().getPreferenceStore();
        String perlInterpreter = prefStore.getString(ITalendCorePrefConstants.PERL_INTERPRETER);
        if (perlInterpreter == null || perlInterpreter.length() == 0) {
            // throw new ProcessorException(Messages.getString("Processor.configurePerl")); //$NON-NLS-1$
        }

        String perlLib = null;

        String perlCode = project + projectSeparator + wordSeparator + jobName + perlExt;

        String contextCode = project + projectSeparator + wordSeparator + jobName + wordSeparator + context + perlExt;

        String[] cmd = new String[] { perlInterpreter, perlCode, contextArg + contextCode };

        StringBuffer sb = new StringBuffer();
        sb.append("");
        for (String s : cmd) {
            sb.append(s).append(' ');
        }
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }

    /**
     * DOC qian Gets the perl launcher location.
     * 
     * @return
     */
    public String getPerlLauncher() {
        String perlIntepreter = CorePlugin.getDefault().getPreferenceStore().getString(
                ITalendCorePrefConstants.PERL_INTERPRETER);
        return perlIntepreter;
    }

    /**
     * DOC qian Gets system routines.
     * 
     * @param needSystemRoutine
     * @return
     */
    private List<URL> getSystemRoutine(boolean needSystemRoutine) {
        // IPerlModuleService s = (IPerlModuleService) GlobalServiceRegister.getDefault().getService(
        // IPerlModuleService.class);
        // List<URL> l = s.getBuiltInRoutines();
        // for (URL url : l) {
        // System.out.println("system routinte: " + url.getPath());
        // }

        List list = new ArrayList();
        if (needSystemRoutine) {
            getRoutineNames(list, true);
        }

        return getResourcesURL(getAllPerlFiles(), list);
    }

    /**
     * DOC qian Gets resources' URL.
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
     * DOC qian Gets all the perl files in the project .Perl.
     * 
     * @return
     */
    private IResource[] getAllPerlFiles() {
        if (resouces == null) {
            try {

                Workspace workspace = (Workspace) ResourcesPlugin.getWorkspace();
                IProject perlProject = workspace.getRoot().getProject(".Perl");

                resouces = perlProject.members();
            } catch (Exception e) {
                ExceptionHandler.process(e);
                resouces = new IResource[0];
            }
        }
        return resouces;
    }

    /**
     * DOC qian Gets routine names.
     * 
     * @param list
     * @param isBuildin
     */
    private void getRoutineNames(List list, boolean isBuildin) {
        String projectName = getCurrentProjectName();
        try {
            List<IRepositoryObject> routines = ProxyRepositoryFactory.getInstance().getAll(
                    ERepositoryObjectType.ROUTINES);
            for (int i = 0; i < routines.size(); i++) {
                RoutineItem routine = (RoutineItem) routines.get(i).getProperty().getItem();
                if (routine.isBuiltIn() == isBuildin) {
                    String name = routine.getProperty().getLabel();
                    name = projectName + "__" + name + ".pm";
                    list.add(name);
                }
            }
        } catch (PersistenceException e) {
            ExceptionHandler.process(e);
        }
    }

    /**
     * DOC qian Gets user routine.
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
     * DOC qian Gets current project name.
     * 
     * @return
     */
    private String getCurrentProjectName() {
        RepositoryContext repositoryContext = (RepositoryContext) CorePlugin.getContext().getProperty(
                Context.REPOSITORY_CONTEXT_KEY);
        Project project = repositoryContext.getProject();

        String name = project.getLabel();
        name = name.replaceAll(" ", "_");
        name = name.toUpperCase();

        return name;
    }

    /**
     * DOC qian Gets required perl models.
     * 
     * @param needModel
     * @return
     */
    private List<URL> getModel(boolean needModel) {
        List<URL> list = null;
        if (needModel) {
            try {
                IPerlModuleService service = RepositoryPlugin.getDefault().getPerlModuleService();
                list = service.getPerlModule();
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
     * DOC qian Gets Job Scripts.
     * 
     * @param process
     * @param needJob
     * @return
     */
    private List getJobScripts(ProcessItem[] process, boolean needJob) {
        List<String> list = new ArrayList<String>();
        if (needJob) {
            String projectName = getCurrentProjectName();
            try {

                for (int i = 0; i < process.length; i++) {
                    String name = escapeFileNameSpace(process[i]);
                    name = projectName + ".job_" + name + ".pl";
                    list.add(name);
                }

            } catch (Exception e) {
                ExceptionHandler.process(e);
            }

        }

        IResource[] resources = this.getAllPerlFiles();
        return getResourcesURL(resources, list);
    }

    /**
     * DOC qian Gets context scripts.
     * 
     * @param process
     * @param needContext
     * @return
     */
    private List<URL> getContextScripts(ProcessItem[] process, boolean needContext) {
        List<String> list = new ArrayList<String>();
        if (needContext) {
            List<String> contexts = getJobContexts(process[0]);
            // String processName = process[0].getProperty().getLabel();
            String processName = escapeFileNameSpace(process[0]);
            String projectName = getCurrentProjectName();
            for (Iterator<String> iter = contexts.iterator(); iter.hasNext();) {
                String contextName = iter.next();
                contextName = escapeSpace(contextName);
                String contextFullName = projectName + ".job_" + processName + "_" + contextName + ".pl";
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

    /**
     * DOC ftang Comment method "escapeFileNameSpace".
     * 
     * @param processItem
     * @return
     */
    private String escapeFileNameSpace(ProcessItem processItem) {
        String jobName = processItem.getProperty().getLabel();
        return escapeSpace(jobName);
    }

    /**
     * DOC ftang Comment method "escapeSpace".
     * 
     * @param name
     * @return
     */
    private String escapeSpace(String name) {
        return name != null ? name.replace(" ", "") : "";

    }
}
