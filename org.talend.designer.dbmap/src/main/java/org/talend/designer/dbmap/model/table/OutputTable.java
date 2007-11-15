// ============================================================================
//
// Copyright (C) 2006-2007 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.designer.dbmap.model.table;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.talend.commons.ui.swt.extended.table.ExtendedTableModel;
import org.talend.core.model.metadata.IMetadataColumn;
import org.talend.core.model.metadata.IMetadataTable;
import org.talend.core.model.process.IProcess;
import org.talend.designer.dbmap.external.data.ExternalDbMapEntry;
import org.talend.designer.dbmap.external.data.ExternalDbMapTable;
import org.talend.designer.dbmap.managers.MapperManager;
import org.talend.designer.dbmap.model.tableentry.AbstractInOutTableEntry;
import org.talend.designer.dbmap.model.tableentry.FilterTableEntry;
import org.talend.designer.dbmap.model.tableentry.OutputColumnTableEntry;

/**
 * DOC amaumont class global comment. Detailled comment <br/>
 * 
 * $Id: OutputTable.java 1795 2007-02-04 04:15:56Z bqian $
 * 
 */
public class OutputTable extends AbstractInOutTable {

    protected List<FilterTableEntry> filterTableEntries = new ArrayList<FilterTableEntry>(0);

    private ExtendedTableModel<FilterTableEntry> tableFiltersEntriesModel;

    private String uniqueName;

    private String tableName;

    public OutputTable(MapperManager mapperManager, IMetadataTable metadataTable, String uniqueName, String tableName) {
        super(mapperManager, metadataTable, uniqueName);
        if (uniqueName == null) {
            final IProcess process = mapperManager.getComponent().getProcess();
            this.uniqueName = process.generateUniqueConnectionName("table"); //$NON-NLS-1$
            process.addUniqueConnectionName(this.uniqueName);
        } else {
            this.uniqueName = uniqueName;
        }
        this.tableName = tableName;
        this.tableFiltersEntriesModel = new ExtendedTableModel<FilterTableEntry>(
                uniqueName + " : model for Filters", filterTableEntries); //$NON-NLS-1$
    }

    public void initFromExternalData(ExternalDbMapTable externalMapperTable) {
        super.initFromExternalData(externalMapperTable);
        List<IMetadataColumn> columns = this.metadataTable.getListColumns();
        Map<String, ExternalDbMapEntry> nameToPerTabEntry = new HashMap<String, ExternalDbMapEntry>();
        if (externalMapperTable != null && externalMapperTable.getMetadataTableEntries() != null) {
            for (ExternalDbMapEntry perTableEntry : externalMapperTable.getMetadataTableEntries()) {
                nameToPerTabEntry.put(perTableEntry.getName(), perTableEntry);
            }
        }

        for (IMetadataColumn column : columns) {
            AbstractInOutTableEntry columnEntry = getNewTableEntry(column);
            ExternalDbMapEntry externalMapperTableEntry = nameToPerTabEntry.get(columnEntry.getMetadataColumn()
                    .getLabel());
            // Entry match with current column
            if (externalMapperTableEntry != null) {
                columnEntry.setExpression(externalMapperTableEntry.getExpression());
            }
            dataMapTableEntries.add(columnEntry);
        }
        if (externalMapperTable != null) {
            List<ExternalDbMapEntry> externalConstraintTableEntries = externalMapperTable.getCustomConditionsEntries();
            if (externalConstraintTableEntries != null) {
                for (ExternalDbMapEntry entry : externalConstraintTableEntries) {
                    FilterTableEntry filterTableEntry = new FilterTableEntry(this, entry.getName(), entry
                            .getExpression());
                    // mapperManager.getProblemsManager().checkProblemsForTableEntry(filterTableEntry, false);
                    addFilterEntry(filterTableEntry);
                }
            }
        }
    }

    @Override
    protected AbstractInOutTableEntry getNewTableEntry(IMetadataColumn metadataColumn) {
        return new OutputColumnTableEntry(this, metadataColumn);
    }

    public void addFilterEntry(FilterTableEntry constraintTableEntry) {
        this.tableFiltersEntriesModel.add(constraintTableEntry);
    }

    public void addFilterEntry(FilterTableEntry constraintTableEntry, Integer index) {
        this.tableFiltersEntriesModel.add(constraintTableEntry, index);
    }

    public void removeFilterEntry(FilterTableEntry constraintTableEntry) {
        this.tableFiltersEntriesModel.remove(constraintTableEntry);
    }

    public List<FilterTableEntry> getFilterEntries() {
        return this.tableFiltersEntriesModel.getBeansList();
    }

    /**
     * Getter for tableFiltersEntriesModel.
     * 
     * @return the tableFiltersEntriesModel
     */
    public ExtendedTableModel<FilterTableEntry> getTableFiltersEntriesModel() {
        return this.tableFiltersEntriesModel;
    }

    /**
     * Getter for uniqueName.
     * 
     * @return the uniqueName
     */
    public String getUniqueName() {
        return this.uniqueName;
    }

    /**
     * Getter for tableName.
     * 
     * @return the tableName
     */
    public String getTableName() {
        return this.tableName;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.dbmap.model.table.IDataMapTable#getName()
     */
    @Override
    public String getName() {
        return getUniqueName();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.dbmap.model.table.IDataMapTable#getTitle()
     */
    @Override
    public String getTitle() {
        return getTableName() + " (" + getUniqueName() + ")";
    }

}
