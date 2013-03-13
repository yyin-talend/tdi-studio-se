// ============================================================================
//
// Copyright (C) 2006-2013 Talend Inc. - www.talend.com
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

import org.eclipse.gef.commands.Command;
import org.talend.core.model.metadata.IMetadataColumn;
import org.talend.core.model.metadata.IMetadataTable;
import org.talend.core.model.process.EConnectionType;
import org.talend.core.model.process.EParameterFieldType;
import org.talend.core.model.process.IConnection;
import org.talend.core.model.process.IConnectionCategory;
import org.talend.core.model.process.IElementParameter;
import org.talend.core.model.process.INode;
import org.talend.designer.core.model.components.EParameterName;
import org.talend.designer.core.model.components.ElementParameter;
import org.talend.designer.core.ui.editor.nodes.Node;

public class SetParallelizationCommand extends Command {

    INode node;

    public SetParallelizationCommand(INode node) {
        this.node = node;
    }

    public SetParallelizationCommand(String label) {
        super(label);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void execute() {
        setParallelization(this.node);
    }

    private boolean isComponentCanParlization(IConnection parConnection, Node needToPar) {

        // TODO:Temply fix,later need a parameter in components to judge this node can be paralization or not
        if (needToPar.getComponent().getName().contains("tMatchGroup") || needToPar.getComponent().getName().contains("tSortRow")) { // ||
            return true;
        } else if (needToPar.getComponent().getName().contains("tAggregate")) {
            return false;
        }
        // some components have no field table but can be paralization such as tMap
        for (IConnection outCon : needToPar.getOutgoingConnections()) {
            if (outCon.getLineStyle().hasConnectionCategory(IConnectionCategory.MAIN)
                    || outCon.getLineStyle().hasConnectionCategory(IConnectionCategory.MERGE)) {
                IMetadataTable metaTable = outCon.getMetadataTable();
                if (parConnection.getMetadataTable().sameMetadataAs(metaTable)) {
                    return true;
                }
            }
        }
        return false;
    }

    private void setParallelization(INode node) {
        if (node.getOutgoingConnections().size() > 0) {
            for (IConnection con : node.getOutgoingConnections()) {
                EConnectionType lineStyle = con.getLineStyle();
                if (lineStyle.hasConnectionCategory(IConnectionCategory.MAIN)
                        || lineStyle.hasConnectionCategory(IConnectionCategory.MERGE)) {
                    if (isComponentCanParlization(con, (Node) con.getTarget())) {
                        if (existPreviousParCon((Node) con.getTarget())) {
                            con.getElementParameter(EParameterName.PARTITIONER.getName()).setValue(Boolean.FALSE);
                            con.getElementParameter(EParameterName.DEPARTITIONER.getName()).setValue(Boolean.FALSE);
                            con.setPropertyValue(EParameterName.NONE.getName(), Boolean.TRUE);
                            node = con.getTarget();
                            setParallelization(node);
                        } else {
                            // List<String> conKeyColumnList = getKeyColumnList(con.getMetadataTable());
                            con.getElementParameter(EParameterName.DEPARTITIONER.getName()).setValue(Boolean.FALSE);
                            con.getElementParameter(EParameterName.NONE.getName()).setValue(Boolean.FALSE);
                            con.setPropertyValue(EParameterName.PARTITIONER.getName(), Boolean.TRUE);

                            // set the keys for hash keys
                            setHashKeysFromCon(con);

                            if (con.getTarget() != null) {
                                setParallelization(con.getTarget());
                            }
                        }
                    } else {
                        if (!con.getSource().isStart()) {
                            if (!existPreviousDeparCon((Node) con.getTarget())) {
                                setDeparallelization(con.getTarget());
                            }
                        }
                    }
                } else {
                    node = con.getTarget();
                    setParallelization(node);
                }
            }
        } else {
            if (!node.isStart()) {
                setDeparallelization(node);
            }
        }
    }

    private void setHashKeysFromCon(IConnection con) {
        List<String> conKeyColumnList = getKeyColumnList(con.getMetadataTable());
        IElementParameter parTableCon = con.getElementParameter("HASH_KEYS");
        boolean isExistHashValue = false;
        if (parTableCon != null) {
            ((List) parTableCon.getValue()).clear();
            if (conKeyColumnList.size() > 0) {
                con.getElementParameter("HASH_PARTITION").setValue(true);

                Object[] itemCon = parTableCon.getListItemsValue();
                String clumnKeyListName = "";
                for (Object itemList : itemCon) {
                    if (((ElementParameter) itemList).getFieldType().equals(EParameterFieldType.PREV_COLUMN_LIST)
                            || ((ElementParameter) itemList).getFieldType().equals(EParameterFieldType.COLUMN_LIST)) {
                        clumnKeyListName = ((ElementParameter) itemList).getName();
                    }
                }
                for (String partionValue : conKeyColumnList) {
                    for (Object keyParMap : ((List) parTableCon.getValue())) {
                        Map existKeyMap = (Map) keyParMap;
                        if (existKeyMap.get(clumnKeyListName).equals(partionValue)) {
                            isExistHashValue = true;
                            break;
                        }
                    }
                    if (!isExistHashValue) {
                        Map partionKeyMap = new HashMap<String, String>();
                        partionKeyMap.put(clumnKeyListName, partionValue);
                        ((List) parTableCon.getValue()).add(partionKeyMap);
                    }
                }
            }
        }
    }

    private static List<String> getKeyColumnList(IMetadataTable table) {
        List<String> columnList = new ArrayList<String>();
        if (table != null) {
            for (IMetadataColumn column : table.getListColumns()) {
                if (column.isKey()) {
                    String label = column.getLabel();
                    columnList.add(label);
                }
            }
        }
        return columnList;
    }

    private boolean existPreviousParCon(Node currentNode) {
        // To judge if there has par/col on previous connection
        boolean hasParInPreviousCon = false;
        for (IConnection con : currentNode.getIncomingConnections()) {
            Node sourceNode = (Node) con.getSource();
            hasParInPreviousCon = isExistPreviousParCon(sourceNode);
        }
        return hasParInPreviousCon;
    }

    private boolean isExistPreviousParCon(Node previousNode) {
        boolean hasParInPreviousCon = false;
        if (previousNode.getIncomingConnections().size() > 0) {
            for (IConnection con : previousNode.getIncomingConnections()) {
                if (con.getElementParameter(EParameterName.PARTITIONER.getName()) != null
                        && con.getElementParameter(EParameterName.PARTITIONER.getName()).getValue().equals(true)) {
                    hasParInPreviousCon = true;
                } else {
                    hasParInPreviousCon = isExistPreviousParCon((Node) con.getSource());
                }
            }
        }
        return hasParInPreviousCon;
    }

    private boolean existPreviousDeparCon(Node currentNode) {
        // To judge if there has depar/recol on previous connection
        boolean hasDeparInPreviousCon = false;
        for (IConnection con : currentNode.getIncomingConnections()) {
            Node sourceNode = (Node) con.getSource();
            hasDeparInPreviousCon = isExistPreviouDeparCon(sourceNode);
        }
        return hasDeparInPreviousCon;
    }

    private boolean isExistPreviouDeparCon(Node previousNode) {
        boolean hasDeparInPreviousCon = false;
        if (previousNode.getIncomingConnections().size() > 0) {
            for (IConnection con : previousNode.getIncomingConnections()) {
                if (con.getElementParameter(EParameterName.DEPARTITIONER.getName()) != null
                        && con.getElementParameter(EParameterName.DEPARTITIONER.getName()).getValue().equals(true)) {
                    hasDeparInPreviousCon = true;
                } else {
                    hasDeparInPreviousCon = isExistPreviouDeparCon((Node) con.getSource());
                }
            }
        }
        return hasDeparInPreviousCon;
    }

    private void setDeparallelization(INode node) {
        for (IConnection con : node.getIncomingConnections()) {
            EConnectionType lineStyle = con.getLineStyle();
            if (lineStyle.hasConnectionCategory(IConnectionCategory.MAIN)
                    || lineStyle.hasConnectionCategory(IConnectionCategory.MERGE)) {
                con.getElementParameter(EParameterName.PARTITIONER.getName()).setValue(Boolean.FALSE);
                con.getElementParameter(EParameterName.NONE.getName()).setValue(Boolean.FALSE);
                con.setPropertyValue(EParameterName.DEPARTITIONER.getName(), Boolean.TRUE);
            }
        }
        if (node.getOutgoingConnections().size() > 0) {
            setParallelization(node);
        }
    }

}
