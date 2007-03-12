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
package org.talend.designer.runprocess.java;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationType;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.debug.core.JDIDebugModel;
import org.eclipse.jdt.launching.IJavaLaunchConfigurationConstants;
import org.eclipse.jface.preference.IPreferenceStore;
import org.talend.commons.exception.SystemException;
import org.talend.commons.utils.generation.JavaUtils;
import org.talend.core.CorePlugin;
import org.talend.core.context.Context;
import org.talend.core.context.RepositoryContext;
import org.talend.core.model.general.Project;
import org.talend.core.model.process.IContext;
import org.talend.core.model.process.INode;
import org.talend.core.model.process.IProcess;
import org.talend.core.prefs.ITalendCorePrefConstants;
import org.talend.designer.codegen.ICodeGenerator;
import org.talend.designer.codegen.ICodeGeneratorService;
import org.talend.designer.core.ISyntaxCheckableEditor;
import org.talend.designer.runprocess.IJavaProcessorStates;
import org.talend.designer.runprocess.Processor;
import org.talend.designer.runprocess.ProcessorException;
import org.talend.designer.runprocess.RunProcessPlugin;
import org.talend.designer.runprocess.i18n.Messages;

/**
 * Creat the package folder for the java file, and put the generated file to the correct folder.
 * 
 * The creation for the java package should follow the pattern below:
 * 
 * 1)The name for the first grade folder should keep same with the T.O.S project name. 2)The folder name within the
 * project should be the job name.
 * 
 * <br/>
 * 
 * $Id: JavaProcessor.java 2007-1-22 上�?�10:53:24 yzhang $
 * 
 */
public class JavaProcessor extends Processor {

    /** Process to be turned in JAVA code. */
    private IProcess process;

    /** The project contains the java project. */
    private IProject project;

    /** The java project within the project. */
    private static IJavaProject javaProject;

    /** The path of the java source file sroted the generated java code. */
    private IPath srcCodePath;

    /** The path of the java source file sroted the generated context java code. */
    private IPath srcContextPath;

    /** The complied code path. */
    private IPath compiledCodePath;

    /** The complied context file path. */
    private IPath compiledContextPath;

    /** Tells if filename is based on id or label of the process. */
    private boolean filenameFromLabel;

    private String typeName;

    private IJavaProcessorStates states;

    private static List<ISyntaxCheckableEditor> checkableEditors = new ArrayList<ISyntaxCheckableEditor>();

    /**
     * Set current status.
     * 
     * DOC yzhang Comment method "setStatus".
     * 
     * @param states
     */
    public void setStatus(IJavaProcessorStates states) {
        this.states = states;
    }

    /**
     * Constructs a new JavaProcessor.
     * 
     * @param process Process to be turned in Java code.
     * @param filenameFromLabel Tells if filename is based on id or label of the process.
     */
    public JavaProcessor(IProcess process, boolean filenameFromLabel) {
        super();

        this.process = process;
        this.filenameFromLabel = filenameFromLabel;
        setProcessorStates(STATES_RUNTIME);
    }

