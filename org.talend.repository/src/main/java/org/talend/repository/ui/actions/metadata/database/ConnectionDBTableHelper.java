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
package org.talend.repository.ui.actions.metadata.database;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.talend.commons.exception.BusinessException;
import org.talend.commons.exception.PersistenceException;
import org.talend.commons.ui.utils.PathUtils;
import org.talend.commons.utils.VersionUtils;
import org.talend.commons.utils.data.list.UniqueStringGenerator;
import org.talend.core.CorePlugin;
import org.talend.core.context.Context;
import org.talend.core.context.RepositoryContext;
import org.talend.core.database.EDatabaseTypeName;
import org.talend.core.language.ECodeLanguage;
import org.talend.core.language.LanguageManager;
import org.talend.core.model.metadata.MetadataTalendType;
import org.talend.core.model.metadata.builder.connection.ConnectionFactory;
import org.talend.core.model.metadata.builder.connection.DatabaseConnection;
import org.talend.core.model.metadata.builder.connection.MetadataColumn;
import org.talend.core.model.metadata.builder.connection.MetadataTable;
import org.talend.core.model.metadata.types.TypesManager;
import org.talend.core.model.properties.DatabaseConnectionItem;
import org.talend.core.model.properties.Item;
import org.talend.core.model.properties.ItemState;
import org.talend.core.model.properties.PropertiesFactory;
import org.talend.core.model.properties.Property;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.IRepositoryObject;
import org.talend.repository.i18n.Messages;
import org.talend.repository.model.IProxyRepositoryFactory;
import org.talend.repository.model.ProxyRepositoryFactory;
import org.talend.repository.model.RepositoryConstants;
import org.talend.repository.ui.utils.DataStringConnection;

/**
 * ggu class global comment. Detailled comment <br/>
 * 
 */
public final class ConnectionDBTableHelper {

    private static boolean connectionCreated = false;

    private static Map<String, String> tableName = new HashMap<String, String>();

    private static final IProxyRepositoryFactory FACTORY = ProxyRepositoryFactory.getInstance();

