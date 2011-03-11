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
package org.talend.designer.xmlmap.parts;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.gef.EditPart;
import org.talend.designer.xmlmap.figures.ExpressionFigure;
import org.talend.designer.xmlmap.figures.OutputTreeNodeFigure;
import org.talend.designer.xmlmap.figures.TreeBranchFigure;
import org.talend.designer.xmlmap.figures.XmlTreeBranch;
import org.talend.designer.xmlmap.figures.layout.TreeNodeLayout;
import org.talend.designer.xmlmap.model.emf.xmlmap.OutputTreeNode;
import org.talend.designer.xmlmap.model.emf.xmlmap.TreeNode;
import org.talend.designer.xmlmap.util.XmlMapUtil;

/**
 * wchen class global comment. Detailled comment
 */
public class OutputTreeNodeEditPart extends TreeNodeEditPart {

    @Override
    protected IFigure createFigure() {
        OutputTreeNode model = (OutputTreeNode) getModel();
        IFigure figure = null;
        // nodes in xml tree
        if (XmlMapUtil.getXPathLength(model.getXpath()) > 2) {
            figure = new XmlTreeBranch(new TreeBranchFigure(model), XmlTreeBranch.STYLE_ROW_HANGING);
            treeBranchFigure = (XmlTreeBranch) figure;
        }
        // normal column and tree root
        else {
            figure = new OutputTreeNodeFigure(this);
            figure.setLayoutManager(new TreeNodeLayout(findOutputXmlTreePart(this)));
            // figure.setLayoutManager(new EqualWidthLayout());
        }
        return figure;
    }

    @Override
    public List getModelChildren() {
        TreeNode model = (TreeNode) getModel();
        return model.getChildren();
    }

    @Override
    protected List getModelTargetConnections() {
        OutputTreeNode model = (OutputTreeNode) getModel();
        return model.getIncomingConnections();
    }

    @Override
    protected void addChildVisual(EditPart childEditPart, int index) {
        OutputTreeNodeEditPart childPart = (OutputTreeNodeEditPart) childEditPart;
        OutputTreeNodeEditPart parentPart = (OutputTreeNodeEditPart) childEditPart.getParent();
        IFigure parentFigure = parentPart.getFigure();
        // when add one child node of a tree,should also add a label for firstColumn
        /* 1.should find the related root outputTreeNodeFigure */
        OutputTreeNodeFigure rootOutputTreeNodeFigure = (OutputTreeNodeFigure) findRootTreeNodeFigure(parentFigure);
        /* 2.add label to first column of rootOutputTreeNodeFigure */
        if (childPart.getFigure() instanceof XmlTreeBranch && rootOutputTreeNodeFigure != null) {
            Figure rootOutputTreeNodeExpressionFigure = rootOutputTreeNodeFigure.getExpressionContainer();
            ExpressionFigure expressionFigure = new ExpressionFigure();
            expressionFigure.setText(((OutputTreeNode) childPart.getModel()).getExpression());
            XmlTreeBranch treeBranch = (XmlTreeBranch) childPart.getFigure();
            expressionFigure.setTreeBranch(treeBranch);
            expressionFigure.setTreeNodePart(childPart);

            treeBranch.setExpressionFigure(expressionFigure);
            Map<TreeNode, Integer> nodeAndIndex = new HashMap<TreeNode, Integer>();
            int expressionIndex = getExpressionIndex((OutputTreeNode) childPart.getModel(),
                    getModelTreeRoot((OutputTreeNode) childPart.getModel()), 0, nodeAndIndex);
            rootOutputTreeNodeExpressionFigure.add(expressionFigure, expressionIndex);
            getViewer().getVisualPartMap().put(expressionFigure, childEditPart);
        }
        super.addChildVisual(childEditPart, index);
    }

    @Override
    public IFigure getContentPane() {
        if (getFigure() instanceof OutputTreeNodeFigure) {
            return ((OutputTreeNodeFigure) getFigure()).getContentPane();
        } else if (getFigure() instanceof XmlTreeBranch) {
            return ((XmlTreeBranch) getFigure()).getContentsPane();
        }
        return super.getContentPane();
    }

    private OutputXmlTreeEditPart findOutputXmlTreePart(OutputTreeNodeEditPart treeNodePart) {
        if (treeNodePart.getParent() instanceof OutputXmlTreeEditPart) {
            return (OutputXmlTreeEditPart) treeNodePart.getParent();
        } else {
            return findOutputXmlTreePart((OutputTreeNodeEditPart) treeNodePart.getParent());
        }

    }

}
