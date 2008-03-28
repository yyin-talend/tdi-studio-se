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
package org.talend.repository.model.migration;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.exception.PersistenceException;
import org.talend.core.model.components.ComponentUtilities;
import org.talend.core.model.components.IComponent;
import org.talend.core.model.components.IComponentsFactory;
import org.talend.core.model.migration.AbstractJobMigrationTask;
import org.talend.core.model.process.INodeConnector;
import org.talend.core.model.properties.ProcessItem;
import org.talend.designer.core.model.utils.emf.talendfile.ColumnType;
import org.talend.designer.core.model.utils.emf.talendfile.ConnectionType;
import org.talend.designer.core.model.utils.emf.talendfile.ElementParameterType;
import org.talend.designer.core.model.utils.emf.talendfile.MetadataType;
import org.talend.designer.core.model.utils.emf.talendfile.NodeType;
import org.talend.designer.core.model.utils.emf.talendfile.ProcessType;
import org.talend.designer.core.model.utils.emf.talendfile.TalendFileFactory;
import org.talend.repository.model.ComponentsFactoryProvider;
import org.talend.repository.model.ProxyRepositoryFactory;

/**
 * DOC nrousseau class global comment. Detailled comment
 */
public class ReplaceMultiFlowBytReplicateMigrationTask extends AbstractJobMigrationTask {

    private static final int GRID_SIZE = 32;

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.migration.AbstractJobMigrationTask#executeOnProcess(org.talend.core.model.properties.ProcessItem)
     */
    @Override
    public ExecutionResult executeOnProcess(ProcessItem processItem) {
        IComponentsFactory componentFactory = ComponentsFactoryProvider.getInstance();
        boolean modified = false;
        try {
            List<NodeType> initialNodes = new ArrayList<NodeType>(processItem.getProcess().getNode());
            for (NodeType nodeType : initialNodes) {
                IComponent component = componentFactory.get(nodeType.getComponentName());
                if (component != null) {
                    if (checkMaxOutputAndUpdate(processItem, component, nodeType)) {
                        modified = true;
                    }
                } else {
                    ExceptionHandler.log("Component \"" + nodeType.getComponentName() + "\" in the job \""
                            + processItem.getProperty().getLabel() + "\" doesn't exist anymore");
                }
            }
            if (modified) {
                return ExecutionResult.SUCCESS_WITH_ALERT;
            } else {
                return ExecutionResult.NOTHING_TO_DO;
            }
        } catch (PersistenceException e) {
            ExceptionHandler.process(e);
            return ExecutionResult.FAILURE;
        }
    }

    /**
     * DOC nrousseau Comment method "checkMaxOutputAndUpdate".
     * 
     * @param processItem
     * @param component
     */
    @SuppressWarnings("unchecked")
    private boolean checkMaxOutputAndUpdate(ProcessItem processItem, IComponent component, NodeType nodeTypeSource)
            throws PersistenceException {
        boolean modified = false;

        String nodeSourceUniqueName = ComponentUtilities.getNodeUniqueName(nodeTypeSource);

        ProxyRepositoryFactory factory = ProxyRepositoryFactory.getInstance();
        List<INodeConnector> nodeConnectors = (List<INodeConnector>) component.createConnectors();
        for (INodeConnector nodeConnector : nodeConnectors) {
            if (nodeConnector.getBaseSchema().equals("FLOW") && nodeConnector.getMaxLinkOutput() == 1) {
                List<ConnectionType> connections = getConnectionsToMove(processItem, nodeConnector, nodeSourceUniqueName);
                if (connections != null) {
                    List<Rectangle> targetPos = getTargetsRectangle(connections);
                    Rectangle startPos = getNodeRectangle(processItem.getProcess(), nodeSourceUniqueName);
                    Integer tReplicateYPos = 0;
                    Integer tReplicateXPos = Integer.MAX_VALUE;
                    for (Rectangle rect : targetPos) {
                        if (rect.x < tReplicateXPos) {
                            tReplicateXPos = rect.x;
                        }
                        tReplicateYPos += rect.y;
                    }
                    tReplicateXPos = (tReplicateXPos + startPos.x) / 2;
                    tReplicateYPos = tReplicateYPos / targetPos.size();
                    int tempVar = tReplicateXPos / GRID_SIZE;
                    tReplicateXPos = tempVar * GRID_SIZE;
                    tempVar = tReplicateYPos / GRID_SIZE;
                    tReplicateYPos = tempVar * GRID_SIZE;
                    Point tReplicatePos = findLocationForNode(processItem, new Point(tReplicateXPos, tReplicateYPos));
                    addtReplicateComponent(processItem, tReplicatePos, nodeTypeSource, nodeSourceUniqueName, nodeConnector,
                            connections);
                    modified = true;
                }
            }
        }

        if (modified) {
            factory.save(processItem);
        }

        return modified;
    }

