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
package org.talend.repository.ui.wizards.metadata.connection.files.xml;

import org.eclipse.swt.widgets.Table;
import org.talend.commons.ui.swt.drawing.link.IExtremityLink;
import org.talend.core.model.metadata.builder.connection.SchemaTarget;

/**
 * DOC amaumont class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 */
public class SchemaTargetExtremityDescriptor implements IExtremityLink<Table, SchemaTarget> {

    private SchemaTarget schemaTarget;
    private Table table;

    
    
    /**
     * DOC amaumont TableItemExtremityDescriptor constructor comment.
     * 
     * @param tableItem
     */
    public SchemaTargetExtremityDescriptor(Table graphicalObject, SchemaTarget schemaTarget) {
        super();
        this.schemaTarget = schemaTarget;
        this.table = graphicalObject;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.commons.ui.swt.drawing.link.IExtremityLink#getDataItem()
     */
    public SchemaTarget getDataItem() {
        return this.schemaTarget;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.commons.ui.swt.drawing.link.IExtremityLink#setDataItem(java.lang.Object)
     */
    public void setDataItem(SchemaTarget dataItem) {
        this.schemaTarget = dataItem;
    }

    /* (non-Javadoc)
     * @see org.talend.commons.ui.swt.drawing.link.IExtremityLink#getGraphicalItem()
     */
    public Table getGraphicalObject() {
        return table;
    }

    /* (non-Javadoc)
     * @see org.talend.commons.ui.swt.drawing.link.IExtremityLink#setGraphicalItem(java.lang.Object)
     */
    public void setGraphicalObject(Table graphicalObject) {
        this.table = graphicalObject;
    }

    
    
}
