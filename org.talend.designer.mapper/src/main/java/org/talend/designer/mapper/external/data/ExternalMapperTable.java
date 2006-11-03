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
package org.talend.designer.mapper.external.data;

import java.io.Serializable;
import java.util.List;

/**
 * DOC amaumont class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 */
public class ExternalMapperTable implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 8768004929161302382L;

    private List<ExternalMapperTableEntry> constraintTableEntries;

    private List<ExternalMapperTableEntry> metadataTableEntries;

    private String name;

    private boolean minimized;

    /**
     * used only for outputs.
     */
    private boolean reject;

    public String getName() {
        return this.name;
    }

    public void setName(String medataTableName) {
        this.name = medataTableName;
    }

    public boolean isMinimized() {
        return this.minimized;
    }

    public void setMinimized(boolean minimized) {
        this.minimized = minimized;
    }

    public List<ExternalMapperTableEntry> getMetadataTableEntries() {
        return this.metadataTableEntries;
    }

    public void setMetadataTableEntries(List<ExternalMapperTableEntry> tableEntries) {
        this.metadataTableEntries = tableEntries;
    }

    public List<ExternalMapperTableEntry> getConstraintTableEntries() {
        return this.constraintTableEntries;
    }

    public void setConstraintTableEntries(List<ExternalMapperTableEntry> constraintTableEntries) {
        this.constraintTableEntries = constraintTableEntries;
    }

    /**
     * 
     * used only for outputs.
     * 
     * @return
     */
    public boolean isReject() {
        return this.reject;
    }

    /**
     * 
     * used only for outputs.
     * 
     * @param reject
     */
    public void setReject(boolean reject) {
        this.reject = reject;
    }

}
