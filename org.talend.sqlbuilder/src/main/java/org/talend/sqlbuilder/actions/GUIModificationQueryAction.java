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

import java.util.List;

import org.eclipse.core.runtime.Preferences;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;
import org.talend.repository.model.RepositoryNode;
import org.talend.sqlbuilder.IConstants;
import org.talend.sqlbuilder.Messages;
import org.talend.sqlbuilder.SqlBuilderPlugin;
import org.talend.sqlbuilder.erdiagram.ui.ErDiagramDialog;
import org.talend.sqlbuilder.repository.utility.EMFRepositoryNodeManager;
import org.talend.sqlbuilder.ui.ISQLBuilderDialog;
import org.talend.sqlbuilder.ui.SQLBuilderDesignerComposite;
import org.talend.sqlbuilder.ui.SqlEditDialog;
import org.talend.sqlbuilder.util.ConnectionParameters;
import org.talend.sqlbuilder.util.ImageUtil;
import org.talend.sqlbuilder.util.QueryTokenizer;

/**
 * qzhang class global comment. Detailled comment <br/>
 * 
 * $Id: talend-code-templates.xml 1 2007-2-9 下午01:06:55 (星期五, 29 九月 2006) qzhang $
 * 
 */
public class GUIModificationQueryAction extends AbstractEditorAction {

    private Preferences prefs = SqlBuilderPlugin.getDefault().getPluginPreferences();

    private String queryDelimiter = prefs.getString(IConstants.QUERY_DELIMITER);

    private String alternateDelimiter = prefs.getString(IConstants.ALTERNATE_DELIMITER);

    private String commentDelimiter = prefs.getString(IConstants.COMMENT_DELIMITER);

    private RepositoryNode currentNode;

    private ISQLBuilderDialog dialog;

    private ImageDescriptor image = ImageUtil.getDescriptor("Images.OpenSQLIcon"); //$NON-NLS-1$

    private String currentSql;

    /**
     * qzhang GUIModificationQuery constructor comment.
     */
    public GUIModificationQueryAction(RepositoryNode node, ConnectionParameters parameters, ISQLBuilderDialog dialog) {
        this.currentNode = node;
        this.dialog = dialog;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.sqlbuilder.actions.AbstractEditorAction#getToolTipText()
     */
    @Override
    public String getToolTipText() {
        if (getParentId() != null && getParentId().equals(SQLBuilderDesignerComposite.ID)) {
            return Messages.getString("GUIModificationQueryAction.TextDialog.TitleText");
        }
        return Messages.getString("GUIModificationQueryAction.ButtonText");
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.action.Action#getImageDescriptor()
     */
    @Override
    public ImageDescriptor getImageDescriptor() {
        return image;
    }

    /*
     * (non-Java)
     * 
     * @see org.talend.sqlbuilder.actions.AbstractEditorAction#getText()
     */
    @Override
    public String getText() {
        if (getParentId() != null && getParentId().equals(SQLBuilderDesignerComposite.ID)) {
            return Messages.getString("GUIModificationQueryAction.TextDialog.TitleText");
        }
        return Messages.getString("GUIModificationQueryAction.ButtonText");
    }

    /*
     * (non-Java)
     * 
     * @see org.talend.sqlbuilder.actions.AbstractEditorAction#run()
     */
    @Override
    public void run() {
        currentSql = getEditor().getSQLToBeExecuted();
        // QueryTokenizer qt = new QueryTokenizer(currentSql, queryDelimiter, alternateDelimiter, commentDelimiter);
        // List<String> queryStrings = new ArrayList<String>();
        // while (qt.hasQuery()) {
        // String querySql = qt.nextQuery();
        // // ignore commented lines.
        // if (!querySql.startsWith("--")) { //$NON-NLS-1$
        // queryStrings.add(querySql);
        // }
        // }
        // if (queryStrings.size() < 1) {
        // MessageDialog.openError(new Shell(), "Notice", "You must input one Sql Statement!");
        // return;
        // }
        // if (queryStrings.size() > 1) {
        // isForce = MessageDialog.openQuestion(new Shell(), "Notice",
        // "You only modify first Sql Statement by GUI, Would you like to continue?");
        // }
        // String string = queryStrings.get(0).toLowerCase().replaceAll("\n", " ");
        // string.replaceAll("\t", " ");
        // if (!string.startsWith("select ")) {
        // MessageDialog.openError(new Shell(), "Notice", "GUI Sql Editor only use for 'select' Statement !");
        // return;
        // }
        // if (isForce != null && !isForce.booleanValue()) {
        // return;
        // }
        String query = null;
        if (getParentId() != null && getParentId().equals(SQLBuilderDesignerComposite.ID)) {
            SqlEditDialog textDialog = new SqlEditDialog(dialog.getShell(), Messages
                    .getString("GUIModificationQueryAction.TextDialog.TitleText"), currentSql, currentNode);
            if (Window.OK == textDialog.open()) {
                query = textDialog.getSql();
            }
        } else {
            List<RepositoryNode> selectedNodes = null;
            try {
                selectedNodes = EMFRepositoryNodeManager.getInstance().parseSqlStatement(currentSql, currentNode);
            } catch (Exception e) {
                MessageDialog
                        .openError(
                                new Shell(),
                                Messages.getString("GUIModificationQueryAction.Error.Notice"), Messages.getString("GUIModificationQueryAction.SqlStatement.ErrorMsg")); //$NON-NLS-1$ //$NON-NLS-2$
            }
            if (selectedNodes == null) {
                return;
            }

            String info = Messages.getString("MultiPageSqlBuilderEditor.Notice.InformationNotFull");
            MessageDialog.openInformation(new Shell(), Messages.getString("GUIModificationQueryAction.Information.Msg"), info); //$NON-NLS-1$

            ErDiagramDialog erDiagramDialog = new ErDiagramDialog(dialog.getShell(), Messages
                    .getString("GUIModificationQueryAction.Dialog.TitleText"), currentNode); //$NON-NLS-1$
            erDiagramDialog.setDialog(dialog);
            erDiagramDialog.setNodes(selectedNodes);
            erDiagramDialog.setSqlText(currentSql);
            EMFRepositoryNodeManager.getInstance().setRoot(currentNode);
            if (Window.OK == erDiagramDialog.open()) {
                query = erDiagramDialog.getSql();
            }
        }
        if (query == null) {
            return;
        }
        query += ";\n";
        String targetSql = "";
        boolean isfirst = true;
        QueryTokenizer qt = new QueryTokenizer(currentSql, queryDelimiter, alternateDelimiter, commentDelimiter);
        while (qt.hasQuery()) {
            String querySql = qt.nextQuery();
            if (querySql.startsWith("--") || !isfirst) { //$NON-NLS-1$
                targetSql += querySql + ";\n";
            } else {
                targetSql += query;
                isfirst = false;
            }
        }
        if (!qt.hasQuery()) {
            targetSql = query;
        }
        // currentSql = currentSql.replaceFirst(queryStrings.get(0), query);
        getEditor().setEditorContent(targetSql);

    }

    
    public void setCurrentNode(RepositoryNode currentNode) {
        this.currentNode = currentNode;
    }

}
