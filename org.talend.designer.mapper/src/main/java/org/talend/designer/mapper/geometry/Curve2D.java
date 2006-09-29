// Java & Bézier
// (C) 2002 Emmanuel Turquin
//
// Curve2D.java
//
// This program is free software; you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation; either version 2 of the License, or
// (at your option) any later version.
//
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
// GNU General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with this program; if not, write to the Free Software
// Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA

package org.talend.designer.mapper.geometry;

import java.util.ListIterator;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;

public abstract class Curve2D extends Curve {

    int subdiv;

    private int minimumSegements = 0;

    private int maximumSegements = Integer.MAX_VALUE;

    static int DEFAULT_SUBDIV = 10;

    // constructor
    public Curve2D() {
        super();
        subdiv = DEFAULT_SUBDIV;
    }

    public Curve2D(Point2DList pl) {
        super(pl);
        subdiv = DEFAULT_SUBDIV;
    }

    // getSubdiv
    public int getSubdiv() {
        return subdiv;
    }

    // setSubdiv
    public void setSubdiv(int sd) {
        subdiv = Math.max(sd, 1);
    }

    // draw
    public void draw(GC gc, Point2DList current, Point2DList active, Color foregroundColor) {
        if (plist == null)
            return;

        Point2DList pl = (Point2DList) plist;

        if (!visible || pl.size() <= (nbMin - 1))
            return;
        gc.setForeground(foregroundColor);
        // g.setStroke(new BasicStroke(THICKNESS));

        if (pl == current || pl == active) {
            list.clear();
            computeSegments();
        }

        ListIterator li = list.listIterator();
        java.awt.geom.Point2D.Double p1, p2;

        p2 = (java.awt.geom.Point2D.Double) li.next();

        while (li.hasNext()) {
            p1 = p2;
            p2 = (java.awt.geom.Point2D.Double) li.next();
            if (p1 != null && p2 != null) {
                // gc.setForeground(gc.getDevice().getSystemColor(SWT.COLOR_WHITE));
                gc.drawLine((int) java.lang.Math.round(p1.x), (int) java.lang.Math.round(p1.y), (int) java.lang.Math.round(p2.x),
                        (int) java.lang.Math.round(p2.y));
                // gc.setForeground(gc.getDevice().getSystemColor(SWT.COLOR_BLUE));
                // gc.drawPoint((int)java.lang.Math.round(p1.x), (int)java.lang.Math.round(p1.y));
            }
        }
    }

    // computeSegments
    abstract void computeSegments();

    public int getMaximumSegements() {
        return this.maximumSegements;
    }

    /**
     * 
     * DOC amaumont Comment method "setMaximumSegements".
     * 
     * @param maximumSegements positive or equals to 0 value
     */
    public void setMaximumSegements(int maximumSegements) {
        if (maximumSegements < 0) {
            throw new IllegalArgumentException("maximumSegements must positive or 0");
        }
        this.maximumSegements = maximumSegements;
    }

    public int getMinimumSegements() {
        return this.minimumSegements;
    }

    /**
     * 
     * DOC amaumont Comment method "setMinimumSegements".
     * 
     * @param minimumSegements positive or equals to 0 value
     */
    public void setMinimumSegements(int minimumSegements) {
        if (maximumSegements < 0) {
            throw new IllegalArgumentException("minimumSegements must positive or 0");
        }
        this.minimumSegements = minimumSegements;
    }

}
