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

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.commands.Command;
import org.talend.designer.core.ui.editor.connections.ConnectionLabel;

/**
 * Command that moves the label of a connection. <br/>
 * 
 * $Id$
 * 
 */
public class MoveConnTextCommand extends Command {

    ConnectionLabel label = null;

    Point location = null;

    Figure parent = null;

    Point oldOffset = new Point();

    /**
     * Initialisation of the command. ConnectionLabel, Figure that will be the parent to calculate the position of the
     * label, delta of the move of the label after drag it
     * 
     * @param label
     * @param parent
     * @param delta
     */
    public MoveConnTextCommand(ConnectionLabel label, Figure parent, Point delta) {
        this.label = label;
        this.parent = parent;
        this.location = delta;
    }

    public void execute() {
        oldOffset = label.getOffset();
        Point newOffset = label.getOffset().getCopy();
        parent.translateToAbsolute(newOffset);
        newOffset.translate(location);
        parent.translateToRelative(newOffset);
        label.setOffset(newOffset);
    }

    public void redo() {
        execute();
    }

    public void undo() {
        label.setOffset(oldOffset);
    }
}
