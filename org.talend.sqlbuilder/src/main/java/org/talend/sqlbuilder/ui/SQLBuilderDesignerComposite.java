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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.List;

import org.eclipse.jface.action.CoolBarManager;
import org.eclipse.jface.action.ToolBarContributionItem;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.util.Assert;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.CoolBar;
import org.eclipse.swt.widgets.FileDialog;
import org.talend.core.model.metadata.builder.connection.Query;
import org.talend.repository.model.RepositoryNode;
import org.talend.sqlbuilder.Messages;
import org.talend.sqlbuilder.SqlBuilderPlugin;
import org.talend.sqlbuilder.actions.AbstractEditorAction;
import org.talend.sqlbuilder.actions.ClearTextAction;
import org.talend.sqlbuilder.actions.ExecSQLAction;
import org.talend.sqlbuilder.actions.GUIModificationQueryAction;
import org.talend.sqlbuilder.actions.OpenFileAction;
import org.talend.sqlbuilder.actions.SQLEditorSessionSwitcher;
import org.talend.sqlbuilder.actions.SaveFileAsAction;
import org.talend.sqlbuilder.actions.SaveSQLAction;
import org.talend.sqlbuilder.actions.explain.DB2ExplainPlanAction;
import org.talend.sqlbuilder.actions.explain.OracleExplainPlanAction;
import org.talend.sqlbuilder.dbstructure.SqlBuilderRepositoryObject;
import org.talend.sqlbuilder.editors.MultiPageSqlBuilderEditor;
import org.talend.sqlbuilder.erdiagram.ui.ErDiagramComposite;
import org.talend.sqlbuilder.repository.utility.EMFRepositoryNodeManager;
import org.talend.sqlbuilder.repository.utility.SQLBuilderRepositoryNodeManager;
import org.talend.sqlbuilder.ui.editor.ISQLEditor;
import org.talend.sqlbuilder.util.ConnectionParameters;
import org.talend.sqlbuilder.util.UIUtils;

/**
 * qzhang class global comment. Detailled comment <br/>
 * 
 * $Id: talend-code-templates.xml 1 2007-3-7 上午09:35:17 (星期五, 29 九月 2006) qzhang $
 * 
 */
public class SQLBuilderDesignerComposite extends Composite implements ISQLEditor {

    public static final String ID = "sqlbuilder.sqleditor.designer";

    private CTabItem tabItem;

    private ConnectionParameters connParam;

    private boolean isDefaultEditor;

    private ISQLBuilderDialog dialog;

    private RepositoryNode repositoryNode;

    private Query queryObject;

    private ErDiagramComposite erDiagramComposite;

    

    private boolean ifLimit = true;

    /**
     * qzhang SQLBuilderDesignerComposite constructor comment.
     * 
     * @param parent
     * @param style
     */
    public SQLBuilderDesignerComposite(Composite parent, CTabItem tabItem, boolean isDefaultEditor,
            ConnectionParameters connParam, RepositoryNode node, ISQLBuilderDialog d, List<RepositoryNode> nodes) {
        super(parent, SWT.NONE);
        dialog = d;
        this.tabItem = tabItem;
        this.isDefaultEditor = isDefaultEditor;
        isOpen = isDefaultEditor;
        this.connParam = connParam;
        repositoryNode = node;
        initialContent(this, nodes);
        setRepositoryNode(node);
    }

    /**
     * qzhang Comment method "initialContent".
     * 
     * @param composite
     */
    private void initialContent(SQLBuilderDesignerComposite composite, List<RepositoryNode> nodes) {
        GridLayout layout = new GridLayout();
        layout.numColumns = 1;
        layout.marginLeft = 0;
        layout.horizontalSpacing = 0;
        layout.verticalSpacing = 0;
        layout.marginWidth = 0;
        layout.marginHeight = 0;

        setLayout(layout);
        createToolBar();
        createDesignerArea(composite, nodes);
    }

