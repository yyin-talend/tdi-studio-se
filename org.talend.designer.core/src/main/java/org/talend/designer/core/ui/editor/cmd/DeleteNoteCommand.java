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
package org.talend.designer.core.ui.editor.cmd;

import java.util.List;

import org.eclipse.gef.commands.Command;
import org.talend.designer.core.i18n.Messages;
import org.talend.designer.core.ui.editor.notes.Note;
import org.talend.designer.core.ui.editor.process.Process;

/**
 */
public class DeleteNoteCommand extends Command {

    private List<Note> noteList;
    private Process process;

    public DeleteNoteCommand(Process process, List<Note> noteList) {
        this.process = process;
        this.noteList = noteList;
        setLabel(Messages.getString("DeleteNoteCommand.Name")); //$NON-NLS-1$
    }
    
    @Override
    public void execute() {
        for (Note note : noteList) {
            process.removeNote(note);
        }
    }

    @Override
    public void undo() {
        for (Note note : noteList) {
            process.addNote(note);
        }
    }
}
