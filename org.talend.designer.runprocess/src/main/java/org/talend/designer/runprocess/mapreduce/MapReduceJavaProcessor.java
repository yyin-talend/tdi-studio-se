// ============================================================================
//
// Copyright (C) 2006-2013 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.designer.runprocess.mapreduce;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Level;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.talend.commons.exception.CommonExceptionHandler;
import org.talend.commons.utils.generation.JavaUtils;
import org.talend.commons.utils.io.FilesUtils;
import org.talend.core.CorePlugin;
import org.talend.core.model.process.IElementParameter;
import org.talend.core.model.process.IProcess;
import org.talend.core.model.properties.ProcessItem;
import org.talend.core.model.properties.Property;
import org.talend.core.prefs.ITalendCorePrefConstants;
import org.talend.designer.core.model.components.EParameterName;
import org.talend.designer.runprocess.IProcessMessageManager;
import org.talend.designer.runprocess.IProcessor;
import org.talend.designer.runprocess.ProcessorConstants;
import org.talend.designer.runprocess.ProcessorException;
import org.talend.designer.runprocess.ProcessorUtilities;
import org.talend.designer.runprocess.RunProcessPlugin;
import org.talend.designer.runprocess.java.JavaProcessor;
import org.talend.designer.runprocess.java.JavaProcessorUtilities;
import org.talend.designer.runprocess.prefs.RunProcessPrefsConstants;
import org.talend.repository.documentation.ArchiveFileExportOperationFullPath;
import org.talend.repository.documentation.ExportFileResource;
import org.talend.repository.ui.utils.ZipToFile;
import org.talend.repository.ui.wizards.exportjob.JavaJobScriptsExportWSWizardPage.JobExportType;
import org.talend.repository.ui.wizards.exportjob.scriptsmanager.JobScriptsManager;
import org.talend.repository.ui.wizards.exportjob.scriptsmanager.JobScriptsManager.ExportChoice;
import org.talend.repository.ui.wizards.exportjob.scriptsmanager.JobScriptsManagerFactory;

/**
 * <pre>
 * This is a java processor for map/reduce job to run. For map/reduce job the way to run is not the same as common job.
 * The following is the steps to run map/reduce job:    
 * <li>1. Build a zip exported with m/r job name like "mrJob.zip" by {@link #buildExportZip(ProcessItem, IProgressMonitor)}.
 * <li>2. Unzip the zip file by {@link #unzipAndDeploy(IProcess, String)}.
 * <li>3. Get the commands by {@link #getCommandLine()} to execute job.
 * </pre>
 * 
 * Created by Marvin Wang on Mar 5, 2013.
 */
public class MapReduceJavaProcessor extends JavaProcessor {

    private String unzipFolder;

    /**
     * DOC marvin RemoteMapReduceJavaProcessor constructor comment.
     * 
     * @param process
     * @param property
     * @param filenameFromLabel
     */
    public MapReduceJavaProcessor(IProcess process, Property property, boolean filenameFromLabel) {
        super(process, property, filenameFromLabel);
    }

    @Override
    public Process run(int statisticsPort, int tracePort, String watchParam, IProgressMonitor monitor,
            IProcessMessageManager processMessageManager) throws ProcessorException {
        ProcessItem processItem = (ProcessItem) property.getItem();
        // Step 1: Export Map/Reduce job
        String archive = buildExportZip(processItem, new NullProgressMonitor());
        // Step 2: Deploy in local(Maybe just unpack)
        unzipFolder = unzipAndDeploy(archive);
        // Step 3: Run Map/Reduce job.

        return super.exec(Level.INFO, statisticsPort, tracePort);

    }

