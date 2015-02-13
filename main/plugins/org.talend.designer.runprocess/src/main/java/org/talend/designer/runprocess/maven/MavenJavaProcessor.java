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

import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IPath;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.utils.generation.JavaUtils;
import org.talend.commons.utils.resource.FileExtensions;
import org.talend.core.model.process.IProcess;
import org.talend.core.model.process.JobInfo;
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
        generatePom();
        checkProjectPomModules();
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
        String exportJar = libPrefixPath + jarName + FileExtensions.JAR_FILE_SUFFIX;

        Set<JobInfo> infos = getBuildChildrenJobs();
        for (JobInfo jobInfo : infos) {
            String childJarName = JavaResourcesHelper.getJobJarName(jobInfo.getJobName(), jobInfo.getJobVersion());
            exportJar += classPathSeparator + libPrefixPath + childJarName + FileExtensions.JAR_FILE_SUFFIX;
        }
        return exportJar;

    }

    protected void checkProjectPomModules() {
        final ITalendProcessJavaProject talendJavaProject = getTalendJavaProject();
        // job self
        String jobModule = this.getSrcCodePath().removeLastSegments(1).toPortableString();
        talendJavaProject.addChildModules(jobModule);
    }
}
