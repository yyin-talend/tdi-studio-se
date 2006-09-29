// Java & Bézier
// (C) 2002 Emmanuel Turquin
//
// PointList.java
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

import java.awt.Color;
import java.util.ArrayList;
import java.util.ListIterator;

public abstract class PointList {

    java.util.List list;

    boolean visible = true;

    Point current;

    Color colorBG = Color.black;

    Color colorSel = Color.red;

    // constructor
    public PointList() {
        list = new ArrayList();
    }

    // listIterator
    public ListIterator listIterator() {
        return list.listIterator();
    }

    // add
    public void add(Point p) {
        p.setColor(colorBG);
        list.add(p);
        setCurrent(p);
    }

    public void add(int i, Point p) {
        p.setColor(colorBG);
        list.add(i, p);
        setCurrent(p);
    }

    // remove
    public int remove(Point p) {
        boolean cr = false;
        int i = getCurrentIndex();

        if (p == current)
            cr = true;
        list.remove(p);
        if (size() == 0) {
            current = null;
            return 1;
        }
        if (cr)
            setCurrent(Math.max(i - 1, 0));
        return 0;
    }

    public int remove(int i) {
        boolean cr = false;

        if (get(i) == current)
            cr = true;
        list.remove(i);
        if (size() == 0) {
            current = null;
            return 1;
        }
        if (cr)
            setCurrent(Math.max(i - 1, 0));
        return 0;
    }

    // clear
    public void clear() {
        current = null;
        list.clear();
    }

    // get
    public Point get(int i) {
        return (Point) list.get(i);
    }

    // size
    public int size() {
        return list.size();
    }

    // getCurrent
    public Point getCurrent() {
        return current;
    }

    // getCurrentIndex
    public int getCurrentIndex() {
        if (current == null)
            return -1;
        return list.indexOf(current);
    }

    // setCurrent
    public void setCurrent(Point p) {
        current = p;
        refreshColor();
    }

    public void setCurrent(int i) {
        current = (Point) list.get(i);
        refreshColor();
    }

    // isVisible
    public boolean isVisible() {
        return visible;
    }

    // setVisible
    public void setVisible(boolean b) {
        Point point;

        for (ListIterator li = list.listIterator(); li.hasNext();) {
            point = (Point) li.next();
            point.setVisible(b);
        }
        visible = b;
    }

    // getColorBG
    public Color getColorBG() {
        return colorBG;
    }

    // getColorSel
    public Color getColorSel() {
        return colorSel;
    }

    // setColor
    public void setColor(Color bg, Color sel) {
        colorBG = bg;
        colorSel = sel;
    }

    // refreshColor
    public void refreshColor() {
        Point point;

        for (ListIterator li = list.listIterator(); li.hasNext();) {
            point = (Point) li.next();
            if (point == current)
                point.setColor(colorSel);
            else
                point.setColor(colorBG);
        }
    }

}
