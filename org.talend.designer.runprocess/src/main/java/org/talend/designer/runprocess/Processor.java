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

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.lang.ArrayUtils;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.exception.MessageBoxExceptionHandler;
import org.talend.core.model.process.IContext;
import org.talend.designer.core.ISyntaxCheckableEditor;
import org.talend.designer.runprocess.i18n.Messages;

/**
 * DOC nrousseau class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 */
public abstract class Processor implements IProcessor {

    private static Logger log = Logger.getLogger(Processor.class);

    private static final String CTX_ARG = "--context="; //$NON-NLS-1$

    private static final String STAT_PORT_ARG = "--stat_port="; //$NON-NLS-1$

    private static final String TRACE_PORT_ARG = "--trace_port="; //$NON-NLS-1$

    private static boolean externalUse = false;

    private IContext context;

    private String libraryPath;

    private String interpreter;

    private String codeLocation;

    /**
     * Construct a new Processor.
     * 
     * @param process Process to be run.
     */
    public Processor() {
        super();
        if (ProcessorUtilities.isExportConfig()) {
            setInterpreter(ProcessorUtilities.getInterpreter());
            setLibraryPath(ProcessorUtilities.getLibraryPath());
            setCodeLocation(ProcessorUtilities.getCodeLocation());
        }
    }

    /**
     * Run the process using a given context.
     * 
     * @param context The context to be used.
     * @param statisticsPort TCP port used to get statistics from the process, <code>NO_STATISTICS</code> if none.
     * @param tracePort TCP port used to get trace from the process, <code>NO_TRACE</code> if none.
     * @param watchPort
     * @return The running process.
     * @throws ProcessorException Process failed.
     */
    // public Process run(final IContext context, int statisticsPort, int tracePort, int swatchPort) throws
    // ProcessorException { //Old
    public Process run(int statisticsPort, int tracePort, String watchParam) throws ProcessorException {
        if (context == null) {
            throw new IllegalArgumentException("Context is empty, context must be set before call"); //$NON-NLS-1$
        }

        setProcessorStates(STATES_RUNTIME);
        generateCode(context, statisticsPort != NO_STATISTICS, tracePort != NO_TRACES, true);
        if (watchParam == null) {
            // only works with context name and remove context interpereter option
            return exec(Level.INFO, statisticsPort, tracePort);
        }
        return exec(Level.INFO, statisticsPort, tracePort, watchParam);
    }

    /**
     * Debug the process using a given context.
     * 
     * @param context Context to be used.
     * @return The configuration to be launched in debug mode.
     * @throws ProcessorException Process failed.
     * @throws CoreException
     * @throws ProcessorException
     */
    public ILaunchConfiguration debug() throws ProcessorException {
        if (context == null) {
            throw new IllegalArgumentException("Context is empty, context must be set before call"); //$NON-NLS-1$
        }
        ILaunchConfiguration config = null;
        try {
            setProcessorStates(STATES_EDIT);
            generateCode(context, false, false, true);
            config = (ILaunchConfiguration) saveLaunchConfiguration();
        } catch (CoreException ce) {
            throw new ProcessorException(ce);
        }
        return config;
    }

    /**
     * Get the executable commandLine.
     * 
     * @param contextName
     * @param statOption
     * @param traceOption
     * @param codeOptions
     * @return
     */
    public String[] getCommandLine(boolean externalUse, int statOption, int traceOption, String... codeOptions) {
        setExternalUse(externalUse);
        String[] cmd = null;
        try {
            cmd = getCommandLine();

        } catch (ProcessorException e) {
            ExceptionHandler.process(e);
        }
        cmd = addCommmandLineAttch(cmd, context.getName(), statOption, traceOption, codeOptions);
        return cmd;
    }

    /**
     * Add the attchment condition to commmandline .
     * 
     * @param commandLine
     * @param contextName
     * @param statOption
     * @param traceOption
     * @param codeOptions
     * @return
     */
    protected static String[] addCommmandLineAttch(String[] commandLine, String contextName, int statOption,
            int traceOption, String... codeOptions) {
        String[] cmd = (String[]) ArrayUtils.addAll(commandLine, codeOptions);
        if (contextName != null) {
            cmd = (String[]) ArrayUtils.add(cmd, CTX_ARG + contextName);
        }
        if (statOption != -1) {
            cmd = (String[]) ArrayUtils.add(cmd, STAT_PORT_ARG + statOption);
        }
        if (traceOption != -1) {
            cmd = (String[]) ArrayUtils.add(cmd, TRACE_PORT_ARG + traceOption);
        }
        return cmd;
    }

