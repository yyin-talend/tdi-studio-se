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

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.lang.reflect.InvocationTargetException;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.math.NumberUtils;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.internal.dialogs.EventLoopProgressMonitor;
import org.eclipse.ui.progress.IProgressService;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.ui.swt.dialogs.ProgressDialog;
import org.talend.core.model.process.IConnection;
import org.talend.core.model.process.IContext;
import org.talend.core.model.process.INode;
import org.talend.core.model.process.IProcess;
import org.talend.core.model.process.ITargetExecutionConfig;
import org.talend.designer.runprocess.ProcessMessage.MsgType;
import org.talend.designer.runprocess.data.PerformanceData;
import org.talend.designer.runprocess.data.TraceData;
import org.talend.designer.runprocess.i18n.Messages;
import org.talend.designer.runprocess.ui.ProcessContextComposite;
import org.talend.designer.runprocess.ui.actions.ClearPerformanceAction;
import org.talend.designer.runprocess.ui.actions.ClearTraceAction;

/**
 * Context of a running process. <br/>
 * 
 * $Id$
 * 
 * 
 */
public class RunProcessContext {

    private static final int STRING_LENGTH = 256;

    public static final String PROP_RUNNING = "RunProcessContext.Running"; //$NON-NLS-1$

    public static final String PROP_MONITOR = "RunProcessContext.MonitorPerf"; //$NON-NLS-1$

    public static final String TRACE_MONITOR = "RunProcessContext.MonitorTrace"; //$NON-NLS-1$

    // Added by ftang
    private static final String PROR_SWITCH_TIME = "RunProcesscontext.Message.Watch"; //$NON-NLS-1$

    private static final String WATCH_PARAM = "--watch"; //$NON-NLS-1$

    private boolean watchAllowed;

    // Ends

    /** Change property listeners. */
    private transient PropertyChangeSupport pcsDelegate;

    /** The process. */
    private IProcess process;

    /** The selected context to run process with. */
    private IContext selectedContext;

    /** The selected server configuration to run process with. */
    private ITargetExecutionConfig selectedTargetExecutionConfig;

    /** Performance monitoring activated. */
    private boolean monitorPerf;

    /** Trace monitoring activated. */
    private boolean monitorTrace;

    /** Is process running. */
    private boolean running;

    /** The executing process. */
    private Process ps;

    /** Monitor of the running process. */
    private IProcessMonitor psMonitor;

    /** Monitor of the running process. */
    private PerformanceMonitor perfMonitor;

    /** Monitor for Traces of the running process. */
    private TraceMonitor traceMonitor;

    /** Kill is in progress. */
    private boolean killing;

    private IProcessMessageManager processMessageManager;

    private int statsPort = Processor.NO_STATISTICS;

    private int tracesPort = Processor.NO_TRACES;

    /**
     * Constrcuts a new RunProcessContext.
     * 
     * @param process The process.
     */
    public RunProcessContext(IProcess process) {
        super();

        this.process = process;
        selectedContext = process.getContextManager().getDefaultContext();

        pcsDelegate = new PropertyChangeSupport(this);
        this.processMessageManager = new ProcessMessageManager();
    }

    public synchronized void addPropertyChangeListener(PropertyChangeListener l) {
        if (l == null) {
            throw new IllegalArgumentException();
        }

        processMessageManager.addPropertyChangeListener(l);
        pcsDelegate.addPropertyChangeListener(l);
    }

    protected void firePropertyChange(String property, Object oldValue, Object newValue) {
        if (pcsDelegate.hasListeners(property)) {
            pcsDelegate.firePropertyChange(property, oldValue, newValue);
        }
    }

    public synchronized void removePropertyChangeListener(PropertyChangeListener l) {
        if (l != null) {
            pcsDelegate.removePropertyChangeListener(l);
            processMessageManager.removePropertyChangeListener(l);
        }
    }

    public IProcess getProcess() {
        return process;
    }

    public void clearMessages() {
        processMessageManager.clearMessages();
    }

    // Added by ftang
    public void switchTime() {
        // TODO should do something here.
        firePropertyChange(PROR_SWITCH_TIME, "true", "false"); //$NON-NLS-1$ //$NON-NLS-2$
    }

    // Ends

    public Collection<IProcessMessage> getMessages() {
        return processMessageManager.getMessages();
    }

    /**
     * Getter for monitorPerf.
     * 
     * @return the monitorPerf
     */
    public boolean isMonitorPerf() {
        return this.monitorPerf;
    }

