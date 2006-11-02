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
package org.talend.designer.core.ui.editor;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.editparts.GridLayer;
import org.eclipse.swt.graphics.Color;

/**
 * Grid that will be used for the designer. (modification of the default grid to have black points)
 * 
 * $Id$
 * 
 */
public class TalendGridLayer extends GridLayer {

    public static final Color GRID_COLOR = ColorConstants.black;

    public TalendGridLayer() {
        super();
        setForegroundColor(GRID_COLOR);
    }

    @Override
    protected void paintGrid(Graphics g) {
        paintGrid(g, this, origin, gridX, gridY);
    }

    protected void paintGrid(Graphics g, IFigure f, org.eclipse.draw2d.geometry.Point origin, int distanceX, int distanceY) {
        Rectangle clip = g.getClip(Rectangle.SINGLETON);

        if (distanceX > 0 && distanceY > 0) {
            if (origin.x >= clip.x) {
                while (origin.x - distanceX >= clip.x) {
                    origin.x -= distanceX;
                }
            } else {
                while (origin.x < clip.x) {
                    origin.x += distanceX;
                }
            }

            if (origin.y >= clip.y) {
                while (origin.y - distanceY >= clip.y) {
                    origin.y -= distanceY;
                }
            } else {
                while (origin.y < clip.y) {
                    origin.y += distanceY;
                }
            }

            for (int i = origin.x; i < clip.x + clip.width; i += distanceX) {
                for (int j = origin.y; j < clip.y + clip.height; j += distanceY) {
                    g.drawPoint(i, j);
                }
            }
        }
    }
}
