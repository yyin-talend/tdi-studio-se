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
package org.talend.designer.xmlmap.figures.layout;

import java.util.List;

import org.eclipse.draw2d.AbstractLayout;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import org.talend.commons.ui.swt.geftree.figure.TreeBranch;
import org.talend.designer.xmlmap.figures.ExpressionFigure;

/**
 * wchen class global comment. Detailled comment
 */
public class ExpressionLayout extends AbstractLayout {

    public void layout(IFigure parent) {
        List children = parent.getChildren();
        int numChildren = children.size();
        Rectangle clientArea = parent.getClientArea();
        int cX = clientArea.x;
        int cY = clientArea.y;

        IFigure child;

        if (children.size() == 1) {
            child = (IFigure) children.get(0);
            Rectangle newBounds = new Rectangle(cX, cY, clientArea.width, clientArea.width);
            child.setBounds(newBounds);

        } else {
            int y = cY;
            for (int i = 0; i < numChildren; i++) {
                child = (IFigure) children.get(i);
                if (child instanceof ExpressionFigure) {
                    ExpressionFigure expressionChild = (ExpressionFigure) child;
                    int height = 0;
                    TreeBranch treeBranch = expressionChild.getTreeBranch();
                    if (treeBranch != null) {
                        IFigure element = treeBranch.getElement();
                        height = element.getBounds().height + treeBranch.getRoot().getMajorSpacing();
                        Rectangle newBounds = new Rectangle(cX, y, clientArea.width, height);
                        child.setBounds(newBounds);
                        y = newBounds.bottom();

                    }

                    // Map<TreeNodeEditPart, Label> partExpressionMap = expreessionFigure.getPartExpressionMap();
                    // Iterator<TreeNodeEditPart> iterator = partExpressionMap.keySet().iterator();
                    //
                    // while (iterator.hasNext()) {
                    // TreeNodeEditPart next = iterator.next();
                    // if (partExpressionMap.get(next) == child) {
                    // IFigure figure = next.getFigure();
                    // TreeBranch treeBranch = null;
                    // if (figure instanceof TreeNodeFigure) {
                    // treeBranch = ((TreeNodeFigure) figure).getTreeBranch();
                    // } else if (figure instanceof TreeBranch) {
                    // treeBranch = (TreeBranch) figure;
                    // }
                    // if (treeBranch != null) {
                    // IFigure element = treeBranch.getElement();
                    // height = element.getBounds().height + treeBranch.getRoot().getMajorSpacing();
                    // Rectangle newBounds = new Rectangle(cX, y, clientArea.width, height);
                    // child.setBounds(newBounds);
                    // y = newBounds.bottom();
                    //
                    // }
                    // }
                    // }
                }

            }
        }
    }

    @Override
    protected Dimension calculatePreferredSize(IFigure container, int wHint, int hHint) {

        // no use ,need modify later
        List children = container.getChildren();
        Dimension childSize;
        IFigure child;
        int height = 0, width = 0;
        for (int i = 0; i < children.size(); i++) {
            child = (IFigure) children.get(i);

        }
        return new Dimension(width, height);
    }

}
