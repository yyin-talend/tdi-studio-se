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
import org.talend.core.model.process.INodeConnector;
import org.talend.designer.core.i18n.Messages;
import org.talend.designer.core.ui.editor.connections.Connection;
import org.talend.designer.core.ui.editor.nodes.Node;
import org.talend.designer.core.ui.editor.process.Process;

/**
 * Command that will delete a given connection. <br/>
 * 
 * $Id$
 * 
 */
public class ConnectionDeleteCommand extends Command {

    private Connection connection;

    /**
     * Initialisation of the command that will delete the given connection.
     * 
     * @param connection connection to delete
     */
    public ConnectionDeleteCommand(Connection connection) {
        setLabel(Messages.getString("ConnectionDeleteCommand.0")); //$NON-NLS-1$
        this.connection = connection;
    }

    public void execute() {
        connection.disconnect();
        Node prevNode = connection.getSource();
        INodeConnector nodeConnectorSource, nodeConnectorTarget;
        nodeConnectorSource = prevNode.getConnectorFromType(connection.getLineStyle());
        nodeConnectorSource.setCurLinkNbOutput(nodeConnectorSource.getCurLinkNbOutput() - 1);

        Node nextNode = connection.getTarget();
        nodeConnectorTarget = nextNode.getConnectorFromType(connection.getLineStyle());
        nodeConnectorTarget.setCurLinkNbInput(nodeConnectorTarget.getCurLinkNbInput() - 1);

        ((Process) prevNode.getProcess()).checkProcess();
    }

    public void undo() {
        connection.reconnect();
        Node prevNode = (Node) connection.getSource();
        INodeConnector nodeConnectorSource, nodeConnectorTarget;
        nodeConnectorSource = prevNode.getConnectorFromType(connection.getLineStyle());
        nodeConnectorSource.setCurLinkNbOutput(nodeConnectorSource.getCurLinkNbOutput() + 1);

        Node nextNode = (Node) connection.getTarget();
        nodeConnectorTarget = nextNode.getConnectorFromType(connection.getLineStyle());
        nodeConnectorTarget.setCurLinkNbInput(nodeConnectorTarget.getCurLinkNbInput() + 1);

        ((Process) prevNode.getProcess()).checkProcess();
    }
}
