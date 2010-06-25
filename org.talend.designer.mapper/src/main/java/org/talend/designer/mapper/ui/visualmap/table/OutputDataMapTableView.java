// ============================================================================
//
// Copyright (C) 2006-2010 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.designer.mapper.ui.visualmap.table;

import java.util.List;

import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.talend.commons.ui.image.EImage;
import org.talend.commons.ui.image.ImageProvider;
import org.talend.commons.ui.swt.advanced.dataeditor.commands.ExtendedTableRemoveCommand;
import org.talend.commons.ui.swt.extended.table.AbstractExtendedTableViewer;
import org.talend.commons.ui.swt.extended.table.ExtendedTableModel;
import org.talend.commons.ui.swt.tableviewer.TableViewerCreator;
import org.talend.commons.ui.swt.tableviewer.TableViewerCreatorColumn;
import org.talend.commons.ui.swt.tableviewer.TableViewerCreator.LAYOUT_MODE;
import org.talend.commons.ui.swt.tableviewer.behavior.DefaultCellModifier;
import org.talend.commons.ui.swt.tableviewer.behavior.DefaultTableLabelProvider;
import org.talend.commons.ui.swt.tableviewer.behavior.ITableCellValueModifiedListener;
import org.talend.commons.ui.swt.tableviewer.behavior.TableCellValueModifiedEvent;
import org.talend.commons.ui.swt.tableviewer.celleditor.ExtendedTextCellEditor;
import org.talend.commons.ui.swt.tableviewer.tableeditor.ButtonPushImageTableEditorContent;
import org.talend.commons.ui.ws.WindowSystem;
import org.talend.commons.utils.data.bean.IBeanPropertyAccessors;
import org.talend.core.model.metadata.IMetadataColumn;
import org.talend.designer.abstractmap.model.table.IDataMapTable;
import org.talend.designer.abstractmap.model.tableentry.ITableEntry;
import org.talend.designer.mapper.i18n.Messages;
import org.talend.designer.mapper.managers.MapperManager;
import org.talend.designer.mapper.managers.UIManager;
import org.talend.designer.mapper.model.table.AbstractInOutTable;
import org.talend.designer.mapper.model.table.OutputTable;
import org.talend.designer.mapper.model.tableentry.FilterTableEntry;
import org.talend.designer.mapper.model.tableentry.GlobalMapEntry;
import org.talend.designer.mapper.model.tableentry.OutputColumnTableEntry;
import org.talend.designer.mapper.ui.dnd.DragNDrop;
import org.talend.designer.mapper.ui.image.ImageProviderMapper;
import org.talend.designer.mapper.ui.visualmap.zone.Zone;

/**
 * DOC amaumont class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 */
public class OutputDataMapTableView extends DataMapTableView {

    private OutputTableCellModifier cellModifier;

    private ExtendedTextCellEditor expressionCellEditor;

    public OutputDataMapTableView(Composite parent, int style, IDataMapTable abstractDataMapTable, MapperManager mapperManager) {
        super(parent, style, abstractDataMapTable, mapperManager);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.mapper.ui.visualmap.table.DataMapTableView#createContent()
     */
    @Override
    protected void createContent() {
        createTableForColumns();
    }

    protected void createMapSettingTable() {

        ExtendedTableModel<GlobalMapEntry> tableMapSettingEntriesModel = ((OutputTable) abstractDataMapTable)
                .getTableMapSettingEntriesModel();

        extendedTableViewerForMapSetting = new AbstractExtendedTableViewer<GlobalMapEntry>(tableMapSettingEntriesModel,
                centerComposite) {

            @Override
            protected void createColumns(TableViewerCreator<GlobalMapEntry> tableViewerCreator, Table table) {
                initMapSettingColumns(tableViewerCreator);
            }

            @Override
            protected void setTableViewerCreatorOptions(TableViewerCreator<GlobalMapEntry> newTableViewerCreator) {
                super.setTableViewerCreatorOptions(newTableViewerCreator);
                newTableViewerCreator.setBorderVisible(false);
            }
        };

        if (tableMapSettingEntriesModel != null) {
            tableMapSettingEntriesModel.add(new GlobalMapEntry(abstractDataMapTable, OUTPUT_REJECT, null));
            tableMapSettingEntriesModel.add(new GlobalMapEntry(abstractDataMapTable, LOOK_UP_INNER_JOIN_REJECT, null));
        }

        mapSettingViewerCreator = extendedTableViewerForMapSetting.getTableViewerCreator();
        mapSettingTable = extendedTableViewerForMapSetting.getTable();
        tableForMapSettingGridData = new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1);
        mapSettingTable.setLayoutData(tableForMapSettingGridData);
        mapSettingTable.setHeaderVisible(true);
        mapSettingTable.setLinesVisible(true);

        boolean mappingSettingVisible = false;
        if (abstractDataMapTable instanceof AbstractInOutTable) {
            mappingSettingVisible = ((AbstractInOutTable) abstractDataMapTable).isActivateCondensedTool();
        }
        tableForMapSettingGridData.exclude = !mappingSettingVisible;
        mapSettingTable.setVisible(mappingSettingVisible);

        // mapSettingViewerCreator.setCellModifier(new TableCellModifier(mapSettingViewerCreator));

    }

