// ============================================================================
//
// Copyright (C) 2006-2016 Talend Inc. - www.talend.com
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
import java.util.List;
import java.util.Map;

import org.eclipse.gef.commands.Command;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;
import org.talend.core.model.process.EConnectionType;
import org.talend.core.model.process.EParameterFieldType;
import org.talend.core.model.process.IConnection;
import org.talend.core.model.process.IConnectionCategory;
import org.talend.core.model.process.IElementParameter;
import org.talend.core.model.process.INode;
import org.talend.designer.core.i18n.Messages;
import org.talend.designer.core.model.components.EParameterName;
import org.talend.designer.core.model.components.ElementParameter;
import org.talend.designer.core.ui.editor.nodes.Node;
import org.talend.designer.core.utils.ParallelExecutionUtils;

public class SetParallelizationCommand extends Command {

    INode node;

    private static final String HASH_KEYS = "HASH_KEYS";

    private boolean isExistParallel = false;

    public SetParallelizationCommand(INode node) {
        this.node = node;
    }

    public SetParallelizationCommand(String label) {
        super(label);
    }

    @Override
    public void execute() {
        setParallelization(this.node);
        if (!isExistParallel) {
            MessageDialog.openInformation(new Shell(), Messages.getString("Node.setPartitioning"),
                    Messages.getString("Node.nothingDoForPartitioning"));
        }
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
            if (ParallelExecutionUtils.existPreviousPar((Node) con.getSource())
                    || ParallelExecutionUtils.existPreviousDepar((Node) con.getSource())
                    || ParallelExecutionUtils.existPreviousRepar((Node) con.getSource())
                    || ParallelExecutionUtils.existPreviousNone((Node) con.getSource())) {
                return false;
            }
            return true;
        } else {
            // compare the target node's key with the previous tPartitioner's hashKeys to see if need repartitioning.
            boolean needRepar = false;
            IConnection previousParCon = ParallelExecutionUtils.getPreviousParCon((Node) con.getSource());
            if (previousParCon != null) {
                String[] partitionKey = partitioning.split("\\.");
                IElementParameter parTableCon = previousParCon.getElementParameter(HASH_KEYS);
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
                                    columnKeyValues.add(nodeElemForList.getListItemsDisplayName()[index]);
                                }
                            }
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
        if (node.isActivate()) {
            if (node.getOutgoingConnections().size() > 0) {
                for (IConnection con : node.getOutgoingConnections()) {
                    EConnectionType lineStyle = con.getLineStyle();
                    if (lineStyle.hasConnectionCategory(IConnectionCategory.DATA)) {
                        // if (con.isActivate()) {
                        boolean isEndRow = con.getTarget().getOutgoingConnections().size() == 0;
                        boolean isStartRow = node.isStart();
                        if (ParallelExecutionUtils.isPartitionKeysExist(con)) {
                            ParallelExecutionUtils.reSetParKeyValuesForCon(con);
                        }
                        if (!isEndRow && isComponentCanParlization(con, (Node) con.getTarget())) {
                            // For those component support tPartitioner,but its keys not same as previous
                            // tPartitioner,need do Repartitioner automatic
                            if (isExistParallel && !isStartRow && isComponentNeedRepartion(con, (Node) con.getTarget())) {
                                con.getElementParameter(EParameterName.NONE.getName()).setValue(Boolean.FALSE);
                                con.getElementParameter(EParameterName.PARTITIONER.getName()).setValue(Boolean.FALSE);
                                con.getElementParameter(EParameterName.DEPARTITIONER.getName()).setValue(Boolean.FALSE);
                                con.setPropertyValue(EParameterName.REPARTITIONER.getName(), Boolean.TRUE);

                                // set the keys for hash keys
                                ParallelExecutionUtils.setHashKeysFromTarget(con, (Node) con.getTarget());
                                setParallelization(con.getTarget());

                            } else {
                                // when pervious con is par/repar/none,keep current is none
                                if (isExistParallel
                                        && (ParallelExecutionUtils.existPreviousPar((Node) con.getSource())
                                                || ParallelExecutionUtils.existPreviousNone((Node) con.getSource()) || ParallelExecutionUtils
                                                    .existPreviousRepar((Node) con.getSource()))) {
                                    con.getElementParameter(EParameterName.REPARTITIONER.getName()).setValue(Boolean.FALSE);
                                    con.getElementParameter(EParameterName.PARTITIONER.getName()).setValue(Boolean.FALSE);
                                    con.getElementParameter(EParameterName.DEPARTITIONER.getName()).setValue(Boolean.FALSE);
                                    con.setPropertyValue(EParameterName.NONE.getName(), Boolean.TRUE);
                                    setParallelization(con.getTarget());
                                } else {
                                    // add flag here is judge for if has did parallelization
                                    isExistParallel = true;
                                    INode nextPartionerNode = null;
                                    IElementParameter deparElem = con.getElementParameter(EParameterName.DEPARTITIONER.getName());
                                    deparElem.setValue(Boolean.FALSE);
                                    con.getElementParameter(EParameterName.REPARTITIONER.getName()).setValue(Boolean.FALSE);
                                    con.getElementParameter(EParameterName.NONE.getName()).setValue(Boolean.FALSE);
                                    con.setPropertyValue(EParameterName.PARTITIONER.getName(), Boolean.TRUE);

                                    if (isStartRow && lineStyle.equals(EConnectionType.FLOW_MERGE)) {
                                        con.setPropertyValue(EParameterName.PARTITIONER.getName(), Boolean.FALSE);
                                        con.getElementParameter(EParameterName.NONE.getName()).setValue(Boolean.TRUE);
                                        isExistParallel = false;
                                    } else {
                                        nextPartionerNode = ParallelExecutionUtils.getNextPartionerTargetNode(con);

                                        // set the keys from target node keys
                                        if (nextPartionerNode != null) {
                                            // TDI-26555:in case the target partitioner key not in the main flow.such as
                                            // in
                                            // lookup row,need to go next connection for partitioning
                                            if (ParallelExecutionUtils.isConClumnsContainsPartionKey(con,
                                                    (Node) nextPartionerNode)) {
                                                if (ParallelExecutionUtils.getColumnListFromTargetNode((Node) nextPartionerNode)
                                                        .size() > 0) {
                                                    ParallelExecutionUtils.setHashKeysFromTarget(con, (Node) nextPartionerNode);
                                                } else {
                                                    ParallelExecutionUtils.setHashKeysForCon(con);
                                                }
                                            } else {
                                                if (isStartRow) {
                                                    con.setPropertyValue(EParameterName.PARTITIONER.getName(), Boolean.FALSE);
                                                    isExistParallel = false;
                                                }
                                            }
                                        } else {
                                            ParallelExecutionUtils.setHashKeysForCon(con);
                                        }
                                    }
                                    if (con.getTarget() != null) {
                                        setParallelization(con.getTarget());
                                    }
                                }
                            }
                        } else {
                            if (!con.getSource().isStart()) {
                                if (isExistParallel && !ParallelExecutionUtils.existPreviousDepar((Node) con.getSource())) {
                                    setDeparallelization(con.getTarget());
                                }
                            }
                        }
                        // }
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
    }

    private void setDeparallelization(INode node) {
        if (node.isActivate()) {
            for (IConnection con : node.getIncomingConnections()) {
                EConnectionType lineStyle = con.getLineStyle();
                if (lineStyle.hasConnectionCategory(IConnectionCategory.DATA)) {
                    IElementParameter parElem = con.getElementParameter(EParameterName.PARTITIONER.getName());
                    parElem.setValue(Boolean.FALSE);
                    con.getElementParameter(EParameterName.REPARTITIONER.getName()).setValue(Boolean.FALSE);
                    con.getElementParameter(EParameterName.NONE.getName()).setValue(Boolean.FALSE);
                    con.setPropertyValue(EParameterName.DEPARTITIONER.getName(), Boolean.TRUE);
                }
            }
            if (node.getOutgoingConnections().size() > 0) {
                setParallelization(node);
            }
        }
    }
}
