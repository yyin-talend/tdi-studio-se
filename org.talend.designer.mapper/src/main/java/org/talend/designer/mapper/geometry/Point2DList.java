// Java & Bézier
// (C) 2002 Emmanuel Turquin
//
// Point2DList.java
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
import java.util.ListIterator;

public class Point2DList extends PointList {

    // setLocation
    public void setLocation(double x, double y, Dimension dim) {
        double dx = x - ((Point2D) current).getX();
        double dy = y - ((Point2D) current).getY();
        Point2D point;

        int lstSize = list.size();
        for (int i = 0; i < lstSize; i++) {
            point = (Point2D) list.get(i);
            if (point.getX() + dx < Point2D.WIDTH / 2)
                dx = Point2D.WIDTH / 2 - point.getX();
            else if (point.getX() + dx > dim.width - Point2D.WIDTH / 2)
                dx = dim.width - Point2D.WIDTH / 2 - point.getX();
            if (point.getY() + dy < Point2D.HEIGHT / 2)
                dy = Point2D.HEIGHT / 2 - point.getY();
            else if (point.getY() + dy > dim.height - Point2D.HEIGHT / 2)
                dy = dim.height - Point2D.HEIGHT / 2 - point.getY();
        }
        for (int i = 0; i < lstSize; i++) {
            point = (Point2D) list.get(i);
            point.setLocation(point.getX() + dx, point.getY() + dy);
        }
    }

    // contains
    public int contains(double x, double y) {
        if (current != null && ((Point2D) current).contains(x, y))
            return list.indexOf(current);
        Point2D point;
        for (ListIterator li = list.listIterator(list.size()); li.hasPrevious();) {
            point = (Point2D) li.previous();
            if (point.contains(x, y))
                return li.nextIndex();
        }
        return -1;
    }

    // draw
    public void draw(Graphics2D g) {
        if (!visible)
            return;
        Point2D point;
        for (ListIterator li = list.listIterator(); li.hasNext();) {
            point = (Point2D) li.next();
            point.draw(g);
        }
        if (current != null)
            ((Point2D) current).draw(g);
    }
}
