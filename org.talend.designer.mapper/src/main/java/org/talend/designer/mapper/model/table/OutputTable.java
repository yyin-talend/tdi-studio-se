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
package org.talend.designer.mapper.model.table;

import java.util.ArrayList;
import java.util.List;

import org.talend.core.model.metadata.IMetadataColumn;
import org.talend.core.model.metadata.IMetadataTable;
import org.talend.designer.mapper.external.data.ExternalMapperTable;
import org.talend.designer.mapper.external.data.ExternalMapperTableEntry;
import org.talend.designer.mapper.model.tableentry.AbstractInOutTableEntry;
import org.talend.designer.mapper.model.tableentry.ConstraintTableEntry;
import org.talend.designer.mapper.model.tableentry.OutputColumnTableEntry;

/**
 * DOC amaumont class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 */
public class OutputTable extends AbstractInOutTable {

    protected List<ConstraintTableEntry> constraintTableEntries = new ArrayList<ConstraintTableEntry>(0);

    private boolean reject;

    public OutputTable(IMetadataTable metadataTable, ExternalMapperTable externalMapperTable, String name) {
        super(metadataTable, externalMapperTable, name);
        initFromExternalData(externalMapperTable);
    }

    private void initFromExternalData(ExternalMapperTable externalMapperTable) {
        if (externalMapperTable != null) {
            this.reject = externalMapperTable.isReject();
            List<ExternalMapperTableEntry> externalConstraintTableEntries = externalMapperTable
                    .getConstraintTableEntries();
            if (externalConstraintTableEntries != null) {
                for (ExternalMapperTableEntry entry : externalConstraintTableEntries) {
                    addConstraintEntry(new ConstraintTableEntry(this, entry.getName(), entry.getExpression()));
                }
            }
        }
    }

    @Override
    protected AbstractInOutTableEntry getNewTableEntry(IMetadataColumn metadataColumn) {
        return new OutputColumnTableEntry(this, metadataColumn);
    }

    public boolean isReject() {
        return this.reject;
    }

    public void setReject(boolean reject) {
        this.reject = reject;
    }

    public void addConstraintEntry(ConstraintTableEntry constraintTableEntry) {
        this.constraintTableEntries.add(constraintTableEntry);
    }

    public void addConstraintEntry(ConstraintTableEntry constraintTableEntry, Integer index) {
        this.constraintTableEntries.add(index, constraintTableEntry);
    }

    public void removeConstraintEntry(ConstraintTableEntry constraintTableEntry) {
        this.constraintTableEntries.remove(constraintTableEntry);
    }

    public List<ConstraintTableEntry> getConstraintEntries() {
        return this.constraintTableEntries;
    }

}
