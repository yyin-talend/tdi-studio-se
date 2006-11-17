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
import java.util.ArrayList;
import java.util.List;

import org.talend.commons.exception.PersistenceException;
import org.talend.core.CorePlugin;
import org.talend.core.context.Context;
import org.talend.core.context.RepositoryContext;
import org.talend.core.model.general.Version;
import org.talend.core.model.metadata.IMetadataConnection;
import org.talend.core.model.metadata.builder.ConvertionHelper;
import org.talend.core.model.metadata.builder.connection.ConnectionFactory;
import org.talend.core.model.metadata.builder.connection.DatabaseConnection;
import org.talend.core.model.metadata.builder.connection.MetadataColumn;
import org.talend.core.model.metadata.builder.connection.MetadataTable;
import org.talend.core.model.metadata.builder.connection.QueriesConnection;
import org.talend.core.model.metadata.builder.connection.Query;
import org.talend.core.model.metadata.builder.database.ExtractMetaDataFromDataBase;
import org.talend.core.model.metadata.builder.database.ExtractMetaDataUtils;
import org.talend.core.model.properties.DatabaseConnectionItem;
import org.talend.core.model.properties.ItemState;
import org.talend.core.model.properties.PropertiesFactory;
import org.talend.core.model.properties.Property;
import org.talend.core.model.repository.IRepositoryObject;
import org.talend.core.model.repository.RepositoryObject;
import org.talend.repository.model.IRepositoryFactory;
import org.talend.repository.model.RepositoryFactoryProvider;
import org.talend.repository.model.RepositoryNode;
import org.talend.repository.model.RepositoryNode.ENodeType;
import org.talend.repository.model.RepositoryNode.EProperties;
import org.talend.repository.ui.utils.ManagerConnection;
import org.talend.sqlbuilder.SqlBuilderPlugin;
import org.talend.sqlbuilder.dbstructure.RepositoryNodeType;
import org.talend.sqlbuilder.util.ConnectionParameters;

/**
 * DOC dev class global comment. Detailled comment <br/>
 * 
 * $Id: talend-code-templates.xml 1 2006-09-29 17:06:40 +0000 (Fri, 29 Sep 2006) nrousseau $
 * 
 */
public class SQLBuilderRepositoryNodeManager {

    private static List<RepositoryNode> repositoryNodes = new ArrayList<RepositoryNode>();

    public void addRepositoryNode(RepositoryNode node) {
        if (repositoryNodes.contains(node)) {
            return;
        }
        repositoryNodes.add(node);
    }

    public RepositoryNode getRepositoryNodebyName(String name) {
        for (RepositoryNode node : repositoryNodes) {
            if (node.getObject().getLabel().equals(name)) {
                return node;
            }
        }
        return null;
    }
    
    @SuppressWarnings("unchecked")
	public static void  reductionALLRepositoryNode() {
    	for (RepositoryNode node : repositoryNodes) {
    		DatabaseConnection connection = (DatabaseConnection) getItem(node).getConnection();
        	List<MetadataTable> tables = connection.getTables();
    		List<MetadataTable> newtables = new ArrayList<MetadataTable>();

    		for (MetadataTable table : tables) {
    			List<MetadataColumn> columns = table.getColumns();
    			List<MetadataColumn> newcloumns = new ArrayList<MetadataColumn>();
    			for (MetadataColumn column : columns) {
    				if (!column.getLabel().equals("")) {
    					newcloumns.add(column);
    				}
    			}
    			table.getColumns().clear();
    			table.getColumns().addAll(newcloumns);
    			if (!table.getLabel().equals("")) {
    				newtables.add(table);
    			}
    		}
    		connection.getTables().clear();
    		connection.getTables().addAll(newtables);
		}
    	
	}

