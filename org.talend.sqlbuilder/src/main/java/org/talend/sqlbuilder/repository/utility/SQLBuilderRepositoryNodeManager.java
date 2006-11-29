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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.talend.commons.exception.PersistenceException;
import org.talend.commons.utils.VersionUtils;
import org.talend.core.CorePlugin;
import org.talend.core.context.Context;
import org.talend.core.context.RepositoryContext;
import org.talend.core.model.metadata.IMetadataConnection;
import org.talend.core.model.metadata.IMetadataTable;
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
import org.talend.repository.model.ProxyRepositoryFactory;
import org.talend.repository.model.RepositoryNode;
import org.talend.repository.model.RepositoryNode.ENodeType;
import org.talend.repository.model.RepositoryNode.EProperties;
import org.talend.repository.ui.utils.ManagerConnection;
import org.talend.sqlbuilder.SqlBuilderPlugin;
import org.talend.sqlbuilder.dbstructure.RepositoryNodeType;
import org.talend.sqlbuilder.dbstructure.SqlBuilderRepositoryObject;
import org.talend.sqlbuilder.dbstructure.DBTreeProvider.MetadataTableRepositoryObject;
import org.talend.sqlbuilder.util.ConnectionParameters;

/**
 * DOC dev class global comment. Detailled comment <br/>
 * 
 * $Id: talend-code-templates.xml 1 2006-09-29 17:06:40 +0000 (Fri, 29 Sep 2006)
 * nrousseau $
 * 
 */
public class SQLBuilderRepositoryNodeManager {

	// /store all DatabaseConnection's RepositoryNode.
	private static List<RepositoryNode> repositoryNodes = new ArrayList<RepositoryNode>();

	private static Map<String, String> labelsAndNames = new HashMap<String, String>();

	/**
	 * DOC dev Comment method "isChangeElementColor".
	 * 
	 * @param node
	 * @return
	 */
	public boolean isChangeElementColor(RepositoryNode node) {
		boolean flag = false;
		Object type = node.getProperties(EProperties.CONTENT_TYPE);
		if (type.equals(RepositoryNodeType.DATABASE)) {
			return getItem(node).getConnection().isDivergency();
		}

		if (type.equals(RepositoryNodeType.TABLE)) {
			MetadataTableRepositoryObject object = (MetadataTableRepositoryObject) node
					.getObject();
			MetadataTable table = object.getTable();
			flag = table.getSourceName().equals(table.getLabel());
			flag = flag && table.isDivergency();
		}
		return flag;
	}

	/**
	 * DOC dev Comment method "isDiff".
	 * 
	 * @param node
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public boolean[] isDiff(RepositoryNode node) {
		boolean isDiffDivergency = false;
		boolean isDiffSyschronize = false;
		boolean isDiffGray = false;
		Object type = node.getProperties(EProperties.CONTENT_TYPE);
		if (type.equals(RepositoryNodeType.DATABASE)) {
			DatabaseConnection connection = (DatabaseConnection) getItem(node)
					.getConnection();
			List<MetadataTable> tables = connection.getTables();
			for (MetadataTable table : tables) {
				List<MetadataColumn> columns = table.getColumns();
				for (MetadataColumn column : columns) {
					if (column.isDivergency()) {
						isDiffDivergency = true;
					}
					if (column.isSynchronised()) {
						isDiffSyschronize = true;
					}
					if (column.getLabel() == null
							|| "".equals(column.getLabel())) {
						isDiffGray = true;
					}
				}
				if (table.isDivergency()) {
					isDiffDivergency = true;
				}
				if (table.getLabel() == null || "".equals(table.getLabel())) {
					isDiffGray = true;
				}
			}
		} else if (type.equals(RepositoryNodeType.TABLE)) {
			MetadataTableRepositoryObject object = (MetadataTableRepositoryObject) node
					.getObject();
			MetadataTable table = object.getTable();
			List<MetadataColumn> columns = table.getColumns();
			for (MetadataColumn column : columns) {
				if (column.isDivergency()) {
					isDiffDivergency = true;
				}
				if (column.isSynchronised()) {
					isDiffSyschronize = true;
				}
				if (column.getLabel() == null || "".equals(column.getLabel())) {
					isDiffGray = true;
				}
			}
		}

		return new boolean[] { isDiffGray, isDiffDivergency, isDiffSyschronize };
	}

	/**
	 * DOC dev Comment method "removeAllRepositoryNodes".
	 */
	public void removeAllRepositoryNodes() {
		repositoryNodes.clear();
	}

