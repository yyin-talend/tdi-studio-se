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
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;

/**
 * wchen class global comment. Detailled comment
 */
public class TreeNodeLayout extends EqualWidthLayout {

    private AbstractGraphicalEditPart treeEditPart;

    public TreeNodeLayout(AbstractGraphicalEditPart treeEditPart) {
        super();
        this.treeEditPart = treeEditPart;
    }

    @Override
    public void layout(IFigure parent) {
        List children = parent.getChildren();
        int numChildren = children.size();
        Rectangle clientArea = transposer.t(parent.getClientArea());
        int x = clientArea.x;
        int y = clientArea.y;
        int availableHeight = clientArea.height;

        Rectangle treeBounds = treeEditPart.getFigure().getBounds();
        int avialableExpressionWidth = (treeBounds.width - numChildren - 1) / numChildren;

        Dimension prefSizes[] = new Dimension[numChildren];
        Dimension minSizes[] = new Dimension[numChildren];

        int wHint = -1;
        int hHint = -1;
        if (isHorizontal()) {
            hHint = parent.getClientArea(Rectangle.SINGLETON).height;
        } else {
            wHint = parent.getClientArea(Rectangle.SINGLETON).width;
        }
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
        int totalWith = 0;

        for (int i = 0; i < numChildren; i++) {
            int amntShrinkCurrentHeight = 0;
            int prefHeight = prefSizes[i].height;
            int minHeight = minSizes[i].height;
            int prefWidth = prefSizes[i].width;
            Rectangle newBounds = new Rectangle(x, y, prefWidth, prefHeight);

            child = (IFigure) children.get(i);
            if (prefMinSumHeight != 0)
                amntShrinkCurrentHeight = (prefHeight - minHeight) * amntShrinkHeight / (prefMinSumHeight);

            if (i == 0) {
                newBounds.width = avialableExpressionWidth;
            } else if (i == numChildren - 1) {
                if (newBounds.width < avialableExpressionWidth) {
                    newBounds.width = avialableExpressionWidth;
                }
            }

            newBounds.height -= amntShrinkCurrentHeight;
            child.setBounds(transposer.t(newBounds));

            amntShrinkHeight -= amntShrinkCurrentHeight;
            prefMinSumHeight -= (prefHeight - minHeight);
            if (i != 0 && i % numChildren == 0) {
                y += newBounds.height + spacing;
            }
            x += newBounds.width + spacing;

            totalWith = totalWith + newBounds.width + spacing;
            maxHeightInRow = Math.max(maxHeightInRow, newBounds.height);
        }

        for (int i = 0; i < numChildren; i++) {
            child = (IFigure) children.get(i);
            child.getBounds().height = maxHeightInRow;
        }

    }

    @Override
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

        List children = container.getChildren();
        Dimension prefSize = calculateChildrenSize(children, wHint, hHint, true);
        // Do a second pass, if necessary
        if (wHint >= 0 && prefSize.width > wHint) {
            prefSize = calculateChildrenSize(children, prefSize.width, hHint, true);
        } else if (hHint >= 0 && prefSize.width > hHint) {
            prefSize = calculateChildrenSize(children, wHint, prefSize.width, true);
        }

        /*
         * add expression with ,because calculatePreferredSize of ExpressionLayout returns Dimension(0,0) , need modify
         * later
         */
        Rectangle treeBounds = treeEditPart.getFigure().getBounds();
        int avialableExpressionWidth = (treeBounds.width - container.getChildren().size() - 1) / container.getChildren().size();

        Dimension pSize = transposer.t(prefSize).expand(insets.getWidth(), insets.getHeight())
                .union(getBorderPreferredSize(container));

        return new Dimension(pSize.width + avialableExpressionWidth, pSize.height);

    }

}
