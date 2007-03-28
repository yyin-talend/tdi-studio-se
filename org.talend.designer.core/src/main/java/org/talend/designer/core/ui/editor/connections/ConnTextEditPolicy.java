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
package org.talend.designer.core.ui.editor.connections;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.DirectEditPolicy;
import org.eclipse.gef.requests.DirectEditRequest;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.talend.core.model.process.EConnectionType;
import org.talend.designer.core.i18n.Messages;
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
        if (connec.getLineStyle().equals(EConnectionType.FLOW_MAIN)
                || connec.getLineStyle().equals(EConnectionType.FLOW_REF)) {
            if (!((Node) connec.getSource()).getProcess().checkValidConnectionName(labelText)) {
                String message = Messages.getString("ConnectionCreateAction.errorCreateConnectionName", labelText); //$NON-NLS-1$
                MessageDialog.openError(getHost().getViewer().getControl().getShell(), Messages
                        .getString("ConnTextEditPolicy.ErrorTitle"), message); //$NON-NLS-1$
                return null;
            }
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
