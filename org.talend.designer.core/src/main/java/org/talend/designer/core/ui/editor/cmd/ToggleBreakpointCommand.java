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

import org.eclipse.gef.commands.Command;
import org.talend.core.CorePlugin;
import org.talend.core.context.Context;
import org.talend.core.model.process.INode;
import org.talend.core.model.process.IProcess;

/**
 * Command that toggle a breakpoint on a node. <br/>
 * 
 * $Id$
 * 
 */
public class ToggleBreakpointCommand extends Command {

    private IProcess process;

    private INode node;

    /**
     * Constructs a new ToggleBreakpointCommand.
     */
    public ToggleBreakpointCommand(IProcess process, INode node) {
        super();

        this.process = process;
        this.node = node;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.gef.commands.Command#execute()
     */
    @Override
    public void execute() {
        Context context = CorePlugin.getContext();
        if (context.getBreakpointNodes(process).contains(node)) {
            context.removeBreakpoint(process, node);
        } else {
            context.addBreakpoint(process, node);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.gef.commands.Command#undo()
     */
    @Override
    public void undo() {
        execute();
    }
}