    public static DBTableForDelimitedBean getRowData(final String line) {
        DBTableForDelimitedBean bean = new DBTableForDelimitedBean();

        String[] datas = parseLines(line);

        String tmp = null;

        /**
         * Properties
         */
        // name
        tmp = datas[0].trim();
        if ("".equals(tmp)) {
            return null;
        }
        bean.setName(tmp);
        // Purpose and Description
        bean.setPurpose(datas[1].trim());
        bean.setDescription(datas[2].trim());
        // Version
        tmp = datas[3].trim();
        if ("".equals(tmp)) {
            bean.setVersion(VersionUtils.DEFAULT_VERSION); //$NON-NLS-1$
        } else {
            bean.setVersion(tmp);
        }
        // Status
        bean.setStatus(datas[4].trim());

        /**
         * Database Connection
         */
        // Database Type
        bean.setDatabaseType(datas[5].trim());
        // Connection String
        bean.setConnectionStr(datas[6].trim());
        // Login and Password
        bean.setLogin(datas[7].trim());
        bean.setPassword(datas[8]);
        // Server
        bean.setServer(datas[9].trim());
        // Port
        tmp = datas[10].trim();
        boolean b = true;
        if (!isNullable(bean.getDatabaseType())) {
            if (bean.getDatabaseType().equals("Ingres")) { //$NON-NLS-1$
                b = Pattern.matches(Messages.getString("DatabaseForm.ingresDBRegex"), tmp); //$NON-NLS-1$
            } else {
                b = Pattern.matches(Messages.getString("DatabaseForm.otherDBRegex"), tmp); //$NON-NLS-1$
            }
        }
        if (!b) { // not right
            return null;
        }
        bean.setPort(tmp);

        // Database, Schema, DataSource
        bean.setDatabase(datas[11].trim());
        bean.setDbSchema(datas[12].trim());
        bean.setDataSource(datas[13].trim());
        // file and Database Root Path
        bean.setFile(datas[14].trim());
        bean.setDbRootPath(datas[15].trim());

        /**
         * Metadata Schema
         */
        // Original Table Name
        tmp = datas[17].trim();
        if ("".equals(tmp)) {
            return null;
        }
        bean.setOriginalTableName(tmp);
        // Table Name
        tmp = datas[16].trim();
        if (tmp == "") {
            bean.setTableName(bean.getOriginalTableName());
        } else {
            bean.setTableName(tmp);
        }
        // Original Label
        tmp = datas[19].trim();
        if ("".equals(tmp)) {
            return null;
        }
        bean.setOriginalLabel(tmp);
        // Label
        tmp = datas[18].trim();
        if ("".equals(tmp)) {
            bean.setLabel(bean.getOriginalLabel());
        } else {
            bean.setLabel(tmp);
        }
        // Comment
        bean.setComment(datas[20].trim());
        // Default Value
        bean.setDefaultValue(datas[21].trim());
        // key
        tmp = datas[22].trim();
        if ("".equals(tmp)) {
            return null;
        }
        if ("true".equals(tmp.toLowerCase())) { //$NON-NLS-1$
            bean.setKey(true);
        } else if ("false".equals(tmp.toLowerCase())) { //$NON-NLS-1$
            bean.setKey(false);
        } else {
            return null;
        }
        // Length
        tmp = datas[23].trim();
        if ("".equals(tmp)) {
            return null;
        }
        try {
            int length = Integer.parseInt(tmp);
            bean.setLength(length);
        } catch (NumberFormatException e) {
            return null;
        }
        // Nullable
        tmp = datas[24].trim();
        if ("true".equals(tmp.toLowerCase())) { //$NON-NLS-1$
            bean.setNullable(true);
        } else if ("false".equals(tmp.toLowerCase())) { //$NON-NLS-1$
            bean.setNullable(false);
        } else {
            return null;
        }
        // Pattern
        bean.setPattern(datas[25].trim());
        // Precision
        tmp = datas[26].trim();
        if ("".equals(tmp)) {
            bean.setPrecision(0);
        } else {
            try {
                int precision = Integer.parseInt(tmp);
                bean.setPrecision(precision);
            } catch (NumberFormatException e) {
                bean.setPrecision(0);
            }
        }
        // Talend Type and DB Type
        bean.setTalendType(datas[27].trim());
        bean.setDbType(datas[28].trim());

        return bean;
    }

    private static String[] parseLines(final String line) {
        // FIXME there are some problem about parsing the line,
        // process the data that contain semicolon, such as Description= "a;b,c""
        String[] datas = new String[DBTableForDelimitedBean.TOTAL];
        for (int i = 0; i < datas.length; i++) {
            datas[i] = ""; //$NON-NLS-1$
        }

        String[] tmpDatas = line.split(";"); //$NON-NLS-1$
        for (int i = 0; i < tmpDatas.length; i++) {
            datas[i] = tmpDatas[i];
        }

        return datas;
    }

    public static boolean isNullable(String name) {
        if (name == null || "".equals(name.trim())) {
            return true;
        }
        return false;
    }

    public static DatabaseConnectionItem setConnectionItemData(DBTableForDelimitedBean bean) throws PersistenceException,
            BusinessException {
        if (isNullable(bean.getName())) {
            return null;
        }
        connectionCreated = false;
        DatabaseConnectionItem connItem = getSuitableConnectionItem(bean);
        if (connItem == null) {
            return null;
        }

        DatabaseConnection conn = (DatabaseConnection) connItem.getConnection();
        if (conn == null) {
            return null;
        }
        if (!setMetadataTableData(conn, bean)) {
            return null;
        }

        return connItem;
    }

