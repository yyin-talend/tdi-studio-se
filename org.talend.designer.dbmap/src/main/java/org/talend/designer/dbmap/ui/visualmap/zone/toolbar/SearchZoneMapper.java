// ============================================================================
//
// Copyright (C) 2006-2013 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.designer.dbmap.ui.visualmap.zone.toolbar;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.ScrollBar;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.dialogs.SearchPattern;
import org.talend.commons.ui.swt.tableviewer.TableViewerCreator;
import org.talend.designer.abstractmap.model.table.IDataMapTable;
import org.talend.designer.abstractmap.model.tableentry.IColumnEntry;
import org.talend.designer.abstractmap.model.tableentry.ITableEntry;
import org.talend.designer.dbmap.managers.MapperManager;
import org.talend.designer.dbmap.managers.UIManager;
import org.talend.designer.dbmap.model.table.InputTable;
import org.talend.designer.dbmap.model.table.OutputTable;
import org.talend.designer.dbmap.model.tableentry.FilterTableEntry;
import org.talend.designer.dbmap.ui.visualmap.table.EntryState;

/**
 * DOC hcyi class global comment. Detailled comment
 */
public class SearchZoneMapper {

    private MapperManager mapperManager;

    private final UIManager uiManager;

    private boolean isHightlightAll = false;

    public SearchZoneMapper(MapperManager mapperManager) {
        this.mapperManager = mapperManager;
        uiManager = mapperManager.getUiManager();
    }

    public void search(Map<Integer, ITableEntry> searchMaps, String searchValue) {
        if (searchValue.equals("")) {
            uiManager.unselectAllEntriesOfAllTables();
            return;
        }
        // SearchPattern
        SearchPattern matcher = new SearchPattern();
        matcher.setPattern("*" + searchValue.trim() + "*");

        List<InputTable> inputTables = mapperManager.getInputTables();
        List<OutputTable> outputTables = mapperManager.getOutputTables();

        int index = -1;

        // for the Lookup InputTables
        for (InputTable inputTable : inputTables) {
            for (IColumnEntry column : inputTable.getColumnEntries()) {
                if (column.getExpression() != null && matcher.matches(column.getExpression())) {
                    index++;
                    searchMaps.put(index, column);
                }
            }
        }

        // for the OutputTables
        for (OutputTable outputTable : outputTables) {
            // OutputTable Filters
            for (FilterTableEntry entry : outputTable.getFilterEntries()) {
                if (entry.getExpression() != null && matcher.matches(entry.getExpression())) {
                    index++;
                    searchMaps.put(index, entry);
                }
            }
            // OutputTable Columns
            for (IColumnEntry column : outputTable.getColumnEntries()) {
                if (column.getExpression() != null && matcher.matches(column.getExpression())) {
                    index++;
                    searchMaps.put(index, column);
                }
            }
        }
    }

    public Integer selectHightlight(Map<Integer, ITableEntry> searchMaps, Integer selectKey, String option) {
        if (searchMaps.containsKey(selectKey)) {
            if (option.equals("next") && selectKey + 1 < searchMaps.size()) {
                if (isHightlightAll) {
                    setEntryState(mapperManager, EntryState.HIGHLIGHTALL, searchMaps.get(selectKey));
                    setEntryState(mapperManager, EntryState.SEARCH_HIGHLIGHT, searchMaps.get(selectKey + 1));
                } else {
                    setEntryState(mapperManager, EntryState.NONE, searchMaps.get(selectKey));
                    setEntryState(mapperManager, EntryState.SEARCH_HIGHLIGHT, searchMaps.get(selectKey + 1));
                }
                // move scrollBarZone at selected TableItem
                moveScrollBarZoneAtSelectedTableItem(searchMaps.get(selectKey + 1));
                return selectKey + 1;
            } else if (option.equals("previous") && selectKey > 0) {
                if (isHightlightAll) {
                    setEntryState(mapperManager, EntryState.HIGHLIGHTALL, searchMaps.get(selectKey));
                    setEntryState(mapperManager, EntryState.SEARCH_HIGHLIGHT, searchMaps.get(selectKey - 1));
                } else {
                    setEntryState(mapperManager, EntryState.NONE, searchMaps.get(selectKey));
                    setEntryState(mapperManager, EntryState.SEARCH_HIGHLIGHT, searchMaps.get(selectKey - 1));
                }
                // move scrollBarZone at selected TableItem
                moveScrollBarZoneAtSelectedTableItem(searchMaps.get(selectKey - 1));
                return selectKey - 1;
            } else if (option.equals("first")) {
                setEntryState(mapperManager, EntryState.SEARCH_HIGHLIGHT, searchMaps.get(0));
                // move scrollBarZone at selected TableItem
                moveScrollBarZoneAtSelectedTableItem(searchMaps.get(0));
                return 0;
            }
        }
        return selectKey;
    }

