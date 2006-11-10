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
import net.sourceforge.squirrel_sql.fw.persist.ValidationException;
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
import org.talend.sqlbuilder.ui.RefreshTreeCommand;
import org.talend.sqlbuilder.util.ConnectionParameters;

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

    public static List<INode> getCachedAllNodes() {
        return cachedAllNodes;
    }

    public static void setCachedAllNodes(INode[] nodes) {
        if (nodes == null) {
            return;
        }
        for (INode node : nodes) {
            cachedAllNodes.add(node);
        }
    }

    public static List<INode> getCachedCurrentNodes() {
        return cachedCurrentNodes;
    }

    public static void setCachedCurrentNodes(INode[] nodes) {
        if (nodes == null) {
            return;
        }
        for (INode node : nodes) {
            cachedCurrentNodes.add(node);
        }
    }

    /**
     * getDatabaseNameByTableName.
     * @param tableName table name
     * @return database name
     * @exception
     */
    public static String getDatabaseNameByTableName(String tableName) {
        if (tableName == null || tableName.trim().equals("")) {
            return null;
        }
        for (INode node : tableNodes) {
            if (node.getLabelText().equals(tableName)) {
                return node.getParent().getParent().getLabelText();
            }
        }
        return null;
    }
    
    /**
     * getAllTableNames. 
     * @return Table names list.
     */
    public static List<String> getAllTableNames() {
        List<String> allTableNames = new ArrayList<String>();
        for (INode node : tableNodes) {
            allTableNames.add(node.getQualifiedName());
        }
        return allTableNames;
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
//    /**
//     * @return SessionTreeNode list.
//     */
//    public static List<SessionTreeNode> getSessionTreeNodes() {
//        return sessionTreeNodes;
//    }
    
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
//             SqlBuilderPlugin.log(e.getMessage(), e);
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
    
    public static INode[] getAllNodes(RepositoryNode repositoryNode) {
        List<RepositoryNode> repositoryNodes = repositoryNode.getChildren();
        List<INode> nodes = new ArrayList<INode>();
        for (int i = 0, size = repositoryNodes.size(); i < size; i++) {
            RepositoryNode tempRepositoryNode = repositoryNodes.get(i);
            if (tempRepositoryNode.getObject().getType() != ERepositoryObjectType.METADATA_CONNECTIONS 
                    && !isEmptyFolder(tempRepositoryNode)) {
                RepositoryExtNode repositoryExtNode = new RepositoryExtNode();
                repositoryExtNode.setRepositoryNode(tempRepositoryNode);
                nodes.add(repositoryExtNode);
            } else {
                DatabaseModel databaseModel = ((DatabaseModel) convert2DatabaseModel(tempRepositoryNode));
                if (databaseModel == null || databaseModel.getRoot().getChildNodes().length == 0) {
                    continue;
                }
                nodes.add(databaseModel.getRoot().getChildNodes()[0]);
                RefreshTreeCommand.getInstance().setCurrentNodes(nodes.toArray(new INode[]{}));
                RefreshTreeCommand.getInstance().execute();
                //if the connection is a valid connection then add to CatalogNodes list.
                if (databaseModel.getSession().getInteractiveConnection() != null) {
                    SessionTreeNodeUtils.getCatalogNodes().add(databaseModel.getRoot().getChildNodes()[0]);
                }
            }
        }
        return nodes.toArray(new INode[]{}); 
    }
    
    /**
     * Check if a repositoryNode is a empty folder.
     * 
     * method description.
     * @param repositoryNode2 RepositoryNode
     * @return isEmptyFolder
     * @exception
     */
    private static boolean isEmptyFolder(RepositoryNode repositoryNode2) {
        boolean isEmptyFolder = true;
        if (repositoryNode2.hasChildren()) {
            List<RepositoryNode> list = repositoryNode2.getChildren();
            for (RepositoryNode node : list) {
                if (node.getObject().getType() == ERepositoryObjectType.METADATA_CONNECTIONS) {
                    isEmptyFolder = false;
                } else {
                    isEmptyFolder = isEmptyFolder(node);
                }
            }
        }
        return isEmptyFolder;
    }

    /**
     * Convert RepositoryNode ==> DatabaseModel
     * 
     * method description.
     * @param repositoryNode2  the node will be converted.
     * @return INode
     * @exception
     */
    private static INode convert2DatabaseModel(RepositoryNode repositoryNode2) {
        if (repositoryNode2 == null || repositoryNode2.getObject() == null 
                || repositoryNode2.getObject().getProperty() == null || repositoryNode2.getObject().getProperty().getItem() == null) {
            return null;
        }
        DatabaseConnection connection = (DatabaseConnection) ((ConnectionItem) repositoryNode2.getObject()
                .getProperty().getItem()).getConnection();
        
        SessionTreeNode sessionTreeNode = SessionTreeNodeUtils.getSessionTreeNode(getRepositoryName(repositoryNode2), 
                connection.getDatabaseType(), connection.getURL(), connection.getUsername(), 
                connection.getPassword(), connection.getSID(), repositoryNode2);
        return sessionTreeNode.getDbModel();
    }
    
    /**
     * Get RepsotiryNode name.
     * 
     * method description.
     * @param element -- RepositoryNode
     * @return the name of the RepositoryNode
     * @exception
     */
    private static String getRepositoryName(RepositoryNode element) {
        RepositoryNode node = (RepositoryNode) element;
        if (node.getType() == ENodeType.REPOSITORY_ELEMENT) {
            IRepositoryObject object = node.getObject();

            StringBuffer string = new StringBuffer();
            string.append(object.getLabel());

            return string.toString();
        } else if (node.getType() == ENodeType.SIMPLE_FOLDER) {
            StringBuffer string = new StringBuffer(node.getProperties(EProperties.LABEL).toString());

            return string.toString();
        } else {
            return node.getProperties(EProperties.LABEL).toString();
        }
    
    }
    
    /**
     * Get buildIn Nodes.
     * @return INode[]
     */
    public static INode[] getBuildInNodes(ConnectionParameters connectionParameters) {
        List<INode> nodes = new ArrayList<INode>();
        SessionTreeNode sessionTreeNode = SessionTreeNodeUtils.getSessionTreeNode("", 
                connectionParameters.getDbType(), connectionParameters.getURL(), connectionParameters.getUserName(), 
                connectionParameters.getPassword(), connectionParameters.getDbName(), null);
        
        DatabaseModel databaseModel = sessionTreeNode.getDbModel();
        if (databaseModel != null && databaseModel.getRoot().getChildNodes().length != 0) {
            nodes.add(databaseModel.getRoot().getChildNodes()[0]);
            RefreshTreeCommand.getInstance().setCurrentNodes(nodes.toArray(new INode[]{}));
            RefreshTreeCommand.getInstance().execute();
            SessionTreeNodeUtils.getCatalogNodes().add(databaseModel.getRoot().getChildNodes()[0]);
        }
        return nodes.toArray(new INode[]{});
    }
    
    /**
     * Get repositoryNodes.
     * @return INode[]
     */
    public static INode[] getRepositoryNodes(RepositoryNode rootRepositoryNode, ConnectionParameters connectionParameters) {
        List<INode> nodes = new ArrayList<INode>();
        RepositoryNode currentRepositoryNode = getRepositoryNodeByName(rootRepositoryNode, connectionParameters.getRepositoryName());
        DatabaseModel databaseModel = ((DatabaseModel) convert2DatabaseModel(currentRepositoryNode));
        if (databaseModel != null && databaseModel.getRoot().getChildNodes().length != 0) {
            nodes.add(databaseModel.getRoot().getChildNodes()[0]);
            RefreshTreeCommand.getInstance().setCurrentNodes(nodes.toArray(new INode[]{}));
            RefreshTreeCommand.getInstance().execute();
            SessionTreeNodeUtils.getCatalogNodes().add(databaseModel.getRoot().getChildNodes()[0]);
        }
        return nodes.toArray(new INode[]{});
    }
        
    /**
     * Get repositoryNode by repositoryName.
     * @param repositoryName repository name.
     * @return RepositoryNode.
     */
    private static RepositoryNode getRepositoryNodeByName(RepositoryNode rootRepositoryNode, String repositoryName) {
        List<RepositoryNode> repositoryNodes = rootRepositoryNode.getChildren();
        List<INode> nodes = new ArrayList<INode>();
        for (int i = 0, size = repositoryNodes.size(); i < size; i++) {
            RepositoryNode tempRepositoryNode = repositoryNodes.get(i);
            if (tempRepositoryNode.getObject().getType() != ERepositoryObjectType.METADATA_CONNECTIONS 
                    && !isEmptyFolder(tempRepositoryNode)) {
                continue;
            } else {
                if (tempRepositoryNode.getProperties(EProperties.LABEL).equals(repositoryName)) {
                    return tempRepositoryNode;
                }
            }
        }
        
        return null;
    }
    
    /**
     * Get the current nodes.
     * @param isShowAllConnections isShowAllConnections
     * @param rootRepositoryNode root repository
     * @param connectionParameters connection parameters
     * @return current nodes.
     * @exception
     */
    public static INode[] getCurrentNodes(boolean isShowAllConnections, RepositoryNode rootRepositoryNode, 
            ConnectionParameters connectionParameters) {
        if (isShowAllConnections) {
            List<INode> list = SessionTreeNodeUtils.getCachedAllNodes();
            if (list.size() == 0) {
                INode[] rNodes = getAllNodes(rootRepositoryNode);
                SessionTreeNodeUtils.setCachedAllNodes(rNodes);
                return rNodes;
            } else {
                return list.toArray(new INode[]{});
            }
        }
        
        List<INode> cachedCurrentNodes = SessionTreeNodeUtils.getCachedCurrentNodes();
        if (cachedCurrentNodes.size() != 0) {
            return cachedCurrentNodes.toArray(new INode[]{});
        }
        INode[] rNodes = null;
        if (!connectionParameters.isRepository()) {
            rNodes = getBuildInNodes(connectionParameters);
        } else {
            rNodes = getRepositoryNodes(rootRepositoryNode, connectionParameters);
        }
        SessionTreeNodeUtils.setCachedCurrentNodes(rNodes);
        return rNodes;
    }
}
