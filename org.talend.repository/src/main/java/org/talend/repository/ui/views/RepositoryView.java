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
package org.talend.repository.ui.views;

import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.core.commands.IHandler;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.util.LocalSelectionTransfer;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeViewerListener;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeExpansionEvent;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSourceAdapter;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.commands.ActionHandler;
import org.eclipse.ui.contexts.IContextActivation;
import org.eclipse.ui.contexts.IContextService;
import org.eclipse.ui.handlers.IHandlerService;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.ui.views.properties.IPropertySheetPage;
import org.eclipse.ui.views.properties.tabbed.ITabbedPropertySheetPageContributor;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;
import org.talend.commons.ui.swt.actions.ITreeContextualAction;
import org.talend.core.CorePlugin;
import org.talend.core.context.Context;
import org.talend.core.context.RepositoryContext;
import org.talend.core.ui.images.EImage;
import org.talend.core.ui.images.ImageProvider;
import org.talend.repository.RepositoryPlugin;
import org.talend.repository.i18n.Messages;
import org.talend.repository.model.RepositoryNode;
import org.talend.repository.model.RepositoryNode.ENodeType;
import org.talend.repository.model.actions.MoveObjectAction;
import org.talend.repository.ui.actions.ActionsHelper;
import org.talend.repository.ui.actions.ExportJobScriptAction;
import org.talend.repository.ui.actions.ImportProjectsAction;
import org.talend.repository.ui.actions.RefreshAction;
import org.talend.repository.ui.actions.RepositoryDoubleClickAction;

/**
 * 
 * View that presents all the content of the repository.<br/>
 * 
 * $Id$
 * 
 */
public class RepositoryView extends ViewPart implements IRepositoryView, ITabbedPropertySheetPageContributor {

    private static Logger log = Logger.getLogger(RepositoryView.class);

    private static TreeViewer viewer;

    private static RepositoryNode root = new RepositoryNode(null, null, ENodeType.STABLE_SYSTEM_FOLDER);

    private List<ITreeContextualAction> contextualsActions;

    private Action doubleClickAction;

    private Action refreshAction;

    private Action importAction;

    private Action exportJobscriptAction;

    private Listener dragDetectListener;

    public RepositoryView() {
        this((RepositoryContext) CorePlugin.getContext().getProperty(Context.REPOSITORY_CONTEXT_KEY));
    }

    public RepositoryView(RepositoryContext repositoryContext) {
        super();
    }

    @Override
    public void createPartControl(Composite parent) {
        viewer = new TreeViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);

        viewer.setContentProvider(new RepositoryContentProvider(this));
        viewer.setLabelProvider(new RepositoryLabelProvider(this));
        viewer.setSorter(new RepositoryNameSorter());
        IViewSite viewSite = getViewSite();
        viewer.setInput(viewSite);
        getSite().setSelectionProvider(viewer);

        // This only tree listener aim is to change open/close icons on folders :
        viewer.addTreeListener(new ITreeViewerListener() {

            public void treeCollapsed(TreeExpansionEvent event) {
                RepositoryNode node = (RepositoryNode) event.getElement();
                if (node.getType().equals(ENodeType.SIMPLE_FOLDER)) {
                    TreeItem item = getObject(viewer.getTree(), event.getElement());
                    if (item != null) {
                        item.setImage(ImageProvider.getImage(EImage.FOLDER_CLOSE_ICON));
                    }
                }
            }

            public void treeExpanded(TreeExpansionEvent event) {
                RepositoryNode node = (RepositoryNode) event.getElement();
                if (node.getType().equals(ENodeType.SIMPLE_FOLDER)) {
                    TreeItem item = getObject(viewer.getTree(), event.getElement());
                    if (item != null) {
                        item.setImage(ImageProvider.getImage(EImage.FOLDER_OPEN_ICON));
                    }
                }
            }
        });

