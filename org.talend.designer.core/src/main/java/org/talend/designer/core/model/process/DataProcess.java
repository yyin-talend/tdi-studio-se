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
package org.talend.designer.core.model.process;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.talend.core.model.components.IComponent;
import org.talend.core.model.components.IMultipleComponentConnection;
import org.talend.core.model.components.IMultipleComponentItem;
import org.talend.core.model.components.IMultipleComponentManager;
import org.talend.core.model.components.IMultipleComponentParameter;
import org.talend.core.model.metadata.IMetadataTable;
import org.talend.core.model.process.AbstractConnection;
import org.talend.core.model.process.AbstractExternalNode;
import org.talend.core.model.process.AbstractNode;
import org.talend.core.model.process.EConnectionType;
import org.talend.core.model.process.EParameterFieldType;
import org.talend.core.model.process.IConnection;
import org.talend.core.model.process.IConnectionCategory;
import org.talend.core.model.process.IElement;
import org.talend.core.model.process.IElementParameter;
import org.talend.core.model.process.IExternalData;
import org.talend.core.model.process.IExternalNode;
import org.talend.core.model.process.INode;
import org.talend.core.model.process.INodeConnector;
import org.talend.core.model.process.IProcess;
import org.talend.designer.core.model.components.EParameterName;
import org.talend.designer.core.model.process.jobsettings.JobSettingsManager;
import org.talend.designer.core.ui.editor.connections.Connection;
import org.talend.designer.core.ui.editor.nodecontainer.NodeContainer;
import org.talend.designer.core.ui.editor.nodes.Node;
import org.talend.designer.core.ui.editor.nodes.Node.Data;
import org.talend.designer.core.ui.editor.process.Process;
import org.talend.repository.model.ComponentsFactoryProvider;
import org.talend.repository.model.ExternalNodesFactory;

/**
 * This class will create the list of nodes that will be used to generate the code.
 * 
 * $Id$
 * 
 */
public class DataProcess {

    private static final String HASH_COMPONENT_NAME = "tHash"; //$NON-NLS-1$

    private Map<INode, INode> buildCheckMap = null;

    private List<Node> checkRefList = null;

    private Map<INode, INode> checkMultipleMap = null;

    private List<INode> dataNodeList;

    private final Process process;

    private Process duplicatedProcess;

    public DataProcess(Process process) {
        this.process = process;
    }

    private void initialize() {
        buildCheckMap = new HashMap<INode, INode>();
        checkRefList = new ArrayList<Node>();
        checkMultipleMap = new HashMap<INode, INode>();
        dataNodeList = new ArrayList<INode>();
    }

    private void copyElementParametersValue(IElement sourceElement, IElement targetElement) {
        for (IElementParameter sourceParam : sourceElement.getElementParameters()) {
            IElementParameter targetParam = targetElement.getElementParameter(sourceParam.getName());
            if (targetParam != null) {
                targetParam.setValue(sourceParam.getValue());
                if (targetParam.getField() == EParameterFieldType.TABLE) {
                    targetParam.setListItemsValue(sourceParam.getListItemsValue());
                }
                for (String name : targetParam.getChildParameters().keySet()) {
                    IElementParameter targetChildParam = targetParam.getChildParameters().get(name);
                    IElementParameter sourceChildParam = sourceParam.getChildParameters().get(name);
                    targetChildParam.setValue(sourceChildParam.getValue());
                    if (targetChildParam.getField() == EParameterFieldType.TABLE) {
                        targetChildParam.setListItemsValue(sourceChildParam.getListItemsValue());
                    }
                }
            }
        }
    }

