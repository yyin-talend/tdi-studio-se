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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.talend.designer.mapper.model.table.AbstractDataMapTable;
import org.talend.designer.mapper.model.table.OutputTable;
import org.talend.designer.mapper.model.tableentry.ConstraintTableEntry;
import org.talend.designer.mapper.model.tableentry.IColumnEntry;
import org.talend.designer.mapper.model.tableentry.ITableEntry;
import org.talend.designer.mapper.model.tableentry.TableEntryLocation;
import org.talend.designer.mapper.ui.visualmap.TableEntryProperties;
import org.talend.designer.mapper.ui.visualmap.table.DataMapTableView;

/**
 * DOC amaumont class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 */
public class TableEntriesManager {

    private Map<TableEntryLocation, ITableEntry> tableEntries;

    private Map<ITableEntry, TableEntryProperties> dataMapTableEntryToProperties;

    MapperManager mapperManager;

    static final int HEIGHT_REACTION = 2;

    TableEntriesManager(MapperManager mapperManager) {
        super();
        this.tableEntries = new HashMap<TableEntryLocation, ITableEntry>();
        this.dataMapTableEntryToProperties = new HashMap<ITableEntry, TableEntryProperties>();
        this.mapperManager = mapperManager;
    }

    void removeAll(List<? extends ITableEntry> dataMapTableEntriesGroup) {

        for (ITableEntry dataMapTableEntry : new ArrayList<ITableEntry>(dataMapTableEntriesGroup)) {
            remove(dataMapTableEntry);
        }

    }

    /**
     * DOC amaumont Comment method "load".
     * 
     * @param tableEntries2
     */
    void addAll(List<? extends ITableEntry> dataMapTableEntriesGroup) {
        for (ITableEntry dataMapTableEntry : dataMapTableEntriesGroup) {
            add(dataMapTableEntry);
        }
    }

    void addTableEntry(ITableEntry dataMapTableEntry) {
        if (dataMapTableEntry == null) {
            throw new IllegalArgumentException("dataMapTableEntry can't be null.");
        }
        AbstractDataMapTable dataMapTable = dataMapTableEntry.getParent();
        if (dataMapTableEntry instanceof IColumnEntry) {
            dataMapTable.addColumnEntry((IColumnEntry) dataMapTableEntry);
        } else if (dataMapTableEntry instanceof ConstraintTableEntry) {
            ((OutputTable) dataMapTable).addConstraintEntry((ConstraintTableEntry) dataMapTableEntry);
        } else {
            throw new IllegalArgumentException("Type '" + dataMapTableEntry.getClass() + "' is not a valid type");
        }
        add(dataMapTableEntry);
    }

    /**
     * DOC amaumont Comment method "addTableEntry".
     * 
     * @param dataMapTableEntry
     * @param index
     */
    void addTableEntry(ITableEntry dataMapTableEntry, Integer index) {
        if (dataMapTableEntry == null) {
            throw new IllegalArgumentException("dataMapTableEntry can't be null.");
        }
        AbstractDataMapTable dataMapTable = dataMapTableEntry.getParent();
        if (dataMapTableEntry instanceof IColumnEntry) {
            dataMapTable.addColumnEntry((IColumnEntry) dataMapTableEntry, index);
        } else if (dataMapTableEntry instanceof ConstraintTableEntry) {
            ((OutputTable) dataMapTable).addConstraintEntry((ConstraintTableEntry) dataMapTableEntry, index);
        } else {
            throw new IllegalArgumentException("Type '" + dataMapTableEntry.getClass() + "' is not a valid type");
        }
        add(dataMapTableEntry);
    }

    /**
     * DOC amaumont Comment method "add".
     * 
     * @param dataMapTableEntry
     */
    private void add(ITableEntry dataMapTableEntry) {
        tableEntries.put(TableEntryLocation.getNewInstance(dataMapTableEntry), dataMapTableEntry);
        mapperManager.getUiManager().parseExpression(dataMapTableEntry.getExpression(), dataMapTableEntry, false, false, false);
    }