    protected IBeanPropertyAccessors<GlobalMapEntry, Object> getMapSettingValueAccess(final ComboBoxCellEditor functComboBox) {
        return new IBeanPropertyAccessors<GlobalMapEntry, Object>() {

            public Object get(GlobalMapEntry bean) {
                functComboBox.setItems(new String[] { "true", "false" });
                IDataMapTable parent = bean.getParent();
                OutputTable outputTable = (OutputTable) parent;
                if (OUTPUT_REJECT.equals(bean.getName())) {
                    return String.valueOf(outputTable.isReject());
                } else if (LOOK_UP_INNER_JOIN_REJECT.equals(bean.getName())) {
                    return String.valueOf(outputTable.isRejectInnerJoin());
                }

                return "";
            }

            public void set(GlobalMapEntry bean, Object value) {
                if (value == null) {
                    return;
                }
                IDataMapTable parent = bean.getParent();
                OutputTable outputTable = (OutputTable) parent;
                Object previous = null;
                if (OUTPUT_REJECT.equals(bean.getName())) {
                    previous = outputTable.isReject();
                    outputTable.setReject(Boolean.valueOf(value.toString()));
                } else if (LOOK_UP_INNER_JOIN_REJECT.equals(bean.getName())) {
                    previous = outputTable.isRejectInnerJoin();
                    outputTable.setRejectInnerJoin(Boolean.valueOf(value.toString()));
                }

                refreshCondensedImage(outputTable, bean.getName(), previous);
            }
        };
    }

    private void refreshCondensedImage(OutputTable table, String option, Object previousValue) {
        if (OUTPUT_REJECT.equals(option)) {
            if (mapperManager.getDefaultSetting().get(OUTPUT_REJECT).equals(table.isReject())) {
                if (changedOptions > 0)
                    changedOptions--;
            } else if (mapperManager.getDefaultSetting().get(OUTPUT_REJECT).equals(previousValue)) {
                if (changedOptions < 4)
                    changedOptions++;
            }
        } else if (LOOK_UP_INNER_JOIN_REJECT.equals(option)) {
            if (mapperManager.getDefaultSetting().get(LOOK_UP_INNER_JOIN_REJECT).equals(table.isRejectInnerJoin())) {
                if (changedOptions > 0)
                    changedOptions--;
            } else if (mapperManager.getDefaultSetting().get(LOOK_UP_INNER_JOIN_REJECT).equals(previousValue)) {
                if (changedOptions < 4)
                    changedOptions++;
            }
        }
        condensedItem.setImage(ImageProviderMapper.getImage(getCondencedItemImage(changedOptions)));
    }

    protected boolean needColumnBgColor(GlobalMapEntry bean) {
        OutputTable outputTable = (OutputTable) bean.getParent();

        if (OUTPUT_REJECT.equals(bean.getName())) {
            if (!mapperManager.getDefaultSetting().get(OUTPUT_REJECT).equals(outputTable.isReject())) {
                return true;
            }
        } else if (LOOK_UP_INNER_JOIN_REJECT.equals(bean.getName())) {
            if (!mapperManager.getDefaultSetting().get(LOOK_UP_INNER_JOIN_REJECT).equals(outputTable.isRejectInnerJoin())) {
                return true;
            }
        }
        return false;
    }

