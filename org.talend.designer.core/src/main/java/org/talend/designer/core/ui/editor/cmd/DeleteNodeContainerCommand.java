// ============================================================================
//
// Copyright (C) 2006-2007 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the  agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//   
// ============================================================================
package org.talend.designer.core.ui.editor.cmd;

import java.util.List;

import org.eclipse.gef.commands.Command;
import org.talend.core.model.metadata.IMetadataTable;
import org.talend.core.model.process.EConnectionType;
import org.talend.core.model.process.IConnectionCategory;
import org.talend.core.model.process.INodeConnector;
import org.talend.designer.core.i18n.Messages;
import org.talend.designer.core.ui.editor.connections.Connection;
import org.talend.designer.core.ui.editor.nodecontainer.NodeContainer;
import org.talend.designer.core.ui.editor.nodes.Node;
import org.talend.designer.core.ui.editor.process.Process;

/**
 * Delete a list of nodes in the process. <br/>
 * 
 * $Id$
 * 
 */
public class DeleteNodeContainerCommand extends Command {

    private Process process;

    private List<Node> nodeList;

    public DeleteNodeContainerCommand(Process process, List<Node> nodeList) {
        this.process = process;
        this.nodeList = nodeList;
        setLabel(Messages.getString("DeleteNodeCommand.Label")); //$NON-NLS-1$
    }

    @SuppressWarnings("unchecked")//$NON-NLS-1$
    public void execute() {
        process.setActivate(false);

        for (Node node : nodeList) {
            NodeContainer nodeContainer = node.getNodeContainer();

            this.process.removeNodeContainer(nodeContainer);
            List<Connection> inputList = (List<Connection>) node.getIncomingConnections();
            List<Connection> outputList = (List<Connection>) node.getOutgoingConnections();
            boolean builtIn = node.getConnectorFromType(EConnectionType.FLOW_MAIN).isBuiltIn()
                    | node.getConnectorFromType(EConnectionType.TABLE).isBuiltIn();
            for (Connection connection : inputList) {
                Node prevNode = (Node) connection.getSource();
                if (!nodeList.contains(prevNode)) {
                    INodeConnector nodeConnector = prevNode.getConnectorFromType(connection.getLineStyle());
                    nodeConnector.setCurLinkNbOutput(nodeConnector.getCurLinkNbOutput() - 1);
                    prevNode.removeOutput(connection);
                    process.removeUniqueConnectionName(connection.getName());
                }
            }
            for (Connection connection : outputList) {
                Node nextNode = (Node) connection.getTarget();
                if (!nodeList.contains(nextNode)) {
                    INodeConnector nodeConnector = nextNode.getConnectorFromType(connection.getLineStyle());
                    nodeConnector.setCurLinkNbInput(nodeConnector.getCurLinkNbInput() - 1);
                    nextNode.removeInput(connection);
                    if (nextNode != null) {
                        for (int i = 0; i < nextNode.getIncomingConnections().size(); i++) {
                            Connection nextNodeConnection = (Connection) nextNode.getIncomingConnections().get(i);
                            nextNodeConnection.updateName();
                        }
                    }
                }
                if (!builtIn) {
                    process.removeUniqueConnectionName(connection.getName());
                }
            }
            if (builtIn) {
                for (IMetadataTable meta : node.getMetadataList()) {
                    String metaName = meta.getTableName();
                    process.removeUniqueConnectionName(metaName);
                }
            }
        }

        process.setActivate(true);
        process.checkStartNodes();
        process.checkProcess();
    }

    @SuppressWarnings("unchecked")//$NON-NLS-1$
    public void undo() {
        process.setActivate(false);
        for (Node node : nodeList) {
            NodeContainer nodeContainer = node.getNodeContainer();
            this.process.addUniqueNodeName(node.getUniqueName());
            this.process.addNodeContainer(nodeContainer);

            List<Connection> inputList = (List<Connection>) node.getIncomingConnections();
            List<Connection> outputList = (List<Connection>) node.getOutgoingConnections();
            boolean builtIn = node.getConnectorFromType(EConnectionType.FLOW_MAIN).isBuiltIn()
                    | node.getConnectorFromType(EConnectionType.TABLE).isBuiltIn();
            for (Connection connection : inputList) {
                Node prevNode = (Node) connection.getSource();
                if (!nodeList.contains(prevNode)) {
                    prevNode.addOutput(connection);
                    INodeConnector nodeConnector = prevNode.getConnectorFromType(connection.getLineStyle());
                    nodeConnector.setCurLinkNbOutput(nodeConnector.getCurLinkNbOutput() + 1);
                    connection.reconnect();
                    if (connection.getLineStyle().hasConnectionCategory(IConnectionCategory.UNIQUE_NAME)) {
                        process.addUniqueConnectionName(connection.getName());
                    }
                }
            }
            for (Connection connection : outputList) {
                Node nextNode = (Node) connection.getTarget();
                if (!nodeList.contains(nextNode)) {
                    nextNode.addInput(connection);
                    INodeConnector nodeConnector = nextNode.getConnectorFromType(connection.getLineStyle());
                    nodeConnector.setCurLinkNbInput(nodeConnector.getCurLinkNbInput() + 1);
                    connection.reconnect();
                }
                if (!builtIn) {
                    if (connection.getLineStyle().hasConnectionCategory(IConnectionCategory.UNIQUE_NAME)) {
                        process.addUniqueConnectionName(connection.getName());
                    }
                }
            }
            if (builtIn) {
                for (IMetadataTable meta : node.getMetadataList()) {
                    String metaName = meta.getTableName();
                    process.addUniqueConnectionName(metaName);
                }
            }
        }

        process.setActivate(true);
        process.checkStartNodes();
        process.checkProcess();
    }

    public void redo() {
        this.execute();
    }
}
