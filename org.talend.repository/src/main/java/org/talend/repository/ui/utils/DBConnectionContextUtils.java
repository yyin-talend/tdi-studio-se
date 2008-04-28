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
import org.talend.core.model.context.ContextUtils;
import org.talend.core.model.metadata.builder.connection.DatabaseConnection;
import org.talend.core.model.metadata.types.JavaTypesManager;
import org.talend.core.model.process.IContextParameter;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.core.model.properties.ContextItem;
import org.talend.core.model.utils.ContextParameterUtils;
import org.talend.designer.core.model.utils.emf.talendfile.ContextType;
import org.talend.repository.ui.wizards.metadata.ContextSetsSelectionDialog;

/**
 * ggu class global comment. Detailled comment
 */
public final class DBConnectionContextUtils {

    private static final ECodeLanguage LANGUAGE = LanguageManager.getCurrentLanguage();

    /**
     * 
     */
    public enum EDBParamName {
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
        conn.setAdditionalParams(ContextParameterUtils.getNewScriptCode(paramName, LANGUAGE));

        paramName = prefixName + EDBParamName.DatasourceName;
        ConnectionContextHelper.createParameters(varList, paramName, conn.getDatasourceName());
        conn.setDatasourceName(ContextParameterUtils.getNewScriptCode(paramName, LANGUAGE));

        paramName = prefixName + EDBParamName.DBRootPath;
        ConnectionContextHelper.createParameters(varList, paramName, conn.getDBRootPath(), JavaTypesManager.DIRECTORY);
        conn.setDBRootPath(ContextParameterUtils.getNewScriptCode(paramName, LANGUAGE));

        paramName = prefixName + EDBParamName.FileFieldName;
        ConnectionContextHelper.createParameters(varList, paramName, conn.getFileFieldName(), JavaTypesManager.FILE);
        conn.setFileFieldName(ContextParameterUtils.getNewScriptCode(paramName, LANGUAGE));

        paramName = prefixName + EDBParamName.Password;
        ConnectionContextHelper.createParameters(varList, paramName, conn.getPassword());
        conn.setPassword(ContextParameterUtils.getNewScriptCode(paramName, LANGUAGE));

        paramName = prefixName + EDBParamName.Port;
        ConnectionContextHelper.createParameters(varList, paramName, conn.getPort(), JavaTypesManager.INTEGER);
        conn.setPort(ContextParameterUtils.getNewScriptCode(paramName, LANGUAGE));

        paramName = prefixName + EDBParamName.Schema;
        if (conn.getProductId().equals(EDatabaseTypeName.ORACLEFORSID.getProduct())) {
            String schema = conn.getSchema();
            if (schema != null) {
                conn.setSchema(schema.toUpperCase());
            }
        }
        ConnectionContextHelper.createParameters(varList, paramName, conn.getSchema());
        conn.setSchema(ContextParameterUtils.getNewScriptCode(paramName, LANGUAGE));

        paramName = prefixName + EDBParamName.ServerName;
        ConnectionContextHelper.createParameters(varList, paramName, conn.getServerName());
        conn.setServerName(ContextParameterUtils.getNewScriptCode(paramName, LANGUAGE));

        paramName = prefixName + EDBParamName.SID;
        ConnectionContextHelper.createParameters(varList, paramName, conn.getSID());
        conn.setSID(ContextParameterUtils.getNewScriptCode(paramName, LANGUAGE));

        paramName = prefixName + EDBParamName.Username;
        ConnectionContextHelper.createParameters(varList, paramName, conn.getUsername());
        conn.setUsername(ContextParameterUtils.getNewScriptCode(paramName, LANGUAGE));

        return varList;
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
        ContextItem contextItem = ContextUtils.getContextItemById(dbConn.getContextId());

        String server = dbConn.getServerName();
        String username = dbConn.getUsername();
        String password = dbConn.getPassword();
        String port = dbConn.getPort();
        String sidOrDatabase = dbConn.getSID();
        String datasource = dbConn.getDatasourceName();
        String filePath = dbConn.getFileFieldName();
        String schemaOracle = dbConn.getSchema();
        String dbRootPath = dbConn.getDBRootPath();
        String additionParam = dbConn.getAdditionalParams();

