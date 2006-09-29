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
package org.talend.designer.core.model.process;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.talend.core.model.components.IComponent;
import org.talend.core.model.metadata.IMetadataTable;
import org.talend.core.model.process.AbstractNode;
import org.talend.core.model.process.EConnectionType;
import org.talend.core.model.process.IConnection;
import org.talend.core.model.process.IExternalNode;
import org.talend.core.model.process.INode;
import org.talend.designer.core.ui.editor.nodes.Node;
import org.talend.repository.model.ComponentsFactoryProvider;
import org.talend.repository.model.ExternalNodesFactory;

/**
 * This class will create the list of nodes that will be used to generate the code.
 * 
 * $Id$
 * 
 */
public class DataProcess {

    private static final String HASH_COMPONENT_NAME = "tHash";

    private static Map<Node, INode> buildCheckMap = null;

    private static List<Node> checkRefList = null;

    private static List<INode> dataNodeList;

    private static void initialize() {
        buildCheckMap = new HashMap<Node, INode>();
        checkRefList = new ArrayList<Node>();
        dataNodeList = new ArrayList<INode>();
    }

    // should only be called by a starting node
    @SuppressWarnings("unchecked")
    private static INode buildfromNode(final Node graphicalNode) {
        if (buildCheckMap.containsKey(graphicalNode)) {
            return buildCheckMap.get(graphicalNode);
        }

        AbstractNode dataNode;
        if (graphicalNode.getExternalNode() == null) {
            dataNode = new DataNode();
        } else {
            // mapper
            dataNode = (AbstractNode) ExternalNodesFactory.getInstance(graphicalNode.getPluginFullName());
            Object externalData = graphicalNode.getExternalData();
            if (externalData != null) {
                ((IExternalNode) dataNode).setExternalData(externalData);
            }
        }
        buildCheckMap.put(graphicalNode, dataNode);
        dataNodeList.add(dataNode);
        dataNode.setActivate(graphicalNode.isActivate());
        dataNode.setStart(graphicalNode.isStart());
        dataNode.setComponentName(graphicalNode.getComponentName());
        dataNode.setMetadataList(graphicalNode.getMetadataList());
        dataNode.setPluginFullName(graphicalNode.getPluginFullName());
        dataNode.setElementParameters(graphicalNode.getElementParameters());
        dataNode.setUniqueName(graphicalNode.getUniqueName());
        dataNode.setSubProcessStart(graphicalNode.isSubProcessStart());
        dataNode.setMultipleMethods(graphicalNode.isMultipleMethods());
        dataNode.setProcess(graphicalNode.getProcess());
        dataNode.setComponent(graphicalNode.getComponent());

        List<IConnection> outgoingConnections = new ArrayList<IConnection>();
        List<IConnection> incomingConnections = new ArrayList<IConnection>();
        dataNode.setIncomingConnections(incomingConnections);
        dataNode.setOutgoingConnections(outgoingConnections);

        DataConnection dataConnec;
        for (IConnection connection : graphicalNode.getOutgoingConnections()) {
            dataConnec = new DataConnection();
            dataConnec.setActivate(connection.isActivate());
            dataConnec.setLineStyle(connection.getLineStyle());
            dataConnec.setMetadataTable(connection.getMetadataTable());
            dataConnec.setName(connection.getName());
            dataConnec.setSource(dataNode);
            dataConnec.setCondition(connection.getCondition());
            INode target = buildfromNode((Node) connection.getTarget());
            dataConnec.setTarget(target);
            incomingConnections = (List<IConnection>) target.getIncomingConnections();
            if (incomingConnections == null) {
                incomingConnections = new ArrayList<IConnection>();
            }
            outgoingConnections.add(dataConnec);
            incomingConnections.add(dataConnec);
        }
        return dataNode;
    }

