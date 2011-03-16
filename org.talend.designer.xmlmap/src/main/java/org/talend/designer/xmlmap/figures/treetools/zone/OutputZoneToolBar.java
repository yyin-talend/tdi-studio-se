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
import org.talend.core.model.metadata.MetadataTable;
import org.talend.designer.xmlmap.XmlMapComponent;
import org.talend.designer.xmlmap.editor.XmlMapGraphicViewer;
import org.talend.designer.xmlmap.figures.layout.ZoneToolBarLayout;
import org.talend.designer.xmlmap.figures.treetools.ToolBarButtonImageFigure;
import org.talend.designer.xmlmap.model.emf.xmlmap.OutputTreeNode;
import org.talend.designer.xmlmap.model.emf.xmlmap.OutputXmlTree;
import org.talend.designer.xmlmap.model.emf.xmlmap.XmlMapData;
import org.talend.designer.xmlmap.model.emf.xmlmap.XmlmapFactory;
import org.talend.designer.xmlmap.parts.OutputXmlTreeEditPart;
import org.talend.designer.xmlmap.parts.XmlMapDataEditPart;
import org.talend.designer.xmlmap.ui.resource.ColorInfo;
import org.talend.designer.xmlmap.ui.resource.ColorProviderMapper;
import org.talend.designer.xmlmap.ui.resource.ImageInfo;
import org.talend.designer.xmlmap.ui.resource.ImageProviderMapper;
import org.talend.designer.xmlmap.ui.tabs.MapperManager;
import org.talend.designer.xmlmap.util.XmlMapUtil;

/**
 * wchen class global comment. Detailled comment
 */
public class OutputZoneToolBar extends Figure {

    private ToolBarButtonImageFigure add_btn, remove_btn, move_up, move_down, min_size, auto_map, die_on_error;

    private XmlMapData mapData;

    private XmlMapDataEditPart mapDataPart;

    private XmlMapGraphicViewer graphicViewer;

    private Image restorImage = ImageProviderMapper.getImage(ImageInfo.RESTORE_ICON);

    private Image miniImage = ImageProviderMapper.getImage(ImageInfo.MINIMIZE_ICON);

    private boolean minimized = true;