    /**
     * DOC nrousseau Comment method "addtReplicateComponent".
     * 
     * @param processItem
     * @param replicatePos
     * @param connections
     */
    private void addtReplicateComponent(ProcessItem processItem, Point replicatePos, NodeType nodeTypeSource, String sourceName,
            INodeConnector nodeConnector, List<ConnectionType> connections) {
        TalendFileFactory fileFact = TalendFileFactory.eINSTANCE;
        // create the node
        String uniqueName = createNodeType(processItem, replicatePos, nodeTypeSource, sourceName, nodeConnector, connections,
                fileFact);

        // create the connection from source component to tReplicate
        createConnectionType(processItem, sourceName, nodeConnector, fileFact, uniqueName);
    }

    /**
     * DOC nrousseau Comment method "createConnectionType".
     * 
     * @param processItem
     * @param sourceName
     * @param nodeConnector
     * @param fileFact
     * @param uniqueName
     */
    private void createConnectionType(ProcessItem processItem, String sourceName, INodeConnector nodeConnector,
            TalendFileFactory fileFact, String uniqueName) {
        List<String> connectionNames = new ArrayList<String>();
        for (Object oConnection : processItem.getProcess().getConnection()) {
            ConnectionType connection = (ConnectionType) oConnection;
            connectionNames.add(connection.getLabel());
        }
        String baseName = "row";
        int id = 1;
        String connectionName = baseName + id;
        while (connectionNames.contains(connectionName)) {
            id++;
            connectionName = baseName + id;
        }
        ConnectionType connectionType = fileFact.createConnectionType();
        connectionType.setConnectorName(nodeConnector.getName());
        connectionType.setLabel(connectionName);
        connectionType.setLineStyle(nodeConnector.getDefaultConnectionType().getId());
        connectionType.setMetaname(sourceName);
        connectionType.setSource(sourceName);
        connectionType.setTarget(uniqueName);

        processItem.getProcess().getConnection().add(connectionType);
    }

    /**
     * DOC nrousseau Comment method "createNodeType".
     * 
     * @param processItem
     * @param replicatePos
     * @param nodeTypeSource
     * @param sourceName
     * @param nodeConnector
     * @param connections
     * @param fileFact
     * @return
     */
    private String createNodeType(ProcessItem processItem, Point replicatePos, NodeType nodeTypeSource, String sourceName,
            INodeConnector nodeConnector, List<ConnectionType> connections, TalendFileFactory fileFact) {
        NodeType nodeType = fileFact.createNodeType();
        String uniqueName = ComponentUtilities.generateUniqueNodeName("tReplicate", processItem.getProcess());
        ElementParameterType elemParam = fileFact.createElementParameterType();
        elemParam.setField("TEXT");
        elemParam.setName("UNIQUE_NAME");
        elemParam.setValue(uniqueName);
        nodeType.getElementParameter().add(elemParam);
        ComponentUtilities.setNodeUniqueName(nodeType, uniqueName);
        nodeType.setComponentName("tReplicate");
        for (ConnectionType connectionType : connections) {
            connectionType.setConnectorName("FLOW");
            connectionType.setSource(uniqueName);
            connectionType.setMetaname(uniqueName);
        }
        nodeType.setPosX(replicatePos.x);
        nodeType.setPosY(replicatePos.y);
        nodeType.setSizeX(GRID_SIZE);
        nodeType.setSizeY(GRID_SIZE);

        // create the metadata for the new node

        if (nodeTypeSource.getMetadata().size() != 0) {
            MetadataType metadataTypeSource = null;
            for (Object oMetadataType : nodeTypeSource.getMetadata()) {
                MetadataType metadataType = (MetadataType) oMetadataType;
                if ((metadataType.getConnector() != null && metadataType.getConnector().equals(nodeConnector.getName()))
                        || metadataType.getName().equals(sourceName)) {
                    metadataTypeSource = metadataType;
                }
            }

            if (metadataTypeSource != null) {
                MetadataType newMetadataType = fileFact.createMetadataType();
                newMetadataType.setComment(metadataTypeSource.getComment());
                newMetadataType.setConnector("FLOW");
                newMetadataType.setName(uniqueName);
                for (Object oColumn : metadataTypeSource.getColumn()) {
                    ColumnType columnType = (ColumnType) oColumn;
                    ColumnType newColumnType = fileFact.createColumnType();
                    newColumnType.setComment(columnType.getComment());
                    newColumnType.setDefaultValue(columnType.getDefaultValue());
                    if (columnType.isSetKey()) {
                        newColumnType.setKey(columnType.isKey());
                    }
                    if (columnType.isSetLength()) {
                        newColumnType.setLength(columnType.getLength());
                    }
                    newColumnType.setName(columnType.getName());
                    if (columnType.isSetNullable()) {
                        newColumnType.setNullable(columnType.isNullable());
                    }
                    newColumnType.setOriginalDbColumnName(columnType.getOriginalDbColumnName());
                    newColumnType.setPattern(columnType.getPattern());
                    if (columnType.isSetPrecision()) {
                        newColumnType.setPrecision(columnType.getPrecision());
                    }
                    newColumnType.setSourceType(columnType.getSourceType());
                    newColumnType.setType(columnType.getType());
                    newMetadataType.getColumn().add(newColumnType);
                }
                nodeType.getMetadata().add(newMetadataType);
            }
        }

        processItem.getProcess().getNode().add(nodeType);
        return uniqueName;
    }

