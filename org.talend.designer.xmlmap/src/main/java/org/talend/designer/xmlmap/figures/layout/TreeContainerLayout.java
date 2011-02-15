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
package org.talend.designer.xmlmap.figures.layout;

import java.util.List;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.MarginBorder;
import org.eclipse.draw2d.ToolbarLayout;
import org.eclipse.draw2d.geometry.Rectangle;

/**
 * DOC talend class global comment. Detailled comment
 */
public class TreeContainerLayout extends ToolbarLayout {

    private XmlMapDataLayout parentLayout;

    private Figure mainFigure;

    public TreeContainerLayout(Figure mainFigure, XmlMapDataLayout parentLayout) {
        this.parentLayout = parentLayout;
        this.mainFigure = mainFigure;
    }

    @Override
    public void layout(IFigure parent) {
        super.layout(parent);
        List children = parent.getChildren();
        int numChildren = children.size();
        if (parentLayout != null) {

            if (parentLayout.getDevideWidth() == 0) {
                parentLayout.layout(mainFigure);
            }
            int availableWidth = parentLayout.getDevideWidth();
            if (parent.getBorder() instanceof MarginBorder) {
                MarginBorder border = (MarginBorder) parent.getBorder();
                availableWidth = availableWidth - border.getInsets(parent).left - border.getInsets(parent).right;
            }

            for (int i = 0; i < numChildren; i++) {
                IFigure child = (IFigure) children.get(i);
                Rectangle bounds = child.getBounds().getCopy();
                bounds.width = availableWidth;
                child.setBounds(bounds);
            }
        }

    }
}
