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
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.progress.IProgressService;
import org.talend.core.model.process.IConnection;
import org.talend.core.model.process.IContext;
import org.talend.core.model.process.INode;
import org.talend.core.model.process.IProcess;
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
 * $Id: RunProcessContext.java 666 2006-11-22 02:28:05 +0000 (сре, 22 нов 2006)
 * ftang $
 * 
 */
public class RunProcessContext {

	private static final int STRING_LENGTH = 256;

	public static final String PROP_RUNNING = "RunProcessContext.Running"; //$NON-NLS-1$

	public static final String PROP_MONITOR = "RunProcessContext.MonitorPerf"; //$NON-NLS-1$

	public static final String TRACE_MONITOR = "RunProcessContext.MonitorTrace"; //$NON-NLS-1$

	public static final String PROP_MESSAGE_ADD = "RunProcessContext.Message.Added"; //$NON-NLS-1$

	public static final String PROP_MESSAGE_CLEAR = "RunProcessContext.Message.Cleared"; //$NON-NLS-1$

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

	/** Performance monitoring activated. */
	private boolean monitorPerf;

	/** Trace monitoring activated. */
	private boolean monitorTrace;

	/** Is process running. */
	private boolean running;

	/** Messages associated to the process. */
	private List<ProcessMessage> messages;

	/** The executing process. */
	private Process ps;

	/** Monitor of the running process. */
	private ProcessMonitor psMonitor;

	/** Monitor of the running process. */
	private PerformanceMonitor perfMonitor;

	/** Monitor for Traces of the running process. */
	private TraceMonitor traceMonitor;

	/** Kill is in progress. */
	private boolean killing;

	/**
	 * Constrcuts a new RunProcessContext.
	 * 
	 * @param process
	 *            The process.
	 */
	public RunProcessContext(IProcess process) {
		super();

		this.process = process;
		selectedContext = process.getContextManager().getDefaultContext();
		messages = new ArrayList<ProcessMessage>();

		pcsDelegate = new PropertyChangeSupport(this);
	}

	public synchronized void addPropertyChangeListener(PropertyChangeListener l) {
		if (l == null) {
			throw new IllegalArgumentException();
		}

		pcsDelegate.addPropertyChangeListener(l);
	}

	protected void firePropertyChange(String property, Object oldValue,
			Object newValue) {
		if (pcsDelegate.hasListeners(property)) {
			pcsDelegate.firePropertyChange(property, oldValue, newValue);
		}
	}

	public synchronized void removePropertyChangeListener(
			PropertyChangeListener l) {
		if (l != null) {
			pcsDelegate.removePropertyChangeListener(l);
		}
	}

	public IProcess getProcess() {
		return process;
	}

	protected void addMessage(ProcessMessage message) {
		synchronized (messages) {
			messages.add(message);
		}

		firePropertyChange(PROP_MESSAGE_ADD, null, message);
	}

	public void clearMessages() {
		synchronized (messages) {
			messages.clear();
		}

		firePropertyChange(PROP_MESSAGE_CLEAR, messages, null);
	}

	// Added by ftang
	public void switchTime() {
		// TODO should do something here.
		firePropertyChange(PROR_SWITCH_TIME, "true", "false");
	}

	// Ends

