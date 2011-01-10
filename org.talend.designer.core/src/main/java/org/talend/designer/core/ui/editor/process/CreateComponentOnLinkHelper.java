// ============================================================================
//
// Copyright (C) 2006-2007 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.designer.core.ui.editor.process;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.talend.core.model.metadata.IMetadataTable;
import org.talend.core.model.metadata.MetadataTable;
import org.talend.core.model.process.EConnectionType;
import org.talend.core.model.process.IConnectionCategory;
import org.talend.core.model.process.INode;
import org.talend.core.model.process.INodeConnector;
import org.talend.designer.core.model.process.ConnectionManager;
import org.talend.designer.core.ui.dialog.LinkSelectDialog;
import org.talend.designer.core.ui.editor.connections.Connection;
import org.talend.designer.core.ui.editor.connections.ConnectionPart;
import org.talend.designer.core.ui.editor.nodecontainer.NodeContainerPart;
import org.talend.designer.core.ui.editor.nodes.Node;
import org.talend.designer.core.ui.editor.nodes.NodePart;
import org.talend.designer.core.ui.editor.subjobcontainer.SubjobContainerPart;

/**
 * wzhang class global comment. Detailled comment
 */
public class CreateComponentOnLinkHelper {

    public static List<ConnectionPart> getConnectionPart(SubjobContainerPart containerPart, Point point) {
        List<ConnectionPart> connectionList = new ArrayList<ConnectionPart>();
        List<ConnectionPart> connList = new ArrayList<ConnectionPart>();
        List children = containerPart.getChildren();
        for (int i = 0; i < children.size(); i++) {
            Object object = children.get(i);
            if (object instanceof NodeContainerPart) {
                NodeContainerPart nodePart = (NodeContainerPart) object;
                Object o = nodePart.getChildren().get(0);
                if (o instanceof NodePart) {
                    List connections = ((NodePart) o).getSourceConnections();
                    for (int j = 0; j < connections.size(); j++) {
                        Object connection = connections.get(j);
                        if (connection instanceof ConnectionPart) {
                            ConnectionPart connPart = (ConnectionPart) connection;
                            Connection conn = (Connection) connPart.getModel();
                            if (isFlowConnection(conn)) {
                                Rectangle bounds = ((ConnectionPart) connection).getFigure().getBounds();
                                if (bounds.contains(point)) {
                                    connectionList.add(connPart);
                                }
                                if (containsPoint(bounds, point)) {
                                    connList.add(connPart);
                                }
                            }
                        }
                    }
                }
            }
        }
        if (!connectionList.isEmpty()) {
            return connectionList;
        } else {
            return connList;
        }
    }

    /**
     * 
     * comment method "containsPoint". this function is different with Rectangle api to avoid problem that can't get
     * link even it's clearly the target in talend studio.
     * 
     * @return
     */
    public static boolean containsPoint(Rectangle rec, Point p) {
        if (rec != null && p != null) {
            return p.y >= rec.y && p.y < rec.y + rec.height * 2 && p.x >= rec.x && p.x < rec.x + rec.width * 2;
        }
        return false;
    }

    public static boolean isFlowConnection(Connection connection) {
        if (connection != null) {
            INode source = connection.getSource();
            INodeConnector connctor = connection.getSourceNodeConnector();
            EConnectionType connectionType = source.getConnectorFromName(connctor.getName()).getDefaultConnectionType();
            return connectionType.hasConnectionCategory(EConnectionType.FLOW);
        }
        return false;
    }

    public static boolean canCreateNodeOnLink(org.talend.designer.core.ui.editor.connections.Connection connection, Node node) {
        if (connection != null && node != null) {
            INode source = connection.getSource();
            INodeConnector sourceConnector = connection.getSourceNodeConnector();
            INode target = connection.getTarget();
            INodeConnector targetConnector = connection.getTargetNodeConnector();

            // only consider Flow connection
            EConnectionType connectionType = source.getConnectorFromName(sourceConnector.getName()).getDefaultConnectionType();
            if (connectionType.hasConnectionCategory(EConnectionType.FLOW)) {
                boolean b1 = ConnectionManager.canConnectToTarget(source, null, node, connectionType, sourceConnector.getName(),
                        null);
                boolean b2 = ConnectionManager.canConnectToSource(source, node, target,
                        target.getConnectorFromName(targetConnector.getName()).getDefaultConnectionType(),
                        targetConnector.getName(), null);
                return b1 && b2;
            }
        }
        return false;
    }

