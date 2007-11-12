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
package org.talend.repository.ui.utils;

import org.apache.log4j.Logger;
import org.talend.core.model.metadata.IMetadataConnection;
import org.talend.core.model.metadata.builder.database.ConnectionStatus;
import org.talend.core.model.metadata.builder.database.ExtractMetaDataFromDataBase;
import org.talend.repository.i18n.Messages;

/**
 * @author ocarbone
 * 
 */
public class ManagerConnection {

    private static Logger log = Logger.getLogger(ManagerConnection.class);

    private boolean isValide = false;

    String messageException = null;

    // Strings to save

    String dbTypeString;

    String urlConnectionString;

    String server;

    String username;

    String password;

    String port;

    String sidOrDatabase;

    String datasource;

    String sqlSyntax;

    String strQuote;

    String nullChar;

    String filePath;

    Integer id = null;

    private String schemaOracle;

    private String dbRootPath;

    /**
     * setValue.
     * 
     * @param id
     * @param dbType
     * @param sidStr
     * @param connectionString
     * @param server
     * @param username
     * @param password
     */
    public void setValue(Integer id, final String dbType, final String url, final String server, final String username,
            final String password, final String sidOrDatabase, final String port, final String file,
            final String datasource, final String schemaOracle) {
        this.id = id;
        this.dbTypeString = dbType;
        this.urlConnectionString = url;
        this.server = server;
        this.username = username;
        this.password = password;
        this.sidOrDatabase = sidOrDatabase;
        this.port = port;
        this.filePath = file;
        this.datasource = datasource;
        this.schemaOracle = schemaOracle;
    }

    public void setValueProperties(final String sqlSyntax, final String strQuote, final String nullChar) {
        this.sqlSyntax = sqlSyntax;
        this.strQuote = strQuote;
        this.nullChar = nullChar;
    }

    /**
     * Check connexion from the fields form.
     * 
     * @return isValide
     */
    public boolean check() {
        messageException = null;
        try {
            ConnectionStatus testConnection = ExtractMetaDataFromDataBase.testConnection(dbTypeString,
                    urlConnectionString, username, password, schemaOracle);
            isValide = testConnection.getResult();
            messageException = testConnection.getMessageException();
        } catch (Exception e) {
            log.error(Messages.getString("CommonWizard.exception") + "\n" + e.toString()); //$NON-NLS-1$ //$NON-NLS-2$
        }
        return isValide;
    }

    /**
     * DOC cantoine : Check connexion from IMetadataConnection comment. Detailled comment.
     * 
     * @return isValide
     */
    public boolean check(IMetadataConnection metadataConnection) {
        messageException = null;
        if (metadataConnection.getDbRootPath() != null && !metadataConnection.getDbRootPath().equals("")) {
            setDbRootPath(metadataConnection.getDbRootPath());
        }
        try {
            ConnectionStatus testConnection = ExtractMetaDataFromDataBase.testConnection(
                    metadataConnection.getDbType(), metadataConnection.getUrl(), metadataConnection.getUsername(),
                    metadataConnection.getPassword(), metadataConnection.getSchema());
            isValide = testConnection.getResult();
            messageException = testConnection.getMessageException();
        } catch (Exception e) {
            log.error(Messages.getString("CommonWizard.exception") + "\n" + e.toString()); //$NON-NLS-1$ //$NON-NLS-2$
        }
        return isValide;
    }

    /**
     * Getter for IsValide.
     * 
     * @return isValide
     */
    public boolean getIsValide() {
        return isValide;
    }

    /**
     * Getter for messageException.
     * 
     * @return the messageException
     */
    public String getMessageException() {
        return this.messageException;
    }

    /**
     * Sets the messageException.
     * 
     * @param messageException the messageException to set
     */
    public void setMessageException(final String messageException) {
        this.messageException = messageException;
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
        if (dbRootPath != null) {
            System.setProperty("derby.system.home", dbRootPath);
        }
        this.dbRootPath = dbRootPath;
    }
}
