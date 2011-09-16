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
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.IWorkbenchPart;
import org.talend.designer.xmlmap.model.emf.xmlmap.NodeType;
import org.talend.designer.xmlmap.model.emf.xmlmap.OutputTreeNode;
import org.talend.designer.xmlmap.parts.OutputTreeNodeEditPart;

/**
 * DOC talend class global comment. Detailled comment
 */
public class SetAggregateAction extends SelectionAction {

    public static String ID = "xml map set as aggregate action";

    public SetAggregateAction(IWorkbenchPart part) {
        super(part);
        setId(ID);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.gef.ui.actions.WorkbenchPartAction#calculateEnabled()
     */
    @Override
    protected boolean calculateEnabled() {
        if (getSelectedObjects().isEmpty()) {
            return false;
        }
        if (getSelectedObjects().get(0) instanceof OutputTreeNodeEditPart) {
            OutputTreeNodeEditPart nodePart = (OutputTreeNodeEditPart) getSelectedObjects().get(0);
            OutputTreeNode model = (OutputTreeNode) nodePart.getModel();
            // root can't be aggregate
            if (NodeType.NAME_SPACE.equals(model.getNodeType()) || !(model.eContainer() instanceof OutputTreeNode)) {
                return false;
            }
            if (!model.getChildren().isEmpty()) {
                return false;
            }

        } else {
            return false;
        }

        return true;
    }

    public void update(Object selection) {
        setSelection(new StructuredSelection(selection));
        if (selection instanceof OutputTreeNodeEditPart) {
            OutputTreeNodeEditPart nodePart = (OutputTreeNodeEditPart) getSelectedObjects().get(0);
            OutputTreeNode model = (OutputTreeNode) nodePart.getModel();
            if (!model.isAggregate()) {
                setText("As aggregate element");
            } else {
                setText("Remove aggregate element");
            }
        }
    }

    @Override
    public void run() {
        OutputTreeNodeEditPart nodePart = (OutputTreeNodeEditPart) getSelectedObjects().get(0);
        OutputTreeNode model = (OutputTreeNode) nodePart.getModel();
        model.setAggregate(!model.isAggregate());
    }

}
