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

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.talend.commons.ui.swt.geftree.figure.TreeRoot;

/**
 * wchen class global comment. Detailled comment
 */
public class XmlTreeRoot extends XmlTreeBranch {

    public XmlTreeRoot(IFigure title, int style) {
        super(title, style, false);
    }

    @Override
    protected void paintFigure(Graphics graphics) {
        super.paintFigure(graphics);
        if (isExpanded())
            getBranchLayout().paintRows(graphics);
    }

    /**
     * @see org.eclipse.draw2d.Figure#validate()
     */
    public void validate() {
        if (isValid())
            return;
        setRowHeights(getPreferredRowHeights(), 0);
        super.validate();
    }

    public TreeRoot getRoot() {
        return this;
    }
}
