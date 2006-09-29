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
package org.talend.designer.mapper.model.table;

import java.util.ArrayList;
import java.util.List;

import org.talend.designer.mapper.external.data.ExternalMapperTable;
import org.talend.designer.mapper.model.tableentry.IColumnEntry;

/**
 * DOC amaumont class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 */
public abstract class AbstractDataMapTable {

    protected List<IColumnEntry> dataMapTableEntries = new ArrayList<IColumnEntry>();

    protected boolean minimized;

    private String name;

    /**
     * DOC amaumont DataMapTable constructor comment.
     * 
     * @param persistentTable
     * 
     * @param metadataTable
     * @param externalMapperTable
     */
    public AbstractDataMapTable(String name, ExternalMapperTable persistentTable) {
        this.name = name;
        if (name == null) {
            throw new IllegalArgumentException("Name's AbstractDataMapTable can't be null");
        }
        initFromExternalData(persistentTable);
    }

    /**
     * DOC amaumont AbstractDataMapTable constructor comment.
     * 
     * @param name2
     */
    public AbstractDataMapTable(String name) {
        this(name, null);
    }

    /**
     * DOC amaumont Comment method "init".
     */
    private void initFromExternalData(ExternalMapperTable externalMapperTable) {
        if (externalMapperTable != null) {
            this.minimized = externalMapperTable.isMinimized();
        }
    }

    /**
     * 
     * @return a <code>List</code> of entries.
     */
    public List<IColumnEntry> getColumnEntries() {
        return this.dataMapTableEntries;
    }

    public void removeColumnEntry(IColumnEntry dataMapTableEntry) {
        this.dataMapTableEntries.remove(dataMapTableEntry);
    }

    public void addColumnEntry(IColumnEntry dataMapTableEntry) {
        this.dataMapTableEntries.add(dataMapTableEntry);
    }

    public void addColumnEntry(IColumnEntry dataMapTableEntry, int index) {
        this.dataMapTableEntries.add(index, dataMapTableEntry);
    }

    public String getName() {
        return this.name;
    }

    public boolean isMinimized() {
        return this.minimized;
    }

    public void setMinimized(boolean minimized) {
        this.minimized = minimized;
    }

    /**
     * DOC amaumont Comment method "swapEntries".
     * 
     * @param integer2
     * @param integer
     */
    public void swapColumnEntries(List<Integer> indexOrigin, List<Integer> indexDestination) {
        int lstSize = indexOrigin.size();
        for (int i = 0; i < lstSize; i++) {
            swapColumnEntries(indexOrigin.get(i), indexDestination.get(i));
        }
    }

    /**
     * DOC amaumont Comment method "swapEntries".
     * 
     * @param integer2
     * @param integer
     */
    private void swapColumnEntries(int index1, int index2) {
        if (index1 == index2) {
            return;
        }
        IColumnEntry temp = dataMapTableEntries.get(index1);
        dataMapTableEntries.set(index1, dataMapTableEntries.get(index2));
        dataMapTableEntries.set(index2, temp);
    }

}