    private static DatabaseConnectionItem getSuitableConnectionItem(DBTableForDelimitedBean bean) throws PersistenceException,
            BusinessException {
        if (isNullable(bean.getName())) {
            return null;
        }
        if (!Pattern.matches(RepositoryConstants.getPattern(ERepositoryObjectType.METADATA_CONNECTIONS), bean.getName())) {
            return null;
        }
        DatabaseConnectionItem connItemAll = searchConnectionItem(bean.getName(), true);
        DatabaseConnectionItem connItem = searchConnectionItem(bean.getName(), false);
        if (connItemAll == null) {
            // need create new connection item.
            connItem = createConnectionItem(bean);

        } else if (connItem == null) {
            // the item in recycle, need rename the item.
            String genName = tableName.get(bean.getName());
            if (genName == null) {
                connItem = createConnectionItem(bean);
                // generate a new name.
                setPropNewName(connItem.getProperty());
                tableName.put(bean.getName(), connItem.getProperty().getLabel());
            } else {
                // Use the existed connection.
                connItem = searchConnectionItem(genName, false);

            }

        }

        return connItem;

    }

    private static DatabaseConnectionItem searchConnectionItem(String name, boolean withDeleted) throws PersistenceException {
        if (isNullable(name)) {
            return null;
        }

        // RootContainer<String, IRepositoryObject> metadata = FACTORY.getMetadataConnection();
        // ContentList<String, IRepositoryObject> processAbsoluteMembers = metadata.getAbsoluteMembers();
        //
        // for (Content<String, IRepositoryObject> object : processAbsoluteMembers.values()) {
        // IRepositoryObject meta = object.getContent();
        // System.out.println(meta.getLabel());
        // }

        List<IRepositoryObject> repositoryObjs = FACTORY.getAll(ERepositoryObjectType.METADATA_CONNECTIONS, withDeleted);
        for (IRepositoryObject obj : repositoryObjs) {
            if (obj.getLabel().toLowerCase().equals(name.toLowerCase())) {
                Item item = obj.getProperty().getItem();
                if (item instanceof DatabaseConnectionItem) {
                    return (DatabaseConnectionItem) item;
                }
            }
        }
        return null;
    }

    private static DatabaseConnectionItem createConnectionItem(DBTableForDelimitedBean bean) {

        DatabaseConnection conn = createDBConnection(bean);
        DatabaseConnectionItem connItem = PropertiesFactory.eINSTANCE.createDatabaseConnectionItem();
        createProperty(connItem, bean);
        if (conn == null) {
            return null;
        }
        connItem.setConnection(conn);

        connectionCreated = true;

        return connItem;

    }

    private static DatabaseConnection createDBConnection(DBTableForDelimitedBean bean) {
        DatabaseConnection connection = ConnectionFactory.eINSTANCE.createDatabaseConnection();
        connection.setDatabaseType(bean.getDatabaseType());
        connection.setDatasourceName(bean.getDataSource());

        connection.setDBRootPath(bean.getDbRootPath());
        connection.setFileFieldName(PathUtils.getPortablePath(bean.getFile()));

        connection.setPassword(bean.getPassword());
        connection.setPort(bean.getPort());

        if (EDatabaseTypeName.ORACLEFORSID.getProduct().equals(connection.getProductId()) && bean.getDbSchema() != null) {
            connection.setSchema(bean.getDbSchema().toUpperCase());
        } else {
            connection.setSchema(bean.getDbSchema());
        }

        connection.setServerName(bean.getServer());
        connection.setURL(bean.getConnectionStr());
        connection.setUsername(bean.getLogin());

        if (!isNullable(bean.getDatabaseType())) {
            final String product = EDatabaseTypeName.getTypeFromDisplayName(bean.getDatabaseType()).getProduct();
            connection.setProductId(product);

            final String mapping = MetadataTalendType.getDefaultDbmsFromProduct(product).getId();
            connection.setDbmsId(mapping);
            if (!checkDBConnectionURL(bean)) {
                return null;
            }
        }

        return connection;

    }

