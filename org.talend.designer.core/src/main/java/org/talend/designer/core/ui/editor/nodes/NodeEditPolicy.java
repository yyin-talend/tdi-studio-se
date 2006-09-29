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
package org.talend.designer.core.ui.editor.nodes;

import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.ComponentEditPolicy;
import org.eclipse.gef.requests.GroupRequest;
import org.talend.designer.core.ui.editor.cmd.DeleteNodeContainerCommand;
import org.talend.designer.core.ui.editor.nodecontainer.NodeContainer;
import org.talend.designer.core.ui.editor.process.Process;

/**
 * Edit policy that will manage the deletion of a node and the changement of status. <br/>
 * 
 * $Id$
 * 
 */
public class NodeEditPolicy extends ComponentEditPolicy {

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.gef.editpolicies.ComponentEditPolicy#getCommand(org.eclipse.gef.Request)
     */
    public Command getCommand(Request request) {
        return super.getCommand(request);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.gef.editpolicies.ComponentEditPolicy#createDeleteCommand(org.eclipse.gef.requests.GroupRequest)
     */
    protected Command createDeleteCommand(GroupRequest request) {
        Object parent = getHost().getParent().getModel();
        if (!(parent instanceof Process)) {
            return null;
        }
        if (((Node) getHost().getModel()).isReadOnly()) {
            return null;
        }
        DeleteNodeContainerCommand deleteCommand = new DeleteNodeContainerCommand((Process) parent,
                (NodeContainer) ((Node) getHost().getModel()).getNodeContainer());
        return deleteCommand;
    }
}
