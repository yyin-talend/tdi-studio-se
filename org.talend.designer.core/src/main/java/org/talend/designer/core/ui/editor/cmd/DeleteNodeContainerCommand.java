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

import java.util.List;

import org.eclipse.gef.commands.Command;
import org.talend.core.model.metadata.IMetadataTable;
import org.talend.core.model.process.EConnectionType;
import org.talend.core.model.process.INodeConnector;
import org.talend.designer.core.i18n.Messages;
import org.talend.designer.core.ui.editor.connections.Connection;
import org.talend.designer.core.ui.editor.nodecontainer.NodeContainer;
import org.talend.designer.core.ui.editor.nodes.Node;
import org.talend.designer.core.ui.editor.process.Process;

/**
 * Delete a given node on the diagram. <br/>
 * 
 * $Id$
 * 
 */
public class DeleteNodeContainerCommand extends Command {

    private Process process;

    private NodeContainer nodeContainer;

    List<Connection> inputList;

    List<Connection> outputList;

    /**
     * Delete the node on the diagram.
     * 
     * @param diagram
     * @param node
     */
    @SuppressWarnings("unchecked")
    public DeleteNodeContainerCommand(Process process, NodeContainer nodeContainer) {
        this.process = process;
        this.nodeContainer = nodeContainer;
        setLabel(Messages.getString("DeleteNodeCommand.0")); //$NON-NLS-1$
        Node node = nodeContainer.getNode();
        inputList = (List<Connection>) node.getIncomingConnections();
        outputList = (List<Connection>) node.getOutgoingConnections();
    }

    public void execute() {
        this.process.removeNodeContainer(nodeContainer);
        boolean builtIn = nodeContainer.getNode().getConnectorFromType(EConnectionType.FLOW_MAIN).isBuiltIn();
        for (Connection connection : inputList) {
            Node prevNode = (Node) connection.getSource();
            INodeConnector nodeConnector = prevNode.getConnectorFromType(connection.getLineStyle());
            nodeConnector.setCurLinkNbOutput(nodeConnector.getCurLinkNbOutput() - 1);
            connection.getSource().removeOutput(connection);
            connection.getSource().getProcess().removeUniqueConnectionName(connection.getName());
        }
        for (Connection connection : outputList) {
            Node nextNode = (Node) connection.getTarget();
            INodeConnector nodeConnector = nextNode.getConnectorFromType(connection.getLineStyle());
            nodeConnector.setCurLinkNbInput(nodeConnector.getCurLinkNbInput() - 1);
            connection.getTarget().removeInput(connection);
            if (!builtIn) {
                connection.getSource().getProcess().removeUniqueConnectionName(connection.getName());
            }
        }
        if (builtIn) {
            for (IMetadataTable meta : nodeContainer.getNode().getMetadataList()) {
                String metaName = meta.getTableName();
                process.removeUniqueConnectionName(metaName);
            }
        }
        process.checkProcess();
    }

    public void undo() {
        this.process.addUniqueNodeName(nodeContainer.getNode().getUniqueName());
        this.process.addNodeContainer(nodeContainer);
        boolean builtIn = nodeContainer.getNode().getConnectorFromType(EConnectionType.FLOW_MAIN).isBuiltIn();
        for (Connection connection : inputList) {
            Node prevNode = (Node) connection.getSource();
            prevNode.addOutput(connection);
            INodeConnector nodeConnector = prevNode.getConnectorFromType(connection.getLineStyle());
            nodeConnector.setCurLinkNbOutput(nodeConnector.getCurLinkNbOutput() + 1);
            connection.reconnect();
            if (connection.getLineStyle().equals(EConnectionType.FLOW_MAIN)
                    || connection.getLineStyle().equals(EConnectionType.FLOW_REF)) {
                connection.getSource().getProcess().addUniqueConnectionName(connection.getName());
            }
        }
        for (Connection connection : outputList) {
            Node nextNode = (Node) connection.getTarget();
            nextNode.addInput(connection);
            INodeConnector nodeConnector = nextNode.getConnectorFromType(connection.getLineStyle());
            nodeConnector.setCurLinkNbInput(nodeConnector.getCurLinkNbInput() + 1);
            connection.reconnect();
            if (!builtIn) {
                connection.getSource().getProcess().addUniqueConnectionName(connection.getName());
            }
        }
        if (builtIn) {
            for (IMetadataTable meta : nodeContainer.getNode().getMetadataList()) {
                String metaName = meta.getTableName();
                process.addUniqueConnectionName(metaName);
            }
        }
        process.checkProcess();
    }

    public void redo() {
        this.execute();
    }
}
