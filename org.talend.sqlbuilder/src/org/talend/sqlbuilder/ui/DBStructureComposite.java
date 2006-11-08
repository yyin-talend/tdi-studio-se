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
import org.eclipse.jface.resource.ImageDescriptor;
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
import org.talend.core.ui.ImageProvider;
import org.talend.core.ui.ImageProvider.EImage;
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
 * Detailled comment for this class. <br/> $Id: DBStructureComposite.java,v 1.28 2006/11/08 05:21:17 peiqin.hou Exp $
 * 
 * @author Hou Peiqin (Soyatec)
 * 
 */

public class DBStructureComposite extends Composite {

    private static final int METADATA_INDEX = 5;

    private static final int COLUMN_REPOSITORY_WIDTH = 100;

    private static final int COLUMN_DATABASE_WIDTH = 150;

    private TreeViewer treeViewer;
    
    private Action openNewEditorAction;
    
    private SQLBuilderDialog builderDialog;
    
    private IProgressMonitor progressMonitor;
    
    private RepositoryExtNode repositoryExtNode;
    
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

    public DBStructureComposite(SashForm sashFormStructureAndEditor, int none, SQLBuilderDialog dialog)
    {
        this(sashFormStructureAndEditor, none);
        this.builderDialog = dialog;
        createToolbar();
        createDBTree();
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
        
        repositoryExtNode = new RepositoryExtNode(repositoryView.getRoot().getChildren().get(METADATA_INDEX).getChildren().get(0));
        repositoryExtNode.setConnectionParameters(builderDialog.getConnParameters());
        treeViewer.setInput(repositoryExtNode);
        addContextMenu();
        
    }
    
    /*
    private void doTest(RepositoryNode node)
    {
        node = node.getChildren().get(1).getChildren().get(0);
        DatabaseConnection connection = null;
        MetadataTable metadataTable = null;
        DatabaseConnectionItem item = null;
        String metadataTableLabel = (String) node.getProperties(EProperties.LABEL);
        item = (DatabaseConnectionItem) node.getParent().getObject().getProperty().getItem();
        connection = (DatabaseConnection) item.getConnection();
        metadataTable = TableHelper.findByLabel(connection, metadataTableLabel);
        
        MetadataColumnImpl column = (MetadataColumnImpl) metadataTable.getColumns().get(0);
        
        System.out.println(column.getLabel());
        System.out.println(column.getTalendType());
        System.out.println(column.getSourceType());
        System.out.println(column.getLength());
        
        System.out.println();
    }
    */

    /**
     * Add context menu.
     * 
     * method description.
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
                //if the node instanceof CatalogNode then the Generate action will not display.
                IStructuredSelection selection = (IStructuredSelection) treeViewer.getSelection();
                INode element = (INode) selection.getFirstElement();
                if (element != null && !(element instanceof CatalogNode)) {
                    GenerateSelectSQLAction generateSelectAction = new GenerateSelectSQLAction((
                            INode[]) selection.toList().toArray(new INode[]{}), builderDialog.getEditorComposite());
                    generateSelectAction.setText("Generate Select Statement");
                    generateSelectAction.setToolTipText("Generate Select Statement");
                    manager.add(generateSelectAction);
                }
                
                //open editor
                if (openNewEditorAction == null) {
                    openNewEditorAction = new OpenNewEditorAction(treeViewer, builderDialog.getEditorComposite());
                    openNewEditorAction.setText("New Editor");
                    openNewEditorAction.setToolTipText("New Editor");
                }
                manager.add(openNewEditorAction);
                
                //Separator
                manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
                
                //refresh
                manager.add(new RefreshConnectionAction());
                manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
                
                //metadata refresh
                manager.add(new MetadataRefreshAction(treeViewer));
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
        toolBarMgr.add(new ListAllConnectionAction(SWT.TOGGLE));
        toolBarMgr.add(new RefreshAllConnectionAction());
        toolBarMgr.update(true);
        toolBarMgr.getControl().setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_END));
    }
    
    /**
     * RefreshAction.
     */
    private class ListAllConnectionAction extends Action {

