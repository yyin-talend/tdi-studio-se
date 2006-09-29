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
package org.talend.designer.mapper.ui.visualmap.link;

import org.talend.designer.mapper.model.tableentry.ITableEntry;
import org.talend.designer.mapper.ui.visualmap.zone.Zone;

/**
 * DOC amaumont class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 */
public class PointLinkDescriptor {

    private ITableEntry dataMapTableEntry;

    private Zone zone;

    public PointLinkDescriptor(ITableEntry dataMapTableEntry, Zone zone) {
        super();
        this.dataMapTableEntry = dataMapTableEntry;
        this.zone = zone;
    }

    public ITableEntry getTableEntry() {
        return this.dataMapTableEntry;
    }

    public void setITableEntry(ITableEntry dataMapTableEntry) {
        this.dataMapTableEntry = dataMapTableEntry;
    }

    public Zone getZone() {
        return this.zone;
    }

    public void setZone(Zone zone) {
        this.zone = zone;
    }

}
