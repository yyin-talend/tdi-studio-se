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
package org.talend.repository.ui.actions.metadata.database;

/**
 * DOC ggu class global comment. Detailled comment
 */
public class TalendTypeMappingBean {

    private String dbName;

    private String tableName;

    private String columnName;

    private String talendType;

    private String dbType;

    /**
     * DOC ggu TalendTypeMappingBean constructor comment.
     * 
     * @param dbName
     * @param tableName
     * @param columnName
     * @param talendType
     * @param dbType
     */
    public TalendTypeMappingBean(String dbName, String tableName, String columnName, String talendType, String dbType) {
        super();
        this.dbName = dbName;
        this.tableName = tableName;
        this.columnName = columnName;
        this.talendType = talendType;
        this.dbType = dbType;
    }

    /**
     * Getter for talendType.
     * 
     * @return the talendType
     */
    public String getTalendType() {
        return this.talendType;
    }

    /**
     * Getter for dbType.
     * 
     * @return the dbType
     */
    public String getDbType() {
        return this.dbType;
    }

    /**
     * Getter for dbName.
     * 
     * @return the dbName
     */
    public String getDbName() {
        return this.dbName;
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
     * Getter for columnName.
     * 
     * @return the columnName
     */
    public String getColumnName() {
        return this.columnName;
    }

}