        public ListAllConnectionAction(int style) {
            super("", style);
        }
        /**
         * run.
         */
        @Override
        public void run() {
            if(isChecked()) {
                repositoryExtNode.setShowAllConnections(true);
                SessionTreeNodeUtils.getCatalogNodes().clear();
                SessionTreeNodeUtils.getCatalogNodes().addAll(SessionTreeNodeUtils.getCachedAllNodes());
            } else {
                repositoryExtNode.setShowAllConnections(false);
                SessionTreeNodeUtils.getCatalogNodes().clear();
            }
            treeViewer.refresh();
        }
        
        /**
         * @return HoverImageDescriptor
         */
        @Override
        public ImageDescriptor getHoverImageDescriptor() {
            return ImageProvider.getImageDesc(EImage.ADD_ICON);
        }
        
        /**
         * @return ImageDescriptor
         */
        @Override
        public ImageDescriptor getImageDescriptor() {
            return ImageProvider.getImageDesc(EImage.ADD_ICON);
        }
        
        /**
         * @return Text.
         */
        @Override
        public String getText() {
            return "";
        }
        
        /**
         * @return ToolTipText.
         */
        @Override
        public String getToolTipText() {
            return "Show All Connections";
        }
        
        
    }
    
    /**
     * RefreshAction.
     */
    private class RefreshAllConnectionAction extends Action {
        
        /**
         * run.
         */
        @Override
        public void run() {
//            final IRunnableWithProgress r = new IRunnableWithProgress() {
//                 public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
//                     monitor.beginTask("Refresh Connections", -1);
////                     for (int i = 0; i < 100; i++) {
////                         monitor.worked(1);
////                         try {
////                             Thread.sleep(50);
////                         } catch (Exception e) {
////                         }
////                     }
//                     treeViewer.refresh();
//                     monitor.done();
//               }
//             };
//             
//             UIUtils.runWithProgress(r, true, monitor, Display.getCurrent());
            SessionTreeNodeUtils.getCachedAllNodes().clear();
            SessionTreeNodeUtils.getCatalogNodes().clear();
            treeViewer.refresh();
        }
        
        /**
         * @return HoverImageDescriptor
         */
        @Override
        public ImageDescriptor getHoverImageDescriptor() {
            return ImageProvider.getImageDesc(EImage.REFRESH_ICON);
        }
        
        /**
         * @return ImageDescriptor
         */
        @Override
        public ImageDescriptor getImageDescriptor() {
            return ImageProvider.getImageDesc(EImage.REFRESH_ICON);
        }
        
        /**
         * @return Text.
         */
        @Override
        public String getText() {
            return "Refresh";
        }
        
        /**
         * @return ToolTipText.
         */
        @Override
        public String getToolTipText() {
            return "Refresh";
        }
        
        
    }
    
    /**
     * RefreshAction.
     */
    private class RefreshConnectionAction extends Action {
        
        /**
         * run.
         */
        @Override
        public void run() {
            IStructuredSelection selection = (IStructuredSelection) treeViewer.getSelection();
            INode node = (INode) selection.getFirstElement();
            if (node instanceof ColumnNode) {
                node.getParent().getParent().getParent().refresh();
                treeViewer.refresh(node.getParent().getParent().getParent());
            } else if (node instanceof TableNode) {
                node.getParent().getParent().refresh();
                treeViewer.refresh(node.getParent().getParent());
            } else if (node instanceof CatalogNode) {
                node.refresh();
                treeViewer.refresh(node);
            }
                
        }
        
        /**
         * @return HoverImageDescriptor
         */
        @Override
        public ImageDescriptor getHoverImageDescriptor() {
            return ImageProvider.getImageDesc(EImage.REFRESH_ICON);
        }
        
        /**
         * @return ImageDescriptor
         */
        @Override
        public ImageDescriptor getImageDescriptor() {
            return ImageProvider.getImageDesc(EImage.REFRESH_ICON);
        }
        
        /**
         * @return Text.
         */
        @Override
        public String getText() {
            return "Refresh";
        }
        
        /**
         * @return ToolTipText.
         */
        @Override
        public String getToolTipText() {
            return "Refresh";
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