    // should only be called by a starting node
    @SuppressWarnings("unchecked")//$NON-NLS-1$
    public INode buildDataNodeFromNode(final Node graphicalNode, String prefix) {
        if (buildCheckMap.containsKey(graphicalNode)) {
            return buildCheckMap.get(graphicalNode);
        }

        AbstractNode dataNode;

        if (graphicalNode.getExternalNode() == null) {
            dataNode = new DataNode();
        } else {
            // mapper
            dataNode = (AbstractNode) ExternalNodesFactory.getInstance(graphicalNode.getPluginFullName());
            IExternalData externalData = graphicalNode.getExternalData();
            if (externalData != null) {
                ((IExternalNode) dataNode).setExternalData(externalData);
            }
        }
        dataNode.setActivate(graphicalNode.isActivate());
        dataNode.setStart(graphicalNode.isStart());
        dataNode.setMetadataList(graphicalNode.getMetadataList());
        dataNode.setPluginFullName(graphicalNode.getPluginFullName());
        dataNode.setElementParameters(graphicalNode.getComponent().createElementParameters(dataNode));
        dataNode.setListConnector(graphicalNode.getListConnector());
        copyElementParametersValue(graphicalNode, dataNode);
        String uniqueName = graphicalNode.getUniqueName();
        if (prefix != null) {
            uniqueName = prefix + uniqueName;
        }
        dataNode.setUniqueName(uniqueName);
        dataNode.setSubProcessStart(graphicalNode.isSubProcessStart());
        dataNode.setThereLinkWithHash(graphicalNode.isThereLinkWithHash());
        dataNode.setHasConditionalOutputs(graphicalNode.hasConditionalOutputs());
        dataNode.setIsMultiplyingOutputs(graphicalNode.isMultiplyingOutputs());
        dataNode.setIsSubtreeWithLoop(graphicalNode.isSubtreeWithLoop());
        dataNode.setProcess(graphicalNode.getProcess());
        dataNode.setComponent(graphicalNode.getComponent());

        if (graphicalNode.isDummy() && !graphicalNode.isActivate()) {
            dataNode = new DataNode(ComponentsFactoryProvider.getInstance().get("tDummyRow"), uniqueName); //$NON-NLS-1$
            dataNode.setActivate(true);
            dataNode.setStart(graphicalNode.isStart());
            dataNode.setMetadataList(graphicalNode.getMetadataList());
            dataNode.setSubProcessStart(graphicalNode.isSubProcessStart());
            dataNode.setThereLinkWithHash(graphicalNode.isThereLinkWithHash());
            dataNode.setHasConditionalOutputs(false);
            dataNode.setIsMultiplyingOutputs(graphicalNode.isMultiplyingOutputs());
            dataNode.setIsSubtreeWithLoop(graphicalNode.isSubtreeWithLoop());
            dataNode.setProcess(graphicalNode.getProcess());
        }
        dataNode.setDesignSubjobStartNode(graphicalNode.getDesignSubjobStartNode());

        dataNodeList.add(dataNode);
        buildCheckMap.put(graphicalNode, dataNode);

        List<IConnection> outgoingConnections = new ArrayList<IConnection>();
        List<IConnection> incomingConnections = new ArrayList<IConnection>();
        dataNode.setIncomingConnections(incomingConnections);
        dataNode.setOutgoingConnections(outgoingConnections);

        DataConnection dataConnec;
        for (IConnection connection : graphicalNode.getOutgoingConnections()) {
            if (!connection.isActivate()) {
                continue;
            }
            IElementParameter monitorParam = connection.getElementParameter("MONITOR_CONNECTION");
            if (monitorParam != null && (!connection.getLineStyle().equals(EConnectionType.FLOW_REF))
                    && ((Boolean) monitorParam.getValue())) {
                addvFlowMeterBetween(dataNode, buildDataNodeFromNode((Node) connection.getTarget(), prefix), connection,
                        graphicalNode.getProcess(), connection.getElementParameters());
            } else {
                dataConnec = new DataConnection();
                dataConnec.setActivate(connection.isActivate());
                dataConnec.setLineStyle(connection.getLineStyle());
                if ((connection.getLineStyle().hasConnectionCategory(IConnectionCategory.EXECUTION_ORDER))
                        && (connection.getTarget().getMetadataList().size() > 0)) {
                    dataConnec.setMetadataTable(connection.getTarget().getMetadataList().get(0));
                } else {
                    dataConnec.setMetadataTable(connection.getMetadataTable());
                }
                String name = connection.getName();
                if (prefix != null) {
                    name = prefix + name;
                }
                dataConnec.setName(name);
                String uniqueName2 = connection.getUniqueName();
                if (prefix != null) {
                    uniqueName2 = prefix + uniqueName2;
                }
                dataConnec.setUniqueName(uniqueName2);
                dataConnec.setSource(dataNode);
                dataConnec.setCondition(connection.getCondition());
                dataConnec.setConnectorName(connection.getConnectorName());
                dataConnec.setInputId(connection.getInputId());
                INode target = buildDataNodeFromNode((Node) connection.getTarget(), prefix);
                dataConnec.setTarget(target);
                incomingConnections = (List<IConnection>) target.getIncomingConnections();
                if (incomingConnections == null) {
                    incomingConnections = new ArrayList<IConnection>();
                }
                outgoingConnections.add(dataConnec);
                incomingConnections.add(dataConnec);

                if (!connection.getName().equals(name)) {
                    if (target instanceof AbstractExternalNode) {
                        // System.out.println("dataProcess: rename input:" + connection.getName() + " to " + name);
                        ((AbstractExternalNode) target).renameInputConnection(connection.getName(), name);
                    }
                    if (dataNode instanceof AbstractExternalNode) {
                        // System.out.println("dataProcess: rename output:" + connection.getName() + " to " + name);
                        ((AbstractExternalNode) dataNode).renameOutputConnection(connection.getName(), name);
                    }
                }
            }
        }

        return dataNode;
    }

    public INode buildDataNodeFromNode(final Node graphicalNode) {
        return buildDataNodeFromNode(graphicalNode, null);
    }

    private INode addvFlowMeterBetween(INode sourceNode, INode targetNode, IConnection connection, IProcess process,
            List<? extends IElementParameter> parameters) {
        // from current node to vFlowMeter node.
        DataConnection dataConnec = new DataConnection();
        dataConnec.setActivate(connection.isActivate());
        if (connection.getLineStyle().hasConnectionCategory(IConnectionCategory.FLOW)) {
            dataConnec.setLineStyle(EConnectionType.FLOW_MAIN);
        } else {
            dataConnec.setLineStyle(connection.getLineStyle());
        }
        dataConnec.setMetadataTable(connection.getMetadataTable());
        dataConnec.setName(connection.getName());
        dataConnec.setUniqueName(connection.getUniqueName());
        dataConnec.setSource(sourceNode);
        dataConnec.setCondition(connection.getCondition());
        dataConnec.setConnectorName(connection.getConnectorName());
        dataConnec.setInputId(connection.getInputId());
        DataNode meterNode = new DataNode(ComponentsFactoryProvider.getInstance().get("tFlowMeter"), "vFlowMeter_"
                + connection.getUniqueName());
        meterNode.getMetadataList().get(0).setListColumns(connection.getMetadataTable().getListColumns());
        meterNode.setActivate(connection.isActivate());
        meterNode.setProcess(process);
        meterNode.setDesignSubjobStartNode(sourceNode.getDesignSubjobStartNode());
        for (IElementParameter param : parameters) {
            IElementParameter meterParam = meterNode.getElementParameter(param.getName());
            if (meterParam != null) {
                meterParam.setValue(param.getValue());
            }
        }
        dataConnec.setTarget(meterNode);
        ((List<IConnection>) meterNode.getIncomingConnections()).add(dataConnec);
        ((List<IConnection>) sourceNode.getOutgoingConnections()).add(dataConnec);
        dataNodeList.add(meterNode);

        // from vFlowMeter node to next node.
        dataConnec = new DataConnection();
        dataConnec.setActivate(connection.isActivate());
        dataConnec.setLineStyle(connection.getLineStyle());
        dataConnec.setMetadataTable(meterNode.getMetadataList().get(0));
        dataConnec.setName(connection.getName());
        dataConnec.setUniqueName("meterRow" + connection.getUniqueName());
        dataConnec.setSource(meterNode);
        dataConnec.setCondition(connection.getCondition());
        dataConnec.setConnectorName(connection.getConnectorName());
        dataConnec.setInputId(connection.getInputId());
        dataConnec.setTarget(targetNode);
        ((List<IConnection>) targetNode.getIncomingConnections()).add(dataConnec);
        ((List<IConnection>) meterNode.getOutgoingConnections()).add(dataConnec);

        return meterNode;
    }

