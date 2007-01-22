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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.talend.core.model.metadata.builder.connection.DatabaseConnection;
import org.talend.core.model.metadata.builder.connection.MetadataTable;
import org.talend.core.model.metadata.builder.connection.QueriesConnection;
import org.talend.core.model.metadata.builder.connection.Query;
import org.talend.core.model.properties.DatabaseConnectionItem;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.repository.model.RepositoryNode;
import org.talend.repository.model.RepositoryNode.EProperties;
import org.talend.sqlbuilder.dbstructure.RepositoryNodeType;
import org.talend.sqlbuilder.dbstructure.DBTreeProvider.MetadataColumnRepositoryObject;
import org.talend.sqlbuilder.dbstructure.DBTreeProvider.MetadataTableRepositoryObject;

/**
 * DOC qzhang class global comment. Detailled comment <br/>
 * 
 * $Id: talend-code-templates.xml 1 2006-09-29 17:06:40 +0000 (Fri, 29 Sep 2006) nrousseau $
 * 
 */
public class EMFRepositoryNodeManager {

    /**
     * DOC dev Comment method "getQueryByLabel".
     * 
     * @param node all repository node Type
     * @param label search query label
     * @return
     */
    @SuppressWarnings("unchecked")
    public static Query getQueryByLabel(RepositoryNode node, String label) {
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

    @SuppressWarnings("unchecked")
    public static List<MetadataTable> getTables(List<RepositoryNode> nodes) {
        List<MetadataTable> tables = new ArrayList<MetadataTable>();
        for (RepositoryNode node : nodes) {
            RepositoryNodeType type = SQLBuilderRepositoryNodeManager.getRepositoryType(node);
            if (type == RepositoryNodeType.DATABASE) {
                DatabaseConnection connection = (DatabaseConnection) SQLBuilderRepositoryNodeManager.getItem(node)
                        .getConnection();
                for (MetadataTable table : (List<MetadataTable>) connection.getTables()) {
                    if (!tables.contains(table)) {
                        tables.add(table);
                    }
                }
            } else if (type == RepositoryNodeType.TABLE) {
                MetadataTable table = ((MetadataTableRepositoryObject) node.getObject()).getTable();
                if (!tables.contains(table)) {
                    tables.add(table);
                }
            } else if (type == RepositoryNodeType.COLUMN) {
                MetadataTable table = ((MetadataColumnRepositoryObject) node.getObject()).getColumn().getTable();
                if (!tables.contains(table)) {
                    tables.add(table);
                }
            }

        }
        return tables;
    }

    private static List<RepositoryNode> folderdbNodes = new ArrayList<RepositoryNode>();

    public static boolean setParentNodesOverDatabaseNode(RepositoryNode databaseNode) {
        folderdbNodes.clear();
        RepositoryNode parentNode = databaseNode.getParent();
        if (parentNode == null) {
            return false;
        } else if (parentNode.getProperties(EProperties.CONTENT_TYPE).equals(RepositoryNodeType.FOLDER)) {
            folderdbNodes.add(parentNode);
        }
        return setParentNodesOverDatabaseNode(parentNode);
    }

    
    public static List<RepositoryNode> getFolderdbNodes() {
        return folderdbNodes;
    }

    
    public static void setFolderdbNodes(List<RepositoryNode> folderdbNodes) {
        EMFRepositoryNodeManager.folderdbNodes = folderdbNodes;
    }
    
}
