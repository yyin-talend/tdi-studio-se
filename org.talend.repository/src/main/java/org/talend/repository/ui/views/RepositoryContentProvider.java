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
package org.talend.repository.ui.views;

import java.util.List;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.talend.commons.exception.PersistenceException;
import org.talend.commons.utils.data.container.Container;
import org.talend.core.model.general.Version;
import org.talend.core.model.metadata.MetadataTable;
import org.talend.core.model.metadata.builder.connection.Connection;
import org.talend.core.model.metadata.builder.connection.DatabaseConnection;
import org.talend.core.model.metadata.builder.connection.DelimitedFileConnection;
import org.talend.core.model.metadata.builder.connection.LdifFileConnection;
import org.talend.core.model.metadata.builder.connection.PositionalFileConnection;
import org.talend.core.model.metadata.builder.connection.RegexpFileConnection;
import org.talend.core.model.metadata.builder.connection.TableHelper;
import org.talend.core.model.metadata.builder.connection.XmlFileConnection;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.core.model.properties.Property;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.Folder;
import org.talend.core.model.repository.IRepositoryObject;
import org.talend.repository.model.IRepositoryFactory;
import org.talend.repository.model.RepositoryFactoryProvider;
import org.talend.repository.model.RepositoryNode;
import org.talend.repository.model.RepositoryNode.ENodeType;
import org.talend.repository.model.RepositoryNode.EProperties;

/**
 * Content provider for the repository view.<br/>
 * 
 * $Id$
 * 
 */
public class RepositoryContentProvider implements IStructuredContentProvider, ITreeContentProvider {

    private IRepositoryView view;

    public RepositoryContentProvider(IRepositoryView view) {
        super();
        this.view = view;
    }

    public void inputChanged(Viewer v, Object oldInput, Object newInput) {
    }

    public void dispose() {
    }

    public Object[] getElements(Object parent) {
        if (parent.equals(view.getViewSite())) {
            RepositoryNode systemFolders = view.getRoot();
            if (systemFolders.getChildren().isEmpty()) {
                initialize();
            }

            return systemFolders.getChildren().toArray();
        }
        return getChildren(parent);
    }

    public Object getParent(Object child) {
        return ((RepositoryNode) child).getParent();
    }

    public Object[] getChildren(Object parent) {
        return ((RepositoryNode) parent).getChildren().toArray();
    }

    public boolean hasChildren(Object parent) {
        return !((RepositoryNode) parent).getChildren().isEmpty();
    }

