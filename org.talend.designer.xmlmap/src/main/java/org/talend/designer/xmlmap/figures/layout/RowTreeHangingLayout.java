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

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.swt.SWT;
import org.talend.commons.ui.swt.geftree.figure.TreeBranch;
import org.talend.commons.ui.swt.geftree.layout.TreeHangingLayout;
import org.talend.designer.xmlmap.figures.XmlTreeBranch;

public class RowTreeHangingLayout extends TreeHangingLayout {

    public RowTreeHangingLayout(XmlTreeBranch branch) {
        super(branch);
    }

    @Override
    public void paintRows(Graphics g) {
        int pY = getBranch().getBounds().y;
        drawRows(g, getBranch(), pY);

    }

    private int drawRows(Graphics g, TreeBranch rootBranch, int pY) {
        g.setLineStyle(SWT.LINE_DOT);
        g.setForegroundColor(generateColor());

        IFigure contents = rootBranch.getContentsPane();

        List children = contents.getChildren();
        if (children.size() == 0)
            return pY;
        for (int i = 0; i < children.size(); i++) {
            int height = 0;
            XmlTreeBranch child = (XmlTreeBranch) children.get(i);
            height = child.getElement().getBounds().height + child.getRoot().getMajorSpacing();
            int x = child.getRoot().getBounds().x;
            pY += height;
            g.drawLine(x, pY, rootBranch.getRoot().getBounds().right(), pY);
            pY = drawRows(g, child, pY);
        }

        return pY;

    }

}
