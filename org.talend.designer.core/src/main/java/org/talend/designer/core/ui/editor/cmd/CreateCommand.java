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

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.commands.Command;
import org.talend.designer.core.ui.editor.TalendEditor;
import org.talend.designer.core.ui.editor.process.Process;

/**
 */
public abstract class CreateCommand extends Command {

    protected Process process;
    protected Point location;

    public CreateCommand(String label, Process process, Point location) {
        super(label);
        this.process = process;
        this.location = location;

        if (process.isGridEnabled()) {
            // replace the component to set it on the grid if it's enabled
            int tempVar = location.x / TalendEditor.GRID_SIZE;
            this.location.x = tempVar * TalendEditor.GRID_SIZE;
            tempVar = location.y / TalendEditor.GRID_SIZE;
            this.location.y = tempVar * TalendEditor.GRID_SIZE;
        }
}

}
