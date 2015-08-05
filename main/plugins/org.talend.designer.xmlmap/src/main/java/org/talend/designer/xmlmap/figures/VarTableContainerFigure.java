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

import java.util.List;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.MarginBorder;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.ToolbarLayout;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
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
                // container.validate();
                List children = container.getChildren();
                Rectangle result = new Rectangle().setLocation(container.getClientArea().getLocation());
                boolean isMinimized = vartable.isMinimized();
                if (isMinimized) {
                    result.setSize(wHint, 0);
                } else if (!isMinimized) {
                    // for (int i = 0; i < children.size(); i++) {
                    // Dimension preferredSize2 = ((IFigure) children.get(i)).getPreferredSize();
                    // result.union(((IFigure) children.get(i)).getBounds());
                    // if (result.width > preferredSize2.width) {
                    // result.width = preferredSize2.width;
                    // }
                    // }
                    // result.resize(container.getInsets().getWidth(), container.getInsets().getHeight());
                    result.setSize(super.calculatePreferredSize(container, wHint, hHint));
                }
                return result.getSize();
            }

        });
        columnTitle = new ColumnTitleFigure();
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

    class ColumnTitleFigure extends Figure {

        public ColumnTitleFigure() {

            Label expression = new Label();
            expression.setText("Expression");
            expression.setBorder(new MarginBorder(3, 10, 3, -1));
            expression.setLabelAlignment(PositionConstants.LEFT);
            this.add(expression);

            Label column1 = new Label();
            column1.setText("Type");
            column1.setBorder(new MarginBorder(3, 10, 3, -1));
            column1.setLabelAlignment(PositionConstants.LEFT);
            this.add(column1);

            Label varriable = new Label();
            varriable.setText("Varriable");
            varriable.setBorder(new MarginBorder(3, 10, 3, -1));
            varriable.setLabelAlignment(PositionConstants.LEFT);
            this.add(varriable);

            this.setLayoutManager(new EqualWidthLayout());

            setBackgroundColor(ColorConstants.menuBackground);
            setOpaque(true);
        }

    }
}
