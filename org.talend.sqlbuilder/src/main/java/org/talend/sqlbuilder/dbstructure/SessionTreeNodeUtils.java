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
package org.talend.sqlbuilder.dbstructure;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import net.sourceforge.sqlexplorer.IdentifierFactory;
import net.sourceforge.sqlexplorer.SQLAlias;
import net.sourceforge.squirrel_sql.fw.sql.ISQLAlias;
import net.sourceforge.squirrel_sql.fw.sql.SQLConnection;

import org.talend.core.model.metadata.builder.connection.DatabaseConnection;
import org.talend.core.model.metadata.builder.database.ExtractMetaDataUtils;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.IRepositoryObject;
import org.talend.repository.model.RepositoryNode;
import org.talend.repository.model.RepositoryNode.ENodeType;
import org.talend.repository.model.RepositoryNode.EProperties;
import org.talend.sqlbuilder.SqlBuilderPlugin;
import org.talend.sqlbuilder.dbstructure.nodes.CatalogNode;
import org.talend.sqlbuilder.dbstructure.nodes.INode;
import org.talend.sqlbuilder.sessiontree.model.SessionTreeModel;
import org.talend.sqlbuilder.sessiontree.model.SessionTreeNode;

/**
 * Detailled comment for this class. <br/> $Id: SessionTreeNodeUtils.java,v 1.24 2006/11/08 09:58:55 peiqin.hou Exp $
 * 
 * @author Hou Peiqin (Soyatec)
 * 
 */
public class SessionTreeNodeUtils {

    private static Set connections = new HashSet();

//    private static List<SessionTreeNode> sessionTreeNodes = new ArrayList<SessionTreeNode>();
    
    private static List<INode> catalogNodes = new ArrayList<INode>();
    
    private static List<INode> tableNodes = new ArrayList<INode>();
    
    private static List<INode> cachedAllNodes = new ArrayList<INode>();
    
    private static List<INode> cachedCurrentNodes = new ArrayList<INode>();
    /**
     * Get all CatelogNodes.
     * @return CatelogNodes List.
     */
    public static List<INode> getCatalogNodes() {
        return catalogNodes;
    }
    
    /**
     * Get all TableNodes.
     * @return TableNodes List.
     */
    public static List<INode> getTableNodes() {
        return tableNodes;
    }

  
   
    /**
     * Get Table names from SessionTreeNode.
     * @param sessionTreeNode sessionTreeNode
     * @return table names.
     */
    public static List<String> getTableNames(SessionTreeNode sessionTreeNode) {
        List<String> tableNames = new ArrayList<String>();
        DatabaseModel databaseModel = sessionTreeNode.getDbModel();
        if (databaseModel == null || databaseModel.getRoot().getChildNodes().length == 0) {
            return tableNames;
        }
        CatalogNode catalogNode = (CatalogNode) databaseModel.getRoot().getChildNodes()[0];
        INode[] nodes = catalogNode.getChildNodes();
        if (nodes == null) {
            return tableNames;
        }
        for (INode node : nodes) {
            tableNames.add(node.getQualifiedName());
        }
        
        return tableNames;
    }
    
    /**
     * dispose.
     */
    public static void dispose() {
        cachedAllNodes.clear();
        cachedCurrentNodes.clear();
        catalogNodes.clear();
        tableNodes.clear();
        disposeConnections();
    }

    /**
     * @return RepositoryName list.
     */
    public static List<String> getRepositoryNames() {
        List<String> repositoryNames = new ArrayList<String>();
        for (INode catalogNode : catalogNodes) {
            repositoryNames.add(catalogNode.getLabelAtColumn(1) == null 
                   ? catalogNode.getLabelAtColumn(0) : catalogNode.getLabelAtColumn(1));
        }
        return repositoryNames;
    }
    
    /**
     * @param repositoryName repositoryName
     * @return SessionTreeNode
     */
    public static SessionTreeNode getSessionTreeNode(String repositoryName) {
        if (repositoryName == null) {
            return null;
        }
        for (INode catalogNode : catalogNodes) {
            if (repositoryName.equals(catalogNode.getLabelAtColumn(1))) {
                return catalogNode.getSession();
            }
        }
        return null;
    }
    
    /**
     * @param name RepositoryName
     * @param url Connection Url
     * @param userName DB username
     * @param password DB password
     * @param databaseName databaseName
     * @param repositoryNode RepositoryNode
     * @param driverName driverName
     * @return SessionTreeNode SessionTreeNode.
     */
    public static SessionTreeNode getSessionTreeNode(String name, String dbType, String url, String userName,
            String password, String databaseName, RepositoryNode repositoryNode) {
        SQLConnection connection = createSQLConnection(dbType, url, userName, password);
        // if(connection == null)
        // {
        // return null;
        // }

        ISQLAlias alias = createSQLAlias(name, url, userName, password, databaseName);
        SessionTreeModel stm = new SessionTreeModel();
        SessionTreeNode session;
        try {
            session = stm.createSessionTreeNode(new SQLConnection[] { connection, connection }, alias, null, "root",
                    repositoryNode);
        } catch (InterruptedException e) {
            SqlBuilderPlugin.log(e.getMessage(), e);
            return null;
        }

        return session;
    }
    
    /**
     * @param url Connection Url
     * @param userName DB username
     * @param password DB password
     * @param databaseName databaseName
     * @param name RepositoryName
     * @return ISQLAlias
     */
    private static ISQLAlias createSQLAlias(String name, String url, String userName, String password, String databaseName) {
        SQLAlias alias = new SQLAlias(IdentifierFactory.getInstance().createIdentifier());
        try {
            alias.setName(databaseName);
            alias.setUrl(url);
            alias.setUserName(userName);
            alias.setPassword(password);
            alias.setSchemaFilterExpression(databaseName);
            alias.setFolderFilterExpression("Tables,Views");
        } catch (Exception e) {
//            SqlBuilderPlugin.log(e.getMessage(), e);
        }
        return alias;
    }

    /**
     * @param url Connection Url
     * @param userName DB username
     * @param password DB password
     * @param driverName driverName
     * @return SQLConnection
     */
    private static SQLConnection createSQLConnection(String dbType, String url, String userName, String password) {
        try {
            Class.forName(ExtractMetaDataUtils.getDriverClassByDbType(dbType)).newInstance();

            Connection connection = DriverManager.getConnection(url, userName, password);

            SQLConnection sqlConnection = new SQLConnection(connection, null);

            return sqlConnection;
        } catch (Exception e) {
             SqlBuilderPlugin.log(e.getMessage(), e);
        }

        return null;
    }

    /**
     * disposeConnections.
     */
    public static void disposeConnections() {
        SQLConnection connection = null;
        for (Iterator it = connections.iterator(); it.hasNext();) {
            connection = (SQLConnection) it.next();
            try {
                connection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        connections.clear();
    }
}
