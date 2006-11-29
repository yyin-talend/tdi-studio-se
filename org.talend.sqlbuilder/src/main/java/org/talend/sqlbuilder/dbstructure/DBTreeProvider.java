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

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.emf.common.util.EList;
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
import org.talend.core.model.metadata.builder.connection.Connection;
import org.talend.core.model.metadata.builder.connection.DatabaseConnection;
import org.talend.core.model.metadata.builder.connection.MetadataColumn;
import org.talend.core.model.metadata.builder.connection.MetadataTable;
import org.talend.core.model.metadata.builder.connection.QueriesConnection;
import org.talend.core.model.metadata.builder.connection.Query;
import org.talend.core.model.metadata.builder.connection.TableHelper;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.core.model.properties.Property;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.Folder;
import org.talend.core.model.repository.IRepositoryObject;
import org.talend.repository.model.ProxyRepositoryFactory;
import org.talend.repository.model.RepositoryNode;
import org.talend.repository.model.RepositoryStatus;
import org.talend.repository.model.RepositoryNode.ENodeType;
import org.talend.repository.model.RepositoryNode.EProperties;
import org.talend.repository.ui.views.RepositoryContentProvider;
import org.talend.repository.ui.views.RepositoryView;
import org.talend.sqlbuilder.repository.utility.SQLBuilderRepositoryNodeManager;
import org.talend.sqlbuilder.util.ConnectionParameters;
import org.talend.sqlbuilder.util.ImageUtil;

/**
 * Detailled comment for this class. <br/>
 * $Id:  DBTreeProvider.java Version 1.0 Nov 6, 2006 2:29:19 AM $
 * @author phou
 * 
 */
