// ============================================================================
//
// Copyright (C) 2006-2007 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.sqlbuilder.ui;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.util.Assert;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.talend.core.model.metadata.builder.connection.Query;
import org.talend.repository.model.RepositoryNode;
import org.talend.repository.model.RepositoryNode.EProperties;
import org.talend.sqlbuilder.Messages;
import org.talend.sqlbuilder.SqlBuilderPlugin;
import org.talend.sqlbuilder.dbstructure.RepositoryNodeType;
import org.talend.sqlbuilder.dbstructure.DBTreeProvider.QueryRepositoryObject;
import org.talend.sqlbuilder.editors.MultiPageSqlBuilderEditor;
import org.talend.sqlbuilder.repository.utility.EMFRepositoryNodeManager;
import org.talend.sqlbuilder.repository.utility.SQLBuilderRepositoryNodeManager;
import org.talend.sqlbuilder.util.ConnectionParameters;
import org.talend.sqlbuilder.util.TextUtil;

/**
 * This class is responsible for creating tab composite. <br/>
 * 
 * $Id: SQLBuilderTabComposite.java,v 1.23 2006/11/09 08:40:43 tangfn Exp $
 * 
 */
public class SQLBuilderTabComposite extends Composite {

    private CTabFolder tabFolder;

    private ISQLBuilderDialog dialog;

    public SQLBuilderTabComposite(Composite parent, int style, ISQLBuilderDialog d) {
        super(parent, style);
        this.dialog = d;
        this.setLayout(new FillLayout());

    }

    /**
     * Opens an new sql editor.
     * 
     * @param node RepositoryNode with the DatabaseConnection
     * @param repositoryNames all the repositories' name
     * @param connParam ConnectionParameters
     * @param isDefaultEditor whether is the DefaultEditor
     */
    public void openNewEditor(RepositoryNode node, List<String> repositories, ConnectionParameters connParam,
            boolean isDefaultEditor) {

        Assert.isNotNull(node, Messages.getString("SQLBuilderTabComposite.assertMessage")); //$NON-NLS-1$
        createTabFolder();
        try {
            createTabItem(node, connParam, isDefaultEditor);
        } catch (Exception e) {
            SqlBuilderPlugin.log(e.getMessage(), e);
        }
    }

