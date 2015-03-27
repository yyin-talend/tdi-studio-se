// ============================================================================
//
// Copyright (C) 2006-2015 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.designer.runprocess.maven;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.utils.generation.JavaUtils;
import org.talend.commons.utils.resource.FileExtensions;
import org.talend.core.model.process.IProcess;
import org.talend.core.model.process.JobInfo;
import org.talend.core.model.properties.ProcessItem;
import org.talend.core.model.properties.Property;
import org.talend.core.model.utils.JavaResourcesHelper;
import org.talend.core.runtime.process.ITalendProcessJavaProject;
import org.talend.designer.maven.model.MavenConstants;
import org.talend.designer.maven.template.CreateJobTemplateMavenPom;
import org.talend.designer.maven.template.MavenTemplateConstants;
import org.talend.designer.runprocess.ProcessorException;
import org.talend.designer.runprocess.ProcessorUtilities;
import org.talend.designer.runprocess.java.JavaProcessor;

/**
 * created by ggu on 2 Feb 2015 Detailled comment
 *
 */
public class MavenJavaProcessor extends JavaProcessor {

    public MavenJavaProcessor(IProcess process, Property property, boolean filenameFromLabel) {
        super(process, property, filenameFromLabel);
    }

    @Override
    public void generateCode(boolean statistics, boolean trace, boolean javaProperties) throws ProcessorException {
        super.generateCode(statistics, trace, javaProperties);
        if (property != null) { // only job, if Shadow Process, will be null.
            generatePom();
        }
    }

    @Override
    public Set<JobInfo> getBuildChildrenJobs() {
        if (buildChildrenJobs == null || buildChildrenJobs.isEmpty()) {
            buildChildrenJobs = new HashSet<JobInfo>();

            if (property != null) {
                Set<JobInfo> infos = ProcessorUtilities.getChildrenJobInfo((ProcessItem) property.getItem());
                for (JobInfo jobInfo : infos) {
                    buildChildrenJobs.add(jobInfo);
                }
            }
        }
        return this.buildChildrenJobs;
    }

    @Override
    protected String getBaseLibPath() {
        return JavaUtils.JAVA_LIB_DIRECTORY;
    }

    @Override
    public void initJobClasspath() {
        // FIXME, must make sure the exportConfig is true, and the classpath is same as export.
        // ProcessorUtilities.setExportConfig(label, false);
        String routinesJarPath = getBaseLibPath() + JavaUtils.PATH_SEPARATOR + JavaUtils.ROUTINE_JAR_NAME + '-'
                + JavaUtils.ROUTINE_JAR_DEFAULT_VERSION + FileExtensions.JAR_FILE_SUFFIX
                + ProcessorUtilities.TEMP_JAVA_CLASSPATH_SEPARATOR;
        ProcessorUtilities.setExportConfig(JavaUtils.JAVA_APP_NAME, routinesJarPath, getBaseLibPath());

        setClasspaths();

        ProcessorUtilities.resetExportConfig();
    }

    protected void generatePom() {
        final IPath srcCodePath = getSrcCodePath();
        final IProject codeProject = getCodeProject();

        IPath jobCurPath = srcCodePath.removeLastSegments(1);
        IFolder jobCurFolder = codeProject.getFolder(jobCurPath);
        IFile jobPomFile = jobCurFolder.getFile(MavenConstants.POM_FILE_NAME);

        // if (jobPomFile.exists()) {// FIXME keep the old one? if no, remove this code.
        // return;
        // }

        initJobClasspath();

        try {
            CreateJobTemplateMavenPom createTemplatePom = new CreateJobTemplateMavenPom(this, jobPomFile,
                    MavenTemplateConstants.JOB_TEMPLATE_FILE_NAME);
            // TODO when export, need same as JobJavaScriptsManager.getJobInfoFile
            createTemplatePom.setAddStat(false);
            createTemplatePom.setApplyContextToChild(false);

            createTemplatePom.setUnixClasspath(this.unixClasspath);
            createTemplatePom.setWindowsClasspath(this.windowsClasspath);

            createTemplatePom.setOverwrite(true);

            createTemplatePom.create(null);

        } catch (Exception e) {
            ExceptionHandler.process(e);
        }

    }

