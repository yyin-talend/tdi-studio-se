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
package org.talend.designer.xmlmap.figures.treeNode;

import java.util.List;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.SWT;
import org.talend.designer.xmlmap.figures.ExpressionFigure;
import org.talend.designer.xmlmap.figures.table.TableTree;
import org.talend.designer.xmlmap.ui.resource.ColorInfo;
import org.talend.designer.xmlmap.ui.resource.ColorProviderMapper;

/**
 * DOC talend class global comment. Detailled comment
 */
public class RootTreeNodeFigure extends TreeNodeFigure {

    public RootTreeNodeFigure(RowFigure rowFigure) {
        super(rowFigure);
    }

    @Override
    public RootTreeNodeFigure getRoot() {
        return this;
    }

    public TableTree getTable() {
        return (TableTree) this.getParent().getParent();
    }

    public int getExpressionColumnWidth() {
        return getTable().getExpressionWidth();
    }

    protected void paintFigure(Graphics graphics) {
        super.paintFigure(graphics);
        // paint rows and columns in root figure
        graphics.setForegroundColor(ColorConstants.menuBackground);
        paintLines(graphics);
        if (this.isExpanded()) {
            graphics.setForegroundColor(ColorProviderMapper.getColor(ColorInfo.COLOR_TREE_LINES));
            this.getElement().getTreeBranch().getBranchLayout().paintLines(graphics);
            // getBranchLayout().paintLines(graphics);
        }

    }

    private void paintLines(Graphics graphics) {
        final Point bottomLeft = getBounds().getBottomLeft();
        final Point bottomRight = getBounds().getBottomRight();

        graphics.drawLine(getBounds().x, getBounds().getBottom().y - 1, getBounds().getRight().x, getBounds().getBottom().y - 1);

        final ExpressionFigure expressionFigure = getElement().getExpressionFigure();
        if (expressionFigure != null) {
            final Rectangle expBounds = expressionFigure.getBounds();
            graphics.drawLine(expBounds.x + expBounds.width, expBounds.y, expBounds.x + expBounds.width, getBounds().y
                    + getBounds().height);
        }

        paintChildrenLines(this, graphics);

    }

    private void paintChildrenLines(TreeNodeFigure treeNode, Graphics graphics) {
        if (treeNode.isExpanded()) {
            graphics.setLineStyle(SWT.LINE_DOT);
            final List children = treeNode.getContents().getChildren();
            final Rectangle elementBounds = treeNode.getElementBounds();
            final ExpressionFigure expressionFigure = treeNode.getElement().getExpressionFigure();
            if (expressionFigure != null) {
                final Rectangle expressionBounds = expressionFigure.getBounds();
                graphics.drawLine(elementBounds.x, expressionBounds.getBottom().y - 1, elementBounds.getRight().x,
                        expressionBounds.getBottom().y - 1);
            } else {
                graphics.drawLine(elementBounds.getBottomLeft(), elementBounds.getBottomRight());
            }
            for (int i = 0; i < children.size(); i++) {
                final Object object = children.get(i);
                if (object instanceof TreeNodeFigure) {
                    TreeNodeFigure child = (TreeNodeFigure) object;
                    final Rectangle childElemBounds = child.getElementBounds();
                    paintChildrenLines(child, graphics);
                }
            }
        }
    }

}
