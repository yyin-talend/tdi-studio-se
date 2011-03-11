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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.MarginBorder;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.ToolbarLayout;
import org.eclipse.swt.graphics.Font;
import org.talend.designer.xmlmap.figures.borders.RowBorder;
import org.talend.designer.xmlmap.figures.layout.EqualWidthLayout;
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

    protected VarTableContainerFigure VarTableContainerFigure;

    protected VarToolBarFigure imageButtonsFigure;

    private List<VarNode> selectionNodes = new ArrayList<VarNode>();

    public CenterVarFigure(VarTableEditPart tablePart) {
        this.tablePart = tablePart;
        this.table = (VarTable) tablePart.getModel();
        this.setOpaque(true);
        this.setBorder(new LineBorder());
        this.setBackgroundColor(ColorConstants.white);
        ToolbarLayout mainLayout = new ToolbarLayout();
        mainLayout.setVertical(true);
        this.setLayoutManager(mainLayout);
        createComponents();
    }

    /**
     * DOC hywang Comment method "createComponents".
     */
    protected void createComponents() {
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
        VarTableContainerFigure = new VarTableContainerFigure(table);
        // if (table.isIsMinimized()) {
        // VarTableContainerFigure.setPreferredSize(imageButtonsFigure.getPreferredSize().width, 0);
        // } else {
        // VarTableContainerFigure.setPreferredSize(imageButtonsFigure.getPreferredSize().width, 30);
        // }
        this.add(header);
        this.add(VarTableContainerFigure);
        // this.add(columnTitle);
        // this.add(columnsContainer);
    }

    public Figure getHeader() {
        return this.header;
    }

    public VarTableContainerFigure getVarTableContainerFigure() {
        return this.VarTableContainerFigure;
    }

    public List<VarNode> getSelectionNodes() {
        return this.selectionNodes;
    }

    public VarToolBarFigure getToolBarFigure() {
        return this.imageButtonsFigure;
    }

}
