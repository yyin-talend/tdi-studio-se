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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
import org.talend.core.model.metadata.types.JavaTypesManager;
import org.talend.core.model.metadata.types.TypesManager;
import org.talend.core.model.properties.ConnectionItem;
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
import org.talend.repository.ui.actions.metadata.database.DBProcessRecords.ProcessType;
import org.talend.repository.ui.actions.metadata.database.DBProcessRecords.RecordsType;
import org.talend.repository.ui.actions.metadata.database.DBProcessRecords.RejectedType;
import org.talend.repository.ui.utils.DataStringConnection;

/**
 * ggu class global comment. Detailled comment <br/>
 * 
 */
public final class ConnectionDBTableHelper {

    private File importedfile = null;

    private String importedName = ""; //$NON-NLS-1$

    private boolean connectionCreated = false;

    private StringBuilder unknownLine = new StringBuilder();

    private Map<String, String> connNameMap = new HashMap<String, String>();

    private Map<String, Set<String>> tableNameMap = new HashMap<String, Set<String>>();

    private Map<String, Set<String>> rejectedTableNameMap = new HashMap<String, Set<String>>();

    private final IProxyRepositoryFactory factory = ProxyRepositoryFactory.getInstance();

    private DBProcessRecords processRecords = new DBProcessRecords();

    private int rejectedNum = 0;

    public ConnectionDBTableHelper(File file) {
        this.importedfile = file;

        String name = file.getName();
        int index = name.lastIndexOf("."); //$NON-NLS-1$
        if (index > 0) {
            importedName = name.substring(0, index);
        } else {
            importedName = name;
        }

    }

