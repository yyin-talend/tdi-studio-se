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
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.actions.ActionFactory;
import org.talend.designer.core.i18n.Messages;
import org.talend.designer.core.ui.editor.TalendEditor;
import org.talend.designer.core.ui.editor.cmd.NodesPasteCommand;
import org.talend.designer.core.ui.editor.nodes.NodePart;

/**
 * DOC nrousseau class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 */
public class NodesPasteAction extends SelectionAction {

    /**
     * DOC nrousseau NodesPasteAction constructor comment.
     * 
     * @param part
     */
    public NodesPasteAction(IWorkbenchPart part) {
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
            if (!(currentObject instanceof NodePart)) {
                return false;
            }
        }
        return true;
    }

    @SuppressWarnings("unchecked") //$NON-NLS-1$
    public void run() {
        List<NodePart> nodeParts = (List<NodePart>) Clipboard.getDefault().getContents();
        TalendEditor editor = (TalendEditor) this.getWorkbenchPart();
        NodesPasteCommand cmd = new NodesPasteCommand(nodeParts, editor.getProcess());
        execute(cmd);
    }
}