    private static boolean checkDBConnectionURL(DBTableForDelimitedBean bean) {
        if (isNullable(bean.getDatabaseType())) {
            // not need check
            return true;
        }
        // check the url.
        DataStringConnection urlDBStr = new DataStringConnection();
        int index = urlDBStr.getIndexOfLabel(bean.getDatabaseType());
        if (index == -1) {
            // not existed Database.
            return false;
        }

        if (LanguageManager.getCurrentLanguage() == ECodeLanguage.PERL) {
            Collection<String> databasePerl = new ArrayList<String>(Arrays.asList(urlDBStr.getItem()));
            databasePerl.remove("Microsoft SQL Server");
            databasePerl.remove("Ingres");
            databasePerl.remove("Interbase");
            databasePerl.remove("FireBird");
            databasePerl.remove("Informix");
            databasePerl.remove("Access");
            databasePerl.remove("Teradata");
            databasePerl.remove("AS400");

            databasePerl.remove("JavaDB Embeded");
            databasePerl.remove("JavaDB JCCJDBC");
            databasePerl.remove("JavaDB DerbyClient");

            databasePerl.remove("HSQLDB Server");
            databasePerl.remove("HSQLDB WebServer");
            databasePerl.remove("HSQLDB In-Process");
            if (!databasePerl.contains(bean.getDatabaseType())) {
                // not supported by perl
                return false;
            }
        }
        urlDBStr.setSelectionIndex(index);
        // check the schema needed
        // if (urlDBStr.isSchemaNeeded() && isNullable(bean.getDbSchema())) {
        // return false;
        // }

        // need check the database url.

        return true;

    }

    private static Property createProperty(DatabaseConnectionItem connItem, DBTableForDelimitedBean bean) {

        Property connectionProperty = PropertiesFactory.eINSTANCE.createProperty();

        connectionProperty.setAuthor(((RepositoryContext) CorePlugin.getContext().getProperty(Context.REPOSITORY_CONTEXT_KEY))
                .getUser());

        connectionProperty.setVersion(VersionUtils.DEFAULT_VERSION);

        connectionProperty.setStatusCode(bean.getStatus());
        connectionProperty.setDescription(bean.getDescription());
        connectionProperty.setPurpose(bean.getPurpose());

        connectionProperty.setLabel(bean.getName());
        connItem.setProperty(connectionProperty);
        ItemState state = PropertiesFactory.eINSTANCE.createItemState();
        state.setDeleted(false);
        connItem.setState(state);
        return connectionProperty;
    }

    private static void setPropNewName(Property theProperty) throws PersistenceException, BusinessException {
        String originalLabel = theProperty.getLabel();

        char j = 'a';
        while (!FACTORY.isNameAvailable(theProperty.getItem(), null)) {
            String nextTry = originalLabel + "_" + (j++); //$NON-NLS-1$ //$NON-NLS-2$
            theProperty.setLabel(nextTry);
            if (j > 'z') {
                setPropNewName(theProperty);
                return;
            }
        }
    }

    /*
     * set the table data in a connection.
     */
    public static boolean setMetadataTableData(DatabaseConnection conn, DBTableForDelimitedBean bean) {

        if (isNullable(bean.getTableName())) {
            return false;
        }
        boolean found = true;
        // FIXME need check the table in recycle bin.

        MetadataTable metadataTable = searchMetadataTable(conn, bean.getTableName());
        if (metadataTable == null) { // need create new table
            found = false;
            metadataTable = ConnectionFactory.eINSTANCE.createMetadataTable();
            metadataTable.setConnection(conn);
            metadataTable.setId(FACTORY.getNextId());
            metadataTable.setLabel(bean.getTableName());
            metadataTable.setSourceName(bean.getOriginalTableName());
            metadataTable.setTableType("TABLE"); //$NON-NLS-1$ 
        }
        boolean isOk = setMetadataColumnData(metadataTable, bean);
        if (!isOk) {
            return false;
        }

        if (!found) {
            // avoid add empty table.
            conn.getTables().add(metadataTable);
        }
        return true;

    }

