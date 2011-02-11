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
package org.talend.designer.xmlmap.figures;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.ToolbarLayout;
import org.talend.commons.ui.swt.geftree.TreeAnimation;
import org.talend.commons.ui.swt.geftree.figure.TreeBranch;
import org.talend.commons.ui.swt.geftree.figure.TreeRoot;
import org.talend.designer.xmlmap.figures.layout.RowTreeHangingLayout;

/**
 * cli class global comment. Detailled comment
 */
@SuppressWarnings("unchecked")
public class XmlTreeBranch extends TreeRoot {

    private ExpressionFigure expressionFigure;

    public static final int STYLE_ROW_HANGING = 3;

    public XmlTreeBranch(IFigure title, int style) {
        super(title, style);
        // TODO Auto-generated constructor stub
    }

    public XmlTreeBranch(IFigure title, int style, boolean enableExpand) {
        super(title, style, enableExpand);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void setStyle(int style) {
        setLayoutManager(new RowTreeHangingLayout(this));
    }

    /**
     * @see org.eclipse.draw2d.Figure#containsPoint(int, int)
     */
    public boolean containsPoint(int x, int y) {
        boolean contains = getElement().containsPoint(x, y) || getContents().containsPoint(x, y);
        if (isEnableExpand()) {
            contains = contains || getExpandCollapseFigure().containsPoint(x, y);
        }
        return contains;
    }

    public void validate() {
        if (isValid())
            return;
        ToolbarLayout layout = new ToolbarLayout(!getRoot().isHorizontal()) {

            public void layout(IFigure parent) {
                TreeAnimation.recordInitialState(parent);
                if (TreeAnimation.playbackState(parent))
                    return;

                super.layout(parent);
            }

        };
        layout.setMinorAlignment(ToolbarLayout.ALIGN_TOPLEFT);
        layout.setStretchMinorAxis(false);
        getContents().setLayoutManager(layout);
        repaint();
        super.validate();
    }

    public TreeRoot getRoot() {
        if (getParent() != null && getParent().getParent() != null) {
            return ((TreeBranch) getParent().getParent()).getRoot();
        }
        return null;
    }

    public ExpressionFigure getExpressionFigure() {
        return expressionFigure;
    }

    public void setExpressionFigure(ExpressionFigure expressionFigure) {
        this.expressionFigure = expressionFigure;
    }

}
