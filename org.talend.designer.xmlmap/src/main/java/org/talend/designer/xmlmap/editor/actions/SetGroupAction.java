package org.talend.designer.xmlmap.editor.actions;

import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.ui.IWorkbenchPart;
import org.talend.designer.xmlmap.editor.XmlMapEditor;
import org.talend.designer.xmlmap.model.emf.xmlmap.NodeType;
import org.talend.designer.xmlmap.model.emf.xmlmap.OutputTreeNode;
import org.talend.designer.xmlmap.model.emf.xmlmap.TreeNode;
import org.talend.designer.xmlmap.parts.OutputTreeNodeEditPart;
import org.talend.designer.xmlmap.util.XmlMapUtil;

public class SetGroupAction extends SelectionAction {

    public static String ID = "xml map set as group action";

    private boolean isRemove;

    public SetGroupAction(IWorkbenchPart part) {
        super(part);
        setId(ID);
        setText("As group element");
    }

    @Override
    protected boolean calculateEnabled() {
        if (getSelectedObjects().isEmpty()) {
            return false;
        }
        if (getSelectedObjects().get(0) instanceof OutputTreeNodeEditPart) {
            OutputTreeNodeEditPart nodePart = (OutputTreeNodeEditPart) getSelectedObjects().get(0);
            OutputTreeNode model = (OutputTreeNode) nodePart.getModel();
            if (NodeType.ATTRIBUT.equals(model.getNodeType()) || NodeType.NAME_SPACE.equals(model.getNodeType())
                    || !(model.eContainer() instanceof TreeNode)) {
                return false;
            }
            OutputTreeNode findDownLoopNode = findDownLoopNode(model);
            if (findDownLoopNode == null) {
                return false;
            }

            if (isRemove) {
                if (model.isGroup()) {
                    return true;
                } else {
                    return false;
                }
            }

        } else {
            return false;
        }

        return true;
    }

    private static OutputTreeNode findDownLoopNode(OutputTreeNode treeNode) {

        for (TreeNode child : treeNode.getChildren()) {
            OutputTreeNode tmp = findDownLoopNode((OutputTreeNode) child);
            if (tmp != null) {
                return tmp;
            }
        }
        return null;
    }

    public void update() {
        setSelection(((XmlMapEditor) getWorkbenchPart()).getViewer().getSelection());
        if (getSelectedObjects().get(0) instanceof OutputTreeNodeEditPart) {
            OutputTreeNodeEditPart nodePart = (OutputTreeNodeEditPart) getSelectedObjects().get(0);
            OutputTreeNode model = (OutputTreeNode) nodePart.getModel();
            if (!model.isGroup()) {
                setText("As group element");
            } else {
                setText("Remove group element");
                isRemove = true;
            }
        }
    }

    @Override
    public void run() {
        OutputTreeNodeEditPart nodePart = (OutputTreeNodeEditPart) getSelectedObjects().get(0);
        OutputTreeNode model = (OutputTreeNode) nodePart.getModel();
        OutputTreeNode outputDocumentRoot = XmlMapUtil.getOutputDocumentRoot(model);
        if (outputDocumentRoot != null) {
            XmlMapUtil.cleanSubGroup(outputDocumentRoot);
        }
        if (!isRemove) {
            model.setGroup(true);
        } else {
            model.setGroup(false);
        }
    }

}
