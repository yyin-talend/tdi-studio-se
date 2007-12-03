// ============================================================================
//
// Copyright (C) 2006-2007 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.designer.core.ui.editor;

import java.util.List;

import org.eclipse.gef.ContextMenuProvider;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.gef.ui.actions.GEFActionConstants;
import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.actions.ActionFactory;
import org.talend.core.model.process.EConnectionType;
import org.talend.core.model.process.INodeConnector;
import org.talend.designer.core.i18n.Messages;
import org.talend.designer.core.ui.action.ActivateElementAction;
import org.talend.designer.core.ui.action.ActivateSubjobAction;
import org.talend.designer.core.ui.action.ActivateSubjobOneComponentAction;
import org.talend.designer.core.ui.action.BringForwardAction;
import org.talend.designer.core.ui.action.BringToFrontAction;
import org.talend.designer.core.ui.action.ConnectionCreateAction;
import org.talend.designer.core.ui.action.ConnectionSetAsMainRef;
import org.talend.designer.core.ui.action.GEFCopyAction;
import org.talend.designer.core.ui.action.GEFPasteAction;
import org.talend.designer.core.ui.action.ModifyMergeOrderAction;
import org.talend.designer.core.ui.action.NodeBreakpointAction;
import org.talend.designer.core.ui.action.SendBackwardAction;
import org.talend.designer.core.ui.action.SendToBackAction;
import org.talend.designer.core.ui.action.ShowComponentSettingViewerAction;

/**
 * Class that manages the context menu in the Gef Editor. <br/>
 * 
 * $Id$
 * 
 */
public class TalendEditorContextMenuProvider extends ContextMenuProvider {

    private ActionRegistry actionRegistry;

    private IWorkbenchPart part;

    public static final String ID = "org.talend.designer.core.ui.editor.TalendEditorContextMenuProvider"; //$NON-NLS-1$

    private static final String GROUP_CONNECTIONS = "ConnectionsGroup"; //$NON-NLS-1$

    private static boolean enableContextMenu = true;

    public TalendEditorContextMenuProvider(IWorkbenchPart part, EditPartViewer viewer, ActionRegistry registry) {
        super(viewer);
        if (registry == null) {
            throw new IllegalArgumentException();
        }
        this.actionRegistry = registry;
        this.part = part;
    }

