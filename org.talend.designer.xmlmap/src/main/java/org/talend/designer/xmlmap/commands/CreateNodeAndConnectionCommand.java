// ============================================================================
//
// Copyright (C) 2006-2011 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.designer.xmlmap.commands;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;
import org.talend.core.model.utils.TalendTextUtils;
import org.talend.designer.xmlmap.dnd.DragAndDrogDialog;
import org.talend.designer.xmlmap.dnd.TransferedObject;
import org.talend.designer.xmlmap.model.emf.xmlmap.AbstractInOutTree;
import org.talend.designer.xmlmap.model.emf.xmlmap.AbstractNode;
import org.talend.designer.xmlmap.model.emf.xmlmap.Connection;
import org.talend.designer.xmlmap.model.emf.xmlmap.FilterConnection;
import org.talend.designer.xmlmap.model.emf.xmlmap.LookupConnection;
import org.talend.designer.xmlmap.model.emf.xmlmap.NodeType;
import org.talend.designer.xmlmap.model.emf.xmlmap.OutputTreeNode;
import org.talend.designer.xmlmap.model.emf.xmlmap.TreeNode;
import org.talend.designer.xmlmap.model.emf.xmlmap.VarNode;
import org.talend.designer.xmlmap.model.emf.xmlmap.VarTable;
import org.talend.designer.xmlmap.model.emf.xmlmap.XmlMapData;
import org.talend.designer.xmlmap.model.emf.xmlmap.XmlmapFactory;
import org.talend.designer.xmlmap.parts.AbstractNodePart;
import org.talend.designer.xmlmap.parts.InputXmlTreeEditPart;
import org.talend.designer.xmlmap.parts.OutputTreeNodeEditPart;
import org.talend.designer.xmlmap.parts.OutputXmlTreeEditPart;
import org.talend.designer.xmlmap.parts.TreeNodeEditPart;
import org.talend.designer.xmlmap.parts.VarNodeEditPart;
import org.talend.designer.xmlmap.util.XmlMapUtil;

/**
 * wchen class global comment. Detailled comment
 */
public class CreateNodeAndConnectionCommand extends Command {

    private Object newObjects;

    private EditPart targetEditPart;

    private XmlMapData xmlMapData;

    /*
     * if true update expression,else create a new child
     */
    private boolean update;

    public CreateNodeAndConnectionCommand(Object newObjects, EditPart targetEditPart, boolean update) {
        this.newObjects = newObjects;
        this.targetEditPart = targetEditPart;
        this.update = update;

    }

