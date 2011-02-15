// ============================================================================
//
// Copyright (C) 2006-2010 Talend Inc. - www.talend.com
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
import org.eclipse.draw2d.Viewport;
import org.eclipse.draw2d.geometry.Rectangle;

/**
 * wchen class global comment. Detailled comment
 */
public class XmlMapDataLayout extends EqualWidthLayout {

    private int devideWidth;

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.draw2d.LayoutManager#layout(org.eclipse.draw2d.IFigure)
     */
    public void layout(IFigure parent) {
        List children = parent.getChildren();
        int numChildren = children.size();
        Viewport viewPort = getViewPort(parent);
        Rectangle clientArea = viewPort.getClientArea();
        devideWidth = (clientArea.width - (numChildren - 1) * spacing) / numChildren;
        int x = clientArea.x;
        for (int i = 0; i < numChildren; i++) {
            IFigure child = (IFigure) children.get(i);
            Rectangle newBounds = new Rectangle(x, clientArea.y, devideWidth, clientArea.height);
            child.setBounds(newBounds);
            x = x + devideWidth;
        }
    }

    public Viewport getViewPort(IFigure figure) {
        if (figure.getParent() instanceof Viewport) {
            return (Viewport) figure.getParent();
        } else {
            return getViewPort(figure.getParent());
        }
    }

    public int getDevideWidth() {
        return this.devideWidth;
    }

}
