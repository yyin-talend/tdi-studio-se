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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.commands.Command;
import org.eclipse.ui.PlatformUI;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.core.model.metadata.IMetadataTable;
import org.talend.core.model.process.EConnectionType;
import org.talend.core.model.process.EParameterFieldType;
import org.talend.core.model.process.IExternalNode;
import org.talend.core.model.process.INodeConnector;
import org.talend.designer.core.i18n.Messages;
import org.talend.designer.core.model.components.EParameterName;
import org.talend.designer.core.model.components.ElementParameter;
import org.talend.designer.core.ui.MultiPageTalendEditor;
import org.talend.designer.core.ui.editor.TalendEditor;
import org.talend.designer.core.ui.editor.connections.Connection;
import org.talend.designer.core.ui.editor.nodecontainer.NodeContainer;
import org.talend.designer.core.ui.editor.nodes.Node;
import org.talend.designer.core.ui.editor.nodes.NodePart;
import org.talend.designer.core.ui.editor.process.Process;
import org.talend.designer.core.ui.editor.process.ProcessPart;
import org.talend.repository.model.ComponentsFactoryProvider;

/**
 * Command used to paste all the components.
 * 
 * $Id$
 * 
 */
public class NodesPasteCommand extends Command {

    private Process process;

    private List<NodeContainer> nodeContainerList;

    private List<EditPart> oldSelection;

    private List<NodePart> nodeParts;

    private List<String> createdNames;

    public NodesPasteCommand(List<NodePart> nodeParts, Process process) {
        this.process = process;
        orderNodeParts(nodeParts);
        setLabel(Messages.getString("NodesPasteCommand.label")); //$NON-NLS-1$
    }

    @SuppressWarnings("unchecked")//$NON-NLS-1$
    private String createNewConnectionName(String oldName) {
        String newName = checkExistingNames(oldName);
        newName = checkNewNames(newName);
        createdNames.add(newName);
        return newName;
    }

    private void orderNodeParts(List<NodePart> nodeParts) {
        this.nodeParts = new ArrayList<NodePart>();

        Point curLocation;

        NodePart toAdd = null;

        List<NodePart> restToOrder = new ArrayList<NodePart>();
        restToOrder.addAll(nodeParts);

        for (NodePart copiedNodePart : nodeParts) {
            curLocation = null;
            for (NodePart partToOrder : restToOrder) {
                Node copiedNode = (Node) partToOrder.getModel();
                if (curLocation == null) {
                    curLocation = copiedNode.getLocation();
                    toAdd = partToOrder;
                } else {
                    if (curLocation.y >= copiedNode.getLocation().y) {
                        if (curLocation.x >= copiedNode.getLocation().x) {
                            curLocation = copiedNode.getLocation();
                            toAdd = partToOrder;
                        }
                    }
                }
            }
            if (toAdd != null) {
                this.nodeParts.add(toAdd);
                restToOrder.remove(toAdd);
            }
        }
    }

    private String checkExistingNames(String oldName) {
        String newName = new String(oldName);

        for (Node node : (List<Node>) process.getGraphicalNodes()) {
            for (Connection connection : (List<Connection>) node.getOutgoingConnections()) {
                if (connection.getName().equals(newName)) {
                    newName = checkExistingNames("copyOf" + newName); //$NON-NLS-1$
                }
            }
        }
        return newName;
    }

    private String checkNewNames(String oldName) {
        String newName = new String(oldName);

        for (String name : createdNames) {
            if (name.equals(newName)) {
                newName = checkNewNames("copyOf" + newName); //$NON-NLS-1$
            }
        }
        return newName;
    }

    /**
     * 
     * Will return a empty location for a component from a given point.
     * 
     * @param location
     * @return
     */
    private Point findLocationForNode(final Point location) {
        Point newLocation = findLocationForNodeInProcess(location);
        newLocation = findLocationForNodeInContainerList(newLocation);
        return newLocation;
    }

    @SuppressWarnings("unchecked")//$NON-NLS-1$
    private Point findLocationForNodeInProcess(final Point location) {
        Point newLocation = new Point(location);
        for (Node node : (List<Node>) process.getGraphicalNodes()) {
            if ((node.getLocation().x == newLocation.x) && (node.getLocation().y == newLocation.y)) {
                newLocation.x += TalendEditor.GRID_SIZE;
                newLocation.y += TalendEditor.GRID_SIZE;
                findLocationForNodeInProcess(newLocation);
            }
        }
        return newLocation;
    }

    private Point findLocationForNodeInContainerList(final Point location) {
        Point newLocation = new Point(location);
        for (NodeContainer nodeContainer : nodeContainerList) {
            Node node = nodeContainer.getNode();
            if ((node.getLocation().x == newLocation.x) && (node.getLocation().y == newLocation.y)) {
                newLocation.x += TalendEditor.GRID_SIZE;
                newLocation.y += TalendEditor.GRID_SIZE;
                findLocationForNodeInContainerList(newLocation);
            }
        }
        return newLocation;
    }

