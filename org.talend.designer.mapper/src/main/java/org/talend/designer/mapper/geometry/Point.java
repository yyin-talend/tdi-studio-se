// Java & Bézier
// (C) 2002 Emmanuel Turquin
//
// Point.java
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

public abstract class Point {

    boolean visible = true;

    Color color = Color.black;

    // isVisible
    public boolean isVisible() {
        return visible;
    }

    // setVisible
    public void setVisible(boolean b) {
        visible = b;
    }

    // getColor
    public Color getColor() {
        return color;
    }

    // setColor
    public void setColor(Color c) {
        color = c;
    }

}
