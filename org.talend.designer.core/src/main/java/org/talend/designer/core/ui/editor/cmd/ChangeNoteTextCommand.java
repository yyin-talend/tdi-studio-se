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

import org.eclipse.gef.commands.Command;
import org.talend.designer.core.i18n.Messages;
import org.talend.designer.core.ui.editor.notes.Note;

/**
 */
public class ChangeNoteTextCommand extends Command {
    
    private Note note;
    private String newText;
    private String oldText;

    public ChangeNoteTextCommand(Note note, String newText) {
        super(Messages.getString("ChangeNoteTextCommand.Name")); //$NON-NLS-1$
        this.note = note;
        this.newText = newText;
    }
    
    @Override
    public void execute() {
        oldText = note.getText();
        note.setText(newText);
    }
    
    @Override
    public void undo() {
        note.setText(oldText);
    }
}
