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
package org.talend.designer.core.ui.editor.palette;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.internal.ui.palette.editparts.DrawerFigure;
import org.eclipse.swt.widgets.Control;

/**
 * 
 */
public class TalendDrawerFigure extends DrawerFigure {

    private static final int X_OFFSET = 17;

    public TalendDrawerFigure(Control control) {
        super(control);
    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle(bounds.x + X_OFFSET, bounds.y, bounds.width, bounds.height);
    }
}
