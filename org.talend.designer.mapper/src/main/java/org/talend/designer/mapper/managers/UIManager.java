// ============================================================================
//
// Talend Community Edition
//
// Copyright (C) 2006 Talend - www.talend.com
//
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either
// version 2.1 of the License, or (at your option) any later version.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
// Lesser General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with this program; if not, write to the Free Software
// Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
//
// ============================================================================
package org.talend.designer.mapper.managers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.ScrollBar;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.ToolItem;
import org.talend.commons.ui.swt.tableviewer.TableViewerCreator;
import org.talend.commons.utils.data.list.IListenableListListener;
import org.talend.commons.utils.data.list.ListenableListEvent;
import org.talend.commons.utils.data.list.ListenableListEvent.TYPE;
import org.talend.commons.utils.image.ImageCapture;
import org.talend.commons.utils.image.ImageUtils;
import org.talend.commons.utils.time.TimeMeasure;
import org.talend.core.model.metadata.IMetadataColumn;
import org.talend.core.model.metadata.IMetadataTable;
import org.talend.core.model.metadata.editor.IMetadataEditorListener;
import org.talend.core.model.metadata.editor.MetadataEditorEvent;
import org.talend.core.model.metadata.editor.MetadataTableEditor;
import org.talend.core.model.process.IProcess;
import org.talend.core.ui.metadata.editor.MetadataTableEditorView;
import org.talend.designer.core.model.components.EParameterName;
import org.talend.designer.mapper.external.data.ExternalMapperUiProperties;
import org.talend.designer.mapper.language.LanguageProvider;
import org.talend.designer.mapper.model.table.AbstractDataMapTable;
import org.talend.designer.mapper.model.table.AbstractInOutTable;
import org.talend.designer.mapper.model.table.InputTable;
import org.talend.designer.mapper.model.table.OutputTable;
import org.talend.designer.mapper.model.tableentry.ConstraintTableEntry;
import org.talend.designer.mapper.model.tableentry.IColumnEntry;
import org.talend.designer.mapper.model.tableentry.ITableEntry;
import org.talend.designer.mapper.model.tableentry.InputColumnTableEntry;
import org.talend.designer.mapper.model.tableentry.TableEntryLocation;
import org.talend.designer.mapper.ui.MapperUI;
import org.talend.designer.mapper.ui.dnd.DraggingInfosPopup;
import org.talend.designer.mapper.ui.dnd.DropTargetOperationListener;
import org.talend.designer.mapper.ui.tabs.TabFolderEditors;
import org.talend.designer.mapper.ui.visualmap.TableEntryProperties;
import org.talend.designer.mapper.ui.visualmap.link.AbstractLink;
import org.talend.designer.mapper.ui.visualmap.link.IGraphicLink;
import org.talend.designer.mapper.ui.visualmap.link.LinkState;
import org.talend.designer.mapper.ui.visualmap.link.PointLinkDescriptor;
import org.talend.designer.mapper.ui.visualmap.table.DataMapTableView;
import org.talend.designer.mapper.ui.visualmap.table.EntryState;
import org.talend.designer.mapper.ui.visualmap.table.InputDataMapTableView;
import org.talend.designer.mapper.ui.visualmap.table.OutputDataMapTableView;
import org.talend.designer.mapper.ui.visualmap.zone.InputsZone;
import org.talend.designer.mapper.ui.visualmap.zone.OutputsZone;
import org.talend.designer.mapper.ui.visualmap.zone.Zone;
import org.talend.designer.mapper.ui.visualmap.zone.scrollable.TablesZoneView;
import org.talend.designer.mapper.ui.visualmap.zone.toolbar.ToolbarOutputZone;
import org.talend.designer.mapper.ui.visualmap.zone.toolbar.ToolbarZone;
import org.talend.designer.mapper.utils.DataMapExpressionParser;

/**
 * DOC amaumont class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 */
public class UIManager {

    private MapperManager mapperManager;

    private MapperUI mapperUI;

    private Composite refComposite;

    private boolean shiftPressed;

    private boolean ctrlPressed;

    private int mapperResponse = SWT.NONE;

    private TableManager tableManager;

    private ISelectionChangedListener inputsSelectionChangedListener;

    private ISelectionChangedListener outputsSelectionChangedListener;

    private List<IColumnEntry> lastCreatedInOutColumnEntries = new ArrayList<IColumnEntry>();

    private ExternalMapperUiProperties uiProperties;

    private OutputDataMapTableView currentSelectedOutputTableView;

    private InputDataMapTableView currentSelectedInputTableView;

    private Zone previousSelectedZone;

    private boolean previousSelectedTableIsConstraint;

    int currentDragDetail;

    private boolean dragging;

    /**
     * DOC amaumont UIManager constructor comment.
     * 
     * @param tableManager
     * @param manager
     */
    public UIManager(MapperManager mapperManager, TableManager tableManager) {
        this.mapperManager = mapperManager;
        this.tableManager = tableManager;
    }

