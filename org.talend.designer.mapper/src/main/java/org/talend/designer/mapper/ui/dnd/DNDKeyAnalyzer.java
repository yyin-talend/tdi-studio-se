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
package org.talend.designer.mapper.ui.dnd;

import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTargetEvent;

/**
 * DOC amaumont class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 */
public class DNDKeyAnalyzer {

    private boolean ctrlPressed;

    private boolean shiftPressed;

    public DNDKeyAnalyzer(DropTargetEvent event) {
        super();
        analyze(event);
    }

    /**
     * DOC amaumont Comment method "analyze".
     * 
     * @param event
     * @param mapperManager
     */
    private void analyze(DropTargetEvent event) {
        if ((event.detail & DND.DROP_COPY) != 0) { // Ctrl key
            // System.out.println("Ctrl key");
            ctrlPressed = true;
            shiftPressed = false;
        } else if ((event.detail & DND.DROP_LINK) != 0) { // Ctrl + Shift keys
            // System.out.println("Ctrl + Shift keys");
            ctrlPressed = true;
            shiftPressed = true;
        } else if ((event.detail & DND.DROP_MOVE) != 0) { // Shift key
            // System.out.println("Shift key");
            ctrlPressed = false;
            shiftPressed = true;
        } else if ((event.detail & DND.DROP_DEFAULT) != 0) { // no key
            // System.out.println("Default");
            ctrlPressed = false;
            shiftPressed = false;
        }

    }

    public boolean isCtrlPressed() {
        return this.ctrlPressed;
    }

    public boolean isShiftPressed() {
        return this.shiftPressed;
    }

}
