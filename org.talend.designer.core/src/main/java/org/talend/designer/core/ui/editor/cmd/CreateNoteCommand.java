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
import org.talend.designer.core.i18n.Messages;
import org.talend.designer.core.ui.editor.notes.Note;
import org.talend.designer.core.ui.editor.process.Process;


/**
 */
public class CreateNoteCommand extends CreateCommand {
    
    private Note note;

    public CreateNoteCommand(Process process, Note note, Point location) {
        super(Messages.getString("CreateNoteCommand.Name"), process, location); //$NON-NLS-1$
        this.note = note;
    }

    @Override
    public void execute() {
        if (location != null) {
            note.setLocation(this.location);
        }
        process.addNote(note);
    }

    @Override
    public void undo() {
        process.removeNote(note);
    }
}