    /**
     * Sets the monitorPerf.
     * 
     * @param monitorPerf the monitorPerf to set
     */
    public void setMonitorPerf(boolean monitorPerf) {
        if (this.monitorPerf != monitorPerf) {
            this.monitorPerf = monitorPerf;
            firePropertyChange(PROP_MONITOR, Boolean.valueOf(!monitorPerf), Boolean.valueOf(monitorPerf));
        }
    }

    /**
     * Getter for monitorTrace.
     * 
     * @return the monitorTrace
     */
    public boolean isMonitorTrace() {
        return this.monitorTrace;
    }

    /**
     * Sets the monitorTrace.
     * 
     * @param monitorTrace the monitorTraceto set
     */
    public void setMonitorTrace(boolean monitorTrace) {
        if (this.monitorTrace != monitorTrace) {
            this.monitorTrace = monitorTrace;
            firePropertyChange(TRACE_MONITOR, Boolean.valueOf(!monitorTrace), Boolean.valueOf(monitorTrace));
        }
    }

    /**
     * Getter for running.
     * 
     * @return the running
     */
    public boolean isRunning() {
        return this.running;
    }

    /**
     * Sets the running.
     * 
     * @param running the running to set
     */
    private void setRunning(boolean running) {
        if (this.running != running) {
            this.running = running;
            firePropertyChange(PROP_RUNNING, Boolean.valueOf(!running), Boolean.valueOf(running));
        }
    }

    public IContext getSelectedContext() {
        return selectedContext;
    }

    public void setSelectedContext(IContext context) {
        selectedContext = context;
    }

    /**
     * Launch the process.
     */
    public void exec(final Shell shell) {
        setRunning(true);

        if (ProcessContextComposite.promptConfirmLauch(shell, getSelectedContext())) {

            ClearPerformanceAction clearPerfAction = new ClearPerformanceAction();
            clearPerfAction.setProcess(process);
            clearPerfAction.run();

            ClearTraceAction clearTraceAction = new ClearTraceAction();
            clearTraceAction.setProcess(process);
            clearTraceAction.run();

            final IProcessor processor = getProcessor(process);
            IProgressService progressService = PlatformUI.getWorkbench().getProgressService();

            try {
                // progressService.runInUI(PlatformUI.getWorkbench().getProgressService(), new IRunnableWithProgress() {
                progressService.run(false, true, new IRunnableWithProgress() {

                    // ProgressDialog progressDialog = new ProgressDialog(shell) {

                    public void run(final IProgressMonitor monitor) {

                        final EventLoopProgressMonitor monitorWrap = new EventLoopProgressMonitor(monitor);

                        monitorWrap.beginTask(
                                Messages.getString("ProcessComposite.buildTask"), IProgressMonitor.UNKNOWN); //$NON-NLS-1$
                        try {
                            findNewStatsPort();
                            if (monitorPerf) {
                                perfMonitor = new PerformanceMonitor();
                                new Thread(perfMonitor).start();
                            }
                            findNewTracesPort();
                            if (monitorTrace) {
                                traceMonitor = new TraceMonitor();
                                new Thread(traceMonitor).start();
                            }

                            final String watchParam = RunProcessContext.this.isWatchAllowed() ? WATCH_PARAM : null;
                            IContext context = getSelectedContext();
                            processor.setContext(context);
                            processor.setTargetExecutionConfig(getSelectedTargetExecutionConfig());
                            final boolean[] refreshUiAndWait = new boolean[1];
                            refreshUiAndWait[0] = true;
                            final Display display = shell.getDisplay();
                            new Thread(new Runnable() {

                                public void run() {
                                    display.syncExec(new Runnable() {

                                        public void run() {
                                            try {

                                                ps = processor.run(getStatisticsPort(), getTracesPort(), watchParam,
                                                        monitorWrap, processMessageManager);
                                                if (ps != null && !monitorWrap.isCanceled()) {
                                                    psMonitor = createProcessMonitor(ps);
                                                    final String startingPattern = Messages
                                                            .getString("ProcessComposite.startPattern"); //$NON-NLS-1$
                                                    MessageFormat mf = new MessageFormat(startingPattern);
                                                    String welcomeMsg = mf.format(new Object[] { process.getLabel(),
                                                            new Date() });
                                                    processMessageManager.addMessage(new ProcessMessage(
                                                            MsgType.CORE_OUT, welcomeMsg));
                                                    new Thread(psMonitor).start();
                                                } else {
                                                    setRunning(false);
                                                }
                                            } catch (Exception e) {
                                                Throwable cause = e.getCause();
                                                if (cause != null
                                                        && cause.getClass().equals(InterruptedException.class)) {
                                                    setRunning(false);
                                                } else {
                                                    ExceptionHandler.process(e);
                                                    addErrorMessage(e);
                                                    kill();
                                                }
                                            } finally {
                                                monitorWrap.done();
                                                refreshUiAndWait[0] = false;
                                            }
                                        }
                                    });
                                }
                            }).start();
                            while (refreshUiAndWait[0] && !monitorWrap.isCanceled()) {
                                if (!display.readAndDispatch()) {
                                    display.sleep();
                                }
                                synchronized (this) {
                                    try {
                                        final long waitTime = 50;
                                        wait(waitTime);
                                    } catch (InterruptedException e) {
                                        // Do nothing
                                    }
                                }

                            }

                        } catch (Exception e) {
                            ExceptionHandler.process(e);
                            addErrorMessage(e);
                            kill();
                        } finally {
                            monitorWrap.done();
                        }
                    }
                    // };
                    // progressDialog.executeProcess();
                });
                // }, null);
            } catch (InvocationTargetException e1) {
                addErrorMessage(e1);
            } catch (InterruptedException e1) {
                addErrorMessage(e1);
            }

        } else {
            setRunning(false);
        }
    }

