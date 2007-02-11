// ============================================================================
//
// Talend Community Edition
//
// Copyright (C) 2006 Talend - www.talend.com
//
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either
// version 2.1 of the License, or (at your option) any later version.
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
package org.talend.sqlbuilder.repository.utility;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.List;

import org.talend.core.model.metadata.IMetadataConnection;
import org.talend.core.model.metadata.builder.ConvertionHelper;
import org.talend.core.model.metadata.builder.connection.DatabaseConnection;
import org.talend.core.model.metadata.builder.connection.MetadataColumn;
import org.talend.core.model.metadata.builder.connection.MetadataTable;
import org.talend.core.model.metadata.builder.connection.QueriesConnection;
import org.talend.core.model.metadata.builder.connection.Query;
import org.talend.core.model.metadata.builder.database.ExtractMetaDataUtils;
import org.talend.core.model.properties.DatabaseConnectionItem;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.repository.model.RepositoryNode;
import org.talend.sqlbuilder.Messages;
import org.talend.sqlbuilder.SqlBuilderPlugin;
import org.talend.sqlbuilder.dbstructure.RepositoryNodeType;
import org.talend.sqlbuilder.dbstructure.DBTreeProvider.MetadataColumnRepositoryObject;
import org.talend.sqlbuilder.dbstructure.DBTreeProvider.MetadataTableRepositoryObject;

/**
 * qzhang class global comment. Detailled comment <br/>
 * 
 * $Id: talend-code-templates.xml 1 2006-09-29 17:06:40 +0000 (Fri, 29 Sep 2006) nrousseau $
 * 
 */
public final class EMFRepositoryNodeManager {

    public static final String TABLE_ALIAS_PREFIX = "TableAlias:";

    public static final String COLUMN_ALIAS_PREFIX = "ColumnAlias:";

    private static EMFRepositoryNodeManager instance = new EMFRepositoryNodeManager();

    private DatabaseMetaData dbMetaData;

    private SQLBuilderRepositoryNodeManager rnmanager = new SQLBuilderRepositoryNodeManager();

    /**
     * qzhang EMFRepositoryNodeManager constructor comment.
     */
    private EMFRepositoryNodeManager() {
    }

    /**
     * dev Comment method "getQueryByLabel".
     * 
     * @param node all repository node Type
     * @param label search query label
     * @return
     */
    @SuppressWarnings("unchecked")//$NON-NLS-1$
    public Query getQueryByLabel(RepositoryNode node, String label) {
        RepositoryNode root = null;
        if (node.getObjectType().equals(ERepositoryObjectType.METADATA_CON_QUERY)) {
            root = node.getParent().getParent();
        } else {
            root = SQLBuilderRepositoryNodeManager.getRoot(node);
        }
        if (root == null) {
            return null;
        }
        DatabaseConnectionItem item = SQLBuilderRepositoryNodeManager.getItem(root);
        DatabaseConnection connection = (DatabaseConnection) item.getConnection();
        QueriesConnection queriesConnection = (QueriesConnection) connection.getQueries().get(0);
        List<Query> queries = queriesConnection.getQuery();
        for (Query query : queries) {
            if (query.getLabel().equals(label)) {
                return query;
            }
        }
        return null;
    }

    @SuppressWarnings("unchecked")//$NON-NLS-1$
    public List<MetadataTable> getTables(List<RepositoryNode> nodes, List<MetadataColumn> selectedColumns) {
        List<MetadataTable> tables = new ArrayList<MetadataTable>();
        IMetadataConnection iMetadataConnection = null;
        RepositoryNode root = null;
        for (RepositoryNode node : nodes) {
            RepositoryNodeType type = SQLBuilderRepositoryNodeManager.getRepositoryType(node);
            if (type == RepositoryNodeType.DATABASE) {
                root = node;
                DatabaseConnection connection = (DatabaseConnection) SQLBuilderRepositoryNodeManager.getItem(node)
                        .getConnection();
                for (MetadataTable table : (List<MetadataTable>) connection.getTables()) {
                    if (!tables.contains(table)) {
                        tables.add(table);
                        selectedColumns.addAll(table.getColumns());
                    }
                }
                // if database is selected , It does not need to check others.
                break;
            } else if (type == RepositoryNodeType.TABLE) {
                MetadataTable table = ((MetadataTableRepositoryObject) node.getObject()).getTable();
                if (!tables.contains(table)) {
                    tables.add(table);
                    selectedColumns.addAll(table.getColumns());
                }
                if (root == null) {
                    root = SQLBuilderRepositoryNodeManager.getRoot(node);
                }

            } else if (type == RepositoryNodeType.COLUMN) {

                MetadataColumn column = ((MetadataColumnRepositoryObject) node.getObject()).getColumn();
                if (!selectedColumns.contains(column)) {
                    selectedColumns.add(column);
                }

                MetadataTable table = column.getTable();
                if (!tables.contains(table)) {
                    tables.add(table);
                }
                if (root == null) {
                    root = SQLBuilderRepositoryNodeManager.getRoot(node);
                }
            }

        }
        if (root != null) {
            iMetadataConnection = ConvertionHelper.convert((DatabaseConnection) SQLBuilderRepositoryNodeManager.getItem(root)
                    .getConnection());
            dbMetaData = rnmanager.getDatabaseMetaData(iMetadataConnection);
        }
        return tables;
    }

