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
package org.talend.designer.core.ui.editor.cmd;

import java.util.List;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.commands.Command;
import org.talend.designer.core.i18n.Messages;
import org.talend.designer.core.ui.editor.nodes.Node;

/**
 * Move a given node to another location. <br/>
 * 
 * $Id$
 * 
 */
public class MoveNodeCommand extends Command {

    private Node node;

    private Point newPos;

    private Point oldPos;

    /**
     * Move the given node to another location.
     * 
     * @param node
     * @param newPos
     */
    public MoveNodeCommand(Node node, Point newPos) {
        this.node = node;
        this.newPos = newPos;
        setLabel(Messages.getString("MoveNodeCommand.0")); //$NON-NLS-1$
    }

    @SuppressWarnings("unchecked") //$NON-NLS-1$
    @Override
    public boolean canExecute() {
        for (Node currentNode : (List<Node>) node.getProcess().getGraphicalNodes()) {
            if ((currentNode.getLocation().x == newPos.x) && (currentNode.getLocation().y == newPos.y)) {
                return false;
            }
        }
        return true;
    }

    public void execute() {
        oldPos = this.node.getLocation();
        this.node.setLocation(newPos);
    }

    public void undo() {
        this.node.setLocation(oldPos);
    }

    public void redo() {
        this.node.setLocation(newPos);
    }
}
