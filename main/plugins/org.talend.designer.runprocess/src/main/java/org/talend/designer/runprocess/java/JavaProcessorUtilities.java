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
package org.talend.designer.runprocess.java;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.ArrayUtils;
import org.eclipse.core.resources.IFile;
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
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.common.util.EList;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.talend.commons.CommonsPlugin;
import org.talend.commons.exception.BusinessException;
import org.talend.commons.exception.CommonExceptionHandler;
import org.talend.commons.exception.PersistenceException;
import org.talend.commons.ui.gmf.util.DisplayUtils;
import org.talend.commons.ui.runtime.exception.ExceptionHandler;
import org.talend.commons.utils.data.extractor.ModuleNameExtractor;
import org.talend.commons.utils.generation.JavaUtils;
import org.talend.commons.utils.io.FilesUtils;
import org.talend.core.CorePlugin;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.ILibraryManagerService;
import org.talend.core.context.Context;
import org.talend.core.context.RepositoryContext;
import org.talend.core.model.general.ModuleNeeded;
import org.talend.core.model.general.Project;
import org.talend.core.model.process.IProcess;
import org.talend.core.model.process.IProcess2;
import org.talend.core.model.properties.Item;
import org.talend.core.model.properties.ProcessItem;
import org.talend.core.model.properties.Property;
import org.talend.core.model.properties.RoutineItem;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.core.model.utils.ContextParameterUtils;
import org.talend.core.runtime.CoreRuntimePlugin;
import org.talend.designer.core.ICamelDesignerCoreService;
import org.talend.designer.core.model.utils.emf.component.IMPORTType;
import org.talend.designer.runprocess.IRunProcessService;
import org.talend.designer.runprocess.LastGenerationInfo;
import org.talend.designer.runprocess.i18n.Messages;
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

        initLogFiles(prj);
    }

    /**
     * DOC ycbai Comment method "initLogFiles".
     * 
     * Create common-logging.properties and log4j.properties files if they are non-existent.
     * 
     * @param project
     */
    private static void initLogFiles(IProject project) {
        IRunProcessService service = null;
        if (GlobalServiceRegister.getDefault().isServiceRegistered(IRunProcessService.class)) {
            service = (IRunProcessService) GlobalServiceRegister.getDefault().getService(IRunProcessService.class);
        }
        if (service != null) {
            service.updateLogFiles(project);
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
        File libDir = getJavaProjectLibFolder();
        if ((libDir != null) && (libDir.isDirectory())) {
            Set<String> jarsNeedRetrieve = new HashSet<String>(listModulesReallyNeeded);
            for (File externalLib : libDir.listFiles(FilesUtils.getAcceptJARFilesFilter())) {
                jarsNeedRetrieve.remove(externalLib.getName());
            }
            ILibraryManagerService repositoryBundleService = CorePlugin.getDefault().getRepositoryBundleService();
            repositoryBundleService.retrieve(jarsNeedRetrieve, libDir.getAbsolutePath());
            for (File externalLib : libDir.listFiles(FilesUtils.getAcceptJARFilesFilter())) {
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

    /**
     * DOC ycbai Comment method "updateLibrariesAndClasspath".
     * 
     * @param process
     * @throws CoreException
     */
    private static void updateLibrariesAndClasspath(IProcess process) {
        try {
            Set<String> neededLibraries = getNeededLibrariesForProcess(process);
            ILibraryManagerService repositoryBundleService = CorePlugin.getDefault().getRepositoryBundleService();

            // Update libraries of java project.
            File libDir = getJavaProjectLibFolder();
            List<String> jarNamesInLib = new ArrayList<String>();
            File[] jarFiles = libDir.listFiles(FilesUtils.getAcceptJARFilesFilter());
            if (jarFiles != null && jarFiles.length > 0) {
                for (File file : jarFiles) {
                    if (file.isFile()) {
                        String fileName = file.getName();
                        if (!neededLibraries.contains(fileName)) {
                            FilesUtils.removeFile(file);
                        } else {
                            jarNamesInLib.add(file.getName());
                        }
                    }
                }
            }
            for (String lib : neededLibraries) {
                if (!jarNamesInLib.contains(lib)) {
                    repositoryBundleService.retrieve(lib, libDir.getAbsolutePath());
                    jarNamesInLib.add(lib);
                }
            }

            // Update classpath of java project.
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
            jarFiles = libDir.listFiles(FilesUtils.getAcceptJARFilesFilter());
            for (File jarFile : jarFiles) {
                IClasspathEntry newEntry = JavaCore.newLibraryEntry(new Path(jarFile.getAbsolutePath()), null, null);
                if (!ArrayUtils.contains(classpathEntryArray, newEntry)) {
                    classpathEntryArray = (IClasspathEntry[]) ArrayUtils.add(classpathEntryArray, newEntry);
                    modified = true;
                }
            }

            // sort
            int exchange = 2; // The first,second library is JVM and SRC.
            for (String jar : jarNamesInLib) {
                int index = indexOfEntry(classpathEntryArray, jar);
                if (index < 0) {
                    throw new BusinessException("Missing jar:" + jar);
                }
                if (index >= 0 && index != exchange) {
                    // exchange
                    IClasspathEntry entry = classpathEntryArray[index];
                    IClasspathEntry first = classpathEntryArray[exchange];
                    classpathEntryArray[index] = first;
                    classpathEntryArray[exchange] = entry;
                }
                exchange++;
            }

            List<IClasspathEntry> classpathEntryList = new ArrayList<IClasspathEntry>(Arrays.asList(classpathEntryArray));
            for (Iterator<IClasspathEntry> iterator = classpathEntryList.iterator(); iterator.hasNext();) {
                IClasspathEntry entry = iterator.next();
                if (classpathEntryList.indexOf(entry) > 1) {
                    IPath path = entry.getPath();
                    if (path != null && !jarNamesInLib.contains(path.lastSegment())) {
                        iterator.remove();
                        modified = true;
                    }
                }
            }
            classpathEntryArray = classpathEntryList.toArray(new IClasspathEntry[classpathEntryList.size()]);
            if (modified) {
                javaProject.setRawClasspath(classpathEntryArray, null);
            }
            javaProject.setOutputLocation(javaProject.getPath().append(JavaUtils.JAVA_CLASSES_DIRECTORY), null);
        } catch (JavaModelException e) {
            ExceptionHandler.process(e);
        } catch (BusinessException e) {
            ExceptionHandler.process(e);
        }
    }

    /**
     * DOC ycbai Comment method "getNeededLibrariesForProcess".
     * 
     * @return
     */
    public static Set<String> getNeededLibrariesForProcess(IProcess process) {
        Set<String> neededLibraries = new HashSet<String>();
        Set<ModuleNeeded> neededModules = LastGenerationInfo.getInstance().getModulesNeededWithSubjobPerJob(process.getId(),
                process.getVersion());
        for (ModuleNeeded module : neededModules) {
            neededLibraries.add(module.getModuleName());
        }

        // Added by Marvin Wang on Nov. 8, 2012 just for extract the jar name without directory.
        neededLibraries = ModuleNameExtractor.extractFileName(neededLibraries);

        if (process == null || !(process instanceof IProcess2)) {
            if (neededLibraries.isEmpty() && process != null) {
                neededLibraries = process.getNeededLibraries(true);
                if (neededLibraries == null) {
                    neededLibraries = new HashSet<String>();
                    // for (ModuleNeeded moduleNeeded : ModulesNeededProvider.getModulesNeeded()) {
                    // neededLibraries.add(moduleNeeded.getModuleName());
                    // }
                }
            } else {
                for (ModuleNeeded moduleNeeded : ModulesNeededProvider.getModulesNeededForRoutines()) {
                    neededLibraries.add(moduleNeeded.getModuleName());
                }
            }
            return neededLibraries;
        }
        Property property = ((IProcess2) process).getProperty();
        if (neededLibraries.isEmpty()) {
            neededLibraries = process.getNeededLibraries(true);
            if (neededLibraries == null) {
                neededLibraries = new HashSet<String>();
                for (ModuleNeeded moduleNeeded : ModulesNeededProvider.getModulesNeeded()) {
                    neededLibraries.add(moduleNeeded.getModuleName());
                }
            }
        } else {
            if (property != null && property.getItem() instanceof ProcessItem) {
                List<ModuleNeeded> modulesNeededs = ModulesNeededProvider.getModulesNeededForRoutines((ProcessItem) property
                        .getItem());
                for (ModuleNeeded moduleNeeded : modulesNeededs) {
                    neededLibraries.add(moduleNeeded.getModuleName());
                }

            } else {
                for (ModuleNeeded moduleNeeded : ModulesNeededProvider.getModulesNeededForRoutines()) {
                    neededLibraries.add(moduleNeeded.getModuleName());
                }
            }
        }
        if (property != null && GlobalServiceRegister.getDefault().isServiceRegistered(ICamelDesignerCoreService.class)) {
            ICamelDesignerCoreService camelService = (ICamelDesignerCoreService) GlobalServiceRegister.getDefault().getService(
                    ICamelDesignerCoreService.class);
            if (camelService.isInstanceofCamel(property.getItem())) {
                ERepositoryObjectType beansType = camelService.getBeansType();
                List<IRepositoryViewObject> collectedBeans = new ArrayList<IRepositoryViewObject>();
                try {
                    collectedBeans = CoreRuntimePlugin.getInstance().getProxyRepositoryFactory().getAll(beansType);
                    for (IRepositoryViewObject object : collectedBeans) {
                        Item item = object.getProperty().getItem();
                        if (item instanceof RoutineItem) {
                            RoutineItem routine = (RoutineItem) item;
                            EList imports = routine.getImports();
                            for (Object o : imports) {
                                IMPORTType type = (IMPORTType) o;
                                neededLibraries.add(type.getMODULE());
                            }
                        }
                    }
                } catch (PersistenceException e) {
                    ExceptionHandler.process(e);
                }
            }

            // http://jira.talendforge.org/browse/TESB-5887 LiXiaopeng 2012-6-19
            // Synchronize Route resources
            camelService.synchronizeRouteResource(property.getItem());
        }
        return neededLibraries;
    }

    /**
     * DOC ycbai Comment method "checkJavaProjectLib".
     * 
     * @param jarsNeeded
     */
    public static void checkJavaProjectLib(Collection<String> jarsNeeded) {
        File libDir = getJavaProjectLibFolder();
        if ((libDir != null) && (libDir.isDirectory())) {
            // Changed by Marvin Wang on Nov. 8, 2012, it should extract the jar name without directory.
            Set<String> jarsNeededOnlyIncludeName = ModuleNameExtractor.extractFileName(jarsNeeded);
            Set<String> jarsNeedRetrieve = new HashSet<String>(jarsNeededOnlyIncludeName);
            for (File externalLib : libDir.listFiles(FilesUtils.getAcceptJARFilesFilter())) {
                jarsNeedRetrieve.remove(externalLib.getName());
            }

            if (!jarsNeedRetrieve.isEmpty()) {
                ILibraryManagerService repositoryBundleService = CorePlugin.getDefault().getRepositoryBundleService();
                repositoryBundleService.retrieve(jarsNeedRetrieve, libDir.getAbsolutePath(), false);
            }
        }
    }

    /**
     * DOC ycbai Comment method "getJavaProjectLibPath".
     * 
     * @return
     */
    public static File getJavaProjectLibFolder() {
        try {
            if (javaProject == null) {
                initializeProject();
            }
        } catch (CoreException e) {
            ExceptionHandler.process(e);
        }

        IPath libPath = javaProject.getResource().getLocation().append(JavaUtils.JAVA_LIB_DIRECTORY);
        File libDir = libPath.toFile();
        if (!libDir.exists()) {
            libDir.mkdirs();
        }
        return libDir;
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
                // updateClasspath(jobModuleList);
                projectSetup = project.getTechnicalLabel();
            }
            try {
                sortClasspath(jobModuleList, process);
            } catch (BusinessException be1) {
                ExceptionHandler.process(be1);
            }

            checkAndUpdateLog4jFile();
        } catch (CoreException e) {
            ExceptionHandler.process(e);
        }
    }

    // // see bug 3914, make the order of the jar files consistent with the
    // command
    // // line in run mode
    private static void sortClasspath(Set<String> jobModuleList, IProcess process) throws CoreException, BusinessException {
        IClasspathEntry[] entries = javaProject.getRawClasspath();
        IClasspathEntry jreClasspathEntry = JavaCore.newContainerEntry(new Path("org.eclipse.jdt.launching.JRE_CONTAINER")); //$NON-NLS-1$
        IClasspathEntry classpathEntry = JavaCore.newSourceEntry(javaProject.getPath().append(JavaUtils.JAVA_SRC_DIRECTORY));

        boolean changesDone = false;
        if (!ArrayUtils.contains(entries, jreClasspathEntry)) {
            entries = (IClasspathEntry[]) ArrayUtils.add(entries, jreClasspathEntry);
            changesDone = true;
        }
        if (!ArrayUtils.contains(entries, classpathEntry)) {
            IClasspathEntry source = null;
            for (IClasspathEntry entry : entries) {
                if (entry.getEntryKind() == IClasspathEntry.CPE_SOURCE) {
                    source = entry;
                    break;
                }
            }
            if (source != null) {
                entries = (IClasspathEntry[]) ArrayUtils.remove(entries, ArrayUtils.indexOf(entries, source));
            }
            entries = (IClasspathEntry[]) ArrayUtils.add(entries, classpathEntry);
            changesDone = true;
        }

        // Added by Marvin Wang on Nov. 8, 2012. Maybe some modules are in the list with a directory, so cut the
        // directory only file name remaining.
        Set<String> listModulesReallyNeeded = ModuleNameExtractor.extractFileName(jobModuleList);
        Set<String> listModulesNeededByProcess = new HashSet<String>();
        if (listModulesReallyNeeded != null && listModulesReallyNeeded.size() > 0) {
            for (String jobModule : listModulesReallyNeeded) {
                listModulesNeededByProcess.add(jobModule);
            }
        }

        Set<String> optionalJarsOnlyForRoutines = new HashSet<String>();

        if (listModulesReallyNeeded == null) {
            listModulesReallyNeeded = new HashSet<String>();
        }
        // only for wizards or additional jars only to make the java project compile without any error.
        for (ModuleNeeded moduleNeeded : ModulesNeededProvider.getModulesNeededForRoutines()) {
            optionalJarsOnlyForRoutines.add(moduleNeeded.getModuleName());
        }
        // list contains all routines linked to job as well as routines not used in the job
        // rebuild the list to have only the libs linked to routines "not used".
        optionalJarsOnlyForRoutines.removeAll(listModulesReallyNeeded);

        // at this point, the Set for optional jars really only contains optional jars for routines
        // only to be able to compile java project without error.
        for (String jar : optionalJarsOnlyForRoutines) {
            listModulesReallyNeeded.add(jar);
        }

        addLog4jToJarList(listModulesReallyNeeded);

        File libDir = getJavaProjectLibFolder();
        if ((libDir != null) && (libDir.isDirectory())) {
            Set<String> jarsNeedRetrieve = new HashSet<String>(listModulesReallyNeeded);
            for (File externalLib : libDir.listFiles(FilesUtils.getAcceptJARFilesFilter())) {
                jarsNeedRetrieve.remove(externalLib.getName());
            }
            List<IClasspathEntry> entriesToRemove = new ArrayList<IClasspathEntry>();
            for (IClasspathEntry entry : entries) {
                if (entry.getEntryKind() == IClasspathEntry.CPE_LIBRARY) {
                    boolean found = false;
                    for (File externalLib : libDir.listFiles(FilesUtils.getAcceptJARFilesFilter())) {
                        if (entry.getPath().toPortableString().endsWith(externalLib.getName())) {
                            found = true;
                            break;
                        }
                    }
                    if (!found) {
                        // jar not anymore in the lib path, need to update the classpath
                        entriesToRemove.add(entry);
                    }
                }
            }
            for (IClasspathEntry entry : entriesToRemove) {
                entries = (IClasspathEntry[]) ArrayUtils.remove(entries, ArrayUtils.indexOf(entries, entry));
                changesDone = true;
            }

            if (!jarsNeedRetrieve.isEmpty()) {
                ILibraryManagerService repositoryBundleService = CorePlugin.getDefault().getRepositoryBundleService();
                repositoryBundleService.retrieve(jarsNeedRetrieve, libDir.getAbsolutePath());
            }
            for (File externalLib : libDir.listFiles(FilesUtils.getAcceptJARFilesFilter())) {
                if (externalLib.isFile() && listModulesReallyNeeded.contains(externalLib.getName())) {
                    IClasspathEntry newEntry = JavaCore.newLibraryEntry(new Path(externalLib.getAbsolutePath()), null, null);
                    if (!ArrayUtils.contains(entries, newEntry)) {
                        entries = (IClasspathEntry[]) ArrayUtils.add(entries, newEntry);
                        changesDone = true;
                    }
                }
            }
        }

        String missingJars = null;
        // String missingJarsForRoutinesOnly = null;
        Set<String> missingJarsForRoutinesOnly = new HashSet<String>();
        Set<String> missingJarsForProcessOnly = new HashSet<String>();
        // sort
        int exchange = 2; // The first,second library is JVM and SRC.
        for (String jar : listModulesReallyNeeded) {
            int index = indexOfEntry(entries, jar);
            if (index < 0) {
	        	if (ContextParameterUtils.isContainContextParam(jar)) {
	                 continue;
	            }
                if (listModulesNeededByProcess.contains(jar)) {
                    missingJarsForProcessOnly.add(jar);
                } else {
                    missingJarsForRoutinesOnly.add(jar);
                }
                if (missingJars == null) {
                    missingJars = Messages.getString("JavaProcessorUtilities.msg.missingjar.forProcess") + jar; //$NON-NLS-1$
                } else {
                    missingJars = missingJars + ", " + jar; //$NON-NLS-1$
                }
            }
            if (index >= 0 && index != exchange) {
                // exchange
                IClasspathEntry entry = entries[index];
                IClasspathEntry first = entries[exchange];
                entries[index] = first;
                entries[exchange] = entry;
                changesDone = true;
                exchange++;
            }
        }
        if (changesDone) {
            javaProject.setRawClasspath(entries, null);
            javaProject.setOutputLocation(javaProject.getPath().append(JavaUtils.JAVA_CLASSES_DIRECTORY), null);
        }
        if (missingJars != null) {
            handleMissingJarsForProcess(missingJarsForRoutinesOnly, missingJarsForProcessOnly, missingJars);
        }
    }

    /**
     * DOC ycbai Comment method "addLog4jToJarList".
     * 
     * Add log4j jar to jarList if it is non-existent.
     * 
     * @param jarList
     * @return
     */
    public static boolean addLog4jToJarList(Collection<String> jarList) {
        boolean added = false;
        boolean foundLog4jJar = false;
        for (String jar : jarList) {
            if (jar.matches("log4j-\\d+\\.\\d+\\.\\d+\\.jar")) { //$NON-NLS-1$
                foundLog4jJar = true;
            }
        }
        if (!foundLog4jJar) {
            jarList.add("log4j-1.2.15.jar"); //$NON-NLS-1$
            added = true;
        }

        return added;
    }

    /**
     * 
     * Added by Marvin Wang on Nov 7, 2012.
     * 
     * @param missingJarsForRoutines
     * @param missingJarsForProcess
     * @param missingJars
     * @throws BusinessException
     */
    private static void handleMissingJarsForProcess(Set<String> missingJarsForRoutines, final Set<String> missingJarsForProcess,
            String missingJars) throws BusinessException {
        final StringBuffer sb = new StringBuffer(""); //$NON-NLS-1$
        if (missingJarsForProcess.size() > 0) {
            sb.append(Messages.getString("JavaProcessorUtilities.msg.missingjar.forProcess")); //$NON-NLS-1$
            for (String missingJar : missingJarsForProcess) {
                sb.append(missingJar);
                sb.append(", "); //$NON-NLS-1$
            }
            if (missingJarsForRoutines.size() > 0) {
                // subForMsg(sb.toString());
                sb.append("\r\n\r\n\r\n"); //$NON-NLS-1$
                sb.append(Messages.getString("JavaProcessorUtilities.msg.missingjar.note")); //$NON-NLS-1$
                sb.append("\r\n"); //$NON-NLS-1$
                sb.append(Messages.getString("JavaProcessorUtilities.msg.missingjar.onlyforroutine")); //$NON-NLS-1$
                sb.append("\r\n"); //$NON-NLS-1$
                for (String missingJar : missingJarsForRoutines) {
                    sb.append(missingJar);
                    sb.append(", "); //$NON-NLS-1$
                }
                subForMsg(sb.toString());
            } else {
                subForMsg(sb.toString());
            }
            if (!CommonsPlugin.isHeadless()) {
                Display display = DisplayUtils.getDisplay();
                if (display != null) {
                    display.syncExec(new Runnable() {

                        @Override
                        public void run() {
                            MessageDialog.openWarning(Display.getDefault().getActiveShell(),
                                    Messages.getString("JavaProcessorUtilities.msg.missingjar.warningtitle"), //$NON-NLS-1$
                                    subForMsg(sb.toString()));
                        }

                    });
                }
            }
            throw new BusinessException(missingJars);

        } else {
            if (missingJarsForRoutines.size() > 0) {
                sb.append(Messages.getString("JavaProcessorUtilities.msg.missingjar.onlyforroutine")); //$NON-NLS-1$
                for (String missingJar : missingJarsForRoutines) {
                    sb.append(missingJar);
                    sb.append(", "); //$NON-NLS-1$
                }
                CommonExceptionHandler.warn(subForMsg(sb.toString()));
            }
        }
    }

    private static String subForMsg(String outputMsg) {
        String trimedStr = outputMsg.trim();
        int lastIndexOf = trimedStr.lastIndexOf(","); //$NON-NLS-1$
        return outputMsg.trim().substring(0, lastIndexOf);
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
        if (javaProject == null) {
            try {
                initializeProject();
            } catch (CoreException e) {
                ExceptionHandler.process(e);
            }
        }
        return javaProject;
    }

    public static void checkAndUpdateLog4jFile() {
        try {
            if (javaProject == null) {
                initializeProject();
            }

            Path path = new Path(JavaUtils.JAVA_SRC_DIRECTORY);
            IFile logFile = javaProject.getProject().getFile(path.append("log4j.properties"));
            if (!logFile.exists()) {
                initLogFiles(javaProject.getProject());
            }
        } catch (CoreException e) {
            ExceptionHandler.process(e);
        }
    }

}