    private void initialize() {
        RepositoryNode root = view.getRoot();
        List<RepositoryNode> nodes = root.getChildren();

        IRepositoryFactory factory = RepositoryFactoryProvider.getInstance();
        try {
            // 0. Recycle bin
            RepositoryNode recBinNode = new RepositoryNode(null, root, ENodeType.STABLE_SYSTEM_FOLDER);
            recBinNode.setProperties(EProperties.LABEL, ERepositoryObjectType.RECYCLE_BIN);
            recBinNode.setProperties(EProperties.CONTENT_TYPE, ERepositoryObjectType.RECYCLE_BIN);
            nodes.add(recBinNode);

            // 1. Business process
            RepositoryNode businessProcessNode = new RepositoryNode(null, root, ENodeType.SYSTEM_FOLDER);
            businessProcessNode.setProperties(EProperties.LABEL, ERepositoryObjectType.BUSINESS_PROCESS);
            businessProcessNode.setProperties(EProperties.CONTENT_TYPE, ERepositoryObjectType.BUSINESS_PROCESS);
            nodes.add(businessProcessNode);

            convert(factory.getBusinessProcess(), businessProcessNode, ERepositoryObjectType.BUSINESS_PROCESS, recBinNode);

            // 2. Process
            RepositoryNode processNode = new RepositoryNode(null, root, ENodeType.SYSTEM_FOLDER);
            processNode.setProperties(EProperties.LABEL, ERepositoryObjectType.PROCESS);
            processNode.setProperties(EProperties.CONTENT_TYPE, ERepositoryObjectType.PROCESS);
            nodes.add(processNode);

            convert(factory.getProcess(), processNode, ERepositoryObjectType.PROCESS, recBinNode);
            // convert(factory.getAll(project, ERepositoryObjectType.PROCESS), processNode,
            // ERepositoryObjectType.PROCESS,
            // recBinNode);

            // 3. Routines
            RepositoryNode routineNode = new RepositoryNode(null, root, ENodeType.SYSTEM_FOLDER);
            routineNode.setProperties(EProperties.LABEL, ERepositoryObjectType.ROUTINES);
            routineNode.setProperties(EProperties.CONTENT_TYPE, ERepositoryObjectType.ROUTINES);
            nodes.add(routineNode);
            // 3.1. Snippets
            // PTODO SML this is a temp inplementation of snippets
            RepositoryNode snippetsNode = new RepositoryNode(null, routineNode, ENodeType.STABLE_SYSTEM_FOLDER);
            snippetsNode.setProperties(EProperties.LABEL, ERepositoryObjectType.SNIPPETS);
            snippetsNode.setProperties(EProperties.CONTENT_TYPE, ERepositoryObjectType.SNIPPETS);
            routineNode.getChildren().add(snippetsNode);

            convert(factory.getRoutine(), routineNode, ERepositoryObjectType.ROUTINES, recBinNode);

            // 4. Documentation
            RepositoryNode docNode = new RepositoryNode(null, root, ENodeType.SYSTEM_FOLDER);
            docNode.setProperties(EProperties.LABEL, ERepositoryObjectType.DOCUMENTATION);
            docNode.setProperties(EProperties.CONTENT_TYPE, ERepositoryObjectType.DOCUMENTATION);
            nodes.add(docNode);

            convert(factory.getDocumentation(), docNode, ERepositoryObjectType.DOCUMENTATION, recBinNode);

            // 5. Metadata
            RepositoryNode metadataNode = new RepositoryNode(null, root, ENodeType.STABLE_SYSTEM_FOLDER);
            metadataNode.setProperties(EProperties.LABEL, ERepositoryObjectType.METADATA);
            metadataNode.setProperties(EProperties.CONTENT_TYPE, ERepositoryObjectType.METADATA);
            nodes.add(metadataNode);

            // 5.1. Metadata connections
            RepositoryNode metadataConNode = new RepositoryNode(null, root, ENodeType.SYSTEM_FOLDER);
            metadataConNode.setProperties(EProperties.LABEL, ERepositoryObjectType.METADATA_CONNECTIONS);
            metadataConNode.setProperties(EProperties.CONTENT_TYPE, ERepositoryObjectType.METADATA_CONNECTIONS);
            metadataNode.getChildren().add(metadataConNode);

            convert(factory.getMetadataConnection(), metadataConNode, ERepositoryObjectType.METADATA_CONNECTIONS, recBinNode);

            // 5.2. Metadata file delimited
            RepositoryNode metadataFileNode = new RepositoryNode(null, root, ENodeType.SYSTEM_FOLDER);
            metadataFileNode.setProperties(EProperties.LABEL, ERepositoryObjectType.METADATA_FILE_DELIMITED);
            metadataFileNode.setProperties(EProperties.CONTENT_TYPE, ERepositoryObjectType.METADATA_FILE_DELIMITED);
            metadataNode.getChildren().add(metadataFileNode);

            convert(factory.getMetadataFileDelimited(), metadataFileNode, ERepositoryObjectType.METADATA_FILE_DELIMITED,
                    recBinNode);

            // 5.3. Metadata file positional
            RepositoryNode metadataFilePositionalNode = new RepositoryNode(null, root, ENodeType.SYSTEM_FOLDER);
            metadataFilePositionalNode.setProperties(EProperties.LABEL, ERepositoryObjectType.METADATA_FILE_POSITIONAL);
            metadataFilePositionalNode.setProperties(EProperties.CONTENT_TYPE, ERepositoryObjectType.METADATA_FILE_POSITIONAL);
            metadataNode.getChildren().add(metadataFilePositionalNode);

            convert(factory.getMetadataFilePositional(), metadataFilePositionalNode,
                    ERepositoryObjectType.METADATA_FILE_POSITIONAL, recBinNode);

            // 5.4. Metadata file regexp
            RepositoryNode metadataFileRegexpNode = new RepositoryNode(null, root, ENodeType.SYSTEM_FOLDER);
            metadataFileRegexpNode.setProperties(EProperties.LABEL, ERepositoryObjectType.METADATA_FILE_REGEXP);
            metadataFileRegexpNode.setProperties(EProperties.CONTENT_TYPE, ERepositoryObjectType.METADATA_FILE_REGEXP);
            metadataNode.getChildren().add(metadataFileRegexpNode);

            convert(factory.getMetadataFileRegexp(), metadataFileRegexpNode, ERepositoryObjectType.METADATA_FILE_REGEXP,
                    recBinNode);

            // 5.5. Metadata file xml
            RepositoryNode metadataFileXmlNode = new RepositoryNode(null, root, ENodeType.SYSTEM_FOLDER);
            metadataFileXmlNode.setProperties(EProperties.LABEL, ERepositoryObjectType.METADATA_FILE_XML);
            metadataFileXmlNode.setProperties(EProperties.CONTENT_TYPE, ERepositoryObjectType.METADATA_FILE_XML);
            metadataNode.getChildren().add(metadataFileXmlNode);

            convert(factory.getMetadataFileXml(), metadataFileXmlNode, ERepositoryObjectType.METADATA_FILE_XML, recBinNode);

            // 5.6. Metadata file ldif
            RepositoryNode metadataFileLdifNode = new RepositoryNode(null, root, ENodeType.SYSTEM_FOLDER);
            metadataFileLdifNode.setProperties(EProperties.LABEL, ERepositoryObjectType.METADATA_FILE_LDIF);
            metadataFileLdifNode.setProperties(EProperties.CONTENT_TYPE, ERepositoryObjectType.METADATA_FILE_LDIF);
            metadataNode.getChildren().add(metadataFileLdifNode);

            convert(factory.getMetadataFileLdif(), metadataFileLdifNode, ERepositoryObjectType.METADATA_FILE_LDIF, recBinNode);

            
        } catch (PersistenceException e) {
            e.printStackTrace();
        }
    }

