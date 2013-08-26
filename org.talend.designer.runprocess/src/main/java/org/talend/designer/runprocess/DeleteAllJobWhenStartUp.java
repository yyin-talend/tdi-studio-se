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
package org.talend.designer.runprocess;

import java.io.File;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.jobs.ISchedulingRule;
import org.eclipse.ui.IStartup;
import org.talend.commons.ui.runtime.exception.ExceptionHandler;
import org.talend.commons.utils.generation.JavaUtils;
import org.talend.core.CorePlugin;
import org.talend.designer.runprocess.java.JavaProcessorUtilities;

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
    @Override
    public void earlyStartup() {

        if (!startUnderPluginModel && !CorePlugin.getDefault().getRepositoryService().isRCPMode()) {
            return;
        }

        final IWorkspace workspace = ResourcesPlugin.getWorkspace();
        final IWorkspaceRunnable op = new IWorkspaceRunnable() {

            @Override
            public void run(IProgressMonitor monitor) throws CoreException {
                IWorkspaceRoot workspaceRoot = workspace.getRoot();

                IProject rootProject = null;
                try {
                    rootProject = JavaProcessorUtilities.getProcessorProject();
                } catch (CoreException e1) {
                    ExceptionHandler.process(e1);
                }
                IResource javaRecs;
                if (rootProject != null) {
                    try {
                        javaRecs = workspaceRoot.findMember(JavaUtils.JAVA_PROJECT_NAME + File.separator
                                + JavaUtils.JAVA_SRC_DIRECTORY);
                        if (javaRecs != null && javaRecs.getType() == IResource.FOLDER) {
                            IFolder javaSrcFolder = (IFolder) javaRecs;

                            IResource[] javaProRecs = javaSrcFolder.members();
                            if (javaProRecs.length > 0) {
                                for (IResource javaProRec : javaProRecs) {
                                    javaProRec.delete(true, null);
                                }
                            }

                        }
                    } catch (CoreException e) {
                        ExceptionHandler.process(e);
                    }
                }
            };

        };

        try {
            ISchedulingRule schedulingRule = workspace.getRoot();
            // the update the project files need to be done in the workspace runnable to avoid all
            // notification
            // of changes before the end of the modifications.
            workspace.run(op, schedulingRule, IWorkspace.AVOID_UPDATE, new NullProgressMonitor());
        } catch (CoreException e) {
            ExceptionHandler.process(e);
        }

        executed = true;

    }
}
