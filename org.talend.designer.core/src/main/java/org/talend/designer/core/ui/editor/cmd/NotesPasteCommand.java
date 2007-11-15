// ============================================================================
//
// Copyright (C) 2006-2007 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//   
// ============================================================================
package org.talend.designer.core.ui.editor.cmd;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.commands.Command;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.ui.PlatformUI;
import org.talend.designer.core.i18n.Messages;
import org.talend.designer.core.ui.MultiPageTalendEditor;
import org.talend.designer.core.ui.editor.TalendEditor;
import org.talend.designer.core.ui.editor.notes.Note;
import org.talend.designer.core.ui.editor.notes.NoteEditPart;
import org.talend.designer.core.ui.editor.process.Process;
import org.talend.designer.core.ui.editor.process.ProcessPart;

/**
 * Command used to paste all the components.
 * 
 * $Id: NodesPasteCommand.java 4549 2007-07-13 05:18:48Z nrousseau $
 * 
 */
public class NotesPasteCommand extends Command {

    private Process process;

    private List<EditPart> oldSelection;

    private List<Note> noteList;

    private List<NoteEditPart> noteParts;

    private boolean multipleCommand;

    public NotesPasteCommand(List<NoteEditPart> noteParts, Process process) {
        this.process = process;
        orderNoteParts(noteParts);
        setLabel(Messages.getString("NotesPasteCommand.label")); //$NON-NLS-1$
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.gef.commands.Command#canExecute()
     */
    @Override
    public boolean canExecute() {
        return !process.isReadOnly();
    }

    private void orderNoteParts(List<NoteEditPart> noteParts) {
        this.noteParts = new ArrayList<NoteEditPart>();

        Point curLocation;

        NoteEditPart toAdd = null;

        List<NoteEditPart> restToOrder = new ArrayList<NoteEditPart>();
        restToOrder.addAll(noteParts);

        for (NoteEditPart copiedNodePart : noteParts) {
            curLocation = null;
            for (NoteEditPart partToOrder : restToOrder) {
                Note copiedNote = (Note) partToOrder.getModel();
                if (curLocation == null) {
                    curLocation = copiedNote.getLocation();
                    toAdd = partToOrder;
                } else {
                    if (curLocation.y >= copiedNote.getLocation().y) {
                        if (curLocation.x >= copiedNote.getLocation().x) {
                            curLocation = copiedNote.getLocation();
                            toAdd = partToOrder;
                        }
                    }
                }
            }
            if (toAdd != null) {
                this.noteParts.add(toAdd);
                restToOrder.remove(toAdd);
            }
        }
    }

    /**
     * 
     * Will return a empty location for a component from a given point.
     * 
     * @param location
     * @return
     */
    private Point findLocationForNote(final Note note) {
        Rectangle rect = new Rectangle(note.getLocation().x, note.getLocation().y, note.getSize().width,
                note.getSize().height);
        Point newLocation = findLocationForNoteInProcess(rect);
        return newLocation;
    }

    @SuppressWarnings("unchecked")//$NON-NLS-1$
    private Point findLocationForNoteInProcess(final Rectangle rectangle) {
        Point newLocation = new Point(rectangle.x, rectangle.y);
        for (Note currentNote : (List<Note>) process.getNotes()) {
            Rectangle currentRect = new Rectangle(currentNote.getLocation().x, currentNote.getLocation().y, currentNote
                    .getSize().width, currentNote.getSize().height);
            if (currentRect.intersects(rectangle)) {
                rectangle.x += currentRect.width;
                rectangle.y += currentRect.height;
                return findLocationForNoteInProcess(rectangle);
            }
        }
        return newLocation;
    }

    @SuppressWarnings("unchecked")//$NON-NLS-1$
    private void createNoteList() {
        noteList = new ArrayList<Note>();

        // create the notes
        for (NoteEditPart copiedNodePart : noteParts) {
            Note copiedNote = (Note) copiedNodePart.getModel();
            Note pastedNote = new Note();
            pastedNote.setOpaque(copiedNote.isOpaque());
            pastedNote.setText(copiedNote.getText());

            Point location = copiedNote.getLocation();
            if (process.isGridEnabled()) {
                // replace the component to set it on the grid if it's enabled
                int tempVar = location.x / TalendEditor.GRID_SIZE;
                location.x = tempVar * TalendEditor.GRID_SIZE;
                tempVar = location.y / TalendEditor.GRID_SIZE;
                location.y = tempVar * TalendEditor.GRID_SIZE;
            }
            pastedNote.setLocation(findLocationForNote(copiedNote));

            noteList.add(pastedNote);
        }
    }

    @SuppressWarnings("unchecked")//$NON-NLS-1$
    @Override
    public void execute() {
        // create the note list to paste
        createNoteList();

        MultiPageTalendEditor multiPageTalendEditor = (MultiPageTalendEditor) PlatformUI.getWorkbench()
                .getActiveWorkbenchWindow().getActivePage().getActiveEditor();
        GraphicalViewer viewer = multiPageTalendEditor.getTalendEditor().getViewer();

        // save old selection
        if (!multipleCommand) {
            oldSelection = new ArrayList<EditPart>();
            for (EditPart editPart : (List<EditPart>) viewer.getSelectedEditParts()) {
                oldSelection.add(editPart);
            }
            // remove the old selection
            viewer.deselectAll();
        }

        // creates the different notes
        for (Note note : noteList) {
            process.addNote(note);
        }
        // set the new note as the current selection
        if (!multipleCommand) {
            EditPart processPart = (EditPart) viewer.getRootEditPart().getChildren().get(0);
            if (processPart instanceof ProcessPart) { // can only be ProcessPart but still test
                for (EditPart editPart : (List<EditPart>) processPart.getChildren()) {
                    if (editPart instanceof NoteEditPart) {
                        Note currentNode = (Note) editPart.getModel();
                        if (noteList.contains(currentNode)) {
                            viewer.appendSelection(editPart);
                        }
                    }
                }
            }
        }
        process.checkStartNodes();
        process.checkProcess();
    }

    @SuppressWarnings("unchecked")//$NON-NLS-1$
    @Override
    public void undo() {
        // remove the current selection
        MultiPageTalendEditor multiPageTalendEditor = (MultiPageTalendEditor) PlatformUI.getWorkbench()
                .getActiveWorkbenchWindow().getActivePage().getActiveEditor();
        GraphicalViewer viewer = multiPageTalendEditor.getTalendEditor().getViewer();
        if (!multipleCommand) {
            viewer.deselectAll();
        }

        for (Note note : noteList) {
            process.removeNote(note);
        }

        // set the old selection active
        if (!multipleCommand) {
            for (EditPart editPart : oldSelection) {
                viewer.appendSelection(editPart);
            }
        }

        process.checkStartNodes();
        process.checkProcess();
    }

    /**
     * Getter for multipleCommand.
     * 
     * @return the multipleCommand
     */
    public boolean isMultipleCommand() {
        return multipleCommand;
    }

    /**
     * Sets the multipleCommand.
     * 
     * @param multipleCommand the multipleCommand to set
     */
    public void setMultipleCommand(boolean multipleCommand) {
        this.multipleCommand = multipleCommand;
    }

    /**
     * Getter for noteList.
     * 
     * @return the noteList
     */
    public List<Note> getNoteList() {
        return noteList;
    }
}