    public static List<Object> getTargetArgs(Connection connection, Node node) {
        List<Object> connArgs = new ArrayList<Object>();
        IMetadataTable meta = null;
        String connectName = null;
        INodeConnector connector = connection.getTargetNodeConnector();
        EConnectionType connectType = node.getConnectorFromName(connector.getName()).getDefaultConnectionType();
        if (connectType.hasConnectionCategory(EConnectionType.FLOW)) {
            // String name = "*New Output*" + " (" + connector.getMenuName() + ")";
            if (node.getConnectorFromName(connector.getName()).isMultiSchema()) {
                String tableName = connection.getMetaName();
                meta = connection.getSource().getMetadataTable(tableName);
                if (connector.isMultiSchema()) {
                    if (connection.getTarget().getConnectorFromName(node.getConnectorFromName(connector.getName()).getName())
                            .isMultiSchema()) {
                        // Connection conn = (Connection) connection.getTarget().getIncomingConnections().get(0);
                        // node.getMetadataList().add(conn.getMetadataTable());
                    } else {
                        MetadataTable table = (MetadataTable) connection.getTarget().getMetadataList().get(0).clone();
                        table.setLabel(tableName);
                        node.getMetadataList().add(table);
                    }
                }
            } else {
                if (node.getMetadataList().size() == 0) {
                    meta = null;
                } else {
                    meta = node.getMetadataFromConnector(connector.getName());
                }
                connectName = node.getProcess().generateUniqueConnectionName(Process.DEFAULT_ROW_CONNECTION_NAME);
            }

            if (connectType.equals(EConnectionType.FLOW_MAIN) || connectType.equals(EConnectionType.FLOW_REF)) {
                if (meta == null) {
                    connArgs.add(null);
                } else {
                    connArgs.add(meta.getTableName());
                }
            } else {
                connArgs.add(node.getUniqueName());
            }
            String baseName = node.getConnectionName();
            String fromConnectionName = null;
            if (node.getProcess().checkValidConnectionName(baseName)) {
                fromConnectionName = node.getProcess().generateUniqueConnectionName(baseName);
            }
            if (fromConnectionName != null && connectType.hasConnectionCategory(IConnectionCategory.FLOW)
                    && node.getProcess().checkValidConnectionName(fromConnectionName, false) && !connector.isMultiSchema()) {
                connArgs.add(fromConnectionName);
            } else {
                connArgs.add(connectName);
            }
            connArgs.add(null);
        }

        return connArgs;
    }

    public static org.talend.designer.core.ui.editor.connections.Connection getTargetConnection(List<ConnectionPart> connections) {
        org.talend.designer.core.ui.editor.connections.Connection connection = null;
        if (connections != null && !connections.isEmpty()) {
            if (connections.size() == 1) {
                org.talend.designer.core.ui.editor.connections.Connection conn = (org.talend.designer.core.ui.editor.connections.Connection) connections
                        .get(0).getModel();
                String name = conn.getName();
                boolean flag = MessageDialog.openConfirm(Display.getDefault().getActiveShell(), "Add component",
                        "Do you want to add the component on link \'" + name + "\'?");
                if (flag) {
                    connection = conn;
                }
            } else {
                boolean flag = MessageDialog.openConfirm(Display.getDefault().getActiveShell(), "Add component",
                        "Do you want to create the component on reference link?");
                if (flag) {
                    LinkSelectDialog dialog = new LinkSelectDialog(Display.getDefault().getActiveShell(), connections);
                    dialog.setInitialSelections(new Object[] { connections.get(0) });
                    if (dialog.open() == IDialogConstants.OK_ID) {
                        ConnectionPart selectLink = dialog.getSelectLink();
                        connection = (org.talend.designer.core.ui.editor.connections.Connection) selectLink.getModel();
                    }
                }
            }
        }
        return connection;
    }
}
