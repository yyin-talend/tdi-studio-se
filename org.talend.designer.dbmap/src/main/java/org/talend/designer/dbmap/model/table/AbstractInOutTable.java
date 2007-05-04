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
package org.talend.designer.dbmap.model.table;

import org.talend.core.model.metadata.IMetadataColumn;
import org.talend.core.model.metadata.IMetadataTable;
import org.talend.designer.dbmap.external.data.ExternalDbMapTable;
import org.talend.designer.dbmap.managers.MapperManager;
import org.talend.designer.dbmap.model.tableentry.AbstractInOutTableEntry;

/**
 * DOC amaumont class global comment. Detailled comment <br/>
 * 
 * $Id: AbstractInOutTable.java 1744 2007-01-31 13:05:30Z amaumont $
 * 
 */
public abstract class AbstractInOutTable extends AbstractDataMapTable {

    protected IMetadataTable metadataTable;

    /**
     * 
     * DOC amaumont AbstractInOutTable constructor comment.
     * 
     * @param metadataTable
     * @param externalMapperTable can be null
     * @param name
     */
    public AbstractInOutTable(MapperManager mapperManager, IMetadataTable metadataTable, String name) {
        super(mapperManager, name);
        this.metadataTable = metadataTable;
    }

    protected void initFromExternalData(ExternalDbMapTable externalMapperTable) {
        super.initFromExternalData(externalMapperTable);
    }

    protected abstract AbstractInOutTableEntry getNewTableEntry(IMetadataColumn metadataColumn);

    public IMetadataTable getMetadataTable() {
        return this.metadataTable;
    }
    
    protected void setMetadataTable(IMetadataTable metadataTable) {
        this.metadataTable = metadataTable;
    }

}
