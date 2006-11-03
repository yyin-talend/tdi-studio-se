// ============================================================================
//
// Talend Community Edition
//
// Copyright (C) 2006 Talend - www.talend.com
//
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either
// version 2.1 of the License, or (at your option) any later version.
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
package org.talend.designer.mapper.ui.visualmap.link;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.talend.commons.ui.swt.drawing.link.IDrawableLink;
import org.talend.commons.ui.swt.drawing.link.IStyleLink;
import org.talend.designer.mapper.managers.MapperManager;
import org.talend.designer.mapper.ui.MapperUI;
import org.talend.designer.mapper.ui.visualmap.zone.Zone;

/**
 * DOC amaumont class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 */
public class Link extends AbstractLink {

    /**
     * DOC amaumont Link constructor comment.
     * 
     * @param pointDescriptor1
     * @param pointDescriptor2
     * @param mapperManager
     */
    public Link(PointLinkDescriptor pointDescriptor1, PointLinkDescriptor pointDescriptor2, MapperManager mapperManager) {
        super(pointDescriptor1, pointDescriptor2, mapperManager);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.mapper.ui.visualmap.link.AbstractLink#calculate()
     */
    @Override
    public void calculate() {
        // TODO Auto-generated method stub
        super.calculate();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.mapper.ui.visualmap.link.AbstractLink#draw(org.eclipse.swt.graphics.GC, int, int)
     */
    @Override
    public void draw(GC gc, Rectangle boundsOfDrawing) {
        StyleLinkFactory drawableLinkFactory = mapperManager.getUiManager().getDrawableLinkFactory();

        IStyleLink styleLink = drawableLinkFactory.getStyleLink(this);
        IDrawableLink drawableLink = styleLink.getDrawableLink();
        if (drawableLink == null) {
            throw new NullPointerException();
        }

        Zone zone1 = pointDescriptor1.getZone();
        Zone zone2 = pointDescriptor2.getZone();

        int yOffsetPoint1 = uiManager.getVerticalScrolledOffsetBar(zone1);
        int yOffsetPoint2 = uiManager.getVerticalScrolledOffsetBar(zone2);

        boolean leftSidePosition = ((zone1 == Zone.INPUTS || zone1 == Zone.VARS) && zone1 == zone2);

        int point1xForTraceLink = point1.x + (leftSidePosition ? 0 : widthTable1 - 4);
        int point2xForTraceLink = point2.x + (leftSidePosition ? 0 : 1);

        int point1yOffset = point1.y + yOffsetPoint1;
        int point2yOffset = point2.y + yOffsetPoint2;

        drawableLink.setPoint1(new Point(point1xForTraceLink, point1yOffset));
        drawableLink.setPoint2(new Point(point2xForTraceLink, point2yOffset));
        drawableLink.setBoundsOfCalculate(boundsOfDrawing);

        Rectangle boundsOfSolidLine = new Rectangle(boundsOfDrawing.x, boundsOfDrawing.y, boundsOfDrawing.width,
                boundsOfDrawing.height);
        boundsOfSolidLine.y = boundsOfDrawing.y + MapperUI.OFFSET_VISIBLES_POINTS;
        boundsOfSolidLine.height = boundsOfDrawing.height - 2 * MapperUI.OFFSET_VISIBLES_POINTS;

        if (!boundsOfSolidLine.contains(new Point(point1xForTraceLink, point1yOffset))
                || !boundsOfSolidLine.contains(new Point(point2xForTraceLink, point2yOffset))) {
            // gc.setLineDash(new int[] { 1, spaceBetweenPoints, 1 });
            gc.setLineStyle(SWT.LINE_DOT);
            // gc.setLineStyle(SWT.LINE_CUSTOM);
            // System.out.println("SWT.LINE_DOT");
        } else {
            gc.setLineStyle(SWT.LINE_SOLID);
            // System.out.println("SWT.LINE_SOLID");
        }

        drawableLink.draw(gc);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.mapper.ui.visualmap.link.AbstractLink#getPointLinkDescriptorSource()
     */
    @Override
    public PointLinkDescriptor getPointLinkDescriptorSource() {
        // TODO Auto-generated method stub
        return super.getPointLinkDescriptorSource();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.mapper.ui.visualmap.link.AbstractLink#getPointLinkDescriptorTarget()
     */
    @Override
    public PointLinkDescriptor getPointLinkDescriptorTarget() {
        // TODO Auto-generated method stub
        return super.getPointLinkDescriptorTarget();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.mapper.ui.visualmap.link.AbstractLink#getState()
     */
    @Override
    public LinkState getState() {
        // TODO Auto-generated method stub
        return super.getState();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.mapper.ui.visualmap.link.AbstractLink#setState(org.talend.designer.mapper.ui.visualmap.link.LinkState)
     */
    @Override
    public void setState(LinkState linkState) {
        // TODO Auto-generated method stub
        super.setState(linkState);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.mapper.ui.visualmap.link.AbstractLink#toString()
     */
    @Override
    public String toString() {
        // TODO Auto-generated method stub
        return super.toString();
    }

}
