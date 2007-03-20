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

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.commands.Command;
import org.talend.designer.core.i18n.Messages;
import org.talend.designer.core.ui.editor.notes.Note;

/**
 * Move a given node to another location. <br/>
 * 
 * $Id: MoveNodeCommand.java 2102 2007-02-22 08:33:22 +0000 (jeu., 22 févr. 2007) cantoine $
 * 
 */
public class MoveNoteCommand extends Command {

    private Note note;

    private Point newPos;

    private Point oldPos;

    /**
     * Move the given node to another location.
     * 
     * @param node
     * @param newPos
     */
    public MoveNoteCommand(Note note, Point newPos) {
        this.note = note;
        this.newPos = newPos;
        setLabel(Messages.getString("MoveNodeCommand.Label")); //$NON-NLS-1$
    }

    public void execute() {
        oldPos = note.getLocation();
        note.setLocation(newPos);
    }

    public void undo() {
        note.setLocation(oldPos);
    }

    public void redo() {
        note.setLocation(newPos);
    }
}
