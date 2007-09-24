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
package org.talend.spagic.engines.client.ui.wizards;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.emf.common.util.EList;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.core.CorePlugin;
import org.talend.core.language.ECodeLanguage;
import org.talend.core.model.components.IComponent;
import org.talend.core.model.general.ILibrariesService;
import org.talend.core.model.general.ModuleNeeded;
import org.talend.core.model.genhtml.HTMLDocUtils;
import org.talend.core.model.properties.ProcessItem;
import org.talend.designer.core.model.utils.emf.talendfile.JobType;
import org.talend.designer.core.model.utils.emf.talendfile.NodeType;
import org.talend.designer.runprocess.IProcessor;
import org.talend.designer.runprocess.ProcessorUtilities;
import org.talend.repository.RepositoryPlugin;
import org.talend.repository.model.ComponentsFactoryProvider;
import org.talend.repository.ui.utils.JavaResourcesHelper;
import org.talend.repository.ui.utils.PerlResourcesHelper;
import org.talend.repository.ui.wizards.exportjob.ExportFileResource;

/**
 * DOC qwei class global comment. Detailled comment <br/>
 * 
 */
public class SpagicPerlDeployManager extends org.talend.repository.ui.wizards.exportjob.scriptsmanager.JobPerlScriptsManager {

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
    public List<ExportFileResource> getExportResources(ExportFileResource[] process, Map<ExportChoice, Boolean> exportChoice,
            String contextName, String launcher, int statisticPort, int tracePort, String... codeOptions) {

        ProcessorUtilities.setExportConfig("perl", "", LIBRARY_FOLDER_NAME);

        for (int i = 0; i < process.length; i++) {
            ProcessItem processItem = process[i].getProcess();
            generateJobFiles(processItem, contextName, statisticPort != IProcessor.NO_STATISTICS,
                    statisticPort != IProcessor.NO_TRACES, exportChoice.get(ExportChoice.applyToChildren));
            List<URL> resources = new ArrayList<URL>();
            resources.addAll(getLauncher(exportChoice.get(ExportChoice.needLauncher), processItem, escapeSpace(contextName),
                    escapeSpace(launcher), statisticPort, tracePort, codeOptions));

            // Gets system routines.
            List<URL> systemRoutineList = getSystemRoutine(exportChoice.get(ExportChoice.needSystemRoutine));
            if (systemRoutineList.size() > 0) {
                process[i].addResources(LIBRARY_FOLDER_NAME + PATH_SEPARATOR + ILibrariesService.SOURCE_PERL_ROUTINES_FOLDER
                        + PATH_SEPARATOR + SYSTEM_ROUTINES_FOLDER_NAME, systemRoutineList);
            }
            // Gets user routines.
            try {
                List<URL> userRoutineList = getUserRoutine(exportChoice.get(ExportChoice.needUserRoutine));
                if (userRoutineList.size() > 0) {
                    process[i].addResources(LIBRARY_FOLDER_NAME + PATH_SEPARATOR + ILibrariesService.SOURCE_PERL_ROUTINES_FOLDER
                            + PATH_SEPARATOR + this.getCurrentProjectName(), userRoutineList);
                }
            } catch (MalformedURLException e) {
                ExceptionHandler.process(e);
            }

            List<URL> talendLibraries = getTalendLibraries(exportChoice.get(ExportChoice.needTalendLibraries));
            if (talendLibraries.size() > 0) {
                process[i].addResources(LIBRARY_FOLDER_NAME + PATH_SEPARATOR + "talend", talendLibraries);
            }
            resources.addAll(getJobScripts(processItem, exportChoice.get(ExportChoice.needJob)));
            resources.addAll(getContextScripts(processItem, exportChoice.get(ExportChoice.needContext)));
            resources.addAll(getProperties(processItem));
            boolean needChildren = exportChoice.get(ExportChoice.needJob) && exportChoice.get(ExportChoice.needContext);
            addChildrenResources(processItem, needChildren, process[i], exportChoice);
            process[i].addResources(resources);
        }
        return Arrays.asList(process);
    }

    private List<URL> getJobScripts(ProcessItem process, boolean needJob) {
        List<String> list = new ArrayList<String>();
        if (needJob) {
            String projectName = getCurrentProjectName();
            try {
                String name = escapeFileNameSpace(process);
                name = projectName + ".job_" + name + PerlResourcesHelper.CONTEXT_FILE_SUFFIX; //$NON-NLS-1$ //$NON-NLS-2$
                list.add(name);
            } catch (Exception e) {
                ExceptionHandler.process(e);
            }

        }

        IResource[] resources = this.getAllPerlFiles();
        return getResourcesURL(resources, list);
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
        ILibrariesService librariesService = CorePlugin.getDefault().getLibrariesService();
        String path = librariesService.getLibrariesPath() + PATH_SEPARATOR + ILibrariesService.SOURCE_PERL_ROUTINES_FOLDER
                + PATH_SEPARATOR + SYSTEM_ROUTINES_FOLDER_NAME;
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
        return list;
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
        String folderPath = librariesService.getLibrariesPath() + PATH_SEPARATOR + ILibrariesService.SOURCE_PERL_ROUTINES_FOLDER
                + PATH_SEPARATOR + this.getCurrentProjectName();
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
     * DOC acer Comment method "getCurrentProjectName".
     * 
     * @return
     */
    private String getCurrentProjectName() {
        return PerlResourcesHelper.getCurrentProjectName();
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
                        + contextName + PerlResourcesHelper.CONTEXT_FILE_SUFFIX; //$NON-NLS-3$
                list.add(contextFullName);
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
                getChildrenJobAndContextName(process.getProperty().getLabel(), list, process, projectName, processedJob,
                        resource, exportChoice);
            } catch (Exception e) {
                ExceptionHandler.process(e);
            }

        }