    /**
     * DOC nrousseau Comment method "addMultipleNode".
     * 
     * @param graphicalNode
     * @param multipleComponentManager
     * @return
     */
    @SuppressWarnings("unchecked")//$NON-NLS-1$
    private void addMultipleNode(INode graphicalNode, List<IMultipleComponentManager> multipleComponentManagers) {
        AbstractNode dataNode;
        // prepare all the nodes

        INode previousNode = buildCheckMap.get(graphicalNode);
        dataNodeList.remove(previousNode);

        for (IMultipleComponentManager multipleComponentManager : multipleComponentManagers) {

            Map<IMultipleComponentItem, AbstractNode> itemsMap = new HashMap<IMultipleComponentItem, AbstractNode>();
            prepareAllMultipleComponentNodes(itemsMap, multipleComponentManager, graphicalNode);
            setMultipleComponentParameters(multipleComponentManager, itemsMap, graphicalNode);

            // set the first one (input) with the properties of the graphical node.
            dataNode = itemsMap.get(multipleComponentManager.getInput());
            dataNode.setStart(graphicalNode.isStart());
            dataNode.setSubProcessStart(graphicalNode.isSubProcessStart());
            dataNode.setThereLinkWithHash(graphicalNode.isThereLinkWithHash());
            List<IConnection> incomingConnections = (List<IConnection>) dataNode.getIncomingConnections();
            for (IConnection connection : previousNode.getIncomingConnections()) {
                if (connection.getLineStyle().hasConnectionCategory(IConnectionCategory.FLOW)) {
                    if (multipleComponentManager.isSetConnector()) {
                        String parameterString = "SCHEMA_" + multipleComponentManager.getConnector() + ":CONNECTION";
                        String tempLinkName = (String) previousNode.getElementParameter(parameterString).getValue();
                        if (!connection.getName().equals(tempLinkName)) {
                            continue;
                        }
                    }
                }
                AbstractConnection asbractConnect = (AbstractConnection) connection;
                asbractConnect.setTarget(dataNode);
                incomingConnections.add(connection);
            }
            List<IConnection> outgoingConnections = (List<IConnection>) dataNode.getOutgoingConnections();

            // RunBefore / RunAfter Links won't be linked to the output but on the first element of the subprocess.
            for (IConnection connection : previousNode.getOutgoingConnections()) {
                if (connection.getLineStyle().hasConnectionCategory(IConnectionCategory.EXECUTION_ORDER)) {
                    AbstractConnection asbractConnect = (AbstractConnection) connection;
                    asbractConnect.setSource(dataNode);
                    outgoingConnections.add(0, connection);
                }
            }

            // set informations for the last node, so the outgoing connections.
            INode outputNode = itemsMap.get(multipleComponentManager.getOutput());
            outgoingConnections = (List<IConnection>) outputNode.getOutgoingConnections();
            if (multipleComponentManager.existsLinkTo()) {
                // RunBefore / RunAfter Links won't be linked to the output but on the first element of the subprocess.
                for (IConnection connection : previousNode.getOutgoingConnections()) {
                    if (!connection.getLineStyle().hasConnectionCategory(IConnectionCategory.EXECUTION_ORDER)) {
                        AbstractConnection asbractConnect = (AbstractConnection) connection;
                        asbractConnect.setSource(outputNode);
                        outgoingConnections.add(connection);
                    }
                }
            }

            // adds all connections between these nodes
            addAllMultipleComponentConnections(itemsMap, multipleComponentManager, graphicalNode, dataNode, previousNode);

            // return dataNode;
        }
    }

