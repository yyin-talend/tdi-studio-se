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
package org.talend.repository.ui.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.talend.commons.ui.swt.actions.ITreeContextualAction;
import org.talend.repository.ui.views.IRepositoryView;
import org.talend.repository.ui.views.RepositoryView;

/**
 * Used to manage contextual actions on repository objects.<br/>
 * 
 * $Id$
 * 
 */
public abstract class AContextualAction extends Action implements ITreeContextualAction {

    private int level; // Used to manage order of actions in contextual menu

    /**
     * Getter for level.
     * 
     * @return the level
     */
    public int getLevel() {
        return this.level;
    }

    /**
     * Sets the level.
     * 
     * @param level the level to set
     */
    public void setLevel(int level) {
        this.level = level;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.commons.ui.swt.actions.ITreeContextualAction#isVisible()
     */
    public boolean isVisible() {
        return isEnabled();
    }

    /**
     * Returns if this action is accessible by double-click.
     * 
     * @return <code>true</code> if action is accessible by double-click, <code>true</code> otherwise
     */
    public final boolean isDoubleClickAction() {
        return getClassForDoubleClick() != null;
    }

    /**
     * Returns the class on wich this action may be call by double-click.
     * 
     * @return the class on wich this action may be call by double-click
     */
    public Class getClassForDoubleClick() {
        return null;
    }

    /**
     * Convenience method user to refresh view on wich action had been called.
     */
    public final void refresh() {
        getViewPart().refresh();
    }

    /**
     * Convenience method user to refresh view (starting with the given element) on wich action had been called.
     * 
     * @param obj - object to start the refresh on
     */
    public final void refresh(Object obj) {
        getViewPart().refresh(obj);
        getViewPart().expand(obj, true);
    }

    /**
     * The repository view selection.
     * 
     * @return the selection
     */
    protected final ISelection getSelection() {
        return getViewPart().getViewer().getSelection();
    }

    /**
     * 
     * Returns the repository view..
     * 
     * @return - the repository biew
     */
    protected final IRepositoryView getViewPart() {
        IViewPart viewPart = (IViewPart) getActivePage().findView(RepositoryView.VIEW_ID);
        return (IRepositoryView) viewPart;
    }

    /**
     * Returns the currently active page for this workbench window.
     * 
     * @return the active page, or <code>null</code> if none
     */
    protected final IWorkbenchPage getActivePage() {
        return PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
    }
}
