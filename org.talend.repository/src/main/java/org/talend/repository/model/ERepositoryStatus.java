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
package org.talend.repository.model;

/**
 * DOC smallet class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 */
public enum ERepositoryStatus {
    NEW(false, true),
    LOCK_BY_USER(true, false),
    LOCK_BY_OTHER(false, false),
    DEFAULT(false, true),
    NOT_UP_TO_DATE(false, false),
    DELETED(false, false),
    READ_ONLY(false, false);

    private boolean editable;

    private boolean potentiallyEditable;

    ERepositoryStatus(boolean editable, boolean potentiallyEditable) {
        this.editable = editable;
        this.potentiallyEditable = potentiallyEditable;
    }

    /**
     * Getter for editable.
     * 
     * @return the editable
     */
    public boolean isEditable() {
        return this.editable;
    }

    /**
     * Getter for potentiallyEditable.
     * 
     * @return the potentiallyEditable
     */
    public boolean isPotentiallyEditable() {
        return this.potentiallyEditable;
    }

}
