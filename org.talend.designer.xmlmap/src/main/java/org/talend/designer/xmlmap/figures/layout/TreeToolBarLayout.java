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
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;

/**
 * wchen class global comment. Detailled comment
 */
public class TreeToolBarLayout extends EqualWidthLayout {

    @Override
    public void layout(IFigure parent) {
        List children = parent.getChildren();
        int numChildren = children.size();
        Rectangle clientArea = transposer.t(parent.getClientArea());
        int x = clientArea.x + clientArea.width;
        int y = clientArea.y + 2;
        int availableHeight = clientArea.height;

        Dimension prefSizes[] = new Dimension[numChildren];
        Dimension minSizes[] = new Dimension[numChildren];

        // Calculate the width and height hints. If it's a vertical
        // ToolBarLayout,
        // then ignore the height hint (set it to -1); otherwise, ignore the
        // width hint. These hints will be passed to the children of the parent
        // figure when getting their preferred size.
        int wHint = -1;
        int hHint = -1;
        if (isHorizontal()) {
            hHint = parent.getClientArea(Rectangle.SINGLETON).height;
        } else {
            wHint = parent.getClientArea(Rectangle.SINGLETON).width;
        }

        /*
         * Calculate sum of preferred heights of all children(totalHeight). Calculate sum of minimum heights of all
         * children(minHeight). Cache Preferred Sizes and Minimum Sizes of all children.
         * 
         * totalHeight is the sum of the preferred heights of all children totalMinHeight is the sum of the minimum
         * heights of all children prefMinSumHeight is the sum of the difference between all children's preferred
         * heights and minimum heights. (This is used as a ratio to calculate how much each child will shrink).
         */
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

        for (int i = numChildren - 1; i >= 0; i--) {
            int amntShrinkCurrentHeight = 0;
            int prefHeight = prefSizes[i].height;
            int minHeight = minSizes[i].height;
            int prefWidth = prefSizes[i].width;
            Rectangle newBounds = new Rectangle(x, y, prefWidth, prefHeight);

            child = (IFigure) children.get(i);
            if (prefMinSumHeight != 0)
                amntShrinkCurrentHeight = (prefHeight - minHeight) * amntShrinkHeight / (prefMinSumHeight);

            newBounds.x = x - newBounds.width - spacing;

            newBounds.height -= amntShrinkCurrentHeight;
            child.setBounds(transposer.t(newBounds));

            amntShrinkHeight -= amntShrinkCurrentHeight;
            prefMinSumHeight -= (prefHeight - minHeight);
            x = newBounds.x - spacing;
        }
    }
}
