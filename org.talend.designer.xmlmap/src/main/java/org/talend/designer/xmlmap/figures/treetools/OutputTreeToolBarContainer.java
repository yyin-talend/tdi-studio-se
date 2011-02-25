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
package org.talend.designer.xmlmap.figures.treetools;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.draw2d.Clickable;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.MouseEvent;
import org.eclipse.swt.graphics.Image;
import org.talend.designer.xmlmap.figures.layout.TreeToolBarLayout;
import org.talend.designer.xmlmap.figures.treesettings.TreeSettingsManager;
import org.talend.designer.xmlmap.model.emf.xmlmap.OutputXmlTree;
import org.talend.designer.xmlmap.ui.resource.ImageInfo;
import org.talend.designer.xmlmap.ui.resource.ImageProviderMapper;

/**
 * wchen class global comment. Detailled comment
 */
public class OutputTreeToolBarContainer extends Figure {

    private ToolBarButtonImageFigure condensedButton;

    private ToolBarButtonImageFigure expressionFilterButton;

    private OutputXmlTree outputTree;

    private Map<String, Object> defaultSettingMap = new HashMap<String, Object>();

    public OutputTreeToolBarContainer(OutputXmlTree outputTree) {
        this.outputTree = outputTree;
        createToolbar();
    }

    private void createToolbar() {
        TreeToolBarLayout manager = new TreeToolBarLayout();
        manager.setSpacing(5);
        this.setLayoutManager(manager);
        condensedButton = new CondensedButton(ImageProviderMapper.getImage(ImageInfo.CONDENSED_TOOL_ICON));
        condensedButton.setStyle(Clickable.STYLE_TOGGLE);
        condensedButton.setSelected(outputTree.isActivateCondensedTool());
        this.add(condensedButton);

        expressionFilterButton = new ExpressionFilterButton(ImageProviderMapper.getImage(ImageInfo.ACTIVATE_FILTER_ICON));
        expressionFilterButton.setSelected(outputTree.isActivateExpressionFilter());
        this.add(expressionFilterButton);
    }

    public Map<String, Object> getDefaultSetting() {
        if (defaultSettingMap.isEmpty()) {
            defaultSettingMap.put(TreeSettingsManager.OUTPUT_REJECT, false);
            defaultSettingMap.put(TreeSettingsManager.LOOK_UP_INNER_JOIN_REJECT, false);
        }
        return defaultSettingMap;
    }

    class CondensedButton extends ToolBarButtonImageFigure {

        public CondensedButton(Image image) {
            super(image);
        }

        @Override
        public void toolBarButtonPressed(MouseEvent me) {
            super.toolBarButtonPressed(me);
            outputTree.setActivateCondensedTool(this.isSelected());
        }
    }

    class ExpressionFilterButton extends ToolBarButtonImageFigure {

        public ExpressionFilterButton(Image image) {
            super(image);
        }

        @Override
        public void toolBarButtonPressed(MouseEvent me) {
            super.toolBarButtonPressed(me);
            outputTree.setActivateExpressionFilter(this.isSelected());
        }
    }
}
