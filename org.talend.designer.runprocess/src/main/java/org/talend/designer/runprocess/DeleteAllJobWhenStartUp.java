// ============================================================================
//
// Copyright (C) 2006-2010 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.designer.runprocess;

import java.io.File;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.ui.IStartup;
import org.talend.commons.ui.runtime.exception.RuntimeExceptionHandler;
import org.talend.commons.utils.generation.JavaUtils;
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

    public static boolean executed;

    public static final String PERL_PROJECT_NAME = ".Perl";

    private boolean startUnderPluginModel;

    public void startup(boolean pluginModel) {
        startUnderPluginModel = pluginModel;
        earlyStartup();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.IStartup#earlyStartup()
     */
    public void earlyStartup() {

        if (!startUnderPluginModel && !CorePlugin.getDefault().getRepositoryService().isRCPMode()) {
            return;
        }

        IWorkspace workspace = ResourcesPlugin.getWorkspace();
        IWorkspaceRoot workspaceRoot = workspace.getRoot();

        IResource perlRecs = workspaceRoot.findMember(PERL_PROJECT_NAME);
        if (perlRecs != null && perlRecs.getType() == IResource.PROJECT) {
            IProject perlProject = (IProject) perlRecs;
            try {
                if (!perlProject.isOpen()) {
                    perlProject.open(null);
                }
                IResource[] perlProResrouces = perlProject.members();
                for (int i = 0; i < perlProResrouces.length; i++) {
                    if (perlProResrouces[i].getType() == IResource.FILE
                            && "pl".equalsIgnoreCase(perlProResrouces[i].getFileExtension())) { //$NON-NLS-1$
                        perlProResrouces[i].delete(true, null);
                    }
                }
            } catch (CoreException e) {
                RuntimeExceptionHandler.process(e);
            }
        }
        IResource javaRecs = workspaceRoot.findMember(JavaUtils.JAVA_PROJECT_NAME);
        if (javaRecs != null && javaRecs.getType() == IResource.PROJECT) {
            IProject javaProject = (IProject) javaRecs;
            try {
                if (!javaProject.isOpen()) {
                    javaProject.open(null);
                }
                javaRecs = workspaceRoot.findMember(JavaUtils.JAVA_PROJECT_NAME + File.separator + JavaUtils.JAVA_SRC_DIRECTORY);
                if (javaRecs != null && javaRecs.getType() == IResource.FOLDER) {
                    IFolder javaSrcFolder = (IFolder) javaRecs;

                    IResource[] javaProRecs = javaSrcFolder.members();
                    if (javaProRecs.length > 0) {
                        for (int i = 0; i < javaProRecs.length; i++) {
                            javaProRecs[i].delete(true, null);
                        }
                    }

                }
            } catch (CoreException e) {
                RuntimeExceptionHandler.process(e);
            }
        }

        // no need to synchronize the routines here, as it will be done in the code generation.

        // // fix bug 1151, move the sync all routines here from JavaProcessor and PerlProcessor.
        // Display.getDefault().asyncExec(new Runnable() {
        //
        // public void run() {
        // try {
        // RunProcessPlugin.getDefault().getCodeGeneratorService().createRoutineSynchronizer().syncAllRoutines();
        // } catch (Exception e) {
        // ExceptionHandler.process(e);
        // }
        // }
        // });

        executed = true;

    }
}
