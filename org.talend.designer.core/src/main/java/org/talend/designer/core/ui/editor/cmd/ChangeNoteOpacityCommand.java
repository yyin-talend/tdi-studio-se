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

import org.eclipse.gef.commands.Command;
import org.talend.designer.core.i18n.Messages;
import org.talend.designer.core.ui.editor.notes.Note;

/**
 */
public class ChangeNoteOpacityCommand extends Command {
    
    private Note note;
    private boolean oldState;
    private boolean newState;

    public ChangeNoteOpacityCommand(Note note, boolean newState) {
        super(Messages.getString("ChangeNoteOpacityCommand.Name")); //$NON-NLS-1$
        this.note = note;
        this.newState = newState;
    }
    
    @Override
    public void execute() {
        oldState = note.isOpaque();
        note.setOpaque(newState);
    }
    
    @Override
    public void undo() {
        note.setOpaque(oldState);
    }
}
