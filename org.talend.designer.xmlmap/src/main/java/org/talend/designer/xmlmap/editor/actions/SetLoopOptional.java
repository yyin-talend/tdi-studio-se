package org.talend.designer.xmlmap.editor.actions;

import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.IWorkbenchPart;
import org.talend.designer.xmlmap.model.emf.xmlmap.TreeNode;
import org.talend.designer.xmlmap.parts.TreeNodeEditPart;

public class SetLoopOptional extends SelectionAction {

    public static String ID = "org.talend.designer.xmlmap.editor.actions.SetLoopOptional";

    public SetLoopOptional(IWorkbenchPart part) {
        super(part);
        setId(ID);
        setText("As optional loop");
    }

    @Override
    protected boolean calculateEnabled() {
        if (getSelectedObjects().isEmpty()) {
            return false;
        }
        if (getSelectedObjects().get(0) instanceof TreeNodeEditPart) {
            TreeNodeEditPart nodePart = (TreeNodeEditPart) getSelectedObjects().get(0);
            TreeNode model = (TreeNode) nodePart.getModel();
            if (model.isLoop()) {
                if (model.isOptional()) {
                    setText("Remove loop optional");
                } else {
                    setText("As optional loop");
                }
                return true;
            }

        } else {
            return false;
        }
        return false;
    }

    public void update(Object selection) {
        setSelection(new StructuredSelection(selection));
    }

    @Override
    public void run() {
        TreeNodeEditPart nodePart = (TreeNodeEditPart) getSelectedObjects().get(0);
        TreeNode model = (TreeNode) nodePart.getModel();
        model.setOptional(!model.isOptional());
    }

}
