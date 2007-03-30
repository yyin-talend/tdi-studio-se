// ============================================================================
//
// Talend Community Edition
//
// Copyright (C) 2006-2007 Talend - www.talend.com
//
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either
// version 2.1 of the License.
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
package org.talend.designer.dbmap.external.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * DOC amaumont class global comment. Detailled comment <br/>
 * 
 * $Id: ExternalMapperTable.java 898 2006-12-07 11:06:17Z amaumont $
 * 
 */
public class ExternalDbMapTable implements Serializable, Cloneable {

    /**
     * 
     */
    private static final long serialVersionUID = 8768004929161302382L;

    private List<ExternalDbMapEntry> customConditionsEntries;

    private List<ExternalDbMapEntry> metadataTableEntries;

    private String name;

    private boolean minimized;

    /**
     * Used only for inputs.
     */
    private String joinType;

    /**
     * Used only for inputs.
     */
    private String alias;

    /**
     * Used only for inputs.
     */
    private String tableName;

    /**
     * Used only for inputs.
     */
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

    public List<ExternalDbMapEntry> getMetadataTableEntries() {
        return this.metadataTableEntries;
    }

    public void setMetadataTableEntries(List<ExternalDbMapEntry> tableEntries) {
        this.metadataTableEntries = tableEntries;
    }

    public List<ExternalDbMapEntry> getCustomConditionsEntries() {
        return this.customConditionsEntries;
    }

    public void setCustomConditionsEntries(List<ExternalDbMapEntry> constraintTableEntries) {
        this.customConditionsEntries = constraintTableEntries;
    }

    /**
     * Getter for joinType.
     * 
     * @return the joinType
     */
    public String getJoinType() {
        return this.joinType;
    }

    /**
     * Sets the joinType.
     * 
     * @param joinType the joinType to set
     */
    public void setJoinType(String joinType) {
        this.joinType = joinType;
    }

    /**
     * Getter for alias.
     * 
     * @return the alias
     */
    public String getAlias() {
        return this.alias;
    }

    /**
     * Sets the alias.
     * 
     * @param alias the alias to set
     */
    public void setAlias(String alias) {
        this.alias = alias;
    }

    /**
     * Getter for tableName.
     * 
     * @return the tableName
     */
    public String getTableName() {
        return this.tableName;
    }

    /**
     * Sets the tableName.
     * 
     * @param tableName the tableName to set
     */
    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#clone()
     */
    @SuppressWarnings("unchecked")
    @Override
    public Object clone() throws CloneNotSupportedException {
        ExternalDbMapTable cloned = (ExternalDbMapTable) super.clone();
        if (customConditionsEntries != null) {
            cloned.customConditionsEntries = (List<ExternalDbMapEntry>) ((ArrayList) customConditionsEntries).clone();
            int listSizecustomConditionsEntries = customConditionsEntries.size();
            for (int i = 0; i < listSizecustomConditionsEntries; i++) {
                cloned.customConditionsEntries.set(i, (ExternalDbMapEntry) cloned.customConditionsEntries.get(i)
                        .clone());
            }
        }
        if (metadataTableEntries != null) {
            cloned.metadataTableEntries = (List<ExternalDbMapEntry>) ((ArrayList) metadataTableEntries).clone();
            int listSizemetadataTableEntries = metadataTableEntries.size();
            for (int i = 0; i < listSizemetadataTableEntries; i++) {
                cloned.metadataTableEntries.set(i, (ExternalDbMapEntry) cloned.metadataTableEntries.get(i).clone());
            }
        }
        return cloned;
    }

}
