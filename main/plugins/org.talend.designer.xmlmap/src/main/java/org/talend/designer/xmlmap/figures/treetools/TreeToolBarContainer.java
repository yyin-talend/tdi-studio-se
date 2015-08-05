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
package org.talend.designer.xmlmap.figures.treetools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.draw2d.Clickable;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.MouseEvent;
import org.eclipse.emf.common.util.EList;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CommandStack;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.talend.designer.xmlmap.editor.XmlMapGraphicViewer;
import org.talend.designer.xmlmap.figures.layout.TreeToolBarLayout;
import org.talend.designer.xmlmap.figures.treesettings.TreeSettingsManager;
import org.talend.designer.xmlmap.model.emf.xmlmap.AbstractInOutTree;
import org.talend.designer.xmlmap.model.emf.xmlmap.InputLoopNodesTable;
import org.talend.designer.xmlmap.model.emf.xmlmap.InputXmlTree;
import org.talend.designer.xmlmap.model.emf.xmlmap.OutputXmlTree;
import org.talend.designer.xmlmap.model.emf.xmlmap.TreeNode;
import org.talend.designer.xmlmap.model.emf.xmlmap.XmlmapFactory;
import org.talend.designer.xmlmap.parts.AbstractInOutTreeEditPart;
import org.talend.designer.xmlmap.ui.dialog.SetLoopFunctionDialog;
import org.talend.designer.xmlmap.ui.resource.ImageInfo;
import org.talend.designer.xmlmap.ui.resource.ImageProviderMapper;
import org.talend.designer.xmlmap.util.XmlMapUtil;

/**
 * wchen class global comment. Detailled comment
 */
public class TreeToolBarContainer extends Figure {

    private ToolBarButtonImageFigure setLoopFunctionButton;

    private ToolBarButtonImageFigure condensedButton;

    private ToolBarButtonImageFigure expressionFilterButton;

    private ToolBarButtonImageFigure min_size;

    private AbstractInOutTree abstractTree;

    private AbstractInOutTreeEditPart abstractTreePart;

    private Map<String, Object> defaultSettingMap = new HashMap<String, Object>();

    private Image restorImage = ImageProviderMapper.getImage(ImageInfo.RESTORE_ICON);

    private Image miniImage = ImageProviderMapper.getImage(ImageInfo.MINIMIZE_ICON);

    private InputXmlTree inputMainTable;

    public TreeToolBarContainer(AbstractInOutTreeEditPart treePart) {
        this.abstractTreePart = treePart;
        this.abstractTree = (AbstractInOutTree) treePart.getModel();
        inputMainTable = ((XmlMapGraphicViewer) treePart.getViewer()).getMapperManager().getMainInputTree();
        createToolbar();
    }

    private void createToolbar() {
        TreeToolBarLayout manager = new TreeToolBarLayout();
        manager.setVertical(false);
        manager.setSpacing(5);
        this.setLayoutManager(manager);

        if (abstractTree instanceof OutputXmlTree) {
            ImageInfo info = ImageInfo.SETLOOPFUNCTION_BUTTON;
            EList<InputLoopNodesTable> inputLoopNodesTables = ((OutputXmlTree) abstractTree).getInputLoopNodesTables();
            if (!inputLoopNodesTables.isEmpty() && inputLoopNodesTables.get(0) != null) {
                if (inputLoopNodesTables.get(0).getInputloopnodes().isEmpty()) {
                    info = ImageInfo.SETLOOPFUNCTION_BUTTON_ERROR;
                }
            }
            setLoopFunctionButton = new SetLoopFunctionButton(ImageProviderMapper.getImage(info));
            this.add(setLoopFunctionButton);
            if (inputMainTable == null || !inputMainTable.isMultiLoops()) {
                setLoopFunctionButton.setVisible(false);
            }
        }

        // TDI-22087
        if (abstractTree instanceof OutputXmlTree
                || (abstractTree instanceof InputXmlTree && ((InputXmlTree) abstractTree).isLookup())) {
            condensedButton = new CondensedButton(ImageProviderMapper.getImage(ImageInfo.CONDENSED_TOOL_ICON));
            condensedButton.setSelected(abstractTree.isActivateCondensedTool());
            this.add(condensedButton);

            expressionFilterButton = new ExpressionFilterButton(ImageProviderMapper.getImage(ImageInfo.ACTIVATE_FILTER_ICON));
            expressionFilterButton.setSelected(abstractTree.isActivateExpressionFilter());
            this.add(expressionFilterButton);
        }

        boolean isErrorReject = false;
        if (abstractTree instanceof OutputXmlTree) {
            isErrorReject = ((OutputXmlTree) abstractTree).isErrorReject();
        }
        if (isErrorReject) {
            condensedButton.setEnabled(false);
            expressionFilterButton.setEnabled(false);
        }

        Image image = null;
        if (abstractTree.isMinimized()) {
            image = restorImage;
        } else {
            image = miniImage;
        }
        min_size = new MinSizeButton(image);
        this.add(min_size);
        setTooltips();
    }

    private void setTooltips() {
        Label tooltip = new Label();
        if (setLoopFunctionButton != null) {
            tooltip.setText("set Loop Function");
            setLoopFunctionButton.setToolTip(tooltip);
        }

        tooltip = new Label();
        if (condensedButton != null) {
            tooltip.setText("tXmlMap settings");
            condensedButton.setToolTip(tooltip);
        }

        tooltip = new Label();
        if (expressionFilterButton != null) {
            tooltip.setText("Enable/disable expression filter");
            expressionFilterButton.setToolTip(tooltip);
        }

        tooltip = new Label();
        tooltip.setText("Minimize");
        min_size.setToolTip(tooltip);

    }