    /**
     * Select a table view.
     * @param dataMapTableView
     */
    public void selectDataMapTableView(final DataMapTableView dataMapTableView) {
        
        TabFolderEditors tabFolderEditors = mapperUI.getTabFolderEditors();
        // tabFolderEditors.setSelection(TabFolderEditors.INDEX_TAB_METADATA_EDITOR);
        MetadataTableEditorView metadataTableEditorView = null;
        final Zone currentZone = dataMapTableView.getZone();

        List<? extends AbstractDataMapTable> tables = null;

        if (currentZone == Zone.INPUTS) {
            metadataTableEditorView = tabFolderEditors.getInputMetaEditor();
            tables = mapperManager.getInputTables();
            this.currentSelectedInputTableView = (InputDataMapTableView) dataMapTableView;
        } else if (currentZone == Zone.OUTPUTS) {
            metadataTableEditorView = tabFolderEditors.getOutputMetaEditor();
            tables = mapperManager.getOutputTables();
            this.currentSelectedOutputTableView = (OutputDataMapTableView) dataMapTableView;
        }

        if (currentZone != Zone.VARS) {

            updateToolbarButtonsStates(currentZone);

            final AbstractInOutTable abstractDataMapTable = (AbstractInOutTable) mapperManager
                    .retrieveAbstractDataMapTable(dataMapTableView);
            MetadataTableEditor currentMetadataTableEditor = metadataTableEditorView.getMetadataTableEditor();
            final TableViewer dataMapTableViewer = dataMapTableView.getTableViewerCreatorForColumns().getTableViewer();
            if (currentMetadataTableEditor == null || currentMetadataTableEditor != null
                    && !currentMetadataTableEditor.getMetadataTable().equals(abstractDataMapTable.getMetadataTable())) {

                currentMetadataTableEditor = new MetadataTableEditor(abstractDataMapTable.getMetadataTable(), abstractDataMapTable
                        .getName());

                currentMetadataTableEditor.addListener(new IMetadataEditorListener() {

                    public void handleEvent(MetadataEditorEvent event) {
                        if (event.type == MetadataEditorEvent.TYPE.METADATA_NAME_VALUE_CHANGED) {
                            List modifiedObjects = event.entries;
                            IMetadataColumn modifiedObject = null;
                            if (modifiedObjects != null && modifiedObjects.size() > 0) {
                                modifiedObject = (IMetadataColumn) modifiedObjects.get(0);
                            }
                            if (modifiedObject != null) {
                                TableEntryLocation tableEntryLocation = new TableEntryLocation(
                                        dataMapTableView.getDataMapTable().getName(), (String) event.previousValue);
                                ITableEntry dataMapTableEntry = mapperManager.retrieveTableEntry(tableEntryLocation);
                                processColumnNameChanged((String) event.newValue, dataMapTableView, dataMapTableEntry);
                            }
                            dataMapTableViewer.refresh();
                        } else if (event.type == MetadataEditorEvent.TYPE.METADATA_KEY_VALUE_CHANGED) {
                            for (int indice : event.entriesIndices) {
                                ITableEntry entry = abstractDataMapTable.getColumnEntries().get(indice);
                                dataMapTableViewer.refresh(entry, true);
                            }
                        }
                    }

                });

                final MetadataTableEditorView metadataTableEditorViewFinal = metadataTableEditorView;
                final TableViewer metadataEditorTableViewer = metadataTableEditorViewFinal.getTableViewerCreator().getTableViewer();
                final MetadataTableEditor metadataTableEditor = currentMetadataTableEditor;

                modifySelectionChangedListener(currentZone, metadataTableEditorViewFinal, metadataEditorTableViewer, metadataTableEditor);

                // init actions listeners for list which contains metadata
                metadataTableEditor.addModifiedListListener(new IListenableListListener() {

                    @SuppressWarnings("unchecked")
                    public void handleEvent(ListenableListEvent event) {

                        DataMapTableView view = mapperManager.retrieveAbstractDataMapTableView(abstractDataMapTable);
                        if (event.getType() == TYPE.ADDED) {
                            metadataEditorTableViewer.refresh();
                            List<IMetadataColumn> metadataColumns = (List<IMetadataColumn>) event.getAddedObjects();

                            lastCreatedInOutColumnEntries.clear();
                            int index = event.getIndex();
                            for (IMetadataColumn metadataColumn : metadataColumns) {
                                lastCreatedInOutColumnEntries.add(mapperManager
                                        .addNewColumnEntry(dataMapTableView, metadataColumn, index++));
                            }
                            refreshBackground(false, false);
                            dataMapTableView.changeSize(view.getPreferredSize(true, false, false), true, true);
                            dataMapTableViewer.refresh();
                            dataMapTableViewer.getTable().setSelection(event.getIndex());
                        }

                        if (event.getType() == TYPE.REMOVED) {
                            metadataEditorTableViewer.refresh();
                            List<IMetadataColumn> metadataColumns = (List<IMetadataColumn>) event.getRemovedObjects();
                            for (IMetadataColumn metadataColumn : metadataColumns) {
                                ITableEntry metadataTableEntry = mapperManager.retrieveTableEntry(new TableEntryLocation(
                                        abstractDataMapTable.getName(), metadataColumn.getLabel()));
                                mapperManager.removeTableEntry(metadataTableEntry);
                            }
                            dataMapTableViewer.refresh();
                            dataMapTableView.resizeAtExpandedSize();
                            resizeTablesZoneViewAtComputedSize(dataMapTableView.getZone());
                            moveScrollBarZoneAtSelectedTable(dataMapTableView.getZone());
                            refreshBackground(true, false);
                        }

                        if (event.getType() == TYPE.SWAPED) {
                            List<Integer> listIndexDestination = event.getIndexDestination();
                            abstractDataMapTable.swapColumnEntries(event.getIndexOrigin(), listIndexDestination);
                            // metadataEditorTableViewerCreator.refresh(false);
                            // metadataEditorTableReflect.refresh(event.swapedObject2, true);
                            // List<ITableEntry> metadataTableEntries = dataMapTable.getMetadataTableEntries();
                            // Object[] swapedObjects = event.swapedObjects;
                            // for (int i = 0; i < swapedObjects.length; i++) {
                            // dataMapTableViewer.refresh(swapedObjects[i]);
                            // }
                            dataMapTableViewer.refresh();
                            refreshBackground(true, false);
                        }

                    }

                });
                metadataTableEditorView.setMetadataTableEditor(metadataTableEditor);
                metadataTableEditorView.getTableViewerCreator().getTable()
                        .setSelection(dataMapTableViewer.getTable().getSelectionIndices());

                // disable highlight for other DataMapTableView and highlight selected DataMapTableView
                for (AbstractDataMapTable table : tables) {
                    DataMapTableView otherDataMapTableView = mapperManager.retrieveAbstractDataMapTableView(table);
                    otherDataMapTableView.setBackground(dataMapTableView.getDisplay().getSystemColor(SWT.COLOR_WIDGET_LIGHT_SHADOW));
                }
                dataMapTableView.setBackground(dataMapTableView.getDisplay().getSystemColor(SWT.COLOR_YELLOW));
            }

        }

    }

    /**
     * DOC amaumont Comment method "updateToolbarButtonsStates".
     * 
     * @param currentZone
     */
    public void updateToolbarButtonsStates(Zone currentZone) {
        ToolbarZone toolbar = null;
        if (currentZone == Zone.INPUTS) {
            toolbar = getInputsZone().getToolbar();
            toolbar.setEnabledMinimizeTablesButton(mapperManager.getInputsTablesView().size() > 0);
        } else if (currentZone == Zone.OUTPUTS) {
            toolbar = getOutputsZone().getToolbar();
            ((ToolbarOutputZone) toolbar).setEnabledRemoveTableButton(currentSelectedOutputTableView != null);
            toolbar.setEnabledMinimizeTablesButton(mapperManager.getOutputsTablesView().size() > 0);
        }
        toolbar.setEnabledMoveTableButton(true, isTableViewMoveable(currentZone, true));
        toolbar.setEnabledMoveTableButton(false, isTableViewMoveable(currentZone, false));
    }

