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
package org.talend.sqlbuilder.editors;

import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.part.MultiPageEditorPart;
import org.talend.repository.model.RepositoryNode;
import org.talend.sqlbuilder.Messages;
import org.talend.sqlbuilder.erdiagram.ui.ErDiagramComposite;
import org.talend.sqlbuilder.repository.utility.EMFRepositoryNodeManager;
import org.talend.sqlbuilder.ui.ISQLBuilderDialog;
import org.talend.sqlbuilder.ui.SQLBuilderEditorComposite;
import org.talend.sqlbuilder.util.ConnectionParameters;

/**
 * qzhang class global comment. Detailled comment <br/>
 * 
 * $Id: talend-code-templates.xml 1 2007-2-9 上午10:33:48 (星期五, 29 九月 2006) qzhang $
 * 
 */
public class MultiPageSqlBuilderEditor extends MultiPageEditorPart {

    private SQLBuilderEditorComposite builderEditorComposite;

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

    private ErDiagramComposite editor;

    private ConnectionParameters connParam;

    private CTabItem tabItem;

    private boolean isDefaultEditor;

    private RepositoryNode rootNode;

    private ISQLBuilderDialog dialog;

    /*
     * (non-Java)
     * 
     * @see org.eclipse.ui.part.MultiPageEditorPart#createPages()
     */
    @Override
    protected void createPages() {
        try {
            builderEditorComposite = new SQLBuilderEditorComposite(this.getContainer(), tabItem, isDefaultEditor, connParam,
                    rootNode, dialog);
            builderEditorComposite.setEditorContent(connParam);
            builderEditorComposite.setRepositoryNode(rootNode);
            builderEditorComposite.setQueryObject(dialog.getConnParameters().getQueryObject());
            builderEditorComposite.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));

            int index = addPage(builderEditorComposite);
            setPageText(index, Messages.getString("MultiPageSqlBuilderEditor.EditTab.Text")); //$NON-NLS-1$

            editor = new ErDiagramComposite(this.getContainer(), SWT.VERTICAL);
            editor.setDialog(dialog);
            editor.setNodes(nodes);
            editor.setRootNode(rootNode);
            editor.setSqlText(builderEditorComposite.getSQLToBeExecuted());
            editor.setWeights(new int[] { 12, 1 });
            EMFRepositoryNodeManager.getInstance().setRoot(rootNode);
            index = addPage(editor);
            setPageText(index, Messages.getString("MultiPageSqlBuilderEditor.DesignerTab.Text")); //$NON-NLS-1$
        } catch (Exception e) {
            MessageDialog.openError(getContainer().getShell(), Messages
                    .getString("MultiPageSqlBuilderEditor.CreatePage.ErrorTitle"), e.getMessage());
        }

    }

    public void showDesignerPage() {
        setActivePage(1);
        editor.setSqlText(editor.getSqlStatement());
    }

    public void showEditPage() {
        setActivePage(0);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.part.MultiPageEditorPart#setActivePage(int)
     */
    @Override
    protected void setActivePage(int pageIndex) {
        super.setActivePage(pageIndex);

    }

    public void setSqlText(String sql) {
        editor.setSqlText(sql);
    }

    public boolean isModified() {
        if ("".equals(editor.getSqlText())) {
            return (!"".equals(builderEditorComposite.getSQLToBeExecuted()));
        } else {
            if ("".equals(builderEditorComposite.getSQLToBeExecuted())) {
                return true;
            } else {
                String orginSql = EMFRepositoryNodeManager.getInstance().initSqlStatement(
                        builderEditorComposite.getSQLToBeExecuted());
                String sqlText = EMFRepositoryNodeManager.getInstance().initSqlStatement(editor.getSqlText());
                return !sqlText.trim().equals(orginSql.trim());
            }
        }
        // return editor.isModified();

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

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.part.MultiPageEditorPart#getContainer()
     */
    @Override
    public Composite getContainer() {
        return super.getContainer();
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
                String toSql = builderEditorComposite.getSQLToBeExecuted();
                if (toSql != null && !"".equals(toSql) && isModified()) {
                    String info = Messages.getString("Notice.InformationNotFull");
                    // "GUI Sql Editor maybe not show all features of your Sql Statement!\n And your full sql Statement
                    // will show in buttom of the GUI.";
                    MessageDialog.openInformation(new Shell(),
                            Messages.getString("MultiPageSqlBuilderEditor.NoticeTitle.Text"), info); //$NON-NLS-1$

                    List<RepositoryNode> nodeSel = EMFRepositoryNodeManager.getInstance().parseSqlStatement(toSql, rootNode);
                    if (nodeSel.isEmpty()) {
                        return;
                    }
                    editor.updateNodes(nodeSel, toSql);
                }
                editor.setModified(false);
            } catch (Exception e) {
                MessageDialog.openError(getContainer().getShell(), Messages
                        .getString("MultiPageSqlBuilderEditor.CreatePage.ErrorTitle2"), e.getMessage());
            }
        } else if (newPageIndex == 0) {
            if (isModified()) {
                Boolean iskeep = MessageDialog.openQuestion(new Shell(), Messages
                        .getString("MultiPageSqlBuilderEditor.AddComment.Title"), //$NON-NLS-1$
                        Messages.getString("MultiPageSqlBuilderEditor.AddComment.Info")); //$NON-NLS-1$
                if (iskeep != null && iskeep) {
                    // String newSql = EMFRepositoryNodeManager.getInstance()
                    // .addComment(builderEditorComposite.getSQLToBeExecuted())
                    // + "\n";
                    // newSql += editor.getSqlText();
                    builderEditorComposite.setEditorContent(editor.getSqlText());
                }
            }
            builderEditorComposite.setModified(false);
        }
    }
}