    protected void initCondensedItemImage() {
        if (!mapperManager.getDefaultSetting().get(OUTPUT_REJECT).equals(getOutputTable().isReject())) {
            if (changedOptions < 2)
                changedOptions++;
        }
        if (!mapperManager.getDefaultSetting().get(LOOK_UP_INNER_JOIN_REJECT).equals(getOutputTable().isRejectInnerJoin())) {
            if (changedOptions < 2)
                changedOptions++;
        }

        condensedItem.setImage(ImageProviderMapper.getImage(getCondencedItemImage(changedOptions)));

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.mapper.ui.visualmap.table.DataMapTableView#notifyFocusLost()
     */
    @Override
    public void notifyFocusLost() {
        if (expressionCellEditor != null) {
            expressionCellEditor.focusLost();
        }
    }

    @Override
    public void initColumnsOfTableColumns(final TableViewerCreator tableViewerCreatorForColumns) {
        TableViewerCreatorColumn column = new TableViewerCreatorColumn(tableViewerCreatorForColumns);
        column.setTitle(Messages.getString("OutputDataMapTableView.columnTitle.expression")); //$NON-NLS-1$
        column.setId(DataMapTableView.ID_EXPRESSION_COLUMN);
        expressionCellEditor = createExpressionCellEditor(tableViewerCreatorForColumns, column, new Zone[] { Zone.INPUTS,
                Zone.VARS }, false);
        column.setBeanPropertyAccessors(new IBeanPropertyAccessors<OutputColumnTableEntry, String>() {

            public String get(OutputColumnTableEntry bean) {
                return bean.getExpression();
            }

            public void set(OutputColumnTableEntry bean, String value) {
                bean.setExpression(value);
                mapperManager.getProblemsManager().checkProblemsForTableEntry(bean, true);
                tableViewerCreatorForColumns.getTableViewer().refresh(bean);
            }

        });
        column.setModifiable(!mapperManager.componentIsReadOnly());
        column.setDefaultInternalValue(""); //$NON-NLS-1$
        column.setWeight(COLUMN_EXPRESSION_SIZE_WEIGHT);

        column = new TableViewerCreatorColumn(tableViewerCreatorForColumns);
        column.setTitle(DataMapTableView.COLUMN_NAME);
        column.setId(DataMapTableView.ID_NAME_COLUMN);
        column.setBeanPropertyAccessors(new IBeanPropertyAccessors<OutputColumnTableEntry, String>() {

            public String get(OutputColumnTableEntry bean) {
                return bean.getMetadataColumn().getLabel();
            }

            public void set(OutputColumnTableEntry bean, String value) {
                bean.getMetadataColumn().setLabel(value);
            }

        });
        column.setWeight(COLUMN_NAME_SIZE_WEIGHT);

        configureCellModifier(tableViewerCreatorForColumns);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.mapper.ui.visualmap.table.DataMapTableView#initTableConstraints()
     */
    @Override
    protected void initExtraTable() {
        createFiltersTable();

        List<FilterTableEntry> entries = ((OutputTable) getDataMapTable()).getFilterEntries();

        // correct partially layout problem with GTK when cell editor value is
        // applied
        tableViewerCreatorForFilters.setAdjustWidthValue(WindowSystem.isGTK() ? -20 : ADJUST_WIDTH_VALUE);

        tableViewerCreatorForFilters.init(entries);
        updateGridDataHeightForTableConstraints();

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.mapper.ui.visualmap.table.DataMapTableView#addEntriesActionsComponents()
     */
    @Override
    protected boolean addToolItems() {
        createFiltersToolItems();
        // addToolItemSeparator();
        // createToolItems();
        return true;
    }

    protected void createFiltersTable() {

        this.extendedTableViewerForFilters = new AbstractExtendedTableViewer<FilterTableEntry>(
                ((OutputTable) abstractDataMapTable).getTableFiltersEntriesModel(), centerComposite) {

            @Override
            protected void createColumns(TableViewerCreator<FilterTableEntry> tableViewerCreator, Table table) {
                createFiltersColumns(tableViewerCreator);
            }

            /*
             * (non-Javadoc)
             * 
             * @see
             * org.talend.commons.ui.swt.extended.macrotable.AbstractExtendedTableViewer#setTableViewerCreatorOptions
             * (org.talend.commons.ui.swt.tableviewer.TableViewerCreator)
             */
            @Override
            protected void setTableViewerCreatorOptions(TableViewerCreator<FilterTableEntry> newTableViewerCreator) {
                super.setTableViewerCreatorOptions(newTableViewerCreator);
                newTableViewerCreator.setColumnsResizableByDefault(false);
                newTableViewerCreator.setBorderVisible(false);
                newTableViewerCreator.setLayoutMode(LAYOUT_MODE.FILL_HORIZONTAL);
                newTableViewerCreator.setKeyboardManagementForCellEdition(true);
                // tableViewerCreatorForColumns.setUseCustomItemColoring(this.getDataMapTable()
                // instanceof
                // AbstractInOutTable);
                newTableViewerCreator.setFirstColumnMasked(true);

            }

        };
        tableViewerCreatorForFilters = this.extendedTableViewerForFilters.getTableViewerCreator();
        this.extendedTableViewerForFilters.setCommandStack(mapperManager.getCommandStack());

        tableForConstraints = tableViewerCreatorForFilters.getTable();
        tableForConstraintsGridData = new GridData(GridData.FILL_HORIZONTAL);
        tableForConstraints.setLayoutData(tableForConstraintsGridData);

        boolean tableConstraintsVisible = false;
        if (abstractDataMapTable instanceof OutputTable) {
            tableConstraintsVisible = ((OutputTable) abstractDataMapTable).getFilterEntries().size() > 0;
        }

        tableForConstraintsGridData.exclude = !tableConstraintsVisible;
        tableForConstraints.setVisible(tableConstraintsVisible);

        if (!mapperManager.componentIsReadOnly()) {
            new DragNDrop(mapperManager, tableForConstraints, false, true);
        }

        tableViewerCreatorForFilters.addCellValueModifiedListener(new ITableCellValueModifiedListener() {

            public void cellValueModified(TableCellValueModifiedEvent e) {
                unselectAllEntriesIfErrorDetected(e);
            }
        });

        final TableViewer tableViewer = tableViewerCreatorForFilters.getTableViewer();

        tableViewer.addSelectionChangedListener(new ISelectionChangedListener() {

            public void selectionChanged(SelectionChangedEvent event) {
                ISelection selection = event.getSelection();
                selectThisDataMapTableView();
                UIManager uiManager = mapperManager.getUiManager();
                uiManager
                        .selectLinks(OutputDataMapTableView.this, uiManager.extractSelectedTableEntries(selection), false, false);
            }

        });

        tableForConstraints.addListener(SWT.KeyDown, new Listener() {

            public void handleEvent(Event event) {
                processEnterKeyDown(tableViewerCreatorForFilters, event);
            }

        });

        tableViewerCreatorForFilters.setLabelProvider(new DefaultTableLabelProvider(tableViewerCreatorForFilters) {

            @Override
            public Color getBackground(Object element, int columnIndex) {
                return getBackgroundCellColor(tableViewerCreator, element, columnIndex);
            }

            @Override
            public Color getForeground(Object element, int columnIndex) {
                return getForegroundCellColor(tableViewerCreator, element, columnIndex);
            }

        });

        initShowMessageErrorListener(tableForConstraints);
    }

    public void createFiltersColumns(final TableViewerCreator<FilterTableEntry> tableViewerCreatorForFilters) {
        TableViewerCreatorColumn column = new TableViewerCreatorColumn(tableViewerCreatorForFilters);
        column.setTitle(Messages.getString("OutputDataMapTableView.columnTitle.filterCondition")); //$NON-NLS-1$
        column.setId(DataMapTableView.ID_EXPRESSION_COLUMN);
        final ExtendedTextCellEditor expressionCellEditor = createExpressionCellEditor(tableViewerCreatorForFilters, column,
                new Zone[] { Zone.INPUTS, Zone.VARS }, true);
        column.setBeanPropertyAccessors(new IBeanPropertyAccessors<FilterTableEntry, String>() {

            public String get(FilterTableEntry bean) {
                return bean.getExpression();
            }

            public void set(FilterTableEntry bean, String value) {
                bean.setExpression(value);
                mapperManager.getProblemsManager().checkProblemsForTableEntry(bean, true);
            }

        });
        column.setModifiable(true);
        column.setDefaultInternalValue(""); //$NON-NLS-1$
        column.setWeight(99);
        column.setMoveable(false);
        column.setResizable(false);

        // Column with remove button
        column = new TableViewerCreatorColumn(tableViewerCreatorForFilters);
        column.setTitle(""); //$NON-NLS-1$
        column.setDefaultDisplayedValue(""); //$NON-NLS-1$
        column.setWidth(16);
        column.setMoveable(false);
        column.setResizable(false);
        ButtonPushImageTableEditorContent buttonImage = new ButtonPushImageTableEditorContent() {

            /*
             * (non-Javadoc)
             * 
             * @see
             * org.talend.commons.ui.swt.tableviewer.tableeditor.ButtonImageTableEditorContent#selectionEvent(org.talend
             * .commons.ui.swt.tableviewer.TableViewerCreatorColumn, java.lang.Object)
             */
            @Override
            protected void selectionEvent(TableViewerCreatorColumn column, Object bean) {
                ExtendedTableRemoveCommand removeCommand = new ExtendedTableRemoveCommand(bean, extendedTableViewerForFilters
                        .getExtendedTableModel());
                mapperManager.removeTableEntry((ITableEntry) bean);
                mapperManager.executeCommand(removeCommand);
                tableViewerCreatorForFilters.getTableViewer().refresh();
                List list = tableViewerCreatorForFilters.getInputList();
                updateGridDataHeightForTableConstraints();
                if (list != null && list.size() == 0) {
                    showTableConstraints(false);
                } else {
                    showTableConstraints(true);
                }

            }

        };
        buttonImage.setImage(ImageProvider.getImage(EImage.MINUS_ICON));
        column.setTableEditorContent(buttonImage);

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.mapper.ui.visualmap.table.DataMapTableView#getZone()
     */
    @Override
    public Zone getZone() {
        return Zone.OUTPUTS;
    }

    @Override
    public void unselectAllConstraintEntries() {
        tableViewerCreatorForFilters.getSelectionHelper().deselectAll();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.mapper.ui.visualmap.table.DataMapTableView#toolbarNeededToBeRightStyle()
     */
    @Override
    public boolean toolbarNeedToHaveRightStyle() {
        return false;
    }

    public OutputTable getOutputTable() {
        return (OutputTable) abstractDataMapTable;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.mapper.ui.visualmap.table.DataMapTableView#hasDropDownToolBarItem()
     */
    @Override
    public boolean hasDropDownToolBarItem() {
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.mapper.ui.visualmap.table.DataMapTableView#getValidZonesForExpressionFilterField()
     */
    @Override
    protected Zone[] getValidZonesForExpressionFilterField() {
        return new Zone[] { Zone.INPUTS };
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.mapper.ui.visualmap.table.DataMapTableView#loaded()
     */
    @Override
    public void loaded() {
        super.loaded();
        configureExpressionFilter();
        checkChangementsAfterEntryModifiedOrAdded(false);
    }

    protected void configureCellModifier(TableViewerCreator<OutputColumnTableEntry> tableViewerCreator) {
        cellModifier = new OutputTableCellModifier(tableViewerCreator);
        tableViewerCreator.setCellModifier(cellModifier);
    }

    /**
     * 
     * 
     * $Id: OutputTableCellModifier.java
     * 
     */
    class OutputTableCellModifier extends DefaultCellModifier {

        /**
         * DOC wchen OutputTableCellModifier constructor comment.
         * 
         * @param tableViewerCreator
         */
        public OutputTableCellModifier(TableViewerCreator tableViewerCreator) {
            super(tableViewerCreator);
        }

        /*
         * (non-Javadoc)
         * 
         * @see org.talend.commons.ui.swt.tableviewer.behavior.DefaultCellModifier#canModify(java.lang.Object,
         * java.lang.String)
         */
        @Override
        public boolean canModify(Object element, String property) {
            if (element instanceof OutputColumnTableEntry) {
                OutputColumnTableEntry outputColumn = (OutputColumnTableEntry) element;
                if (outputColumn.getParent() instanceof OutputTable
                        && ((OutputTable) outputColumn.getParent()).isErrorRejectTable()) {
                    IMetadataColumn metadataColumn = outputColumn.getMetadataColumn();
                    if (metadataColumn != null
                            && (getMapperManager().ERROR_REJECT_MESSAGE.equals(metadataColumn.getLabel()) || getMapperManager().ERROR_REJECT_STACK_TRACE
                                    .equals(metadataColumn.getLabel()))) {
                        return false;

                    }

                }

            }

            return !mapperManager.componentIsReadOnly();

        }

    }

}