    private void modifySelectionChangedListener(final Zone currentZone, final MetadataTableEditorView metadataTableEditorViewFinal,
            final TableViewer metadataEditorTableViewer, final MetadataTableEditor metadataTableEditor) {
        ISelectionChangedListener metadataEditorViewerSelectionChangedListener = new ISelectionChangedListener() {

            public void selectionChanged(SelectionChangedEvent event) {
                // System.out.println("Metadata editor selectionChanged");
                onSelectionChanged();
            }

            public void onSelectionChanged() {
                if (metadataTableEditorViewFinal.isExecuteSelectionEvent()) {
                    mapperManager.getUiManager().selectLinkedTableEntries(metadataTableEditor.getMetadataTable(),
                            metadataEditorTableViewer.getTable().getSelectionIndices());
                }
            }
        };
        ISelectionChangedListener previousSelectionChangedListener = null;
        if (currentZone == Zone.INPUTS) {
            previousSelectionChangedListener = inputsSelectionChangedListener;
        } else if (currentZone == Zone.OUTPUTS) {
            previousSelectionChangedListener = outputsSelectionChangedListener;
        }
        if (previousSelectionChangedListener != null) {
            metadataEditorTableViewer.removeSelectionChangedListener(previousSelectionChangedListener);
        }
        if (currentZone == Zone.INPUTS) {
            inputsSelectionChangedListener = metadataEditorViewerSelectionChangedListener;
        } else if (currentZone == Zone.OUTPUTS) {
            outputsSelectionChangedListener = metadataEditorViewerSelectionChangedListener;
        }
        metadataEditorTableViewer.addSelectionChangedListener(metadataEditorViewerSelectionChangedListener);
    }

    public void setMapperUI(MapperUI mapperUI) {
        this.mapperUI = mapperUI;
    }

    /**
     * DOC amaumont Comment method "refreshBackground".
     * 
     * @param firstExecutionAfterTime
     */
    public void refreshBackground(boolean forceRecalculate, boolean firstExecutionAfterTime) {
        if (forceRecalculate) {
            mapperUI.getBackgroundRefreshLimiterForceRecalculate().startIfExecutable(firstExecutionAfterTime);
        } else {
            mapperUI.getBackgroundRefreshLimiter().startIfExecutable(firstExecutionAfterTime);
        }
    }

    /**
     * DOC amaumont Comment method "refreshBackground".
     */
    public void refreshBackground(boolean forceRecalculate, int timeBeforeNewRefresh, boolean executeAfterTime) {
        if (forceRecalculate) {
            mapperUI.getBackgroundRefreshLimiterForceRecalculate().setTimeBeforeNewExecution(timeBeforeNewRefresh);
        } else {
            mapperUI.getBackgroundRefreshLimiter().setTimeBeforeNewExecution(timeBeforeNewRefresh);
        }
        refreshBackground(forceRecalculate, executeAfterTime);
    }

    public Composite getReferenceComposite() {
        if (this.refComposite == null) {
            this.refComposite = mapperUI.getVisualMapReferenceComposite();
        }
        return this.refComposite;
    }

    public int getVerticalScrolledOffsetBar(Zone zone) {
        switch (zone) {
        case INPUTS:
            return -mapperUI.getScollbarSelectionZoneInputs();

        case VARS:
            return -mapperUI.getScollbarSelectionZoneVars();

        case OUTPUTS:
            return -mapperUI.getScollbarSelectionZoneOutputs();

        default:
            throw new RuntimeException("The zone " + zone + " does'nt exist !");
        }
    }

    public ScrollBar getVerticalScrollBar(Zone zone) {
        switch (zone) {
        case INPUTS:
            return mapperUI.getScollbarZoneInputs();
        case VARS:
            return mapperUI.getScollbarZoneVars();
        case OUTPUTS:
            return mapperUI.getScollbarZoneOutputs();

        default:
            throw new RuntimeException("The zone " + zone + " does'nt exist !");
        }
    }

    public boolean isShiftPressed() {
        return shiftPressed;
    }

    public boolean isCtrlPressed() {
        return ctrlPressed;
    }

    public void setCtrlPressed(boolean ctrlPressed) {
        this.ctrlPressed = ctrlPressed;
    }

    public void setShiftPressed(boolean shiftPressed) {
        this.shiftPressed = shiftPressed;
    }

    /**
     * DOC amaumont Comment method "setDialogResponse".
     * 
     * @param ok
     */
    public void setMapperResponse(int mapperResponse) {
        this.mapperResponse = mapperResponse;
    }

    public int getMapperResponse() {
        return this.mapperResponse;
    }

    /**
     * DOC amaumont Comment method "closeMapperDialog".
     * 
     * @param ok
     */
    public void closeMapper(int mapperResponse) {
        setMapperResponse(mapperResponse);
        Composite parent = mapperUI.getMapperUIParent();
        saveCurrentUIProperties();

        if (mapperResponse == SWT.OK) {
            createVisualMapImage();
        }

        mapperManager.updateEmfParameters(EParameterName.PREVIEW.getName());
        
        if (parent instanceof Shell) {
            ((Shell) parent).close();
        }
    }

    /**
     * DOC amaumont Comment method "createVisualMapImage".
     */
    private void createVisualMapImage() {
        if (mapperManager.getPreviewFilePath() != null) {
            Image image = ImageCapture.capture(mapperUI.getDatasFlowViewSashForm());
            image = ImageUtils.scale(image, 50);
            ImageUtils.save(image, mapperManager.getPreviewFilePath(), SWT.IMAGE_BMP);
        }
    }

    /**
     * DOC amaumont Comment method "saveUiProperties".
     */
    private void saveCurrentUIProperties() {
        uiProperties = new ExternalMapperUiProperties();
        uiProperties.setWeightsMainSashForm(mapperUI.getMainSashForm().getWeights());
        uiProperties.setWeightsDatasFlowViewSashForm(mapperUI.getDatasFlowViewSashForm().getWeights());
        uiProperties.setShellMaximized(getMapperContainer().getShell().getMaximized());
        if (!uiProperties.isShellMaximized()) {
            uiProperties.setBoundsMapper(getMapperContainer().getBounds());
        }
    }

    /**
     * DOC amaumont Comment method "selectTableEntries".
     * 
     * @param metadataTable
     * @param selectedTableEntries
     * @param selectionIndices
     */
    public void selectLinkedTableEntries(IMetadataTable metadataTable, int[] selectionIndices) {
        DataMapTableView dataMapTableView = tableManager.getView(metadataTable);

        dataMapTableView.setTableSelection(selectionIndices);
        List<ITableEntry> list = extractSelectedTableEntries(dataMapTableView.getTableViewerCreatorForColumns().getTableViewer()
                .getSelection());
        processSelectedMetadataTableEntries(dataMapTableView, list, false);
    }

