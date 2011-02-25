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

import java.util.List;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;
import org.talend.core.model.utils.TalendTextUtils;
import org.talend.designer.xmlmap.model.emf.xmlmap.Connection;
import org.talend.designer.xmlmap.model.emf.xmlmap.LookupConnection;
import org.talend.designer.xmlmap.model.emf.xmlmap.NodeType;
import org.talend.designer.xmlmap.model.emf.xmlmap.OutputTreeNode;
import org.talend.designer.xmlmap.model.emf.xmlmap.OutputXmlTree;
import org.talend.designer.xmlmap.model.emf.xmlmap.TreeNode;
import org.talend.designer.xmlmap.model.emf.xmlmap.VarNode;
import org.talend.designer.xmlmap.model.emf.xmlmap.VarTable;
import org.talend.designer.xmlmap.model.emf.xmlmap.XmlMapData;
import org.talend.designer.xmlmap.model.emf.xmlmap.XmlmapFactory;
import org.talend.designer.xmlmap.parts.OutputTreeNodeEditPart;
import org.talend.designer.xmlmap.parts.TreeNodeEditPart;
import org.talend.designer.xmlmap.parts.VarNodeEditPart;
import org.talend.designer.xmlmap.util.XmlMapUtil;
import org.talend.repository.ui.wizards.metadata.connection.files.xml.view.DragAndDrogDialog;

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
        NodeType nodeType = null;
        OutputTreeNode targetOutputNode = null;
        VarNode targetVarNode = null;
        TreeNode treeNode = null;

        if (targetEditPart == null) {
            return;
        }

        if (targetEditPart.getModel() instanceof OutputTreeNode) {
            targetOutputNode = (OutputTreeNode) ((OutputTreeNodeEditPart) targetEditPart).getModel();
            OutputTreeNode outputTreeNodeRoot = XmlMapUtil.getOutputTreeNodeRoot(targetOutputNode);
            if (outputTreeNodeRoot != null && outputTreeNodeRoot.eContainer() != null
                    && outputTreeNodeRoot.eContainer().eContainer() instanceof XmlMapData) {
                xmlMapData = (XmlMapData) outputTreeNodeRoot.eContainer().eContainer();
            }
        } else if (targetEditPart.getModel() instanceof VarNode) {
            targetVarNode = (VarNode) targetEditPart.getModel();
            xmlMapData = (XmlMapData) targetVarNode.eContainer().eContainer();
        } else if (targetEditPart.getModel() instanceof TreeNode) {
            treeNode = (TreeNode) targetEditPart.getModel();
            TreeNode treeNodeRoot = XmlMapUtil.getInputTreeNodeRoot(treeNode);
            if (treeNodeRoot != null && treeNodeRoot.eContainer() != null
                    && treeNodeRoot.eContainer().eContainer() instanceof XmlMapData) {
                xmlMapData = (XmlMapData) treeNodeRoot.eContainer().eContainer();
            }
        }
        if (targetOutputNode != null) {
            if (targetOutputNode.eContainer() instanceof OutputXmlTree) {
                update = true;
            }
        }

        if (!update && targetOutputNode != null) {
            Shell shell = targetEditPart.getViewer().getControl().getShell();
            DragAndDrogDialog selectDialog = new DragAndDrogDialog(shell);
            int open = selectDialog.open();
            if (open == Window.OK) {
                if (DragAndDrogDialog.CREATE_AS_SUBELEMENT.equals(selectDialog.getSelectValue())) {
                    nodeType = NodeType.ELEMENT;
                } else if (DragAndDrogDialog.CREATE_AS_ATTRIBUTE.equals(selectDialog.getSelectValue())) {
                    nodeType = NodeType.ATTRIBUT;
                } else if (DragAndDrogDialog.CREATE_AS_TEXT.equals(selectDialog.getSelectValue())) {
                    update = true;
                }
            } else {
                return;
            }

            if (!update && !targetOutputNode.getIncomingConnections().isEmpty()) {
                boolean canContinue = MessageDialog.openConfirm(null, "Warning",
                        "Do you want to disconnect the existing linker and then add an sub element for the selected element ?");
                if (canContinue) {
                    XmlMapUtil.detachConnectionsSouce(targetOutputNode, xmlMapData);
                    targetOutputNode.getIncomingConnections().clear();
                    targetOutputNode.setExpression("");
                } else {
                    return;
                }
            }
        }

        if (newObjects instanceof List) {
            for (Object o : (List) newObjects) {
                if (o instanceof TreeNodeEditPart) {
                    TreeNodeEditPart sourceEditPart = (TreeNodeEditPart) o;
                    TreeNode sourceNode = (TreeNode) sourceEditPart.getModel();
                    if (targetEditPart instanceof OutputTreeNodeEditPart) {
                        if (update) {
                            // update expression
                            String expression = targetOutputNode.getExpression();
                            if (expression == null) {
                                expression = XmlMapUtil.convertToExpression(sourceNode.getXpath());
                            } else {
                                expression = expression + " " + XmlMapUtil.convertToExpression(sourceNode.getXpath());
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

                        } else {
                            // add target node
                            OutputTreeNode targetNode = XmlmapFactory.eINSTANCE.createOutputTreeNode();
                            targetNode.setName(sourceNode.getName());
                            targetNode.setType(XmlMapUtil.DEFAULT_DATA_TYPE);
                            // if (NodeType.ATTRIBUT.equals(nodeType)) {
                            // targetNode.setXpath(targetOutputNode.getXpath() + XmlMapUtil.XPATH_SEPARATOR
                            // + XmlMapUtil.XPATH_ATTRIBUTE + sourceNode.getName());
                            // } else {
                            // targetNode.setXpath(targetOutputNode.getXpath() + XmlMapUtil.XPATH_SEPARATOR
                            // + sourceNode.getName());
                            // }
                            targetNode.setXpath(XmlMapUtil.getXPath(targetOutputNode.getXpath(), targetNode.getName(), nodeType));

                            targetNode.setNodeType(nodeType);
                            targetNode.setExpression(XmlMapUtil.convertToExpression(sourceNode.getXpath()));

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
                    /* for varTable drag drop */
                    else if (targetEditPart instanceof VarNodeEditPart) {
                        VarNodeEditPart targetPart = (VarNodeEditPart) targetEditPart;
                        VarNode targetNode = (VarNode) targetPart.getModel();
                        if (update) {
                            String expression = targetNode.getExpression();
                            if (expression == null) {
                                expression = "";
                            }
                            expression = expression + " " + XmlMapUtil.convertToExpression(sourceNode.getXpath());
                            if (targetNode.getName() == null || "".equals(targetNode.getName())) {
                                String findUniqueVarColumnName = XmlMapUtil.findUniqueVarColumnName(sourceNode.getName(),
                                        xmlMapData.getVarTables().get(0));
                                targetNode.setName(findUniqueVarColumnName);
                            }
                            targetNode.setExpression(expression.trim());
                            targetNode.setType(sourceNode.getType());
                            // targetNode.setNodeType(sourceNode.getNodeType());
                            // targetNode.setXpath(sourceNode.getXpath());
                            Connection conn = XmlmapFactory.eINSTANCE.createConnection();
                            conn.setSource(sourceNode);
                            conn.setTarget(targetNode);
                            targetNode.getIncomingConnections().add(conn);
                            sourceNode.getOutgoingConnections().add(conn);
                            if (xmlMapData != null) {
                                xmlMapData.getConnections().add(conn);
                            }
                            /* add a new varNode */
                        } else {
                            VarNode newVarNode = XmlmapFactory.eINSTANCE.createVarNode();
                            newVarNode.setExpression("");
                            newVarNode.setName("");
                            newVarNode.setType("id_String");
                            newVarNode.setNullable(false);
                            if (xmlMapData != null) {
                                VarTable varTable = xmlMapData.getVarTables().get(0);
                                varTable.getNodes().add(newVarNode);
                            }

                        }
                    }
                    /* for lookup connections */
                    else if (targetEditPart instanceof TreeNodeEditPart) {
                        TreeNode targetTreeNode = (TreeNode) targetEditPart.getModel();
                        String expression = targetTreeNode.getExpression();
                        if (expression == null) {
                            expression = "";
                        }
                        expression = expression + " " + XmlMapUtil.convertToExpression(sourceNode.getXpath());
                        targetTreeNode.setExpression(expression);

                        LookupConnection conn = XmlmapFactory.eINSTANCE.createLookupConnection();
                        conn.setSource(sourceNode);
                        conn.setTarget(targetTreeNode);
                        targetTreeNode.getLookupIncomingConnections().add(conn);
                        sourceNode.getLookupOutgoingConnections().add(conn);
                        if (xmlMapData != null) {
                            xmlMapData.getConnections().add(conn);
                        }
                    }
                } else if (o instanceof VarNodeEditPart) {
                    VarNodeEditPart sourceVarNode = (VarNodeEditPart) o;
                    VarNode sourceNode = (VarNode) sourceVarNode.getModel();

                    String tableName = "Var";
                    if (sourceNode.eContainer() instanceof VarTable) {
                        tableName = ((VarTable) sourceNode.eContainer()).getName();
                    }

                    if (targetEditPart instanceof OutputTreeNodeEditPart) {
                        if (update) {
                            // update expression
                            String expression = targetOutputNode.getExpression();
                            if (expression == null) {
                                expression = tableName + "." + sourceNode.getName();
                            } else {
                                expression = expression + " " + tableName + "." + sourceNode.getName();
                            }
                            targetOutputNode.setExpression(expression);
                            Connection conn = XmlmapFactory.eINSTANCE.createConnection();
                            conn.setSource(sourceNode);
                            conn.setTarget(targetOutputNode);
                            if (xmlMapData != null) {
                                xmlMapData.getConnections().add(conn);
                            }
                            targetOutputNode.getIncomingConnections().add(conn);
                            sourceNode.getOutgoingConnections().add(conn);

                        } else {
                            // add target node
                            OutputTreeNode targetNode = XmlmapFactory.eINSTANCE.createOutputTreeNode();
                            targetNode.setName(sourceNode.getName());
                            targetNode.setType(XmlMapUtil.DEFAULT_DATA_TYPE);

                            targetNode.setXpath(XmlMapUtil.getXPath(targetOutputNode.getXpath(), targetNode.getName(), nodeType));

                            String variable = sourceNode.getName();
                            targetNode.setNodeType(nodeType);
                            if (sourceNode.eContainer() instanceof VarTable) {
                                VarTable container = (VarTable) sourceNode.eContainer();
                                variable = container.getName() + TalendTextUtils.JAVA_END_STRING + variable;
                            }
                            targetNode.setExpression(variable);

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
        }
    }
}