    /**
     * DOC nrousseau Comment method "addAllMultipleComponentConnections".
     * 
     * @param itemsMap
     * @param multipleComponentManager
     * @param graphicalNode
     * @param dataNode
     */
    private void addAllMultipleComponentConnections(Map<IMultipleComponentItem, AbstractNode> itemsMap,
            IMultipleComponentManager multipleComponentManager, INode graphicalNode, AbstractNode dataNode, INode previousNode) {
        List<IConnection> incomingConnections, outgoingConnections;
        int inputID = 0;
        for (IMultipleComponentItem curItem : multipleComponentManager.getItemList()) {
            for (IMultipleComponentConnection curConnec : curItem.getOutputConnections()) {
                AbstractNode nodeSource = itemsMap.get(curItem);
                AbstractNode nodeTarget;

                nodeTarget = itemsMap.get(curConnec.getTarget());

                DataConnection dataConnec = new DataConnection();
                dataConnec.setActivate(graphicalNode.isActivate());
                dataConnec.setLineStyle(EConnectionType.getTypeFromName(curConnec.getConnectionType()));
                dataConnec.setConnectorName(curConnec.getConnectionType());
                dataConnec.setMetadataTable(nodeSource.getMetadataList().get(0));
                // INodeConnector connector = nodeSource.getConnectorFromName(curConnec.getConnectionType());
                // dataConnec.setLineStyle(connector.getDefaultConnectionType());
                // dataConnec.setConnectorName(curConnec.getConnectionType());
                // dataConnec.setMetadataTable(nodeSource.getMetadataFromConnector(curConnec.getConnectionType()));

                dataConnec.setName(EConnectionType.getTypeFromName(curConnec.getConnectionType()).getDefaultLinkName());

                switch (dataConnec.getLineStyle()) {
                case FLOW_MAIN:
                    dataConnec.setName("row_" + itemsMap.get(curItem).getUniqueName()); //$NON-NLS-1$
                    break;
                case FLOW_MERGE:
                    if (curConnec.equals(multipleComponentManager.getInput())) {
                        dataConnec.setInputId(1);
                    } else {
                        dataConnec.setInputId(1 + inputID++);
                    }
                    dataConnec.setName("merge_" + itemsMap.get(curItem).getUniqueName()); //$NON-NLS-1$
                    break;
                // case RUN_BEFORE:
                case ON_SUBJOB_ERROR:
                case ON_SUBJOB_OK:
                    // case RUN_AFTER:
                    if (nodeSource.equals(dataNode)) {
                        INode dataStartNode = ((DataNode) nodeSource).getSubProcessStartNode(false);
                        if (dataStartNode != previousNode) {
                            nodeSource = (AbstractNode) dataStartNode;
                        }
                        nodeTarget.setSubProcessStart(true);

                        List<IConnection> connectionsToRemoveFromList = new ArrayList<IConnection>();
                        outgoingConnections = (List<IConnection>) nodeSource.getOutgoingConnections();
                        for (IConnection connec : outgoingConnections) {
                            if (connec.getLineStyle().equals(curConnec.getConnectionType())) {
                                connectionsToRemoveFromList.add(connec);
                                AbstractConnection connection = (AbstractConnection) connec;
                                connection.setSource(nodeTarget);
                                nodeTarget.getIncomingConnections().remove(connec);
                                ((List<IConnection>) connec.getTarget().getIncomingConnections()).add(connec);
                            }
                        }
                        outgoingConnections.removeAll(connectionsToRemoveFromList);
                        outgoingConnections = (List<IConnection>) nodeTarget.getOutgoingConnections();
                        outgoingConnections.addAll(connectionsToRemoveFromList);

                        // notice: here deal with the case, if the tSortRow is in the second branch of the merge node.
                        if (graphicalNode.isThereLinkWithMerge()) {
                            Map<INode, Integer> linkedMergeInfo = graphicalNode.getLinkedMergeInfo();
                            if (linkedMergeInfo.get(linkedMergeInfo.keySet().toArray()[0]) > 1) {
                                dataConnec.setLineStyle(EConnectionType.RUN_AFTER);
                                dataConnec.setConnectorName(EConnectionType.RUN_AFTER.getName());
                                dataConnec.setName(EConnectionType.ON_SUBJOB_OK.getDefaultLinkName());
                                AbstractNode tmp = nodeSource;
                                nodeSource = nodeTarget;
                                nodeTarget = tmp;
                            }
                        }

                    }

                    break;
                case ON_COMPONENT_OK:
                case ON_COMPONENT_ERROR:
                    nodeTarget.setSubProcessStart(true);
                    break;
                default:
                    break;
                }
                dataConnec.setSource(nodeSource);
                dataConnec.setTarget(nodeTarget);
                dataConnec.setCondition(""); //$NON-NLS-1$
                outgoingConnections = (List<IConnection>) nodeSource.getOutgoingConnections();
                int indexOfFirstThenRun = 0;
                for (IConnection connection : outgoingConnections) {
                    if (connection.getLineStyle().equals(EConnectionType.ON_SUBJOB_OK)) {
                        break;
                    }
                    indexOfFirstThenRun++;
                }
                // outgoingConnections.add(indexOfFirstThenRun, dataConnec);
                outgoingConnections.add(0, dataConnec);
                incomingConnections = (List<IConnection>) nodeTarget.getIncomingConnections();
                incomingConnections.add(dataConnec);
            }
        }
    }

