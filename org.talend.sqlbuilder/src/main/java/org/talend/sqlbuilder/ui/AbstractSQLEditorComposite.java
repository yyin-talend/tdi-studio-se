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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.List;

import org.eclipse.jface.action.CoolBarManager;
import org.eclipse.jface.action.ToolBarContributionItem;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.layout.GridData;
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
import org.talend.sqlbuilder.actions.SaveAsSQLAction;
import org.talend.sqlbuilder.actions.SaveFileAsAction;
import org.talend.sqlbuilder.actions.SaveSQLAction;
import org.talend.sqlbuilder.actions.explain.DB2ExplainPlanAction;
import org.talend.sqlbuilder.actions.explain.OracleExplainPlanAction;
import org.talend.sqlbuilder.dbstructure.SqlBuilderRepositoryObject;
import org.talend.sqlbuilder.repository.utility.SQLBuilderRepositoryNodeManager;
import org.talend.sqlbuilder.ui.editor.ISQLEditor;
import org.talend.sqlbuilder.util.ConnectionParameters;
import org.talend.sqlbuilder.util.UIUtils;

/**
 * qzhang class global comment. Detailled comment <br/>
 * 
 */
public abstract class AbstractSQLEditorComposite extends Composite implements ISQLEditor {

    public static final String[] SUPPORTED_FILETYPES = new String[] { "*.txt", //$NON-NLS-1$
            "*.sql", "*.*" };

    public ISQLBuilderDialog dialog;

    protected ConnectionParameters connParam;

    public static final String QUERY_PREFIX = Messages.getString("SQLBuilderEditorComposite.titleQuery");

    /**
     * qzhang AbstractSQLEditorComposite constructor comment.
     * 
     * @param parent
     * @param style
     */
    public AbstractSQLEditorComposite(Composite parent, int style, ISQLBuilderDialog d, ConnectionParameters connParam) {
        super(parent, style);
        this.dialog = d;
        this.connParam = connParam;
    }

    private String title2 = "";

    protected void setEditorTitle(RepositoryNode node, ConnectionParameters connParam, CTabItem tabItem) {
        String dbName = SQLBuilderRepositoryNodeManager.getDatabaseNameByRepositoryNode(node);
        String title = "";
        if (connParam.isFromDBNode()) {
            String selectedComponentName = connParam.getSelectedComponentName();
            if (selectedComponentName != null && selectedComponentName.length() != 0) {
                title = selectedComponentName + "."; //$NON-NLS-1$
            }
            title = dbName + "(" + getRepositoryName() + ").sql";
            tabItem.setText(title);
        } else {
            if (connParam.getQueryObject() != null) {
                title = QUERY_PREFIX + connParam.getQueryObject().getLabel();
                tabItem.setData(connParam.getQueryObject());
            } else if (dialog.getConnParameters().getQueryObject() != null) {
                title = QUERY_PREFIX + dialog.getConnParameters().getQueryObject().getLabel();
                tabItem.setData(dialog.getConnParameters().getQueryObject());
            }
            if (connParam.getEditorTitle() != null) {
                tabItem.setText(connParam.getEditorTitle());
            } else {
                tabItem.setText(title);
            }
        }
        title2 = tabItem.getText();
    }

