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
package org.talend.designer.runprocess;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.lang.ArrayUtils;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.core.model.process.IContext;
import org.talend.core.model.process.IProcess;
import org.talend.designer.runprocess.i18n.Messages;

/**
 * DOC chuger class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 */
public class Processor {

    public static final String RUNTIME = "runtime"; //$NON-NLS-1$

    public static final String EDIT = "edit"; //$NON-NLS-1$

    private static Logger log = Logger.getLogger(Processor.class);

    public static final int NO_STATISTICS = -1;

    public static final int NO_TRACES = -1;

    public static final int WATCH_LIMITED = -1;

    public static final int WATCH_ALLOWED = 1;

    private static final String CTX_ARG = "--context="; //$NON-NLS-1$

    private static final String STAT_PORT_ARG = "--stat_port="; //$NON-NLS-1$

    private static final String TRACE_PORT_ARG = "--trace_port="; //$NON-NLS-1$

    /** Process to be run. */
    private IProcess process;

    private static IProcessor processor;

    /**
     * Construct a new Processor.
     * 
     * @param process Process to be run.
     */
    public Processor(IProcess process) {
        super();

        this.process = process;
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
    public Process run(final IContext context, int statisticsPort, int tracePort, String watchParam)
            throws ProcessorException {
        IProcessor concreteProcessor = ProcessorUtilities.getProcessor(process, context);
        processor = concreteProcessor;
        concreteProcessor.setProcessorStates(RUNTIME);
        concreteProcessor.generateCode(context, statisticsPort != NO_STATISTICS, tracePort != NO_TRACES, true);
        if (watchParam == null) {
            // only works with context name and remove context interpereter option
            return exec(context.getName(), Level.INFO, statisticsPort, tracePort);
        }
        return exec(context.getName(), Level.INFO, statisticsPort, tracePort, watchParam);
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
    public ILaunchConfiguration debug(final IContext context) throws ProcessorException {
        IProcessor concreteProcessor = ProcessorUtilities.getProcessor(process, context);
        processor = concreteProcessor;
        ILaunchConfiguration config = null;
        try {
            concreteProcessor.setProcessorStates(Processor.EDIT);
            concreteProcessor.generateCode(context, false, false, true);
            config = (ILaunchConfiguration) concreteProcessor.saveLaunchConfiguration();
        } catch (CoreException ce) {
            throw new ProcessorException(ce);
        }
        return config;
    }
    
    
    /**
     * Get the executable commandLine.
     * @param contextName
     * @param statOption
     * @param traceOption
     * @param codeOptions
     * @return
     */
    public static String[] getCommandLine(String contextName, int statOption, int traceOption,
            String... codeOptions) {
        String[] cmd = null;
        try {
            cmd = processor.getCommandLine();

        } catch (ProcessorException e) {
            ExceptionHandler.process(e);
        } 
        cmd = addCommmandLineAttch(cmd, contextName, statOption, traceOption, codeOptions);
        return cmd;
    }
    
    /**
     * Add the attchment condition to commmandline .
     * @param commandLine
     * @param contextName
     * @param statOption
     * @param traceOption
     * @param codeOptions
     * @return
     */
    public static String[] addCommmandLineAttch(String[] commandLine, String contextName, int statOption, int traceOption,
            String... codeOptions) {
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
    public static Process exec(String contextName, Level level, int statOption, int traceOption, String... codeOptions)
            throws ProcessorException {

        String[] cmd = getCommandLine(contextName, statOption, traceOption, codeOptions);

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
        // IStatus status = new Status(IStatus.INFO, RunProcessPlugin.PLUGIN_ID, IStatus.OK, sb.toString(), null);
        // RunProcessPlugin.getDefault().getLog().log(status);
    }
}
