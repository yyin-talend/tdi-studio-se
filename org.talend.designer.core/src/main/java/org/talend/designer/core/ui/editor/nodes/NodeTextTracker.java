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
package org.talend.designer.core.ui.editor.nodes;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.tools.DragEditPartsTracker;

/**
 * Tracker that allow to move the node's label. <br/>
 * 
 * $Id$
 * 
 */
public class NodeTextTracker extends DragEditPartsTracker {

    EditPart part;

    /**
     * Gives the Node Part and the NodeLabelEditPart to the tracker.
     * 
     * @param source
     * @param node
     */
    public NodeTextTracker(EditPart source, EditPart part) {
        super(source);
        this.part = part;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.gef.tools.TargetingTool#getTargetEditPart()
     */
    protected EditPart getTargetEditPart() {
        return part;
    }
}