    @SuppressWarnings("unchecked")//$NON-NLS-1$
    private void createNodeContainerList() {
        nodeContainerList = new ArrayList<NodeContainer>();
        createdNames = new ArrayList<String>();
        Map<String, String> oldNameTonewNameMap = new HashMap<String, String>();
        Map<String, String> oldMetaToNewMeta = new HashMap<String, String>();

        // create the nodes
        for (NodePart copiedNodePart : nodeParts) {
            Node copiedNode = (Node) copiedNodePart.getModel();
            Node pastedNode = new Node(
                    ComponentsFactoryProvider.getInstance().get(copiedNode.getComponent().getName()), process);

            Point location = copiedNode.getLocation();
            if (process.isGridEnabled()) {
                // replace the component to set it on the grid if it's enabled
                int tempVar = location.x / TalendEditor.GRID_SIZE;
                location.x = tempVar * TalendEditor.GRID_SIZE;
                tempVar = location.y / TalendEditor.GRID_SIZE;
                location.y = tempVar * TalendEditor.GRID_SIZE;
            }
            pastedNode.setLocation(findLocationForNode(location));

            if (pastedNode.getExternalNode() == null) {
                IMetadataTable metaTable = copiedNode.getMetadataList().get(0);
                IMetadataTable newMetaTable = metaTable.clone();
                newMetaTable.setTableName(pastedNode.getUniqueName());
                pastedNode.getMetadataList().clear(); // remove the old "empty" metadata
                pastedNode.getMetadataList().add(newMetaTable);
            } else {
                List<IMetadataTable> copyOfMetadataList = new ArrayList<IMetadataTable>();
                for (IMetadataTable metaTable : copiedNode.getMetadataList()) {
                    IMetadataTable newTable = metaTable.clone();
                    newTable.setTableName(createNewConnectionName(metaTable.getTableName()));
                    oldMetaToNewMeta.put(pastedNode.getUniqueName() + ":" + metaTable.getTableName(), newTable
                            .getTableName());
                    copyOfMetadataList.add(newTable);
                }
                pastedNode.setMetadataList(copyOfMetadataList);
                IExternalNode externalNode = pastedNode.getExternalNode();
                if (copiedNode.getExternalData() != null) {
                    try {
                        externalNode.setExternalData(copiedNode.getExternalData().clone());
                    } catch (CloneNotSupportedException e) {
                        ExceptionHandler.process(e);
                    }
                    pastedNode.setExternalData(externalNode.getExternalData());
                }
                for (IMetadataTable metaTable : copiedNode.getMetadataList()) {
                    String oldName = metaTable.getTableName();
                    String newName = oldMetaToNewMeta.get(pastedNode.getUniqueName() + ":" + metaTable.getTableName());
                    externalNode.renameOutputConnection(oldName, newName);
                }
            }
            pastedNode.getNodeLabel().setOffset(new Point(copiedNode.getNodeLabel().getOffset()));
            oldNameTonewNameMap.put(copiedNode.getUniqueName(), pastedNode.getUniqueName());

            for (ElementParameter param : (List<ElementParameter>) copiedNode.getElementParameters()) {
                if (!EParameterName.UNIQUE_NAME.getName().equals(param.getName())) {
                    if (param.getField() == EParameterFieldType.TABLE) {
                        List<Map<String, Object>> tableValues = (List<Map<String, Object>>) param.getValue();
                        ArrayList newValues = new ArrayList();
                        for (Map<String, Object> map : tableValues) {
                            Map<String, Object> newMap = new HashMap<String, Object>();
                            newMap.putAll(map);
                            newValues.add(newMap);
                        }
                        pastedNode.getElementParameter(param.getName()).setValue(newValues);
                    } else {
                        pastedNode.getElementParameter(param.getName()).setValue(param.getValue());
                    }
                }
            }
            nodeContainerList.add(new NodeContainer(pastedNode));
        }

        // add the connections
        for (NodePart copiedNodePart : nodeParts) {
            Node copiedNode = (Node) copiedNodePart.getModel();
            for (Connection connection : (List<Connection>) copiedNode.getOutgoingConnections()) {
                Node pastedTargetNode = null, pastedSourceNode = null;

                String nodeSource = oldNameTonewNameMap.get(copiedNode.getUniqueName());
                for (NodeContainer nodeContainer : nodeContainerList) {
                    Node node = nodeContainer.getNode();
                    if (node.getUniqueName().equals(nodeSource)) {
                        pastedSourceNode = node;
                    }
                }

                Node targetNode = connection.getTarget();
                // test if the target is in the nodes to paste to add the connection
                // if the targeted node is not in the nodes to paste, then the string will be null
                String nodeToConnect = oldNameTonewNameMap.get(targetNode.getUniqueName());
                if (nodeToConnect != null) {
                    for (NodeContainer nodeContainer : nodeContainerList) {
                        Node node = nodeContainer.getNode();
                        if (node.getUniqueName().equals(nodeToConnect)) {
                            pastedTargetNode = node;
                        }
                    }
                }
                if ((pastedSourceNode != null) && (pastedTargetNode != null)) {
                    String newConnectionName;
                    String metaTableName;

                    if (connection.getLineStyle().equals(EConnectionType.FLOW_MAIN)
                            || connection.getLineStyle().equals(EConnectionType.FLOW_REF)) {
                        newConnectionName = createNewConnectionName(connection.getName());
                    } else {
                        newConnectionName = connection.getName();
                    }

                    if (pastedSourceNode.getExternalNode() == null) {
                        metaTableName = pastedSourceNode.getMetadataList().get(0).getTableName();
                    } else {
                        metaTableName = connection.getMetaName();

                        // IMetadataTable metaTable = connection.getMetadataTable();
                        // IMetadataTable newMetaTable = metaTable.clone();
                        // newMetaTable.setTableName(metaTableName);
                        // pastedSourceNode.getMetadataList().add(newMetaTable);

                    }
                    String meta = oldMetaToNewMeta.get(pastedSourceNode.getUniqueName() + ":"
                            + connection.getMetaName());
                    if (meta != null) {
                        newConnectionName = meta;
                        metaTableName = meta;
                    }
                    Connection pastedConnection = new Connection(pastedSourceNode, pastedTargetNode, connection
                            .getLineStyle(), metaTableName, newConnectionName);
                    for (ElementParameter param : (List<ElementParameter>) connection.getElementParameters()) {
                        pastedConnection.getElementParameter(param.getName()).setValue(param.getValue());
                    }
                    pastedConnection.getConnectionLabel().setOffset(
                            new Point(connection.getConnectionLabel().getOffset()));
                    INodeConnector connector = pastedSourceNode.getConnectorFromType(pastedConnection.getLineStyle());
                    connector.setCurLinkNbOutput(connector.getCurLinkNbOutput() + 1);
                    connector = pastedTargetNode.getConnectorFromType(pastedConnection.getLineStyle());
                    connector.setCurLinkNbInput(connector.getCurLinkNbInput() + 1);
                    IExternalNode externalNode = pastedTargetNode.getExternalNode();
                    if (externalNode != null) {
                        externalNode.renameInputConnection(connection.getName(), newConnectionName);
                    }
                }
            }
        }
    }

