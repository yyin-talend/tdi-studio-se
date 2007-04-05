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
package org.talend.designer.core.ui.editor.notes;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;

/**
 */
public class NoteFigure extends Figure {

    private static final int CORNER_SIZE = 12;

    private Label label = new Label();

    public NoteFigure() {
        setBackgroundColor(new Color(Display.getCurrent(), 255, 255, 203));
        setForegroundColor(ColorConstants.orange);

        add(label);
        label.setForegroundColor(ColorConstants.black);
    }

    protected void paintFigure(Graphics graphics) {
        Rectangle rect = getBounds().getCopy();

        graphics.translate(getLocation());

        // fill the note
        if (isOpaque()) {
            PointList outline = new PointList();

            outline.addPoint(0, 0);
            outline.addPoint(rect.width - CORNER_SIZE, 0);
            outline.addPoint(rect.width - 1, CORNER_SIZE);
            outline.addPoint(rect.width - 1, rect.height - 1);
            outline.addPoint(0, rect.height - 1);

            graphics.fillPolygon(outline);
        }

        // draw the inner outline
        PointList innerLine = new PointList();

        innerLine.addPoint(rect.width - CORNER_SIZE - 1, 0);
        innerLine.addPoint(rect.width - CORNER_SIZE - 1, CORNER_SIZE);
        innerLine.addPoint(rect.width - 1, CORNER_SIZE);
        innerLine.addPoint(rect.width - CORNER_SIZE - 1, 0);
        innerLine.addPoint(0, 0);
        innerLine.addPoint(0, rect.height - 1);
        innerLine.addPoint(rect.width - 1, rect.height - 1);
        innerLine.addPoint(rect.width - 1, CORNER_SIZE);

        graphics.drawPolygon(innerLine);

        graphics.translate(getLocation().getNegated());

        label.setLocation(new Point(getLocation().x + CORNER_SIZE, getLocation().y + CORNER_SIZE));
        label.setSize(getSize().width - CORNER_SIZE * 2, getSize().height - CORNER_SIZE * 2);
    }

    public String getText() {
        return label.getText();
    }

    public void setText(String text) {
        label.setText(text);
    }

}
