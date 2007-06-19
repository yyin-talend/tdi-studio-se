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

import org.eclipse.gef.commands.Command;
import org.talend.designer.core.i18n.Messages;
import org.talend.designer.core.ui.editor.connections.Connection;
import org.talend.designer.core.ui.editor.nodes.Node;
import org.talend.designer.core.ui.editor.process.Process;

/**
 * DOC nrousseau class global comment. Detailled comment <br/>
 * 
 */
public class ChangeMergeOrderCommand extends Command {

    private Node mergeNode;

    private List<Connection> newInputConnections;

    private List<Connection> oldInputConnections;

    public ChangeMergeOrderCommand(Node mergeNode, List<Connection> inputConnections) {
        this.mergeNode = mergeNode;
        this.newInputConnections = inputConnections;
        this.oldInputConnections = (List<Connection>) mergeNode.getIncomingConnections();
        if (newInputConnections.size() != oldInputConnections.size()) {
            throw new IllegalArgumentException("new connection list should have the same size as the old one"); //$NON-NLS-1$
        }
        this.setLabel(Messages.getString("ChangeMergeOrderCommand.label")); //$NON-NLS-1$
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.gef.commands.Command#execute()
     */
    @Override
    public void execute() {
        mergeNode.setIncomingConnections(newInputConnections);
        newInputConnections.get(0).updateAllId();
        ((Process) mergeNode.getProcess()).checkStartNodes();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.gef.commands.Command#undo()
     */
    @Override
    public void undo() {
        mergeNode.setIncomingConnections(oldInputConnections);
        oldInputConnections.get(0).updateAllId();
        ((Process) mergeNode.getProcess()).checkStartNodes();
    }

}
