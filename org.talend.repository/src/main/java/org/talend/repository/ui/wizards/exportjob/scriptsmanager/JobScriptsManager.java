// ============================================================================
//
// Copyright (C) 2006-2010 Talend Inc. - www.talend.com
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
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.BooleanUtils;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.core.CorePlugin;
import org.talend.core.language.LanguageManager;
import org.talend.core.model.process.IContext;
import org.talend.core.model.process.IElementParameter;
import org.talend.core.model.process.INode;
import org.talend.core.model.process.IProcess;
import org.talend.core.model.properties.Item;
import org.talend.core.model.properties.ProcessItem;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.IRepositoryObject;
import org.talend.core.model.utils.PerlResourcesHelper;
import org.talend.core.prefs.ITalendCorePrefConstants;
import org.talend.designer.core.IDesignerCoreService;
import org.talend.designer.core.model.utils.emf.talendfile.ContextParameterType;
import org.talend.designer.core.model.utils.emf.talendfile.ContextType;
import org.talend.designer.core.model.utils.emf.talendfile.ProcessType;
import org.talend.designer.runprocess.ProcessorException;
import org.talend.designer.runprocess.ProcessorUtilities;
import org.talend.repository.RepositoryPlugin;
import org.talend.repository.constants.FileConstants;
import org.talend.repository.documentation.ExportFileResource;
import org.talend.repository.i18n.Messages;
import org.talend.repository.local.ExportItemUtil;
import org.talend.repository.model.IProxyRepositoryFactory;

/**
 * Manages the job scripts to be exported. <br/>
 * 
 * $Id: JobScriptsManager.java 1 2006-12-14 下�?�05:06:49 bqian
 * 
 */
public abstract class JobScriptsManager {

    protected static final String UNIX_LAUNCHER = "run.sh"; //$NON-NLS-1$

    protected static final String WINDOWS_LAUNCHER = "run.bat"; //$NON-NLS-1$

    protected static final String LIBRARY_FOLDER_NAME = "lib"; //$NON-NLS-1$

    protected static final String PATH_SEPARATOR = "/"; //$NON-NLS-1$

    public static final String ALL_ENVIRONMENTS = Messages.getString("JobPerlScriptsManager.allInterpreter"); //$NON-NLS-1$

    public static final String UNIX_ENVIRONMENT = "Unix"; //$NON-NLS-1$

    public static final String WINDOWS_ENVIRONMENT = "Windows"; //$NON-NLS-1$

    protected static final String JOB_SOURCE_FOLDER_NAME = "src"; //$NON-NLS-1$

    protected static final String JOB_ITEMS_FOLDER_NAME = "items"; //$NON-NLS-1$

    public static final String JOB_CONTEXT_FOLDER = "contexts"; //$NON-NLS-1$

    private String selectedJobVersion; //$NON-NLS-1$

    protected IProgressMonitor progressMonitor; // achen added to fix bug 0006222

    public Map<ExportChoice, Object> getDefaultExportChoiseMap() {
        Map<ExportChoice, Object> exportChoiceMap = new EnumMap<ExportChoice, Object>(ExportChoice.class);
        exportChoiceMap.put(ExportChoice.needLauncher, true);
        exportChoiceMap.put(ExportChoice.needSystemRoutine, true);
        exportChoiceMap.put(ExportChoice.needUserRoutine, true);
        exportChoiceMap.put(ExportChoice.needTalendLibraries, true);
        exportChoiceMap.put(ExportChoice.needJobItem, true);
        exportChoiceMap.put(ExportChoice.needJobScript, true);
        exportChoiceMap.put(ExportChoice.needContext, true);
        exportChoiceMap.put(ExportChoice.needSourceCode, true);
        exportChoiceMap.put(ExportChoice.applyToChildren, false);
        exportChoiceMap.put(ExportChoice.doNotCompileCode, false);
        return exportChoiceMap;
    }

    public void setProgressMonitor(IProgressMonitor progressMonitor) {
        this.progressMonitor = progressMonitor;
    }

