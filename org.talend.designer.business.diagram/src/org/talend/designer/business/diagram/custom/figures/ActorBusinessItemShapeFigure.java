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
 * $Id: ActionBusinessItemShapeFigure.java 1 2006-09-29 17:06:40 +0000 (ven, 29 sep 2006) nrousseau $
 * 
 */
public class ActorBusinessItemShapeFigure extends BusinessItemShapeFigure {

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.gmf.runtime.gef.ui.figures.NodeFigure#paintFigure(org.eclipse.draw2d.Graphics)
     */
    @Override
    protected void paintFigure(Graphics graphics) {
        Rectangle r = getInnerBounds();

        int headHeight = (int) (r.height * 0.25);

        int legsHeight = (int) (r.height * 0.3);
        int legsSpace = (int) (r.width * 0.15);

        int armsHeigth = (int) (headHeight + r.height * 0.2);
        int armsLength = (int) (legsSpace * 2);
        int armsOffset = (int) (r.height * 0.03);

        // head
        Rectangle head = new Rectangle(r.x + r.width / 2 - headHeight / 2, r.y, headHeight, headHeight);
        graphics.fillOval(head);
        graphics.drawOval(head);

        // body
        graphics.drawLine(r.x + r.width / 2, r.y + headHeight, r.x + r.width / 2, r.y + r.height - legsHeight);

        // legs
        graphics
                .drawLine(r.x + r.width / 2, r.y + r.height - legsHeight, r.x + r.width / 2 + legsSpace, r.y + r.height);
        graphics
                .drawLine(r.x + r.width / 2, r.y + r.height - legsHeight, r.x + r.width / 2 - legsSpace, r.y + r.height);

        // arms
        graphics.drawLine(r.x + r.width / 2, r.y + armsHeigth, r.x + r.width / 2 - armsLength, r.y + armsHeigth
                - armsOffset);
        graphics.drawLine(r.x + r.width / 2, r.y + armsHeigth, r.x + r.width / 2 + armsLength, r.y + armsHeigth
                - armsOffset);
    }
}
