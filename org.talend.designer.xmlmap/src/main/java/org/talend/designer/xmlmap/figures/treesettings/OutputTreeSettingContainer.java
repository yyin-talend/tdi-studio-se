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
package org.talend.designer.xmlmap.figures.treesettings;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.CompoundBorder;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.MarginBorder;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.ToolbarLayout;
import org.talend.designer.xmlmap.figures.ComboCellLabel;
import org.talend.designer.xmlmap.figures.borders.ColumnBorder;
import org.talend.designer.xmlmap.figures.borders.RowBorder;
import org.talend.designer.xmlmap.figures.layout.EqualWidthLayout;
import org.talend.designer.xmlmap.model.emf.xmlmap.OutputXmlTree;
import org.talend.designer.xmlmap.model.emf.xmlmap.XmlmapPackage;
import org.talend.designer.xmlmap.parts.directedit.DirectEditType;

/**
 * wchen class global comment. Detailled comment
 */
public class OutputTreeSettingContainer extends Figure {

    private OutputXmlTree outputxmlTree;

    private ComboCellLabel reject;

    private ComboCellLabel innerJoinReject;

    public OutputTreeSettingContainer(OutputXmlTree outputxmlTree) {
        this.outputxmlTree = outputxmlTree;
        createContent();
    }

    public void createContent() {
        setLayoutManager(new ToolbarLayout());
        ColumnTitleFigure columnTitle = new ColumnTitleFigure();
        this.add(columnTitle);

        Figure container = new Figure();
        container.setLayoutManager(new ToolbarLayout());

        Figure joinModelRow = new Figure();
        joinModelRow.setLayoutManager(new EqualWidthLayout());
        Label label = new Label();
        label.setText("Catch Output Reject");
        label.setLabelAlignment(PositionConstants.LEFT);
        CompoundBorder compoundBorder = new CompoundBorder(new RowBorder(), new ColumnBorder());
        label.setBorder(compoundBorder);
        joinModelRow.add(label);
        reject = new ComboCellLabel();
        reject.setDirectEditType(DirectEditType.OUTPUT_REJECT);
        reject.setText(String.valueOf(outputxmlTree.isReject()));
        reject.setLabelAlignment(PositionConstants.LEFT);
        reject.setBorder(new RowBorder(2, 10, 2, -1));
        joinModelRow.add(reject);
        container.add(joinModelRow);

        Figure persistentModelRow = new Figure();
        persistentModelRow.setLayoutManager(new EqualWidthLayout());
        label = new Label();
        label.setText("Catch Lookup Inner Join Reject");
        label.setLabelAlignment(PositionConstants.LEFT);
        compoundBorder = new CompoundBorder(new RowBorder(), new ColumnBorder());
        label.setBorder(compoundBorder);
        persistentModelRow.add(label);
        innerJoinReject = new ComboCellLabel();
        innerJoinReject.setDirectEditType(DirectEditType.LOOK_UP_INNER_JOIN_REJECT);
        innerJoinReject.setText(String.valueOf(outputxmlTree.isRejectInnerJoin()));
        innerJoinReject.setLabelAlignment(PositionConstants.LEFT);
        innerJoinReject.setBorder(new RowBorder(2, 10, 2, -1));
        persistentModelRow.add(innerJoinReject);
        container.add(persistentModelRow);
        container.setOpaque(true);
        container.setBackgroundColor(ColorConstants.white);
        this.add(container);
    }

    class ColumnTitleFigure extends Figure {

        public ColumnTitleFigure() {

            Label expression = new Label();
            expression.setText("Property");
            expression.setBorder(new MarginBorder(3, 10, 3, -1));
            expression.setLabelAlignment(PositionConstants.LEFT);
            this.add(expression);

            Label column1 = new Label();
            column1.setText("Value");
            column1.setBorder(new MarginBorder(3, 10, 3, -1));
            column1.setLabelAlignment(PositionConstants.LEFT);
            this.add(column1);
            this.setLayoutManager(new EqualWidthLayout());

            setBackgroundColor(ColorConstants.menuBackground);
            setOpaque(true);
        }
    }

    public void update(int type) {
        switch (type) {
        case XmlmapPackage.OUTPUT_XML_TREE__REJECT:
            reject.setText(String.valueOf(outputxmlTree.isReject()));
            break;
        case XmlmapPackage.OUTPUT_XML_TREE__REJECT_INNER_JOIN:
            innerJoinReject.setText(String.valueOf(outputxmlTree.isRejectInnerJoin()));
            break;
        }
    }

}