    @SuppressWarnings("unchecked")
	public static List<String> getTableNamesByRepositoryNode(RepositoryNode node) {
    	List<String> tableNames = new ArrayList<String>();
    	DatabaseConnectionItem item = getItem(node);
        DatabaseConnection connection = (DatabaseConnection) item.getConnection();
    	List<MetadataTable> tablesFromEMF = connection.getTables();
    	boolean isOdbc = connection.getSID() == null || connection.getSID().length() == 0;
    	String sid = isOdbc ? connection.getDatasourceName() : connection.getSID(); 
    	for (MetadataTable table : tablesFromEMF) {
    		String tableName = table.getSourceName();
    		if (tableName != null && !"".equals(tableName)) {
    		       tableName = "'" + sid + "'.'" + tableName + "'";
    		       tableNames.add(tableName);
    		}
		}
    	return tableNames;
	}
    public List<String> getALLReposotoryNodeNames() {
		List<String> names = new ArrayList<String>();
		for (RepositoryNode node : repositoryNodes) {
			names.add(node.getObject().getLabel());
		}
		return names;
	}
    /**
     * Get RepositoryNodeFromDB .
     * 
     * @param oldNode selected RepositoryNode
     * @return RepositoryNode from Database
     */
    @SuppressWarnings("unchecked")
    public RepositoryNode getRepositoryNodeFromDB(RepositoryNode oldNode) {

        DatabaseConnectionItem item = getItem(oldNode);
        DatabaseConnection connection = (DatabaseConnection) item.getConnection();
        IMetadataConnection iMetadataConnection = ConvertionHelper.convert(connection);
        boolean status = new ManagerConnection().check(iMetadataConnection);
        connection.setDivergency(!status);
        if (status) {
            // /Get TableNames From DB
            // List<String> tableNamesFromDB =
            // ExtractMetaDataFromDataBase.returnTablesFormConnection(iMetadataConnection);

            // /Get MetadataTable From DB
            List<MetadataTable> tablesFromDB = getTablesFromDB(iMetadataConnection);
            // Get MetadataTable From EMF(Old RepositoryNode)
            List<MetadataTable> tablesFromEMF = connection.getTables();

            for (MetadataTable tableFromDB : tablesFromDB) {
                // /Get MetadataColumn from DB
                List<MetadataColumn> columnsFromDB = ExtractMetaDataFromDataBase.returnMetadataColumnsFormTable(
                        iMetadataConnection, tableFromDB.getLabel());
                for (MetadataTable tableFromEMF : tablesFromEMF) {
                    // /Get MetadataColumn From EMF
                    List<MetadataColumn> columnsFromEMF = tableFromEMF.getColumns();
                    if (tableFromDB.getSourceName().equals(tableFromEMF.getSourceName())) {
                    	fixedColumns(columnsFromDB, columnsFromEMF);
                    }
                }
            }
            fixedTables(tablesFromDB, tablesFromEMF, iMetadataConnection);
        }
        return oldNode;
    }

    
    
    @SuppressWarnings("unchecked")
	public RepositoryNode getRepositoryNodeByBuildIn(RepositoryNode node, ConnectionParameters parameters) {
		DatabaseConnection connection = createConnection(parameters);
		IMetadataConnection iMetadataConnection = ConvertionHelper.convert(connection);
		boolean status = new ManagerConnection().check(iMetadataConnection);
        connection.setDivergency(!status);
        connection.getTables().clear();
        if (status) {
        	List<MetadataTable> tablesFromDB = getTablesFromDB(iMetadataConnection);
        	for (MetadataTable table : tablesFromDB) {
        		List<MetadataColumn> columnsFromDB = ExtractMetaDataFromDataBase.returnMetadataColumnsFormTable(
                        iMetadataConnection, table.getSourceName());
        		table.getColumns().clear();
        		for (MetadataColumn column : columnsFromDB) {
        			column.setLabel("");
        			table.getColumns().add(column);
				}
        		table.setLabel("");
				connection.getTables().add(table);
			}
        }
		DatabaseConnectionItem item = PropertiesFactory.eINSTANCE.createDatabaseConnectionItem();
		Property connectionProperty = PropertiesFactory.eINSTANCE.createProperty();
        connectionProperty
                 .setAuthor(((RepositoryContext) CorePlugin.getContext().getProperty(Context.REPOSITORY_CONTEXT_KEY))
                         .getUser().getEmfUser());
         connectionProperty.setVersion(new Version().toString());
         connectionProperty.setStatusCode("");

         item.setProperty(connectionProperty);
         item.setConnection(connection);
         RepositoryObject object = new RepositoryObject(connectionProperty);
         object.setLabel("");
         ItemState state = PropertiesFactory.eINSTANCE.createItemState();
         state.setDeleted(false);
         item.setState(state);
//         node.getObject().getProperty().setItem(item);
         RepositoryNode newNode = new RepositoryNode(object, node, ENodeType.SYSTEM_FOLDER);
         
    	return newNode;
	}

	/**
	 * DOC dev Comment method "getConnection".
	 * @param parameters
	 * @return
	 */
	private DatabaseConnection createConnection(ConnectionParameters parameters) {
		DatabaseConnection connection = ConnectionFactory.eINSTANCE.createDatabaseConnection();
		connection.setDatabaseType(parameters.getDbType());
		connection.setUsername(parameters.getUserName());
		connection.setPort(parameters.getPort());
		connection.setPassword(parameters.getPassword());
		connection.setSID(parameters.getDbName());
		connection.setLabel(parameters.getDbName());
		connection.setDatasourceName(parameters.getDatasource());
//		connection.setComment("");
		connection.setURL(parameters.getURL());
		connection.setDriverClass(ExtractMetaDataUtils.getDriverClassByDbType(parameters.getDbType()));
		return connection;
	}
    /**
     * DOC dev Comment method "getItem".
     * 
     * @param newNode
     * @return
     */
    private static DatabaseConnectionItem getItem(RepositoryNode newNode) {
        IRepositoryObject repositoryObject = newNode.getObject();
        DatabaseConnectionItem item = (DatabaseConnectionItem) repositoryObject.getProperty().getItem();
        return item;
    }

