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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.gef.ui.actions.Clipboard;
import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.actions.ActionFactory;
import org.talend.designer.core.i18n.Messages;
import org.talend.designer.core.ui.editor.TalendEditor;
import org.talend.designer.core.ui.editor.cmd.MultiplePasteCommand;
import org.talend.designer.core.ui.editor.cmd.NodesPasteCommand;
import org.talend.designer.core.ui.editor.cmd.NotesPasteCommand;
import org.talend.designer.core.ui.editor.nodes.NodePart;
import org.talend.designer.core.ui.editor.notes.NoteEditPart;

/**
 * DOC nrousseau class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 */
public class GEFPasteAction extends SelectionAction {

    /**
     * DOC nrousseau NodesPasteAction constructor comment.
     * 
     * @param part
     */
    public GEFPasteAction(IWorkbenchPart part) {
        super(part);
        setId(ActionFactory.PASTE.getId());
        setText(Messages.getString("NodesPasteAction.paste")); //$NON-NLS-1$
        ISharedImages sharedImages = part.getSite().getWorkbenchWindow().getWorkbench().getSharedImages();
        setImageDescriptor(sharedImages.getImageDescriptor(ISharedImages.IMG_TOOL_PASTE));
        setDisabledImageDescriptor(sharedImages.getImageDescriptor(ISharedImages.IMG_TOOL_PASTE_DISABLED));
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.gef.ui.actions.WorkbenchPartAction#calculateEnabled()
     */
    @Override
    protected boolean calculateEnabled() {
        Object o = Clipboard.getDefault().getContents();
        if (!(o instanceof List)) {
            return false;
        }
        List objects = (List) o;
        if (objects.isEmpty()) {
            return false;
        }
        for (Object currentObject : objects) {
            if (!(currentObject instanceof NodePart) && !(currentObject instanceof NoteEditPart)) {
                return false;
            }
        }
        return true;
    }

    @SuppressWarnings("unchecked")//$NON-NLS-1$
    public void run() {
        List<EditPart> partsList = (List<EditPart>) Clipboard.getDefault().getContents();
        if (partsList == null || partsList.isEmpty()) {
            return;
        }

        List<NodePart> nodeParts = new ArrayList<NodePart>();
        List<NoteEditPart> noteParts = new ArrayList<NoteEditPart>();

        for (Object o : partsList) {
            if (o instanceof NodePart) {
                nodeParts.add((NodePart) o);
            } else if (o instanceof NoteEditPart) {
                noteParts.add((NoteEditPart) o);
            }
        }

        TalendEditor editor = (TalendEditor) this.getWorkbenchPart();
        if (nodeParts.size() != 0 && noteParts.size() != 0) {
            
            MultiplePasteCommand mpc = new MultiplePasteCommand(nodeParts, noteParts, editor.getProcess());            
            execute(mpc);
        } else if (nodeParts.size() != 0) {
            NodesPasteCommand cmd = new NodesPasteCommand(nodeParts, editor.getProcess());
            execute(cmd);
        } else if (noteParts.size() != 0) {
            NotesPasteCommand cmd = new NotesPasteCommand(noteParts, editor.getProcess());
            execute(cmd);
        }
    }
}
