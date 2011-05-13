// ============================================================================
//
// Copyright (C) 2006-2011 Talend Inc. - www.talend.com
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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
import org.talend.commons.CommonsPlugin;
import org.talend.commons.exception.BusinessException;
import org.talend.commons.ui.runtime.exception.ExceptionHandler;
import org.talend.commons.utils.generation.JavaUtils;
import org.talend.commons.utils.io.FilesUtils;
import org.talend.core.CorePlugin;
import org.talend.core.context.Context;
import org.talend.core.context.RepositoryContext;
import org.talend.core.model.general.ModuleNeeded;
import org.talend.core.model.general.Project;
import org.talend.core.model.process.IProcess;
import org.talend.core.model.process.IProcess2;
import org.talend.core.model.properties.ProcessItem;
import org.talend.librariesmanager.model.ModulesNeededProvider;

/**
 * DOC nrousseau class global comment. Detailled comment
 */
public class JavaProcessorUtilities {

    /** The java project within the project. */
    private static IJavaProject javaProject;

    private static IProject rootProject;

    /**
     * A java project under folder .Java will be created if there is no existed.
     * 
     * DOC yzhang Comment method "getProject".
     * 
     * @return
     * @throws CoreException
     */
    public static IProject getProcessorProject() throws CoreException {
        if (rootProject != null) {
            return rootProject;
        }
        return initializeProject();

    }

    /**
     * DOC mhirt Comment method "initJavaProject".
     * 
     * @param prj
     * @throws CoreException
     */
    private static void initJavaProject(IProject prj) throws CoreException {
        // Does the java nature exists in the environment
        IExtensionRegistry registry = Platform.getExtensionRegistry();
        IExtension nature = registry.getExtension("org.eclipse.core.resources.natures", JavaCore.NATURE_ID); //$NON-NLS-1$

        if (!prj.exists()) {
            final IWorkspace workspace = ResourcesPlugin.getWorkspace();
            final IProjectDescription desc = workspace.newProjectDescription(prj.getName());
            if (nature != null) {
                desc.setNatureIds(new String[] { JavaCore.NATURE_ID });
            }
            prj.create(null);
            prj.open(IResource.BACKGROUND_REFRESH, null);
            prj.setDescription(desc, null);

            IFolder runtimeFolder = prj.getFolder(new Path(JavaUtils.JAVA_CLASSES_DIRECTORY));
            if (!runtimeFolder.exists()) {
                runtimeFolder.create(false, true, null);
            }

            IFolder sourceFolder = prj.getFolder(new Path(JavaUtils.JAVA_SRC_DIRECTORY));
            if (!sourceFolder.exists()) {
                sourceFolder.create(false, true, null);
            }
        } else {
            if (!prj.isOpen()) {
                prj.open(null);
            }
            if (prj.getNature(JavaCore.NATURE_ID) == null && nature != null) {
                IProjectDescription description = prj.getDescription();
                String[] natures = description.getNatureIds();
                String[] newNatures = new String[natures.length + 1];
                System.arraycopy(natures, 0, newNatures, 0, natures.length);
                newNatures[natures.length] = JavaCore.NATURE_ID;
                description.setNatureIds(newNatures);
                prj.open(IResource.BACKGROUND_REFRESH, null);
                prj.setDescription(description, null);
            }
        }
    }

    private static IProject initializeProject() throws CoreException {
        IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
        rootProject = root.getProject(JavaUtils.JAVA_PROJECT_NAME);

        initJavaProject(rootProject);
        javaProject = JavaCore.create(rootProject);
        return rootProject;
    }

    private static void updateClasspath() throws CoreException {
        updateClasspath(null);
    }

    private static void updateClasspath(Set<String> additionalNeededJars) throws CoreException {
        if (rootProject == null || javaProject == null) {
            initializeProject();
        }
        boolean modified = false;
        IClasspathEntry jreClasspathEntry = JavaCore.newContainerEntry(new Path("org.eclipse.jdt.launching.JRE_CONTAINER")); //$NON-NLS-1$
        IClasspathEntry classpathEntry = JavaCore.newSourceEntry(javaProject.getPath().append(JavaUtils.JAVA_SRC_DIRECTORY));

        IClasspathEntry[] classpathEntryArray = javaProject.getRawClasspath();

        if (!ArrayUtils.contains(classpathEntryArray, jreClasspathEntry)) {
            classpathEntryArray = (IClasspathEntry[]) ArrayUtils.add(classpathEntryArray, jreClasspathEntry);
            modified = true;
        }
        if (!ArrayUtils.contains(classpathEntryArray, classpathEntry)) {
            IClasspathEntry source = null;
            for (IClasspathEntry entry : classpathEntryArray) {
                if (entry.getEntryKind() == IClasspathEntry.CPE_SOURCE) {
                    source = entry;
                    break;
                }
            }
            if (source != null) {
                classpathEntryArray = (IClasspathEntry[]) ArrayUtils.remove(classpathEntryArray,
                        ArrayUtils.indexOf(classpathEntryArray, source));
            }
            classpathEntryArray = (IClasspathEntry[]) ArrayUtils.add(classpathEntryArray, classpathEntry);
            modified = true;
        }

        Set<String> listModulesReallyNeeded = new HashSet<String>();
        for (ModuleNeeded moduleNeeded : ModulesNeededProvider.getModulesNeeded()) {
            listModulesReallyNeeded.add(moduleNeeded.getModuleName());
        }

        for (ModuleNeeded moduleNeeded : ModulesNeededProvider.getModulesNeededForApplication()) {
            listModulesReallyNeeded.add(moduleNeeded.getModuleName());
        }

        // see bug 0005559: Import cannot be resolved in routine after opening
        // Job Designer
        for (ModuleNeeded moduleNeeded : ModulesNeededProvider.getModulesNeededForRoutines()) {
            listModulesReallyNeeded.add(moduleNeeded.getModuleName());
        }

        listModulesReallyNeeded.addAll(additionalNeededJars);

        File externalLibDirectory = new File(CorePlugin.getDefault().getLibrariesService().getLibrariesPath());
        if ((externalLibDirectory != null) && (externalLibDirectory.isDirectory())) {
            for (File externalLib : externalLibDirectory.listFiles(FilesUtils.getAcceptJARFilesFilter())) {
                if (externalLib.isFile() && listModulesReallyNeeded.contains(externalLib.getName())) {
                    IClasspathEntry newEntry = JavaCore.newLibraryEntry(new Path(externalLib.getAbsolutePath()), null, null);
                    if (!ArrayUtils.contains(classpathEntryArray, newEntry)) {
                        classpathEntryArray = (IClasspathEntry[]) ArrayUtils.add(classpathEntryArray, newEntry);
                        modified = true;
                    }
                }
            }
        }
        if (modified) {
            javaProject.setRawClasspath(classpathEntryArray, null);
        }

        javaProject.setOutputLocation(javaProject.getPath().append(JavaUtils.JAVA_CLASSES_DIRECTORY), null);
    }

