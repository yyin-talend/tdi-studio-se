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

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.commands.Command;
import org.talend.designer.core.i18n.Messages;
import org.talend.designer.core.ui.editor.nodes.NodeLabel;

/**
 * Command that moves the label of a node. <br/>
 * 
 * $Id$
 * 
 */
public class MoveNodeLabelCommand extends Command {

    NodeLabel nodeLabel;

    Point newPos;

    Point oldOffset;

    Point textOffset;

    boolean nodeSelected;

    /**
     * Move the label to the given position. The selection of the node will change the calculation of the new offset.
     * 
     * @param nodeLabel
     * @param newPos
     * @param nodeSelected
     */
    public MoveNodeLabelCommand(NodeLabel nodeLabel, Point newPos, boolean nodeSelected) {
        this.nodeLabel = nodeLabel;
        this.newPos = newPos;
        this.nodeSelected = nodeSelected;
        oldOffset = nodeLabel.getOffset().getCopy();
        setLabel(Messages.getString("MoveNodeLabelCommand.Label")); //$NON-NLS-1$
    }

    public void execute() {
        Point oldPos;

        oldPos = nodeLabel.getLocation();
        textOffset = nodeLabel.getTextOffset();
        // parent.translateToAbsolute(newOffset);
        Point newOffset = oldOffset.getCopy();
        if (!nodeSelected) {
            newOffset.x += newPos.x - oldPos.x - oldOffset.x - textOffset.x;
            newOffset.y += newPos.y - oldPos.y - oldOffset.y - textOffset.y;
        }
        nodeLabel.setOffset(newOffset);
    }

    public void redo() {
        execute();
    }

    public void undo() {
        nodeLabel.setOffset(oldOffset);
    }
}