	/**
	 * DOC dev Comment method "addRepositoryNode".
	 * 
	 * @param node
	 */
	public void addRepositoryNode(RepositoryNode node) {
		if (repositoryNodes.contains(node)) {
			return;
		}
		repositoryNodes.add(node);
	}

	/**
	 * DOC dev Comment method "getRepositoryNodebyName".
	 * 
	 * @param name
	 * @return
	 */
	public RepositoryNode getRepositoryNodebyName(String name) {
		for (RepositoryNode node : repositoryNodes) {
			if (((SqlBuilderRepositoryObject) node.getObject())
					.getRepositoryName().equals(name)) {
				return node;
			}
		}
		return null;
	}

	/**
	 * DOC dev Comment method "removeRepositoryNodeExceptNodeByName".
	 * 
	 * @param repositoryNodeName
	 */
	public void removeRepositoryNodeExceptNodeByName(String repositoryNodeName) {
		for (Iterator it = repositoryNodes.iterator(); it.hasNext();) {
			RepositoryNode node = (RepositoryNode) it.next();
			if (!((SqlBuilderRepositoryObject) node.getObject())
					.getRepositoryName().equals(repositoryNodeName)) {
				it.remove();
			}
		}
	}

	/**
	 * DOC dev Comment method "reductionALLRepositoryNode".
	 */
	@SuppressWarnings("unchecked")
	public static void reductionALLRepositoryNode() {
		for (RepositoryNode node : repositoryNodes) {
			DatabaseConnection connection = (DatabaseConnection) getItem(node)
					.getConnection();
			List<MetadataTable> tables = connection.getTables();
			List<MetadataTable> newtables = new ArrayList<MetadataTable>();

			for (MetadataTable table : tables) {
				List<MetadataColumn> columns = table.getColumns();
				List<MetadataColumn> newcloumns = new ArrayList<MetadataColumn>();
				for (MetadataColumn column : columns) {
					if (!column.getLabel().equals("")) {
						if (column.getOriginalField().equals(" ")) {
							column.setOriginalField(labelsAndNames
									.get("Columns: " + column.getLabel()));
						}
						column.setDivergency(false);
						column.setSynchronised(false);
						newcloumns.add(column);
					}
				}
				table.getColumns().clear();
				table.getColumns().addAll(newcloumns);
				if (!table.getLabel().equals("")) {
					if (table.getSourceName().equals(" ")) {
						table.setSourceName(labelsAndNames.get("Tables: "
								+ table.getLabel()));
					}
					table.setDivergency(false);
					table.setSynchronised(false);
					newtables.add(table);
				}
			}
			connection.getTables().clear();
			connection.getTables().addAll(newtables);
			connection.setDivergency(false);
			connection.setSynchronised(false);
		}
		repositoryNodes.clear();
		labelsAndNames.clear();
	}

