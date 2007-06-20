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
package org.talend.sqlbuilder.ui;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IProgressMonitorWithBlocking;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressIndicator;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.actions.SelectionProviderAction;
import org.talend.core.model.metadata.builder.connection.Query;
import org.talend.repository.IRepositoryChangedListener;
import org.talend.repository.RepositoryChangedEvent;
import org.talend.repository.model.RepositoryNode;
import org.talend.sqlbuilder.Messages;
import org.talend.sqlbuilder.SqlBuilderPlugin;
import org.talend.sqlbuilder.dbstructure.RepositoryNodeType;
import org.talend.sqlbuilder.dbstructure.SessionTreeNodeManager;
import org.talend.sqlbuilder.dbstructure.SessionTreeNodeUtils;
import org.talend.sqlbuilder.dbstructure.nodes.INode;
import org.talend.sqlbuilder.editors.MultiPageSqlBuilderEditor;
import org.talend.sqlbuilder.repository.utility.SQLBuilderRepositoryNodeManager;
import org.talend.sqlbuilder.util.ConnectionParameters;
import org.talend.sqlbuilder.util.ImageUtil;
import org.talend.sqlbuilder.util.UIUtils;

/**
 * This Dialog is used for building sql.
 * 
 * $Id: SQLBuilderDialog.java,v 1.44 2006/11/09 08:44:09 tangfn Exp $
 * 
 */
public class SQLBuilderDialog extends Dialog implements ISQLBuilderDialog, IRepositoryChangedListener {

    private boolean isFromRepositoryView = false;

    private DBDetailsComposite dbDetailsComposite;

    private DBStructureComposite structureComposite;

    private SQLBuilderTabComposite editorComposite;

    // Added by Tang Fengneng
    private ConnectionParameters connParameters;

    // Ends

    /**
     * The progress indicator control.
     */
    protected ProgressIndicator progressIndicator;

    /**
     * The progress monitor.
     */
    private ProgressMonitor progressMonitor = new ProgressMonitor();

    /**
     * SessionTreeNode Manager.
     */
    SessionTreeNodeManager nodeManager = new SessionTreeNodeManager();

    /**
     * Internal progress monitor implementation.
     */
    private class ProgressMonitor implements IProgressMonitorWithBlocking {

        private boolean fIsCanceled;

        protected boolean forked = false;

        protected boolean locked = false;

        public void beginTask(String name, int totalWork) {
            if (progressIndicator == null) {
                return;
            }
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
            if (progressIndicator == null) {
                return;
            }
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
     * Create the dialog.
     * 
     * @param parentShell
     */
    public SQLBuilderDialog(Shell parentShell) {
        super(parentShell);
        setShellStyle(SWT.DIALOG_TRIM | SWT.RESIZE | SWT.RESIZE | SWT.MIN | SWT.MAX);
        parentShell.setImage(ImageUtil.getImage("Images.title")); //$NON-NLS-1$
        SqlBuilderPlugin.getDefault().getRepositoryService().registerRepositoryChangedListener(this);
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
     * Create contents of the dialog.
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
        sashFormStructureAndEditor.setWeights(new int[] { 4, 6 });

        structureComposite.openNewEditor();

        // RefreshDetailCompositeAction refreshAction =
        new RefreshDetailCompositeAction(structureComposite.getTreeViewer());

        return container;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.sqlbuilder.ui.ISQLBuilderDialog#openEditor(org.talend.repository.model.RepositoryNode,
     * java.util.List, org.talend.sqlbuilder.util.ConnectionParameters, boolean)
     */
    public void openEditor(RepositoryNode node, List<String> repositoryName, ConnectionParameters connParam,
            boolean isDefaultEditor) {
        editorComposite.openNewEditor(node, repositoryName, connParam, isDefaultEditor);
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
        // SQLResultComposite resultView =
        new SQLResultComposite(sashFormResultAndDetail, SWT.BORDER);

    }

    /**
     * Creates the sql editor composite.
     * 
     * @param sashFormStructureAndEditor
     */
    private void createSQLEditor(SashForm sashFormStructureAndEditor) {

        editorComposite = new SQLBuilderTabComposite(sashFormStructureAndEditor, SWT.BORDER, this);
    }

    /**
     * Creates composite to display database structure.
     * 
     * @param sashFormStructureAndEditor
     */
    private void createDatabaseStructure(SashForm sashFormStructureAndEditor) {
        // if (connParameters.getMetadataTable() == null || (connParameters.isRepository() &&
        // connParameters.isSchemaRepository())) {
        // structureComposite = new DBStructureComposite(sashFormStructureAndEditor, SWT.BORDER, this);
        // } else {
        BuildInDBStructure buildInDBStructure = new BuildInDBStructure(sashFormStructureAndEditor, SWT.NONE | SWT.VERTICAL, this,
                connParameters);
        structureComposite = buildInDBStructure.getDbstructureCom();
        // }
        structureComposite.setProgressMonitor(this.getProgressMonitor());
    }

    /**
     * Create contents of the button bar.
     * 
     * @param parent
     */
    protected void createButtonsForButtonBar(Composite parent) {
        GridData data = new GridData(GridData.FILL_HORIZONTAL);
        parent.setLayoutData(data);

        // increment the number of columns in the button bar
        GridLayout layout = (GridLayout) parent.getLayout();
        layout.makeColumnsEqualWidth = false;
        layout.numColumns = 4;

        GridData gd = new GridData(GridData.FILL_HORIZONTAL);
        progressIndicator = new ProgressIndicator(parent);
        progressIndicator.setLayoutData(gd);

        gd = new GridData();
        gd.widthHint = 200;
        Label l = new Label(parent, SWT.NONE);
        l.setLayoutData(gd);

        createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, false);
        createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
    }

