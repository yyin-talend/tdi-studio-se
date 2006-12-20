// ============================================================================
//
// Talend Community Edition
//
// Copyright (C) 2006 Talend - www.talend.com
//
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either
// version 2.1 of the License, or (at your option) any later version.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
// Lesser General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with this program; if not, write to the Free Software
// Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
//
// ============================================================================
package org.talend.designer.codegen.model;

import java.io.File;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.emf.codegen.CodeGenPlugin;
import org.eclipse.jdt.core.JavaCore;

/**
 * DOC mhirt class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 */
public final class EmfEmittersPersistenceFactory {

    private static Logger log = Logger.getLogger(EmfEmittersPersistenceFactory.class);

    private static EmfEmittersPersistence singleton = null;

    private EmfEmittersPersistenceFactory() {
    }

    public static EmfEmittersPersistence getInstance() {
        if (singleton == null) {

            final IProject project = getJetProject();
            IFile iFile = null;
            if (project != null) {
                iFile = project.getFile("JetPersistence");
            }
            File file = iFile.getLocation().toFile();

            singleton = new EmfEmittersPersistence(file);
        }

        return singleton;
    }

    /**
     * DOC mhirt Comment method "getJetProject".
     * 
     * @return
     * @throws CoreException
     */
    private static IProject getJetProject() {
        IProject project = null;
        try {
            IProgressMonitor monitor = new NullProgressMonitor();
            IProgressMonitor progressMonitor = new SubProgressMonitor(monitor, 1);

            IWorkspace workspace = ResourcesPlugin.getWorkspace();
            project = workspace.getRoot().getProject(".JETEmitters");
            if (!project.exists()) {
                project.create(new SubProgressMonitor(progressMonitor, 1));
                progressMonitor.subTask(CodeGenPlugin.getPlugin().getString("_UI_JETCreatingProject_message",
                        new Object[] { project.getName() }));
                IProjectDescription description = workspace.newProjectDescription(project.getName());
                description.setNatureIds(new String[] { JavaCore.NATURE_ID });
                description.setLocation(null);
                project.open(new SubProgressMonitor(progressMonitor, 1));
                project.setDescription(description, new SubProgressMonitor(progressMonitor, 1));
            }
        } catch (CoreException e) {
            log.error(e.getMessage(), e);
            project = null;
        }
        return project;
    }

}