    /**
     * DOC nrousseau Comment method "prepareAllMultipleComponentNodes".
     * 
     * @param itemsMap
     * @param multipleComponentManager
     * @param graphicalNode
     */
    private void prepareAllMultipleComponentNodes(Map<IMultipleComponentItem, AbstractNode> itemsMap,
            IMultipleComponentManager multipleComponentManager, INode graphicalNode) {

        List<IMultipleComponentItem> itemList = multipleComponentManager.getItemList();

        for (IMultipleComponentItem curItem : itemList) {
            String uniqueName = graphicalNode.getUniqueName() + "_" + curItem.getName(); //$NON-NLS-1$
            IComponent component = ComponentsFactoryProvider.getInstance().get(curItem.getComponent());
            if (component == null) {
                continue;
            }
            DataNode curNode = new DataNode(component, uniqueName);
            curNode.setActivate(graphicalNode.isActivate());
            IMetadataTable newMetadata = null;
            if (multipleComponentManager.isSetConnector()) {
                newMetadata = graphicalNode.getMetadataFromConnector(multipleComponentManager.getConnector()).clone();
            } else {
                newMetadata = graphicalNode.getMetadataList().get(0).clone();
            }
            newMetadata.setTableName(uniqueName);
            if (graphicalNode.isDesignSubjobStartNode()) {
                curNode.setDesignSubjobStartNode(null);
            } else {
                curNode.setDesignSubjobStartNode(graphicalNode.getDesignSubjobStartNode());
            }

            // propagate metadataLists for output component. only apply to multi-input virtual component
            if (multipleComponentManager.isSetConnector() && multipleComponentManager.getOutputName().equals(curItem.getName())) {
                // deactivate dummy component
                if (curNode.getComponentName().equals("tDummyRow")) {// or use
                    // "!multipleComponentManager.existsLinkTo()"
                    curNode.setActivate(false);
                } else {
                    // propagate all metadataTables
                    List<IMetadataTable> newMetadataList = new ArrayList<IMetadataTable>();
                    for (IMetadataTable metadataTable : graphicalNode.getMetadataList()) {
                        newMetadataList.add(metadataTable.clone());
                    }
                    curNode.setMetadataList(newMetadataList);
                }

            } else {
                curNode.getMetadataList().remove(0);
                curNode.getMetadataList().add(newMetadata);
            }

            List<IConnection> outgoingConnections = new ArrayList<IConnection>();
            List<IConnection> incomingConnections = new ArrayList<IConnection>();
            curNode.setIncomingConnections(incomingConnections);
            curNode.setOutgoingConnections(outgoingConnections);
            curNode.setProcess(graphicalNode.getProcess());
            curNode.setVirtualGenerateNode(true);
            dataNodeList.add(curNode);
            itemsMap.put(curItem, curNode);
        }
    }

    /**
     * DOC nrousseau Comment method "setMultipleComponentParameters".
     * 
     * @param multipleComponentManager
     * @param itemsMap
     * @param graphicalNode
     */
    private void setMultipleComponentParameters(IMultipleComponentManager multipleComponentManager,
            Map<IMultipleComponentItem, AbstractNode> itemsMap, INode graphicalNode) {

        List<IMultipleComponentItem> itemList = multipleComponentManager.getItemList();
        boolean targetFound;
        INode targetNode = null;
        for (int i = 0; i < itemList.size(); i++) {
            targetNode = itemsMap.get(itemList.get(i));
            targetFound = false;
            for (int j = 0; j < targetNode.getElementParameters().size() && !targetFound; j++) {
                if (targetNode.getElementParameters().get(j).getName().equals(EParameterName.TSTATCATCHER_STATS.getName())) {
                    IElementParameter param = targetNode.getElementParameters().get(j);
                    boolean statsFound = false;
                    for (int k = 0; k < graphicalNode.getElementParameters().size() && !statsFound; k++) {
                        IElementParameter currentParam = graphicalNode.getElementParameters().get(k);
                        if (currentParam.getName().equals(EParameterName.TSTATCATCHER_STATS.getName())) {
                            param.setValue(currentParam.getValue());
                            statsFound = true;
                        }
                    }
                    targetFound = true;
                }
            }
        }

        // set the specific parameters value.
        for (IMultipleComponentParameter param : multipleComponentManager.getParamList()) {
            INode sourceNode = null;
            boolean sourceFound = false;
            targetFound = false;
            for (int i = 0; i < itemList.size() && !targetFound; i++) {
                if (itemList.get(i).getName().equals(param.getTargetComponent())) {
                    targetNode = itemsMap.get(itemList.get(i));
                    targetFound = true;
                }
            }
            if (param.getSourceComponent() != null) { // target.value = source.value
                if (param.getSourceComponent().equals("self")) { //$NON-NLS-1$
                    sourceNode = graphicalNode;
                } else {
                    for (int i = 0; i < itemList.size() && !sourceFound; i++) {
                        if (itemList.get(i).getName().equals(param.getSourceComponent())) {
                            sourceNode = itemsMap.get(itemList.get(i));
                            sourceFound = true;
                        }
                    }
                }
                if ((sourceNode != null) && (targetNode != null)) {
                    sourceFound = false;
                    targetFound = false;
                    IElementParameter paramSource = null, paramTarget = null;
                    for (int i = 0; i < sourceNode.getElementParameters().size() && !sourceFound; i++) {
                        if (sourceNode.getElementParameters().get(i).getName().equals(param.getSourceValue())) {
                            paramSource = sourceNode.getElementParameters().get(i);
                            sourceFound = true;
                        }
                    }

                    for (int i = 0; i < targetNode.getElementParameters().size() && !targetFound; i++) {
                        if (targetNode.getElementParameters().get(i).getName().equals(param.getTargetValue())) {
                            paramTarget = targetNode.getElementParameters().get(i);
                            targetFound = true;
                        }
                    }
                    if ((paramSource != null) && (paramTarget != null)) {
                        paramTarget.setDefaultClosedListValue(paramSource.getDefaultClosedListValue());
                        paramTarget.setListItemsDisplayCodeName(paramSource.getListItemsDisplayCodeName());
                        paramTarget.setListItemsValue(paramSource.getListItemsValue());

                        // adjust destination value based on the connector name.(only apply to multi-input virtual
                        // component)
                        if (multipleComponentManager.isSetConnector() && param.getSourceComponent().equals("self")
                                && param.getSourceValue().equals("UNIQUE_NAME") && param.getTargetValue().equals("DESTINATION")) {
                            paramTarget.setValue(paramSource.getValue() + multipleComponentManager.getConnector());
                        } else {
                            paramTarget.setValue(paramSource.getValue());
                        }
                    }
                    if ((paramSource == null) && (paramTarget != null)) {
                        // set connection name to paramTarget
                        if (param.getSourceValue().endsWith(":CONNECTION")) {
                            paramTarget.setValue(sourceNode.getElementParameter(param.getSourceValue()).getValue());
                        }
                    }
                }
            } else {
                if (param.getSourceValue() != null && targetFound) { // target.value = value
                    targetFound = false;
                    IElementParameter paramTarget = null;
                    for (int i = 0; i < targetNode.getElementParameters().size() && !targetFound; i++) {
                        if (targetNode.getElementParameters().get(i).getName().equals(param.getTargetValue())) {
                            paramTarget = targetNode.getElementParameters().get(i);
                            targetFound = true;
                        }
                    }
                    if (paramTarget != null) {
                        paramTarget.setValue(param.getSourceValue());
                    }
                }
            }
        }
    }