    void remove(ITableEntry dataMapTableEntry) {
        if (dataMapTableEntry != null) {
            mapperManager.removeLinksOf(dataMapTableEntry);
            tableEntries.remove(TableEntriesManager.buildLocation(dataMapTableEntry));
            dataMapTableEntryToProperties.remove(dataMapTableEntry);
            AbstractDataMapTable dataMapTable = dataMapTableEntry.getParent();
            if (dataMapTableEntry instanceof IColumnEntry) {
                dataMapTableEntry.getParent().removeColumnEntry((IColumnEntry) dataMapTableEntry);
            } else if (dataMapTableEntry instanceof ConstraintTableEntry) {
                if (dataMapTable instanceof OutputTable) {
                    ((OutputTable) dataMapTable).removeConstraintEntry((ConstraintTableEntry) dataMapTableEntry);
                }
            } else {
                throw new IllegalArgumentException("Type '" + dataMapTableEntry.getClass() + "' is not a valid type");
            }
        }
    }

    /**
     * DOC amaumont Comment method "searchTableEntry".
     * 
     * @param tableName
     * @param columnName
     */
    ITableEntry retrieveTableEntry(TableEntryLocation location) {
        return tableEntries.get(location);
    }

    /**
     * DOC amaumont Comment method "getTableEntryProperties".
     * 
     * @param dataMapTableEntry
     * @return
     */
    TableEntryProperties getTableEntryProperties(ITableEntry dataMapTableEntry) {
        TableEntryProperties tableEntryProperties = dataMapTableEntryToProperties.get(dataMapTableEntry);
        if (tableEntryProperties == null) {
            tableEntryProperties = new TableEntryProperties();
            dataMapTableEntryToProperties.put(dataMapTableEntry, tableEntryProperties);
        }
        return tableEntryProperties;
    }

    TableItem retrieveTableItem(ITableEntry dataMapTableEntry) {
        DataMapTableView dataMapTableView = this.mapperManager.retrieveAbstractDataMapTableView(dataMapTableEntry.getParent());
        TableItem[] tableItems = new TableItem[0];
        if (dataMapTableEntry instanceof IColumnEntry) {
            tableItems = dataMapTableView.getTableViewerCreatorForColumns().getTable().getItems();
        } else if (dataMapTableEntry instanceof ConstraintTableEntry) {
            tableItems = dataMapTableView.getTableViewerCreatorForConstraints().getTable().getItems();
        } else {
            throw new IllegalArgumentException("case not found");
        }
        TableItem tableItem = null;
        for (int i = 0; i < tableItems.length; i++) {
            if (tableItems[i].getData() == dataMapTableEntry) {
                tableItem = tableItems[i];
                break;
            }
        }
        getTableEntryProperties(dataMapTableEntry).setTableItem(tableItem);
        return tableItem;
    }

    /**
     * DOC amaumont Comment method "retrieveTableFromTableEntry".
     * 
     * @param dataMapTableEntry
     * @return
     */
    Table retrieveTable(ITableEntry dataMapTableEntry) {
        return retrieveTableItem(dataMapTableEntry).getParent();
    }

    /**
     * DOC amaumont Comment method "renameEntryName".
     * 
     * @param dataMapTableEntry
     * @param newColumnName
     * @param newColumnName 
     */
    public void renameEntryName(ITableEntry dataMapTableEntry, String previousColumnName, String newColumnName) {
        TableEntryLocation tableEntryLocationKey = new TableEntryLocation(dataMapTableEntry.getParentName(), previousColumnName);
//            TableEntriesManager.buildLocation(dataMapTableEntry);
        ITableEntry entry = tableEntries.get(tableEntryLocationKey);
        if (entry != dataMapTableEntry) {
            throw new IllegalStateException("tableEntries are not the same !");
        }
        tableEntries.remove(tableEntryLocationKey);
        tableEntryLocationKey.columnName = newColumnName;
        tableEntries.put(tableEntryLocationKey, dataMapTableEntry);
        dataMapTableEntry.setName(newColumnName);
    }

    public static TableEntryLocation buildLocation(ITableEntry dataMapTableEntry) {
        return new TableEntryLocation(dataMapTableEntry.getParentName(), dataMapTableEntry.getName());
    }

}
