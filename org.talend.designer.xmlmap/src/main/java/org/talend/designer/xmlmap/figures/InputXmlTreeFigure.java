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
import org.talend.designer.xmlmap.figures.borders.RowBorder;
import org.talend.designer.xmlmap.figures.layout.EqualWidthLayout;
import org.talend.designer.xmlmap.figures.layout.TreeLayout;
import org.talend.designer.xmlmap.figures.treesettings.InputTreeSettingContainer;
import org.talend.designer.xmlmap.figures.treetools.InputTreeToolBarContainer;
import org.talend.designer.xmlmap.model.emf.xmlmap.InputXmlTree;
import org.talend.designer.xmlmap.ui.resource.ColorInfo;
import org.talend.designer.xmlmap.ui.resource.ColorProviderMapper;
import org.talend.designer.xmlmap.ui.resource.FontInfo;
import org.talend.designer.xmlmap.ui.resource.FontProviderMapper;

/**
 * wchen class global comment. Detailled comment
 */
public class InputXmlTreeFigure extends GenericFigure {

    protected Figure columnContainer;

    protected InputXmlTree xmlTree;

    protected Figure tableColumnstitle;

    private Figure header;

    private InputTreeToolBarContainer imageButtonsFigure;

    private InputTreeSettingContainer settingContainer;

    public InputXmlTreeFigure(InputXmlTree xmlTree) {
        this.xmlTree = xmlTree;
        createContents();
    }

    protected void createContents() {
        setLayoutManager(new TreeLayout(xmlTree));
        this.setBorder(new LineBorder(ColorProviderMapper.getColor(ColorInfo.COLOR_TREE_BORDER)));

        header = new Figure();
        header.setOpaque(true);
        header.setBackgroundColor(ColorConstants.tooltipBackground);
        header.setBorder(new RowBorder());
        header.setLayoutManager(new EqualWidthLayout());
        Label tableName = new Label();
        // tableName.setBorder(new LineBorder(ColorConstants.black));
        tableName.setText((xmlTree.isLookup() ? "lookup : " : "main :") + xmlTree.getName());
        tableName.setFont(FontProviderMapper.getFont(FontInfo.FONT_SYSTEM_BOLD));
        tableName.setLabelAlignment(PositionConstants.LEFT);
        tableName.setBorder(new MarginBorder(5, 10, 5, -1));

        header.add(tableName);
        imageButtonsFigure = new InputTreeToolBarContainer(xmlTree);

        header.setOpaque(true);
        header.setBackgroundColor(ColorConstants.yellow);
        this.add(header);

        if (xmlTree.isLookup()) {
            header.add(imageButtonsFigure);
            settingContainer = new InputTreeSettingContainer(xmlTree);
            this.add(settingContainer);
        }

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
        // this.add(columnContainer);
        this.add(scroll);
    }

    public IFigure getColumnContainer() {
        return this.columnContainer;
    }

    public void update(int type) {
        settingContainer.update(type);

    }

    class ColumnTitleFigure extends Figure {

        public ColumnTitleFigure() {
            if (xmlTree.isLookup()) {
                Label expression = new Label();
                expression.setText("Exp.key");
                expression.setBorder(new MarginBorder(3, 10, 3, -1));
                expression.setLabelAlignment(PositionConstants.LEFT);
                this.add(expression);
            }

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
