// ============================================================================
//
// Copyright (C) 2006-2009 Talend Inc. - www.talend.com
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
import org.eclipse.draw2d.ToolbarLayout;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.talend.designer.xmlmap.figures.layout.EqualWidthLayout;
import org.talend.designer.xmlmap.model.emf.xmlmap.VarNode;
import org.talend.designer.xmlmap.model.emf.xmlmap.VarTable;

/**
 * DOC hywang class global comment. Detailled comment
 */
public class CenterVarFigure extends Figure {

    protected ToolBarContainer header;

    private VarTable table;

    protected VarTableContainerFigure VarTableContainerFigure;

    protected ButtonsImageToolBarFigure imageButtonsFigure;

    private List<VarNode> selectionNodes = new ArrayList<VarNode>();

    public CenterVarFigure(VarTable table) {
        this.table = table;
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
        Font cFont = new Font(null, "Arial", 10, SWT.BOLD);
        header = new ToolBarContainer();
        header.setOpaque(true);
        header.setBackgroundColor(ColorConstants.tooltipBackground);
        header.setBorder(new LineBorder());
        header.setLayoutManager(new EqualWidthLayout());
        Label varText = new Label();
        varText.setFont(cFont);
        imageButtonsFigure = new ButtonsImageToolBarFigure(table);
        header.add(varText);
        header.setVarText(varText);
        header.add(imageButtonsFigure);
        header.setToolbar(imageButtonsFigure);
        VarTableContainerFigure = new VarTableContainerFigure();
        this.add(header);
        this.add(VarTableContainerFigure);
        // this.add(columnTitle);
        // this.add(columnsContainer);
    }

    public ToolBarContainer getHeader() {
        return this.header;
    }

    public VarTableContainerFigure getVarTableContainerFigure() {
        return this.VarTableContainerFigure;
    }

    public List<VarNode> getSelectionNodes() {
        return this.selectionNodes;
    }

}
