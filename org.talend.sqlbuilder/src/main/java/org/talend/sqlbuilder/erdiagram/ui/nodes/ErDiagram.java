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
package org.talend.sqlbuilder.erdiagram.ui.nodes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.talend.core.model.metadata.builder.connection.MetadataTable;
import org.talend.sqlbuilder.repository.utility.EMFRepositoryNodeManager;

/**
 * DOC qzhang class global comment. Detailled comment <br/>
 * 
 * $Id: talend.epf 1 2006-09-29 17:06:40 +0000 (ææäº, 29 ä¹æ 2006) nrousseau $
 * 
 */
public class ErDiagram extends Element {

    private EMFRepositoryNodeManager nodeManager;
    
    public static final String PROP_TABLES = "tables"; //$NON-NLS-1$

    public static final String PROP_RELATIONS = "relation"; //$NON-NLS-1$

    private List<Table> tables;

    private List<String[]> relations;

    private static Map<String, Column> allColumns = new HashMap<String, Column>();

    private List<MetadataTable> metadataTables;
    
    /**
     * DOC admin ErDiagram constructor comment.
     */
    public ErDiagram() {
        tables = new ArrayList<Table>();
    }

    /**
     * 
     */
    private static final long serialVersionUID = 10000L;

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.sqlbuider.erdiagram.ui.editor.nodes.Element#getElementName()
     */
    @Override
    public String getElementName() {
        // TODO Auto-generated method stub
        return null;
    }

    public List<Table> getTables() {
        return this.tables;
    }

    @SuppressWarnings("unchecked") //$NON-NLS-1$
    public void addTable(Table table) {
        this.tables.add(table);
        for (Column column : (List<Column>) table.getColumns()) {
            allColumns.put(table.getElementName() + "." + column.getElementName(), column); //$NON-NLS-1$
        }
        
        fireStructureChange(PROP_TABLES, this.tables);
    }

    @SuppressWarnings("unchecked") //$NON-NLS-1$
    public void removeTable(Table table) {
        this.tables.remove(table);
        fireStructureChange(PROP_TABLES, this.tables);
    }

    public List<String[]> getRelations() {
        return this.relations;
    }

    public void setRelations(List<String[]> relations) {
        this.relations = relations;
        for (String[] strings : relations) {
            String pk = strings[0];
            String fk = strings[1];
            if (allColumns.keySet().contains(fk) && allColumns.keySet().contains(pk)) {
                new Relation(allColumns.get(pk), allColumns.get(fk));
            }
        }
    }

    
    public List<MetadataTable> getMetadataTables() {
        return this.metadataTables;
    }

    
    public void setMetadataTables(List<MetadataTable> metadataTables) {
        this.metadataTables = metadataTables;
    }

    
    public EMFRepositoryNodeManager getNodeManager() {
        return this.nodeManager;
    }

    
    public void setNodeManager(EMFRepositoryNodeManager nodeManager) {
        this.nodeManager = nodeManager;
    }
}
