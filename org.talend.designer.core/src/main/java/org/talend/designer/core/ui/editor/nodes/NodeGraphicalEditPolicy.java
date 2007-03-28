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
package org.talend.designer.core.ui.editor.nodes;

import java.util.List;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.GraphicalNodeEditPolicy;
import org.eclipse.gef.requests.CreateConnectionRequest;
import org.eclipse.gef.requests.ReconnectRequest;
import org.talend.core.model.process.EConnectionType;
import org.talend.designer.core.ui.editor.cmd.ConnectionCreateCommand;
import org.talend.designer.core.ui.editor.cmd.ConnectionReconnectCommand;
import org.talend.designer.core.ui.editor.connections.Connection;

/**
 * Edit policy that will allow connections to connect to the node. <br/>
 * 
 * $Id$
 * 
 */
public class NodeGraphicalEditPolicy extends GraphicalNodeEditPolicy {

    protected Command getConnectionCompleteCommand(CreateConnectionRequest request) {
        ConnectionCreateCommand cmd = (ConnectionCreateCommand) request.getStartCommand();
        cmd.setTarget((Node) getHost().getModel());
        return cmd;
    }

    @SuppressWarnings("unchecked") //$NON-NLS-1$
    protected Command getConnectionCreateCommand(CreateConnectionRequest request) {
        Node source = (Node) getHost().getModel();
        if (source.isReadOnly()) {
            return null;
        }
        EConnectionType style = (EConnectionType) request.getNewObjectType();
        ConnectionCreateCommand cmd = new ConnectionCreateCommand(source, style, (List<Object>) request.getNewObject());
        request.setStartCommand(cmd);
        return cmd;
    }

    protected Command getReconnectSourceCommand(ReconnectRequest request) {
        Connection conn = (Connection) request.getConnectionEditPart().getModel();
        Node newSource = (Node) getHost().getModel();
        if (newSource.isReadOnly()) {
            return null;
        }
        ConnectionReconnectCommand cmd = new ConnectionReconnectCommand(conn);
        cmd.setNewSource(newSource);
        return cmd;
    }

    protected Command getReconnectTargetCommand(ReconnectRequest request) {
        Connection conn = (Connection) request.getConnectionEditPart().getModel();
        Node newTarget = (Node) getHost().getModel();
        if (newTarget.isReadOnly()) {
            return null;
        }

        ConnectionReconnectCommand cmd = new ConnectionReconnectCommand(conn);
        cmd.setNewTarget(newTarget);
        return cmd;
    }
}
