package org.talend.designer.core.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.talend.core.model.metadata.IMetadataColumn;
import org.talend.core.model.metadata.IMetadataTable;
import org.talend.core.model.process.EParameterFieldType;
import org.talend.core.model.process.IConnection;
import org.talend.core.model.process.IConnectionCategory;
import org.talend.core.model.process.IElementParameter;
import org.talend.core.model.process.INode;
import org.talend.designer.core.model.components.EParameterName;
import org.talend.designer.core.model.components.ElementParameter;
import org.talend.designer.core.ui.editor.nodes.Node;

public class ParallelExecutionUtils {

    private static final String HASH_KEYS = "HASH_KEYS";

    public static boolean compareKeyPartions(IConnection parConnection, Node needToPar) {
        String partitioning = needToPar.getComponent().getPartitioning();
        boolean isSame = false;
        String[] partitionKey = partitioning.split("\\.");
        IElementParameter parTableCon = parConnection.getElementParameter(HASH_KEYS);
        IElementParameter parTableNode = needToPar.getElementParameter(partitionKey[0]);
        if (parTableNode != null) {
            String clumnKeyListName = "KEY_COLUMN";// for the partition key
            String clumnNodeListName = partitionKey[1];

            List<String> parKeyValues = new ArrayList<String>();
            List<String> columnKeyValues = new ArrayList<String>();

            ElementParameter nodeElemForList = null;
            for (Map conColumnListMap : (List<Map>) parTableCon.getValue()) {
                if (conColumnListMap.get(clumnKeyListName) instanceof String) {
                    parKeyValues.add((String) conColumnListMap.get(clumnKeyListName));
                }
            }

            for (Object nodeItemList : parTableNode.getListItemsValue()) {
                if (((ElementParameter) nodeItemList).getFieldType().equals(EParameterFieldType.PREV_COLUMN_LIST)
                        || ((ElementParameter) nodeItemList).getFieldType().equals(EParameterFieldType.COLUMN_LIST)) {
                    nodeElemForList = (ElementParameter) nodeItemList;
                    break;
                }
            }
            if (nodeElemForList != null) {
                for (Map nodeColumnListMap : (List<Map>) parTableNode.getValue()) {
                    Object value = nodeColumnListMap.get(clumnNodeListName);
                    if (nodeColumnListMap.get(clumnNodeListName) instanceof String) {
                        columnKeyValues.add((String) value);
                    } else if (value instanceof Integer) {
                        Integer index = (Integer) value;
                        if (nodeElemForList.getListItemsDisplayName().length > index) {
                            columnKeyValues.add((String) nodeElemForList.getListItemsDisplayName()[index]);
                        }
                    }
                }
            }
            if (columnKeyValues.size() > 0) {
                if (columnKeyValues.equals(parKeyValues)) {
                    isSame = true;
                } else {
                    isSame = false;
                }
            }
        }
        return isSame;
    }

    public static INode getFirstPartionerTargetNode(IConnection con) {
        INode targetNode = null;
        if (con.getTarget() != null) {
            String partitioning = con.getTarget().getComponent().getPartitioning();
            if (!(partitioning.equals("NONE"))) {
                targetNode = con.getTarget();
            } else {
                for (IConnection nextCon : con.getTarget().getOutgoingConnections()) {
                    if (nextCon.getLineStyle().hasConnectionCategory(IConnectionCategory.MAIN)
                            || nextCon.getLineStyle().hasConnectionCategory(IConnectionCategory.MERGE)) {
                        return getFirstPartionerTargetNode(nextCon);
                    }
                }
            }
        }
        return targetNode;
    }

