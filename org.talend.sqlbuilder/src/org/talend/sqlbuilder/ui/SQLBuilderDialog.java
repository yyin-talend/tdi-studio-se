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
package org.talend.sqlbuilder.ui;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IProgressMonitorWithBlocking;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.ProgressIndicator;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.actions.SelectionProviderAction;
import org.talend.sqlbuilder.dbstructure.SessionTreeNodeUtils;
import org.talend.sqlbuilder.dbstructure.nodes.ColumnNode;
import org.talend.sqlbuilder.dbstructure.nodes.INode;
import org.talend.sqlbuilder.util.ConnectionParameters;

/**
 * This Dialog is used for building sql.
 * 
 * $Id: SQLBuilderDialog.java,v 1.35 2006/11/08 05:21:17 peiqin.hou Exp $
 * 
 */
public class SQLBuilderDialog extends Dialog {

    private DBDetailsComposite dbDetailsComposite;

    private DBStructureComposite structureComposite;

    private SQLBuilderTabComposite editorComposite;
    
    //Added by Tang Fengneng
    private ConnectionParameters connParameters;
    //Ends

    /**
     * The progress indicator control.
     */
    protected ProgressIndicator progressIndicator;

    /**
     * The progress monitor.
     */
    private ProgressMonitor progressMonitor = new ProgressMonitor();

    /**
     * Internal progress monitor implementation.
     */
    private class ProgressMonitor implements IProgressMonitorWithBlocking {

        private boolean fIsCanceled;

        protected boolean forked = false;

        protected boolean locked = false;

        public void beginTask(String name, int totalWork) {
            if (progressIndicator.isDisposed()) {
                return;
            }
            if (totalWork == UNKNOWN) {
                progressIndicator.beginAnimatedTask();
            } else {
                progressIndicator.beginTask(totalWork);
            }
        }

        public void done() {
            if (!progressIndicator.isDisposed()) {
                progressIndicator.sendRemainingWork();
                progressIndicator.done();
            }
        }

        public void setTaskName(String name) {
        }

        public boolean isCanceled() {
            return fIsCanceled;
        }

        public void setCanceled(boolean b) {
            fIsCanceled = b;
            if (locked) {
                clearBlocked();
            }
        }

        public void subTask(String name) {
        }

        public void worked(int work) {
            internalWorked(work);
        }

        public void internalWorked(double work) {
            if (!progressIndicator.isDisposed()) {
                progressIndicator.worked(work);
            }
        }

        /*
         * (non-Javadoc)
         * 
         * @see org.eclipse.core.runtime.IProgressMonitorWithBlocking#clearBlocked()
         */
        public void clearBlocked() {
        }

        /*
         * (non-Javadoc)
         * 
         * @see org.eclipse.core.runtime.IProgressMonitorWithBlocking#setBlocked(org.eclipse.core.runtime.IStatus)
         */
        public void setBlocked(IStatus reason) {
            locked = true;
        }
    }

