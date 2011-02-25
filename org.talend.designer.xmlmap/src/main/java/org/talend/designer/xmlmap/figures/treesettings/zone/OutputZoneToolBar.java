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
package org.talend.designer.xmlmap.figures.treesettings.zone;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.RoundedRectangle;
import org.eclipse.draw2d.ToolbarLayout;
import org.eclipse.draw2d.geometry.Dimension;

/**
 * wchen class global comment. Detailled comment
 */
public class OutputZoneToolBar extends Figure {

    private static final int DEFAULT_HEIGHT = 30;

    private RoundedRectangle test;

    private Label autoMap;

    public OutputZoneToolBar() {
        createContent();
    }

    private void createContent() {
        ToolbarLayout manager = new ToolbarLayout() {

            @Override
            protected Dimension calculatePreferredSize(IFigure container, int wHint, int hHint) {
                Dimension dimension = super.calculatePreferredSize(container, wHint, hHint);
                dimension.height = DEFAULT_HEIGHT;
                return dimension;
            }
        };
        manager.setVertical(false);
        setLayoutManager(manager);
        test = new RoundedRectangle();
        test.setSize(new Dimension(10, 10));
        test.setCornerDimensions(new Dimension(3, 3));
        this.add(test);

        autoMap = new Label();
        autoMap.setText("auto map");
        this.add(autoMap);
    }

}
