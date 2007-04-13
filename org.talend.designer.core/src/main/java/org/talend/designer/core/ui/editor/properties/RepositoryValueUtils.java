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
package org.talend.designer.core.ui.editor.properties;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.BidiMap;
import org.eclipse.swt.widgets.Composite;
import org.talend.commons.exception.PersistenceException;
import org.talend.core.model.metadata.IMetadataTable;
import org.talend.core.model.metadata.builder.ConvertionHelper;
import org.talend.core.model.metadata.builder.connection.Connection;
import org.talend.core.model.metadata.builder.connection.DatabaseConnection;
import org.talend.core.model.metadata.builder.connection.QueriesConnection;
import org.talend.core.model.metadata.builder.connection.Query;
import org.talend.core.model.metadata.designerproperties.RepositoryToComponentProperty;
import org.talend.core.model.process.EComponentCategory;
import org.talend.core.model.process.EConnectionType;
import org.talend.core.model.process.Element;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.designer.core.DesignerPlugin;
import org.talend.designer.core.model.components.EParameterName;
import org.talend.designer.core.ui.editor.nodes.Node;
import org.talend.repository.model.ERepositoryStatus;
import org.talend.repository.model.IProxyRepositoryFactory;

/**
 * DOC Administrator class global comment. Detailled comment <br/>
 * 
 */
public class RepositoryValueUtils {

    protected Element elem;

    protected Composite composite;

    protected BidiMap hashCurControls;

    protected String currentComponent;

    protected EComponentCategory section;

    private Map<String, IMetadataTable> repositoryTableMap;

    private Map<String, ConnectionItem> repositoryConnectionItemMap;

    private Map<String, Query> repositoryQueryStoreMap;

    private Map<String, String> tableIdAndDbTypeMap;

    private Map<String, String> tableIdAndDbSchemaMap;

    private Map<String, List<String>> tablesMap = new HashMap<String, List<String>>();

    private Map<String, List<String>> queriesMap = new HashMap<String, List<String>>();

    // added
    private Map<String, String> repositoryDBIdAndNameMap = new HashMap<String, String>();

    public Map<String, String> getRepositoryDBIdAndNameMap() {
        return this.repositoryDBIdAndNameMap;
    }

    // end

    /**
     * DOC Administrator RepositoryValueUtils constructor comment.
     */
    public RepositoryValueUtils() {
        this.elem = elem;
        updateRepositoryList();
    }

    private void updateRepositoryList() {
        IProxyRepositoryFactory factory = DesignerPlugin.getDefault().getProxyRepositoryFactory();
        tableIdAndDbTypeMap = new HashMap<String, String>();
        tableIdAndDbSchemaMap = new HashMap<String, String>();
        repositoryTableMap = new HashMap<String, IMetadataTable>();
        repositoryQueryStoreMap = new HashMap<String, Query>();
        repositoryConnectionItemMap = new HashMap<String, ConnectionItem>();

        List<ConnectionItem> metadataConnectionsItem = null;
        try {
            metadataConnectionsItem = factory.getMetadataConnectionsItem();
        } catch (PersistenceException e) {
            throw new RuntimeException(e);
        }
        if (metadataConnectionsItem != null) {
            repositoryTableMap.clear();
            repositoryQueryStoreMap.clear();
            repositoryConnectionItemMap.clear();

            tablesMap.clear();
            queriesMap.clear();
            List<String> tableNamesList = new ArrayList<String>();
            List<String> tableValuesList = new ArrayList<String>();
            List<String> queryStoreNameList = new ArrayList<String>();
            List<String> queryStoreValuesList = new ArrayList<String>();
            for (ConnectionItem connectionItem : metadataConnectionsItem) {
                Connection connection = (Connection) connectionItem.getConnection();
                if (!connection.isReadOnly()) {
                    repositoryConnectionItemMap.put(connectionItem.getProperty().getId() + "", connectionItem); //$NON-NLS-1$
                    repositoryDBIdAndNameMap.put(connectionItem.getProperty().getId(),
                            getRepositoryAliasName(connectionItem) + ":" //$NON-NLS-1$
                                    + connectionItem.getProperty().getLabel());
                    for (Object tableObj : connection.getTables()) {
                        org.talend.core.model.metadata.builder.connection.MetadataTable table;

                        table = (org.talend.core.model.metadata.builder.connection.MetadataTable) tableObj;

                        if (factory.getStatus(connectionItem) != ERepositoryStatus.DELETED) {
                            if (!factory.isDeleted(table)) {
                                String name = getRepositoryAliasName(connectionItem) + ":" //$NON-NLS-1$
                                        + connectionItem.getProperty().getLabel() + " - " + table.getLabel(); //$NON-NLS-1$
                                String value = connectionItem.getProperty().getId() + " - " + table.getLabel(); //$NON-NLS-1$

                                IMetadataTable newTable = ConvertionHelper.convert(table);
                                repositoryTableMap.put(value, newTable);
                                if (connection instanceof DatabaseConnection) {
                                    String dbType = ((DatabaseConnection) connection).getDatabaseType();
                                    String schema = ((DatabaseConnection) connection).getSchema();
                                    tableIdAndDbTypeMap.put(newTable.getId(), dbType);
                                    if (schema != null && !schema.equals("")) {
                                        tableIdAndDbSchemaMap.put(newTable.getId(), schema);
                                    }
                                }
                                tableNamesList.add(name);
                                tableValuesList.add(value);
                            }
                        }
                    }
                }
                tablesMap.put(connectionItem.getProperty().getId(), tableValuesList);
                if (connection instanceof DatabaseConnection && !connection.isReadOnly()) {
                    DatabaseConnection dbConnection = (DatabaseConnection) connection;
                    QueriesConnection queriesConnection = dbConnection.getQueries();
                    if (queriesConnection != null) {
                        List<Query> qs = queriesConnection.getQuery();
                        for (Query query : qs) {
                            String name = getRepositoryAliasName(connectionItem) + ":" //$NON-NLS-1$
                                    + connectionItem.getProperty().getLabel() + " - " + query.getLabel(); //$NON-NLS-1$
                            String value = connectionItem.getProperty().getId() + " - " + query.getLabel(); //$NON-NLS-1$
                            repositoryQueryStoreMap.put(value, query);
                            queryStoreNameList.add(name);
                            queryStoreValuesList.add(value);
                        }
                    }
                }
                queriesMap.put(connectionItem.getProperty().getId(), queryStoreValuesList);
            }
        }
    }

