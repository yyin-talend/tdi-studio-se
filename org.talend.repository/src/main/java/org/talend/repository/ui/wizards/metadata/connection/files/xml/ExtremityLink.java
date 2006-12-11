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

/**
 * DOC amaumont class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 * @param <G>
 * @param <D>
 * 
 */
public class ExtremityLink<G, D> implements IExtremityLink<G, D> {

    private D dataItem;
    private G graphicalObject;

    
    
    /**
     * DOC amaumont TableItemExtremityDescriptor constructor comment.
     * 
     * @param tableItem
     */
    public ExtremityLink(G graphicalObject, D dataItem) {
        super();
        this.dataItem = dataItem;
        this.graphicalObject = graphicalObject;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.commons.ui.swt.drawing.link.IExtremityLink#getDataItem()
     */
    public D getDataItem() {
        return this.dataItem;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.commons.ui.swt.drawing.link.IExtremityLink#setDataItem(java.lang.Object)
     */
    public void setDataItem(D dataItem) {
        this.dataItem = dataItem;
    }

    /* (non-Javadoc)
     * @see org.talend.commons.ui.swt.drawing.link.IExtremityLink#getGraphicalItem()
     */
    public G getGraphicalObject() {
        return graphicalObject;
    }

    /* (non-Javadoc)
     * @see org.talend.commons.ui.swt.drawing.link.IExtremityLink#setGraphicalItem(java.lang.Object)
     */
    public void setGraphicalObject(G graphicalObject) {
        this.graphicalObject = graphicalObject;
    }

    
    
}
