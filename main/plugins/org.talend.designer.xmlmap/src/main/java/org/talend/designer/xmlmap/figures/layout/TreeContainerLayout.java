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
package org.talend.designer.xmlmap.figures.layout;

import java.util.List;

import org.eclipse.draw2d.Border;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.ToolbarLayout;
import org.eclipse.draw2d.Viewport;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.emf.common.util.EList;
import org.talend.designer.xmlmap.figures.InputXmlTreeFigure;
import org.talend.designer.xmlmap.model.emf.xmlmap.IConnection;
import org.talend.designer.xmlmap.model.emf.xmlmap.InputXmlTree;
import org.talend.designer.xmlmap.model.emf.xmlmap.TreeNode;
import org.talend.designer.xmlmap.util.XmlMapUtil;

/**
 * DOC talend class global comment. Detailled comment
 */
public class TreeContainerLayout extends ToolbarLayout {

    public TreeContainerLayout() {
    }

    @Override
    public void layout(IFigure parent) {
        Rectangle vBounds = null;
        if (parent.getParent() instanceof Viewport) {
            vBounds = ((Viewport) parent.getParent()).getBounds();
        }

        int wHint = -1;
        int hHint = -1;
        if (isHorizontal()) {
            hHint = parent.getClientArea(Rectangle.SINGLETON).height;
        } else {
            wHint = parent.getClientArea(Rectangle.SINGLETON).width;
        }

        List children = parent.getChildren();
        int numChildren = children.size();
        Rectangle clientArea = transposer.t(parent.getClientArea());
        int x = clientArea.x;
        int y = clientArea.y;
        int availableHeight = clientArea.height;

        Dimension prefSizes[] = new Dimension[numChildren];
        Dimension minSizes[] = new Dimension[numChildren];

        IFigure child;
        int totalHeight = 0;
        int totalMinHeight = 0;
        int prefMinSumHeight = 0;
        int connSize = 0;
        boolean isLookupConnMax = true;
        for (int i = 0; i < numChildren; i++) {
            child = (IFigure) children.get(i);

            prefSizes[i] = transposer.t(getChildPreferredSize(child, wHint, hHint));
            minSizes[i] = transposer.t(getChildMinimumSize(child, wHint, hHint));

            totalHeight += prefSizes[i].height;
            totalMinHeight += minSizes[i].height;

            if (child instanceof InputXmlTreeFigure) {
                InputXmlTree inputModel = ((InputXmlTreeFigure) child).getInputXmlTree();
                EList<TreeNode> nodeList = inputModel.getNodes();
                int maxSize = 0;
                List<IConnection> lookConnections = XmlMapUtil.getAllNodeLookConnections(inputModel);
                List<IConnection> filterConnections = XmlMapUtil.getInputTreeFilterConnections(inputModel);
                if (lookConnections.size() < filterConnections.size()) {
                    isLookupConnMax = false;
                    maxSize = filterConnections.size();
                } else {
                    maxSize = lookConnections.size();
                }

                if (connSize < maxSize) {
                    connSize = maxSize;
                }

            }
        }
        totalHeight += (numChildren - 1) * spacing;
        totalMinHeight += (numChildren - 1) * spacing;
        prefMinSumHeight = totalHeight - totalMinHeight;

        int defaultSize = XmlMapUtil.DEFAULT_OFFSET;
        if (!isLookupConnMax) {
            defaultSize = XmlMapUtil.DEFAULT_OFFSET_FILTER;
        }

        for (int i = 0; i < numChildren; i++) {
            int amntShrinkCurrentHeight = 0;
            int prefHeight = prefSizes[i].height;
            int minHeight = minSizes[i].height;
            int prefWidth = prefSizes[i].width;
            int minWidth = minSizes[i].width;
            child = (IFigure) children.get(i);
            Rectangle newBounds = new Rectangle(x, y, prefWidth, prefHeight);
            if (connSize > 0) {
                newBounds = new Rectangle(x + (connSize - 1) * defaultSize, y, prefWidth, prefHeight);
            }

            Border border = parent.getBorder();
            Insets insets = border.getInsets(child);

            if (vBounds != null) {
                newBounds.width = vBounds.width - insets.left - insets.right;
                if (connSize > 0) {
                    newBounds.width = newBounds.width - (connSize - 1) * defaultSize;
                }
            }

            newBounds.height -= amntShrinkCurrentHeight;

            child.setBounds(transposer.t(newBounds));

            prefMinSumHeight -= (prefHeight - minHeight);
            y += newBounds.height + spacing;
        }

    }

    @Override
    protected Dimension calculatePreferredSize(IFigure container, int wHint, int hHint) {
        // TODO Auto-generated method stub
        return super.calculatePreferredSize(container, wHint, hHint);
    }

}
