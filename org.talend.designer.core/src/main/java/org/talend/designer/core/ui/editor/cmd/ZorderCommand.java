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

import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.Command;
import org.talend.core.model.process.Element;
import org.talend.designer.core.ui.editor.process.Process;

/**
 */
public abstract class ZorderCommand extends Command {
    
    private EditPart editPart;

    protected Element element;
    protected Process process;
    protected int oldIndex = -1;
    protected int size;
    
    public ZorderCommand(EditPart editPart) {
        this.editPart = editPart;
        
        element = (Element) editPart.getModel();
        process = (Process) editPart.getParent().getModel();
        oldIndex = process.getElements().indexOf(element);
        size = process.getElements().size();
        
        if (oldIndex == -1) {
            throw new IllegalStateException();
        }
    }

    @Override
    public void execute() {
        process.getElements().remove(element);
        process.getElements().add(getNewIndex(), element);
        
        editPart.getParent().refresh();
    }

    @Override
    public void undo() {
        Element element = (Element) editPart.getModel();
        Process process = (Process) editPart.getParent().getModel();
        
        process.getElements().remove(element);
        process.getElements().add(oldIndex, element);
        
        editPart.getParent().refresh();
    }
    
    protected abstract int getNewIndex();

    @Override
    public boolean canExecute() {
        return !process.isReadOnly() && subCanExecute();
    }

    protected abstract boolean subCanExecute();
    
}
