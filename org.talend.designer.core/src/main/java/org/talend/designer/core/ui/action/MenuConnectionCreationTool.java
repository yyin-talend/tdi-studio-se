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

import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.requests.CreationFactory;
import org.eclipse.gef.tools.ConnectionCreationTool;
import org.talend.designer.core.ui.editor.nodes.NodePart;

/**
 * This tool is used to create a connection in the context menu. After the constructor, the function
 * "performConnectionStartWith" must be called, it will define the first point for the connection. <br/>
 * 
 * $Id$
 * 
 */
public class MenuConnectionCreationTool extends ConnectionCreationTool {

    public MenuConnectionCreationTool(CreationFactory factory) {
        super(factory);
        setUnloadWhenFinished(true);
    }

    /**
     * The node part of the source must be given in argument. This will define the fist point of the connection.
     * 
     * @param sourcePart the edit part that will be the source of the connection
     */
    public void performConnectionStartWith(EditPart sourcePart) {
        setConnectionSource(sourcePart);
        updateTargetRequest();
        Command cmd = ((NodePart) sourcePart).getCommand(getTargetRequest());
        setCurrentCommand(cmd);
        setState(STATE_CONNECTION_STARTED);
    }
}
