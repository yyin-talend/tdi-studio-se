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
package org.talend.designer.xmlmap.figures.treetools.zone;

import java.util.List;

import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.MouseEvent;
import org.eclipse.emf.common.util.EList;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CommandStack;
import org.eclipse.swt.graphics.Image;
import org.talend.commons.ui.runtime.image.EImage;
import org.talend.commons.ui.runtime.image.ImageProvider;
import org.talend.core.model.metadata.IMetadataTable;
import org.talend.core.model.metadata.MetadataTable;
import org.talend.designer.xmlmap.XmlMapComponent;
import org.talend.designer.xmlmap.editor.XmlMapGraphicViewer;
import org.talend.designer.xmlmap.figures.layout.ZoneToolBarLayout;
import org.talend.designer.xmlmap.figures.treetools.ToolBarButtonImageFigure;
import org.talend.designer.xmlmap.model.emf.xmlmap.OutputTreeNode;
import org.talend.designer.xmlmap.model.emf.xmlmap.OutputXmlTree;
import org.talend.designer.xmlmap.model.emf.xmlmap.XmlmapFactory;
import org.talend.designer.xmlmap.parts.OutputXmlTreeEditPart;
import org.talend.designer.xmlmap.parts.XmlMapDataEditPart;
import org.talend.designer.xmlmap.ui.resource.ColorInfo;
import org.talend.designer.xmlmap.ui.resource.ColorProviderMapper;
import org.talend.designer.xmlmap.ui.tabs.MapperManager;
import org.talend.designer.xmlmap.util.XmlMapUtil;

/**
 * wchen class global comment. Detailled comment
 */
public class OutputZoneToolBar extends ZoneToolBar {

    private ToolBarButtonImageFigure add_btn, remove_btn, auto_map;

    public OutputZoneToolBar(XmlMapDataEditPart mapDataPart) {
        super(mapDataPart);

        ZoneToolBarLayout manager = new ZoneToolBarLayout();
        manager.setVertical(false);
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
        setTooltips();
    }

    private void setTooltips() {
        Label tooltip = new Label();
        tooltip.setText("Add output table");
        add_btn.setToolTip(tooltip);

        tooltip = new Label();
        tooltip.setText("Remove selected output table");
        remove_btn.setToolTip(tooltip);

        tooltip = new Label();
        tooltip.setText("Move up selected output table");
        move_up.setToolTip(tooltip);

        tooltip = new Label();
        tooltip.setText("Move down selected output table");
        move_down.setToolTip(tooltip);

        tooltip = new Label();
        tooltip.setText("Minimize all output tables");
        min_size.setToolTip(tooltip);

        tooltip = new Label();
        tooltip.setText("Map automatically inputs and outputs (for empty expressions only)");
        auto_map.setToolTip(tooltip);

    }

    private void removeMetadataTableByName(String name) {
        if (name == null) {
            return;
        }
        IMetadataTable found = null;
        if (mapperComponent != null && mapperComponent.getMetadataList() != null) {
            for (IMetadataTable table : mapperComponent.getMetadataList()) {
                if (name.equals(table.getTableName())) {
                    found = table;
                    break;
                }
            }
            if (found != null) {
                mapperComponent.getMetadataList().remove(found);
            }
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
                            mapperComponent.getMetadataList().add(metadataTable);
                            mapperComponent.getProcess().addUniqueConnectionName(outputName);

                            int indexOf = mapDataPart.getModelChildren().indexOf(createOutputXmlTree);
                            if (indexOf != -1) {
                                mapDataPart.getViewer().select((EditPart) mapDataPart.getChildren().get(indexOf));
                            }

                            // need set focus back to canvas

                            if (!min_size.isEnabled()) {
                                min_size.setEnabled(true);
                            }
                            // check problem for created output incase input main is multiloop
                            graphicViewer.getMapperManager().getProblemsAnalyser().checkProblems(createOutputXmlTree);
                            graphicViewer.getMapperManager().getMapperUI().updateStatusBar();
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
                            int indexOf = mapDataPart.getModelChildren().indexOf(outputTree);

                            XmlMapComponent mapperComponent = manager.getMapperComponent();

                            mapperComponent.getProcess().removeUniqueConnectionName(outputTree.getName());
                            removeMetadataTableByName(outputTree.getName());
                            for (OutputTreeNode treeNode : outputTree.getNodes()) {
                                XmlMapUtil.detachConnectionsSouce(treeNode, mapData);
                            }
                            mapData.getOutputTrees().remove(outputTree);
                            XmlMapUtil.detachFilterSource(outputTree, mapData);

                            indexOf = indexOf - 1;
                            if (indexOf > -1
                                    && (EditPart) mapDataPart.getChildren().get(indexOf) instanceof OutputXmlTreeEditPart) {
                                mapDataPart.getViewer().select((EditPart) mapDataPart.getChildren().get(indexOf));
                            } else if (indexOf > -1 && indexOf + 1 < mapDataPart.getChildren().size()
                                    && (EditPart) mapDataPart.getChildren().get(indexOf + 1) instanceof OutputXmlTreeEditPart) {
                                mapDataPart.getViewer().select((EditPart) mapDataPart.getChildren().get(indexOf + 1));
                            } else {
                                manager.getMapperUI().getTabFolderEditors().getOutputMetaEditorView()
                                        .setMetadataTableEditor(null);
                            }
                            graphicViewer.getMapperManager().getProblemsAnalyser().clearProblemsForTree(outputTree);
                            graphicViewer.getMapperManager().getMapperUI().updateStatusBar();
                        }
                        if (mapData.getOutputTrees().isEmpty() && min_size.isEnabled()) {
                            min_size.setEnabled(false);
                        }

                    }
                });

            }
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

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.xmlmap.figures.treetools.zone.ZoneToolBar#moveUp()
     */
    @Override
    public void moveUp() {
        List<OutputXmlTree> outputTrees = mapData.getOutputTrees();
        OutputXmlTreeEditPart currentSelectedOutputXmlTree = graphicViewer.getFiguresManager().getCurrentSelectedOutputXmlTree();
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

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.xmlmap.figures.treetools.zone.ZoneToolBar#moveDown()
     */
    @Override
    public void moveDown() {
        List<OutputXmlTree> outputTrees = mapData.getOutputTrees();
        OutputXmlTreeEditPart currentSelectedOutputXmlTree = graphicViewer.getFiguresManager().getCurrentSelectedOutputXmlTree();
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

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.xmlmap.figures.treetools.zone.ZoneToolBar#minSize()
     */
    @Override
    public void minSize() {
        minimized = !minimized;
        EList<OutputXmlTree> outputTrees = mapData.getOutputTrees();
        for (OutputXmlTree outputTree : outputTrees) {
            if (minimized != outputTree.isMinimized()) {
                outputTree.setMinimized(minimized);
            }
        }
    }
}