    /*
     * Initialization of the variable codePath and contextPath.
     * 
     * @see org.talend.designer.runprocess.IProcessor#initPaths(org.talend.core.model.process.IContext)
     */
    public void initPaths(IContext context) throws ProcessorException {

        try {
            this.project = getProcessorProject();
        } catch (CoreException e1) {
            throw new ProcessorException(Messages.getString("JavaProcessor.notFoundedProjectException")); //$NON-NLS-1$
        }
        RepositoryContext repositoryContext = (RepositoryContext) CorePlugin.getContext().getProperty(
                Context.REPOSITORY_CONTEXT_KEY);
        Project project = repositoryContext.getProject();

        String projectFolderName = project.getTechnicalLabel();
        projectFolderName = projectFolderName.toLowerCase();
        String jobFolderName = process.getLabel();
        jobFolderName = jobFolderName.toLowerCase();
        String fileName = filenameFromLabel ? escapeFilename(process.getLabel()) : process.getId();

        try {
            IPackageFragment projectPackage = getProjectPackage(projectFolderName);
            IPackageFragment jobPackage = getProjectPackage(projectPackage, jobFolderName);
            IPackageFragment contextPackage = getProjectPackage(jobPackage, "contexts"); //$NON-NLS-1$

            this.srcCodePath = jobPackage.getPath().append(fileName + JavaUtils.JAVA_EXTENSION); //$NON-NLS-1$
            this.srcCodePath = this.srcCodePath.removeFirstSegments(1);
            this.compiledCodePath = this.srcCodePath.removeLastSegments(1).append(fileName);
            this.compiledCodePath = new Path(JavaUtils.JAVA_CLASSES_DIRECTORY).append(this.compiledCodePath
                    .removeFirstSegments(1)); //$NON-NLS-1$

            this.typeName = jobPackage.getPath().append(fileName).removeFirstSegments(2).toString().replace('/', '.');

            this.srcContextPath = contextPackage.getPath().append(
                    escapeFilename(context.getName()) + JavaUtils.JAVA_CONTEXT_EXTENSION); //$NON-NLS-1$
            this.srcContextPath = this.srcContextPath.removeFirstSegments(1);
            this.compiledContextPath = this.srcContextPath.removeLastSegments(1).append(fileName);
            this.compiledContextPath = new Path(JavaUtils.JAVA_CLASSES_DIRECTORY).append(this.compiledContextPath
                    .removeFirstSegments(1)); //$NON-NLS-1$

        } catch (CoreException e) {
            throw new ProcessorException(Messages.getString("JavaProcessor.notFoundedFolderException")); //$NON-NLS-1$
        }

    }

    /*
     * Append the generated java code form context into java file wihtin the project. If the file not existed new one
     * will be created.
     * 
     * @see org.talend.designer.runprocess.IProcessor#generateCode(org.talend.core.model.process.IContext, boolean,
     * boolean, boolean)
     */
    public void generateCode(IContext context, boolean statistics, boolean trace, boolean javaProperties)
            throws ProcessorException {
        initPaths(context);
        try {
            RepositoryContext repositoryContext = (RepositoryContext) CorePlugin.getContext().getProperty(
                    Context.REPOSITORY_CONTEXT_KEY);
            Project project = repositoryContext.getProject();

            ICodeGenerator codeGen;
            ICodeGeneratorService service = RunProcessPlugin.getDefault().getCodeGeneratorService();
            service.createJavaRoutineSynchronizer().syncAllRoutines();
            if (javaProperties) {
                String javaInterpreter = ""; //$NON-NLS-1$
                String javaLib = ""; //$NON-NLS-1$
                String currentJavaProject = project.getTechnicalLabel();
                String javaContext = getContextPath().toOSString();

                codeGen = service.createCodeGenerator(process, statistics, trace, javaInterpreter, javaLib, javaContext,
                        currentJavaProject);
            } else {
                codeGen = service.createCodeGenerator(process, statistics, trace);
            }

            String processCode = ""; //$NON-NLS-1$
            String processContext = "false"; //$NON-NLS-1$
            try {
                processCode = codeGen.generateProcessCode();
                processContext = codeGen.generateContextCode(context);
            } catch (SystemException e) {
                throw new ProcessorException(Messages.getString("Processor.generationFailed"), e); //$NON-NLS-1$
            }

            // Generating files
            IFile codeFile = this.project.getFile(this.srcCodePath);

            InputStream codeStream = new ByteArrayInputStream(processCode.getBytes());

            if (!codeFile.exists()) {
                codeFile.create(codeStream, true, null);
            } else {
                codeFile.setContents(codeStream, true, false, null);
            }

            // IFile contextFile = javaProject.getFile(contextPath);
            IFile contextFile = this.project.getProject().getFile(this.srcContextPath);
            InputStream contextStream = new ByteArrayInputStream(processContext.getBytes());
            if (!contextFile.exists()) {
                contextFile.create(contextStream, true, null);
            } else {
                contextFile.setContents(contextStream, true, false, null);
            }
            // /add the if ,by qzhang. for RowGenerator2's Preview function.
            if (!process.getName().equals("Preview_RowGenerator2") || !process.getId().equals("Preview_RowGenerator2")) {
                syntaxCheck();
            }

            javaProject.getResource().getWorkspace().build(IncrementalProjectBuilder.AUTO_BUILD, null);

            List<INode> breakpointNodes = CorePlugin.getContext().getBreakpointNodes(process);
            if (!breakpointNodes.isEmpty()) {
                String typeName = getTypeName();
                String[] nodeNames = new String[breakpointNodes.size()];
                int pos = 0;
                for (INode node : breakpointNodes) {
                    nodeNames[pos++] = "[" + node.getUniqueName() + " main ] start"; //$NON-NLS-1$ //$NON-NLS-2$
                }
                int[] lineNumbers = getLineNumbers(codeFile, nodeNames);
                setBreakpoints(codeFile, typeName, lineNumbers);
            }

        } catch (CoreException e1) {
            throw new ProcessorException(Messages.getString("Processor.tempFailed"), e1); //$NON-NLS-1$
        } catch (SystemException e) {
            throw new ProcessorException(Messages.getString("Processor.tempFailed"), e); //$NON-NLS-1$
        }
    }

