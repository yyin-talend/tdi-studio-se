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
import org.eclipse.swt.widgets.Composite;
import org.talend.designer.xmlmap.figures.borders.RowBorder;
import org.talend.designer.xmlmap.figures.layout.EqualWidthLayout;
import org.talend.designer.xmlmap.figures.layout.TreeLayout;
import org.talend.designer.xmlmap.figures.treesettings.FilterContainer;
import org.talend.designer.xmlmap.figures.treesettings.OutputTreeSettingContainer;
import org.talend.designer.xmlmap.figures.treetools.TreeToolBarContainer;
import org.talend.designer.xmlmap.model.emf.xmlmap.OutputXmlTree;
import org.talend.designer.xmlmap.parts.OutputXmlTreeEditPart;
import org.talend.designer.xmlmap.ui.resource.ColorInfo;
import org.talend.designer.xmlmap.ui.resource.ColorProviderMapper;
import org.talend.designer.xmlmap.ui.resource.FontInfo;
import org.talend.designer.xmlmap.ui.resource.FontProviderMapper;

/**
 * wchen class global comment. Detailled comment
 */
public class OutputXmlTreeFigure extends AbstractInOutTreeFigure {

    protected Figure columnContainer;

    protected OutputXmlTree xmlTree;

    protected Figure tableColumnstitle;

    private OutputTreeSettingContainer settingContainer;

    private OutputXmlTreeEditPart treePart;

    public OutputXmlTreeFigure(OutputXmlTreeEditPart treePart) {
        this.treePart = treePart;
        this.xmlTree = (OutputXmlTree) treePart.getModel();
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
        tableName.setText(xmlTree.getName());
        tableName.setFont(FontProviderMapper.getFont(FontInfo.FONT_SYSTEM_BOLD));
        tableName.setLabelAlignment(PositionConstants.LEFT);
        tableName.setBorder(new MarginBorder(5, 10, 5, -1));

        header.add(tableName);

        imageButtonsFigure = new TreeToolBarContainer(treePart);

        header.setOpaque(true);
        header.setBackgroundColor(ColorProviderMapper.getColor(ColorInfo.ZONE_BACKGROUND_COLOR));
        this.add(header);

        header.add(imageButtonsFigure);
        settingContainer = new OutputTreeSettingContainer(xmlTree);
        this.add(settingContainer);

        filterFigure = new FilterContainer(treePart, (Composite) treePart.getViewer().getControl());
        this.add(filterFigure);

        tableColumnstitle = new ColumnTitleFigure();
        this.add(tableColumnstitle);

        ScrollPane scroll = new ScrollPane();
        // scroll.setLayoutManager(new CustomScrollPaneLayout());
        scroll.setVerticalScrollBarVisibility(ScrollPane.NEVER);
        columnContainer = new Figure();
        scroll.getViewport().setContents(columnContainer);
        ToolbarLayout layout = new ToolbarLayout();
        layout.setStretchMinorAxis(true);
        columnContainer.setLayoutManager(layout);
        columnContainer.setOpaque(true);
        columnContainer.setBackgroundColor(ColorConstants.white);
        // this.add(columnContainer);
        this.add(scroll);
    }

    public void resetHeaderColor() {
        header.setBackgroundColor(ColorProviderMapper.getColor(ColorInfo.ZONE_BACKGROUND_COLOR));
    }

    public void update(int type) {
        settingContainer.update(type);
        filterFigure.update(type);
        imageButtonsFigure.updateMinSizeImage();
    }

    public IFigure getColumnContainer() {
        return this.columnContainer;
    }

    class ColumnTitleFigure extends Figure {

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
