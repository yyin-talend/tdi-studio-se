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

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.SelectionProviderAction;
import org.talend.core.ui.ImageProvider;
import org.talend.core.ui.ImageProvider.EImage;
import org.talend.repository.model.RepositoryNode;
import org.talend.repository.ui.views.RepositoryView;
import org.talend.sqlbuilder.actions.GenerateSelectSQLAction;
import org.talend.sqlbuilder.actions.MetadataRefreshAction;
import org.talend.sqlbuilder.actions.OpenNewEditorAction;
import org.talend.sqlbuilder.dbstructure.DBTreeLabelProvider;
import org.talend.sqlbuilder.dbstructure.RepositoryExtNode;
import org.talend.sqlbuilder.dbstructure.SessionTreeNodeUtils;
import org.talend.sqlbuilder.dbstructure.nodes.CatalogNode;
import org.talend.sqlbuilder.dbstructure.nodes.ColumnNode;
import org.talend.sqlbuilder.dbstructure.nodes.INode;
import org.talend.sqlbuilder.dbstructure.nodes.TableNode;
import org.talend.sqlbuilder.util.UIUtils;

/**
 * Detailled comment for this class. <br/> $Id: DBStructureComposite.java,v 1.40 2006/11/09 07:29:18 peiqin.hou Exp $
 * 
 * @author Hou Peiqin (Soyatec)
 * 
 */

public class DBStructureComposite extends Composite {

    private static final int METADATA_INDEX = 5;

    private static final int COLUMN_REPOSITORY_WIDTH = 100;

    private static final int COLUMN_DATABASE_WIDTH = 150;

    private TreeViewer treeViewer;

    private SQLBuilderDialog builderDialog;

    private IProgressMonitor progressMonitor;

    private RepositoryExtNode repositoryExtNode;
    
    private RepositoryNode rootRepositoryNode;
    
    private boolean isShowAllConnections;
    
    private Action openNewEditorAction;
    private Action refreshAllConnectionsAction;
    private Action refreshConnectionAction;
    private Action listAllConnectionsAction;
    private Action metadataRefreshAction;
    private Action generateSelectAction;
    private Separator separator = new Separator(IWorkbenchActionConstants.MB_ADDITIONS);
    /**
     * Create the composite.
     * 
     * @param parent parent
     * @param style style
     */
    public DBStructureComposite(Composite parent, int style) {
        super(parent, style);
        GridLayout layout = new GridLayout();
        layout.numColumns = 2;
        layout.marginHeight = 0;
        layout.marginWidth = 0;
        layout.marginTop = 0;
        layout.verticalSpacing = 0;
        layout.horizontalSpacing = 0;
        setLayout(layout);
        //
    }

    /**
     * @return ProgressMonitor
     */
    public IProgressMonitor getProgressMonitor() {
        return progressMonitor;
    }

    /**
     * @param progressMonitor ProgressMonitor.
     */
    public void setProgressMonitor(IProgressMonitor progressMonitor) {
        this.progressMonitor = progressMonitor;
    }

    public DBStructureComposite(SashForm sashFormStructureAndEditor, int none, SQLBuilderDialog dialog) {
        this(sashFormStructureAndEditor, none);
        this.builderDialog = dialog;
        createToolbar();
        createDBTree();
    }

    public void openNewEditor() {
        treeViewer.getTree().setSelection(treeViewer.getTree().getItem(0));
        Action tempOpenNewEditorAction = new OpenNewEditorAction(treeViewer, builderDialog.getEditorComposite(), builderDialog.getConnParameters(), true);
        tempOpenNewEditorAction.run();
    }
    
