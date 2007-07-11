// ============================================================================
//
// Talend Community Edition
//
// Copyright (C) 2006 Talend – www.talend.com
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
package org.talend.designer.business.diagram.custom.util;

import org.eclipse.swt.SWT;

/**
 * DOC mhelleboid class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 */
public class KeyHelper {

    public boolean ctrlPressed(int keyCode) {
        return modifierPressed(keyCode, SWT.CTRL);
    }

    public boolean altPressed(int keyCode) {
        return modifierPressed(keyCode, SWT.ALT);
    }

    public boolean shiftPressed(int keyCode) {
        return modifierPressed(keyCode, SWT.SHIFT);
    }

    public boolean anyModifierPressed(int keyCode) {
        return ctrlPressed(keyCode) || shiftPressed(keyCode) || altPressed(keyCode);
    }

    public boolean cursorPressed(int keyCode) {
        boolean arrow = false;
        arrow = arrow || keyCode == SWT.ARROW_UP;
        arrow = arrow || keyCode == SWT.ARROW_DOWN;
        arrow = arrow || keyCode == SWT.ARROW_LEFT;
        arrow = arrow || keyCode == SWT.ARROW_RIGHT;
        return arrow;
    }

    private boolean modifierPressed(int keyCode, int modifier) {
        return (keyCode & modifier) != 0;
    }
}
