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
import org.talend.designer.xmlmap.model.emf.xmlmap.InputXmlTree;
import org.talend.designer.xmlmap.parts.directedit.DirectEditType;

/**
 * wchen class global comment. Detailled comment
 */
public class InputTreeSettingContainer extends Figure {

    private InputXmlTree inputxmlTree;

    private ComboCellLabel lookupModel;

    private ComboCellLabel matchModel;

    private ComboCellLabel joinModel;

    private ComboCellLabel persistentModel;

    public InputTreeSettingContainer(InputXmlTree inputxmlTree) {
        this.inputxmlTree = inputxmlTree;
        createContent();
    }

    public void createContent() {
        setLayoutManager(new ToolbarLayout());
        ColumnTitleFigure columnTitle = new ColumnTitleFigure();
        this.add(columnTitle);

        Figure container = new Figure();
        container.setLayoutManager(new ToolbarLayout());
        Figure lookupModelRow = new Figure();

        lookupModelRow.setLayoutManager(new EqualWidthLayout());
        Label label = new Label();
        label.setText("Lookup Model");
        label.setLabelAlignment(PositionConstants.LEFT);
        CompoundBorder compoundBorder = new CompoundBorder(new RowBorder(), new ColumnBorder());
        label.setBorder(compoundBorder);
        lookupModelRow.add(label);
        lookupModel = new ComboCellLabel();
        lookupModel.setDirectEditType(DirectEditType.LOOKUP_MODEL);
        lookupModel.setText(inputxmlTree.getLookupMode());
        lookupModel.setLabelAlignment(PositionConstants.LEFT);
        lookupModel.setBorder(new RowBorder(2, 10, 2, -1));
        lookupModelRow.add(lookupModel);
        container.add(lookupModelRow);

        Figure matchModelRow = new Figure();
        matchModelRow.setLayoutManager(new EqualWidthLayout());
        label = new Label();
        label.setText("Match Model");
        label.setLabelAlignment(PositionConstants.LEFT);
        compoundBorder = new CompoundBorder(new RowBorder(), new ColumnBorder());
        label.setBorder(compoundBorder);
        matchModelRow.add(label);
        matchModel = new ComboCellLabel();
        matchModel.setDirectEditType(DirectEditType.MATCH_MODEL);
        matchModel.setText(inputxmlTree.getMatchingMode());
        matchModel.setLabelAlignment(PositionConstants.LEFT);
        matchModel.setBorder(new RowBorder(2, 10, 2, -1));
        matchModelRow.add(matchModel);
        container.add(matchModelRow);

        Figure joinModelRow = new Figure();
        joinModelRow.setLayoutManager(new EqualWidthLayout());
        label = new Label();
        label.setText("Join Model");
        label.setLabelAlignment(PositionConstants.LEFT);
        compoundBorder = new CompoundBorder(new RowBorder(), new ColumnBorder());
        label.setBorder(compoundBorder);
        joinModelRow.add(label);
        joinModel = new ComboCellLabel();
        joinModel.setDirectEditType(DirectEditType.JOIN_MODEL);
        joinModel.setText(inputxmlTree.isInnerJoin() ? "true" : "false");
        joinModel.setLabelAlignment(PositionConstants.LEFT);
        joinModel.setBorder(new RowBorder(2, 10, 2, -1));
        joinModelRow.add(joinModel);
        container.add(joinModelRow);

        Figure persistentModelRow = new Figure();
        persistentModelRow.setLayoutManager(new EqualWidthLayout());
        label = new Label();
        label.setText("Store Temp Data");
        label.setLabelAlignment(PositionConstants.LEFT);
        compoundBorder = new CompoundBorder(new RowBorder(), new ColumnBorder());
        label.setBorder(compoundBorder);
        persistentModelRow.add(label);
        persistentModel = new ComboCellLabel();
        persistentModel.setDirectEditType(DirectEditType.PERSISTENT_MODEL);
        persistentModel.setText(inputxmlTree.isPersistent() ? "true" : "false");
        persistentModel.setLabelAlignment(PositionConstants.LEFT);
        persistentModel.setBorder(new RowBorder(2, 10, 2, -1));
        persistentModelRow.add(persistentModel);
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

    public void update() {

    }

}
