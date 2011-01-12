// ============================================================================
//
// Copyright (C) 2006-2010 Talend Inc. - www.talend.com
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
import org.eclipse.draw2d.ToolbarLayout;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.talend.designer.xmlmap.figures.layout.EqualWidthLayout;
import org.talend.designer.xmlmap.model.emf.xmlmap.OutputXmlTree;

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
        this.setBorder(new LineBorder());
        Label tableName = new Label();
        tableName.setBorder(new LineBorder(ColorConstants.black));
        tableName.setText(xmlTree.getName());
        Font erFont = new Font(null, "Arial", 9, SWT.BOLD); //$NON-NLS-1$
        tableName.setFont(erFont);
        tableName.setOpaque(true);
        tableName.setBackgroundColor(ColorConstants.yellow);
        this.add(tableName);

        tableColumnstitle = new ColumnTitleFigure();

        this.add(tableColumnstitle);
        columnContainer = new Figure();
        ToolbarLayout layout = new ToolbarLayout();
        layout.setStretchMinorAxis(true);
        layout.setVertical(true);
        columnContainer.setLayoutManager(layout);
        columnContainer.setOpaque(true);
        columnContainer.setBackgroundColor(ColorConstants.white);
        this.add(columnContainer);
    }

    public IFigure getColumnContainer() {
        return this.columnContainer;
    }

    class ColumnTitleFigure extends ToolBarContainer {

        public ColumnTitleFigure() {

            Label expression = new Label();
            expression.setText("Expression");
            expression.setBorder(new LineBorder());
            Font cFont = new Font(null, "Arial", 10, SWT.BOLD);
            expression.setFont(cFont);
            this.add(expression);

            Label column1 = new Label();
            column1.setText("Column");
            column1.setBorder(new LineBorder());
            column1.setFont(cFont);
            this.add(column1);
            this.setLayoutManager(new EqualWidthLayout());
        }

    }
}
