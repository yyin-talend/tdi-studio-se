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

import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.ui.IWorkbenchPart;
import org.talend.designer.xmlmap.editor.XmlMapEditor;
import org.talend.designer.xmlmap.model.emf.xmlmap.InputXmlTree;
import org.talend.designer.xmlmap.model.emf.xmlmap.OutputTreeNode;
import org.talend.designer.xmlmap.model.emf.xmlmap.TreeNode;
import org.talend.designer.xmlmap.parts.TreeNodeEditPart;
import org.talend.designer.xmlmap.ui.tabs.MapperManager;
import org.talend.designer.xmlmap.util.XmlMapUtil;

/**
 * wchen class global comment. Detailled comment
 */
public class DeleteTreeNodeAction extends SelectionAction {

    private boolean input;

    private MapperManager mapperManager;

    public static final String ID = "org.talend.designer.xmlmap.editor.actions.DeleteTreeNodeAction";

    public DeleteTreeNodeAction(IWorkbenchPart part) {
        super(part);
        setId(ID);
        setText("Delete");
    }

    public void update() {
        setSelection(((XmlMapEditor) getWorkbenchPart()).getViewer().getSelection());
    }

    @Override
    protected boolean calculateEnabled() {
        if (getSelectedObjects().isEmpty()) {
            return false;
        } else {
            boolean enable = true;
            for (Object obj : getSelectedObjects()) {
                if (obj instanceof TreeNodeEditPart) {
                    TreeNodeEditPart nodePart = (TreeNodeEditPart) obj;
                    TreeNode treeNode = (TreeNode) nodePart.getModel();
                    int xPathLength = XmlMapUtil.getXPathLength(treeNode.getXpath());
                    if (xPathLength <= 2) {
                        enable = false;
                    }
                    // can't delete root
                    if (treeNode.eContainer() instanceof TreeNode
                            && XmlMapUtil.DOCUMENT.equals(((TreeNode) treeNode.eContainer()).getType())) {
                        enable = false;
                    }

                } else {
                    enable = false;
                }
            }
            return enable;
        }
    }

    @Override
    public void run() {
        TreeNode docRoot = null;
        for (Object obj : getSelectedObjects()) {
            TreeNodeEditPart nodePart = (TreeNodeEditPart) obj;
            TreeNode treeNode = (TreeNode) nodePart.getModel();
            if (treeNode.eContainer() instanceof TreeNode) {
                TreeNode parent = (TreeNode) treeNode.eContainer();
                if (input) {
                    // find root before remove
                    if (docRoot == null) {
                        docRoot = XmlMapUtil.getInputTreeNodeRoot(parent);
                    }
                    detachConnectionsTarget(treeNode);
                } else {
                    detachConnectionsSoruce((OutputTreeNode) treeNode);
                }
                parent.getChildren().remove(treeNode);
            }

        }
        if (mapperManager != null) {
            if (input) {
                Object model = ((TreeNodeEditPart) getSelectedObjects().get(0)).getModel();
                TreeNode node = (TreeNode) model;

                if (docRoot != null && docRoot.eContainer() instanceof InputXmlTree) {
                    mapperManager.refreshInputTreeSchemaEditor((InputXmlTree) docRoot.eContainer());
                }
            }

        }

    }

    private void detachConnectionsSoruce(OutputTreeNode treeNode) {
        if (treeNode.getChildren().isEmpty()) {
            XmlMapUtil.detachConnectionsSouce(treeNode);
        } else {
            for (int i = 0; i < treeNode.getChildren().size(); i++) {
                TreeNode child = treeNode.getChildren().get(i);
                detachConnectionsSoruce((OutputTreeNode) child);
            }
        }
    }

    private void detachConnectionsTarget(TreeNode treeNode) {
        if (treeNode.getChildren().isEmpty()) {
            XmlMapUtil.detachConnectionsTarget(treeNode);
        } else {
            for (int i = 0; i < treeNode.getChildren().size(); i++) {
                TreeNode child = treeNode.getChildren().get(i);
                detachConnectionsTarget(child);
            }
        }
    }

    public boolean isInput() {
        return input;
    }

    public void setInput(boolean input) {
        this.input = input;
    }

    public void setMapperManager(MapperManager mapperManager) {
        this.mapperManager = mapperManager;
    }
}
