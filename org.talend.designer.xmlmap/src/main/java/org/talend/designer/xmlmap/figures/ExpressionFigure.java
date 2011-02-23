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

import org.eclipse.draw2d.CompoundBorder;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.PositionConstants;
import org.talend.designer.xmlmap.figures.borders.ColumnBorder;
import org.talend.designer.xmlmap.figures.borders.RowBorder;
import org.talend.designer.xmlmap.parts.TreeNodeEditPart;

/**
 * DOC talend class global comment. Detailled comment
 */
public class ExpressionFigure extends Label implements IExpressionBuilderCell {

    private static final int DEFAULT_SIZE = 200;

    private XmlTreeBranch treeBranch;

    private TreeNodeEditPart treeNodePart;

    public ExpressionFigure() {
        setBorder(new CompoundBorder(new RowBorder(), new ColumnBorder()));
        setLabelAlignment(PositionConstants.LEFT);
    }

    public XmlTreeBranch getTreeBranch() {
        return treeBranch;
    }

    public void setTreeBranch(XmlTreeBranch treeBranch) {
        this.treeBranch = treeBranch;
    }

    public TreeNodeEditPart getTreeNodePart() {
        return treeNodePart;
    }

    public void setTreeNodePart(TreeNodeEditPart treeNodePart) {
        this.treeNodePart = treeNodePart;
    }

    // @Override
    // public Dimension getPreferredSize(int wHint, int hHint) {
    // Dimension preferredSize = super.getPreferredSize(wHint, hHint);
    // return new Dimension(DEFAULT_SIZE, preferredSize.height);
    // }
    //
    // @Override
    // public Dimension getMinimumSize(int w, int h) {
    // Dimension minimumSize = super.getMinimumSize(w, h);
    // return new Dimension(DEFAULT_SIZE, minimumSize.height);
    // }

}
