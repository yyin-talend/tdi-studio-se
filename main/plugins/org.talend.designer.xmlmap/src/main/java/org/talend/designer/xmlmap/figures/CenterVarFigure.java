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
package org.talend.designer.xmlmap.figures;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.MarginBorder;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.ToolbarLayout;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Font;
import org.talend.designer.xmlmap.figures.borders.RowBorder;
import org.talend.designer.xmlmap.figures.layout.EqualWidthLayout;
import org.talend.designer.xmlmap.figures.table.ColumnKeyConstant;
import org.talend.designer.xmlmap.figures.table.ColumnSash;
import org.talend.designer.xmlmap.figures.table.Table;
import org.talend.designer.xmlmap.figures.table.TableColumn;
import org.talend.designer.xmlmap.figures.treetools.VarToolBarFigure;
import org.talend.designer.xmlmap.model.emf.xmlmap.VarNode;
import org.talend.designer.xmlmap.model.emf.xmlmap.VarTable;
import org.talend.designer.xmlmap.parts.VarTableEditPart;
import org.talend.designer.xmlmap.ui.resource.ColorInfo;
import org.talend.designer.xmlmap.ui.resource.ColorProviderMapper;
import org.talend.designer.xmlmap.ui.resource.FontInfo;
import org.talend.designer.xmlmap.ui.resource.FontProviderMapper;

/**
 * DOC hywang class global comment. Detailled comment
 */
public class CenterVarFigure extends Figure {

    protected Figure header;

    private VarTableEditPart tablePart;

    private VarTable table;

    protected VarToolBarFigure imageButtonsFigure;

    private List<VarNode> selectionNodes = new ArrayList<VarNode>();

    private Table tableFigure;

    public CenterVarFigure(VarTableEditPart tablePart) {
        this.tablePart = tablePart;
        this.table = (VarTable) tablePart.getModel();
        this.setOpaque(true);
        this.setBorder(new LineBorder());
        this.setBackgroundColor(ColorConstants.white);
        CenterVarTableLayout mainLayout = new CenterVarTableLayout();
        this.setLayoutManager(mainLayout);
        createComponents();
    }

    /**
     * DOC hywang Comment method "createComponents".
     */
    protected void createComponents() {
        setBorder(new LineBorder(ColorProviderMapper.getColor(ColorInfo.COLOR_TREE_BORDER)));
        header = new Figure();
        header.setOpaque(true);
        header.setBackgroundColor(ColorProviderMapper.getColor(ColorInfo.ZONE_BACKGROUND_COLOR));
        header.setBorder(new RowBorder());
        header.setLayoutManager(new EqualWidthLayout());
        Label varText = new Label();
        varText.setText(table.getName());
        Font erFont = FontProviderMapper.getFont(FontInfo.FONT_SYSTEM_BOLD);
        varText.setFont(erFont);
        varText.setLabelAlignment(PositionConstants.LEFT);
        varText.setBorder(new MarginBorder(5, 10, 5, -1));

        imageButtonsFigure = new VarToolBarFigure(tablePart);
        header.add(varText);
        header.add(imageButtonsFigure);

        // var table
        tableFigure = new Table();
        TableColumn column = new TableColumn(ColumnKeyConstant.KEY_EXPRESSION);
        column.setText("Expression");
        tableFigure.addColumn(column);

        ColumnSash sash = new ColumnSash(tableFigure);
        sash.setLeftColumn(column);
        tableFigure.addSeparator(sash);

        column = new TableColumn(ColumnKeyConstant.KEY_TYPE);
        column.setText("Type");
        sash.setRightColumn(column);
        tableFigure.addColumn(column);

        sash = new ColumnSash(tableFigure);
        sash.setLeftColumn(column);
        tableFigure.addSeparator(sash);

        column = new TableColumn(ColumnKeyConstant.KEY_VARIABLE);
        column.setText("Variable");
        sash.setRightColumn(column);
        tableFigure.addColumn(column);

        this.add(header);
        this.add(tableFigure);

    }

    public Figure getHeader() {
        return this.header;
    }

    public Figure getTableItemContainer() {
        return tableFigure.getTableItemContainer();
    }

    public List<VarNode> getSelectionNodes() {
        return this.selectionNodes;
    }

    public VarToolBarFigure getToolBarFigure() {
        return this.imageButtonsFigure;
    }

    class CenterVarTableLayout extends ToolbarLayout {

        @Override
        public void layout(IFigure parent) {
            super.layout(parent);
            if (table.isMinimized()) {
                tableFigure.setBounds(new Rectangle());
            }
        }

        @Override
        protected Dimension calculatePreferredSize(IFigure container, int wHint, int hHint) {
            Dimension dimension = super.calculatePreferredSize(container, wHint, hHint);
            if (table.isMinimized()) {
                Dimension tableSize = tableFigure.getPreferredSize();
                dimension.height -= tableSize.height;
            }
            return dimension;
        }
    }

}
