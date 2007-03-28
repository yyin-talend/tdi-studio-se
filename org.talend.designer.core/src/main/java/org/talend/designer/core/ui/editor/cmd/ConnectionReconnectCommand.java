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

import java.util.ArrayList;
import java.util.Iterator;

import org.eclipse.gef.commands.Command;
import org.talend.core.model.metadata.IMetadataColumn;
import org.talend.core.model.metadata.IMetadataTable;
import org.talend.core.model.process.EConnectionCategory;
import org.talend.core.model.process.EConnectionType;
import org.talend.core.model.process.IConnection;
import org.talend.core.model.process.INodeConnector;
import org.talend.designer.core.i18n.Messages;
import org.talend.designer.core.model.components.EParameterName;
import org.talend.designer.core.model.components.EmfComponent;
import org.talend.designer.core.ui.editor.connections.Connection;
import org.talend.designer.core.ui.editor.nodes.Node;
import org.talend.designer.core.ui.editor.process.Process;

/**
 * Command that allows to change a connection to a new source or a new target. <br/>
 * 
 * $Id$
 * 
 */
public class ConnectionReconnectCommand extends Command {

    private Connection connection;

    private Node newSource;

    private Node newTarget;

    private Node oldSource;

    private Node oldTarget;

    private EConnectionType oldConnectionType;

    private EConnectionType newConnectionType;

    private IMetadataTable oldMetadataTable;

    private String oldSourceSchemaType, newSourceSchemaType;

    private String newTargetSchemaType;

    /**
     * Initialisation of the command with the given connection. This will initialize the source and target before change
     * them.
     * 
     * @param connection
     */
    public ConnectionReconnectCommand(Connection connection) {
        this.connection = connection;
        this.oldSource = connection.getSource();
        this.oldTarget = connection.getTarget();
        oldConnectionType = connection.getLineStyle();
        newConnectionType = oldConnectionType;
        if (oldConnectionType.getCategory().equals(EConnectionCategory.MAIN)) {
            oldMetadataTable = connection.getMetadataTable().clone();
        }
        oldSourceSchemaType = (String) oldSource.getPropertyValue(EParameterName.SCHEMA_TYPE.getName());
    }

    /**
     * Set a new source for the connection.
     * 
     * @param nodeSource
     */
    public void setNewSource(Node nodeSource) {
        setLabel(Messages.getString("ConnectionReconnectCommand.LabelSource")); //$NON-NLS-1$
        newSource = nodeSource;
        newTarget = null;
    }

    /**
     * Set a new target for the connection.
     * 
     * @param connectionTarget
     */
    public void setNewTarget(Node nodeTarget) {
        setLabel(Messages.getString("ConnectionReconnectCommand.LabelTarget")); //$NON-NLS-1$
        newSource = null;
        newTarget = nodeTarget;
    }

    public boolean canExecute() {
        if (newSource != null) {
            return checkSourceReconnection();
        } else if (newTarget != null) {
            return checkTargetReconnection();
        }
        return false;
    }

    /**
     * Check if a new connection on the selected source is allowed.
     * 
     * @return true / false
     */
    private boolean checkSourceReconnection() {
        // connection endpoints must be different Shapes
        if (newSource.equals(oldTarget)) {
            return false;
        }
        // return false, if the connection exists already
        for (Iterator iter = newSource.getOutgoingConnections().iterator(); iter.hasNext();) {
            Connection conn = (Connection) iter.next();
            if (conn.getTarget().equals(oldTarget) && !conn.equals(connection)) {
                return false;
            }
        }
        for (Iterator iter = newSource.getIncomingConnections().iterator(); iter.hasNext();) {
            Connection conn = (Connection) iter.next();
            if (conn.getSource().equals(oldTarget) && !conn.equals(connection)) {
                return false;
            }
        }

        INodeConnector nodeConnectorSource;
        nodeConnectorSource = newSource.getConnectorFromType(oldConnectionType);
        if (nodeConnectorSource.getMaxLinkOutput() != -1) {
            if (nodeConnectorSource.getCurLinkNbOutput() >= nodeConnectorSource.getMaxLinkOutput()) {
                return false;
            }
        }

        if (connection.getLineStyle().equals(EConnectionType.RUN_AFTER)
                || connection.getLineStyle().equals(EConnectionType.RUN_BEFORE)) {
            if (!(Boolean) newSource.getPropertyValue(EParameterName.STARTABLE.getName())
                    || (!newSource.isSubProcessStart())) {
                return false;
            }
        }

        return true;
    }

