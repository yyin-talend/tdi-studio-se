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
            // if (source.equals(target)) {
            // return false;
            // }
            //
            // if (source.sameProcessAs(target, false)) {
            // return false;
            // }
            //
            // // Check existing connections to avoid to have more than one link
            // // no matter the type of the connection and the direction
            // List connections = this.source.getOutgoingConnections();
            // for (int i = 0; i < connections.size(); i++) {
            // if (((Connection) connections.get(i)).getTarget().equals(target)) {
            // return false;
            // }
            // }
            // connections = this.source.getIncomingConnections();
            // for (int i = 0; i < connections.size(); i++) {
            // if (((Connection) connections.get(i)).getSource().equals(target)) {
            // return false;
            // }
            // }
            //
            // if (!target.isActivate()) {
            // return false;
            // }
            // INodeConnector nodeConnectorTarget;
            // nodeConnectorTarget = target.getConnectorFromType(lineStyle);
            // if (nodeConnectorTarget.getMaxLinkInput() != -1) {
            // if (nodeConnectorTarget.getCurLinkNbInput() >= nodeConnectorTarget.getMaxLinkInput()) {
            // return false;
            // }
            // }
            //
            // if ((!lineStyle.equals(EConnectionType.FLOW_MAIN)) && (!lineStyle.equals(EConnectionType.FLOW_REF))
            // && (!lineStyle.equals(EConnectionType.ITERATE) && (!lineStyle.equals(EConnectionType.TABLE)))) {
            // if (!(Boolean) target.getPropertyValue(EParameterName.STARTABLE.getName())) {
            // return false;
            // }
            // if (!target.isSubProcessStart()) {
            // return false;
            // }
            // }
            //
            // if (lineStyle.equals(EConnectionType.FLOW_MAIN)) {
            // // if the component don't use merge links, then use ref links for additionals
            // if (!target.getComponent().useMerge()) {
            // for (IConnection connec : target.getIncomingConnections()) {
            // if (connec.getLineStyle().equals(EConnectionType.FLOW_MAIN)) {
            // lineStyle = EConnectionType.FLOW_REF;
            // }
            // }
            // } else {
            // lineStyle = EConnectionType.FLOW_MERGE;
            // }
            // }
            //
            // connections = this.target.getIncomingConnections();
            // for (int i = 0; i < connections.size(); i++) {
            // if (lineStyle.equals(EConnectionType.FLOW_MAIN) || lineStyle.equals(EConnectionType.FLOW_REF)
            // || lineStyle.equals(EConnectionType.TABLE)) {
            // if (((Connection) connections.get(i)).getName().equals(connectionName)) {
            // return false;
            // }
            // }
            // }
            // boolean targetHasRefLinks = ((Process) target.getProcess()).isThereRefLink(target)
            // | lineStyle.equals(EConnectionType.FLOW_REF);
            // if (lineStyle.equals(EConnectionType.RUN_IF) || lineStyle.equals(EConnectionType.RUN_IF_ERROR)
            // || lineStyle.equals(EConnectionType.RUN_IF_OK)) {
            // if (targetHasRefLinks) {
            // return false;
            // }
            // }
            // if (targetHasRefLinks && source.hasRunIfLink()) {
            // return false;
            // }
            if (!ConnectionManager.canConnectToTarget(source, null, target, source.getConnectorFromName(connectorName)
                    .getDefaultConnectionType(), connectorName, connectionName)) {
                return false;
            }
            newLineStyle = ConnectionManager.getNewConnectionType();
        }

        // List connections = this.source.getOutgoingConnections();
        // for (int i = 0; i < connections.size(); i++) {
        // if (((Connection) connections.get(i)).getName().equals(connectionName)) {
        // return false;
        // }
        // }
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
}
