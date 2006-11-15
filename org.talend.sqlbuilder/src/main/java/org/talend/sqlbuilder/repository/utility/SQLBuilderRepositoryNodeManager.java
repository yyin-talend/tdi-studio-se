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


import org.talend.commons.exception.PersistenceException;
import org.talend.core.model.metadata.IMetadataConnection;
import org.talend.core.model.metadata.builder.ConvertionHelper;
import org.talend.core.model.metadata.builder.connection.ConnectionFactory;
import org.talend.core.model.metadata.builder.connection.DatabaseConnection;
import org.talend.core.model.metadata.builder.connection.MetadataColumn;
import org.talend.core.model.metadata.builder.connection.MetadataTable;
import org.talend.core.model.metadata.builder.database.ExtractMetaDataFromDataBase;
import org.talend.core.model.metadata.builder.database.ExtractMetaDataUtils;

import org.talend.core.model.properties.DatabaseConnectionItem;
import org.talend.core.model.repository.IRepositoryObject;
import org.talend.repository.model.IRepositoryFactory;
import org.talend.repository.model.RepositoryFactoryProvider;
import org.talend.repository.model.RepositoryNode;
import org.talend.repository.ui.utils.ManagerConnection;

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
		
		RepositoryNode newNode = null;
		IRepositoryObject repositoryObject = oldNode.getObject();
        DatabaseConnectionItem item = (DatabaseConnectionItem) repositoryObject.getProperty().getItem();
        DatabaseConnection connection = (DatabaseConnection) item.getConnection();
//        connection.getQueries().add();
        IMetadataConnection iMetadataConnection = ConvertionHelper.convert(connection);
        boolean status = new ManagerConnection().check(iMetadataConnection);
        if (status) {
        	///Get MetadataTable From DB
        	List<MetadataTable> tablesFromDB = getTablesFromDB(iMetadataConnection);
        	//Get MetadataTable From EMF(Old RepositoryNode)
        	List<MetadataTable> tablesFromEMF = connection.getTables();
        	
        	for (MetadataTable tableFromDB : tablesFromDB) {
        		///Get MetadataColumn from DB
        		List<MetadataColumn> columnsFromDB  = 
        			ExtractMetaDataFromDataBase.returnMetadataColumnsFormTable(iMetadataConnection, tableFromDB.getLabel());
        		for (MetadataTable tableFromEMF : tablesFromEMF) {
        			List<MetadataColumn> columnsFromEMF = tableFromEMF.getColumns();
        			columnsFromDB = fixedColumnDivergency(columnsFromDB, columnsFromEMF);
				}
        		tableFromDB.getColumns().clear();
        		tableFromDB.getColumns().addAll(columnsFromDB);
			}
        	tablesFromDB = fixedTableDivergency(tablesFromDB, tablesFromEMF);
        	connection.getTables().clear();
        	connection.getTables().addAll(tablesFromDB);
        	newNode = oldNode;
        }
		return newNode;
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
            String[] tableTypes = { "TABLE" };
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
        } catch (SQLException e) {
//            log.error(e.toString());
            throw new RuntimeException(e);
        } catch (Exception e) {
//            log.error(e.toString());
            throw new RuntimeException(e);
        }
        return metadataTables;
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
            e.printStackTrace();
        }
	}
	
	/**
	 * fixed Table Divergency flag.
	 * @param metaFromDB MetadataTable from Database
	 * @param metaFromEMF MetadataTable from Emf
	 * @return MetadataTable List has set divergency flag
	 */
	private List<MetadataTable> fixedTableDivergency(List<MetadataTable> metaFromDB, 
			List<MetadataTable> metaFromEMF) {
		List<MetadataTable> newMetaFromDB = new ArrayList<MetadataTable>();
		for (MetadataTable db : metaFromDB) {
				for (MetadataTable emf : metaFromEMF) {
					if (emf.getSourceName().equals(db.getSourceName())) {
						if (emf.getLabel().equals(db.getLabel())) {
							db.setDivergency(false);
						} else {
							db.setDivergency(true);
						}
					}
				}
			newMetaFromDB.add(db);
		}
		return newMetaFromDB;
	}
	
	/**
	 * fixed Column Divergency flag.
	 * @param columnsFromDB MetadataColumn from Database
	 * @param cloumnsFromEMF MetadataColumn from Emf
	 * @return MetadataColumn List has set divergency flag
	 */
	private List<MetadataColumn> fixedColumnDivergency(List<MetadataColumn> columnsFromDB, 
			List<MetadataColumn> cloumnsFromEMF) {
		List<MetadataColumn> newMetaFromDB = new ArrayList<MetadataColumn>();
		for (MetadataColumn db : columnsFromDB) {
			for (MetadataColumn emf : cloumnsFromEMF) {
				if (db.getOriginalField().equals(emf.getOriginalField())) {
					db.setDivergency(!isEquivalent(db, emf));
				}
			}
			newMetaFromDB.add(db);
		}
		return newMetaFromDB;
	}
	
	 /**
     * Check if TableColumnInfo and MetadataColumnImpl are the same..
     * @param info MetadataColumn
     * @param column MetadataColumnImpl
     * @return isEquivalent.
     * @exception
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

}
