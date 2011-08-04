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

import java.util.HashMap;
import java.util.ListIterator;
import java.util.Map;

import org.eclipse.draw2d.AbstractHintLayout;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.LayoutManager;
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
public class RowLayout extends AbstractHintLayout {

    private Dimension cachedPreferredHint = new Dimension(-1, -1);

    protected Map constraints = new HashMap();

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
    public void layout(IFigure parent) {
        int initExpressionWidth = (treeNodePart.getViewer().getControl().getSize().x / 3 - 80 - 10) / 2;

        final Rectangle clientArea = parent.getClientArea();

        int x = clientArea.x;
        int y = clientArea.y;

        // int branchWidth = clientArea.width - initExpressionWidth;

        final ExpressionFigure expressionFigure = rowFigure.getExpressionFigure();
        if (expressionFigure != null) {
            Rectangle newBounds = new Rectangle(x, y, initExpressionWidth, FIXED_ROW_HEIGHT);
            expressionFigure.setBounds(newBounds);
            setConstraint(expressionFigure, newBounds);
            x = x + newBounds.width;
        }

        final TreeBranch treeBranch = rowFigure.getTreeBranch();
        final Dimension preSize = treeBranch.getPreferredSize(clientArea.width, -1);
        int branchWidth = Math.max(initExpressionWidth, preSize.width);
        Rectangle newBounds = new Rectangle(x, y, branchWidth, FIXED_ROW_HEIGHT);
        treeBranch.setBounds(newBounds);
        setConstraint(treeBranch, newBounds);

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.draw2d.XYLayout#calculatePreferredSize(org.eclipse.draw2d.IFigure, int, int)
     */
    @Override
    protected Dimension calculatePreferredSize(IFigure f, int wHint, int hHint) {
        Insets insets = f.getInsets();
        if (wHint >= 0)
            wHint = Math.max(0, wHint - insets.getWidth());

        boolean flush = cachedPreferredHint.width != wHint;
        if (flush) {
            f.invalidate();
            cachedPreferredHint.width = wHint;
        }

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
        return new Dimension(d.width + insets.getWidth(), d.height + insets.getHeight()).union(getBorderPreferredSize(f));

    }

    public void remove(IFigure figure) {
        super.remove(figure);
        constraints.remove(figure);
    }

    /**
     * Sets the layout constraint of the given figure. The constraints can only be of type {@link Rectangle}.
     * 
     * @see LayoutManager#setConstraint(IFigure, Object)
     * @since 2.0
     */
    public void setConstraint(IFigure figure, Object newConstraint) {
        super.setConstraint(figure, newConstraint);
        if (newConstraint != null)
            constraints.put(figure, newConstraint);
    }

}