    /**
     * DOC amaumont Comment method "selectTableEntries".
     * 
     * @param view
     * @param selectedTableEntries
     * @param selectionIndices
     */
    public void selectLinkedMetadataEditorEntries(DataMapTableView view, int[] selectionIndices) {
        MetadataTableEditorView metadataTableEditorView = null;
        if (view.getZone() == Zone.INPUTS) {
            metadataTableEditorView = getInputMetaEditorView();
        } else if (view.getZone() == Zone.OUTPUTS) {
            metadataTableEditorView = getOutputMetaEditorView();
        }
        if (metadataTableEditorView != null) {
            metadataTableEditorView.setTableSelection(selectionIndices, false);
        }
    }

    /**
     * Highlight links and linked cells which have are referenced by the selected items.
     * 
     * @param dataMapTableView
     * @param selectedMetadataTableEntries
     * @param isConstraintsTableSelected TODO
     */
    public void processSelectedMetadataTableEntries(DataMapTableView dataMapTableView, List<ITableEntry> selectedMetadataTableEntries,
            boolean isConstraintsTableSelected) {

        UIManager uiManager = mapperManager.getUiManager();
        TableViewerCreator<ITableEntry> currentTableViewer = null;

        if (selectedMetadataTableEntries.size() > 0) {
            if (selectedMetadataTableEntries.get(0) instanceof ConstraintTableEntry) {
                currentTableViewer = dataMapTableView.getTableViewerCreatorForConstraints();
            } else if (selectedMetadataTableEntries.get(0) instanceof ITableEntry) {
                currentTableViewer = dataMapTableView.getTableViewerCreatorForColumns();
            } else {
                throw new IllegalArgumentException("Case not found");
            }
        }

        // Color selectedColor = dataMapTableView.getDisplay().getSystemColor(SWT.COLOR_YELLOW);
        Color unselectedColor = dataMapTableView.getDisplay().getSystemColor(SWT.COLOR_WHITE);

        Set<ITableEntry> hashSelectedMetadataTableEntries = new HashSet<ITableEntry>(selectedMetadataTableEntries);

        // ////////////////////////////////////////////////////////////////////////
        // Unselect all links and tableEntries if Ctrl or Shift keys are not pressed or if zone different of last
        // selection
        Zone currentZone = dataMapTableView.getZone();
        boolean zoneHasChanged = (previousSelectedZone == Zone.INPUTS || previousSelectedZone == Zone.VARS) && currentZone == Zone.OUTPUTS
                || (currentZone == Zone.INPUTS || currentZone == Zone.VARS) && previousSelectedZone == Zone.OUTPUTS;
        boolean tableTypeHasChanged = previousSelectedTableIsConstraint != isConstraintsTableSelected && currentZone == Zone.OUTPUTS;
        boolean resetHighlightObjectsForOtherTables = !uiManager.isDragging()
                && (!uiManager.isCtrlPressed() && !uiManager.isShiftPressed() || zoneHasChanged);
        if (resetHighlightObjectsForOtherTables) {
            for (IGraphicLink link : mapperManager.getLinks()) {
                if (!hashSelectedMetadataTableEntries.contains(link.getPointLinkDescriptorSource().getTableEntry())
                        && !hashSelectedMetadataTableEntries.contains(link.getPointLinkDescriptorTarget().getTableEntry())) {
                    link.setState(LinkState.UNSELECTED);
                    ITableEntry sourceITableEntry = link.getPointLinkDescriptorSource().getTableEntry();
                    TableItem tableItem = mapperManager.retrieveTableItem(sourceITableEntry);
                    tableItem.setBackground(unselectedColor);
                    ITableEntry targetITableEntry = link.getPointLinkDescriptorTarget().getTableEntry();
                    tableItem = mapperManager.retrieveTableItem(targetITableEntry);
                    tableItem.setBackground(unselectedColor);
                }
            }

            if (currentZone == Zone.INPUTS || currentZone == Zone.VARS) {
                unselectAllOutputMetaDataEntries();
            } else if (currentZone == Zone.OUTPUTS) {
                unselectAllInputMetaDataEntries();
            }

            Collection<DataMapTableView> tablesToDeselectEntries = mapperManager.getTablesView();
            for (DataMapTableView viewToDeselectEntries : tablesToDeselectEntries) {
                if (viewToDeselectEntries != dataMapTableView) {
                    viewToDeselectEntries.unselectAllEntries();
                } else if (viewToDeselectEntries == dataMapTableView && tableTypeHasChanged) {
                    if (isConstraintsTableSelected) {
                        viewToDeselectEntries.unselectAllColumnEntries();
                    } else {
                        viewToDeselectEntries.unselectAllConstraintEntries();
                    }
                }
            }
        }
        // ////////////////////////////////////////////////////////////////////////

        // ////////////////////////////////////////////////////////////////////////
        // Select or unselect links and tableEntries
        List<ITableEntry> allEntriesOfCurrentTableView = new ArrayList<ITableEntry>();
        if (currentTableViewer != null) {
            allEntriesOfCurrentTableView.addAll(currentTableViewer.getInputList());
        }
        int lstSize = allEntriesOfCurrentTableView.size();
        Set<IGraphicLink> linksAlreadySelected = new HashSet<IGraphicLink>();
        for (int i = 0; i < lstSize; i++) {
            ITableEntry entry = allEntriesOfCurrentTableView.get(i);
            Set<IGraphicLink> linksFromSource = mapperManager.getGraphicalLinksFromSource(entry);
            Set<IGraphicLink> linksFromTarget = mapperManager.getGraphicalLinksFromTarget(entry);
            LinkState linkState = null;
            if (hashSelectedMetadataTableEntries.contains(entry)) {
                linkState = LinkState.SELECTED;
            } else {
                linkState = LinkState.UNSELECTED;
            }
            for (IGraphicLink link : linksFromSource) {
                ITableEntry targetITableEntry = link.getPointLinkDescriptorTarget().getTableEntry();
                if (linkState == LinkState.SELECTED || !linksAlreadySelected.contains(link) && linkState == LinkState.UNSELECTED) {
                    link.setState(linkState);
                    if (linkState == LinkState.SELECTED) {
                        linksAlreadySelected.add(link);
                    }
                }
                EntryState entryState = (link.getState() == LinkState.SELECTED ? EntryState.HIGHLIGHT : EntryState.NONE);
                setEntryState(mapperManager, entryState, targetITableEntry);
            }
            for (IGraphicLink link : linksFromTarget) {
                ITableEntry sourceITableEntry = link.getPointLinkDescriptorSource().getTableEntry();
                if (linkState == LinkState.SELECTED || !linksAlreadySelected.contains(link) && linkState == LinkState.UNSELECTED) {
                    link.setState(linkState);
                    if (linkState == LinkState.SELECTED) {
                        linksAlreadySelected.add(link);
                    }
                }
                EntryState entryState = (link.getState() == LinkState.SELECTED ? EntryState.HIGHLIGHT : EntryState.NONE);
                setEntryState(mapperManager, entryState, sourceITableEntry);
            }
        }
        // ////////////////////////////////////////////////////////////////////////

        // order links to place selected links at last position (last drawn)
        mapperManager.orderLinks();

        uiManager.refreshBackground(false, false);

        previousSelectedZone = dataMapTableView.getZone();
        previousSelectedTableIsConstraint = isConstraintsTableSelected;
    }

