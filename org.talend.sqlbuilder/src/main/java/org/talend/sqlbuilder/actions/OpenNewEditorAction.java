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

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.actions.SelectionProviderAction;
import org.talend.repository.model.RepositoryNode;
import org.talend.repository.model.RepositoryNode.EProperties;
import org.talend.sqlbuilder.Messages;
import org.talend.sqlbuilder.dbstructure.RepositoryNodeType;
import org.talend.sqlbuilder.repository.utility.SQLBuilderRepositoryNodeManager;
import org.talend.sqlbuilder.ui.ISQLBuilderDialog;
import org.talend.sqlbuilder.ui.SQLBuilderTabComposite;
import org.talend.sqlbuilder.util.ConnectionParameters;

/**
 * This class is responsible for opening new editor. <br/>
 * 
 * @author ftang
 * 
 */
public class OpenNewEditorAction extends SelectionProviderAction {

    private ISQLBuilderDialog dialog;

    private ISelectionProvider selectionProvider;

    private ConnectionParameters connParam;

    private boolean isDefaultEditor;

    private SQLBuilderRepositoryNodeManager repositoryNodeManager = new SQLBuilderRepositoryNodeManager();

    /**
     * OpenNewEditorAction constructor.
     * 
     * @param selectionProvider
     * @param editorComposite
     * @param connParam
     * @param isDefaultEditor
     */
    public OpenNewEditorAction(ISelectionProvider selectionProvider, ISQLBuilderDialog dialog,
            ConnectionParameters connParam, boolean isDefaultEditor) {
        super(selectionProvider, "New Editor");
        setText(Messages.getString("DBStructureComposite.NewEditor")); //$NON-NLS-1$
        setToolTipText(Messages.getString("DBStructureComposite.NewEditor"));
        this.dialog = dialog;
        this.selectionProvider = selectionProvider;
        this.connParam = connParam;
        this.isDefaultEditor = isDefaultEditor;
        init();
    }

    @SuppressWarnings("unchecked")
    public void init() {
        RepositoryNode[] selectedNodes = (RepositoryNode[]) ((IStructuredSelection) selectionProvider.getSelection())
                .toList().toArray(new RepositoryNode[] {});
        if (selectedNodes.length == 0) {
            this.setEnabled(false);
            return;
        }
        int i = 0;
        for (RepositoryNode node : selectedNodes) {
            if (node.getProperties(EProperties.CONTENT_TYPE) == RepositoryNodeType.FOLDER) {
                i++;
            }
        }
        if (i > 0) {
            this.setEnabled(false);
        } else {
            this.setEnabled(true);
        }
    }

    @Override
    public void selectionChanged(ISelection selection) {
        init();
    }

    @Override
    public void run() {
        IStructuredSelection selection = (IStructuredSelection) selectionProvider.getSelection();
        RepositoryNode firstNode = (RepositoryNode) selection.getFirstElement();
        if (firstNode.getProperties(EProperties.CONTENT_TYPE) == RepositoryNodeType.FOLDER) {
            firstNode = repositoryNodeManager.getRepositoryNodebyName(connParam.getRepositoryName());
        } else {
            firstNode = SQLBuilderRepositoryNodeManager.getRoot(firstNode);
        }
        List<String> repositoryNames = repositoryNodeManager.getALLReposotoryNodeNames();
        dialog.openEditor(firstNode, repositoryNames, connParam, isDefaultEditor);
    }
}
