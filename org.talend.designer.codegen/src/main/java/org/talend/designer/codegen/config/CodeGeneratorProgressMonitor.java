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
package org.talend.designer.codegen.config;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IProgressMonitorWithBlocking;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.ProgressMonitorWrapper;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.widgets.Display;

/**
 * DOC mhirt class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 */
public class CodeGeneratorProgressMonitor extends ProgressMonitorWrapper implements IProgressMonitorWithBlocking {

    /**
     * Threshold for how often the event loop is spun, in ms.
     */
    private static int tTHRESH = 100;

    /**
     * Maximum amount of time to spend processing events, in ms.
     */
    private static int tMAX = 50;

    /**
     * Last time the event loop was spun.
     */
    private long lastTime = System.currentTimeMillis();

    /**
     * The task name is the name of the current task in the event loop.
     */
    private String taskName;

    /**
     * Constructs a new instance of the receiver and forwards to monitor.
     * 
     * @param monitor
     */
    public CodeGeneratorProgressMonitor(IProgressMonitor monitor) {
        super(monitor);
    }

    /**
     * @see IProgressMonitor#beginTask
     */
    public void beginTask(String name, int totalWork) {
        super.beginTask(name, totalWork);
        taskName = name;
        runEventLoop();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.core.runtime.IProgressMonitorWithBlocking#clearBlocked()
     */
    public void clearBlocked() {
        Dialog.getBlockedHandler().clearBlocked();
    }

    /**
     * @see IProgressMonitor#done
     */
    public void done() {
        super.done();
        taskName = null;
        runEventLoop();
    }

    /**
     * @see IProgressMonitor#internalWorked
     */
    public void internalWorked(double work) {
        super.internalWorked(work);
        runEventLoop();
    }

    /**
     * @see IProgressMonitor#isCanceled
     */
    public boolean isCanceled() {
        runEventLoop();
        return super.isCanceled();
    }

    /**
     * Runs an event loop.
     */
    private void runEventLoop() {
        // Only run the event loop so often, as it is expensive on some platforms
        // (namely Motif).
        long t = System.currentTimeMillis();
        if (t - lastTime < tTHRESH) {
            return;
        }
        lastTime = t;
        // Run the event loop.
        Display disp = Display.getDefault();
        if (disp == null) {
            return;
        }

        for (;;) {
            try {
                if (!disp.readAndDispatch()) {
                    break;
                }
            } catch (SWTException se) {
                // do nothing;
            }

            // Only run the event loop for so long.
            // Otherwise, this would never return if some other thread was
            // constantly generating events.
            if (System.currentTimeMillis() - t > tMAX) {
                break;
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.core.runtime.IProgressMonitorWithBlocking#setBlocked(org.eclipse.core.runtime.IStatus)
     */
    public void setBlocked(IStatus reason) {
        Dialog.getBlockedHandler().showBlocked(this, reason, taskName);
    }

    /**
     * @see IProgressMonitor#setCanceled
     */
    public void setCanceled(boolean b) {
        super.setCanceled(b);
        taskName = null;
        runEventLoop();
    }

    /**
     * @see IProgressMonitor#setTaskName
     */
    public void setTaskName(String name) {
        super.setTaskName(name);
        taskName = name;
        runEventLoop();
    }

    /**
     * @see IProgressMonitor#subTask
     */
    public void subTask(String name) {
        // Be prepared in case the first task was null
        if (taskName == null) {
            taskName = name;
        }
        super.subTask(name);
        runEventLoop();
    }

    /**
     * @see IProgressMonitor#worked
     */
    public void worked(int work) {
        super.worked(work);
        runEventLoop();
    }

    /**
     * Return the name of the current task.
     * 
     * @return Returns the taskName.
     */
    protected String getTaskName() {
        return taskName;
    }
}
