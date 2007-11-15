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
