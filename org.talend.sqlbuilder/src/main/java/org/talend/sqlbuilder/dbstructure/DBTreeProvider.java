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

import org.eclipse.jface.viewers.ITableColorProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.talend.commons.exception.PersistenceException;
import org.talend.commons.utils.data.container.Container;
import org.talend.core.model.general.Version;
import org.talend.core.model.metadata.builder.connection.Connection;
import org.talend.core.model.metadata.builder.connection.DatabaseConnection;
import org.talend.core.model.metadata.builder.connection.MetadataColumn;
import org.talend.core.model.metadata.builder.connection.MetadataTable;
import org.talend.core.model.metadata.builder.connection.TableHelper;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.core.model.properties.Property;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.Folder;
import org.talend.core.model.repository.IRepositoryObject;
import org.talend.core.model.repository.RepositoryObject;
import org.talend.repository.model.IRepositoryFactory;
import org.talend.repository.model.RepositoryFactoryProvider;
import org.talend.repository.model.RepositoryNode;
import org.talend.repository.model.RepositoryNode.ENodeType;
import org.talend.repository.model.RepositoryNode.EProperties;
import org.talend.repository.ui.views.RepositoryContentProvider;
import org.talend.repository.ui.views.RepositoryView;
import org.talend.sqlbuilder.SqlBuilderPlugin;
import org.talend.sqlbuilder.repository.utility.SQLBuilderRepositoryNodeManager;
import org.talend.sqlbuilder.util.ConnectionParameters;
import org.talend.sqlbuilder.util.ImageUtil;

/**
 * Detailled comment for this class. <br/>
 * $Id:  DBTreeProvider.java Version 1.0 Nov 6, 2006 2:29:19 AM $
 * @author Hou Peiqin
 * 
 */