    public static void setHashKeysFromTarget(IConnection con, Node target) {
        List<String> targetKeyColumnList = getColumnListFromTargetNode(target);
        IElementParameter parTableCon = con.getElementParameter(HASH_KEYS);
        boolean isExistHashValue = false;
        if (parTableCon != null) {
            ((List) parTableCon.getValue()).clear();
            if (targetKeyColumnList.size() > 0) {
                con.getElementParameter("HASH_PARTITION").setValue(true);

                Object[] itemCon = parTableCon.getListItemsValue();
                String clumnKeyListName = "";
                for (Object itemList : itemCon) {
                    if (((ElementParameter) itemList).getFieldType().equals(EParameterFieldType.PREV_COLUMN_LIST)
                            || ((ElementParameter) itemList).getFieldType().equals(EParameterFieldType.COLUMN_LIST)) {
                        clumnKeyListName = ((ElementParameter) itemList).getName();
                        break;
                    }
                }
                for (String partionValue : targetKeyColumnList) {
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

    public static List<String> getColumnListFromTargetNode(Node target) {
        List<String> columnKeyValues = new ArrayList<String>();
        IElementParameter parTableNode = target.getElementParameterFromField(EParameterFieldType.TABLE);
        if (parTableNode != null) {
            Object[] itemNode = parTableNode.getListItemsValue();
            ElementParameter clumnNodeList = null;
            for (Object itemList : itemNode) {
                if (((ElementParameter) itemList).getFieldType().equals(EParameterFieldType.PREV_COLUMN_LIST)
                        || ((ElementParameter) itemList).getFieldType().equals(EParameterFieldType.COLUMN_LIST)) {
                    clumnNodeList = ((ElementParameter) itemList);
                    break;
                }
            }
            if (clumnNodeList != null) {
                for (Map nodeColumnListMap : (List<Map>) parTableNode.getValue()) {
                    Object value = nodeColumnListMap.get(clumnNodeList.getName());
                    if (nodeColumnListMap.get(clumnNodeList.getName()) instanceof String) {
                        columnKeyValues.add((String) value);
                    } else if (value instanceof Integer) {
                        Integer index = (Integer) value;
                        if (clumnNodeList.getListItemsDisplayName().length > index) {
                            columnKeyValues.add((String) clumnNodeList.getListItemsDisplayName()[index]);
                        }
                    }
                }
            }
        }
        return columnKeyValues;
    }

    public static void reSetParKeyValuesForCon(IConnection parConnection) {
        IElementParameter parTableCon = parConnection.getElementParameter(HASH_KEYS);
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
            if (conElemForList != null) {
                for (Map conColumnListMap : (List<Map>) parTableCon.getValue()) {
                    if (conColumnListMap.get(clumnKeyListName) instanceof String) {
                        parKeyValues.add((String) conColumnListMap.get(clumnKeyListName));
                    }
                    if (conColumnListMap.get(clumnKeyListName) instanceof Integer) {
                        Integer index = (Integer) conColumnListMap.get(clumnKeyListName);
                        parKeyValues.add((String) conElemForList.getListItemsValue()[index]);
                        // if the value of key is Integer index,need to set it back to the value
                        if (conElemForList.getListItemsDisplayName().length > index) {
                            conColumnListMap.put(clumnKeyListName, conElemForList.getListItemsDisplayName()[index]);
                        }
                    }
                }
            }
        }
    }

    public static boolean isPartitionKeysExist(IConnection con) {
        IElementParameter parTableCon = con.getElementParameter(HASH_KEYS);
        if (parTableCon != null) {
            Object[] itemParKey = parTableCon.getListItemsValue();
            if (((List<Map>) parTableCon.getValue()).size() > 0) {
                return true;
            }
        }
        return false;
    }

    public static void setHashKeysForCon(IConnection con) {
        List<String> conKeyColumnList = getKeyColumnList(con.getMetadataTable());
        IElementParameter parTableCon = con.getElementParameter(HASH_KEYS);
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
                        break;
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

    public static List<String> getKeyColumnList(IMetadataTable table) {
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

    public static boolean existPreviousPar(Node currentNode) {
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

    public static boolean existPreviousDepar(Node currentNode) {
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

    public static boolean existNextDepar(Node currentNode) {
        // To judge if there has par/col on previous connection
        boolean hasNextCon = false;
        for (IConnection con : currentNode.getOutgoingConnections()) {
            if ((con.getElementParameter(EParameterName.DEPARTITIONER.getName()) != null && con
                    .getElementParameter(EParameterName.DEPARTITIONER.getName()).getValue().equals(true))) {
                hasNextCon = true;
            }
        }
        return hasNextCon;
    }

    public static boolean existPreviousRepar(Node currentNode) {
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

    public static boolean existPreviousNone(Node currentNode) {
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

    public static IConnection getPreviousParCon(Node previousNode) {
        IConnection previousCon = null;
        if (previousNode.getIncomingConnections().size() > 0) {
            for (IConnection con : previousNode.getIncomingConnections()) {
                if ((con.getElementParameter(EParameterName.PARTITIONER.getName()) != null && con
                        .getElementParameter(EParameterName.PARTITIONER.getName()).getValue().equals(true))
                        || (con.getElementParameter(EParameterName.REPARTITIONER.getName()) != null && con
                                .getElementParameter(EParameterName.REPARTITIONER.getName()).getValue().equals(true))) {
                    previousCon = con;
                } else {
                    previousCon = getPreviousParCon((Node) con.getSource());
                }
            }
        }
        return previousCon;
    }

    public static boolean isExistPreviousParCon(Node previousNode) {
        boolean hasParInPreviousCon = false;
        if (previousNode.getIncomingConnections().size() > 0) {
            for (IConnection con : previousNode.getIncomingConnections()) {
                if (con.getElementParameter(EParameterName.PARTITIONER.getName()) != null
                        && con.getElementParameter(EParameterName.PARTITIONER.getName()).getValue().equals(true)
                        || con.getElementParameter(EParameterName.REPARTITIONER.getName()) != null
                        && con.getElementParameter(EParameterName.REPARTITIONER.getName()).getValue().equals(true)) {
                    hasParInPreviousCon = true;
                } else {
                    hasParInPreviousCon = isExistPreviousParCon((Node) con.getSource());
                }
            }
        }
        return hasParInPreviousCon;
    }

    public static boolean isExistPreviouDeparCon(Node previousNode) {
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
}
