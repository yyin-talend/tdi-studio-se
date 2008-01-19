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
package org.talend.designer.core.ui.editor.cmd;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.commands.Command;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.ui.PlatformUI;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.core.model.components.IComponent;
import org.talend.core.model.metadata.IMetadataColumn;
import org.talend.core.model.metadata.IMetadataTable;
import org.talend.core.model.process.EConnectionType;
import org.talend.core.model.process.EParameterFieldType;
import org.talend.core.model.process.IConnectionCategory;
import org.talend.core.model.process.IExternalNode;
import org.talend.core.model.process.INodeConnector;
import org.talend.designer.core.i18n.Messages;
import org.talend.designer.core.model.components.EParameterName;
import org.talend.designer.core.model.components.ElementParameter;
import org.talend.designer.core.ui.AbstractMultiPageTalendEditor;
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

    private NodePart nodePart;

    private List<NodePart> nodeParts;

    private List<String> createdNames;

    private boolean multipleCommand;

    Point cursorLocation = null;

    /**
     * Getter for cursorLocation.
     * 
     * @return the cursorLocation
     */
    public Point getCursorLocation() {
        return this.cursorLocation;
    }

    /**
     * Sets the cursorLocation.
     * 
     * @param cursorLocation the cursorLocation to set
     */
    public void setCursorLocation(Point cursorLocation) {
        this.cursorLocation = cursorLocation;
    }

    public NodesPasteCommand(List<NodePart> nodeParts, Process process, Point cursorLocation) {
        this.process = process;
        nodePart = nodeParts.get(0);
        setCursorLocation(cursorLocation);
        orderNodeParts(nodeParts);
        setLabel(Messages.getString("NodesPasteCommand.label")); //$NON-NLS-1$

    }

    @SuppressWarnings("unchecked")//$NON-NLS-1$
    private String createNewConnectionName(String oldName) {
        final String copyOf = "copyOf"; //$NON-NLS-1$
        String newName = checkExistingNames(copyOf + oldName);
        newName = checkNewNames(newName);
        createdNames.add(newName);
        return newName;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.gef.commands.Command#canExecute()
     */
    @Override
    public boolean canExecute() {
        return !process.isReadOnly();
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

    private String checkExistingNames(final String oldName) {
        final String tmpName = oldName + "_"; //$NON-NLS-1$
        String newName = oldName;

        int index = 0;
        while (!process.checkValidConnectionName(newName, true)) {
            newName = tmpName + (index++);
        }
        return newName;
    }

    private String checkNewNames(final String oldName) {
        final String tmpName = oldName + "_"; //$NON-NLS-1$
        String newName = oldName;

        int index = 0;
        while (createdNames.contains(newName)) {
            newName = tmpName + index++;
        }
        // check the name again in process.
        while (!process.checkValidConnectionName(newName, true)) {
            newName = tmpName + (index++);
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
    private Point findLocationForNode(final Point location, final Dimension size, int index, int firstIndex,
            NodePart copiedNodePart) {
        Point newLocation = findLocationForNodeInProcess(location, size);
        newLocation = findLocationForNodeInContainerList(newLocation, size, index, firstIndex, copiedNodePart);
        return newLocation;
    }

    @SuppressWarnings("unchecked")//$NON-NLS-1$
    private Point findLocationForNodeInProcess(final Point location, Dimension size) {
        Rectangle copiedRect = new Rectangle(location, size);
        Point newLocation = new Point(location);
        for (Node node : (List<Node>) process.getGraphicalNodes()) {
            Rectangle currentRect = new Rectangle(node.getLocation(), node.getSize());
            if (currentRect.intersects(copiedRect)) {
                newLocation.x += TalendEditor.GRID_SIZE;
                newLocation.y += TalendEditor.GRID_SIZE;
                return findLocationForNodeInProcess(newLocation, size);
            }
        }
        return newLocation;
    }

    private Point findLocationForNodeInContainerList(final Point location, Dimension size, int index, int firstIndex,
            NodePart copiedNodePart) {
        Rectangle copiedRect = new Rectangle(location, size);
        Point newLocation = new Point(location);
        // for (NodeContainer nodeContainer : nodeContainerList) {
        // Node node = nodeContainer.getNode();
        // Rectangle currentRect = new Rectangle(node.getLocation(), node.getSize());
        // if (currentRect.intersects(copiedRect)) {
        // newLocation = computeTheDistance(index, firstIndex, newLocation);
        // // return findLocationForNodeInContainerList(newLocation, size, index, firstIndex);
        // }
        // }
        if (getCursorLocation() == null) {
            return newLocation;
        }
        if (!nodePart.equals(copiedNodePart)) {
            newLocation = computeTheDistance(index, firstIndex, newLocation);
        }
        return newLocation;
    }

    private Point computeTheDistance(int index, int firstIndex, Point location) {
        Point firstNodeLocation = ((Node) nodePart.getModel()).getLocation();
        Point currentNodeLocation = ((Node) nodeParts.get(index).getModel()).getLocation();

        int distanceX = firstNodeLocation.x - currentNodeLocation.x;
        int distanceY = firstNodeLocation.y - currentNodeLocation.y;
        location.x = location.x - distanceX;
        location.y = location.y - distanceY;
        return location;
    }

    @SuppressWarnings("unchecked")//$NON-NLS-1$
    private void createNodeContainerList() {
        int firstIndex = 0;
        int index = 0;
        nodeContainerList = new ArrayList<NodeContainer>();
        createdNames = new ArrayList<String>();
        Map<String, String> oldNameTonewNameMap = new HashMap<String, String>();
        Map<String, String> oldMetaToNewMeta = new HashMap<String, String>();

        // create the nodes
        for (NodePart copiedNodePart : nodeParts) {
            Node copiedNode = (Node) copiedNodePart.getModel();
            IComponent component = ComponentsFactoryProvider.getInstance().get(copiedNode.getComponent().getName());
            if (component == null) {
                component = copiedNode.getComponent();
            }
            Node pastedNode = new Node(component, process);

            Point location = null;
            if (getCursorLocation() == null) {
                location = copiedNode.getLocation();
            } else {
                location = getCursorLocation();
                index = nodeParts.indexOf(copiedNodePart);
            }

            if (process.isGridEnabled()) {
                // replace the component to set it on the grid if it's enabled
                int tempVar = location.x / TalendEditor.GRID_SIZE;
                location.x = tempVar * TalendEditor.GRID_SIZE;
                tempVar = location.y / TalendEditor.GRID_SIZE;
                location.y = tempVar * TalendEditor.GRID_SIZE;
            }
            pastedNode.setLocation(findLocationForNode(location, copiedNode.getSize(), index, firstIndex, copiedNodePart));
            pastedNode.setSize(copiedNode.getSize());

            INodeConnector mainConnector;
            if (pastedNode.isELTComponent()) {
                mainConnector = pastedNode.getConnectorFromType(EConnectionType.TABLE);
            } else {
                mainConnector = pastedNode.getConnectorFromType(EConnectionType.FLOW_MAIN);
            }

            if (!mainConnector.isBuiltIn()) {
                if (copiedNode.getMetadataList().size() != 0) {
                    pastedNode.getMetadataList().clear();
                    for (IMetadataTable metaTable : copiedNode.getMetadataList()) {
                        IMetadataTable newMetaTable = metaTable.clone();
                        if (metaTable.getTableName().equals(copiedNode.getUniqueName())) {
                            newMetaTable.setTableName(pastedNode.getUniqueName());
                        }
                        for (IMetadataColumn column : metaTable.getListColumns()) {
                            if (column.isCustom()) {
                                IMetadataColumn newColumn = newMetaTable.getColumn(column.getLabel());
                                newColumn.setReadOnly(column.isReadOnly());
                                newColumn.setCustom(column.isCustom());
                            }
                        }
                        pastedNode.getMetadataList().add(newMetaTable);
                    }
                }
            } else {
                List<IMetadataTable> copyOfMetadataList = new ArrayList<IMetadataTable>();
                for (IMetadataTable metaTable : copiedNode.getMetadataList()) {
                    IMetadataTable newTable = metaTable.clone();
                    newTable.setTableName(createNewConnectionName(metaTable.getTableName()));
                    oldMetaToNewMeta.put(pastedNode.getUniqueName() + ":" + metaTable.getTableName(), newTable.getTableName());

                    for (IMetadataColumn column : metaTable.getListColumns()) {
                        if (column.isCustom()) {
                            IMetadataColumn newColumn = newTable.getColumn(column.getLabel());
                            newColumn.setReadOnly(column.isReadOnly());
                            newColumn.setCustom(column.isCustom());
                        }
                    }
                    newTable.sortCustomColumns();
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

            for (ElementParameter param : (List<ElementParameter>) copiedNode.getElementParametersWithChildrens()) {
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
                        // pastedNode.getElementParameter(param.getName()).setValue(param.getValue());
                        pastedNode.setPropertyValue(param.getName(), param.getValue());
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
                // test if the target is in the nodes to paste to add the
                // connection
                // if the targeted node is not in the nodes to paste, then the
                // string will be null
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

                    if (connection.getLineStyle().hasConnectionCategory(IConnectionCategory.UNIQUE_NAME)) {
                        String newNameBuiltIn = oldMetaToNewMeta.get(pastedSourceNode.getUniqueName() + ":"
                                + connection.getMetaName());
                        if (newNameBuiltIn == null) {
                            newConnectionName = createNewConnectionName(connection.getName());
                        } else {
                            newConnectionName = newNameBuiltIn;
                        }
                    } else {
                        newConnectionName = connection.getName();
                    }

                    String meta = oldMetaToNewMeta.get(pastedSourceNode.getUniqueName() + ":" + connection.getMetaName());
                    if (meta != null) {
                        if (pastedSourceNode.getConnectorFromType(connection.getLineStyle()).isBuiltIn()
                                && !connection.getLineStyle().equals(EConnectionType.TABLE)) {
                            newConnectionName = meta;
                        }
                        metaTableName = meta;
                    } else {
                        if (pastedSourceNode.getConnectorFromType(connection.getLineStyle()).isBuiltIn()) {
                            metaTableName = pastedSourceNode.getMetadataList().get(0).getTableName();
                        } else {
                            metaTableName = pastedSourceNode.getUniqueName(); // connection.getMetaName();
                        }
                    }
                    Connection pastedConnection = new Connection(pastedSourceNode, pastedTargetNode, connection.getLineStyle(),
                            connection.getConnectorName(), metaTableName, newConnectionName);
                    // pastedConnection.setActivate(pastedSourceNode.isActivate());
                    for (ElementParameter param : (List<ElementParameter>) connection.getElementParameters()) {
                        // pastedConnection.getElementParameter(param.getName())
                        // .setValue(param.getValue());
                        pastedConnection.setPropertyValue(param.getName(), param.getValue());
                    }
                    pastedConnection.getConnectionLabel().setOffset(new Point(connection.getConnectionLabel().getOffset()));
                    INodeConnector connector = pastedConnection.getSourceNodeConnector();
                    connector.setCurLinkNbOutput(connector.getCurLinkNbOutput() + 1);
                    connector = pastedConnection.getTargetNodeConnector();
                    connector.setCurLinkNbInput(connector.getCurLinkNbInput() + 1);
                    IExternalNode externalNode = pastedTargetNode.getExternalNode();
                    if (externalNode != null) {
                        externalNode.renameInputConnection(connection.getName(), newConnectionName);
                    }
                }
            }
        }

        // check if the new components use the old components name.
        boolean useOldComponentsName = false;
        for (NodeContainer nodeContainer : nodeContainerList) {
            Node currentNode = nodeContainer.getNode();
            for (String oldName : oldNameTonewNameMap.keySet()) {
                if ((!oldName.equals(oldNameTonewNameMap.get(oldName))) && currentNode.useData(oldName)) {
                    useOldComponentsName = true;
                    break;
                }
            }
            if (useOldComponentsName) {
                break;
            }
        }
        if (useOldComponentsName) {
            MessageBox msgBox = new MessageBox(PlatformUI.getWorkbench().getDisplay().getActiveShell(), SWT.YES | SWT.NO
                    | SWT.ICON_WARNING);
            msgBox.setMessage("Components variable are used in the copied elements, do you want to rename them automatically?");
            if (msgBox.open() == SWT.YES) {
                for (NodeContainer nodeContainer : nodeContainerList) {
                    Node currentNode = nodeContainer.getNode();
                    for (String oldName : oldNameTonewNameMap.keySet()) {
                        currentNode.renameData(oldName, oldNameTonewNameMap.get(oldName));
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

        AbstractMultiPageTalendEditor multiPageTalendEditor = (AbstractMultiPageTalendEditor) PlatformUI.getWorkbench()
                .getActiveWorkbenchWindow().getActivePage().getActiveEditor();
        GraphicalViewer viewer = multiPageTalendEditor.getTalendEditor().getViewer();

        // save old selection
        if (!multipleCommand) {
            oldSelection = new ArrayList<EditPart>();
            for (EditPart editPart : (List<EditPart>) viewer.getSelectedEditParts()) {
                oldSelection.add(editPart);
            }
            // remove the old selection
            viewer.deselectAll();
        }

        // creates the different nodes
        for (NodeContainer nodeContainer : nodeContainerList) {
            process.addNodeContainer(nodeContainer);
        }
        // set the new node as the current selection
        if (!multipleCommand) {
            EditPart processPart = (EditPart) viewer.getRootEditPart().getChildren().get(0);
            if (processPart instanceof ProcessPart) { // can only be
                // ProcessPart but still
                // test
                for (EditPart editPart : (List<EditPart>) processPart.getChildren()) {
                    if (editPart instanceof NodePart) {
                        Node currentNode = (Node) editPart.getModel();
                        if (nodeContainerList.contains(currentNode.getNodeContainer())) {
                            viewer.appendSelection(editPart);
                        }
                    }
                }
            }
        }

        // check that the created connections exists now, or create them if needed
        for (String newConnectionName : createdNames) {
            if (process.checkValidConnectionName(newConnectionName, true)) {
                process.addUniqueConnectionName(newConnectionName);
            }
        }
        process.checkStartNodes();
        process.checkProcess();
    }

    @SuppressWarnings("unchecked")//$NON-NLS-1$
    @Override
    public void undo() {
        // remove the current selection
        AbstractMultiPageTalendEditor multiPageTalendEditor = (AbstractMultiPageTalendEditor) PlatformUI.getWorkbench()
                .getActiveWorkbenchWindow().getActivePage().getActiveEditor();
        GraphicalViewer viewer = multiPageTalendEditor.getTalendEditor().getViewer();
        if (!multipleCommand) {
            viewer.deselectAll();
        }

        for (NodeContainer nodeContainer : nodeContainerList) {
            // remove the connections name from the list
            for (Connection connection : (List<Connection>) nodeContainer.getNode().getOutgoingConnections()) {
                process.removeUniqueConnectionName(connection.getName());
            }
            process.removeNodeContainer(nodeContainer);
        }

        // set the old selection active
        if (!multipleCommand) {
            for (EditPart editPart : oldSelection) {
                /*
                 * if (editPart instanceof NodePart) { Node node = (Node) editPart.getModel(); }
                 */
                viewer.appendSelection(editPart);
            }
        }
        // check that the created connections are removed, remove them if not
        for (String newConnectionName : createdNames) {
            if (!process.checkValidConnectionName(newConnectionName, true)) {
                process.removeUniqueConnectionName(newConnectionName);
            }
        }

        process.checkStartNodes();
        process.checkProcess();
    }

    /**
     * Getter for multipleCommand.
     * 
     * @return the multipleCommand
     */
    public boolean isMultipleCommand() {
        return multipleCommand;
    }

    /**
     * Sets the multipleCommand.
     * 
     * @param multipleCommand the multipleCommand to set
     */
    public void setMultipleCommand(boolean multipleCommand) {
        this.multipleCommand = multipleCommand;
    }

    /**
     * Getter for nodeContainerList.
     * 
     * @return the nodeContainerList
     */
    public List<NodeContainer> getNodeContainerList() {
        return nodeContainerList;
    }
}
