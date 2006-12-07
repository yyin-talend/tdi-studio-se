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

import org.talend.commons.ui.swt.drawing.link.IExtremityLink;
import org.talend.core.model.metadata.builder.connection.SchemaTarget;

/**
 * DOC amaumont class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 */
public class SchemaTargetExtremityDescriptor implements IExtremityLink<SchemaTarget> {

    private SchemaTarget schemaTarget;

    /**
     * DOC amaumont TableItemExtremityDescriptor constructor comment.
     * 
     * @param tableItem
     */
    public SchemaTargetExtremityDescriptor(SchemaTarget schemaTarget) {
        super();
        this.schemaTarget = schemaTarget;
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

}