    /**
     * Check if a new connection on the selected target is allowed.
     * 
     * @return true / false
     */
    private boolean checkTargetReconnection() {
        if (newTarget.equals(oldSource)) {
            return false;
        }
        if (!newTarget.isActivate()) {
            return false;
        }
        // return false, if the connection exists already
        for (Iterator iter = newTarget.getIncomingConnections().iterator(); iter.hasNext();) {
            Connection conn = (Connection) iter.next();
            if (conn.getSource().equals(oldSource) && !conn.equals(connection)) {
                return false;
            }
        }
        for (Iterator iter = newTarget.getOutgoingConnections().iterator(); iter.hasNext();) {
            Connection conn = (Connection) iter.next();
            if (conn.getTarget().equals(oldSource) && !conn.equals(connection)) {
                return false;
            }
        }

        INodeConnector nodeConnectorTarget;
        newConnectionType = oldConnectionType;
        nodeConnectorTarget = newTarget.getConnectorFromType(newConnectionType);
        if (nodeConnectorTarget.getMaxLinkInput() != -1) {
            if (nodeConnectorTarget.getCurLinkNbInput() >= nodeConnectorTarget.getMaxLinkInput()) {
                return false;
            }
        }

        if ((!newConnectionType.getCategory().equals(EConnectionCategory.MAIN))
                && (!newConnectionType.equals(EConnectionType.ITERATE))) {
            if (!(Boolean) newTarget.getPropertyValue(EParameterName.STARTABLE.getName())) {
                return false;
            }
            if (!newTarget.isSubProcessStart()) {
                return false;
            }
        }

        int nbMain = 0;
        for (IConnection connec : newTarget.getIncomingConnections()) {
            if (connec.getLineStyle().equals(EConnectionType.FLOW_MAIN)) {
                nbMain++;
            }
        }
        if (nbMain > 0) {
            if (oldConnectionType.equals(EConnectionType.FLOW_MAIN)) {
                newConnectionType = EConnectionType.FLOW_REF;
            }
        } else {
            if (oldConnectionType.equals(EConnectionType.FLOW_REF)) {
                newConnectionType = EConnectionType.FLOW_MAIN;
            }
        }
        if (newConnectionType.equals(EConnectionType.FLOW_REF)) {
            Node sourceStart = oldSource.getSubProcessStartNode(false);
            Node targetStart = newTarget.getSubProcessStartNode(false);
            if (sourceStart.equals(targetStart)) {
                return false;
            }
        }

        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.gef.commands.Command#execute()
     */
    public void execute() {
        if (newSource != null) {
            INodeConnector connector = oldSource.getConnectorFromType(oldConnectionType);
            connector.setCurLinkNbOutput(connector.getCurLinkNbOutput() - 1);
            connector = newSource.getConnectorFromType(oldConnectionType);
            connector.setCurLinkNbOutput(connector.getCurLinkNbOutput() + 1);
            if (connection.getLineStyle().getCategory().equals(EConnectionCategory.MAIN)) {
                newSourceSchemaType = (String) newSource.getPropertyValue(EParameterName.SCHEMA_TYPE.getName());
                boolean builtInNewSource = newSource.getConnectorFromType(connection.getLineStyle()).isBuiltIn();
                boolean builtInOldSource = oldSource.getConnectorFromType(connection.getLineStyle()).isBuiltIn();
                if ((!builtInNewSource) && (!builtInOldSource)) {
                    oldSource.getMetadataList().get(0).setDescription(null);
                    oldSource.getMetadataList().get(0).setListColumns(new ArrayList<IMetadataColumn>());
                    newSource.getMetadataList().get(0).setDescription(oldMetadataTable.getDescription());
                    newSource.getMetadataList().get(0).setListColumns(oldMetadataTable.getListColumns());
                    connection.setMetaName(newSource.getUniqueName());
                } else {
                    if (!builtInNewSource) {
                        int num = 0;
                        for (int i = 0; i < oldSource.getMetadataList().size(); i++) {
                            IMetadataTable meta = oldSource.getMetadataList().get(i);
                            if (meta.getTableName().equals(oldMetadataTable.getTableName())) {
                                num = i;
                            }
                        }
                        oldSource.getMetadataList().remove(num);
                        newSource.getMetadataList().get(0).setDescription(oldMetadataTable.getDescription());
                        newSource.getMetadataList().get(0).setListColumns(oldMetadataTable.getListColumns());
                        connection.setMetaName(newSource.getUniqueName());
                    }
                    if (!builtInOldSource) {
                        oldSource.getMetadataList().get(0).setDescription(null);
                        oldSource.getMetadataList().get(0).setListColumns(new ArrayList<IMetadataColumn>());
                        IMetadataTable meta = oldMetadataTable.clone();
                        meta.setTableName(connection.getUniqueName());
                        newSource.getMetadataList().add(meta);
                        connection.setMetaName(meta.getTableName());
                    }
                    if ((builtInOldSource) && (builtInNewSource)) {
                        int num = 0;
                        for (int i = 0; i < oldSource.getMetadataList().size(); i++) {
                            IMetadataTable meta = oldSource.getMetadataList().get(i);
                            if (meta.getTableName().equals(oldMetadataTable.getTableName())) {
                                num = i;
                            }
                        }
                        oldSource.getMetadataList().remove(num);
                        newSource.getMetadataList().add(oldMetadataTable);
                    }
                }
                if (newSourceSchemaType != null) {
                    newSource.setPropertyValue(EParameterName.SCHEMA_TYPE.getName(), EmfComponent.BUILTIN);
                }
                if (oldSourceSchemaType != null) {
                    oldSource.setPropertyValue(EParameterName.SCHEMA_TYPE.getName(), EmfComponent.BUILTIN);
                }
            } else {
                connection.setMetaName(newSource.getUniqueName());
            }
            connection.reconnect(newSource, oldTarget);
            ((Process) newSource.getProcess()).checkStartNodes();
            ((Process) newSource.getProcess()).checkProcess();
        } else if (newTarget != null) {
            newTargetSchemaType = (String) newTarget.getPropertyValue(EParameterName.SCHEMA_TYPE.getName());
            connection.setLineStyle(newConnectionType);
            INodeConnector connector = oldTarget.getConnectorFromType(oldConnectionType);
            connector.setCurLinkNbInput(connector.getCurLinkNbInput() - 1);
            connector = newTarget.getConnectorFromType(newConnectionType);
            connector.setCurLinkNbInput(connector.getCurLinkNbInput() + 1);
            connection.reconnect(oldSource, newTarget);
            if (newTargetSchemaType != null) {
                newTarget.setPropertyValue(EParameterName.SCHEMA_TYPE.getName(), EmfComponent.BUILTIN);
            }
            ((Process) oldSource.getProcess()).checkStartNodes();
            ((Process) oldSource.getProcess()).checkProcess();
        } else {
            throw new IllegalStateException("Should not happen"); //$NON-NLS-1$
        }
    }

    public void undo() {
        /*
         * if ((oldSource.getExternalNode() != null) && (oldSource.getExternalNode() != null)) {
         * oldSource.getProcess().removeUniqueConnectionName(oldMetadataTable.getTableName()); }
         */
        connection.setLineStyle(oldConnectionType);
        if (newSource != null) {
            INodeConnector connector = oldSource.getConnectorFromType(oldConnectionType);
            connector.setCurLinkNbOutput(connector.getCurLinkNbOutput() + 1);
            connector = newSource.getConnectorFromType(oldConnectionType);
            connector.setCurLinkNbOutput(connector.getCurLinkNbOutput() - 1);
            if (connection.getLineStyle().getCategory().equals(EConnectionCategory.MAIN)) {
                boolean builtInNewSource = newSource.getConnectorFromType(connection.getLineStyle()).isBuiltIn();
                boolean builtInOldSource = oldSource.getConnectorFromType(connection.getLineStyle()).isBuiltIn();
                if ((!builtInNewSource) && (!builtInOldSource)) {
                    newSource.getMetadataList().get(0).setDescription(null);
                    newSource.getMetadataList().get(0).setListColumns(new ArrayList<IMetadataColumn>());
                    oldSource.getMetadataList().get(0).setDescription(oldMetadataTable.getDescription());
                    oldSource.getMetadataList().get(0).setListColumns(oldMetadataTable.getListColumns());
                    connection.setMetaName(oldSource.getUniqueName());
                } else {
                    if (!builtInNewSource) {
                        newSource.getMetadataList().get(0).setDescription(null);
                        newSource.getMetadataList().get(0).setListColumns(new ArrayList<IMetadataColumn>());
                        oldSource.getMetadataList().add(oldMetadataTable);
                        connection.setMetaName(oldMetadataTable.getTableName());
                    }
                    if (!builtInOldSource) {
                        int num = 0;
                        for (int i = 0; i < newSource.getMetadataList().size(); i++) {
                            IMetadataTable meta = newSource.getMetadataList().get(i);
                            if (meta.getTableName().equals(oldMetadataTable.getTableName())) {
                                num = i;
                            }
                        }
                        newSource.getMetadataList().remove(num);
                        oldSource.getMetadataList().get(0).setDescription(oldMetadataTable.getDescription());
                        oldSource.getMetadataList().get(0).setListColumns(oldMetadataTable.getListColumns());
                        connection.setMetaName(oldSource.getUniqueName());
                    }
                    if ((builtInOldSource) && (builtInNewSource)) {
                        int num = 0;
                        for (int i = 0; i < newSource.getMetadataList().size(); i++) {
                            IMetadataTable meta = newSource.getMetadataList().get(i);
                            if (meta.getTableName().equals(oldMetadataTable.getTableName())) {
                                num = i;
                            }
                        }
                        newSource.getMetadataList().remove(num);
                        oldSource.getMetadataList().add(oldMetadataTable);
                    }
                }
                if (newSourceSchemaType != null) {
                    newSource.setPropertyValue(EParameterName.SCHEMA_TYPE.getName(), newSourceSchemaType);
                }
                if (oldSourceSchemaType != null) {
                    oldSource.setPropertyValue(EParameterName.SCHEMA_TYPE.getName(), oldSourceSchemaType);
                }
            } else {
                connection.setMetaName(oldSource.getUniqueName());
            }
        } else if (newTarget != null) {
            INodeConnector connector = oldTarget.getConnectorFromType(oldConnectionType);
            connector.setCurLinkNbInput(connector.getCurLinkNbInput() + 1);
            connector = newTarget.getConnectorFromType(newConnectionType);
            connector.setCurLinkNbInput(connector.getCurLinkNbInput() - 1);
            if (newTargetSchemaType != null) {
                newTarget.setPropertyValue(EParameterName.SCHEMA_TYPE.getName(), newTargetSchemaType);
            }
        }
        connection.reconnect(oldSource, oldTarget);
        ((Process) oldSource.getProcess()).checkStartNodes();
        ((Process) oldSource.getProcess()).checkProcess();
    }

}
