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
import java.util.List;

import org.eclipse.draw2d.geometry.Dimension;
import org.talend.core.model.metadata.builder.connection.MetadataColumn;

/**
 *  qzhang class global comment. Detailled comment <br/>
 * 
 * $Id: Column.java 2006-12-25 下午03:03:20 +0000 (2006-12-25) qzhang $
 * 
 */
public class Column extends Element {

    /**
     * 
     */
    private static final long serialVersionUID = 0L;

    public static final String PROP_SELECTED = "selected"; //$NON-NLS-1$

    public static final String PROP_TALBE = "table"; //$NON-NLS-1$

    public static final String PROP_INPUTS = "INPUTS"; //$NON-NLS-1$

    public static final String PROP_OUTPUTS = "OUTPUTS"; //$NON-NLS-1$

    private Table table;

    private boolean selected;

    private List<Relation> inputs;

    private List<Relation> outputs;

    private MetadataColumn metadataColumn;

    private String columnName;

    private Dimension size = new Dimension();

    /**
     *  yzhang Column constructor comment.
     */
    public Column() {
        inputs = new ArrayList<Relation>();
        outputs = new ArrayList<Relation>();
    }

    /**
     *  yzhang Comment method "setSelected".
     * 
     * @param selected
     */
    public void setSelected(boolean selected) {
        this.selected = selected;
        fireStructureChange(PROP_SELECTED, this.selected);
    }

    /**
     *  yzhang Comment method "isSelected".
     */
    public boolean isSelected() {
        return selected;
    }

    /**
     *  yzhang Comment method "getTable".
     * 
     * @return
     */
    public Table getTable() {
        return this.table;
    }

    /*
     * (non-Java)
     * 
     * @see org.talend.sqlbuider.erdiagram.model.Element#getElementName()
     */
    @Override
    public String getElementName() {
        return columnName;
    }

    public void setElementName(String name) {
        this.columnName = name;
        size.height = 23;
        
        size.width = Math.round(columnName.length() * 5.2f) + 45;
    }

    public void setMetadataColumn(MetadataColumn metadataColumn) {
        this.metadataColumn = metadataColumn;
        if (this.metadataColumn != null) {
            setElementName(this.metadataColumn.getOriginalField());
        }
    }

    public void setTable(Table table) {
        this.table = table;
        fireStructureChange(PROP_TALBE, this.table);
    }

    public List<Relation> getInputs() {
        return this.inputs;
    }

    public List<Relation> getOutputs() {
        return this.outputs;
    }

    public void addInputRelation(Relation relation) {
        this.inputs.add(relation);
        fireStructureChange(PROP_INPUTS, this.inputs);
    }

    public void removeInputRelation(Relation relation) {
        this.inputs.remove(relation);
        fireStructureChange(PROP_INPUTS, this.inputs);
    }

    public void addOutputRelation(Relation relation) {
        this.outputs.add(relation);
        fireStructureChange(PROP_OUTPUTS, this.outputs);
    }

    public void removeOutputRelation(Relation relation) {
        this.outputs.remove(relation);
        fireStructureChange(PROP_OUTPUTS, this.outputs);
    }

    public Dimension getSize() {
        return this.size;
    }

    public void setSize(Dimension size) {
        this.size = size;
    }
}
