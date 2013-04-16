// ============================================================================
//
// Copyright (C) 2006-2013 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.repository;

import java.util.Map.Entry;
import java.util.Properties;

import org.eclipse.emf.common.util.EMap;
import org.talend.commons.bridge.ReponsitoryContextBridge;
import org.talend.commons.ui.runtime.exception.ExceptionHandler;
import org.talend.commons.utils.PasswordEncryptUtil;
import org.talend.core.database.EDatabaseTypeName;
import org.talend.core.database.conn.DatabaseConnStrUtil;
import org.talend.core.model.metadata.Dbms;
import org.talend.core.model.metadata.IMetadataConnection;
import org.talend.core.model.metadata.MetadataTalendType;
import org.talend.core.model.metadata.builder.connection.ConnectionFactory;
import org.talend.core.model.metadata.builder.connection.DatabaseConnection;
import org.talend.core.model.metadata.builder.database.PluginConstant;
import org.talend.core.model.utils.ContextParameterUtils;
import org.talend.core.utils.TalendQuoteUtils;
import org.talend.cwm.helper.ConnectionHelper;
import org.talend.designer.core.model.utils.emf.talendfile.ContextType;

/**
 * DOC root class global comment. Detailled comment
 */
public class StandaloneConnectionContextUtils {

    /**
     * DOC root Comment method "cloneOriginalValueConnection".
     * 
     * @param dbConn
     * @param contextProperties
     * @return
     */
    public static DatabaseConnection cloneOriginalValueConnection(DatabaseConnection dbConn, Properties contextProperties) {
        if (dbConn == null) {
            return null;
        }
        ContextType contextType = null;
        DatabaseConnection cloneConn = ConnectionFactory.eINSTANCE.createDatabaseConnection();
        // get values
        String server = getOriginalValue(contextProperties, dbConn.getServerName());
        String username = getOriginalValue(contextProperties, dbConn.getUsername());
        String password = getOriginalValue(contextProperties, dbConn.getRawPassword());
        String port = getOriginalValue(contextProperties, dbConn.getPort());
        String sidOrDatabase = getOriginalValue(contextProperties, dbConn.getSID());
        String datasource = getOriginalValue(contextProperties, dbConn.getDatasourceName());
        String filePath = getOriginalValue(contextProperties, dbConn.getFileFieldName());
        String schemaOracle = getOriginalValue(contextProperties, dbConn.getUiSchema());
        String dbRootPath = getOriginalValue(contextProperties, dbConn.getDBRootPath());
        String additionParam = getOriginalValue(contextProperties, dbConn.getAdditionalParams());
        String url = getOriginalValue(contextProperties, dbConn.getURL());
        String className = getOriginalValue(contextProperties, dbConn.getDriverClass());
        String jarPath = getOriginalValue(contextProperties, dbConn.getDriverJarPath());

        // hyWang add for bug 0007252
        String dbmsID = getOriginalValue(contextProperties, dbConn.getDbmsId());

        filePath = TalendQuoteUtils.removeQuotes(filePath);
        dbRootPath = TalendQuoteUtils.removeQuotes(dbRootPath);
        cloneConn.setAdditionalParams(additionParam);
        cloneConn.setDatasourceName(datasource);
        cloneConn.setDBRootPath(dbRootPath);
        cloneConn.setFileFieldName(filePath);

        // if use context
        if (contextType != null) {
            String encryptedPassword = null;
            try {
                encryptedPassword = PasswordEncryptUtil.encryptPassword(password);
            } catch (Exception e) {
                ExceptionHandler.process(e);
            }
            cloneConn.setPassword(encryptedPassword);
        } else {
            cloneConn.setPassword(password);
        }

        cloneConn.setPort(port);
        cloneConn.setUiSchema(schemaOracle);
        cloneConn.setServerName(server);
        cloneConn.setSID(sidOrDatabase);
        cloneConn.setUsername(username);
        cloneConn.setDriverJarPath(jarPath);

        cloneConn.setComment(dbConn.getComment());
        cloneConn.setDatabaseType(dbConn.getDatabaseType());
        cloneConn.setDbmsId(dbmsID);
        cloneConn.setDivergency(dbConn.isDivergency());
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
        cloneConn.setDriverClass(className);
        cloneConn.setName(dbConn.getName());
        cloneOtherParameters(dbConn, cloneConn);
        if (dbConn.isSetSQLMode()) {
            cloneConn.setSQLMode(dbConn.isSQLMode());
        } else {
            // set true by default as it's only used actually for teradata.
            // should be modified if default value is changed later.
            cloneConn.setSQLMode(true);
        }

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
        // cloneConn.setContextMode(dbConn.isContextMode()); // if use context

        // Added 20130311 TDQ-7000, when it is context mode and not general
        // jdbc, reset the url.
        if (contextType != null
                && !EDatabaseTypeName.GENERAL_JDBC.equals(EDatabaseTypeName.getTypeFromDbType(dbConn.getDatabaseType()))) {
            String newURL = DatabaseConnStrUtil.getURLString(cloneConn.getDatabaseType(), dbConn.getDbVersionString(), server,
                    username, password, port, sidOrDatabase, filePath.toLowerCase(), datasource, dbRootPath, additionParam);
            cloneConn.setURL(newURL);
            return cloneConn;
        }// ~

        if (dbConn.getURL() != null && !dbConn.getURL().equals("")) { //$NON-NLS-1$
            // for general db, url is given directly.
            cloneConn.setURL(url);
        } else {
            String newURL = DatabaseConnStrUtil.getURLString(cloneConn.getDatabaseType(), dbConn.getDbVersionString(), server,
                    username, password, port, sidOrDatabase, filePath.toLowerCase(), datasource, dbRootPath, additionParam);
            cloneConn.setURL(newURL);
        }
        return cloneConn;
    }

