// ============================================================================
//
// Copyright (C) 2006-2009 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.spagic.engines.client.ui.wizards;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
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
import org.eclipse.core.runtime.IPath;
import org.eclipse.emf.common.util.EList;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.exception.PersistenceException;
import org.talend.core.CorePlugin;
import org.talend.core.language.ECodeLanguage;
import org.talend.core.model.components.IComponent;
import org.talend.core.model.general.ILibrariesService;
import org.talend.core.model.general.ModuleNeeded;
import org.talend.core.model.genhtml.HTMLDocUtils;
import org.talend.core.model.process.IContextParameter;
import org.talend.core.model.process.IProcess;
import org.talend.core.model.properties.Item;
import org.talend.core.model.properties.ProcessItem;
import org.talend.core.model.utils.JavaResourcesHelper;
import org.talend.core.model.utils.PerlResourcesHelper;
import org.talend.designer.core.IDesignerCoreService;
import org.talend.designer.core.model.utils.emf.talendfile.NodeType;
import org.talend.designer.runprocess.IProcessor;
import org.talend.designer.runprocess.JobInfo;
import org.talend.designer.runprocess.ProcessorException;
import org.talend.designer.runprocess.ProcessorUtilities;
import org.talend.repository.RepositoryPlugin;
import org.talend.repository.documentation.ExportFileResource;
import org.talend.repository.model.ComponentsFactoryProvider;
import org.talend.repository.model.ProxyRepositoryFactory;

/**
 * DOC qwei class global comment. Detailled comment <br/>
 * 
 */
public class SpagicPerlDeployManager extends org.talend.repository.ui.wizards.exportjob.scriptsmanager.JobPerlScriptsManager {

    private static final String SYSTEM_ROUTINES_FOLDER_NAME = "system"; //$NON-NLS-1$

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
     * @throws ProcessorException
     */
    @Override
    public List<ExportFileResource> getExportResources(ExportFileResource[] process, Map<ExportChoice, Object> exportChoice,
            String contextName, String launcher, int statisticPort, int tracePort, String... codeOptions)
            throws ProcessorException {

        ProcessorUtilities.setExportConfig("perl", "", LIBRARY_FOLDER_NAME); //$NON-NLS-1$ //$NON-NLS-2$

        for (int i = 0; i < process.length; i++) {
            ProcessItem processItem = (ProcessItem) process[i].getItem();
            if (!isOptionChoosed(exportChoice, ExportChoice.doNotCompileCode)) {
                generateJobFiles(processItem, contextName, statisticPort != IProcessor.NO_STATISTICS,
                        statisticPort != IProcessor.NO_TRACES, isOptionChoosed(exportChoice, ExportChoice.applyToChildren));
            }
            List<URL> resources = new ArrayList<URL>();
            resources.addAll(getLauncher(isOptionChoosed(exportChoice, ExportChoice.needLauncher), processItem,
                    escapeSpace(contextName), escapeSpace(launcher), statisticPort, tracePort, codeOptions));

            // Gets system routines.
            List<URL> systemRoutineList = getSystemRoutine(isOptionChoosed(exportChoice, ExportChoice.needSystemRoutine));
            if (systemRoutineList.size() > 0) {
                process[i].addResources(LIBRARY_FOLDER_NAME + PATH_SEPARATOR + ILibrariesService.SOURCE_PERL_ROUTINES_FOLDER
                        + PATH_SEPARATOR + SYSTEM_ROUTINES_FOLDER_NAME, systemRoutineList);
            }
            // Gets user routines.
            try {
                List<URL> userRoutineList = getUserRoutine(isOptionChoosed(exportChoice, ExportChoice.needUserRoutine));
                if (userRoutineList.size() > 0) {
                    process[i].addResources(LIBRARY_FOLDER_NAME + PATH_SEPARATOR + ILibrariesService.SOURCE_PERL_ROUTINES_FOLDER
                            + PATH_SEPARATOR + this.getCorrespondingProjectName(null), userRoutineList);
                }
            } catch (MalformedURLException e) {
                ExceptionHandler.process(e);
            }

            List<URL> talendLibraries = getTalendLibraries(isOptionChoosed(exportChoice, ExportChoice.needTalendLibraries));
            if (talendLibraries.size() > 0) {
                process[i].addResources(LIBRARY_FOLDER_NAME + PATH_SEPARATOR + "talend", talendLibraries); //$NON-NLS-1$
            }
            resources.addAll(getJobScripts(processItem, isOptionChoosed(exportChoice, ExportChoice.needJobScript)));
            resources.addAll(getContextScripts(processItem, isOptionChoosed(exportChoice, ExportChoice.needContext)));
            resources.addAll(getProperties(processItem, contextName));
            boolean needChildren = true;
            addChildrenResources(process, processItem, needChildren, process[i], exportChoice);
            process[i].addResources(resources);
        }
        return Arrays.asList(process);
    }

