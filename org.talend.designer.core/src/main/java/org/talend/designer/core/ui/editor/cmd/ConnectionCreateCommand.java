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
package org.talend.designer.core.ui.editor.cmd;

import java.util.List;

import org.eclipse.gef.commands.Command;
import org.talend.core.model.metadata.IMetadataTable;
import org.talend.core.model.process.EConnectionType;
import org.talend.core.model.process.INodeConnector;
import org.talend.designer.core.i18n.Messages;
import org.talend.designer.core.model.process.ConnectionManager;
import org.talend.designer.core.ui.editor.connections.Connection;
import org.talend.designer.core.ui.editor.nodes.Node;
import org.talend.designer.core.ui.editor.process.Process;

/**
 * Command that creates a new connection. <br/>
 * 
 * $Id$
 * 
 */
public class ConnectionCreateCommand extends Command {

    protected Connection connection;

    private String connectorName;

    private EConnectionType newLineStyle;

    protected Node source;

    protected Node target = null;

    private String metaName;

    private String connectionName;

    private IMetadataTable newMetadata;

    private static boolean creatingConnection = false;

    /**
     * Initialisation of the creation of the connection with a source and style of connection.
     * 
     * @param nodeSource source of the connection
     * @param lineStyle line style
     * @param meta
     */
    public ConnectionCreateCommand(Node nodeSource, String connectorName, List<Object> listArgs) {
        setLabel(Messages.getString("ConnectionCreateCommand.Label")); //$NON-NLS-1$
        this.source = nodeSource;
        this.connectorName = connectorName;
        this.metaName = (String) listArgs.get(0);
        this.connectionName = (String) listArgs.get(1);
        newMetadata = (IMetadataTable) listArgs.get(2);
    }

    /**
     * Set the target of the connection, the source has already been set before.
     * 
     * @param target
     */
    public void setTarget(Node targetNode) {
        this.target = targetNode;
    }

    public boolean canExecute() {
        if (target != null) {
            if (!ConnectionManager.canConnectToTarget(source, null, target, source.getConnectorFromName(connectorName)
                    .getDefaultConnectionType(), connectorName, connectionName)) {
                creatingConnection = false;
                return false;
            }
            newLineStyle = ConnectionManager.getNewConnectionType();
        }
        creatingConnection = true;
        return true;
    }

    public void execute() {
        if (newMetadata != null) {
            source.getMetadataList().add(newMetadata);
            this.connection = new Connection(source, target, newLineStyle, connectorName, metaName, connectionName);
        } else {
            this.connection = new Connection(source, target, newLineStyle, connectorName, metaName, connectionName, metaName);
        }
        INodeConnector nodeConnectorSource, nodeConnectorTarget;
        nodeConnectorSource = connection.getSourceNodeConnector();
        nodeConnectorSource.setCurLinkNbOutput(nodeConnectorSource.getCurLinkNbOutput() + 1);
        nodeConnectorTarget = connection.getTargetNodeConnector();
        nodeConnectorTarget.setCurLinkNbInput(nodeConnectorTarget.getCurLinkNbInput() + 1);

        creatingConnection = false;
        ((Process) source.getProcess()).checkStartNodes();
        source.checkAndRefreshNode();
        target.checkAndRefreshNode();
        // ((Process) source.getProcess()).checkProcess();
    }

    public void undo() {
        connection.disconnect();
        INodeConnector nodeConnectorSource, nodeConnectorTarget;
        nodeConnectorSource = connection.getSourceNodeConnector();
        nodeConnectorSource.setCurLinkNbOutput(nodeConnectorSource.getCurLinkNbOutput() - 1);
        nodeConnectorTarget = connection.getTargetNodeConnector();
        nodeConnectorTarget.setCurLinkNbInput(nodeConnectorTarget.getCurLinkNbInput() - 1);
        if (newMetadata != null) {
            source.getMetadataList().remove(newMetadata);
        }
        ((Process) source.getProcess()).checkStartNodes();
        source.checkAndRefreshNode();
        target.checkAndRefreshNode();
        // ((Process) source.getProcess()).checkProcess();
    }

    public void redo() {
        execute();
    }

    /**
     * Getter for creatingConnection.
     * 
     * @return the creatingConnection
     */
    public static boolean isCreatingConnection() {
        return creatingConnection;
    }

    /**
     * Sets the creatingConnection.
     * 
     * @param creatingConnection the creatingConnection to set
     */
    public static void setCreatingConnection(boolean creatingConnection) {
        ConnectionCreateCommand.creatingConnection = creatingConnection;
    }
}
