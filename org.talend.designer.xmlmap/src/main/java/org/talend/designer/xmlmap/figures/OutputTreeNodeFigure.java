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
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.talend.designer.xmlmap.figures.borders.ColumnBorder;
import org.talend.designer.xmlmap.figures.borders.RowBorder;
import org.talend.designer.xmlmap.figures.layout.ExpressionLayout;
import org.talend.designer.xmlmap.model.emf.xmlmap.InputXmlTree;
import org.talend.designer.xmlmap.model.emf.xmlmap.OutputTreeNode;
import org.talend.designer.xmlmap.model.emf.xmlmap.OutputXmlTree;
import org.talend.designer.xmlmap.parts.OutputTreeNodeEditPart;
import org.talend.designer.xmlmap.util.XmlMapUtil;

/**
 * wchen class global comment. Detailled comment
 */
public class OutputTreeNodeFigure extends TreeNodeFigure {

    private XmlTreeBranch treeBranch;

    private OutputTreeNodeEditPart treeNodePart;

    private OutputTreeNode treeNode;

    private Label nameLabel;

    public OutputTreeNodeFigure(OutputTreeNodeEditPart treeNodePart) {
        this.treeNodePart = treeNodePart;
        this.treeNode = (OutputTreeNode) treeNodePart.getModel();
        createContent();
    }

    private void createContent() {
        // normal column
        if (!XmlMapUtil.DOCUMENT.equals(treeNode.getType())) {
            columnExpressionFigure = new ExpressionFigure();
            columnExpressionFigure.setText(treeNode.getExpression());
            CompoundBorder compoundBorder = new CompoundBorder(new RowBorder(), new ColumnBorder());
            columnExpressionFigure.setBorder(compoundBorder);

            nameLabel = new Label();
            nameLabel.setText(treeNode.getName());
            nameLabel.setBorder(new RowBorder());

            this.add(columnExpressionFigure);
            this.add(nameLabel);
        }
        // xml root
        else if (XmlMapUtil.DOCUMENT.equals(treeNode.getType()) && treeNode.eContainer() instanceof OutputXmlTree) {
            expressionContainer = new Figure();
            expressionContainer.setLayoutManager(new ExpressionLayout());
            this.add(expressionContainer);
            ExpressionFigure figure = new ExpressionFigure();
            expressionContainer.add(figure, 0);

            treeBranch = new XmlTreeRoot(new TreeBranchFigure(treeNode), XmlTreeBranch.STYLE_ROW_HANGING);
            treeBranch.setBorder(new RowBorder());

            figure.setTreeBranch(treeBranch);
            figure.setTreeNodePart(treeNodePart);
            treeBranch.setExpressionFigure(figure);

            this.add(treeBranch);
        }

    }

    public IFigure getContentPane() {
        if (!XmlMapUtil.DOCUMENT.equals(treeNode.getType())) {
            return this;
        } else {
            return treeBranch.getContentsPane();
        }
    }

    public Label getColumnExpressionFigure() {
        return columnExpressionFigure;
    }

    public XmlTreeBranch getTreeBranch() {
        return treeBranch;
    }

    public void updateNameFigure() {
        if (!XmlMapUtil.DOCUMENT.equals(treeNode.getType())) {
            nameLabel.setText(treeNode.getName());
        } else if (XmlMapUtil.DOCUMENT.equals(treeNode.getType()) && treeNode.eContainer() instanceof InputXmlTree) {
            ((TreeBranchFigure) treeBranch.getElement()).updataNameFigure();
        }
    }

    /**
     * used to change figure if talend type switched form Document to others
     */
    public void refreshChildren() {
        this.getChildren().clear();
        createContent();
    }
}