    public List<String[]> getPKFromTables(List<MetadataTable> tables) {
        List<String[]> fks = new ArrayList<String[]>();
        String fk = ""; //$NON-NLS-1$
        String pk = ""; //$NON-NLS-1$
        for (MetadataTable table : tables) {
            try {
                if (dbMetaData != null) {
                    ResultSet resultSet = dbMetaData.getExportedKeys("", ExtractMetaDataUtils.schema, table.getSourceName()); //$NON-NLS-1$
                    ResultSetMetaData metadata = resultSet.getMetaData();
                    int[] relevantIndeces = new int[metadata.getColumnCount()];
                    for (int i = 1; i <= metadata.getColumnCount(); i++) {
                        relevantIndeces[i - 1] = i;
                    }

                    while (resultSet.next()) {
                        for (int i = 0; i < relevantIndeces.length; i++) {
                            String key = metadata.getColumnName(relevantIndeces[i]);
                            if (key.equals("FKCOLUMN_NAME")) { //$NON-NLS-1$
                                fk += resultSet.getString(relevantIndeces[i]);
                            } else if (key.equals("FKTABLE_NAME")) { //$NON-NLS-1$
                                fk = resultSet.getString(relevantIndeces[i]) + "."; //$NON-NLS-1$
                            } else if (key.equals("PKCOLUMN_NAME")) { //$NON-NLS-1$
                                pk = table.getSourceName() + "." + resultSet.getString(relevantIndeces[i]); //$NON-NLS-1$
                            }
                        }
                        if (!"".equals(fk) && !"".equals(pk)) { //$NON-NLS-1$ //$NON-NLS-2$
                            String[] strs = new String[2];
                            strs[0] = pk;
                            strs[1] = fk;
                            fks.add(strs);
                            fk = ""; //$NON-NLS-1$
                            pk = ""; //$NON-NLS-1$
                        }
                    }
                    resultSet.close();
                }

            } catch (Exception e) {
                SqlBuilderPlugin.log(Messages.getString("EMFRepositoryNodeManager.logMessage"), e); //$NON-NLS-1$
            }
        }
        if (!relations.isEmpty()) {
            fks.addAll(relations);
            relations.clear();
        }
        return fks;
    }

    private List<String[]> relations = new ArrayList<String[]>();