        makeActions();
        hookContextMenu();
        contributeToActionBars();
        initDragAndDrop();
        hookDoubleClickAction();

        setPartName(Messages.getString("repository.title", ((RepositoryContext) CorePlugin.getContext().getProperty(
                Context.REPOSITORY_CONTEXT_KEY)).getProject()));

        viewer.getTree().addFocusListener(new FocusListener() {

            public void focusGained(FocusEvent e) {
                log.trace("Repository gain focus");
                IContextService contextService = (IContextService) RepositoryPlugin.getDefault().getWorkbench()
                        .getAdapter(IContextService.class);
                ca = contextService.activateContext("talend.repository");
            }

            public void focusLost(FocusEvent e) {
                log.trace("Repository lost focus");
                if (ca != null) {
                    IContextService contextService = (IContextService) RepositoryPlugin.getDefault().getWorkbench()
                            .getAdapter(IContextService.class);
                    contextService.deactivateContext(ca);
                }
            }
        });
    }

    IContextActivation ca;

    private TreeItem getObject(Tree tree, Object objectToFind) {
        for (TreeItem item : tree.getItems()) {
            TreeItem toReturn = getObject(item, objectToFind);
            if (toReturn != null) {
                return toReturn;
            }
        }
        return null;
    }

    private TreeItem getObject(TreeItem parent, Object objectToFind) {
        for (TreeItem currentChild : parent.getItems()) {
            if (objectToFind.equals(currentChild.getData())) {
                return currentChild;
            }
            TreeItem toReturn = getObject(currentChild, objectToFind);
            if (toReturn != null) {
                return toReturn;
            }
        }
        return null;
    }

    protected void initDragAndDrop() {
        int ops = DND.DROP_COPY | DND.DROP_MOVE;
        Transfer[] transfers = new Transfer[] { LocalSelectionTransfer.getTransfer() };
        viewer.addDragSupport(ops, transfers, new DragSourceAdapter() {

            private static final long FFFFFFFFL = 0xFFFFFFFFL;

            /*
             * (non-Javadoc)
             * 
             * @see org.eclipse.swt.dnd.DragSourceAdapter#dragSetData(org.eclipse.swt.dnd.DragSourceEvent)
             */
            @Override
            public void dragSetData(DragSourceEvent event) {
                event.data = LocalSelectionTransfer.getTransfer().getSelection();
            }

            /*
             * (non-Javadoc)
             * 
             * @see org.eclipse.swt.dnd.DragSourceAdapter#dragStart(org.eclipse.swt.dnd.DragSourceEvent)
             */
            @Override
            public void dragStart(DragSourceEvent event) {
                ISelection selection = viewer.getSelection();

                for (Object obj : ((StructuredSelection) selection).toArray()) {
                    RepositoryNode sourceNode = (RepositoryNode) obj;

                    // As i don't know how to get event operation i test on MoveOperation
                    event.doit = MoveObjectAction.getInstance().validateAction(sourceNode, null);
                }

                LocalSelectionTransfer.getTransfer().setSelection(selection);
                LocalSelectionTransfer.getTransfer().setSelectionSetTime(event.time & FFFFFFFFL);
            }

            /*
             * (non-Javadoc)
             * 
             * @see org.eclipse.swt.dnd.DragSourceAdapter#dragFinished(org.eclipse.swt.dnd.DragSourceEvent)
             */
            @Override
            public void dragFinished(DragSourceEvent event) {
                refresh();
                LocalSelectionTransfer.getTransfer().setSelection(null);
                LocalSelectionTransfer.getTransfer().setSelectionSetTime(0);
            }
        });
        RepositoryDropAdapter adapter = new RepositoryDropAdapter(viewer);
        adapter.setFeedbackEnabled(false);
        viewer.addDropSupport(ops | DND.DROP_DEFAULT, transfers, adapter);
        dragDetectListener = new Listener() {

            public void handleEvent(Event event) {
                // dragDetected = true;
            }
        };
        viewer.getControl().addListener(SWT.DragDetect, dragDetectListener);
    }

    private void makeActions() {
        IHandlerService handlerService = (IHandlerService) getSite().getService(IHandlerService.class);

        importAction = ImportProjectsAction.getInstance();

        exportJobscriptAction = new ExportJobScriptAction();

        refreshAction = new RefreshAction(this);
        IHandler handler1 = new ActionHandler(refreshAction);
        handlerService.activateHandler(refreshAction.getActionDefinitionId(), handler1);

        contextualsActions = ActionsHelper.getActions();
        for (ITreeContextualAction action : contextualsActions) {
            if (action.getActionDefinitionId() != null) {
                handler1 = new ActionHandler(action);
                handlerService.activateHandler(action.getActionDefinitionId(), handler1);
            }
        }
        doubleClickAction = new RepositoryDoubleClickAction(this, contextualsActions);
    }

    private void hookDoubleClickAction() {
        viewer.addDoubleClickListener(new IDoubleClickListener() {

            public void doubleClick(DoubleClickEvent event) {
                doubleClickAction.run();
            }
        });
    }

    private void hookContextMenu() {
        MenuManager menuMgr = new MenuManager("#PopupMenu");
        menuMgr.setRemoveAllWhenShown(true);

        menuMgr.addMenuListener(new IMenuListener() {

            public void menuAboutToShow(IMenuManager manager) {
                RepositoryView.this.fillContextMenu(manager);
            }
        });

        Menu menu = menuMgr.createContextMenu(viewer.getControl());
        viewer.getControl().setMenu(menu);
        getSite().registerContextMenu(menuMgr, viewer);
    }

    private void contributeToActionBars() {
        IActionBars bars = getViewSite().getActionBars();
        fillLocalPullDown(bars.getMenuManager());
        fillLocalToolBar(bars.getToolBarManager());
    }

    private void fillLocalPullDown(IMenuManager manager) {
    }

    private void fillContextMenu(IMenuManager manager) {
        IStructuredSelection sel = (IStructuredSelection) viewer.getSelection();

        for (ITreeContextualAction action : contextualsActions) {
            action.init(getViewer(), sel);
            if (action.isVisible()) {
                manager.add(action);
            }
        }
        manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
    }

    private void fillLocalToolBar(IToolBarManager manager) {
        manager.add(refreshAction);
        manager.add(importAction);
    }

    @Override
    public void setFocus() {
        viewer.getControl().setFocus();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.ui.repository.views.IRepositoryView#getViewer()
     */
    public TreeViewer getViewer() {
        return viewer;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.ui.repository.views.IRepositoryView#refresh()
     */
    public void refresh() {
        root = new RepositoryNode(null, null, ENodeType.STABLE_SYSTEM_FOLDER);
        viewer.refresh();
        // unsetting the selection will prevent the propertyView from displaying dirty data
        viewer.setSelection(new TreeSelection());
    }

    public void refresh(Object object) {
        refresh();
        viewer.setExpandedState(object, true);
    }

    public void expand(Object object) {
        boolean state = getExpandedState(object);
        expand(object, !state);
    }

    public boolean getExpandedState(Object object) {
        boolean state = viewer.getExpandedState(object);
        return state;
    }

    public void expand(Object object, boolean state) {
        viewer.setExpandedState(object, state);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.ui.repository.views.IRepositoryView#getSystemFolders()
     */
    public RepositoryNode getRoot() {
        return root;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.views.properties.tabbed.ITabbedPropertySheetPageContributor#getContributorId()
     */
    public String getContributorId() {
        return getSite().getId();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.part.WorkbenchPart#getAdapter(java.lang.Class)
     */
    @Override
    public Object getAdapter(Class adapter) {
        if (adapter == IPropertySheetPage.class) {
            return new TabbedPropertySheetPage(this);
        }
        return super.getAdapter(adapter);
    }

}
