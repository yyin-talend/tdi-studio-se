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
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.IBreakpointManager;
import org.eclipse.debug.core.model.IBreakpoint;
import org.eclipse.jdt.core.IPackageFragment;
import org.talend.commons.exception.SystemException;
import org.talend.core.CorePlugin;
import org.talend.core.context.Context;
import org.talend.core.context.RepositoryContext;
import org.talend.core.model.general.Project;
import org.talend.core.model.process.IContext;
import org.talend.core.model.process.INode;
import org.talend.core.model.process.IProcess;
import org.talend.designer.codegen.ICodeGenerator;
import org.talend.designer.codegen.ICodeGeneratorService;
import org.talend.designer.runprocess.IProcessor;
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
 * $Id: JavaProcessor.java 2007-1-22 上午10:53:24 yzhang $
 * 
 */
public class JavaProcessor implements IProcessor {

    /** added by rxl. */
    public static final String JAVATIP = "//The function of generating Java code haven't achive yet"
            + System.getProperty("line.separator") + "public class JavaTest extends Test {}";

    /** Process to be turned in JAVA code. */
    private IProcess process;

    /** Java project. */
    private IProject javaProject;

    /** The path of the java file sroted the generated java code. */
    private IPath codePath;

    /** The path of the java file sroted the generated context java code. */
    private IPath contextPath;

