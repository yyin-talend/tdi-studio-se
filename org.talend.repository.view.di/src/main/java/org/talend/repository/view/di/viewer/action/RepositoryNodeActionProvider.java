// ============================================================================
//
// Copyright (C) 2006-2012 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.repository.view.di.viewer.action;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.core.commands.IHandler;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.commands.ActionHandler;
import org.eclipse.ui.handlers.IHandlerService;
import org.eclipse.ui.internal.navigator.TextActionHandler;
import org.eclipse.ui.navigator.CommonActionProvider;
import org.eclipse.ui.navigator.ICommonActionConstants;
import org.eclipse.ui.navigator.ICommonActionExtensionSite;
import org.eclipse.ui.navigator.ICommonViewerWorkbenchSite;
import org.talend.commons.ui.swt.actions.ITreeContextualAction;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.ICoreService;
import org.talend.repository.ui.actions.CopyAction;
import org.talend.repository.ui.actions.DeleteAction;
import org.talend.repository.ui.actions.PasteAction;

public class RepositoryNodeActionProvider extends CommonActionProvider {

    static private List<ITreeContextualAction> contextualsActions;

    static private RepositoryDoubleClickAction doubleClickAction;

    public RepositoryNodeActionProvider() {

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.navigator.CommonActionProvider#init(org.eclipse.ui.navigator.ICommonActionExtensionSite)
     */
    @Override
    public void init(ICommonActionExtensionSite aSite) {

        super.init(aSite);
        makeActions();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.actions.ActionGroup#fillContextMenu(org.eclipse.jface.action.IMenuManager)
     */
    @Override
    public void fillContextMenu(IMenuManager manager) {
        super.fillContextMenu(manager);

        IStructuredSelection sel = (IStructuredSelection) getContext().getSelection();
        MenuManager[] menuManagerGroups = null;
        if (GlobalServiceRegister.getDefault().isServiceRegistered(ICoreService.class)) {
            final ICoreService coreService = (ICoreService) GlobalServiceRegister.getDefault().getService(ICoreService.class);
            if (coreService != null) {
                menuManagerGroups = coreService.getRepositoryContextualsActionGroups();
            }
        }
        // find group
        Set<String> processedGroupIds = new HashSet<String>();
        for (ITreeContextualAction action : contextualsActions) {
            action.init((TreeViewer) getActionSite().getStructuredViewer(), sel);
            if (action.isVisible() && action.isEnabled()) {
                IMenuManager groupMenu = findMenuManager(menuManagerGroups, action.getGroupId(), true); // find root
                if (groupMenu != null) { // existed
                    final String rootId = groupMenu.getId();
                    if (!processedGroupIds.contains(rootId)) {
                        manager.add(groupMenu);
                        processedGroupIds.add(rootId);
                    }
                }
                groupMenu = findMenuManager(menuManagerGroups, action.getGroupId(), false); // find last child
                if (groupMenu != null) { // existed
                    groupMenu.add(action);
                } else { // child
                    manager.add(action);
                }
            }
        }

        manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
    }

    private MenuManager findMenuManager(final MenuManager[] menuManagerGroups, String groupId, boolean findParent) {
        if (menuManagerGroups == null) {
            return null;
        }
        for (MenuManager groupMenu : menuManagerGroups) {
            if (groupMenu.getId().equals(groupId)) {
                if (findParent) {
                    final MenuManager parent = (MenuManager) groupMenu.getParent();
                    if (parent == null) {
                        return groupMenu;
                    } else {
                        return findMenuManager(menuManagerGroups, parent.getId(), findParent);
                    }
                } else {
                    return groupMenu;
                }
            }
        }
        return null;
    }

    protected void makeActions() {
        if (contextualsActions == null) {
            ICommonViewerWorkbenchSite navWorkSite = ((ICommonViewerWorkbenchSite) getActionSite().getViewSite());
            IHandlerService handlerService = (IHandlerService) navWorkSite.getSite().getService(IHandlerService.class);
            IHandler handler = null;
            if (GlobalServiceRegister.getDefault().isServiceRegistered(ICoreService.class)) {
                final ICoreService coreService = (ICoreService) GlobalServiceRegister.getDefault().getService(ICoreService.class);
                if (coreService != null) {
                    contextualsActions = coreService.getRepositoryContextualsActions();
                    for (ITreeContextualAction action : contextualsActions) {
                        action.setWorkbenchPart(navWorkSite.getSite().getPart());
                        if (action.getActionDefinitionId() != null) {
                            // TODO ActionHandler is deprecated, should be changed.
                            handler = new ActionHandler(action);
                            handlerService.activateHandler(action.getActionDefinitionId(), handler);
                        }
                    }
                }
            }
            doubleClickAction = new RepositoryDoubleClickAction(getActionSite().getStructuredViewer(), contextualsActions);
        }// else already initialised.

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.actions.ActionGroup#fillActionBars(org.eclipse.ui.IActionBars)
     */
    @Override
    public void fillActionBars(IActionBars actionBars) {
        super.fillActionBars(actionBars);
        actionBars.setGlobalActionHandler(ICommonActionConstants.OPEN, doubleClickAction);
        actionBars.setGlobalActionHandler(ActionFactory.COPY.getId(), CopyAction.getInstance());
        actionBars.setGlobalActionHandler(ActionFactory.PASTE.getId(), PasteAction.getInstance());
        actionBars.setGlobalActionHandler(ActionFactory.DELETE.getId(), DeleteAction.getInstance());
        // TODO TextActionHandler is an internal class and should not be used.
        TextActionHandler textActionHandler = new TextActionHandler(actionBars);
        textActionHandler.setCopyAction(CopyAction.getInstance());
        textActionHandler.setPasteAction(PasteAction.getInstance());
        textActionHandler.setDeleteAction(DeleteAction.getInstance());

    }

}