    @Override
    protected Button createButton(Composite parent, int id, String label, boolean defaultButton) {
        Button button = new Button(parent, SWT.PUSH);
        button.setText(label);
        button.setFont(JFaceResources.getDialogFont());
        button.setData(new Integer(id));
        button.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent event) {
                buttonPressed(((Integer) event.widget.getData()).intValue());
            }
        });
        if (defaultButton) {
            Shell shell = parent.getShell();
            if (shell != null) {
                shell.setDefaultButton(button);
            }
        }
        GridData data = new GridData(GridData.HORIZONTAL_ALIGN_END);
        int widthHint = convertHorizontalDLUsToPixels(IDialogConstants.BUTTON_WIDTH);
        Point minSize = button.computeSize(SWT.DEFAULT, SWT.DEFAULT, true);
        data.widthHint = Math.max(widthHint, minSize.x);
        button.setLayoutData(data);
        return button;
    }

    /**
     * Return the initial size of the dialog.
     */
    protected Point getInitialSize() {
        return new Point(800, 600);
    }

    public SQLBuilderTabComposite getEditorComposite() {
        return editorComposite;
    }

    @Override
    public boolean close() {
        SqlBuilderPlugin.getDefault().getRepositoryService().removeRepositoryChangedListener(this);

        clean();

        return super.close();
    }

    private void clean() {
        SessionTreeNodeUtils.dispose();
        nodeManager.clear();
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
     * Added by Tang Fengneng Sets the connParameters.
     * 
     * @param connParameters the connParameters to set
     */
    public void setConnParameters(ConnectionParameters connParameters) {
        this.connParameters = connParameters;
    }

    public ConnectionParameters getConnParameters() {
        return connParameters;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.dialogs.Dialog#okPressed()
     */
    public void okPressed() {
        String sql = ""; //$NON-NLS-1$
        // sql = editorComposite.getDefaultTabSql();
        sql = editorComposite.getCurrentTabSql();
        // if (ConnectionParameters.isJavaProject()) {
        // sql = sql.replace("\"", "\\" + "\"");
        // } else {
        // sql = sql.replace("'", "\\'");
        // }

        if (connParameters.isFromRepository()) {
            SQLBuilderRepositoryNodeManager manager = new SQLBuilderRepositoryNodeManager();
            List<Query> qs = new ArrayList<Query>();
            List<RepositoryNode> nodes = new ArrayList<RepositoryNode>();
            connParameters.setQuery(sql);
            boolean isInfo = false;
            final CTabFolder tabFolder = getEditorComposite().getTabFolder();
            final CTabItem[] items = tabFolder.getItems();

            for (int i = 0; i < items.length; i++) {
                CTabItem item = items[i];
                boolean isInfo2 = item.getText().startsWith(Messages.getString("SQLBuilderEditorComposite.titleQuery"))
                        && item.getData() instanceof Query;
                if (isInfo2) {
                    isInfo = true;
                    Query q = (Query) item.getData();
                    RepositoryNode node = null;
                    MultiPageSqlBuilderEditor meditor = null;
                    Object control = item.getData("KEY");
                    if (control instanceof MultiPageSqlBuilderEditor) {
                        meditor = (MultiPageSqlBuilderEditor) control;
                    }
                    if (meditor != null) {
                        q.setValue(meditor.getActivePageSqlString());
                        node = meditor.getActivePageRepositoryNode();
                    }
                    qs.add(q);
                    nodes.add(node);
                }
            }
            if (isInfo) {
                String title = Messages.getString("SQLBuilderDialog.SaveAllQueries.Title"); //$NON-NLS-1$
                String info = Messages.getString("SQLBuilderDialog.SaveAllQueries.Info"); //$NON-NLS-1$
                boolean openQuestion = MessageDialog.openQuestion(getShell(), title, info);
                if (openQuestion) {
                    for (int i = 0; i < nodes.size(); i++) {
                        RepositoryNode n = nodes.get(i);
                        Query q = qs.get(i);
                        if (q != null && n != null) {
                            manager.saveQuery(n, q);
                        }
                    }
                }
            }
        }
        super.okPressed();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.sqlbuilder.ui.ISQLBuilderDialog#refreshNode(org.talend.repository.model.RepositoryNode)
     */
    public void refreshNode(RepositoryNode node) {
        structureComposite.doRefresh(node);
    }

    /**
     * qianbing class global comment. Refreshes Detail Composite according to selection changing of the database
     * structure viewer. <br/>
     * 
     * $Id: talend-code-templates.xml,v 1.3 2006/11/01 05:38:28 nicolas Exp $
     * 
     */
    public class RefreshDetailCompositeAction extends SelectionProviderAction {

        /**
         * qianbing RefreshDetailCompositeAction constructor comment.
         * 
         * @param provider
         */
        public RefreshDetailCompositeAction(ISelectionProvider provider) {
            super(provider, "Refresh DetailComposite");
        }

        /*
         * (non-Java)
         * 
         * @see org.eclipse.ui.actions.SelectionProviderAction#selectionChanged(org.eclipse.jface.viewers.IStructuredSelection)
         */
        public void selectionChanged(final IStructuredSelection selection) {
            IRunnableWithProgress progress = new IRunnableWithProgress() {

                public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
                    monitor.beginTask("", IProgressMonitor.UNKNOWN); //$NON-NLS-1$

                    try {
                        INode node = null;
                        String msg = null;
                        if (!selection.isEmpty()) {
                            try {
                                final RepositoryNode repositoryNode = (RepositoryNode) selection.getFirstElement();
                                if (SQLBuilderRepositoryNodeManager.getRepositoryType(repositoryNode) == RepositoryNodeType.FOLDER) {
                                    return;
                                }
                                node = nodeManager.convert2INode(repositoryNode);
                            } catch (Exception e) {
                                msg = e.getMessage();
                                SqlBuilderPlugin.log(msg, e);
                            }
                            final INode argNode = node;
                            final String argMsg = msg;
                            Display.getDefault().asyncExec(new Runnable() {

                                public void run() {
                                    dbDetailsComposite.setSelectedNode(argNode, argMsg);
                                }
                            });
                        }
                    } finally {
                        monitor.done();
                    }
                }
            };

            UIUtils.runWithProgress(progress, true, getProgressMonitor(), getShell());

        }
    }

    public boolean isFromRepositoryView() {
        return this.isFromRepositoryView;
    }

    public void setFromRepositoryView(boolean isFromRepositoryView) {
        this.isFromRepositoryView = isFromRepositoryView;
    }

    public void repositoryChanged(RepositoryChangedEvent event) {
        clean();
        structureComposite.updateStructureView(event);
    }

    /*
     * (non-Java)
     * 
     * @see org.talend.sqlbuilder.ui.ISQLBuilderDialog#openEditor(org.talend.repository.model.RepositoryNode,
     * java.util.List, org.talend.sqlbuilder.util.ConnectionParameters, boolean, java.util.List)
     */
    public void openEditor(RepositoryNode node, List<String> repositoryName, ConnectionParameters connParam,
            boolean isDefaultEditor, List<RepositoryNode> nodeSel) {
        editorComposite.setNodesSel(nodeSel);
        editorComposite.openNewEditor(node, repositoryName, connParam, isDefaultEditor);
    }
}