    public static String getDatabaseNameByRepositoryNode(RepositoryNode node) {
    	DatabaseConnection connection = (DatabaseConnection) getItem(node).getConnection();
    	boolean isOdbc = connection.getSID() == null || connection.getSID().length() == 0;
        return isOdbc ? connection.getDatasourceName() : connection.getSID(); 
    }

    /**
     * Get Tables From DataBase.
     * 
     * @param iMetadataConnection contain Connection's information
     * @return MetadataTable List from Database
     */
    private List<MetadataTable> getTablesFromDB(IMetadataConnection iMetadataConnection) {

        ExtractMetaDataUtils.getConnection(iMetadataConnection.getDbType(), iMetadataConnection.getUrl(),
                iMetadataConnection.getUsername(), iMetadataConnection.getPassword(),
                iMetadataConnection.getDatabase(), iMetadataConnection.getSchema());
        DatabaseMetaData dbMetaData = ExtractMetaDataUtils.getDatabaseMetaData(ExtractMetaDataUtils.conn);

        List<MetadataTable> metadataTables = new ArrayList<MetadataTable>();
        try {
            String[] tableTypes = { "TABLE", "VIEW" };
            ResultSet rsTables = null;
            rsTables = dbMetaData.getTables(null, ExtractMetaDataUtils.schema, null, tableTypes);
            while (rsTables.next()) {
                MetadataTable medataTable = ConnectionFactory.eINSTANCE.createMetadataTable();
                medataTable.setId(metadataTables.size() + 1 + "");
                medataTable.setLabel(rsTables.getString("TABLE_NAME"));
                medataTable.setSourceName(medataTable.getLabel());
                medataTable.setComment(ExtractMetaDataUtils.getStringMetaDataInfo(rsTables, "REMARKS"));
                metadataTables.add(medataTable);
            }
            rsTables.close();
            ExtractMetaDataUtils.closeConnection();
        } catch (Exception e) {
            SqlBuilderPlugin.log("Connection Exception", e);
            throw new RuntimeException(e);
        }
        return metadataTables;
    }

    @SuppressWarnings("unchecked")
    public List<String> getALLQueryLabels(RepositoryNode repositoryNode) {
        List<String> allQueries = new ArrayList<String>();
        DatabaseConnectionItem item = getItem(repositoryNode);
        DatabaseConnection connection = (DatabaseConnection) item.getConnection();
        List<QueriesConnection> qcs = connection.getQueries();
        for (QueriesConnection connection2 : qcs) {
            List<Query> qs = connection2.getQuery();
            for (Query q1 : qs) {
                allQueries.add(q1.getLabel());
            }
        }
        return allQueries;
    }

    @SuppressWarnings("unchecked")
    public void saveQuery(RepositoryNode repositoryNode, Query query) {
        DatabaseConnectionItem item = getItem(repositoryNode);
        QueriesConnection qc = ConnectionFactory.eINSTANCE.createQueriesConnection();
        DatabaseConnection connection = (DatabaseConnection) item.getConnection();
        qc.setConnection(connection);
        qc.getQuery().add(query);
        connection.getQueries().add(qc);
        saveMetaData(item);
    }

    /**
     * save MetaData into EMF's xml files.
     * 
     * @param item need to be saved Item
     */
    public void saveMetaData(DatabaseConnectionItem item) {

        IRepositoryFactory factory = RepositoryFactoryProvider.getInstance();
        try {
            factory.save(item);
        } catch (PersistenceException e) {
            SqlBuilderPlugin.log("Save MetaData Failure", e);
        }
    }

    /**
     * fixed Table .
     * 
     * @param metaFromDB MetadataTable from Database
     * @param metaFromEMF MetadataTable from Emf
     */
    @SuppressWarnings("unchecked")
	private void fixedTables(List<MetadataTable> metaFromDB, List<MetadataTable> metaFromEMF, IMetadataConnection iMetadataConnection) {

    	for (MetadataTable emf : metaFromEMF) {
        	boolean flag = true;
			for (MetadataTable db : metaFromDB) {
				if (db.getSourceName().equals(emf.getSourceName())) {
					flag = false;
					break;
				}
			}
			if (flag) {
				emf.setDivergency(true);
			}
		}
        while (!metaFromDB.isEmpty()) {
            boolean flag = true;
            MetadataTable db = metaFromDB.remove(0);
            for (MetadataTable emf : metaFromEMF) {
                if (db.getSourceName().equals(emf.getSourceName())) {
                    flag = false;
                    if (emf.getLabel().equals(db.getSourceName().replaceAll("_", "-"))) {
                        emf.setDivergency(false);
                    } else {
                        emf.setDivergency(true);
                    }
                }
            }
            if (flag) {
            	MetadataTable table = ConnectionFactory.eINSTANCE.createMetadataTable();
            	table.setSourceName(db.getSourceName());
            	table.setLabel("");
            	 List<MetadataColumn> columns = ExtractMetaDataFromDataBase.returnMetadataColumnsFormTable(
                         iMetadataConnection, db.getSourceName());
            	for (MetadataColumn column : columns) {
            		MetadataColumn column1 = ConnectionFactory.eINSTANCE.createMetadataColumn();
            		column1.setOriginalField(column.getOriginalField());
            		column1.setLabel("");
            		table.getColumns().add(column1);
				}
                metaFromEMF.add(table);
            }
        }
    }