    @SuppressWarnings("unchecked")//$NON-NLS-1$
    @Override
    public void execute() {
        // create the node container list to paste
        createNodeContainerList();

        // save old selection
        MultiPageTalendEditor multiPageTalendEditor = (MultiPageTalendEditor) PlatformUI.getWorkbench()
                .getActiveWorkbenchWindow().getActivePage().getActiveEditor();
        GraphicalViewer viewer = multiPageTalendEditor.getTalendEditor().getViewer();
        oldSelection = new ArrayList<EditPart>();
        for (EditPart editPart : (List<EditPart>) viewer.getSelectedEditParts()) {
            oldSelection.add(editPart);
        }

        // remove the old selection
        viewer.deselectAll();

        // creates the different nodes
        for (NodeContainer nodeContainer : nodeContainerList) {
            process.addNodeContainer(nodeContainer);
        }
        // set the new node as the current selection
        EditPart processPart = (EditPart) viewer.getRootEditPart().getChildren().get(0);
        if (processPart instanceof ProcessPart) { // can only be ProcessPart but still test
            for (EditPart editPart : (List<EditPart>) processPart.getChildren()) {
                if (editPart instanceof NodePart) {
                    Node currentNode = (Node) editPart.getModel();
                    if (nodeContainerList.contains(currentNode.getNodeContainer())) {
                        viewer.appendSelection(editPart);
                    }
                }
            }
        }
        process.checkStartNodes();
        process.checkProcess();
    }

    @SuppressWarnings("unchecked")//$NON-NLS-1$
    @Override
    public void undo() {
        // remove the current selection
        MultiPageTalendEditor multiPageTalendEditor = (MultiPageTalendEditor) PlatformUI.getWorkbench()
                .getActiveWorkbenchWindow().getActivePage().getActiveEditor();
        GraphicalViewer viewer = multiPageTalendEditor.getTalendEditor().getViewer();
        viewer.deselectAll();

        for (NodeContainer nodeContainer : nodeContainerList) {
            // remove the connections name from the list
            for (Connection connection : (List<Connection>) nodeContainer.getNode().getOutgoingConnections()) {
                process.removeUniqueConnectionName(connection.getName());
            }
            process.removeNodeContainer(nodeContainer);
        }

        // set the old selection active
        for (EditPart editPart : oldSelection) {
            /*
             * if (editPart instanceof NodePart) { Node node = (Node) editPart.getModel(); }
             */
            viewer.appendSelection(editPart);
        }

        process.checkStartNodes();
        process.checkProcess();
    }
}