    public void addSyntaxCheckableEditor(ISyntaxCheckableEditor checkableEditor) {
        if (!JavaProcessor.checkableEditors.contains(checkableEditor)) {
            JavaProcessor.checkableEditors.add(checkableEditor);
        }
    }

    private void syntaxCheck() {
        for (Iterator iter = JavaProcessor.checkableEditors.iterator(); iter.hasNext();) {
            ISyntaxCheckableEditor checkableEditor = (ISyntaxCheckableEditor) iter.next();
            if (checkableEditor.isDisposed()) {
                JavaProcessor.checkableEditors.remove(checkableEditor);
                continue;
            }
            checkableEditor.validateSyntax();
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.runprocess.IProcessor#getCodeContext()
     */
    public String getCodeContext() {
        return getCodeProject().getLocation().append(getContextPath()).removeLastSegments(1).toOSString();
    }

    private String escapeFilename(final String filename) {
        return filename != null ? filename.replace(" ", "") : ""; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.runprocess.IProcessor#getCodePath()
     */
    public IPath getCodePath() {
        return this.states.getCodePath();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.runprocess.IProcessor#getContextPath()
     */
    public IPath getContextPath() {
        return this.states.getContextPath();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.runprocess.IProcessor#getCodeProject()
     */
    public IProject getCodeProject() {
        return this.project.getProject();
    }

    /**
     * Find line numbers of the beginning of the code of process nodes.
     * 
     * @param file Code file where we are searching node's code.
     * @param nodes List of nodes searched.
     * @return Line numbers where code of nodes appears.
     * @throws CoreException Search failed.
     */
    private static int[] getLineNumbers(IFile file, String[] nodes) throws CoreException {
        List<Integer> lineNumbers = new ArrayList<Integer>();

        // List of code's lines searched in the file
        List<String> searchedLines = new ArrayList<String>();
        for (String node : nodes) {
            searchedLines.add(node);
        }

        LineNumberReader lineReader = new LineNumberReader(new InputStreamReader(file.getContents()));
        try {
            String line = lineReader.readLine();
            while (!searchedLines.isEmpty() && line != null) {
                boolean nodeFound = false;
                for (Iterator<String> i = searchedLines.iterator(); !nodeFound && i.hasNext();) {
                    String nodeMain = i.next();
                    if (line.indexOf(nodeMain) != -1) {
                        nodeFound = true;
                        i.remove();

                        // Search the first valid code line
                        boolean lineCodeFound = false;
                        line = lineReader.readLine();
                        while (line != null && !lineCodeFound) {
                            if (isCodeLine(line)) {
                                lineCodeFound = true;
                                lineNumbers.add(new Integer(lineReader.getLineNumber()));
                            }
                            line = lineReader.readLine();
                        }
                    }
                }
                line = lineReader.readLine();
            }
        } catch (IOException ioe) {
            IStatus status = new Status(IStatus.ERROR, "", IStatus.OK, "Source code read failure.", ioe); //$NON-NLS-1$ //$NON-NLS-2$
            throw new CoreException(status);
        }

        int[] res = new int[lineNumbers.size()];
        int pos = 0;
        for (Integer i : lineNumbers) {
            res[pos++] = i.intValue();
        }
        return res;
    }

    /**
     * Return line number where stands specific node in code generated.
     * 
     * @param nodeName
     */
    public int getLineNumber(String nodeName) {
        IFile codeFile = this.project.getProject().getFile(this.srcCodePath);
        int[] lineNumbers = new int[] { 0 };
        try {
            lineNumbers = JavaProcessor.getLineNumbers(codeFile, new String[] { nodeName });
        } catch (CoreException e) {
            lineNumbers = new int[] { 0 };
            e.printStackTrace();
        }
        return lineNumbers[0];

    }

    /**
     * Tells if a line is a line of perl code, not an empty or comment line.
     * 
     * @param line The tested line of code.
     * @return true if the line is a line of code.
     */
    private static boolean isCodeLine(String line) {
        String trimed = line.trim();
        return trimed.length() > 0 && trimed.charAt(0) != '#';
    }

    /**
     * Set java breakpoints in a java file.
     * 
     * @param srcFile Java file in wich breakpoints are added.
     * @param lineNumbers Line numbers in the source file where breakpoints are installed.
     * @throws CoreException Breakpoint addition failed.
     */
    private static void setBreakpoints(IFile codeFile, String typeName, int[] lines) throws CoreException {
        final String javaLineBrekPointMarker = "org.eclipse.jdt.debug.javaLineBreakpointMarker"; //$NON-NLS-1$
        codeFile.deleteMarkers(javaLineBrekPointMarker, true, IResource.DEPTH_ZERO);

        for (int lineNumber = 0; lineNumber < lines.length; lineNumber++) {
            JDIDebugModel.createLineBreakpoint(codeFile, typeName, lines[lineNumber] + 1, -1, -1, 0, true, null);
        }
    }

    /**
     * A java project under folder .Java will be created if there is no existed.
     * 
     * DOC yzhang Comment method "getProject".
     * 
     * @return
     * @throws CoreException
     */
    public static IProject getProcessorProject() throws CoreException {

        IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
        IProject prj = root.getProject(JavaUtils.JAVA_PROJECT_NAME);

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
        } else if (prj.getNature(JavaCore.NATURE_ID) == null && nature != null) {
            IProjectDescription description = prj.getDescription();
            String[] natures = description.getNatureIds();
            String[] newNatures = new String[natures.length + 1];
            System.arraycopy(natures, 0, newNatures, 0, natures.length);
            newNatures[natures.length] = JavaCore.NATURE_ID;
            description.setNatureIds(newNatures);
            prj.open(IResource.BACKGROUND_REFRESH, null);
            prj.setDescription(description, null);
        }
        javaProject = JavaCore.create(prj);

        IClasspathEntry jreClasspathEntry = JavaCore.newContainerEntry(new Path("org.eclipse.jdt.launching.JRE_CONTAINER")); //$NON-NLS-1$
        IClasspathEntry classpathEntry = JavaCore.newSourceEntry(javaProject.getPath().append(JavaUtils.JAVA_SRC_DIRECTORY)); //$NON-NLS-1$

        List<IClasspathEntry> classpath = new ArrayList<IClasspathEntry>();
        classpath.add(jreClasspathEntry);
        classpath.add(classpathEntry);
        // classpath.add(libClasspathEntry);

        IFolder runtimeFolder = prj.getFolder(new Path(JavaUtils.JAVA_CLASSES_DIRECTORY)); //$NON-NLS-1$
        if (!runtimeFolder.exists()) {
            runtimeFolder.create(false, true, null);
        }

        IFolder sourceFolder = prj.getFolder(new Path(JavaUtils.JAVA_SRC_DIRECTORY)); //$NON-NLS-1$
        if (!sourceFolder.exists()) {
            sourceFolder.create(false, true, null);
        }

        File externalLibDirectory = new File(CorePlugin.getDefault().getLibrariesService().getLibrariesPath());
        if ((externalLibDirectory != null) && (externalLibDirectory.isDirectory())) {
            for (File externalLib : externalLibDirectory.listFiles()) {
                if (externalLib.isFile()) {
                    classpath.add(JavaCore.newLibraryEntry(new Path(externalLib.getAbsolutePath()), null, null));
                }
            }
        }

        IClasspathEntry[] classpathEntryArray = (IClasspathEntry[]) classpath.toArray(new IClasspathEntry[classpath.size()]);

        javaProject.setRawClasspath(classpathEntryArray, null);

        javaProject.setOutputLocation(javaProject.getPath().append(JavaUtils.JAVA_CLASSES_DIRECTORY), null); //$NON-NLS-1$

        CorePlugin.getDefault().getLibrariesService().checkLibraries();

        return prj;

    }

    /**
     * Get the required project package under java project, if not existed new one will be created.
     * 
     * DOC yzhang Comment method "getProjectPackage".
     * 
     * @param packageName The required package name, should keep same with the T.O.S project name.
     * @return The required packaged.
     * @throws JavaModelException
     */
    private IPackageFragment getProjectPackage(String packageName) throws JavaModelException {

        IPackageFragmentRoot root = this.javaProject.getPackageFragmentRoot(this.javaProject.getProject().getFolder(
                JavaUtils.JAVA_SRC_DIRECTORY)); //$NON-NLS-1$
        IPackageFragment leave = root.getPackageFragment(packageName);
        if (!leave.exists()) {
            root.createPackageFragment(packageName, false, null);
        }

        return root.getPackageFragment(packageName);
    }

    /**
     * Get the required job package under the project package within the tranfered project, if not existed new one will
     * be created.
     * 
     * DOC yzhang Comment method "getJobPackage".
     * 
     * @param projectPackage The project package within which the job package you need to get, can be getted by method
     * getProjectPackage().
     * @param jobName The required job package name.
     * @return The required job package.
     * @throws JavaModelException
     */
    private IPackageFragment getProjectPackage(IPackageFragment projectPackage, String jobName) throws JavaModelException {

        IPackageFragmentRoot root = this.javaProject.getPackageFragmentRoot(projectPackage.getResource());
        IPackageFragment leave = root.getPackageFragment(jobName);
        if (!leave.exists()) {
            root.createPackageFragment(jobName, false, null);
        }

        return root.getPackageFragment(jobName);

    }

    /*
     * Get the interpreter of Java.
     * 
     * @see org.talend.designer.runprocess.IProcessor#getInterpreter()
     */
    public String getInterpreter() throws ProcessorException {
        IPreferenceStore prefStore = CorePlugin.getDefault().getPreferenceStore();
        String javaInterpreter = prefStore.getString(ITalendCorePrefConstants.JAVA_INTERPRETER);
        if (javaInterpreter == null || javaInterpreter.length() == 0) {
            throw new ProcessorException(Messages.getString("Processor.configureJava")); //$NON-NLS-1$
        }
        return javaInterpreter;
    }

    /**
     * Getter for compliedCodePath.
     * 
     * @return the compliedCodePath
     */
    public IPath getCompiledCodePath() {
        return this.compiledCodePath;
    }

    /**
     * Getter for compiledContextPath.
     * 
     * @return the compiledContextPath
     */
    public IPath getCompiledContextPath() {
        return this.compiledContextPath;
    }

    /**
     * Getter for srcCodePath.
     * 
     * @return the srcCodePath
     */
    public IPath getSrcCodePath() {
        return this.srcCodePath;
    }

    /**
     * Getter for srcContextPath.
     * 
     * @return the srcContextPath
     */
    public IPath getSrcContextPath() {
        return this.srcContextPath;
    }

    public String[] getCommandLine() throws ProcessorException {
        // java -cp libdirectory/*.jar;project_path classname;

        // init java interpreter
        String command;
        try {
            command = setStringPath(getInterpreter());
        } catch (ProcessorException e1) {
            command = "java"; //$NON-NLS-1$
        }

        // init lib path
        StringBuffer libPath = new StringBuffer();
        File externalLibDirectory = new File(CorePlugin.getDefault().getLibrariesService().getLibrariesPath());
        if ((externalLibDirectory != null) && (externalLibDirectory.isDirectory())) {
            for (File externalLib : externalLibDirectory.listFiles()) {
                if (externalLib.isFile()) {
                    libPath.append(externalLib.getAbsolutePath() + JavaUtils.JAVA_CLASSPATH_SEPARATOR);
                }
            }
        }

        // init project_path
        IFolder classesFolder = javaProject.getProject().getFolder(JavaUtils.JAVA_CLASSES_DIRECTORY); //$NON-NLS-1$
        IPath projectFolderPath = classesFolder.getFullPath().removeFirstSegments(1);
        String projectPath = getCodeProject().getLocation().append(projectFolderPath).toOSString();

        // init class name
        IPath classPath = getCodePath().removeFirstSegments(1);
        String className = classPath.toString().replace('/', '.');

        return new String[] { command, "-Xms256M", "-Xmx1024M", "-cp", setStringPath(libPath + projectPath), className }; //$NON-NLS-1$
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.runprocess.IProcessor#getProcessorType()
     */
    public String getProcessorType() {
        return JavaUtils.PROCESSOR_TYPE;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.runprocess.IProcessor#getProcessorStates()
     */
    public void setProcessorStates(int states) {
        if (states == STATES_RUNTIME) {
            new JavaProcessorRuntimeStates(this);
        } else if (states == STATES_EDIT) {
            new JavaProcessorEditStates(this);
        }

    }

    /*
     * Get current class name, and it imported package structure.
     * 
     * @see org.talend.designer.runprocess.IProcessor#getTypeName()
     */
    public String getTypeName() {
        return this.typeName;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.runprocess.IProcessor#saveLaunchConfiguration()
     */
    public Object saveLaunchConfiguration() throws CoreException {
        ILaunchConfiguration config = null;
        ILaunchManager launchManager = DebugPlugin.getDefault().getLaunchManager();
        String projectName = this.getCodeProject().getName();
        ILaunchConfigurationType type = launchManager
                .getLaunchConfigurationType(IJavaLaunchConfigurationConstants.ID_JAVA_APPLICATION);
        if (type != null) {
            ILaunchConfigurationWorkingCopy wc = type.newInstance(null, launchManager
                    .generateUniqueLaunchConfigurationNameFrom(this.getCodePath().lastSegment()));
            wc.setAttribute(IJavaLaunchConfigurationConstants.ATTR_PROJECT_NAME, projectName);
            wc.setAttribute(IJavaLaunchConfigurationConstants.ATTR_MAIN_TYPE_NAME, this.getTypeName());
            wc.setAttribute(IJavaLaunchConfigurationConstants.ATTR_STOP_IN_MAIN, true);
            config = wc.doSave();
        }
        return config;
    }
}
