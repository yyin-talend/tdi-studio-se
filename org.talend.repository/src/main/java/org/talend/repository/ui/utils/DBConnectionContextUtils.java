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
package org.talend.repository.ui.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.talend.core.database.EDatabaseTypeName;
import org.talend.core.language.ECodeLanguage;
import org.talend.core.language.LanguageManager;
import org.talend.core.model.metadata.builder.connection.ConnectionFactory;
import org.talend.core.model.metadata.builder.connection.DatabaseConnection;
import org.talend.core.model.metadata.types.JavaTypesManager;
import org.talend.core.model.process.IContextParameter;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.core.model.utils.ContextParameterUtils;
import org.talend.core.model.utils.TalendTextUtils;
import org.talend.designer.core.model.utils.emf.talendfile.ContextType;

/**
 * ggu class global comment. Detailled comment
 */
public final class DBConnectionContextUtils {

    private static final ECodeLanguage LANGUAGE = LanguageManager.getCurrentLanguage();

    /**
     * 
     */
    enum EDBParamName {
        AdditionalParams,
        DatasourceName,
        DBRootPath,
        FileFieldName,
        Password,
        Port,
        Schema,
        ServerName,
        SID,
        Username;
    }

    static List<IContextParameter> getDBVariables(String prefixName, DatabaseConnection conn) {
        if (conn == null || prefixName == null) {
            return Collections.emptyList();
        }

        List<IContextParameter> varList = new ArrayList<IContextParameter>();
        prefixName = prefixName + ConnectionContextHelper.LINE;
        String paramName = null;

        paramName = prefixName + EDBParamName.AdditionalParams;
        ConnectionContextHelper.createParameters(varList, paramName, conn.getAdditionalParams());

        paramName = prefixName + EDBParamName.DatasourceName;
        ConnectionContextHelper.createParameters(varList, paramName, conn.getDatasourceName());

        paramName = prefixName + EDBParamName.DBRootPath;
        ConnectionContextHelper.createParameters(varList, paramName, conn.getDBRootPath(), JavaTypesManager.DIRECTORY);

        paramName = prefixName + EDBParamName.FileFieldName;
        ConnectionContextHelper.createParameters(varList, paramName, conn.getFileFieldName(), JavaTypesManager.FILE);

        paramName = prefixName + EDBParamName.Password;
        ConnectionContextHelper.createParameters(varList, paramName, conn.getPassword());

        paramName = prefixName + EDBParamName.Port;
        ConnectionContextHelper.createParameters(varList, paramName, conn.getPort(), JavaTypesManager.INTEGER);

        paramName = prefixName + EDBParamName.Schema;
        if (conn.getProductId().equals(EDatabaseTypeName.ORACLEFORSID.getProduct())) {
            String schema = conn.getSchema();
            if (schema != null) {
                conn.setSchema(schema.toUpperCase());
            }
        }
        ConnectionContextHelper.createParameters(varList, paramName, conn.getSchema());

        paramName = prefixName + EDBParamName.ServerName;
        ConnectionContextHelper.createParameters(varList, paramName, conn.getServerName());

        paramName = prefixName + EDBParamName.SID;
        ConnectionContextHelper.createParameters(varList, paramName, conn.getSID());

        paramName = prefixName + EDBParamName.Username;
        ConnectionContextHelper.createParameters(varList, paramName, conn.getUsername());

        return varList;
    }

