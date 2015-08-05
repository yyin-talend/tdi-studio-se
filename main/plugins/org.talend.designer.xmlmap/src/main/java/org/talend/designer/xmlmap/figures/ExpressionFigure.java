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
package org.talend.designer.xmlmap.figures;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.MarginBorder;
import org.eclipse.draw2d.PositionConstants;
import org.talend.designer.xmlmap.figures.cells.IExpressionBuilderCell;
import org.talend.designer.xmlmap.parts.TreeNodeEditPart;
import org.talend.designer.xmlmap.parts.directedit.DirectEditType;

/**
 * DOC talend class global comment. Detailled comment
 */
public class ExpressionFigure extends Label implements IExpressionBuilderCell {

    private static final int DEFAULT_SIZE = 200;

    private TreeNodeEditPart treeNodePart;

    private DirectEditType type;

    public ExpressionFigure() {
        setBorder(new MarginBorder(-1, 6, -1, -1));
        setLabelAlignment(PositionConstants.LEFT);
    }

    public TreeNodeEditPart getTreeNodePart() {
        return treeNodePart;
    }

    public void setTreeNodePart(TreeNodeEditPart treeNodePart) {
        this.treeNodePart = treeNodePart;
    }

    public void setDirectEditType(DirectEditType type) {
        this.type = type;
    }

    public DirectEditType getDirectEditType() {
        return DirectEditType.EXPRESSION;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.draw2d.Label#paintFigure(org.eclipse.draw2d.Graphics)
     */
    @Override
    protected void paintFigure(Graphics graphics) {
        super.paintFigure(graphics);
        graphics.setForegroundColor(ColorConstants.menuBackground);
        graphics.drawLine(getBounds().x, getBounds().y + getBounds().height - 1, getBounds().x + getBounds().width, getBounds().y
                + getBounds().height - 1);
    }

}
