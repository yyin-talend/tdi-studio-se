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

/**
 */
public class NoteFigure extends Figure {

    private static int cornerSize = 7;
    
    private Label label = new Label();
    
    public NoteFigure() {
        setBackgroundColor(ColorConstants.tooltipBackground);
        setForegroundColor(ColorConstants.tooltipForeground);
        add(label);
    }

    protected void paintFigure(Graphics graphics) {
        Rectangle rect = getBounds().getCopy();

        graphics.translate(getLocation());

        // fill the note
        if (isOpaque()) {
            PointList outline = new PointList();
            
            outline.addPoint(0, 0);
            outline.addPoint(rect.width - cornerSize, 0);
            outline.addPoint(rect.width - 1, cornerSize);
            outline.addPoint(rect.width - 1, rect.height - 1);
            outline.addPoint(0, rect.height - 1);
            
            graphics.fillPolygon(outline); 
        }
        
        // draw the inner outline
        PointList innerLine = new PointList();
        
        innerLine.addPoint(rect.width - cornerSize - 1, 0);
        innerLine.addPoint(rect.width - cornerSize - 1, cornerSize);
        innerLine.addPoint(rect.width - 1, cornerSize);
        innerLine.addPoint(rect.width - cornerSize - 1, 0);
        innerLine.addPoint(0, 0);
        innerLine.addPoint(0, rect.height - 1);
        innerLine.addPoint(rect.width - 1, rect.height - 1);
        innerLine.addPoint(rect.width - 1, cornerSize);
        
        graphics.drawPolygon(innerLine);
        
        graphics.translate(getLocation().getNegated());
        
        label.setLocation(new Point(getLocation().x + cornerSize, getLocation().y + cornerSize));
        label.setSize(getSize().width - cornerSize * 2, getSize().height - cornerSize * 2);
    }

    public String getText() {
        return label.getText();
    }

    public void setText(String text) {
        label.setText(text);
    }

}
