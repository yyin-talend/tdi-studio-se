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
    }

    @Override
    public void execute() {
        setParallelization(this.node);
    }

    private boolean isComponentCanParlization(IConnection parConnection, Node needToPar) {
        if (needToPar.getComponent().getPartitioning().equals("NONE")) {
            return false;
        } else {
            return true;
        }
    }

    private boolean isComponentNeedRepartion(IConnection con, Node needToPar) {
        String partitioning = needToPar.getComponent().getPartitioning();
        if (partitioning.equals("AUTO")) {
            if (existPreviousPar((Node) con.getSource()) || existPreviousDepar((Node) con.getSource())
                    || existPreviousRepar((Node) con.getSource()) || existPreviousNone((Node) con.getSource())) {
                return false;
            }
            return true;
        } else {
            // compare the target node's key with the previous tPartitioner's hashKeys to see if need repartitioning.
            boolean needRepar = false;
            if (getPreviousParCon(needToPar) != null) {
                String[] partitionKey = partitioning.split("\\.");
                IElementParameter parTableCon = getPreviousParCon(needToPar).getElementParameter("HASH_KEYS");
                IElementParameter parTableNode = needToPar.getElementParameter(partitionKey[0]);
                if (parTableNode != null) {
                    ElementParameter conElemForList = null;
                    String clumnKeyListName = "KEY_COLUMN";// for the partition key
                    String clumnNodeListName = partitionKey[1];

                    List<String> parKeyValues = new ArrayList<String>();
                    List<String> columnKeyValues = new ArrayList<String>();
                    for (Map conColumnListMap : (List<Map>) parTableCon.getValue()) {
                        if (conColumnListMap.get(clumnKeyListName) instanceof String) {
                            parKeyValues.add((String) conColumnListMap.get(clumnKeyListName));
                        }
                        if (conElemForList != null && conColumnListMap.get(clumnKeyListName) instanceof Integer) {
                            Integer index = (Integer) conColumnListMap.get(clumnKeyListName);
                            parKeyValues.add((String) conElemForList.getListItemsValue()[index]);
                        }
                    }
                    for (Map nodeColumnListMap : (List<Map>) parTableNode.getValue()) {
                        if ((String) nodeColumnListMap.get(clumnNodeListName) != null) {
                            columnKeyValues.add((String) nodeColumnListMap.get(clumnNodeListName));
                        }
                    }
                    if (columnKeyValues.size() > 0) {
                        if (columnKeyValues.equals(parKeyValues)) {
                            needRepar = false;
                        } else {
                            needRepar = true;
                        }
                    }
                }
            }
            return needRepar;
        }
    }

    private void setParallelization(INode node) {
        if (node.getOutgoingConnections().size() > 0) {
            for (IConnection con : node.getOutgoingConnections()) {
                EConnectionType lineStyle = con.getLineStyle();
                if (lineStyle.hasConnectionCategory(IConnectionCategory.MAIN)
                        || lineStyle.hasConnectionCategory(IConnectionCategory.MERGE)) {
                    boolean isEndRow = con.getTarget().getOutgoingConnections().size() == 0;
                    boolean isStartRow = node.isStart();
                    if (isPartitionKeysExist(con)) {
                        reSetParKeyValuesForCon(con);
                    }
                    if (!isEndRow && isComponentCanParlization(con, (Node) con.getTarget())) {
                        // For those component support tPartitioner,but its keys not same as previous tPartitioner,need
                        // do Repartitioner automaticlly
                        if (!isStartRow && isComponentNeedRepartion(con, (Node) con.getTarget())) {
                            con.getElementParameter(EParameterName.NONE.getName()).setValue(Boolean.FALSE);
                            con.getElementParameter(EParameterName.PARTITIONER.getName()).setValue(Boolean.FALSE);
                            con.getElementParameter(EParameterName.DEPARTITIONER.getName()).setValue(Boolean.FALSE);
                            con.setPropertyValue(EParameterName.REPARTITIONER.getName(), Boolean.TRUE);

                            // set the keys for hash keys
                            setHashKeysFromTarget(con, (Node) con.getTarget());
                            setParallelization(con.getTarget());

                        } else {
                            // when pervious con is par/repar,keep current is none
                            if (existPreviousPar((Node) con.getSource()) || existPreviousNone((Node) con.getSource())
                                    || existPreviousRepar((Node) con.getSource())) {
                                con.getElementParameter(EParameterName.REPARTITIONER.getName()).setValue(Boolean.FALSE);
                                con.getElementParameter(EParameterName.PARTITIONER.getName()).setValue(Boolean.FALSE);
                                con.getElementParameter(EParameterName.DEPARTITIONER.getName()).setValue(Boolean.FALSE);
                                con.setPropertyValue(EParameterName.NONE.getName(), Boolean.TRUE);
                                setParallelization(con.getTarget());
                            } else {
                                IElementParameter deparElem = con.getElementParameter(EParameterName.DEPARTITIONER.getName());
                                deparElem.setValue(Boolean.FALSE);
                                con.getElementParameter(EParameterName.REPARTITIONER.getName()).setValue(Boolean.FALSE);
                                con.getElementParameter(EParameterName.NONE.getName()).setValue(Boolean.FALSE);
                                con.setPropertyValue(EParameterName.PARTITIONER.getName(), Boolean.TRUE);

                                // the first flow,should not show departitioner row.
                                if (con.getSource().isStart()) {
                                    deparElem.setShow(false);
                                }
                                // set the keys for hash keys
                                setHashKeysForCon(con);

                                if (con.getTarget() != null) {
                                    setParallelization(con.getTarget());
                                }
                            }
                        }
                    } else {
                        if (!con.getSource().isStart()) {
                            if (!existPreviousDepar((Node) con.getSource())) {
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

    private boolean isPartitionKeysExist(IConnection con) {
        IElementParameter parTableCon = con.getElementParameter("HASH_KEYS");
        if (parTableCon != null) {
            Object[] itemParKey = parTableCon.getListItemsValue();
            if (((List<Map>) parTableCon.getValue()).size() > 0) {
                return true;
            }
        }
        return false;
    }

    private void reSetParKeyValuesForCon(IConnection parConnection) {
        IElementParameter parTableCon = parConnection.getElementParameter("HASH_KEYS");
        if (parTableCon != null) {
            Object[] itemCon = parTableCon.getListItemsValue();
            ElementParameter conElemForList = null;
            String clumnKeyListName = "";
            for (Object itemList : itemCon) {
                if (((ElementParameter) itemList).getFieldType().equals(EParameterFieldType.PREV_COLUMN_LIST)
                        || ((ElementParameter) itemList).getFieldType().equals(EParameterFieldType.COLUMN_LIST)) {
                    conElemForList = (ElementParameter) itemList;
                    clumnKeyListName = ((ElementParameter) itemList).getName();
                }
            }
            List<String> parKeyValues = new ArrayList<String>();
            for (Map conColumnListMap : (List<Map>) parTableCon.getValue()) {
                if (conColumnListMap.get(clumnKeyListName) instanceof String) {
                    parKeyValues.add((String) conColumnListMap.get(clumnKeyListName));
                }
                if (conColumnListMap.get(clumnKeyListName) instanceof Integer) {
                    Integer index = (Integer) conColumnListMap.get(clumnKeyListName);
                    parKeyValues.add((String) conElemForList.getListItemsValue()[index]);
                    // if the value of key is Integer index,need to set it back to the value
                    conColumnListMap.put(clumnKeyListName, conElemForList.getListItemsValue()[index]);
                }
            }
        }
    }

    private void setHashKeysForCon(IConnection con) {
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

    private void setHashKeysFromTarget(IConnection con, Node target) {
        List<String> conKeyColumnList = getColumnListFromTargetNode(target);
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

    private static List<String> getColumnListFromTargetNode(Node target) {
        List<String> columnKeyValues = new ArrayList<String>();
        IElementParameter parTableNode = target.getElementParameterFromField(EParameterFieldType.TABLE);
        Object[] itemNode = parTableNode.getListItemsValue();
        String clumnNodeListName = "";
        for (Object itemList : itemNode) {
            if (((ElementParameter) itemList).getFieldType().equals(EParameterFieldType.PREV_COLUMN_LIST)
                    || ((ElementParameter) itemList).getFieldType().equals(EParameterFieldType.COLUMN_LIST)) {
                clumnNodeListName = ((ElementParameter) itemList).getName();
            }
        }

        for (Map nodeColumnListMap : (List<Map>) parTableNode.getValue()) {
            columnKeyValues.add((String) nodeColumnListMap.get(clumnNodeListName));
        }
        return columnKeyValues;
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

    private boolean existPreviousPar(Node currentNode) {
        // To judge if there has par/col on previous connection
        boolean hasInPreviousCon = false;
        for (IConnection con : currentNode.getIncomingConnections()) {
            if ((con.getElementParameter(EParameterName.PARTITIONER.getName()) != null && con
                    .getElementParameter(EParameterName.PARTITIONER.getName()).getValue().equals(true))) {
                hasInPreviousCon = true;
            }
        }
        return hasInPreviousCon;
    }

    private boolean existPreviousDepar(Node currentNode) {
        // To judge if there has par/col on previous connection
        boolean hasInPreviousCon = false;
        for (IConnection con : currentNode.getIncomingConnections()) {
            if ((con.getElementParameter(EParameterName.DEPARTITIONER.getName()) != null && con
                    .getElementParameter(EParameterName.DEPARTITIONER.getName()).getValue().equals(true))) {
                hasInPreviousCon = true;
            }
        }
        return hasInPreviousCon;
    }

    private boolean existPreviousRepar(Node currentNode) {
        // To judge if there has par/col on previous connection
        boolean hasInPreviousCon = false;
        for (IConnection con : currentNode.getIncomingConnections()) {
            if ((con.getElementParameter(EParameterName.REPARTITIONER.getName()) != null && con
                    .getElementParameter(EParameterName.REPARTITIONER.getName()).getValue().equals(true))) {
                hasInPreviousCon = true;
            }
        }
        return hasInPreviousCon;
    }

    private boolean existPreviousNone(Node currentNode) {
        // To judge if there has par/col on previous connection
        boolean hasInPreviousCon = false;
        for (IConnection con : currentNode.getIncomingConnections()) {
            if ((con.getElementParameter(EParameterName.NONE.getName()) != null && con
                    .getElementParameter(EParameterName.NONE.getName()).getValue().equals(true))) {
                hasInPreviousCon = true;
            }
        }
        return hasInPreviousCon;
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

    private IConnection getPreviousParCon(Node previousNode) {
        IConnection previousCon = null;
        if (previousNode.getIncomingConnections().size() > 0) {
            for (IConnection con : previousNode.getIncomingConnections()) {
                if (con.getElementParameter(EParameterName.PARTITIONER.getName()) != null
                        && con.getElementParameter(EParameterName.PARTITIONER.getName()).getValue().equals(true)) {
                    previousCon = con;
                } else {
                    previousCon = getPreviousParCon((Node) con.getSource());
                }
            }
        }
        return previousCon;
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
                IElementParameter parElem = con.getElementParameter(EParameterName.PARTITIONER.getName());
                parElem.setValue(Boolean.FALSE);
                con.getElementParameter(EParameterName.REPARTITIONER.getName()).setValue(Boolean.FALSE);
                con.getElementParameter(EParameterName.NONE.getName()).setValue(Boolean.FALSE);
                con.setPropertyValue(EParameterName.DEPARTITIONER.getName(), Boolean.TRUE);

                // the last flow,should not show partitioner row.
                if (node.getOutgoingConnections().size() < 0) {
                    parElem.setShow(false);
                }
            }
        }
        if (node.getOutgoingConnections().size() > 0) {
            setParallelization(node);
        }
    }
}