    // bug 8720
    protected boolean isOptionChoosed(Map<ExportChoice, Object> exportChoice, Object key) {
        if (key != null) {
            final Object object = exportChoice.get(key);
            if (object instanceof Boolean) {
                return BooleanUtils.isTrue((Boolean) object);
            }
        }
        return false;
    }

    /**
     * 
     * DOC Represent exportchoice <br/>
     * .
     * 
     * $Id: JobScriptsExportWizardPage.java 1 2007-1-31下�?�06:14:19 +0000 ylv $
     * 
     */
    public enum ExportChoice {
        needMetaInfo,
        needWEBXML,
        needCONFIGFILE,
        needAXISLIB,
        needWSDD,
        needWSDL,
        needLauncher,
        needSystemRoutine,
        needUserRoutine,
        needTalendLibraries,
        needJobItem,
        needJobScript,
        needSourceCode, // only usefull for Java, as source code is job script in Perl. Activated when needJobItem is
        // selected
        needContext,
        applyToChildren,
        doNotCompileCode,
        needDependencies,
        esbQueueMessageName,
        esbServiceName,
        esbCategory,
        esbExportType
    }

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

    public abstract List<ExportFileResource> getExportResources(ExportFileResource[] process,
            Map<ExportChoice, Object> exportChoiceMap, IContext context, String launcher, int statisticPort, int tracePort,
            String... codeOptions) throws ProcessorException;

    public abstract List<ExportFileResource> getExportResources(ExportFileResource[] process,
            Map<ExportChoice, Object> exportChoiceMap, String contextName, String launcher, int statisticPort, int tracePort,
            String... codeOptions) throws ProcessorException;

    protected String getTmpFolder() {
        String tmpFold = getTmpFolderPath();
        File f = new File(tmpFold);
        if (!f.exists()) {
            f.mkdir();
        }
        return tmpFold;
    }

    private String getTmpFolderPath() {
        String tmpFolder = System.getProperty("user.dir"); //$NON-NLS-1$
        tmpFolder = tmpFolder + "/talendExporter"; //$NON-NLS-1$
        return tmpFolder;
    }

    /**
     * Gets the perl launcher location.
     * 
     * @return
     */
    public String[] getLauncher() {
        String[] launchers = { ALL_ENVIRONMENTS, UNIX_ENVIRONMENT, WINDOWS_ENVIRONMENT };
        return launchers;
    }

    /**
     * 
     * Create launcher(s) and get url(s).
     * 
     * @param needLauncher
     * @param process
     * @param contextName
     * @param environment use JobScriptsManager.ALL_ENVIRONMENTS, JobScriptsManager.UNIX_ENVIRONMENT or
     * JobScriptsManager.WINDOWS_ENVIRONMENT
     * @param statisticPort TODO
     * @param tracePort TODO
     * @param codeOptions TODO
     * @return
     */
    protected List<URL> getLauncher(boolean needLauncher, ProcessItem process, String contextName, String environment,
            int statisticPort, int tracePort, String... codeOptions) {

        List<URL> list = new ArrayList<URL>();
        if (!needLauncher) {
            return list;
        }
        String processId = process.getProperty().getId();
        String windowsCmd = getCommandByTalendJob(Platform.OS_WIN32, processId, contextName, process.getProperty().getVersion(),
                statisticPort, tracePort, codeOptions);
        String unixCmd = getCommandByTalendJob(Platform.OS_LINUX, processId, contextName, process.getProperty().getVersion(),
                statisticPort, tracePort, codeOptions);
        String tmpFold = getTmpFolder();

        if (environment.equals(ALL_ENVIRONMENTS)) {
            createLauncherFile(process, list, unixCmd, UNIX_LAUNCHER, tmpFold);
            createLauncherFile(process, list, windowsCmd, WINDOWS_LAUNCHER, tmpFold);
        } else if (environment.equals(UNIX_ENVIRONMENT)) {
            createLauncherFile(process, list, unixCmd, UNIX_LAUNCHER, tmpFold);
        } else if (environment.equals(WINDOWS_ENVIRONMENT)) {
            createLauncherFile(process, list, windowsCmd, WINDOWS_LAUNCHER, tmpFold);
        }

        return list;
    }