    private void unselectAllInputMetaDataEntries() {
        getInputMetaEditorView().getTableViewerCreator().getTableViewer().getTable().deselectAll();
    }

    private void unselectAllOutputMetaDataEntries() {
        getOutputMetaEditorView().getTableViewerCreator().getTableViewer().getTable().deselectAll();
    }

    public void setEntryState(MapperManager mapperManager, EntryState entryState, ITableEntry entry) {
        TableItem tableItem = mapperManager.retrieveTableItem(entry);
        tableItem.setBackground(entryState.getColor());
    }

    /**
     * DOC amaumont Comment method "extractTableEntries".
     * 
     * @param selection
     * @return
     */
    public List<ITableEntry> extractSelectedTableEntries(ISelection selection) {
        StructuredSelection currentSelection = (StructuredSelection) selection;
        return (List<ITableEntry>) currentSelection.toList();
    }

    /**
     * DOC amaumont Comment method "getTableEntryPosition".
     * 
     * @param manager TODO
     * @param tableEntry
     * @param forceRecalculate TODO
     * @return
     */
    public Point getTableEntryPosition(ITableEntry tableEntry, boolean forceRecalculate) {
        TableEntryProperties tableEntryProperties = mapperManager.getTableEntryProperties(tableEntry);
        Point returnedPoint = tableEntryProperties.position;
        if (forceRecalculate || returnedPoint == null) {
            TableItem tableItem = mapperManager.retrieveTableItem(tableEntry);
            DataMapTableView dataMapTableView = mapperManager.retrieveDataMapTableView(tableEntry);
            Rectangle tableViewBounds = dataMapTableView.getBounds();
            Table table = tableItem.getParent();
            Rectangle boundsTableItem = tableItem.getBounds();

            int x = 0;
            int y = boundsTableItem.y + table.getItemHeight() / 2 + dataMapTableView.getBorderWidth();
            if (y < 0) {
                y = 0;
            }

            Point point = new Point(x, y);

            Display display = dataMapTableView.getDisplay();
            Point pointFromTableViewOrigin = display.map(tableItem.getParent(), dataMapTableView, point);

            if (pointFromTableViewOrigin.y > tableViewBounds.height - TableEntriesManager.HEIGHT_REACTION) {
                pointFromTableViewOrigin.y = tableViewBounds.height - TableEntriesManager.HEIGHT_REACTION;
            }

            returnedPoint = convertPointToReferenceOrigin(getReferenceComposite(), pointFromTableViewOrigin, dataMapTableView);
            tableEntryProperties.position = returnedPoint;
        }
        return returnedPoint;
    }

    public Point convertPointToReferenceOrigin(final Composite referenceComposite, Point point, Composite child) {
        Point returnedPoint = new Point(point.x, point.y);
        while (child != referenceComposite) {
            Rectangle bounds = child.getBounds();
            child = child.getParent();
            ScrollBar vScrollBar = child.getVerticalBar();
            if (vScrollBar != null) {
                returnedPoint.y += vScrollBar.getSelection();
            }
            returnedPoint.x += bounds.x;
            returnedPoint.y += bounds.y;
        }
        return returnedPoint;
    }

    public MetadataTableEditorView getInputMetaEditorView() {
        return mapperUI.getTabFolderEditors().getInputMetaEditor();
    }

    public MetadataTableEditorView getOutputMetaEditorView() {
        return mapperUI.getTabFolderEditors().getOutputMetaEditor();
    }

    /**
     * DOC amaumont Comment method "processNewExpression".
     * 
     * @param text
     * @param dataMapTableView
     * @param currentModifiedObject
     */
    public void processNewExpression(String expression, ITableEntry currentModifiedITableEntry) {
        if (processExpression(expression, currentModifiedITableEntry, true, true)) {
            mapperManager.getUiManager().refreshBackground(false, false);
        }

    }

    /**
     * DOC amaumont Comment method "processAllExpressions".
     */
    public void processAllExpressions(DataMapTableView dataMapTableView) {
        List<ITableEntry> columnsEntriesList = dataMapTableView.getTableViewerCreatorForColumns().getInputList();
        processAllExpressions(columnsEntriesList);
        if (dataMapTableView.getZone() == Zone.OUTPUTS) {
            List<ITableEntry> constraintEntriesList = dataMapTableView.getTableViewerCreatorForConstraints().getInputList();
            processAllExpressions(constraintEntriesList);
        }
    }

    private void processAllExpressions(List<ITableEntry> inputList) {
        for (ITableEntry entry : inputList) {
            processExpression(entry.getExpression(), entry, false, false);
        }
    }

