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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.MouseEvent;
import org.eclipse.emf.common.util.EList;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CommandStack;
import org.eclipse.swt.graphics.Image;
import org.talend.commons.ui.runtime.image.EImage;
import org.talend.commons.ui.runtime.image.ImageProvider;
import org.talend.designer.xmlmap.figures.layout.ZoneToolBarLayout;
import org.talend.designer.xmlmap.figures.treetools.ToolBarButtonImageFigure;
import org.talend.designer.xmlmap.model.emf.xmlmap.InputXmlTree;
import org.talend.designer.xmlmap.model.emf.xmlmap.XmlMapData;
import org.talend.designer.xmlmap.parts.InputXmlTreeEditPart;
import org.talend.designer.xmlmap.parts.XmlMapDataEditPart;
import org.talend.designer.xmlmap.ui.resource.ColorInfo;
import org.talend.designer.xmlmap.ui.resource.ColorProviderMapper;
import org.talend.designer.xmlmap.ui.resource.ImageInfo;
import org.talend.designer.xmlmap.ui.resource.ImageProviderMapper;

/**
 * wchen class global comment. Detailled comment
 */
public class InputZoneToolBar extends Figure {

    private ToolBarButtonImageFigure move_up, move_down, min_size;

    private XmlMapData mapData;

    private XmlMapDataEditPart mapDataPart;

    private boolean minimized = true;

    private Image restorImage = ImageProviderMapper.getImage(ImageInfo.RESTORE_ICON);

    private Image miniImage = ImageProviderMapper.getImage(ImageInfo.MINIMIZE_ICON);

    public InputZoneToolBar(XmlMapDataEditPart mapDataPart) {
        this.mapDataPart = mapDataPart;
        this.mapData = (XmlMapData) mapDataPart.getModel();

        ZoneToolBarLayout manager = new ZoneToolBarLayout();
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

    class MoveUpButton extends ToolBarButtonImageFigure {

        public MoveUpButton() {
            super(ImageProvider.getImage(EImage.UP_ICON));
        }

        @Override
        public void toolBarButtonPressed(MouseEvent me) {
            super.toolBarButtonPressed(me);
            List selectedEditParts = mapDataPart.getViewer().getSelectedEditParts();
            final Integer[] indexToChange = new Integer[selectedEditParts.size()];
            for (int i = 0; i < selectedEditParts.size(); i++) {
                Object selection = selectedEditParts.get(i);
                if (selection instanceof InputXmlTreeEditPart) {
                    InputXmlTreeEditPart part = (InputXmlTreeEditPart) selection;
                    final InputXmlTree tree = (InputXmlTree) part.getModel();
                    final int indexOf = mapData.getInputTrees().indexOf(tree);
                    if (indexOf != -1 && indexOf != 0) {
                        indexToChange[i] = indexOf;
                    }
                }
            }
            Arrays.sort(indexToChange);
            CommandStack commandStack = mapDataPart.getViewer().getEditDomain().getCommandStack();
            commandStack.execute(new Command() {

                @Override
                public void execute() {
                    List<InputXmlTree> inputTrees = mapData.getInputTrees();

                    List<InputXmlTree> movedObjects = new ArrayList<InputXmlTree>();
                    for (int i = 0; i < indexToChange.length; i++) {
                        if (indexToChange[i] != null) {
                            int index = indexToChange[i];
                            InputXmlTree temp = inputTrees.get(index);
                            movedObjects.add(temp);
                            inputTrees.remove(temp);
                            inputTrees.add(index - 1, temp);
                        }
                    }

                    for (InputXmlTree tree : movedObjects) {
                        int indexOf = mapDataPart.getModelChildren().indexOf(tree);
                        if (indexOf != -1) {
                            mapDataPart.getViewer().appendSelection((EditPart) mapDataPart.getChildren().get(indexOf));
                        }
                    }
                }
            });

        }
    }

    class MoveDownButton extends ToolBarButtonImageFigure {

        public MoveDownButton() {
            super(ImageProvider.getImage(EImage.DOWN_ICON));
        }

        @Override
        public void toolBarButtonPressed(MouseEvent me) {
            super.toolBarButtonPressed(me);
            List selectedEditParts = mapDataPart.getViewer().getSelectedEditParts();
            final List<Integer> indexToChange = new ArrayList<Integer>();
            for (int i = 0; i < selectedEditParts.size(); i++) {
                Object selection = selectedEditParts.get(i);
                if (selection instanceof InputXmlTreeEditPart) {
                    InputXmlTreeEditPart part = (InputXmlTreeEditPart) selection;
                    final InputXmlTree tree = (InputXmlTree) part.getModel();
                    final int indexOf = mapData.getInputTrees().indexOf(tree);
                    if (indexOf != -1 && indexOf < mapData.getInputTrees().size() - 1) {
                        indexToChange.add(indexOf);
                    }
                }
            }

            Collections.sort(indexToChange);
            Collections.reverse(indexToChange);

            CommandStack commandStack = mapDataPart.getViewer().getEditDomain().getCommandStack();
            commandStack.execute(new Command() {

                @Override
                public void execute() {
                    List<InputXmlTree> inputTrees = mapData.getInputTrees();
                    List<InputXmlTree> movedObjects = new ArrayList<InputXmlTree>();
                    for (int i = 0; i < indexToChange.size(); i++) {
                        int index = indexToChange.get(i);
                        InputXmlTree temp = inputTrees.get(index);
                        movedObjects.add(temp);
                        inputTrees.remove(temp);
                        inputTrees.add(index + 1, temp);
                    }

                    for (int i = 0; i < movedObjects.size(); i++) {
                        InputXmlTree tree = movedObjects.get(i);
                        int indexOf = mapDataPart.getModelChildren().indexOf(tree);
                        if (indexOf != -1) {
                            mapDataPart.getViewer().appendSelection((EditPart) mapDataPart.getChildren().get(indexOf));

                        }
                    }
                }
            });

        }
    }

    class MinSizeButton extends ToolBarButtonImageFigure {

        public MinSizeButton(Image image) {
            super(image);
        }

        @Override
        public void toolBarButtonPressed(MouseEvent me) {
            super.toolBarButtonPressed(me);
            CommandStack commandStack = mapDataPart.getViewer().getEditDomain().getCommandStack();
            commandStack.execute(new Command() {

                @Override
                public void execute() {
                    minimized = !minimized;
                    EList<InputXmlTree> inputTrees = mapData.getInputTrees();
                    for (InputXmlTree inputTree : inputTrees) {
                        if (minimized != inputTree.isMinimized()) {
                            inputTree.setMinimized(minimized);
                        }
                    }
                }
            });

            if (minimized) {
                setImage(restorImage);
            } else {
                setImage(miniImage);
            }
            mapDataPart.getViewer().deselectAll();
        }
    }

}
