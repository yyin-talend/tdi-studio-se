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
package org.talend.designer.core.ui.action;

import org.eclipse.ui.IWorkbenchPart;
import org.talend.designer.core.i18n.Messages;
import org.talend.designer.core.ui.editor.cmd.SendToBackCommand;

/**
 */
public class SendToBackAction extends ZorderAction {

    public static final String ID = "org.talend.designer.core.ui.editor.action.SendToBackAction"; //$NON-NLS-1$

    private static final String TEXT = Messages.getString("SendToBackAction.sendToBack"); //$NON-NLS-1$

    public SendToBackAction(IWorkbenchPart part) {
        super(part);
        setId(ID);
        setText(TEXT);
    }

    @Override
    protected void createZOrderCommand() {
        zorderCommand = new SendToBackCommand(editPart);
    }
}