    /**
     * qzhang Comment method "createDesignerArea".
     * 
     * @param composite
     */
    private void createDesignerArea(SQLBuilderDesignerComposite composite, List<RepositoryNode> nodes) {
        // create divider line
        Composite div1 = new Composite(composite, SWT.NONE);
        GridData lgid = new GridData();
        lgid.grabExcessHorizontalSpace = true;
        lgid.horizontalAlignment = GridData.FILL;
        lgid.heightHint = 1;
        lgid.verticalIndent = 1;
        div1.setLayoutData(lgid);
        div1.setBackground(composite.getShell().getDisplay().getSystemColor(SWT.COLOR_WIDGET_NORMAL_SHADOW));

        erDiagramComposite = new ErDiagramComposite(composite, SWT.VERTICAL);
        erDiagramComposite.setDialog(dialog);
        erDiagramComposite.setRootNode(repositoryNode);
        erDiagramComposite.setNodes(nodes);
        erDiagramComposite.setWeights(new int[] { 12, 1 });
    }

    /**
     * qzhang Comment method "createToolBar".
     */
    private void createToolBar() {
        // create coolbar
        coolBar = new CoolBar(this, SWT.NONE);
        coolBarMgr = new CoolBarManager(coolBar);

        GridData gid = new GridData();
        gid.horizontalAlignment = GridData.FILL;
        coolBar.setLayoutData(gid);

        // initialize default actions
        defaultToolBarMgr = new ToolBarManager(SWT.NONE);

        execSQLAction = new ExecSQLAction(SQLResultComposite.getInstance(), this);
        execSQLAction.setParentId(ID);

        openFileAction = new OpenFileAction();
        openFileAction.setEditor(this);
        openFileAction.setParentId(ID);

        saveSQLAction = new SaveSQLAction(repositoryNode, queryObject);
        saveSQLAction.setEditor(this);
        saveSQLAction.setParentId(ID);

        exportAction = new SaveFileAsAction();
        exportAction.setEditor(this);
        exportAction.setParentId(ID);

        clearTextAction = new ClearTextAction();
        clearTextAction.setEditor(this);
        clearTextAction.setParentId(ID);

        guiModificationQueryAction = new GUIModificationQueryAction(repositoryNode, connParam, dialog);
        guiModificationQueryAction.setEditor(this);
        guiModificationQueryAction.setParentId(ID);

        addDefaultActions();

        // initialize session actions
        sessionToolBarMgr = new ToolBarManager(SWT.NONE);

        sessionSwitcher = new SQLEditorSessionSwitcher(this);
        sessionToolBarMgr.add(sessionSwitcher);
        sessionSwitcher.setParentId(ID);

        // add all toolbars to parent coolbar
        coolBar.setLocked(true);
        coolBarMgr.add(new ToolBarContributionItem(defaultToolBarMgr));
        coolBarMgr.add(new ToolBarContributionItem(sessionToolBarMgr));

        coolBarMgr.update(true);
    }

    private ToolBarManager defaultToolBarMgr;

    private AbstractEditorAction clearTextAction;

    private CoolBar coolBar;

    private CoolBarManager coolBarMgr;

    private AbstractEditorAction execSQLAction;

    private AbstractEditorAction openFileAction;

    private AbstractEditorAction exportAction;

    private AbstractEditorAction saveSQLAction;

    private AbstractEditorAction explainAction;

    private SQLEditorSessionSwitcher sessionSwitcher;
    
    private GUIModificationQueryAction guiModificationQueryAction;
    
    private ToolBarManager sessionToolBarMgr;
    
    private boolean isOpen = true;
    /**
     * qzhang Comment method "addDefaultActions".
     * 
     */
    private void addDefaultActions() {
        // /addDefaultActions(defaultToolBarMgr);

        defaultToolBarMgr.removeAll();
        execSQLAction.setEnabled(true);
        defaultToolBarMgr.add(execSQLAction);
        defaultToolBarMgr.add(openFileAction);
        defaultToolBarMgr.add(saveSQLAction);
        defaultToolBarMgr.add(exportAction);
        defaultToolBarMgr.add(clearTextAction);
        explainAction = addExplainPlanAction(repositoryNode);
        if (explainAction != null) {
            openFileAction.setParentId(ID);
            defaultToolBarMgr.add(explainAction);
        }
        defaultToolBarMgr.add(guiModificationQueryAction);
    }

    public AbstractEditorAction addExplainPlanAction(RepositoryNode node) {

        if (SQLBuilderRepositoryNodeManager.getDbTypeFromRepositoryNode(node).startsWith("Oracle")) { //$NON-NLS-1$
            return new OracleExplainPlanAction(SQLResultComposite.getInstance(), this);
        } else if (SQLBuilderRepositoryNodeManager.getDbTypeFromRepositoryNode(node).startsWith("IBM DB2")) { //$NON-NLS-1$
            return new DB2ExplainPlanAction(SQLResultComposite.getInstance(), this);
        }
        return null;
    }

