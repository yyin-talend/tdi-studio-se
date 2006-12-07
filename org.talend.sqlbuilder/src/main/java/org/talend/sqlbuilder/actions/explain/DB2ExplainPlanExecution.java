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


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Random;

import net.sourceforge.sqlexplorer.sqlpanel.SQLResult;

import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TableTreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.talend.sqlbuilder.Messages;
import org.talend.sqlbuilder.SqlBuilderPlugin;
import org.talend.sqlbuilder.sessiontree.model.SessionTreeNode;
import org.talend.sqlbuilder.sqlcontrol.AbstractSQLExecution;

/**
 * DOC dev  class global comment. Detailled comment
 * <br/>
 *
 * $Id: talend-code-templates.xml 1 2006-09-29 17:06:40 +0000 (Fri, 29 Sep 2006) nrousseau $
 *
 */
public class DB2ExplainPlanExecution extends AbstractSQLExecution {

	/**
	 * DOC dev DB2ExplainPlanExecution class global comment. Detailled comment
	 * <br/>
	 *
	 * $Id: talend-code-templates.xml 1 2006-09-29 17:06:40 +0000 (Fri, 29 Sep 2006) nrousseau $
	 *
	 */
	static class TreeLabelProvider extends LabelProvider implements ITableLabelProvider {

        public Image getColumnImage(Object element, int columnIndex) {

            return null;
        }


        public String getColumnText(Object element, int columnIndex) {

            ExplainNode en = (ExplainNode) element;
            if (columnIndex == 0) {
                return en.toString();
            }
            if (columnIndex == 1) {
                int cost = en.getCost();
                if (cost != -1) {
                    return "" + cost;
                } else {
                    return "";
                }
            } else if (columnIndex == 2) {
                int card = en.getCardinality();
                if (card != -1) {
                    return "" + card;
                } else {
                    return "";
                }
            }
            return "";
        }
    }
	
	private PreparedStatement prepStmt;

    private SQLResult sqlResult;

    private Statement stmt;
    
	/**
	 * DOC dev DB2ExplainPlanExecution constructor comment.
	 */
	public DB2ExplainPlanExecution(String sqlString,
            SessionTreeNode sessionTreeNode) {
		this.sqlStatement = sqlString;
		session = sessionTreeNode;
		sqlResult = new SQLResult();
        sqlResult.setSqlStatement(sqlStatement);
        
//      set initial message
        setProgressMessage(Messages.getString("SQLResultsView.ConnectionWait"));
	}
	