    static void setPropertiesForContextMode(String prefixName, DatabaseConnection conn) {
        if (conn == null || prefixName == null) {
            return;
        }
        prefixName = prefixName + ConnectionContextHelper.LINE;
        String paramName = null;

        paramName = prefixName + EDBParamName.AdditionalParams;
        conn.setAdditionalParams(ContextParameterUtils.getNewScriptCode(paramName, LANGUAGE));

        paramName = prefixName + EDBParamName.DatasourceName;
        conn.setDatasourceName(ContextParameterUtils.getNewScriptCode(paramName, LANGUAGE));

        paramName = prefixName + EDBParamName.DBRootPath;
        conn.setDBRootPath(ContextParameterUtils.getNewScriptCode(paramName, LANGUAGE));

        paramName = prefixName + EDBParamName.FileFieldName;
        conn.setFileFieldName(ContextParameterUtils.getNewScriptCode(paramName, LANGUAGE));

        paramName = prefixName + EDBParamName.Password;
        conn.setPassword(ContextParameterUtils.getNewScriptCode(paramName, LANGUAGE));

        paramName = prefixName + EDBParamName.Port;
        conn.setPort(ContextParameterUtils.getNewScriptCode(paramName, LANGUAGE));

        paramName = prefixName + EDBParamName.Schema;
        conn.setSchema(ContextParameterUtils.getNewScriptCode(paramName, LANGUAGE));

        paramName = prefixName + EDBParamName.ServerName;
        conn.setServerName(ContextParameterUtils.getNewScriptCode(paramName, LANGUAGE));

        paramName = prefixName + EDBParamName.SID;
        conn.setSID(ContextParameterUtils.getNewScriptCode(paramName, LANGUAGE));

        paramName = prefixName + EDBParamName.Username;
        conn.setUsername(ContextParameterUtils.getNewScriptCode(paramName, LANGUAGE));
    }

    /**
     * 
     * ggu Comment method "setManagerConnectionValues".
     * 
     * set the ManagerConnection parameter and return the url string connection.
     */
    public static String setManagerConnectionValues(ManagerConnection managerConnection, ConnectionItem connectionItem,
            final String dbType, final int dbTypeIndex) {
        if (managerConnection == null || connectionItem == null || dbType == null || dbTypeIndex < 0) {
            return null;
        }
        DatabaseConnection dbConn = (DatabaseConnection) connectionItem.getConnection();

        ContextType contextType = ConnectionContextHelper.getContextTypeForContextMode(dbConn);

        String server = ConnectionContextHelper.getOriginalValue(contextType, dbConn.getServerName());
        String username = ConnectionContextHelper.getOriginalValue(contextType, dbConn.getUsername());
        String password = ConnectionContextHelper.getOriginalValue(contextType, dbConn.getPassword());
        String port = ConnectionContextHelper.getOriginalValue(contextType, dbConn.getPort());
        String sidOrDatabase = ConnectionContextHelper.getOriginalValue(contextType, dbConn.getSID());
        String datasource = ConnectionContextHelper.getOriginalValue(contextType, dbConn.getDatasourceName());
        String filePath = ConnectionContextHelper.getOriginalValue(contextType, dbConn.getFileFieldName());
        String schemaOracle = ConnectionContextHelper.getOriginalValue(contextType, dbConn.getSchema());
        String dbRootPath = ConnectionContextHelper.getOriginalValue(contextType, dbConn.getDBRootPath());
        String additionParam = ConnectionContextHelper.getOriginalValue(contextType, dbConn.getAdditionalParams());

        filePath = TalendTextUtils.removeQuotes(filePath);
        dbRootPath = TalendTextUtils.removeQuotes(dbRootPath);
        // url
        DataStringConnection dataStringConn = new DataStringConnection();
        dataStringConn.setSelectionIndex(dbTypeIndex);
        dataStringConn.getString(dbTypeIndex, server, username, password, port, sidOrDatabase, filePath.toLowerCase(),
                datasource, dbRootPath, additionParam);

        String urlConnection = dataStringConn.getUrlConnectionStr();

        if (dbConn.getProductId().equals(EDatabaseTypeName.ORACLEFORSID.getProduct())) {
            schemaOracle = schemaOracle.toUpperCase();
        }
        // set the value
        managerConnection.setValue(0, dbType, urlConnection, server, username, password, sidOrDatabase, port, filePath,
                datasource, schemaOracle, additionParam);
        managerConnection.setDbRootPath(dbRootPath);

        return urlConnection;
    }

