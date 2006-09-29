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
package org.talend.designer.core.ui.action;

import java.util.List;

import org.eclipse.gef.ui.actions.Clipboard;
import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.actions.ActionFactory;
import org.talend.designer.core.ui.editor.nodes.NodePart;

/**
 * Action that manage to create a connection from the context menu. A connection type is used to know which kind of
 * connection will be created. <br/>
 * 
 * $Id$
 * 
 */
public class NodesCopyAction extends SelectionAction {

    // public static final String ID = "org.talend.designer.core.ui.editor.action.NodesCopyAction"; //$NON-NLS-1$

    /**
     * Define the type of the connection and the workbench part who will manage the connection.
     * 
     * @param part
     * @param connecType
     */
    public NodesCopyAction(IWorkbenchPart part) {
        super(part);
        setId(ActionFactory.COPY.getId());
        setText("Copy");
        ISharedImages sharedImages = part.getSite().getWorkbenchWindow().getWorkbench().getSharedImages();
        setImageDescriptor(sharedImages.getImageDescriptor(ISharedImages.IMG_TOOL_COPY));
        setDisabledImageDescriptor(sharedImages.getImageDescriptor(ISharedImages.IMG_TOOL_COPY_DISABLED));
    }

    @Override
    protected boolean calculateEnabled() {
        List objects = getSelectedObjects();
        if (!objects.isEmpty()) {
            for (Object o : objects) {
                if (!(o instanceof NodePart)) {
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
    public void run() {
        List objects = getSelectedObjects();
        if (!objects.isEmpty()) {
            Clipboard.getDefault().setContents(objects);
        }
    }
}
