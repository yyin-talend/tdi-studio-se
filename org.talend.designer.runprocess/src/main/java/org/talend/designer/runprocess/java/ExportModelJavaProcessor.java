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
package org.talend.designer.runprocess.java;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Level;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.talend.commons.utils.generation.JavaUtils;
import org.talend.core.model.components.ComponentCategory;
import org.talend.core.model.process.IProcess;
import org.talend.core.model.process.JobInfo;
import org.talend.core.model.properties.ProcessItem;
import org.talend.core.model.properties.Property;
import org.talend.designer.runprocess.IProcessMessageManager;
import org.talend.designer.runprocess.LastGenerationInfo;
import org.talend.designer.runprocess.ProcessorException;
import org.talend.designer.runprocess.ProcessorUtilities;
import org.talend.repository.ui.utils.ZipToFile;
import org.talend.repository.ui.wizards.exportjob.scriptsmanager.JobJavaScriptsManager;

/**
 * Created by wchen on Mar 5, 2013.
 */
public class ExportModelJavaProcessor extends JavaProcessor {

    private String unzipFolder;

    /**
     * DOC wchen ExportModelJavaProcessor constructor comment.
     * 
     * @param process
     * @param property
     * @param filenameFromLabel
     */
    public ExportModelJavaProcessor(IProcess process, Property property, boolean filenameFromLabel) {
        super(process, property, filenameFromLabel);
    }

    @Override
    public Process run(int statisticsPort, int tracePort, String watchParam, IProgressMonitor monitor,
            IProcessMessageManager processMessageManager) throws ProcessorException {
        ExportProcessorHelper helper = new ExportProcessorHelper();

        // export job
        String archive = helper.exportJob(this, statisticsPort, tracePort, watchParam, monitor);
        unzipFolder = unzipAndDeploy(process, archive);

        Process process = super.exec(Level.INFO, statisticsPort, tracePort);

        // delete tempfiles after exeute ??
        // FilesUtils.deleteFile(new File(archive), true);
        // FilesUtils.deleteFile(new File(unzipFolder), true);
        return process;

    }

    @Override
    protected String getLibFolderInWorkingDir() {
        return unzipFolder + File.separator + "lib";
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

    @Override
    public String[] getCommandLine() throws ProcessorException {
        boolean exportingJob = ProcessorUtilities.isExportConfig();
        // if (exportingJob) {
        // return super.getCommandLine();
        // }
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

        Set<String> neededLibraries = JavaProcessorUtilities.getNeededLibrariesForProcess(process);
        if (!exportingJob) {
            JavaProcessorUtilities.addLog4jToJarList(neededLibraries);
        }
        JavaProcessorUtilities.checkJavaProjectLib(neededLibraries);

        String unixRootPathVar = "$ROOT_PATH"; //$NON-NLS-1$
        String unixRootPath = unixRootPathVar + "/"; //$NON-NLS-1$

        StringBuffer libPath = new StringBuffer();
        for (String jarFile : neededLibraries) {
            if (!win32 && exportingJob) {
                libPath.append(unixRootPath);
            }
            String singleLibPath = getLibBasePath(exportingJob).append(jarFile).toPortableString();
            libPath.append(singleLibPath).append(classPathSeparator);
        }

        // init project_path
        String systemRoutinePath = getLibBasePath(exportingJob).append(JobJavaScriptsManager.SYSTEMROUTINE_JAR)
                .toPortableString();
        if (!win32 && exportingJob) {
            systemRoutinePath = unixRootPath + systemRoutinePath;
        }
        String userRoutinePath = getLibBasePath(exportingJob).append(JobJavaScriptsManager.USERROUTINE_JAR).toPortableString();
        if (!win32 && exportingJob) {
            userRoutinePath = unixRootPath + userRoutinePath;
        }
        String pigUdfPath = null;
        if (!process.getNeededPigudf().isEmpty()) {
            pigUdfPath = getLibBasePath(exportingJob).append(JobJavaScriptsManager.USERPIGUDF_JAR).toPortableString();
            if (!win32 && exportingJob) {
                pigUdfPath = unixRootPath + pigUdfPath;
            }
        }
        String routinePath = systemRoutinePath + classPathSeparator + userRoutinePath
                + (pigUdfPath == null ? "" : classPathSeparator + pigUdfPath);

        // init class name
        IPath classPath = getCodePath().removeFirstSegments(1);
        String className = classPath.toString().replace('/', '.');

        String exportJar = ""; //$NON-NLS-1$
        String version = ""; //$NON-NLS-1$
        if (process.getVersion() != null) {
            version = "_" + process.getVersion(); //$NON-NLS-1$
            version = version.replace(".", "_"); //$NON-NLS-1$ //$NON-NLS-2$
        }
        String baseJobPath = getExecuteBasePath(exportingJob).toPortableString();
        exportJar = classPathSeparator
                + (baseJobPath.length() == 0 ? "" : baseJobPath + "/")
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
            exportJar += (baseJobPath.length() == 0 ? "" : baseJobPath + "/") + (!win32 && exportingJob ? unixRootPath : "") + jobInfo.getJobName().toLowerCase() + version + ".jar" + classPathSeparator; //$NON-NLS-1$
        }

        String portableCommand = new Path(command).toPortableString();
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
        tmpParams.add(libPath.toString() + routinePath + exportJar + classPathSeparator + "." + classPathSeparator);
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

    private IPath getExecuteBasePath(boolean exportingJob) {
        IPath basePath = null;
        if (exportingJob) {
            basePath = new Path("");
        } else {
            if (unzipFolder != null) {
                basePath = new Path(unzipFolder);
                basePath = basePath.append(process.getName());
            } else {
                return new Path("");
            }
        }
        return basePath;
    }

    private IPath getLibBasePath(boolean exportingJob) {
        IPath basePath = null;
        if (exportingJob) {
            basePath = new Path("../lib");
        } else {
            if (unzipFolder != null) {
                basePath = new Path(unzipFolder);
                basePath = basePath.append("lib");
            } else {
                return new Path("");
            }
        }
        return basePath;
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
