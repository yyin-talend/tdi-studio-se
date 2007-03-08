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
package org.talend.designer.runprocess.perl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;
import org.apache.log4j.Level;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.IBreakpointManager;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationType;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.debug.core.model.IBreakpoint;
import org.eclipse.jface.preference.IPreferenceStore;
import org.talend.commons.exception.SystemException;
import org.talend.core.CorePlugin;
import org.talend.core.context.Context;
import org.talend.core.context.RepositoryContext;
import org.talend.core.language.ECodeLanguage;
import org.talend.core.model.general.Project;
import org.talend.core.model.process.IContext;
import org.talend.core.model.process.INode;
import org.talend.core.model.process.IProcess;
import org.talend.core.prefs.ITalendCorePrefConstants;
import org.talend.designer.codegen.ICodeGenerator;
import org.talend.designer.codegen.ICodeGeneratorService;
import org.talend.designer.core.ISyntaxCheckableEditor;
import org.talend.designer.runprocess.Processor;
import org.talend.designer.runprocess.ProcessorException;
import org.talend.designer.runprocess.RunProcessPlugin;
import org.talend.designer.runprocess.i18n.Messages;

/**
 * DOC chuger class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 */
public class PerlProcessor extends Processor {

    public static final String PROCESSOR_TYPE = "perlProcessor"; //$NON-NLS-1$

    private static final String CTX_ARG = "--context="; //$NON-NLS-1$

    /** Process to be turned in PERL code. */
    private IProcess process;

    /** Perl project. */
    private IProject perlProject;

    /** Path to generated perl code. */
    private IPath codePath;

    /** Path to generated context code. */
    private IPath contextPath;

    /** Context used * */
    private IContext context;

    /** Tells if filename is based on id or label of the process. */
    private boolean filenameFromLabel;

    /**
     * Constructs a new PerlProcessor.
     * 
     * @param process Process to be turned in PERL code.
     * @param filenameFromLabel Tells if filename is based on id or label of the process.
     */
    public PerlProcessor(IProcess process, boolean filenameFromLabel) {
        super();

        this.process = process;
        this.filenameFromLabel = filenameFromLabel;
    }

    public void initPaths(IContext context) throws ProcessorException {
        try {
            perlProject = PerlUtils.getProject();
        } catch (CoreException e1) {
            throw new ProcessorException(Messages.getString("PerlProcessor.notFoundedPerlProject")); //$NON-NLS-1$
        }
        RepositoryContext repositoryContext = (RepositoryContext) CorePlugin.getContext().getProperty(
                Context.REPOSITORY_CONTEXT_KEY);
        Project project = repositoryContext.getProject();
        String filePrefix = project.getTechnicalLabel() + "."; //$NON-NLS-1$
        filePrefix += "job_"; //$NON-NLS-1$
        filePrefix += filenameFromLabel ? escapeFilename(process.getLabel()) : process.getId();
        codePath = new Path(filePrefix + ".pl"); //$NON-NLS-1$
        contextPath = new Path(filePrefix + "_" + escapeFilename(context.getName()) + ".pl"); //$NON-NLS-1$ //$NON-NLS-2$
        this.context = context;
    }

    public void generateCode(IContext context, boolean statistics, boolean trace, boolean perlProperties)
            throws ProcessorException {
        initPaths(context);
        try {
            RepositoryContext repositoryContext = (RepositoryContext) CorePlugin.getContext().getProperty(
                    Context.REPOSITORY_CONTEXT_KEY);
            Project project = repositoryContext.getProject();

            ICodeGenerator codeGen;
            ICodeGeneratorService service = RunProcessPlugin.getDefault().getCodeGeneratorService();
            service.createPerlRoutineSynchronizer().syncAllRoutines();
            if (perlProperties) {
                String perlInterpreter = getPerlInterpreter();
                String perlLib = getPerlLib();
                String currentPerlProject = project.getTechnicalLabel();
                String perlContext = getCodeContext();

                codeGen = service.createCodeGenerator(process, statistics, trace, perlInterpreter, perlLib, perlContext,
                        currentPerlProject);

            } else {
                codeGen = service.createCodeGenerator(process, statistics, trace);
            }

            String processCode = ""; //$NON-NLS-1$
            String processContext = ""; //$NON-NLS-1$
            try {
                processCode = codeGen.generateProcessCode();
                processContext = codeGen.generateContextCode(context);
            } catch (SystemException e) {
                throw new ProcessorException(Messages.getString("Processor.generationFailed"), e); //$NON-NLS-1$
            }

            // Generating files
            IFile codeFile = perlProject.getFile(codePath);
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
                    nodeNames[pos++] = "[" + node.getUniqueName() + " main ] start"; //$NON-NLS-1$ //$NON-NLS-2$
                }
                int[] lineNumbers = getLineNumbers(codeFile, nodeNames);
                setBreakpoints(codeFile, lineNumbers);
            }

