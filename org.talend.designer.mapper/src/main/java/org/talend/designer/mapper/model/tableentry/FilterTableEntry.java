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
package org.talend.designer.mapper.model.tableentry;

import org.talend.designer.abstractmap.model.table.IDataMapTable;

/**
 * DOC amaumont class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 */
public class FilterTableEntry extends DataMapTableEntry {

    public FilterTableEntry(IDataMapTable abstractDataMapTable, String name, String expression) {
        super(abstractDataMapTable, name, expression);
    }

    /* (non-Javadoc)
     * @see org.talend.designer.mapper.model.tableentry.IDataMapTableEntry#isTableEntry()
     */
    public boolean isTableEntry() {
        return true;
    }

    /* (non-Javadoc)
     * @see org.talend.designer.mapper.model.tableentry.IDataMapTableEntry#isColumnEntry()
     */
    public boolean isColumnEntry() {
        return false;
    }

    
    
}
