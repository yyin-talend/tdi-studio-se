// ============================================================================
//
// Copyright (C) 2006-2014 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.designer.runprocess.bigdata;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Level;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.talend.commons.exception.CommonExceptionHandler;
import org.talend.commons.utils.generation.JavaUtils;
import org.talend.commons.utils.resource.FileExtensions;
import org.talend.core.CorePlugin;
import org.talend.core.model.general.ModuleNeeded;
import org.talend.core.model.process.IElementParameter;
import org.talend.core.model.process.IProcess;
import org.talend.core.model.properties.ProcessItem;
import org.talend.core.model.properties.Property;
import org.talend.core.model.utils.JavaResourcesHelper;
import org.talend.core.prefs.ITalendCorePrefConstants;
import org.talend.core.ui.export.ArchiveFileExportOperationFullPath;
import org.talend.designer.core.model.components.EParameterName;
import org.talend.designer.maven.tools.creator.CreateMavenBundleTemplatePom;
import org.talend.designer.maven.tools.creator.CreateMavenJobPom;
import org.talend.designer.maven.utils.PomIdsHelper;
import org.talend.designer.runprocess.IProcessMessageManager;
import org.talend.designer.runprocess.ProcessorConstants;
import org.talend.designer.runprocess.ProcessorException;
import org.talend.designer.runprocess.ProcessorUtilities;
import org.talend.designer.runprocess.RunProcessPlugin;
import org.talend.designer.runprocess.java.JavaProcessorUtilities;
import org.talend.designer.runprocess.maven.MavenJavaProcessor;
import org.talend.designer.runprocess.prefs.RunProcessPrefsConstants;
import org.talend.repository.documentation.ExportFileResource;
import org.talend.repository.ui.utils.ZipToFile;
import org.talend.repository.ui.wizards.exportjob.scriptsmanager.JobScriptsManager;
import org.talend.repository.ui.wizards.exportjob.scriptsmanager.JobScriptsManager.ExportChoice;
import org.talend.repository.ui.wizards.exportjob.scriptsmanager.JobScriptsManagerFactory;

/**
 * created by rdubois on 27 janv. 2015 Detailled comment
 *
 */
public abstract class BigDataJavaProcessor extends MavenJavaProcessor {

    protected String windowsAddition, unixAddition;

    protected String unzipFolder;

    /**
     * DOC rdubois BigDataJavaProcessor constructor comment.
     * 
     * @param process
     * @param property
     * @param filenameFromLabel
     */
    public BigDataJavaProcessor(IProcess process, Property property, boolean filenameFromLabel) {
        super(process, property, filenameFromLabel);
        this.setOldBuildJob(true);// for BD jobs, set to use old way by default.
    }

    protected abstract JobScriptsManager createJobScriptsManager(ProcessItem processItem,
            Map<ExportChoice, Object> exportChoiceMap);

