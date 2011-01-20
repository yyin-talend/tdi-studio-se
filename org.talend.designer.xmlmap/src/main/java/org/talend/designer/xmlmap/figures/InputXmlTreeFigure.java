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

    class ColumnTitleFigure extends Figure {

        private Label column1;

        public ColumnTitleFigure() {
            column1 = new Label();
            column1.setText("Column");
            Font cFont = new Font(null, "Arial", 10, SWT.BOLD);
            column1.setFont(cFont);
            this.add(column1);
            this.setLayoutManager(new ToolbarLayout());
            setBackgroundColor(ColorConstants.menuBackground);
            setOpaque(true);
        }

        public Label getColumn1() {
            return this.column1;
        }

    }
}