    @Override
    public void execute() {
        xmlMapData = getXmlMapData(targetEditPart.getModel());
        if (xmlMapData == null) {
            return;
        }
        if (newObjects instanceof TransferedObject) {
            TransferedObject tranceferedObj = (TransferedObject) newObjects;
            NodeType nodeType = NodeType.ELEMENT;
            if (!update && targetEditPart instanceof OutputTreeNodeEditPart) {
                OutputTreeNode targetOutputNode = (OutputTreeNode) ((OutputTreeNodeEditPart) targetEditPart).getModel();
                Shell shell = targetEditPart.getViewer().getControl().getShell();
                DragAndDrogDialog selectDialog = new DragAndDrogDialog(shell, !targetOutputNode.getChildren().isEmpty());
                int open = selectDialog.open();
                if (open == Window.OK) {
                    if (DragAndDrogDialog.CREATE_AS_SUBELEMENT.equals(selectDialog.getSelectValue())) {
                        nodeType = NodeType.ELEMENT;
                    } else if (DragAndDrogDialog.CREATE_AS_ATTRIBUTE.equals(selectDialog.getSelectValue())) {
                        nodeType = NodeType.ATTRIBUT;
                    } else if (DragAndDrogDialog.CREATE_AS_SUBELEMENT.equals(selectDialog.getSelectValue())) {
                        nodeType = NodeType.NAME_SPACE;
                    } else if (DragAndDrogDialog.CREATE_AS_TEXT.equals(selectDialog.getSelectValue())) {
                        update = true;
                    }
                } else {
                    return;
                }

                if (!update && !targetOutputNode.getIncomingConnections().isEmpty()) {
                    boolean canContinue = MessageDialog
                            .openConfirm(null, "Warning",
                                    "Do you want to disconnect the existing linker and then add an sub element for the selected element ?");
                    if (canContinue) {
                        XmlMapUtil.detachNodeConnections(targetOutputNode, xmlMapData, true);
                        targetOutputNode.setExpression("");
                    } else {
                        return;
                    }
                }
            }

            for (Object o : (tranceferedObj.getToTransfer())) {
                if (!(o instanceof AbstractNodePart)) {
                    continue;
                }
                AbstractNode sourceNode = (AbstractNode) ((AbstractNodePart) o).getModel();

                if (update) {
                    doUpdate(sourceNode);
                } else {
                    // only drop output can create a new node now
                    if (targetEditPart instanceof OutputTreeNodeEditPart) {
                        OutputTreeNode targetOutputNode = (OutputTreeNode) ((OutputTreeNodeEditPart) targetEditPart).getModel();
                        OutputTreeNode targetNode = XmlmapFactory.eINSTANCE.createOutputTreeNode();
                        targetNode.setName(sourceNode.getName());
                        targetNode.setType(XmlMapUtil.DEFAULT_DATA_TYPE);
                        targetNode.setXpath(XmlMapUtil.getXPath(targetOutputNode.getXpath(), targetNode.getName(), nodeType));
                        targetNode.setNodeType(nodeType);
                        if (sourceNode instanceof TreeNode) {
                            targetNode.setExpression(XmlMapUtil.convertToExpression(((TreeNode) sourceNode).getXpath()));
                        } else if (sourceNode instanceof VarNode) {
                            String variable = sourceNode.getName();
                            targetNode.setNodeType(nodeType);
                            if (sourceNode.eContainer() instanceof VarTable) {
                                VarTable container = (VarTable) sourceNode.eContainer();
                                variable = container.getName() + TalendTextUtils.JAVA_END_STRING + variable;
                            }
                            targetNode.setExpression(variable);
                        }

                        targetOutputNode.getChildren().add(targetNode);
                        // add connection
                        Connection conn = XmlmapFactory.eINSTANCE.createConnection();
                        conn.setSource(sourceNode);
                        conn.setTarget(targetNode);
                        // attach source and target
                        targetNode.getIncomingConnections().add(conn);
                        sourceNode.getOutgoingConnections().add(conn);
                        if (xmlMapData != null) {
                            xmlMapData.getConnections().add(conn);
                        }
                    }
                }

            }
        }
        if (targetEditPart instanceof OutputTreeNodeEditPart) {
            OutputTreeNode model = (OutputTreeNode) targetEditPart.getModel();
            if (NodeType.NAME_SPACE.equals(model.getNodeType()) && model.getExpression() != null
                    && !"".equals(model.getExpression())) {
                model.setDefaultValue("");
            }
        }
    }

    private XmlMapData getXmlMapData(Object obj) {
        if (obj instanceof AbstractNode) {
            return XmlMapUtil.getXmlMapData((AbstractNode) obj);
        } else if (obj instanceof AbstractInOutTree) {
            return (XmlMapData) ((AbstractInOutTree) obj).eContainer();
        }
        return null;
    }

