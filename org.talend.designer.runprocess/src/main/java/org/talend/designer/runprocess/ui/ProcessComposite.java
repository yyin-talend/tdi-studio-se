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
package org.talend.designer.runprocess.ui;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.lang.reflect.InvocationTargetException;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.MessageFormat;
import java.util.Date;
import java.util.Iterator;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.progress.IProgressService;
import org.talend.core.model.process.INode;
import org.talend.core.model.process.IProcess;
import org.talend.designer.runprocess.Processor;
import org.talend.designer.runprocess.ProcessorException;
import org.talend.designer.runprocess.RunProcessPlugin;
import org.talend.designer.runprocess.data.PerformanceData;
import org.talend.designer.runprocess.data.TraceData;
import org.talend.designer.runprocess.i18n.Messages;
import org.talend.designer.runprocess.ui.actions.ClearPerformanceAction;
import org.talend.designer.runprocess.ui.actions.ClearTraceAction;

/**
 * Composite showing a process. Detailled comment <br/>
 * 
 * $Id$
 * @deprecated
 */
public class ProcessComposite extends Composite {

    private static final int STRING_LENGTH = 256;

    private static final int PERF_PORT = 3334;
    
    private static final int TRACE_PORT = 4334;
    
    private static final int H_WEIGHT = 5;

    private static final int MINIMUM_HEIGHT = 65;

    public static final String PROP_RUNNING = "ProcessComposite.Running"; //$NON-NLS-1$

    private transient PropertyChangeSupport pcsDelegate;

    /** The designer process to be run. */
    private IProcess process;

    /** Performance monitoring activated. */
    private boolean monitorPerf;
    
    /** Trace monitoring activated. */
    private boolean monitorTrace;

    /** Context composite. */
    private ProcessContextComposite contextComposite;

    /** Performance button. */
    private Button perfBtn;
    
    /** Trace button. */
    private Button traceBtn;

    /** Clear trace & performance button. */
    private Button clearTracePerfBtn;
    
    /** Exec button. */
    private Button execBtn;

    /** Kill button. */
    private Button killBtn;

    /** Execution console. */
    private StyledText consoleText;

    /** The executing process. */
    private Process ps;

    /** Monitor of the running process. */
    private ProcessMonitor psMonitor;

    /** Monitor of the running process. */
    private PerformanceMonitor perfMonitor;
    
    /** Monitor of the running process. */
    private TraceMonitor traceMonitor;

    private boolean running;