    /**
     * 
     * ggu Comment method "getUrlConnectionString".
     * 
     * if display is false, the string connection will be returned by default context.
     */
    public static DataStringConnection getUrlConnectionString(int dbIndex, ConnectionItem connectionItem, boolean defaultContext) {
        if (dbIndex < 0 || connectionItem == null) {
            return null;
        }
        DatabaseConnection dbConn = (DatabaseConnection) connectionItem.getConnection();
        ContextType contextType = ConnectionContextHelper.getContextTypeForContextMode(dbConn, defaultContext);

        String server = ConnectionContextHelper.getOriginalValue(contextType, dbConn.getServerName());
        String username = ConnectionContextHelper.getOriginalValue(contextType, dbConn.getUsername());
        String password = ConnectionContextHelper.getOriginalValue(contextType, dbConn.getPassword());
        String port = ConnectionContextHelper.getOriginalValue(contextType, dbConn.getPort());
        String sidOrDatabase = ConnectionContextHelper.getOriginalValue(contextType, dbConn.getSID());
        String datasource = ConnectionContextHelper.getOriginalValue(contextType, dbConn.getDatasourceName());
        String filePath = ConnectionContextHelper.getOriginalValue(contextType, dbConn.getFileFieldName());
        String schemaOracle = ConnectionContextHelper.getOriginalValue(contextType, dbConn.getSchema());
        String dbRootPath = ConnectionContextHelper.getOriginalValue(contextType, dbConn.getDBRootPath());
        String additionParam = ConnectionContextHelper.getOriginalValue(contextType, dbConn.getAdditionalParams());

        filePath = TalendTextUtils.removeQuotes(filePath);
        dbRootPath = TalendTextUtils.removeQuotes(dbRootPath);
        DataStringConnection dataStringConn = new DataStringConnection();
        dataStringConn.setSelectionIndex(dbIndex);
        dataStringConn.getString(dbIndex, server, username, password, port, sidOrDatabase, filePath.toLowerCase(), datasource,
                dbRootPath, additionParam);
        return dataStringConn;

    }

    /**
     * 
     * ggu Comment method "cloneOriginalValueConnection".
     * 
     * perhaps, if connection is in context mode, will open dialog to choose context sets.
     */
    public static DatabaseConnection cloneOriginalValueConnection(DatabaseConnection dbConn) {
        return cloneOriginalValueConnection(dbConn, false);
    }