    private static MetadataTable searchMetadataTable(DatabaseConnection conn, String name) {
        for (Object tableObject : conn.getTables()) {
            MetadataTable metadataTable = (MetadataTable) tableObject;
            if (name.trim().equals(metadataTable.getLabel())) {
                return metadataTable;
            }
        }
        return null;
    }

    /*
     * add the a column data in table
     */
    public static boolean setMetadataColumnData(MetadataTable table, DBTableForDelimitedBean bean) {

        MetadataColumn metadataColumn = ConnectionFactory.eINSTANCE.createMetadataColumn();

        metadataColumn.setComment(bean.getComment());
        String genLabel = getUniqueColumnName(table, bean);
        if (genLabel == null) {
            return false;
        }
        metadataColumn.setLabel(genLabel);

        metadataColumn.setOriginalField(bean.getOriginalLabel());
        ECodeLanguage codeLanguage = LanguageManager.getCurrentLanguage();
        if (codeLanguage == ECodeLanguage.JAVA) {
            if ("".equals(bean.getTalendType())) {
                metadataColumn.setTalendType("id_String"); //$NON-NLS-1$
            } else {
                metadataColumn.setTalendType(bean.getTalendType());
            }
        } else {
            metadataColumn.setTalendType(bean.getTalendType());
        }

        metadataColumn.setDefaultValue(bean.getDefaultValue());
        metadataColumn.setPattern(bean.getPattern());

        metadataColumn.setKey(bean.isKey());
        metadataColumn.setNullable(bean.isNullable());

        metadataColumn.setLength(bean.getLength());
        metadataColumn.setPrecision(bean.getPrecision());

        if ("".equals(bean.getDbType())) {
            if (codeLanguage == ECodeLanguage.JAVA) {
                if (!"".equals(bean.getDatabaseType()) && !"".equals(bean.getTalendType())) {
                    metadataColumn.setSourceType(TypesManager.getDBTypeFromTalendType(bean.getDatabaseType(), bean
                            .getTalendType()));
                } else {
                    metadataColumn.setSourceType("");
                }
            } else {
                metadataColumn.setSourceType("");
            }
        } else {
            metadataColumn.setSourceType(bean.getDbType());
        }

        table.getColumns().add(metadataColumn);

        return true;

    }

    private static String getUniqueColumnName(MetadataTable table, DBTableForDelimitedBean bean) {
        if (isNullable(bean.getLabel()) || isNullable(bean.getOriginalLabel())) {
            // can't add
            return null;
        }
        if (isExistedColumn(table, bean.getOriginalLabel(), true)) {
            // not allow the duplicated OriginalField
            return null;
        }
        if (isExistedColumn(table, bean.getLabel(), false)) {

            UniqueStringGenerator<MetadataColumn> uniqueStringGenerator = new UniqueStringGenerator<MetadataColumn>(bean
                    .getLabel(), table.getColumns()) {

                @Override
                protected String getBeanString(MetadataColumn bean) {
                    return bean.getLabel();
                }
            };
            return uniqueStringGenerator.getUniqueString();

        }
        return bean.getLabel();

    }

    private static boolean isExistedColumn(MetadataTable table, String label, boolean isOriginal) {
        for (Object colObject : table.getColumns()) {
            MetadataColumn column = (MetadataColumn) colObject;
            if (isOriginal && label.trim().equals(column.getOriginalField())) {
                return true;
            } else if (label.trim().equals(column.getLabel())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Getter for connectionCreated.
     * 
     * @return the connectionCreated
     */
    public static boolean isConnectionCreated() {
        return connectionCreated;
    }

    public static void initGenTableName() {
        tableName.clear();
    }
}
