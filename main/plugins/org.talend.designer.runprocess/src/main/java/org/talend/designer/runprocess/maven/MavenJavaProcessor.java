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

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IPath;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.core.model.process.IProcess;
import org.talend.core.model.properties.Property;
import org.talend.designer.maven.model.MavenConstants;
import org.talend.designer.maven.template.CreateJobTemplateMavenPom;
import org.talend.designer.maven.template.MavenTemplateConstants;
import org.talend.designer.runprocess.ProcessorException;
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

    protected void generatePom() {
        final IPath srcCodePath = getSrcCodePath();
        final IProject codeProject = getCodeProject();

        IPath jobCurPath = srcCodePath.removeLastSegments(1);
        IFolder jobCurFolder = codeProject.getFolder(jobCurPath);
        IFile jobPomFile = jobCurFolder.getFile(MavenConstants.POM_FILE_NAME);

        if (jobPomFile.exists()) {// FIXME keep the old one? if no, remove this code.
            return;
        }

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

    protected void checkProjectPomModules() {
        IPath jobPomPath = this.getSrcCodePath().removeLastSegments(1);
        getTalendJavaProject().addJobModuleInProject(jobPomPath.toString());
    }
}