    /**
     * DOC root Comment method "getOriginalValue".
     * 
     * @param contextProperties
     * @param dbmsId
     * @return
     */
    private static String getOriginalValue(Properties contextProperties, String value) {
        if (value != null && ContextParameterUtils.containContextVariables(value)) {
            String var = ContextParameterUtils.getVariableFromCode(value);
            String res = contextProperties.getProperty(var);
            return res != null ? res : PluginConstant.EMPTY_STRING;
        }
        return value;
    }

    /**
     * Clones other parameters from the original parameters. Added by Marvin Wang on Aug 13, 2012.
     * 
     * @param dbConn
     * @param cloneConn
     */
    protected static void cloneOtherParameters(DatabaseConnection dbConn, DatabaseConnection cloneConn) {
        EMap<String, String> originalParams = dbConn.getParameters();
        if (originalParams != null && originalParams.size() > 0) {
            EMap<String, String> clonedMap = cloneConn.getParameters();
            for (Entry<String, String> entry : originalParams.entrySet()) {
                clonedMap.put(entry.getKey(), entry.getValue());
            }
        }
    }

    /**
     * DOC root Comment method "setMetadataConnectionParameter".
     * 
     * @param dbConn
     * @param metaConn
     * @param contextProperties
     * @return
     */
    public static void setMetadataConnectionParameter(DatabaseConnection conn, IMetadataConnection metadataConnection,
            Properties contextProperties) {

        if (conn == null || metadataConnection == null) {
            return;
        }
        ContextType contextType = getContextTypeForContextMode(conn, conn.getContextName());
        if (contextType == null) {
            return;
        }

        // driverPath
        metadataConnection.setDriverJarPath(getOriginalValue(contextProperties, conn.getDriverJarPath()));

        // set dbType
        metadataConnection.setDbType(getOriginalValue(contextProperties, conn.getDatabaseType()));
        // set product(ProductId) and Schema(UISchema)
        EDatabaseTypeName edatabasetypeInstance = EDatabaseTypeName.getTypeFromDisplayName(getOriginalValue(contextProperties,
                conn.getDatabaseType()));
        String product = edatabasetypeInstance.getProduct();
        metadataConnection.setProduct(product);
        // set mapping(DbmsId)
        if (!ReponsitoryContextBridge.isDefautProject()) {
            Dbms defaultDbmsFromProduct = MetadataTalendType.getDefaultDbmsFromProduct(product);
            if (defaultDbmsFromProduct != null) {
                String mapping = defaultDbmsFromProduct.getId();
                metadataConnection.setMapping(mapping);
            }
        }
        // set dbVersionString
        metadataConnection.setDbVersionString(getOriginalValue(contextProperties, conn.getDbVersionString()));

        // filePath
        metadataConnection.setFileFieldName(getOriginalValue(contextProperties, conn.getFileFieldName()));
        // jdbcUrl
        metadataConnection.setUrl(getOriginalValue(contextProperties, conn.getURL()));
        // aDDParameter
        metadataConnection.setAdditionalParams(getOriginalValue(contextProperties, conn.getAdditionalParams()));
        // driverClassName
        metadataConnection.setDriverClass(getOriginalValue(contextProperties, conn.getDriverClass()));
        // host
        metadataConnection.setServerName(getOriginalValue(contextProperties, conn.getServerName()));
        // port
        metadataConnection.setPort(getOriginalValue(contextProperties, conn.getPort()));
        // dbName
        metadataConnection.setDatabase(getOriginalValue(contextProperties, conn.getSID()));
        // otherParameter
        metadataConnection.setOtherParameter(getOriginalValue(contextProperties, ConnectionHelper.getOtherParameter(conn)));
        // password
        metadataConnection.setPassword(getOriginalValue(contextProperties, ConnectionHelper.getPassword(conn)));
        // user
        metadataConnection.setUsername(getOriginalValue(contextProperties, conn.getUsername()));
        // dbName
        metadataConnection.setDataSourceName(getOriginalValue(contextProperties, conn.getDatasourceName()));
        // schema
        metadataConnection.setSchema(conn.getUiSchema());
        // dbmsId
        if (metadataConnection.getMapping() == null) {
            metadataConnection.setMapping(getOriginalValue(contextProperties, conn.getDbmsId()));
        }

    }

    /**
     * DOC root Comment method "getContextTypeForContextMode".
     * 
     * @param conn
     * @param contextName
     * @return
     */
    private static ContextType getContextTypeForContextMode(DatabaseConnection conn, String contextName) {
        return null;
    }
}
