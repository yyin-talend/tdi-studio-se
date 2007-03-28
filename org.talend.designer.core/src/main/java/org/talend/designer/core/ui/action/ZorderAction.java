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

import java.util.List;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.ui.IWorkbenchPart;
import org.talend.designer.core.ui.editor.cmd.ZorderCommand;
import org.talend.designer.core.ui.editor.notes.NoteEditPart;

/**
 */
public abstract class ZorderAction extends SelectionAction {

    protected ZorderCommand zorderCommand;

    protected EditPart editPart;

    public ZorderAction(IWorkbenchPart part) {
        super(part);
    }

    @Override
    protected boolean calculateEnabled() {
        List editparts = getSelectedObjects();
        if (editparts.size() == 1) {
            if (editparts.get(0) instanceof NoteEditPart) {
                editPart = (EditPart) editparts.get(0);
                createZOrderCommand();
                return zorderCommand.canExecute();
            }
        }
        return false;
    }

    @Override
    public void run() {
        execute(zorderCommand);
    }

    protected abstract void createZOrderCommand();
}
