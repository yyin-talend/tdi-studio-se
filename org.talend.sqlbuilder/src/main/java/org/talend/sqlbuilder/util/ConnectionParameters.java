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
package org.talend.sqlbuilder.util;

import java.util.ArrayList;
import java.util.Hashtable;

import org.talend.core.CorePlugin;
import org.talend.core.context.Context;
import org.talend.core.context.RepositoryContext;
import org.talend.core.language.ECodeLanguage;
import org.talend.core.model.metadata.IMetadataColumn;
import org.talend.core.model.metadata.IMetadataTable;
import org.talend.core.model.metadata.MetadataTable;
import org.talend.core.model.metadata.builder.connection.DatabaseConnection;
import org.talend.core.model.metadata.builder.connection.Query;
import org.talend.repository.model.RepositoryNode;
import org.talend.repository.ui.utils.DataStringConnection;
import org.talend.sqlbuilder.Messages;
import org.talend.sqlbuilder.repository.utility.SQLBuilderRepositoryNodeManager;

/**
 * This class is used for representing connection parameters. <br/>
 * 
 * @author ftang
 * 
 */
public class ConnectionParameters {

    private static boolean isNodeReadOnly;
    
    private IMetadataTable metadataTable;
    
    private String schemaName;
    
    private boolean isSchemaRepository;
    
    private String query;

    private String port;

    private String userName;

    private String password;

    private String host;

    private String dbName;

    private String dbType;

    private String datasource;

    private String filename;

    private String repositoryName;

    private String selectedComponentName;

    private Query queryObject;

    private RepositoryNode repositoryNodeBuiltIn;

    private String connectionComment;

    private boolean status = true;

    private String schema;

    private static Hashtable<String, String> hashTable = new Hashtable<String, String>();