    private void convert(List<IRepositoryObject> list, RepositoryNode parent, ERepositoryObjectType type,
            RepositoryNode recBinNode) {
        for (IRepositoryObject obj : list) {
            addNode(parent, type, recBinNode, obj);
        }
    }

    private void convert(Container fromModel, RepositoryNode parent, ERepositoryObjectType type, RepositoryNode recBinNode) {
        if (fromModel.isEmpty()) {
            return;
        }

        for (Object obj : fromModel.getSubContainer()) {
            Container container = (Container) obj;
            Folder oFolder = new Folder(container.getId(), container.getLabel());
            RepositoryNode folder = new RepositoryNode(oFolder, parent, ENodeType.SIMPLE_FOLDER);
            folder.setProperties(EProperties.LABEL, container.getLabel());
            folder.setProperties(EProperties.CONTENT_TYPE, type);// ERepositoryObjectType.FOLDER);
            parent.getChildren().add(folder);
            convert(container, folder, type, recBinNode);
        }

        for (Object obj : fromModel.getMembers()) {
            IRepositoryObject repositoryObject = (IRepositoryObject) obj;
            addNode(parent, type, recBinNode, repositoryObject);
        }
    }

    private void addNode(RepositoryNode parent, ERepositoryObjectType type, RepositoryNode recBinNode,
            IRepositoryObject repositoryObject) {
        IRepositoryFactory factory = RepositoryFactoryProvider.getInstance();

        RepositoryNode node = new RepositoryNode(repositoryObject, parent, ENodeType.REPOSITORY_ELEMENT);

        node.setProperties(EProperties.CONTENT_TYPE, type);
        node.setProperties(EProperties.LABEL, repositoryObject.getLabel());
        try {
            if (factory.isDeleted(repositoryObject)) {
                recBinNode.getChildren().add(node);
            } else {
                parent.getChildren().add(node);
            }
        } catch (PersistenceException e1) {
            e1.printStackTrace();
        }

        if (type == ERepositoryObjectType.METADATA_CONNECTIONS) {
            DatabaseConnection metadataConnection = (DatabaseConnection) ((ConnectionItem) repositoryObject.getProperty()
                    .getItem()).getConnection();
            createTables(recBinNode, node, repositoryObject, metadataConnection);
        }
        // PTODO tgu implementation a revoir
        if (type == ERepositoryObjectType.METADATA_FILE_DELIMITED) {
            DelimitedFileConnection metadataConnection = (DelimitedFileConnection) ((ConnectionItem) repositoryObject
                    .getProperty().getItem()).getConnection();
            createTables(recBinNode, node, repositoryObject, metadataConnection);
        }
        if (type == ERepositoryObjectType.METADATA_FILE_POSITIONAL) {
            PositionalFileConnection metadataConnection = (PositionalFileConnection) ((ConnectionItem) repositoryObject
                    .getProperty().getItem()).getConnection();
            createTables(recBinNode, node, repositoryObject, metadataConnection);
        }
        if (type == ERepositoryObjectType.METADATA_FILE_REGEXP) {
            RegexpFileConnection metadataConnection = (RegexpFileConnection) ((ConnectionItem) repositoryObject.getProperty()
                    .getItem()).getConnection();
            createTables(recBinNode, node, repositoryObject, metadataConnection);
        }
        if (type == ERepositoryObjectType.METADATA_FILE_XML) {
            XmlFileConnection metadataConnection = (XmlFileConnection) ((ConnectionItem) repositoryObject.getProperty().getItem())
                    .getConnection();
            createTables(recBinNode, node, repositoryObject, metadataConnection);
        }
        if (type == ERepositoryObjectType.METADATA_FILE_LDIF) {
            LdifFileConnection metadataConnection = (LdifFileConnection) ((ConnectionItem) repositoryObject.getProperty().getItem())
                    .getConnection();
            createTables(recBinNode, node, repositoryObject, metadataConnection);
        }
    }