    /**
     * Construct a new ProcessComposite.
     * 
     * @param parent Parent composite.
     * @param style Style bits.
     */
    public ProcessComposite(Composite parent, int style) {
        super(parent, style);

        pcsDelegate = new PropertyChangeSupport(this);

        GridLayout layout = new GridLayout();
        setLayout(layout);

        // Splitter
        SashForm sash = new SashForm(this, SWT.HORIZONTAL | SWT.SMOOTH);
        sash.setLayoutData(new GridData(GridData.FILL_BOTH));

        // Group context
        contextComposite = new ProcessContextComposite(sash, SWT.NONE);

        // Group execution
        Group execGroup = new Group(sash, SWT.NONE);
        execGroup.setText(Messages.getString("ProcessComposite.execGroup")); //$NON-NLS-1$
        layout = new GridLayout();
        layout.marginHeight = 0;
        layout.marginWidth = 0;
        execGroup.setLayout(layout);

        ScrolledComposite execScroll = new ScrolledComposite(execGroup, SWT.V_SCROLL | SWT.H_SCROLL);
        execScroll.setExpandHorizontal(true);
        execScroll.setExpandVertical(true);
        execScroll.setLayoutData(new GridData(GridData.FILL_BOTH));

        Composite execContent = new Composite(execScroll, SWT.NONE);
        layout = new GridLayout();
        execContent.setLayout(layout);
        execScroll.setContent(execContent);

        layout = new GridLayout(2, false);
        layout.marginHeight = 0;
        layout.marginWidth = 0;
        Composite execHeader = new Composite(execContent, SWT.NONE);
        execHeader.setLayout(layout);
        execHeader.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        
        Composite statisticsComposite = new Composite(execHeader, SWT.NONE);
        layout = new GridLayout(2, false);
        layout.marginWidth = 0;
        statisticsComposite.setLayout(layout);
        statisticsComposite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        Composite statisticsButtonComposite = new Composite(statisticsComposite, SWT.NONE);
        layout = new GridLayout(1, false);
        layout.marginWidth = 0;
        statisticsButtonComposite.setLayout(layout);
        statisticsButtonComposite.setLayoutData(new GridData(GridData.FILL_VERTICAL));
        
        perfBtn = new Button(statisticsButtonComposite, SWT.CHECK);
        perfBtn.setText(Messages.getString("ProcessComposite.stat")); //$NON-NLS-1$
        perfBtn.setToolTipText(Messages.getString("ProcessComposite.statHint")); //$NON-NLS-1$
        perfBtn.setImage(RunProcessPlugin.imageDescriptorFromPlugin(RunProcessPlugin.PLUGIN_ID,
                "icons/process_stat.gif").createImage()); //$NON-NLS-1$
        // perfBtn.setLayoutData(new GridData(GridData.VERTICAL_ALIGN_CENTER | GridData.FILL_HORIZONTAL));
        perfBtn.setEnabled(false);
        
        traceBtn = new Button(statisticsButtonComposite, SWT.CHECK);
        traceBtn.setText(Messages.getString("ProcessComposite.trace")); //$NON-NLS-1$
        traceBtn.setToolTipText(Messages.getString("ProcessComposite.traceHint")); //$NON-NLS-1$
        // PTODO MHI Change this image
        traceBtn.setImage(RunProcessPlugin.imageDescriptorFromPlugin(RunProcessPlugin.PLUGIN_ID,
                "icons/process_stat.gif").createImage()); //$NON-NLS-1$
        // traceBtn.setLayoutData(new GridData(GridData.VERTICAL_ALIGN_CENTER | GridData.FILL_HORIZONTAL));
        traceBtn.setEnabled(false);

        
        clearTracePerfBtn = new Button(statisticsComposite, SWT.CHECK);
        clearTracePerfBtn.setText(Messages.getString("ProcessComposite.clear")); //$NON-NLS-1$
        clearTracePerfBtn.setToolTipText(Messages.getString("ProcessComposite.clearHint")); //$NON-NLS-1$
        clearTracePerfBtn.setImage(RunProcessPlugin.imageDescriptorFromPlugin(RunProcessPlugin.PLUGIN_ID,
                "icons/process_stat_clear.gif").createImage()); //$NON-NLS-1$
        clearTracePerfBtn.setEnabled(false);
        
        Composite buttonBar = new Composite(execHeader, SWT.NONE);
        layout = new GridLayout(2, true);
        layout.marginWidth = 0;
        buttonBar.setLayout(layout);
        buttonBar.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_END));

        execBtn = new Button(buttonBar, SWT.PUSH);
        execBtn.setText(Messages.getString("ProcessComposite.exec")); //$NON-NLS-1$
        execBtn.setToolTipText(Messages.getString("ProcessComposite.execHint")); //$NON-NLS-1$
        execBtn.setImage(RunProcessPlugin
                .imageDescriptorFromPlugin(RunProcessPlugin.PLUGIN_ID, "icons/process_run.png").createImage()); //$NON-NLS-1$
        setButtonLayoutData(execBtn);
        execBtn.setEnabled(false);

        killBtn = new Button(buttonBar, SWT.PUSH);
        killBtn.setText(Messages.getString("ProcessComposite.kill")); //$NON-NLS-1$
        killBtn.setToolTipText(Messages.getString("ProcessComposite.killHint")); //$NON-NLS-1$
        killBtn.setImage(RunProcessPlugin.imageDescriptorFromPlugin(RunProcessPlugin.PLUGIN_ID,
                "icons/process_kill.gif").createImage()); //$NON-NLS-1$
        setButtonLayoutData(killBtn);
        killBtn.setEnabled(false);

        consoleText = new StyledText(execContent, SWT.BORDER | SWT.WRAP | SWT.V_SCROLL | SWT.READ_ONLY);
        GridData data = new GridData(GridData.FILL_BOTH);
        data.horizontalSpan = 2;
        data.minimumHeight = MINIMUM_HEIGHT;
        consoleText.setLayoutData(data);

        execScroll.setMinSize(execContent.computeSize(SWT.DEFAULT, SWT.DEFAULT));
        sash.setWeights(new int[] { 2, H_WEIGHT });

        addListeners();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.swt.widgets.Widget#dispose()
     */
    @Override
    public void dispose() {
        killProcess();

        super.dispose();
    }

    public synchronized void addPropertyChangeListener(PropertyChangeListener l) {
        if (l == null) {
            throw new IllegalArgumentException();
        }

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
        }
    }

    private void addListeners() {
        perfBtn.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(final SelectionEvent e) {
                monitorPerf = perfBtn.getSelection();
            }
        });
        
        traceBtn.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(final SelectionEvent e) {
                monitorTrace = traceBtn.getSelection();
            }
        });

        clearTracePerfBtn.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                ClearPerformanceAction clearPerfAction = new ClearPerformanceAction();
                clearPerfAction.setProcess(process);
                clearPerfAction.run();
                ClearTraceAction clearTraceAction = new ClearTraceAction();
                clearTraceAction.setProcess(process);
                clearTraceAction.run();
            }
        });

        execBtn.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(final SelectionEvent e) {
                exec();
            }
        });

        killBtn.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(final SelectionEvent e) {
                kill();
            }
        });
    }

    /**
     * Set the layout data of the button to a GridData with appropriate heights and widths.
     * 
     * @param button
     */
    protected static void setButtonLayoutData(final Button button) {
        GridData data = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
        final int widthHint = 80;
        Point minSize = button.computeSize(SWT.DEFAULT, SWT.DEFAULT, true);
        data.widthHint = Math.max(widthHint, minSize.x);
        button.setLayoutData(data);
    }

    public void setProcess(IProcess newProcess, boolean newMonitorPerf, boolean newMonitorTrace) {
        this.process = newProcess;
        this.monitorPerf = newMonitorPerf;
        this.monitorTrace = newMonitorTrace;

        setRunning(false);
        perfBtn.setSelection(monitorPerf);
        traceBtn.setSelection(monitorTrace);
        setRunnable(process != null);
        contextComposite.setProcess(process);
        consoleText.setText(""); //$NON-NLS-1$
    }

    private void exec() {
        setRunnable(false);

        consoleText.setText(""); //$NON-NLS-1$
        final String startingPattern = Messages.getString("ProcessComposite.startPattern"); //$NON-NLS-1$
        MessageFormat mf = new MessageFormat(startingPattern);
        String welcomeMsg = mf.format(new Object[] { process.getLabel(), new Date() });
        appendMetaToConsole(welcomeMsg);

        ClearPerformanceAction clearAction = new ClearPerformanceAction();
        clearAction.setProcess(process);
        clearAction.run();

        if (contextComposite.promptConfirmLauch()) {

            final Processor processor = new Processor(process);
            IProgressService progressService = PlatformUI.getWorkbench().getProgressService();
            try {
                progressService.runInUI(PlatformUI.getWorkbench().getProgressService(), new IRunnableWithProgress() {

                    public void run(final IProgressMonitor monitor) {
                        monitor.beginTask(Messages.getString("ProcessComposite.buildTask"), IProgressMonitor.UNKNOWN); //$NON-NLS-1$
                        setRunning(true);
                        try {
                            try {
                                if (monitorPerf) {
                                    perfMonitor = new PerformanceMonitor();
                                    new Thread(perfMonitor).start();
                                }
                                if (monitorTrace) {
                                    traceMonitor = new TraceMonitor();
                                    new Thread(traceMonitor).start();
                                }
                                int port = monitorPerf ? PERF_PORT : Processor.NO_STATISTICS;
                                int portTrace = monitorTrace ? TRACE_PORT : Processor.NO_TRACES;
//                                int watchPort = watchAllowed? SWATCH_PORT : Processor.WATCH_LIMITED;//Old
                                ps = processor.run(contextComposite.getSelectedContext(), port, portTrace,null);
                                psMonitor = new ProcessMonitor(ps);
                                new Thread(psMonitor).start();

                                killBtn.setEnabled(true);
                            } catch (ProcessorException e) {
                                setRunning(false);
                                setRunnable(true);
                                logFailure(e);
                            }
                        } finally {
                            monitor.done();
                        }
                    }
                }, null);
            } catch (InvocationTargetException e1) {
                logFailure(e1);
            } catch (InterruptedException e1) {
                logFailure(e1);
            }

        } else {
            setRunnable(true);
        }
    }

    private int killProcess() {
        if (psMonitor != null) {
            if (perfMonitor != null) {
                perfMonitor.stopThread();
                perfMonitor = null;
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

    private void kill() {
        killBtn.setEnabled(false);

        int exitCode = killProcess();

        final String endingPattern = Messages.getString("ProcessComposite.endPattern"); //$NON-NLS-1$
        MessageFormat mf = new MessageFormat(endingPattern);
        String byeMsg = mf.format(new Object[] { process.getLabel(), new Date(), new Integer(exitCode) });
        appendMetaToConsole(byeMsg);

        setRunning(false);
        setRunnable(true);
    }

    private void processEnded() {
        getDisplay().asyncExec(new Runnable() {

            public void run() {
                kill();
            }
        });
    }

    private void setRunnable(boolean runnable) {
        perfBtn.setEnabled(runnable);
        traceBtn.setEnabled(runnable);
        clearTracePerfBtn.setEnabled(runnable);
        execBtn.setEnabled(runnable);
    }

    private void appendToConsole(final String data, final Color foreground) {
        StyleRange style = new StyleRange();
        style.start = consoleText.getText().length();
        style.length = data.length();
        style.foreground = foreground;

        consoleText.append(data);
        consoleText.setStyleRange(style);
        scrollToEnd();
    }

    private void appendMetaToConsole(String data) {
        StyleRange style = new StyleRange();
        style.start = consoleText.getText().length();
        style.length = data.length();
        style.fontStyle = SWT.ITALIC;
        style.foreground = getDisplay().getSystemColor(SWT.COLOR_BLUE);

        consoleText.append(data);
        consoleText.setStyleRange(style);
        scrollToEnd();
    }

    private void scrollToEnd() {
        consoleText.setCaretOffset(consoleText.getText().length());
        consoleText.showSelection();
    }

    private void logFailure(final Exception e) {
        Color failureColor = getDisplay().getSystemColor(SWT.COLOR_DARK_RED);
        StringBuffer message = new StringBuffer(STRING_LENGTH);
        message.append(Messages.getString("ProcessComposite.execFailed")); //$NON-NLS-1$
        message.append(e.getMessage());
        if (e.getCause() != null) {
            message.append("\n["); //$NON-NLS-1$
            message.append(e.getCause().getMessage());
            message.append("]"); //$NON-NLS-1$
        }
        appendToConsole(message.toString(), failureColor);
    }

    /**
     * Process activity monitor. <br/>
     * 
     * $Id$
     * 
     */
    private class ProcessMonitor implements Runnable {

        private volatile boolean stopThread;

        /** The monitoring process. */
        private Process ps;

        /** Input stream for stdout of the process. */
        private InputStream outIs;

        /** Input stream for stderr of the process. */
        private InputStream errIs;

        /** Color for stdout stream. */
        private Color outColor;

        /** Color for stderr stream. */
        private Color errColor;

        public ProcessMonitor(Process ps) {
            super();

            this.ps = ps;
            this.outIs = ps.getInputStream();
            this.errIs = ps.getErrorStream();

            outColor = ProcessComposite.this.getDisplay().getSystemColor(SWT.COLOR_BLACK);
            errColor = ProcessComposite.this.getDisplay().getSystemColor(SWT.COLOR_RED);
        }

        /**
         * @see java.lang.Runnable#run()
         */
        public void run() {
            while (!stopThread) {
                boolean dataPiped = false;
                try {
                    dataPiped = pipeData(outIs, outColor);
                    dataPiped |= pipeData(errIs, errColor);
                } catch (IOException ioe) {
                    logFailure(ioe);
                }

                boolean ended;
                try {
                    ps.exitValue();
                    ended = true;
                    stopThread = true;

                    // Read the end of the stream after the end of the process
                    try {
                        pipeData(outIs, outColor);
                        pipeData(errIs, errColor);
                    } catch (IOException ioe) {
                        logFailure(ioe);
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

            processEnded();
        }

        public void stopThread() {
            stopThread = true;
            synchronized (this) {
                notify();
            }
        }

        /**
         * Pipe data from a stream to the console with a given color.
         * 
         * @param is Input stream to be read.
         * @param color Color of text in the console.
         * @return true if some data was piped.
         * @throws IOException Pipe failure.
         */
        private boolean pipeData(final InputStream is, final Color color) throws IOException {
            int len = is.available();
            if (len > 0) {
                byte[] data = new byte[len];
                is.read(data);
                final String dataStr = new String(data);
                ProcessComposite.this.getDisplay().asyncExec(new Runnable() {

                    public void run() {
                        appendToConsole(dataStr, color);
                    }
                });
            }
            return len > 0;
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
            final int port = PERF_PORT;
            final int acceptTimeout = 5000;

            // Waiting connection from process
            Socket processSocket = null;
            ServerSocket serverSock = null;
            try {
                do {
                    try {
                        serverSock = new ServerSocket(port);
                        serverSock.setSoTimeout(acceptTimeout);
                        processSocket = serverSock.accept();
                    } catch (IOException e) {
                        stopThread |= !isRunning();
                    }
                } while (processSocket == null && !stopThread);
            } finally {
                try {
                    serverSock.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    stopThread = true;
                }
            }

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
                            if (node != null && !isDisposed()) {
                                getDisplay().asyncExec(new Runnable() {

                                    public void run() {
                                        node.setPerformanceData(data);
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
            for (Iterator< ? extends INode> i = process.getGraphicalNodes().iterator(); node == null && i.hasNext();) {
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
            final int port = TRACE_PORT;
            final int acceptTimeout = 5000;

            // Waiting connection from process
            Socket processSocket = null;
            ServerSocket serverSock = null;
            try {
                do {
                    try {
                        serverSock = new ServerSocket(port);
                        serverSock.setSoTimeout(acceptTimeout);
                        processSocket = serverSock.accept();
                    } catch (IOException e) {
                        stopThread |= !isRunning();
                    }
                } while (processSocket == null && !stopThread);
            } finally {
                try {
                    serverSock.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    stopThread = true;
                }
            }

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
                            String nodeId = traceData.getElementId();
                            final INode node = findNode(nodeId);
                            if (node != null && !isDisposed()) {
                                getDisplay().asyncExec(new Runnable() {

                                    public void run() {
                                        node.setPerformanceData(data);
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
            for (Iterator< ? extends INode> i = process.getGraphicalNodes().iterator(); node == null && i.hasNext();) {
                INode psNode = i.next();
                if (nodeId.equals(psNode.getUniqueName())) {
                    node = psNode;
                }
            }
            return node;
        }
    }

    /**
     * Getter for running.
     * 
     * @return the running
     */
    private boolean isRunning() {
        return this.running;
    }

    /**
     * Sets the running.
     * 
     * @param running the running to set
     */
    private void setRunning(boolean running) {
        if (running != this.running) {
            this.running = running;

            firePropertyChange(PROP_RUNNING, new Boolean(!running), new Boolean(running));
        }
    }
}
