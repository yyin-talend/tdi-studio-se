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
package org.talend.designer.core.ui.action;

import java.util.List;

import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.ui.IWorkbenchPart;
import org.talend.core.model.components.IComponent;
import org.talend.core.model.metadata.IMetadataTable;
import org.talend.core.model.process.EComponentCategory;
import org.talend.core.model.process.EConnectionType;
import org.talend.core.model.process.EParameterFieldType;
import org.talend.core.model.process.IConnection;
import org.talend.core.model.process.IConnectionCategory;
import org.talend.core.model.process.IElementParameter;
import org.talend.core.model.process.INode;
import org.talend.designer.core.i18n.Messages;
import org.talend.designer.core.model.components.EParameterName;
import org.talend.designer.core.model.components.ElementParameter;
import org.talend.designer.core.ui.editor.nodecontainer.NodeContainer;
import org.talend.designer.core.ui.editor.nodecontainer.NodeContainerPart;
import org.talend.designer.core.ui.editor.nodes.Node;
import org.talend.designer.core.ui.editor.nodes.NodePart;
import org.talend.designer.core.ui.editor.process.Process;
import org.talend.designer.core.ui.editor.properties.controllers.ColumnListController;
import org.talend.designer.core.ui.editor.subjobcontainer.SubjobContainer;
import org.talend.designer.core.ui.editor.subjobcontainer.SubjobContainerPart;
import org.talend.repository.model.ComponentsFactoryProvider;

public class SetParallelizationAction extends SelectionAction {

    public static final String ID = "org.talend.designer.core.ui.editor.action.SetParallelizationAction"; //$NON-NLS-1$

    private static final String INPUT = "Input";

    private static final String OUTPUT = "Output";

    public SetParallelizationAction(IWorkbenchPart part) {
        super(part);
        setId(ID);
        setText(Messages.getString("PropertiesContextAction.parallelization")); //$NON-NLS-1$
    }

