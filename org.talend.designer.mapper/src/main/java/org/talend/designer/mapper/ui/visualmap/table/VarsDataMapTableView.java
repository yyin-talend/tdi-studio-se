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
package org.talend.designer.mapper.ui.visualmap.table;

import java.util.List;

import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.ToolItem;
import org.talend.commons.ui.image.EImage;
import org.talend.commons.ui.image.ImageProvider;
import org.talend.commons.ui.swt.advanced.dataeditor.commands.ExtendedTableMoveCommand;
import org.talend.commons.ui.swt.extended.table.AbstractExtendedTableViewer;
import org.talend.commons.ui.swt.tableviewer.TableViewerCreator;
import org.talend.commons.ui.swt.tableviewer.TableViewerCreatorColumn;
import org.talend.commons.ui.swt.tableviewer.TableViewerCreator.CELL_EDITOR_STATE;
import org.talend.commons.ui.swt.tableviewer.celleditor.DialogErrorForCellEditorListener;
import org.talend.commons.ui.swt.tableviewer.data.ModifiedObjectInfo;
import org.talend.commons.utils.data.bean.IBeanPropertyAccessors;
import org.talend.commons.utils.data.list.IListenableListListener;
import org.talend.commons.utils.data.list.ListenableListEvent;
import org.talend.core.ui.images.ECoreImage;
import org.talend.designer.mapper.managers.MapperManager;
import org.talend.designer.mapper.managers.UIManager;
import org.talend.designer.mapper.model.table.AbstractDataMapTable;
import org.talend.designer.mapper.model.table.VarsTable;
import org.talend.designer.mapper.model.tableentry.ITableEntry;
import org.talend.designer.mapper.model.tableentry.VarTableEntry;
import org.talend.designer.mapper.ui.visualmap.zone.Zone;

/**
 * DOC amaumont class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 */
public class VarsDataMapTableView extends DataMapTableView {

    protected ToolItem removeEntryItem;

    protected ToolItem moveUpEntryItem;

    protected ToolItem moveDownEntryItem;

    public VarsDataMapTableView(Composite parent, int style, AbstractDataMapTable abstractDataMapTable, MapperManager mapperManager) {
        super(parent, style, abstractDataMapTable, mapperManager);
    }

    @Override
    protected void addListeners() {
        super.addListeners();
        tableViewerCreatorForColumns.getTable().addSelectionListener(new SelectionListener() {

            public void widgetDefaultSelected(SelectionEvent e) {
            }

            public void widgetSelected(SelectionEvent e) {
                boolean atLeastOneItemIsSelected = tableViewerCreatorForColumns.getTable().getSelectionCount() > 0;
                removeEntryItem.setEnabled(atLeastOneItemIsSelected);
                moveUpEntryItem.setEnabled(atLeastOneItemIsSelected);
                moveDownEntryItem.setEnabled(atLeastOneItemIsSelected);
            }

        });

        getExtendedTableViewerForColumns().getExtendedTableModel().addAfterOperationListListener(new IListenableListListener() {

            public void handleEvent(ListenableListEvent event) {
                if (event.type == ListenableListEvent.TYPE.SWAPED) {
                    DataMapTableView varsDataMapTableView = mapperManager.retrieveDataMapTableView(getExtendedTableViewerForColumns()
                            .getTable());
                    UIManager uiManager = mapperManager.getUiManager();
                    uiManager.parseAllExpressions(varsDataMapTableView);
                    uiManager.refreshBackground(true, false);
                    List<ITableEntry> list = uiManager.extractSelectedTableEntries(varsDataMapTableView.getTableViewerCreatorForColumns()
                            .getTableViewer().getSelection());

                    uiManager.processSelectedDataMapEntries(varsDataMapTableView, list, false);
                }
            }

        });

    }

