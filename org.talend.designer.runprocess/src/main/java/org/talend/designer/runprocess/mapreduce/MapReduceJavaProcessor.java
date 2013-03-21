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
import java.io.FileFilter;
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
import org.talend.core.model.components.ComponentCategory;
import org.talend.core.model.process.IElementParameter;
import org.talend.core.model.process.IProcess;
import org.talend.core.model.process.JobInfo;
import org.talend.core.model.properties.ProcessItem;
import org.talend.core.model.properties.Property;
import org.talend.core.prefs.ITalendCorePrefConstants;
import org.talend.designer.core.model.components.EParameterName;
import org.talend.designer.runprocess.IProcessMessageManager;
import org.talend.designer.runprocess.IProcessor;
import org.talend.designer.runprocess.LastGenerationInfo;
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
        unzipFolder = unzipAndDeploy(process, archive);
        // Step 3: Run Map/Reduce job.

        return super.exec(Level.INFO, statisticsPort, tracePort);

    }

    @Override
    protected String getLibFolderInWorkingDir() {
        boolean isExported = ProcessorUtilities.isExportConfig();
        if (!isExported) {
            return unzipFolder + File.separator + "lib" + File.separator; //$NON-NLS-1$
        } else {
            return "$ROOT_PATH/../lib/";
        }
    }

    protected String getRootWorkingDir() {
        boolean isExported = ProcessorUtilities.isExportConfig();
        if (!isExported) {
            return unzipFolder + File.separator;
        } else {
            return "$ROOT_PATH" + File.separator;
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
                + "/mr_export_" + process.getName() + ".zip"; //$NON-NLS-1$
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

    private String unzipAndDeploy(IProcess process, String archiveZipFileStr) {
        String unzipFolder = unzipProcess(process, archiveZipFileStr);
        return unzipFolder;
    }

    private String unzipProcess(IProcess process, String archiveZipFileStr) {
        // throws OozieJobDeployException {
        String jobName = process.getName();
        String tempFolder = null;
        if (archiveZipFileStr != null && !"".equals(archiveZipFileStr)) {
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

    protected List<String> makeUpCommandSegments() {
        List<String> commands = new ArrayList<String>();
        commands.addAll(extractAheadCommandSegments());
        commands.addAll(extractJavaCommandSegments());
        commands.addAll(extractCPCommandSegments());
        commands.add(extractMainClassSegments());
        commands.addAll(extractArgumentSegments());
        return commands;
    }

    protected List<String> extractAheadCommandSegments() {
        List<String> aheadSegments = new ArrayList<String>();
        if (ProcessorUtilities.isExportConfig()) {
            if (Platform.WS_WIN32 != Platform.getWS()) {
                aheadSegments.add("cd `dirname $0`\n"); //$NON-NLS-1$
                aheadSegments.add("ROOT_PATH=`pwd`\n"); //$NON-NLS-1$
            } else {
                aheadSegments.add("%~d0\r\n"); //$NON-NLS-1$
                aheadSegments.add("cd %~dp0\r\n"); //$NON-NLS-1$
            }
        }
        return aheadSegments;
    }

    /**
     * Extracts the segment of java part, like "Java -Xms256m", with some jvm arguments by invoking
     * {@link #extractJavaVMArguments()}. Added by Marvin Wang on Mar 20, 2013.
     * 
     * @return
     */
    protected List<String> extractJavaCommandSegments() {
        List<String> commandSegments = new ArrayList<String>();
        String command = "java"; //$NON-NLS-1$
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

    /**
    * 
    */
    @Override
    protected List<String> extractCPCommandSegments() {
        List<String> cpCommandSegments = new ArrayList<String>();
        cpCommandSegments.add(ProcessorConstants.CMD_KEY_WORD_CP);
        cpCommandSegments.add(makeUpClassPathString());
        return cpCommandSegments;
    }

    @Override
    protected String extractMainClassSegments() {
        IPath classPath = getCodePath().removeFirstSegments(1);
        String className = classPath.toString().replace('/', '.');
        return className;
    }

    @Override
    protected List<String> extractArgumentSegments() {
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

    protected String makeUpClassPathString() {
        String rootPath = getRootWorkingDir();
        String libPath = getLibFolderInWorkingDir();
        StringBuffer sb = new StringBuffer("");//$NON-NLS-1$
        Set<String> libs = extractAllLibs();
        for (String lib : libs) {
            sb.append(libPath).append(lib);
            sb.append(JavaUtils.JAVA_CLASSPATH_SEPARATOR);
        }
        sb.append("."); //$NON-NLS-1$
        sb.append(JavaUtils.JAVA_CLASSPATH_SEPARATOR);

        Set<String> jobJars = extractJobJars();
        if (jobJars != null && jobJars.size() > 0) {
            for (String jobJar : jobJars) {
                sb.append(rootPath).append(process.getName()).append(File.separator);
                sb.append(jobJar);
                sb.append(JavaUtils.JAVA_CLASSPATH_SEPARATOR);
            }
        }

        return sb.substring(0, sb.length() - 1);
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
     * 
     * Added by Marvin Wang on Mar 20, 2013.
     * 
     * @return
     */
    protected Set<String> extractJobJars() {
        Set<String> set = new HashSet<String>();
        String jobFolderString = getRootWorkingDir() + process.getName();
        File jobFolder = new File(jobFolderString);
        File[] jarFiles = jobFolder.listFiles(new FileFilter() {

            @Override
            public boolean accept(File pathname) {
                if (pathname.getName().endsWith(".jar")) {//$NON-NLS-1$
                    return true;
                }
                return false;
            }
        });
        if (jarFiles != null && jarFiles.length > 0) {
            for (File tmpFile : jarFiles) {
                set.add(tmpFile.getName());
            }
        }
        return set;
    }

    @Override
    public String[] getCommandLine() throws ProcessorException {
        List<String> commandList = makeUpCommandSegments();
        String[] commandStr = commandList.toArray(new String[commandList.size()]);

        return commandStr;
    }

    @Deprecated
    public String[] getCommandLine1() throws ProcessorException {
        // java -cp libdirectory/*.jar;project_path classname;

        // init java interpreter
        String command;
        try {
            command = getInterpreter();
        } catch (ProcessorException e1) {
            command = "java"; //$NON-NLS-1$
        }
        // zli
        boolean win32 = false;
        String classPathSeparator;
        if (targetPlatform == null) {
            targetPlatform = Platform.getOS();
            win32 = Platform.OS_WIN32.equals(targetPlatform);
            classPathSeparator = JavaUtils.JAVA_CLASSPATH_SEPARATOR;
        } else {
            win32 = targetPlatform.equals(Platform.OS_WIN32);
            if (win32) {
                classPathSeparator = ";"; //$NON-NLS-1$
            } else {
                classPathSeparator = ":"; //$NON-NLS-1$
            }
        }
        boolean exportingJob = ProcessorUtilities.isExportConfig();

        Set<String> neededLibraries = JavaProcessorUtilities.getNeededLibrariesForProcess(process);
        if (!exportingJob) {
            JavaProcessorUtilities.addLog4jToJarList(neededLibraries);
        }
        JavaProcessorUtilities.checkJavaProjectLib(neededLibraries);

        String unixRootPathVar = "$ROOT_PATH"; //$NON-NLS-1$
        String unixRootPath = unixRootPathVar + "/"; //$NON-NLS-1$

        StringBuffer libPath = new StringBuffer();
        File libDir = new File(unzipFolder + File.separator + "lib");
        File[] jarFiles = libDir.listFiles(FilesUtils.getAcceptJARFilesFilter());
        if (jarFiles != null && jarFiles.length > 0) {
            for (File jarFile : jarFiles) {
                if (jarFile.isFile() && neededLibraries.contains(jarFile.getName())) {
                    if (!win32 && exportingJob) {
                        libPath.append(unixRootPath);
                    }
                    String singleLibPath = new Path(jarFile.getAbsolutePath()).toPortableString();
                    if (exportingJob) {
                        singleLibPath = singleLibPath.replace(new Path(libDir.getAbsolutePath()).toPortableString(), "../lib");
                    }
                    libPath.append(singleLibPath).append(classPathSeparator);
                }
            }
        }

        libPath.append(libDir.getAbsolutePath() + File.separator + "systemRoutines.jar").append(classPathSeparator);
        libPath.append(libDir.getAbsolutePath() + File.separator + "userRoutines.jar").append(classPathSeparator);

        List<String> jobJars = extractJobJar();
        if (jobJars != null && jobJars.size() > 0) {
            for (String jobJar : jobJars) {
                libPath.append(jobJar).append(classPathSeparator);
            }
        }

        if (!exportingJob) {
            libPath.append(".").append(classPathSeparator);
        }

        // init project_path
        String projectPath;
        if (exportingJob) {
            projectPath = getCodeLocation();
            if (projectPath != null) {
                projectPath = projectPath.replace(ProcessorUtilities.TEMP_JAVA_CLASSPATH_SEPARATOR, classPathSeparator);
            }
        } else {
            // IFolder classesFolder = JavaProcessorUtilities.getJavaProject().getProject()
            // .getFolder(JavaUtils.JAVA_CLASSES_DIRECTORY);
            // IPath projectFolderPath = classesFolder.getFullPath().removeFirstSegments(1);
            // projectPath =
            // Path.fromOSString(getCodeProject().getLocation().toOSString()).append(projectFolderPath).toOSString()
            // + classPathSeparator;
        }

        // init class name
        IPath classPath = getCodePath().removeFirstSegments(1);
        String className = classPath.toString().replace('/', '.');

        String exportJar = ""; //$NON-NLS-1$
        if (exportingJob) {
            String version = ""; //$NON-NLS-1$
            if (process.getVersion() != null) {
                version = "_" + process.getVersion(); //$NON-NLS-1$
                version = version.replace(".", "_"); //$NON-NLS-1$ //$NON-NLS-2$
            }

            exportJar = classPathSeparator
                    + (!win32 && exportingJob ? unixRootPath : "") + process.getName().toLowerCase() + version + ".jar" + classPathSeparator; //$NON-NLS-1$

            JobInfo lastMainJob = LastGenerationInfo.getInstance().getLastMainJob();
            Set<JobInfo> infos = null;
            if (lastMainJob == null && property != null) {
                infos = ProcessorUtilities.getChildrenJobInfo((ProcessItem) property.getItem());
            } else {
                infos = LastGenerationInfo.getInstance().getLastGeneratedjobs();
            }
            for (JobInfo jobInfo : infos) {
                if (lastMainJob != null && lastMainJob.equals(jobInfo)) {
                    continue;
                }
                if (jobInfo.getJobVersion() != null) {
                    version = "_" + jobInfo.getJobVersion(); //$NON-NLS-1$
                    version = version.replace(".", "_"); //$NON-NLS-1$ //$NON-NLS-2$
                }
                exportJar += (!win32 && exportingJob ? unixRootPath : "") + jobInfo.getJobName().toLowerCase() + version + ".jar" + classPathSeparator; //$NON-NLS-1$
            }
        }
        // TDI-17845:can't run job correctly in job Conductor
        String libFolder = ""; //$NON-NLS-1$
        // libFolder = new Path(libDir.getAbsolutePath()).toPortableString() + classPathSeparator;
        if (exportingJob) {
            String tmp = this.getCodeLocation();
            tmp = tmp.replace(ProcessorUtilities.TEMP_JAVA_CLASSPATH_SEPARATOR, classPathSeparator);
            libFolder = new Path(tmp) + classPathSeparator;
        } else {
            libFolder = new Path(libDir.getAbsolutePath()).toPortableString() + classPathSeparator;
        }

        String portableCommand = new Path(command).toPortableString();
        // String portableProjectPath = new Path(projectPath).toPortableString();

        // if (!win32 && exportingJob) {
        // portableProjectPath = unixRootPathVar + classPathSeparator + portableProjectPath;
        //
        // String libraryPath = ProcessorUtilities.getLibraryPath();
        // if (libraryPath != null) {
        // portableProjectPath = portableProjectPath.replace(libraryPath, unixRootPath + libraryPath);
        // libFolder = libFolder.replace(libraryPath, unixRootPath + libraryPath);
        // }
        //
        // }
        String[] strings;

        List<String> tmpParams = new ArrayList<String>();
        tmpParams.add(portableCommand);

        String[] proxyParameters = getProxyParameters();
        if (proxyParameters != null && proxyParameters.length > 0) {
            for (String str : proxyParameters) {
                tmpParams.add(str);
            }
        }
        tmpParams.add("-cp"); //$NON-NLS-1$
        // if (exportingJob) {
        // tmpParams.add(libPath.toString() + portableProjectPath + exportJar);
        // } else {
        // tmpParams.add(libPath.toString() + portableProjectPath + exportJar + libFolder);
        // }
        tmpParams.add(libPath.toString());
        tmpParams.add(className);
        strings = tmpParams.toArray(new String[0]);

        // If the current job is m/r job, then add the argument "-libjars *.jar" for mapper and reducer method if
        // required.
        if (process.getComponentsType().equals(ComponentCategory.CATEGORY_4_MAPREDUCE.getName())) {
            strings = addMapReduceJobCommands(strings);
        }

        String[] cmd2 = addVMArguments(strings, exportingJob);
        // achen modify to fix 0001268
        if (!exportingJob) {
            return cmd2;
        } else {
            List<String> list = new ArrayList<String>();
            if (":".equals(classPathSeparator)) { //$NON-NLS-1$
                list.add("cd `dirname $0`\n"); //$NON-NLS-1$
                list.add("ROOT_PATH=`pwd`\n"); //$NON-NLS-1$
            } else {
                list.add("%~d0\r\n"); //$NON-NLS-1$
                list.add("cd %~dp0\r\n"); //$NON-NLS-1$
            }
            list.addAll(Arrays.asList(cmd2));
            return list.toArray(new String[0]);
        }
        // end
    }

    protected List<String> extractJobJar() {
        List<String> list = new ArrayList<String>();
        String jobFolderString = unzipFolder + File.separator + process.getName();
        File jobFolder = new File(jobFolderString);
        File[] jarFiles = jobFolder.listFiles(new FileFilter() {

            @Override
            public boolean accept(File pathname) {
                if (pathname.getName().endsWith(".jar")) {//$NON-NLS-1$
                    return true;
                }
                return false;
            }
        });
        if (jarFiles != null && jarFiles.length > 0) {
            for (File tmpFile : jarFiles) {
                list.add(tmpFile.getAbsolutePath() + extractClassPathSeparator());
            }
        }
        return list;
    }
}
