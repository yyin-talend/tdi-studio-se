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

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.talend.repository.i18n.Messages;
import org.talend.repository.model.RepositoryNode;
import org.talend.repository.ui.views.IRepositoryView;

/**
 * Action use to expand a folder in repository. Many objects can be expand in the same time. System folder can also be
 * expand. All children are expand<br/>
 * 
 * $Id$
 * 
 */
public class ExpandAllAction extends AContextualAction {

    private static final String MOVE_TITLE = Messages.getString("ExpandAction.action.title"); //$NON-NLS-1$

    private static final String MOVE_TOOLTIP = Messages.getString("ExpandAction.action.toolTipText"); //$NON-NLS-1$

    public ExpandAllAction() {
        super();
        setText(MOVE_TITLE);
        setToolTipText(MOVE_TOOLTIP);
    }

    public void run() {
        IRepositoryView view = getViewPart();
        ISelection selection = getSelection();

        for (Object obj : ((IStructuredSelection) selection).toArray()) {
            expand(view, (RepositoryNode) obj, !view.getExpandedState(obj));
        }

        refresh();
    }

    private void expand(IRepositoryView view, RepositoryNode obj, boolean state) {
        view.expand(obj, state);
        if (obj.hasChildren()) {
            for (RepositoryNode currentChild : obj.getChildren()) {
                expand(view, currentChild, state);
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.ui.actions.ITreeContextualAction#init(org.eclipse.jface.viewers.TreeViewer,
     * org.eclipse.jface.viewers.IStructuredSelection)
     */
    public void init(TreeViewer viewer, IStructuredSelection selection) {
        boolean canWork = !selection.isEmpty();

        for (Object o : ((IStructuredSelection) selection).toArray()) {
            if (canWork) {
                RepositoryNode node = (RepositoryNode) o;
                switch (node.getType()) {
                case STABLE_SYSTEM_FOLDER:
                case SYSTEM_FOLDER:
                case SIMPLE_FOLDER:
                	canWork = node.hasChildren();
                    break;
                default:
                    canWork = false;
                    break;
                }
            }
        }
        setEnabled(canWork);
    }
}