    @SuppressWarnings("unchecked")//$NON-NLS-1$
    public void checkFlowRefLink(final Node graphicalNode) {
        if (checkRefList.contains(graphicalNode)) {
            return;
        }
        checkRefList.add(graphicalNode);
        for (IConnection connection : graphicalNode.getOutgoingConnections()) {
            if (connection.isActivate() && connection.getLineStyle().hasConnectionCategory(IConnectionCategory.USE_HASH)) {
                INode refSource = buildCheckMap.get(graphicalNode);

                // retrieve the starts node of each current nodes to add a before link
                INode subNodeStartTarget = graphicalNode.getSubProcessStartNode(true);
                INode subNodeStartSource = ((Node) connection.getTarget()).getSubProcessStartNode(false);

                AbstractNode subDataNodeStartSource = (AbstractNode) buildCheckMap.get(subNodeStartSource);
                AbstractNode subDataNodeStartTarget = (AbstractNode) buildCheckMap.get(subNodeStartTarget);

                if (subDataNodeStartSource == null) {
                    // means the graphic process is not complete, so ignore it.
                    continue;
                }

                // if (subDataNodeStartSource.getMetadataList().isEmpty()) {
                // continue;
                // }

                // create a link before between the two subprocess
                DataConnection dataConnec = new DataConnection();
                dataConnec.setActivate(connection.isActivate());
                dataConnec.setLineStyle(EConnectionType.RUN_AFTER);
                // dataConnec.setLineStyle(EConnectionType.THEN_RUN);
                if (!subDataNodeStartSource.getMetadataList().isEmpty()) {
                    dataConnec.setMetadataTable(subDataNodeStartSource.getMetadataList().get(0));
                }
                dataConnec.setName("after_" + subDataNodeStartSource.getUniqueName()); //$NON-NLS-1$
                // dataConnec.setConnectorName(EConnectionType.THEN_RUN.getName());
                dataConnec.setConnectorName(EConnectionType.RUN_AFTER.getName());
                dataConnec.setSource(subDataNodeStartSource);
                // dataConnec.setSource(subDataNodeStartTarget);
                dataConnec.setTarget(subDataNodeStartTarget);
                // dataConnec.setTarget(subDataNodeStartSource);
                List<IConnection> outgoingConnections = (List<IConnection>) subDataNodeStartSource.getOutgoingConnections();
                outgoingConnections.add(dataConnec);
                List<IConnection> incomingConnections = (List<IConnection>) subDataNodeStartTarget.getIncomingConnections();
                incomingConnections.add(dataConnec);

                // add a new hash node
                // (to replace by a Node maybe that will take the informations of an IComponent)
                String uniqueName = null;
                IComponent component = null;

                String baseConnector = connection.getSource().getConnectorFromName(connection.getConnectorName()).getBaseSchema();
                INodeConnector connector = connection.getTarget().getConnectorFromName(baseConnector);
                String hashComponent = connector.getConnectionProperty(EConnectionType.FLOW_REF).getLinkedComponent();

                if (hashComponent == null) {
                    uniqueName = HASH_COMPONENT_NAME + "_" + connection.getName(); //$NON-NLS-1$
                    component = ComponentsFactoryProvider.getInstance().get(HASH_COMPONENT_NAME);
                } else {
                    uniqueName = hashComponent + "_" + connection.getName(); //$NON-NLS-1$
                    component = ComponentsFactoryProvider.getInstance().get(hashComponent);
                }
                if (component == null) {
                    continue;
                }
                DataNode hashNode = new DataNode(component, uniqueName);
                hashNode.setActivate(connection.isActivate());
                hashNode.setStart(false);
                hashNode.setDesignSubjobStartNode(graphicalNode.getDesignSubjobStartNode());
                IMetadataTable newMetadata = connection.getMetadataTable().clone();
                newMetadata.setTableName(uniqueName);
                hashNode.getMetadataList().remove(0);
                hashNode.getMetadataList().add(newMetadata);
                hashNode.setSubProcessStart(false);
                hashNode.setProcess(graphicalNode.getProcess());
                outgoingConnections = new ArrayList<IConnection>();
                incomingConnections = new ArrayList<IConnection>();
                hashNode.setIncomingConnections(incomingConnections);
                hashNode.setOutgoingConnections(outgoingConnections);

                dataNodeList.add(hashNode);

                // create a link flow_main between the node that had ref and the hash file
                dataConnec = new DataConnection();
                dataConnec.setActivate(connection.isActivate());
                dataConnec.setLineStyle(EConnectionType.FLOW_MAIN);
                dataConnec.setMetadataTable(newMetadata);
                dataConnec.setName(connection.getName());
                // dataConnec.setName(refSource.getUniqueName() + "_to_hash_" + connection.getName());
                dataConnec.setSource(refSource);
                dataConnec.setTarget(hashNode);

                IElementParameter monitorParam = connection.getElementParameter("MONITOR_CONNECTION");
                // if there is a monitor on this connection, then add the vFlowMeter and move the base lookup connection
                // source from "graphicalNode" to the new meterNode.
                if (monitorParam != null && ((Boolean) monitorParam.getValue())) {
                    INode meterNode = addvFlowMeterBetween(refSource, hashNode, dataConnec, graphicalNode.getProcess(),
                            connection.getElementParameters());
                    // move the FLOW_REF link from "refSource" to the "meterNode".
                    List<IConnection> connectionRefList = (List<IConnection>) refSource
                            .getOutgoingConnections(EConnectionType.FLOW_REF);
                    IConnection connecToMove = null;
                    for (IConnection curConnection : connectionRefList) {
                        if (curConnection.getSource().equals(buildCheckMap.get(connection.getSource()))
                                && curConnection.getTarget().equals(buildCheckMap.get(connection.getTarget()))) {
                            connecToMove = curConnection;
                        }
                    }
                    if (connecToMove != null) {
                        ((List<IConnection>) refSource.getOutgoingConnections()).remove(connecToMove);
                        ((List<IConnection>) meterNode.getOutgoingConnections()).add(connecToMove);
                        ((DataConnection) connecToMove).setSource(meterNode);
                        ((DataConnection) connecToMove).setName(connecToMove.getName());
                        ((DataConnection) connecToMove).setUniqueName("meterRow" + connecToMove.getUniqueName());
                    }
                } else {
                    outgoingConnections = (List<IConnection>) refSource.getOutgoingConnections();
                    outgoingConnections.add(dataConnec);
                    incomingConnections = (List<IConnection>) hashNode.getIncomingConnections();
                    incomingConnections.add(dataConnec);
                }
            }
            if (connection.getTarget() instanceof Node) {
                checkFlowRefLink((Node) connection.getTarget());
            }
        }
    }

