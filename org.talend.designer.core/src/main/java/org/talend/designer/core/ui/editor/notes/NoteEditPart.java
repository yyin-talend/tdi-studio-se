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
package org.talend.designer.core.ui.editor.notes;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.draw2d.IFigure;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CommandStack;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.gef.tools.DirectEditManager;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.talend.designer.core.ui.MultiPageTalendEditor;
import org.talend.designer.core.ui.editor.cmd.ChangeNoteOpacityCommand;

/**
 */
public class NoteEditPart extends AbstractGraphicalEditPart implements PropertyChangeListener {

    private DirectEditManager directEditManager;

    @Override
    protected IFigure createFigure() {
        return new NoteFigure();
    }

    @Override
    protected void createEditPolicies() {
        installEditPolicy(EditPolicy.GRAPHICAL_NODE_ROLE, new NoteGraphicalEditPolicy());
        installEditPolicy(EditPolicy.DIRECT_EDIT_ROLE, new NoteDirectEditPolicy());
        installEditPolicy(EditPolicy.COMPONENT_ROLE, new NoteEditPolicy());
        installEditPolicy(EditPolicy.LAYOUT_ROLE, new NoteResizableEditPolicy());
    }

    @Override
    protected void refreshVisuals() {
        Note note = (Note) getModel();
        NoteFigure noteFigure = ((NoteFigure) getFigure());

        noteFigure.setLocation(note.getLocation());
        noteFigure.setSize(note.getSize());
        noteFigure.setText(note.getText());
        noteFigure.setOpaque(note.isOpaque());
    }

    @Override
    public void activate() {
        if (!isActive()) {
            super.activate();
            ((Note) getModel()).addPropertyChangeListener(this);
        }
    }

    @Override
    public void deactivate() {
        if (isActive()) {
            super.deactivate();
            ((Note) getModel()).removePropertyChangeListener(this);
        }
    }

    public void propertyChange(final PropertyChangeEvent changeEvent) {
        refresh();
    }

    @Override
    public void performRequest(Request request) {
        Note note = (Note) getModel();
        if (note.isReadOnly()) {
            return;
        }

        if (request.getType() == RequestConstants.REQ_OPEN) {
            IWorkbenchPart part = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
                    .getActiveEditor();
            if (part instanceof MultiPageTalendEditor) {
                CommandStack commandStack = (CommandStack) part.getAdapter(CommandStack.class);

                Command command = new ChangeNoteOpacityCommand(note, !note.isOpaque());
                commandStack.execute(command);
            }
        }
        if (request.getType() == RequestConstants.REQ_DIRECT_EDIT) {
            if (directEditManager == null) {
                NoteFigure noteFigure = (NoteFigure) getFigure();
                directEditManager = new NoteDirectEditManager(this, TextCellEditor.class, new NoteCellEditorLocator(
                        noteFigure));
            }
            directEditManager.show();
        }
    }

    /**
     * yzhang Comment method "getNoteDirectEditManager".
     * 
     * @return
     */
    public NoteDirectEditManager getDirectEditManager() {
        return (NoteDirectEditManager) directEditManager;
    }
}
