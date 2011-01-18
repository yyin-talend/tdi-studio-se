// ============================================================================
//
// Copyright (C) 2006-2010 Talend Inc. - www.talend.com
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
import org.talend.designer.xmlmap.model.emf.xmlmap.Connection;
import org.talend.designer.xmlmap.model.emf.xmlmap.NodeType;
import org.talend.designer.xmlmap.model.emf.xmlmap.OutputTreeNode;
import org.talend.designer.xmlmap.model.emf.xmlmap.TreeNode;
import org.talend.designer.xmlmap.model.emf.xmlmap.XmlmapFactory;
import org.talend.designer.xmlmap.parts.OutputTreeNodeEditPart;
import org.talend.designer.xmlmap.parts.TreeNodeEditPart;
import org.talend.designer.xmlmap.util.XmlMapUtil;
import org.talend.repository.ui.wizards.metadata.connection.files.xml.view.DragAndDrogDialog;

/**
 * wchen class global comment. Detailled comment
 */
public class CreateNodeAndConnectionCommand extends Command {

    private Object newObjects;

    private EditPart targetEditPart;

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
        OutputTreeNode parent = (OutputTreeNode) ((OutputTreeNodeEditPart) targetEditPart).getModel();
        if (!update) {

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

            if (!update && !parent.getIncomingConnections().isEmpty()) {
                boolean canContinue = MessageDialog.openConfirm(null, "Warning",
                        "Do you want to disconnect the existing linker and then add an sub element for the selected element ?");
                if (canContinue) {
                    XmlMapUtil.detachConnectionsSouce(parent);
                    parent.getIncomingConnections().clear();
                    parent.setExpression("");
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
                        if (parent.eContainer() instanceof OutputTreeNode) {

                            if (update) {
                                // update expression
                                String expression = parent.getExpression();
                                expression = expression + " " + XmlMapUtil.convertToExpression(sourceNode.getXpath());
                                parent.setExpression(expression);
                                Connection conn = XmlmapFactory.eINSTANCE.createConnection();
                                conn.setSource(sourceNode);
                                conn.setTarget(parent);
                                parent.getIncomingConnections().add(conn);
                                sourceNode.getOutgoingConnections().add(conn);
                            } else {
                                // add target node
                                OutputTreeNode targetNode = XmlmapFactory.eINSTANCE.createOutputTreeNode();
                                targetNode.setName(sourceNode.getName());
                                targetNode.setType(XmlMapUtil.DEFAULT_DATA_TYPE);
                                if (NodeType.ATTRIBUT.equals(nodeType)) {
                                    targetNode.setXpath(parent.getXpath() + XmlMapUtil.XPATH_SEPARATOR
                                            + XmlMapUtil.XPATH_ATTRIBUTE + sourceNode.getName());
                                } else {
                                    targetNode.setXpath(parent.getXpath() + XmlMapUtil.XPATH_SEPARATOR + sourceNode.getName());
                                }
                                targetNode.setNodeType(nodeType);
                                targetNode.setExpression(XmlMapUtil.convertToExpression(sourceNode.getXpath()));

                                parent.getChildren().add(targetNode);
                                // add connection
                                Connection conn = XmlmapFactory.eINSTANCE.createConnection();
                                conn.setSource(sourceNode);
                                conn.setTarget(targetNode);
                                // attach source and target
                                targetNode.getIncomingConnections().add(conn);
                                sourceNode.getOutgoingConnections().add(conn);
                            }
                        } else {
                            String expression = parent.getExpression();
                            expression = expression + " " + XmlMapUtil.convertToExpression(sourceNode.getXpath());
                            parent.setExpression(expression);
                            Connection conn = XmlmapFactory.eINSTANCE.createConnection();
                            conn.setSource(sourceNode);
                            conn.setTarget(parent);
                            parent.getIncomingConnections().add(conn);
                            sourceNode.getOutgoingConnections().add(conn);
                        }
                    }

                }
            }
        }
    }
}