    /**
     * DOC nrousseau Comment method "replaceMultipleComponents".
     * 
     * @param node
     */
    private void replaceMultipleComponents(INode graphicalNode) {
        if (checkMultipleMap.containsKey(graphicalNode)) {
            // return checkMultipleMap.get(graphicalNode);
            return;
        }
        // if the original component is in dummy state, no need to replace it
        if ((graphicalNode instanceof Node) && ((Node) graphicalNode).isDummy() && !graphicalNode.isActivate()) {
            return;
        }
        AbstractNode dataNode;

        List<IMultipleComponentManager> multipleComponentManagers = graphicalNode.getComponent().getMultipleComponentManagers();

        dataNode = (AbstractNode) buildCheckMap.get(graphicalNode);
        checkMultipleMap.put(graphicalNode, dataNode);
        if (multipleComponentManagers.size() > 0) {
            // dataNode = addMultipleNode(graphicalNode, multipleComponentManagers);
            addMultipleNode(graphicalNode, multipleComponentManagers);
        }
        for (IConnection connection : graphicalNode.getOutgoingConnections()) {
            if (connection.isActivate()) {
                replaceMultipleComponents(connection.getTarget());
            }
        }
        // return dataNode;
    }

    public void buildFromGraphicalProcess(List<Node> graphicalNodeList) {
        initialize();
        if (graphicalNodeList.size() == 0) {
            return;
        }
        List<Node> newGraphicalNodeList = buildCopyOfGraphicalNodeList(graphicalNodeList);

        replaceNodeFromProviders(newGraphicalNodeList);

        // job settings extra (feature 2710)
        if (JobSettingsManager.isImplicittContextLoadActived(duplicatedProcess)) {
            List<DataNode> contextLoadNodes = JobSettingsManager.createExtraContextLoadNodes(duplicatedProcess);
            for (DataNode node : contextLoadNodes) {
                buildCheckMap.put(node, node);
                dataNodeList.add(node);
                replaceMultipleComponents(node);
            }
        }

        for (Node node : newGraphicalNodeList) {
            if (node.isSubProcessStart() && node.isActivate()) {
                buildDataNodeFromNode(node);
            }
        }
        for (Node node : newGraphicalNodeList) {
            if (node.isSubProcessStart() && node.isActivate()) {
                checkFlowRefLink(node);
            }
        }
        for (Node node : newGraphicalNodeList) {
            if (node.isSubProcessStart() && node.isActivate()) {
                replaceMultipleComponents(node);
            }
        }

        // change the design subjob start as the value stored while building process is the graphical node
        for (INode dataNode : dataNodeList) {
            if (dataNode instanceof AbstractNode) {
                INode graphicalNode = ((AbstractNode) dataNode).getDesignSubjobStartNode();
                ((AbstractNode) dataNode).setDesignSubjobStartNode(buildCheckMap.get(graphicalNode));
            }
        }

        // job settings stats & logs
        if (JobSettingsManager.isStatsAndLogsActivated(duplicatedProcess)) {
            // will add the Stats & Logs managements
            Boolean realTimeStats = ((Boolean) duplicatedProcess.getElementParameter(
                    EParameterName.CATCH_REALTIME_STATS.getName()).getValue())
                    && duplicatedProcess.getElementParameter(EParameterName.CATCH_REALTIME_STATS.getName()).isShow(
                            duplicatedProcess.getElementParameters());

            if (!realTimeStats) {
                for (INode node : dataNodeList) {
                    IElementParameter param = node.getElementParameter(EParameterName.TSTATCATCHER_STATS.getName());
                    if (param != null) {
                        param.setValue(Boolean.FALSE);
                    }
                }
            }

            List<DataNode> statsAndLogsNodeList = JobSettingsManager.createStatsAndLogsNodes(duplicatedProcess);

            for (DataNode node : statsAndLogsNodeList) {
                buildCheckMap.put(node, node);
                dataNodeList.add(node);
                replaceMultipleComponents(node);
            }
        }

        // calculate the merge info for every node
        for (INode node : dataNodeList) {
            int mergeOrder = duplicatedProcess.getMergelinkOrder(node);
            if (mergeOrder >= 1) {
                ((AbstractNode) node).setThereLinkWithMerge(true);
                ((AbstractNode) node).setLinkedMergeInfo(duplicatedProcess.getLinkedMergeInfo(node));
            }
        }
        checkRefList = null;
        checkMultipleMap = null;
        buildCheckMap = null;
    }

