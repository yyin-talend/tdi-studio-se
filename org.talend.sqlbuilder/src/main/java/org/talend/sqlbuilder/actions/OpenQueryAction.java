// ============================================================================
//
// Talend Community Edition
//
// Copyright (C) 2006-2007 Talend - www.talend.com
//
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either
// version 2.1 of the License.
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

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.talend.repository.model.RepositoryNode;
import org.talend.repository.model.RepositoryNode.EProperties;
import org.talend.sqlbuilder.Messages;
import org.talend.sqlbuilder.dbstructure.RepositoryNodeType;
import org.talend.sqlbuilder.dbstructure.DBTreeProvider.QueryRepositoryObject;
import org.talend.sqlbuilder.repository.utility.SQLBuilderRepositoryNodeManager;
import org.talend.sqlbuilder.ui.ISQLBuilderDialog;
import org.talend.sqlbuilder.util.ConnectionParameters;

/**
 * DOC qianbing class global comment. Detailled comment <br/>
 * 
 * $Id: talend-code-templates.xml,v 1.3 2006/11/01 05:38:28 nicolas Exp $
 * 
 */
public class OpenQueryAction extends OpenNewEditorAction {

    private final ISQLBuilderDialog dialog;

    /**
     * DOC qianbing OpenQueryAction constructor comment.
     */
    public OpenQueryAction(TreeViewer selectionProvider, ISQLBuilderDialog d, ConnectionParameters connParam) {
        super(selectionProvider, d, connParam, false);
        this.dialog = d;
        setText(Messages.getString("DBStructureComposite.OpenQuery")); //$NON-NLS-1$
        setToolTipText(Messages.getString("DBStructureComposite.OpenQuery")); //$NON-NLS-1$
    }

    SQLBuilderRepositoryNodeManager repositoryNodeManager = new SQLBuilderRepositoryNodeManager();

    @Override
    public void run() {
        RepositoryNode node = (RepositoryNode) getStructuredSelection().getFirstElement();
        if (node == null) {
            return;
        }
        if (node.getProperties(EProperties.CONTENT_TYPE) == RepositoryNodeType.QUERY) {
            List<String> repositoryName = repositoryNodeManager.getALLReposotoryNodeNames();
            ConnectionParameters connectionParameters = new ConnectionParameters();
            if (node.getObject() instanceof QueryRepositoryObject) {
                connectionParameters.setQueryObject(((QueryRepositoryObject) node.getObject()).getQuery());
            }
            connectionParameters.setQuery(dialog.getConnParameters().getQuery());
            connectionParameters.setShowDesignerPage(false);
            dialog.openEditor(node, repositoryName, connectionParameters, false);
        }
    }

    @Override
    public void selectionChanged(IStructuredSelection selection) {
        boolean enabled = true;
        if (selection.size() != 1) {
            enabled = false;
        } else {
            RepositoryNode node = (RepositoryNode) selection.getFirstElement();
            RepositoryNodeType type = (RepositoryNodeType) node.getProperties(EProperties.CONTENT_TYPE);
            if (type != RepositoryNodeType.QUERY) {
                enabled = false;
            }
        }
        setEnabled(enabled);
    }

}
