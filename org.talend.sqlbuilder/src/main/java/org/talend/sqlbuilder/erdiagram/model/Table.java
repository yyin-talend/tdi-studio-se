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
package org.talend.sqlbuilder.erdiagram.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.talend.core.model.metadata.builder.connection.MetadataColumn;
import org.talend.core.model.metadata.builder.connection.MetadataTable;

/**
 * DOC yzhang class global comment. Detailled comment <br/>
 * 
 * $Id: Table.java 1 2006-12-25 下午02:55:50 +0000 (ææäº, 29 ä¹æ 2006) yzhang $
 * 
 */
public class Table extends ErElement {

    private MetadataTable metadataTable;

    private List<Column> columns;

    /**
     * DOC yzhang Table constructor comment.
     */
    public Table(MetadataTable meTable) {
        metadataTable = meTable;
        columns = new ArrayList<Column>();
        formColumns();
    }

    /**
     * Form the columns within table depends on the metadata columns in metadata table.
     * 
     * DOC yzhang Comment method "formColumns".
     */
    private void formColumns() {
        EList metadataColumns = metadataTable.getColumns();
        Iterator iterator = metadataColumns.iterator();
        while (iterator.hasNext()) {
            MetadataColumn metadataColumn = (MetadataColumn) iterator.next();
            addColumn(new Column(this, metadataColumn));
        }

    }

    /**
     * DOC yzhang Comment method "addColumn".
     * 
     * @param column
     */
    private void addColumn(Column column) {
        columns.add(column);
    }

}
