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
package org.talend.sqlbuilder.editors;

import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.part.MultiPageEditorPart;
import org.talend.core.model.metadata.builder.connection.Query;
import org.talend.repository.model.RepositoryNode;
import org.talend.sqlbuilder.Messages;
import org.talend.sqlbuilder.actions.SaveAsSQLAction;
import org.talend.sqlbuilder.actions.SaveSQLAction;
import org.talend.sqlbuilder.erdiagram.ui.ErDiagramComposite;
import org.talend.sqlbuilder.repository.utility.EMFRepositoryNodeManager;
import org.talend.sqlbuilder.ui.ISQLBuilderDialog;
import org.talend.sqlbuilder.ui.SQLBuilderDesignerComposite;
import org.talend.sqlbuilder.ui.SQLBuilderEditorComposite;
import org.talend.sqlbuilder.util.ConnectionParameters;

/**
 * qzhang class global comment. Detailled comment <br/>
 * 
 * $Id: talend-code-templates.xml 1 2007-2-9 上午10:33:48 (星期五, 29 九月 2006) qzhang $
 * 
 */
public class MultiPageSqlBuilderEditor extends MultiPageEditorPart {

    private SQLBuilderEditorComposite sqlEdit;

    private SQLBuilderDesignerComposite sqlDesigner;

    private List<RepositoryNode> nodes;

    /**
     * qzhang MultiPageSqlBuilderEditor constructor comment.
     */
    public MultiPageSqlBuilderEditor(List<RepositoryNode> nodes, CTabItem tabItem, boolean isDefaultEditor,
            ConnectionParameters connParam, RepositoryNode rootNode, ISQLBuilderDialog dialog) {
        super();
        this.nodes = nodes;
        this.tabItem = tabItem;
        this.isDefaultEditor = isDefaultEditor;
        this.connParam = connParam;
        this.rootNode = rootNode;
        this.dialog = dialog;
    }

    public static final String ID = "org.talend.sqlbuilder.editors.MultiPageSqlBuilderEditor"; //$NON-NLS-1$

    private ConnectionParameters connParam;

    private CTabItem tabItem;

    private boolean isDefaultEditor;

    private RepositoryNode rootNode;

    private ISQLBuilderDialog dialog;

    private ErDiagramComposite erDiagramComposite;

    /*
     * (non-Java)
     * 
     * @see org.eclipse.ui.part.MultiPageEditorPart#createPages()
     */
    @Override
    protected void createPages() {
        try {
            sqlEdit = new SQLBuilderEditorComposite(this.getContainer(), tabItem, isDefaultEditor, connParam, rootNode, dialog);
            sqlEdit.setEditorContent(connParam);
            sqlEdit.setRepositoryNode(rootNode);
            sqlEdit.setQueryObject(dialog.getConnParameters().getQueryObject());
            sqlEdit.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
            sqlEdit.setMultiPageEditor(this);

            int index = addPage(sqlEdit);
            setPageText(index, Messages.getString("MultiPageSqlBuilderEditor.EditTab.Text")); //$NON-NLS-1$
            sqlDesigner = new SQLBuilderDesignerComposite(this.getContainer(), tabItem, isDefaultEditor, connParam, rootNode,
                    dialog, nodes);
            sqlDesigner.setSqlText(sqlEdit.getSQLToBeExecuted());
            sqlDesigner.setEditorContent(connParam);

            // sqlDesigner.setQueryObject(dialog.getConnParameters().getQueryObject());
            sqlDesigner.setMaxResult(sqlEdit.getMaxResult());
            sqlDesigner.setIfLimit(sqlEdit.getIfLimit());
            sqlDesigner.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
            sqlDesigner.setMultiPageEditor(this);

            erDiagramComposite = sqlDesigner.getErDiagramComposite();
            index = addPage(sqlDesigner);
            setPageText(index, Messages.getString("MultiPageSqlBuilderEditor.DesignerTab.Text")); //$NON-NLS-1$
            attachListeners();
        } catch (Exception e) {
            MessageDialog.openError(getContainer().getShell(), Messages.getString("MultiPageSqlBuilderEditor.ErrorTitle"),
                    Messages.getString("MultiPageSqlBuilderEditor.ErrorInfo") + e.getMessage());
        }

    }

    private boolean isFirst = true;

    private boolean isFirst2 = true;

    /**
     * qzhang Comment method "attachListeners".
     */
    private void attachListeners() {
        sqlDesigner.getColorText().addModifyListener(new ModifyListener() {

            /*
             * (non-Javadoc)
             * 
             * @see org.eclipse.swt.events.ModifyListener#modifyText(org.eclipse.swt.events.ModifyEvent)
             */
            public void modifyText(ModifyEvent e) {
                if (!isFirst) {
                    updateEditorTitle(null);
                } else {
                    isFirst = false;
                }
            }

        });
        sqlEdit.getColorText().addModifyListener(new ModifyListener() {

            /*
             * (non-Javadoc)
             * 
             * @see org.eclipse.swt.events.ModifyListener#modifyText(org.eclipse.swt.events.ModifyEvent)
             */
            public void modifyText(ModifyEvent e) {
                if (!isFirst2) {
                    updateEditorTitle(null);
                } else {
                    isFirst2 = false;
                }
            }

        });
    }

    public void showDesignerPage() {
        setActivePage(1);
        erDiagramComposite.setSqlText(erDiagramComposite.getSqlText());
    }

