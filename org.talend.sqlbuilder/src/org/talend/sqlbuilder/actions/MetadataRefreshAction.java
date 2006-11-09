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
package org.talend.sqlbuilder.actions;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.ui.actions.SelectionProviderAction;
import org.talend.commons.exception.PersistenceException;
import org.talend.core.model.metadata.IMetadataConnection;
import org.talend.core.model.metadata.builder.ConvertionHelper;
import org.talend.core.model.metadata.builder.connection.DatabaseConnection;
import org.talend.core.model.metadata.builder.connection.MetadataColumn;
import org.talend.core.model.metadata.builder.connection.MetadataTable;
import org.talend.core.model.metadata.builder.connection.TableHelper;
import org.talend.core.model.metadata.builder.database.ExtractMetaDataFromDataBase;
import org.talend.core.model.properties.DatabaseConnectionItem;
import org.talend.core.model.repository.IRepositoryObject;
import org.talend.core.ui.ImageProvider;
import org.talend.core.ui.ImageProvider.EImage;
import org.talend.repository.model.IRepositoryFactory;
import org.talend.repository.model.RepositoryFactoryProvider;
import org.talend.repository.model.RepositoryNode;
import org.talend.sqlbuilder.dbstructure.nodes.ColumnNode;
import org.talend.sqlbuilder.dbstructure.nodes.TableNode;
import org.talend.sqlbuilder.sessiontree.model.SessionTreeNode;

/**
 * DOC dev  class global comment. Detailled comment
 * <br/>
 *
 * $Id: MetadataRefreshAction.java,v 1.22 2006/11/09 01:21:53 qiang.zhang Exp $
 *
 */
public class MetadataRefreshAction  extends SelectionProviderAction {
    private ImageDescriptor img = ImageProvider.getImageDesc(EImage.REFRESH_ICON);
    private ISelectionProvider selectionProvider;
    private List<ColumnNode> columnNodes;
    
    /**
     * DOC dev MetadataRefreshAction constructor comment.
     * @param selectionProvider
     */
    public MetadataRefreshAction(ISelectionProvider selectionProvider) {
        super(selectionProvider, "");
        this.selectionProvider = selectionProvider;
        init();
    }

    /* (non-Javadoc)
     * @see org.eclipse.ui.actions.SelectionProviderAction#selectionChanged(org.eclipse.jface.viewers.IStructuredSelection)
     */
    @Override
    public void selectionChanged(IStructuredSelection selection) {
        init();
    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.action.Action#run()
     * 
     */
    @Override
    public void run() {
        for (ColumnNode columnNode : columnNodes) {
            TableNode tableNode = (TableNode) columnNode.getParent();
            SessionTreeNode session = tableNode.getSession();
            RepositoryNode repositorynode = session.getRepositoryNode();
            if (repositorynode != null) {
                IRepositoryObject repositoryObject = repositorynode.getObject();
                DatabaseConnectionItem item = (DatabaseConnectionItem) repositoryObject.getProperty().getItem();
                saveMetadataColumn(tableNode, columnNode, item);
            }
            
        }
        
    }
    /**
     * DOC dev Comment method "saveMetadataColumn".
     * @param tableNode columNode's parent.
     * @param columnNode selected columnNode
     * @param item selected DatabaseConnectionItem
     */
    private void saveMetadataColumn(TableNode tableNode, ColumnNode columnNode
            , DatabaseConnectionItem item) {
        modifyMetadataColumn(tableNode, columnNode, item);
        saveMetaData(item);
        columnNode.getParent().refresh();
        ((TreeViewer) selectionProvider).refresh(columnNode.getParent());
    }
    /**
     * 
     * DOC dev Comment method "modifyMetadataColumn".
     * @param tableNode columNode's parent.
     * @param columnNode selected columnNode
     * @param item selected DatabaseConnectionItem
     */
    private void modifyMetadataColumn(TableNode tableNode, ColumnNode columnNode,
            DatabaseConnectionItem item) {
        IMetadataConnection iMetadataConnection = ConvertionHelper.convert((DatabaseConnection) item.getConnection());
        List<MetadataColumn> metadataColumns = new ArrayList<MetadataColumn>();
        metadataColumns = ExtractMetaDataFromDataBase
        .returnMetadataColumnsFormTable(iMetadataConnection, tableNode.getLabelText().replace("-", "_"));
        Iterator iterate = metadataColumns.iterator();
        while (iterate.hasNext()) {
            MetadataColumn metadataColumn = (MetadataColumn) iterate.next();
            if (metadataColumn.getLabel().equals(columnNode.getLabelText())) {
                EList columns = getRepositoryMetadataColumns(tableNode, (DatabaseConnection) item.getConnection()); 
                for (int i = 0, size = columns.size(); i < size; i++) {
                    MetadataColumn column = (MetadataColumn) columns.get(i);
                    if (column.getLabel().equals(columnNode.getLabelText())) {
                        column.setComment(metadataColumn.getComment());
                        column.setDefaultValue(metadataColumn.getDefaultValue());
                        column.setKey(metadataColumn.isKey());
                        column.setLength(metadataColumn.getLength());
                        column.setNullable(metadataColumn.isNullable());
                        column.setPrecision(metadataColumn.getPrecision());
                        column.setSourceType(metadataColumn.getSourceType());
                        column.setTalendType(metadataColumn.getTalendType());
                        column.setDivergency(true);
                    }
                }
            }
        }
        
    }
    
    /**
     * DOC dev Comment method "getRepositoryMetadataColumns".
     * @param tableNode
     * @param connection
     * @return
     */
    @SuppressWarnings({ "unchecked", "deprecation" })
    private EList getRepositoryMetadataColumns(TableNode tableNode, DatabaseConnection connection){
        String metadataTableLabel = (String) tableNode.getRepositoryName();
        MetadataTable metadataTable =  TableHelper.findByLabel(connection, metadataTableLabel);
        return metadataTable.getColumns();
    }
    /**
     * DOC dev Comment method "saveMetaData".
     * @param item
     */
    private void saveMetaData(DatabaseConnectionItem item) {
        IRepositoryFactory factory = RepositoryFactoryProvider.getInstance();
        try {
            factory.save(item);
        } catch (PersistenceException e) {
            e.printStackTrace();
        }
    }
    /* (non-Javadoc)
     * @see org.eclipse.jface.action.Action#getImageDescriptor()
     */
    @Override
    public ImageDescriptor getImageDescriptor() {
        return img;
    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.action.Action#getHoverImageDescriptor()
     */
    @Override
    public ImageDescriptor getHoverImageDescriptor() {
        return img;
    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.action.Action#getText()
     */
    @Override
    public String getText() {
        return "Synchronize";
    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.action.Action#getToolTipText()
     */
    @Override
    public String getToolTipText() {
        return "Synchronize";
    }

    /**
     * DOC dev Comment method "init".
     */
    public void init() {
        
        IStructuredSelection selection = (IStructuredSelection) selectionProvider.getSelection();
        columnNodes = new ArrayList<ColumnNode>();
        boolean flag = true;
        for (Object object : selection.toList()) {
            if (!(object instanceof ColumnNode)) {
                this.setEnabled(false);
                return  ;
            }
            ColumnNode col = (ColumnNode) object;
            if (!col.isSameToColumn()) {
                flag = false;
                columnNodes.add(col);
            }
        }
        if (flag) {
            this.setEnabled(false);
            return  ;
        }
    }
}