    /**
     * Create contents of the dialog.
     */
    protected void createDBTree() {
        RepositoryView repositoryView = (RepositoryView) PlatformUI.getWorkbench().getActiveWorkbenchWindow()
                .getActivePage().findView(RepositoryView.VIEW_ID);

        treeViewer = new TreeViewer(this);
        //
        treeViewer.getControl().setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true, 2, 1));
        treeViewer.setUseHashlookup(true);

        treeViewer.setContentProvider(new DBTreeLabelProvider());
        treeViewer.setLabelProvider(new DBTreeLabelProvider());

        Tree tree = treeViewer.getTree();
        TreeColumn database = new TreeColumn(tree, SWT.LEFT);
        database.setText("Databases");
        database.setWidth(COLUMN_DATABASE_WIDTH);

        TreeColumn repository = new TreeColumn(tree, SWT.RIGHT_TO_LEFT);
        repository.setText("Repository");
        repository.setWidth(COLUMN_REPOSITORY_WIDTH);

        tree.setHeaderVisible(true);

        rootRepositoryNode = repositoryView.getRoot().getChildren().get(METADATA_INDEX)
                        .getChildren().get(0);
        repositoryExtNode = new RepositoryExtNode(rootRepositoryNode);
        repositoryExtNode.setConnectionParameters(builderDialog.getConnParameters());
        treeViewer.setInput(repositoryExtNode);
        addContextMenu();

    }

    /*
     * private void doTest(RepositoryNode node) { node = node.getChildren().get(1).getChildren().get(0);
     * DatabaseConnection connection = null; MetadataTable metadataTable = null; DatabaseConnectionItem item = null;
     * String metadataTableLabel = (String) node.getProperties(EProperties.LABEL); item = (DatabaseConnectionItem)
     * node.getParent().getObject().getProperty().getItem(); connection = (DatabaseConnection) item.getConnection();
     * metadataTable = TableHelper.findByLabel(connection, metadataTableLabel);
     * 
     * MetadataColumnImpl column = (MetadataColumnImpl) metadataTable.getColumns().get(0);
     * 
     * System.out.println(column.getLabel()); System.out.println(column.getTalendType());
     * System.out.println(column.getSourceType()); System.out.println(column.getLength());
     * 
     * System.out.println(); }
     */

    /**
     * Add context menu.
     * 
     * method description.
     * 
     * @param
     * @return
     * @exception
     */
    private void addContextMenu() {
        MenuManager menuMgr = new MenuManager("Menu"); //$NON-NLS-1$
        menuMgr.setRemoveAllWhenShown(true);
        menuMgr.addMenuListener(new IMenuListener() {

            public void menuAboutToShow(IMenuManager manager) {
                fillContextMenu(manager);
            }

            @SuppressWarnings("unchecked")
            private void fillContextMenu(IMenuManager manager) {
                // if the node instanceof CatalogNode then the Generate action will not display.
                IStructuredSelection selection = (IStructuredSelection) treeViewer.getSelection();
                INode element = (INode) selection.getFirstElement();
                if (element != null && !(element instanceof CatalogNode)) {
                    if (generateSelectAction == null) {
                        generateSelectAction = new GenerateSelectSQLAction(null, builderDialog.getEditorComposite(), false);
                    }
                    ((GenerateSelectSQLAction)generateSelectAction).setSelectedNodes((INode[]) selection
                            .toList().toArray(new INode[] {}));
                    generateSelectAction.setText("Generate Select Statement");
                    generateSelectAction.setToolTipText("Generate Select Statement");
                    manager.add(generateSelectAction);
                }

                // open editor
                builderDialog.getConnParameters().setQuery("");
                if (openNewEditorAction == null) {
                    openNewEditorAction = new OpenNewEditorAction(treeViewer, builderDialog.getEditorComposite(), builderDialog.getConnParameters(), false);
                }
                openNewEditorAction.setText("New Editor");
                openNewEditorAction.setToolTipText("New Editor");
                manager.add(openNewEditorAction);

                // Separator
                manager.add(separator);
                
                // refresh
                if (refreshConnectionAction == null) {
                    refreshConnectionAction = new RefreshConnectionAction(treeViewer, "Refresh");
                }
                manager.add(refreshConnectionAction);
                manager.add(separator);

                // metadata refresh
                if (metadataRefreshAction == null) {
                    metadataRefreshAction = new MetadataRefreshAction(treeViewer);
                }
                manager.add(metadataRefreshAction);
            }
        });
        Menu contextMenu = menuMgr.createContextMenu(treeViewer.getTree());
        treeViewer.getTree().setMenu(contextMenu);
    }

    /**
     * create toobar.
     */
    private void createToolbar() {
        Label label = new Label(this, SWT.NONE);
        label.setText("  " + "Database Structure");
        label.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING));
        ToolBarManager toolBarMgr = new ToolBarManager(SWT.FLAT);
        toolBarMgr.createControl(this);
        if (listAllConnectionsAction == null) {
            listAllConnectionsAction = new ListAllConnectionAction(SWT.TOGGLE);
        }
        toolBarMgr.add(listAllConnectionsAction);
        if (refreshAllConnectionsAction == null) {
            refreshAllConnectionsAction = new RefreshAllConnectionAction();
        }
        toolBarMgr.add(refreshAllConnectionsAction);
        toolBarMgr.update(true);
        toolBarMgr.getControl().setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_END));
    }

    /**
     * RefreshAction.
     */
    private class ListAllConnectionAction extends Action {

        public ListAllConnectionAction(int style) {
            super("", style);
            setImageDescriptor(ImageProvider.getImageDesc(EImage.ADD_ICON));
            setToolTipText("Show All Connections");
        }

        @Override
        public void run() {
            final IRunnableWithProgress r = new IRunnableWithProgress() {

                public void run(final IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
                    monitor.beginTask("Refresh Connections", -1);
                    
                    if (isChecked()) {
                        repositoryExtNode.setShowAllConnections(true);
                        isShowAllConnections = true;
                        SessionTreeNodeUtils.getCatalogNodes().clear();
                        SessionTreeNodeUtils.getCatalogNodes().addAll(SessionTreeNodeUtils.getCachedAllNodes());
                    } else {
                        repositoryExtNode.setShowAllConnections(false);
                        isShowAllConnections = false;
                        SessionTreeNodeUtils.getCatalogNodes().clear();
                    }
                    final INode[] currentNodes = SessionTreeNodeUtils.getCurrentNodes(isShowAllConnections, rootRepositoryNode, builderDialog.getConnParameters());
                    Display.getDefault().asyncExec(new Runnable() {
                        public void run() {
                            if (treeViewer.getTree() != null && !treeViewer.getTree().isDisposed()) {
                                ((RepositoryExtNode)treeViewer.getInput()).setChildNodes(currentNodes);
                                treeViewer.refresh();
                            }
                        }
                    });
                    monitor.done();
                }
            };

            UIUtils.runWithProgress(r, true, getProgressMonitor(), getShell().getDisplay());
        }
    }

    /**
     * RefreshAction.
     */
    private class RefreshAllConnectionAction extends Action {
        public RefreshAllConnectionAction() {
            super("");
            setImageDescriptor(ImageProvider.getImageDesc(EImage.REFRESH_ICON));
            setToolTipText("Refresh");
        }
        @Override
        public void run() {
            final IRunnableWithProgress r = new IRunnableWithProgress() {

                public void run(final IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
                    monitor.beginTask("Refresh Connections", -1);

                    SessionTreeNodeUtils.getCachedAllNodes().clear();
                    SessionTreeNodeUtils.getCachedCurrentNodes().clear();
                    SessionTreeNodeUtils.getCatalogNodes().clear();
                    final INode[] currentNodes = SessionTreeNodeUtils.getCurrentNodes(isShowAllConnections, rootRepositoryNode, builderDialog.getConnParameters()); 
                    Display.getDefault().asyncExec(new Runnable() {

                        public void run() {
                            if (treeViewer.getTree() != null && !treeViewer.getTree().isDisposed()) {
                                ((RepositoryExtNode)treeViewer.getInput()).setChildNodes(currentNodes);
                                treeViewer.refresh();
                            }
                        }
                    });
                    monitor.done();
                }
            };

            UIUtils.runWithProgress(r, true, getProgressMonitor(), getShell().getDisplay());
        }
    }

    /**
     * RefreshAction.
     */
    private class RefreshConnectionAction extends SelectionProviderAction {
        protected RefreshConnectionAction(ISelectionProvider provider, String text) {
            super(provider, text);
            setImageDescriptor(ImageProvider.getImageDesc(EImage.REFRESH_ICON));
            setToolTipText("Refresh");
            init();
        }
        @Override
        public void selectionChanged(ISelection selection) {
            init();
        }
        @SuppressWarnings("unchecked")
        private void init() {
            ISelection selection = getSelectionProvider().getSelection();
            int i = 0;
            INode[] nodes = (INode[]) ((IStructuredSelection)selection).toList().toArray(new INode[]{});
            for (INode node : nodes) {
                if (node instanceof CatalogNode) {
                    i++;
                }
            }
            if(i > 1) {
                this.setEnabled(false);
            } else {
                this.setEnabled(true);
            }
        }
        @Override
        public void run() {
            final IStructuredSelection selection = (IStructuredSelection) treeViewer.getSelection();
            final IRunnableWithProgress r = new IRunnableWithProgress() {
                public void run(final IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
                    monitor.beginTask("Refresh Connections", -1);
                   
                    INode node = (INode) selection.getFirstElement();
                    if (node instanceof ColumnNode) {
                        node.getParent().getParent().getParent().refresh();
                        doRefresh(node.getParent().getParent().getParent());
                    } else if (node instanceof TableNode) {
                        node.getParent().getParent().refresh();
                        doRefresh(node.getParent().getParent());
                    } else if (node instanceof CatalogNode) {
                        node.refresh();
                        doRefresh(node);
                    }
                
                    monitor.done();
                }
                private void doRefresh(final INode refreshNode) {
                    Display.getDefault().asyncExec(new Runnable() {
                        public void run() {
                            if(treeViewer.getTree()!=null&&treeViewer.getTree().isDisposed()){
                                treeViewer.refresh(refreshNode);
                            }
                        }
                    });
                }
            };

            UIUtils.runWithProgress(r, true, getProgressMonitor(), getShell().getDisplay());
        }
    }

    /**
     * Close the connection.
     * 
     * @Override
     */
    public void dispose() {
        super.dispose();
    }

    /**
     * checkSubclass.
     */
    @Override
    protected void checkSubclass() {
    }

    /**
     * @return TreeViewer.s
     */
    public TreeViewer getTreeViewer() {
        return treeViewer;
    }
}