    public void showEditPage() {
        setActivePage(0);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.part.MultiPageEditorPart#getActivePage()
     */
    @Override
    public int getActivePage() {
        return super.getActivePage();
    }

    public void setSqlText(String sql) {
        erDiagramComposite.setSqlText(sql);
    }

    public String getActivePageSqlString() {
        final int activePage = getActivePage();
        if (activePage == 0) {
            return sqlEdit.getSQLToBeExecuted();
        } else {
            return sqlDesigner.getSQLToBeExecuted();
        }
    }

    public RepositoryNode getActivePageRepositoryNode() {
        final int activePage = getActivePage();
        if (activePage == 0) {
            return sqlEdit.getRepositoryNode();
        } else {
            return sqlDesigner.getRepositoryNode();
        }

    }

    public boolean isModified() {
        if ("".equals(erDiagramComposite.getSqlText())) {
            return (!"".equals(sqlEdit.getSQLToBeExecuted()));
        } else {
            if ("".equals(sqlEdit.getSQLToBeExecuted())) {
                return true;
            } else {
                String orginSql = EMFRepositoryNodeManager.getInstance().initSqlStatement(sqlEdit.getSQLToBeExecuted(), false);
                String sqlText = EMFRepositoryNodeManager.getInstance().initSqlStatement(erDiagramComposite.getSqlText(), false);
                if (sqlText == null) {
                    return orginSql != null;
                } else {
                    if (orginSql == null) {
                        return true;
                    } else {
                        return !sqlText.trim().equals(orginSql.trim());
                    }
                }
            }
        }

    }

    /*
     * (non-Java)
     * 
     * @see org.eclipse.ui.part.EditorPart#doSave(org.eclipse.core.runtime.IProgressMonitor)
     */
    @Override
    public void doSave(IProgressMonitor monitor) {

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.part.MultiPageEditorPart#setFocus()
     */
    @Override
    public void setFocus() {
        int focusIndex = getActivePage();
        final Control control = getControl(focusIndex);
        if (control != null) {
            control.setFocus();
        }
    }

    /*
     * (non-Java)
     * 
     * @see org.eclipse.ui.part.EditorPart#doSaveAs()
     */
    @Override
    public void doSaveAs() {

    }

    /*
     * (non-Java)
     * 
     * @see org.eclipse.ui.part.EditorPart#isSaveAsAllowed()
     */
    @Override
    public boolean isSaveAsAllowed() {
        return false;
    }

    public void setRepositoryNode(RepositoryNode node) {
        sqlEdit.setRepositoryNode(node);
        sqlEdit.refresh(true);
        sqlDesigner.setRepositoryNode(node);
        sqlDesigner.refresh(true);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.part.MultiPageEditorPart#pageChange(int)
     */
    @Override
    public void pageChange(int newPageIndex) {
        setFocus();
        if (newPageIndex == 1) {
            try {
                String toSql = sqlEdit.getSQLToBeExecuted();
                EMFRepositoryNodeManager.getInstance().updateErDiagram(isModified(), erDiagramComposite, toSql, rootNode);
            } catch (Exception e) {
                MessageDialog.openError(getContainer().getShell(), Messages.getString("MultiPageSqlBuilderEditor.ErrorTitle"),
                        Messages.getString("MultiPageSqlBuilderEditor.ErrorInfo") + e.getMessage());
            }
        } else if (newPageIndex == 0) {
            if (isModified()) {
                Boolean iskeep = null;
                if (connParam.isNeedTakePrompt()) {
                    iskeep = MessageDialog.openQuestion(new Shell(), Messages
                            .getString("MultiPageSqlBuilderEditor.AddComment.Title"), //$NON-NLS-1$
                            Messages.getString("MultiPageSqlBuilderEditor.AddComment.Info")); //$NON-NLS-1$
                }
                connParam.setNeedTakePrompt(true);
                if (iskeep != null && iskeep) {
                    // String newSql = EMFRepositoryNodeManager.getInstance()
                    // .addComment(builderEditorComposite.getSQLToBeExecuted())
                    // + "\n";
                    // newSql += editor.getSqlText();
                    sqlEdit.setEditorContent(erDiagramComposite.getSqlText());
                }
            }
            sqlEdit.setModified(false);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.part.MultiPageEditorPart#getContainer()
     */
    @Override
    public Composite getContainer() {
        return super.getContainer();
    }

    public void updateEditorTitle(String text) {
        if (text == null) {
            text = this.tabItem.getText();
            if (!text.substring(0, 1).equals("*")) {
                text = "*" + text;
            }
            tabItem.setText(text);
        } else {
            tabItem.setText(text);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.part.WorkbenchPart#getTitle()
     */
    @Override
    public String getTitle() {
        if (tabItem != null) {
            return tabItem.getText();
        }
        return super.getTitle();
    }

    public SaveSQLAction getDeactivePageSaveSQLAction() {
        switch (getActivePage()) {
        case 1:
            return sqlEdit.getSaveSQLAction();
        case 0:
            return sqlDesigner.getSaveSQLAction();
        default:
            return null;
        }
    }

    public SaveAsSQLAction getActivePageSaveAsSQLAction() {
        switch (getActivePage()) {
        case 0:
            return sqlEdit.getSaveAsSQLAction();
        case 1:
            return sqlDesigner.getSaveAsSQLAction();
        default:
            return null;
        }
    }
    
    public void setItemData(Query query) {
        this.tabItem.setData(query);
    }
}
