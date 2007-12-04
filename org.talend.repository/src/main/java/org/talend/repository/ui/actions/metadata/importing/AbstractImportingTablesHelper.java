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
package org.talend.repository.ui.actions.metadata.importing;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import org.talend.commons.exception.BusinessException;
import org.talend.commons.exception.PersistenceException;
import org.talend.commons.utils.data.list.UniqueStringGenerator;
import org.talend.core.CorePlugin;
import org.talend.core.context.Context;
import org.talend.core.context.RepositoryContext;
import org.talend.core.database.EDatabaseTypeName;
import org.talend.core.language.ECodeLanguage;
import org.talend.core.language.LanguageManager;
import org.talend.core.model.metadata.MetadataTalendType;
import org.talend.core.model.metadata.builder.connection.Connection;
import org.talend.core.model.metadata.builder.connection.ConnectionFactory;
import org.talend.core.model.metadata.builder.connection.MetadataColumn;
import org.talend.core.model.metadata.builder.connection.MetadataTable;
import org.talend.core.model.metadata.types.JavaTypesManager;
import org.talend.core.model.metadata.types.TypesManager;
import org.talend.core.model.properties.ConnectionItem;
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
import org.talend.repository.ui.actions.metadata.database.DBBeanParserHelper;
import org.talend.repository.ui.actions.metadata.database.DBProcessRecords;
import org.talend.repository.ui.actions.metadata.database.DBTableForDelimitedBean;
import org.talend.repository.ui.actions.metadata.database.LogDetailsHelper;
import org.talend.repository.ui.actions.metadata.database.DBProcessRecords.ProcessType;
import org.talend.repository.ui.actions.metadata.database.DBProcessRecords.RecordsType;
import org.talend.repository.ui.actions.metadata.database.DBProcessRecords.RejectedType;
import org.talend.repository.ui.actions.metadata.database.DBTableForDelimitedBean.BeanType;

/**
 * DOC ggu class global comment. Detailled comment
 */
public abstract class AbstractImportingTablesHelper {

    private File importedfile = null;

    private String importedName = ""; //$NON-NLS-1$

    protected boolean connectionCreated = false;

    // private StringBuilder unknownLine = new StringBuilder();

    protected Map<String, String> connNameMap = new HashMap<String, String>();

    protected Map<String, Set<String>> tableNameMap = new HashMap<String, Set<String>>();

    protected Map<String, Set<String>> rejectedTableNameMap = new HashMap<String, Set<String>>();

    protected final IProxyRepositoryFactory factory = ProxyRepositoryFactory.getInstance();

    protected DBProcessRecords processRecords = new DBProcessRecords();

    protected int rejectedNum = 0;

    protected ERepositoryObjectType nodeType;

    /**
     * DOC ggu AbstractImportingTableHelper constructor comment.
     */
    public AbstractImportingTablesHelper(ERepositoryObjectType nodeType, File file) {
        this.nodeType = nodeType;
        this.importedfile = file;
        String name = file.getName();
        int index = name.lastIndexOf("."); //$NON-NLS-1$
        if (index > 0) {
            importedName = name.substring(0, index);
        } else {
            importedName = name;
        }
    }

    public boolean isNullable(String name) {
        if (name == null || "".equals(name.trim())) { //$NON-NLS-1$
            return true;
        }
        return false;
    }

    public File getImportedFile() {
        return this.importedfile;
    }

    public File getRejectsFile() {
        File path = importedfile.getParentFile();
        return new File(path, importedName + ".rejects"); //$NON-NLS-1$

    }

    public File getLogsFile() {
        File path = importedfile.getParentFile();
        return new File(path, importedName + ".log"); //$NON-NLS-1$

    }

    public boolean isConnectionCreated() {
        return this.connectionCreated;
    }

    public void setConnectionCreated(boolean connectionCreated) {
        this.connectionCreated = connectionCreated;
    }

    public ConnectionItem convertBeanToItem(DBTableForDelimitedBean bean) throws PersistenceException, BusinessException {

        if (isNullable(bean.getName())) {
            return null;
        }
        setConnectionCreated(false);
        ConnectionItem connItem = getSuitableConnectionItem(bean);
        if (connItem == null) {
            return null;
        }

        Connection conn = connItem.getConnection();
        if (conn == null) {
            return null;
        }
        if (bean.getBeanType() == BeanType.COLUMN || bean.getBeanType() == BeanType.TABLE) {
            if (!setMetadataTableData(conn, bean)) {
                addTableName(bean.getName(), bean.getTableName(), true);
                return null;
            }
        }
        connNameMap.put(bean.getName(), connItem.getProperty().getLabel());
        return connItem;
    }

