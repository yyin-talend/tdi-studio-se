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
import org.eclipse.draw2d.MouseEvent;
import org.eclipse.draw2d.MouseListener;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.ToolbarLayout;
import org.talend.designer.xmlmap.figures.ComboCellLabel;
import org.talend.designer.xmlmap.figures.borders.ColumnBorder;
import org.talend.designer.xmlmap.figures.borders.RowBorder;
import org.talend.designer.xmlmap.figures.layout.EqualWidthLayout;
import org.talend.designer.xmlmap.model.emf.xmlmap.OutputXmlTree;
import org.talend.designer.xmlmap.model.emf.xmlmap.XmlmapPackage;
import org.talend.designer.xmlmap.parts.directedit.DirectEditType;
import org.talend.designer.xmlmap.ui.resource.ColorInfo;
import org.talend.designer.xmlmap.ui.resource.ColorProviderMapper;
import org.talend.designer.xmlmap.util.XmlMapUtil;

/**
 * wchen class global comment. Detailled comment
 */
public class OutputTreeSettingContainer extends AbstractTreeSettingContainer {

    private OutputXmlTree outputxmlTree;

    private Figure settingsContainer;

    private Figure rejectRow;

    private ComboCellLabel reject;

    private Figure innerJoinRejectRow;

    private ComboCellLabel innerJoinReject;

    private Figure allInOneRow;

    private ComboCellLabel allInOne;

    private Figure enableEmptyElementRow;

    private ComboCellLabel enableEmptyElement;

    public OutputTreeSettingContainer(OutputXmlTree outputxmlTree) {
        this.outputxmlTree = outputxmlTree;
        createContent();
    }

    public void createContent() {
        setLayoutManager(new ToolbarLayout());
        ColumnTitleFigure columnTitle = new ColumnTitleFigure();
        this.add(columnTitle);

        settingsContainer = new Figure();
        settingsContainer.setLayoutManager(new ToolbarLayout());

        rejectRow = new Figure();
        rejectRow.setLayoutManager(new EqualWidthLayout());
        Label label = new Label();
        label.setText("Catch Output Reject");
        label.setLabelAlignment(PositionConstants.LEFT);
        CompoundBorder compoundBorder = new CompoundBorder(new RowBorder(), new ColumnBorder());
        label.setBorder(compoundBorder);
        rejectRow.add(label);
        reject = new ComboCellLabel();
        reject.setDirectEditType(DirectEditType.OUTPUT_REJECT);
        reject.setText(String.valueOf(outputxmlTree.isReject()));
        reject.setLabelAlignment(PositionConstants.LEFT);
        reject.setBorder(new RowBorder(2, 10, 2, -1));
        rejectRow.add(reject);
        settingsContainer.add(rejectRow);

        innerJoinRejectRow = new Figure();
        innerJoinRejectRow.setLayoutManager(new EqualWidthLayout());
        label = new Label();
        label.setText("Catch Lookup Inner Join Reject");
        label.setLabelAlignment(PositionConstants.LEFT);
        compoundBorder = new CompoundBorder(new RowBorder(), new ColumnBorder());
        label.setBorder(compoundBorder);
        innerJoinRejectRow.add(label);
        innerJoinReject = new ComboCellLabel();
        innerJoinReject.setDirectEditType(DirectEditType.LOOK_UP_INNER_JOIN_REJECT);
        innerJoinReject.setText(String.valueOf(outputxmlTree.isRejectInnerJoin()));
        innerJoinReject.setLabelAlignment(PositionConstants.LEFT);
        innerJoinReject.setBorder(new RowBorder(2, 10, 2, -1));
        innerJoinRejectRow.add(innerJoinReject);
        settingsContainer.add(innerJoinRejectRow);

        allInOneRow = new Figure();
        allInOneRow.setLayoutManager(new EqualWidthLayout());
        label = new Label();
        label.setText("All in one");
        label.setLabelAlignment(PositionConstants.LEFT);
        compoundBorder = new CompoundBorder(new RowBorder(), new ColumnBorder());
        label.setBorder(compoundBorder);
        allInOneRow.add(label);
        allInOne = new ComboCellLabel();
        allInOne.setDirectEditType(DirectEditType.ALL_IN_ONE);
        allInOne.setText(String.valueOf(outputxmlTree.isAllInOne()));
        allInOne.setLabelAlignment(PositionConstants.LEFT);
        allInOne.setBorder(new RowBorder(2, 10, 2, -1));
        allInOneRow.add(allInOne);
        // container.add(allInOneRow);

        enableEmptyElementRow = new Figure();
        enableEmptyElementRow.setLayoutManager(new EqualWidthLayout());
        label = new Label();
        label.setText("Create empty element");
        label.setLabelAlignment(PositionConstants.LEFT);
        compoundBorder = new CompoundBorder(new RowBorder(), new ColumnBorder());
        label.setBorder(compoundBorder);
        enableEmptyElementRow.add(label);
        enableEmptyElement = new ComboCellLabel();
        enableEmptyElement.setDirectEditType(DirectEditType.ENABLE_EMPTY_ELEMENT);
        enableEmptyElement.setText(String.valueOf(outputxmlTree.isEnableEmptyElement()));
        enableEmptyElement.setLabelAlignment(PositionConstants.LEFT);
        enableEmptyElement.setBorder(new RowBorder(2, 10, 2, -1));
        enableEmptyElementRow.add(enableEmptyElement);
        // container.add(enableEmptyElementRow);

        showOrHideDocumentSetting();

        settingsContainer.setOpaque(true);
        settingsContainer.setBackgroundColor(ColorConstants.white);
        this.add(settingsContainer);

        // show selection
        settingsContainer.addMouseListener(new MouseListener() {

            Figure selectedFigure = null;

            public void mousePressed(MouseEvent me) {
                boolean joinModel = rejectRow.containsPoint(me.x, me.y);
                if (joinModel) {
                    if (selectedFigure != rejectRow) {
                        rejectRow.setBackgroundColor(ColorProviderMapper.getColor(ColorInfo.COLOR_SELECTION));
                        rejectRow.setOpaque(true);
                        innerJoinRejectRow.setOpaque(false);
                        allInOneRow.setOpaque(false);
                        enableEmptyElementRow.setOpaque(false);
                    }
                    return;
                }
                boolean persistentModel = innerJoinRejectRow.containsPoint(me.x, me.y);
                if (persistentModel) {
                    if (selectedFigure != innerJoinRejectRow) {
                        innerJoinRejectRow.setBackgroundColor(ColorProviderMapper.getColor(ColorInfo.COLOR_SELECTION));
                        innerJoinRejectRow.setOpaque(true);
                        rejectRow.setOpaque(false);
                        allInOneRow.setOpaque(false);
                        enableEmptyElementRow.setOpaque(false);
                    }
                    return;
                }

                boolean allInOne = allInOneRow.containsPoint(me.x, me.y);
                if (allInOne) {
                    if (selectedFigure != allInOneRow) {
                        allInOneRow.setBackgroundColor(ColorProviderMapper.getColor(ColorInfo.COLOR_SELECTION));
                        allInOneRow.setOpaque(true);
                        rejectRow.setOpaque(false);
                        innerJoinRejectRow.setOpaque(false);
                        enableEmptyElementRow.setOpaque(false);
                    }
                    return;
                }

                boolean emptyElement = enableEmptyElementRow.containsPoint(me.x, me.y);
                if (emptyElement) {
                    if (selectedFigure != enableEmptyElementRow) {
                        enableEmptyElementRow.setBackgroundColor(ColorProviderMapper.getColor(ColorInfo.COLOR_SELECTION));
                        enableEmptyElementRow.setOpaque(true);
                        rejectRow.setOpaque(false);
                        innerJoinRejectRow.setOpaque(false);
                        allInOneRow.setOpaque(false);
                    }
                }
            }

            public void mouseReleased(MouseEvent me) {
            }

            public void mouseDoubleClicked(MouseEvent me) {
            }

        });

    }

