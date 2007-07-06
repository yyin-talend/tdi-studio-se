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
package org.talend.designer.mapper.external.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * DOC amaumont class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 */
public class ExternalMapperTable implements Serializable, Cloneable {

    /**
     * 
     */
    private static final long serialVersionUID = 8768004929161302382L;

    private List<ExternalMapperTableEntry> constraintTableEntries;

    private List<ExternalMapperTableEntry> metadataTableEntries;

    private String name;

    private boolean minimized;
    
    private String sizeState;

    /**
     * Used only for outputs.
     */
    private boolean reject;

    /**
     * Used only for outputs.
     */
    private boolean rejectInnerJoin;

    private String expressionFilter;
    
    private boolean activateExpressionFilter;
 
    private String matchingMode;
    
    /**
     * Used only for lookup inputs. Reject main row if this lookup row doesn't exist.
     */
    private boolean innerJoin;

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

    /**
     * Getter for rejectInnerJoin.
     * 
     * @return the rejectInnerJoin
     */
    public boolean isRejectInnerJoin() {
        return this.rejectInnerJoin;
    }

    /**
     * Sets the rejectInnerJoin.
     * 
     * @param rejectInnerJoin the rejectInnerJoin to set
     */
    public void setRejectInnerJoin(boolean rejectInnerJoin) {
        this.rejectInnerJoin = rejectInnerJoin;
    }

    /**
     * Getter for innerJoin.
     * 
     * @return the innerJoin
     */
    public boolean isInnerJoin() {
        return this.innerJoin;
    }

    /**
     * Sets the innerJoin.
     * 
     * @param innerJoin the innerJoin to set
     */
    public void setInnerJoin(boolean innerJoin) {
        this.innerJoin = innerJoin;
    }

    
    
    
    public String getExpressionFilter() {
        return this.expressionFilter;
    }

    
    public void setExpressionFilter(String expressionFilter) {
        this.expressionFilter = expressionFilter;
    }
    
    /**
     * Getter for activateExpressionFilter.
     * @return the activateExpressionFilter
     */
    public boolean isActivateExpressionFilter() {
        return this.activateExpressionFilter;
    }
    
    /**
     * Sets the activateExpressionFilter.
     * @param activateExpressionFilter the activateExpressionFilter to set
     */
    public void setActivateExpressionFilter(boolean activateExpressionFilter) {
        this.activateExpressionFilter = activateExpressionFilter;
    }

    /**
     * Getter for lookupType.
     * @return the lookupType
     */
    public String getMatchingMode() {
        return this.matchingMode;
    }

    
    /**
     * Sets the lookupType.
     * @param matchingMode the lookupType to set
     */
    public void setMatchingMode(String matchingMode) {
        this.matchingMode = matchingMode;
    }

    
    
    
    /**
     * Getter for tableSizeState.
     * @return the tableSizeState
     */
    public String getSizeState() {
        return this.sizeState;
    }

    
    /**
     * Sets the tableSizeState.
     * @param tableSizeState the tableSizeState to set
     */
    public void setSizeState(String tableSizeState) {
        this.sizeState = tableSizeState;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#clone()
     */
    @SuppressWarnings("unchecked")
    @Override
    public Object clone() throws CloneNotSupportedException {
        ExternalMapperTable cloned = (ExternalMapperTable) super.clone();
        if (constraintTableEntries != null) {
            cloned.constraintTableEntries = (List<ExternalMapperTableEntry>) ((ArrayList) constraintTableEntries)
                    .clone();
            int listSizeconstraintTableEntries = constraintTableEntries.size();
            for (int i = 0; i < listSizeconstraintTableEntries; i++) {
                cloned.constraintTableEntries.set(i, (ExternalMapperTableEntry) cloned.constraintTableEntries.get(i)
                        .clone());
            }
        }
        if (metadataTableEntries != null) {
            cloned.metadataTableEntries = (List<ExternalMapperTableEntry>) ((ArrayList) metadataTableEntries).clone();
            int listSizemetadataTableEntries = metadataTableEntries.size();
            for (int i = 0; i < listSizemetadataTableEntries; i++) {
                cloned.metadataTableEntries.set(i, (ExternalMapperTableEntry) cloned.metadataTableEntries.get(i)
                        .clone());

            }
        }
        return cloned;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (this.activateExpressionFilter ? 1231 : 1237);
        result = prime * result + ((this.constraintTableEntries == null) ? 0 : this.constraintTableEntries.hashCode());
        result = prime * result + ((this.expressionFilter == null) ? 0 : this.expressionFilter.hashCode());
        result = prime * result + (this.innerJoin ? 1231 : 1237);
        result = prime * result + ((this.matchingMode == null) ? 0 : this.matchingMode.hashCode());
        result = prime * result + ((this.metadataTableEntries == null) ? 0 : this.metadataTableEntries.hashCode());
        result = prime * result + (this.minimized ? 1231 : 1237);
        result = prime * result + ((this.name == null) ? 0 : this.name.hashCode());
        result = prime * result + (this.reject ? 1231 : 1237);
        result = prime * result + (this.rejectInnerJoin ? 1231 : 1237);
        result = prime * result + ((this.sizeState == null) ? 0 : this.sizeState.hashCode());
        return result;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final ExternalMapperTable other = (ExternalMapperTable) obj;
        if (this.activateExpressionFilter != other.activateExpressionFilter)
            return false;
        if (this.constraintTableEntries == null) {
            if (other.constraintTableEntries != null)
                return false;
        } else if (!this.constraintTableEntries.equals(other.constraintTableEntries))
            return false;
        if (this.expressionFilter == null) {
            if (other.expressionFilter != null)
                return false;
        } else if (!this.expressionFilter.equals(other.expressionFilter))
            return false;
        if (this.innerJoin != other.innerJoin)
            return false;
        if (this.matchingMode == null) {
            if (other.matchingMode != null)
                return false;
        } else if (!this.matchingMode.equals(other.matchingMode))
            return false;
        if (this.metadataTableEntries == null) {
            if (other.metadataTableEntries != null)
                return false;
        } else if (!this.metadataTableEntries.equals(other.metadataTableEntries))
            return false;
        if (this.minimized != other.minimized)
            return false;
        if (this.name == null) {
            if (other.name != null)
                return false;
        } else if (!this.name.equals(other.name))
            return false;
        if (this.reject != other.reject)
            return false;
        if (this.rejectInnerJoin != other.rejectInnerJoin)
            return false;
        if (this.sizeState == null) {
            if (other.sizeState != null)
                return false;
        } else if (!this.sizeState.equals(other.sizeState))
            return false;
        return true;
    }

    
    
}
