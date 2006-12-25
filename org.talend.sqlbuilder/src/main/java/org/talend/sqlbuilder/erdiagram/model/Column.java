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
import java.util.List;

import org.talend.core.model.metadata.builder.connection.MetadataColumn;

/**
 * DOC yzhang class global comment. Detailled comment <br/>
 * 
 * $Id: Column.java 2006-12-25 下午03:03:20 +0000 (2006-12-25) yzhang $
 * 
 */
public class Column extends ErElement {

    private static final String PROP_SELECTED = "selected";

    private static final String PROP_INPUTS = "inputs";

    private static final String PROP_OUTPUTS = "outputs";

    private Table talbe;

    private boolean selected;

    private List<Relation> inputs;

    private List<Relation> outputs;

    private MetadataColumn metadataColumn;

    /**
     * DOC yzhang Column constructor comment.
     */
    public Column(Table talbe, MetadataColumn meColumn) {
        this.talbe = talbe;
        metadataColumn = meColumn;
        inputs = new ArrayList<Relation>();
        outputs = new ArrayList<Relation>();
    }

    /**
     * DOC yzhang Comment method "setSelected".
     * 
     * @param selected
     */
    public void setSelected(boolean selected) {
        this.selected = selected;
        fireStructureChange(PROP_SELECTED, getTable());
    }

    /**
     * DOC yzhang Comment method "isSelected".
     */
    public boolean isSelected() {
        return selected;
    }

    /**
     * DOC yzhang Comment method "getTable".
     * 
     * @return
     */
    public Table getTable() {
        return this.talbe;
    }

    /**
     * DOC yzhang Comment method "addInput".
     * 
     * @param relation
     */
    public void addInput(Relation input) {
        inputs.add(input);
        fireStructureChange(PROP_INPUTS, input);
    }

    /**
     * DOC yzhang Comment method "addOutput".
     * 
     * @param relation
     */
    public void addOutput(Relation output) {
        outputs.add(output);
        fireStructureChange(PROP_OUTPUTS, output);
    }
}