    protected String getCommandByTalendJob(String targetPlatform, String processId, String context, String processVersion,
            int statisticPort, int tracePort, String... codeOptions) {
        String[] cmd = new String[] {};
        try {
            cmd = ProcessorUtilities.getCommandLine(targetPlatform, true, processId, context, processVersion, statisticPort,
                    tracePort, codeOptions);
        } catch (ProcessorException e) {
            ExceptionHandler.process(e);
        }
        StringBuffer sb = new StringBuffer();
        sb.append(""); //$NON-NLS-1$
        for (String s : cmd) {
            sb.append(s).append(' ');
        }

        String commandStr = CorePlugin.getDefault().getPreferenceStore().getString(ITalendCorePrefConstants.COMMAND_STR);
        String finalCommand = commandStr.replace(ITalendCorePrefConstants.DEFAULT_COMMAND_STR, sb.toString());
        return finalCommand;
    }

    protected String getCommandByTalendJob(String targetPlatform, ProcessItem processItem, String context, int statisticPort,
            int tracePort, String... codeOptions) {
        String[] cmd = new String[] {};
        try {
            cmd = ProcessorUtilities.getCommandLine(targetPlatform, true, processItem, context, statisticPort, tracePort,
                    codeOptions);
        } catch (ProcessorException e) {
            ExceptionHandler.process(e);
        }
        StringBuffer sb = new StringBuffer();
        sb.append(""); //$NON-NLS-1$
        for (String s : cmd) {
            sb.append(s).append(' ');
        }
        return sb.toString();
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

    /**
     * Deletes the temporary files.
     */
    public void deleteTempFiles() {
        String tmpFold = getTmpFolderPath();
        File file = new File(tmpFold);
        if (!file.exists() && !file.isDirectory()) {
            return;
        }
        deleteDirectory(file);
    }

    public void deleteDirectory(File dir) {
        File[] entries = dir.listFiles();
        int sz = entries.length;
        for (int i = 0; i < sz; i++) {
            if (entries[i].isDirectory()) {
                deleteDirectory(entries[i]);
            } else {
                entries[i].delete();
            }
        }
        dir.delete();
    }

    /**
     * 
     * Gets the set of current job's context.
     * 
     * @return a List of context names.
     * 
     */
    public abstract List<String> getJobContexts(ProcessItem processItem);

    /**
     * ftang Comment method "escapeFileNameSpace".
     * 
     * @param processItem
     * @return
     */
    protected String escapeFileNameSpace(ProcessItem processItem) {
        String jobName = processItem.getProperty().getLabel();
        return escapeSpace(jobName);
    }

    /**
     * ftang Comment method "escapeSpace".
     * 
     * @param name
     * @return
     */
    public String escapeSpace(String name) {
        return PerlResourcesHelper.escapeSpace(name);
    }

    /**
     * Generates the perl files.
     * 
     * @param needGenerateCode
     * @param contextName
     * @param process
     * @throws ProcessorException
     */
    protected void generateJobFiles(ProcessItem process, String contextName, boolean statistics, boolean trace,
            boolean applyContextToChildren) throws ProcessorException {
        ProcessorUtilities.generateCode(process, contextName, statistics, trace, applyContextToChildren);
    }

    /**
     * Generates the job files.
     * 
     * @param needGenerateCode
     * @param context
     * @param process
     * @throws ProcessorException
     */
    protected void generateJobFiles(ProcessItem process, IContext context, String version, boolean statistics, boolean trace,
            boolean applyContextToChildren, IProgressMonitor monitor) throws ProcessorException {
        ProcessorUtilities.generateCode(process, context, version, statistics, trace, applyContextToChildren, monitor);
    }

    /**
     * Generates the job files.
     * 
     * @param needGenerateCode
     * @param contextName
     * @param process
     * @throws ProcessorException
     */
    protected void generateJobFiles(ProcessItem process, String contextName, String version, boolean statistics, boolean trace,
            boolean applyContextToChildren, IProgressMonitor monitor) throws ProcessorException {
        ProcessorUtilities.generateCode(process, contextName, version, statistics, trace, applyContextToChildren, monitor);
    }

    protected IResource[] sourceResouces = null;

    private boolean isMultiNodes;

    protected void addNodeToResource(IResource[] resources, List<IResource> sourceFile) throws CoreException {

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

    protected void addJobItem(ExportFileResource[] resources, ProcessItem processItem, boolean needChoice,
            ExportFileResource curResource, String... selectedJobVersion) {
        List<URL> list = new ArrayList<URL>();
        if (needChoice) {
            try {
                Set<File> files = new ExportItemUtil().createLocalResources(new File(getTmpFolder()), processItem);
                for (File file : files) {
                    list.add(file.toURI().toURL());
                }
                curResource.addResources(JOB_SOURCE_FOLDER_NAME, list);
            } catch (Exception e) {
                ExceptionHandler.process(e);
            }
        }
    }

    /**
     * Gets resources' URL.
     * 
     * @param resources
     * @param fileNames
     * @return
     */
    protected List<URL> getResourcesURL(IResource[] resources, List<String> fileNames) {
        List<URL> list = new ArrayList<URL>();

        for (Iterator<String> iter = fileNames.iterator(); iter.hasNext();) {
            String name = iter.next();
            for (int i = 0; i < resources.length; i++) {
                IResource resource = resources[i];

                if (resource.getName().equals(name)) {
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

    protected void addToList(List<String> list, String o) {
        if (!list.contains(o)) {
            list.add(o);
        }
    }

    /**
     * ftang Comment method "setJobVersion".
     * 
     * @param selectedJobVersion
     */
    public void setJobVersion(String selectedJobVersion) {
        this.selectedJobVersion = selectedJobVersion;
    }

    /**
     * ftang Comment method "getSelectedJobVersion".
     * 
     * @return
     */
    public String getSelectedJobVersion() {
        return this.selectedJobVersion;
    }

    /**
     * ftang Comment method "setMultiNodes".
     * 
     * @param b
     */
    public void setMultiNodes(boolean isMultiNodes) {
        this.isMultiNodes = isMultiNodes;
    }

    /**
     * ftang Comment method "setMultiNodes".
     * 
     * @param b
     */
    public boolean isMultiNodes() {
        return this.isMultiNodes;
    }

    protected IPath getEmfFileRootPath(Item item) throws Exception {
        IPath root = getCorrespondingProjectRootPath(item).append(
                ERepositoryObjectType.getFolderName(ERepositoryObjectType.PROCESS));
        return root;
    }

    protected IPath getEmfContextRootPath(Item item) throws Exception {
        IPath root = getCorrespondingProjectRootPath(item).append(
                ERepositoryObjectType.getFolderName(ERepositoryObjectType.CONTEXT));
        return root;
    }

    /**
     * ggu Comment method "getCorrespondingProjectRootPath".
     * 
     * if item is null, will return currrent probject path.
     */
    protected IPath getCorrespondingProjectRootPath(Item item) throws Exception {
        org.talend.core.model.properties.Project p = CorePlugin.getDefault().getProxyRepositoryFactory().getProject(item);
        IProject project = null;
        if (p != null) {
            project = ResourcesPlugin.getWorkspace().getRoot().getProject(p.getTechnicalLabel().toUpperCase());
            if (project != null) {
                return project.getLocation();
            }
        }
        // maybe, not used
        project = RepositoryPlugin.getDefault().getRunProcessService().getProject(LanguageManager.getCurrentLanguage());
        IPath root = project.getParent().getLocation().append(getCorrespondingProjectName(item).toUpperCase());
        return root;
    }

    /**
     * Gets current project name.
     * 
     * @param item TODO
     * 
     * @return
     */
    protected abstract String getCorrespondingProjectName(Item item);

    /**
     * DOC qwei Comment method "addDepencies".
     */
    protected void addDependencies(ExportFileResource[] allResources, ProcessItem processItem, Boolean needDependencies,
            ExportFileResource resource) {
        if (!needDependencies) {
            return;
        }
        addContext(allResources, processItem, resource);
        addMetadata(allResources, processItem, resource);
    }

    /**
     * DOC qwei Comment method "addMetadata".
     * 
     * @param processItem
     * @param resource
     */
    private void addMetadata(ExportFileResource[] allResources, ProcessItem processItem, ExportFileResource resource) {
        IDesignerCoreService designerCoreService = CorePlugin.getDefault().getDesignerCoreService();
        if (designerCoreService == null) {
            return;
        }
        IProcess process = null;
        process = designerCoreService.getProcessFromProcessItem(processItem);
        if (process != null) {
            List<INode> nodes = (List<INode>) process.getGraphicalNodes();
            for (INode node : nodes) {
                List<IElementParameter> eleParams = (List<IElementParameter>) node.getElementParameters();
                for (IElementParameter elementParameter : eleParams) {
                    String repositoryMetadataId = ""; //$NON-NLS-1$
                    if (elementParameter.getName().equals("PROPERTY")) { //$NON-NLS-1$
                        repositoryMetadataId = (String) elementParameter.getChildParameters().get("REPOSITORY_PROPERTY_TYPE") //$NON-NLS-1$
                                .getValue();
                    }
                    if (elementParameter.getName().equals("SCHEMA")) { //$NON-NLS-1$
                        repositoryMetadataId = (String) elementParameter.getChildParameters().get("REPOSITORY_SCHEMA_TYPE") //$NON-NLS-1$
                                .getValue();
                    }
                    if (elementParameter.getName().equals("QUERYSTORE")) { //$NON-NLS-1$
                        repositoryMetadataId = (String) elementParameter.getChildParameters().get("REPOSITORY_QUERYSTORE_TYPE") //$NON-NLS-1$
                                .getValue();
                    }

                    if (repositoryMetadataId != null && !repositoryMetadataId.equals("")) { //$NON-NLS-1$
                        String[] id = repositoryMetadataId.split(" - "); //$NON-NLS-1$
                        if (id.length > 0) {
                            repositoryMetadataId = id[0];
                            try {
                                IProxyRepositoryFactory factory = CorePlugin.getDefault().getProxyRepositoryFactory();
                                IRepositoryObject lastVersion = factory.getLastVersion(repositoryMetadataId.trim());
                                if (lastVersion != null) {
                                    Item item2 = lastVersion.getProperty().getItem();
                                    if (item2 != null) {
                                        ERepositoryObjectType itemType = ERepositoryObjectType.getItemType(item2);
                                        IPath typeFolderPath = new Path(ERepositoryObjectType.getFolderName(itemType));
                                        String metadataName = item2.getProperty().getLabel();
                                        String metadataVersion = item2.getProperty().getVersion();
                                        String metadataPath = item2.getState().getPath();

                                        metadataPath = metadataPath == null || metadataPath.equals("") ? "" : metadataPath; //$NON-NLS-1$ //$NON-NLS-2$
                                        IPath projectRootPath = getCorrespondingProjectRootPath(item2);
                                        String projectName = getCorrespondingProjectName(item2);
                                        // project file
                                        IPath projectFilePath = getCorrespondingProjectRootPath(item2).append(
                                                FileConstants.LOCAL_PROJECT_FILENAME);
                                        checkAndAddProjectResource(allResources, resource, JOB_ITEMS_FOLDER_NAME + PATH_SEPARATOR
                                                + projectName, FileLocator.toFileURL(projectFilePath.toFile().toURL()));

                                        IPath itemFilePath = projectRootPath.append(typeFolderPath).append(metadataPath).append(
                                                metadataName + "_" + metadataVersion + "." + FileConstants.ITEM_EXTENSION); //$NON-NLS-1$ //$NON-NLS-2$
                                        IPath propertiesFilePath = projectRootPath.append(typeFolderPath).append(metadataPath)
                                                .append(metadataName + "_" + metadataVersion + "." //$NON-NLS-1$ //$NON-NLS-2$
                                                        + FileConstants.PROPERTIES_EXTENSION);
                                        List<URL> metadataNameFileUrls = new ArrayList<URL>();
                                        metadataNameFileUrls.add(FileLocator.toFileURL(itemFilePath.toFile().toURL()));
                                        metadataNameFileUrls.add(FileLocator.toFileURL(propertiesFilePath.toFile().toURL()));
                                        String basePath = JOB_ITEMS_FOLDER_NAME + PATH_SEPARATOR + projectName + PATH_SEPARATOR
                                                + typeFolderPath.toOSString();
                                        resource.addResources(basePath, metadataNameFileUrls);
                                    }
                                }
                            } catch (Exception e) {
                                ExceptionHandler.process(e);
                            }

                        }
                    }

                }

            }

        }
    }

    /**
     * DOC qwei Comment method "addContext".
     * 
     * @param processItem
     * @param resource
     */
    private void addContext(ExportFileResource[] allResources, ProcessItem processItem, ExportFileResource resource) {
        ProcessType process = processItem.getProcess();
        if (process != null) {
            ContextType contextType = (ContextType) process.getContext().get(0);
            for (ContextParameterType param : (List<ContextParameterType>) contextType.getContextParameter()) {
                String repositoryContextId = param.getRepositoryContextId();
                if (repositoryContextId != null && !"".equals(repositoryContextId)) { //$NON-NLS-1$
                    try {
                        IProxyRepositoryFactory factory = CorePlugin.getDefault().getProxyRepositoryFactory();
                        IRepositoryObject lastVersion = factory.getLastVersion(repositoryContextId);
                        if (lastVersion != null) {
                            Item item2 = lastVersion.getProperty().getItem();
                            String contextName = item2.getProperty().getLabel();
                            String contextVersion = item2.getProperty().getVersion();
                            String contextPath = item2.getState().getPath();

                            contextPath = contextPath == null || contextPath.equals("") ? "" : contextPath; //$NON-NLS-1$ //$NON-NLS-2$
                            IPath emfContextRootPath = getEmfContextRootPath(item2);
                            String projectName = getCorrespondingProjectName(item2);
                            // project file
                            IPath projectFilePath = getCorrespondingProjectRootPath(item2).append(
                                    FileConstants.LOCAL_PROJECT_FILENAME);
                            checkAndAddProjectResource(allResources, resource, JOB_ITEMS_FOLDER_NAME + PATH_SEPARATOR
                                    + projectName, FileLocator.toFileURL(projectFilePath.toFile().toURL()));

                            IPath itemFilePath = emfContextRootPath.append(contextPath).append(
                                    contextName + "_" + contextVersion + "." + FileConstants.ITEM_EXTENSION); //$NON-NLS-1$ //$NON-NLS-2$
                            IPath propertiesFilePath = emfContextRootPath.append(contextPath).append(
                                    contextName + "_" + contextVersion + "." + FileConstants.PROPERTIES_EXTENSION); //$NON-NLS-1$ //$NON-NLS-2$
                            List<URL> contextFileUrls = new ArrayList<URL>();
                            contextFileUrls.add(FileLocator.toFileURL(itemFilePath.toFile().toURL()));
                            contextFileUrls.add(FileLocator.toFileURL(propertiesFilePath.toFile().toURL()));
                            String basePath = JOB_ITEMS_FOLDER_NAME + PATH_SEPARATOR + projectName + PATH_SEPARATOR
                                    + JOB_CONTEXT_FOLDER;
                            resource.addResources(basePath, contextFileUrls);
                        }
                    } catch (Exception e) {
                        ExceptionHandler.process(e);
                    }

                }
            }

        }
    }

    protected void checkAndAddProjectResource(ExportFileResource[] allResources, ExportFileResource curResource,
            String relativePath, URL projectURL) {
        if (allResources == null || curResource == null || projectURL == null) {
            return;
        }
        if (relativePath == null) {
            relativePath = ""; //$NON-NLS-1$
        }
        boolean found = false;
        for (ExportFileResource res : allResources) {
            Set<URL> urls = res.getResourcesByRelativePath(relativePath);
            if (urls != null && urls.contains(projectURL)) {
                found = true;
                break;
            }
        }
        if (!found) {
            List<URL> projectUrls = new ArrayList<URL>();
            projectUrls.add(projectURL);
            curResource.addResources(relativePath, projectUrls);
        }
    }

    /**
     * DOC Administrator Comment method "getJobContextsComboValue".
     * 
     * @param item
     * @return
     */
    public List<String> getJobContextsComboValue(ProcessItem item) {
        // TODO Auto-generated method stub
        return null;
    }
}
