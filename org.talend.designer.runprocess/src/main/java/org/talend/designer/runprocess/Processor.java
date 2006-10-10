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
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationType;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.jface.preference.IPreferenceStore;
import org.talend.core.CorePlugin;
import org.talend.core.model.process.IContext;
import org.talend.core.model.process.IProcess;
import org.talend.core.prefs.ITalendCorePrefConstants;
import org.talend.designer.runprocess.i18n.Messages;
import org.talend.designer.runprocess.perl.PerlProcessor;
import org.talend.designer.runprocess.perl.PerlUtils;

/**
 * DOC chuger class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 */
public class Processor {

    public static final int NO_STATISTICS = -1;

    public static final int NO_TRACES = -1;

    private static final String CTX_ARG = "--context="; //$NON-NLS-1$

    private static final String STAT_PORT_ARG = "--stat_port="; //$NON-NLS-1$

    private static final String TRACE_PORT_ARG = "--trace_port="; //$NON-NLS-1$

    /** Process to be run. */
    private IProcess process;

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
     * @return The running process.
     * @throws ProcessorException Process failed.
     */
    public Process run(final IContext context, int statisticsPort, int tracePort) throws ProcessorException {
        PerlProcessor plProcessor = new PerlProcessor(process, true);
        plProcessor.generateCode(context, statisticsPort != NO_STATISTICS, tracePort != NO_TRACES, true);

        String perlLib;
        try {
            perlLib = PerlUtils.getPerlModulePath().toOSString();
        } catch (CoreException e) {
            throw new ProcessorException(Messages.getString("Processor.perlModuleNotFound")); //$NON-NLS-1$
        }
        String perlLibOption = perlLib != null && perlLib.length() > 0 ? "-I" + perlLib : ""; //$NON-NLS-1$ //$NON-NLS-2$
        IPath absCodePath = plProcessor.getPerlProject().getLocation().append(plProcessor.getCodePath());
        IPath absContextPath = plProcessor.getPerlProject().getLocation().append(plProcessor.getContextPath());
        String perlLibCtxOption = "-I" + absContextPath.removeLastSegments(1).toOSString(); //$NON-NLS-1$     

        return exec(absCodePath, absContextPath, perlLibOption, perlLibCtxOption, statisticsPort, tracePort);
    }

