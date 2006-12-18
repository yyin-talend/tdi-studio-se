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

import org.apache.commons.lang.ArrayUtils;
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
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
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.ui.swt.tableviewer.IModifiedBeanListener;
import org.talend.commons.ui.swt.tableviewer.ModifiedBeanEvent;
import org.talend.commons.ui.swt.tableviewer.TableViewerCreator;
import org.talend.commons.ui.swt.tableviewer.selection.ILineSelectionListener;
import org.talend.commons.ui.swt.tableviewer.selection.LineSelectionEvent;
import org.talend.commons.utils.data.list.IListenableListListener;
import org.talend.commons.utils.data.list.ListenableListEvent;
import org.talend.commons.utils.data.list.ListenableListEvent.TYPE;
import org.talend.commons.utils.image.ImageCapture;
import org.talend.commons.utils.image.ImageUtils;
import org.talend.commons.utils.threading.AsynchronousThreading;
import org.talend.core.model.metadata.IMetadataColumn;
import org.talend.core.model.metadata.IMetadataTable;
import org.talend.core.model.metadata.editor.MetadataTableEditor;
import org.talend.core.model.process.IProcess;
import org.talend.core.ui.metadata.editor.MetadataTableEditorView;
import org.talend.designer.core.model.components.EParameterName;
import org.talend.designer.mapper.external.data.ExternalMapperUiProperties;
import org.talend.designer.mapper.language.LanguageProvider;
import org.talend.designer.mapper.model.table.AbstractDataMapTable;
import org.talend.designer.mapper.model.table.AbstractInOutTable;
import org.talend.designer.mapper.model.table.OutputTable;
import org.talend.designer.mapper.model.tableentry.AbstractInOutTableEntry;
import org.talend.designer.mapper.model.tableentry.FilterTableEntry;
import org.talend.designer.mapper.model.tableentry.IColumnEntry;
import org.talend.designer.mapper.model.tableentry.ITableEntry;
import org.talend.designer.mapper.model.tableentry.InputColumnTableEntry;
import org.talend.designer.mapper.model.tableentry.TableEntryLocation;
import org.talend.designer.mapper.ui.MapperUI;
import org.talend.designer.mapper.ui.commands.DataMapTableViewSelectedCommand;
import org.talend.designer.mapper.ui.dnd.DraggingInfosPopup;
import org.talend.designer.mapper.ui.dnd.DropTargetOperationListener;
import org.talend.designer.mapper.ui.tabs.TabFolderEditors;
import org.talend.designer.mapper.ui.visualmap.TableEntryProperties;
import org.talend.designer.mapper.ui.visualmap.link.IMapperLink;
import org.talend.designer.mapper.ui.visualmap.link.Link;
import org.talend.designer.mapper.ui.visualmap.link.LinkState;
import org.talend.designer.mapper.ui.visualmap.link.PointLinkDescriptor;
import org.talend.designer.mapper.ui.visualmap.link.StyleLinkFactory;
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
import org.talend.designer.mapper.utils.ParseExpressionResult;

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

    private ILineSelectionListener inputsSelectionChangedListener;

    private ILineSelectionListener outputsSelectionChangedListener;

    private List<IColumnEntry> lastCreatedInOutColumnEntries = new ArrayList<IColumnEntry>();

    private ExternalMapperUiProperties uiProperties;

    private OutputDataMapTableView currentSelectedOutputTableView;

    private InputDataMapTableView currentSelectedInputTableView;

    private Zone previousSelectedZone;

    private boolean previousSelectedTableIsConstraint;

    int currentDragDetail;

    private boolean dragging;

    private DisposeListener commonMetadataDisposeListener;

    private StyleLinkFactory drawableLinkFactory;

    private IModifiedBeanListener<IMetadataColumn> inputModifiedBeanListener;

    private IModifiedBeanListener<IMetadataColumn> outputModifiedBeanListener;

    /**
     * DOC amaumont UIManager constructor comment.
     * 
     * @param tableManager
     * @param manager
     */
    public UIManager(MapperManager mapperManager, TableManager tableManager) {
        this.mapperManager = mapperManager;
        this.tableManager = tableManager;
        this.drawableLinkFactory = new StyleLinkFactory();
    }

    /**
     * Select a table view.
     * 
     * @param dataMapTableView
     * @param useNewCommand
     */
    public void selectDataMapTableView(final DataMapTableView dataMapTableView, boolean useNewCommand) {

        TabFolderEditors tabFolderEditors = mapperUI.getTabFolderEditors();
        // tabFolderEditors.setSelection(TabFolderEditors.INDEX_TAB_METADATA_EDITOR);
        MetadataTableEditorView metadataTableEditorView = null;
        final Zone currentZone = dataMapTableView.getZone();

        List<? extends AbstractDataMapTable> tables = null;

        DataMapTableView previousSelectedTableView = null;

        if (currentZone == Zone.INPUTS) {
            metadataTableEditorView = tabFolderEditors.getInputMetaEditor();
            tables = mapperManager.getInputTables();
            previousSelectedTableView = this.currentSelectedInputTableView;
            this.currentSelectedInputTableView = (InputDataMapTableView) dataMapTableView;
        } else if (currentZone == Zone.OUTPUTS) {
            metadataTableEditorView = tabFolderEditors.getOutputMetaEditor();
            tables = mapperManager.getOutputTables();
            previousSelectedTableView = this.currentSelectedOutputTableView;
            this.currentSelectedOutputTableView = (OutputDataMapTableView) dataMapTableView;
        }

        if (currentZone != Zone.VARS) {

            updateToolbarButtonsStates(currentZone);

            final AbstractInOutTable abstractDataMapTable = (AbstractInOutTable) mapperManager
                    .retrieveAbstractDataMapTable(dataMapTableView);
            MetadataTableEditor currentMetadataTableEditor = metadataTableEditorView.getMetadataTableEditor();
            final TableViewerCreator dataMapTVCreator = dataMapTableView.getTableViewerCreatorForColumns();
            final TableViewer dataMapTableViewer = dataMapTableView.getTableViewerCreatorForColumns().getTableViewer();
            if (currentMetadataTableEditor == null || currentMetadataTableEditor != null
                    && !currentMetadataTableEditor.getMetadataTable().equals(abstractDataMapTable.getMetadataTable())) {

                if (useNewCommand) {
                    DataMapTableViewSelectedCommand command = new DataMapTableViewSelectedCommand(this, previousSelectedTableView,
                            dataMapTableView);
                    mapperManager.executeCommand(command);
                }

                currentMetadataTableEditor = new MetadataTableEditor(abstractDataMapTable.getMetadataTable(), abstractDataMapTable
                        .getName());

                currentMetadataTableEditor.setModifiedBeanListenable(metadataTableEditorView.getTableViewerCreator());

                final MetadataTableEditorView metadataTableEditorViewFinal = metadataTableEditorView;
                final TableViewerCreator metadataTVCreator = metadataTableEditorViewFinal.getTableViewerCreator();
                final MetadataTableEditor metadataTableEditor = currentMetadataTableEditor;

                modifySelectionChangedListener(currentZone, metadataTableEditorViewFinal, metadataTVCreator, metadataTableEditor,
                        dataMapTableView);

                // init actions listeners for list which contains metadata
                metadataTableEditor.addAfterOperationListListener(new IListenableListListener() {

                    @SuppressWarnings("unchecked")
                    public void handleEvent(ListenableListEvent event) {

                        DataMapTableView view = mapperManager.retrieveAbstractDataMapTableView(abstractDataMapTable);
                        if (event.type == TYPE.ADDED) {
                            // metadataEditorTableViewer.refresh();
                            List<IMetadataColumn> metadataColumns = (List<IMetadataColumn>) event.addedObjects;

                            lastCreatedInOutColumnEntries.clear();
                            if (event.index != null) {
                                int index = event.index;
                                for (IMetadataColumn metadataColumn : metadataColumns) {
                                    lastCreatedInOutColumnEntries.add(mapperManager.addNewColumnEntry(dataMapTableView, metadataColumn,
                                            index++));
                                }
                            } else if (event.indicesTarget != null) {
                                List<Integer> indicesTarget = event.indicesTarget;
                                int lstSize = indicesTarget.size();
                                for (int i = 0; i < lstSize; i++) {
                                    Integer indice = indicesTarget.get(i);
                                    IMetadataColumn metadataColumn = metadataColumns.get(i);
                                    lastCreatedInOutColumnEntries.add(mapperManager.addNewColumnEntry(dataMapTableView, metadataColumn,
                                            indice));
                                }

                            } else {
                                throw new IllegalStateException("Case not found");
                            }
                            refreshBackground(false, false);
                            if (event.index != null) {
                                dataMapTableView.changeSize(view.getPreferredSize(true, false, false), true, true);
                                dataMapTableViewer.refresh();
                                dataMapTVCreator.getSelectionHelper().setSelection(event.index);
                            } else if (event.indicesTarget != null) {
                                dataMapTableViewer.refresh();
                                dataMapTableView.changeSize(view.getPreferredSize(false, true, false), true, true);
                                int[] selection = ArrayUtils.toPrimitive((Integer[]) event.indicesTarget.toArray(new Integer[0]));
                                dataMapTVCreator.getSelectionHelper().setSelection(selection);
                            }
                        }

                        if (event.type == TYPE.REMOVED) {
                            // metadataEditorTableViewer.refresh();
                            List<IMetadataColumn> metadataColumns = (List<IMetadataColumn>) event.removedObjects;
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

                        if (event.type == TYPE.SWAPED) {
                            List<Integer> listIndexTarget = event.indicesTarget;
                            abstractDataMapTable.swapColumnElements(event.indicesOrigin, listIndexTarget);
                            // dataMapTableViewer.refresh();
                            refreshBackground(true, false);
                        }

                    }

                });
//                metadataTableEditorView.getTableViewerCreator().getSelectionHelper().setActiveFireSelectionChanged(false);
                metadataTableEditorView.setMetadataTableEditor(metadataTableEditor);
                dataMapTVCreator.getSelectionHelper().setActiveFireSelectionChanged(false);
                metadataTableEditorView. getTableViewerCreator().getSelectionHelper().setSelection(dataMapTableViewer.getTable().getSelectionIndices());
                dataMapTVCreator.getSelectionHelper().setActiveFireSelectionChanged(true);
//                metadataTableEditorView.getTableViewerCreator().getSelectionHelper().setActiveFireSelectionChanged(true);

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
            final TableViewerCreator metadataTVCreator, final MetadataTableEditor metadataTableEditor,
            final DataMapTableView dataMapTableView) {
        // ISelectionChangedListener metadataEditorViewerSelectionChangedListener = new ISelectionChangedListener() {
        //
        // public void selectionChanged(SelectionChangedEvent event) {
        // // System.out.println("Metadata editor selectionChanged");
        // onSelectionChanged();
        // }
        //
        // public void onSelectionChanged() {
        // if (metadataTableEditorViewFinal.isExecuteSelectionEvent()) {
        // mapperManager.getUiManager().selectLinkedTableEntries(metadataTableEditor.getMetadataTable(),
        // metadataTVCreator.getTable().getSelectionIndices());
        // }
        // }
        // };

        final TableViewer dataMapTableViewer = dataMapTableView.getTableViewerCreatorForColumns().getTableViewer();

        IModifiedBeanListener<IMetadataColumn> modifiedBeanListener = new IModifiedBeanListener<IMetadataColumn>() {

            public void handleEvent(ModifiedBeanEvent<IMetadataColumn> event) {
                if (MetadataTableEditorView.ID_COLUMN_NAME.equals(event.column.getId()) && !event.previousValue.equals(event.newValue)) {
                    IMetadataColumn modifiedObject = (IMetadataColumn) event.bean;
                    if (modifiedObject != null) {
                        TableEntryLocation tableEntryLocation = new TableEntryLocation(dataMapTableView.getDataMapTable().getName(),
                                (String) event.previousValue);
                        final ITableEntry dataMapTableEntry = mapperManager.retrieveTableEntry(tableEntryLocation);
                        processColumnNameChanged((String) event.previousValue, (String) event.newValue, dataMapTableView, dataMapTableEntry);
                    }
                    // dataMapTableViewer.refresh(event.bean, true);
                    dataMapTableViewer.refresh(true);
                } else if (MetadataTableEditorView.ID_COLUMN_KEY.equals(event.column.getId())) {
                    dataMapTableViewer.refresh(true);
                }
            }

        };

        ILineSelectionListener metadataEditorViewerSelectionChangedListener = new ILineSelectionListener() {

            public void handle(LineSelectionEvent e) {
                // System.out.println("LineSelectionEvent");
                if (metadataTableEditorViewFinal.getExtendedTableViewer().isExecuteSelectionEvent()) {
                    mapperManager.getUiManager().selectLinkedTableEntries(metadataTableEditor.getMetadataTable(),
                            metadataTVCreator.getTable().getSelectionIndices());
                }
            }

        };

        // ISelectionChangedListener previousSelectionChangedListener = null;
        ILineSelectionListener previousSelectionChangedListener = null;
        IModifiedBeanListener<IMetadataColumn> previousModifiedBeanListener = null;
        if (currentZone == Zone.INPUTS) {
            previousSelectionChangedListener = inputsSelectionChangedListener;
            previousModifiedBeanListener = inputModifiedBeanListener;
        } else if (currentZone == Zone.OUTPUTS) {
            previousSelectionChangedListener = outputsSelectionChangedListener;
            previousModifiedBeanListener = outputModifiedBeanListener;
        }
        if (previousSelectionChangedListener != null) {
            // metadataTVCreator.removeSelectionChangedListener(previousSelectionChangedListener);
            metadataTVCreator.getSelectionHelper().removeAfterSelectionListener(previousSelectionChangedListener);
        }

        if (previousModifiedBeanListener != null) {
            metadataTableEditor.removeModifiedBeanListener(previousModifiedBeanListener);
        }

        if (currentZone == Zone.INPUTS) {
            inputsSelectionChangedListener = metadataEditorViewerSelectionChangedListener;
            inputModifiedBeanListener = modifiedBeanListener;
        } else if (currentZone == Zone.OUTPUTS) {
            outputsSelectionChangedListener = metadataEditorViewerSelectionChangedListener;
            outputModifiedBeanListener = modifiedBeanListener;
        }
        // metadataTVCreator.addSelectionChangedListener(metadataEditorViewerSelectionChangedListener);
        metadataTVCreator.getSelectionHelper().addAfterSelectionListener(metadataEditorViewerSelectionChangedListener);
        metadataTableEditor.addModifiedBeanListener(modifiedBeanListener);

        if (this.commonMetadataDisposeListener == null) {
            this.commonMetadataDisposeListener = new DisposeListener() {

                public void widgetDisposed(DisposeEvent e) {
                    if (inputsSelectionChangedListener != null) {
                        getMetadataEditorView(Zone.INPUTS).getTableViewerCreator().getSelectionHelper().removeAfterSelectionListener(
                                inputsSelectionChangedListener);
                    }
                    if (outputsSelectionChangedListener != null) {
                        getMetadataEditorView(Zone.OUTPUTS).getTableViewerCreator().getSelectionHelper().removeAfterSelectionListener(
                                outputsSelectionChangedListener);
                    }
                    if (inputModifiedBeanListener != null) {
                        MetadataTableEditor metadataTableEditor = getMetadataEditorView(Zone.INPUTS).getMetadataTableEditor();
                        if (metadataTableEditor != null) {
                            metadataTableEditor.removeModifiedBeanListener(inputModifiedBeanListener);
                        }
                    }
                    if (outputModifiedBeanListener != null) {
                        MetadataTableEditor metadataTableEditor = getMetadataEditorView(Zone.OUTPUTS).getMetadataTableEditor();
                        if (metadataTableEditor != null) {
                            metadataTableEditor.removeModifiedBeanListener(outputModifiedBeanListener);
                        }
                    }
                }
            };
            metadataTVCreator.getTable().addDisposeListener(this.commonMetadataDisposeListener);
        }

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
    public void closeMapper(int response) {
        setMapperResponse(response);
        Composite parent = mapperUI.getMapperUIParent();
        saveCurrentUIProperties();

        if (response == SWT.OK) {
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
        String previewFilePath = mapperManager.getPreviewFilePath();
        if (previewFilePath != null) {
            Image image = ImageCapture.capture(mapperUI.getDatasFlowViewSashForm());
            image = ImageUtils.scale(image, 50);
            ImageUtils.save(image, previewFilePath, SWT.IMAGE_BMP);
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

//        view.getTableViewerCreatorForColumns().getSelectionHelper().setActiveFireSelectionChanged(false);
//        metadataTableEditorView.getExtendedTableViewer().getTableViewerCreator().getSelectionHelper().setSelection(selectionIndices);
//        view.getTableViewerCreatorForColumns().getSelectionHelper().setActiveFireSelectionChanged(true);
        dataMapTableView.setTableSelection(selectionIndices);

        List<ITableEntry> list = extractSelectedTableEntries(dataMapTableView.getTableViewerCreatorForColumns().getTableViewer()
                .getSelection());
        processSelectedDataMapEntries(dataMapTableView, list, false);
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
            view.getTableViewerCreatorForColumns().getSelectionHelper().setActiveFireSelectionChanged(false);
            metadataTableEditorView.getExtendedTableViewer().getTableViewerCreator().getSelectionHelper().setSelection(selectionIndices);
            view.getTableViewerCreatorForColumns().getSelectionHelper().setActiveFireSelectionChanged(true);
        }
    }

    /**
     * Highlight links and linked cells which have are referenced by the selected items.
     * 
     * @param dataMapTableView
     * @param selectedMetadataTableEntries
     * @param isConstraintsTableSelected TODO
     */
    @SuppressWarnings("unchecked")
    public void processSelectedDataMapEntries(DataMapTableView dataMapTableView, List<ITableEntry> selectedMetadataTableEntries,
            boolean isConstraintsTableSelected) {

        UIManager uiManager = mapperManager.getUiManager();
        TableViewerCreator<ITableEntry> currentTableViewer = null;

        if (selectedMetadataTableEntries.size() > 0) {
            if (selectedMetadataTableEntries.get(0) instanceof FilterTableEntry) {
                currentTableViewer = dataMapTableView.getTableViewerCreatorForFilters();
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
            for (IMapperLink link : mapperManager.getLinks()) {
                if (!hashSelectedMetadataTableEntries.contains(link.getPointLinkDescriptor1().getTableEntry())
                        && !hashSelectedMetadataTableEntries.contains(link.getPointLinkDescriptor2().getTableEntry())) {
                    link.setState(LinkState.UNSELECTED);
                    ITableEntry sourceITableEntry = link.getPointLinkDescriptor1().getTableEntry();
                    TableItem tableItem = mapperManager.retrieveTableItem(sourceITableEntry);
                    tableItem.setBackground(unselectedColor);
                    ITableEntry targetITableEntry = link.getPointLinkDescriptor2().getTableEntry();
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
        Set<IMapperLink> linksAlreadySelected = new HashSet<IMapperLink>();
        for (int i = 0; i < lstSize; i++) {
            ITableEntry entry = allEntriesOfCurrentTableView.get(i);
            Set<IMapperLink> linksFromSource = mapperManager.getGraphicalLinksFromSource(entry);
            Set<IMapperLink> linksFromTarget = mapperManager.getGraphicalLinksFromTarget(entry);
            LinkState linkState = null;
            if (hashSelectedMetadataTableEntries.contains(entry)) {
                linkState = LinkState.SELECTED;
            } else {
                linkState = LinkState.UNSELECTED;
            }
            for (IMapperLink link : linksFromSource) {
                ITableEntry targetITableEntry = link.getPointLinkDescriptor2().getTableEntry();
                if (linkState == LinkState.SELECTED || !linksAlreadySelected.contains(link) && linkState == LinkState.UNSELECTED) {
                    link.setState(linkState);
                    if (linkState == LinkState.SELECTED) {
                        linksAlreadySelected.add(link);
                    }
                }
                EntryState entryState = (link.getState() == LinkState.SELECTED ? EntryState.HIGHLIGHT : EntryState.NONE);
                setEntryState(mapperManager, entryState, targetITableEntry);
            }
            for (IMapperLink link : linksFromTarget) {
                ITableEntry sourceITableEntry = link.getPointLinkDescriptor1().getTableEntry();
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
        getInputMetaEditorView().getTable().deselectAll();
    }

    private void unselectAllOutputMetaDataEntries() {
        getOutputMetaEditorView().getTable().deselectAll();
    }

    public void setEntryState(MapperManager pMapperManager, EntryState entryState, ITableEntry entry) {
        TableItem tableItem = pMapperManager.retrieveTableItem(entry);
        tableItem.setBackground(entryState.getColor());
    }

    /**
     * DOC amaumont Comment method "extractTableEntries".
     * 
     * @param selection
     * @return
     */
    @SuppressWarnings("unchecked")
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
     * @param appliedOrCanceled TODO
     * @param text
     * @param newSelectedDataMapTableView
     * @param currentModifiedObject
     */
    public void parseNewExpression(String expression, ITableEntry currentModifiedITableEntry, boolean appliedOrCanceled) {
        ParseExpressionResult result = parseExpression(expression, currentModifiedITableEntry, true, true, appliedOrCanceled);
        if (result.isAtLeastOneLinkHasBeenAddedOrRemoved()) {
            mapperManager.getUiManager().refreshBackground(false, false);
        }

    }

    /**
     * DOC amaumont Comment method "processAllExpressions".
     */
    @SuppressWarnings("unchecked")
    public void parseAllExpressionsForAllTables() {
        List<DataMapTableView> tablesView = tableManager.getInputsTablesView();
        tablesView.addAll(tableManager.getVarsTablesView());
        tablesView.addAll(tableManager.getOutputsTablesView());
        for (DataMapTableView view : tablesView) {
            parseAllExpressions(view);
        }
    }

    /**
     * DOC amaumont Comment method "processAllExpressions".
     */
    @SuppressWarnings("unchecked")
    public void parseAllExpressions(DataMapTableView dataMapTableView) {
        List<IColumnEntry> columnsEntriesList = dataMapTableView.getDataMapTable().getColumnEntries();
        parseAllExpressions(columnsEntriesList);
        if (dataMapTableView.getZone() == Zone.OUTPUTS) {
            List<ITableEntry> constraintEntriesList = dataMapTableView.getTableViewerCreatorForFilters().getInputList();
            parseAllExpressions(constraintEntriesList);
        }
    }

    private void parseAllExpressions(List<? extends ITableEntry> inputList) {
        for (ITableEntry entry : inputList) {
            parseExpression(entry.getExpression(), entry, false, false, false);
        }
    }

    /**
     * 
     * 
     * @param expression
     * @param currentModifiedITableEntry
     * @param linkMustHaveSelectedState
     * @param checkInputKeyAutomatically TODO
     * @param inputExpressionAppliedOrCanceled TODO
     * @param newSelectedDataMapTableView
     * @return true if a link has been added or removed, false else
     */
    public ParseExpressionResult parseExpression(String expression, ITableEntry currentModifiedITableEntry,
            boolean linkMustHaveSelectedState, boolean checkInputKeyAutomatically, boolean inputExpressionAppliedOrCanceled) {

        DataMapTableView dataMapTableView = mapperManager.retrieveDataMapTableView(currentModifiedITableEntry);
        boolean linkHasBeenAdded = false;
        boolean linkHasBeenRemoved = false;

        DataMapExpressionParser dataMapExpressionParser = new DataMapExpressionParser(LanguageProvider.getCurrentLanguage());
        TableEntryLocation[] tableEntriesLocationsSources = dataMapExpressionParser.parseTableEntryLocations(expression);
        Set<TableEntryLocation> alreadyProcessed = new HashSet<TableEntryLocation>();
        Set<ITableEntry> sourcesForTarget = mapperManager.getSourcesForTarget(currentModifiedITableEntry);
        Set<ITableEntry> sourcesForTargetToDelete = new HashSet<ITableEntry>(sourcesForTarget);

        for (int i = 0; i < tableEntriesLocationsSources.length; i++) {
            TableEntryLocation location = tableEntriesLocationsSources[i];
            if (!alreadyProcessed.contains(location) && mapperManager.checkSourceLocationIsValid(location, currentModifiedITableEntry)) {
                ITableEntry sourceTableEntry = mapperManager.retrieveTableEntry(location);
                sourcesForTargetToDelete.remove(sourceTableEntry);
                if (sourceTableEntry != null && !sourcesForTarget.contains(sourceTableEntry)) {
                    DataMapTableView sourceDataMapTableView = mapperManager.retrieveDataMapTableView(sourceTableEntry);
                    IMapperLink link = new Link(new PointLinkDescriptor(sourceTableEntry, sourceDataMapTableView.getZone()),
                            new PointLinkDescriptor(currentModifiedITableEntry, dataMapTableView.getZone()), mapperManager);
                    link.setState(linkMustHaveSelectedState ? LinkState.SELECTED : LinkState.UNSELECTED);
                    mapperManager.addLink(link);
                    linkHasBeenAdded = true;
                }
                alreadyProcessed.add(location);
            }
        }

        Set<IMapperLink> targets = mapperManager.getGraphicalLinksFromTarget(currentModifiedITableEntry);
        Set<IMapperLink> linksFromTarget = new HashSet<IMapperLink>(targets);
        for (IMapperLink link : linksFromTarget) {
            if (sourcesForTargetToDelete.contains(link.getPointLinkDescriptor1().getTableEntry())) {
                mapperManager.removeLink(link, link.getPointLinkDescriptor2().getTableEntry());
                linkHasBeenRemoved = true;
            }
        }
        mapperManager.orderLinks();

        if (dataMapTableView.getZone() == Zone.INPUTS) {
            if (linkHasBeenAdded || linkHasBeenRemoved) {
                checkTargetInputKey(currentModifiedITableEntry, checkInputKeyAutomatically, inputExpressionAppliedOrCanceled);
                if (inputExpressionAppliedOrCanceled) {
                    openChangeKeysDialog((InputDataMapTableView) dataMapTableView);
                }
            }
        }

        return new ParseExpressionResult(linkHasBeenAdded, linkHasBeenRemoved);
    }

    /**
     * DOC amaumont Comment method "removeInvalidKeys".
     * 
     * @param newSelectedDataMapTableView
     */
    private void removeInvalidInputKeys(InputDataMapTableView inputDataMapTableView) {
        List<IColumnEntry> targetTableEntries = inputDataMapTableView.getDataMapTable().getColumnEntries();
        for (IColumnEntry entry : targetTableEntries) {
            InputColumnTableEntry inputEntry = (InputColumnTableEntry) entry;
            if (!checkEntryHasValidKey(inputEntry)) {
                inputEntry.getMetadataColumn().setKey(false);
            }
        }
    }

    /**
     * DOC amaumont Comment method "checkEntryHasValidKey".
     * 
     * @param inputEntry
     */
    private boolean checkEntryHasValidKey(InputColumnTableEntry inputEntry) {
        return inputEntry.getMetadataColumn().isKey() && inputEntry.getExpression() == null
                || inputEntry.getExpression().trim().length() == 0;
    }

    /**
     * 
     * DOC amaumont Comment method "hasInvalidInputKeys".
     * 
     * @param newSelectedDataMapTableView
     * @return
     */
    private boolean hasInvalidInputExpressionKeys(InputDataMapTableView inputDataMapTableView) {

        if (inputDataMapTableView.getTableViewerCreatorForColumns() == null) {
            return false;
        }

        List<IColumnEntry> targetTableEntries = inputDataMapTableView.getDataMapTable().getColumnEntries();
        for (IColumnEntry entry : targetTableEntries) {
            InputColumnTableEntry inputEntry = (InputColumnTableEntry) entry;
            if (checkEntryHasValidKey(inputEntry)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 
     * DOC amaumont Comment method "checkTargetInputKey".
     * 
     * @param currentModifiedTableEntry
     * @param inputDataMapTableView
     * @param checkInputKeyAutomatically
     * @param appliedOrCanceled TODO
     */
    private void checkTargetInputKey(ITableEntry currentModifiedTableEntry, boolean checkInputKeyAutomatically, boolean appliedOrCanceled) {
        // check key
        if (checkInputKeyAutomatically && currentModifiedTableEntry instanceof InputColumnTableEntry) {

            IMetadataColumn metadataColumn = ((InputColumnTableEntry) currentModifiedTableEntry).getMetadataColumn();
            if (!metadataColumn.isKey()) {
                metadataColumn.setKey(true);
                refreshInOutTableAndMetaTable((AbstractInOutTableEntry) currentModifiedTableEntry);
            }
        }
    }

    /**
     * DOC amaumont Comment method "refreshInOutTableAndMetaTable".
     * 
     * @param currentModifiedTableEntry can be null
     */
    private void refreshInOutTableAndMetaTable(AbstractInOutTableEntry currentModifiedTableEntry) {
        DataMapTableView dataMapTableView = mapperManager.retrieveDataMapTableView(currentModifiedTableEntry);
        IMetadataTable metadataTableTarget = ((AbstractInOutTable) dataMapTableView.getDataMapTable()).getMetadataTable();
        dataMapTableView.getTableViewerCreatorForColumns().getTableViewer().refresh(currentModifiedTableEntry);
        MetadataTableEditorView metadataEditorView = getMetadataEditorView(dataMapTableView.getZone());
        if (metadataEditorView != null && metadataEditorView.getMetadataTableEditor() != null
                && metadataEditorView.getMetadataTableEditor().getMetadataTable() == metadataTableTarget) {
            metadataEditorView.getTableViewerCreator().getTableViewer().refresh(currentModifiedTableEntry.getMetadataColumn());
            metadataEditorView.getTableViewerCreator().refreshTableEditorControls();
        }
    }

    /**
     * 
     * DOC amaumont Comment method "refreshInOutTableAndMetaTable".
     * 
     * @param dataMapTableView
     */
    private void refreshInOutTableAndMetaTable(DataMapTableView dataMapTableView) {
        IMetadataTable metadataTableTarget = ((AbstractInOutTable) dataMapTableView.getDataMapTable()).getMetadataTable();
        dataMapTableView.getTableViewerCreatorForColumns().getTableViewer().refresh();
        MetadataTableEditorView metadataEditorView = getMetadataEditorView(dataMapTableView.getZone());
        if (metadataEditorView != null && metadataEditorView.getMetadataTableEditor() != null
                && metadataEditorView.getMetadataTableEditor().getMetadataTable() == metadataTableTarget) {
            metadataEditorView.getTableViewerCreator().getTableViewer().refresh();
            metadataEditorView.getTableViewerCreator().refreshTableEditorControls();
        }
    }

    /**
     * DOC amaumont Comment method "processNewProcessColumnName".
     * 
     * @param previousColumnName TODO
     * @param dataMapTableView
     * @param text
     * @param entry
     */
    public void processColumnNameChanged(final String previousColumnName, final String newColumnName,
            final DataMapTableView dataMapTableView, final ITableEntry currentModifiedITableEntry) {

        mapperManager.changeColumnName(currentModifiedITableEntry, previousColumnName, newColumnName);
        Collection<DataMapTableView> tableViews = mapperManager.getTablesView();
        boolean atLeastOneLinkHasBeenRemoved = false;
        for (DataMapTableView view : tableViews) {
            AbstractDataMapTable dataMapTable = view.getDataMapTable();
            List<IColumnEntry> metadataTableEntries = dataMapTable.getColumnEntries();
            for (IColumnEntry entry : metadataTableEntries) {
                if (parseExpression(entry.getExpression(), entry, true, true, false).isAtLeastOneLinkRemoved()) {
                    atLeastOneLinkHasBeenRemoved = true;
                }
            }
            if (dataMapTable instanceof OutputTable) {
                List<FilterTableEntry> constraintEntries = ((OutputTable) dataMapTable).getFilterEntries();
                for (FilterTableEntry entry : constraintEntries) {
                    if (parseExpression(entry.getExpression(), entry, true, true, false).isAtLeastOneLinkRemoved()) {
                        atLeastOneLinkHasBeenRemoved = true;
                    }
                }
            }
        }
        mapperManager.getUiManager().refreshBackground(false, false);
        dataMapTableView.getTableViewerCreatorForColumns().getTableViewer().refresh(currentModifiedITableEntry);

        if (atLeastOneLinkHasBeenRemoved) {
            new AsynchronousThreading(20, false, dataMapTableView.getDisplay(), new Runnable() {

                public void run() {

                    TableViewerCreator tableViewerCreatorForColumns = dataMapTableView.getTableViewerCreatorForColumns();
                    boolean propagate = MessageDialog.openQuestion(tableViewerCreatorForColumns.getTable().getShell(), "Propagate",
                            "Propagate changes to all related expressions in order to keep the links valid ?");
                    if (propagate) {
                        TableEntryLocation previousLocation = new TableEntryLocation(currentModifiedITableEntry.getParentName(),
                                previousColumnName);
                        TableEntryLocation newLocation = new TableEntryLocation(currentModifiedITableEntry.getParentName(), newColumnName);
                        mapperManager.replacePreviousLocationInAllExpressions(previousLocation, newLocation);
                    }
                }
            }).start();
        }
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
        // dataMapTableView.registerStyledExpressionEditor(mapperUI.getTabFolderEditors().getStyledTextHandler());
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
     * @param newSelectedDataMapTableView
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
        final IProcess process = mapperManager.getComponent().getProcess();
        String outputName = process.generateUniqueConnectionName("newOutput");
        InputDialog id = new InputDialog(getMapperContainer().getShell(), "Add a new output table", //$NON-NLS-1$
                "Type a valid output table name :", outputName, new IInputValidator() {

                    public String isValid(String newText) {
                        if (!process.checkValidConnectionName(newText)) {
                            return "The name of this connection/table is not valid or already exists";
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
     * DOC amaumont Comment method "openAddNewOutputDialog".
     * 
     * @param inputDataMapTableView
     */
    private void openChangeKeysDialog(final InputDataMapTableView inputDataMapTableView) {

        new AsynchronousThreading(50, false, inputDataMapTableView.getDisplay(), new Runnable() {

            public void run() {

                if (hasInvalidInputExpressionKeys(inputDataMapTableView)) {
                    if (MessageDialog.openConfirm(inputDataMapTableView.getShell(), "Remove invalid keys",
                            "Press [Ok] to remove invalid keys of the input table '" + inputDataMapTableView.getDataMapTable().getName()
                                    + "'")) {
                        removeInvalidInputKeys(inputDataMapTableView);
                    }
                    refreshInOutTableAndMetaTable(inputDataMapTableView);
                }

            }
        }).start();

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
            if (currentSelectedInputTableView == null) {
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
            if (currentSelectedOutputTableView == null) {
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
        parseAllExpressions(currentSelectedTableView);
        parseAllExpressions(previousTableView);
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
        parseAllExpressions(currentSelectedTableView);
        parseAllExpressions(nextTableView);
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

        try {
            tablesZoneView.setLayout(null);
            for (DataMapTableView view : tablesView) {
                view.minimizeTable(minimize);
            }
        } catch (RuntimeException e) {
            ExceptionHandler.process(e);
        } finally {
            tablesZoneView.setLayout(layout);
        }

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

    public void registerCustomPaint() {
        List<DataMapTableView> tablesView = mapperManager.getOutputsTablesView();
        tablesView.addAll(mapperManager.getInputsTablesView());
        for (DataMapTableView view : tablesView) {
            view.getTableViewerCreatorForColumns().setUseCustomItemColoring(false);
        }

    }

    public void unregisterCustomPaint() {
        List<DataMapTableView> tablesView = mapperManager.getOutputsTablesView();
        tablesView.addAll(mapperManager.getInputsTablesView());
        for (DataMapTableView view : tablesView) {
            view.getTableViewerCreatorForColumns().setUseCustomItemColoring(false);
        }
    }

    /**
     * Getter for drawableLinkFactory.
     * 
     * @return the drawableLinkFactory
     */
    public StyleLinkFactory getStyleLinkFactory() {
        return this.drawableLinkFactory;
    }

}