    /**
     * fixed Column from EMF use Column From DataBase .
     * 
     * @param columnsFromDB MetadataColumn from Database
     * @param cloumnsFromEMF MetadataColumn from Emf
     */
    private void fixedColumns(List<MetadataColumn> columnsFromDB, List<MetadataColumn> cloumnsFromEMF) {
        for (MetadataColumn emf : cloumnsFromEMF) {
        	boolean flag = true;
			for (MetadataColumn db : columnsFromDB) {
				 
				if (db.getOriginalField().equals(emf.getOriginalField())) {
					flag = false;
					break;
				}
			}
			if (flag) {
				emf.setDivergency(true);
			}
		}
    	while (!columnsFromDB.isEmpty()) {
            boolean flag = true;
            MetadataColumn db = columnsFromDB.remove(0);
            for (MetadataColumn emf : cloumnsFromEMF) {
                if (db.getOriginalField().equals(emf.getOriginalField())) {
                    flag = false;
                    if (emf.getLabel().length() != 0) {
                        boolean is = !isEquivalent(db, emf);
                           emf.setDivergency(is);
                           emf.setSynchronised(is);
                     }
                }
            }
            if (flag) {
            	MetadataColumn column = ConnectionFactory.eINSTANCE.createMetadataColumn();
            	column.setOriginalField(db.getOriginalField());
            	column.setLabel("");
                cloumnsFromEMF.add(column);
            }
        }
        
    }

    /**
     * Check if Two MetadataColumns are the same..
     * 
     * @param info MetadataColumn
     * @param column MetadataColumn
     * @return isEquivalent.
     */
    private boolean isEquivalent(MetadataColumn info, MetadataColumn column) {

        if (info.getLength() != column.getLength()) {
            return false;
        }
        if (info.getDefaultValue() != null && !info.getDefaultValue().equals(column.getDefaultValue())) {
            return false;
        }
        if (column.getDefaultValue() != null && column.getDefaultValue().length() != 0
                && !column.getDefaultValue().equals(info.getDefaultValue())) {
            return false;
        }
        if (info.isNullable() != column.isNullable()) {
            return false;
        }
        if (info.isKey() != column.isKey()) {
            return false;
        }
        if (info.getPrecision() != column.getPrecision()) {
            return false;
        }
        if (info.getSourceType() != null && !info.getSourceType().equals(column.getSourceType())) {
            return false;
        }
        if (info.getComment() != null && info.getComment().length() != 0
                && !info.getComment().equals(column.getComment())) {
            return false;
        }
        if (column.getComment() != null && column.getComment().length() != 0
                && !column.getComment().equals(info.getComment())) {
            return false;
        }

        if (info.getTalendType() != null && !info.getTalendType().equals(column.getTalendType())) {
            return false;
        }
        if (column.getTalendType() != null && !column.getTalendType().equals(info.getTalendType())) {
            return false;
        }

        return true;
    }

    /**
     * DOC qianbing Comment method "getRepositoryType". Gets the type of the RepositoryNode.
     * 
     * @param repositoryNode RepositoryNode
     * @return RepositoryNodeType
     * @see RepositoryNodeType
     */
    public static RepositoryNodeType getRepositoryType(RepositoryNode repositoryNode) {
        return (RepositoryNodeType) repositoryNode.getProperties(EProperties.CONTENT_TYPE);
    }

    /**
     * be a RepositoryNode with database infomation.
     * 
     * @param repositoryNode RepositoryNode
     * @return RepositoryNode
     */
    public static RepositoryNode getRoot(RepositoryNode repositoryNode) {
        if (getRepositoryType(repositoryNode) == RepositoryNodeType.FOLDER) {
            throw new RuntimeException("RepositoryNode with folder info should not call this.");
        }

        if (getRepositoryType(repositoryNode) == RepositoryNodeType.DATABASE) {
            return repositoryNode;
        }
        return getRoot(repositoryNode.getParent());
    }
}
