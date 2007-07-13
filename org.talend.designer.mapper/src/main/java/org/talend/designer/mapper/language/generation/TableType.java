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
package org.talend.designer.mapper.language.generation;

import org.talend.designer.mapper.model.table.VarsTable;

/**
 * 
 * 
 */
public enum TableType {
    INPUT("in", 10), //$NON-NLS-1$
    OUTPUT("out", 10), //$NON-NLS-1$
    VARS(VarsTable.PREFIX_VARS_TABLE_NAME, 1);

    String baseTableName;

    int nTables;

    TableType(String baseTableName, int nTables) {
        this.baseTableName = baseTableName;
        this.nTables = nTables;
    }

    public String getBaseTableName() {
        return this.baseTableName;
    }

    public int getNTables() {
        return this.nTables;
    }

};