    @Override
    protected String getLibFolderInWorkingDir() {
        boolean isExported = ProcessorUtilities.isExportConfig();
        if (!isExported) {
            return unzipFolder + File.separator + ProcessorConstants.CMD_KEY_WORD_LIB + File.separator;
        } else {
            if (targetPlatform == null) {
                if (Platform.getOS().contains(Platform.WS_WIN32)) {
                    // ../lib
                    return ProcessorConstants.CMD_KEY_WORD_TWO_DOT + File.separator + ProcessorConstants.CMD_KEY_WORD_LIB
                            + File.separator;
                } else {
                    // "$ROOT_PATH/../lib";
                    return ProcessorConstants.CMD_KEY_WORD_ROOTPATH + File.separator + ProcessorConstants.CMD_KEY_WORD_TWO_DOT
                            + File.separator + ProcessorConstants.CMD_KEY_WORD_LIB + File.separator;
                }
            } else {
                if (Platform.OS_WIN32.equals(targetPlatform)) {
                    // ../lib
                    return ProcessorConstants.CMD_KEY_WORD_TWO_DOT + File.separator + ProcessorConstants.CMD_KEY_WORD_LIB
                            + File.separator;
                } else {
                    // "$ROOT_PATH/../lib";
                    return ProcessorConstants.CMD_KEY_WORD_ROOTPATH + File.separator + ProcessorConstants.CMD_KEY_WORD_TWO_DOT
                            + File.separator + ProcessorConstants.CMD_KEY_WORD_LIB + File.separator;
                }
            }

        }
    }

