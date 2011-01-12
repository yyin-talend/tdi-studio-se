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

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.GridLayout;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.swt.graphics.Image;
import org.talend.commons.ui.swt.geftree.ITreeAction;
import org.talend.designer.xmlmap.model.emf.xmlmap.NodeType;
import org.talend.designer.xmlmap.model.emf.xmlmap.OutputTreeNode;
import org.talend.designer.xmlmap.model.emf.xmlmap.TreeNode;
import org.talend.designer.xmlmap.util.XmlMapUtil;

/**
 * wchen class global comment. Detailled comment
 */
public class TreeBranchFigure extends Figure implements ITreeAction {

    private boolean selected;;

    private int alpha = 255;

    protected Label nameFigure;

    protected Label statusFigure;

    private TreeNode node;

    public TreeBranchFigure(TreeNode node) {
        this.node = node;
        GridLayout manager = new GridLayout(2, false);
        manager.horizontalSpacing = 5;
        manager.verticalSpacing = 1;
        manager.marginHeight = 1;
        manager.marginWidth = 1;
        setLayoutManager(manager);
        nameFigure = new Label();
        nameFigure.setText(getTreeBranchName(node));

        statusFigure = new Label();
        statusFigure.setForegroundColor(ColorConstants.red);
        statusFigure.setText(getStatus(node));

        setOpaque(true);

        this.add(nameFigure);
        this.add(statusFigure);
    }

    private String getStatus(TreeNode node) {
        String status = "";
        if (node.isLoop()) {
            status = "(loop :" + String.valueOf(node.isLoop()) + ")";
        } else if (node instanceof OutputTreeNode) {
            OutputTreeNode outputNode = (OutputTreeNode) node;
            if (outputNode.isGroup()) {
                status = "(group :" + String.valueOf(outputNode.isGroup()) + ")";
            }
        }
        return status;
    }

    private String getTreeBranchName(TreeNode model) {
        String name = "";
        if (NodeType.ATTRIBUT.equals(model.getNodeType()) || NodeType.NAME_SPACE.equals(model.getNodeType())) {
            name = model.getXpath().substring(model.getXpath().lastIndexOf(XmlMapUtil.XPATH_SEPARATOR) + 1,
                    model.getXpath().length());
        } else {
            name = model.getName();
        }
        return name;
    }

    public void setSelected(boolean value) {
        // this.selected = value;
        // if (selected) {
        // setBackgroundColor(ColorConstants.blue);
        // } else {
        // setBackgroundColor(null);
        // }
        // setSelectedChild(this, value);
        // repaint();
    }

    private void setSelectedChild(TreeBranchFigure parent, boolean value) {
        if (parent != null) {
            for (Object o : parent.getChildren()) {
                if (o instanceof TreeBranchFigure) {
                    TreeBranchFigure fig = (TreeBranchFigure) o;
                    setSelectedChild(fig, value);
                    fig.setSelected(value);
                }
            }
        }
    }

    public boolean enableSelect() {
        // TODO Auto-generated method stub
        return true;
    }

    public boolean enableDoubleClick() {
        // TODO Auto-generated method stub
        return true;
    }

    public boolean enableHover() {
        // TODO Auto-generated method stub
        return true;
    }

    public void setHoverColor(IFigure fig, boolean enter) {
        if (enableHover()) {
            if (enter) {
                fig.setBackgroundColor(ColorConstants.yellow);
            } else {
                fig.setBackgroundColor(null);
            }
        }
    }

    public Image getElementImage() {
        // TODO Auto-generated method stub
        return null;
    }

    public void dispose() {
        // TODO Auto-generated method stub

    }

    @Override
    public void paint(Graphics graphics) {

        if (alpha != -1) {
            graphics.setAlpha(alpha);
        } else {
            graphics.setAlpha(255);
        }
        super.paint(graphics);
    }

    public int getAlpha() {
        return this.alpha;
    }

    public void setAlpha(int alpha) {
        this.alpha = alpha;
    }

    public void updateStatus() {
        statusFigure.setText(getStatus(node));
    }

}