    private void doUpdate(AbstractNode sourceNode) {
        if (targetEditPart instanceof OutputTreeNodeEditPart) {
            OutputTreeNode targetOutputNode = (OutputTreeNode) ((OutputTreeNodeEditPart) targetEditPart).getModel();

            String expression = targetOutputNode.getExpression();
            if (sourceNode instanceof TreeNode) {
                if (expression == null) {
                    expression = XmlMapUtil.convertToExpression(((TreeNode) sourceNode).getXpath());
                } else {
                    expression = expression + " " + XmlMapUtil.convertToExpression(((TreeNode) sourceNode).getXpath());
                }
            } else if (sourceNode instanceof VarNode) {
                String tableName = "Var";
                if (sourceNode.eContainer() instanceof VarTable) {
                    tableName = ((VarTable) sourceNode.eContainer()).getName();
                }
                if (expression == null) {
                    expression = tableName + "." + sourceNode.getName();
                } else {
                    expression = expression + " " + tableName + "." + sourceNode.getName();
                }
            }
            targetOutputNode.setExpression(expression);
            Connection conn = XmlmapFactory.eINSTANCE.createConnection();
            conn.setSource(sourceNode);
            conn.setTarget(targetOutputNode);
            targetOutputNode.getIncomingConnections().add(conn);
            sourceNode.getOutgoingConnections().add(conn);
            if (xmlMapData != null) {
                xmlMapData.getConnections().add(conn);
            }

        } else if (targetEditPart instanceof TreeNodeEditPart) {
            /* for lookup connections */
            if (sourceNode instanceof TreeNode) {
                TreeNode targetTreeNode = (TreeNode) targetEditPart.getModel();
                String expression = targetTreeNode.getExpression();
                if (expression == null) {
                    expression = "";
                }
                expression = expression + " " + XmlMapUtil.convertToExpression(((TreeNode) sourceNode).getXpath());
                targetTreeNode.setExpression(expression);

                LookupConnection conn = XmlmapFactory.eINSTANCE.createLookupConnection();
                conn.setSource(sourceNode);
                conn.setTarget(targetTreeNode);
                targetTreeNode.getLookupIncomingConnections().add(conn);
                ((TreeNode) sourceNode).getLookupOutgoingConnections().add(conn);
                if (xmlMapData != null) {
                    xmlMapData.getConnections().add(conn);
                }
            }
        } else if (targetEditPart instanceof VarNodeEditPart) {
            /* for varTable drag drop */
            if (sourceNode instanceof TreeNode) {
                VarNodeEditPart targetPart = (VarNodeEditPart) targetEditPart;
                VarNode targetNode = (VarNode) targetPart.getModel();
                String expression = targetNode.getExpression();
                if (expression == null) {
                    expression = "";
                }
                expression = expression + " " + XmlMapUtil.convertToExpression(((TreeNode) sourceNode).getXpath());
                if (targetNode.getName() == null || "".equals(targetNode.getName())) {
                    String findUniqueVarColumnName = XmlMapUtil.findUniqueVarColumnName(sourceNode.getName(), xmlMapData
                            .getVarTables().get(0));
                    targetNode.setName(findUniqueVarColumnName);
                }
                targetNode.setExpression(expression.trim());
                targetNode.setType(sourceNode.getType());
                Connection conn = XmlmapFactory.eINSTANCE.createConnection();
                conn.setSource(sourceNode);
                conn.setTarget(targetNode);
                targetNode.getIncomingConnections().add(conn);
                sourceNode.getOutgoingConnections().add(conn);
                if (xmlMapData != null) {
                    xmlMapData.getConnections().add(conn);
                }
            }

        } else if (targetEditPart instanceof InputXmlTreeEditPart || targetEditPart instanceof OutputXmlTreeEditPart) {
            AbstractInOutTree treeModel = (AbstractInOutTree) targetEditPart.getModel();
            String expression = treeModel.getExpressionFilter();
            if (expression == null) {
                expression = XmlMapUtil.convertToExpression(((TreeNode) sourceNode).getXpath());
            } else {
                expression = expression + " " + XmlMapUtil.convertToExpression(((TreeNode) sourceNode).getXpath());
            }
            treeModel.setExpressionFilter(expression);
            FilterConnection connection = XmlmapFactory.eINSTANCE.createFilterConnection();
            connection.setSource(sourceNode);
            connection.setTarget(treeModel);
            treeModel.getFilterIncomingConnections().add(connection);
            sourceNode.getFilterOutGoingConnections().add(connection);

            xmlMapData.getConnections().add(connection);

        }

    }
}
