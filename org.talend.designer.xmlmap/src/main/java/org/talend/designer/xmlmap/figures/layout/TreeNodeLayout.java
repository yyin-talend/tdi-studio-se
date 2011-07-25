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
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.Rectangle;
import org.talend.commons.ui.swt.geftree.layout.TreeAnimatingLayer;
import org.talend.designer.xmlmap.figures.treeNode.RowFigure;
import org.talend.designer.xmlmap.figures.treeNode.TreeNodeFigure;

/**
 * DOC talend class global comment. Detailled comment
 */
public class TreeNodeLayout extends ToolbarLayout {

    private TreeNodeFigure treeNode;

    public TreeNodeLayout(TreeNodeFigure treeNode) {
        this.treeNode = treeNode;
    }

    @Override
    public void layout(IFigure parent) {
        Rectangle clientArea = transposer.t(parent.getClientArea());
        int x = clientArea.x;
        int y = clientArea.y;

        final TreeAnimatingLayer contents = treeNode.getContents();
        final RowFigure element = treeNode.getElement();

        contents.getBounds().width = clientArea.width;
        contents.validate();

        int maxWidth = clientArea.width;
        final Dimension contentsSize = contents.getPreferredSize(clientArea.x, -1);
        final Dimension elementSize = element.getPreferredSize(clientArea.x, -1);
        maxWidth = Math.max(contentsSize.width, Math.max(maxWidth, elementSize.width));

        Rectangle rectangle = new Rectangle();
        elementSize.width = maxWidth;
        rectangle.setLocation(x, y);
        rectangle.setSize(elementSize);
        element.setBounds(rectangle);
        y = y + rectangle.height;

        rectangle = new Rectangle();
        contentsSize.width = maxWidth;
        rectangle.setLocation(x, y);
        rectangle.setSize(contentsSize);
        contents.setBounds(rectangle);

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

        prefSize.height += Math.max(0, children.size() - 1) * spacing;
        return transposer.t(prefSize).expand(insets.getWidth(), insets.getHeight()).union(getBorderPreferredSize(container));
    }

    private Dimension calculateChildrenSize(List children, int wHint, int hHint, boolean preferred) {
        Dimension childSize;
        IFigure child;
        int height = 0, width = 0;
        for (int i = 0; i < children.size(); i++) {
            child = (IFigure) children.get(i);
            if (!child.isVisible()) {
                continue;
            }
            childSize = transposer.t(preferred ? getChildPreferredSize(child, wHint, hHint) : getChildMinimumSize(child, wHint,
                    hHint));
            height += childSize.height;
            width = Math.max(width, childSize.width);
        }
        return new Dimension(width, height);
    }

}
