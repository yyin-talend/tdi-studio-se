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

import org.eclipse.draw2d.Label;
import org.talend.designer.xmlmap.parts.TreeNodeEditPart;

/**
 * DOC talend class global comment. Detailled comment
 */
public class ExpressionFigure extends Label {

    private XmlTreeBranch treeBranch;

    private TreeNodeEditPart treeNodePart;

    public ExpressionFigure() {

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

}