    /**
     * DOC tguiu Comment method "createTables".
     * 
     * @param node
     * @param iMetadataConnection
     * @param metadataConnection
     */
    private void createTables(RepositoryNode recBinNode, RepositoryNode node, final IRepositoryObject repObj,
            Connection metadataConnection) {
        for (Object currentTable : metadataConnection.getTables()) {
            org.talend.core.model.metadata.builder.connection.MetadataTable metadataTable = (org.talend.core.model.metadata.builder.connection.MetadataTable) currentTable;
            RepositoryNode tableNode = createMetatable(node, repObj, metadataTable);
            if (TableHelper.isDeleted(metadataTable)) {
                recBinNode.getChildren().add(tableNode);
            } else {
                node.getChildren().add(tableNode);
            }
        }
    }

    /**
     * DOC tguiu Comment method "createMetatable".
     * 
     * @param node
     * @param iMetadataFileDelimited
     * @param table
     * @return
     */
    private RepositoryNode createMetatable(RepositoryNode node, IRepositoryObject repObj,
            final org.talend.core.model.metadata.builder.connection.MetadataTable table) {
        MetadataTable modelObj = new MetadataTableRepositoryObject(repObj, table);
        modelObj.setLabel(table.getLabel());
        RepositoryNode tableNode = new RepositoryNode(modelObj, node, ENodeType.REPOSITORY_ELEMENT);
        tableNode.setProperties(EProperties.LABEL, table.getLabel());
        tableNode.setProperties(EProperties.CONTENT_TYPE, ERepositoryObjectType.METADATA_CON_TABLE);
        return tableNode;
    }

    /**
     */
    public static class MetadataTableRepositoryObject extends MetadataTable {

        private IRepositoryObject repObj;

        private org.talend.core.model.metadata.builder.connection.MetadataTable table;

        public MetadataTableRepositoryObject(IRepositoryObject repObj,
                org.talend.core.model.metadata.builder.connection.MetadataTable table) {
            this.repObj = repObj;
            this.table = table;
        }

        public Property getProperty() {
            return repObj.getProperty();
        }

        public Version getVersion() {
            return repObj.getVersion();
        }

        public String getLabel() {
            return table.getLabel();
        }

        public String getId() {
            return table.getId();
        }

        public org.talend.core.model.metadata.builder.connection.MetadataTable getTable() {
            return this.table;
        }
    }
}
