// ============================================================================
//
// Talend Community Edition
//
// Copyright (C) 2006-2007 Talend - www.talend.com
//
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either
// version 2.1 of the License.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
// Lesser General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with this program; if not, write to the Free Software
// Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
//
// ============================================================================
package org.talend.designer.core.ui.action;

import java.util.List;

import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.ui.IWorkbenchPart;
import org.talend.designer.core.i18n.Messages;
import org.talend.designer.core.model.components.EParameterName;
import org.talend.designer.core.ui.editor.cmd.ChangeActivateStatusNodeCommand;
import org.talend.designer.core.ui.editor.nodes.Node;
import org.talend.designer.core.ui.editor.nodes.NodePart;

/**
 * Action used to set the start status on a node with the context menu. <br/>
 * 
 * $Id$
 * 
 */
public class NodeSetActivateAction extends SelectionAction {

    public static final String ID = "org.talend.designer.core.ui.editor.action.NodeSetActivateAction"; //$NON-NLS-1$

    private static final String TEXT_SET_ACTIVATE = Messages.getString("NodeSetActivateAction.Activate"); //$NON-NLS-1$

    private static final String TEXT_REM_ACTIVATE = Messages.getString("NodeSetActivateAction.Deactivate"); //$NON-NLS-1$

    public NodeSetActivateAction(IWorkbenchPart part) {
        super(part);
        setId(ID);
    }

    @Override
    protected boolean calculateEnabled() {
        return canPerformAction();
    }

    /**
     * Test if the selected item is a node.
     * 
     * @return true / false
     */
    private boolean canPerformAction() {
        if (getSelectedObjects().isEmpty()) {
            return false;
        }
        List parts = getSelectedObjects();
        if (parts.size() == 1) {
            Object o = parts.get(0);
            if (!(o instanceof NodePart)) {
                return false;
            }
            NodePart part = (NodePart) o;
            if (!(part.getModel() instanceof Node)) {
                return false;
            }
            Node n = (Node) part.getModel();
            if (n.isReadOnly()) {
                return false;
            }
            if (n.getPropertyValue(EParameterName.ACTIVATE.getName()) == null) {
                return false;
            }

            if (n.isActivate()) {
                setText(TEXT_REM_ACTIVATE);
            } else {
                setText(TEXT_SET_ACTIVATE);
            }
            return true;
        }
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.action.Action#run()
     */
    public void run() {
        List editparts = getSelectedObjects();
        if (editparts.size() == 1) {
            NodePart part = (NodePart) editparts.get(0);

            ChangeActivateStatusNodeCommand changeActivateStatusCommand = new ChangeActivateStatusNodeCommand((Node) part
                    .getModel());
            execute(changeActivateStatusCommand);
        }
    }
}
