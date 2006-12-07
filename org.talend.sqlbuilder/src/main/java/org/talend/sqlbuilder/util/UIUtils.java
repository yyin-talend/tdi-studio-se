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
package org.talend.sqlbuilder.util;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.operation.ModalContext;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.talend.sqlbuilder.SqlBuilderPlugin;

/**
 * DOC qianbing class global comment. Detailled comment <br/>
 * 
 * $Id: UIUtils.java,v 1.3 2006/11/07 07:37:22 qianbing Exp $
 * 
 */
public class UIUtils {

	/**
	 * DOC dev UIUtils constructor comment.
	 */
	public UIUtils() {
	}
    /**
     * Open a error dialog.
     * 
     * @param msg String
     * @param e Exception
     */
    public static void openErrorDialog(final String msg, final Exception e) {

        SqlBuilderPlugin.log(msg, e);
        final Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
        Display.getDefault().asyncExec(new Runnable() {

            public void run() {
                MessageDialog.openError(shell, msg, e.getMessage());
            }
        });
    }

    /**
     * This implementation of IRunnableContext#run(boolean, boolean, IRunnableWithProgress) runs the given
     * <code>IRunnableWithProgress</code> using the progress monitor for this progress dialog and blocks until the
     * runnable has been run, regardless of the value of <code>fork</code>. The dialog is opened before the runnable
     * is run, and closed after it completes. It is recommended that <code>fork</code> is set to true in most cases.
     * If <code>fork</code> is set to <code>false</code>, the runnable will run in the UI thread and it is the
     * runnable's responsibility to call <code>Display.readAndDispatch()</code> to ensure UI responsiveness.
     * 
     * 
     * <pre>
     * final IRunnableWithProgress r = new IRunnableWithProgress() {
     * 
     *     public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
     *         monitor.beginTask(&quot;test task&quot;, 100);
     *         for (int i = 0; i &lt; 100; i++) {
     *             monitor.worked(1);
     *             try {
     *                 Thread.sleep(50);
     *             } catch (Exception e) {
     *             }
     *         }
     *         monitor.done();
     *     }
     * };
     * runWithProgress(r);
     * </pre>
     */
    /**
     * DOC dev Comment method "runWithProgress".
     * @param operation
     * @param fork
     * @param monitor
     * @param shell
     */
    public static void runWithProgress(final IRunnableWithProgress operation, final boolean fork,
            final IProgressMonitor monitor, final Shell shell) {
        final IRunnableWithProgress progress = new IRunnableWithProgress() {
            public void run(final IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
                // monitor.beginTask("test task", scale * total);
                // SubProgressMonitor sm = new SubProgressMonitor(monitor, 1 * scale);
                Thread t = new Thread() {

                    public void run() {
                        try {
                            operation.run(monitor);
                        } catch (Exception e) {
                            SqlBuilderPlugin.log("error occurs", e);
                        }
                    }
                };
                t.start();
                try {
                    t.join();
                } catch (Exception e) {
                    // TODO: handle exception
                }
            }
        };

        Runnable run = new Runnable() {
            public void run() {
                try {
                    ModalContext.run(progress, fork, monitor, shell.getDisplay());
                } catch (Exception e) {
                    SqlBuilderPlugin.log("something errors with the runnable process", e);
                }
            }
        };

        BusyIndicator.showWhile(Display.getDefault(), run);
    }
}