    /**
     * 
     * 
     * @param expression
     * @param currentModifiedITableEntry
     * @param linkMustHaveSelectedState
     * @param checkInputKeyAutomatically TODO
     * @param dataMapTableView
     * @return true if a link has been added or removed, false else
     */
    @SuppressWarnings("deprecation")
    public boolean processExpression(String expression, ITableEntry currentModifiedITableEntry, boolean linkMustHaveSelectedState,
            boolean checkInputKeyAutomatically) {

        DataMapTableView dataMapTableView = mapperManager.retrieveDataMapTableView(currentModifiedITableEntry);
        boolean linkHasBeenAddedOrRemoved = false;

        DataMapExpressionParser dataMapExpressionParser = new DataMapExpressionParser(LanguageProvider.getCurrentLanguage());
        TableEntryLocation[] tableEntriesLocations = dataMapExpressionParser.parseTableEntryLocations(expression);
        Set<TableEntryLocation> alreadyProcessed = new HashSet<TableEntryLocation>();
        Set<ITableEntry> sourcesForTarget = mapperManager.getSourcesForTarget(currentModifiedITableEntry);
        Set<ITableEntry> sourcesForTargetToDelete = new HashSet<ITableEntry>(sourcesForTarget);

        // mapperManager.clearLinks();

        for (int i = 0; i < tableEntriesLocations.length; i++) {
            TableEntryLocation couple = tableEntriesLocations[i];
            if (!alreadyProcessed.contains(couple) && mapperManager.checkSourceLocationIsValid(couple, currentModifiedITableEntry)) {
                ITableEntry sourceITableEntry = mapperManager.retrieveTableEntry(couple);
                sourcesForTargetToDelete.remove(sourceITableEntry);
                if (sourceITableEntry != null && !sourcesForTarget.contains(sourceITableEntry)) {
                    DataMapTableView sourceDataMapTableView = mapperManager.retrieveDataMapTableView(sourceITableEntry);
                    AbstractLink link = new AbstractLink(new PointLinkDescriptor(sourceITableEntry, sourceDataMapTableView.getZone()),
                            new PointLinkDescriptor(currentModifiedITableEntry, dataMapTableView.getZone()), mapperManager);
                    link.setState(linkMustHaveSelectedState ? LinkState.SELECTED : LinkState.UNSELECTED);
                    mapperManager.addLink(link);
                    checkInputKey(currentModifiedITableEntry, dataMapTableView, checkInputKeyAutomatically);
                    linkHasBeenAddedOrRemoved = true;
                }
                alreadyProcessed.add(couple);
            }
        }

        Set<IGraphicLink> targets = mapperManager.getGraphicalLinksFromTarget(currentModifiedITableEntry);
        Set<IGraphicLink> linksFromTarget = new HashSet<IGraphicLink>(targets);
        for (IGraphicLink link : linksFromTarget) {
            if (sourcesForTargetToDelete.contains(link.getPointLinkDescriptorSource().getTableEntry())) {
                mapperManager.removeLink(link, link.getPointLinkDescriptorTarget().getTableEntry());
                linkHasBeenAddedOrRemoved = true;
            }
        }
        mapperManager.orderLinks();

        return linkHasBeenAddedOrRemoved;
    }

    private void checkInputKey(ITableEntry currentModifiedITableEntry, DataMapTableView dataMapTableView, boolean checkInputKeyAutomatically) {
        // check key
        if (checkInputKeyAutomatically && currentModifiedITableEntry instanceof InputColumnTableEntry) {
            IMetadataColumn metadataColumn = ((InputColumnTableEntry) currentModifiedITableEntry).getMetadataColumn();
            metadataColumn.setKey(true);
            dataMapTableView.getTableViewerCreatorForColumns().getTableViewer().refresh(currentModifiedITableEntry);
            IMetadataTable metadataTableTarget = ((InputTable) dataMapTableView.getDataMapTable()).getMetadataTable();
            MetadataTableEditorView metadataEditorView = getMetadataEditorView(dataMapTableView.getZone());
            if (metadataEditorView != null && metadataEditorView.getMetadataTableEditor() != null
                    && metadataEditorView.getMetadataTableEditor().getMetadataTable() == metadataTableTarget) {
                metadataEditorView.getTableViewerCreator().getTableViewer().refresh(metadataColumn);
                metadataEditorView.getTableViewerCreator().refreshTableEditorControls();
            }
        }
    }

    /**
     * DOC amaumont Comment method "processNewProcessColumnName".
     * 
     * @param text
     * @param dataMapTableView
     * @param entry
     */
    public void processColumnNameChanged(String newColumnName, DataMapTableView dataMapTableView, ITableEntry currentModifiedITableEntry) {
        mapperManager.changeColumnName(currentModifiedITableEntry, newColumnName);
        Collection<DataMapTableView> tableViews = mapperManager.getTablesView();
        for (DataMapTableView view : tableViews) {
            AbstractDataMapTable dataMapTable = view.getDataMapTable();
            List<IColumnEntry> metadataTableEntries = dataMapTable.getColumnEntries();
            for (IColumnEntry entry : metadataTableEntries) {
                processExpression(entry.getExpression(), entry, true, true);
            }
            if (dataMapTable instanceof OutputTable) {
                List<ConstraintTableEntry> constraintEntries = ((OutputTable) dataMapTable).getConstraintEntries();
                for (ConstraintTableEntry entry : constraintEntries) {
                    processExpression(entry.getExpression(), entry, true, true);
                }
            }
        }
        mapperManager.getUiManager().refreshBackground(false, false);
    }

    public OutputDataMapTableView createNewOutputTableView(Control previousControl, AbstractDataMapTable abstractDataMapTable,
            Composite parent) {
        OutputDataMapTableView dataMapTableView = new OutputDataMapTableView(parent, SWT.BORDER, abstractDataMapTable, mapperManager);
        FormData formData = new FormData();
        formData.left = new FormAttachment(0, 0);
        formData.right = new FormAttachment(100, 0);
        formData.top = new FormAttachment(previousControl);
        dataMapTableView.setLayoutData(formData);
        dataMapTableView.minimizeTable(abstractDataMapTable.isMinimized());
        dataMapTableView.registerStyledExpressionEditor(mapperUI.getTabFolderEditors().getStyledTextHandler());
        return dataMapTableView;
    }

    public TablesZoneView getTablesZoneViewOutputs() {
        return this.mapperUI.getOutputTablesZoneView();
    }

    public TablesZoneView getTablesZoneViewInputs() {
        return this.mapperUI.getInputTablesZoneView();
    }

    public TablesZoneView getTablesZoneViewVars() {
        return this.mapperUI.getVarsTableZoneView();
    }

    public ScrolledComposite getScrolledCompositeViewOutputs() {
        return this.mapperUI.getScrolledCompositeViewOutputs();
    }

    public ScrolledComposite getScrolledCompositeViewInputs() {
        return this.mapperUI.getScrolledCompositeViewInputs();
    }

    public InputsZone getInputsZone() {
        return this.mapperUI.getInputsZone();
    }

    public OutputsZone getOutputsZone() {
        return this.mapperUI.getOutputsZone();
    }

    /**
     * DOC amaumont Comment method "getMetadataEditorView".
     * 
     * @param dataMapTableView
     */
    public MetadataTableEditorView getMetadataEditorView(Zone zone) {
        if (zone == Zone.INPUTS) {
            return getInputMetaEditorView();
        }
        if (zone == Zone.OUTPUTS) {
            return getOutputMetaEditorView();
        }
        return null;
    }

    public List<IColumnEntry> getLastCreatedInOutColumnEntries() {
        return this.lastCreatedInOutColumnEntries;
    }

    public void clearLastCreatedInOutColumnEntries() {
        this.lastCreatedInOutColumnEntries.clear();
    }

