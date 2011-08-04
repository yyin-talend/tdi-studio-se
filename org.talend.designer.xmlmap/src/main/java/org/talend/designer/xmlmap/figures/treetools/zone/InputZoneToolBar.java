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
package org.talend.designer.xmlmap.figures.treetools.zone;

import java.util.List;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Label;
import org.eclipse.emf.common.util.EList;
import org.eclipse.gef.EditPart;
import org.eclipse.swt.graphics.Image;
import org.talend.designer.xmlmap.figures.layout.ZoneToolBarLayout;
import org.talend.designer.xmlmap.model.emf.xmlmap.InputXmlTree;
import org.talend.designer.xmlmap.parts.InputXmlTreeEditPart;
import org.talend.designer.xmlmap.parts.XmlMapDataEditPart;
import org.talend.designer.xmlmap.ui.resource.ColorInfo;
import org.talend.designer.xmlmap.ui.resource.ColorProviderMapper;

/**
 * wchen class global comment. Detailled comment
 */
public class InputZoneToolBar extends ZoneToolBar {

    public InputZoneToolBar(XmlMapDataEditPart mapDataPart) {
        super(mapDataPart);
        ZoneToolBarLayout manager = new ZoneToolBarLayout();
        manager.setVertical(false);
        manager.setSpacing(8);
        setLayoutManager(manager);
        setOpaque(true);
        setBackgroundColor(ColorProviderMapper.getColor(ColorInfo.ZONE_BACKGROUND_COLOR));

        move_up = new MoveUpButton();
        move_up.setEnabled(false);
        this.add(move_up);
        move_down = new MoveDownButton();
        move_down.setEnabled(false);
        this.add(move_down);
        Label figure = new Label("");
        figure.setOpaque(true);
        figure.setBackgroundColor(ColorConstants.lightGray);
        add(figure);

        for (InputXmlTree tree : mapData.getInputTrees()) {
            if (!tree.isMinimized()) {
                minimized = false;
            }
        }
        Image image = null;
        if (minimized) {
            image = restorImage;
        } else {
            image = miniImage;
        }
        min_size = new MinSizeButton(image);
        if (mapData.getInputTrees().isEmpty()) {
            min_size.setEnabled(false);
        }
        this.add(min_size);

    }

    public void setMoveUpEnable(boolean enable) {
        move_up.setEnabled(enable);
    }

    public void setMoveDownEnable(boolean enable) {
        move_down.setEnabled(enable);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.xmlmap.figures.treetools.zone.ZoneToolBar#moveUp()
     */
    @Override
    public void moveUp() {
        List<InputXmlTree> inputTrees = mapData.getInputTrees();
        InputXmlTreeEditPart currentSelectedInputXmlTree = graphicViewer.getFiguresManager().getCurrentSelectedInputXmlTree();
        if (currentSelectedInputXmlTree != null) {
            InputXmlTree selectedTree = (InputXmlTree) currentSelectedInputXmlTree.getModel();

            int index = inputTrees.indexOf(selectedTree);
            if (index != -1 && index - 1 >= 0) {
                inputTrees.remove(selectedTree);
                inputTrees.add(index - 1, selectedTree);

                // index of modelchildren is different from index of tree
                int indexOf = mapDataPart.getModelChildren().indexOf(selectedTree);
                if (indexOf != -1) {
                    mapDataPart.getViewer().appendSelection((EditPart) mapDataPart.getChildren().get(indexOf));
                }
            }
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.xmlmap.figures.treetools.zone.ZoneToolBar#moveDown()
     */
    @Override
    public void moveDown() {
        List<InputXmlTree> inputTrees = mapData.getInputTrees();
        InputXmlTreeEditPart currentSelectedInputXmlTree = graphicViewer.getFiguresManager().getCurrentSelectedInputXmlTree();
        if (currentSelectedInputXmlTree != null) {
            InputXmlTree selectedTree = (InputXmlTree) currentSelectedInputXmlTree.getModel();

            int index = inputTrees.indexOf(selectedTree);
            if (index != -1 && index + 1 < inputTrees.size()) {
                inputTrees.remove(selectedTree);
                inputTrees.add(index + 1, selectedTree);

                // index of modelchildren is different from index of tree
                int indexOf = mapDataPart.getModelChildren().indexOf(selectedTree);
                if (indexOf != -1) {
                    mapDataPart.getViewer().appendSelection((EditPart) mapDataPart.getChildren().get(indexOf));
                }
            }
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.xmlmap.figures.treetools.zone.ZoneToolBar#minSize()
     */
    @Override
    public void minSize() {
        minimized = !minimized;
        EList<InputXmlTree> inputTrees = mapData.getInputTrees();
        for (InputXmlTree inputTree : inputTrees) {
            if (minimized != inputTree.isMinimized()) {
                inputTree.setMinimized(minimized);
            }
        }

    }

}