    private void showOrHideDocumentSetting() {
        boolean show = XmlMapUtil.hasDocument(outputxmlTree);
        if (show) {
            if (!settingsContainer.getChildren().contains(allInOneRow)) {
                settingsContainer.add(allInOneRow);
            }
            if (!settingsContainer.getChildren().contains(enableEmptyElementRow)) {
                settingsContainer.add(enableEmptyElementRow);
            }
        } else {
            if (settingsContainer.getChildren().contains(enableEmptyElementRow)) {
                settingsContainer.remove(enableEmptyElementRow);
                outputxmlTree.setEnableEmptyElement(true);
            }
            if (settingsContainer.getChildren().contains(allInOneRow)) {
                settingsContainer.remove(allInOneRow);
                outputxmlTree.setAllInOne(false);
            }
        }
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
        case XmlmapPackage.OUTPUT_XML_TREE__ALL_IN_ONE:
            allInOne.setText(String.valueOf(outputxmlTree.isAllInOne()));
            break;
        case XmlmapPackage.OUTPUT_XML_TREE__ENABLE_EMPTY_ELEMENT:
            enableEmptyElement.setText(String.valueOf(outputxmlTree.isEnableEmptyElement()));
            break;
        case XmlmapPackage.TREE_NODE__TYPE:
            showOrHideDocumentSetting();
        }
    }

    public void deselectTreeSettingRows() {
        if (rejectRow.isOpaque()) {
            rejectRow.setOpaque(false);
        }
        if (innerJoinRejectRow.isOpaque()) {
            innerJoinRejectRow.setOpaque(false);
        }
        if (allInOneRow.isOpaque()) {
            allInOneRow.setOpaque(false);
        }
        if (enableEmptyElementRow.isOpaque()) {
            enableEmptyElementRow.setOpaque(false);
        }
    }
}
