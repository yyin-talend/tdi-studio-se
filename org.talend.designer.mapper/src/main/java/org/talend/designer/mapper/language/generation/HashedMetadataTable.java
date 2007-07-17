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

import java.util.HashMap;
import java.util.List;

import org.talend.core.model.metadata.IMetadataColumn;
import org.talend.core.model.metadata.IMetadataTable;

/**
 * 
 * DOC amaumont JavaGenerationManager class global comment. Detailled comment <br/>
 * 
 */
public class HashedMetadataTable {

    private IMetadataTable metadataTable;

    private HashMap<String, IMetadataColumn> hashMetataColumns;

    /**
     * DOC amaumont JavaGenerationManager.HashedMetadataTable constructor comment.
     */
    public HashedMetadataTable(IMetadataTable metadataTable) {
        this.metadataTable = metadataTable;
        hashMetataColumns = new HashMap<String, IMetadataColumn>();
        List<IMetadataColumn> listColumns = metadataTable.getListColumns();
        int listColumnsListSize = listColumns.size();
        for (int i = 0; i < listColumnsListSize; i++) {
            IMetadataColumn column = listColumns.get(i);
            hashMetataColumns.put(column.getLabel(), column);
        }
    }

    public IMetadataColumn getColumn(String columnName) {
        return hashMetataColumns.get(columnName);
    }

    /**
     * Getter for metadataTable.
     * 
     * @return the metadataTable
     */
    public IMetadataTable getMetadataTable() {
        return this.metadataTable;
    }
}
