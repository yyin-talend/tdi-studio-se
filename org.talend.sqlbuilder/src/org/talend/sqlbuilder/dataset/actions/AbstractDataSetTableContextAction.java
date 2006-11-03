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
package org.talend.sqlbuilder.dataset.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.swt.custom.TableCursor;
import org.eclipse.swt.widgets.Table;


/**
 * Abstract implementation for a context menu action of a DataSetTable.
 * Extend this class to add new actions to the DataSetTable.
 * 
 * @author Davy Vanherbergen
 */
public abstract class AbstractDataSetTableContextAction extends Action {

    protected Table ptable;
    
    protected TableCursor pcursor;
    

    /**
     * Store table for use in the actions.
     * @param table
     */
    public final void setTable(Table table) {
        ptable = table;        
    }

    /**
     * Store table cursor for use in the actions.
     * @param tableCursor
     */
    public final void setTableCursor(TableCursor cursor) {
        pcursor = cursor;        
    }

    
    /**
     * Implement this method to return true when your action is available
     * for the active table.  When true, the action will be included in the
     * context menu, when false it will be ignored.
     * 
     * @return true if the action should be included in the context menu
     */
    public boolean isAvailable() {
        return true;
    }
}