            IFile contextFile = perlProject.getFile(contextPath);
            InputStream contextStream = new ByteArrayInputStream(processContext.getBytes());
            if (!contextFile.exists()) {
                contextFile.create(contextStream, true, null);
            } else {
                contextFile.setContents(contextStream, true, false, null);
            }

            service.createPerlRoutineSynchronizer().syncAllRoutines();
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
    public static String getPerlLib() throws ProcessorException {
        String perlLib;
        try {
            perlLib = PerlUtils.getPerlModulePath().toOSString();
        } catch (CoreException e) {
            throw new ProcessorException(Messages.getString("Processor.perlModuleNotFound")); //$NON-NLS-1$
        }
        return perlLib;
    }

    /**
     * DOC mhirt Comment method "getPerlInterpreter".
     * 
     * @throws ProcessorException
     */
    public static String getPerlInterpreter() throws ProcessorException {
        IPreferenceStore prefStore = CorePlugin.getDefault().getPreferenceStore();
        String perlInterpreter = prefStore.getString(ITalendCorePrefConstants.PERL_INTERPRETER);
        if (perlInterpreter == null || perlInterpreter.length() == 0) {
            throw new ProcessorException(Messages.getString("Processor.configurePerl")); //$NON-NLS-1$
        }
        return perlInterpreter;
    }

