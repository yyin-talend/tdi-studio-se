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

import org.eclipse.emf.common.util.EList;
import org.eclipse.gef.commands.Command;
import org.talend.designer.xmlmap.model.emf.xmlmap.Connection;
import org.talend.designer.xmlmap.model.emf.xmlmap.InputXmlTree;
import org.talend.designer.xmlmap.model.emf.xmlmap.OutputTreeNode;
import org.talend.designer.xmlmap.model.emf.xmlmap.OutputXmlTree;
import org.talend.designer.xmlmap.model.emf.xmlmap.TreeNode;

/**
 * wchen class global comment. Detailled comment
 */
public class DeleteTreeNodeCommand extends Command {

    private TreeNode toDelete;

    public DeleteTreeNodeCommand(TreeNode toDelete) {
        this.toDelete = toDelete;
    }

    @Override
    public void execute() {
        if (toDelete != null) {
            if (toDelete.eContainer() instanceof InputXmlTree) {
                InputXmlTree inputTree = (InputXmlTree) toDelete.eContainer();
                EList<Connection> outgoingConnections = toDelete.getOutgoingConnections();
                for (Connection connection : outgoingConnections) {
                    if (connection.getTarget() instanceof OutputTreeNode) {
                        OutputTreeNode outputNode = (OutputTreeNode) connection.getTarget();
                        outputNode.getIncomingConnections().remove(connection);
                    }
                }
                inputTree.getNodes().remove(toDelete);
            } else if (toDelete.eContainer() instanceof OutputXmlTree) {
                OutputXmlTree outputTree = (OutputXmlTree) toDelete.eContainer();
                OutputTreeNode outputNode = (OutputTreeNode) toDelete;
                EList<Connection> outgoingConnections = outputNode.getIncomingConnections();
                for (Connection connection : outgoingConnections) {
                    if (connection.getSource() instanceof TreeNode) {
                        TreeNode inputNode = (TreeNode) connection.getSource();
                        inputNode.getOutgoingConnections().remove(connection);
                    }
                }

                outputTree.getNodes().remove(toDelete);
            } else if (toDelete.eContainer() instanceof TreeNode) {
                TreeNode treeNode = (TreeNode) toDelete.eContainer();
                if (toDelete instanceof OutputTreeNode) {
                    OutputTreeNode outputNode = (OutputTreeNode) toDelete;
                    EList<Connection> outgoingConnections = outputNode.getIncomingConnections();
                    for (Connection connection : outgoingConnections) {
                        if (connection.getSource() instanceof TreeNode) {
                            TreeNode inputNode = (TreeNode) connection.getSource();
                            if (inputNode.getOutgoingConnections().contains(connection)) {
                                inputNode.getOutgoingConnections().remove(connection);
                            }
                        }
                    }
                } else if (toDelete instanceof TreeNode) {
                    EList<Connection> outgoingConnections = toDelete.getOutgoingConnections();
                    for (Connection connection : outgoingConnections) {
                        if (connection.getTarget() instanceof OutputTreeNode) {
                            OutputTreeNode outputNode = (OutputTreeNode) connection.getTarget();
                            outputNode.getIncomingConnections().remove(connection);
                        }
                    }
                }

                treeNode.getChildren().remove(toDelete);
            }
        }

    }

}