    private static String projectSetup;

    /*
     * @see bug 0005633. Classpath error when current job inlcude some tRunJob-es.
     * 
     * @see org.talend.designer.runprocess.IProcessor#computeLibrariesPath(Set<String>)
     */
    public static void computeLibrariesPath(Set<String> jobModuleList, IProcess process) {
        try {
            RepositoryContext repositoryContext = (RepositoryContext) CorePlugin.getContext().getProperty(
                    Context.REPOSITORY_CONTEXT_KEY);
            Project project = repositoryContext.getProject();
            if (projectSetup == null || !projectSetup.equals(project.getTechnicalLabel())) {
                updateClasspath(jobModuleList);
                projectSetup = project.getTechnicalLabel();
            }
            // see bug 5633
            try {
                sortClasspath(jobModuleList, process);
            } catch (BusinessException be) {
                updateClasspath(jobModuleList);
                try {
                    sortClasspath(jobModuleList, process);
                } catch (BusinessException be1) {
                    ExceptionHandler.process(be1);
                }
            }
        } catch (CoreException e) {
            ExceptionHandler.process(e);
        }
    }

    // // see bug 3914, make the order of the jar files consistent with the
    // command
    // // line in run mode
    private static void sortClasspath(Set<String> jobModuleList, IProcess process) throws CoreException, BusinessException {
        IClasspathEntry[] entries = javaProject.getRawClasspath();

        Set<String> listModulesReallyNeeded = jobModuleList;
        if (listModulesReallyNeeded == null) {
            listModulesReallyNeeded = new HashSet<String>();
        } else {
            // see bug 0005559: Import cannot be resolved in routine after
            // opening Job Designer
            if (process != null && process instanceof IProcess2 && ((IProcess2) process).getProperty() != null
                    && ((IProcess2) process).getProperty().getItem() instanceof ProcessItem) {
                List<ModuleNeeded> modulesNeededs = ModulesNeededProvider
                        .getModulesNeededForRoutines((ProcessItem) ((IProcess2) process).getProperty().getItem());
                for (ModuleNeeded moduleNeeded : modulesNeededs) {
                    listModulesReallyNeeded.add(moduleNeeded.getModuleName());
                }

            } else {
                for (ModuleNeeded moduleNeeded : ModulesNeededProvider.getModulesNeededForRoutines()) {
                    listModulesReallyNeeded.add(moduleNeeded.getModuleName());
                }
            }
        }

        // sort
        int exchange = 2; // The first,second library is JVM and SRC.
        boolean changesDone = false;
        for (String jar : listModulesReallyNeeded) {
            int index = indexOfEntry(entries, jar);
            if (index < 0) {
                throw new BusinessException("Missing jar:" + jar);
            }
            if (index >= 0 && index != exchange) {
                // exchange
                IClasspathEntry entry = entries[index];
                IClasspathEntry first = entries[exchange];
                entries[index] = first;
                entries[exchange] = entry;
                changesDone = true;
            }
            exchange++;
        }
        if (!CommonsPlugin.isHeadless() && changesDone) {
            javaProject.setRawClasspath(entries, null);
        }
    }

    private static int indexOfEntry(final IClasspathEntry[] dest, final String jarName) {

        if (jarName == null) {
            return -1;
        }

        for (int i = 0; i < dest.length; i++) {
            IClasspathEntry entry = dest[i];

            if (entry == null) {
                continue;
            }

            if (entry.getPath() == null || entry.getPath().lastSegment() == null) {
                continue;
            }
            if (entry.getPath().lastSegment().equals(jarName)) {
                return i;
            }
        }
        return -1;
    }

    public static IJavaProject getJavaProject() {
        return javaProject;
    }
}
