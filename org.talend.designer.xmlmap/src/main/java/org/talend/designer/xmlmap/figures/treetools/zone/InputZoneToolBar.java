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
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.MouseEvent;
import org.eclipse.emf.common.util.EList;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CommandStack;
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

    public InputZoneToolBar(XmlMapDataEditPart mapDataPart) {
        this.mapDataPart = mapDataPart;
        this.mapData = (XmlMapData) mapDataPart.getModel();

        ZoneToolBarLayout manager = new ZoneToolBarLayout();
        manager.setSpacing(8);
        setLayoutManager(manager);
        setOpaque(true);
        setBackgroundColor(ColorProviderMapper.getColor(ColorInfo.ZONE_BACKGROUND_COLOR));

        move_up = new MoveUpButton();
        this.add(move_up);
        move_down = new MoveDownButton();
        this.add(move_down);
        Label figure = new Label("");
        figure.setOpaque(true);
        figure.setBackgroundColor(ColorConstants.lightGray);
        add(figure);
        min_size = new MinSizeButton();
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

            CommandStack commandStack = mapDataPart.getViewer().getEditDomain().getCommandStack();
            commandStack.execute(new Command() {

                @Override
                public void execute() {
                    List<InputXmlTree> inputTrees = mapData.getInputTrees();
                    for (int i = 0; i < indexToChange.length; i++) {
                        if (indexToChange[i] != null) {
                            int index = indexToChange[i];
                            InputXmlTree temp = inputTrees.get(index);
                            inputTrees.remove(temp);
                            inputTrees.add(index - 1, temp);
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

            CommandStack commandStack = mapDataPart.getViewer().getEditDomain().getCommandStack();
            commandStack.execute(new Command() {

                @Override
                public void execute() {
                    List<InputXmlTree> inputTrees = mapData.getInputTrees();
                    for (int i = 0; i < indexToChange.length; i++) {
                        if (indexToChange[i] != null) {
                            int index = indexToChange[i];
                            InputXmlTree temp = inputTrees.get(index);
                            inputTrees.remove(temp);
                            inputTrees.add(index + 1, temp);
                        }
                    }
                }
            });

        }
    }

    class MinSizeButton extends ToolBarButtonImageFigure {

        private boolean isMinSize = false;

        public MinSizeButton() {
            super(ImageProviderMapper.getImage(ImageInfo.MINIMIZE_ICON));
        }

        @Override
        public void toolBarButtonPressed(MouseEvent me) {
            super.toolBarButtonPressed(me);
            CommandStack commandStack = mapDataPart.getViewer().getEditDomain().getCommandStack();
            commandStack.execute(new Command() {

                @Override
                public void execute() {
                    EList<InputXmlTree> inputTrees = mapData.getInputTrees();
                    for (InputXmlTree inputTree : inputTrees) {
                        if (inputTree.isMinimized() != !isMinSize) {
                            inputTree.setMinimized(!isMinSize);
                        }
                    }

                }
            });

        }
    }

}
