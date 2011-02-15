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
package org.talend.designer.xmlmap.figures;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.ScrollPane;
import org.eclipse.draw2d.ToolbarLayout;
import org.talend.designer.xmlmap.figures.layout.EqualWidthLayout;
import org.talend.designer.xmlmap.model.emf.xmlmap.InputXmlTree;
import org.talend.designer.xmlmap.ui.color.ColorInfo;
import org.talend.designer.xmlmap.ui.color.ColorProviderMapper;

/**
 * wchen class global comment. Detailled comment
 */
public class InputXmlTreeFigure extends GenericFigure {

    protected Figure columnContainer;

    protected InputXmlTree xmlTree;

    protected Figure tableColumnstitle;

    public InputXmlTreeFigure(InputXmlTree xmlTree) {
        this.xmlTree = xmlTree;
        createContents();
    }

    protected void createContents() {
        setLayoutManager(new ToolbarLayout());
        this.setBorder(new LineBorder(ColorProviderMapper.getColor(ColorInfo.COLOR_TREE_BORDER)));
        Label tableName = new Label();
        // tableName.setBorder(new LineBorder(ColorConstants.black));
        tableName.setText(xmlTree.getName());

        tableName.setOpaque(true);
        tableName.setBackgroundColor(ColorConstants.yellow);
        this.add(tableName);

        tableColumnstitle = new ColumnTitleFigure();
        this.add(tableColumnstitle);

        ScrollPane scroll = new ScrollPane();
        scroll.setVerticalScrollBarVisibility(ScrollPane.NEVER);
        columnContainer = new Figure();
        scroll.getViewport().setContents(columnContainer);
        ToolbarLayout layout = new ToolbarLayout();
        layout.setStretchMinorAxis(true);
        layout.setVertical(true);
        columnContainer.setLayoutManager(layout);
        columnContainer.setOpaque(true);
        columnContainer.setBackgroundColor(ColorConstants.white);
        this.add(columnContainer);
        // this.add(scroll);
    }

    public IFigure getColumnContainer() {
        return this.columnContainer;
    }

    class ColumnTitleFigure extends Figure {

        private Label column1;

        private Label column0;

        public ColumnTitleFigure() {
            if (xmlTree.isLookup()) {
                column0 = new Label();
                column0.setText("Exp.key");
                this.add(column0);
            }
            column1 = new Label();
            column1.setText("Column");
            this.add(column1);
            this.setLayoutManager(new EqualWidthLayout());
            setBackgroundColor(ColorConstants.menuBackground);
            setOpaque(true);
        }

        public Label getColumn1() {
            return this.column1;
        }

    }
}