    public OutputZoneToolBar(XmlMapDataEditPart mapDataPart) {
        this.mapDataPart = mapDataPart;
        this.mapData = (XmlMapData) mapDataPart.getModel();

        if (mapDataPart.getViewer() instanceof XmlMapGraphicViewer) {
            this.graphicViewer = (XmlMapGraphicViewer) mapDataPart.getViewer();
        }

        ZoneToolBarLayout manager = new ZoneToolBarLayout();
        manager.setSpacing(8);
        setLayoutManager(manager);
        setOpaque(true);
        setBackgroundColor(ColorProviderMapper.getColor(ColorInfo.ZONE_BACKGROUND_COLOR));
        add_btn = new AddButton();
        this.add(add_btn);
        remove_btn = new RemoveButton();
        remove_btn.setEnabled(false);
        this.add(remove_btn);
        move_up = new MoveUpButton();
        move_up.setEnabled(false);
        this.add(move_up);
        move_down = new MoveDownButton();
        move_down.setEnabled(false);
        this.add(move_down);
        add(new Label(" "));

        for (OutputXmlTree tree : mapData.getOutputTrees()) {
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
        if (mapData.getOutputTrees().isEmpty()) {
            min_size.setEnabled(false);
        }
        this.add(min_size);

        auto_map = new AutoMapButton();
        this.add(auto_map);
        if (mapData.getOutputTrees().isEmpty()) {
            min_size.setEnabled(false);
        }

    }

    public void setRemoveButtonEnable(boolean enable) {
        remove_btn.setEnabled(enable);
    }

    public void setMoveUpEnable(boolean enable) {
        move_up.setEnabled(enable);
    }

    public void setMoveDownEnable(boolean enable) {
        move_down.setEnabled(enable);
    }

    class AddButton extends ToolBarButtonImageFigure {

        public AddButton() {
            super(ImageProvider.getImage(EImage.ADD_ICON));
        }

        @Override
        public void toolBarButtonPressed(MouseEvent me) {
            super.toolBarButtonPressed(me);
            if (graphicViewer != null) {
                final OutputXmlTree createOutputXmlTree = XmlmapFactory.eINSTANCE.createOutputXmlTree();
                CommandStack commandStack = mapDataPart.getViewer().getEditDomain().getCommandStack();
                commandStack.execute(new Command() {

                    @Override
                    public void execute() {
                        if (mapDataPart.getViewer() instanceof XmlMapGraphicViewer) {
                            XmlMapGraphicViewer viewer = (XmlMapGraphicViewer) mapDataPart.getViewer();
                            MapperManager manager = viewer.getMapperManager();
                            String outputName = manager.getMapperUI().openNewOutputCreationDialog();
                            if (outputName == null) {
                                return;
                            }
                            createOutputXmlTree.setName(outputName);
                            mapData.getOutputTrees().add(createOutputXmlTree);
                            MetadataTable metadataTable = new MetadataTable();
                            metadataTable.setLabel(outputName);
                            metadataTable.setTableName(outputName);
                            manager.getMapperComponent().getMetadataList().add(metadataTable);
                            manager.getMapperComponent().getProcess().addUniqueConnectionName(outputName);

                            int indexOf = mapDataPart.getModelChildren().indexOf(createOutputXmlTree);
                            if (indexOf != -1) {
                                mapDataPart.getViewer().select((EditPart) mapDataPart.getChildren().get(indexOf));
                            }

                            // need set focus back to canvas

                            if (!min_size.isEnabled()) {
                                min_size.setEnabled(true);
                            }

                        }

                    }
                });
            }
        }
    }

    class RemoveButton extends ToolBarButtonImageFigure {

        public RemoveButton() {
            super(ImageProvider.getImage(EImage.MINUS_ICON));
        }

        @Override
        public void toolBarButtonPressed(MouseEvent me) {
            super.toolBarButtonPressed(me);
            if (graphicViewer != null) {
                CommandStack commandStack = graphicViewer.getEditDomain().getCommandStack();
                commandStack.execute(new Command() {

                    @Override
                    public void execute() {
                        MapperManager manager = graphicViewer.getMapperManager();
                        OutputXmlTreeEditPart currentSelectedOutputXmlTree = graphicViewer.getFiguresManager()
                                .getCurrentSelectedOutputXmlTree();
                        if (currentSelectedOutputXmlTree != null) {
                            OutputXmlTree outputTree = (OutputXmlTree) currentSelectedOutputXmlTree.getModel();
                            XmlMapComponent mapperComponent = manager.getMapperComponent();

                            mapperComponent.getProcess().removeUniqueConnectionName(outputTree.getName());
                            for (OutputTreeNode treeNode : outputTree.getNodes()) {
                                XmlMapUtil.detachConnectionsSouce(treeNode, mapData);
                            }
                            mapData.getOutputTrees().remove(outputTree);
                        }
                        if (mapData.getOutputTrees().isEmpty() && min_size.isEnabled()) {
                            min_size.setEnabled(false);
                        }
                    }
                });

            }
        }
    }

    class MoveUpButton extends ToolBarButtonImageFigure {

        public MoveUpButton() {
            super(ImageProvider.getImage(EImage.UP_ICON));
        }

        @Override
        public void toolBarButtonPressed(MouseEvent me) {
            super.toolBarButtonPressed(me);
            if (graphicViewer != null) {
                CommandStack commandStack = graphicViewer.getEditDomain().getCommandStack();
                commandStack.execute(new Command() {

                    @Override
                    public void execute() {
                        List<OutputXmlTree> outputTrees = mapData.getOutputTrees();
                        OutputXmlTreeEditPart currentSelectedOutputXmlTree = graphicViewer.getFiguresManager()
                                .getCurrentSelectedOutputXmlTree();
                        if (currentSelectedOutputXmlTree != null) {
                            OutputXmlTree selectedTree = (OutputXmlTree) currentSelectedOutputXmlTree.getModel();

                            int index = outputTrees.indexOf(selectedTree);
                            if (index != -1 && index - 1 >= 0) {
                                outputTrees.remove(selectedTree);
                                outputTrees.add(index - 1, selectedTree);

                                // index of modelchildren is different from index of tree
                                int indexOf = mapDataPart.getModelChildren().indexOf(selectedTree);
                                if (indexOf != -1) {
                                    mapDataPart.getViewer().appendSelection((EditPart) mapDataPart.getChildren().get(indexOf));
                                }
                            }
                        }
                    }
                });
            }

        }
    }

    class MoveDownButton extends ToolBarButtonImageFigure {

        public MoveDownButton() {
            super(ImageProvider.getImage(EImage.DOWN_ICON));
        }

        @Override
        public void toolBarButtonPressed(MouseEvent me) {
            super.toolBarButtonPressed(me);
            if (graphicViewer != null) {
                CommandStack commandStack = graphicViewer.getEditDomain().getCommandStack();
                commandStack.execute(new Command() {

                    @Override
                    public void execute() {
                        List<OutputXmlTree> outputTrees = mapData.getOutputTrees();
                        OutputXmlTreeEditPart currentSelectedOutputXmlTree = graphicViewer.getFiguresManager()
                                .getCurrentSelectedOutputXmlTree();
                        if (currentSelectedOutputXmlTree != null) {
                            OutputXmlTree selectedTree = (OutputXmlTree) currentSelectedOutputXmlTree.getModel();

                            int index = outputTrees.indexOf(selectedTree);
                            if (index != -1 && index + 1 < outputTrees.size()) {
                                outputTrees.remove(selectedTree);
                                outputTrees.add(index + 1, selectedTree);

                                // index of modelchildren is different from index of tree
                                int indexOf = mapDataPart.getModelChildren().indexOf(selectedTree);
                                if (indexOf != -1) {
                                    mapDataPart.getViewer().appendSelection((EditPart) mapDataPart.getChildren().get(indexOf));
                                }
                            }
                        }
                    }
                });
            }

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
                    EList<OutputXmlTree> outputTrees = mapData.getOutputTrees();
                    for (OutputXmlTree outputTree : outputTrees) {
                        if (minimized != outputTree.isMinimized()) {
                            outputTree.setMinimized(minimized);
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

    class AutoMapButton extends ToolBarButtonImageFigure {

        public AutoMapButton() {
            super(null);
            setText("Auto Map");
        }

        @Override
        public void toolBarButtonPressed(MouseEvent me) {
            super.toolBarButtonPressed(me);
            AutoMapper autoMap = new AutoMapper(mapData);
            autoMap.map();
        }
    }

    class DieOnErrorButton extends ToolBarButtonImageFigure {

        public DieOnErrorButton(Image image) {
            super(image);
        }

        @Override
        public void toolBarButtonPressed(MouseEvent me) {
            super.toolBarButtonPressed(me);
        }
    }
}
