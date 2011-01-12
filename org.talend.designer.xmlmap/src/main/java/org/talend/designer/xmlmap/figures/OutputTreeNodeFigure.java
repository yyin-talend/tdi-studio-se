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
package org.talend.designer.xmlmap.figures;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.LineBorder;
import org.talend.designer.xmlmap.figures.layout.ExpressionLayout;
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

    public OutputTreeNodeFigure(OutputTreeNodeEditPart treeNodePart) {
        this.treeNodePart = treeNodePart;
        this.treeNode = (OutputTreeNode) treeNodePart.getModel();
        createContent();
    }

    private void createContent() {
        // normal column
        if (!XmlMapUtil.DOCUMENT.equals(treeNode.getType())) {
            columnExpressionFigure = new Label();
            columnExpressionFigure.setText(treeNode.getExpression());
            columnExpressionFigure.setBorder(new LineBorder());

            Label valueLabel = new Label();
            valueLabel.setText(treeNode.getName());
            valueLabel.setBorder(new LineBorder());

            this.add(columnExpressionFigure);
            this.add(valueLabel);
        }
        // xml root
        else if (XmlMapUtil.DOCUMENT.equals(treeNode.getType()) && treeNode.eContainer() instanceof OutputXmlTree) {
            /* column1,expression */
            treeNodeExpressionFigure = new Figure();
            treeNodeExpressionFigure.setLayoutManager(new ExpressionLayout());
            this.add(treeNodeExpressionFigure);
            ExpressionFigure figure = new ExpressionFigure();

            // label.setText(((OutputTreeNode) childPart.getModel()).getExpression());
            figure.setBorder(new LineBorder());
            treeNodeExpressionFigure.add(figure, 0);
            // outputTreeNodeExpressionFigure.setOpaque(true);
            // outputTreeNodeExpressionFigure.setBackgroundColor(ColorConstants.red);

            /* column2, column */
            treeBranch = new XmlTreeRoot(new TreeBranchFigure(treeNode), XmlTreeBranch.STYLE_ROW_HANGING);
            treeBranch.setBorder(new LineBorder());

            figure.setTreeBranch(treeBranch);
            figure.setTreeNodePart(treeNodePart);
            treeBranch.setExpressionFigure(figure);

            // outputTreeNodeNameFigure.generateSplitFigures();
            // createTreeNode(root, treeNode.getChildren());
            this.add(treeBranch);
        }
        /* column3,node status */
        // Label nodeStatus = new Label();
        // // nodeStatus.setText("element loop");
        // nodeStatus.setBorder(new LineBorder());
        // this.add(nodeStatus);

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

}
