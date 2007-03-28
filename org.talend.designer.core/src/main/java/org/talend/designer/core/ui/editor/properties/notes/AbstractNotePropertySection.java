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
package org.talend.designer.core.ui.editor.properties.notes;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.gef.commands.CommandStack;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.views.properties.tabbed.AbstractPropertySection;
import org.talend.designer.core.ui.MultiPageTalendEditor;
import org.talend.designer.core.ui.editor.notes.Note;
import org.talend.designer.core.ui.editor.notes.NoteEditPart;

/**
 */
public abstract class AbstractNotePropertySection extends AbstractPropertySection implements PropertyChangeListener {

    protected Note note;

    private MultiPageTalendEditor multiPageTalendEditor;

    protected CommandStack getCommandStack() {
        return (CommandStack) multiPageTalendEditor.getTalendEditor().getAdapter(CommandStack.class);
    }

    @Override
    public void setInput(IWorkbenchPart part, ISelection selection) {
        super.setInput(part, selection);

        if (part instanceof MultiPageTalendEditor) {
            multiPageTalendEditor = (MultiPageTalendEditor) part;
        } else {
            multiPageTalendEditor = (MultiPageTalendEditor) PlatformUI.getWorkbench().getActiveWorkbenchWindow()
                    .getActivePage().getActiveEditor();
        }

        if (getSelection() instanceof IStructuredSelection) {
            IStructuredSelection structuredSelection = (IStructuredSelection) getSelection();
            if (structuredSelection.getFirstElement() instanceof NoteEditPart) {
                NoteEditPart noteEditPart = (NoteEditPart) structuredSelection.getFirstElement();
                note = (Note) noteEditPart.getModel();
            }
        }
    }

    @Override
    public void aboutToBeShown() {
        note.addPropertyChangeListener(this);
    }

    @Override
    public void aboutToBeHidden() {
        note.removePropertyChangeListener(this);
    }

    public void propertyChange(PropertyChangeEvent evt) {
        refresh();
    }

}
