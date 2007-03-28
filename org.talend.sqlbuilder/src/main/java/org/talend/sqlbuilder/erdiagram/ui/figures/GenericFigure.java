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
package org.talend.sqlbuilder.erdiagram.ui.figures;

import org.eclipse.draw2d.Shape;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;

/**
 * DOC qzhang  class global comment. Detailled comment
 * <br/>
 *
 * $Id: talend.epf 1 2006-09-29 17:06:40 +0000 (ææäº, 29 ä¹æ 2006) nrousseau $
 *
 */
public abstract class GenericFigure extends Shape {

    protected boolean fLocalCoordinates = true;

    private boolean useNotation2X = false;

    public int getShadow() {
        return 3;
    }

    /**
     * Returns <code>true</code> if this figure uses local coordinates. This means its children are placed relative to
     * this figure's top-left corner.
     * 
     * @since 2.0
     */
    protected boolean useLocalCoordinates() {
        return fLocalCoordinates;
    }

    protected void setUseLocalCoordinates(boolean local) {
        fLocalCoordinates = local;
    }

    public void setBounds(Rectangle rect) {
        int x = bounds.x, y = bounds.y;

        boolean resize = (rect.width != bounds.width) || (rect.height != bounds.height), translate = (rect.x != x)
                || (rect.y != y);

        if (isVisible() && (resize || translate)) {
            erase();
        }
        if (translate) {
            int dx = rect.x - x;
            int dy = rect.y - y;
            primTranslate(dx, dy);
        }
        bounds.width = rect.width;
        bounds.height = rect.height;
        if (resize) {
            invalidate();
        }
        if (resize || translate) {
            fireFigureMoved();
            repaint();
        }
    }

    /**
     * Returns a Color the same as the passed color in a lighter hue.
     * 
     * @since 2.0
     */
    public static Color lighter(Color darker, int index, int total) {
        int r = darker.getRed(), g = darker.getGreen(), b = darker.getBlue();

        float percent = (float) index / (float) total;
        return ColorUtilities.getColor((int) (r * percent), (int) (g * percent), (int) (b * percent));
    }

    // TODO pour debug uniquement
    public void setIcon(Image image) {

    }

    public Rectangle getConnectionBounds() {
        return getBounds().getCopy();
    }

    protected boolean isNotation1X() {
        return !useNotation2X;
    }

    protected boolean isNotation2X() {
        return useNotation2X;
    }

    public void setNotation2X(boolean value) {
        useNotation2X = value;
    }
}