    /**
     * DOC Administrator Comment method "getRepositoryAliasName".
     * 
     * @param connectionItem
     * @return
     */
    public String getRepositoryAliasName(ConnectionItem connectionItem) {
        ERepositoryObjectType repositoryObjectType = ERepositoryObjectType.getItemType(connectionItem);
        String aliasName = repositoryObjectType.getAlias();
        Connection connection = (Connection) connectionItem.getConnection();
        if (connection instanceof DatabaseConnection) {
            String currentDbType = (String) RepositoryToComponentProperty.getValue(connection, "TYPE"); //$NON-NLS-1$
            aliasName += " (" + currentDbType + ")"; //$NON-NLS-1$ //$NON-NLS-2$
        }
        return aliasName;
    }

    /**
     * DOC Administrator Comment method "initMaps".
     */
    private void initMaps() {
        for (String key : tablesMap.keySet()) {
            List<String> tablesName = new ArrayList<String>();
            List<String> queriesName = new ArrayList<String>();
            queriesName.addAll(queriesMap.get(key));
            tablesName.addAll(tablesMap.get(key));
            for (String string : tablesMap.get(key)) {
                if (!string.startsWith(key)) {
                    tablesName.remove(string);
                }
            }

            for (String string : queriesMap.get(key)) {
                if (!string.startsWith(key)) {
                    queriesName.remove(string);
                }
            }
            tablesMap.put(key, tablesName);
            queriesMap.put(key, queriesName);
        }
    }

    /**
     * qzhang Comment method "getDefaultRepository".
     * 
     * @return
     */
    private String getDefaultRepository(boolean istable, String defaultPropertyValue) {
        boolean metadataInput = false;
        if (((Node) elem).getCurrentActiveLinksNbInput(EConnectionType.FLOW_MAIN) > 0
                || ((Node) elem).getCurrentActiveLinksNbInput(EConnectionType.FLOW_REF) > 0
                || ((Node) elem).getCurrentActiveLinksNbInput(EConnectionType.TABLE) > 0) {
            metadataInput = true;
        }

        if (metadataInput && istable) {
            return (String) elem.getPropertyValue(EParameterName.REPOSITORY_SCHEMA_TYPE.getName());
        }
        Object propertyValue = elem.getPropertyValue(EParameterName.REPOSITORY_PROPERTY_TYPE.getName());
        if ((propertyValue == null || !(propertyValue instanceof String)) && defaultPropertyValue != null) {
            propertyValue = defaultPropertyValue;
        }
        if (propertyValue == null || propertyValue.equals("")) {
            return "";
        }
        if (istable) {
            List<String> list = tablesMap.get(propertyValue);
            if (list != null) {
                if (list.size() > 0) {
                    return tablesMap.get(propertyValue).get(0);
                }
            }
        } else {
            List<String> list = queriesMap.get(propertyValue);
            if (list != null) {
                if (queriesMap.get(propertyValue).size() > 0) {
                    return queriesMap.get(propertyValue).get(0);
                }
            }
        }
        return "";
    }

    /**
     * dev Comment method "getRepositoryTableMap".
     * 
     * @return Map
     */
    public Map<String, IMetadataTable> getRepositoryTableMap() {
        if (this.repositoryTableMap.keySet().isEmpty()) {
            updateRepositoryList();
        }
        return this.repositoryTableMap;
    }

    /**
     * dev Comment method "getRepositoryConnectionItemMap".
     * 
     * @return Map
     */
    public Map<String, ConnectionItem> getRepositoryConnectionItemMap() {
        return this.repositoryConnectionItemMap;
    }

    /**
     * Getter for repositoryQueryStoreMap.
     * 
     * @return the repositoryQueryStoreMap
     */
    public Map<String, Query> getRepositoryQueryStoreMap() {
        return repositoryQueryStoreMap;
    }

    /**
     * Getter for tablesMap.
     * 
     * @return the tablesMap
     */
    public Map<String, List<String>> getTablesMap() {
        initMaps();
        return this.tablesMap;
    }

    /**
     * Getter for queriesMap.
     * 
     * @return the queriesMap
     */
    public Map<String, List<String>> getQueriesMap() {
        initMaps();
        return this.queriesMap;
    }

    /**
     * Getter for tableIdAndDbTypeMap.
     * 
     * @return the tableIdAndDbTypeMap
     */
    public Map<String, String> getTableIdAndDbTypeMap() {
        return this.tableIdAndDbTypeMap;
    }

    /**
     * Getter for tableIdAndDbSchemaMap.
     * 
     * @return the tableIdAndDbSchemaMap
     */
    public Map<String, String> getTableIdAndDbSchemaMap() {
        return this.tableIdAndDbSchemaMap;
    }
}
