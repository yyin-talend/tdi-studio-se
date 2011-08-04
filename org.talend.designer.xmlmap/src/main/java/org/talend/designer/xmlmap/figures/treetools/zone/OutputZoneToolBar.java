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

import org.eclipse.draw2d.Clickable;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.MouseEvent;
import org.eclipse.emf.common.util.EList;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CommandStack;
import org.eclipse.swt.graphics.Image;
import org.talend.commons.ui.runtime.image.EImage;
import org.talend.commons.ui.runtime.image.ImageProvider;
import org.talend.core.CorePlugin;
import org.talend.core.model.metadata.IMetadataTable;
import org.talend.core.model.metadata.MetadataColumn;
import org.talend.core.model.metadata.MetadataTable;
import org.talend.core.model.process.IElementParameter;
import org.talend.core.model.process.IProcess;
import org.talend.core.model.process.IProcess2;
import org.talend.core.prefs.ui.MetadataTypeLengthConstants;
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

    private ToolBarButtonImageFigure add_btn, remove_btn, auto_map, die_on_error;

    private XmlMapComponent mapperComponent;

    private boolean isDieOnError = false;

    public OutputZoneToolBar(XmlMapDataEditPart mapDataPart) {
        super(mapDataPart);
        if (mapDataPart.getViewer() instanceof XmlMapGraphicViewer) {
            mapperComponent = this.graphicViewer.getMapperManager().getMapperComponent();
        }

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

        if (mapperComponent != null) {
            IElementParameter elementParameter = mapperComponent.getElementParameter("DIE_ON_ERROR");
            if (elementParameter != null && elementParameter.getValue() != null) {
                isDieOnError = Boolean.valueOf(elementParameter.getValue().toString());
            }
            // /////////////////////////////// test
            // isDieOnError should set true in default on component side , but this part is not done now
            isDieOnError = true;
            // ///////////////////////////////
            graphicViewer.getMapperManager().setDieOnError(isDieOnError);
        }

        if (isDieOnError) {
            image = ImageProvider.getImage(EImage.CHECKED_ICON);
        } else {
            image = ImageProvider.getImage(EImage.UNCHECKED_ICON);
        }
        die_on_error = new DieOnErrorButton(image);
        this.add(die_on_error);

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
                            removeMetadataTableByName(outputTree.getName());
                            for (OutputTreeNode treeNode : outputTree.getNodes()) {
                                XmlMapUtil.detachConnectionsSouce(treeNode, mapData);
                            }
                            mapData.getOutputTrees().remove(outputTree);
                            XmlMapUtil.detachFilterSource(outputTree, mapData);
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

    class DieOnErrorButton extends ToolBarButtonImageFigure {

        public DieOnErrorButton(Image image) {
            super(image);
            setText("Die on Error");
            setStyle(Clickable.STYLE_TOGGLE);
        }

        @Override
        public void toolBarButtonPressed(MouseEvent me) {
            super.toolBarButtonPressed(me);
            if (graphicViewer != null) {
                CommandStack commandStack = mapDataPart.getViewer().getEditDomain().getCommandStack();
                commandStack.execute(new Command() {

                    @Override
                    public void execute() {
                        isDieOnError = !isDieOnError;
                        graphicViewer.getMapperManager().setDieOnError(isDieOnError);
                        Image image = null;
                        if (isDieOnError) {
                            image = ImageProvider.getImage(EImage.CHECKED_ICON);
                            if (!mapData.getOutputTrees().isEmpty()) {
                                OutputXmlTree outputTree = mapData.getOutputTrees().get(0);
                                if (outputTree.isErrorReject()) {
                                    // mapperComponent.getMetadataList().add(metadataTable);
                                    mapData.getOutputTrees().remove(0);
                                    mapperComponent.getProcess().removeUniqueConnectionName(outputTree.getName());
                                    removeMetadataTableByName(outputTree.getName());
                                    if (!mapData.getOutputTrees().isEmpty()) {
                                        int indexOf = mapDataPart.getModelChildren().indexOf(mapData.getOutputTrees().get(0));
                                        if (indexOf != -1) {
                                            mapDataPart.getViewer().select((EditPart) mapDataPart.getChildren().get(indexOf));
                                        }
                                    }
                                }
                            }
                        } else {
                            image = ImageProvider.getImage(EImage.UNCHECKED_ICON);
                            boolean hasRejectTable = false;
                            if (!mapData.getOutputTrees().isEmpty()) {
                                OutputXmlTree outputTree = mapData.getOutputTrees().get(0);
                                if (outputTree.isErrorReject()) {
                                    hasRejectTable = true;
                                }
                            }
                            if (!hasRejectTable) {
                                String baseName = MapperManager.ERROR_REJECT;

                                IProcess process = mapperComponent.getProcess();
                                String tableName = baseName;
                                if (!process.checkValidConnectionName(baseName) && process instanceof IProcess2) {
                                    final String uniqueName = ((IProcess2) process).generateUniqueConnectionName("row", baseName);
                                    tableName = uniqueName;
                                    ((IProcess2) process).addUniqueConnectionName(uniqueName);
                                } else if (process instanceof IProcess2) {
                                    tableName = baseName;
                                    ((IProcess2) process).addUniqueConnectionName(baseName);
                                }
                                OutputXmlTree outputXmlTree = XmlmapFactory.eINSTANCE.createOutputXmlTree();
                                outputXmlTree.setErrorReject(true);
                                outputXmlTree.setName(tableName);
                                mapData.getOutputTrees().add(0, outputXmlTree);

                                MetadataTable metadataTable = new MetadataTable();
                                metadataTable.setLabel(tableName);
                                metadataTable.setTableName(tableName);

                                MetadataColumn errorMessageCol = new MetadataColumn();
                                errorMessageCol.setLabel(MapperManager.ERROR_REJECT_MESSAGE);
                                errorMessageCol.setTalendType(CorePlugin.getDefault().getPreferenceStore()
                                        .getString(MetadataTypeLengthConstants.FIELD_DEFAULT_TYPE));
                                errorMessageCol.setNullable(true);
                                errorMessageCol.setOriginalDbColumnName(MapperManager.ERROR_REJECT_MESSAGE);
                                errorMessageCol.setReadOnly(true);
                                errorMessageCol.setCustom(true);
                                metadataTable.getListColumns().add(errorMessageCol);

                                MetadataColumn errorStackTrace = new MetadataColumn();
                                errorStackTrace.setLabel(MapperManager.ERROR_REJECT_STACK_TRACE);
                                errorStackTrace.setTalendType(CorePlugin.getDefault().getPreferenceStore()
                                        .getString(MetadataTypeLengthConstants.FIELD_DEFAULT_TYPE));
                                errorStackTrace.setNullable(true);
                                errorStackTrace.setOriginalDbColumnName(MapperManager.ERROR_REJECT_STACK_TRACE);
                                errorStackTrace.setReadOnly(true);
                                errorStackTrace.setCustom(true);
                                metadataTable.getListColumns().add(errorStackTrace);
                                mapperComponent.getMetadataList().add(metadataTable);

                                OutputTreeNode errorMessageNode = XmlmapFactory.eINSTANCE.createOutputTreeNode();
                                errorMessageNode.setName(MapperManager.ERROR_REJECT_MESSAGE);
                                errorMessageNode.setType(errorMessageCol.getTalendType());
                                errorMessageNode.setNullable(true);
                                outputXmlTree.getNodes().add(errorMessageNode);

                                OutputTreeNode errorStackTraceNode = XmlmapFactory.eINSTANCE.createOutputTreeNode();
                                errorStackTraceNode.setName(MapperManager.ERROR_REJECT_STACK_TRACE);
                                errorStackTraceNode.setType(errorStackTrace.getTalendType());
                                errorStackTraceNode.setNullable(true);
                                outputXmlTree.getNodes().add(errorStackTraceNode);

                                int indexOf = mapDataPart.getModelChildren().indexOf(outputXmlTree);
                                if (indexOf != -1) {
                                    mapDataPart.getViewer().select((EditPart) mapDataPart.getChildren().get(indexOf));
                                }

                                move_down.setEnabled(false);
                                remove_btn.setEnabled(false);

                                if (!min_size.isEnabled()) {
                                    min_size.setEnabled(true);
                                }
                            }
                        }

                        setImage(image);
                    }
                });
            }
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
