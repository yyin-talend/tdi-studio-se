// ============================================================================
//
// Copyright (C) 2006-2011 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.designer.xmlmap.figures.layout;

import java.util.List;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;

/**
 * wchen class global comment. Detailled comment
 */
public class XmlMapDataLayout extends EqualWidthLayout {

    AbstractGraphicalEditPart editPart;

    public XmlMapDataLayout(AbstractGraphicalEditPart editPart) {
        this.editPart = editPart;
    }

    private int devideWidth;

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.draw2d.LayoutManager#layout(org.eclipse.draw2d.IFigure)
     */
    public void layout(IFigure parent) {
        List children = parent.getChildren();
        int numChildren = children.size();
        org.eclipse.swt.graphics.Point avilableSize = editPart.getViewer().getControl().getSize();
        devideWidth = (avilableSize.x - (numChildren - 1) * spacing) / numChildren;
        Rectangle clientArea = parent.getClientArea();
        int x = clientArea.x;
        int y = clientArea.y;
        for (int i = 0; i < numChildren; i++) {
            IFigure child = (IFigure) children.get(i);
            Rectangle newBounds = new Rectangle(x, y, devideWidth, avilableSize.y);
            child.setBounds(newBounds);
            x = x + devideWidth;
        }
    }

}
