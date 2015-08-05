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

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.MouseEvent;
import org.eclipse.draw2d.MouseListener;
import org.talend.designer.xmlmap.figures.ExpressionFigure;
import org.talend.designer.xmlmap.figures.layout.RowLayout;
import org.talend.designer.xmlmap.model.emf.xmlmap.InputXmlTree;
import org.talend.designer.xmlmap.model.emf.xmlmap.OutputTreeNode;
import org.talend.designer.xmlmap.model.emf.xmlmap.TreeNode;
import org.talend.designer.xmlmap.parts.TreeNodeEditPart;
import org.talend.designer.xmlmap.ui.resource.ColorInfo;
import org.talend.designer.xmlmap.ui.resource.ColorProviderMapper;
import org.talend.designer.xmlmap.util.XmlMapUtil;

/**
 * DOC talend class global comment. Detailled comment
 */
public class RowFigure extends Figure {

    private TreeBranch treeBranch;

    private ExpressionFigure expression;

    private final TreeSelectManager selectManager = TreeSelectManager.getManager();

    private TreeBranchContent branchContent;

    private TreeNode treeNode;

    public RowFigure(TreeNodeEditPart treeNodePart) {
        this(treeNodePart, true);
    }

    public RowFigure(TreeNodeEditPart treeNodePart, boolean enableExpaned) {
        this.treeNode = (TreeNode) treeNodePart.getModel();
        setLayoutManager(new RowLayout(this, treeNodePart));
        boolean isLookup = false;
        if (treeNode instanceof OutputTreeNode) {
            isLookup = true;
        } else {
            TreeNode inputTreeNodeRoot = XmlMapUtil.getTreeNodeRoot(treeNode);
            if (inputTreeNodeRoot != null && inputTreeNodeRoot.eContainer() instanceof InputXmlTree) {
                isLookup = ((InputXmlTree) inputTreeNodeRoot.eContainer()).isLookup();
            }
        }

        if (isLookup) {
            expression = new ExpressionFigure();
            expression.setText(treeNode.getExpression());
            if (!XmlMapUtil.isExpressionEditable(treeNode)) {
                expression.setOpaque(true);
                expression.setBackgroundColor(ColorProviderMapper.getColor(ColorInfo.COLOR_EXPREESION_DISABLE));
            } else {
                expression.setOpaque(false);
            }
            this.add(expression);
        }

        branchContent = new TreeBranchContent(treeNodePart);

        treeBranch = new TreeBranch(branchContent, enableExpaned);
        this.add(treeBranch);

        this.addMouseListener(new MouseListener.Stub() {

            public void mousePressed(MouseEvent me) {
                selectManager.setSelection(RowFigure.this);
            }
        });

        // ///////////////test
        // setOpaque(true);
        // setBackgroundColor(ColorConstants.red);

    }

    public TreeBranch getTreeBranch() {
        return this.treeBranch;
    }

    public ExpressionFigure getExpressionFigure() {
        return this.expression;

    }

    public TreeNodeFigure getTreeNodeFigure() {
        return (TreeNodeFigure) getParent();
    }

    public void setSelected(boolean selected) {
        // if (originalBgColor == null) {
        // originalBgColor = this.getBackgroundColor();
        // }

        // if (selected) {
        // this.setOpaque(true);
        // this.setBackgroundColor(ColorConstants.green);
        // } else {
        // this.setOpaque(false);
        // this.setBackgroundColor(originalBgColor);
        // }
    }

    public RowLayout getRowLayout() {
        return (RowLayout) getLayoutManager();
    }

    public TreeBranchContent getBranchContent() {
        return this.branchContent;
    }

    public void updateExpression() {
        if (expression != null) {
            // TDI-18185
            if (XmlMapUtil.DOCUMENT.equals(treeNode.getType())) {
                expression.setText("");
            } else {
                expression.setText(treeNode.getExpression());
            }
        }
    }
}
