// ============================================================================
//
// Talend Community Edition
//
// Copyright (C) 2006-2007 Talend - www.talend.com
//
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either
// version 2.1 of the License.
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
package org.talend.sqlbuilder.ui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.ui.actions.SelectionProviderAction;
import org.talend.core.model.metadata.IMetadataColumn;
import org.talend.core.model.metadata.IMetadataConnection;
import org.talend.core.model.metadata.IMetadataTable;
import org.talend.core.model.metadata.MetadataColumn;
import org.talend.core.model.metadata.MetadataConnection;
import org.talend.core.model.metadata.MetadataTable;
import org.talend.core.model.metadata.QueryUtil;
import org.talend.core.model.utils.TalendTextUtils;
import org.talend.sqlbuilder.Messages;
import org.talend.sqlbuilder.util.ConnectionParameters;
import org.talend.sqlbuilder.util.ImageUtil;
import org.talend.sqlbuilder.util.TextUtil;

/**
 * qzhang class global comment. Detailled comment <br/>
 * 
 * $Id: talend-code-templates.xml 1 2007-3-20 下午05:42:58 (星期五, 29 九月 2006)
 * qzhang $
 * 
 */
public class BuildInDBStructure extends SashForm {

	private Image tableImage = ImageUtil.getImage("Images.TableNodeIcon");

	private Image columnImage = ImageUtil.getImage("Images.ColumnNodeIcon");

	private SQLBuilderDialog dialog;

	private DBStructureComposite dbstructureCom;

	private TreeViewer treeViewer;

	private IMetadataTable metadataTable;

	private IMetadataConnection parentMetadata;

	private String schema = QueryUtil.DEFAULT_TABLE_NAME;

	private ConnectionParameters connectionParameters;

	/**
	 * qzhang BuildInDBStructure constructor comment.
	 */
	public BuildInDBStructure(Composite parent, int style,
			SQLBuilderDialog dialog, ConnectionParameters connectionParameters) {
		super(parent, style);
		this.dialog = dialog;
		this.metadataTable = connectionParameters.getMetadataTable();
		this.connectionParameters = connectionParameters;
		schema = (connectionParameters.getSchemaName() != null && !connectionParameters
				.getSchemaName().equals("")) ? connectionParameters
				.getSchemaName() : schema;
		addCompnoents();
		setWeights(new int[] { 1, 2 });
	}

	/**
	 * qzhang Comment method "addCompnoents".
	 */
	private void addCompnoents() {
		createCurrentSchemaComposite();
		dbstructureCom = new DBStructureComposite(this, SWT.BORDER, dialog);
	}

	/**
	 * qzhang Comment method "createCurrentSchemaComposite".
	 */
	private void createCurrentSchemaComposite() {
		Composite curShemaComposite = new Composite(this, SWT.BORDER);
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 1;
		gridLayout.marginBottom = 0;
		gridLayout.marginHeight = 0;
		gridLayout.marginLeft = 0;
		gridLayout.marginRight = 0;
		gridLayout.marginTop = 0;
		gridLayout.marginWidth = 0;
		gridLayout.verticalSpacing = 0;
		curShemaComposite.setLayout(gridLayout);
		GridData gridData = new GridData(GridData.FILL_BOTH);
		curShemaComposite.setLayoutData(gridData);

		treeViewer = new TreeViewer(curShemaComposite, SWT.MULTI | SWT.H_SCROLL
				| SWT.V_SCROLL | SWT.FULL_SELECTION);
		treeViewer.getControl().setLayoutData(new GridData(GridData.FILL_BOTH));
		treeViewer.setUseHashlookup(true);
		Tree tree = treeViewer.getTree();
		tree.setHeaderVisible(true);
		TreeColumn treeColumn = new TreeColumn(tree, SWT.LEFT);
		treeColumn.setText("Current Schema");
		treeColumn.setWidth(300);
		SchemaTreePrivder schemaTreePrivder = new SchemaTreePrivder();
		treeViewer.setContentProvider(schemaTreePrivder);
		treeViewer.setLabelProvider(schemaTreePrivder);
		parentMetadata = new MetadataConnection();
		List<IMetadataTable> tables = new ArrayList<IMetadataTable>();
		if (metadataTable == null) {
			metadataTable = new MetadataTable();
			metadataTable.setListColumns(new ArrayList<IMetadataColumn>());
		}
		tables.add(metadataTable);
		parentMetadata.setListTables(tables);
		treeViewer.setInput(parentMetadata);
		generateSelectAction = new GenerateSqlAction(treeViewer);
		addContextMenu();
	}

	/**
	 * qzhang Comment method "addContextMenu".
	 */
	private void addContextMenu() {
		MenuManager menuMgr = new MenuManager("Menu"); //$NON-NLS-1$
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(new IMenuListener() {

			public void menuAboutToShow(IMenuManager manager) {
				manager.add(generateSelectAction);
			}

		});
		Menu contextMenu = menuMgr.createContextMenu(treeViewer.getTree());
		treeViewer.getTree().setMenu(contextMenu);
	}

	private GenerateSqlAction generateSelectAction;

	/**
	 * qzhang BuildInDBStructure class global comment. Detailled comment <br/>
	 * 
	 */
	public class GenerateSqlAction extends SelectionProviderAction {

		private List<IMetadataColumn> selectedNodes = new ArrayList<IMetadataColumn>();

		/**
		 * qzhang GenerateSqlAction constructor comment.
		 * 
		 * @param provider
		 * @param text
		 */
		protected GenerateSqlAction(ISelectionProvider provider) {
			super(
					provider,
					Messages
							.getString("GenerateSelectSQLAction.textCenerateSelectStatement"));
			setImageDescriptor(ImageUtil.getDescriptor("Images.SqlEditorIcon"));
			init();
		}