    private List<URL> getJobScripts(ProcessItem process, boolean needJob) {
        List<String> list = new ArrayList<String>();
        if (needJob) {
            try {
                String projectName = PerlResourcesHelper.getRootProjectName(process);
                String fileName = PerlResourcesHelper.getJobFileName(projectName, process.getProperty().getLabel(), process
                        .getProperty().getVersion());
                list.add(fileName);
            } catch (Exception e) {
                ExceptionHandler.process(e);
            }
        }

        IResource[] resources = this.getAllPerlFiles(true);
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
                + PATH_SEPARATOR + this.getCorrespondingProjectName(null);
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

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.ui.wizards.exportjob.scriptsmanager.JobPerlScriptsManager#getCurrentProjectName()
     */
    protected String getCorrespondingProjectName(Item item) {
        return PerlResourcesHelper.getRootProjectName(item);
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
            String projectName = PerlResourcesHelper.getRootProjectName(process);
            for (String contextName : contexts) {
                String contextFileName = PerlResourcesHelper.getContextFileName(projectName, process.getProperty().getLabel(),
                        process.getProperty().getVersion(), contextName);
                list.add(contextFileName);
            }
        }

        IResource[] resources = this.getAllPerlFiles();
        return getResourcesURL(resources, list);
    }

    private void addChildrenResources(ExportFileResource[] allResources, ProcessItem process, boolean needChildren,
            ExportFileResource resource, Map<ExportChoice, Object> exportChoice) {
        List<String> list = new ArrayList<String>();
        if (needChildren) {
            String projectName = getCorrespondingProjectName(null);
            try {
                List<ProcessItem> processedJob = new ArrayList<ProcessItem>();
                getChildrenJobAndContextName(allResources, process.getProperty().getLabel(), list, process, projectName,
                        processedJob, resource, exportChoice);
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

    private void getChildrenJobAndContextName(ExportFileResource[] allResources, String rootName, List<String> list,
            ProcessItem process, String projectName, List<ProcessItem> processedJob, ExportFileResource resource,
            Map<ExportChoice, Object> exportChoice) {
        if (processedJob.contains(process)) {
            // prevent circle
            return;
        }
        try {
            process = (ProcessItem) ProxyRepositoryFactory.getInstance().getUptodateProperty(process.getProperty()).getItem();
        } catch (PersistenceException e) {
            e.printStackTrace();
        }
        processedJob.add(process);
        addComponentModules(process, resource);
        addJobItem(allResources, process, isOptionChoosed(exportChoice, ExportChoice.needJobItem), resource,
                JOB_SOURCE_FOLDER_NAME);

        Set<JobInfo> subjobInfos = ProcessorUtilities.getChildrenJobInfo(process);
        String rootProjectName = PerlResourcesHelper.getRootProjectName(resource.getItem());
        for (JobInfo subjobInfo : subjobInfos) {
            if (subjobInfo.getJobName().equals(rootName)) {
                continue;
            }
            String jobScriptName = PerlResourcesHelper.getJobFileName(rootProjectName, subjobInfo.getJobName(), subjobInfo
                    .getJobVersion());
            String contextFullName = PerlResourcesHelper.getContextFileName(rootProjectName, subjobInfo.getJobName(), subjobInfo
                    .getJobVersion(), subjobInfo.getContextName());

            addToList(list, jobScriptName);
            addToList(list, contextFullName);

            getChildrenJobAndContextName(allResources, rootName, list, subjobInfo.getProcessItem(), projectName, processedJob,
                    resource, exportChoice);
        }
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
                String[] string = module.getModuleName().split("::"); //$NON-NLS-1$
                if (string.length != 2) {
                    continue;
                }
                resource.addResources(LIBRARY_FOLDER_NAME + PATH_SEPARATOR + string[0], getComponentModules(string[0] + "/" //$NON-NLS-1$
                        + string[1] + ".pm")); //$NON-NLS-1$
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

    public List<URL> getProperties(ProcessItem processItem, String contextName) {
        List<URL> list = new ArrayList<URL>();
        Properties p = new Properties();
        FileOutputStream out = null;
        String rootProjectName = PerlResourcesHelper.getRootProjectName(processItem);
        String name = PerlResourcesHelper.getJobFileName(rootProjectName, processItem.getProperty().getLabel(), processItem
                .getProperty().getVersion());

        try {
            IPath path = getSrcRootLocation();
            path = path.append(name);
            BufferedReader buff = new BufferedReader(new FileReader(path.toPortableString()));
            int nbLine = 0;
            while (buff.readLine() != null) {
                nbLine++;
            }
            File file = new File(getTmpFolder() + PATH_SEPARATOR + "spagic.properties"); //$NON-NLS-1$
            out = new FileOutputStream(file);
            PrintStream ps = new PrintStream(out);
            IDesignerCoreService service = CorePlugin.getDefault().getDesignerCoreService();
            IProcess process = service.getProcessFromProcessItem(processItem);
            List<IContextParameter> ctxParams = process.getContextManager().getContext(contextName).getContextParameterList();
            for (IContextParameter ctxParam : ctxParams) {
                p.put(ctxParam.getName(), ctxParam.getValue());
            }
            p.put("JobClassName", getCorrespondingProjectName(null) //$NON-NLS-1$
                    + "." //$NON-NLS-1$
                    + JavaResourcesHelper.getJobFolderName(processItem.getProperty().getLabel(), processItem.getProperty()
                            .getVersion()) + "." + processItem.getProperty().getLabel()); //$NON-NLS-1$
            p.put("talendJobClassDescription", HTMLDocUtils.checkString(processItem.getProperty().getDescription())); //$NON-NLS-1$
            p.put("rowNumber", Integer.toString(nbLine)); //$NON-NLS-1$
            p.put("host", "localhost"); //$NON-NLS-1$ //$NON-NLS-2$
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

    /**
     * Get the path of .perl
     * 
     * @throws Exception
     */
    protected IPath getSrcRootLocation() throws Exception {
        IProject project = RepositoryPlugin.getDefault().getRunProcessService().getProject(ECodeLanguage.PERL);
        IPath root = project.getLocation();
        return root;
    }
}
