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
import org.eclipse.draw2d.GridData;
import org.eclipse.draw2d.GridLayout;
import org.eclipse.draw2d.MouseEvent;
import org.eclipse.swt.graphics.Image;
import org.talend.designer.xmlmap.figures.treesettings.TreeSettingsManager;
import org.talend.designer.xmlmap.model.emf.xmlmap.InputXmlTree;
import org.talend.designer.xmlmap.model.tree.IUIMatchingMode;
import org.talend.designer.xmlmap.model.tree.XML_MAP_LOOKUP_MODE;
import org.talend.designer.xmlmap.model.tree.XML_MAP_MATCHING_MODE;
import org.talend.designer.xmlmap.ui.resource.ImageInfo;
import org.talend.designer.xmlmap.ui.resource.ImageProviderMapper;

/**
 * wchen class global comment. Detailled comment
 */
public class InputTreeToolBarContainer extends Figure {

    private ToolBarButtonImageFigure condensedButton;

    private InputXmlTree inputTree;

    private Map<String, Object> defaultSettingMap = new HashMap<String, Object>();

    public InputTreeToolBarContainer(InputXmlTree inputTree) {
        this.inputTree = inputTree;
        createToolbar();
    }

    private void createToolbar() {
        GridLayout manager = new GridLayout();
        this.setLayoutManager(manager);
        // this.setBorder(new MarginBorder(-1, -1, -1, 10));
        condensedButton = new CondensedButton(ImageProviderMapper.getImage(ImageInfo.CONDENSED_TOOL_ICON));
        condensedButton.setSelected(inputTree.isActivateCondensedTool());
        GridData data = new GridData(GridData.HORIZONTAL_ALIGN_END | GridData.FILL_HORIZONTAL);
        data.horizontalSpan = 10;
        manager.setConstraint(condensedButton, data);
        this.add(condensedButton);

    }

    public Map<String, Object> getDefaultSetting() {
        if (defaultSettingMap.isEmpty()) {
            defaultSettingMap.put(TreeSettingsManager.LOOKUP_MODEL_SETTING, XML_MAP_LOOKUP_MODE.LOAD_ONCE);
            defaultSettingMap.put(TreeSettingsManager.MATCH_MODEL_SETTING, new IUIMatchingMode[] {
                    XML_MAP_MATCHING_MODE.ALL_ROWS, XML_MAP_MATCHING_MODE.UNIQUE_MATCH });
            defaultSettingMap.put(TreeSettingsManager.JOIN_MODEL_SETTING, false);
            defaultSettingMap.put(TreeSettingsManager.PERSISTENCE_MODEL_SETTING, false);
        }
        return defaultSettingMap;
    }

    class CondensedButton extends ToolBarButtonImageFigure {

        public CondensedButton(Image image) {
            super(image);
            setStyle(Clickable.STYLE_TOGGLE);
        }

        @Override
        public void toolBarButtonPressed(MouseEvent me) {
            super.toolBarButtonPressed(me);
            inputTree.setActivateCondensedTool(this.isSelected());
            revalidate();
        }
    }
}
