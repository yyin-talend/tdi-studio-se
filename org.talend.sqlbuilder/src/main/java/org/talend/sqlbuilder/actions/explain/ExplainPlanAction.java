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
package org.talend.sqlbuilder.actions.explain;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.squirrel_sql.fw.sql.SQLConnection;

import org.eclipse.jface.dialogs.MessageDialog;
import org.talend.repository.model.RepositoryNode;
import org.talend.sqlbuilder.Messages;
import org.talend.sqlbuilder.SqlBuilderPlugin;
import org.talend.sqlbuilder.actions.AbstractEditorAction;
import org.talend.sqlbuilder.actions.IResultDisplayer;
import org.talend.sqlbuilder.dbstructure.SessionTreeNodeManager;
import org.talend.sqlbuilder.sessiontree.model.SessionTreeNode;
import org.talend.sqlbuilder.sqlcontrol.SQLExecution;
import org.talend.sqlbuilder.ui.editor.ISQLEditor;
import org.talend.sqlbuilder.util.IConstants;
import org.talend.sqlbuilder.util.QueryTokenizer;



/**
 * DOC dev  class global comment. Detailled comment
 * <br/>
 *
 * $Id: talend-code-templates.xml 1 2006-09-29 17:06:40 +0000 (Fri, 29 Sep 2006) nrousseau $
 *
 */
public class ExplainPlanAction extends AbstractEditorAction {

	static String createPlanTableScript = "CREATE TABLE PLAN_TABLE ("
        + "  STATEMENT_ID                    VARCHAR2(30)," + " TIMESTAMP                       DATE,"
        + "  REMARKS                         VARCHAR2(80)," + "  OPERATION                       VARCHAR2(30),"
        + "  OPTIONS                         VARCHAR2(30)," + "  OBJECT_NODE                     VARCHAR2(128),"
        + "  OBJECT_OWNER                    VARCHAR2(30)," + "  OBJECT_NAME                     VARCHAR2(30),"
        + "  OBJECT_INSTANCE                 NUMBER(38)," + "  OBJECT_TYPE                     VARCHAR2(30),"
        + "  OPTIMIZER                       VARCHAR2(255)," + "  SEARCH_COLUMNS                  NUMBER,"
        + "  ID                              NUMBER(38)," + "  PARENT_ID                       NUMBER(38),"
        + "  POSITION                        NUMBER(38)," + "  COST                            NUMBER(38),"
        + "  CARDINALITY                     NUMBER(38)," + "  BYTES                           NUMBER(38),"
        + "  OTHER_TAG                       VARCHAR2(255)," + "  PARTITION_START                 VARCHAR2(255),"
        + "  PARTITION_STOP                  VARCHAR2(255)," + "  PARTITION_ID                    NUMBER(38),"
        + "  OTHER                           LONG," + "  DISTRIBUTION                    VARCHAR2(30)" + ")";


	private ISQLEditor editor;
	private IResultDisplayer resultDisplayer;
	/**
	 * DOC dev ExplainPlanAction constructor comment.
	 */
	public ExplainPlanAction(IResultDisplayer resultDisplayer, ISQLEditor editor) {
		super();
		this.editor = editor;
		this.resultDisplayer = resultDisplayer;
	}

	/* (non-Javadoc)
	 * @see org.talend.sqlbuilder.actions.AbstractEditorAction#getText()
	 */
	@Override
	public String getText() {
		return Messages.getString("oracle.editor.actions.explain");
	}

	/* (non-Javadoc)
	 * @see org.talend.sqlbuilder.actions.AbstractEditorAction#run()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void run() {

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
        QueryTokenizer qt = new QueryTokenizer(getSQLToBeExecuted(), IConstants.QUERY_DELIMITER,
                IConstants.ALTERNATE_DELIMITER, IConstants.COMMENT_DELIMITER);
        List queryStrings = new ArrayList();
        while (qt.hasQuery()) {
            final String querySql = qt.nextQuery();
            // ignore commented lines.
            if (!querySql.startsWith("--")) {
                queryStrings.add(querySql);
            }
        }
        
        // check if we can run explain plans
        try {
            Statement st = runNode.getInteractiveConnection().createStatement();
            boolean createPlanTable = false;
            boolean notFoundTable = true;
            try {
                ResultSet rs = st.executeQuery("select statement_id from plan_table");
                notFoundTable = false;
                rs.close();
            } catch (Throwable e) {
                createPlanTable = MessageDialog.openQuestion(null,
                        Messages.getString("oracle.editor.actions.explain.notFound.Title"),
                        Messages.getString("oracle.editor.actions.explain.notFound"));
            } finally {
                try {
                    st.close();
                } catch (Throwable e) {
                	SqlBuilderPlugin.log("Error creating the plan table", e);
                }
            }
            if (notFoundTable && !createPlanTable) {
                return;
            }

            if (notFoundTable && createPlanTable) {

                SQLConnection conn = runNode.getInteractiveConnection();
                st = conn.createStatement();

                try {
                    st.execute(createPlanTableScript);

                    if (!conn.getAutoCommit()) {
                        conn.commit();
                    }

                } catch (Throwable e) {
                    SqlBuilderPlugin.log("Error creating the plan table", e);
                    MessageDialog.openError(null,
                            Messages.getString("oracle.editor.actions.explain.createError.Title"),
                            Messages.getString("oracle.editor.actions.explain.createError"));
                    try {
                        st.close();
                    } catch (Throwable e1) {
                    }
                    return;
                }
                try {
                    st.close();
                } catch (Throwable e) {
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        // execute explain plan for all statements

        try {
            while (!queryStrings.isEmpty()) {

                String querySql = (String) queryStrings.remove(0);

                if (querySql != null) {
                    resultDisplayer.addSQLExecution(new ExplainPlanExecution(querySql, runNode));
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
