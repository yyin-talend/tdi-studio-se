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

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.widgets.Display;
import org.talend.sqlbuilder.Messages;
import org.talend.sqlbuilder.SqlBuilderPlugin;
import org.talend.sqlbuilder.sessiontree.model.SessionTreeNode;
import org.talend.sqlbuilder.sqlcontrol.SQLExecution;
import org.talend.sqlbuilder.ui.editor.ISQLEditor;
import org.talend.sqlbuilder.util.IConstants;
import org.talend.sqlbuilder.util.ImageUtil;
import org.talend.sqlbuilder.util.QueryTokenizer;
import org.talend.sqlbuilder.util.UIUtils;

/**
 * DOC dev class global comment. Detailled comment <br/>
 * 
 * $Id: ExecSQLAction.java,v 1.21 2006/11/03 03:19:05 qiang.zhang Exp $
 * 
 */
public class ExecSQLAction extends AbstractEditorAction {

    private ImageDescriptor img = ImageUtil.getDescriptor("Images.ExecSQLIcon");

    private IResultDisplayer resultViewer = null;

    /**
     * ExecSQLAction constructor.
     * 
     * @param resultViewer
     */
    public ExecSQLAction(IResultDisplayer resultViewer,ISQLEditor editor) {
        this.resultViewer = resultViewer;
        this.editor=editor;
        setActionDefinitionId(ICommandIds.EXEC_SQL);
    }

    public ImageDescriptor getImageDescriptor() {
        return img;
    }

    public String getText() {
        return Messages.getString("SQLEditor.Actions.Execute");
    }

    public String getToolTipText() {
        return Messages.getString("SQLEditor.Actions.Execute.ToolTip");
    }

    public void run() {

      
        int maxresults =IConstants.WARN_RESEULTS;
        try {
            boolean isLimit=editor.getIfLimit();
            
            if (isLimit) {
                String tmp =editor.getMaxResult();
                if (tmp != null && tmp.trim().length() != 0) {
                    maxresults = Integer.parseInt(tmp);
                }
            }
        } catch (final Exception e) {
            UIUtils.openErrorDialog("Invalid Row Limit", e);
            return;
        }
        try {

            if (maxresults < 0) {
                Exception e= new Exception(Messages.getString("SQLEditor.LimitRows.Error"));
                UIUtils.openErrorDialog(Messages.getString("SQLEditor.Error.InvalidRowLimit.Title"), e);
                return;
            }


            if (maxresults > IConstants.WARN_RESEULTS) {

                final int largeResults = maxresults;
                Display.getDefault().asyncExec(new Runnable() {

                    public void run() {

                        boolean okToExecute = MessageDialog.openConfirm(Display.getDefault().getShells()[0],
                                Messages.getString("SQLEditor.LimitRows.ConfirmNoLimit.Title"),
                                Messages.getString("SQLEditor.LimitRows.ConfirmNoLimit.Message"));

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

    protected void run(int maxRows) {

        SessionTreeNode runNode = editor.getSessionTreeNode();
        QueryTokenizer qt = new QueryTokenizer(getSQLToBeExecuted(), 
                IConstants.QUERY_DELIMITER, 
                IConstants.ALTERNATE_DELIMITER,
                IConstants.COMMENT_DELIMITER);
        
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
                    SQLExecution sqlExe=new SQLExecution(querySql,maxRows,runNode);
                    resultViewer.addSQLExecution(sqlExe);
//                    editor.setSQLRunTime(sqlExe.getSQLResult().getExecutionTimeMillis());
                }
            }
        } catch (Exception e) {
            SqlBuilderPlugin.log("Error creating sql execution tab", e);
        }
    }

    String getSQLToBeExecuted() {
        String sql= editor.getSQLToBeExecuted();
        return sql;
    }

}