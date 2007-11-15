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

import org.talend.commons.utils.VersionUtils;

/**
 * DOC ggu class global comment. Detailled comment <br/>
 * 
 */
public class DBTableForDelimitedBean {

    /**
     * this var is total of the atrributes.
     * 
     * if add a new attribute, need change this NUM +1.
     */
    public static final int TOTAL = 29;

    private String name;

    private String purpose;

    private String description;

    private String version;

    private String status;

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

    private String tableName;

    private String originalTableName;

    private String label;

    private String originalLabel;

    private String comment;

    private String defaultValue;

    private boolean key;

    private int length;

    private boolean nullable;

    private String pattern;

    private int precision;

    private String talendType;

    private String dbType;

    /**
     * DOC ggu DatabaseTableForDelimitedBean constructor comment.
     */
    public DBTableForDelimitedBean() {
        super();
    }

    /**
     * Getter for name.
     * 
     * @return the name
     */
    public String getName() {
        return this.name;
    }

    /**
     * Sets the name.
     * 
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Getter for purpose.
     * 
     * @return the purpose
     */
    public String getPurpose() {
        return this.purpose;
    }

    /**
     * Sets the purpose.
     * 
     * @param purpose the purpose to set
     */
    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    /**
     * Getter for description.
     * 
     * @return the description
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * Sets the description.
     * 
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Getter for version.
     * 
     * @return the version
     */
    public String getVersion() {
        if (this.version == null || "".equals(version.trim())) {
            this.version = VersionUtils.DEFAULT_VERSION;
        }
        return this.version;
    }

    /**
     * Sets the version.
     * 
     * @param version the version to set
     */
    public void setVersion(String version) {
        this.version = version;
    }

    /**
     * Getter for status.
     * 
     * @return the status
     */
    public String getStatus() {
        return this.status;
    }

    /**
     * Sets the status.
     * 
     * @param status the status to set
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * Getter for databaseType.
     * 
     * @return the databaseType
     */
    public String getDatabaseType() {
        return this.databaseType;
    }

    /**
     * Sets the databaseType.
     * 
     * @param databaseType the databaseType to set
     */
    public void setDatabaseType(String databaseType) {
        this.databaseType = databaseType;
    }

    /**
     * Getter for connectionStr.
     * 
     * @return the connectionStr
     */
    public String getConnectionStr() {
        return this.connectionStr;
    }

    /**
     * Sets the connectionStr.
     * 
     * @param connectionStr the connectionStr to set
     */
    public void setConnectionStr(String connectionStr) {
        this.connectionStr = connectionStr;
    }

    /**
     * Getter for login.
     * 
     * @return the login
     */
    public String getLogin() {
        return this.login;
    }

    /**
     * Sets the login.
     * 
     * @param login the login to set
     */
    public void setLogin(String login) {
        this.login = login;
    }

    /**
     * Getter for password.
     * 
     * @return the password
     */
    public String getPassword() {
        return this.password;
    }

    /**
     * Sets the password.
     * 
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Getter for server.
     * 
     * @return the server
     */
    public String getServer() {
        return this.server;
    }

    /**
     * Sets the server.
     * 
     * @param server the server to set
     */
    public void setServer(String server) {
        this.server = server;
    }

    /**
     * Getter for port.
     * 
     * @return the port
     */
    public String getPort() {
        return this.port;
    }

    /**
     * Sets the port.
     * 
     * @param port the port to set
     */
    public void setPort(String port) {
        this.port = port;
    }

    /**
     * Getter for database.
     * 
     * @return the database
     */
    public String getDatabase() {
        return this.database;
    }

    /**
     * Sets the database.
     * 
     * @param database the database to set
     */
    public void setDatabase(String database) {
        this.database = database;
    }

    /**
     * Getter for dbSchema.
     * 
     * @return the dbSchema
     */
    public String getDbSchema() {
        return this.dbSchema;
    }

    /**
     * Sets the dbSchema.
     * 
     * @param dbSchema the dbSchema to set
     */
    public void setDbSchema(String dbSchema) {
        this.dbSchema = dbSchema;
    }

    /**
     * Getter for dataSource.
     * 
     * @return the dataSource
     */
    public String getDataSource() {
        return this.dataSource;
    }

