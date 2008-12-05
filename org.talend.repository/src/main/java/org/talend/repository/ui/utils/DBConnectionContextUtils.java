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
import java.util.Set;

import org.talend.commons.exception.ExceptionHandler;
import org.talend.core.database.EDatabaseTypeName;
import org.talend.core.language.ECodeLanguage;
import org.talend.core.language.LanguageManager;
import org.talend.core.model.PasswordEncryptUtil;
import org.talend.core.model.metadata.builder.connection.ConnectionFactory;
import org.talend.core.model.metadata.builder.connection.DatabaseConnection;
import org.talend.core.model.metadata.types.JavaTypesManager;
import org.talend.core.model.process.IContextParameter;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.core.model.utils.ContextParameterUtils;
import org.talend.core.model.utils.TalendTextUtils;
import org.talend.designer.core.model.utils.emf.talendfile.ContextType;
import org.talend.repository.model.IConnParamName;

/**
 * ggu class global comment. Detailled comment
 */
public final class DBConnectionContextUtils {

    private static final ECodeLanguage LANGUAGE = LanguageManager.getCurrentLanguage();

    /**
     * 
     */
    public enum EDBParamName implements IConnParamName {
        Login,
        Password,
        Port,
        Server,
        Schema,
        AdditionalParams,
        Datasource,
        DBRootPath,
        File,
        //
        Sid,
        Database,
        ServiceName,
    }

    static List<IContextParameter> getDBVariables(String prefixName, DatabaseConnection conn, Set<IConnParamName> paramSet) {
        if (conn == null || prefixName == null || paramSet == null || paramSet.isEmpty()) {
            return Collections.emptyList();
        }

        List<IContextParameter> varList = new ArrayList<IContextParameter>();
        prefixName = prefixName + ConnectionContextHelper.LINE;
        String paramName = null;
        for (IConnParamName param : paramSet) {
            if (param instanceof EDBParamName) {
                EDBParamName dbParam = (EDBParamName) param;
                paramName = prefixName + dbParam;
                switch (dbParam) {
                case AdditionalParams:
                    ConnectionContextHelper.createParameters(varList, paramName, conn.getAdditionalParams());
                    break;
                case Datasource:
                    ConnectionContextHelper.createParameters(varList, paramName, conn.getDatasourceName());
                    break;
                case DBRootPath:
                    ConnectionContextHelper
                            .createParameters(varList, paramName, conn.getDBRootPath(), JavaTypesManager.DIRECTORY);
                    break;
                case File:
                    ConnectionContextHelper.createParameters(varList, paramName, conn.getFileFieldName(), JavaTypesManager.FILE);
                    break;
                case Password:
                    ConnectionContextHelper.createParameters(varList, paramName, conn.getPassword(), JavaTypesManager.PASSWORD);
                    break;
                case Port:
                    ConnectionContextHelper.createParameters(varList, paramName, conn.getPort(), JavaTypesManager.STRING);
                    break;
                case Schema:
                    if (conn.getProductId().equals(EDatabaseTypeName.ORACLEFORSID.getProduct())) {
                        String schema = conn.getSchema();
                        if (schema != null) {
                            conn.setSchema(schema.toUpperCase());
                        }
                    }
                    ConnectionContextHelper.createParameters(varList, paramName, conn.getSchema());
                    break;
                case Server:
                    ConnectionContextHelper.createParameters(varList, paramName, conn.getServerName());
                    break;
                case Sid:
                case Database:
                case ServiceName:
                    ConnectionContextHelper.createParameters(varList, paramName, conn.getSID());
                    break;
                case Login:
                    ConnectionContextHelper.createParameters(varList, paramName, conn.getUsername());
                    break;
                default:
                }
            }
        }

        return varList;
    }

    static void setPropertiesForContextMode(String prefixName, DatabaseConnection conn, Set<IConnParamName> paramSet) {
        if (conn == null || prefixName == null || paramSet == null || paramSet.isEmpty()) {
            return;
        }
        prefixName = prefixName + ConnectionContextHelper.LINE;
        String paramName = null;
        for (IConnParamName param : paramSet) {
            if (param instanceof EDBParamName) {
                EDBParamName dbParam = (EDBParamName) param;
                paramName = prefixName + dbParam;
                switch (dbParam) {
                case AdditionalParams:
                    conn.setAdditionalParams(ContextParameterUtils.getNewScriptCode(paramName, LANGUAGE));
                    break;
                case Datasource:
                    conn.setDatasourceName(ContextParameterUtils.getNewScriptCode(paramName, LANGUAGE));
                    break;
                case DBRootPath:
                    conn.setDBRootPath(ContextParameterUtils.getNewScriptCode(paramName, LANGUAGE));
                    break;
                case File:
                    conn.setFileFieldName(ContextParameterUtils.getNewScriptCode(paramName, LANGUAGE));
                    break;
                case Password:
                    conn.setPassword(ContextParameterUtils.getNewScriptCode(paramName, LANGUAGE));
                    break;
                case Port:
                    conn.setPort(ContextParameterUtils.getNewScriptCode(paramName, LANGUAGE));
                    break;
                case Schema:
                    conn.setSchema(ContextParameterUtils.getNewScriptCode(paramName, LANGUAGE));
                    break;
                case Server:
                    conn.setServerName(ContextParameterUtils.getNewScriptCode(paramName, LANGUAGE));
                    break;
                case Sid:
                case Database:
                case ServiceName:
                    conn.setSID(ContextParameterUtils.getNewScriptCode(paramName, LANGUAGE));
                    break;
                case Login:
                    conn.setUsername(ContextParameterUtils.getNewScriptCode(paramName, LANGUAGE));
                    break;
                default:
                }
            }
        }

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
        String driverClassName = ConnectionContextHelper.getOriginalValue(contextType, dbConn.getDriverClass());
        String driverJarPath = ConnectionContextHelper.getOriginalValue(contextType, dbConn.getDriverJarPath());
        String dbVersionString = ConnectionContextHelper.getOriginalValue(contextType, dbConn.getDbVersionString());

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
                datasource, schemaOracle, additionParam, driverClassName, driverJarPath, dbVersionString);
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
        String password = ConnectionContextHelper.getOriginalValue(contextType,
				dbConn.getRawPassword());
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
        cloneConn.setDriverJarPath(dbConn.getDriverJarPath());
        cloneConn.setDbVersionString(dbConn.getDbVersionString());
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
        if (dbConn.getURL() != null && !dbConn.getURL().equals("")) {
            // for general db, url is given directly.
            cloneConn.setURL(dbConn.getURL());
        } else {
            cloneConn.setURL(dataStringConn.getUrlConnectionStr());
        }

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
        try {
			conn.setPassword(PasswordEncryptUtil.encryptPassword(password));
		} catch (Exception e) {
			ExceptionHandler.process(e);
		}
        conn.setPort(port);
        conn.setSchema(schemaOracle);
        conn.setServerName(server);
        conn.setSID(sidOrDatabase);
        conn.setUsername(username);

    }
}