    /**
     * DOC amaumont Comment method "getProcessor".
     * 
     * @return
     */
    protected IProcessor getProcessor(IProcess process) {
        return ProcessorUtilities.getProcessor(process);
    }

    /**
     * Kill the process.
     * 
     * @return Exit code of the process.
     */
    public synchronized int kill() {
        int exitCode;

        if (!killing && isRunning()) {
            killing = true;
            try {
                boolean showEndMessage = (ps != null);
                exitCode = killProcess();

                if (showEndMessage) {
                    final String endingPattern = Messages.getString("ProcessComposite.endPattern"); //$NON-NLS-1$
                    MessageFormat mf = new MessageFormat(endingPattern);
                    String byeMsg = mf.format(new Object[] { process.getLabel(), new Date(), new Integer(exitCode) });
                    byeMsg = (processMessageManager.isLastMessageEndWithCR() ? "" : "\n") + byeMsg;
                    processMessageManager.addMessage(new ProcessMessage(MsgType.CORE_OUT, byeMsg));
                }
            } finally {
                killing = false;
            }
        } else {
            exitCode = 0;
        }

        setRunning(false);
        return exitCode;
    }

    private int killProcess() {
        if (psMonitor != null) {
            if (perfMonitor != null) {
                perfMonitor.stopThread();
                perfMonitor = null;
            }
            if (traceMonitor != null) {
                traceMonitor.stopThread();
                traceMonitor = null;
            }
            psMonitor.stopThread();
            psMonitor = null;
        }
        int exitCode = 0;
        if (ps != null) {
            ps.destroy();
            try {
                exitCode = ps.exitValue();
            } catch (IllegalThreadStateException itse) {
                // Can be throw on some UNIX system :(
                // but the process is really killed.
            }
            ps = null;
        }
        return exitCode;
    }

    private void addErrorMessage(Exception e) {
        StringBuffer message = new StringBuffer(STRING_LENGTH);
        message.append(Messages.getString("ProcessComposite.execFailed")); //$NON-NLS-1$
        message.append(e.getMessage());
        if (e.getCause() != null) {
            message.append("\n["); //$NON-NLS-1$
            message.append(e.getCause().getMessage());
            message.append("]"); //$NON-NLS-1$
        }
        message.append("\n"); //$NON-NLS-1$

        IProcessMessage processMsg = new ProcessMessage(MsgType.CORE_ERR, message.toString());
        processMessageManager.addMessage(processMsg);
    }

    private void findNewStatsPort() {
        statsPort = monitorPerf ? RunProcessPlugin.getDefault().getRunProcessContextManager().getPortForStatistics()
                : Processor.NO_STATISTICS;
    }

    private int getStatisticsPort() {
        return statsPort;
    }

    private void findNewTracesPort() {
        tracesPort = monitorTrace ? RunProcessPlugin.getDefault().getRunProcessContextManager().getPortForTraces()
                : Processor.NO_TRACES;
    }

    private int getTracesPort() {
        return tracesPort;
    }

    // private int getWatchPort() {
    // int port = watchAllowed ? RunProcessPlugin.getDefault()
    // .getRunProcessContextManager().getPortForWatch(this)
    // : Processor.WATCH_LIMITED;
    // return port;
    // }

    /**
     * Process activity monitor. <br/>
     * 
     * $Id$
     * 
     */
    protected class ProcessMonitor implements IProcessMonitor {

