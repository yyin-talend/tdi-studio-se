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
import org.eclipse.draw2d.ToolbarLayout;
import org.eclipse.draw2d.Viewport;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.Rectangle;
import org.talend.designer.xmlmap.figures.ToolBarContainer;

/**
 * DOC Administrator class global comment. Detailled comment
 */
public class EqualWidthLayout extends ToolbarLayout {

    private boolean useParentHeight;

    @Override
    public void layout(IFigure parent) {
        List children = parent.getChildren();
        int numChildren = children.size();
        Rectangle clientArea = transposer.t(parent.getClientArea());
        int x = clientArea.x;
        int y = clientArea.y;
        int availableHeight = clientArea.height;
        int availableWidth = clientArea.width;

        Dimension prefSizes[] = new Dimension[numChildren];
        Dimension minSizes[] = new Dimension[numChildren];

        int wHint = -1;
        int hHint = -1;
        if (isHorizontal()) {
            hHint = parent.getClientArea(Rectangle.SINGLETON).height;
        } else {
            wHint = parent.getClientArea(Rectangle.SINGLETON).width;
        }

        int devideWidth = (wHint - (numChildren - 1) * spacing) / numChildren;

        IFigure child;
        int totalHeight = 0;
        int totalMinHeight = 0;
        int prefMinSumHeight = 0;

        for (int i = 0; i < numChildren; i++) {
            child = (IFigure) children.get(i);

            prefSizes[i] = transposer.t(getChildPreferredSize(child, wHint, hHint));
            minSizes[i] = transposer.t(getChildMinimumSize(child, wHint, hHint));

            totalHeight += prefSizes[i].height;
            totalMinHeight += minSizes[i].height;
        }
        totalHeight += (numChildren - 1) * spacing;
        totalMinHeight += (numChildren - 1) * spacing;
        prefMinSumHeight = totalHeight - totalMinHeight;
        /*
         * The total amount that the children must be shrunk is the sum of the preferred Heights of the children minus
         * Max(the available area and the sum of the minimum heights of the children).
         * 
         * amntShrinkHeight is the combined amount that the children must shrink amntShrinkCurrentHeight is the amount
         * each child will shrink respectively
         */
        int amntShrinkHeight = totalHeight - Math.max(availableHeight, totalMinHeight);

        if (amntShrinkHeight < 0) {
            amntShrinkHeight = 0;
        }

        int maxHeightInRow = 0;
        for (int i = 0; i < numChildren; i++) {
            int amntShrinkCurrentHeight = 0;
            int prefHeight = prefSizes[i].height;
            int minHeight = minSizes[i].height;
            int prefWidth = prefSizes[i].width;
            int minWidth = minSizes[i].width;
            Rectangle newBounds = new Rectangle(x, y, prefWidth, prefHeight);

            child = (IFigure) children.get(i);
            if (prefMinSumHeight != 0)
                amntShrinkCurrentHeight = (prefHeight - minHeight) * amntShrinkHeight / (prefMinSumHeight);

            newBounds.width = devideWidth;

            newBounds.height -= amntShrinkCurrentHeight;
            child.setBounds(transposer.t(newBounds));

            amntShrinkHeight -= amntShrinkCurrentHeight;
            prefMinSumHeight -= (prefHeight - minHeight);
            if (i != 0 && i % numChildren == 0) {
                y += newBounds.height + spacing;
            }
            x += newBounds.width + spacing;

            maxHeightInRow = Math.max(maxHeightInRow, newBounds.height);
        }

        if (isUseParentHeight()) {
            Viewport viewPort = getViewPort(parent);
            if (viewPort != null) {
                if (viewPort.getBounds().height < availableHeight) {
                    availableHeight = viewPort.getBounds().height;
                }
                if (viewPort.getBounds().width < availableWidth) {
                    availableWidth = viewPort.getBounds().width;
                }
            }
        }

        for (int i = 0; i < numChildren; i++) {
            child = (IFigure) children.get(i);
            if (isUseParentHeight()) {
                child.getBounds().height = availableHeight;
            } else {
                child.getBounds().height = maxHeightInRow;

            }
        }

    }

    /**
     * 
     * Used for three main scrollPane to get the same size as the editor , maybe changed latter
     * 
     * @return
     */
    private Viewport getViewPort(IFigure figure) {
        if (figure.getParent() instanceof Viewport) {
            return (Viewport) figure.getParent();
        } else {
            return getViewPort(figure.getParent());
        }
    }

    protected Dimension calculatePreferredSize(IFigure container, int wHint, int hHint) {
        Insets insets = container.getInsets();
        if (isHorizontal()) {
            wHint = -1;
            if (hHint >= 0)
                hHint = Math.max(0, hHint - insets.getHeight());
        } else {
            hHint = -1;
            if (wHint >= 0)
                wHint = Math.max(0, wHint - insets.getWidth());
        }
        boolean isToolBarContainer = false;
        if (container instanceof ToolBarContainer) {
            isToolBarContainer = true;
        }

        List children = container.getChildren();
        Dimension prefSize = calculateChildrenSize(children, wHint, hHint, true, isToolBarContainer);
        // Do a second pass, if necessary
        if (wHint >= 0 && prefSize.width > wHint) {
            prefSize = calculateChildrenSize(children, prefSize.width, hHint, true, isToolBarContainer);
        } else if (hHint >= 0 && prefSize.width > hHint) {
            prefSize = calculateChildrenSize(children, wHint, prefSize.width, true, isToolBarContainer);
        }

        prefSize.height += Math.max(0, children.size() - 1) * spacing;
        return transposer.t(prefSize).expand(insets.getWidth(), insets.getHeight()).union(getBorderPreferredSize(container));
    }

    private Dimension calculateChildrenSize(List children, int wHint, int hHint, boolean preferred, boolean isToolBarContainer) {
        Dimension childSize;
        IFigure child;
        int height = 0, width = 0;
        for (int i = 0; i < children.size(); i++) {
            child = (IFigure) children.get(i);
            childSize = transposer.t(preferred ? getChildPreferredSize(child, wHint, hHint) : getChildMinimumSize(child, wHint,
                    hHint));
            width = Math.max(width, childSize.width);
            if (isToolBarContainer) {
                height = Math.max(height, childSize.height);
            } else {
                height += childSize.height;
            }
        }
        return new Dimension(width, height);
    }

    public boolean isUseParentHeight() {
        return useParentHeight;
    }

    public void setUseParentHeight(boolean useParentHeight) {
        this.useParentHeight = useParentHeight;
    }
}
