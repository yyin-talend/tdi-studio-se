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

import org.eclipse.gef.commands.Command;
import org.talend.core.model.process.EConnectionType;
import org.talend.core.model.process.IExternalNode;
import org.talend.designer.core.i18n.Messages;
import org.talend.designer.core.ui.editor.connections.Connection;
import org.talend.designer.core.ui.editor.process.Process;

/**
 * Command that change the label of a connection. <br/>
 * 
 * $Id$
 * 
 */
public class ChangeConnTextCommand extends Command {

    private String newName;

    private String oldName;

    private Connection connection;

    /**
     * Initialisation of the command with the label of the connection and the new text.
     * 
     * @param connectionLabel Gef object that contains the label of the connection.
     * @param newName new name of the connection label
     */
    public ChangeConnTextCommand(Connection connection, String newName) {
        this.connection = connection;
        if (newName != null) {
            this.newName = newName;
        } else {
            this.newName = ""; //$NON-NLS-1$
        }
        setLabel(Messages.getString("ChangeConnTextCommand.1")); //$NON-NLS-1$
    }

    public void execute() {
        oldName = connection.getName();
        connection.setName(newName);

        if (connection.getLineStyle().equals(EConnectionType.FLOW_MAIN)
                || connection.getLineStyle().equals(EConnectionType.FLOW_REF)) {
            connection.getSource().getProcess().removeUniqueConnectionName(oldName);
            connection.getSource().getProcess().addUniqueConnectionName(newName);
        }

        IExternalNode externalNode = connection.getTarget().getExternalNode();
        if (externalNode != null) {
            externalNode.renameInputConnection(oldName, newName);
        }
        externalNode = connection.getSource().getExternalNode();
        if (externalNode != null) {
            externalNode.renameOutputConnection(oldName, newName);
        }
        ((Process) connection.getSource().getProcess()).checkProcess();
    }

    public void redo() {
        execute();
    }

    public void undo() {
        connection.setName(oldName);

        if (connection.getLineStyle().equals(EConnectionType.FLOW_MAIN)
                || connection.getLineStyle().equals(EConnectionType.FLOW_REF)) {
            connection.getSource().getProcess().removeUniqueConnectionName(newName);
            connection.getSource().getProcess().addUniqueConnectionName(oldName);
        }

        IExternalNode externalNode = connection.getTarget().getExternalNode();
        if (externalNode != null) {
            externalNode.renameInputConnection(newName, oldName);
        }
        externalNode = connection.getSource().getExternalNode();
        if (externalNode != null) {
            externalNode.renameOutputConnection(newName, oldName);
        }
        ((Process) connection.getSource().getProcess()).checkProcess();
    }
}
