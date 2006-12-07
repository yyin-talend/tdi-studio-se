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
package org.talend.designer.mapper.ui.image;

/**
 * DOC mhelleboid class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 */
public enum ImageInfo {

    MINIMIZE_ICON("/icons/collapse16.png"),
    RESTORE_ICON("/icons/uncollapse16.png"),
    // MAPPER_ICON("/icons/tMap32.ico"),
    ADD_CONSTRAINT_ICON("/icons/add_constraint16.png"),
    REMOVE_CONSTRAINT_ICON("/icons/remove_constraint16.png"),
    CHECKED_ICON("/icons/checked.png"),
    UNCHECKED_ICON("/icons/unchecked.png"),
    ADD_FILTER_ICON("/icons/addfilter.png"),
    REJECT_FILTER_ICON("/icons/rejectfilter.png"),
    REJECT_LOOKUP_ICON("/icons/rejectlookup.png");

    private String path;

    ImageInfo(String path) {
        this.path = path;
    }

    public String getPath() {
        return this.path;
    }

}