    public Composite getMapperContainer() {
        return mapperUI.getMapperUIParent();
    }

    public ExternalMapperUiProperties getUiProperties() {
        if (this.uiProperties == null) {
            this.uiProperties = new ExternalMapperUiProperties();
        }
        return this.uiProperties;
    }

    public void setUiProperties(ExternalMapperUiProperties uiProperties) {
        this.uiProperties = uiProperties;
    }

    public OutputDataMapTableView getCurrentSelectedOutputTableView() {
        return this.currentSelectedOutputTableView;
    }

    public InputDataMapTableView getCurrentSelectedInputTableView() {
        return this.currentSelectedInputTableView;
    }

    public TabFolderEditors getTabFolderEditors() {
        return this.mapperUI.getTabFolderEditors();
    }

    /**
     * DOC amaumont Comment method "openAddNewOutputDialog".
     */
    public String openNewOutputCreationDialog() {
        final IProcess process = mapperManager.getConnector().getProcess();
        String outputName = process.generateUniqueConnectionName("newOutput");
        InputDialog id = new InputDialog(getMapperContainer().getShell(), "Add new output", //$NON-NLS-1$
                "Type a valid output name :", outputName, new IInputValidator() {

                    public String isValid(String newText) {
                        if (!process.checkValidConnectionName(newText)) {
                            return "The name of this connection is not valid or already exists";
                        }
                        return null;
                    }

                });
        int response = id.open();
        if (response == InputDialog.OK) {
            return id.getValue();
        }
        return null;
    }

    /**
     * DOC amaumont Comment method "getTablesZoneView".
     * 
     * @param dataMapTableViewTarget
     */
    public TablesZoneView getTablesZoneView(DataMapTableView dataMapTableViewTarget) {
        Zone zone = dataMapTableViewTarget.getZone();
        if (zone == Zone.OUTPUTS) {
            return getTablesZoneViewOutputs();
        } else if (zone == Zone.INPUTS) {
            return getTablesZoneViewInputs();
        } else if (zone == Zone.VARS) {
            return getTablesZoneViewVars();
        } else {
            throw new IllegalArgumentException("Case not found");
        }
    }

    public DropTargetOperationListener getDropTargetOperationListener() {
        return this.mapperUI.getDropTargetOperationListener();
    }

    public int getCurrentDragDetail() {
        return this.currentDragDetail;
    }

    public void setCurrentDragDetail(int currentDragDetail) {
        this.currentDragDetail = currentDragDetail;
    }

    public DraggingInfosPopup getDraggingInfosPopup() {
        return this.mapperUI.getDraggingInfosPopup();
    }

    public void moveOutputScrollBarZoneToMax() {
        ScrolledComposite scrolledCompositeViewOutputs = getScrolledCompositeViewOutputs();
        setPositionOfVerticalScrollBarZone(scrolledCompositeViewOutputs, scrolledCompositeViewOutputs.getVerticalBar().getMaximum());
    }

    public void moveScrollBarZoneAtSelectedTable(Zone zone) {
        if (zone == Zone.INPUTS) {
            moveInputScrollBarZoneAtSelectedTable();
        } else if (zone == Zone.OUTPUTS) {
            moveOutputScrollBarZoneAtSelectedTable();
        }
    }

    private void moveOutputScrollBarZoneAtSelectedTable() {
        Rectangle bounds = currentSelectedOutputTableView.getBounds();
        int selection = bounds.y - 30;
        ScrolledComposite scrolledCompositeViewOutputs = getScrolledCompositeViewOutputs();
        setPositionOfVerticalScrollBarZone(scrolledCompositeViewOutputs, selection);
    }

    private void moveInputScrollBarZoneAtSelectedTable() {
        Rectangle bounds = currentSelectedInputTableView.getBounds();
        int selection = bounds.y - 30;
        ScrolledComposite scrolledCompositeViewInputs = getScrolledCompositeViewInputs();
        setPositionOfVerticalScrollBarZone(scrolledCompositeViewInputs, selection);
    }

    private void setPositionOfVerticalScrollBarZone(ScrolledComposite scrollComposite, int scrollBarSelection) {
        ScrollBar verticalBar = scrollComposite.getVerticalBar();
        verticalBar.setSelection(scrollBarSelection);
        scrollComposite.setOrigin(0, scrollBarSelection);
    }

    /**
     * DOC amaumont Comment method "getDisplay".
     */
    public Display getDisplay() {
        return getMapperContainer().getDisplay();
    }

    /**
     * 
     * DOC amaumont Comment method "moveSelectedTable".
     * 
     * @param zone
     * @param moveUp true to move up, else false to move down
     */
    public void moveSelectedTable(Zone zone, boolean moveUp) {
        if (zone == Zone.INPUTS) {
            List<DataMapTableView> inputsTablesView = mapperManager.getInputsTablesView();
            if (moveUp) {
                moveSelectTableUp(currentSelectedInputTableView, inputsTablesView, 2);
            } else {
                moveSelectedTableDown(currentSelectedInputTableView, inputsTablesView, 1);
            }
            moveInputScrollBarZoneAtSelectedTable();
        } else if (zone == Zone.OUTPUTS) {
            List<DataMapTableView> outputsTablesView = mapperManager.getOutputsTablesView();
            if (moveUp) {
                moveSelectTableUp(currentSelectedOutputTableView, outputsTablesView, 1);
            } else {
                moveSelectedTableDown(currentSelectedOutputTableView, outputsTablesView, 0);
            }
            moveOutputScrollBarZoneAtSelectedTable();
        }
        updateToolbarButtonsStates(zone);
        refreshBackground(true, false);
    }

    public boolean isTableViewMoveable(Zone zone, boolean moveUp) {
        if (zone == Zone.INPUTS) {
            if(currentSelectedInputTableView == null) {
                return false;
            }
            List<DataMapTableView> tablesView = mapperManager.getInputsTablesView();
            int indexCurrentTable = tablesView.indexOf(currentSelectedInputTableView);
            if (moveUp) {
                if (indexCurrentTable > 1) {
                    return true;
                }
                return false;
            } else {
                if (indexCurrentTable < tablesView.size() - 1 && indexCurrentTable > 0) {
                    return true;
                }
                return false;
            }
        } else if (zone == Zone.OUTPUTS) {
            if(currentSelectedOutputTableView == null) {
                return false;
            }
            List<DataMapTableView> tablesView = mapperManager.getOutputsTablesView();
            int indexCurrentTable = tablesView.indexOf(currentSelectedOutputTableView);
            if (moveUp) {
                if (indexCurrentTable > 0) {
                    return true;
                }
                return false;
            } else {
                if (indexCurrentTable < tablesView.size() - 1) {
                    return true;
                }
                return false;
            }
        }
        return false;
    }

