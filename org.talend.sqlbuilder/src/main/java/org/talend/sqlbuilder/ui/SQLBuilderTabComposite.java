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
package org.talend.sqlbuilder.ui;

import java.util.List;

import org.eclipse.jface.util.Assert;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.talend.core.model.metadata.builder.connection.Query;
import org.talend.repository.model.RepositoryNode;
import org.talend.repository.model.RepositoryNode.EProperties;
import org.talend.sqlbuilder.SqlBuilderPlugin;
import org.talend.sqlbuilder.dbstructure.RepositoryNodeType;
import org.talend.sqlbuilder.dbstructure.DBTreeProvider.QueryRepositoryObject;
import org.talend.sqlbuilder.repository.utility.SQLBuilderRepositoryNodeManager;
import org.talend.sqlbuilder.util.ConnectionParameters;

/**
 * This class is responsible for creating tab composite. <br/>
 * 
 * $Id: SQLBuilderTabComposite.java,v 1.23 2006/11/09 08:40:43 tangfn Exp $
 * 
 */
public class SQLBuilderTabComposite extends Composite {

   
	private CTabFolder tabFolder;

	private ISQLBuilderDialog dialog;

	public SQLBuilderTabComposite(Composite parent, int style,
			ISQLBuilderDialog d) {
		super(parent, style);
		this.dialog = d;
		this.setLayout(new FillLayout());
        
	}

	/**
	 * Opens an new sql editor.
	 * 
	 * @param node
	 *            RepositoryNode with the DatabaseConnection
	 * @param repositoryNames
	 *            all the repositories' name
	 * @param connParam
	 *            ConnectionParameters
	 * @param isDefaultEditor
	 *            whether is the DefaultEditor
	 */
	public void openNewEditor(RepositoryNode node, List<String> repositories,
			ConnectionParameters connParam, boolean isDefaultEditor) {

		Assert.isNotNull(node, "SessionTreeNode should not be null");
		createTabFolder();
		try {
			createTabItem(node, connParam, isDefaultEditor);
		} catch (Exception e) {
			SqlBuilderPlugin.log(e.getMessage(), e);
		}
	}

	/**
	 * 
	 * Creates tab folder.
	 * 
	 */
	private void createTabFolder() {

		if (tabFolder == null || tabFolder.isDisposed()) {

			clearParent();

			// create tab folder for different sessions
			tabFolder = new CTabFolder(this, SWT.NULL | SWT.BORDER);
			tabFolder.setSimple(false);
			tabFolder.setToolTipText("SQL editor");
			this.layout();
			this.redraw();
		}
	}

	/**
	 * Dispose all parent Controls.
	 * 
	 */
	private void clearParent() {

		Control[] children = this.getChildren();
		if (children != null) {
			for (int i = 0; i < children.length; i++) {
				children[i].dispose();
			}
		}
	}

	/**
	 * Creates tab item.
	 * 
	 * @param node
	 * @param connParam
	 * @param isDefaultEditor
	 */
	private void createTabItem(RepositoryNode node,
			ConnectionParameters connParam, boolean isDefaultEditor) {
		if (node != null) {
			CTabItem[] tabItems = tabFolder.getItems();
			for (int i = 0; i < tabItems.length; i++) {
				SQLBuilderEditorComposite editorComposite = (SQLBuilderEditorComposite) tabItems[i]
						.getControl();
				Query query2 = editorComposite.getConnParam().getQueryObject();
				if ((RepositoryNodeType) node
						.getProperties(EProperties.CONTENT_TYPE) == RepositoryNodeType.QUERY) {
					Query query = ((QueryRepositoryObject) node.getObject())
							.getQuery();
					if (query2 != null
							&& query.getLabel().equals(query2.getLabel())) {
                        if ("".equals(editorComposite.getEditorContent())) {
                            editorComposite.setEditorContent(query.getValue());
                        }
						tabFolder.setSelection(i);
						return;
					}
					connParam.setQueryObject(query);
				} else {
					String queryString = connParam.getQuery();
					if (editorComposite.getConnParam().getQuery().equals(
							queryString)) {
                        if ("".equals(editorComposite.getEditorContent())) {
                            editorComposite.setEditorContent(queryString);
                        }
						tabFolder.setSelection(i);
						return;
					}
				}
			}
		}
		CTabItem tabItem = null;
		if (isDefaultEditor) {
			tabItem = new CTabItem(tabFolder, SWT.NULL);
		} else {
			// Do not allow user to close the default editor.
			tabItem = new CTabItem(tabFolder, SWT.CLOSE);
		}
		node = SQLBuilderRepositoryNodeManager.getRoot(node);

		SQLBuilderEditorComposite builderEditorComposite = new SQLBuilderEditorComposite(
				tabFolder, tabItem, isDefaultEditor, connParam, node, dialog);
		// builderEditorComposite.setEditorContent(connParam);
		// builderEditorComposite.setRepositoryNode(node);
        builderEditorComposite.setQueryObject(dialog.getConnParameters().getQueryObject());
		builderEditorComposite.setLayoutData(new GridData(SWT.FILL, SWT.TOP,
				true, false));

		tabItem.setControl(builderEditorComposite);

		// set new tab as the active one.
		tabFolder.setSelection(tabFolder.getItemCount() - 1);

		// refresh view
		tabFolder.layout();
		tabFolder.redraw();
	}

	/**
	 * 
	 * Gets default tab page's text.
	 * 
	 * @return a string representing sql text.
	 */
	public String getDefaultTabSql() {
		Control control = tabFolder.getChildren()[0];
		SQLBuilderEditorComposite composite = (SQLBuilderEditorComposite) control;
		return composite.getSQLToBeExecuted();
	}

	/**
	 * method "getCurrentTabSql" : Get Cuurent Tab page (Used tab) 's Text.
	 * 
	 * @return a string representing sql text.
	 */
	public String getCurrentTabSql() {
		Control control = tabFolder.getSelection().getControl();
		SQLBuilderEditorComposite composite = (SQLBuilderEditorComposite) control;
		return composite.getSQLToBeExecuted();
	}

    
    public CTabFolder getTabFolder() {
        return this.tabFolder;
    }
}
