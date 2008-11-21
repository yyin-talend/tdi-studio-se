// ============================================================================
//
// Copyright (C) 2006-2007 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.designer.core.ui.action;

import java.util.List;

import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.ui.IWorkbenchPart;
import org.talend.core.model.process.EConnectionType;
import org.talend.core.model.process.IConnectionCategory;
import org.talend.designer.core.ui.dialog.mergeorder.ModifyOutputOrderDialog;
import org.talend.designer.core.ui.editor.cmd.ChangeOutputConnectionOrderCommand;
import org.talend.designer.core.ui.editor.connections.ConnLabelEditPart;
import org.talend.designer.core.ui.editor.connections.Connection;
import org.talend.designer.core.ui.editor.connections.ConnectionPart;
import org.talend.designer.core.ui.editor.nodes.Node;
import org.talend.designer.core.ui.editor.nodes.NodePart;

/**
 * yzhang class global comment. Detailled comment
 */
public class ModifyOutputOrderAction extends SelectionAction {

    public static final String ID = "org.talend.designer.core.ui.editor.action.ModifyOutputOrderAction"; //$NON-NLS-1$

    private Node multipleOutputNode;

    /**
     * yzhang ModifyOutputOrderAction constructor comment.
     * 
     * @param part
     * @param style
     */
    public ModifyOutputOrderAction(IWorkbenchPart part) {
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
        List parts = getSelectedObjects();
        if (parts.size() != 1) {
            return false;
        }
        Object o = parts.get(0);

        Node node = null;
        if (o instanceof ConnectionPart) {
            ConnectionPart part = (ConnectionPart) o;
            Connection connection = (Connection) part.getModel();
            node = connection.getSource();
        } else if (o instanceof ConnLabelEditPart) {
            ConnectionPart part = (ConnectionPart) ((ConnLabelEditPart) o).getParent();
            Connection connection = (Connection) part.getModel();
            node = connection.getSource();
        } else if (o instanceof NodePart) {
            node = (Node) ((NodePart) o).getModel();
        }

        if (node == null) {
            return false;
        }
        boolean isIterate = false;
        int nb = 0;
        for (Connection connection : (List<Connection>) node.getOutgoingConnections()) {
            if (connection.getLineStyle().hasConnectionCategory(IConnectionCategory.DATA)) {
                nb++;

            } else
            // feature 4505
            if (connection.getLineStyle() == EConnectionType.ITERATE) {
                isIterate = true;
                nb++;
            }
        }

        boolean multipleOutput = nb > 1;

        if (!multipleOutput) {
            return false;
        }
        multipleOutputNode = node;
        String midStr = "output"; //$NON-NLS-1$
        if (isIterate) {
            midStr = "iterate"; //$NON-NLS-1$
        }
        setText("Modify " + midStr + " links order"); //$NON-NLS-1$ //$NON-NLS-2$
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.action.Action#run()
     */
    @Override
    public void run() {
        super.run();
        ModifyOutputOrderDialog dialog = new ModifyOutputOrderDialog(this.getWorkbenchPart().getSite().getShell(),
                multipleOutputNode);

        if (dialog.open() == ModifyOutputOrderDialog.OK) {
            ChangeOutputConnectionOrderCommand cmd = new ChangeOutputConnectionOrderCommand(multipleOutputNode, dialog
                    .getConnectionList());
            execute(cmd);
        }
    }

}