    public INode buildNodeFromNode(final Node graphicalNode, final Process process) {
        if (buildCheckMap.containsKey(graphicalNode)) {
            return buildCheckMap.get(graphicalNode);
        }

        Node newGraphicalNode = new Node(graphicalNode.getComponent(), process);
        newGraphicalNode.setMetadataList(graphicalNode.getMetadataList());
        if (graphicalNode.getExternalData() != null) {
            Data data = graphicalNode.getExternalBytesData();
            newGraphicalNode.setData(data.getBytesData(), data.getStringData());
        }
        copyElementParametersValue(graphicalNode, newGraphicalNode);
        newGraphicalNode.setDummy(graphicalNode.isDummy());

        process.addNodeContainer(new NodeContainer(newGraphicalNode));
        buildCheckMap.put(graphicalNode, newGraphicalNode);

        // List<Connection> outgoingConnections = new ArrayList<Connection>();
        // List<Connection> incomingConnections = new ArrayList<Connection>();
        // newGraphicalNode.setIncomingConnections(incomingConnections);
        // newGraphicalNode.setOutgoingConnections(outgoingConnections);

        Connection dataConnec;
        for (Connection connection : (List<Connection>) graphicalNode.getOutgoingConnections()) {
            if (!connection.isActivate()) {
                continue;
            }
            INode target = buildNodeFromNode((Node) connection.getTarget(), process);

            dataConnec = new Connection(newGraphicalNode, (Node) target, connection.getLineStyle(),
                    connection.getConnectorName(), connection.getMetaName(), connection.getName(), connection.getUniqueName());
            // incomingConnections = (List<Connection>) target.getIncomingConnections();
            // if (incomingConnections == null) {
            // incomingConnections = new ArrayList<Connection>();
            // }
            // outgoingConnections.add(dataConnec);
            // incomingConnections.add(dataConnec);
            copyElementParametersValue(connection, dataConnec);
        }
        newGraphicalNode.setActivate(graphicalNode.isActivate());

        return newGraphicalNode;
    }

    /**
     * DOC nrousseau Comment method "buildCopyOfGraphicalNodeList".
     * 
     * @param graphicalNodeList
     * @return
     */
    private List<Node> buildCopyOfGraphicalNodeList(List<Node> graphicalNodeList) {
        if (graphicalNodeList.size() == 0) {
            return Collections.EMPTY_LIST;
        }

        duplicatedProcess = new Process(process.getProperty());
        duplicatedProcess.setActivate(false);
        duplicatedProcess.setEditor(process.getEditor());
        duplicatedProcess.setGeneratingProcess(this);
        duplicatedProcess.setProcessModified(false);

        copyElementParametersValue(graphicalNodeList.get(0).getProcess(), duplicatedProcess);

        // keep the same instance of context manager as it won't be modified
        duplicatedProcess.setContextManager(process.getContextManager());
        for (Node node : graphicalNodeList) {
            if (node.isSubProcessStart() && node.isActivate()) {
                buildNodeFromNode(node, duplicatedProcess);
            }
        }

        List<Node> newBuildNodeList = new ArrayList<Node>();

        for (INode gnode : graphicalNodeList) {
            INode newNode = buildCheckMap.get(gnode);
            if (newNode != null) {
                newBuildNodeList.add((Node) newNode);
            }

        }
        for (Node node : newBuildNodeList) {
            if (node.isExternalNode()) {
                node.getExternalNode().initialize();
            }
        }
        duplicatedProcess.setActivate(true);
        duplicatedProcess.checkStartNodes();
        buildCheckMap.clear();
        return newBuildNodeList;
    }

    /**
     * DOC qzhang Comment method "replaceJoblets".
     * 
     * @param graphicalNodeList
     * @return
     */
    private void replaceNodeFromProviders(List<Node> graphicalNodeList) {
        List<Node> orginalList = new ArrayList<Node>(graphicalNodeList);
        for (Node node : orginalList) {
            IComponent component = node.getComponent();
            AbstractProcessProvider processProvider = AbstractProcessProvider.findProcessProviderFromPID(component
                    .getPluginFullName());
            if (processProvider != null) {
                processProvider.rebuildGraphicProcessFromNode(node, graphicalNodeList);
            }
        }
    }

    public List<INode> getNodeList() {
        return dataNodeList;
    }

    /**
     * Getter for duplicatedProcess.
     * 
     * @return the duplicatedProcess
     */
    public Process getDuplicatedProcess() {
        return this.duplicatedProcess;
    }

}
