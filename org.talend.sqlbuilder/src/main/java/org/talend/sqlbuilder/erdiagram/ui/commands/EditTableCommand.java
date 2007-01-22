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
package org.talend.sqlbuilder.erdiagram.ui.commands;

import java.util.List;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.commands.Command;
import org.talend.sqlbuilder.erdiagram.ui.nodes.Table;

/**
 * qzhang class Move a given node to another location. <br/>
 * 
 * $Id: MoveNodeCommand.java 1 2006-09-29 17:06:40 +0000 (星期五, 29 九月 2006) nrousseau $
 * 
 */
public class EditTableCommand extends Command {

    private Table node;

    private Rectangle newPos = new Rectangle();

    private Rectangle oldPos = new Rectangle();

    
    /**
     * Move the given node to another location.
     * 
     * @param node
     * @param newPos
     */
    public EditTableCommand(Table node, Rectangle newPos) {
        this.node = node;
        this.newPos = newPos;
        setLabel("MoveNodeCommand.0"); //$NON-NLS-1$
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean canExecute() {
        for (Table currentNode : (List<Table>) node.getErDiagram().getTables()) {
            if ((currentNode.getLocation().x == newPos.x) 
                    && (currentNode.getLocation().y == newPos.y)
                    && (currentNode.getSize().height == newPos.height)
                    && (currentNode.getSize().width == newPos.width)) {
                return false;
            }
        }
        return true;
    }

    public void execute() {
        oldPos.setLocation(this.node.getLocation());
        this.node.setLocation(newPos.getLocation());
        oldPos.setSize(this.node.getSize());
        this.node.setSize(newPos.getSize());
    }

    public void undo() {
        this.node.setLocation(oldPos.getLocation());
        this.node.setSize(oldPos.getSize());
    }

    public void redo() {
        this.node.setLocation(newPos.getLocation());
        this.node.setSize(newPos.getSize());
    }
}