    protected abstract String getFilePathPrefix();

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.core.runprocess.Processor#run(java.lang.String[], int, int,
     * org.eclipse.core.runtime.IProgressMonitor, org.talend.designer.runprocess.IProcessMessageManager)
     */
    @Override
    public Process run(String[] optionsParam, int statisticsPort, int tracePort, IProgressMonitor monitor,
            IProcessMessageManager processMessageManager) throws ProcessorException {
        ProcessItem processItem = (ProcessItem) property.getItem();
        // Step 1: Export Map/Reduce job
        String archive = buildExportZip(processItem, new NullProgressMonitor());
        // Step 2: Deploy in local(Maybe just unpack)
        unzipFolder = unzipAndDeploy(archive);
        // Step 3: Run Map/Reduce job from given folder.

        return super.execFrom(unzipFolder + File.separatorChar + process.getName(), Level.INFO, statisticsPort, tracePort,
                optionsParam);
    }

    @Override
    protected String getLibFolderInWorkingDir() {
        if (ProcessorUtilities.isExportConfig()) {
            if (targetPlatform == null) {
                if (Platform.getOS().contains(Platform.WS_WIN32)) {
                    // ../lib
                    return ProcessorConstants.CMD_KEY_WORD_TWO_DOT + ProcessorConstants.CMD_KEY_WORD_SLASH
                            + ProcessorConstants.CMD_KEY_WORD_LIB + ProcessorConstants.CMD_KEY_WORD_SLASH;
                } else {
                    // "$ROOT_PATH/../lib";
                    return ProcessorConstants.CMD_KEY_WORD_ROOTPATH + ProcessorConstants.CMD_KEY_WORD_SLASH
                            + ProcessorConstants.CMD_KEY_WORD_TWO_DOT + ProcessorConstants.CMD_KEY_WORD_SLASH
                            + ProcessorConstants.CMD_KEY_WORD_LIB + ProcessorConstants.CMD_KEY_WORD_SLASH;
                }
            } else {
                if (Platform.OS_WIN32.equals(targetPlatform)) {
                    // ../lib
                    return ProcessorConstants.CMD_KEY_WORD_TWO_DOT + ProcessorConstants.CMD_KEY_WORD_SLASH
                            + ProcessorConstants.CMD_KEY_WORD_LIB + ProcessorConstants.CMD_KEY_WORD_SLASH;
                } else {
                    // "$ROOT_PATH/../lib";
                    return ProcessorConstants.CMD_KEY_WORD_ROOTPATH + ProcessorConstants.CMD_KEY_WORD_SLASH
                            + ProcessorConstants.CMD_KEY_WORD_TWO_DOT + ProcessorConstants.CMD_KEY_WORD_SLASH
                            + ProcessorConstants.CMD_KEY_WORD_LIB + ProcessorConstants.CMD_KEY_WORD_SLASH;
                }
            }
        } else {
            // ../lib
            return ProcessorConstants.CMD_KEY_WORD_TWO_DOT + ProcessorConstants.CMD_KEY_WORD_SLASH
                    + ProcessorConstants.CMD_KEY_WORD_LIB + ProcessorConstants.CMD_KEY_WORD_SLASH;
        }
    }

    /**
     * Returns the root path. Added by Marvin Wang on Mar 22, 2013.
     * 
     * @return
     */
    protected String getRootWorkingDir() {
        if (ProcessorUtilities.isExportConfig()) {
            if (targetPlatform == null) {
                if (Platform.getOS().contains(Platform.WS_WIN32)) {
                    // empty
                    return ""; //$NON-NLS-1$
                } else {
                    // "$ROOT_PATH/";
                    return ProcessorConstants.CMD_KEY_WORD_ROOTPATH + ProcessorConstants.CMD_KEY_WORD_SLASH;
                }
            } else {
                if (Platform.OS_WIN32.equals(targetPlatform)) {
                    // empty
                    return ""; //$NON-NLS-1$
                } else {
                    // "$ROOT_PATH/";
                    return ProcessorConstants.CMD_KEY_WORD_ROOTPATH + ProcessorConstants.CMD_KEY_WORD_SLASH;
                }
            }
        } else {
            return ""; //$NON-NLS-1$
        }
    }

    private String buildExportZip(ProcessItem processItem, IProgressMonitor progressMonitor) throws ProcessorException {
        Map<ExportChoice, Object> exportChoiceMap = JobScriptsManagerFactory.getDefaultExportChoiceMap();
        exportChoiceMap.put(ExportChoice.needLauncher, false);
        exportChoiceMap.put(ExportChoice.needJobItem, false);
        exportChoiceMap.put(ExportChoice.needJobScript, true);
        exportChoiceMap.put(ExportChoice.needSourceCode, false);
        exportChoiceMap.put(ExportChoice.needContext, true);

        // IProcess2 process = findProcessFromRepository(processItem.getProperty().getId(),
        // processItem.getProperty().getVersion());

        String processName = processItem.getProperty().getLabel();

        ExportFileResource fileResource = new ExportFileResource(processItem, processName);

        ExportFileResource[] exportFileResources = new ExportFileResource[] { fileResource };

        if (progressMonitor.isCanceled()) {
            throw new ProcessorException(new InterruptedException());
        }
        // JobScriptsManager jobScriptsManager = JobScriptsManagerFactory.createManagerInstance(exportChoiceMap,
        // processItem
        // .getProcess().getDefaultContext(), JobScriptsManager.ALL_ENVIRONMENTS, IProcessor.NO_STATISTICS,
        // IProcessor.NO_TRACES, JobExportType.POJO);

        // Now only support the JobExportType.POJO, means "Autonomous job".
        JobScriptsManager jobScriptsManager = createJobScriptsManager(processItem, exportChoiceMap);
        String codeOptions = null;
        List<ExportFileResource> exportResources = jobScriptsManager.getExportResources(exportFileResources, codeOptions);

        if (progressMonitor.isCanceled()) {
            throw new ProcessorException(new InterruptedException());
        }

        final String archiveFilePath = Path.fromOSString(CorePlugin.getDefault().getPreferenceStore()
                .getString(ITalendCorePrefConstants.FILE_PATH_TEMP))
                + "/" + getFilePathPrefix() + "_" + process.getName() + ".zip"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        File exportZipFile = new File(archiveFilePath);
        if (!exportZipFile.getParentFile().exists()) {
            exportZipFile.getParentFile().mkdirs();
        }
        final ArchiveFileExportOperationFullPath exporterOperation = new ArchiveFileExportOperationFullPath(exportResources,
                archiveFilePath);
        exporterOperation.setCreateLeadupStructure(true);
        exporterOperation.setUseCompression(true);

        final IProgressMonitor subProgressMonitor = new SubProgressMonitor(progressMonitor, 1);

        if (progressMonitor.isCanceled()) {
            throw new ProcessorException(new InterruptedException());
        }

        try {
            exporterOperation.run(subProgressMonitor);
        } catch (InvocationTargetException e) {
            throw new ProcessorException(e);
        } catch (InterruptedException e) {
            throw new ProcessorException(e);
        }

        // path can like name/name
        jobScriptsManager.deleteTempFiles();
        ProcessorUtilities.resetExportConfig();
        return archiveFilePath;
    }

    private String unzipAndDeploy(String archiveZipFileStr) {
        return unzipProcess(archiveZipFileStr);
    }

    private String unzipProcess(String archiveZipFileStr) {
        // throws OozieJobDeployException {
        String jobName = process.getName();
        String tempFolder = null;
        if (archiveZipFileStr != null && !"".equals(archiveZipFileStr)) { //$NON-NLS-1$
            File file = new File(archiveZipFileStr);
            tempFolder = file.getParentFile().getPath() + File.separator + jobName;
            try {
                ZipToFile.unZipFile(archiveZipFileStr, tempFolder);
            } catch (Exception e) {
                // throw new OozieJobDeployException("Can not unzip a file!", e);
            }
        }
        return tempFolder;
    }

    /**
     * This method gets the command list by the method {@link #makeUpCommandSegments()}.
     */
    @Override
    public String[] getCommandLine() throws ProcessorException {
        List<String> commandList = makeUpCommandSegments();
        return commandList.toArray(new String[commandList.size()]);
    }

    /**
     * <pre>
     * Makes all command segments up, like ahead commands, jvm commands, cp commands, main-class command, and others.
     * This method should be invoked by {@link #getCommandLine()}. The following is about some methods invoked by this
     * method. 
     * <li>{@link #extractAheadCommandSegments()} to extract ahead command segments like "cd `dirname $0`\n".
     * <li>{@link #extractJavaCommandSegments()} to extract java command segments like "java -Xms256M ". 
     * <li>{@link #extractCPCommandSegments()} to extract cp command segments like "cp ../a.jar:../b.jar..". 
     * <li>{@link #extractMainClassSegments()} to extract the main-class command segment. 
     * <li>{@link #extractArgumentSegments()} to extract other arguments for commands.
     * </pre>
     * 
     * Added by Marvin Wang on Mar 22, 2013.
     * 
     * @return
     */
    @Override
    protected List<String> makeUpCommandSegments() {
        return super.makeUpCommandSegments();
    }

    @Override
    public List<String> extractAheadCommandSegments() {
        List<String> aheadSegments = new ArrayList<String>();
        if (ProcessorUtilities.isExportConfig()) {
            if (Platform.OS_WIN32.equals(targetPlatform)) {
                aheadSegments.add("%~d0\r\n"); //$NON-NLS-1$
                aheadSegments.add("cd %~dp0\r\n"); //$NON-NLS-1$
            } else {
                aheadSegments.add("cd `dirname $0`\n"); //$NON-NLS-1$
                aheadSegments.add("ROOT_PATH=`pwd`\n"); //$NON-NLS-1$
            }
        }
        return aheadSegments;
    }

    @Override
    public List<String> extractJavaCommandSegments() {
        List<String> commandSegments = new ArrayList<String>();
        String command = ProcessorConstants.CMD_KEY_WORD_JAVA;
        try {
            command = getInterpreter();
        } catch (ProcessorException e1) {
            CommonExceptionHandler.process(e1);
        }

        commandSegments.add(command);
        commandSegments.addAll(extractJavaVMArguments());
        return commandSegments;
    }

    /**
     * Extracts all JVM arguments from job or preference store. It invokes by {@link #extractJavaCommandSegments()}.
     * Added by Marvin Wang on Mar 20, 2013.
     * 
     * @return
     */
    protected List<String> extractJavaVMArguments() {
        List<String> vmArgsSegments = new ArrayList<String>();
        String vmArgsString = ""; //$NON-NLS-1$
        // VMs from job or preference store
        if (this.process != null) {
            IElementParameter param = this.process.getElementParameter(EParameterName.JOB_RUN_VM_ARGUMENTS_OPTION.getName());
            if (param != null && param.getValue() instanceof Boolean && (Boolean) param.getValue()) { // checked
                param = this.process.getElementParameter(EParameterName.JOB_RUN_VM_ARGUMENTS.getName());
                if (param != null) {
                    vmArgsString = (String) param.getValue();
                }
            }
        }
        // if not check or the value is empty, should use preference
        if (vmArgsString == null || "".equals(vmArgsString)) { //$NON-NLS-1$
            vmArgsString = RunProcessPlugin.getDefault().getPreferenceStore().getString(RunProcessPrefsConstants.VMARGUMENTS);
        }

        String[] vmArgs = vmArgsString.trim().split(" "); //$NON-NLS-1$

        vmArgsSegments.addAll(Arrays.asList(vmArgs));
        return vmArgsSegments;
    }

    @Override
    public List<String> extractCPCommandSegments() {
        List<String> cpCommandSegments = new ArrayList<String>();
        cpCommandSegments.add(ProcessorConstants.CMD_KEY_WORD_CP);
        cpCommandSegments.add(makeUpClassPathString());
        return cpCommandSegments;
    }

    @Override
    public String extractMainClassSegments() {
        // IPath classPath = getCodePath().removeFirstSegments(1);
        // String className = classPath.toString().replace('/', '.');
        // return className;
        return super.extractMainClassSegments();
    }

    @Override
    public List<String> extractArgumentSegments() {
        List<String> list = new ArrayList<String>();
        list.add(ProcessorConstants.CMD_KEY_WORD_LIBJAR);
        StringBuffer libJars = new StringBuffer("");//$NON-NLS-1$
        Set<String> libNames = JavaProcessorUtilities.extractLibNamesOnlyForMapperAndReducer(process);
        if (!this.isOldBuildJob()) {
            libNames.remove(JavaUtils.SYSTEM_ROUTINE_JAR);
            libNames.remove(JavaUtils.USER_ROUTINE_JAR);
            libNames.add(JavaUtils.ROUTINE_JAR_NAME + FileExtensions.JAR_FILE_SUFFIX);
        }
        if (libNames != null && libNames.size() > 0) {
            Iterator<String> itLibNames = libNames.iterator();
            while (itLibNames.hasNext()) {
                libJars.append(getLibFolderInWorkingDir() + itLibNames.next()).append(",");//$NON-NLS-1$
            }
        }
        list.add(libJars.substring(0, libJars.length() - 1));
        return list;
    }

    /**
     * Makes up the class path string that should be like this
     * "[rootPath]/../lib/a.jar:[rootPath]/../lib/b.jar:[rootPath]/job.jar:" in linux. In windows, ":" should be
     * replaced by ";". About the root path it depends on {@link #getRootWorkingDir()}. The job jar can be gotton by
     * {@link #makeupJobJarName()}. Added by Marvin Wang on Mar 21, 2013.
     * 
     * @return
     */
    protected String makeUpClassPathString() {
        StringBuffer sb = new StringBuffer("");//$NON-NLS-1$
        Set<String> libs = extractAllLibs();
        for (String lib : libs) {
            sb.append(getLibFolderInWorkingDir()).append(lib);
            sb.append(getJavaClassSeparator());
        }
        boolean addUnixRootPath = false;

        // Append root path to class path.
        if (ProcessorUtilities.isExportConfig()) {
            if (targetPlatform == null) {
                if (!Platform.getOS().contains(Platform.WS_WIN32)) {
                    addUnixRootPath = true;
                }
            } else {
                if (!Platform.OS_WIN32.equals(targetPlatform)) {
                    addUnixRootPath = true;
                }
            }
        }
        if (addUnixRootPath) {
            sb.append(ProcessorConstants.CMD_KEY_WORD_ROOTPATH);
        } else { // add current always
            // Append the current directory.
            sb.append("."); //$NON-NLS-1$
        }
        sb.append(getJavaClassSeparator());

        // Append job jar to class path with ";" or ":".
        sb.append(getRootWorkingDir());
        sb.append(makeupJobJarName());
        sb.append(getJavaClassSeparator());

        return sb.toString();
    }

    /**
     * Returns all libs required by classpath, refer to this method {@link #extractCPCommandSegments()}. Added by Marvin
     * Wang on Mar 20, 2013.
     * 
     * @return
     */
    protected Set<String> extractAllLibs() {
        Set<String> libsRequiredByJob = new HashSet<String>();
        Set<ModuleNeeded> neededModules = JavaProcessorUtilities.getNeededModulesForProcess(process);
        if (!ProcessorUtilities.isExportConfig()) {
            JavaProcessorUtilities.addLog4jToModuleList(neededModules);
        }
        JavaProcessorUtilities.checkJavaProjectLib(neededModules);

        for (ModuleNeeded neededModule : neededModules) {
            libsRequiredByJob.add(neededModule.getModuleName());
        }
        if (isOldBuildJob()) {
            libsRequiredByJob.add(JavaUtils.SYSTEM_ROUTINE_JAR);
            libsRequiredByJob.add(JavaUtils.USER_ROUTINE_JAR);
        } else {
            // after maven only one jar for routine.
            libsRequiredByJob.add(JavaUtils.ROUTINE_JAR_NAME + FileExtensions.JAR_FILE_SUFFIX);
        }
        return libsRequiredByJob;
    }

    /**
     * Gets the separator for java class Added by Marvin Wang on Mar 22, 2013.
     * 
     * @return
     */
    protected String getJavaClassSeparator() {
        if (targetPlatform == null) {
            return JavaUtils.JAVA_CLASSPATH_SEPARATOR;
        } else {
            if (Platform.OS_WIN32.equals(targetPlatform)) {
                return ";"; //$NON-NLS-1$
            } else {
                return ":";//$NON-NLS-1$
            }
        }
    }

    /**
     * Makes up a job jar name that should be like "Test-0.1.jar" or "Test.jar". Added by Marvin Wang on Mar 21, 2013.
     * 
     * @return
     */
    protected String makeupJobJarName() {
        if (isOldBuildJob()) {
            String version = process.getVersion();
            String jobJarName = process.getName().toLowerCase() + "_" + version.replace(".", "_") + ".jar"; //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$ //$NON-NLS-4$
            return jobJarName;
        } else {
            // Test-0.1
            String jarName = JavaResourcesHelper.getJobJarName(process.getName(), process.getVersion());
            if (PomIdsHelper.FLAG_VERSION_WITH_CLASSIFIER) { // only jar name without version
                jarName = JavaResourcesHelper.escapeFileName(process.getName());
            }
            return jarName + FileExtensions.JAR_FILE_SUFFIX;
        }
    }

    protected void setValuesFromCommandline(String tp, String[] cmds) {
        super.setValuesFromCommandline(tp, cmds);

        String libJarStr = null;
        int libJarIndex = ArrayUtils.indexOf(cmds, ProcessorConstants.CMD_KEY_WORD_LIBJAR);
        if (libJarIndex > -1) { // found
            // return the cp values in the next index.
            libJarStr = cmds[libJarIndex + 1];
        }
        if (Platform.OS_WIN32.equals(tp)) {
            this.windowsAddition = libJarStr;
        } else {
            String unixRootPath = getLibPrefixPath(true);
            this.unixAddition = libJarStr.replace(unixRootPath, ""); // remove the Path root string
        }
    }

    protected CreateMavenBundleTemplatePom createMavenTemplatePom() {
        CreateMavenBundleTemplatePom createMavenTemplatePom = super.createMavenTemplatePom();
        if (createMavenTemplatePom instanceof CreateMavenJobPom) {
            CreateMavenJobPom createMavenJobPom = (CreateMavenJobPom) createMavenTemplatePom;

            if (!StringUtils.isBlank(this.windowsAddition)) {
                String windowsScriptAddition = createMavenJobPom.getWindowsScriptAddition();
                if (windowsScriptAddition != null && windowsScriptAddition.length() > 0) {
                    windowsScriptAddition += ' '; // add one space to separate existed additions.
                } else {
                    windowsScriptAddition = ""; //$NON-NLS-1$
                }
                windowsScriptAddition += ProcessorConstants.CMD_KEY_WORD_LIBJAR + ' ' + this.windowsAddition;
                createMavenJobPom.setWindowsScriptAddition(windowsScriptAddition);
            }
            if (!StringUtils.isBlank(this.unixAddition)) {
                String unixScriptAddition = createMavenJobPom.getUnixScriptAddition();
                if (unixScriptAddition != null && unixScriptAddition.length() > 0) {
                    unixScriptAddition += ' '; // add one space to separate existed additions.
                } else {
                    unixScriptAddition = ""; //$NON-NLS-1$
                }
                unixScriptAddition += ProcessorConstants.CMD_KEY_WORD_LIBJAR + ' ' + this.unixAddition;
                createMavenJobPom.setUnixCcriptAddition(unixScriptAddition);
            }
        }
        return createMavenTemplatePom;
    }
}
