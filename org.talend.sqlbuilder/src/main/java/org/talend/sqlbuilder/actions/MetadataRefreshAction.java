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

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.actions.SelectionProviderAction;
import org.talend.commons.exception.PersistenceException;
import org.talend.core.model.metadata.IMetadataConnection;
import org.talend.core.model.metadata.builder.ConvertionHelper;
import org.talend.core.model.metadata.builder.connection.DatabaseConnection;
import org.talend.core.model.metadata.builder.connection.MetadataColumn;
import org.talend.core.model.metadata.builder.connection.MetadataTable;
import org.talend.core.model.metadata.builder.database.ExtractMetaDataFromDataBase;
import org.talend.core.model.properties.DatabaseConnectionItem;
import org.talend.core.model.repository.IRepositoryObject;
import org.talend.core.ui.EImage;
import org.talend.core.ui.ImageProvider;
import org.talend.repository.model.ProxyRepositoryFactory;
import org.talend.repository.model.RepositoryNode;
import org.talend.repository.model.RepositoryNode.EProperties;
import org.talend.sqlbuilder.dbstructure.DBTreeProvider;
import org.talend.sqlbuilder.dbstructure.RepositoryNodeType;
import org.talend.sqlbuilder.repository.utility.SQLBuilderRepositoryNodeManager;
import org.talend.sqlbuilder.ui.ISQLBuilderDialog;

/**
 * DOC dev class global comment. Detailled comment <br/>
 * 
 * $Id: MetadataRefreshAction.java,v 1.22 2006/11/09 01:21:53 qiang.zhang Exp $
 * 
 */
public class MetadataRefreshAction extends SelectionProviderAction {
	private ImageDescriptor img = ImageProvider
			.getImageDesc(EImage.REFRESH_ICON);

	private ISelectionProvider selectionProvider;

	private List<MetadataColumn> columnNodes;

	private List<RepositoryNode> repositorynodes;
	
	private ISQLBuilderDialog dialog;
	