        volatile boolean stopThread;

        /** The monitoring process. */
        private Process process;

        /** Input stream for stdout of the process. */
        private BufferedReader outIs;

        /** Input stream for stderr of the process. */
        private BufferedReader errIs;

        public ProcessMonitor(Process ps) {
            super();

            this.process = ps;
            this.outIs = new BufferedReader(new InputStreamReader(ps.getInputStream()));
            this.errIs = new BufferedReader(new InputStreamReader(ps.getErrorStream()));
        }

        /**
         * @see java.lang.Runnable#run()
         */
        public void run() {
            while (!stopThread) {
                boolean dataPiped = extractMessages(false);

                boolean ended;
                try {
                    process.exitValue();

                    // Read the end of the stream after the end of the process
                    int i = 0;
                    while (extractMessages(true)) {
                        System.out.println(i++);
                        // nothing
                    }

                    ended = true;
                    stopThread = true;
                    try {
                        this.process.getInputStream().close();
                    } catch (IOException e) {
                        ExceptionHandler.process(e);
                    }
                    try {
                        this.process.getErrorStream().close();
                    } catch (IOException e) {
                        ExceptionHandler.process(e);
                    }

                } catch (IllegalThreadStateException itse) {
                    ended = false;
                }
                if (!dataPiped && !ended) {
                    synchronized (this) {
                        try {
                            final long waitTime = 100;
                            wait(waitTime);
                        } catch (InterruptedException e) {
                            // Do nothing
                        }
                    }
                }
            }

            kill();
        }

        public void stopThread() {
            stopThread = true;
            synchronized (this) {
                notify();
            }
        }

        private boolean extractMessages(boolean flush) {
            IProcessMessage messageOut = null;
            IProcessMessage messageErr = null;
            try {
                messageOut = extractMessage(outIs, MsgType.STD_OUT, flush);
                if (messageOut != null) {
                    processMessageManager.addMessage(messageOut);
                }
                messageErr = extractMessage(errIs, MsgType.STD_ERR, flush);
                if (messageErr != null) {
                    processMessageManager.addMessage(messageErr);
                }
            } catch (IOException ioe) {
                addErrorMessage(ioe);
                ExceptionHandler.process(ioe);
            }
            return messageOut != null || messageErr != null;
        }

        /**
         * Extract a message from a stream.
         * 
         * @param is Input stream to be read.
         * @param type Type of message read.
         * @param flush
         * @return the message extracted or null if no message was present.
         * @throws IOException Extraction failure.
         */
        private IProcessMessage extractMessage(final BufferedReader is, MsgType type, boolean flush) throws IOException {

            IProcessMessage msg;
            if (is.ready()) {
                int sizeBuffer = 1024;
                int currentSizeContent = 0;
                StringBuilder sb = new StringBuilder();
                while (is.ready() && (flush || currentSizeContent < sizeBuffer)) {
                    String dataStr = is.readLine();
                    sb.append(dataStr + "\n");
                    currentSizeContent += dataStr.length();
                    // byte[] data = new byte[len];
                    // is.read(data);
                    // final String dataStr = new String(data);
                    break;
                }
                msg = new ProcessMessage(type, sb.toString());
            } else {
                msg = null;
            }
            return msg;
        }

        protected Process getProcess() {
            return this.process;
        }

    }

    /**
     * Performance monitor. <br/>
     * 
     * $Id$
     * 
     */
    private class PerformanceMonitor implements Runnable {

        private volatile boolean stopThread;

        public PerformanceMonitor() {
            super();
        }

