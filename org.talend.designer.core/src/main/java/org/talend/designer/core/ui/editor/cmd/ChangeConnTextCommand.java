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

import org.eclipse.gef.commands.Command;
import org.talend.core.model.process.EConnectionType;
import org.talend.core.model.process.IConnectionCategory;
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
        setLabel(Messages.getString("ChangeConnTextCommand.Label")); //$NON-NLS-1$
    }

    public void execute() {
        oldName = connection.getName();
        connection.setName(newName);

        if (connection.getLineStyle().hasConnectionCategory(IConnectionCategory.UNIQUE_NAME)) {
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

        if (connection.getLineStyle().hasConnectionCategory(IConnectionCategory.UNIQUE_NAME)) {
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
