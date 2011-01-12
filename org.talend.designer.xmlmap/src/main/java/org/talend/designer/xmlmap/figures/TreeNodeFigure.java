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
import org.talend.designer.xmlmap.model.emf.xmlmap.InputXmlTree;
import org.talend.designer.xmlmap.model.emf.xmlmap.TreeNode;
import org.talend.designer.xmlmap.parts.TreeNodeEditPart;
import org.talend.designer.xmlmap.util.XmlMapUtil;

/**
 * wchen class global comment. Detailled comment
 */
public class TreeNodeFigure extends ToolBarContainer {

    private XmlTreeBranch treeBranch;

    private TreeNodeEditPart treeNodePart;

    private TreeNode treeNode;

    protected Figure treeNodeExpressionFigure;

    protected Label columnExpressionFigure;

    public TreeNodeFigure() {

    }

    public TreeNodeFigure(TreeNodeEditPart treeNodePart) {
        this.treeNodePart = treeNodePart;
        this.treeNode = (TreeNode) treeNodePart.getModel();
        createContent();
    }

    private void createContent() {
        // column
        if (!XmlMapUtil.DOCUMENT.equals(treeNode.getType())) {
            Label nameLabel = new Label();
            nameLabel.setText(treeNode.getName());
            nameLabel.setBorder(new LineBorder());
            this.add(nameLabel);
        }
        // xml root
        else if (XmlMapUtil.DOCUMENT.equals(treeNode.getType()) && treeNode.eContainer() instanceof InputXmlTree) {
            String status = treeNode.isLoop() ? String.valueOf(treeNode.isLoop()) : "";
            treeBranch = new XmlTreeRoot(new TreeBranchFigure(treeNode), XmlTreeBranch.STYLE_ROW_HANGING);
            treeBranch.setBorder(new LineBorder());
            this.add(treeBranch);
        }
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

    public XmlTreeBranch getTreeBranch() {
        return treeBranch;
    }

    public Figure getTreeNodeExpressionFigure() {
        return this.treeNodeExpressionFigure;
    }

    public Label getColumnExpressionFigure() {
        return columnExpressionFigure;
    }

}