    /**
     * Create the dialog
     * 
     * @param parentShell
     */
    public SQLBuilderDialog(Shell parentShell) {
        super(parentShell);
        setShellStyle(SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL | SWT.RESIZE | SWT.RESIZE | SWT.MIN | SWT.MAX);

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.window.Window#configureShell(org.eclipse.swt.widgets.Shell)
     */
    protected void configureShell(Shell shell) {
        super.configureShell(shell);
        // Set the title bar text
        shell.setText("SQL Builder"); //$NON-NLS-1$
    }

    /**
     * Create contents of the dialog
     * 
     * @param parent
     */
    protected Control createDialogArea(Composite parent) {
        Composite container = (Composite) super.createDialogArea(parent);
        // container.setLayout(new GridLayout());

        final SashForm mainSashForm = new SashForm(container, SWT.NONE | SWT.VERTICAL);
        mainSashForm.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

        final SashForm sashFormStructureAndEditor = new SashForm(mainSashForm, SWT.NONE);

        final SashForm sashFormResultAndDetail = new SashForm(mainSashForm, SWT.NONE);
        mainSashForm.setWeights(new int[] { 3, 1 });

        createResult(sashFormResultAndDetail);
        createDetail(sashFormResultAndDetail);
        sashFormResultAndDetail.setWeights(new int[] { 4, 3 });

        createDatabaseStructure(sashFormStructureAndEditor);
        createSQLEditor(sashFormStructureAndEditor);
        sashFormStructureAndEditor.setWeights(new int[] { 1, 3 });

        RefreshDetailCompositeAction refreshAction = new RefreshDetailCompositeAction(structureComposite
                .getTreeViewer());

        progressIndicator = new ProgressIndicator(container);
        GridData gd = new GridData(GridData.FILL_HORIZONTAL);
        progressIndicator.setLayoutData(gd);
        return container;
    }

    /**
     * Creates the sql detail composite.
     * 
     * @param sashFormResultAndDetail
     */
    private void createDetail(SashForm sashFormResultAndDetail) {
        dbDetailsComposite = new DBDetailsComposite(sashFormResultAndDetail, SWT.BORDER);
    }

    /**
     * Creates the composite to display sql execution result.
     * 
     * @param sashFormResultAndDetail
     */
    private void createResult(SashForm sashFormResultAndDetail) {
        SQLResultComposite resultView = new SQLResultComposite(sashFormResultAndDetail, SWT.BORDER);

    }

    /**
     * Creates the sql editor composite.
     * 
     * @param sashFormStructureAndEditor
     */
    private void createSQLEditor(SashForm sashFormStructureAndEditor) {
        editorComposite = new SQLBuilderTabComposite(sashFormStructureAndEditor, SWT.BORDER);
    }

    /**
     * Creates composite to display database structure.
     * 
     * @param sashFormStructureAndEditor
     */
    private void createDatabaseStructure(SashForm sashFormStructureAndEditor) {
        structureComposite = new DBStructureComposite(sashFormStructureAndEditor, SWT.BORDER, this);
    }

    /**
     * Create contents of the button bar
     * 
     * @param parent
     */
    protected void createButtonsForButtonBar(Composite parent) {
        createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, false);
        createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
    }

    /**
     * Return the initial size of the dialog
     */
    protected Point getInitialSize() {
        return new Point(800, 600);
    }

    /**
     * RefreshDetailCompositeAction
     */
    public class RefreshDetailCompositeAction extends SelectionProviderAction {

        /**
         * 
         */
        public RefreshDetailCompositeAction(ISelectionProvider provider) {
            super(provider, "Refresh DetailComposite");
        }

        public void selectionChanged(final IStructuredSelection selection) {
            BusyIndicator.showWhile(Display.getDefault(), new Runnable() {

                public void run() {
                    INode selectedNode = null;
                    if (selection != null && (selection.getFirstElement() instanceof INode)) {
                        selectedNode = (INode) selection.getFirstElement();
                        if (selectedNode instanceof ColumnNode) {
                            selectedNode = selectedNode.getParent();
                        }
                    }
                    dbDetailsComposite.setSelectedNode(selectedNode);
                }
            });
        }
    }

    public SQLBuilderTabComposite getEditorComposite() {
        return editorComposite;
    }

    @Override
    public boolean close() {
        SessionTreeNodeUtils.dispose();
        return super.close();
    }

    /**
     * Returns the progress monitor to use for operations run in this progress dialog.
     * 
     * @return the progress monitor
     */
    public IProgressMonitor getProgressMonitor() {
        return progressMonitor;
    }

    
    /**
     * Added by Tang Fengneng
     * Sets the connParameters.
     * @param connParameters the connParameters to set
     */
    public void setConnParameters(ConnectionParameters connParameters) {
        this.connParameters = connParameters;
    }//Ends

    public ConnectionParameters getConnParameters() {
        ConnectionParameters connectionParameters = new ConnectionParameters();
        connectionParameters.setDbName("test");
        connectionParameters.setDbType("MySQL");
        connectionParameters.setUserName("root");
        connectionParameters.setPassword("root");
        connectionParameters.setHost("127.0.0.1");
        connectionParameters.setPort("3306");
        return connectionParameters;
    }
    
    
}
