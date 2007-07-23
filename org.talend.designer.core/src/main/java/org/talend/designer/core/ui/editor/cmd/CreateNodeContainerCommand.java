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

import java.util.List;

import org.eclipse.draw2d.geometry.Point;
import org.talend.designer.core.i18n.Messages;
import org.talend.designer.core.ui.editor.nodecontainer.NodeContainer;
import org.talend.designer.core.ui.editor.nodes.Node;
import org.talend.designer.core.ui.editor.process.Process;
import org.talend.designer.core.ui.views.problems.Problems;

/**
 * Command that create a new node. <br/>
 * 
 * $Id$
 * 
 */
public class CreateNodeContainerCommand extends CreateCommand {

    private NodeContainer nodeContainer;

    /**
     * Create the node on the given diagram.
     * 
     * @param diagram
     * @param node
     * @param location
     */
    public CreateNodeContainerCommand(Process process, NodeContainer nodeContainer, Point location) {
        super(Messages.getString("CreateNodeCommand.Label"), process, location); //$NON-NLS-1$
        this.nodeContainer = nodeContainer;
    }

    @SuppressWarnings("unchecked") //$NON-NLS-1$
    @Override
    public boolean canExecute() {
        for (Node currentNode : (List<Node>) process.getGraphicalNodes()) {
            if ((currentNode.getLocation().x == location.x) && (currentNode.getLocation().y == location.y)) {
                return false;
            }
        }
        return true;
    }

    public void execute() {
        if (this.location != null) {
            this.nodeContainer.getNode().setLocation(this.location);
        }
        this.process.addNodeContainer(this.nodeContainer);
        process.checkStartNodes();
        nodeContainer.getNode().checkAndRefreshNode();
    }

    public void undo() {
        this.process.removeNodeContainer(this.nodeContainer);
//        process.checkProcess();
        process.checkStartNodes();
        Problems.clearAll(nodeContainer.getNode());
        Problems.refreshView();
    }

    public void redo() {
        this.execute();
    }
}