	/**
	 * method "getTableNamesByRepositoryNode" get All Table Names in current
	 * RepositoryNode's DatabaseConnectionItem.
	 * 
	 * @param node
	 *            current RepositoryNode
	 * @return List :all Table Names.
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, List<String>> getAllNamesByRepositoryNode(
			RepositoryNode node) {
		Map<String, List<String>> allNames = new HashMap<String, List<String>>();
		DatabaseConnectionItem item = getItem(getRoot(node));
		DatabaseConnection connection = (DatabaseConnection) item
				.getConnection();
		List<MetadataTable> tablesFromEMF = connection.getTables();
		boolean isOdbc = connection.getSID() == null
				|| connection.getSID().length() == 0;
		String sid = isOdbc ? connection.getDatasourceName() : connection
				.getSID();
		for (MetadataTable table : tablesFromEMF) {
			String tableName = table.getSourceName();
			if (tableName != null && !"".equals(tableName)) {
				List<String> columnNames = new ArrayList<String>();
				tableName = "\"" + sid + "\".\"" + tableName + "\"";
				List<MetadataColumn> columns = table.getColumns();
				for (MetadataColumn column : columns) {
					String columnName = column.getOriginalField();
					if (columnName != null && !"".equals(columnName)) {
						columnName = tableName + ".\"" + columnName + "\"";
						columnNames.add(columnName);
					}
				}

				allNames.put(tableName, columnNames);
			}
		}
		return allNames;
	}

	/**
	 * method "getALLReposotoryNodeNames" get all DatabaseConnection's
	 * RepositoryNode Names.
	 * 
	 * @return List : all DatabaseConnection's RepositoryNode Names
	 */
	public List<String> getALLReposotoryNodeNames() {
		List<String> names = new ArrayList<String>();
		for (RepositoryNode node : repositoryNodes) {
			names.add(((SqlBuilderRepositoryObject) node.getObject())
					.getRepositoryName());
		}
		return names;
	}

	/**
	 * method "getRepositoryNodeFromDB".
	 * 
	 * @param oldNode
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public RepositoryNode getRepositoryNodeFromDB(RepositoryNode oldNode) {

		@SuppressWarnings("unused")
		RepositoryNode newNode = cloneRepositoryNode(oldNode);
		
		DatabaseConnectionItem item = getItem(oldNode);
		DatabaseConnection connection = (DatabaseConnection) item
				.getConnection();
		IMetadataConnection iMetadataConnection = ConvertionHelper
				.convert(connection);
		
		 modifyOldRepositoryNode(connection, iMetadataConnection);
		return oldNode;
	}

	private RepositoryNode cloneRepositoryNode(RepositoryNode oldNode) {
		
		DatabaseConnectionItem oldItem = getItem(oldNode);
		
		DatabaseConnection oldConnection = (DatabaseConnection) oldItem.getConnection();
		DatabaseConnection connection =	cloneNewConnection(oldConnection);
		
		DatabaseConnectionItem item = PropertiesFactory.eINSTANCE
				.createDatabaseConnectionItem();
		
		Property oldPerProperty = oldItem.getProperty();
		Property connectionProperty = createNewProperty(oldPerProperty);
		item.setProperty(connectionProperty);
		item.setConnection(connection);
		
		ItemState oldState = oldItem.getState();
		
		ItemState state = cloneNewItemState(oldState);
		item.setState(state);
		
		RepositoryObject object = new RepositoryObject(connectionProperty);
		
		RepositoryNode newNode = new RepositoryNode(object, oldNode.getParent(),
				ENodeType.SYSTEM_FOLDER);
		
		return newNode;
	}

	/**
	 * DOC dev Comment method "cloneNewItemState".
	 * @param oldState
	 * @return
	 */
	private ItemState cloneNewItemState(ItemState oldState) {
		ItemState state = PropertiesFactory.eINSTANCE.createItemState();
		state.setCommitDate(oldState.getCommitDate());
		state.setDeleted(oldState.isDeleted());
		state.setLockDate(oldState.getLockDate());
		state.setLocked(oldState.isLocked());
		state.setLocker(oldState.getLocker());
		state.setPath(oldState.getPath());
		
		return state;
	}

	/**
	 * DOC dev Comment method "createNewProperty".
	 * @param oldPerProperty
	 * @return
	 */
	private Property createNewProperty(Property oldPerProperty) {
		Property connectionProperty = PropertiesFactory.eINSTANCE
				.createProperty();
		connectionProperty.setAuthor(oldPerProperty.getAuthor());
		connectionProperty.setCreationDate(oldPerProperty.getCreationDate());
		connectionProperty.setDescription(oldPerProperty.getDescription());
		connectionProperty.setId(oldPerProperty.getId());
		connectionProperty.setLabel(oldPerProperty.getLabel());
		connectionProperty.setModificationDate(oldPerProperty.getModificationDate());
		connectionProperty.setPurpose(oldPerProperty.getPurpose());
		connectionProperty.setStatusCode(oldPerProperty.getStatusCode());
		connectionProperty.setVersion(oldPerProperty.getVersion());
		
		return connectionProperty;
	}

