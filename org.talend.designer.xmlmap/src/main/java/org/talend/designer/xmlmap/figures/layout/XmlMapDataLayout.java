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
import java.util.ListIterator;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.XYLayout;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.talend.designer.xmlmap.figures.SashSeparator;
import org.talend.designer.xmlmap.parts.XmlMapDataEditPart;

/**
 * wchen class global comment. Detailled comment
 */
public class XmlMapDataLayout extends XYLayout {

    XmlMapDataEditPart editPart;

    private int zoneWidth;

    org.eclipse.swt.graphics.Point previousAvilableSize;

    public XmlMapDataLayout(XmlMapDataEditPart editPart) {
        this.editPart = editPart;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.draw2d.LayoutManager#layout(org.eclipse.draw2d.IFigure)
     */
    public void layout(IFigure parent) {
        List children = parent.getChildren();
        int numChildren = children.size();
        org.eclipse.swt.graphics.Point avilableSize = editPart.getViewer().getControl().getSize();

        double shrinkWidth = 1;
        if (previousAvilableSize != null && previousAvilableSize != avilableSize) {
            shrinkWidth = (double) avilableSize.x / (double) previousAvilableSize.x;
            previousAvilableSize = avilableSize;
        } else {
            previousAvilableSize = avilableSize;
        }

        // get the number of separators and the total width, the width of separator for zone is fixed
        int separatorWith = 0;
        int separatorNum = 0;
        for (int i = 0; i < numChildren; i++) {
            IFigure child = (IFigure) children.get(i);
            if (child instanceof SashSeparator) {
                separatorNum++;
                SashSeparator separator = (SashSeparator) child;
                separatorWith = separatorWith + separator.getWidth();
            }
        }

        zoneWidth = (avilableSize.x - separatorWith) / (numChildren - separatorNum);
        // zoneWidth = (avilableSize.x - separatorWith - 1) / (numChildren - separatorNum);
        Rectangle clientArea = parent.getClientArea();
        int x = clientArea.x;
        int y = clientArea.y;
        Point offset = getOrigin(parent);

        int toltalWidth = 0;
        for (int i = 0; i < numChildren; i++) {
            IFigure f = (IFigure) children.get(i);
            Rectangle bounds = (Rectangle) getConstraint(f);
            if (bounds == null) {
                Rectangle newBounds = null;
                if (f instanceof SashSeparator) {
                    SashSeparator separator = (SashSeparator) f;
                    newBounds = new Rectangle(x, y, separator.getWidth(), avilableSize.y);
                    f.setBounds(newBounds);
                    x = x + separator.getWidth();
                } else {
                    newBounds = new Rectangle(x, y, zoneWidth, avilableSize.y);
                    f.setBounds(newBounds);
                    x = x + zoneWidth;
                }
                setConstraint(f, newBounds);
                continue;

            }
            bounds = bounds.getCopy();

            // avialable size changed.
            if (shrinkWidth != 1) {
                bounds.x = x;
                if (!(f instanceof SashSeparator)) {
                    int w = (int) Math.floor(bounds.width * shrinkWidth);
                    bounds.width = w;
                }
                x = x + bounds.width;
            }

            if (bounds.width == -1 || bounds.height == -1) {
                Dimension preferredSize = f.getPreferredSize(bounds.width, bounds.height);
                bounds = bounds.getCopy();
                if (bounds.width == -1)
                    bounds.width = preferredSize.width;
                if (bounds.height == -1)
                    bounds.height = preferredSize.height;
            }
            bounds = bounds.getTranslated(offset);
            bounds.height = avilableSize.y;
            toltalWidth = toltalWidth + bounds.width;
            f.setBounds(bounds);
            setConstraint(f, bounds);

        }

        // in case some blank width
        if (toltalWidth != 0) {
            int diff = avilableSize.x - toltalWidth;
            if (diff < 0) {
                diff = 0;
            }
            int avg = diff / (numChildren - separatorNum);
            int remainder = diff % (numChildren - separatorNum);
            if (avg != 0) {
                x = clientArea.x;
                for (int i = 0; i < numChildren; i++) {
                    IFigure f = (IFigure) children.get(i);
                    Rectangle bounds = f.getBounds();
                    bounds.x = x;
                    if (!(f instanceof SashSeparator)) {
                        bounds.width = bounds.width + avg;
                    }
                    f.setBounds(bounds);
                    x = x + bounds.width;
                }
            }
            if (remainder != 0) {
                IFigure lastChild = (IFigure) children.get(children.size() - 1);
                final Rectangle bounds = lastChild.getBounds();
                bounds.width = bounds.width + remainder;
                lastChild.setBounds(bounds);
            }
        }

    }

    protected Dimension calculatePreferredSize(IFigure f, int wHint, int hHint) {
        Rectangle rect = new Rectangle();
        ListIterator children = f.getChildren().listIterator();
        while (children.hasNext()) {
            IFigure child = (IFigure) children.next();
            Rectangle r = (Rectangle) constraints.get(child);
            if (r == null)
                continue;

            if (r.width == -1 || r.height == -1) {
                Dimension preferredSize = child.getPreferredSize(r.width, r.height);
                r = r.getCopy();
                if (r.width == -1)
                    r.width = preferredSize.width;
                if (r.height == -1)
                    r.height = preferredSize.height;
            }
            rect.union(r);
        }
        Dimension d = rect.getSize();
        Insets insets = f.getInsets();

        org.eclipse.swt.graphics.Point avilableSize = editPart.getViewer().getControl().getSize();
        d.width = avilableSize.x;
        // should be d.width + insets.getWidth(), -1 only used to sovle a problem:when resize the shell the layout is
        // never called
        return new Dimension(d.width, d.height + insets.getHeight()).union(getBorderPreferredSize(f));
    }

}
