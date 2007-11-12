// ============================================================================
//
// Copyright (C) 2006-2007 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the  agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
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