	public List<ProcessMessage> getMessages() {
		return messages;
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
	 * @param monitorPerf
	 *            the monitorPerf to set
	 */
	public void setMonitorPerf(boolean monitorPerf) {
		if (this.monitorPerf != monitorPerf) {
			this.monitorPerf = monitorPerf;
			firePropertyChange(PROP_MONITOR, Boolean.valueOf(!monitorPerf),
					Boolean.valueOf(monitorPerf));
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
	 * @param monitorTrace
	 *            the monitorTraceto set
	 */
	public void setMonitorTrace(boolean monitorTrace) {
		if (this.monitorTrace != monitorTrace) {
			this.monitorTrace = monitorTrace;
			firePropertyChange(TRACE_MONITOR, Boolean.valueOf(!monitorTrace),
					Boolean.valueOf(monitorTrace));
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
	 * @param running
	 *            the running to set
	 */
	private void setRunning(boolean running) {
		if (this.running != running) {
			this.running = running;
			firePropertyChange(PROP_RUNNING, Boolean.valueOf(!running), Boolean
					.valueOf(running));
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
	public void exec(Shell shell) {
		setRunning(true);

		if (ProcessContextComposite.promptConfirmLauch(shell,
				getSelectedContext())) {

			final String startingPattern = Messages
					.getString("ProcessComposite.startPattern"); //$NON-NLS-1$
			MessageFormat mf = new MessageFormat(startingPattern);
			String welcomeMsg = mf.format(new Object[] { process.getLabel(),
					new Date() });
			addMessage(new ProcessMessage(MsgType.CORE_OUT, welcomeMsg));

			ClearPerformanceAction clearPerfAction = new ClearPerformanceAction();
			clearPerfAction.setProcess(process);
			clearPerfAction.run();

			ClearTraceAction clearTraceAction = new ClearTraceAction();
			clearTraceAction.setProcess(process);
			clearTraceAction.run();

			final Processor processor = new Processor(process);
			IProgressService progressService = PlatformUI.getWorkbench()
					.getProgressService();
			try {
				progressService.runInUI(PlatformUI.getWorkbench()
						.getProgressService(), new IRunnableWithProgress() {

					public void run(final IProgressMonitor monitor) {
						monitor
								.beginTask(
										Messages
												.getString("ProcessComposite.buildTask"), IProgressMonitor.UNKNOWN); //$NON-NLS-1$
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

								String watchParam = RunProcessContext.this
										.isWatchAllowed() ? WATCH_PARAM : null;
								
								ps = processor.run(getSelectedContext(),
										getStatisticsPort(), getTracesPort(),
										watchParam);
								psMonitor = new ProcessMonitor(ps);
								new Thread(psMonitor).start();
							} catch (ProcessorException e) {
								addErrorMessage(e);
								kill();
							}
						} finally {
							monitor.done();
						}
					}
				}, null);
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
	 * Kill the process.
	 * 
	 * @return Exit code of the process.
	 */
	public int kill() {
		int exitCode;

		if (!killing && isRunning()) {
			killing = true;
			try {
				exitCode = killProcess();

				final String endingPattern = Messages
						.getString("ProcessComposite.endPattern"); //$NON-NLS-1$
				MessageFormat mf = new MessageFormat(endingPattern);
				String byeMsg = mf.format(new Object[] { process.getLabel(),
						new Date(), new Integer(exitCode) });
				addMessage(new ProcessMessage(MsgType.CORE_OUT, byeMsg));
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
		message.append("\n");

		ProcessMessage processMsg = new ProcessMessage(MsgType.CORE_ERR,
				message.toString());
		addMessage(processMsg);
	}

	private int getStatisticsPort() {
		int port = monitorPerf ? RunProcessPlugin.getDefault()
				.getRunProcessContextManager().getPortForStatistics(this)
				: Processor.NO_STATISTICS;
		return port;
	}

	private int getTracesPort() {
		int port = monitorTrace ? RunProcessPlugin.getDefault()
				.getRunProcessContextManager().getPortForTraces(this)
				: Processor.NO_TRACES;
		return port;
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
	 * $Id: RunProcessContext.java 666 2006-11-22 02:28:05 +0000 (сре, 22 нов
	 * 2006) ftang $
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

		public ProcessMonitor(Process ps) {
			super();

			this.ps = ps;
			this.outIs = ps.getInputStream();
			this.errIs = ps.getErrorStream();
		}

		/**
		 * @see java.lang.Runnable#run()
		 */
		public void run() {
			while (!stopThread) {
				boolean dataPiped = extractMessages();

				boolean ended;
				try {
					ps.exitValue();
					ended = true;
					stopThread = true;

					// Read the end of the stream after the end of the process
					extractMessages();
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

		private boolean extractMessages() {
			ProcessMessage messageOut = null;
			ProcessMessage messageErr = null;
			try {
				messageOut = extractMessage(outIs, MsgType.STD_OUT);
				if (messageOut != null) {
					addMessage(messageOut);
				}
				messageErr = extractMessage(errIs, MsgType.STD_ERR);
				if (messageErr != null) {
					addMessage(messageErr);
				}
			} catch (IOException ioe) {
				addErrorMessage(ioe);
			}
			return messageOut != null || messageErr != null;
		}

		/**
		 * Extract a message from a stream.
		 * 
		 * @param is
		 *            Input stream to be read.
		 * @param type
		 *            Type of message read.
		 * @return the message extracted or null if no message was present.
		 * @throws IOException
		 *             Extraction failure.
		 */
		private ProcessMessage extractMessage(final InputStream is, MsgType type)
				throws IOException {
			ProcessMessage msg;
			int len = is.available();
			if (len > 0) {
				byte[] data = new byte[len];
				is.read(data);
				final String dataStr = new String(data);
				msg = new ProcessMessage(type, dataStr);
			} else {
				msg = null;
			}
			return msg;
		}
	}

	/**
	 * Performance monitor. <br/>
	 * 
	 * $Id: RunProcessContext.java 666 2006-11-22 02:28:05 +0000 (сре, 22 нов
	 * 2006) ftang $
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
			final int acceptTimeout = 5000;

			// Waiting connection from process
			Socket processSocket = null;
			ServerSocket serverSock = null;
			try {
				do {
					try {
						serverSock = new ServerSocket(getStatisticsPort());
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
					LineNumberReader reader = new LineNumberReader(
							new InputStreamReader(in));
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
			for (Iterator<? extends INode> i = process.getGraphicalNodes()
					.iterator(); node == null && i.hasNext();) {
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
	 * $Id: RunProcessContext.java 666 2006-11-22 02:28:05 +0000 (сре, 22 нов
	 * 2006) ftang $
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
			final int acceptTimeout = 5000;

			// Waiting connection from process
			Socket processSocket = null;
			ServerSocket serverSock = null;
			try {
				do {
					try {
						serverSock = new ServerSocket(getTracesPort());
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
					LineNumberReader reader = new LineNumberReader(
							new InputStreamReader(in));
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
			for (Iterator<? extends INode> i = process.getGraphicalNodes()
					.iterator(); connection == null && i.hasNext();) {
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
	 * @param watchAllowed
	 *            the watchAllowed to set
	 */
	public void setWatchAllowed(boolean watchAllowed) {
		if (this.watchAllowed != watchAllowed) {
			this.watchAllowed = watchAllowed;
			firePropertyChange(PROR_SWITCH_TIME,
					Boolean.valueOf(!watchAllowed), Boolean
							.valueOf(watchAllowed));
		}
	}
}