	private void displayResults(final ExplainNode node) {

		 Display.getDefault().asyncExec(new Runnable() {

            @SuppressWarnings("deprecation")
			public void run() {

                clearCanvas();

                GridLayout gLayout = new GridLayout();
                gLayout.numColumns = 2;
                gLayout.marginLeft = 0;
                gLayout.horizontalSpacing = 0;
                gLayout.verticalSpacing = 0;
                gLayout.marginWidth = 0;
                gLayout.marginHeight = 0;
                composite.setLayout(gLayout);

                try {
                    composite.setData("parenttab", parentTab);

                    Composite pp = new Composite(composite, SWT.NULL);
                    pp.setLayout(new FillLayout());
                    pp.setLayoutData(new GridData(GridData.FILL_BOTH));
                    TableTreeViewer tv = new TableTreeViewer(pp, SWT.BORDER | SWT.FULL_SELECTION);
                    Table table = tv.getTableTree().getTable();
                    table.setLinesVisible(true);
                    table.setHeaderVisible(true);
                    TableColumn tc = new TableColumn(table, SWT.NULL);
                    tc.setText("");
                    tc = new TableColumn(table, SWT.NULL);
                    tc.setText("Cost");
                    tc = new TableColumn(table, SWT.NULL);
                    tc.setText("Cardinality");
                    TableLayout tableLayout = new TableLayout();
                    tableLayout.addColumnData(new ColumnWeightData(6, 150, true));
                    tableLayout.addColumnData(new ColumnWeightData(1, 50, true));
                    tableLayout.addColumnData(new ColumnWeightData(1, 50, true));
                    table.setLayout(tableLayout);

                    tv.setContentProvider(new ITreeContentProvider() {

                        public void dispose() {

                        }


                        public Object[] getChildren(Object parentElement) {

                            return ((ExplainNode) parentElement).getChildren();
                        }


                        public Object[] getElements(Object inputElement) {

                            ExplainNode nd = ((ExplainNode) inputElement);

                            return nd.getChildren();
                        }


                        public Object getParent(Object element) {

                            return ((ExplainNode) element).getParent();
                        }


                        public boolean hasChildren(Object element) {

                            if (((ExplainNode) element).getChildren().length > 0) {
                            	return true;
                            }
                            return false;
                        }


                        public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {

                        }
                    });
                    tv.setLabelProvider(new TreeLabelProvider() {
                    });
                    tv.setInput(node);
                    tv.refresh();
                    tv.expandAll();

                    // make columns full size
                    for (int i = 0; i < table.getColumnCount(); i++) {
                        table.getColumn(i).pack();
                    }
                    
                    final Composite parent = composite;                    
                    table.addKeyListener(new KeyAdapter() {
                        
                        public void keyReleased(KeyEvent e) {

                            switch (e.keyCode) {

                            case SWT.F5:

                                // refresh SQL Results
                                try {
                                    Object o = parent.getData("parenttab");
                                    if (o != null) {
                                        AbstractSQLExecution sqlExec = (AbstractSQLExecution) ((TabItem) o).getData();
                                        if (sqlExec != null) {
                                            sqlExec.startExecution();
                                        }
                                    }
                                } catch (Exception e1) {
                                    SqlBuilderPlugin.log("Error refreshing", e1);
                                }

                                break;
                            default:
                                return;

                            }

                        }

                    });
                    
                    
                } catch (Exception e) {

                    // add message
                    String message = e.getMessage();
                    Label errorLabel = new Label(composite, SWT.FILL);
                    errorLabel.setText(message);
                    errorLabel.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));

                    SqlBuilderPlugin.log("Error creating explain tab", e);
                }

                composite.layout();
                composite.redraw();

            };
        });
    }


    @SuppressWarnings("unchecked")
	protected void doExecution() throws Exception {

        try {

            setProgressMessage(Messages.getString("SQLResultsView.Executing"));

            int idp = new Random().nextInt(1000);

            prepStmt = connection.prepareStatement(
                    "delete from SYSTOOLS.explain_statement where queryno = ? ");
            prepStmt.setInt(1, idp);
            prepStmt.executeUpdate();
            prepStmt.close();
            prepStmt = null;

            if (isCancelled) {
                return;
            }
            
            stmt = connection.createStatement();
            stmt.execute("EXPLAIN PLAN SET queryno = " + idp + " FOR " + sqlStatement);
            stmt.close();
            stmt = null;

            if (isCancelled) {
                return;
            }
            
            prepStmt = connection.prepareStatement(
                    "SELECT O.Operator_ID as id, S2.Target_ID as parent_id, O.Operator_Type, "
                            + "S.OBJECT_SCHEMA, EOB.OBJECT_TYPE, S.Object_Name, O.CPU_COST,  "
                            + "CAST(O.Total_Cost AS INTEGER) Cost FROM SYSTOOLS.EXPLAIN_OPERATOR O "
                            + "LEFT OUTER JOIN SYSTOOLS.EXPLAIN_STREAM S2 ON O.Operator_ID=S2.Source_ID "
                            + "LEFT OUTER JOIN SYSTOOLS.EXPLAIN_STREAM S  ON O.Operator_ID = S.Target_ID "
                            + "AND O.Explain_Time = S.Explain_Time AND S.Object_Name IS NOT NULL "
                            + "LEFT OUTER JOIN SYSTOOLS.explain_object EOB ON O.Explain_Time = EOB.Explain_Time     "
                            + "AND S.OBJECT_NAME = EOB.OBJECT_NAME "
                            + "where o.explain_time =  (select max(explain_time) from SYSTOOLS.explain_statement where queryno = ?) "
                            + "ORDER BY O.Operator_ID ASC, S2.TARGET_ID ASC ");
            prepStmt.setInt(1, idp);
            ResultSet rs = prepStmt.executeQuery();

            if (isCancelled) {
                return;
            }
            
            HashMap mp = new HashMap();
            ExplainNode baseNode = new ExplainNode(null);
            mp.put(new Integer(-1), baseNode);
            int lastId = -1;

            while (rs.next()) {
                String objectType = rs.getString("object_type");
                String operation = rs.getString("operator_type");
                String options = null;
                String objectOwner = rs.getString("object_schema");
                String objectName = rs.getString("object_name");
                String optimizer = null;
                int cardinality = rs.getInt("cpu_cost");
                if (rs.wasNull()) {
                    cardinality = -1;
                }

                int cost = rs.getInt("cost");
                if (rs.wasNull()) {
                	cost = -1;
                	
                }
                int parentID = rs.getInt("parent_id");
                if (rs.wasNull()) {
                    parentID = -1;
                }
                int id = rs.getInt("id");

                if (id != lastId) {

                    lastId = id;
                    ExplainNode ndParent = (ExplainNode) mp.get(new Integer(parentID));
                    ExplainNode nd = new ExplainNode(ndParent);
                    mp.put(new Integer(id), nd);

                    ndParent.add(nd);
                    nd.setId(id);
                    nd.setCardinality(cardinality);
                    nd.setCost(cost);
                    nd.setObjectName(objectName);
                    nd.setObjectOwner(objectOwner);
                    nd.setObjectType(objectType);
                    nd.setOperation(operation);
                    nd.setOptimizer(optimizer);
                    nd.setOptions(options);
                }
            }
            rs.close();
            prepStmt.close();
            prepStmt = null;

            if (isCancelled) {
                return;
            }
            
            displayResults(baseNode);

        } catch (Exception e) {

            if (stmt != null) {

                try {
                    stmt.close();
                    stmt = null;
                } catch (Exception e1) {
                	SqlBuilderPlugin.log("Error closing statement.", e);
                }
            }

            if (prepStmt != null) {
                try {
                    prepStmt.close();
                    prepStmt = null;
                } catch (Exception e1) {
                	SqlBuilderPlugin.log("Error closing statement.", e);
                }
            }
            throw e;
        }

    }


    protected void doStop() throws Exception {

        Exception t = null;

        if (stmt != null) {

            try {
                stmt.cancel();
            } catch (Exception e) {
                t = e;
                SqlBuilderPlugin.log("Error cancelling statement.", e);
            }
            try {
                stmt.close();
                stmt = null;
            } catch (Exception e) {
            	SqlBuilderPlugin.log("Error closing statement.", e);
            }
        }

        if (prepStmt != null) {

            try {
                prepStmt.cancel();
            } catch (Exception e) {
                t = e;
                SqlBuilderPlugin.log("Error cancelling statement.", e);
            }
            try {
                prepStmt.close();
                prepStmt = null;
            } catch (Exception e) {
            	SqlBuilderPlugin.log("Error closing statement.", e);
            }
        }

        if (t != null) {
            throw t;
        }
    }

}
