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

import org.eclipse.draw2d.FocusEvent;
import org.eclipse.draw2d.FocusListener;
import org.eclipse.draw2d.IFigure;
import org.eclipse.gef.internal.ui.palette.editparts.DrawerEditPart;
import org.eclipse.gef.internal.ui.palette.editparts.DrawerFigure;
import org.eclipse.gef.palette.PaletteDrawer;

/**
 * 
 */
public class TalendDrawerEditPart extends DrawerEditPart {

    public TalendDrawerEditPart(PaletteDrawer drawer) {
        super(drawer);
    }

    public IFigure createFigure() {
        if (getParent() instanceof TalendDrawerEditPart) {
            DrawerFigure fig = new TalendDrawerFigure(getViewer().getControl()) {
                IFigure buildTooltip() {
                    return createToolTip();
                }
            };
            fig.setExpanded(getDrawer().isInitiallyOpen());
            fig.setPinned(getDrawer().isInitiallyPinned());

            fig.getCollapseToggle().addFocusListener(new FocusListener.Stub() {
                public void focusGained(FocusEvent fe) {
                    getViewer().select(TalendDrawerEditPart.this);
                }
            });

            return fig;
        }
        return super.createFigure();
    }

}
