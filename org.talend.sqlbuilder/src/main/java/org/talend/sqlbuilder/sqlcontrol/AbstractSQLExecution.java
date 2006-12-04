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
package org.talend.sqlbuilder.sqlcontrol;


import org.talend.sqlbuilder.Messages;
import org.talend.sqlbuilder.SqlBuilderPlugin;
import org.talend.sqlbuilder.sessiontree.model.SessionTreeNode;
import net.sourceforge.squirrel_sql.fw.sql.SQLConnection;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.TabItem;


/**
 * DOC dev  class global comment. Detailled comment
 * <br/>
 *
 * $Id: AbstractSQLExecution.java,v 1.13 2006/10/27 17:40:40 qiang.zhang Exp $
 *
 */
public abstract class AbstractSQLExecution {
    /**
     * DOC dev AbstrsactSQLExecution class global comment. Detailled comment
     * <br/>
     *
     * $Id: AbstractSQLExecution.java,v 1.13 2006/10/27 17:40:40 qiang.zhang Exp $
     *
     */
    private class LocalThread extends Thread {

        public void run() {

            try {

                while (connection == null) {

                    if (isCancelled) {
                        break;
                    }
                    connection = session.getQueuedConnection(connectionNumber);

                    if (connection == null) {
                        sleep(100);
                    }
                }

                if ((!isCancelled) && connection != null) {
                    doExecution();
                }

            } catch (final Exception e) {

                if (!(e instanceof java.sql.SQLException || e instanceof InterruptedException)) {
                    // only log non-sql errors
                    SqlBuilderPlugin.log("Error executing.", e); //$NON-NLS-1$
                }

                
                Display.getDefault().asyncExec(new Runnable() {

                    public void run() {

                        clearCanvas();
                        if (!(e instanceof InterruptedException)) {
                            MessageDialog.openError(null, Messages.getString("AbstractSQLExecution.Executing.Error"), //$NON-NLS-1$
                                    e.getMessage());
                        }
                        if (parentTab != null) {
                            parentTab.dispose();
                        }
                    }
                });

            } finally {

                session.releaseQueuedConnection(connectionNumber);
                connection = null;

            }
        }
    }

    private Integer connectionNumber;

    protected boolean isCancelled = false;

    protected Composite composite;

    private LocalThread executionThread;

    private Group group;

    protected CTabItem parentTab;

    private String progressMessage;


    protected SessionTreeNode session;

    protected String sqlStatement;

    protected SQLConnection connection;


    /**
     * Clear progress bar or results.
     */
    protected final void clearCanvas() {

        if (parentTab == null || parentTab.isDisposed()) {
            return;
        }

        if (isCancelled) {
            return;
        }

        // restore correct label
        parentTab.setText((String) parentTab.getData("tabLabel")); //$NON-NLS-1$

        Control[] children = composite.getChildren();

        if (children != null) {

            for (int i = 0; i < children.length; i++) {
                children[i].dispose();
            }
        }

        group = null;
    }


    /**
     * Display progress bar on tab until results are ready.
     */
    protected final void displayProgress() {

        clearCanvas();

        // set label to running
        parentTab.setText(Messages.getString("AbstractSQLExecution.Running")); //$NON-NLS-1$

        GridLayout gLayout = new GridLayout();
        gLayout.numColumns = 2;
        gLayout.marginLeft = 0;
        gLayout.horizontalSpacing = 0;
        gLayout.verticalSpacing = 0;
        gLayout.marginWidth = 0;
        gLayout.marginHeight = 50;
        composite.setLayout(gLayout);

        group = new Group(composite, SWT.NULL);
        group.setLayout(new GridLayout());
        group.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        group.setText(progressMessage);

        // add progress bar
        Composite pbComposite = new Composite(group, SWT.FILL);
        FillLayout pbLayout = new FillLayout();
        pbLayout.marginHeight = 2;
        pbLayout.marginWidth = 5;
        pbComposite.setLayout(pbLayout);
        pbComposite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        ProgressBar pb = new ProgressBar(pbComposite, SWT.HORIZONTAL | SWT.INDETERMINATE | SWT.BORDER);
        pb.setVisible(true);
        pb.setEnabled(true);

        pbComposite.layout();
        composite.layout();

    }


    /**
     * Main execution method. This method is called from a background thread.
     * 
     * @throws Exception
     */
    protected abstract void doExecution() throws Exception;


    /**
     * This method will be called from the UI thread when execution is cancelled
     * and the tab will be disposed. Do any cleanups required in here.
     */
    protected abstract void doStop() throws Exception;


    public final String getSqlStatement() {

        return sqlStatement;
    }


    public final void setComposite(Composite composite) {

        this.composite = composite;
    }


    public final void setParentTab(CTabItem parentTab) {

        this.parentTab = parentTab;
    }


    /**
     * @param progressMessage
     */
    public final void setProgressMessage(String progressMessageInput) {

        progressMessage = progressMessageInput;
        if (group != null) {

           Display.getDefault().asyncExec(new Runnable() {

                public void run() {

                    group.setText(progressMessage);
                    group.redraw();
                }
            });
        }
    }


    /**
     * Start exection
     */
    public final void startExecution() {

        connectionNumber = session.getQueuedConnectionNumber();
        
        // start progress bar
        displayProgress();

        // start execution in seperate thread
        executionThread = new LocalThread();
        executionThread.start();

    }


    /**
     * Cancel execution.
     */
    public final void stop() {

        try {

            isCancelled = true;

            doStop();

        } catch (final Exception e) {

            
            Display.getDefault().asyncExec(new Runnable() {

                public void run() {

                    MessageDialog.openError(null, Messages.getString("AbstractSQLExecution.Executing.Error"), e.getMessage()); //$NON-NLS-1$
                }
            });

        }

    }

}