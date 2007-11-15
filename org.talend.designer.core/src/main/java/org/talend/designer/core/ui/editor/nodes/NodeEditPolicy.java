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
package org.talend.designer.core.ui.editor.nodes;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.ComponentEditPolicy;
import org.eclipse.gef.requests.GroupRequest;
import org.talend.designer.core.ui.editor.cmd.DeleteNodeContainerCommand;
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
        List<Node> nodeList = new ArrayList<Node>();
        for (int i = 0; i < request.getEditParts().size(); i++) {
            if (request.getEditParts().get(i) instanceof NodePart) {
                nodeList.add(((Node) ((NodePart) request.getEditParts().get(i)).getModel()));
            }
        }

        DeleteNodeContainerCommand deleteCommand = new DeleteNodeContainerCommand((Process) parent, nodeList);
        return deleteCommand;
    }
}
