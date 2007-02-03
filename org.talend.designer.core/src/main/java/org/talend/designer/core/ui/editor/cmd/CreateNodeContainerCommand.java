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
package org.talend.designer.core.ui.editor.cmd;

import java.util.List;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.commands.Command;
import org.talend.designer.core.i18n.Messages;
import org.talend.designer.core.ui.editor.TalendEditor;
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
public class CreateNodeContainerCommand extends Command {

    private Process process;

    private NodeContainer nodeContainer;

    private Point location;

    /**
     * Create the node on the given diagram.
     * 
     * @param diagram
     * @param node
     * @param location
     */
    public CreateNodeContainerCommand(Process process, NodeContainer nodeContainer, Point location) {
        this.process = process;
        this.nodeContainer = nodeContainer;
        this.location = location;

        if (process.isGridEnabled()) {
            // replace the component to set it on the grid if it's enabled
            int tempVar = location.x / TalendEditor.GRID_SIZE;
            this.location.x = tempVar * TalendEditor.GRID_SIZE;
            tempVar = location.y / TalendEditor.GRID_SIZE;
            this.location.y = tempVar * TalendEditor.GRID_SIZE;
        }

        setLabel(Messages.getString("CreateNodeCommand.0")); //$NON-NLS-1$
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
//        process.checkProcess();
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
