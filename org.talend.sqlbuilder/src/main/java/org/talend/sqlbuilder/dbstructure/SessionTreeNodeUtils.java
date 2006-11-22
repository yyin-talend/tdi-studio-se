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
 * @author phou
 * 
 */
public class SessionTreeNodeUtils {

    private static Set connections = new HashSet();

    // private static List<SessionTreeNode> sessionTreeNodes = new ArrayList<SessionTreeNode>();

    private static List<INode> catalogNodes = new ArrayList<INode>();

    private static List<INode> tableNodes = new ArrayList<INode>();

    private static List<INode> cachedAllNodes = new ArrayList<INode>();

    private static List<INode> cachedCurrentNodes = new ArrayList<INode>();

    /**
     * Get all CatelogNodes.
     * 
     * @return CatelogNodes List.
     */
    public static List<INode> getCatalogNodes() {
        return catalogNodes;
    }

    /**
     * Get all TableNodes.
     * 
     * @return TableNodes List.
     */
    public static List<INode> getTableNodes() {
        return tableNodes;
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
     * @param name RepositoryName
     * @param url Connection Url
     * @param userName DB username
     * @param password DB password
     * @param databaseName databaseName
     * @param repositoryNode RepositoryNode
     * @param dbType dbType
     * @return SessionTreeNode SessionTreeNode.
     */
    public static SessionTreeNode getSessionTreeNode(String name, String dbType, String url, String userName,
            String password, String databaseName, RepositoryNode repositoryNode) throws Exception {
        SQLConnection connection = createSQLConnection(dbType, url, userName, password);
        ISQLAlias alias = createSQLAlias(name, url, userName, password, databaseName);
        SessionTreeModel stm = new SessionTreeModel();
        SessionTreeNode session;
        session = stm.createSessionTreeNode(new SQLConnection[] { connection, connection }, alias, null, "root",
                repositoryNode);
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
    private static ISQLAlias createSQLAlias(String name, String url, String userName, String password,
            String databaseName) {
        SQLAlias alias = new SQLAlias(IdentifierFactory.getInstance().createIdentifier());
        try {
            alias.setName(databaseName);
            alias.setUrl(url);
            alias.setUserName(userName);
            alias.setPassword(password);
            alias.setSchemaFilterExpression(databaseName);
            alias.setFolderFilterExpression("Tables,Views");
        } catch (Exception e) {
             SqlBuilderPlugin.log(e.getMessage(), e);
        }
        return alias;
    }

    /**
     * DOC qianbing Comment method "createSQLConnection".
     * @param dbType database Type
     * @param url  url
     * @param userName userName
     * @param password password
     * @return SQLConnection
     * @throws Exception  Exception
     */
    private static SQLConnection createSQLConnection(String dbType, String url, String userName, String password)
            throws Exception {
        Class.forName(ExtractMetaDataUtils.getDriverClassByDbType(dbType)).newInstance();
        Connection connection = DriverManager.getConnection(url, userName, password);
        SQLConnection sqlConnection = new SQLConnection(connection, null);
        return sqlConnection;
    }

    /**
     * disposeConnections.
     */
    private static void disposeConnections() {
        SQLConnection connection = null;
        for (Iterator it = connections.iterator(); it.hasNext();) {
            connection = (SQLConnection) it.next();
            try {
                connection.close();
            } catch (Exception e) {
                SqlBuilderPlugin.log("close database connection", e);
            }
        }
        connections.clear();
    }
}
