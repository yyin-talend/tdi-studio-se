// ============================================================================
//
// Talend Community Edition
//
// Copyright (C) 2006-2007 Talend - www.talend.com
//
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either
// version 2.1 of the License.
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
package org.talend.designer.runprocess;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IStartup;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.exception.RuntimeExceptionHandler;
import org.talend.core.CorePlugin;

/**
 * Delete all the perl and java jobs when T.O.S start up.
 * 
 * yzhang class global comment. Detailled comment <br/>
 * 
 * $Id: DeleteAllJobWhenStartUp.java 下午02:50:26 2007-5-29 +0000 (2007-5-29) yzhang $
 * 
 */
public class DeleteAllJobWhenStartUp implements IStartup {

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.IStartup#earlyStartup()
     */
    public void earlyStartup() {

        IWorkspace workspace = ResourcesPlugin.getWorkspace();
        IWorkspaceRoot workspaceRoot = workspace.getRoot();

        IResource perlRecs = workspaceRoot.findMember(".Perl");
        if (perlRecs != null && perlRecs.getType() == IResource.PROJECT) {
            IProject perlProject = (IProject) perlRecs;
            try {
                IResource[] perlProResrouces = perlProject.members();
                for (int i = 0; i < perlProResrouces.length; i++) {
                    if (perlProResrouces[i].getType() == IResource.FILE
                            && "pl".equalsIgnoreCase(perlProResrouces[i].getFileExtension())) {
                        perlProResrouces[i].delete(true, null);
                    }
                }
            } catch (CoreException e) {
                RuntimeExceptionHandler.process(e);
            }
        }

        IResource javaRecs = workspaceRoot.findMember(".Java/src");
        if (javaRecs != null && javaRecs.getType() == IResource.FOLDER) {
            IFolder javaSrcFolder = (IFolder) javaRecs;
            try {
                IResource[] javaProRecs = javaSrcFolder.members();
                if (javaProRecs.length > 0) {
                    for (int i = 0; i < javaProRecs.length; i++) {
                        javaProRecs[i].delete(true, null);
                    }
                }
            } catch (CoreException e) {
                RuntimeExceptionHandler.process(e);
            }
        }

        // fix bug 1151, move the sync all routines here from JavaProcessor and PerlProcessor.
        if (CorePlugin.getDefault().getRepositoryService().isRCPMode()) {
            Display.getDefault().asyncExec(new Runnable() {

                public void run() {
                    try {
                        RunProcessPlugin.getDefault().getCodeGeneratorService().createRoutineSynchronizer()
                                .syncAllRoutines();
                    } catch (Exception e) {
                        ExceptionHandler.process(e);
                    }
                }
            });
        }
    }
}
