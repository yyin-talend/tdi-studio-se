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

import java.util.List;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.CompoundBorder;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.ToolbarLayout;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.talend.designer.xmlmap.figures.borders.ColumnBorder;
import org.talend.designer.xmlmap.figures.borders.RowBorder;
import org.talend.designer.xmlmap.figures.layout.EqualWidthLayout;
import org.talend.designer.xmlmap.model.emf.xmlmap.VarTable;

/**
 * DOC Administrator class global comment. Detailled comment
 */
public class VarTableContainerFigure extends Figure {

    protected Figure columnTitle;

    protected Figure columnsContainer;

    private VarTable vartable;

    public VarTableContainerFigure(VarTable vartable) {
        this.vartable = vartable;
        createContents();
    }

    /**
     * DOC Administrator Comment method "createContents".
     */
    protected void createContents() {
        this.setLayoutManager(new ToolbarLayout() {

            @Override
            protected Dimension calculatePreferredSize(IFigure container, int wHint, int hHint) {
                container.validate();
                List children = container.getChildren();
                Rectangle result = new Rectangle().setLocation(container.getClientArea().getLocation());
                boolean isMinimized = vartable.isMinimized();
                if (isMinimized) {
                    result.setSize(wHint, 0);
                } else if (!isMinimized) {
                    for (int i = 0; i < children.size(); i++) {
                        Dimension preferredSize2 = ((IFigure) children.get(i)).getPreferredSize();
                        result.union(((IFigure) children.get(i)).getBounds());
                        if (result.width > preferredSize2.width) {
                            result.width = preferredSize2.width;
                        }
                    }
                    result.resize(container.getInsets().getWidth(), container.getInsets().getHeight());
                }
                return result.getSize();
            }

        });
        Font cFont = new Font(null, "Arial", 10, SWT.BOLD);
        columnTitle = new ToolBarContainer();
        columnTitle.setOpaque(true);
        // columnTitle.setBackgroundColor(ColorConstants.tooltipBackground);
        // columnTitle.setBorder(new LineBorder());
        columnTitle.setLayoutManager(new EqualWidthLayout());
        Label expression = new Label();
        expression.setOpaque(true);
        expression.setBackgroundColor(ColorConstants.menuBackground);
        expression.setText("Expression");
        CompoundBorder compoundBorder = new CompoundBorder(new RowBorder(), new ColumnBorder());
        expression.setBorder(compoundBorder);
        expression.setFont(cFont);
        Label type = new Label();
        type.setOpaque(true);
        type.setBackgroundColor(ColorConstants.menuBackground);
        type.setText("Type");
        type.setFont(cFont);
        compoundBorder = new CompoundBorder(new RowBorder(), new ColumnBorder());
        type.setBorder(compoundBorder);
        Label varriable = new Label();
        varriable.setText("Varriable");
        varriable.setOpaque(true);
        varriable.setBackgroundColor(ColorConstants.menuBackground);
        varriable.setFont(cFont);
        varriable.setBorder(new RowBorder());
        columnTitle.add(expression);
        columnTitle.add(type);
        columnTitle.add(varriable);
        ToolbarLayout containerLayout = new ToolbarLayout();
        containerLayout.setVertical(true);
        columnsContainer = new Figure();
        // columnsContainer.setBorder(new LineBorder());
        columnsContainer.setLayoutManager(containerLayout);
        this.add(columnTitle);
        this.add(columnsContainer);
    }

    public Figure getColumnTitle() {
        return this.columnTitle;
    }

    public Figure getColumnsContainer() {
        return this.columnsContainer;
    }
}
