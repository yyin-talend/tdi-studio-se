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
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


import org.eclipse.emf.common.util.EList;
import org.talend.commons.exception.PersistenceException;
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
import org.talend.core.model.repository.IRepositoryObject;
import org.talend.repository.model.IRepositoryFactory;
import org.talend.repository.model.RepositoryFactoryProvider;
import org.talend.repository.model.RepositoryNode;
import org.talend.repository.model.RepositoryNode.EProperties;
import org.talend.repository.ui.utils.ManagerConnection;
import org.talend.sqlbuilder.SqlBuilderPlugin;
import org.talend.sqlbuilder.dbstructure.RepositoryNodeType;
import org.talend.sqlbuilder.dbstructure.SessionTreeNodeManager;

/**
 * DOC dev  class global comment. Detailled comment
 * <br/>
 *
 * $Id: talend-code-templates.xml 1 2006-09-29 17:06:40 +0000 (Fri, 29 Sep 2006) nrousseau $
 *
 */
public class SQLBuilderRepositoryNodeManager {

	
	
	/**
	 *  Get RepositoryNodeFromDB .
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
        	///Get TableNames From DB
//        	List<String> tableNamesFromDB = ExtractMetaDataFromDataBase.returnTablesFormConnection(iMetadataConnection);
        	
        	///Get MetadataTable From DB
        	List<MetadataTable> tablesFromDB = getTablesFromDB(iMetadataConnection);
        	//Get MetadataTable From EMF(Old RepositoryNode)
        	List<MetadataTable> tablesFromEMF = connection.getTables();
        	
        	for (MetadataTable tableFromDB : tablesFromDB) {
        		///Get MetadataColumn from DB
        		List<MetadataColumn> columnsFromDB  = 
        			ExtractMetaDataFromDataBase.returnMetadataColumnsFormTable(iMetadataConnection, tableFromDB.getLabel());
        		for (MetadataTable tableFromEMF : tablesFromEMF) {
        			///Get MetadataColumn From EMF
        			List<MetadataColumn> columnsFromEMF = tableFromEMF.getColumns();
        			fixedColumns(columnsFromDB, columnsFromEMF);
        			tableFromEMF.getColumns().clear();
        			tableFromEMF.getColumns().addAll(columnsFromDB);
				}
        		
			}
        	tablesFromDB = fixedTables(tablesFromDB, tablesFromEMF);
        	connection.getTables().clear();
        	connection.getTables().addAll(tablesFromDB);
        } 
		return oldNode;
	}

	/**
	 * DOC dev Comment method "getItem".
	 * @param newNode
	 * @return
	 */
	private DatabaseConnectionItem getItem(RepositoryNode newNode) {
		IRepositoryObject repositoryObject = newNode.getObject();
        DatabaseConnectionItem item = (DatabaseConnectionItem) repositoryObject.getProperty().getItem();
		return item;
	}
	
	/**
	 * Get Tables From DataBase.
	 * @param iMetadataConnection contain Connection's information
	 * @return MetadataTable List from Database
	 */
	private List<MetadataTable> getTablesFromDB(IMetadataConnection iMetadataConnection) {

        ExtractMetaDataUtils.getConnection(iMetadataConnection.getDbType(), iMetadataConnection.getUrl(), iMetadataConnection
                .getUsername(), iMetadataConnection.getPassword(), iMetadataConnection.getDatabase(), iMetadataConnection
                .getSchema());
        DatabaseMetaData dbMetaData = ExtractMetaDataUtils.getDatabaseMetaData(ExtractMetaDataUtils.conn);
      
        List<MetadataTable> metadataTables = new ArrayList<MetadataTable>();
        try {
            String[] tableTypes = { "TABLE", "VIEW"};
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
	public List<String>  getALLQueryLabels(RepositoryNode repositoryNode) {
		List<String> allQueries = new ArrayList<String>();
		if (repositoryNode == null) {
			return null;
		}
		DatabaseConnectionItem item = getItem(repositoryNode);
		DatabaseConnection connection = (DatabaseConnection) item.getConnection();
		List<QueriesConnection> qcs = connection.getQueries();
		for (QueriesConnection connection2 : qcs) {
			List<Query> qs = connection2.getQuery();
			for (Query q1 : qs) {
				allQueries.add(q1.getLabel());
			}
		}
		return null;
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
	 * @param metaFromDB MetadataTable from Database
	 * @param metaFromEMF MetadataTable from Emf
	 * @return MetadataTable List has set divergency flag
	 */
	private List<MetadataTable> fixedTables(List<MetadataTable> metaFromDB, 
			List<MetadataTable> metaFromEMF) {
		List<MetadataTable> newMetaFromDB = new ArrayList<MetadataTable>();
		for (MetadataTable db : metaFromDB) {
			boolean flag = true;
				for (MetadataTable emf : metaFromEMF) {
					if (emf.getSourceName().equals(db.getSourceName())) {
						if (emf.getLabel().equals(db.getSourceName())) {
							emf.setDivergency(false);
						} else {
							emf.setDivergency(true);
						}
						newMetaFromDB.add(emf);
						flag = false;
						break;
					} 
				}
				
				if (flag) {
					newMetaFromDB.add(db);
				}
		}
		return newMetaFromDB;
	}
	
	/**
	 * fixed Column from EMF use Column From DataBase  .
	 * @param columnsFromDB MetadataColumn from Database
	 * @param cloumnsFromEMF MetadataColumn from Emf
	 */
	private void fixedColumns(List<MetadataColumn> columnsFromDB, 
			List<MetadataColumn> cloumnsFromEMF) {
		while (!columnsFromDB.isEmpty()) {
			MetadataColumn db = columnsFromDB.remove(0);
			for (MetadataColumn emf : cloumnsFromEMF) {
				if (db.getOriginalField().equals(emf.getOriginalField())) {
					emf.setDivergency(!isEquivalent(db, emf));
					emf.setSourceType(db.getSourceType());
				} 
			}
			cloumnsFromEMF.add(db);
		}
	}
	
	 /**
     * Check if Two MetadataColumns are the same..
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
    private RepositoryNodeType getRepositoryType(RepositoryNode repositoryNode) {
        return (RepositoryNodeType) repositoryNode.getProperties(EProperties.CONTENT_TYPE);
    }
    
    /**
     * be a RepositoryNode with database infomation.
     * 
     * @param repositoryNode RepositoryNode
     * @return RepositoryNode
     */
    public RepositoryNode getRoot(RepositoryNode repositoryNode) {
        if (getRepositoryType(repositoryNode) == RepositoryNodeType.FOLDER) {
            throw new RuntimeException("RepositoryNode with folder info should not call this.");
        }

        if (getRepositoryType(repositoryNode) == RepositoryNodeType.DATABASE) {
            return repositoryNode;
        }
        return getRoot(repositoryNode.getParent());
    }
}
