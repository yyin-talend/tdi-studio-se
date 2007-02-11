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
import java.util.List;

import org.eclipse.core.runtime.Preferences;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;
import org.talend.repository.model.RepositoryNode;
import org.talend.sqlbuilder.IConstants;
import org.talend.sqlbuilder.SqlBuilderPlugin;
import org.talend.sqlbuilder.erdiagram.ui.ErDiagramDialog;
import org.talend.sqlbuilder.repository.utility.EMFRepositoryNodeManager;
import org.talend.sqlbuilder.ui.ISQLBuilderDialog;
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

    private int max = Integer.parseInt(prefs.getString(IConstants.WARN_RESEULTS));

    private String queryDelimiter = prefs.getString(IConstants.QUERY_DELIMITER);

    private String alternateDelimiter = prefs.getString(IConstants.ALTERNATE_DELIMITER);

    private String commentDelimiter = prefs.getString(IConstants.COMMENT_DELIMITER);

    private RepositoryNode currentNode;

    private ConnectionParameters parameters;

    private ISQLBuilderDialog dialog;

    private Boolean isForce;

    private ImageDescriptor image = ImageUtil.getDescriptor("Images.OpenSQLIcon"); //$NON-NLS-1$

    private String currentSql;

    /**
     * qzhang GUIModificationQuery constructor comment.
     */
    public GUIModificationQueryAction(RepositoryNode node, ConnectionParameters parameters, ISQLBuilderDialog dialog) {
        this.currentNode = node;
        this.parameters = parameters;
        this.dialog = dialog;
        setToolTipText("GUI Modify The Query");
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
        return "GUI Modify The Query";
    }

    /*
     * (non-Java)
     * 
     * @see org.talend.sqlbuilder.actions.AbstractEditorAction#run()
     */
    @Override
    public void run() {
        currentSql = getEditor().getSQLToBeExecuted();
        QueryTokenizer qt = new QueryTokenizer(currentSql, queryDelimiter, alternateDelimiter, commentDelimiter);
        List<String> queryStrings = new ArrayList<String>();
        while (qt.hasQuery()) {
            String querySql = qt.nextQuery();
            // ignore commented lines.
            if (!querySql.startsWith("--")) { //$NON-NLS-1$
                queryStrings.add(querySql);
            }
        }
        if (queryStrings.size() < 1) {
            MessageDialog.openError(new Shell(), "Notice", "You must input one Sql Statement!");
            return;
        }
        if (queryStrings.size() > 1) {
            isForce = MessageDialog.openQuestion(new Shell(), "Notice",
                    "You only modify first Sql Statement by GUI, Would you like to continue?");
        }
        String string = queryStrings.get(0).toLowerCase().replaceAll("\n", " ");
        string.replaceAll("\t", " ");
        if (!string.startsWith("select ")) {
            MessageDialog.openError(new Shell(), "Notice", "GUI Sql Editor only use for 'select' Statement !");
            return;
        }
        if (isForce != null && !isForce.booleanValue()) {
            return;
        }
        List<RepositoryNode> selectedNodes = null;
        try {
            selectedNodes = EMFRepositoryNodeManager.getInstance().parseSqlStatement(string, currentNode);
        } catch (Exception e) {
            MessageDialog.openError(new Shell(), "Notice", "Your inputed Select Statement has some errors, Please Check it.");
        }
        String query = null;
        ErDiagramDialog erDiagramDialog = new ErDiagramDialog(dialog.getShell(), "GUI Modify The Query");
        erDiagramDialog.setNodes(selectedNodes);
        if (Window.OK == erDiagramDialog.open()) {
            query = erDiagramDialog.getSql();
        }
        if (query == null) {
            return;
        }
        query += ";\n";
        String targetSql = "";
        boolean isfirst = true;
        qt = new QueryTokenizer(currentSql, queryDelimiter, alternateDelimiter, commentDelimiter);
        while (qt.hasQuery()) {
            String querySql = qt.nextQuery();
            if (querySql.startsWith("--") || !isfirst) { //$NON-NLS-1$
                targetSql += querySql + ";\n";
            } else {
                targetSql += query;
                isfirst = false;
            }
        }
//        currentSql = currentSql.replaceFirst(queryStrings.get(0), query);
        getEditor().setEditorContent(targetSql);
    }

}