    public DBTableForDelimitedBean getRowData(final String line) {
        DBTableForDelimitedBean bean = new DBTableForDelimitedBean();

        String[] datas = parseLines(line);

        String tmp = null;

        /**
         * Properties
         */
        // name
        tmp = datas[0].trim();
        if ("".equals(tmp)) { //$NON-NLS-1$
            return null;
        }
        bean.setName(tmp);
        // Purpose and Description
        bean.setPurpose(datas[1].trim());
        bean.setDescription(datas[2].trim());
        // Version
        tmp = datas[3].trim();
        if ("".equals(tmp)) { //$NON-NLS-1$
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
        tmp = datas[5].trim();
        bean.setDatabaseType(tmp);

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
        if ("".equals(tmp)) { //$NON-NLS-1$
            return null;
        }
        bean.setOriginalTableName(tmp);
        // Table Name
        tmp = datas[16].trim();
        if (tmp == "") { //$NON-NLS-1$
            bean.setTableName(bean.getOriginalTableName());
        } else {
            bean.setTableName(tmp);
        }
        // Original Label
        tmp = datas[19].trim();
        if ("".equals(tmp)) { //$NON-NLS-1$
            return null;
        }
        bean.setOriginalLabel(tmp);
        // Label
        tmp = datas[18].trim();
        if ("".equals(tmp)) { //$NON-NLS-1$
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
        if ("".equals(tmp)) { //$NON-NLS-1$
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
        if ("".equals(tmp)) { //$NON-NLS-1$
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
        if ("".equals(tmp)) { //$NON-NLS-1$
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

    private String[] parseLines(final String line) {
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

    public boolean isNullable(String name) {
        if (name == null || "".equals(name.trim())) { //$NON-NLS-1$
            return true;
        }
        return false;
    }

    public DatabaseConnectionItem setConnectionItemData(DBTableForDelimitedBean bean) throws PersistenceException,
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
            addTableName(bean.getName(), bean.getTableName(), true);
            return null;
        }
        connNameMap.put(bean.getName(), connItem.getProperty().getLabel());
        return connItem;
    }

    private DatabaseConnectionItem getSuitableConnectionItem(DBTableForDelimitedBean bean) throws PersistenceException,
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
            String genName = connNameMap.get(bean.getName());
            if (genName == null) {
                connItem = createConnectionItem(bean);
                if (connItem == null) {
                    return null;
                }
                // generate a new name.
                setPropNewName(connItem.getProperty());
            } else {
                // Use the existed connection.
                connItem = searchConnectionItem(genName, false);

            }

        }
        return connItem;

    }

    private DatabaseConnectionItem searchConnectionItem(String name, boolean withDeleted) throws PersistenceException {
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

        List<IRepositoryObject> repositoryObjs = factory.getAll(ERepositoryObjectType.METADATA_CONNECTIONS, withDeleted);
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

    private DatabaseConnectionItem createConnectionItem(DBTableForDelimitedBean bean) {

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

    private DatabaseConnection createDBConnection(DBTableForDelimitedBean bean) {
        if (isNullable(bean.getDatabaseType())) {
            processRecords.addRejectedRecords(RejectedType.DATABASETYPE, bean.getDatabaseType(), bean.getName());
            return null;
        }
        final String product = EDatabaseTypeName.getTypeFromDisplayName(bean.getDatabaseType()).getProduct();
        if (product == null) {
            // not suppored database
            processRecords.addRejectedRecords(RejectedType.DATABASETYPE, bean.getDatabaseType(), bean.getName());
            return null;
        }
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

        connection.setProductId(product);

        final String mapping = MetadataTalendType.getDefaultDbmsFromProduct(product).getId();
        connection.setDbmsId(mapping);
        if (!checkDBConnectionURL(bean)) {
            return null;
        }

        return connection;

    }

    private boolean checkDBConnectionURL(DBTableForDelimitedBean bean) {
        if (isNullable(bean.getDatabaseType())) {
            processRecords.addRejectedRecords(RejectedType.DATABASETYPE, bean.getDatabaseType(), bean.getName());
            return false;
        }
        // check the url.
        DataStringConnection urlDBStr = new DataStringConnection();
        int index = urlDBStr.getIndexOfLabel(bean.getDatabaseType());
        if (index == -1) {
            // not existed Database.
            // add rejected dbtype
            processRecords.addRejectedRecords(RejectedType.DATABASETYPE, bean.getDatabaseType(), bean.getName());
            return false;
        }

        if (LanguageManager.getCurrentLanguage() == ECodeLanguage.PERL) {
            Collection<String> databasePerl = new ArrayList<String>(Arrays.asList(urlDBStr.getItem()));
            databasePerl.remove("Microsoft SQL Server"); //$NON-NLS-1$
            databasePerl.remove("Ingres"); //$NON-NLS-1$
            databasePerl.remove("Interbase"); //$NON-NLS-1$
            databasePerl.remove("FireBird"); //$NON-NLS-1$
            databasePerl.remove("Informix"); //$NON-NLS-1$
            databasePerl.remove("Access"); //$NON-NLS-1$
            databasePerl.remove("Teradata"); //$NON-NLS-1$
            databasePerl.remove("AS400"); //$NON-NLS-1$

            databasePerl.remove("JavaDB Embeded"); //$NON-NLS-1$
            databasePerl.remove("JavaDB JCCJDBC"); //$NON-NLS-1$
            databasePerl.remove("JavaDB DerbyClient"); //$NON-NLS-1$

            databasePerl.remove("HSQLDB Server"); //$NON-NLS-1$
            databasePerl.remove("HSQLDB WebServer"); //$NON-NLS-1$
            databasePerl.remove("HSQLDB In-Process"); //$NON-NLS-1$
            if (!databasePerl.contains(bean.getDatabaseType())) {
                // not supported by perl
                // add rejected dbtype
                processRecords.setUnknownDBForPerl(bean.getDatabaseType(), bean.getName());
                return false;
            }
        }
        // urlDBStr.setSelectionIndex(index);
        // check the schema needed
        // if (urlDBStr.isSchemaNeeded() && isNullable(bean.getDbSchema())) {
        // return false;
        // }

        // need check the database url.

        return true;

    }

    private Property createProperty(DatabaseConnectionItem connItem, DBTableForDelimitedBean bean) {

        Property connectionProperty = PropertiesFactory.eINSTANCE.createProperty();

        connectionProperty.setAuthor(((RepositoryContext) CorePlugin.getContext().getProperty(Context.REPOSITORY_CONTEXT_KEY))
                .getUser());

        connectionProperty.setVersion(bean.getVersion());

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

    private void setPropNewName(Property theProperty) throws PersistenceException, BusinessException {
        String originalLabel = theProperty.getLabel();

        char j = 'a';
        while (!factory.isNameAvailable(theProperty.getItem(), null)) {
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
    private boolean setMetadataTableData(DatabaseConnection conn, DBTableForDelimitedBean bean) {

        if (isNullable(bean.getTableName())) {
            return false;
        }
        if (isContainTableName(bean.getName(), bean.getTableName(), true)) {
            // this table have be rejected
            return false;
        }
        boolean found = true;

        MetadataTable metadataTable = searchMetadataTable(conn, bean.getTableName());
        if (metadataTable == null) { // need create new table
            found = false;
            metadataTable = ConnectionFactory.eINSTANCE.createMetadataTable();
            metadataTable.setConnection(conn);
            metadataTable.setId(factory.getNextId());
            metadataTable.setLabel(bean.getTableName());
            metadataTable.setSourceName(bean.getOriginalTableName());
            metadataTable.setTableType("TABLE"); //$NON-NLS-1$ 
            // record the generated table name.
            addTableName(bean.getName(), bean.getTableName(), false);
        } else if (!isContainTableName(bean.getName(), bean.getTableName(), false)) {
            // have existed table, and it's the existed table before.
            // reject it.
            processRecords.addExistedTable(bean);
            // FIXME maybe need check the table in recycle bin.
            return false;

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

    private MetadataTable searchMetadataTable(DatabaseConnection conn, String name) {
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
    private boolean setMetadataColumnData(MetadataTable table, DBTableForDelimitedBean bean) {

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
            if ("".equals(bean.getTalendType())) { //$NON-NLS-1$
                metadataColumn.setTalendType(JavaTypesManager.getDefaultJavaType().getId());
            } else {
                try {
                    JavaTypesManager.getJavaTypeFromId(bean.getTalendType());
                    metadataColumn.setTalendType(bean.getTalendType());
                } catch (IllegalArgumentException e) {
                    // not supported this talendtype
                    processRecords.addRejectedRecords(RejectedType.TALENDTYPE, bean.getTalendType(), bean.getName());
                    return false;
                }
            }
        } else {
            if (!"".equals(bean.getTalendType())) { //$NON-NLS-1$
                List<String> perlTypes = Arrays.asList(MetadataTalendType.getPerlTypes());
                if (perlTypes.contains(bean.getTalendType())) {
                    metadataColumn.setTalendType(bean.getTalendType());
                } else {
                    // not supported by perl
                    processRecords.addRejectedRecords(RejectedType.TALENDTYPE, bean.getTalendType(), bean.getName());
                    return false;
                }
            } else {
                metadataColumn.setTalendType(""); //$NON-NLS-1$
            }

        }

        metadataColumn.setDefaultValue(bean.getDefaultValue());
        metadataColumn.setPattern(bean.getPattern());

        metadataColumn.setKey(bean.isKey());
        metadataColumn.setNullable(bean.isNullable());

        metadataColumn.setLength(bean.getLength());
        metadataColumn.setPrecision(bean.getPrecision());

        if (!"".equals(bean.getDatabaseType()) && !"".equals(bean.getTalendType())) { //$NON-NLS-1$ //$NON-NLS-2$
            final String product = EDatabaseTypeName.getTypeFromDisplayName(bean.getDatabaseType()).getProduct();
            final String mapping = MetadataTalendType.getDefaultDbmsFromProduct(product).getId();

            if ("".equals(bean.getDbType())) { //$NON-NLS-1$  
                metadataColumn.setSourceType(TypesManager.getDBTypeFromTalendType(mapping, bean.getTalendType()));
            } else {
                try {
                    if (!TypesManager.checkDBType(mapping, bean.getTalendType(), bean.getDbType())) {
                        processRecords.setTypeNotMapping(bean);
                        return false;
                    }
                } catch (IllegalArgumentException e) {
                    // database type not be supported.
                    processRecords.addRejectedRecords(RejectedType.DATABASETYPE, bean.getDatabaseType(), bean.getName());
                    return false;
                }
                metadataColumn.setSourceType(bean.getDbType());
            }
        }

        table.getColumns().add(metadataColumn);

        return true;

    }

    private String getUniqueColumnName(MetadataTable table, DBTableForDelimitedBean bean) {
        if (isNullable(bean.getLabel()) || isNullable(bean.getOriginalLabel())) {
            // can't add
            return null;
        }
        if (isExistedColumn(table, bean.getOriginalLabel(), true)) {
            // not allow the duplicated OriginalField
            return null;
        }
        if (isExistedColumn(table, bean.getLabel(), false)) {

            UniqueStringGenerator<MetadataColumn> uniqueStringGenerator = new UniqueStringGenerator<MetadataColumn>("newColumn", //$NON-NLS-1$
                    table.getColumns()) {

                @Override
                protected String getBeanString(MetadataColumn bean) {
                    return bean.getLabel();
                }
            };
            return uniqueStringGenerator.getUniqueString();

        }
        return bean.getLabel();

    }

    private boolean isExistedColumn(MetadataTable table, String label, boolean isOriginal) {
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

    private boolean isContainTableName(String connName, String tableName, boolean isRejected) {
        Map<String, Set<String>> tmpMap = null;
        if (isRejected) {
            tmpMap = rejectedTableNameMap;
        } else {
            tmpMap = tableNameMap;
        }

        Set<String> tableSet = tmpMap.get(connName);
        if (tableSet == null) {
            tmpMap.put(connName, new HashSet<String>());
        } else {
            return tableSet.contains(tableName);
        }
        return false;
    }

    private void addTableName(String connName, String tableName, boolean isRejected) {
        if (isNullable(connName) || isNullable(tableName)) {
            return;
        }
        Map<String, Set<String>> tmpMap = null;
        if (isRejected) {
            tmpMap = rejectedTableNameMap;
        } else {
            tmpMap = tableNameMap;
        }
        Set<String> tableSet = tmpMap.get(connName);
        if (tableSet == null) {
            tableSet = new HashSet<String>();
            tableSet.add(tableName);
            tmpMap.put(connName, tableSet);
        } else {
            tableSet.add(tableName);
        }
    }

    public File getRejectsFile() {
        File path = importedfile.getParentFile();
        return new File(path, importedName + ".rejects"); //$NON-NLS-1$

    }

    public File getLogsFile() {
        File path = importedfile.getParentFile();
        return new File(path, importedName + ".log"); //$NON-NLS-1$

    }

    /**
     * Getter for connectionCreated.
     * 
     * @return the connectionCreated
     */
    public boolean isConnectionCreated() {
        return connectionCreated;
    }

    /**
     * 
     * write the records to log.
     * 
     * @param log
     */
    public void writeLogs() {
        StringBuilder sb = new StringBuilder();

        sb.append(LogDetailsHelper.getMainReportLogs(ProcessType.IMPORT, processRecords));
        sb.append(LogDetailsHelper.getMainReportLogs(ProcessType.REJECT, processRecords));
        if (rejectedNum > 0) {
            sb.append(Messages.getString("ConnectionDBTableHelper.UnknownData")); //$NON-NLS-1$
            sb.append(rejectedNum);
            sb.append(LogDetailsHelper.RETURN);
        }
        sb.append(LogDetailsHelper.RETURN);

        boolean haveRejects = processRecords.getRecords(ProcessType.REJECT, RecordsType.CONNECTION) > 0;
        // have more rejected records
        if (haveRejects) {
            sb.append(Messages.getString("ConnectionDBTableHelper.Details")); //$NON-NLS-1$
            sb.append(LogDetailsHelper.RETURN);
            // added the existed table
            sb.append(LogDetailsHelper.getExistedTableLogs(processRecords));
            // Type unknown
            sb.append(LogDetailsHelper.getUnknownLogs(processRecords));
            sb.append(LogDetailsHelper.RETURN);
        }

        // // added the unknown line
        // if (rejectedNum > 0) {
        // sb.append(Messages.getString("ConnectionDBTableHelper.UnknownLine")); //$NON-NLS-1$
        // sb.append(LogDetailsHelper.RETURN);
        // sb.append(unknownLine);
        // }

        PrintWriter pw = null;
        try {
            pw = new PrintWriter(new FileWriter(getLogsFile()));
            pw.println(sb.toString());

            pw.flush();
        } catch (FileNotFoundException e) {
            // 
        } catch (IOException e) {
            // 
        } finally {
            if (pw != null) {
                pw.close();
                pw = null;
            }
        }

    }

    public void recordRejects(DBTableForDelimitedBean bean, ConnectionItem connItem) {
        if (bean == null) {
            rejectedNum++;
        } else {
            if (connItem == null) {
                addTableName(bean.getName(), bean.getTableName(), true);
            } else {
                addTableName(connItem.getConnection().getLabel(), bean.getTableName(), true);
            }

            processRecords.rejectRecords(bean);
        }
    }

    /**
     * 
     * write .rejects
     * 
     * @param line
     * @param bean
     */
    public void writeRejects() {
        File rFile = getRejectsFile();
        if (rFile == null || importedfile == null) {
            return;
        }

        PrintWriter pw = null;
        BufferedReader reader = null;
        boolean created = false;
        try {
            pw = new PrintWriter(new FileWriter(rFile), true);
            reader = new BufferedReader(new FileReader(importedfile));
            String line;
            while ((line = reader.readLine()) != null) {
                DBTableForDelimitedBean bean = getRowData(line);
                if (bean != null) {
                    // connection name
                    String connName = connNameMap.get(bean.getName());
                    if (connName == null || isContainTableName(connName, bean.getTableName(), true)) {
                        // rejected.
                        pw.println(line);
                        created = true;
                    }
                } else {
                    pw.println(line);
                    unknownLine.append(line);
                    unknownLine.append(LogDetailsHelper.RETURN);
                    created = true;
                }

            }
            pw.flush();
        } catch (FileNotFoundException e) {
            //
        } catch (IOException e) {
            //

        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    // nothing to do
                }
                reader = null;
            }
            if (pw != null) {
                pw.close();
                pw = null;
            }
            if (!created && rFile.exists()) {
                rFile.delete();
            }

        }
        try {
            deleteGeneratedTable();
        } catch (PersistenceException e) {
            // 
        }

    }

    private void deleteGeneratedTable() throws PersistenceException {
        Set<String> connSet = rejectedTableNameMap.keySet();
        if (connSet == null) {
            return;
        }
        for (String connName : connSet) {
            // also be generated connection
            String connMapName = connNameMap.get(connName);
            if (connMapName == null) {
                continue;
            }
            ConnectionItem connItem = searchConnectionItem(connName, false);

            if (connItem != null) {
                DatabaseConnection conn = (DatabaseConnection) connItem.getConnection();
                Set<String> rejectedTableSet = rejectedTableNameMap.get(connName);
                if (rejectedTableSet != null) {
                    for (String tableName : rejectedTableSet) {
                        Set<String> tableSet = tableNameMap.get(connName);
                        if (tableSet != null && tableSet.contains(tableName)) {

                            MetadataTable table = searchMetadataTable(conn, tableName);
                            if (table != null) {
                                conn.getTables().remove(table);
                            }
                        }
                    }
                }
                factory.save(connItem);
            }
        }
    }

    /**
     * 
     * record generated connection , connectionItem and table name.
     * 
     * @param rType
     * @param bean
     */
    public void addRecords(DBTableForDelimitedBean bean) {
        processRecords.importedRecords(bean);
    }
}