    /**
     * Debug the process using a given context.
     * 
     * @param context Context to be used.
     * @return The configuration to be launched in debug mode.
     * @throws ProcessorException Process failed.
     */
    public ILaunchConfiguration debug(final IContext context) throws ProcessorException {
        PerlProcessor plProcessor = new PerlProcessor(process, true);
        plProcessor.generateCode(context, false, false, true);

        // Create LaunchConfiguration
        ILaunchManager launchManager = DebugPlugin.getDefault().getLaunchManager();
        String projectName = plProcessor.getPerlProject().getName();
        ILaunchConfigurationType[] configType = launchManager.getLaunchConfigurationTypes();
        ILaunchConfigurationType type = null;
        for (int i = 0; type == null && i < configType.length; i++) {
            if (PerlUtils.PERL_LAUNCHCONFIGURATION.equals(configType[i].getIdentifier())) {
                type = configType[i];
            }
        }

        ILaunchConfiguration config = null;
        if (type != null) {
            try {
                ILaunchConfigurationWorkingCopy wc = type.newInstance(null, launchManager
                        .generateUniqueLaunchConfigurationNameFrom(plProcessor.getCodePath().lastSegment()));
                wc.setAttribute(PerlUtils.ATTR_STARTUP_FILE, plProcessor.getCodePath().toOSString());
                wc.setAttribute(PerlUtils.ATTR_PROJECT_NAME, projectName);
                wc.setAttribute(PerlUtils.ATTR_WORKING_DIRECTORY, (String) null);
                wc.setAttribute(PerlUtils.ATTR_PROGRAM_PARAMETERS, CTX_ARG + plProcessor.getContextPath().toOSString());

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
     * @param Context path
     * @param Port Statistics
     * @param Port Trace
     * @return Command Process Launched
     * @throws ProcessorException
     */
    public static Process exec(IPath absCodePath, IPath absContextPath, String perlInterpreterLibOption,
            String perlInterpreterLibCtxOption, int statOption, int traceOption, String... codeOptions) throws ProcessorException {

        String[] cmd = getCommandLine(absCodePath, absContextPath, perlInterpreterLibOption, perlInterpreterLibCtxOption,
                statOption, traceOption, codeOptions);

        logCommandLine(cmd);
        try {
            return Runtime.getRuntime().exec(cmd);
        } catch (IOException ioe) {
            throw new ProcessorException(Messages.getString("Processor.execFailed"), ioe); //$NON-NLS-1$
        }
    }

    public static int exec(StringBuffer out, StringBuffer err, IPath absCodePath, IPath absContextPath,
            String perlInterpreterLibOption, String perlInterpreterLibCtxOption, int statOption, int traceOption,
            String... codeOptions) throws ProcessorException {

        String[] cmd = getCommandLine(absCodePath, absContextPath, perlInterpreterLibOption, perlInterpreterLibCtxOption,
                statOption, traceOption, codeOptions);

        logCommandLine(cmd);
        try {
            int status = -1;

            Process process = Runtime.getRuntime().exec(cmd);

            createProdConsThread(process.getErrorStream(), true, 1024, out, err).start();

            createProdConsThread(process.getInputStream(), false, 1024, out, err).start();

            status = process.waitFor();

            return status;
        } catch (IOException ioe) {
            throw new ProcessorException(Messages.getString("Processor.execFailed"), ioe); //$NON-NLS-1$
        } catch (InterruptedException ie) {
            throw new ProcessorException(Messages.getString("Processor.execFailed"), ie); //$NON-NLS-1$
        }
    }

    private static Thread createProdConsThread(final InputStream input, final boolean isError, final int bufferSize,
            final StringBuffer out, final StringBuffer err) {
        Thread thread = new Thread() {

            public void run() {
                try {
                    BufferedInputStream outStreamProcess = new BufferedInputStream(input);
                    byte[] buffer = new byte[bufferSize];

                    while (outStreamProcess.read(buffer, 0, buffer.length) != -1) {
                        if (isError) {
                            err.append(new String(buffer));
                        } else {
                            out.append(new String(buffer));
                        }
                    }
                    outStreamProcess.close();
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            }
        };
        return thread;
    }

    /**
     * DOC smallet Comment method "getCommandLine".
     * 
     * @param absCodePath
     * @param absContextPath
     * @param perlInterpreterLibOption
     * @param perlInterpreterLibCtxOption
     * @param statOption
     * @param traceOption
     * @param codeOptions
     * @return
     * @throws ProcessorException
     */
    public static String[] getCommandLine(IPath absCodePath, IPath absContextPath, String perlInterpreterLibOption,
            String perlInterpreterLibCtxOption, int statOption, int traceOption, String... codeOptions) throws ProcessorException {
        assert (absCodePath != null);

        IPreferenceStore prefStore = CorePlugin.getDefault().getPreferenceStore();
        String perlInterpreter = prefStore.getString(ITalendCorePrefConstants.PERL_INTERPRETER);
        if (perlInterpreter == null || perlInterpreter.length() == 0) {
            throw new ProcessorException(Messages.getString("Processor.configurePerl")); //$NON-NLS-1$
        }

        String[] cmd = new String[] { perlInterpreter };

        if (perlInterpreterLibOption != null && perlInterpreterLibOption.length() > 0) {
            cmd = (String[]) ArrayUtils.add(cmd, perlInterpreterLibOption);
        }
        if (perlInterpreterLibCtxOption != null && perlInterpreterLibCtxOption.length() > 0) {
            cmd = (String[]) ArrayUtils.add(cmd, perlInterpreterLibCtxOption);
        }
        if (absCodePath != null) {
            cmd = (String[]) ArrayUtils.add(cmd, absCodePath.toOSString());
        }
        if (absContextPath != null) {
            cmd = (String[]) ArrayUtils.add(cmd, CTX_ARG + absContextPath.toOSString());
        }
        cmd = (String[]) ArrayUtils.addAll(cmd, codeOptions);
        if (statOption != -1) {
            cmd = (String[]) ArrayUtils.add(cmd, STAT_PORT_ARG + statOption);
        }
        if (traceOption != -1) {
            cmd = (String[]) ArrayUtils.add(cmd, TRACE_PORT_ARG + traceOption);
        }
        return cmd;
    }

    private static void logCommandLine(String[] cmd) {
        StringBuffer sb = new StringBuffer();
        sb.append("Command line:");
        for (String s : cmd) {
            sb.append(' ').append(s);
        }
        IStatus status = new Status(IStatus.INFO, RunProcessPlugin.PLUGIN_ID, IStatus.OK, sb.toString(), null);
        RunProcessPlugin.getDefault().getLog().log(status);
    }
}
