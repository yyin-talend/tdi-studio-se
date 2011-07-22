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

import java.util.ListIterator;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.XYLayout;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.Rectangle;
import org.talend.designer.xmlmap.figures.ExpressionFigure;
import org.talend.designer.xmlmap.figures.treeNode.RowFigure;
import org.talend.designer.xmlmap.figures.treeNode.TreeBranch;
import org.talend.designer.xmlmap.parts.TreeNodeEditPart;

/**
 * DOC talend class global comment. Detailled comment
 */
public class RowLayout extends XYLayout {

    private static final int FIXED_ROW_HEIGHT = 20;

    private Rectangle previousClientArea;

    private RowFigure rowFigure;

    private TreeNodeEditPart treeNodePart;

    public RowLayout(RowFigure rowFigure, TreeNodeEditPart treeNodePart) {
        this.rowFigure = rowFigure;
        this.treeNodePart = treeNodePart;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.draw2d.XYLayout#layout(org.eclipse.draw2d.IFigure)
     */
    @Override
    public void layout(IFigure parent) {
        int initExpressionWidth = (treeNodePart.getViewer().getControl().getSize().x / 3 - 80) / 2;

        final Rectangle clientArea = parent.getClientArea();
        double shrinkHeight = 1;
        double shrinkWidth = 1;
        boolean parentResized = false;
        if (previousClientArea != null && previousClientArea.equals(clientArea)) {
            parentResized = true;
            previousClientArea = clientArea;
            shrinkHeight = (double) clientArea.y / (double) previousClientArea.y;
            shrinkWidth = (double) clientArea.x / (double) previousClientArea.x;
        }

        int x = clientArea.x;
        int y = clientArea.y;

        int branchWidth = clientArea.width - initExpressionWidth;

        final ExpressionFigure expressionFigure = rowFigure.getExpressionFigure();
        if (expressionFigure != null) {
            Rectangle newBounds = new Rectangle(x, y, initExpressionWidth, FIXED_ROW_HEIGHT);
            expressionFigure.setBounds(newBounds);
            setConstraint(expressionFigure, newBounds);
            x = x + newBounds.width;
        } else {
            branchWidth = clientArea.width;
        }

        final TreeBranch treeBranch = rowFigure.getTreeBranch();
        final Dimension preSize = treeBranch.getPreferredSize(clientArea.width, -1);
        branchWidth = Math.max(branchWidth, preSize.width);
        Rectangle newBounds = new Rectangle(x, y, branchWidth, FIXED_ROW_HEIGHT);
        treeBranch.setBounds(newBounds);
        setConstraint(treeBranch, newBounds);
        x = x + newBounds.width;

        // add code for shell resize later

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.draw2d.XYLayout#calculatePreferredSize(org.eclipse.draw2d.IFigure, int, int)
     */
    @Override
    protected Dimension calculatePreferredSize(IFigure f, int wHint, int hHint) {
        f.validate();
        Dimension d = new Dimension();
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

            d.height = Math.max(d.height, r.height);
            d.width = d.width + r.width;

        }

        Insets insets = f.getInsets();
        return new Dimension(d.width + insets.getWidth(), d.height + insets.getHeight()).union(getBorderPreferredSize(f));

    }

    public int getMaxRowHeight() {
        return FIXED_ROW_HEIGHT;
    }

}
