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
package org.talend.designer.xmlmap.editor.actions;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.jface.action.Action;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.talend.designer.xmlmap.model.emf.xmlmap.Connection;
import org.talend.designer.xmlmap.model.emf.xmlmap.NodeType;
import org.talend.designer.xmlmap.model.emf.xmlmap.OutputTreeNode;
import org.talend.designer.xmlmap.model.emf.xmlmap.TreeNode;
import org.talend.designer.xmlmap.model.emf.xmlmap.XmlmapFactory;
import org.talend.designer.xmlmap.util.XmlMapUtil;
import org.talend.repository.ui.wizards.metadata.connection.files.xml.treeNode.Attribute;
import org.talend.repository.ui.wizards.metadata.connection.files.xml.treeNode.Element;
import org.talend.repository.ui.wizards.metadata.connection.files.xml.treeNode.FOXTreeNode;
import org.talend.repository.ui.wizards.metadata.connection.files.xml.treeNode.NameSpaceNode;
import org.talend.repository.ui.wizards.metadata.connection.files.xml.util.TreeUtil;

/**
 * wchen class global comment. Detailled comment
 */
public class ImportTreeFromXml extends Action {

    private Shell shell;

    private TreeNode parentNode;

    public ImportTreeFromXml(Shell shell, TreeNode parentNode) {
        setText("Import Xml");
        this.shell = shell;
        this.parentNode = parentNode;
    }

    @Override
    public void run() {
        List<FOXTreeNode> list = new ArrayList<FOXTreeNode>();
        FileDialog f = new FileDialog(shell);
        String file = f.open();
        if (file != null) {
            list = TreeUtil.getFoxTreeNodes(file);
            detachConnectionsTarget(parentNode);
            parentNode.getChildren().clear();
            prepareEmfTreeNode(list, parentNode);
        }

    }

    private void detachConnectionsTarget(TreeNode parentNode) {
        EList<TreeNode> children = parentNode.getChildren();
        for (TreeNode treeNode : children) {
            for (Connection connection : treeNode.getOutgoingConnections()) {
                OutputTreeNode target = connection.getTarget();
                if (target.getIncomingConnections().contains(connection)) {
                    target.getIncomingConnections().remove(connection);
                }
            }
            if (!treeNode.getChildren().isEmpty()) {
                detachConnectionsTarget(treeNode);
            }

        }

    }

    private void prepareEmfTreeNode(List<FOXTreeNode> list, TreeNode parent) {
        if (list == null && list.isEmpty()) {
            return;
        }
        String xPath = parent.getXpath();
        for (FOXTreeNode foxNode : list) {
            TreeNode createTreeNode = XmlmapFactory.eINSTANCE.createTreeNode();
            createTreeNode.setName(foxNode.getLabel());
            if (foxNode instanceof Element) {
                createTreeNode.setNodeType(NodeType.ELEMENT);
                createTreeNode.setXpath(xPath + XmlMapUtil.XPATH_SEPARATOR + foxNode.getLabel());
            } else if (foxNode instanceof Attribute) {
                createTreeNode.setNodeType(NodeType.ATTRIBUT);
                createTreeNode.setXpath(xPath + XmlMapUtil.XPATH_SEPARATOR + XmlMapUtil.XPATH_ATTRIBUTE + foxNode.getLabel());
            } else if (foxNode instanceof NameSpaceNode) {
                createTreeNode.setNodeType(NodeType.NAME_SPACE);
                createTreeNode.setXpath(xPath + XmlMapUtil.XPATH_SEPARATOR + XmlMapUtil.XPATH_NAMESPACE + foxNode.getLabel());
            }

            createTreeNode.setType(XmlMapUtil.DEFAULT_DATA_TYPE);
            parent.getChildren().add(createTreeNode);
            if (foxNode.getChildren() != null && !foxNode.getChildren().isEmpty()) {
                prepareEmfTreeNode(foxNode.getChildren(), createTreeNode);
            }
        }

    }

}
