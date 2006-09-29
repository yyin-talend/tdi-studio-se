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
package org.talend.designer.core.ui;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.gef.editparts.ZoomManager;
import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.gef.ui.actions.DeleteRetargetAction;
import org.eclipse.gef.ui.actions.GEFActionConstants;
import org.eclipse.gef.ui.actions.RedoRetargetAction;
import org.eclipse.gef.ui.actions.UndoRetargetAction;
import org.eclipse.gef.ui.actions.ZoomComboContributionItem;
import org.eclipse.gef.ui.actions.ZoomInRetargetAction;
import org.eclipse.gef.ui.actions.ZoomOutRetargetAction;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.actions.RetargetAction;
import org.eclipse.ui.part.MultiPageEditorActionBarContributor;
import org.talend.designer.core.i18n.Messages;
import org.talend.designer.core.ui.editor.TalendEditor;

/**
 * Manages the installation/deinstallation of global actions for multi-page editors. Responsible for the redirection of
 * global actions to the active editor. Multi-page contributor replaces the contributors for the individual editors in
 * the multi-page editor. <br/>
 * 
 * $Id$
 * 
 */
public class MultiPageEditorContributor extends MultiPageEditorActionBarContributor {

    private IEditorPart activeEditorPart;

    private ActionRegistry registry = new ActionRegistry();

    public MultiPageEditorContributor() {
        super();
    }

    private List<RetargetAction> retargetActions = new ArrayList<RetargetAction>();

    private List<String> designActionKeys = new ArrayList<String>();

    public void init(final IActionBars bars) {
        buildDesignActions();
        super.init(bars);
    }

    /**
     * Prépare toutes les actions relatives au designer Gef.
     */
    protected void buildDesignActions() {
        addDesignRetargetAction(new ZoomInRetargetAction());
        addDesignRetargetAction(new ZoomOutRetargetAction());
        addDesignRetargetAction(new DeleteRetargetAction());
        addDesignRetargetAction(new UndoRetargetAction());
        addDesignRetargetAction(new RedoRetargetAction());
        addDesignRetargetAction(new RetargetAction(GEFActionConstants.TOGGLE_SNAP_TO_GEOMETRY, Messages
                .getString("MultiPageEditorContributor.0"), //$NON-NLS-1$
                IAction.AS_CHECK_BOX));
        addDesignRetargetAction(new RetargetAction(GEFActionConstants.TOGGLE_GRID_VISIBILITY, Messages
                .getString("MultiPageEditorContributor.1"), //$NON-NLS-1$
                IAction.AS_CHECK_BOX));

        addDesignActionKey(ActionFactory.COPY.getId());
        addDesignActionKey(ActionFactory.PASTE.getId());
        addDesignActionKey(ActionFactory.PRINT.getId());
        addDesignActionKey(ActionFactory.SELECT_ALL.getId());
        addDesignActionKey(ActionFactory.DELETE.getId());
    }

    protected void addAction(final IAction action) {
        getActionRegistry().registerAction(action);
    }

    protected void addDesignActionKey(final String key) {
        designActionKeys.add(key);
    }

    protected void addDesignRetargetAction(final RetargetAction action) {
        addAction(action);
        retargetActions.add(action);
        getPage().addPartListener(action);
        addDesignActionKey(action.getId());
    }

    protected IAction getAction(final String id) {
        return getActionRegistry().getAction(id);
    }

    protected ActionRegistry getActionRegistry() {
        return registry;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.part.MultiPageEditorActionBarContributor#setActivePage(org.eclipse.ui.IEditorPart)
     */
    public void setActivePage(final IEditorPart activeEditor) {
        if ((activeEditorPart == activeEditor) || (activeEditor == null)) {
            return;
        }

        activeEditorPart = activeEditor;

        IActionBars bars = getActionBars();
        if (bars != null) {

            if (activeEditor instanceof TalendEditor) {
                ActionRegistry reg = (ActionRegistry) activeEditor.getAdapter(ActionRegistry.class);
                for (int i = 0; i < designActionKeys.size(); i++) {
                    String id = (String) designActionKeys.get(i);
                    bars.setGlobalActionHandler(id, reg.getAction(id));
                }

            }
            getActionBars().updateActionBars();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.part.EditorActionBarContributor#contributeToMenu(org.eclipse.jface.action.IMenuManager)
     */
    public void contributeToMenu(final IMenuManager menubar) {
        super.contributeToMenu(menubar);
        MenuManager viewMenu = new MenuManager(Messages.getString("MultiPageEditorContributor.2")); //$NON-NLS-1$
        viewMenu.add(getAction(GEFActionConstants.ZOOM_IN));
        viewMenu.add(getAction(GEFActionConstants.ZOOM_OUT));
        viewMenu.add(new Separator());
        viewMenu.add(getAction(GEFActionConstants.TOGGLE_GRID_VISIBILITY));
        viewMenu.add(getAction(GEFActionConstants.TOGGLE_SNAP_TO_GEOMETRY));
        menubar.insertAfter(IWorkbenchActionConstants.M_EDIT, viewMenu);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.part.EditorActionBarContributor#contributeToToolBar(org.eclipse.jface.action.IToolBarManager)
     */
    public void contributeToToolBar(final IToolBarManager toolBarManager) {
        toolBarManager.add(getAction(ActionFactory.UNDO.getId()));
        toolBarManager.add(getAction(ActionFactory.REDO.getId()));

        String[] zoomStrings = new String[] { ZoomManager.FIT_ALL, ZoomManager.FIT_HEIGHT, ZoomManager.FIT_WIDTH };
        toolBarManager.add(new ZoomComboContributionItem(getPage(), zoomStrings));
    }

}