        /**
         * @see java.lang.Runnable#run()
         */
        public void run() {
            final int acceptTimeout = 30000;

            // Waiting connection from process
            Socket processSocket = null;
            ServerSocket serverSock = null;
            do {
                try {
                    serverSock = new ServerSocket(getStatisticsPort());
                    serverSock.setSoTimeout(acceptTimeout);
                    processSocket = serverSock.accept();
                } catch (IOException e) {
                    ExceptionHandler.process(e);
                    stopThread |= !isRunning();
                } finally {
                    try {
                        if (serverSock != null) {
                            serverSock.close();
                        }
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            } while (processSocket == null && !stopThread);

            if (processSocket != null && !stopThread) {
                try {
                    InputStream in = processSocket.getInputStream();
                    LineNumberReader reader = new LineNumberReader(new InputStreamReader(in));
                    while (!stopThread) {
                        final String data = reader.readLine();
                        if (data == null) {
                            stopThread = true;
                        } else {
                            PerformanceData perfData = new PerformanceData(data);
                            String nodeId = perfData.getNodeId();
                            final INode node = findNode(nodeId);
                            if (node != null) {
                                Display.getDefault().asyncExec(new Runnable() {

                                    public void run() {
                                        if (data != null) {
                                            node.setPerformanceData(data);
                                        }
                                    }
                                });
                            }
                        }
                    }
                } catch (IOException e) {
                    // Do nothing : process is ended
                } finally {
                    try {
                        processSocket.close();
                    } catch (IOException ioe) {
                        // Do nothing
                    }
                }
            }
        }

        public void stopThread() {
            stopThread = true;
            synchronized (this) {
                notify();
            }
        }

        private INode findNode(final String nodeId) {
            INode node = null;
            for (Iterator<? extends INode> i = process.getGraphicalNodes().iterator(); node == null && i.hasNext();) {
                INode psNode = i.next();
                if (nodeId.equals(psNode.getUniqueName())) {
                    node = psNode;
                }
            }
            return node;
        }
    }

    /**
     * Trace monitor. <br/>
     * 
     * $Id$
     * 
     */
    private class TraceMonitor implements Runnable {

        private volatile boolean stopThread;

        public TraceMonitor() {
            super();
        }

        /**
         * @see java.lang.Runnable#run()
         */
        public void run() {
            final int acceptTimeout = 30000;

            // Waiting connection from process
            Socket processSocket = null;
            ServerSocket serverSock = null;
            do {
                try {
                    serverSock = new ServerSocket(getTracesPort());
                    serverSock.setSoTimeout(acceptTimeout);
                    processSocket = serverSock.accept();
                } catch (IOException e) {
                    ExceptionHandler.process(e);
                    try {
                        if (serverSock != null) {
                            serverSock.close();
                        }
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    } finally {
                        try {
                            if (serverSock != null) {
                                serverSock.close();
                            }
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                    }
                    stopThread |= !isRunning();
                }
            } while (processSocket == null && !stopThread);

            if (processSocket != null && !stopThread) {
                try {
                    InputStream in = processSocket.getInputStream();
                    LineNumberReader reader = new LineNumberReader(new InputStreamReader(in));
                    while (!stopThread) {
                        final String data = reader.readLine();
                        if (data == null) {
                            stopThread = true;
                        } else {
                            TraceData traceData = new TraceData(data);
                            String connectionId = traceData.getElementId();
                            final IConnection connection = findConnection(connectionId);
                            if (connection != null) {
                                Display.getDefault().asyncExec(new Runnable() {

                                    public void run() {
                                        if (data != null) {
                                            connection.setTraceData(data);
                                        }
                                    }
                                });
                            }
                        }
                    }
                } catch (IOException e) {
                    // Do nothing : process is ended
                } finally {
                    try {
                        processSocket.close();
                    } catch (IOException ioe) {
                        // Do nothing
                    }
                }
            }
        }

        public void stopThread() {
            stopThread = true;
            synchronized (this) {
                notify();
            }
        }

        private IConnection findConnection(final String connectionId) {
            IConnection connection = null;
            for (Iterator<? extends INode> i = process.getGraphicalNodes().iterator(); connection == null
                    && i.hasNext();) {
                INode psNode = i.next();
                for (IConnection connec : psNode.getOutgoingConnections()) {
                    if (connec.getName().equals(connectionId)) {
                        connection = connec;
                    }
                }
            }
            return connection;
        }
    }

    /**
     * Getter for watchAllowed.
     * 
     * @return the watchAllowed
     */
    public boolean isWatchAllowed() {
        return this.watchAllowed;
    }

    /**
     * Sets the watchAllowed.
     * 
     * @param watchAllowed the watchAllowed to set
     */
    public void setWatchAllowed(boolean watchAllowed) {
        if (this.watchAllowed != watchAllowed) {
            this.watchAllowed = watchAllowed;
            firePropertyChange(PROR_SWITCH_TIME, Boolean.valueOf(!watchAllowed), Boolean.valueOf(watchAllowed));
        }
    }

    public ITargetExecutionConfig getSelectedTargetExecutionConfig() {
        return this.selectedTargetExecutionConfig;
    }

    public void setSelectedTargetExecutionConfig(ITargetExecutionConfig selectedTargetExecutionConfiguration) {
        this.selectedTargetExecutionConfig = selectedTargetExecutionConfiguration;
    }

    /**
     * DOC amaumont Comment method "createProcessMonitor".
     * 
     * @param process
     * @return
     */
    protected IProcessMonitor createProcessMonitor(Process process) {
        return new ProcessMonitor(process);
    }

}