    /**
     * Returns the root path. Added by Marvin Wang on Mar 22, 2013.
     * 
     * @return
     */
    protected String getRootWorkingDir() {
        boolean isExported = ProcessorUtilities.isExportConfig();
        if (!isExported) {
            return unzipFolder + File.separator;
        } else {
            if (targetPlatform == null) {
                if (Platform.getOS().contains(Platform.WS_WIN32)) {
                    // empty
                    return ""; //$NON-NLS-1$
                } else {
                    // "$ROOT_PATH/";
                    return ProcessorConstants.CMD_KEY_WORD_ROOTPATH + File.separator;
                }
            } else {
                if (Platform.OS_WIN32.equals(targetPlatform)) {
                    // empty
                    return ""; //$NON-NLS-1$
                } else {
                    // "$ROOT_PATH/";
                    return ProcessorConstants.CMD_KEY_WORD_ROOTPATH + File.separator;
                }
            }
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
        JobScriptsManager jobScriptsManager = JobScriptsManagerFactory.createManagerInstance(exportChoiceMap, processItem
                .getProcess().getDefaultContext(), JobScriptsManager.ALL_ENVIRONMENTS, IProcessor.NO_STATISTICS,
                IProcessor.NO_TRACES, JobExportType.POJO);
        String codeOptions = null;
        List<ExportFileResource> exportResources = jobScriptsManager.getExportResources(exportFileResources, codeOptions);

        if (progressMonitor.isCanceled()) {
            throw new ProcessorException(new InterruptedException());
        }

        final String archiveFilePath = Path.fromOSString(CorePlugin.getDefault().getPreferenceStore()
                .getString(ITalendCorePrefConstants.FILE_PATH_TEMP))
                + "/mr_export_" + process.getName() + ".zip"; //$NON-NLS-1$ //$NON-NLS-2$
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
        if (!ProcessorUtilities.isExportConfig()) {
            String fileEncoding = System.getProperty("file.encoding"); //$NON-NLS-1$
            String encodingFromIni = ProcessorConstants.CMD_KEY_WORD_FILEEMNCODING + "=" + fileEncoding; //$NON-NLS-1$

            if (fileEncoding != null) {
                Iterator<String> vmIt = vmArgsSegments.iterator();
                while (vmIt.hasNext()) {
                    String vmArg = vmIt.next();
                    if (vmArg.startsWith(ProcessorConstants.CMD_KEY_WORD_FILEEMNCODING)) {
                        vmIt.remove();
                    }
                }
                vmArgsSegments.add(encodingFromIni);
            }
        }
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
        IPath classPath = getCodePath().removeFirstSegments(1);
        String className = classPath.toString().replace('/', '.');
        return className;
    }

    @Override
    public List<String> extractArgumentSegments() {
        String nameNodeURI = (String) process.getElementParameter("NAMENODE").getValue();//$NON-NLS-1$
        String jobTrackerURI = (String) process.getElementParameter("JOBTRACKER").getValue();//$NON-NLS-1$
        List<String> list = new ArrayList<String>();

        list.add(ProcessorConstants.CMD_KEY_WORD_LIBJAR);
        StringBuffer libJars = new StringBuffer("");//$NON-NLS-1$
        Set<String> libNames = JavaProcessorUtilities.extractLibNamesOnlyForMapperAndReducer(process);
        if (libNames != null && libNames.size() > 0) {
            Iterator<String> itLibNames = libNames.iterator();
            while (itLibNames.hasNext()) {
                libJars.append(getLibFolderInWorkingDir() + itLibNames.next()).append(",");//$NON-NLS-1$
            }
        }
        list.add(libJars.substring(0, libJars.length() - 1));
        list.add(ProcessorConstants.CMD_KEY_WORD_FS);
        list.add(nameNodeURI == null ? "hdfs://localhost:8020" : nameNodeURI);//$NON-NLS-1$
        list.add(ProcessorConstants.CMD_KEY_WORD_JT);
        list.add(jobTrackerURI == null ? "localhost:50300" : jobTrackerURI);//$NON-NLS-1$

        return list;
    }

    /**
     * Makes up the class path string that should be like this
     * "[rootPath]/../lib/a.jar:[rootPath]/../lib/b.jar:[rootPath]/job.jar" in linux. In windows, ":" should be replaced
     * by ";". About the root path it depends on {@link #getRootWorkingDir()}. The job jar can be gotton by
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
        // Append the current directory.
        sb.append("."); //$NON-NLS-1$
        sb.append(getJavaClassSeparator());

        // Append root path to class path.
        if (ProcessorUtilities.isExportConfig()) {
            if (targetPlatform == null) {
                if (!Platform.getOS().contains(Platform.WS_WIN32)) {
                    sb.append(ProcessorConstants.CMD_KEY_WORD_ROOTPATH);
                    sb.append(getJavaClassSeparator());
                }
            } else {
                if (!Platform.OS_WIN32.equals(targetPlatform)) {
                    sb.append(ProcessorConstants.CMD_KEY_WORD_ROOTPATH);
                    sb.append(getJavaClassSeparator());
                }
            }
        }

        // Append job jar to class path.
        sb.append(getRootWorkingDir());
        if (!ProcessorUtilities.isExportConfig()) {
            sb.append(process.getName()).append(File.separator);
        }
        sb.append(makeupJobJarName());

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
        Set<String> neededLibraries = JavaProcessorUtilities.getNeededLibrariesForProcess(process);
        if (!ProcessorUtilities.isExportConfig()) {
            JavaProcessorUtilities.addLog4jToJarList(neededLibraries);
        }
        JavaProcessorUtilities.checkJavaProjectLib(neededLibraries);

        if (!ProcessorUtilities.isExportConfig()) {
            File libDir = new File(getLibFolderInWorkingDir());
            File[] jarFiles = libDir.listFiles(FilesUtils.getAcceptJARFilesFilter());
            if (jarFiles != null && jarFiles.length > 0) {
                for (File jarFile : jarFiles) {
                    if (jarFile.isFile() && neededLibraries.contains(jarFile.getName())) {
                        libsRequiredByJob.add(jarFile.getName());
                    }
                }
            }

        } else {
            libsRequiredByJob.addAll(neededLibraries);
        }

        libsRequiredByJob.add("systemRoutines.jar"); //$NON-NLS-1$
        libsRequiredByJob.add("userRoutines.jar"); //$NON-NLS-1$

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
     * Makes up a job jar name that should be like "aa_0_4.jar". Added by Marvin Wang on Mar 21, 2013.
     * 
     * @return
     */
    protected String makeupJobJarName() {
        String version = process.getVersion();
        String jobJarName = process.getName().toLowerCase() + "_" + version.replace(".", "_") + ".jar"; //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$ //$NON-NLS-4$
        return jobJarName;
    }

}