    /**
     * dev Comment method "setExplainPlanAction".
     */
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
     * @see org.talend.sqlbuilder.ui.editor.ISQLEditor#doSaveAs()
     */
    public void doSaveAs() {
        FileDialog fileDialog = new FileDialog(this.getShell(), SWT.SAVE);
        fileDialog.setText(Messages.getString("SQLEditor.SaveAsDialog.Title")); //$NON-NLS-1$
        fileDialog.setFilterExtensions(SUPPORTED_FILETYPES);
        fileDialog.setFilterNames(SUPPORTED_FILETYPES);
        fileDialog.setFileName(title2);

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

            String content = getSQLToBeExecuted();
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
     * @see org.talend.sqlbuilder.ui.editor.ISQLEditor#saveSQL()
     */
    public Query doSaveSQL(Query query2, boolean as) {
        SQLBuilderRepositoryNodeManager repositoryNodeManager = new SQLBuilderRepositoryNodeManager();
        List<String> existingName = repositoryNodeManager.getALLQueryLabels(getRepositoryNode());
        if (!as) {
            if (query2 != null && existingName.contains(query2.getLabel())) {
                query2.setValue(getSQLToBeExecuted());
                repositoryNodeManager.saveQuery(getRepositoryNode(), query2,null);
                dialog.refreshNode(getRepositoryNode());
                getMultiPageEditor().updateEditorTitle(QUERY_PREFIX + query2.getLabel());
                getDialog().notifySQLBuilder(getRepositoryNode().getObject());
                return query2;
            }
        }
        SQLPropertyDialog.setDialogTitle(getMultiPageEditor().getTitle());
        SQLPropertyDialog saveSQLDialog = new SQLPropertyDialog(this.getShell(), existingName);
        saveSQLDialog.setSql(getSQLToBeExecuted());
        // saveSQLDialog.setQuery(connParam.getQueryObject());
        if (Window.OK == saveSQLDialog.open()) {
            Query query = saveSQLDialog.getQuery();
            repositoryNodeManager.saveQuery(getRepositoryNode(), query,null);
            dialog.refreshNode(getRepositoryNode());
            getMultiPageEditor().updateEditorTitle(QUERY_PREFIX + query.getLabel());
            getMultiPageEditor().getDeactivePageSaveSQLAction().setQuery(query);
            getMultiPageEditor().setItemData(query);
            getDialog().notifySQLBuilder(getRepositoryNode().getObject());
            return query;
        }
        return null;
    }

    /*
     * (non-Java)
     * 
     * @see org.talend.sqlbuilder.ui.editor.ISQLEditor#getRepositoryName()
     */

    public String getRepositoryName() {
        if (getRepositoryNode() == null) {
            return ""; //$NON-NLS-1$
        }
        String repositoryName = ((SqlBuilderRepositoryObject) getRepositoryNode().getObject()).getRepositoryName();
        return repositoryName;
    }

    public static final String ID = "sqlbuilder.sqleditor.designer";

    protected ToolBarManager defaultToolBarMgr;

    protected AbstractEditorAction clearTextAction;

    protected CoolBar coolBar;

    protected CoolBarManager coolBarMgr;

    protected AbstractEditorAction execSQLAction;

    protected AbstractEditorAction openFileAction;

    protected AbstractEditorAction exportAction;

    protected SaveAsSQLAction saveAsSQLAction;

    protected SaveSQLAction saveSQLAction;

    protected AbstractEditorAction explainAction;

    protected SQLEditorSessionSwitcher sessionSwitcher;

    protected GUIModificationQueryAction guiModificationQueryAction;

    protected ToolBarManager sessionToolBarMgr;

    /**
     * qzhang Comment method "createToolBar".
     */
    protected void createToolBar() {
        // create coolbar
        coolBar = new CoolBar(this, SWT.NONE);
        coolBarMgr = new CoolBarManager(coolBar);

        GridData gid = new GridData();
        gid.horizontalAlignment = GridData.FILL;
        coolBar.setLayoutData(gid);

        // initialize default actions
        defaultToolBarMgr = new ToolBarManager(SWT.NONE);

        execSQLAction = new ExecSQLAction(SQLResultComposite.getInstance(), this);

        openFileAction = new OpenFileAction();
        openFileAction.setEditor(this);

        saveSQLAction = new SaveSQLAction(getRepositoryNode(), connParam);
        saveSQLAction.setEditor(this);

        saveAsSQLAction = new SaveAsSQLAction(getRepositoryNode(), connParam);
        saveAsSQLAction.setEditor(this);

        exportAction = new SaveFileAsAction();
        exportAction.setEditor(this);

        clearTextAction = new ClearTextAction();
        clearTextAction.setEditor(this);

        guiModificationQueryAction = new GUIModificationQueryAction(getRepositoryNode(), connParam, dialog);
        guiModificationQueryAction.setEditor(this);

        addDefaultActions();

        // initialize session actions
        sessionToolBarMgr = new ToolBarManager(SWT.NONE);

        sessionSwitcher = new SQLEditorSessionSwitcher(this);
        sessionToolBarMgr.add(sessionSwitcher);

        // add all toolbars to parent coolbar
        coolBar.setLocked(true);
        coolBarMgr.add(new ToolBarContributionItem(defaultToolBarMgr));
        coolBarMgr.add(new ToolBarContributionItem(sessionToolBarMgr));

        coolBarMgr.update(true);
    }

    /**
     * qzhang Comment method "addDefaultActions".
     * 
     */
    protected void addDefaultActions() {
        // /addDefaultActions(defaultToolBarMgr);

        defaultToolBarMgr.removeAll();
        execSQLAction.setEnabled(true);
        defaultToolBarMgr.add(execSQLAction);
        defaultToolBarMgr.add(openFileAction);
        defaultToolBarMgr.add(saveSQLAction);
        defaultToolBarMgr.add(saveAsSQLAction);
        defaultToolBarMgr.add(exportAction);
        defaultToolBarMgr.add(clearTextAction);
        explainAction = addExplainPlanAction(getRepositoryNode());
        if (explainAction != null) {
            defaultToolBarMgr.add(explainAction);
        }
        defaultToolBarMgr.add(guiModificationQueryAction);
    }

    public abstract StyledText getColorText();

    public SaveAsSQLAction getSaveAsSQLAction() {
        return this.saveAsSQLAction;
    }

    public SaveSQLAction getSaveSQLAction() {
        return this.saveSQLAction;
    }

    /**
     * Getter for dialog.
     * 
     * @return the dialog
     */
    public ISQLBuilderDialog getDialog() {
        return this.dialog;
    }
}