    protected ConnectionItem getSuitableConnectionItem(DBTableForDelimitedBean bean) throws PersistenceException,
            BusinessException {
        if (isNullable(bean.getName())) {
            return null;
        }

        if (!Pattern.matches(RepositoryConstants.getPattern(ERepositoryObjectType.METADATA_CONNECTIONS), bean.getName())) {
            return null;
        }

        ConnectionItem connItemAll = searchConnectionItem(bean.getName(), true);
        ConnectionItem connItem = searchConnectionItem(bean.getName(), false);
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

    protected abstract ConnectionItem createConnectionItem(DBTableForDelimitedBean bean);

    protected ConnectionItem searchConnectionItem(String name, boolean withDeleted) throws PersistenceException {
        if (isNullable(name)) {
            return null;
        }
        List<IRepositoryObject> repositoryObjs = factory.getAll(nodeType, withDeleted);
        for (IRepositoryObject obj : repositoryObjs) {
            if (obj.getLabel().toLowerCase().equals(name.toLowerCase())) {
                Item item = obj.getProperty().getItem();
                if (item instanceof ConnectionItem) {
                    return (ConnectionItem) item;
                }
            }
        }
        return null;
    }

    /*
     * set the table data in a connection.
     */

    protected boolean setMetadataTableData(Connection conn, DBTableForDelimitedBean bean) {

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
            metadataTable.setId(factory.getNextId());
            metadataTable.setLabel(bean.getTableName());
            if (nodeType == ERepositoryObjectType.METADATA_CONNECTIONS) {
                metadataTable.setSourceName(bean.getOriginalTableName());
                metadataTable.setTableType("TABLE"); //$NON-NLS-1$ 
            }
            // record the generated table name.
            addTableName(bean.getName(), bean.getTableName(), false);
        } else if (!isContainTableName(bean.getName(), bean.getTableName(), false)) {
            // have existed table, and it's the existed table before.
            // reject it.
            processRecords.addExistedTable(bean);
            // FIXME maybe need check the table in recycle bin.
            return false;

        }
        if (bean.getBeanType() == BeanType.TABLE) {
            // only add table.
            return true;
        }

        boolean isOk = setMetadataColumnData(metadataTable, bean);
        if (!isOk) {
            return false;
        }

        if (!found) {
            // avoid add empty table.
            metadataTable.setConnection(conn);
            conn.getTables().add(metadataTable);
        }
        return true;

    }

    protected Property createProperty(ConnectionItem connItem, DBTableForDelimitedBean bean) {

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

    protected void setPropNewName(Property theProperty) throws PersistenceException, BusinessException {
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

    /**
     * 
     * write .rejects
     * 
     * @param line
     * @param bean
     */
    protected MetadataTable searchMetadataTable(Connection conn, String name) {
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
    protected boolean setMetadataColumnData(MetadataTable table, DBTableForDelimitedBean bean) {

        MetadataColumn metadataColumn = ConnectionFactory.eINSTANCE.createMetadataColumn();

        metadataColumn.setComment(bean.getComment());

        String genLabel = getUniqueColumnName(table, bean);
        if (genLabel == null) {
            return false;
        }
        metadataColumn.setLabel(genLabel);

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
        if (nodeType == ERepositoryObjectType.METADATA_CONNECTIONS) {

            metadataColumn.setOriginalField(bean.getOriginalLabel());

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
        }
        table.getColumns().add(metadataColumn);

        return true;

    }

    protected String getUniqueColumnName(MetadataTable table, DBTableForDelimitedBean bean) {
        if (isNullable(bean.getOriginalLabel())) {
            // can't add
            return null;
        }
        if (isExistedColumn(table, bean.getOriginalLabel(), true)) {
            // not allow the duplicated OriginalField
            return null;
        }
        if (nodeType != ERepositoryObjectType.METADATA_CONNECTIONS) {
            return bean.getOriginalLabel();
        }
        if (isNullable(bean.getLabel())) {
            // can't add
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

    protected boolean isExistedColumn(MetadataTable table, String label, boolean isOriginal) {
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

    public void writeRejects() {
        File rFile = getRejectsFile();
        if (rFile == null || getImportedFile() == null) {
            return;
        }

        PrintWriter pw = null;
        BufferedReader reader = null;
        boolean created = false;
        try {
            pw = new PrintWriter(new FileWriter(rFile), true);
            reader = new BufferedReader(new FileReader(getImportedFile()));
            String line;
            while ((line = reader.readLine()) != null) {
                if (isNullable(line)) {
                    continue;
                }
                DBTableForDelimitedBean bean = DBBeanParserHelper.parseLineToBean(nodeType, line);
                switch (bean.getBeanType()) {
                case COLUMN:
                case TABLE:
                case CONNECTION:
                    // connection name
                    String connName = connNameMap.get(bean.getName());
                    if (connName == null || isContainTableName(connName, bean.getTableName(), true)) {
                        // rejected.
                        pw.println(line);
                        created = true;
                    }
                    break;
                case UNKNOWN:
                default:
                    pw.println(line);
                    created = true;
                    break;

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
                Connection conn = connItem.getConnection();
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

    protected boolean isContainTableName(String connName, String tableName, boolean isRejected) {
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

    protected void addTableName(String connName, String tableName, boolean isRejected) {
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

    public void recordRejects(DBTableForDelimitedBean bean, ConnectionItem connItem) {
        if (bean == null) {
            rejectedNum++;
        } else {
            if (bean.getBeanType() != BeanType.CONNECTION) {
                if (connItem == null) {
                    addTableName(bean.getName(), bean.getTableName(), true);
                } else {
                    addTableName(connItem.getConnection().getLabel(), bean.getTableName(), true);
                }

                processRecords.rejectRecords(bean);
            }
        }
    }

    public void addRecords(DBTableForDelimitedBean bean) {
        processRecords.importedRecords(bean);
    }

}