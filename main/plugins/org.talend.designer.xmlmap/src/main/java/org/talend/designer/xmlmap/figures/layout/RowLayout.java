// ============================================================================
//
// Copyright (C) 2006-2015 Talend Inc. - www.talend.com
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
import java.util.Map;

import org.eclipse.draw2d.AbstractHintLayout;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.LayoutManager;
import org.eclipse.draw2d.geometry.Dimension;
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
        int initExpressionWidth = 0;

        final Rectangle clientArea = parent.getClientArea();

        int x = clientArea.x;
        int y = clientArea.y;

        // int branchWidth = clientArea.width - initExpressionWidth;

        final ExpressionFigure expressionFigure = rowFigure.getExpressionFigure();
        if (expressionFigure != null) {
            initExpressionWidth = rowFigure.getTreeNodeFigure().getRoot().getExpressionColumnWidth();
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
        Dimension treeBranchSize = rowFigure.getTreeBranch().getPreferredSize();
        int width = treeBranchSize.width;
        ExpressionFigure expressionFigure = rowFigure.getExpressionFigure();
        if (expressionFigure != null) {
            int expressionColumnWidth = rowFigure.getTreeNodeFigure().getRoot().getExpressionColumnWidth();
            width += expressionColumnWidth;
        }
        return new Dimension(width, FIXED_ROW_HEIGHT);
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