    /**
     * 
     * DOC amaumont Comment method "moveSelectTableUp".
     * 
     * @param currentSelectedTableView
     * @param tablesView
     * @param indexStartMovedAuthorized
     */
    private void moveSelectTableUp(DataMapTableView currentSelectedTableView, List<DataMapTableView> tablesView,
            int indexStartMovedAuthorized) {
        int indexCurrentTable = tablesView.indexOf(currentSelectedTableView);

        if (indexCurrentTable < indexStartMovedAuthorized) {
            return;
        }

        FormData formDataCurrent = (FormData) currentSelectedTableView.getLayoutData();
        DataMapTableView beforePreviousTableView = null;
        if (indexCurrentTable - 2 >= 0) {
            beforePreviousTableView = tablesView.get(indexCurrentTable - 2);
            formDataCurrent.top.control = beforePreviousTableView;
        } else {
            formDataCurrent.top.control = null;
        }

        DataMapTableView previousTableView = null;
        if (indexCurrentTable - 1 >= 0) {
            previousTableView = tablesView.get(indexCurrentTable - 1);
            FormData formDataPrevious = (FormData) previousTableView.getLayoutData();
            formDataPrevious.top.control = currentSelectedTableView;
        }

        if (indexCurrentTable + 1 <= tablesView.size() - 1) {
            DataMapTableView nextTableView = tablesView.get(indexCurrentTable + 1);
            FormData formDataNext = (FormData) nextTableView.getLayoutData();
            formDataNext.top.control = previousTableView;
        }

        tableManager.swapWithPreviousTable(currentSelectedTableView.getDataMapTable());
        currentSelectedTableView.getParent().layout();
        processAllExpressions(currentSelectedTableView);
        processAllExpressions(previousTableView);
    }

    private void moveSelectedTableDown(DataMapTableView currentSelectedTableView, List<DataMapTableView> tablesView,
            int indexStartMovedAuthorized) {
        int indexCurrentTable = tablesView.indexOf(currentSelectedTableView);

        if (indexCurrentTable < indexStartMovedAuthorized || indexCurrentTable == tablesView.size() - 1) {
            return;
        }
        DataMapTableView nextTableView = tablesView.get(indexCurrentTable + 1);

        FormData formDataCurrent = (FormData) currentSelectedTableView.getLayoutData();
        formDataCurrent.top.control = nextTableView;

        if (indexCurrentTable + 2 <= tablesView.size() - 1) {
            DataMapTableView afterNextTableView = tablesView.get(indexCurrentTable + 2);
            FormData formDataAfterNext = (FormData) afterNextTableView.getLayoutData();
            formDataAfterNext.top.control = currentSelectedTableView;
        }

        FormData formDataNext = (FormData) nextTableView.getLayoutData();
        if (indexCurrentTable - 1 >= 0) {
            formDataNext.top.control = tablesView.get(indexCurrentTable - 1);
        } else {
            formDataNext.top.control = null;
        }

        tableManager.swapWithNextTable(currentSelectedTableView.getDataMapTable());

        currentSelectedTableView.getParent().layout();
        processAllExpressions(currentSelectedTableView);
        processAllExpressions(nextTableView);
    }

    public void minimizeAllTables(Zone zone, boolean minimize, ToolItem minimizeButton) {

        List<DataMapTableView> tablesView = null;
        TablesZoneView tablesZoneView = null;
        if (zone == Zone.INPUTS) {
            tablesView = mapperManager.getInputsTablesView();
            tablesZoneView = getTablesZoneViewInputs();
        } else if (zone == Zone.OUTPUTS) {
            tablesZoneView = getTablesZoneViewOutputs();
            tablesView = mapperManager.getOutputsTablesView();
        } else {
            throw new RuntimeException("Case not found:" + zone);
        }

        Layout layout = tablesZoneView.getLayout();
        tablesZoneView.setLayout(null);

        for (DataMapTableView view : tablesView) {
            view.minimizeTable(minimize);
        }

        tablesZoneView.setLayout(layout);
        tablesZoneView.layout();
        for (DataMapTableView view : tablesView) {
            view.layout();
        }
        resizeTablesZoneViewAtComputedSize(zone);
    }

    /**
     * recalculate parent size don't hide DataMapTableView when resized.
     */
    public void resizeTablesZoneViewAtComputedSize(Zone zone) {
        TablesZoneView tablesZoneView = null;
        if (zone == Zone.INPUTS) {
            tablesZoneView = getTablesZoneViewInputs();
        } else if (zone == Zone.OUTPUTS) {
            tablesZoneView = getTablesZoneViewOutputs();
        } else if (zone == Zone.VARS) {
            tablesZoneView = getTablesZoneViewVars();
        }
        tablesZoneView.setSize(tablesZoneView.computeSize(SWT.DEFAULT, SWT.DEFAULT));
    }

    /**
     * DOC amaumont Comment method "setDragging".
     * 
     * @param b
     */
    public void setDragging(boolean dragging) {
        this.dragging = dragging;
    }

    public boolean isDragging() {
        return this.dragging;
    }

    /**
     * Call mapperManager.removeSelectedOutput() to remove a table view.
     * 
     * @param dataMapTableViewToRemove
     */
    public void removeOutputTableView(DataMapTableView dataMapTableViewToRemove) {
        List<DataMapTableView> outputsTablesView = mapperManager.getOutputsTablesView();
        int sizeList = outputsTablesView.size();
        for (int i = 0; i < sizeList; i++) {
            Control control = outputsTablesView.get(i);
            if (control == dataMapTableViewToRemove && i < sizeList - 1 && i > 0) {
                FormData formData = (FormData) outputsTablesView.get(i + 1).getLayoutData();
                formData.top = new FormAttachment(outputsTablesView.get(i - 1));
                break;
            }
        }
        mapperManager.removeTablePair(dataMapTableViewToRemove);
        MetadataTableEditorView outputMetaEditorView = getOutputMetaEditorView();
        if (outputMetaEditorView.getMetadataTableEditor().getMetadataTable() == ((OutputTable) dataMapTableViewToRemove.getDataMapTable())
                .getMetadataTable()) {
            getOutputMetaEditorView().setMetadataTableEditor(null);
        }
        dataMapTableViewToRemove.dispose();
        dataMapTableViewToRemove = null;
        getTablesZoneViewOutputs().layout();
        refreshBackground(true, false);
        this.currentSelectedOutputTableView = null;
    }

}