    /*
     * (non-Java)
     * 
     * @see org.talend.sqlbuilder.ui.editor.ISQLEditor#clearText()
     */
    public void clearText() {
        erDiagramComposite.clearAll();
    }

    /*
     * (non-Java)
     * 
     * @see org.talend.sqlbuilder.ui.editor.ISQLEditor#doSaveAs()
     */
    public void doSaveAs() {
        FileDialog fileDialog = new FileDialog(this.getShell(), SWT.SAVE);
        fileDialog.setText(Messages.getString("SQLEditor.SaveAsDialog.Title")); //$NON-NLS-1$
        fileDialog.setFilterExtensions(SQLBuilderEditorComposite.SUPPORTED_FILETYPES);
        fileDialog.setFilterNames(SQLBuilderEditorComposite.SUPPORTED_FILETYPES);
        fileDialog.setFileName(tabItem.getText());

        String path = fileDialog.open();
        if (path == null) {
            return;
        }
        BufferedWriter writer = null;
        try {

            File file = new File(path);
            if (file.exists()) {
                file.delete();
            }

            file.createNewFile();

            String content = erDiagramComposite.getSqlText();
            writer = new BufferedWriter(new FileWriter(file));
            writer.write(content, 0, content.length());

        } catch (Exception e) {

            UIUtils.openErrorDialog(Messages.getString("SQLEditor.SaveAsDialog.Error"), e); //$NON-NLS-1$
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (Exception e) {
                    SqlBuilderPlugin.log(e.getMessage(), e);
                }
            }

        }
    }

    /*
     * (non-Java)
     * 
     * @see org.talend.sqlbuilder.ui.editor.ISQLEditor#doSaveSQL(org.talend.core.model.metadata.builder.connection.Query)
     */
    public void doSaveSQL(Query query2) {
        SQLBuilderRepositoryNodeManager repositoryNodeManager = new SQLBuilderRepositoryNodeManager();
        List<String> existingName = repositoryNodeManager.getALLQueryLabels(repositoryNode);
        if (query2 != null && existingName.contains(query2.getLabel())) {
            query2.setValue(erDiagramComposite.getSqlText());
            repositoryNodeManager.saveQuery(repositoryNode, query2);
            dialog.refreshNode(repositoryNode);
            return;
        }
        SQLPropertyDialog saveSQLDialog = new SQLPropertyDialog(this.getShell(), existingName);
        saveSQLDialog.setSql(this.erDiagramComposite.getSqlText());
        // saveSQLDialog.setQuery(connParam.getQueryObject());
        if (Window.OK == saveSQLDialog.open()) {
            Query query = saveSQLDialog.getQuery();
            repositoryNodeManager.saveQuery(repositoryNode, query);
            dialog.refreshNode(repositoryNode);
        }
    }

    /*
     * (non-Java)
     * 
     * @see org.talend.sqlbuilder.ui.editor.ISQLEditor#getDefaultEditor()
     */
    public boolean getDefaultEditor() {
        return this.isDefaultEditor;
    }

    /*
     * (non-Java)
     * 
     * @see org.talend.sqlbuilder.ui.editor.ISQLEditor#getIfLimit()
     */
    public boolean getIfLimit() {
        return ifLimit;
    }

    /**
     * Sets the ifLimit.
     * 
     * @param ifLimit the ifLimit to set
     */
    public void setIfLimit(boolean ifLimit) {
        this.ifLimit = ifLimit;
    }

    private String maxResult;

    /*
     * (non-Java)
     * 
     * @see org.talend.sqlbuilder.ui.editor.ISQLEditor#getMaxResult()
     */
    public String getMaxResult() {
        return maxResult;
    }

    public void setSqlText(String string) {
        erDiagramComposite.setSqlText(string);
    }

    /**
     * Sets the maxResult.
     * 
     * @param maxResult the maxResult to set
     */
    public void setMaxResult(String maxResult) {
        this.maxResult = maxResult;
    }

    /*
     * (non-Java)
     * 
     * @see org.talend.sqlbuilder.ui.editor.ISQLEditor#getRepositoryName()
     */
    public String getRepositoryName() {
        if (repositoryNode == null) {
            return ""; //$NON-NLS-1$
        }
        String repositoryName = ((SqlBuilderRepositoryObject) repositoryNode.getObject()).getRepositoryName();
        return repositoryName;
    }

    /*
     * (non-Java)
     * 
     * @see org.talend.sqlbuilder.ui.editor.ISQLEditor#getRepositoryNode()
     */
    public RepositoryNode getRepositoryNode() {
        return repositoryNode;
    }

    /*
     * (non-Java)
     * 
     * @see org.talend.sqlbuilder.ui.editor.ISQLEditor#getSQLToBeExecuted()
     */
    public String getSQLToBeExecuted() {
        return erDiagramComposite.getSqlText();
    }

    /*
     * (non-Java)
     * 
     * @see org.talend.sqlbuilder.ui.editor.ISQLEditor#refresh(boolean)
     */
    public void refresh(final boolean b) {
        this.getShell().getDisplay().asyncExec(new Runnable() {

            public void run() {

                if (b) {

                    // reset actions
                    addDefaultActions();
                    defaultToolBarMgr.update(true);
                }

                // update session toolbar
                sessionToolBarMgr.update(true);

                coolBarMgr.update(true);
                coolBar.update();
            }
        });
    }

    /*
     * (non-Java)
     * 
     * @see org.talend.sqlbuilder.ui.editor.ISQLEditor#setEditorContent(java.lang.String)
     */
    public void setEditorContent(String string) {
        try {
            if (isOpen) {
                isOpen = false;
            } else {
                EMFRepositoryNodeManager.getInstance().updateErDiagram(true, erDiagramComposite, string, repositoryNode);
            }
        } catch (Exception e) {
            SqlBuilderPlugin.log(string, e);
        }
    }

    public void setQueryObject(Query queryObject) {
        this.queryObject = queryObject;
    }

    public void setEditorContent(ConnectionParameters connectionParameters) {
        this.connParam = connectionParameters;
        setEditorContent(connectionParameters.getQuery());

    }

    /*
     * (non-Java)
     * 
     * @see org.talend.sqlbuilder.ui.editor.ISQLEditor#setRepositoryNode(org.talend.repository.model.RepositoryNode)
     */
    public void setRepositoryNode(RepositoryNode node) {
        Assert.isNotNull(node, Messages.getString("SQLBuilderEditorComposite.assertMessage")); //$NON-NLS-1$
        this.repositoryNode = node;
        guiModificationQueryAction.setCurrentNode(node);
        this.setEditorTitle(this.repositoryNode);
        sessionSwitcher.refreshSelectedRepository();
        EMFRepositoryNodeManager.getInstance().setRoot(node);
    }

    public ErDiagramComposite getErDiagramComposite() {
        return this.erDiagramComposite;
    }

    /**
     * Sets tab title.
     * 
     * @param node
     */
    private void setEditorTitle(RepositoryNode node) {
        String dbName = SQLBuilderRepositoryNodeManager.getDatabaseNameByRepositoryNode(node);
        String title = ""; //$NON-NLS-1$
        String repositoryName = getRepositoryName();
        String selectedComponentName = connParam.getSelectedComponentName();
        if (this.isDefaultEditor && (selectedComponentName != null && selectedComponentName.length() != 0)) {
            title = selectedComponentName + "."; //$NON-NLS-1$
        }
        title = title + dbName + "(" + repositoryName + ").sql";
        if (connParam.getQueryObject() != null) {
            title = Messages.getString("SQLBuilderEditorComposite.titleQuery") + connParam.getQueryObject().getLabel(); //$NON-NLS-1$
        } else if (dialog.getConnParameters().getQueryObject() != null) {
            title = Messages.getString("SQLBuilderEditorComposite.titleQuery") //$NON-NLS-1$
                    + dialog.getConnParameters().getQueryObject().getLabel();
        }
        tabItem.setText(title);
    }

    private MultiPageSqlBuilderEditor multiPageEditor;
    /**
     * Sets the multiPageEditor.
     * @param multiPageEditor the multiPageEditor to set
     */
    public void setMultiPageEditor(MultiPageSqlBuilderEditor multiPageEditor) {
        this.multiPageEditor = multiPageEditor;
    }
    /* (non-Javadoc)
     * @see org.talend.sqlbuilder.ui.editor.ISQLEditor#getMultiPageEditor()
     */
    public MultiPageSqlBuilderEditor getMultiPageEditor() {
        return multiPageEditor;
    }
    
}
