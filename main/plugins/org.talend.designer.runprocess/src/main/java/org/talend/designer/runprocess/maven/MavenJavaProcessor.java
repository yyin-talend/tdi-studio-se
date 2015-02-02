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

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IPath;
import org.talend.core.model.process.IProcess;
import org.talend.core.model.properties.Property;
import org.talend.designer.runprocess.java.JavaProcessor;

/**
 * created by ggu on 2 Feb 2015 Detailled comment
 *
 */
public class MavenJavaProcessor extends JavaProcessor {

    public MavenJavaProcessor(IProcess process, Property property, boolean filenameFromLabel) {
        super(process, property, filenameFromLabel);
    }

    public void generatePom() {
        final IPath srcCodePath = getSrcCodePath();
        final IProject codeProject = getCodeProject();
        IPath jobCurPath = srcCodePath.removeLastSegments(1);
        IFolder jobCurFolder = codeProject.getFolder(jobCurPath);

        // generate the job pom
        // add into the project for module.
    }
}