    /**
     * 
     * ggu Comment method "cloneOriginalValueConnection".
     * 
     * only clone the properties of connection.
     */
    public static DatabaseConnection cloneOriginalValueConnection(DatabaseConnection dbConn, boolean defaultContext) {
        if (dbConn == null) {
            return null;
        }
        ContextType contextType = ConnectionContextHelper.getContextTypeForContextMode(dbConn, defaultContext);
        DatabaseConnection cloneConn = ConnectionFactory.eINSTANCE.createDatabaseConnection();
        // get values
        String server = ConnectionContextHelper.getOriginalValue(contextType, dbConn.getServerName());
        String username = ConnectionContextHelper.getOriginalValue(contextType, dbConn.getUsername());
        String password = ConnectionContextHelper.getOriginalValue(contextType, dbConn.getPassword());
        String port = ConnectionContextHelper.getOriginalValue(contextType, dbConn.getPort());
        String sidOrDatabase = ConnectionContextHelper.getOriginalValue(contextType, dbConn.getSID());
        String datasource = ConnectionContextHelper.getOriginalValue(contextType, dbConn.getDatasourceName());
        String filePath = ConnectionContextHelper.getOriginalValue(contextType, dbConn.getFileFieldName());
        String schemaOracle = ConnectionContextHelper.getOriginalValue(contextType, dbConn.getSchema());
        String dbRootPath = ConnectionContextHelper.getOriginalValue(contextType, dbConn.getDBRootPath());
        String additionParam = ConnectionContextHelper.getOriginalValue(contextType, dbConn.getAdditionalParams());

        filePath = TalendTextUtils.removeQuotes(filePath);
        dbRootPath = TalendTextUtils.removeQuotes(dbRootPath);
        cloneConn.setAdditionalParams(additionParam);
        cloneConn.setDatasourceName(datasource);
        cloneConn.setDBRootPath(dbRootPath);
        cloneConn.setFileFieldName(filePath);
        cloneConn.setPassword(password);
        cloneConn.setPort(port);
        cloneConn.setSchema(schemaOracle);
        cloneConn.setServerName(server);
        cloneConn.setSID(sidOrDatabase);
        cloneConn.setUsername(username);

        cloneConn.setComment(dbConn.getComment());
        cloneConn.setDatabaseType(dbConn.getDatabaseType());
        cloneConn.setDbmsId(dbConn.getDbmsId());
        cloneConn.setDivergency(dbConn.isDivergency());
        cloneConn.setDriverClass(dbConn.getDriverClass());
        cloneConn.setId(dbConn.getId());
        cloneConn.setLabel(dbConn.getLabel());
        cloneConn.setNullChar(dbConn.getNullChar());
        cloneConn.setProductId(dbConn.getProductId());
        cloneConn.setSqlSynthax(dbConn.getSqlSynthax());
        cloneConn.setStandardSQL(dbConn.isStandardSQL());
        cloneConn.setStringQuote(dbConn.getStringQuote());
        cloneConn.setSynchronised(dbConn.isSynchronised());
        cloneConn.setSystemSQL(dbConn.isSystemSQL());
        cloneConn.setVersion(dbConn.getVersion());
        cloneConn.setReadOnly(dbConn.isReadOnly());

        // cloneConn.setProperties(dbConn.getProperties());
        // cloneConn.setCdcConns(dbConn.getCdcConns());
        // cloneConn.setQueries(dbConn.getQueries());
        // cloneConn.getTables().addAll(dbConn.getTables());
        /*
         * mustn't be set, is flag for method convert in class ConvertionHelper.
         * 
         * working for sql builder especially.
         */
        // cloneConn.setContextId(dbConn.getContextId());
        // cloneConn.setContextMode(dbConn.isContextMode());
        DataStringConnection dataStringConn = new DataStringConnection();
        int dbIndex = 0;
        if (cloneConn.getDatabaseType() != null) {
            dbIndex = dataStringConn.getIndexOfLabel(cloneConn.getDatabaseType());
        }
        dataStringConn.setSelectionIndex(dbIndex);
        dataStringConn.getString(dbIndex, server, username, password, port, sidOrDatabase, filePath.toLowerCase(), datasource,
                dbRootPath, additionParam);
        cloneConn.setURL(dataStringConn.getUrlConnectionStr());

        return cloneConn;
    }

    static void revertPropertiesForContextMode(DatabaseConnection conn, ContextType contextType) {
        if (conn == null || contextType == null) {
            return;
        }
        String server = ConnectionContextHelper.getOriginalValue(contextType, conn.getServerName());
        String username = ConnectionContextHelper.getOriginalValue(contextType, conn.getUsername());
        String password = ConnectionContextHelper.getOriginalValue(contextType, conn.getPassword());
        String port = ConnectionContextHelper.getOriginalValue(contextType, conn.getPort());
        String sidOrDatabase = ConnectionContextHelper.getOriginalValue(contextType, conn.getSID());
        String datasource = ConnectionContextHelper.getOriginalValue(contextType, conn.getDatasourceName());
        String filePath = ConnectionContextHelper.getOriginalValue(contextType, conn.getFileFieldName());
        String schemaOracle = ConnectionContextHelper.getOriginalValue(contextType, conn.getSchema());
        String dbRootPath = ConnectionContextHelper.getOriginalValue(contextType, conn.getDBRootPath());
        String additionParam = ConnectionContextHelper.getOriginalValue(contextType, conn.getAdditionalParams());

        filePath = TalendTextUtils.removeQuotes(filePath);
        dbRootPath = TalendTextUtils.removeQuotes(dbRootPath);
        conn.setAdditionalParams(additionParam);
        conn.setDatasourceName(datasource);
        conn.setDBRootPath(dbRootPath);
        conn.setFileFieldName(filePath);
        conn.setPassword(password);
        conn.setPort(port);
        conn.setSchema(schemaOracle);
        conn.setServerName(server);
        conn.setSID(sidOrDatabase);
        conn.setUsername(username);

    }
}