    @Override
    protected boolean calculateEnabled() {
        List parts = getSelectedObjects();
        if (parts.isEmpty()) {
            return false;
        }
        if (parts.size() == 1) {
            Object o = parts.get(0);
            if (o instanceof SubjobContainerPart) {
                SubjobContainerPart part = (SubjobContainerPart) o;
                SubjobContainer subjob = (SubjobContainer) part.getModel();
                if (subjob.isDisplayed()) {
                    return true;
                } else {
                    return false;
                }
            } else if (o instanceof NodePart) {
                NodePart part = (NodePart) o;
                Node node = (Node) part.getModel();
                if (node.isStart()) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        }
        return false;
    }

    @Override
    public void run() {
        List editparts = getSelectedObjects();
        if (editparts.size() == 1) {
            Object o = editparts.get(0);
            if (o instanceof NodePart) {
                NodePart part = (NodePart) o;
                Node node = (Node) part.getModel();
                setParallelization(node);
            } else if (o instanceof SubjobContainerPart) {
                boolean hasStartNode = false;
                List<NodeContainerPart> childNodes = ((SubjobContainerPart) o).getChildren();
                for (NodeContainerPart childNode : childNodes) {
                    NodeContainerPart part = (NodeContainerPart) childNode;
                    NodeContainer node = (NodeContainer) part.getModel();
                    if (node.getNode().isStart()) {
                        hasStartNode = true;
                        setParallelization(node.getNode());
                    }
                }
                if (!hasStartNode) {
                    for (NodeContainerPart childNode : childNodes) {
                        NodeContainerPart part = (NodeContainerPart) childNode;
                        NodeContainer node = (NodeContainer) part.getModel();
                        if (node.getNode().isSubProcessStart()) {
                            setParallelization(node.getNode());
                        }
                    }
                }
            }
        }
    }

    private void setParallelization(INode node) {
        if (node.getOutgoingConnections().size() > 0) {
            for (IConnection con : node.getOutgoingConnections()) {
                EConnectionType lineStyle = con.getLineStyle();
                if (lineStyle.hasConnectionCategory(IConnectionCategory.MAIN)
                        || lineStyle.hasConnectionCategory(IConnectionCategory.MERGE)) {
                    if (isComponentCanParlization(con, (Node) con.getTarget())) {
                        List<IElementParameter> listParam = (List<IElementParameter>) con.getElementParameters();
                        IComponent componentPar = ComponentsFactoryProvider.getInstance().get("tPartitioner");
                        Node tmpNode = new Node(componentPar, (Process) node.getProcess());
                        tmpNode.setTemplate(node.isTemplate());
                        tmpNode.setGeneratedByJobscriptBool(node.isGeneratedByJobscriptBool());

                        // flag for start the parlization or not for the inputs
                        IElementParameter enableParam = new ElementParameter(con);
                        enableParam.setName(EParameterName.PARALIZATION.getName());
                        enableParam.setValue(new Boolean(true));
                        enableParam.setDisplayName(EParameterName.PARALIZATION.getDisplayName());
                        enableParam.setFieldType(EParameterFieldType.CHECK);
                        enableParam.setCategory(EComponentCategory.PARALLELIZATION);
                        enableParam.setNumRow(1);
                        enableParam.setReadOnly(false);
                        enableParam.setRequired(false);
                        enableParam.setShow(true);
                        listParam.add(enableParam);

                        IElementParameter numParam = con.getElementParameter("NUM_PARTITIONS");

                        if (numParam == null) {
                            IElementParameter tmpParam = tmpNode.getElementParameter("NUM_PARTITIONS");
                            tmpParam.setCategory(EComponentCategory.PARALLELIZATION);
                            tmpParam.setShowIf("PARALIZATION == 'true'");
                            listParam.add(tmpParam);
                        }

                        IElementParameter queueParam = con.getElementParameter("QUEUE_SIZE");

                        if (queueParam == null) {
                            IElementParameter tmpParam = tmpNode.getElementParameter("QUEUE_SIZE");
                            tmpParam.setCategory(EComponentCategory.PARALLELIZATION);
                            tmpParam.setShowIf("PARALIZATION == 'true'");
                            listParam.add(tmpParam);
                        }

                        IElementParameter hashParParam = con.getElementParameter("HASH_PARTITION");

                        if (hashParParam == null) {
                            IElementParameter tmpParam = tmpNode.getElementParameter("HASH_PARTITION");
                            tmpParam.setCategory(EComponentCategory.PARALLELIZATION);
                            tmpParam.setShowIf("PARALIZATION == 'true'");
                            listParam.add(tmpParam);
                        }

                        IElementParameter hashKeyParam = con.getElementParameter("HASH_KEYS");

                        if (hashKeyParam == null) {
                            IElementParameter tmpParam = tmpNode.getElementParameter("HASH_KEYS");
                            tmpParam.setCategory(EComponentCategory.PARALLELIZATION);
                            tmpParam.setShowIf("PARALIZATION == 'true'");
                            ColumnListController.updateColumnList(tmpNode, null, true);
                            listParam.add(tmpParam);
                        }
                        if (con.getTarget() != null) {
                            setParallelization(con.getTarget());
                        }
                    } else {
                        if (!con.getSource().isStart()) {
                            setDeparallelization(con.getTarget());
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

    private void setAllOutputConnection(INode nextTarget) {
        if (nextTarget.getOutgoingConnections().size() > 0) {
            for (IConnection conOutput : nextTarget.getOutgoingConnections()) {
                if (conOutput.getLineStyle().hasConnectionCategory(IConnectionCategory.MAIN)
                        || conOutput.getLineStyle().hasConnectionCategory(IConnectionCategory.MERGE)) {
                    Node outPutTarget = (Node) conOutput.getTarget();
                    if (outPutTarget.getComponent().getName().contains(OUTPUT)) {
                        setDeparallelization(outPutTarget);
                    }
                    if (outPutTarget.getOutgoingConnections().size() > 0) {
                        setAllOutputConnection(outPutTarget);
                    }
                }
            }
        }
    }

    private void setAllInputConnection(INode previousSource) {
        if (previousSource.getIncomingConnections().size() > 0) {
            for (IConnection conInput : previousSource.getIncomingConnections()) {
                if (conInput.getLineStyle().hasConnectionCategory(IConnectionCategory.MAIN)
                        || conInput.getLineStyle().hasConnectionCategory(IConnectionCategory.MERGE)) {
                    Node inputSource = (Node) conInput.getSource();
                    if (inputSource.getComponent().getName().contains(INPUT)) {
                        setParallelization(inputSource);
                    }
                    if (inputSource.getIncomingConnections().size() > 0) {
                        setAllInputConnection(inputSource);
                    }
                }
            }
        }
    }

    private void setDeparallelization(INode node) {
        for (IConnection con : node.getIncomingConnections()) {
            EConnectionType lineStyle = con.getLineStyle();
            if (lineStyle.hasConnectionCategory(IConnectionCategory.MAIN)
                    || lineStyle.hasConnectionCategory(IConnectionCategory.MERGE)) {
                List<IElementParameter> listParam = (List<IElementParameter>) con.getElementParameters();
                IComponent componentPar = ComponentsFactoryProvider.getInstance().get("tDepartitioner");
                Node tmpNode = new Node(componentPar, (Process) node.getProcess());
                tmpNode.setTemplate(node.isTemplate());
                tmpNode.setGeneratedByJobscriptBool(node.isGeneratedByJobscriptBool());

                // flag for start the deparlization or not for the inputs
                IElementParameter enableParam = new ElementParameter(con);
                enableParam.setName(EParameterName.DPARALIZATION.getName());
                enableParam.setValue(new Boolean(true));
                enableParam.setDisplayName(EParameterName.DPARALIZATION.getDisplayName());
                enableParam.setFieldType(EParameterFieldType.CHECK);
                enableParam.setCategory(EComponentCategory.PARALLELIZATION);
                enableParam.setNumRow(1);
                enableParam.setReadOnly(false);
                enableParam.setRequired(false);
                enableParam.setShow(true);
                listParam.add(enableParam);

                IElementParameter queueParam = con.getElementParameter("QUEUE_SIZE");

                if (queueParam == null) {
                    IElementParameter tmpParam = tmpNode.getElementParameter("QUEUE_SIZE");
                    tmpParam.setCategory(EComponentCategory.PARALLELIZATION);
                    tmpParam.setShowIf("DPARALIZATION == 'true'");
                    listParam.add(tmpParam);
                }

                IComponent componentCol = ComponentsFactoryProvider.getInstance().get("tRecollector");
                Node tmpNode1 = new Node(componentCol, (Process) node.getProcess());
                tmpNode1.setTemplate(node.isTemplate());
                tmpNode1.setGeneratedByJobscriptBool(node.isGeneratedByJobscriptBool());

                IElementParameter isSortParam = con.getElementParameter("IS_SORTING");
                if (isSortParam == null) {
                    IElementParameter tmpParam = tmpNode1.getElementParameter("IS_SORTING");
                    tmpParam.setCategory(EComponentCategory.PARALLELIZATION);
                    tmpParam.setShowIf("DPARALIZATION == 'true'");
                    listParam.add(tmpParam);
                }

            }
        }
        if (node.getOutgoingConnections().size() > 0) {
            setParallelization(node);
        }
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
}