    public void updateMinSizeImage() {
        if (abstractTree.isMinimized()) {
            min_size.setImage(restorImage);
        } else {
            min_size.setImage(miniImage);
        }
    }

    public void updateLoopFunctionButton() {
        if (setLoopFunctionButton != null && abstractTree instanceof OutputXmlTree) {
            if (inputMainTable != null && inputMainTable.isMultiLoops()) {
                if (!XmlMapUtil.hasDocument(abstractTree)) {
                    if (abstractTree instanceof OutputXmlTree) {
                        EList<InputLoopNodesTable> inputLoopNodesTables = ((OutputXmlTree) abstractTree)
                                .getInputLoopNodesTables();
                        if (inputLoopNodesTables.isEmpty() || inputLoopNodesTables.get(0).getInputloopnodes().isEmpty()) {
                            setLoopFunctionButton.setImage(ImageProviderMapper.getImage(ImageInfo.SETLOOPFUNCTION_BUTTON_ERROR));
                        } else {
                            setLoopFunctionButton.setImage(ImageProviderMapper.getImage(ImageInfo.SETLOOPFUNCTION_BUTTON));
                        }
                    }
                    setLoopFunctionButton.setVisible(true);
                } else {
                    setLoopFunctionButton.setVisible(false);
                }
            } else {
                setLoopFunctionButton.setVisible(false);
            }
        }
    }

    public void updateButtonsColor(Color color) {
        if (setLoopFunctionButton != null) {
            updateLoopFunctionButton();
            if (!setLoopFunctionButton.isSelected()) {
                setLoopFunctionButton.setBackgroundColor(color);
            }
        }
        if (condensedButton != null) {
            if (!condensedButton.isSelected()) {
                condensedButton.setBackgroundColor(color);
            }
        }
        if (expressionFilterButton != null) {
            if (!expressionFilterButton.isSelected()) {
                expressionFilterButton.setBackgroundColor(color);
            }
        }
        min_size.setBackgroundColor(color);

    }

    public Map<String, Object> getDefaultSetting() {
        if (defaultSettingMap.isEmpty()) {
            defaultSettingMap.put(TreeSettingsManager.OUTPUT_REJECT, false);
            defaultSettingMap.put(TreeSettingsManager.LOOK_UP_INNER_JOIN_REJECT, false);
        }
        return defaultSettingMap;
    }

    class SetLoopFunctionButton extends ToolBarButtonImageFigure {

        public SetLoopFunctionButton(Image image) {
            super(image);
            setStyle(Clickable.STYLE_TOGGLE);
        }

        @Override
        public void toolBarButtonPressed(MouseEvent me) {
            super.toolBarButtonPressed(me);
            CommandStack commandStack = abstractTreePart.getViewer().getEditDomain().getCommandStack();
            commandStack.execute(new Command() {

                @Override
                public void execute() {
                    if (abstractTree instanceof OutputXmlTree) {

                        OutputXmlTree outputXmlTree = (OutputXmlTree) abstractTree;
                        InputLoopNodesTable inputLoopNodesTable = null;
                        if (!outputXmlTree.getInputLoopNodesTables().isEmpty()) {
                            inputLoopNodesTable = outputXmlTree.getInputLoopNodesTables().get(0);
                        } else {
                            inputLoopNodesTable = XmlmapFactory.eINSTANCE.createInputLoopNodesTable();
                            outputXmlTree.getInputLoopNodesTables().add(inputLoopNodesTable);
                        }
                        List<TreeNode> loopNodes = new ArrayList<TreeNode>();
                        if (inputMainTable != null && inputMainTable.isMultiLoops()) {
                            loopNodes.addAll(XmlMapUtil.getMultiLoopsForXmlTree(inputMainTable));
                        }
                        SetLoopFunctionDialog nsDialog = new SetLoopFunctionDialog(null, inputLoopNodesTable, loopNodes);
                        setLoopFunctionButton.setSelected(false);
                        if (nsDialog.open() == Window.OK) {
                            ((XmlMapGraphicViewer) abstractTreePart.getViewer()).getMapperManager().getProblemsAnalyser()
                                    .checkProblems(abstractTree);
                            ((XmlMapGraphicViewer) abstractTreePart.getViewer()).getMapperManager().getMapperUI()
                                    .updateStatusBar();
                        }
                    }
                }
            });
            revalidate();
        }
    }

    class CondensedButton extends ToolBarButtonImageFigure {

        public CondensedButton(Image image) {
            super(image);
            setStyle(Clickable.STYLE_TOGGLE);
        }

        @Override
        public void toolBarButtonPressed(MouseEvent me) {
            super.toolBarButtonPressed(me);
            abstractTree.setActivateCondensedTool(this.isSelected());
            revalidate();
        }
    }

    class ExpressionFilterButton extends ToolBarButtonImageFigure {

        public ExpressionFilterButton(Image image) {
            super(image);
            setStyle(Clickable.STYLE_TOGGLE);
        }

        @Override
        public void toolBarButtonPressed(MouseEvent me) {
            super.toolBarButtonPressed(me);
            abstractTree.setActivateExpressionFilter(this.isSelected());
            revalidate();
        }
    }

    class MinSizeButton extends ToolBarButtonImageFigure {

        public MinSizeButton(Image image) {
            super(image);
        }

        @Override
        public void toolBarButtonPressed(MouseEvent me) {
            super.toolBarButtonPressed(me);
            CommandStack commandStack = abstractTreePart.getViewer().getEditDomain().getCommandStack();
            commandStack.execute(new Command() {

                @Override
                public void execute() {
                    abstractTree.setMinimized(!abstractTree.isMinimized());
                    abstractTreePart.getViewer().deselectAll();
                }
            });

        }
    }

}
