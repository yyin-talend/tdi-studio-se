// ============================================================================
//
// Talend Community Edition
//
// Copyright (C) 2006-2007 Talend - www.talend.com
//
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either
// version 2.1 of the License.
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
package org.talend.designer.abstractmap.model.table;

import java.util.ArrayList;
import java.util.List;

import org.talend.commons.ui.swt.extended.table.ExtendedTableModel;
import org.talend.designer.abstractmap.managers.AbstractMapperManager;
import org.talend.designer.abstractmap.model.tableentry.IColumnEntry;


/**
 * DOC amaumont  class global comment. Detailled comment
 * <br/>
 *
 */
public interface IDataMapTable {

    /**
     * 
     * DOC amaumont AbstractDataMapTable class global comment. Detailled comment
     * <br/>
     *
     */
    public enum SIZE_STATE {
        MINIMIZED,
        INTERMEDIATE,
        MAXIMIZED,
        ;
    }
    
    /**
     * 
     * @return a <code>List</code> of entries.
     */
    public List<IColumnEntry> getColumnEntries();

    public void removeColumnEntry(IColumnEntry dataMapTableEntry);

    public void addColumnEntry(IColumnEntry dataMapTableEntry);

    public void addColumnEntry(IColumnEntry dataMapTableEntry, int index);

    public String getName();

    public boolean isMinimized();

    public void setMinimized(boolean minimized);

    /**
     * Getter for columnsEntriesModel.
     * 
     * @return the columnsEntriesModel
     */
    public ExtendedTableModel<IColumnEntry> getTableColumnsEntriesModel();

    /**
     * DOC amaumont Comment method "swapElements".
     * 
     * @param indicesOrigin
     * @param listIndexTarget
     */
    public void swapColumnElements(List<Integer> indicesOrigin, List<Integer> listIndexTarget);

    /**
     * Getter for mapperManager.
     * 
     * @return the mapperManager
     */
    public AbstractMapperManager getMapperManager();

    /**
     * DOC amaumont Comment method "isReadOnly".
     * 
     * @return
     */
    public boolean isReadOnly();

    /**
     * Sets the readOnly.
     * 
     * @param readOnly the readOnly to set
     */
    public void setReadOnly(boolean readOnly);

    /**
     * Getter for sizeState.
     * @return the sizeState
     */
    public SIZE_STATE getSizeState();

    /**
     * Sets the sizeState.
     * @param sizeState the sizeState to set
     */
    public void setSizeState(SIZE_STATE sizeState);

}