public class DBTreeProvider extends LabelProvider implements ITableLabelProvider, ITreeContentProvider,
ITableColorProvider {
    private SQLBuilderRepositoryNodeManager repositoryNodeManager = new SQLBuilderRepositoryNodeManager();
    private RepositoryContentProvider repositoryContentProvider;
    private ConnectionParameters connectionParameters;
    private RepositoryView repositoryView;
    
    public DBTreeProvider(RepositoryView repositoryView, ConnectionParameters connectionParameters) {
        this.connectionParameters = connectionParameters;
        this.repositoryContentProvider = new RepositoryContentProvider(repositoryView);
        this.repositoryView = repositoryView;
    }
    
    public Image getColumnImage(Object element, int columnIndex) {
        if (columnIndex == 1) {
            return null;
        }
        RepositoryNode node = (RepositoryNode) element;
        return ImageUtil.getImage(node.getObject().getPurpose()); 
    }
    
    public String getColumnText(Object element, int columnIndex) {
        RepositoryNode node = (RepositoryNode) element;
        if (columnIndex == 0) {
            return node.getObject().getStatusCode(); 
        } else if (columnIndex == 1) {
            return node.getObject().getLabel();
        }
        
        return null;
    }
    
    public Object[] getChildren(Object parentElement) {
        return repositoryContentProvider.getChildren(parentElement);
    }

    public Object getParent(Object element) {
        return repositoryContentProvider.getParent(element);
    }

    public boolean hasChildren(Object element) {
        return repositoryContentProvider.hasChildren(element);
    }

    public Object[] getElements(Object inputElement) {
        if (!(inputElement instanceof RepositoryNode)) {
            return null;
        }
        RepositoryNode treeRoot = (RepositoryNode) inputElement;
        initialize(treeRoot);
        return treeRoot.getChildren().toArray();
    }

    private void initialize(RepositoryNode treeRoot) {
        if (!connectionParameters.isRepository()) {
            addNode(treeRoot, repositoryNodeManager.getRepositoryNodeByBuildIn(treeRoot, connectionParameters).getObject());
        }
        IRepositoryFactory factory = RepositoryFactoryProvider.getInstance();
        
        try {
            convert(factory.getMetadataConnection(), treeRoot, ERepositoryObjectType.METADATA_CONNECTIONS);
        } catch (Exception e) {
            SqlBuilderPlugin.log(e.getMessage(), e);
        }
    }
    
    private void convert(Container fromModel, RepositoryNode parent, ERepositoryObjectType type) {
        if (fromModel.isEmpty()) {
            return;
        }

        for (Object obj : fromModel.getSubContainer()) {
            Container container = (Container) obj;
            Folder oFolder = new Folder(container.getId(), container.getLabel());
            oFolder.setPurpose("Images.closedFolder");
            oFolder.setStatusCode(oFolder.getLabel());
            oFolder.setLabel(null);
            RepositoryNode folder = new RepositoryNode(oFolder, parent, ENodeType.SIMPLE_FOLDER);
            folder.setProperties(EProperties.LABEL, container.getLabel());
            //ERepositoryObjectType.FOLDER);
            folder.setProperties(EProperties.CONTENT_TYPE, RepositoryNodeType.FOLDER);
            parent.getChildren().add(folder);
            convert(container, folder, type);
        }

        for (Object obj : fromModel.getMembers()) {
            IRepositoryObject repositoryObject = (IRepositoryObject) obj;
            addNode(parent, repositoryObject);
        }
    }
    
    private void addNode(RepositoryNode parent, IRepositoryObject repositoryObject) {
        IRepositoryFactory factory = RepositoryFactoryProvider.getInstance();
        DatabaseConnection connection = (DatabaseConnection) ((ConnectionItem) repositoryObject
                .getProperty().getItem()).getConnection();
        String sid = connection.getSID();
        repositoryObject.setStatusCode((sid == null || sid.trim().equals("")) ? connection.getDatasourceName() : sid);
        repositoryObject.setPurpose("Images.ConnectionIcon");
        RepositoryNode node = new RepositoryNode(repositoryObject, parent, ENodeType.REPOSITORY_ELEMENT);
        
        node.setProperties(EProperties.CONTENT_TYPE, RepositoryNodeType.DATABASE);
        node.setProperties(EProperties.LABEL, repositoryObject.getLabel());
        try {
            if (factory.isDeleted(repositoryObject)) {
                //ignore recycle node
            } else {
                parent.getChildren().add(node);
                repositoryNodeManager.addRepositoryNode(node);
            }
        } catch (PersistenceException e1) {
            e1.printStackTrace();
        }

        DatabaseConnection metadataConnection = (DatabaseConnection) ((ConnectionItem) repositoryObject.getProperty()
                .getItem()).getConnection();
        createTables(node, repositoryObject, metadataConnection);

    }
    

    /**
     * DOC tguiu Comment method "createTables".
     * 
     * @param node
     * @param iMetadataConnection
     * @param metadataConnection
     */
    private void createTables(RepositoryNode node, final IRepositoryObject repObj,
            Connection metadataConnection) {
        for (Object currentTable : metadataConnection.getTables()) {
            org.talend.core.model.metadata.builder.connection.MetadataTable metadataTable 
            = (org.talend.core.model.metadata.builder.connection.MetadataTable) currentTable;
            RepositoryNode tableNode = createMetatable(node, repObj, metadataTable);
            if (TableHelper.isDeleted(metadataTable)) {
                //ignore recycle node
            } else {
                node.getChildren().add(tableNode);
            }
            
            //create columns
            createColumns(tableNode, repObj, currentTable);
        }
    }

    private void createColumns(RepositoryNode tableNode, IRepositoryObject repObj, Object currentTable) {
        for (Object currentColumn : ((MetadataTable) currentTable).getColumns()) {
            MetadataColumn metadataColumn = (MetadataColumn) currentColumn;
            RepositoryNode columnNode = createMetacolumn(tableNode, repObj, metadataColumn);

            tableNode.getChildren().add(columnNode);
        }
    }

    private RepositoryNode createMetacolumn(RepositoryNode tableNode, IRepositoryObject repObj, MetadataColumn metadataColumn) {
        RepositoryObject modelObj = new MetadataColumnRepositoryObject(repObj, metadataColumn);
        modelObj.setLabel(metadataColumn.getLabel());
        //statusCode use for source table name
        modelObj.setStatusCode(metadataColumn.getOriginalField());
        //purpose use for Image text.
        if (metadataColumn.isDivergency()) {
            modelObj.setPurpose("Images.RefreshIcon");
        } else {
            modelObj.setPurpose("Images.ColumnNodeIcon");
        }
        RepositoryNode columnNode = new RepositoryNode(modelObj, tableNode, ENodeType.REPOSITORY_ELEMENT);
        columnNode.setProperties(EProperties.LABEL, metadataColumn.getLabel());
        columnNode.setProperties(EProperties.CONTENT_TYPE, RepositoryNodeType.COLUMN);
        return columnNode;
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
        org.talend.core.model.metadata.MetadataTable modelObj = new MetadataTableRepositoryObject(repObj, table);
        modelObj.setLabel(table.getLabel());
        //statusCode use for source table name
        modelObj.setStatusCode(table.getSourceName());
        //purpose use for Image text.
        modelObj.setPurpose("Images.TableNodeIcon");
        RepositoryNode tableNode = new RepositoryNode(modelObj, node, ENodeType.REPOSITORY_ELEMENT);
        tableNode.setProperties(EProperties.LABEL, table.getLabel());
        tableNode.setProperties(EProperties.CONTENT_TYPE, RepositoryNodeType.TABLE);
        return tableNode;
    }

    /**
     */
    public static class MetadataTableRepositoryObject extends org.talend.core.model.metadata.MetadataTable {

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
    
    /**
     */
    public static class MetadataColumnRepositoryObject extends RepositoryObject {

        private IRepositoryObject repObj;

        private MetadataColumn column;

        public MetadataColumnRepositoryObject(IRepositoryObject repObj, MetadataColumn column) {
            this.repObj = repObj;
            this.column = column;
        }

        public Property getProperty() {
            return repObj.getProperty();
        }

        public Version getVersion() {
            return repObj.getVersion();
        }

        public String getLabel() {
            return column.getLabel();
        }

        public MetadataColumn getColumn() {
            return this.column;
        }
    }
    
    public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
        
    }

    public Color getBackground(Object element, int columnIndex) {
        RepositoryNode repositoryNode = (RepositoryNode) element;
        if (repositoryNode.getProperties(EProperties.CONTENT_TYPE) == RepositoryNodeType.DATABASE) {
            DatabaseConnection connection = (DatabaseConnection) ((ConnectionItem) repositoryNode.getObject()
                    .getProperty().getItem()).getConnection();
            if (connection.isDivergency()) {
                return Display.getDefault().getSystemColor(SWT.COLOR_RED);
            }
        } else if (repositoryNode.getProperties(EProperties.CONTENT_TYPE) == RepositoryNodeType.TABLE) {
            MetadataTableRepositoryObject tableRepositoryObject = (MetadataTableRepositoryObject) repositoryNode.getObject();
            if (tableRepositoryObject.getTable().isDivergency()) {
                return Display.getDefault().getSystemColor(SWT.COLOR_RED);
            }
            if (tableRepositoryObject.getTable() == null) {
                return Display.getDefault().getSystemColor(SWT.COLOR_GRAY);
            }
        } else if (repositoryNode.getProperties(EProperties.CONTENT_TYPE) == RepositoryNodeType.COLUMN) {
            MetadataColumnRepositoryObject columnRepositoryObject = (MetadataColumnRepositoryObject) repositoryNode.getObject();
            if (columnRepositoryObject.getColumn().isDivergency()) {
                return Display.getDefault().getSystemColor(SWT.COLOR_RED);
            }
            if (columnRepositoryObject.getColumn() == null) {
                return Display.getDefault().getSystemColor(SWT.COLOR_GRAY);
            }
        }
        return null;
    }

    public Color getForeground(Object element, int columnIndex) {
        return null;
    }

    
}
