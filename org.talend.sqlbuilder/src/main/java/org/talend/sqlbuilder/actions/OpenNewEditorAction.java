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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.ui.actions.SelectionProviderAction;
import org.talend.repository.model.RepositoryNode;
import org.talend.repository.model.RepositoryNode.EProperties;
import org.talend.sqlbuilder.dbstructure.RepositoryNodeType;
import org.talend.sqlbuilder.repository.utility.SQLBuilderRepositoryNodeManager;
import org.talend.sqlbuilder.ui.SQLBuilderTabComposite;
import org.talend.sqlbuilder.util.ConnectionParameters;

/**
 * This class is responsible for opening new editor. <br/>
 * 
 * @author ftang
 * 
 */
public class OpenNewEditorAction extends SelectionProviderAction
{

    private SQLBuilderTabComposite editorComposite;

    private ISelectionProvider selectionProvider;

    private ConnectionParameters connParam;

    private boolean isDefaultEditor;

    /**
     * OpenNewEditorAction constructor.
     * 
     * @param selectionProvider
     * @param editorComposite
     * @param connParam
     * @param isDefaultEditor
     */
    public OpenNewEditorAction(ISelectionProvider selectionProvider,
            SQLBuilderTabComposite editorComposite,
            ConnectionParameters connParam, boolean isDefaultEditor)
    {
        super(selectionProvider, "New Editor");
        this.editorComposite = editorComposite;
        this.selectionProvider = selectionProvider;
        this.connParam = connParam;
        this.isDefaultEditor = isDefaultEditor;
        init();
    }

    @SuppressWarnings("unchecked")
    public void init()
    {
        RepositoryNode[] selectedNodes = (RepositoryNode[]) ((IStructuredSelection) selectionProvider
                .getSelection()).toList().toArray(new RepositoryNode[]
        {});
        if (selectedNodes.length == 0)
        {
            this.setEnabled(false);
            return;
        }
        int i = 0;
        for (RepositoryNode node : selectedNodes)
        {
            if (node.getProperties(EProperties.CONTENT_TYPE) == RepositoryNodeType.FOLDER)
            {
                i++;
            }
        }
        if (i > 0)
        {
            this.setEnabled(false);
        }
        else
        {
            this.setEnabled(true);
        }
    }

    @Override
    public void selectionChanged(ISelection selection)
    {
        init();
    }


    @Override
    public void run()
    {
        IStructuredSelection selection = (IStructuredSelection) selectionProvider
                .getSelection();
        RepositoryNode firstNode = (RepositoryNode) selection.getFirstElement();
        firstNode = new SQLBuilderRepositoryNodeManager().getRoot(firstNode);
        TreeViewer treeViewer = (TreeViewer) selectionProvider;
        List repositoryNames = getRepositoryNames((RepositoryNode) treeViewer.getInput());
        editorComposite.openNewEditor(firstNode, repositoryNames, connParam,
                isDefaultEditor);
    }
    
    /**
     * Get all repository node names.
     * @param
     * @return
     * @exception
     */
    @SuppressWarnings("unchecked")
    private List getRepositoryNames(RepositoryNode repositoryNode) {
        List<String> repositoryNames = new ArrayList<String>();
        List<RepositoryNode> repositoryNodes = repositoryNode.getChildren();
        for (RepositoryNode node : repositoryNodes) {
            if (node.getProperties(EProperties.CONTENT_TYPE) == RepositoryNodeType.FOLDER) {
                repositoryNames.addAll(getRepositoryNames(node));
            } else {
                repositoryNames.add(node.getObject().getLabel());
            }
        }

        return repositoryNames;
    }

}
