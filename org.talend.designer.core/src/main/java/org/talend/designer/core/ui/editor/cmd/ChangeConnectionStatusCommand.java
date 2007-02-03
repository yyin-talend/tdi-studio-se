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
package org.talend.designer.core.ui.editor.cmd;

import org.eclipse.gef.commands.Command;
import org.talend.core.model.process.EConnectionType;
import org.talend.core.model.process.INode;
import org.talend.designer.core.i18n.Messages;
import org.talend.designer.core.ui.editor.connections.Connection;
import org.talend.designer.core.ui.editor.process.Process;

/**
 * Command that will set or remove the start status on a node. <br/>
 * 
 * $Id$
 * 
 */
public class ChangeConnectionStatusCommand extends Command {

    private Connection connection;

    private Connection oldMainConnection = null;

    /**
     * Gives the node where the status will be set or removed.
     * 
     * @param newStartNode
     */
    public ChangeConnectionStatusCommand(Connection connection) {
        this.connection = connection;
        if (connection.getLineStyle().equals(EConnectionType.FLOW_MAIN)) {
            setLabel(Messages.getString("ChangeConnectionStatusCommand.setLinkRef")); //$NON-NLS-1$
        } else {
            setLabel(Messages.getString("ChangeConnectionStatusCommand.setLinkMain")); //$NON-NLS-1$
        }
    }

    public void execute() {
        if (connection.getLineStyle().equals(EConnectionType.FLOW_MAIN)) {
            connection.setLineStyle(EConnectionType.FLOW_REF);
        } else {
            INode target = connection.getTarget();
            boolean found = false;
            for (int i = 0; i < target.getIncomingConnections().size() && !found; i++) {
                Connection currentConnection = (Connection) target.getIncomingConnections().get(i);
                if (currentConnection.getLineStyle().equals(EConnectionType.FLOW_MAIN)) {
                    oldMainConnection = currentConnection;
                    oldMainConnection.setLineStyle(EConnectionType.FLOW_REF);
                }
            }
            connection.setLineStyle(EConnectionType.FLOW_MAIN);
        }
        ((Process) connection.getSource().getProcess()).checkStartNodes();
        ((Process) connection.getSource().getProcess()).checkProcess();
    }

    public void undo() {
        if (connection.getLineStyle().equals(EConnectionType.FLOW_MAIN)) {
            if (oldMainConnection != null) {
                oldMainConnection.setLineStyle(EConnectionType.FLOW_MAIN);
            }
            connection.setLineStyle(EConnectionType.FLOW_REF);
        } else {
            connection.setLineStyle(EConnectionType.FLOW_MAIN);
        }
        ((Process) connection.getSource().getProcess()).checkStartNodes();
        ((Process) connection.getSource().getProcess()).checkProcess();
    }

    public void redo() {
        this.execute();
    }
}
