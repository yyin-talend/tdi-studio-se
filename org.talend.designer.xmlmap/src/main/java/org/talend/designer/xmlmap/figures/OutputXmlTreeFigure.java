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
import org.eclipse.draw2d.MarginBorder;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.ScrollPane;
import org.eclipse.draw2d.ToolbarLayout;
import org.eclipse.swt.graphics.Font;
import org.talend.designer.xmlmap.figures.layout.EqualWidthLayout;
import org.talend.designer.xmlmap.model.emf.xmlmap.OutputXmlTree;
import org.talend.designer.xmlmap.ui.resource.ColorInfo;
import org.talend.designer.xmlmap.ui.resource.ColorProviderMapper;
import org.talend.designer.xmlmap.ui.resource.FontInfo;
import org.talend.designer.xmlmap.ui.resource.FontProviderMapper;

/**
 * wchen class global comment. Detailled comment
 */
public class OutputXmlTreeFigure extends GenericFigure {

    protected Figure columnContainer;

    protected OutputXmlTree xmlTree;

    protected Figure tableColumnstitle;

    public OutputXmlTreeFigure(OutputXmlTree xmlTree) {
        this.xmlTree = xmlTree;
        createContents();
    }

    protected void createContents() {
        setLayoutManager(new ToolbarLayout());
        this.setBorder(new LineBorder(ColorProviderMapper.getColor(ColorInfo.COLOR_TREE_BORDER)));
        Label tableName = new Label();
        tableName.setText(xmlTree.getName());
        Font erFont = FontProviderMapper.getFont(FontInfo.FONT_SYSTEM_BOLD);
        tableName.setFont(erFont);
        tableName.setLabelAlignment(PositionConstants.LEFT);
        tableName.setBorder(new MarginBorder(5, 10, 5, -1));
        tableName.setOpaque(true);
        tableName.setBackgroundColor(ColorConstants.yellow);
        this.add(tableName);

        tableColumnstitle = new ColumnTitleFigure();
        this.add(tableColumnstitle);

        ScrollPane scroll = new ScrollPane();
        // scroll.setLayoutManager(new CustomScrollPaneLayout());
        scroll.setVerticalScrollBarVisibility(ScrollPane.NEVER);
        columnContainer = new Figure();
        scroll.getViewport().setContents(columnContainer);
        ToolbarLayout layout = new ToolbarLayout();
        layout.setStretchMinorAxis(true);
        layout.setVertical(true);
        columnContainer.setLayoutManager(layout);
        columnContainer.setOpaque(true);
        columnContainer.setBackgroundColor(ColorConstants.white);
        // this.add(columnContainer);
        this.add(scroll);
    }

    public IFigure getColumnContainer() {
        return this.columnContainer;
    }

    class ColumnTitleFigure extends ToolBarContainer {

        public ColumnTitleFigure() {

            Label expression = new Label();
            expression.setText("Expression");
            expression.setBorder(new MarginBorder(3, 10, 3, -1));
            expression.setLabelAlignment(PositionConstants.LEFT);
            this.add(expression);

            Label column1 = new Label();
            column1.setText("Column");
            column1.setBorder(new MarginBorder(3, 10, 3, -1));
            column1.setLabelAlignment(PositionConstants.LEFT);
            this.add(column1);
            this.setLayoutManager(new EqualWidthLayout());

            setBackgroundColor(ColorConstants.menuBackground);
            setOpaque(true);
        }

    }
}