        String selectedContext = null;
        if (contextItem != null && dbConn.isContextMode()) {
            if (contextItem.getContext().size() > 1) {
                ContextSetsSelectionDialog setsDialog = new ContextSetsSelectionDialog(contextItem);
                setsDialog.open();
                selectedContext = setsDialog.getSelectedContext();
            } else { // not show the select dialog.
                selectedContext = contextItem.getDefaultContext();
            }
            ContextType contextType = ContextUtils.getContextTypeByName(contextItem, selectedContext, true);
            if (contextType != null) {
                final String prefixName = connectionItem.getProperty().getLabel() + ConnectionContextHelper.LINE;
                String paramName = null;

                paramName = prefixName + EDBParamName.ServerName;
                server = ConnectionContextHelper.getContextValue(contextType, server, paramName);

                paramName = prefixName + EDBParamName.Username;
                username = ConnectionContextHelper.getContextValue(contextType, username, paramName);

                paramName = prefixName + EDBParamName.Password;
                password = ConnectionContextHelper.getContextValue(contextType, password, paramName);

                paramName = prefixName + EDBParamName.Port;
                port = ConnectionContextHelper.getContextValue(contextType, port, paramName);

                paramName = prefixName + EDBParamName.SID;
                sidOrDatabase = ConnectionContextHelper.getContextValue(contextType, sidOrDatabase, paramName);

                paramName = prefixName + EDBParamName.DatasourceName;
                datasource = ConnectionContextHelper.getContextValue(contextType, datasource, paramName);

                paramName = prefixName + EDBParamName.FileFieldName;
                filePath = ConnectionContextHelper.getContextValue(contextType, filePath, paramName);

                paramName = prefixName + EDBParamName.Schema;
                schemaOracle = ConnectionContextHelper.getContextValue(contextType, schemaOracle, paramName);

                paramName = prefixName + EDBParamName.DBRootPath;
                dbRootPath = ConnectionContextHelper.getContextValue(contextType, dbRootPath, paramName);

                paramName = prefixName + EDBParamName.AdditionalParams;
                additionParam = ConnectionContextHelper.getContextValue(contextType, additionParam, paramName);

            }

        }
        // url

        String urlConnection = getUrlConnectionString(dbTypeIndex, connectionItem, false, selectedContext).getUrlConnectionStr();

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
    public static DataStringConnection getUrlConnectionString(int dbIndex, ConnectionItem connectionItem, boolean display) {
        return getUrlConnectionString(dbIndex, connectionItem, display, null);
    }

    private static DataStringConnection getUrlConnectionString(int dbIndex, ConnectionItem connectionItem, boolean display,
            String selectedContext) {
        if (dbIndex < 0 || connectionItem == null) {
            return null;
        }
        DatabaseConnection dbConn = (DatabaseConnection) connectionItem.getConnection();

        String server = dbConn.getServerName();
        String username = dbConn.getUsername();
        String password = dbConn.getPassword();
        String port = dbConn.getPort();
        String sidOrDatabase = dbConn.getSID();
        String datasource = dbConn.getDatasourceName();
        String filePath = dbConn.getFileFieldName();
        String schemaOracle = dbConn.getSchema();
        String dbRootPath = dbConn.getDBRootPath();
        String additionParam = dbConn.getAdditionalParams();

        ContextItem contextItem = ContextUtils.getContextItemById(dbConn.getContextId());
        if (contextItem != null && dbConn.isContextMode()) {
            if (display) {
                ContextSetsSelectionDialog setsDialog = new ContextSetsSelectionDialog(contextItem);
                setsDialog.open();
                selectedContext = setsDialog.getSelectedContext();
            } else { // not show the select dialog.
                if (selectedContext == null) { // not set, then set the default.
                    selectedContext = contextItem.getDefaultContext();
                }
            }
            ContextType contextType = ContextUtils.getContextTypeByName(contextItem, selectedContext, true);
            if (contextType != null) {
                final String prefixName = connectionItem.getProperty().getLabel() + ConnectionContextHelper.LINE;
                String paramName = null;

                paramName = prefixName + EDBParamName.ServerName;
                server = ConnectionContextHelper.getContextValue(contextType, server, paramName);

                paramName = prefixName + EDBParamName.Username;
                username = ConnectionContextHelper.getContextValue(contextType, username, paramName);

                paramName = prefixName + EDBParamName.Password;
                password = ConnectionContextHelper.getContextValue(contextType, password, paramName);

                paramName = prefixName + EDBParamName.Port;
                port = ConnectionContextHelper.getContextValue(contextType, port, paramName);

                paramName = prefixName + EDBParamName.SID;
                sidOrDatabase = ConnectionContextHelper.getContextValue(contextType, sidOrDatabase, paramName);

                paramName = prefixName + EDBParamName.DatasourceName;
                datasource = ConnectionContextHelper.getContextValue(contextType, datasource, paramName);

                paramName = prefixName + EDBParamName.FileFieldName;
                filePath = ConnectionContextHelper.getContextValue(contextType, filePath, paramName);

                paramName = prefixName + EDBParamName.Schema;
                schemaOracle = ConnectionContextHelper.getContextValue(contextType, schemaOracle, paramName);

                paramName = prefixName + EDBParamName.DBRootPath;
                dbRootPath = ConnectionContextHelper.getContextValue(contextType, dbRootPath, paramName);

                paramName = prefixName + EDBParamName.AdditionalParams;
                additionParam = ConnectionContextHelper.getContextValue(contextType, additionParam, paramName);

            }

        }
        DataStringConnection dataStringConn = new DataStringConnection();
        dataStringConn.setSelectionIndex(dbIndex);
        dataStringConn.getString(dbIndex, server, username, password, port, sidOrDatabase, filePath.toLowerCase(), datasource,
                dbRootPath, additionParam);
        return dataStringConn;

    }

}
