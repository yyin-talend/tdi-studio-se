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
package org.talend.sqlbuilder.dbstructure.actions;

import net.sourceforge.sqlexplorer.plugin.views.DatabaseStructureView;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.TreeViewer;
import org.talend.sqlbuilder.dbstructure.nodes.INode;

/**
 * Abstract implementation for a context menu action in the database structure view. Extend this class to add actions to
 * the structure view.
 * 
 * @author Davy Vanherbergen
 */
public abstract class AbstractDBTreeContextAction extends Action {

    protected INode[] selectedNodes;

    protected TreeViewer treeViewer;

    protected DatabaseStructureView view;

    /**
     * Store nodes for use in the actions.
     * 
     * @param nodes
     */
    public final void setSelectedNodes(INode[] nodes) {
        selectedNodes = nodes;
    }

    /**
     * Store treeViewer for use in the actions
     * 
     * @param treeViewer
     */
    public void setTreeViewer(TreeViewer treeViewer) {
        this.treeViewer = treeViewer;
    }

    /**
     * Store view for use in actions
     */
    public void setView(DatabaseStructureView view) {
        this.view = view;
    }

    /**
     * Implement this method to return true when your action is available for the selected node(s). When true, the
     * action will be included in the context menu, when false it will be ignored.
     * 
     * 
     * @return true if the action should be included in the context menu
     */
    public boolean isAvailable() {
        return true;
    }
}
