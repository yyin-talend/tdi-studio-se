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

import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Display;
import org.talend.sqlbuilder.dbstructure.RepositoryExtNode;
import org.talend.sqlbuilder.dbstructure.nodes.INode;

/**
 * Detailled comment for this class. <br/>
 * $Id:  RefreshTreeCommand.java Version 1.0 Nov 2, 2006 9:57:21 AM $
 * @author Hou Peiqin 
 * 
 */
public final class RefreshTreeCommand {
    private static RefreshTreeCommand refreshTreeCommand = new RefreshTreeCommand();
    private TreeViewer treeViewer;
    private INode[] currentNodes;
    
    private RefreshTreeCommand() {
    }
    
    
    public void setCurrentNodes(INode[] currentNodes) {
        this.currentNodes = currentNodes;
    }

    public TreeViewer getTreeViewer() {
        return treeViewer;
    }

    public void setTreeViewer(TreeViewer treeViewer) {
        this.treeViewer = treeViewer;
    }

    public void execute() {
        Display.getDefault().asyncExec(new Runnable() {
            public void run() {
                if (treeViewer != null && treeViewer.getTree() != null && !treeViewer.getTree().isDisposed()) {
                    ((RepositoryExtNode) treeViewer.getInput()).setChildNodes(currentNodes);
                    treeViewer.getTree().removeAll();
                    treeViewer.refresh();
                }
            }
        });
    }
    
    public static RefreshTreeCommand getInstance() {
        return refreshTreeCommand;
    }
}