    public void hightlightAll(Map<Integer, ITableEntry> searchMaps, boolean isHightlight) {
        Iterator iter = searchMaps.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            if (isHightlight) {
                setEntryState(mapperManager, EntryState.HIGHLIGHTALL, (ITableEntry) entry.getValue());
            } else {
                setEntryState(mapperManager, EntryState.NONE, (ITableEntry) entry.getValue());
            }
        }
    }

    public void setEntryState(MapperManager mapperManager, EntryState entryState, ITableEntry entry) {
        if (entry != null && entry.getParent() instanceof InputTable) {
            TableItem tableItem = mapperManager.retrieveTableItem(entry);
            tableItem.setBackground(4, entryState.getColor());
        } else {
            TableItem tableItem = mapperManager.retrieveTableItem(entry);
            // tableItem.setForeground(1, entryState.getColor());
            tableItem.setBackground(1, entryState.getColor());
        }
    }

    public void moveScrollBarZoneAtSelectedTableItem(ITableEntry entry) {
        if (entry != null) {
            IDataMapTable table = entry.getParent();
            TableItem tableItem = mapperManager.retrieveTableItem(entry);
            if (table != null && tableItem != null) {
                Rectangle bounds = tableItem.getBounds();
                int selection = bounds.y - 30;

                ScrolledComposite scrollComposite = null;
                if (table instanceof InputTable) {
                    // selection = bounds.y + mainTableBounds * 30;
                    scrollComposite = uiManager.getScrolledCompositeViewInputs();
                } else if (table instanceof OutputTable) {
                    scrollComposite = uiManager.getScrolledCompositeViewOutputs();
                }
                if (scrollComposite != null) {
                    setPositionOfVerticalScrollBarZone(scrollComposite, selection);
                }
            }
        }
    }

    private void setPositionOfVerticalScrollBarZone(ScrolledComposite scrollComposite, int scrollBarSelection) {
        ScrollBar verticalBar = scrollComposite.getVerticalBar();
        verticalBar.setSelection(scrollBarSelection);
        scrollComposite.setOrigin(0, scrollBarSelection);
    }

    public Integer getSelectedKeyAtSelectedTableItem(Map<Integer, ITableEntry> searchMaps) {
        Integer selectKey = 0;
        TableViewerCreator tableViewerCreator = null;
        if (uiManager.getCurrentSelectedInputTableView() != null) {
            tableViewerCreator = uiManager.getCurrentSelectedInputTableView().getTableViewerCreatorForColumns();
        } else if (uiManager.getCurrentSelectedOutputTableView() != null) {
            tableViewerCreator = uiManager.getCurrentSelectedOutputTableView().getTableViewerCreatorForColumns();
        }
        if (tableViewerCreator != null && tableViewerCreator.getTableViewer() != null) {
            ISelection selection = tableViewerCreator.getTableViewer().getSelection();
            if (selection != null && !selection.isEmpty()) {
                List<ITableEntry> list = uiManager.extractSelectedTableEntries(selection);
                if (list != null && !list.isEmpty()) {
                    ITableEntry tableEntry = list.get(0);
                    if (tableEntry != null) {
                        if (searchMaps.containsValue(tableEntry)) {
                            Iterator iter = searchMaps.entrySet().iterator();
                            while (iter.hasNext()) {
                                Map.Entry entry = (Map.Entry) iter.next();
                                if (entry.getValue() != null && entry.getValue() instanceof ITableEntry) {
                                    ITableEntry tableEntryTemp = (ITableEntry) entry.getValue();
                                    if (tableEntry.equals(tableEntryTemp)) {
                                        tableViewerCreator.getTableViewer().getTable().deselectAll();
                                        return (Integer) entry.getKey();
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return selectKey;
    }

    public boolean isHightlightAll() {
        return this.isHightlightAll;
    }

    public void setHightlightAll(boolean isHightlightAll) {
        this.isHightlightAll = isHightlightAll;
    }

}