	/**
	 * DOC dev Comment method "cloneNewConnection".
	 * @param connection
	 * @param oldConnection
	 */
	@SuppressWarnings("unchecked")
	private DatabaseConnection cloneNewConnection(DatabaseConnection oldConnection) {
		DatabaseConnection connection = ConnectionFactory.eINSTANCE
		.createDatabaseConnection();
		
		connection.setComment(oldConnection.getComment());
		connection.setDatabaseType(oldConnection.getDatabaseType());
		connection.setDatasourceName(oldConnection.getDatasourceName());
		connection.setDriverClass(oldConnection.getDriverClass());
		connection.setFileFieldName(oldConnection.getFileFieldName());
		connection.setId(oldConnection.getId());
		
		connection.setLabel(oldConnection.getLabel());
		connection.setNullChar(oldConnection.getNullChar());
		connection.setPassword(oldConnection.getPassword());
		connection.setPort(oldConnection.getPort());
		connection.setReadOnly(oldConnection.isReadOnly());
		connection.setSchema(oldConnection.getSchema());
		
		connection.setServerName(oldConnection.getServerName());
		connection.setSID(oldConnection.getSID());
		connection.setSqlSynthax(oldConnection.getSqlSynthax());
		connection.setStringQuote(oldConnection.getStringQuote());
		connection.setSynchronised(oldConnection.isSynchronised());
		connection.setDivergency(oldConnection.isDivergency());
		
		connection.setURL(oldConnection.getURL());
		connection.setUsername(oldConnection.getUsername());
		connection.setVersion(oldConnection.getVersion());
		
		HashMap properties = oldConnection.getProperties();
		HashMap news = new HashMap();
		Set oldSet = properties.keySet();
		for (Object object : oldSet) {
			news.put(object, properties.get(object));
		}
		connection.getProperties().putAll(news);
		
		return connection;
	}