    /**
     * DOC nrousseau Comment method "findLocationForNode".
     * 
     * @param processItem
     * @param point
     * @return
     */
    private Point findLocationForNode(ProcessItem processItem, Point point) {
        Rectangle newRect = new Rectangle(point, new Dimension(GRID_SIZE, GRID_SIZE));
        Point newLocation = new Point(point);
        for (Object oNodeType : processItem.getProcess().getNode()) {
            NodeType node = (NodeType) oNodeType;
            String uniqueName = ComponentUtilities.getNodeUniqueName(node);
            Rectangle currentRect = getNodeRectangle(processItem.getProcess(), uniqueName);
            if (currentRect.intersects(newRect)) {
                newLocation.x += GRID_SIZE;
                newLocation.y += GRID_SIZE;
                return findLocationForNode(processItem, point);
            }
        }
        return newLocation;
    }

    /**
     * DOC nrousseau Comment method "getNodePosition".
     * 
     * @param nodeUniqueName
     * @return
     */
    private Rectangle getNodeRectangle(ProcessType processType, String nodeUniqueName) {

        NodeType nodeType = ComponentUtilities.getNodeTypeFromUniqueName(processType, nodeUniqueName);
        return new Rectangle(nodeType.getPosX(), nodeType.getPosY(), nodeType.getSizeX(), nodeType.getSizeY());
    }

    /**
     * DOC nrousseau Comment method "getTargetsPosition".
     * 
     * @param connections
     * @return
     */
    private List<Rectangle> getTargetsRectangle(List<ConnectionType> connections) {
        List<Rectangle> targetPosList = new ArrayList<Rectangle>();
        for (ConnectionType connection : connections) {
            targetPosList.add(getNodeRectangle((ProcessType) connection.eContainer(), connection.getTarget()));
        }
        return targetPosList;
    }

    /**
     * DOC nrousseau Comment method "getConnectionsToMove".
     * 
     * @param processItem
     * @param nodeUniqueName
     * @return
     */
    private List<ConnectionType> getConnectionsToMove(ProcessItem processItem, INodeConnector connector, String nodeUniqueName) {
        List<ConnectionType> connToMove = new ArrayList<ConnectionType>();
        for (Object oConnection : processItem.getProcess().getConnection()) {
            ConnectionType connectionType = (ConnectionType) oConnection;
            if (connectionType.getSource().equals(nodeUniqueName)) {
                if (connectionType.getConnectorName().equals(connector.getName())) {
                    connToMove.add(connectionType);
                }
            }
        }

        if (connToMove.size() <= 1) {
            return null;
        }
        return connToMove;
    }
    public Date getOrder() {
        GregorianCalendar gc = new GregorianCalendar(2008, 2, 17, 12, 0, 0);
        return gc.getTime();
    }
}
