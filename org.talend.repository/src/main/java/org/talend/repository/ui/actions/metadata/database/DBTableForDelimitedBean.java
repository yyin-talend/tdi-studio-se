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

import org.talend.repository.ui.actions.metadata.importing.bean.TablesForDelimitedBean;

/**
 * DOC ggu class global comment. Detailled comment <br/>
 * 
 */
public class DBTableForDelimitedBean extends TablesForDelimitedBean {

    private static final int TOTAL = 14;

    private String databaseType;

    private String connectionStr;

    private String login;

    private String password;

    private String server;

    private String port;

    private String database;

    private String dbSchema;

    private String dataSource;

    private String file;

    private String dbRootPath;

    private String originalTableName;

    private String originalLabel;

    private String dbType;

    /**
     * DOC ggu DatabaseTableForDelimitedBean constructor comment.
     */
    public DBTableForDelimitedBean() {
        super();
    }

    public String getDatabaseType() {
        return this.databaseType;
    }

    public void setDatabaseType(String databaseType) {
        this.databaseType = databaseType;
    }

    public String getConnectionStr() {
        return this.connectionStr;
    }

    public void setConnectionStr(String connectionStr) {
        this.connectionStr = connectionStr;
    }

    public String getLogin() {
        return this.login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getServer() {
        return this.server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public String getPort() {
        return this.port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getDatabase() {
        return this.database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public String getDbSchema() {
        return this.dbSchema;
    }

    public void setDbSchema(String dbSchema) {
        this.dbSchema = dbSchema;
    }

    public String getDataSource() {
        return this.dataSource;
    }

    public void setDataSource(String dataSource) {
        this.dataSource = dataSource;
    }

    public String getFile() {
        return this.file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getDbRootPath() {
        return this.dbRootPath;
    }

    public void setDbRootPath(String dbRootPath) {
        this.dbRootPath = dbRootPath;
    }

    public String getOriginalTableName() {
        return this.originalTableName;
    }

    public void setOriginalTableName(String originalTableName) {
        this.originalTableName = originalTableName;
    }

    public String getOriginalLabel() {
        return this.originalLabel;
    }

    public void setOriginalLabel(String originalLabel) {
        this.originalLabel = originalLabel;
    }

    public String getDbType() {
        return this.dbType;
    }

    public void setDbType(String dbType) {
        this.dbType = dbType;
    }

    @Override
    public int getVarNum() {
        return TOTAL + super.getVarNum();
    }

    public int getIndexDatabaseType() {
        return 5;
    }

    @Override
    public int getIndexTableName() {
        return getIndexOriginalTableName() - 1;
    }

    public int getIndexOriginalTableName() {
        return 17;
    }

    public int getIndexOriginalLabel() {
        return 19;
    }

    @Override
    public int getIndexLabel() {
        return getIndexOriginalLabel() - 1;
    }

    @Override
    public int getIndexLengh() {
        return 23;
    }

}
