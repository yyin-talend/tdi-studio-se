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
import org.eclipse.swt.widgets.Display;
import org.talend.repository.model.RepositoryNode;
import org.talend.sqlbuilder.IConstants;
import org.talend.sqlbuilder.Messages;
import org.talend.sqlbuilder.SqlBuilderPlugin;
import org.talend.sqlbuilder.dbstructure.SessionTreeNodeManager;
import org.talend.sqlbuilder.sessiontree.model.SessionTreeNode;
import org.talend.sqlbuilder.sqlcontrol.SQLExecution;
import org.talend.sqlbuilder.ui.editor.ISQLEditor;
import org.talend.sqlbuilder.util.ImageUtil;
import org.talend.sqlbuilder.util.QueryTokenizer;
import org.talend.sqlbuilder.util.UIUtils;

/**
 * This class is used for executing sql query. <br/>
 * 
 * $Id: ExecSQLAction.java,v 1.21 2006/11/03 03:19:05 qiang.zhang Exp $
 * 
 */
public class ExecSQLAction extends AbstractEditorAction {

    private ImageDescriptor img = ImageUtil.getDescriptor("Images.ExecSQLIcon");

    private IResultDisplayer resultViewer = null;

    private Preferences prefs = SqlBuilderPlugin.getDefault().getPluginPreferences();

    private int max = Integer.parseInt(prefs.getString(IConstants.WARN_RESEULTS));
    private String queryDelimiter = prefs.getString(IConstants.QUERY_DELIMITER);
    private String alternateDelimiter = prefs.getString(IConstants.ALTERNATE_DELIMITER);
    private String commentDelimiter = prefs.getString(IConstants.COMMENT_DELIMITER);
    /**
     * ExecSQLAction constructor.
     * 
     * @param resultViewer
     */
    public ExecSQLAction(IResultDisplayer resultViewer, ISQLEditor editor) {
        this.resultViewer = resultViewer;
        this.editor = editor;
        setActionDefinitionId(ICommandIds.EXEC_SQL);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.action.Action#getImageDescriptor()
     */
    public ImageDescriptor getImageDescriptor() {
        return img;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.sqlbuilder.actions.AbstractEditorAction#getText()
     */
    public String getText() {
        return Messages.getString("SQLEditor.Actions.Execute");
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.sqlbuilder.actions.AbstractEditorAction#getToolTipText()
     */
    public String getToolTipText() {
        return Messages.getString("SQLEditor.Actions.Execute.ToolTip");
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.sqlbuilder.actions.AbstractEditorAction#run()
     */
    public void run() {
        int maxresults = max;
        try {
            boolean isLimit = editor.getIfLimit();
            if (isLimit) {
                String tmp = editor.getMaxResult();
                if (tmp != null && tmp.trim().length() != 0) {
                    maxresults = Integer.parseInt(tmp);
                }
                if (maxresults < 0) {
                    new Exception(Messages.getString("SQLEditor.LimitRows.Error"));
                }
            }
        } catch (final Exception e) {
            UIUtils.openErrorDialog(Messages.getString("SQLEditor.Error.InvalidRowLimit.Title"), e);
            return;
        }
        try {
            if (maxresults > max) {
                final int largeResults = maxresults;
                Display.getDefault().asyncExec(new Runnable() {

                    public void run() {
                        boolean okToExecute = MessageDialog.openConfirm(Display.getDefault().getShells()[0], Messages
                                .getString("SQLEditor.LimitRows.ConfirmNoLimit.Title"), Messages
                                .getString("SQLEditor.LimitRows.ConfirmNoLimit.Message"));
                        if (okToExecute) {
                            ExecSQLAction.this.run(largeResults);
                        }
                    }
                });
            } else {
                run(maxresults);
            }
        } catch (final Exception e) {
            UIUtils.openErrorDialog(Messages.getString("SQLResultsView.Error.Title"), e);
        }
    }

    /**
     * DOC qianbing Comment method "run". Processes the database operation.
     * 
     * @param maxRows
     */
    protected void run(int maxRows) {
        RepositoryNode node = editor.getRepositoryNode();
        SessionTreeNodeManager nodeManager = new SessionTreeNodeManager();
        SessionTreeNode runNode = null;
        try {
            runNode = nodeManager.getSessionTreeNode(node);
        } catch (Exception e) {
            MessageDialog.openError(null, Messages.getString("AbstractSQLExecution.Executing.Error"), e.getMessage()); //$NON-NLS-1$
            SqlBuilderPlugin.log("Gets SessionTreeNode failed", e);
            return;
        }
        
        QueryTokenizer qt = new QueryTokenizer(getSQLToBeExecuted(), queryDelimiter,
        		alternateDelimiter, commentDelimiter);

        List<String> queryStrings = new ArrayList<String>();
        while (qt.hasQuery()) {
            String querySql = qt.nextQuery();
            // ignore commented lines.
            if (!querySql.startsWith("--")) {
                queryStrings.add(querySql);
            }
        }
        try {
            // Diaplay data in sqlResult Composites
            while (!queryStrings.isEmpty()) {
                String querySql = (String) queryStrings.remove(0);
                if (querySql != null) {
                    SQLExecution sqlExe = new SQLExecution(querySql, maxRows, runNode);
                    resultViewer.addSQLExecution(sqlExe);
                    // editor.setSQLRunTime(sqlExe.getSQLResult().getExecutionTimeMillis());
                }
            }
        } catch (Exception e) {
            SqlBuilderPlugin.log("Error creating sql execution tab", e);
        }
    }

    /**
     * Gets sql for executing.
     * 
     * @return string
     */
    public String getSQLToBeExecuted() {
        String sql = editor.getSQLToBeExecuted();
        return sql;
    }

}
