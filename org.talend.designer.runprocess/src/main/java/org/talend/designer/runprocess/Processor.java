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
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationType;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.jdt.launching.IJavaLaunchConfigurationConstants;
import org.eclipse.jface.preference.IPreferenceStore;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.core.CorePlugin;
import org.talend.core.context.Context;
import org.talend.core.context.RepositoryContext;
import org.talend.core.language.ECodeLanguage;
import org.talend.core.model.process.IContext;
import org.talend.core.model.process.IProcess;
import org.talend.core.prefs.ITalendCorePrefConstants;
import org.talend.designer.runprocess.i18n.Messages;
import org.talend.designer.runprocess.java.JavaProcessor;
import org.talend.designer.runprocess.java.JavaProcessorEditStates;
import org.talend.designer.runprocess.java.JavaProcessorRuntimeStates;
import org.talend.designer.runprocess.perl.PerlProcessor;
import org.talend.designer.runprocess.perl.PerlUtils;

/**
 * DOC chuger class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 */
public class Processor {

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
        if (concreteProcessor instanceof JavaProcessor) {
            new JavaProcessorRuntimeStates((JavaProcessor) concreteProcessor);
        }
        // plProcessor.generateCode(context, statisticsPort != NO_STATISTICS, tracePort != NO_TRACES, swatchPort !=
        // WATCH_LIMITED, true);
        concreteProcessor.generateCode(context, statisticsPort != NO_STATISTICS, tracePort != NO_TRACES, true);

        IPath absCodePath = concreteProcessor.getCodeProject().getLocation().append(concreteProcessor.getCodePath());
        // MHIRT : Remove Context path in job call, only build with a context Name
//        IPath absContextPath = concreteProcessor.getCodeProject().getLocation().append(
//                concreteProcessor.getContextPath());

        String libOption = null, moduleDirectoryOption = null; //libCtxOption = null, MHIRT remove context interpereter option

        if (concreteProcessor instanceof PerlProcessor) {
            String perlLib;
            try {
                perlLib = PerlUtils.getPerlModulePath().toOSString();
            } catch (CoreException e) {
                throw new ProcessorException(Messages.getString("Processor.perlModuleNotFound")); //$NON-NLS-1$
            }
            libOption = perlLib != null && perlLib.length() > 0 ? "-I" + perlLib : ""; //$NON-NLS-1$ //$NON-NLS-2$

            // MHIRT remove context interpereter option
            //libCtxOption = "-I" + absContextPath.removeLastSegments(1).toOSString(); //$NON-NLS-1$ 
            // Added by ftang

            try {
                moduleDirectoryOption = "-I" + PerlUtils.getPerlModuleDirectoryPath().toOSString(); //$NON-NLS-1$
            } catch (CoreException e) {
                throw new ProcessorException(Messages.getString("Processor.perlModuleDirectoryNotFound")); //$NON-NLS-1$
            }
            //$NON-NLS-1$ } // Ends
        }

        if (watchParam == null) {
            // MHIRT only works with context name and remove context interpereter option
            return exec(absCodePath, context.getName(), Level.INFO, libOption, moduleDirectoryOption,
                    statisticsPort, tracePort);
        }
        return exec(absCodePath, context.getName(), Level.INFO, libOption, moduleDirectoryOption,
                statisticsPort, tracePort, watchParam);
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
        this.processor = concreteProcessor;

        ILaunchConfiguration config = null;

