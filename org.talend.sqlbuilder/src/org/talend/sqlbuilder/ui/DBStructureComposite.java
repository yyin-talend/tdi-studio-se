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

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
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
import org.talend.sqlbuilder.dbstructure.nodes.CatalogNode;
import org.talend.sqlbuilder.dbstructure.nodes.INode;

/**
 * Detailled comment for this class. <br/> $Id: DBStructureComposite.java,v 1.26 2006/11/01 06:56:31 peiqin.hou Exp $
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
    private Action refreshAction;
    
    private SQLBuilderDialog builderDialog; 
    
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
        
        createToolbar();
        createDBTree();
        //
    }


    public DBStructureComposite(SashForm sashFormStructureAndEditor, int none, SQLBuilderDialog dialog)
    {
        this(sashFormStructureAndEditor, none);
        this.builderDialog = dialog;
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
        
        treeViewer.setInput(new RepositoryExtNode(repositoryView.getRoot().getChildren().get(METADATA_INDEX).getChildren().get(0)));
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
                if (refreshAction == null) {
                    refreshAction = new RefreshAction();
                }
                manager.add(refreshAction);
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
        if (refreshAction == null) {
            refreshAction = new RefreshAction();
        }
        toolBarMgr.add(refreshAction);
        toolBarMgr.update(true);
        toolBarMgr.getControl().setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_END));
    }
    
    /**
     * RefreshAction.
     */
    private class RefreshAction extends Action {
        
        /**
         * run.
         */
        @Override
        public void run() {
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
