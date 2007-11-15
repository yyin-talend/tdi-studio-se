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
package org.talend.designer.core.ui.editor.connections;

import java.util.Iterator;
import java.util.List;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.DirectEditPolicy;
import org.eclipse.gef.requests.DirectEditRequest;
import org.eclipse.jface.dialogs.MessageDialog;
import org.talend.core.model.process.EConnectionType;
import org.talend.core.model.process.IConnection;
import org.talend.core.model.process.IConnectionCategory;
import org.talend.designer.core.i18n.Messages;
import org.talend.designer.core.model.process.ConnectionManager;
import org.talend.designer.core.ui.editor.cmd.ChangeConnTextCommand;
import org.talend.designer.core.ui.editor.nodes.Node;

/**
 * Policy to edit the label of the connection. <br/>
 * 
 * $Id$
 * 
 */
public class ConnTextEditPolicy extends DirectEditPolicy {

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.gef.editpolicies.DirectEditPolicy#getDirectEditCommand(org.eclipse.gef.requests.DirectEditRequest)
     */
    protected Command getDirectEditCommand(DirectEditRequest edit) {
        String labelText = (String) edit.getCellEditor().getValue();
        ConnLabelEditPart labelPart = (ConnLabelEditPart) getHost();
        Connection connec = (Connection) getHost().getParent().getModel();

        if (!ConnectionManager.canRename(connec.getSource(), connec.getTarget(), connec.getLineStyle(), labelText)) {
            String message = Messages.getString("ConnectionCreateAction.errorCreateConnectionName", labelText); //$NON-NLS-1$
            MessageDialog.openError(getHost().getViewer().getControl().getShell(), Messages
                    .getString("ConnTextEditPolicy.ErrorTitle"), message); //$NON-NLS-1$
            return null;
        }

        ChangeConnTextCommand command = new ChangeConnTextCommand((Connection) labelPart.getParent().getModel(),
                labelText);
        return command;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.gef.editpolicies.DirectEditPolicy#showCurrentEditValue(org.eclipse.gef.requests.DirectEditRequest)
     */
    protected void showCurrentEditValue(DirectEditRequest request) {
    }

}
