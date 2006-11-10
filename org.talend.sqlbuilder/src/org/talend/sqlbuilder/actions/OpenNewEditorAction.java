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
package org.talend.sqlbuilder.actions;

import java.util.List;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.talend.sqlbuilder.dbstructure.SessionTreeNodeUtils;
import org.talend.sqlbuilder.dbstructure.nodes.AbstractNode;
import org.talend.sqlbuilder.sessiontree.model.SessionTreeNode;
import org.talend.sqlbuilder.ui.SQLBuilderTabComposite;
import org.talend.sqlbuilder.util.ConnectionParameters;

/**
 * This class is responsible for opening new editor. <br/>
 * 
 * @author ftang
 * 
 */
public class OpenNewEditorAction extends Action {

    private SQLBuilderTabComposite editorComposite;

    private ISelectionProvider selectionProvider;
    
    private ConnectionParameters connParam;
    
    private boolean isDefaultEditor;

    /**
     * OpenNewEditorAction constructor.
     * @param selectionProvider
     * @param editorComposite
     * @param connParam
     * @param isDefaultEditor
     */
    public OpenNewEditorAction(ISelectionProvider selectionProvider, SQLBuilderTabComposite editorComposite,ConnectionParameters connParam, boolean isDefaultEditor) {
        this.editorComposite = editorComposite;
        this.selectionProvider = selectionProvider;
        this.connParam = connParam;
        this.isDefaultEditor = isDefaultEditor;
    }

    @Override
    public void run() {
        IStructuredSelection selection = (IStructuredSelection) selectionProvider.getSelection();
        // TODO get SessionTreeNode from the selection
        SessionTreeNode node = getSessionTreeNode(selection);
        List repositoryNames = SessionTreeNodeUtils.getRepositoryNames();
        editorComposite.openNewEditor(node,repositoryNames, connParam, isDefaultEditor);
    }

    /**
     * Gets SessionTreeNode object.
     * 
     * @param selection
     * 
     * @return
     */
    private SessionTreeNode getSessionTreeNode(IStructuredSelection selection) {
        Object element = selection.getFirstElement();
        if (element == null || !(element instanceof AbstractNode)) {
            return null;
        }
        return ((AbstractNode) element).getSession();
    }
}
