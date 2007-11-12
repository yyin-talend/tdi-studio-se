// ============================================================================
//
// Copyright (C) 2006-2007 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the  agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
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

import org.talend.core.model.metadata.IMetadataConnection;
import org.talend.core.model.metadata.builder.ConvertionHelper;
import org.talend.core.model.metadata.builder.connection.DatabaseConnection;
import org.talend.core.model.metadata.builder.database.ExtractMetaDataUtils;
import org.talend.repository.model.RepositoryNode;
import org.talend.sqlbuilder.Messages;
import org.talend.sqlbuilder.SqlBuilderPlugin;
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

    public static SessionTreeNode getSessionTreeNode(DatabaseConnection dbconnection, RepositoryNode repositoryNode)
            throws Exception {
        SQLConnection connection = createSQLConnection(dbconnection);
        ISQLAlias alias = createSQLAlias("Repository Name", dbconnection.getURL(), dbconnection.getUsername(), dbconnection
                .getPassword(),  dbconnection.getSID().length() == 0 ? dbconnection
                        .getDatasourceName() : dbconnection.getSID());
        SessionTreeModel stm = new SessionTreeModel();
        SessionTreeNode session;
        session = stm.createSessionTreeNode(new SQLConnection[] { connection, connection }, alias, null, dbconnection
                .getPassword(), //$NON-NLS-1$
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
    private static ISQLAlias createSQLAlias(String name, String url, String userName, String password, String databaseName) {
        SQLAlias alias = new SQLAlias(IdentifierFactory.getInstance().createIdentifier());
        try {
            alias.setName(databaseName);
            alias.setUrl(url);
            alias.setUserName(userName);
            alias.setPassword(password);
            alias.setSchemaFilterExpression(databaseName);
            alias.setFolderFilterExpression("Tables,Views,Synonyms");
        } catch (Exception e) {
            SqlBuilderPlugin.log(e.getMessage(), e);
        }
        return alias;
    }

    /**
     * DOC qianbing Comment method "createSQLConnection".
     * 
     * @param dbType database Type
     * @param url url
     * @param userName userName
     * @param password password
     * @return SQLConnection
     * @throws Exception Exception
     */
    protected static SQLConnection createSQLConnection(String dbType, String url, String userName, String password)
            throws Exception {
        Class.forName(ExtractMetaDataUtils.getDriverClassByDbType(dbType)).newInstance();
        Connection connection = DriverManager.getConnection(url, userName, password);
        SQLConnection sqlConnection = new SQLConnection(connection, null);
        return sqlConnection;
    }

    private static SQLConnection createSQLConnection(DatabaseConnection con) throws Exception {
        IMetadataConnection iMetadataConnection = ConvertionHelper.convert(con);
        ExtractMetaDataUtils.getConnection(iMetadataConnection.getDbType(), iMetadataConnection.getUrl(), iMetadataConnection
                .getUsername(), iMetadataConnection.getPassword(), iMetadataConnection.getDatabase(), iMetadataConnection
                .getSchema());
        SQLConnection sqlConnection = new SQLConnection(ExtractMetaDataUtils.conn, null);
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
                SqlBuilderPlugin.log(Messages.getString("SessionTreeNodeUtils.logMessage"), e); //$NON-NLS-1$
            }
        }
        connections.clear();
    }
}