    @Override
    protected String getExportJarsStr() {
        // use the maven way for jar
        final String libPrefixPath = getLibPrefixPath(true);
        final String classPathSeparator = extractClassPathSeparator();

        String jarName = JavaResourcesHelper.getJobJarName(process.getName(), process.getVersion());
        String exportJar = libPrefixPath + getBaseLibPath() + JavaUtils.PATH_SEPARATOR + jarName + FileExtensions.JAR_FILE_SUFFIX;

        Set<JobInfo> infos = getBuildChildrenJobs();
        for (JobInfo jobInfo : infos) {
            String childJarName = JavaResourcesHelper.getJobJarName(jobInfo.getJobName(), jobInfo.getJobVersion());
            exportJar += classPathSeparator + libPrefixPath + getBaseLibPath() + JavaUtils.PATH_SEPARATOR + childJarName
                    + FileExtensions.JAR_FILE_SUFFIX;
        }
        return exportJar;

    }

    @Override
    public void build() {
        final ITalendProcessJavaProject talendJavaProject = getTalendJavaProject();
        try {
            // before build, remove all old error markers
            IFile jobSrcFile = talendJavaProject.getProject().getFile(this.getSrcCodePath());
            if (jobSrcFile.exists()) {
                jobSrcFile.refreshLocal(IResource.DEPTH_ZERO, null);
                jobSrcFile.deleteMarkers(IMarker.PROBLEM, true, IResource.DEPTH_ZERO);
            }
        } catch (CoreException e) {
            ExceptionHandler.process(e);
        }

        String[] jobswithChildren = getJobModules();

        talendJavaProject.addChildModules(true, jobswithChildren);

        // if (buildRoutinesOnce) { // use RoutinesMavenInstallLoginTask instead to build once
        /*
         * build each job module with children. If don't build the project level, maybe will be some problem for the
         * xmlMappins and log4j.xml file when run job.
         */
        // talendJavaProject.buildModules(MavenConstants.GOAL_COMPILE, jobswithChildren);
        // } else {
        // build project level.
        talendJavaProject.buildModules(MavenConstants.GOAL_COMPILE, null);
        // }
        // refresh
        try {
            // maybe will be more for the performance
            // talendJavaProject.getProject().refreshLocal(IResource.DEPTH_INFINITE, null);

            IFolder jobSrcFolder = talendJavaProject.getProject().getFolder(this.getSrcCodePath().removeLastSegments(1));
            if (jobSrcFolder.exists()) {
                jobSrcFolder.refreshLocal(IResource.DEPTH_INFINITE, null);
            }
            // IFolder jobClassFolder =
            // talendJavaProject.getProject().getFolder(this.getCompiledCodePath().removeLastSegments(1));
            // if (jobClassFolder.exists()) { //always false, because it's delete before and recreate. refresh whole
            // output folder instead.
            // jobClassFolder.refreshLocal(IResource.DEPTH_INFINITE, null);
            // }
            talendJavaProject.getOutputFolder().refreshLocal(IResource.DEPTH_INFINITE, null);

        } catch (CoreException e) {
            ExceptionHandler.process(e);
        }
    }

    private String[] getJobModules() {
        // find the children jobs for maven build
        Set<JobInfo> infos = getBuildChildrenJobs();
        JobInfo[] childrenJobs = infos.toArray(new JobInfo[0]);
        List<String> jobswithChildren = new ArrayList<String>();

        // add routines always.
        // if (!buildRoutinesOnce) { //RoutinesMavenInstallLoginTask
        // jobswithChildren.add(getRoutineModule());
        // }
        // src/main/java
        IPath srcRelativePath = this.getTalendJavaProject().getSrcFolder().getProjectRelativePath();
        String srcRootPath = srcRelativePath.toString();

        for (JobInfo child : childrenJobs) {
            ProcessItem processItem = child.getProcessItem();
            String childJobFolder = null;
            if (processItem != null) {
                childJobFolder = JavaResourcesHelper.getJobClassPackageFolder(processItem);
            } else {
                String projectFolderName = child.getProjectFolderName();
                childJobFolder = JavaResourcesHelper.getJobClassPackageFolder(projectFolderName, child.getJobName(),
                        child.getJobVersion());
            }
            jobswithChildren.add(srcRootPath + '/' + childJobFolder);
        }

        // the main job is last one.
        jobswithChildren.add(this.getSrcCodePath().removeLastSegments(1).toString());

        return jobswithChildren.toArray(new String[0]);
    }

    private String getRoutineModule() {
        // routine module
        IFolder routinesSrcFolder = this.getTalendJavaProject().getSrcFolder().getFolder(JavaUtils.JAVA_ROUTINES_DIRECTORY);
        String routineModule = routinesSrcFolder.getProjectRelativePath().toString();
        return routineModule;
    }
}
