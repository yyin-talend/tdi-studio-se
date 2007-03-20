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
package org.talend.designer.core.ui.editor.notes;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.ComponentEditPolicy;
import org.eclipse.gef.requests.GroupRequest;
import org.talend.designer.core.ui.editor.cmd.DeleteNoteCommand;
import org.talend.designer.core.ui.editor.process.Process;

/**
 */
public class NoteEditPolicy extends ComponentEditPolicy {
    @Override
    protected Command createDeleteCommand(GroupRequest request) {
        Object parent = getHost().getParent().getModel();
        if (!(parent instanceof Process)) {
            return null;
        }

        Note note = (Note) getHost().getModel();
        if (note.isReadOnly()) {
            return null;
        }

        List<Note> noteList = new ArrayList<Note>();
        for (int i = 0; i < request.getEditParts().size(); i++) {
            if (request.getEditParts().get(i) instanceof NoteEditPart) {
                noteList.add(((Note) ((NoteEditPart) request.getEditParts().get(i)).getModel()));
            }
        }

        DeleteNoteCommand deleteCommand = new DeleteNoteCommand((Process) parent, noteList);
        return deleteCommand;
    }
}
