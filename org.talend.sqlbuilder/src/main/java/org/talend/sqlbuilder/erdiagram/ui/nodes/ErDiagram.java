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
import org.talend.sqlbuilder.erdiagram.ui.ErDiagramComposite;

/**
 * qzhang class global comment. Detailled comment <br/>
 * 
 * $Id: talend.epf 1 2006-09-29 17:06:40 +0000 (ææäº, 29 ä¹æ 2006) nrousseau $
 * 
 */
public class ErDiagram extends Element {

    public static final String PROP_TABLES = "tables"; //$NON-NLS-1$

    public static final String PROP_RELATIONS = "relation"; //$NON-NLS-1$

    public static final String PROP_ISDIRTY = "isDirty";

    private List<Table> tables;

    private List<String[]> relations;

    private List<MetadataTable> metadataTables;

    private boolean isDirty = false;
    
    
    /**
     * Getter for isDirty.
     * @return the isDirty
     */
    public boolean isDirty() {
        return this.isDirty;
    }
    
    
    /**
     * Sets the isDirty.
     * @param isDirty the isDirty to set
     */
    public void setDirty(boolean isDirty) {
        this.isDirty = isDirty;
        fireStructureChange(PROP_ISDIRTY, this.isDirty);
    }
    
    /**
     * admin ErDiagram constructor comment.
     */
    public ErDiagram() {
        tables = new ArrayList<Table>();
    }

    private ErDiagramComposite  erDiagramComposite;
    
    
    /**
     * Sets the erDiagramComposite.
     * @param erDiagramComposite the erDiagramComposite to set
     */
    public void setErDiagramComposite(ErDiagramComposite erDiagramComposite) {
        this.erDiagramComposite = erDiagramComposite;
    }
    
    
    /**
     * Getter for erDiagramComposite.
     * @return the erDiagramComposite
     */
    public ErDiagramComposite getErDiagramComposite() {
        return this.erDiagramComposite;
    }
    public void updateSqlText() {
        erDiagramComposite.setSqlText(erDiagramComposite.getSqlStatement());
    }
    /**
     * 
     */
    private static final long serialVersionUID = 10000L;

    /*
     * (non-Java)
     * 
     * @see org.talend.sqlbuider.erdiagram.ui.editor.nodes.Element#getElementName()
     */
    @Override
    public String getElementName() {
        return null;
    }

    public List<Table> getTables() {
        return this.tables;
    }

    @SuppressWarnings("unchecked")//$NON-NLS-1$
    public void addTable(Table table) {
        this.tables.add(table);
        fireStructureChange(PROP_TABLES, this.tables);
    }

    @SuppressWarnings("unchecked")//$NON-NLS-1$
    public void removeTable(Table table) {
        this.tables.remove(table);
        fireStructureChange(PROP_TABLES, this.tables);
    }

    public List<String[]> getRelations() {
        return this.relations;
    }

    @SuppressWarnings("unchecked")
    public void setRelations(List<String[]> relations) {
        this.relations = relations;

        Map<String, Column> allColumns = new HashMap<String, Column>();
        for (Table table : tables) {
            for (Column column : (List<Column>) table.getColumns()) {
                allColumns.put(table.getElementName().toLowerCase() + "." + column.getElementName().toLowerCase(), column); //$NON-NLS-1$
            }
        }

        for (String[] strings : relations) {
            String pk = strings[0].toLowerCase();
            String fk = strings[1].toLowerCase();
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
}
