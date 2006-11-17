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
package org.talend.repository.ui.wizards.metadata.connection.files.xml.dnd;

import java.util.ArrayList;
import java.util.List;


/**
 * DOC amaumont  class global comment. Detailled comment
 * <br/>
 *
 * $Id$
 *
 */
public class XmlToSchemaDraggedData {

    private List<TransferableXPathEntry> transferableEntryList = new ArrayList<TransferableXPathEntry>();

    /**
     * @param o
     * @return
     * @see java.util.List#add(java.lang.Object)
     */
    public boolean add(TransferableXPathEntry o) {
        return this.transferableEntryList.add(o);
    }

    
    /**
     * Getter for transferableEntryList.
     * @return the transferableEntryList
     */
    public List<TransferableXPathEntry> getTransferableEntryList() {
        return this.transferableEntryList;
    }

    
    
}
