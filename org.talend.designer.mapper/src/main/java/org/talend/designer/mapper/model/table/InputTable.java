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

import org.talend.core.model.metadata.IMetadataColumn;
import org.talend.core.model.process.EConnectionType;
import org.talend.designer.mapper.external.connection.IOConnection;
import org.talend.designer.mapper.external.data.ExternalMapperTable;
import org.talend.designer.mapper.model.tableentry.AbstractInOutTableEntry;
import org.talend.designer.mapper.model.tableentry.InputColumnTableEntry;

/**
 * DOC amaumont class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 */
public class InputTable extends AbstractInOutTable {

    private IOConnection connection;

    /**
     * if innerJoin is true and current lookup row not found, current main row will be rejected.
     */
    private boolean innerJoin; // else outer join

    /**
     * DOC amaumont InputTable constructor comment.
     * 
     * @param metadataTable
     * @param externalMapperTable can be null
     * @param mainConnection
     */
    public InputTable(IOConnection connection, ExternalMapperTable externalMapperTable, String name) {
        super(connection.getTable(), externalMapperTable, name);
        this.connection = connection;
        if (externalMapperTable != null) {
            this.innerJoin = externalMapperTable.isInnerJoin();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.mapper.model.table.DataMapTable#getNewTableEntry(org.talend.core.model.metadata.IMetadataColumn)
     */
    @Override
    protected AbstractInOutTableEntry getNewTableEntry(IMetadataColumn column) {
        return new InputColumnTableEntry(this, column);
    }

    @Override
    public String getName() {
        return connection.getName();
    }

    public boolean isMainConnection() {
        return EConnectionType.FLOW_MAIN == connection.getConnectionType();
    }

    /**
     * Getter for innerJoin.
     * 
     * @return the innerJoin
     */
    public boolean isInnerJoin() {
        return this.innerJoin;
    }

    /**
     * Sets the innerJoin.
     * 
     * @param innerJoin the innerJoin to set
     */
    public void setInnerJoin(boolean innerJoin) {
        this.innerJoin = innerJoin;
    }

}
