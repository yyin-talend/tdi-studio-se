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

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.MouseEvent;
import org.eclipse.emf.common.util.EList;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CommandStack;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.graphics.Image;
import org.talend.core.CorePlugin;
import org.talend.core.model.metadata.IMetadataTable;
import org.talend.core.model.metadata.MetadataColumn;
import org.talend.core.model.metadata.MetadataTable;
import org.talend.core.model.process.IElementParameter;
import org.talend.core.model.process.IProcess;
import org.talend.core.model.process.IProcess2;
import org.talend.core.prefs.ui.MetadataTypeLengthConstants;
import org.talend.designer.xmlmap.figures.layout.ZoneToolBarLayout;
import org.talend.designer.xmlmap.figures.treetools.ToolBarButtonImageFigure;
import org.talend.designer.xmlmap.model.emf.xmlmap.InputXmlTree;
import org.talend.designer.xmlmap.model.emf.xmlmap.OutputTreeNode;
import org.talend.designer.xmlmap.model.emf.xmlmap.OutputXmlTree;
import org.talend.designer.xmlmap.model.emf.xmlmap.XmlmapFactory;
import org.talend.designer.xmlmap.parts.InputXmlTreeEditPart;
import org.talend.designer.xmlmap.parts.XmlMapDataEditPart;
import org.talend.designer.xmlmap.ui.dialog.PropertySetDialog;
import org.talend.designer.xmlmap.ui.resource.ColorInfo;
import org.talend.designer.xmlmap.ui.resource.ColorProviderMapper;
import org.talend.designer.xmlmap.ui.resource.ImageInfo;
import org.talend.designer.xmlmap.ui.resource.ImageProviderMapper;
import org.talend.designer.xmlmap.ui.tabs.MapperManager;

/**
 * wchen class global comment. Detailled comment
 */
public class InputZoneToolBar extends ZoneToolBar {

    private ToolBarButtonImageFigure propertyButton;

    private boolean isDieOnError = true;

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

        // disable for 5.0.0
        // propertyButton = new PropertyButton();
        // add(propertyButton);

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

        final IElementParameter elementParameter = mapperComponent.getElementParameter("DIE_ON_ERROR");
        if (elementParameter != null && elementParameter.getValue() != null) {
            isDieOnError = Boolean.valueOf(elementParameter.getValue().toString());
            graphicViewer.getMapperManager().setDieOnError(isDieOnError);
        }
        setTooltips();
    }

    private void setTooltips() {
        Label tooltip = new Label();
        tooltip.setText("Move up selected input lookup table");
        move_up.setToolTip(tooltip);

        tooltip = new Label();
        tooltip.setText("Move down selected input lookup table");
        move_down.setToolTip(tooltip);

        tooltip = new Label();
        tooltip.setText("Minimize all input tables");
        min_size.setToolTip(tooltip);

        // disable for 5.0.0
        // tooltip = new Label();
        // tooltip.setText("Setup the configurations of tXMLMap");
        // propertyButton.setToolTip(tooltip);

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

    class PropertyButton extends ToolBarButtonImageFigure {

        public PropertyButton() {
            super(ImageProviderMapper.getImage(ImageInfo.PROPERTY_TOOL_ICON));
        }

        @Override
        public void toolBarButtonPressed(MouseEvent me) {
            super.toolBarButtonPressed(me);
            if (mapperComponent != null) {
                boolean dieOnError = graphicViewer.getMapperManager().isDieOnError();

                PropertySetDialog propertySet = new PropertySetDialog(null, dieOnError);
                if (propertySet.open() == Window.OK) {
                    if (graphicViewer != null && propertySet.isValueChanged()) {
                        final boolean newValue = !dieOnError;

                        CommandStack commandStack = graphicViewer.getEditDomain().getCommandStack();
                        commandStack.execute(new Command() {

                            @Override
                            public void execute() {
                                graphicViewer.getMapperManager().setDieOnError(newValue);
                                if (newValue) {
                                    if (!mapData.getOutputTrees().isEmpty()) {
                                        OutputXmlTree outputTree = mapData.getOutputTrees().get(0);
                                        if (outputTree.isErrorReject()) {
                                            // mapperComponent.getMetadataList().add(metadataTable);
                                            mapData.getOutputTrees().remove(0);
                                            mapperComponent.getProcess().removeUniqueConnectionName(outputTree.getName());
                                            removeMetadataTableByName(outputTree.getName());
                                            if (!mapData.getOutputTrees().isEmpty()) {
                                                int indexOf = mapDataPart.getModelChildren().indexOf(
                                                        mapData.getOutputTrees().get(0));
                                                if (indexOf != -1) {
                                                    mapDataPart.getViewer().select(
                                                            (EditPart) mapDataPart.getChildren().get(indexOf));
                                                }
                                            }
                                        }
                                    }
                                } else {
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
                                            final String uniqueName = ((IProcess2) process).generateUniqueConnectionName("row",
                                                    baseName);
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

                                    }
                                }
                            }
                        });
                    }
                }
            }
        }
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

}