    /** Tells if filename is based on id or label of the process. */
    private boolean filenameFromLabel;

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
    }

    /*
     * Initialization of the variable codePath and contextPath.
     * 
     * @see org.talend.designer.runprocess.IProcessor#initPaths(org.talend.core.model.process.IContext)
     */
    public void initPaths(IContext context) throws ProcessorException {

        try {
            javaProject = JavaUtils.getProject();
        } catch (CoreException e1) {
            throw new ProcessorException("Java project not found.");
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
            IPackageFragment projectPackage = JavaUtils.getPackage(projectFolderName);
            IPackageFragment jobPackage = JavaUtils.getPackage(projectPackage, jobFolderName);
            IPackageFragment contextPackage = JavaUtils.getPackage(jobPackage, "contexts");

            codePath = jobPackage.getPath().append(fileName + ".java");
            codePath = codePath.removeFirstSegments(1);

            contextPath = contextPackage.getPath().append(escapeFilename(context.getName()) + ".java");
            contextPath = contextPath.removeFirstSegments(1);

        } catch (CoreException e) {
            throw new ProcessorException("Folder within .Java project not founded");
        }

    }

    public void generateCode(IContext context, boolean statistics, boolean trace, boolean javaProperties)
            throws ProcessorException {
        initPaths(context);
        try {
            RepositoryContext repositoryContext = (RepositoryContext) CorePlugin.getContext().getProperty(
                    Context.REPOSITORY_CONTEXT_KEY);
            Project project = repositoryContext.getProject();

            ICodeGenerator codeGen;
            ICodeGeneratorService service = RunProcessPlugin.getDefault().getCodeGeneratorService();
            service.createRoutineSynchronizer().syncAllRoutines();
            if (javaProperties) {
                String javaInterpreter = "";//getPerlInterpreter();
                String javaLib = "";//getPerlLib();
                String currentJavaProject = project.getTechnicalLabel();
                String javaContext = getContextPath().toOSString();

                codeGen = service.createCodeGenerator(process, statistics, trace, javaInterpreter, javaLib, javaContext,
                        currentJavaProject);

            } else {
                codeGen = service.createCodeGenerator(process, statistics, trace);
            }

            String processCode = "";
            String processContext = "false";
            try {
                processCode = codeGen.generateProcessCode();
                processContext = codeGen.generateContextCode(context);
            } catch (SystemException e) {
                throw new ProcessorException(Messages.getString("Processor.generationFailed"), e); //$NON-NLS-1$
            }

            // Generating files
            IFile codeFile = javaProject.getFile(codePath);
            InputStream codeStream = new ByteArrayInputStream(processCode.getBytes());

            if (!codeFile.exists()) {
                codeFile.create(codeStream, true, null);
            } else {
                codeFile.setContents(codeStream, true, false, null);
            }

            // Set Breakpoints in generated code file
            List<INode> breakpointNodes = CorePlugin.getContext().getBreakpointNodes(process);
            if (!breakpointNodes.isEmpty()) {
                String[] nodeNames = new String[breakpointNodes.size()];
                int pos = 0;
                for (INode node : breakpointNodes) {
                    nodeNames[pos++] = node.getUniqueName();
                }
                int[] lineNumbers = getLineNumbers(codeFile, nodeNames);
                setBreakpoints(codeFile, lineNumbers);
            }

            // IFile contextFile = javaProject.getFile(contextPath);
            IFile contextFile = javaProject.getProject().getFile(contextPath);
            InputStream contextStream = new ByteArrayInputStream(processContext.getBytes());
            if (!contextFile.exists()) {
                contextFile.create(contextStream, true, null);
            } else {
                contextFile.setContents(contextStream, true, false, null);
            }

            service.createRoutineSynchronizer().syncAllRoutines();
        } catch (CoreException e1) {
            throw new ProcessorException(Messages.getString("Processor.tempFailed"), e1); //$NON-NLS-1$
        } catch (SystemException e) {
            throw new ProcessorException(Messages.getString("Processor.tempFailed"), e); //$NON-NLS-1$
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

    /**
     * DOC mhirt Comment method "getPerlLib".
     * 
     * @throws ProcessorException
     */
    // public static String getPerlLib() throws ProcessorException {
    // String perlLib;
    // try {
    // perlLib = PerlUtils.getPerlModulePath().toOSString();
    // } catch (CoreException e) {
    // throw new ProcessorException(Messages.getString("Processor.perlModuleNotFound")); //$NON-NLS-1$
    // }
    // return perlLib;
    // }
    /**
     * DOC mhirt Comment method "getPerlInterpreter".
     * 
     * @throws ProcessorException
     */
    // public static String getPerlInterpreter() throws ProcessorException {
    // IPreferenceStore prefStore = CorePlugin.getDefault().getPreferenceStore();
    // String perlInterpreter = prefStore.getString(ITalendCorePrefConstants.PERL_INTERPRETER);
    // if (perlInterpreter == null || perlInterpreter.length() == 0) {
    // throw new ProcessorException(Messages.getString("Processor.configurePerl")); //$NON-NLS-1$
    // }
    // return perlInterpreter;
    // }
    private String escapeFilename(final String filename) {
        return filename != null ? filename.replace(" ", "") : ""; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    }

    /**
     * Getter for codePath.
     * 
     * @return the codePath
     */
    public IPath getCodePath() {
        return this.codePath;
    }

    /**
     * Getter for contextPath.
     * 
     * @return the contextPath
     */
    public IPath getContextPath() {
        return this.contextPath;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.runprocess.IProcessor#getCodeProject()
     */
    public IProject getCodeProject() {
        return this.javaProject.getProject();
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
            searchedLines.add("[" + node + " main ] starts here");
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
            IStatus status = new Status(IStatus.ERROR, "", IStatus.OK, "Source code read failure.", ioe);
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
        IFile codeFile = javaProject.getProject().getFile(codePath);
        int[] lineNumbers = new int[] { 0 };
        try {
            lineNumbers = JavaProcessor.getLineNumbers(codeFile, new String[] { nodeName });
        } catch (CoreException e) {
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
     * Set perl breakpoints on a perl file.
     * 
     * @param codeFile Perl file in wich breakpoints are added.
     * @param lineNumbers Line numbers in the source file where breakpoints are installed.
     * @throws CoreException Breakpoint addition failed.
     */
    private static void setBreakpoints(IFile codeFile, int[] lineNumbers) throws CoreException {
        final String perlBrekPointMarker = "org.epic.debug.perlLineBreakpointMarker";
        codeFile.deleteMarkers(perlBrekPointMarker, true, IResource.DEPTH_ZERO);

        IExtensionRegistry registry = Platform.getExtensionRegistry();
        IConfigurationElement[] configElems = registry.getConfigurationElementsFor("org.eclipse.debug.core.breakpoints");
        IConfigurationElement perlBreakConfigElem = null;
        for (IConfigurationElement elem : configElems) {
            if (elem.getAttribute("id").equals("perlLineBreakpoint")) {
                perlBreakConfigElem = elem;
            }
        }
        if (perlBreakConfigElem == null) {
            IStatus status = new Status(IStatus.ERROR, RunProcessPlugin.PLUGIN_ID, IStatus.OK,
                    "Breakpoint implementation not found.", null);
            throw new CoreException(status);
        }

        IBreakpointManager breakpointManager = DebugPlugin.getDefault().getBreakpointManager();
        for (int line : lineNumbers) {
            IMarker breakMarker = codeFile.createMarker(perlBrekPointMarker);
            breakMarker.setAttribute(IBreakpoint.ID, "perlBreak" + line);
            breakMarker.setAttribute(IMarker.LINE_NUMBER, new Integer(line));
            breakMarker.setAttribute(IMarker.CHAR_START, new Integer(-1));
            breakMarker.setAttribute(IMarker.CHAR_END, new Integer(-1));
            breakMarker.setAttribute("PerlDebug_INVALID_POS", Boolean.FALSE);
            breakMarker.setAttribute(IBreakpoint.PERSISTED, Boolean.TRUE);
            breakMarker.setAttribute(IBreakpoint.ENABLED, Boolean.TRUE);
            breakMarker.setAttribute(IBreakpoint.REGISTERED, Boolean.TRUE);

            IBreakpoint breakpoint = (IBreakpoint) perlBreakConfigElem.createExecutableExtension("class");
            breakpoint.setMarker(breakMarker);
            breakpointManager.addBreakpoint(breakpoint);
        }
    }
}