public class DBTreeProvider extends LabelProvider implements ITableLabelProvider, ITreeContentProvider,
ITableColorProvider {
    private static final String IMAGES_CLOSED_FOLDER = "Images.closedFolder";
	private static final String BUILT_IN = "Built-In";
	private static final String IMAGES_DATABASE_ICON = "Images.DatabaseIcon";
	private static final String IMAGES_CONNECTION_ICON = "Images.ConnectionIcon";
	private static final String IMAGES_APPEND_TO_EDITOR = "Images.AppendToEditor";
	private static final String IMAGES_SQL_EDITOR_ICON = "Images.SqlEditorIcon";
	private static final String IMAGES_TABLE_NODE_ICON = "Images.TableNodeIcon";
	private static final String IMAGES_COLUMN_NODE_ICON = "Images.ColumnNodeIcon";
	private static final String IMAGES_REFRESH_ICON = "Images.RefreshIcon";
	private static final String IMAGES_RED_ICON = "Images.RedIcon";
	private static final String IMAGES_GRAY_ICON = "Images.GrayIcon";
	public static final String COLOR_GRAY = "COLOR_GRAY";
	public static final String COLOR_RED = "COLOR_RED";
	private SQLBuilderRepositoryNodeManager repositoryNodeManager = new SQLBuilderRepositoryNodeManager();
    private RepositoryContentProvider repositoryContentProvider;
    private ConnectionParameters connectionParameters;
    private boolean isRefresh;
    private Map<String, Color> colors = new HashMap<String, Color>(); 
    private Map<String, RepositoryNode> allRepositoryNodes = new HashMap<String, RepositoryNode>();
    
    public DBTreeProvider(RepositoryView repositoryView, ConnectionParameters connectionParameters) {
        this.connectionParameters = connectionParameters;
        this.repositoryContentProvider = new RepositoryContentProvider(repositoryView);
        colors.put(COLOR_RED, Display.getDefault().getSystemColor(SWT.COLOR_RED));
        colors.put(COLOR_GRAY, Display.getDefault().getSystemColor(SWT.COLOR_GRAY));
    }
    
    
    public boolean isRefresh()  {
        return isRefresh;
    }

    public void setRefresh(boolean isRefresh) {
        this.isRefresh = isRefresh;
    }

    public Image getColumnImage(Object element, int columnIndex) {
    	RepositoryNode node = (RepositoryNode) element;
        SqlBuilderRepositoryObject repositoryObject = (SqlBuilderRepositoryObject) node.getObject();
		if (columnIndex == 1) {
            return null;
        } else if (columnIndex == 2) {
        	if ((repositoryObject).getDiffImage() == null) {
        		return null;
        	}
        	return ImageUtil.getImage((repositoryObject).getDiffImage());
        }
        return ImageUtil.getImage((repositoryObject).getImage()); 
    }
    
    public String getColumnText(Object element, int columnIndex) {
        RepositoryNode node = (RepositoryNode) element;
        if (columnIndex == 0) {
            return ((SqlBuilderRepositoryObject) node.getObject()).getSourceName(); 
        } else if (columnIndex == 1) {
            return ((SqlBuilderRepositoryObject) node.getObject()).getRepositoryName();
        }
        
        return null;
    }
    
    @SuppressWarnings("static-access")
    public Object[] getChildren(Object parentElement) {
        if (isRefresh) {
            RepositoryNode repositoryNode = (RepositoryNode) parentElement;
            RepositoryNode rootNode = repositoryNodeManager.getRoot(repositoryNode);
            refreshRootNode(rootNode);
            rootNode.getChildren().clear();
            DatabaseConnection metadataConnection = (DatabaseConnection) ((ConnectionItem) repositoryNode.getObject().getProperty()
                    .getItem()).getConnection();
            boolean isBuildIn = ((SqlBuilderRepositoryObject) rootNode.getObject()).isBuildIn();
			createTables(rootNode, rootNode.getObject(), metadataConnection, 
            		isBuildIn);
            createQueries(rootNode, rootNode.getObject(), metadataConnection, isBuildIn);
            isRefresh = false;
            return repositoryNode.getChildren().toArray();
        	
        } else {
            return ((RepositoryNode) parentElement).getChildren().toArray();
        }
    }
    
    public void refreshRootNode(RepositoryNode rootNode) {
    	SqlBuilderRepositoryObject repositoryObject = (SqlBuilderRepositoryObject) rootNode.getObject();
        boolean[] isDiffs = repositoryNodeManager.isDiff(rootNode);
        if (isDiffs[0]) {
        	repositoryObject.setDiffImage(IMAGES_GRAY_ICON);
        }
        if (isDiffs[1]) {
        	repositoryObject.setDiffImage(IMAGES_RED_ICON);
        }
        if (isDiffs[2]) {
        	repositoryObject.setDiffImage(IMAGES_REFRESH_ICON);
        }
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
            addNode(treeRoot, repositoryNodeManager.getRepositoryNodeByBuildIn(treeRoot, connectionParameters).getObject(), true, null);
        }
        Container metadataConnection = getMetadataConnection();
        convert(metadataConnection, treeRoot, ERepositoryObjectType.METADATA_CONNECTIONS);
    }
    
    /**
     * @return MetadataConnection
     */
    private Container getMetadataConnection() {
        ProxyRepositoryFactory factory = ProxyRepositoryFactory.getInstance();
    	Container container = null;
		try {
			container = factory.getMetadataConnection();
		} catch (PersistenceException e) {
			e.printStackTrace();
		}
    	return container;
    }
    
    private void convert(Container fromModel, RepositoryNode parent, ERepositoryObjectType type) {
        if (fromModel.isEmpty()) {
            return;
        }

        for (Object obj : fromModel.getSubContainer()) {
            Container container = (Container) obj;
            Folder folderRepositoryObject = new Folder(container.getId(), container.getLabel());
            FolderRepositoryObject oFolder = new FolderRepositoryObject(folderRepositoryObject);
            oFolder.setImage(IMAGES_CLOSED_FOLDER);
            oFolder.setSourceName(oFolder.getLabel());
            oFolder.setRepositoryName(null);
            RepositoryNode folder = new RepositoryNode(oFolder, parent, ENodeType.SIMPLE_FOLDER);
            folder.setProperties(EProperties.LABEL, container.getLabel());
            //ERepositoryObjectType.FOLDER);
            folder.setProperties(EProperties.CONTENT_TYPE, RepositoryNodeType.FOLDER);
            parent.getChildren().add(folder);
            convert(container, folder, type);
        }

        for (Object obj : fromModel.getMembers()) {
            IRepositoryObject repositoryObject = (IRepositoryObject) obj;
            addNode(parent, repositoryObject, false, null);
        }
    }
    
    private void addNode(RepositoryNode parent, IRepositoryObject repositoryObject, boolean isBuildIn, Integer index) {
        ProxyRepositoryFactory factory = ProxyRepositoryFactory.getInstance();
        DatabaseConnection connection = (DatabaseConnection) ((ConnectionItem) repositoryObject
                .getProperty().getItem()).getConnection();
        String sid = connection.getSID();
        MetadataConnectionRepositoryObject  connectionRepositoryObject = new MetadataConnectionRepositoryObject(repositoryObject);
        if (isBuildIn) {
        	connectionRepositoryObject.setRepositoryName(BUILT_IN);
        } else {
        	connectionRepositoryObject.setRepositoryName(repositoryObject.getLabel());
        }
        connectionRepositoryObject.setSourceName((sid == null || sid.trim().equals("")) ? connection.getDatasourceName() : sid);
        if (!isBuildIn) {
        connectionRepositoryObject.setImage(IMAGES_CONNECTION_ICON);
        } else {
        	connectionRepositoryObject.setImage(IMAGES_DATABASE_ICON);
        }
        connectionRepositoryObject.setBuildIn(isBuildIn);
        
        RepositoryNode node = new RepositoryNode(connectionRepositoryObject, parent, ENodeType.REPOSITORY_ELEMENT);
        node.setProperties(EProperties.CONTENT_TYPE, RepositoryNodeType.DATABASE);
        node.setProperties(EProperties.LABEL, repositoryObject.getLabel());
        boolean[] isDiffs = repositoryNodeManager.isDiff(node);
        if (isDiffs[0]) {
        	connectionRepositoryObject.setDiffImage(IMAGES_GRAY_ICON);
        }
        if (isDiffs[1]) {
        	connectionRepositoryObject.setDiffImage(IMAGES_RED_ICON);
        }
        if (isDiffs[2]) {
            connectionRepositoryObject.setDiffImage(IMAGES_REFRESH_ICON);
        }

        if (factory.getStatus(repositoryObject) == RepositoryStatus.DELETED) {
            // ignore recycle node
        } else {
            if (index == null) {
                parent.getChildren().add(node);
            } else {
                parent.getChildren().add(index.intValue(), node);
            }
            allRepositoryNodes.put(node.getObject().getLabel(), node);
            repositoryNodeManager.addRepositoryNode(node);
        }

        DatabaseConnection metadataConnection = (DatabaseConnection) ((ConnectionItem) repositoryObject.getProperty().getItem())
                .getConnection();
        createTables(node, repositoryObject, metadataConnection, isBuildIn);

        createQueries(node, repositoryObject, metadataConnection, isBuildIn);
    }
    

    private void createQueries(RepositoryNode node, final IRepositoryObject repObj,
            DatabaseConnection metadataConnection, boolean isBuildIn) {
        EList queryConnections = metadataConnection.getQueries();
        
        for (Iterator iter = queryConnections.iterator(); iter.hasNext();) {
            QueriesConnection queriesConnection = (QueriesConnection) iter.next();
            QueriesConnectionRepositoryObject repositoryObject 
            	= new QueriesConnectionRepositoryObject(repObj, queriesConnection); 
            repositoryObject.setImage(IMAGES_APPEND_TO_EDITOR);
            repositoryObject.setSourceName("Stored Queries");
            RepositoryNode queriesConnectionNode = new RepositoryNode(repositoryObject, node, ENodeType.REPOSITORY_ELEMENT);
            queriesConnectionNode.setProperties(EProperties.CONTENT_TYPE, RepositoryNodeType.QUERIESCONNECTION);
            node.getChildren().add(queriesConnectionNode);
            allRepositoryNodes.put(queriesConnectionNode.getObject().getLabel(), queriesConnectionNode);
            createQuery(queriesConnectionNode, repObj, queriesConnection);
        }
    }

    private void createQuery(RepositoryNode queriesConnectionNode, IRepositoryObject repObj, QueriesConnection queriesConnection) {
        for (Iterator iter = queriesConnection.getQuery().iterator(); iter.hasNext();) {
        	Query query = (Query) iter.next();
        	QueryRepositoryObject repositoryObject = new QueryRepositoryObject(repObj, query);
        	repositoryObject.setImage(IMAGES_SQL_EDITOR_ICON);
        	repositoryObject.setSourceName(query.getLabel());
        	RepositoryNode node = new RepositoryNode(repositoryObject, queriesConnectionNode, ENodeType.REPOSITORY_ELEMENT);
        	node.setProperties(EProperties.CONTENT_TYPE, RepositoryNodeType.QUERY);
        	queriesConnectionNode.getChildren().add(node);
        	allRepositoryNodes.put(node.getObject().getLabel(), node);
        }
    }


    /**
     * DOC tguiu Comment method "createTables".
     * 
     * @param node
     * @param iMetadataConnection
     * @param metadataConnection
     */
    private void createTables(RepositoryNode node, final IRepositoryObject repObj,
            Connection metadataConnection, boolean isBuildIn) {
        for (Object currentTable : metadataConnection.getTables()) {
            org.talend.core.model.metadata.builder.connection.MetadataTable metadataTable 
            = (org.talend.core.model.metadata.builder.connection.MetadataTable) currentTable;
            RepositoryNode tableNode = createMetatable(node, repObj, metadataTable, isBuildIn);
            if (TableHelper.isDeleted(metadataTable)) {
                //ignore recycle node
            } else {
                node.getChildren().add(tableNode);
                allRepositoryNodes.put(tableNode.getObject().getLabel(), tableNode);
            }
            
            //create columns
            createColumns(tableNode, repObj, currentTable, isBuildIn);
        }
    }

    private void createColumns(RepositoryNode tableNode, IRepositoryObject repObj, Object currentTable, boolean isBuildIn) {
        for (Object currentColumn : ((MetadataTable) currentTable).getColumns()) {
            MetadataColumn metadataColumn = (MetadataColumn) currentColumn;
            RepositoryNode columnNode = createMetacolumn(tableNode, repObj, metadataColumn, isBuildIn);

            tableNode.getChildren().add(columnNode);
            allRepositoryNodes.put(columnNode.getObject().getLabel(), columnNode);
        }
    }

    private RepositoryNode createMetacolumn(RepositoryNode tableNode, IRepositoryObject repObj, 
    		MetadataColumn metadataColumn, boolean isBuildIn) {
    	MetadataColumnRepositoryObject modelObj = new MetadataColumnRepositoryObject(repObj, metadataColumn);
        modelObj.setRepositoryName(metadataColumn.getLabel());
        //statusCode use for source table name
        modelObj.setSourceName(metadataColumn.getOriginalField());
        //purpose use for Image text.
        if (metadataColumn.isSynchronised()) {
            modelObj.setImage(IMAGES_REFRESH_ICON);
        } else {
            modelObj.setImage(IMAGES_COLUMN_NODE_ICON);
        }
        //description use for color.
        if (modelObj.getColumn().isDivergency() && !isBuildIn) {
        	modelObj.setColor(COLOR_RED);
        }
        if (modelObj.getRepositoryName() == null || modelObj.getRepositoryName().trim().equals("")) {
        	modelObj.setColor(COLOR_GRAY);
        }
        modelObj.setBuildIn(isBuildIn);

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
            final org.talend.core.model.metadata.builder.connection.MetadataTable table, boolean isBuildIn) {
    	MetadataTableRepositoryObject modelObj = new MetadataTableRepositoryObject(repObj, table);
        modelObj.setRepositoryName(table.getLabel());
        //statusCode use for source table name
        modelObj.setSourceName(table.getSourceName());
        //purpose use for Image text.
        modelObj.setImage(IMAGES_TABLE_NODE_ICON);
        //description use for color.
        if (modelObj.getTable().isDivergency() && !isBuildIn && !modelObj.getTable().isSynchronised()) {
        	modelObj.setColor(COLOR_RED);
        }
        if (modelObj.getRepositoryName() == null || modelObj.getRepositoryName().trim().equals("")) {
        	modelObj.setColor(COLOR_GRAY);
        }
        modelObj.setBuildIn(isBuildIn);
        
        RepositoryNode tableNode = new RepositoryNode(modelObj, node, ENodeType.REPOSITORY_ELEMENT);
        tableNode.setProperties(EProperties.LABEL, table.getLabel());
        tableNode.setProperties(EProperties.CONTENT_TYPE, RepositoryNodeType.TABLE);
        
        boolean[] isDiffs = repositoryNodeManager.isDiff(tableNode);
        if (isDiffs[0]) {
        	modelObj.setDiffImage(IMAGES_GRAY_ICON);
        }
        if (isDiffs[1]) {
        	modelObj.setDiffImage(IMAGES_RED_ICON);
        }
        if (isDiffs[2]) {
        	modelObj.setDiffImage(IMAGES_REFRESH_ICON);
        }

        
        return tableNode;
    }
    
    /**
     */
    public static class QueriesConnectionRepositoryObject extends SqlBuilderRepositoryObject {

        private IRepositoryObject repObj;
        private QueriesConnection queriesConnection;

        public QueriesConnectionRepositoryObject(IRepositoryObject repObj, QueriesConnection queriesConnection) {
        	super(repObj.getProperty());
            this.repObj = repObj;
            this.queriesConnection = queriesConnection;
        }

        public Property getProperty() {
            return repObj.getProperty();
        }

        public String getLabel() {
            return repObj.getLabel();
        }
        
        public QueriesConnection getQueriesConnection() {
        	return this.queriesConnection;
        }
    }
    
    /**
     */
    public static class QueryRepositoryObject extends SqlBuilderRepositoryObject {

        private IRepositoryObject repObj;

        private Query query;

        public QueryRepositoryObject(IRepositoryObject repObj,
                Query query) {
        	super(repObj.getProperty());
        	this.repObj = repObj;
            this.query = query;
        }

        public Property getProperty() {
            return repObj.getProperty();
        }
        public Query getQuery() {
            return this.query;
        }
    }
    /**
     */
    public static class FolderRepositoryObject extends SqlBuilderRepositoryObject {
    	
    	private IRepositoryObject repObj;
    	
        public FolderRepositoryObject(IRepositoryObject repObj) {
        	super(null);
        	this.repObj = repObj;
        }

        public Property getProperty() {
            return repObj.getProperty();
        }
        
        @Override
        public String getId() {
        	return repObj.getId();
        }
        @Override
        public String getLabel() {
        	return repObj.getLabel();
        }
    }
    
    /**
     */
    public static class MetadataConnectionRepositoryObject extends SqlBuilderRepositoryObject {

        private IRepositoryObject repObj;

        public MetadataConnectionRepositoryObject(IRepositoryObject repObj) {
        	super(repObj.getProperty());
        	this.repObj = repObj;
        }

        public Property getProperty() {
            return repObj.getProperty();
        }
        
        public DatabaseConnection getConnection() {
        	DatabaseConnection metadataConnection = (DatabaseConnection) ((ConnectionItem) repObj.getProperty()
                    .getItem()).getConnection();
        	
        	return metadataConnection;
        }
    }
    
    /**
     */
    public static class MetadataTableRepositoryObject extends SqlBuilderRepositoryObject {

        private IRepositoryObject repObj;

        private org.talend.core.model.metadata.builder.connection.MetadataTable table;

        public MetadataTableRepositoryObject(IRepositoryObject repObj,
                org.talend.core.model.metadata.builder.connection.MetadataTable table) {
        	super(repObj.getProperty());
        	this.repObj = repObj;
            this.table = table;
        }

        public Property getProperty() {
            return repObj.getProperty();
        }
        public org.talend.core.model.metadata.builder.connection.MetadataTable getTable() {
            return this.table;
        }
    }
    

    /**
     */
    public static class MetadataColumnRepositoryObject extends SqlBuilderRepositoryObject {

        private IRepositoryObject repObj;

        private MetadataColumn column;

        public MetadataColumnRepositoryObject(IRepositoryObject repObj, MetadataColumn column) {
        	super(repObj.getProperty());
            this.repObj = repObj;
            this.column = column;
        }
        public Property getProperty() {
            return repObj.getProperty();
        }
        public MetadataColumn getColumn() {
            return this.column;
        }
    }
    
    public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
        
    }

    public Color getBackground(Object element, int columnIndex) {
    	RepositoryNode repositoryNode = (RepositoryNode) element;
    	SqlBuilderRepositoryObject repositoryObject = (SqlBuilderRepositoryObject) repositoryNode.getObject();
    	if (columnIndex == 2 ) {
    		return null;
    	} else {
			return colors.get((repositoryObject).getColor());
    	}
    	        
    }

    public Color getForeground(Object element, int columnIndex) {
        return null;
    }
    
}
