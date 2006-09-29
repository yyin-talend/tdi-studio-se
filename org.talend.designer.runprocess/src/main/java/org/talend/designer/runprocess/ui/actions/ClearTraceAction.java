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
package org.talend.designer.runprocess.ui.actions;

import java.util.Iterator;

import org.eclipse.jface.action.Action;
import org.talend.core.model.process.IConnection;
import org.talend.core.model.process.INode;
import org.talend.core.model.process.IProcess;

/**
 * Clean trace data on a process. <br/>
 * 
 * $Id$
 * 
 */
public class ClearTraceAction extends Action {

    /** The process to be cleared. */
    private IProcess process;

    /**
     * Constructs a new ClearTraceAction.
     */
    public ClearTraceAction() {
        super("Clean trace tracking");

        setEnabled(false);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.action.Action#run()
     */
    @Override
    public void run() {
        IConnection connection = null;
        for (Iterator<? extends INode> i = process.getGraphicalNodes().iterator(); connection == null
                && i.hasNext();) {
            INode psNode = i.next();
            for (IConnection connec : psNode.getOutgoingConnections()) {
                connec.setTraceData(null);
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
