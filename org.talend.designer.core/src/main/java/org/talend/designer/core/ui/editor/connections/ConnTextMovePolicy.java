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
package org.talend.designer.core.ui.editor.connections;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.NonResizableEditPolicy;
import org.eclipse.gef.requests.ChangeBoundsRequest;
import org.talend.designer.core.ui.editor.cmd.MoveConnTextCommand;

/**
 * Policy that will allow to move the label of the connection. <br/>
 * 
 * $Id$
 * 
 */
public class ConnTextMovePolicy extends NonResizableEditPolicy {

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.gef.editpolicies.NonResizableEditPolicy#getMoveCommand(org.eclipse.gef.requests.ChangeBoundsRequest)
     */
    public Command getMoveCommand(ChangeBoundsRequest request) {
        if (((Connection) getHost().getParent().getModel()).isReadOnly()) {
            return null;
        }
        ConnectionLabel model = (ConnectionLabel) getHost().getModel();
        Point delta = request.getMoveDelta();
        ConnectionPart edge = (ConnectionPart) getHost().getParent();
        MoveConnTextCommand command = new MoveConnTextCommand(model, (Figure) edge.getFigure(), delta);
        return command;
    }

}
