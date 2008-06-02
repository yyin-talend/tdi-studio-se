// ============================================================================
//
// Copyright (C) 2006-2007 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.repository.ui.wizards.exportjob.scriptsmanager;

import java.io.File;
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
import org.eclipse.core.resources.IResource;
import org.eclipse.emf.common.util.EList;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.core.CorePlugin;
import org.talend.core.language.ECodeLanguage;
import org.talend.core.model.components.IComponent;
import org.talend.core.model.general.ILibrariesService;
import org.talend.core.model.general.ModuleNeeded;
import org.talend.core.model.properties.ProcessItem;
import org.talend.core.model.utils.PerlResourcesHelper;
import org.talend.designer.core.model.utils.emf.talendfile.ContextType;
import org.talend.designer.core.model.utils.emf.talendfile.NodeType;
import org.talend.designer.runprocess.IProcessor;
import org.talend.designer.runprocess.JobInfo;
import org.talend.designer.runprocess.ProcessorUtilities;
import org.talend.repository.RepositoryPlugin;
import org.talend.repository.documentation.ExportFileResource;
import org.talend.repository.model.ComponentsFactoryProvider;

/**
 * Manages the job scripts to be exported. <br/>
 * 
 * $Id: JobScriptsManager.java 1 2006-12-14 下午05:06:49 ftang
 * 
 */
public class JobPerlScriptsManager extends JobScriptsManager {

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
    @Override
    public List<ExportFileResource> getExportResources(ExportFileResource[] process, Map<ExportChoice, Boolean> exportChoice,
            String contextName, String launcher, int statisticPort, int tracePort, String... codeOptions) {

        ProcessorUtilities.setExportConfig("perl", "", LIBRARY_FOLDER_NAME);

        for (int i = 0; i < process.length; i++) {
            ProcessItem processItem = (ProcessItem) process[i].getItem();
            if (!BooleanUtils.isTrue(exportChoice.get(ExportChoice.doNotCompileCode))) {
                generateJobFiles(processItem, contextName, statisticPort != IProcessor.NO_STATISTICS,
                        statisticPort != IProcessor.NO_TRACES, exportChoice.get(ExportChoice.applyToChildren));
            }
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
            boolean needChildren = exportChoice.get(ExportChoice.needJob) && exportChoice.get(ExportChoice.needContext);
            addChildrenResources(processItem, needChildren, process[i], exportChoice, contextName);
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

    private void addComponentModules(ProcessItem processItem, ExportFileResource resource) {
        EList nList = processItem.getProcess().getNode();
        Set set = new HashSet(nList);
        for (Iterator iter = set.iterator(); iter.hasNext();) {
            NodeType nType = (NodeType) iter.next();
            String componentName = nType.getComponentName();
            IComponent component = ComponentsFactoryProvider.getInstance().get(componentName);
            List<URL> modules = getComponentModules(componentName);
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

    /**
     * Gets all the perl files in the project .Perl.
     * 
     * @param refresh If it is true, reload files from project.
     * @return
     */
    private IResource[] getAllPerlFiles(boolean refresh) {
        if (resouces == null || refresh) {
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

    private IResource[] getAllPerlFiles() {
        return getAllPerlFiles(false);
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
            try {
                String fileName = PerlResourcesHelper.getJobFileName(process.getProperty().getLabel(), process.getProperty()
                        .getVersion());
                list.add(fileName);
            } catch (Exception e) {
                ExceptionHandler.process(e);
            }
        }
        IResource[] resources = this.getAllPerlFiles(true);
        return getResourcesURL(resources, list);
    }

    /**
     * DOC acer Comment method "getCurrentProjectName".
     * 
     * @return
     */
    private String getCurrentProjectName() {
        return PerlResourcesHelper.getCurrentProjectName();
    }

    private void addChildrenResources(ProcessItem process, boolean needChildren, ExportFileResource resource,
            Map<ExportChoice, Boolean> exportChoice, String contextName) {
        List<String> list = new ArrayList<String>();
        if (needChildren) {
            String projectName = getCurrentProjectName();
            try {
                List<ProcessItem> processedJob = new ArrayList<ProcessItem>();
                getChildrenJobAndContextName(process.getProperty().getLabel(), list, process, projectName, processedJob,
                        resource, exportChoice, contextName);
            } catch (Exception e) {
                ExceptionHandler.process(e);
            }

        }

        IResource[] resources = this.getAllPerlFiles();
        resource.addResources(getResourcesURL(resources, list));
    }

    private void getChildrenJobAndContextName(String rootName, List<String> list, ProcessItem process, String projectName,
            List<ProcessItem> processedJob, ExportFileResource resource, Map<ExportChoice, Boolean> exportChoice,
            String fatherContext) {
        if (processedJob.contains(process)) {
            // prevent circle
            return;
        }
        processedJob.add(process);
        addComponentModules(process, resource);
        addSource(process, exportChoice.get(ExportChoice.needSource), resource, JOB_SOURCE_FOLDER_NAME);

        Set<JobInfo> subjobInfos = ProcessorUtilities.getChildrenJobInfo(process);
        for (JobInfo subjobInfo : subjobInfos) {
            if (subjobInfo.getJobName().equals(rootName)) {
                continue;
            }

            String jobScriptName = PerlResourcesHelper.getJobFileName(subjobInfo.getJobName(), subjobInfo.getJobVersion());
            String contextName = null;
            if (exportChoice.get(ExportChoice.applyToChildren)) {
                // see bug 0003862: Export job with the flag "Apply to children" if the child don't have the
                // same context fails.
                ProcessItem processItem = ProcessorUtilities.getProcessItem(subjobInfo.getJobId(), subjobInfo.getJobVersion());
                if (ProcessorUtilities.checkIfContextExisted(processItem, fatherContext)) {
                    contextName = fatherContext;
                } else {
                    // use the default context of subjob
                    contextName = processItem.getProcess().getDefaultContext();
                }
            } else {
                contextName = escapeSpace(subjobInfo.getContextName());
            }

            String contextFullName = PerlResourcesHelper.getContextFileName(subjobInfo.getJobName(), subjobInfo.getJobVersion(),
                    contextName);

            addToList(list, jobScriptName);
            addToList(list, contextFullName);

            getChildrenJobAndContextName(rootName, list, subjobInfo.getProcessItem(), projectName, processedJob, resource,
                    exportChoice, fatherContext);
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
            for (String contextName : contexts) {
                String contextFileName = PerlResourcesHelper.getContextFileName(process.getProperty().getLabel(), process
                        .getProperty().getVersion(), contextName);
                list.add(contextFileName);
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
    @Override
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
