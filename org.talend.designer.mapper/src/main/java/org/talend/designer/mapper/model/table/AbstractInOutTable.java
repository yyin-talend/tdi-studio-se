// ============================================================================
//
// Copyright (C) 2006-2007 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the  agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//   
// ============================================================================
package org.talend.designer.mapper.model.table;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.talend.core.model.metadata.IMetadataColumn;
import org.talend.core.model.metadata.IMetadataTable;
import org.talend.designer.mapper.external.connection.IOConnection;
import org.talend.designer.mapper.external.data.ExternalMapperTable;
import org.talend.designer.mapper.external.data.ExternalMapperTableEntry;
import org.talend.designer.mapper.managers.MapperManager;
import org.talend.designer.mapper.model.tableentry.AbstractInOutTableEntry;
import org.talend.designer.mapper.model.tableentry.ExpressionFilterEntry;

/**
 * DOC amaumont class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 */
public abstract class AbstractInOutTable extends AbstractDataMapTable {

    protected IMetadataTable metadataTable;

    private IOConnection connection;

    private ExpressionFilterEntry expressionFilterEntry;

    private boolean activateExpressionFilter;
    
    /**
     * 
     * DOC amaumont AbstractInOutTable constructor comment.
     * 
     * @param metadataTable
     * @param externalMapperTable can be null
     * @param name
     */
    public AbstractInOutTable(MapperManager mapperManager, IMetadataTable metadataTable, String name) {
        super(mapperManager, name);
        this.metadataTable = metadataTable;
    }

    /**
     * DOC amaumont AbstractInOutTable constructor comment.
     * 
     * @param mapperManager
     * @param connection2
     * @param name
     */
    public AbstractInOutTable(MapperManager mapperManager, IOConnection connection, String name) {
        this(mapperManager, connection.getTable(), name);
        this.connection = connection;
    }

    protected void initFromExternalData(ExternalMapperTable externalMapperTable) {
        super.initFromExternalData(externalMapperTable);
        expressionFilterEntry = new ExpressionFilterEntry(this); 
        if (externalMapperTable != null) {
            this.expressionFilterEntry.setExpression(externalMapperTable.getExpressionFilter());
            this.activateExpressionFilter = externalMapperTable.isActivateExpressionFilter();
        }
        List<IMetadataColumn> columns = this.metadataTable.getListColumns();
        Map<String, ExternalMapperTableEntry> nameToPerTabEntry = new HashMap<String, ExternalMapperTableEntry>();
        if (externalMapperTable != null && externalMapperTable.getMetadataTableEntries() != null) {
            for (ExternalMapperTableEntry perTableEntry : externalMapperTable.getMetadataTableEntries()) {
                nameToPerTabEntry.put(perTableEntry.getName(), perTableEntry);
            }
        }

        for (IMetadataColumn column : columns) {
            AbstractInOutTableEntry columnEntry = getNewTableEntry(column);
            ExternalMapperTableEntry externalMapperTableEntry = nameToPerTabEntry.get(columnEntry.getMetadataColumn()
                    .getLabel());
            if (externalMapperTableEntry != null) {
                columnEntry.setExpression(externalMapperTableEntry.getExpression());
                // mapperManager.getProblemsManager().checkProblemsForTableEntry(columnEntry, false);
            }
            dataMapTableEntries.add(columnEntry);
        }
    }

    protected abstract AbstractInOutTableEntry getNewTableEntry(IMetadataColumn metadataColumn);

    public IMetadataTable getMetadataTable() {
        return this.metadataTable;
    }

    public abstract boolean hasReadOnlyMetadataColumns();

    /**
     * Getter for connection.
     * 
     * @return the connection
     */
    protected IOConnection getConnection() {
        return this.connection;
    }

    
    /**
     * Getter for expressionFilter.
     * @return the expressionFilter
     */
    public ExpressionFilterEntry getExpressionFilter() {
        return this.expressionFilterEntry;
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

    
    
}