    @SuppressWarnings("unchecked")
    private static void checkFlowRefLink(final Node graphicalNode) {
        if (checkRefList.contains(graphicalNode)) {
            return;
        }
        checkRefList.add(graphicalNode);
        for (IConnection connection : graphicalNode.getOutgoingConnections()) {
            if (connection.getLineStyle().equals(EConnectionType.FLOW_REF)) {
                INode refSource = buildCheckMap.get(graphicalNode);

                // retrieve the starts node of each current nodes to add a before link
                Node subNodeStartTarget = graphicalNode.getSubProcessStartNode();
                Node subNodeStartSource = ((Node) connection.getTarget()).getSubProcessStartNode();
                AbstractNode subDataNodeStartSource = (AbstractNode) buildCheckMap.get(subNodeStartSource);
                AbstractNode subDataNodeStartTarget = (AbstractNode) buildCheckMap.get(subNodeStartTarget);

                // create a link before between the two subprocess
                DataConnection dataConnec = new DataConnection();
                dataConnec.setActivate(connection.isActivate());
                dataConnec.setLineStyle(EConnectionType.RUN_AFTER);
                dataConnec.setMetadataTable(subDataNodeStartSource.getMetadataList().get(0));
                dataConnec.setName("after_" + subDataNodeStartSource.getUniqueName());
                dataConnec.setSource(subDataNodeStartSource);
                dataConnec.setTarget(subDataNodeStartTarget);
                List<IConnection> outgoingConnections = (List<IConnection>) subDataNodeStartSource
                        .getOutgoingConnections();
                outgoingConnections.add(dataConnec);
                List<IConnection> incomingConnections = (List<IConnection>) subDataNodeStartTarget
                        .getIncomingConnections();
                incomingConnections.add(dataConnec);

                // add a new hash node
                // (to replace by a Node maybe that will take the informations of an IComponent)
                String uniqueName = HASH_COMPONENT_NAME + "_" + connection.getName();
                IComponent component = ComponentsFactoryProvider.getInstance().get(HASH_COMPONENT_NAME);
                DataNode hashNode = new DataNode(component, uniqueName);
                hashNode.setActivate(connection.isActivate());
                hashNode.setStart(false);
                IMetadataTable newMetadata = refSource.getMetadataList().get(0).clone();
                newMetadata.setTableName(uniqueName);
                hashNode.getMetadataList().remove(0);
                hashNode.getMetadataList().add(newMetadata);
                hashNode.setSubProcessStart(false);
                hashNode.setComponent(component);
                outgoingConnections = new ArrayList<IConnection>();
                incomingConnections = new ArrayList<IConnection>();
                hashNode.setIncomingConnections(incomingConnections);
                hashNode.setOutgoingConnections(outgoingConnections);

                dataNodeList.add(hashNode);

                // create a link flow_main between the node that had ref and the hash file
                dataConnec = new DataConnection();
                dataConnec.setActivate(connection.isActivate());
                dataConnec.setLineStyle(EConnectionType.FLOW_MAIN);
                dataConnec.setMetadataTable(refSource.getMetadataList().get(0));
                dataConnec.setName(connection.getName());
                // dataConnec.setName(refSource.getUniqueName() + "_to_hash_" + connection.getName());
                dataConnec.setSource(refSource);
                dataConnec.setTarget(hashNode);
                outgoingConnections = (List<IConnection>) refSource.getOutgoingConnections();
                outgoingConnections.add(dataConnec);
                incomingConnections = (List<IConnection>) hashNode.getIncomingConnections();
                incomingConnections.add(dataConnec);
            }
            checkFlowRefLink((Node) connection.getTarget());
        }
    }

    public static void buildFromGraphicalProcess(List<Node> graphicalNodeList) {
        initialize();
        for (Node node : graphicalNodeList) {
            if (node.getIncomingConnections().size() == 0) {
                buildfromNode(node);
            }
        }
        for (Node node : graphicalNodeList) {
            if (node.getIncomingConnections().size() == 0) {
                checkFlowRefLink(node);
            }
        }
    }

    public static List<INode> getNodeList() {
        return dataNodeList;
    }
}