	/**
	 * DOC dev MetadataRefreshAction constructor comment.
	 * 
	 * @param selectionProvider
	 */
	public MetadataRefreshAction(ISelectionProvider selectionProvider, ISQLBuilderDialog d) {
		super(selectionProvider, "");
		this.selectionProvider = selectionProvider;
		columnNodes = new ArrayList<MetadataColumn>();
		repositorynodes = new ArrayList<RepositoryNode>();
		this.dialog = d;
		init();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.actions.SelectionProviderAction#selectionChanged(org.eclipse.jface.viewers.IStructuredSelection)
	 */
	@Override
	public void selectionChanged(IStructuredSelection selection) {
		init();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.action.Action#run()
	 * 
	 */
	@Override
	public void run() {
		DatabaseConnectionItem item = null;
		if (repositorynodes.get(0) != null) {
			item = getItem(repositorynodes.get(0));
		}

		for (MetadataColumn columnNode : columnNodes) {
			MetadataTable tableNode = (MetadataTable) columnNode.getTable();
			saveMetadataColumn(tableNode, columnNode, item);
		}

	}

	/**
	 * DOC dev Comment method "getItem".
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private DatabaseConnectionItem getItem(RepositoryNode node) {
		IRepositoryObject repositoryObject = node.getObject();
		DatabaseConnectionItem item = (DatabaseConnectionItem) repositoryObject
				.getProperty().getItem();
		return item;
	}

	/**
	 * DOC dev Comment method "reductionConnection".
	 * @param connection
	 */
	@SuppressWarnings("unchecked")
	private void reductionConnection(DatabaseConnection connection) {
		SQLBuilderRepositoryNodeManager.reductionOneConnection(connection);
	}

	/**
	 * DOC dev Comment method "saveMetadataColumn".
	 * 
	 * @param tableNode
	 *            columNode's parent.
	 * @param columnNode
	 *            selected columnNode
	 * @param item
	 *            selected DatabaseConnectionItem
	 */
	@SuppressWarnings({ "static-access", "unchecked" })
	private void saveMetadataColumn(MetadataTable tableNode,
			MetadataColumn columnNode, DatabaseConnectionItem item) {
		DatabaseConnection connection = (DatabaseConnection) item
		.getConnection();
		modifyMetadataColumn(tableNode, columnNode, connection);
		saveMetaData(item);
		SQLBuilderRepositoryNodeManager.increaseOneConnection(connection);
		for (RepositoryNode repositorynode : repositorynodes) {
			dialog.refreshNode(repositorynode);
		}
	}

	/**
	 * 
	 * DOC dev Comment method "modifyMetadataColumn".
	 * 
	 * @param tableNode
	 *            columNode's parent.
	 * @param columnNode
	 *            selected columnNode
	 * @param connection
	 *            selected DatabaseConnection
	 */
	@SuppressWarnings("unchecked")
	private void modifyMetadataColumn(MetadataTable tableNode,
			MetadataColumn columnNode, DatabaseConnection connection) {
		IMetadataConnection iMetadataConnection = ConvertionHelper
				.convert(connection);
		List<MetadataColumn> metadataColumns = new ArrayList<MetadataColumn>();
		metadataColumns = ExtractMetaDataFromDataBase
				.returnMetadataColumnsFormTable(iMetadataConnection, tableNode
						.getSourceName());
		Iterator iterate = metadataColumns.iterator();
		while (iterate.hasNext()) {
			MetadataColumn metadataColumn = (MetadataColumn) iterate.next();
			if (metadataColumn.getLabel().equals(columnNode.getOriginalField())) {
				if (columnNode.getLabel().equals("")) {
					columnNode.setLabel(columnNode.getOriginalField());
				}
				columnNode.setComment(metadataColumn.getComment());
				columnNode.setDefaultValue(metadataColumn.getDefaultValue());
				columnNode.setKey(metadataColumn.isKey());
				columnNode.setLength(metadataColumn.getLength());
				columnNode.setNullable(metadataColumn.isNullable());
				columnNode.setPrecision(metadataColumn.getPrecision());
				columnNode.setSourceType(metadataColumn.getSourceType());
				columnNode.setTalendType(metadataColumn.getTalendType());
				columnNode.setDivergency(false);
				columnNode.setSynchronised(false);
				break;
			}
		}
//		List<MetadataColumn> columns = tableNode.getColumns();
//		boolean flag = false;
//		for (MetadataColumn column : columns) {
//			if (column.isDivergency()) {
//				flag = true;
//			}
//		}
//		tableNode.setDivergency(flag);
//		flag = false;
//		List<MetadataTable> tables = connection.getTables();
//		for (MetadataTable table : tables) {
//			if (table.isDivergency()) {
//				flag = true;
//			}
//		}
//		connection.setDivergency(flag);
		reductionConnection(connection);
	}

	/**
	 * DOC dev Comment method "saveMetaData".
	 * 
	 * @param item
	 */
	private void saveMetaData(DatabaseConnectionItem item) {
        ProxyRepositoryFactory factory = ProxyRepositoryFactory.getInstance();
		try {
			factory.save(item);
		} catch (PersistenceException e) {
			e.printStackTrace();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.action.Action#getImageDescriptor()
	 */
	@Override
	public ImageDescriptor getImageDescriptor() {
		return img;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.action.Action#getHoverImageDescriptor()
	 */
	@Override
	public ImageDescriptor getHoverImageDescriptor() {
		return img;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.action.Action#getText()
	 */
	@Override
	public String getText() {
		return "Synchronize";
	}

	/*
	 * (non-Javadoc)
	 * 
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

		IStructuredSelection selection = (IStructuredSelection) selectionProvider
				.getSelection();

		repositorynodes.clear();
		columnNodes.clear();
		boolean flag = true;
		for (Object object : selection.toList()) {
			if (!((RepositoryNode) object).getProperties(
					EProperties.CONTENT_TYPE).equals(RepositoryNodeType.COLUMN)) {
				this.setEnabled(false);
				return;
			}

			MetadataColumn col = ((DBTreeProvider.MetadataColumnRepositoryObject) ((RepositoryNode) object)
					.getObject()).getColumn();
			if (col.getLabel().equals("")) {
				this.setEnabled(false);
				return;
			}
			if (col.isSynchronised()) {

				if (!repositorynodes.isEmpty() && repositorynodes.get(0) != null
						&& !SQLBuilderRepositoryNodeManager.getRoot(repositorynodes.get(0))
							.equals(SQLBuilderRepositoryNodeManager.getRoot(((RepositoryNode) object)))) {
					this.setEnabled(false);
					return;
				}

				repositorynodes.add((RepositoryNode) object);

				this.setEnabled(true);
				flag = false;
				columnNodes.add(col);
			}
		}
		if (flag) {
			this.setEnabled(false);
			return;
		}
	}
}
