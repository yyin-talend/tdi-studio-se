package org.talend.designer.xmlmap.editor.actions;

import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.ui.IWorkbenchPart;
import org.talend.designer.xmlmap.editor.XmlMapEditor;
import org.talend.designer.xmlmap.model.emf.xmlmap.NodeType;
import org.talend.designer.xmlmap.model.emf.xmlmap.OutputTreeNode;
import org.talend.designer.xmlmap.model.emf.xmlmap.TreeNode;
import org.talend.designer.xmlmap.parts.TreeNodeEditPart;
import org.talend.designer.xmlmap.util.XmlMapUtil;

public class SetLoopAction extends SelectionAction {

    public static String ID = "xml map set as loop action";

    public SetLoopAction(IWorkbenchPart part) {
        super(part);
        setId(ID);
        setText("As loop element");
    }

    @Override
    protected boolean calculateEnabled() {
        if (getSelectedObjects().isEmpty()) {
            return false;
        }
        if (getSelectedObjects().get(0) instanceof TreeNodeEditPart) {
            TreeNodeEditPart nodePart = (TreeNodeEditPart) getSelectedObjects().get(0);
            TreeNode model = (TreeNode) nodePart.getModel();
            // root can't be loop
            if (model.eContainer() instanceof TreeNode && XmlMapUtil.DOCUMENT.equals(((TreeNode) model.eContainer()).getType())) {
                return false;
            }
            if (NodeType.ATTRIBUT.equals(model.getNodeType()) || NodeType.NAME_SPACE.equals(model.getNodeType())
                    || !(model.eContainer() instanceof TreeNode)) {
                return false;
            }

            if (model.isLoop()) {
                return false;
            }

        } else {
            return false;
        }

        return true;
    }

    public void update() {
        setSelection(((XmlMapEditor) getWorkbenchPart()).getViewer().getSelection());
    }

    @Override
    public void run() {
        TreeNodeEditPart nodePart = (TreeNodeEditPart) getSelectedObjects().get(0);
        TreeNode model = (TreeNode) nodePart.getModel();
        // remove old loop
        if (model instanceof OutputTreeNode) {
            OutputTreeNode outputNode = (OutputTreeNode) model;
            OutputTreeNode outputDocumentRoot = XmlMapUtil.getOutputTreeNodeRoot(outputNode);
            if (outputDocumentRoot != null) {
                cleanSubLoop(outputDocumentRoot);
                XmlMapUtil.cleanSubGroup(outputNode);
                if (findUpGroupNode(outputNode) == null) {
                    // clean all group
                    XmlMapUtil.cleanSubGroup(outputDocumentRoot);
                }
            }
        } else if (model instanceof TreeNode) {
            TreeNode inputDocumentRoot = XmlMapUtil.getInputTreeNodeRoot(model);
            if (inputDocumentRoot != null) {
                cleanSubLoop(inputDocumentRoot);
            }
        }

        model.setLoop(true);

    }

    private void cleanSubLoop(TreeNode docRoot) {
        for (TreeNode treeNode : docRoot.getChildren()) {
            if (treeNode.isLoop()) {
                treeNode.setLoop(false);
            }
            cleanSubLoop(treeNode);
        }
    }

    private TreeNode findUpGroupNode(OutputTreeNode node) {
        if (node.eContainer() instanceof OutputTreeNode) {
            OutputTreeNode parent = (OutputTreeNode) node.eContainer();
            if (parent.isGroup()) {
                return parent;
            } else {
                return findUpGroupNode(parent);
            }
        }
        return null;
    }

}
