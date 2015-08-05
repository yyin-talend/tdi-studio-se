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
package org.talend.designer.runprocess;

import java.io.File;

import org.apache.commons.lang.ArrayUtils;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
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
                ExceptionHandler.process(e);
            }
        }
        IProject rootProject = null;
        try {
            rootProject = JavaProcessorUtilities.getProcessorProject();
        } catch (CoreException e1) {
            ExceptionHandler.process(e1);
        }
        IResource javaRecs = workspaceRoot.findMember(JavaUtils.JAVA_PROJECT_NAME);
        if (javaRecs != null && javaRecs.getType() == IResource.PROJECT) {
            rootProject = (IProject) javaRecs;
            try {
                if (!rootProject.isOpen()) {
                    rootProject.open(null);
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

                IResource libRecs = workspaceRoot.findMember(JavaUtils.JAVA_PROJECT_NAME + File.separator + "lib");
                if (libRecs != null && libRecs.getType() == IResource.FOLDER) {
                    IFolder javaLibFolder = (IFolder) libRecs;

                    IResource[] javaProRecs = javaLibFolder.members();
                    if (javaProRecs.length > 0) {
                        for (int i = 0; i < javaProRecs.length; i++) {
                            javaProRecs[i].delete(true, null);
                        }
                    }

                }
                IExtensionRegistry registry = Platform.getExtensionRegistry();
                IExtension nature = registry.getExtension("org.eclipse.core.resources.natures", JavaCore.NATURE_ID); //$NON-NLS-1$

                if (rootProject.getNature(JavaCore.NATURE_ID) == null && nature != null) {
                    IProjectDescription description = rootProject.getDescription();
                    String[] natures = description.getNatureIds();
                    String[] newNatures = new String[natures.length + 1];
                    System.arraycopy(natures, 0, newNatures, 0, natures.length);
                    newNatures[natures.length] = JavaCore.NATURE_ID;
                    description.setNatureIds(newNatures);
                    rootProject.open(IResource.BACKGROUND_REFRESH, null);
                    rootProject.setDescription(description, null);
                }
                IJavaProject javaProject = JavaCore.create(rootProject);

                IClasspathEntry[] entries = new IClasspathEntry[] {};

                IClasspathEntry jreClasspathEntry = JavaCore
                        .newContainerEntry(new Path("org.eclipse.jdt.launching.JRE_CONTAINER")); //$NON-NLS-1$
                IClasspathEntry classpathEntry = JavaCore.newSourceEntry(javaProject.getPath().append(
                        JavaUtils.JAVA_SRC_DIRECTORY));

                entries = (IClasspathEntry[]) ArrayUtils.add(entries, jreClasspathEntry);
                entries = (IClasspathEntry[]) ArrayUtils.add(entries, classpathEntry);

                javaProject.setRawClasspath(entries, null);
                javaProject.setOutputLocation(javaProject.getPath().append(JavaUtils.JAVA_CLASSES_DIRECTORY), null);

            } catch (CoreException e) {
                ExceptionHandler.process(e);
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
