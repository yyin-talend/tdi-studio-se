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

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.gef.commands.Command;
import org.talend.designer.core.i18n.Messages;
import org.talend.designer.core.ui.editor.TalendEditor;
import org.talend.designer.core.ui.editor.notes.Note;

/**
 */
public class ResizeNoteCommand extends Command {

    private final Note note;

    private Dimension oldSize;

    private final Dimension newSize;

    public ResizeNoteCommand(Note note, Dimension newSize) {
        super(Messages.getString("ResizeNoteCommand.Name")); //$NON-NLS-1$
        this.note = note;
        this.newSize = newSize;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.gef.commands.Command#canExecute()
     */
    @Override
    public boolean canExecute() {
        if (newSize.width < TalendEditor.GRID_SIZE) {
            newSize.width = TalendEditor.GRID_SIZE;
            return false;
        } else if (newSize.height < TalendEditor.GRID_SIZE) {
            newSize.height = TalendEditor.GRID_SIZE;
            return false;
        }
        return true;
    }

    @Override
    public void execute() {
        oldSize = note.getSize();
        note.setSize(newSize);
    }

    @Override
    public void undo() {
        note.setSize(oldSize);
    }
}