    @Override
    public void initColumnsOfTableColumns(final TableViewerCreator tableViewerCreatorForColumns) {
        TableViewerCreatorColumn column = new TableViewerCreatorColumn(tableViewerCreatorForColumns);
        column.setTitle("Expression");
        column.setId(DataMapTableView.ID_EXPRESSION_COLUMN);
        column.setBeanPropertyAccessors(new IBeanPropertyAccessors<VarTableEntry, String>() {

            public String get(VarTableEntry bean) {
                return bean.getExpression();
            }

            public void set(VarTableEntry bean, String value) {
                bean.setExpression(value);
            }

        });
        column.setModifiable(true);
        column.setDefaultInternalValue("");
        createExpressionCellEditor(tableViewerCreatorForColumns, column, new Zone[] { Zone.INPUTS, Zone.VARS }, false);
        column.setWeight(COLUMN_EXPRESSION_SIZE_WEIGHT);

        column = new TableViewerCreatorColumn(tableViewerCreatorForColumns);
        column.setTitle("Variable");
        column.setId(DataMapTableView.ID_NAME_COLUMN);
        column.setBeanPropertyAccessors(new IBeanPropertyAccessors<VarTableEntry, String>() {

            public String get(VarTableEntry bean) {
                return bean.getName();
            }

            public void set(VarTableEntry bean, String value) {
                bean.setName(value);
            }

        });
        column.setModifiable(true);
        column.setWeight(COLUMN_NAME_SIZE_WEIGHT);
        final TextCellEditor cellEditor = new TextCellEditor(tableViewerCreatorForColumns.getTable());
        cellEditor.addListener(new DialogErrorForCellEditorListener(cellEditor, column) {

            @Override
            public void newValidValueTyped(int itemIndex, Object previousValue, Object newValue, CELL_EDITOR_STATE state) {
                if (state == CELL_EDITOR_STATE.APPLYING) {
                    ModifiedObjectInfo modifiedObjectInfo = tableViewerCreatorForColumns.getModifiedObjectInfo();
                    String originalValue = (String) modifiedObjectInfo.getOriginalPropertyBeanValue();
                    Object currentModifiedBean = modifiedObjectInfo.getCurrentModifiedBean();
                    mapperManager.getUiManager().processColumnNameChanged(originalValue.toString(), newValue.toString(),
                            VarsDataMapTableView.this, (ITableEntry) currentModifiedBean);
                }
            }

            @Override
            public String validateValue(String newValue, int beanPosition) {
                return ((VarsTable) getDataMapTable()).validateColumnName(newValue, beanPosition);
            }

        });

        column.setCellEditor(cellEditor);

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.mapper.ui.visualmap.table.DataMapTableView#initTableConstraints()
     */
    @Override
    protected void initTableFilters() {
        // no table constraint
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.mapper.ui.visualmap.table.DataMapTableView#addEntriesActionsComponents()
     */
    @Override
    protected boolean addToolItems() {
        createToolItems();
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.mapper.ui.visualmap.table.DataMapTableView#getZone()
     */
    @Override
    public Zone getZone() {
        return Zone.VARS;
    }

    @Override
    public void unselectAllColumnEntries() {
        super.unselectAllColumnEntries();
        if (removeEntryItem != null) {
            removeEntryItem.setEnabled(false);
            moveUpEntryItem.setEnabled(false);
            moveDownEntryItem.setEnabled(false);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.mapper.ui.visualmap.table.DataMapTableView#toolbarNeededToBeRightStyle()
     */
    @Override
    public boolean toolbarNeededToBeRightStyle() {
        return false;
    }

    protected void createToolItems() {

        // /////////////////////////////////////////////////////////////////
        ToolItem addEntryItem = new ToolItem(toolBarActions, SWT.PUSH);
        addEntryItem.setToolTipText("Add variable");
        addEntryItem.setImage(org.talend.commons.ui.image.ImageProvider.getImage(org.talend.commons.ui.image.ImageProvider
                .getImageDesc(EImage.ADD_ICON)));

        addEntryItem.addSelectionListener(new SelectionListener() {

            public void widgetDefaultSelected(SelectionEvent e) {
            }

            public void widgetSelected(SelectionEvent e) {
                Table table = getExtendedTableViewerForColumns().getTable();

                int[] indices = table.getSelectionIndices();
                int indexInsert = table.getItemCount();
                if (indices.length > 0) {
                    indexInsert = indices[indices.length - 1] + 1;
                }
                AbstractDataMapTable dataMapTable = VarsDataMapTableView.this.getDataMapTable();
                String varName = null;
                if (dataMapTable instanceof VarsTable) {
                    varName = ((VarsTable) dataMapTable).findUniqueColumnName("var");
                } else {
                    throw new UnsupportedOperationException("Can't create new column, case not found");
                }
                mapperManager.addNewVarEntry(VarsDataMapTableView.this, varName, indexInsert);
                VarsDataMapTableView.this.changeSize(VarsDataMapTableView.this.getPreferredSize(true, true, false), true, true);
                changeMinimizeState(false);
                tableViewerCreatorForColumns.getTableViewer().refresh();
                mapperManager.getUiManager().refreshBackground(true, false);
                table.setSelection(indexInsert);
                removeEntryItem.setEnabled(true);
                moveUpEntryItem.setEnabled(true);
                moveDownEntryItem.setEnabled(true);
            }

        });
        // /////////////////////////////////////////////////////////////////

        // /////////////////////////////////////////////////////////////////
        removeEntryItem = new ToolItem(toolBarActions, SWT.PUSH);
        removeEntryItem.setEnabled(false);
        removeEntryItem.setImage(org.talend.commons.ui.image.ImageProvider.getImage(org.talend.commons.ui.image.ImageProvider
                .getImageDesc(EImage.MINUS_ICON)));
        removeEntryItem.setToolTipText("Remove selected variable(s)");

        removeEntryItem.addSelectionListener(new SelectionListener() {

            public void widgetDefaultSelected(SelectionEvent e) {
            }

            public void widgetSelected(SelectionEvent e) {
                TableItem[] tableItems = tableViewerCreatorForColumns.getTable().getSelection();
                for (int i = 0; i < tableItems.length; i++) {
                    TableItem item = tableItems[i];
                    mapperManager.removeTableEntry((ITableEntry) item.getData());
                }
                if (tableItems.length > 0) {
                    tableViewerCreatorForColumns.getTableViewer().refresh();
                    mapperManager.getUiManager().refreshBackground(true, false);
                    resizeAtExpandedSize();
                }
                removeEntryItem.setEnabled(false);
            }

        });
        // /////////////////////////////////////////////////////////////////

        // /////////////////////////////////////////////////////////////////
        moveUpEntryItem = new ToolItem(toolBarActions, SWT.PUSH);
        moveUpEntryItem.setEnabled(false);
        moveUpEntryItem.setImage(ImageProvider.getImage(EImage.UP_ICON));
        moveUpEntryItem.setToolTipText("Move up selected variable(s)");

        moveUpEntryItem.addSelectionListener(new SelectionListener() {

            public void widgetDefaultSelected(SelectionEvent e) {
            }

            public void widgetSelected(SelectionEvent e) {

                AbstractExtendedTableViewer viewer = (AbstractExtendedTableViewer) getExtendedTableViewerForColumns();
                ExtendedTableMoveCommand moveCommand = new ExtendedTableMoveCommand(viewer.getExtendedTableModel(), true, viewer
                        .getTableViewerCreator().getTable().getSelectionIndices());
                viewer.executeCommand(moveCommand);
            }

        });
        // /////////////////////////////////////////////////////////////////

        // /////////////////////////////////////////////////////////////////
        moveDownEntryItem = new ToolItem(toolBarActions, SWT.PUSH);
        moveDownEntryItem.setEnabled(false);
        moveDownEntryItem.setImage(ImageProvider.getImage(EImage.DOWN_ICON));
        moveDownEntryItem.setToolTipText("Move down selected variable(s)");

        moveDownEntryItem.addSelectionListener(new SelectionListener() {

            public void widgetDefaultSelected(SelectionEvent e) {
            }

            public void widgetSelected(SelectionEvent e) {

                AbstractExtendedTableViewer viewer = (AbstractExtendedTableViewer) getExtendedTableViewerForColumns();
                ExtendedTableMoveCommand moveCommand = new ExtendedTableMoveCommand(viewer.getExtendedTableModel(), false, viewer
                        .getTableViewerCreator().getTable().getSelectionIndices());
                viewer.executeCommand(moveCommand);
            }

        });
        // /////////////////////////////////////////////////////////////////

    }

}