	/**
	 * DOC dev Comment method "modifyOldRepositoryNode".
	 * 
	 * @param connection
	 * @param iMetadataConnection
	 */
	@SuppressWarnings({ "unchecked" })
	private void modifyOldRepositoryNode(DatabaseConnection connection,
			IMetadataConnection iMetadataConnection) {
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
				List<MetadataColumn> columnsFromDB = getColumnsFromDB(
						iMetadataConnection, tableFromDB);
				for (MetadataTable tableFromEMF : tablesFromEMF) {
					// /Get MetadataColumn From EMF
					List<MetadataColumn> columnsFromEMF = tableFromEMF
							.getColumns();
					if (tableFromDB.getSourceName().equals(
							tableFromEMF.getSourceName())) {
						fixedColumns(columnsFromDB, columnsFromEMF);
					}
				}
			}
			fixedTables(tablesFromDB, tablesFromEMF, iMetadataConnection);
		} else {
			List<MetadataTable> tablesFromEMF = connection.getTables();
			for (MetadataTable tableFromEMF : tablesFromEMF) {
				List<MetadataColumn> columnsFromEMF = tableFromEMF.getColumns();
				for (MetadataColumn column : columnsFromEMF) {
					labelsAndNames.put("Columns: " + column.getLabel(), column
							.getOriginalField());
					column.setOriginalField(" ");
					column.setDivergency(true);
					column.setSynchronised(false);
				}
				labelsAndNames.put("Tables: " + tableFromEMF.getLabel(),
						tableFromEMF.getSourceName());
				tableFromEMF.setSourceName(" ");
				tableFromEMF.setDivergency(true);
			}
		}
	}

	/**
	 * DOC dev Comment method "getRepositoryNodeByBuildIn".
	 * 
	 * @param node
	 * @param parameters
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public RepositoryNode getRepositoryNodeByBuildIn(RepositoryNode node,
			ConnectionParameters parameters) {
		DatabaseConnection connection = createConnection(parameters);
		IMetadataConnection iMetadataConnection = ConvertionHelper
				.convert(connection);

		RepositoryNode newNode = createNewRepositoryNode(node, parameters,
				connection, iMetadataConnection);

		return newNode;
	}

	/**
	 * DOC dev Comment method "createNewRepositoryNode".
	 * 
	 * @param node
	 * @param parameters
	 * @param connection
	 * @param iMetadataConnection
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private RepositoryNode createNewRepositoryNode(RepositoryNode node,
			ConnectionParameters parameters, DatabaseConnection connection,
			IMetadataConnection iMetadataConnection) {
		ManagerConnection managerConnection = new ManagerConnection();
		boolean status = managerConnection.check(iMetadataConnection);
		connection.setDivergency(!status);
		connection.getTables().clear();
		if (status) {
			List<MetadataTable> tablesFromDB = getTablesFromDB(iMetadataConnection);
			for (MetadataTable table : tablesFromDB) {
				List<MetadataColumn> columnsFromDB = getColumnsFromDB(
						iMetadataConnection, table);
				table.getColumns().clear();
				for (MetadataColumn column : columnsFromDB) {
					column.setLabel("");
					table.getColumns().add(column);
				}
				table.setLabel("");
				connection.getTables().add(table);
			}
		} else {
			if (parameters != null) {
				parameters.setConnectionComment(managerConnection
						.getMessageException());
			}
		}
		DatabaseConnectionItem item = PropertiesFactory.eINSTANCE
				.createDatabaseConnectionItem();
		Property connectionProperty = PropertiesFactory.eINSTANCE
				.createProperty();
		connectionProperty.setAuthor(((RepositoryContext) CorePlugin
				.getContext().getProperty(Context.REPOSITORY_CONTEXT_KEY))
				.getUser());
		connectionProperty.setVersion(VersionUtils.DEFAULT_VERSION);
		connectionProperty.setStatusCode("");

		item.setProperty(connectionProperty);
		item.setConnection(connection);
		RepositoryObject object = new RepositoryObject(connectionProperty);
		object.setLabel("");
		ItemState state = PropertiesFactory.eINSTANCE.createItemState();
		state.setDeleted(false);
		item.setState(state);
		// node.getObject().getProperty().setItem(item);
		if (node == null) {
			node = new RepositoryNode(null, null, ENodeType.SYSTEM_FOLDER);
		}
		RepositoryNode newNode = new RepositoryNode(object, node,
				ENodeType.SYSTEM_FOLDER);
		return newNode;
	}

	/**
	 * DOC dev Comment method "getColumnsFromDB".
	 * 
	 * @param iMetadataConnection
	 * @param table
	 * @return
	 */
	private List<MetadataColumn> getColumnsFromDB(
			IMetadataConnection iMetadataConnection, MetadataTable table) {
		List<MetadataColumn> metadataColumns = new ArrayList<MetadataColumn>();

		try {
			DatabaseMetaData dbMetaData = getDatabaseMetaData(iMetadataConnection);

			IMetadataTable metaTable1 = new org.talend.core.model.metadata.MetadataTable();
			metaTable1.setLabel(table.getLabel());
			metaTable1.setTableName(table.getSourceName());
			metadataColumns = ExtractMetaDataFromDataBase
					.extractMetadataColumnsFormTable(dbMetaData, metaTable1,
							iMetadataConnection.getDbType());
			ExtractMetaDataUtils.closeConnection();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		return metadataColumns;
	}

	/**
	 * DOC dev Comment method "createConnection".
	 * 
	 * @param parameters
	 *            inputed when use Built-In .
	 * @return DatabaseConnection : connetion .
	 */
	public DatabaseConnection createConnection(ConnectionParameters parameters) {
		DatabaseConnection connection = ConnectionFactory.eINSTANCE
				.createDatabaseConnection();
		connection.setDatabaseType(parameters.getDbType());
		connection.setUsername(parameters.getUserName());
		connection.setPort(parameters.getPort());
		connection.setPassword(parameters.getPassword());
		connection.setSID(parameters.getDbName());
		connection.setLabel(parameters.getDbName());
		connection.setDatasourceName(parameters.getDatasource());
		if (parameters.getSchema() == null
				&& parameters.getDbType().equals("PostgreSQL")) {
			connection.setSchema("public");
		}
		// connection.setComment("");
		connection.setURL(parameters.getURL());
		connection.setDriverClass(ExtractMetaDataUtils
				.getDriverClassByDbType(parameters.getDbType()));
		return connection;
	}

	/**
	 * DOC dev Comment method "getDbTypeFromRepositoryNode".
	 * 
	 * @param node
	 * @return
	 */
	public static String getDbTypeFromRepositoryNode(RepositoryNode node) {
		DatabaseConnection connection = (DatabaseConnection) getItem(
				getRoot(node)).getConnection();
		return connection.getDatabaseType();
	}

	/**
	 * method "getItem" get DatabaseConnectionItem by current RepositoryNode .
	 * 
	 * @param newNode
	 *            current RepositoryNode
	 * @return DatabaseConnectionItem : item current node.
	 */
	public static DatabaseConnectionItem getItem(RepositoryNode newNode) {
		IRepositoryObject repositoryObject = newNode.getObject();
		DatabaseConnectionItem item = (DatabaseConnectionItem) repositoryObject
				.getProperty().getItem();
		return item;
	}

	/**
	 * method "getDatabaseNameByRepositoryNode" get databaseName .
	 * 
	 * @param node
	 *            current RepositoryNode
	 * @return String :databaseName
	 */
	public static String getDatabaseNameByRepositoryNode(RepositoryNode node) {
		DatabaseConnection connection = (DatabaseConnection) getItem(node)
				.getConnection();
		boolean isOdbc = connection.getSID() == null
				|| connection.getSID().length() == 0;
		return isOdbc ? connection.getDatasourceName() : connection.getSID();
	}

	/**
	 * method "getTablesFromDB" get all tables from DataBase.
	 * 
	 * @param iMetadataConnection
	 *            contains connection
	 * @return all Tables from Database.
	 */
	private List<MetadataTable> getTablesFromDB(
			IMetadataConnection iMetadataConnection) {

		DatabaseMetaData dbMetaData = getDatabaseMetaData(iMetadataConnection);

		List<MetadataTable> metadataTables = new ArrayList<MetadataTable>();
		try {
			String[] tableTypes = { "TABLE", "VIEW" };
			ResultSet rsTables = null;
			rsTables = dbMetaData.getTables(null, ExtractMetaDataUtils.schema,
					null, tableTypes);
			while (rsTables.next()) {
				MetadataTable medataTable = ConnectionFactory.eINSTANCE
						.createMetadataTable();
				medataTable.setId(metadataTables.size() + 1 + "");
				medataTable.setLabel(rsTables.getString("TABLE_NAME"));
				medataTable.setSourceName(medataTable.getLabel());
				medataTable.setComment(ExtractMetaDataUtils
						.getStringMetaDataInfo(rsTables, "REMARKS"));
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

	/**
	 * method "getDatabaseMetaData" get databaseMetaData.
	 * 
	 * @param iMetadataConnection
	 *            contains connection
	 * @return dbMetaData DatabaseMetaData .
	 */
	private DatabaseMetaData getDatabaseMetaData(
			IMetadataConnection iMetadataConnection) {
		ExtractMetaDataUtils.getConnection(iMetadataConnection.getDbType(),
				iMetadataConnection.getUrl(),
				iMetadataConnection.getUsername(), iMetadataConnection
						.getPassword(), iMetadataConnection.getDatabase(),
				iMetadataConnection.getSchema());
		DatabaseMetaData dbMetaData = ExtractMetaDataUtils
				.getDatabaseMetaData(ExtractMetaDataUtils.conn);
		return dbMetaData;
	}

	/**
	 * DOC dev Comment method "getALLQueryLabels".
	 * 
	 * @param repositoryNode
	 *            current RepositoryNode.
	 * @return all QueryLabels in Emf.
	 */
	@SuppressWarnings("unchecked")
	public List<String> getALLQueryLabels(RepositoryNode repositoryNode) {
		List<String> allQueries = new ArrayList<String>();
		DatabaseConnectionItem item = getItem(repositoryNode);
		DatabaseConnection connection = (DatabaseConnection) item
				.getConnection();
		List<QueriesConnection> qcs = connection.getQueries();
		if (!qcs.isEmpty()) {
			QueriesConnection connection2 = qcs.get(0);
			List<Query> qs = connection2.getQuery();
			for (Query q1 : qs) {
				allQueries.add(q1.getLabel());
			}
		}
		return allQueries;
	}

	/**
	 * method "saveQuery" use save inputed Query to EMF's xml File.
	 * 
	 * @param repositoryNode
	 *            current RepositoryNode
	 * @param query
	 *            need to save Query
	 */
	@SuppressWarnings("unchecked")
	public void saveQuery(RepositoryNode repositoryNode, Query query) {
		DatabaseConnectionItem item = getItem(repositoryNode);
		if (query != null) {
			DatabaseConnection connection = (DatabaseConnection) item
					.getConnection();
			List<QueriesConnection> list = connection.getQueries();
			if (list.isEmpty()) {
				QueriesConnection qc = ConnectionFactory.eINSTANCE
						.createQueriesConnection();
				qc.setConnection(connection);
				qc.getQuery().add(query);
				connection.getQueries().add(qc);
			} else {
				QueriesConnection connection2 = list.get(0);
				List<String> queryNames = getALLQueryLabels(repositoryNode);
				if (!queryNames.contains(query.getLabel())) {
					connection2.getQuery().add(query);
				}
			}
		}
		saveMetaData(item);
	}

	/**
	 * method "deleteQueries" use delete Queries.
	 * 
	 * @param repositoryNode
	 *            databaseConnection's RepositoryNode
	 * @param queries
	 *            need to deleted Queries
	 */
	@SuppressWarnings("unchecked")
	public void deleteQueries(RepositoryNode repositoryNode, List<Query> queries) {
		DatabaseConnectionItem item = getItem(repositoryNode);
		DatabaseConnection connection = (DatabaseConnection) item
				.getConnection();
		List<QueriesConnection> list = connection.getQueries();
		if (!list.isEmpty()) {
			QueriesConnection connection2 = list.get(0);
			List<Query> qs = connection2.getQuery();
			List<Query> qs2 = new ArrayList<Query>();
			qs2.clear();
			for (Query query : qs) {
				boolean flag = true;
				for (Query q : queries) {
					if (query.getLabel().equals(q.getLabel())) {
						flag = false;
					}
				}
				if (flag) {
					qs2.add(query);
				}
			}
			connection2.getQuery().clear();
			connection2.getQuery().addAll(qs2);
		}
		saveMetaData(item);
	}

	/**
	 * save MetaData into EMF's xml files.
	 * 
	 * @param item
	 *            need to be saved Item
	 */
	public void saveMetaData(DatabaseConnectionItem item) {

        ProxyRepositoryFactory factory = ProxyRepositoryFactory.getInstance();
		try {
			factory.save(item);
		} catch (PersistenceException e) {
			SqlBuilderPlugin.log("Save MetaData Failure", e);
		}
	}

	/**
	 * fixed Table .
	 * 
	 * @param metaFromDB
	 *            MetadataTable from Database
	 * @param metaFromEMF
	 *            MetadataTable from Emf
	 * @param iMetadataConnection
	 *            contain Connection.
	 */
	@SuppressWarnings("unchecked")
	private void fixedTables(List<MetadataTable> metaFromDB,
			List<MetadataTable> metaFromEMF,
			IMetadataConnection iMetadataConnection) {

		for (MetadataTable emf : metaFromEMF) {
			boolean flag = true;
			for (MetadataTable db : metaFromDB) {
				if (db.getSourceName().equals(emf.getSourceName())) {
					flag = false;
					break;
				}
			}
			if (flag) {
				List<MetadataColumn> columns = emf.getColumns();
				for (MetadataColumn column : columns) {
					column.setOriginalField("");
					column.setDivergency(true);
				}
				emf.setSourceName("");
				emf.setDivergency(true);
				// emf.getConnection().setDivergency(true);
			}
		}
		while (!metaFromDB.isEmpty()) {
			boolean flag = true;
			MetadataTable db = metaFromDB.remove(0);
			for (MetadataTable emf : metaFromEMF) {
				if (db.getSourceName().equals(emf.getSourceName())) {
					flag = false;
					if (!emf.getLabel().equals("")
							&& !emf.getLabel().equals(
									db.getSourceName().replaceAll("_", "-"))) {
						emf.setDivergency(true);
						// emf.getConnection().setDivergency(true);
					}
				}
			}
			if (flag) {
				MetadataTable table = ConnectionFactory.eINSTANCE
						.createMetadataTable();
				table.setSourceName(db.getSourceName());
				table.setLabel("");
				List<MetadataColumn> columns = getColumnsFromDB(
						iMetadataConnection, db);
				for (MetadataColumn column : columns) {
					MetadataColumn column1 = ConnectionFactory.eINSTANCE
							.createMetadataColumn();
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
	 * @param columnsFromDB
	 *            MetadataColumn from Database
	 * @param cloumnsFromEMF
	 *            MetadataColumn from Emf
	 */
	private void fixedColumns(List<MetadataColumn> columnsFromDB,
			List<MetadataColumn> cloumnsFromEMF) {
		for (MetadataColumn emf : cloumnsFromEMF) {
			boolean flag = true;
			for (MetadataColumn db : columnsFromDB) {

				if (db.getOriginalField().equals(emf.getOriginalField())) {
					flag = false;
					break;
				}
			}
			if (flag) {
				emf.setOriginalField("");
				emf.setDivergency(true);
				// emf.getTable().setDivergency(true);
				// emf.getTable().getConnection().setDivergency(true);
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
						// if (is) {
						// emf.getTable().setSynchronised(true);
						// emf.getTable().getConnection().setSynchronised(true);
						// emf.getTable().setDivergency(true);
						// emf.getTable().getConnection().setDivergency(true);
						// }
					}
				}
			}
			if (flag) {
				MetadataColumn column = ConnectionFactory.eINSTANCE
						.createMetadataColumn();
				column.setOriginalField(db.getOriginalField());
				column.setLabel("");
				cloumnsFromEMF.add(column);
			}
		}

	}

	/**
	 * Check if Two MetadataColumns are the same..
	 * 
	 * @param info
	 *            MetadataColumn
	 * @param column
	 *            MetadataColumn
	 * @return isEquivalent.
	 */
	private boolean isEquivalent(MetadataColumn info, MetadataColumn column) {

		if (info.getLength() != column.getLength()) {
			return false;
		}
		if (info.getDefaultValue() != null
				&& !info.getDefaultValue().equals(column.getDefaultValue())) {
			return false;
		}
		if (column.getDefaultValue() != null
				&& column.getDefaultValue().length() != 0
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
		if (info.getSourceType() != null
				&& !info.getSourceType().equals(column.getSourceType())) {
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

		if (info.getTalendType() != null
				&& !info.getTalendType().equals(column.getTalendType())) {
			return false;
		}
		if (column.getTalendType() != null
				&& !column.getTalendType().equals(info.getTalendType())) {
			return false;
		}

		return true;
	}

	/**
	 * DOC qianbing Comment method "getRepositoryType". Gets the type of the
	 * RepositoryNode.
	 * 
	 * @param repositoryNode
	 *            RepositoryNode
	 * @return RepositoryNodeType
	 * @see RepositoryNodeType
	 */
	public static RepositoryNodeType getRepositoryType(
			RepositoryNode repositoryNode) {
		return (RepositoryNodeType) repositoryNode
				.getProperties(EProperties.CONTENT_TYPE);
	}

	/**
	 * be a RepositoryNode with database infomation.
	 * 
	 * @param repositoryNode
	 *            RepositoryNode
	 * @return RepositoryNode
	 */
	public static RepositoryNode getRoot(RepositoryNode repositoryNode) {
		if (getRepositoryType(repositoryNode) == RepositoryNodeType.FOLDER) {
			throw new RuntimeException(
					"RepositoryNode with folder info should not call this.");
		}

		if (getRepositoryType(repositoryNode) == RepositoryNodeType.DATABASE) {
			return repositoryNode;
		}
		return getRoot(repositoryNode.getParent());
	}
}
