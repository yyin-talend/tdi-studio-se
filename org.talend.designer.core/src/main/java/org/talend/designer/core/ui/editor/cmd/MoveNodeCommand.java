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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.commands.Command;
import org.eclipse.ui.PlatformUI;
import org.talend.designer.core.i18n.Messages;
import org.talend.designer.core.ui.MultiPageTalendEditor;
import org.talend.designer.core.ui.editor.nodes.Node;

/**
 * Move a given node to another location. <br/>
 * 
 * $Id$
 * 
 */
public class MoveNodeCommand extends Command {

    private Node node;

    private Point newPos;

    private Point oldPos;

    /**
     * Move the given node to another location.
     * 
     * @param node
     * @param newPos
     */
    public MoveNodeCommand(Node node, Point newPos) {
        this.node = node;
        this.newPos = newPos;
        setLabel(Messages.getString("MoveNodeCommand.Label")); //$NON-NLS-1$
    }

    @SuppressWarnings("unchecked")//$NON-NLS-1$
    @Override
    public boolean canExecute() {
        Rectangle movingRect = new Rectangle(newPos, node.getSize());
        for (Node currentNode : (List<Node>) node.getProcess().getGraphicalNodes()) {
            Rectangle currentRect = new Rectangle(currentNode.getLocation(), currentNode.getSize());
            if ((currentRect.intersects(movingRect)) && (!isSelected(currentNode))) {
                return false;
            }
        }
        return true;
    }

    private boolean isSelected(Node currentNode) {
        MultiPageTalendEditor multiPageTalendEditor = (MultiPageTalendEditor) PlatformUI.getWorkbench()
                .getActiveWorkbenchWindow().getActivePage().getActiveEditor();
        GraphicalViewer viewer = multiPageTalendEditor.getTalendEditor().getViewer();
        for (EditPart editPart : (List<EditPart>) viewer.getSelectedEditParts()) {
            if (editPart.getModel().equals(currentNode)) {
                return true;
            }
        }

        return false;
    }

    public void execute() {
        oldPos = this.node.getLocation();
        this.node.setLocation(newPos);
    }

    public void undo() {
        this.node.setLocation(oldPos);
    }

    public void redo() {
        this.node.setLocation(newPos);
    }
}