    /**
     * Code Execution, used, when you know where the code stands.
     * 
     * @param Perl Absolute Code Path
     * @param Context Name
     * @param Port Statistics
     * @param Port Trace
     * @return Command Process Launched
     * @throws ProcessorException
     */
    public Process exec(Level level, int statOption, int traceOption, String... codeOptions) throws ProcessorException {

        String[] cmd = getCommandLine(false, statOption, traceOption, codeOptions);

        logCommandLine(cmd, level);
        try {
            return Runtime.getRuntime().exec(cmd);
        } catch (IOException ioe) {
            throw new ProcessorException(Messages.getString("Processor.execFailed"), ioe); //$NON-NLS-1$
        }
    }

    public static Thread createProdConsThread(final InputStream input, final boolean isError, final int bufferSize,
            final StringBuffer out, final StringBuffer err) {
        Thread thread = new Thread() {

            public void run() {
                try {
                    BufferedInputStream outStreamProcess = new BufferedInputStream(input);
                    byte[] buffer = new byte[bufferSize];
                    int count = 0;
                    while ((count = outStreamProcess.read(buffer, 0, buffer.length)) != -1) {
                        if (isError) {
                            err.append(new String(buffer, 0, count));
                        } else {
                            out.append(new String(buffer, 0, count));
                        }
                    }
                    outStreamProcess.close();
                } catch (IOException ioe) {
                    ExceptionHandler.process(ioe);
                } finally {
                    try {
                        input.close();
                    } catch (IOException e) {
                        ExceptionHandler.process(e);
                    }
                }
            }
        };
        return thread;
    }

    public static void logCommandLine(String[] cmd, Level level) {
        StringBuffer sb = new StringBuffer();
        sb.append(Messages.getString("Processor.commandLineLog")); //$NON-NLS-1$
        for (String s : cmd) {
            sb.append(' ').append(s);
        }
        log.log(level, sb.toString());
    }

    /**
     * Sets the externalUse.
     * 
     * @param externalUse the externalUse to set
     */
    public static void setExternalUse(boolean externalUse) {
        Processor.externalUse = externalUse;
    }

    protected static String setStringPath(String path) {
        if (externalUse) {
            return "\"" + path.replace("\\", "/") + "\"";
        } else {
            return path;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.runprocess.IProcessor#getLibraryPath()
     */
    public String getLibraryPath() throws ProcessorException {
        return libraryPath;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.runprocess.IProcessor#getInterpreter()
     */
    public String getInterpreter() throws ProcessorException {
        return interpreter;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.runprocess.IProcessor#setInterpreter(java.lang.String)
     */
    public void setInterpreter(String interpreter) {
        this.interpreter = interpreter;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.runprocess.IProcessor#setLibraryPath(java.lang.String)
     */
    public void setLibraryPath(String libraryPath) {
        this.libraryPath = libraryPath;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.runprocess.IProcessor#getCodeLocation()
     */
    public String getCodeLocation() throws ProcessorException {
        return codeLocation;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.runprocess.IProcessor#setCodeLocation(java.lang.String)
     */
    public void setCodeLocation(String codeLocation) {
        this.codeLocation = codeLocation;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.runprocess.IProcessor#addSyntaxCheckableEditor(org.talend.designer.core.ISyntaxCheckableEditor)
     */
    public abstract void addSyntaxCheckableEditor(ISyntaxCheckableEditor editor);

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.runprocess.IProcessor#generateCode(org.talend.core.model.process.IContext, boolean,
     * boolean, boolean)
     */
    public abstract void generateCode(IContext context, boolean statistics, boolean trace, boolean perlProperties)
            throws ProcessorException;

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.runprocess.IProcessor#getCodeContext()
     */
    public abstract String getCodeContext();

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.runprocess.IProcessor#getCodePath()
     */
    public abstract IPath getCodePath();

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.runprocess.IProcessor#getCodeProject()
     */
    public abstract IProject getCodeProject();

    public abstract String[] getCommandLine() throws ProcessorException;

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.runprocess.IProcessor#getContextPath()
     */
    public abstract IPath getContextPath();

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.runprocess.IProcessor#getLineNumber(java.lang.String)
     */
    public abstract int getLineNumber(String nodeName);

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.runprocess.IProcessor#getProcessorType()
     */
    public abstract String getProcessorType();

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.runprocess.IProcessor#getTypeName()
     */
    public abstract String getTypeName();

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.runprocess.IProcessor#initPaths(org.talend.core.model.process.IContext)
     */
    public abstract void initPaths(IContext context) throws ProcessorException;

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.runprocess.IProcessor#saveLaunchConfiguration()
     */
    public abstract Object saveLaunchConfiguration() throws CoreException;

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.runprocess.IProcessor#setProcessorStates(java.lang.String)
     */
    public abstract void setProcessorStates(int states);

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.runprocess.IProcessor#setContext(org.talend.core.model.process.IContext)
     */
    public void setContext(IContext context) {
        try {
            initPaths(context);
        } catch (ProcessorException pe) {
            MessageBoxExceptionHandler.process(pe);
        }
        this.context = context;
    }
}
