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
package org.talend.designer.runprocess.ui.actions;

import java.util.List;

import org.eclipse.jface.action.Action;
import org.talend.core.model.process.IConnection;
import org.talend.core.model.process.INode;
import org.talend.core.model.process.IPerformance;
import org.talend.core.model.process.IProcess;

/**
 * Clean performance data on a process. <br/>
 * 
 * $Id$
 * 
 */
public class ClearPerformanceAction extends Action {

    /** The process to be cleared. */
    private IProcess process;

    /**
     * Constructs a new ClearPerformanceAction.
     */
    public ClearPerformanceAction() {
        super("Clean performance tracking"); //$NON-NLS-1$

        setEnabled(false);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.action.Action#run()
     */
    @Override
    public void run() {
        IConnection[] conns = process.getAllConnections(null);
        for (int i = 0; i < conns.length; i++) {
            if (conns[i] instanceof IPerformance) {
                ((IPerformance) conns[i]).setPerformanceData("");
            }
        }
    }

    /**
     * Getter for process.
     * 
     * @return the process
     */
    public IProcess getProcess() {
        return this.process;
    }

    /**
     * Sets the process.
     * 
     * @param process the process to set
     */
    public void setProcess(IProcess process) {
        this.process = process;
        setEnabled(process != null);
    }
}