		@Override
		public void selectionChanged(IStructuredSelection selection) {
			init();
		}

		@SuppressWarnings("unchecked")//$NON-NLS-1$
		public void init() {
			selectedNodes.clear();
			Object[] structuredSelection = ((IStructuredSelection) treeViewer
					.getSelection()).toArray();
			for (Object object : structuredSelection) {
				if (object instanceof MetadataTable) {
					selectedNodes.clear();
					if (((MetadataTable) object).getListColumns().isEmpty()) {
						setEnabled(false);
						return;
					}
					selectedNodes.addAll(((MetadataTable) object)
							.getListColumns());
					break;
				}
				if (object instanceof MetadataColumn) {
					selectedNodes.add((IMetadataColumn) object);
				}
			}
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.jface.action.Action#run()
		 */
		@Override
		public void run() {

			CTabItem item = dialog.getEditorComposite().getTabFolder().getItem(
					0);
			CTabItem item2 = ((CTabFolder) item.getControl()).getItem(0);
			SQLBuilderEditorComposite editorComposite = (SQLBuilderEditorComposite) item2
					.getControl();
			editorComposite.getRepositoryNode();
			connectionParameters.setQuery(QueryUtil.generateNewQuery(
					connectionParameters.getNode(), metadataTable,
					connectionParameters.getDbType(), connectionParameters
							.getSchema(), schema));
			dialog.openEditor(editorComposite.getRepositoryNode(),
					Arrays.asList((new String[] { editorComposite
							.getRepositoryName() })), connectionParameters,
					false);
		}

		/**
		 * qzhang Comment method "getSchemaSql".
		 * 
		 * @return
		 */
		private String getSchemaSql() {
			String sql = "select ";
			String newschema = TalendTextUtils.addQuotesWithSpaceField(schema,
					connectionParameters.getDbType());
			for (IMetadataColumn column : selectedNodes) {
				sql += TextUtil.addSqlQuots(connectionParameters.getDbType(),
						TalendTextUtils.addQuotesWithSpaceField(column
								.getOriginalDbColumnName(),
								connectionParameters.getDbType()), newschema)
						+ ", ";
			}
			sql = sql.substring(0, sql.length() - 2);
			sql += "\nfrom "
					+ TextUtil.addSqlQuots(connectionParameters.getDbType(),
							newschema, connectionParameters.getSchema());
			return sql;
		}

	}

	public DBStructureComposite getDbstructureCom() {
		return this.dbstructureCom;
	}

	/**
	 * qzhang BuildInDBStructure class global comment. Detailled comment <br/>
	 * 
	 * $Id: talend-code-templates.xml 1 2007-3-21 上午11:08:52 (星期五, 29 九月 2006)
	 * qzhang $
	 * 
	 */
	public class SchemaTreePrivder extends LabelProvider implements
			ITableLabelProvider, ITreeContentProvider {

		/*
		 * (non-Java)
		 * 
		 * @see org.eclipse.jface.viewers.ITableLabelProvider#getColumnImage(java.lang.Object,
		 *      int)
		 */
		public Image getColumnImage(Object element, int columnIndex) {
			if (element instanceof IMetadataTable) {
				return tableImage;
			} else {
				return columnImage;
			}
		}

		/*
		 * (non-Java)
		 * 
		 * @see org.eclipse.jface.viewers.ITableLabelProvider#getColumnText(java.lang.Object,
		 *      int)
		 */
		public String getColumnText(Object element, int columnIndex) {
			if (element instanceof IMetadataTable) {
				// IMetadataTable metadataTable3 = (IMetadataTable) element;
				// if (metadataTable3.getLabel() == null ||
				// "".equals(metadataTable3.getLabel())) {
				return schema;
				// } else {
				// return metadataTable3.getLabel();
				// }
			} else if (element instanceof IMetadataColumn) {
				final IMetadataColumn metadataColumn = ((IMetadataColumn) element);
				String originalDbColumnName = metadataColumn
						.getOriginalDbColumnName();
				return originalDbColumnName;
			}
			return null;
		}

		/*
		 * (non-Java)
		 * 
		 * @see org.eclipse.jface.viewers.ITreeContentProvider#getChildren(java.lang.Object)
		 */
		public Object[] getChildren(Object parentElement) {
			return getElements(parentElement);
		}

		/*
		 * (non-Java)
		 * 
		 * @see org.eclipse.jface.viewers.ITreeContentProvider#getParent(java.lang.Object)
		 */
		public Object getParent(Object element) {
			if (element instanceof IMetadataColumn) {
				return metadataTable;
			}
			return parentMetadata;
		}

		/*
		 * (non-Java)
		 * 
		 * @see org.eclipse.jface.viewers.ITreeContentProvider#hasChildren(java.lang.Object)
		 */
		public boolean hasChildren(Object element) {
			if (element instanceof IMetadataTable) {
				return true;
			}
			return false;
		}

		/*
		 * (non-Java)
		 * 
		 * @see org.eclipse.jface.viewers.IStructuredContentProvider#getElements(java.lang.Object)
		 */
		public Object[] getElements(Object inputElement) {

			if (inputElement instanceof IMetadataTable) {
				IMetadataTable metadataTable3 = (IMetadataTable) inputElement;
				return metadataTable3.getListColumns().toArray();
			} else if (inputElement instanceof IMetadataConnection) {
				return ((IMetadataConnection) inputElement).getListTables()
						.toArray();
			}
			return Collections.EMPTY_LIST.toArray();
		}

		/*
		 * (non-Java)
		 * 
		 * @see org.eclipse.jface.viewers.IContentProvider#inputChanged(org.eclipse.jface.viewers.Viewer,
		 *      java.lang.Object, java.lang.Object)
		 */
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {

		}

	}
}
