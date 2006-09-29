// Java & Bézier
// (C) 2002 Emmanuel Turquin
//
// Point2D.java
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

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Rectangle;

public class Point2D extends Point {

    public static final int WIDTH = 8;

    public static final int HEIGHT = 8;

    java.awt.geom.Point2D.Double coord;

    Rectangle rect;

    // constructor
    public Point2D() {
        coord = new java.awt.geom.Point2D.Double();
        rect = new Rectangle(0, 0, WIDTH, HEIGHT);
    }

    public Point2D(Point2D p) {
        coord = new java.awt.geom.Point2D.Double(p.getX(), p.getY());
        rect = new Rectangle(0, 0, WIDTH, HEIGHT);
        rect.setLocation((int) p.getX() - 4, (int) p.getY() - 4);
    }

    public Point2D(double x, double y) {
        coord = new java.awt.geom.Point2D.Double(x, y);
        rect = new Rectangle(0, 0, WIDTH, HEIGHT);
        rect.setLocation((int) x - 4, (int) y - 4);
    }

    // getX
    public double getX() {
        return coord.getX();
    }

    // getY
    public double getY() {
        return coord.getY();
    }

    // contains
    public boolean contains(double x, double y) {
        if (x > getX() - WIDTH / 2 && x < getX() + WIDTH / 2)
            if (y > getY() - HEIGHT / 2 && y < getY() + HEIGHT / 2)
                return true;
        return false;
    }

    // setLocation
    public void setLocation(double x, double y) {
        coord.setLocation(x, y);
        rect.setLocation((int) x - 4, (int) y - 4);
    }

    public void setLocation(double x, double y, Dimension dim) {
        if (x < WIDTH / 2)
            x = WIDTH / 2;
        else if (x > dim.width - WIDTH / 2)
            x = dim.width - WIDTH / 2;
        if (y < HEIGHT / 2)
            y = HEIGHT / 2;
        else if (y > dim.height - HEIGHT / 2)
            y = dim.height - HEIGHT / 2;
        setLocation(x, y);
    }

    // draw
    public void draw(Graphics2D g) {
        if (!visible)
            return;
        g.setPaint(color);
        g.fill(this.rect);
    }

    // toAwt
    public java.awt.geom.Point2D.Double toAwt() {
        java.awt.geom.Point2D.Double point = new java.awt.geom.Point2D.Double(getX(), getY());

        return point;
    }

}