    /**
     * Called when the context menu is about to show. Actions, whose state is enabled, will appear in the context menu.
     * 
     * @see org.eclipse.gef.ContextMenuProvider#buildContextMenu(org.eclipse.jface.action.IMenuManager)
     */
    public void buildContextMenu(final IMenuManager menu) {
        if (!isEnableContextMenu()) {
            return;
        }
        // Add standard action groups to the menu
        menu.add(new Separator(GROUP_CONNECTIONS));
        menu.add(new Separator(GEFActionConstants.GROUP_UNDO));
        menu.add(new Separator(GEFActionConstants.GROUP_COPY));
        menu.add(new Separator(GEFActionConstants.GROUP_EDIT));
        menu.add(new Separator(GEFActionConstants.GROUP_REST));
        menu.add(new Separator(GEFActionConstants.GROUP_VIEW));
        IAction action;

        menu.appendToGroup(GEFActionConstants.GROUP_UNDO, // target group id
                getAction(ActionFactory.UNDO.getId())); // action to add
        menu.appendToGroup(GEFActionConstants.GROUP_UNDO, getAction(ActionFactory.REDO.getId()));

        if (part != null) {
            action = getAction(ActionFactory.COPY.getId()); // new GEFCopyAction(part);
            if (action instanceof GEFCopyAction) {
                ((GEFCopyAction) action).update();
            }
            menu.appendToGroup(GEFActionConstants.GROUP_EDIT, action);

            action = getAction(ActionFactory.PASTE.getId()); // new GEFPasteAction(part);
            GEFPasteAction pasteAction = (GEFPasteAction) action;
            Point p = Display.getCurrent().getCursorLocation();

            p = this.getViewer().getControl().toControl(p);

            pasteAction.setCursorLocation(p);
            menu.appendToGroup(GEFActionConstants.GROUP_EDIT, action);

            menu.appendToGroup(GEFActionConstants.GROUP_EDIT, getAction(ActionFactory.DELETE.getId()));

            menu.appendToGroup(GEFActionConstants.GROUP_EDIT, getAction(ActionFactory.SELECT_ALL.getId()));

            action = new ActivateElementAction(part);
            ((ActivateElementAction) action).update();
            if (action.isEnabled()) {
                menu.appendToGroup(GEFActionConstants.GROUP_REST, action);
            }

            action = new ActivateSubjobAction(part);
            ((ActivateSubjobAction) action).update();
            if (action.isEnabled()) {
                menu.appendToGroup(GEFActionConstants.GROUP_REST, action);
            }

            action = new ActivateSubjobOneComponentAction(part);
            ((ActivateSubjobOneComponentAction) action).update();
            if (action.isEnabled()) {
                menu.appendToGroup(GEFActionConstants.GROUP_REST, action);
            }
            action = getAction(ConnectionSetAsMainRef.ID);
            if (action.isEnabled()) {
                menu.appendToGroup(GEFActionConstants.GROUP_REST, action);
            }

            action = getAction(ModifyMergeOrderAction.ID);
            if (action.isEnabled()) {
                menu.appendToGroup(GEFActionConstants.GROUP_REST, action);
            }

            MenuManager subMenu = new MenuManager(Messages.getString("TalendEditorContextMenuProvider.Row")); //$NON-NLS-1$
            menu.appendToGroup(GROUP_CONNECTIONS, subMenu);

            action = new ConnectionCreateAction(part, EConnectionType.FLOW_MAIN);
            ((ConnectionCreateAction) action).update();
            List<INodeConnector> connectors = ((ConnectionCreateAction) action).getConnectors();
            if (connectors.size() > 1) {
                for (INodeConnector connector : connectors) {
                    if (connector.isBuiltIn()) {
                        MenuManager mainSubMenu = new MenuManager(connector.getMenuName());
                        subMenu.appendToGroup(connector.getMenuName(), mainSubMenu);
                        List<String> menuList = ((ConnectionCreateAction) action).getMenuList();
                        for (int i = 0; i < menuList.size(); i++) {
                            action = new ConnectionCreateAction(part, connector);
                            ((ConnectionCreateAction) action).update();
                            ((ConnectionCreateAction) action).setText(menuList.get(i));
                            mainSubMenu.add(action);
                        }
                    } else {
                        action = new ConnectionCreateAction(part, connector);
                        ((ConnectionCreateAction) action).update();
                        if (action.isEnabled()) {
                            ((ConnectionCreateAction) action).setText(connector.getMenuName());
                            subMenu.add(action);
                        }
                    }
                }
            } else {
                if (connectors.size() == 1) {
                    if (action.isEnabled()) {
                        List<String> menuList = ((ConnectionCreateAction) action).getMenuList();
                        for (int i = 0; i < menuList.size(); i++) {
                            action = new ConnectionCreateAction(part, EConnectionType.FLOW_MAIN);
                            ((ConnectionCreateAction) action).update();
                            ((ConnectionCreateAction) action).setText(menuList.get(i));
                            subMenu.add(action);
                        }
                    }
                }
            }

            action = new ConnectionCreateAction(part, EConnectionType.ITERATE);
            ((ConnectionCreateAction) action).update();
            if (action.isEnabled()) {
                subMenu.add(action);
            }
            subMenu = new MenuManager("Link");
            menu.appendToGroup(GROUP_CONNECTIONS, subMenu);
            action = new ConnectionCreateAction(part, EConnectionType.TABLE);
            ((ConnectionCreateAction) action).update();
            if (action.isEnabled()) {
                List<String> menuList = ((ConnectionCreateAction) action).getMenuList();
                for (int i = 0; i < menuList.size(); i++) {
                    action = new ConnectionCreateAction(part, EConnectionType.TABLE);
                    ((ConnectionCreateAction) action).update();
                    ((ConnectionCreateAction) action).setText(menuList.get(i));
                    subMenu.add(action);
                }
            }
            subMenu = new MenuManager(Messages.getString("TalendEditorContextMenuProvider.Trigger")); //$NON-NLS-1$
            menu.appendToGroup(GROUP_CONNECTIONS, subMenu);

            /*
             * action = new ConnectionCreateAction(part, EConnectionType.RUN_BEFORE); ((ConnectionCreateAction)
             * action).update(); if (action.isEnabled()) { subMenu.add(action); }
             */

            action = new ConnectionCreateAction(part, EConnectionType.THEN_RUN);
            ((ConnectionCreateAction) action).update();
            if (action.isEnabled()) {
                subMenu.add(action);
            }

            /*
             * action = new ConnectionCreateAction(part, EConnectionType.RUN_AFTER); ((ConnectionCreateAction)
             * action).update(); if (action.isEnabled()) { subMenu.add(action); }
             */
            /*
             * subMenu.add(new Separator());
             * 
             * action = new ConnectionCreateAction(part, EConnectionType.REFERENCE); ((ConnectionCreateAction)
             * action).update(); if (action.isEnabled()) { subMenu.add(action); }
             */
            subMenu.add(new Separator());

            action = new ConnectionCreateAction(part, EConnectionType.RUN_IF);
            ((ConnectionCreateAction) action).update();
            if (action.isEnabled()) {
                subMenu.add(action);
            }

            action = new ConnectionCreateAction(part, EConnectionType.RUN_IF_OK);
            ((ConnectionCreateAction) action).update();
            if (action.isEnabled()) {
                subMenu.add(action);
            }

            action = new ConnectionCreateAction(part, EConnectionType.RUN_IF_ERROR);
            ((ConnectionCreateAction) action).update();
            if (action.isEnabled()) {
                subMenu.add(action);
            }

            action = new NodeBreakpointAction(part);
            ((NodeBreakpointAction) action).update();
            if (action.isEnabled()) {
                menu.appendToGroup(GEFActionConstants.GROUP_REST, action);
            }
            action = new ShowComponentSettingViewerAction(part);
            ((ShowComponentSettingViewerAction) action).update();
            menu.add(new Separator());
            menu.add(action);

            action = new BringForwardAction(part);
            ((SelectionAction) action).update();
            if (action.isEnabled()) {
                menu.appendToGroup(GEFActionConstants.GROUP_VIEW, action);
            }
            action = new BringToFrontAction(part);
            ((SelectionAction) action).update();
            if (action.isEnabled()) {
                menu.appendToGroup(GEFActionConstants.GROUP_VIEW, action);
            }

            action = new SendBackwardAction(part);
            ((SelectionAction) action).update();
            if (action.isEnabled()) {
                menu.appendToGroup(GEFActionConstants.GROUP_VIEW, action);
            }
            action = new SendToBackAction(part);
            ((SelectionAction) action).update();
            if (action.isEnabled()) {
                menu.appendToGroup(GEFActionConstants.GROUP_VIEW, action);
            }
        }
    }

    private IAction getAction(final String actionId) {
        return actionRegistry.getAction(actionId);
    }

    /**
     * Getter for enableContextMenu.
     * 
     * @return the enableContextMenu
     */
    public static boolean isEnableContextMenu() {
        return enableContextMenu;
    }

    /**
     * Sets the enableContextMenu.
     * 
     * @param enableContextMenu the enableContextMenu to set
     */
    public static void setEnableContextMenu(boolean enableContextMenu) {
        TalendEditorContextMenuProvider.enableContextMenu = enableContextMenu;
    }
}
