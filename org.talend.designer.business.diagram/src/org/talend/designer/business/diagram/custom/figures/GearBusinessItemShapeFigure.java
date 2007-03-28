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
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.draw2d.geometry.Rectangle;

/**
 * DOC mhelleboid class global comment. Detailled comment <br/>
 * 
 * $Id: ActionBusinessItemShapeFigure.java 1 2006-09-29 17:06:40 +0000 (ven, 29 sep 2006) nrousseau $
 * 
 */
public class GearBusinessItemShapeFigure extends BusinessItemShapeFigure {

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.gmf.runtime.gef.ui.figures.NodeFigure#paintFigure(org.eclipse.draw2d.Graphics)
     */
    @Override
    protected void paintFigure(Graphics graphics) {
        Rectangle r = getInnerBounds();

        int nbTeeth = 8;
        double teethAngle = 0.23;
        double innerExternalRatio = 0.80;

        Point center = new Point(r.x + r.width / 2, r.y + r.height / 2);
        int horizontalExternalRadius = r.width / 2;
        int verticalExternalRadius = r.height / 2;
        int horizontalInnerRadius = (int) (horizontalExternalRadius * innerExternalRatio);
        int verticalInnerRadius = (int) (verticalExternalRadius * innerExternalRatio);

        PointList pointList = new PointList();

        boolean externalFirst = false;
        for (double i = 0; i < 2 * Math.PI; i = i + Math.PI * 2 / (nbTeeth * 2)) {
            double j = (externalFirst ? i - Math.PI / nbTeeth * teethAngle : i + Math.PI / nbTeeth * teethAngle);
            Point externalPoint = center.getTranslated((int) (Math.cos(j) * horizontalExternalRadius), (int) (Math
                    .sin(j) * verticalExternalRadius));
            Point internalPoint = center.getTranslated((int) (Math.cos(i) * horizontalInnerRadius),
                    (int) (Math.sin(i) * verticalInnerRadius));
            if (externalFirst) {
                pointList.addPoint(externalPoint);
                pointList.addPoint(internalPoint);
            } else {
                pointList.addPoint(internalPoint);
                pointList.addPoint(externalPoint);
            }
            externalFirst = !externalFirst;
        }

        graphics.drawPolygon(pointList);
        graphics.fillPolygon(pointList);

    }
}