    private static String escapeFilename(final String filename) {
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
        return this.perlProject;
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
            // searchedLines.add("[" + node + " main ] start");
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
        IFile codeFile = perlProject.getFile(codePath);
        int[] lineNumbers = new int[] { 0 };
        try {
            lineNumbers = PerlProcessor.getLineNumbers(codeFile, new String[] { nodeName });
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
        final String perlBrekPointMarker = "org.epic.debug.perlLineBreakpointMarker"; //$NON-NLS-1$
        codeFile.deleteMarkers(perlBrekPointMarker, true, IResource.DEPTH_ZERO);

        IExtensionRegistry registry = Platform.getExtensionRegistry();
        IConfigurationElement[] configElems = registry.getConfigurationElementsFor("org.eclipse.debug.core.breakpoints"); //$NON-NLS-1$
        IConfigurationElement perlBreakConfigElem = null;
        for (IConfigurationElement elem : configElems) {
            if (elem.getAttribute("id").equals("perlLineBreakpoint")) { //$NON-NLS-1$ //$NON-NLS-2$
                perlBreakConfigElem = elem;
            }
        }
        if (perlBreakConfigElem == null) {
            IStatus status = new Status(IStatus.ERROR, RunProcessPlugin.PLUGIN_ID, IStatus.OK,
                    "Breakpoint implementation not found.", null); //$NON-NLS-1$
            throw new CoreException(status);
        }

        IBreakpointManager breakpointManager = DebugPlugin.getDefault().getBreakpointManager();
        for (int line : lineNumbers) {
            IMarker breakMarker = codeFile.createMarker(perlBrekPointMarker);
            breakMarker.setAttribute(IBreakpoint.ID, "perlBreak" + line); //$NON-NLS-1$
            breakMarker.setAttribute(IMarker.LINE_NUMBER, new Integer(line));
            breakMarker.setAttribute(IMarker.CHAR_START, new Integer(-1));
            breakMarker.setAttribute(IMarker.CHAR_END, new Integer(-1));
            breakMarker.setAttribute("PerlDebug_INVALID_POS", Boolean.FALSE); //$NON-NLS-1$
            breakMarker.setAttribute(IBreakpoint.PERSISTED, Boolean.TRUE);
            breakMarker.setAttribute(IBreakpoint.ENABLED, Boolean.TRUE);
            breakMarker.setAttribute(IBreakpoint.REGISTERED, Boolean.TRUE);

            IBreakpoint breakpoint = (IBreakpoint) perlBreakConfigElem.createExecutableExtension("class"); //$NON-NLS-1$
            breakpoint.setMarker(breakMarker);
            breakpointManager.addBreakpoint(breakpoint);
        }
    }

    /*
     * Get the interpreter for Perl language.
     * 
     * @see org.talend.designer.runprocess.IProcessor#getInterpreter()
     */
    public String getInterpreter() throws ProcessorException {
        IPreferenceStore prefStore = CorePlugin.getDefault().getPreferenceStore();
        String perlInterpreter = prefStore.getString(ITalendCorePrefConstants.PERL_INTERPRETER);
        if (perlInterpreter == null || perlInterpreter.length() == 0) {
            throw new ProcessorException(Messages.getString("Processor.configurePerl")); //$NON-NLS-1$
        }
        return perlInterpreter;

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.runprocess.IProcessor#getProcessorType()
     */
    public String getProcessorType() {
        return ECodeLanguage.PERL.getName();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.runprocess.IProcessor#setProcessorStates(java.lang.String)
     */
    public void setProcessorStates(int states) {
        // do nothings for perl right now.

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.runprocess.IProcessor#addSyntaxCheckableEditor(org.talend.designer.core.ISyntaxCheckableEditor)
     */
    public void addSyntaxCheckableEditor(ISyntaxCheckableEditor editor) {
        // do nothing for perl right now.

    }

    public static int exec(StringBuffer out, StringBuffer err, IPath absCodePath, String contextName, Level level,
            String perlInterpreterLibOption, String perlModuleDirectoryOption, int statOption, int traceOption,
            String... codeOptions) throws ProcessorException {

        // String[] cmd = Processor.getCommandLine(absCodePath, contextName, perlInterpreterLibOption,
        // perlModuleDirectoryOption, statOption, traceOption, codeOptions);

        String[] cmd = getCommandLineByCondition(absCodePath, perlInterpreterLibOption, perlModuleDirectoryOption);
        cmd = Processor.addCommmandLineAttch(cmd, contextName, statOption, traceOption, codeOptions);
        Processor.logCommandLine(cmd, level);
        try {
            int status = -1;

            Process process = Runtime.getRuntime().exec(cmd);

            Processor.createProdConsThread(process.getErrorStream(), true, 1024, out, err).start();

            Processor.createProdConsThread(process.getInputStream(), false, 1024, out, err).start();

            status = process.waitFor();

            return status;
        } catch (IOException ioe) {
            throw new ProcessorException(Messages.getString("Processor.execFailed"), ioe); //$NON-NLS-1$
        } catch (InterruptedException ie) {
            throw new ProcessorException(Messages.getString("Processor.execFailed"), ie); //$NON-NLS-1$
        }
    }

    /**
     * get executable commandline by context conditon.
     * 
     * @param absCodePath
     * @param perlInterpreterLibOption
     * @param perlModuleDirectoryOption
     * @return
     * @throws ProcessorException
     */
    private static String[] getCommandLineByCondition(IPath absCodePath, String perlInterpreterLibOption,
            String perlModuleDirectoryOption) throws ProcessorException {
        assert (absCodePath != null);
        IPreferenceStore prefStore = CorePlugin.getDefault().getPreferenceStore();
        String perlInterpreter = prefStore.getString(ITalendCorePrefConstants.PERL_INTERPRETER);
        if (perlInterpreter == null || perlInterpreter.length() == 0) {
            throw new ProcessorException(Messages.getString("Processor.configurePerl")); //$NON-NLS-1$
        }
        String[] cmd = new String[] { setStringPath(perlInterpreter) };
        if (perlInterpreterLibOption != null && perlInterpreterLibOption.length() > 0) {
            cmd = (String[]) ArrayUtils.add(cmd, perlInterpreterLibOption);
        }
        if (perlModuleDirectoryOption != null && perlModuleDirectoryOption.length() > 0) {
            cmd = (String[]) ArrayUtils.add(cmd, perlModuleDirectoryOption);
        }
        if (absCodePath != null) {
            cmd = (String[]) ArrayUtils.add(cmd, setStringPath(absCodePath.toOSString()));
        }
        return cmd;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.runprocess.IProcessor#getTypeName()
     */
    public String getTypeName() {

        return null;
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
        ILaunchConfigurationType type = launchManager.getLaunchConfigurationType(PerlUtils.PERL_LAUNCHCONFIGURATION);
        if (type != null) {
            ILaunchConfigurationWorkingCopy wc = type.newInstance(null, launchManager
                    .generateUniqueLaunchConfigurationNameFrom(this.getCodePath().lastSegment()));
            wc.setAttribute(PerlUtils.ATTR_STARTUP_FILE, this.getCodePath().toOSString());
            wc.setAttribute(PerlUtils.ATTR_PROJECT_NAME, projectName);
            wc.setAttribute(PerlUtils.ATTR_WORKING_DIRECTORY, (String) null);
            wc.setAttribute(PerlUtils.ATTR_PROGRAM_PARAMETERS, CTX_ARG + context.getName());

            config = wc.doSave();
        }
        return config;
    }

    public String[] getCommandLine() throws ProcessorException {
        // String interpreter = getInterpreter();
        // String[] cmd = new String[] { interpreter };
        String perlInterpreterLibOption = null;
        String perlModuleDirectoryOption = null;
        String perlLib;
        try {
            perlLib = PerlUtils.getPerlModulePath().toOSString();
        } catch (CoreException e) {
            throw new ProcessorException(Messages.getString("Processor.perlModuleNotFound")); //$NON-NLS-1$
        }
        perlInterpreterLibOption = perlLib != null && perlLib.length() > 0 ? "-I" + setStringPath(perlLib) : ""; //$NON-NLS-1$ //$NON-NLS-2$

        // This is no usefull anymore since all libs are at the same place:
        // try {
        // perlModuleDirectoryOption = "-I" + setStringPath(PerlUtils.getPerlModuleDirectoryPath().toOSString());
        // //$NON-NLS-1$
        // } catch (CoreException e) {
        // throw new ProcessorException(Messages.getString("Processor.perlModuleDirectoryNotFound")); //$NON-NLS-1$
        // }

        IPath absCodePath = this.getCodeProject().getLocation().append(this.getCodePath());
        String[] cmd = getCommandLineByCondition(absCodePath, perlInterpreterLibOption, perlModuleDirectoryOption);
        return cmd;
    }
}
