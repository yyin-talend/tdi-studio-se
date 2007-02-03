// ============================================================================
//
// Talend Community Edition
//
// Copyright (C) 2006 Talend - www.talend.com
//
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either
// version 2.1 of the License, or (at your option) any later version.
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
import org.talend.core.model.process.EConnectionType;
import org.talend.core.model.process.IConnection;
import org.talend.core.model.process.INode;
import org.talend.designer.core.i18n.Messages;
import org.talend.designer.core.ui.editor.cmd.ChangeConnectionStatusCommand;
import org.talend.designer.core.ui.editor.connections.ConnLabelEditPart;
import org.talend.designer.core.ui.editor.connections.Connection;
import org.talend.designer.core.ui.editor.connections.ConnectionPart;

/**
 * Action to set a connection to main or to ref.
 * 
 * $Id$
 * 
 */
public class ConnectionSetAsMainRef extends SelectionAction {

    public static final String ID = "org.talend.designer.core.ui.editor.action.ConnectionSetAsMainRef"; //$NON-NLS-1$

    private static final String TEXT_SET_MAIN = Messages.getString("ConnectionSetAsMainRef.mainLabel"); //$NON-NLS-1$

    private static final String TEXT_SET_REF = Messages.getString("ConnectionSetAsMainRef.lookupLabel"); //$NON-NLS-1$

    Connection connection;

    public ConnectionSetAsMainRef(IWorkbenchPart part) {
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
        if (parts.size() == 1) {
            Object o = parts.get(0);
            /*
             * if (!(o instanceof ConnectionPart) && !(o instanceof ConnLabelEditPart)) { return false; }
             */
            ConnectionPart part = null;
            if (o instanceof ConnectionPart) {
                part = (ConnectionPart) o;
            } else {
                if (o instanceof ConnLabelEditPart) {
                    part = (ConnectionPart) ((ConnLabelEditPart) o).getParent();
                } else {
                    return false;
                }
            }

            if (!(part.getModel() instanceof Connection)) {
                return false;
            }
            connection = (Connection) part.getModel();

            if (!connection.isActivate()) {
                return false;
            }

            if (connection.getLineStyle().equals(EConnectionType.FLOW_MAIN)) {
                int nbTargetFlowIn = 0;
                INode node = connection.getTarget();
                for (IConnection currentConnec : node.getIncomingConnections()) {
                    if (currentConnec.getLineStyle().equals(EConnectionType.FLOW_MAIN)
                            || currentConnec.getLineStyle().equals(EConnectionType.FLOW_REF)) {
                        nbTargetFlowIn++;
                    }
                }
                if (nbTargetFlowIn <= 1) {
                    return false;
                }
                setText(TEXT_SET_REF);
            } else {
                if (connection.getLineStyle().equals(EConnectionType.FLOW_REF)) {
                    setText(TEXT_SET_MAIN);
                } else {
                    return false;
                }
            }

            return true;
        }
        return false;
    }

    public void run() {
        ChangeConnectionStatusCommand cmd = new ChangeConnectionStatusCommand(connection);
        execute(cmd);
    }
}
