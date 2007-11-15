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
package org.talend.designer.mapper.model.table;

import org.talend.core.model.metadata.IMetadataColumn;
import org.talend.core.model.process.EConnectionType;
import org.talend.core.model.process.EParameterFieldType;
import org.talend.core.model.process.IElementParameter;
import org.talend.core.model.process.INode;
import org.talend.designer.mapper.external.connection.IOConnection;
import org.talend.designer.mapper.external.data.ExternalMapperTable;
import org.talend.designer.mapper.managers.MapperManager;
import org.talend.designer.mapper.model.tableentry.AbstractInOutTableEntry;
import org.talend.designer.mapper.model.tableentry.InputColumnTableEntry;

/**
 * DOC amaumont class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 */
public class InputTable extends AbstractInOutTable {

    /**
     * if innerJoin is true and current lookup row not found, current main row will be rejected.
     */
    private boolean innerJoin; // else outer join

    private ILookupType matchingMode = TMAP_MATCHING_MODE.UNIQUE_MATCH;
    
    /**
     * DOC amaumont InputTable constructor comment.
     * 
     * @param mapperManager
     * 
     * @param metadataTable
     * @param externalMapperTable can be null
     * @param mainConnection
     */
    public InputTable(MapperManager mapperManager, IOConnection connection, String name) {
        super(mapperManager, connection, name);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.mapper.model.table.AbstractInOutTable#initFromExternalData(org.talend.designer.mapper.external.data.ExternalMapperTable)
     */
    @Override
    public void initFromExternalData(ExternalMapperTable externalMapperTable) {
        super.initFromExternalData(externalMapperTable);
        if (externalMapperTable != null) {
            this.innerJoin = externalMapperTable.isInnerJoin();
            this.matchingMode = TMAP_MATCHING_MODE.parse(externalMapperTable.getMatchingMode());
            if (matchingMode == null) {
                if (mapperManager.isTableHasAtLeastOneHashKey(this)) {
                    matchingMode = TMAP_MATCHING_MODE.UNIQUE_MATCH;
                } else {
                    matchingMode = TMAP_MATCHING_MODE.ALL_ROWS;
                }
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.mapper.model.table.DataMapTable#getNewTableEntry(org.talend.core.model.metadata.IMetadataColumn)
     */
    @Override
    protected AbstractInOutTableEntry getNewTableEntry(IMetadataColumn column) {
        return new InputColumnTableEntry(this, column);
    }

    @Override
    public String getName() {
        return getConnection().getName();
    }

    public boolean isMainConnection() {
        return EConnectionType.FLOW_MAIN == getConnection().getConnectionType();
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

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.mapper.model.table.AbstractInOutTable#hasReadOnlyMetadataColumns()
     */
    @Override
    public boolean hasReadOnlyMetadataColumns() {

        boolean hasReadOnlyMetadataColumns = false;

        IOConnection connection = getConnection();

        if (connection != null) {
            INode source = connection.getSource();
            if (source != null) {
                hasReadOnlyMetadataColumns = connection.isReadOnly() || !connection.isActivate() || source.isReadOnly()
                        || !source.isActivate();

                if (!hasReadOnlyMetadataColumns) {
                    for (IElementParameter param : source.getElementParameters()) {
                        if (param.getField() == EParameterFieldType.SCHEMA_TYPE) {
                            if (param.isReadOnly()) {
                                hasReadOnlyMetadataColumns = true;
                                break;
                            }
                        }
                    }
                }
            }
        }
        return hasReadOnlyMetadataColumns;
    }

    /**
     * Getter for lookupType.
     * @return the lookupType
     */
    public ILookupType getMatchingMode() {
        return this.matchingMode;
    }

    
    /**
     * Sets the lookupType.
     * @param lookupType the lookupType to set
     */
    public void setMatchingMode(ILookupType lookupType) {
        this.matchingMode = lookupType;
    }

}
