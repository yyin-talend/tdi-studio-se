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
package org.talend.designer.business.diagram.custom.figures;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Rectangle;

/**
 * DOC mhelleboid class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 */
public class DatabaseBusinessItemShapeFigure extends BusinessItemShapeFigure {

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.gmf.runtime.gef.ui.figures.NodeFigure#paintFigure(org.eclipse.draw2d.Graphics)
     */
    @Override
    protected void paintFigure(Graphics g) {
        Rectangle r = getInnerBounds();

        int ellipseHeight = (int) (r.height * 0.25);

        Rectangle ellipse = new Rectangle(r.x, r.y, r.width, ellipseHeight);
        Rectangle middle = new Rectangle(r.x, r.y + (ellipseHeight / 2), r.width, r.height - ellipseHeight);
        Rectangle lowerArc = new Rectangle(r.x, r.y + r.height - ellipseHeight, r.width, ellipseHeight);

        g.fillOval(ellipse);
        g.fillRectangle(middle);
        g.fillArc(lowerArc, 180, 180);

        g.drawOval(ellipse.x, ellipse.y, ellipse.width, ellipse.height);
        g.drawLine(middle.x, middle.y, middle.x, middle.y + middle.height);
        g.drawLine(middle.x + middle.width, middle.y, middle.x + middle.width, middle.y + middle.height);
        g.drawArc(lowerArc, 180, 180);
    }

}