        if (concreteProcessor instanceof PerlProcessor) {
            concreteProcessor.generateCode(context, false, false, true);
            // Create LaunchConfiguration
            ILaunchManager launchManager = DebugPlugin.getDefault().getLaunchManager();
            String projectName = concreteProcessor.getCodeProject().getName();
            ILaunchConfigurationType[] configType = launchManager.getLaunchConfigurationTypes();
            ILaunchConfigurationType type = null;
            for (int i = 0; type == null && i < configType.length; i++) {
                if (PerlUtils.PERL_LAUNCHCONFIGURATION.equals(configType[i].getIdentifier())) {
                    type = configType[i];
                }
            }

            if (type != null) {
                try {
                    ILaunchConfigurationWorkingCopy wc = type.newInstance(null, launchManager
                            .generateUniqueLaunchConfigurationNameFrom(concreteProcessor.getCodePath().lastSegment()));
                    wc.setAttribute(PerlUtils.ATTR_STARTUP_FILE, concreteProcessor.getCodePath().toOSString());
                    wc.setAttribute(PerlUtils.ATTR_PROJECT_NAME, projectName);
                    wc.setAttribute(PerlUtils.ATTR_WORKING_DIRECTORY, (String) null);
                    wc.setAttribute(PerlUtils.ATTR_PROGRAM_PARAMETERS, CTX_ARG
                            + concreteProcessor.getContextPath().toOSString());

                    config = wc.doSave();
                } catch (CoreException ce) {
                    throw new ProcessorException(ce);
                }
            }
        } else if (concreteProcessor instanceof JavaProcessor) {
            try {
                new JavaProcessorEditStates((JavaProcessor) concreteProcessor);
                concreteProcessor.generateCode(context, false, false, true);

                IProject project = concreteProcessor.getCodeProject();
                ILaunchManager manager = DebugPlugin.getDefault().getLaunchManager();
                ILaunchConfigurationType type = manager
                        .getLaunchConfigurationType(IJavaLaunchConfigurationConstants.ID_JAVA_APPLICATION);

                ILaunchConfigurationWorkingCopy wc = type.newInstance(null, manager
                        .generateUniqueLaunchConfigurationNameFrom(concreteProcessor.getCodePath().lastSegment()));
                wc.setAttribute(IJavaLaunchConfigurationConstants.ATTR_PROJECT_NAME, project.getName());
                wc.setAttribute(IJavaLaunchConfigurationConstants.ATTR_MAIN_TYPE_NAME, concreteProcessor.getTypeName());
                wc.setAttribute(IJavaLaunchConfigurationConstants.ATTR_STOP_IN_MAIN, true);
                config = wc.doSave();
            } catch (CoreException ce) {
                throw new ProcessorException(ce);
            }

        }
        return config;
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
    public static Process exec(IPath absCodePath, String contextName, Level level, String perlInterpreterLibOption,
            String perlModuleDirectoryOption, int statOption, int traceOption,
            String... codeOptions) throws ProcessorException {

        String[] cmd = getCommandLine(absCodePath, contextName, perlInterpreterLibOption,
                perlModuleDirectoryOption, statOption, traceOption, codeOptions);

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

    /**
     * DOC smallet Comment method "getCommandLine".
     * 
     * @param absCodePath
     * @param contextName
     * @param perlInterpreterLibOption
     * @param perlInterpreterLibCtxOption
     * @param statOption
     * @param traceOption
     * @param codeOptions
     * @return
     * @throws ProcessorException
     */
    public static String[] getCommandLine(IPath absCodePath, String contextName, String perlInterpreterLibOption,
            String perlModuleDirectoryOption, int statOption, int traceOption,
            String... codeOptions) {

        assert (absCodePath != null);

        // modified by yzhang for feature 495.
        /*
         * IPreferenceStore prefStore = CorePlugin.getDefault().getPreferenceStore(); String perlInterpreter =
         * prefStore.getString(ITalendCorePrefConstants.PERL_INTERPRETER); if (perlInterpreter == null ||
         * perlInterpreter.length() == 0) { throw new ProcessorException(Messages.getString("Processor.configurePerl"));
         * //$NON-NLS-1$ }
         */

        String interpreter = null;
        try {
            // FIXME
            // interpreter = processor.getInterpreter();
            if (((RepositoryContext) CorePlugin.getContext().getProperty(Context.REPOSITORY_CONTEXT_KEY)).getProject()
                    .getLanguage() == ECodeLanguage.JAVA) {
                IPreferenceStore prefStore = CorePlugin.getDefault().getPreferenceStore();
                String javaInterpreter = prefStore.getString(ITalendCorePrefConstants.JAVA_INTERPRETER);
                if (javaInterpreter == null || javaInterpreter.length() == 0) {
                    throw new ProcessorException(Messages.getString("Processor.configureJava")); //$NON-NLS-1$
                }
                interpreter = javaInterpreter;
            } else {
                IPreferenceStore prefStore = CorePlugin.getDefault().getPreferenceStore();
                String perlInterpreter = prefStore.getString(ITalendCorePrefConstants.PERL_INTERPRETER);
                //String perlInterpreter = ResourcesPlugin.getWorkspace().getRoot(). getFullPath().toOSString()+"/perl.exe";
                if (perlInterpreter == null || perlInterpreter.length() == 0) {
                    throw new ProcessorException(Messages.getString("Processor.configurePerl")); //$NON-NLS-1$
                }
                interpreter = perlInterpreter;
            }
        } catch (ProcessorException e) {
            ExceptionHandler.process(e);
        }
        // Ends.

        String[] cmd = new String[] { interpreter };
        if (((RepositoryContext) CorePlugin.getContext().getProperty(Context.REPOSITORY_CONTEXT_KEY)).getProject()
                .getLanguage() == ECodeLanguage.PERL) {
            // FIXME Code to move in PerlProcessor
            if (perlInterpreterLibOption != null && perlInterpreterLibOption.length() > 0) {
                cmd = (String[]) ArrayUtils.add(cmd, perlInterpreterLibOption);
            }
//          // MHIRT remove context interpereter option
//            if (perlInterpreterLibCtxOption != null && perlInterpreterLibCtxOption.length() > 0) {
//                cmd = (String[]) ArrayUtils.add(cmd, perlInterpreterLibCtxOption);
//            }
            // Added by ftang.
            if (perlModuleDirectoryOption != null && perlModuleDirectoryOption.length() > 0) {
                cmd = (String[]) ArrayUtils.add(cmd, perlModuleDirectoryOption);
            }
            // Ends.
            if (absCodePath != null) {
                cmd = (String[]) ArrayUtils.add(cmd, absCodePath.toOSString());
            }
        } else {
            cmd = ((JavaProcessor) processor).getCommandLine();
        }
        cmd = (String[]) ArrayUtils.addAll(cmd, codeOptions);
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