    public void openNewEditor(RepositoryNode node, List<String> repositories, ConnectionParameters connParam,
            boolean isDefaultEditor, List<RepositoryNode> nodesSel) {

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
            tabFolder.setToolTipText(Messages.getString("SQLBuilderTabComposite.toolTipText")); //$NON-NLS-1$
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
    private void createTabItem(RepositoryNode node, ConnectionParameters connParam, boolean isDefaultEditor) {
        String queryStr = "";
        if (node != null) {
            CTabItem[] tabItems = tabFolder.getItems();
            for (int i = 0; i < tabItems.length; i++) {
                SQLBuilderEditorComposite editorComposite = (SQLBuilderEditorComposite) (((CTabFolder) tabItems[i].getControl())
                        .getItems()[0]).getControl();
                Query query2 = editorComposite.getConnParam().getQueryObject();
                if ((RepositoryNodeType) node.getProperties(EProperties.CONTENT_TYPE) == RepositoryNodeType.QUERY) {
                    Query query = ((QueryRepositoryObject) node.getObject()).getQuery();
                    if (query2 == null && tabItems[i].getData() instanceof Query) {
                        query2 = (Query) tabItems[i].getData();
                    }
                    if (query2 != null && query.getLabel().equals(query2.getLabel())) {
                        if ("".equals(editorComposite.getEditorContent())) { //$NON-NLS-1$
                            editorComposite.setEditorContent(query.getValue());
                        }
                        tabFolder.setSelection(i);
                        return;
                    }
                    connParam.setQueryObject(query);
                    queryStr = query.getValue();
                }
                // else {
                // String queryString = connParam.getQuery();
                // String repositoryName = connParam.getRepositoryName();
                // if (editorComposite.getConnParam().getQuery().equals(queryString)
                // && editorComposite.getConnParam().getRepositoryName().equals(repositoryName)) {
                // if ("".equals(editorComposite.getEditorContent())) { //$NON-NLS-1$
                // editorComposite.setEditorContent(queryString);
                // }
                // tabFolder.setSelection(i);
                // return;
                // }
                // }
            }
        }
        CTabItem tabItem = null;
        if (connParam.isFromDBNode()) {
            tabItem = new CTabItem(tabFolder, SWT.NULL);
        } else {
            tabItem = new CTabItem(tabFolder, SWT.CLOSE);
        }
        node = SQLBuilderRepositoryNodeManager.getRoot(node);

        // SQLBuilderEditorComposite builderEditorComposite = new SQLBuilderEditorComposite(tabFolder, tabItem,
        // isDefaultEditor,
        // connParam, node, dialog);
        // builderEditorComposite.setEditorContent(connParam);
        // builderEditorComposite.setRepositoryNode(node);
        // builderEditorComposite.setQueryObject(dialog.getConnParameters().getQueryObject());
        // builderEditorComposite.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));

        if (!"".equals(queryStr)) {
            try {
                nodesSel = EMFRepositoryNodeManager.getInstance().parseSqlStatement(queryStr, node);
            } catch (Exception e) {
                MessageDialog.openError(new Shell(), Messages.getString("SQLBuilderTabComposite.Notice.title"), Messages
                        .getString("SQLBuilderTabComposite.Notice.Info"));
            }
        }
        MultiPageSqlBuilderEditor builderEditor = new MultiPageSqlBuilderEditor(nodesSel, tabItem, isDefaultEditor, connParam,
                node, dialog);
        builderEditor.createPartControl(tabFolder);
        tabItem.setControl(builderEditor.getContainer());
        tabItem.setData(TextUtil.KEY, builderEditor);
        if (connParam.isFromRepository() && connParam.getQueryObject() != null) {
            queryStr = connParam.getQueryObject().getValue();
        }
        builderEditor.setSqlText(queryStr);
        if (connParam.isShowDesignerPage()) {
            builderEditor.showDesignerPage();
        }
        // set new tab as the active one.
        tabFolder.setSelection(tabFolder.getItemCount() - 1);

        // refresh view
        tabFolder.layout();
        tabFolder.redraw();
    }

    private List<RepositoryNode> nodesSel = new ArrayList<RepositoryNode>();

    /**
     * 
     * Gets default tab page's text.
     * 
     * @return a string representing sql text.
     */
    public String getDefaultTabSql() {
        SQLBuilderEditorComposite editorComposite = (SQLBuilderEditorComposite) (((CTabFolder) tabFolder.getItems()[0]
                .getControl()).getItems()[0]).getControl();

        return editorComposite.getSQLToBeExecuted();
    }

    /**
     * method "getCurrentTabSql" : Get Cuurent Tab page (Used tab) 's Text.
     * 
     * @return a string representing sql text.
     */
    public String getCurrentTabSql() {
        if (tabFolder.getSelection() != null) {
            Control control = (((CTabFolder) tabFolder.getSelection().getControl())).getSelection().getControl();
            if (control instanceof SQLBuilderDesignerComposite) {
                return ((SQLBuilderDesignerComposite) control).getSQLToBeExecuted();
            } else if (control instanceof SQLBuilderEditorComposite) {
                SQLBuilderEditorComposite editorComposite = (SQLBuilderEditorComposite) control;
                return editorComposite.getSQLToBeExecuted();
            }
        }
        return "";
    }

    public CTabFolder getTabFolder() {
        return this.tabFolder;
    }

    public List<RepositoryNode> getNodesSel() {
        return this.nodesSel;
    }

    public void setNodesSel(List<RepositoryNode> nodesSel) {
        this.nodesSel = nodesSel;
    }
}