    /**
     * Sets the dataSource.
     * 
     * @param dataSource the dataSource to set
     */
    public void setDataSource(String dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * Getter for file.
     * 
     * @return the file
     */
    public String getFile() {
        return this.file;
    }

    /**
     * Sets the file.
     * 
     * @param file the file to set
     */
    public void setFile(String file) {
        this.file = file;
    }

    /**
     * Getter for dbRootPath.
     * 
     * @return the dbRootPath
     */
    public String getDbRootPath() {
        return this.dbRootPath;
    }

    /**
     * Sets the dbRootPath.
     * 
     * @param dbRootPath the dbRootPath to set
     */
    public void setDbRootPath(String dbRootPath) {
        this.dbRootPath = dbRootPath;
    }

    /**
     * Getter for tableName.
     * 
     * @return the tableName
     */
    public String getTableName() {
        if (this.tableName == null || "".equals(tableName.trim())) {
            return this.originalTableName;
        }
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

    /**
     * Getter for originalTableName.
     * 
     * @return the originalTableName
     */
    public String getOriginalTableName() {
        return this.originalTableName;
    }

    /**
     * Sets the originalTableName.
     * 
     * @param originalTableName the originalTableName to set
     */
    public void setOriginalTableName(String originalTableName) {
        this.originalTableName = originalTableName;
    }

    /**
     * Getter for label.
     * 
     * @return the label
     */
    public String getLabel() {
        if (this.label == null || "".equals(label.trim())) {
            return this.originalLabel;
        }
        return this.label;
    }

    /**
     * Sets the label.
     * 
     * @param label the label to set
     */
    public void setLabel(String label) {
        this.label = label;
    }

    /**
     * Getter for originalLabel.
     * 
     * @return the originalLabel
     */
    public String getOriginalLabel() {
        return this.originalLabel;
    }

    /**
     * Sets the originalLabel.
     * 
     * @param originalLabel the originalLabel to set
     */
    public void setOriginalLabel(String originalLabel) {
        this.originalLabel = originalLabel;
    }

    /**
     * Getter for comment.
     * 
     * @return the comment
     */
    public String getComment() {
        return this.comment;
    }

    /**
     * Sets the comment.
     * 
     * @param comment the comment to set
     */
    public void setComment(String comment) {
        this.comment = comment;
    }

    /**
     * Getter for defaultValue.
     * 
     * @return the defaultValue
     */
    public String getDefaultValue() {
        return this.defaultValue;
    }

    /**
     * Sets the defaultValue.
     * 
     * @param defaultValue the defaultValue to set
     */
    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    /**
     * Getter for key.
     * 
     * @return the key
     */
    public boolean isKey() {
        return this.key;
    }

    /**
     * Sets the key.
     * 
     * @param key the key to set
     */
    public void setKey(boolean key) {
        this.key = key;
    }

    /**
     * Getter for length.
     * 
     * @return the length
     */
    public int getLength() {
        return this.length;
    }

    /**
     * Sets the length.
     * 
     * @param length the length to set
     */
    public void setLength(int length) {
        this.length = length;
    }

    /**
     * Getter for nullable.
     * 
     * @return the nullable
     */
    public boolean isNullable() {
        return this.nullable;
    }

    /**
     * Sets the nullable.
     * 
     * @param nullable the nullable to set
     */
    public void setNullable(boolean nullable) {
        this.nullable = nullable;
    }

    /**
     * Getter for pattern.
     * 
     * @return the pattern
     */
    public String getPattern() {
        return this.pattern;
    }

    /**
     * Sets the pattern.
     * 
     * @param pattern the pattern to set
     */
    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    /**
     * Getter for precision.
     * 
     * @return the precision
     */
    public int getPrecision() {
        return this.precision;
    }

    /**
     * Sets the precision.
     * 
     * @param precision the precision to set
     */
    public void setPrecision(int precision) {
        this.precision = precision;
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
     * Sets the talendType.
     * 
     * @param talendType the talendType to set
     */
    public void setTalendType(String talendType) {
        this.talendType = talendType;
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
     * Sets the dbType.
     * 
     * @param dbType the dbType to set
     */
    public void setDbType(String dbType) {
        this.dbType = dbType;
    }

}