    static {
        try {
            hashTable.put("MySQL", "MySQL"); //$NON-NLS-1$ //$NON-NLS-2$
            hashTable.put("PostgreSQL", "PostgreSQL"); //$NON-NLS-1$ //$NON-NLS-2$
            hashTable.put("Oracle", "Oracle with SID"); //$NON-NLS-1$ //$NON-NLS-2$
            hashTable.put("Generic ODBC", "Generic ODBC"); //$NON-NLS-1$ //$NON-NLS-2$
            hashTable.put("Microsoft SQL (Odbc driver)", "Microsoft SQL Server (Odbc driver)"); //$NON-NLS-1$ //$NON-NLS-2$
            hashTable.put("IBM DB2", "IBM DB2"); //$NON-NLS-1$ //$NON-NLS-2$
            hashTable.put("Sybase ASE", "Sybase ASE"); //$NON-NLS-1$ //$NON-NLS-2$
            hashTable.put("Sybase IQ", "Sybase IQ"); //$NON-NLS-1$ //$NON-NLS-2$

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Sets the connectionComment.
     * 
     * @param connectionComment the connectionComment to set
     */
    public void setConnectionComment(String connectionComment) {
        this.connectionComment = connectionComment;
    }

    /**
     * Getter for schema.
     * 
     * @return the schema
     */
    public String getSchema() {
        return this.schema;
    }

    /**
     * Sets the schema.
     * 
     * @param schema the schema to set
     */
    public void setSchema(String schema) {
        this.schema = trimInvertedComma(schema);
    }

    /**
     * Getter for status.
     * 
     * @return the status
     */
    public boolean isStatus() {
        return this.status;
    }

    /**
     * Sets the status.
     * 
     * @param status the status to set
     */
    public void setStatus(boolean status) {
        this.status = status;
    }

    /**
     * Getter for connectionComment.
     * 
     * @return the connectionComment
     */
    public String getConnectionComment() {
        return this.connectionComment;
    }

    /**
     * Getter for queryObject.
     * 
     * @return the queryObject
     */
    public Query getQueryObject() {
        return queryObject;
    }

    /**
     * Sets the queryObject.
     * 
     * @param queryObject the queryObject to set
     */
    public void setQueryObject(Query queryObject) {
        this.queryObject = queryObject;
        if (queryObject != null && queryObject.getValue() != null) {
            query = queryObject.getValue();
        }
    }

    /**
     * ConnectionParameters constructor.
     */
    public ConnectionParameters() {

    }

    /**
     * Getter for filename.
     * 
     * @return the filename
     */
    public String getFilename() {
        return this.filename;
    }

    /**
     * Sets the filename.
     * 
     * @param filename the filename to set
     */
    public void setFilename(String filename) {
        this.filename = trimInvertedComma(filename);
    }

    /**
     * Getter for datasource.
     * 
     * @return the datasource
     */
    public String getDatasource() {
        return this.datasource;
    }

    /**
     * Sets the datasource.
     * 
     * @param datasource the datasource to set
     */
    public void setDatasource(String datasource) {
        this.datasource = trimInvertedComma(datasource);
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
        this.dbType = trimInvertedComma(hashTable.get(dbType));
    }

    public boolean isRepository() {
        return repositoryName != null;
    }

    /**
     * Getter for dbName.
     * 
     * @return the dbName
     */
    public String getDbName() {
        return dbName;
    }

    /**
     * Sets the dbName.
     * 
     * @param dbName the dbName to set
     */
    public void setDbName(String dbName) {
        this.dbName = trimInvertedComma(dbName);
        if (this.datasource == null || this.datasource.equals("")) { //$NON-NLS-1$
            this.datasource = this.dbName;
        }
    }

    /**
     * Getter for host.
     * 
     * @return the host
     */
    public String getHost() {
        return host;
    }

    /**
     * Sets the host.
     * 
     * @param host the host to set
     */
    public void setHost(String host) {
        this.host = trimInvertedComma(host);
    }

    /**
     * Getter for password.
     * 
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the password.
     * 
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = trimInvertedComma(password);
    }

    /**
     * Getter for port.
     * 
     * @return the port
     */
    public String getPort() {
        return port;
    }

    /**
     * Sets the port.
     * 
     * @param port the port to set
     */
    public void setPort(String port) {
        this.port = trimInvertedComma(port);
    }

    /**
     * Getter for query.
     * 
     * @return the query
     */
    public String getQuery() {
        return query;
    }

    /**
     * Sets the query.
     * 
     * @param query the query to set
     */
    public void setQuery(String query) {
        this.query = TextUtil.removeQuots(query);
    }

    /**
     * Getter for userName.
     * 
     * @return the userName
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Sets the userName.
     * 
     * @param userName the userName to set
     */
    public void setUserName(String userName) {
        this.userName = trimInvertedComma(userName);
    }

    /**
     * Getter for repositoryName.
     * 
     * @return the repositoryName
     */
    public String getRepositoryName() {
        return repositoryName;
    }

    /**
     * Sets the repositoryName.
     * 
     * @param repositoryName the repositoryName to set
     */
    public void setRepositoryName(String repositoryName) {
        this.repositoryName = trimInvertedComma(repositoryName);
    }

    /**
     * Trims the "'" of the input String.
     * 
     * @param input
     * @return the String without " ' "
     */
    private String trimInvertedComma(String input) {
        String out = null;
        if (isJavaProject()) {
            out = input.replaceAll("\"", "");
        } else {
            out = input.replaceAll("\'", ""); //$NON-NLS-1$ //$NON-NLS-2$
        }
        return out;
    }

    /**
     * Get url String , use it to create Connection.
     * 
     * @return url String from user input parameters.
     */
    public String getURL() {
        if (isRepository()) {
            throw new RuntimeException(Messages.getString("ConnectionParameters.exceptionMessage")); //$NON-NLS-1$
        }
        DataStringConnection urlDataStringConnection = new DataStringConnection();
        int dbIndex = urlDataStringConnection.getIndexOfLabel(dbType);
        urlDataStringConnection.setSelectionIndex(dbIndex);
        String url = urlDataStringConnection.getString(-1, getHost(), getUserName(), getPassword(), getPort(),
                getDbName(), getFilename(), getDatasource());
        return url;

    }

    /**
     * Sets selected component name.
     * 
     * @param selectedComponentName
     */
    public void setSelectedComponentName(String selectedComponentName) {
        this.selectedComponentName = trimInvertedComma(selectedComponentName);

    }

    /**
     * Gets selected component name.
     * 
     * @return
     */
    public String getSelectedComponentName() {
        return selectedComponentName != null ? selectedComponentName : ""; //$NON-NLS-1$
    }

    /**
     * Getter for repositoryNodeBuiltIn.
     * 
     * @return the repositoryNodeBuiltIn
     */
    public RepositoryNode getRepositoryNodeBuiltIn() {
        return this.repositoryNodeBuiltIn;
    }

    /**
     * Sets the repositoryNodeBuiltIn.
     * 
     * @param repositoryNodeBuiltIn the repositoryNodeBuiltIn to set
     */
    public void setRepositoryNodeBuiltIn(RepositoryNode repositoryNodeBuiltIn) {
        this.repositoryNodeBuiltIn = repositoryNodeBuiltIn;
        if (repositoryNodeBuiltIn != null) {
            DatabaseConnection databaseConnection = (DatabaseConnection) SQLBuilderRepositoryNodeManager.getItem(
                    repositoryNodeBuiltIn).getConnection();
            status = !(databaseConnection.isDivergency());
        } else {
            status = false;
        }
    }

    public static boolean isJavaProject() {
        RepositoryContext repositoryContext = (RepositoryContext) CorePlugin.getContext().getProperty(
                Context.REPOSITORY_CONTEXT_KEY);
        ECodeLanguage codeLanguage = repositoryContext.getProject().getLanguage();
        return (codeLanguage == ECodeLanguage.JAVA);
    }

    
    public IMetadataTable getMetadataTable() {
        return this.metadataTable;
    }

    
    public void setMetadataTable(IMetadataTable metadataTable) {
        this.metadataTable = metadataTable;
    }

    
    public boolean isNodeReadOnly() {
        return ConnectionParameters.isNodeReadOnly;
    }

    
    public void setNodeReadOnly(boolean isNodeReadOnly) {
        ConnectionParameters.isNodeReadOnly = isNodeReadOnly;
    }

    
    public boolean isSchemaRepository() {
        return this.isSchemaRepository;
    }

    
    public void setSchemaRepository(boolean isSchemaRepository) {
        this.isSchemaRepository = isSchemaRepository;
    }

    
    public String getSchemaName() {
        return this.schemaName;
    }

    
    public void setSchemaName(String schemaName) {
        this.schemaName = schemaName;
    }

}