        IResource[] resources = this.getAllPerlFiles();
        resource.addResources(getResourcesURL(resources, list));
    }

    /**
     * Gets all the perl files in the project .Perl.
     * 
     * @return
     */
    private IResource[] getAllPerlFiles() {
        if (resouces == null) {
            try {
                IProject perlProject = RepositoryPlugin.getDefault().getRunProcessService().getProject(ECodeLanguage.PERL);
                resouces = perlProject.members();
            } catch (Exception e) {
                ExceptionHandler.process(e);
                resouces = new IResource[0];
            }
        }
        return resouces;
    }

    private void getChildrenJobAndContextName(String rootName, List<String> list, ProcessItem process, String projectName,
            List<ProcessItem> processedJob, ExportFileResource resource, Map<ExportChoice, Boolean> exportChoice) {
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
            String jobScriptName = projectName + ".job_" + processName + PerlResourcesHelper.CONTEXT_FILE_SUFFIX; //$NON-NLS-1$
            String contextName = escapeSpace(jType.getContext());
            String contextFullName = projectName
                    + ".job_" + processName + "_" + contextName + PerlResourcesHelper.CONTEXT_FILE_SUFFIX; //$NON-NLS-1$ 

            addToList(list, jobScriptName);
            addToList(list, contextFullName);

            ProcessItem childProcess = findProcess(processLabel);
            if (childProcess == null) {
                return;
            }
            getChildrenJobAndContextName(rootName, list, childProcess, projectName, processedJob, resource, exportChoice);
        }
    }

    private void addComponentModules(ProcessItem processItem, ExportFileResource resource) {
        EList nList = processItem.getProcess().getNode();
        Set set = new HashSet(nList);
        for (Iterator iter = set.iterator(); iter.hasNext();) {
            NodeType nType = (NodeType) iter.next();
            String componentName = nType.getComponentName();
            IComponent component = ComponentsFactoryProvider.getInstance().get(componentName);
            List<URL> modules = getComponentModules(componentName); //$NON-NLS-1$
            resource.addResources(LIBRARY_FOLDER_NAME + PATH_SEPARATOR + componentName, modules);

            // get the modules that this component depends on.
            for (ModuleNeeded module : component.getModulesNeeded()) {
                // for intance, split the "DtMysqlOutput::Mysql" to {"DtMysqlOutput","Mysql"}
                String[] string = module.getModuleName().split("::");
                if (string.length != 2) {
                    continue;
                }
                resource.addResources(LIBRARY_FOLDER_NAME + PATH_SEPARATOR + string[0], getComponentModules(string[0] + "/"
                        + string[1] + ".pm"));
            }
        }
    }

    private List<URL> getComponentModules(String componentName) {
        List<URL> modules = new ArrayList<URL>();
        ILibrariesService librariesService = CorePlugin.getDefault().getLibrariesService();
        String path = librariesService.getLibrariesPath() + PATH_SEPARATOR + componentName;
        File file = new File(path);
        if (!file.exists()) {
            return modules;
        }

        if (file.isFile()) {
            try {
                modules.add(file.toURL());
            } catch (MalformedURLException e) {
                ExceptionHandler.process(e);
            }
            return modules;
        }

        File[] files = file.listFiles();
        for (int i = 0; i < files.length; i++) {
            File tempFile = files[i];
            try {
                modules.add(tempFile.toURL());
            } catch (MalformedURLException e) {
                ExceptionHandler.process(e);
            }
        }
        return modules;
    }

    IResource[] resouces = null;

    public List<URL> getProperties(ProcessItem processItem) {
        List<URL> list = new ArrayList<URL>();
        Properties p = new Properties();
        FileOutputStream out = null;
        try {
            File file = new File(getTmpFolder() + PATH_SEPARATOR + "spagic.properties");
            out = new FileOutputStream(file);
            PrintStream ps = new PrintStream(out);
            p.put("JobClassName", getCurrentProjectName() + "."
                    + JavaResourcesHelper.getJobFolderName(processItem.getProperty().getLabel()) + "."
                    + processItem.getProperty().getLabel());
            p.put("JobName", processItem.getProperty().getLabel());
            p.put("Author", processItem.getProperty().getAuthor().toString());
            p.put("Description", HTMLDocUtils.checkString(processItem.getProperty().getDescription()));
            p.put("Creation", processItem.getProperty().getCreationDate().toString());
            p.put("Purpose", HTMLDocUtils.checkString(processItem.getProperty().getPurpose()));
            p.put("Modification", processItem.getProperty().getModificationDate().toString());
            p.put("Status", processItem.getProperty().getStatusCode());
            p.put("Version", processItem.getProperty().getVersion());
            p.list(ps);
            ps.flush();
            list.add(file.toURI().toURL());
        } catch (Exception e) {
            ExceptionHandler.process(e);
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (Exception e) {
                }
            }
        }
        return list;
    }
}
