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
package org.talend.designer.xmlmap.editor.actions;

import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.ui.IWorkbenchPart;
import org.talend.designer.xmlmap.model.emf.xmlmap.NodeType;
import org.talend.designer.xmlmap.model.emf.xmlmap.TreeNode;
import org.talend.designer.xmlmap.parts.TreeNodeEditPart;

/**
 * DOC talend class global comment. Detailled comment
 */
public class FixValueAction extends SelectionAction {

    private TreeNode selectedNode;

    public static final String ID = "org.talend.designer.xmlmap.editor.actions.FixValueAction";

    public FixValueAction(IWorkbenchPart part) {
        super(part);
        setId(ID);
        setText("Change Namespace Value");
    }

    @Override
    public void run() {
        if (selectedNode != null) {
            InputDialog dialog = new InputDialog(null, "Input a fix value", "Input a valid default value",
                    selectedNode.getDefaultValue(), null);
            if (dialog.open() == Window.OK) {
                selectedNode.setDefaultValue(dialog.getValue());
            }
        }
    }

    @Override
    protected boolean calculateEnabled() {
        if (getSelectedObjects().isEmpty()) {
            return false;
        } else {
            Object object = getSelectedObjects().get(0);
            if (object instanceof TreeNodeEditPart) {
                TreeNodeEditPart nodePart = (TreeNodeEditPart) object;
                this.selectedNode = (TreeNode) nodePart.getModel();
                boolean isNameSpace = NodeType.NAME_SPACE.equals(selectedNode.getNodeType());
                if (isNameSpace && (selectedNode.getExpression() == null || "".equals(selectedNode.getExpression()))) {
                    return true;
                }
            }
        }
        return false;
    }

    public void update(Object selection) {
        setSelection(new StructuredSelection(selection));
    }

}