    @SuppressWarnings("unchecked")
    public List<RepositoryNode> parseSqlStatement(String sql, RepositoryNode currRoot) throws Exception{

        List<String> tableNames = new ArrayList<String>();
        List<String> columnsNames = new ArrayList<String>();

        parseSqlToNameList(sql, tableNames, columnsNames);

        List<RepositoryNode> nodes = new ArrayList<RepositoryNode>();
        for (RepositoryNode tableNode : currRoot.getChildren()) {
            for (int i = 0; i < tableNames.size(); i++) {
                String tableLabel = tableNode.getObject().getLabel();
                boolean isNeed = false;
                if (columnsNames.size() == 1 && columnsNames.get(0).equals("*")) {
                    for (String string : tableNames) {
                        if (string.equals(tableLabel.toLowerCase())) {
                            nodes.add(tableNode);
                            isNeed = true;
                        }
                    }
                }
                if (tableLabel != null) {
                    for (String string : tableNames) {
                        if (string.equals(tableLabel.toLowerCase())) {
                            isNeed = true;
                        }
                    }
                }
                if (isNeed) {
                    for (RepositoryNode colNode : tableNode.getChildren()) {
                        String collabel = colNode.getObject().getLabel();
                        if (collabel != null) {
                            for (String string : columnsNames) {
                                if (string.equals(collabel.toLowerCase())) {
                                    nodes.add(colNode);
                                }
                                if (string.equals(tableLabel.toLowerCase() + "." + collabel.toLowerCase())) {
                                    if (!nodes.contains(colNode)) {
                                        nodes.add(colNode);
                                    }
                                }
                                for (int j = 0; j < relations.size(); j++) {
                                    String[] pks = relations.get(j);
                                    String pk = pks[0];
                                    String fk = pks[1];
                                    boolean isSet = false;
                                    if (pk.equals(collabel.toLowerCase())) {
                                        isSet = true;
                                        pk = tableLabel.toLowerCase() + "." + collabel.toLowerCase();
                                        if (!nodes.contains(colNode)) {
                                            nodes.add(colNode);
                                        }
                                    }
                                    if (fk.equals(collabel.toLowerCase())) {
                                        isSet = true;
                                        fk = tableLabel.toLowerCase() + "." + collabel.toLowerCase();
                                        if (!nodes.contains(colNode)) {
                                            nodes.add(colNode);
                                        }
                                    }
                                    if (pk.equals(tableLabel.toLowerCase() + "." + collabel.toLowerCase())) {
                                        if (!nodes.contains(colNode)) {
                                            nodes.add(colNode);
                                        }
                                    }
                                    if (fk.equals(tableLabel.toLowerCase() + "." + collabel.toLowerCase())) {
                                        if (!nodes.contains(colNode)) {
                                            nodes.add(colNode);
                                        }
                                    }
                                    if (isSet) {
                                        relations.set(j, new String[] { pk, fk });
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        return nodes;
    }

    /**
     * qzhang Comment method "parseSqlToNameList".
     * 
     * @param sql
     * @param tableNames
     * @param columnsNames
     */
    private void parseSqlToNameList(String sql, List<String> tableNames, List<String> columnsNames) throws Exception{
        String lcSql = sql.toLowerCase();
        String select = lcSql.split("select ")[1];
        String[] s = select.split(" from ");
        String[] columns = s[0].split(",");
        String fromStr = s[1];
        String whereStr = "";
        int indexWhere = s[1].indexOf(" where ");
        if (indexWhere != -1) {
            fromStr = s[1].split(" where ")[0];
            whereStr = s[1].split(" where ")[1];
        }
        String[] tables = fromStr.split(",");
        String[] rel = whereStr.split(" and ");

        for (String string : columns) {
            int dotIndex = string.indexOf(".");
            if (dotIndex != -1) {
                tableNames.add(string.substring(0, dotIndex).trim());
            }
            columnsNames.add(string.trim());
        }
        for (String string : tables) {
            String tableName = string;
            if (string.contains(".")) {
                tableName = string.substring(string.indexOf(".") + 1);
            }
            if (!tableNames.contains(tableName.trim())) {
                tableNames.add(tableName);
            }
        }
        for (int i = 0; i < rel.length; i++) {
            String[] strs = rel[i].split("=");
            if (strs.length == 2) {
                strs[0] = strs[0].trim();
                strs[1] = strs[1].trim();
                relations.add(strs);
            }
        }

        // fixed table Names when contains alias.
        fixedNamesContainAlias(tableNames, TABLE_ALIAS_PREFIX);

        // fixed column Names when contains alias.
        fixedNamesContainAlias(columnsNames, COLUMN_ALIAS_PREFIX);

        // replace table's alias with table real name in columnsNames.
        for (int i = 0; i < columnsNames.size(); i++) {
            String string = columnsNames.get(i);
            if (string.contains(".")) {
                columnsNames.set(i, string.substring(string.indexOf(".") + 1));
                
            }
        }
    }

    /**
     * qzhang Comment method "fixedNamesContainAlias".
     * 
     * @param tableNames
     */
    private void fixedNamesContainAlias(List<String> tableNames, String prefix) throws Exception{
        for (int i = 0; i < tableNames.size(); i++) {
            String name = tableNames.get(i);
            String[] aliasNames = name.split(" ");
            List<String> tableContainAlias = new ArrayList<String>();
            String aliasName = "";
            String realName = "";
            for (int j = 0; j < aliasNames.length; j++) {
                String string = aliasNames[j];
                if (!string.equals("")) {
                    tableContainAlias.add(string);
                }
            }

            if (tableContainAlias.size() == 3) {
                realName = tableContainAlias.get(0);
                aliasName = tableContainAlias.get(2);
                if (!tableNames.contains(aliasName)) {
                    tableNames.add(prefix + realName + "=" + aliasName);
                } else {
                    tableNames.set(tableNames.indexOf(aliasName), prefix + realName + "=" + aliasName);
                }
                tableNames.set(i, realName);
            } else if (tableContainAlias.size() == 2) {
                realName = tableContainAlias.get(0);
                aliasName = tableContainAlias.get(1);
                if (!tableNames.contains(aliasName)) {
                    tableNames.add(prefix + realName + "=" + aliasName);
                } else {
                    tableNames.set(tableNames.indexOf(aliasName), prefix + realName + "=" + aliasName);
                }
                tableNames.set(i, realName);
            } else if (tableContainAlias.size() == 1) {
                realName = tableContainAlias.get(0);
                tableNames.set(i, name.trim());
            }
        }
    }

    public static EMFRepositoryNodeManager getInstance() {
        return instance;
    }

}
