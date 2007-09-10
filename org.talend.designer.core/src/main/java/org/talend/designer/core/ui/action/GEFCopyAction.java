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
package org.talend.designer.core.ui.action;

import java.util.List;

import org.eclipse.gef.ui.actions.Clipboard;
import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.jface.action.IAction;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.actions.ActionFactory;
import org.talend.designer.core.i18n.Messages;
import org.talend.designer.core.ui.editor.TalendEditor;
import org.talend.designer.core.ui.editor.connections.ConnLabelEditPart;
import org.talend.designer.core.ui.editor.nodes.NodeLabelEditPart;
import org.talend.designer.core.ui.editor.nodes.NodePart;
import org.talend.designer.core.ui.editor.notes.NoteDirectEditManager;
import org.talend.designer.core.ui.editor.notes.NoteEditPart;

/**
 * Action that manage to create a connection from the context menu. A connection type is used to know which kind of
 * connection will be created. <br/>
 * 
 * $Id$
 * 
 */
public class GEFCopyAction extends SelectionAction {

    // public static final String ID = "org.talend.designer.core.ui.editor.action.NodesCopyAction"; //$NON-NLS-1$

    /**
     * Define the type of the connection and the workbench part who will manage the connection.
     * 
     * @param part
     * @param connecType
     */
    public GEFCopyAction(IWorkbenchPart part) {
        super(part);
        setId(ActionFactory.COPY.getId());
        setText(Messages.getString("NodesCopyAction.label")); //$NON-NLS-1$
        ISharedImages sharedImages = part.getSite().getWorkbenchWindow().getWorkbench().getSharedImages();
        setImageDescriptor(sharedImages.getImageDescriptor(ISharedImages.IMG_TOOL_COPY));
        setDisabledImageDescriptor(sharedImages.getImageDescriptor(ISharedImages.IMG_TOOL_COPY_DISABLED));
    }

    @Override
    protected boolean calculateEnabled() {
        List objects = getSelectedObjects();
        if (!objects.isEmpty()) {
            for (Object o : objects) {
                if (o instanceof NoteEditPart) {
                    return true;
                }
                if (o instanceof NodeLabelEditPart) {
                    return true;
                }
                if (o instanceof ConnLabelEditPart) {
                    return true;
                }
                if (!(o instanceof NodePart) && !(o instanceof NoteEditPart)) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.action.Action#run()
     */
    @Override
    public void run() {
        List objects = getSelectedObjects();
        if (!objects.isEmpty()) {
            Clipboard clipboard = Clipboard.getDefault();

            boolean noteTextActived = false;

            boolean connectionTextActived = false;

            boolean nodeLabelActived = false;

            if (objects.size() == 1) {
                if (objects.get(0) instanceof NoteEditPart) {
                    NoteDirectEditManager directEditManager = ((NoteEditPart) objects.get(0)).getDirectEditManager();
                    if (directEditManager != null && directEditManager.getCellEditor() != null) {
                        noteTextActived = true;
                    }
                } else if (objects.get(0) instanceof ConnLabelEditPart) {
                    ConnLabelEditPart connLabelEditPart = (ConnLabelEditPart) objects.get(0);
                    if (connLabelEditPart.getDirectEditManager() != null
                            && connLabelEditPart.getDirectEditManager().getTextControl() != null) {
                        connectionTextActived = true;
                    }
                } else if (objects.get(0) instanceof NodeLabelEditPart) {
                    NodeLabelEditPart nodeLabelEditPart = (NodeLabelEditPart) objects.get(0);
                    if (nodeLabelEditPart.getDirectEditManager() != null
                            && nodeLabelEditPart.getDirectEditManager().getCellEditor() != null) {
                        nodeLabelActived = true;
                    }
                }
            }

            if (noteTextActived) {

                Text text = ((NoteEditPart) objects.get(0)).getDirectEditManager().getTextControl();
                clipboard.setContents(text.getSelectionText());

            } else if (connectionTextActived) {

                Text text = ((ConnLabelEditPart) objects.get(0)).getDirectEditManager().getTextControl();
                clipboard.setContents(text.getSelectionText());

            } else if (nodeLabelActived) {
                Text text = (Text) ((NodeLabelEditPart) objects.get(0)).getDirectEditManager().getCellEditor()
                        .getControl();
                clipboard.setContents(text.getSelectionText());
            } else {
                clipboard.setContents(objects);
            }
        }

        // Refreshes the pasteAction's enable status.
        IWorkbenchPart part = getWorkbenchPart();
        if (part instanceof TalendEditor) {
            TalendEditor talendEditor = (TalendEditor) part;
            IAction action = talendEditor.getActionRegistry().getAction(ActionFactory.PASTE.getId());
            action.setEnabled(true);
        }
    }
}
