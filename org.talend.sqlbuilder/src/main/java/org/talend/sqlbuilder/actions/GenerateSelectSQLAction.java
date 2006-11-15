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

import java.util.Iterator;

import org.eclipse.emf.common.util.EList;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.actions.SelectionProviderAction;
import org.talend.core.model.metadata.builder.connection.MetadataColumn;
import org.talend.repository.model.RepositoryNode;
import org.talend.repository.model.RepositoryNode.EProperties;
import org.talend.sqlbuilder.SqlBuilderPlugin;
import org.talend.sqlbuilder.dbstructure.RepositoryNodeType;
import org.talend.sqlbuilder.dbstructure.DBTreeProvider.MetadataTableRepositoryObject;
import org.talend.sqlbuilder.ui.SQLBuilderTabComposite;
import org.talend.sqlbuilder.util.ImageUtil;

/**
 * Detailled comment for this class. <br/> $Id: GenerateSelectSQLAction.java,v 1.13 2006/11/09 07:24:13 tangfn Exp $
 * 
 * @author Hou Peiqin (Soyatec)
 * 
 */
public class GenerateSelectSQLAction extends SelectionProviderAction {

    private static final ImageDescriptor SQL_EDITOR_IMAGE = ImageUtil.getDescriptor("Images.SqlEditorIcon");

    private RepositoryNode[] selectedNodes;
    private ISelectionProvider provider;
    private SQLBuilderTabComposite editorComposite;
    private boolean isDefaultEditor;

    @SuppressWarnings("unchecked")
    public GenerateSelectSQLAction(ISelectionProvider provider, SQLBuilderTabComposite editorComposite, boolean isDefaultEditor) {
        super(provider, "Generate Select Statement");
        this.provider = provider;
        this.editorComposite = editorComposite;
        this.isDefaultEditor = isDefaultEditor;
        init();
    }
    
    @Override
    public void selectionChanged(ISelection selection) {
        init();
    }
    
    @SuppressWarnings("unchecked")
    public void init() {
        selectedNodes = (RepositoryNode[]) ((IStructuredSelection) provider.getSelection()).toList().toArray(new RepositoryNode[] {});
        if (selectedNodes.length == 0) {
            this.setEnabled(false);
            return;
        }
        int i = 0;
        for (RepositoryNode node : selectedNodes) {
            if (node.getProperties(EProperties.CONTENT_TYPE) == RepositoryNodeType.FOLDER 
                    || node.getProperties(EProperties.CONTENT_TYPE) == RepositoryNodeType.DATABASE) {
                i++;
            }
        }
        if (i > 0) {
            this.setEnabled(false);
        } else {
            this.setEnabled(true);
        }
    }
    /**
     * run.
     */
    @Override
    public void run() {
        try {

            String query = null;

            RepositoryNodeType repositoryNodeType = (RepositoryNodeType)selectedNodes[0].getProperties(EProperties.CONTENT_TYPE);
            
            if (repositoryNodeType == RepositoryNodeType.COLUMN) {
                query = createColumnSelect();
            }
            if (repositoryNodeType == RepositoryNodeType.TABLE) {
                query = createTableSelect();
            }

            if (query == null) {
                return;
            }
//            List repositoryNames = SessionTreeNodeUtils.getRepositoryNames();
//            SessionTreeNode node = selectedNodes[0].getSession();
//            ConnectionParameters connParam = new ConnectionParameters();
//            connParam.setQuery(query);
//            editorComposite.openNewEditor(node, repositoryNames, connParam, isDefaultEditor);
        } catch (Throwable e) {
            SqlBuilderPlugin.log("Could generate sql.", e);
        }
    }
    
    /**
     * @return query string for full table select
     */
    private String createColumnSelect() {

        StringBuffer query = new StringBuffer("select ");
        String sep = "";
        String table = "";

        for (int i = 0; i < selectedNodes.length; i++) {

            RepositoryNode node = selectedNodes[i];

            if ((RepositoryNodeType)selectedNodes[0].getProperties(EProperties.CONTENT_TYPE) == RepositoryNodeType.COLUMN) {

                if (table.length() == 0) {
                    table = node.getParent().getObject().getStatusCode();
                }

                query.append(sep);
                query.append(node.getObject().getLabel());
                sep = ", ";
            }
        }

        query.append(" from ");
        query.append(table);

        return query.toString();

    }

    /**
     * @return query string for full table select
     */
    private String createTableSelect() {

        RepositoryNode node = (RepositoryNode) selectedNodes[0];

        StringBuffer query = new StringBuffer("select ");
        String sep = "";

        EList columns = ((MetadataTableRepositoryObject) node.getObject()).getTable().getColumns();
        Iterator it = columns.iterator();

        while (it.hasNext()) {

            query.append(sep);
            String column = ((MetadataColumn) it.next()).getOriginalField();
            query.append(column);
            sep = ", ";
        }

        query.append(" from ");
        query.append(node.getObject().getStatusCode());

        return query.toString();
    }

    /**
     * Custom image for generate SQL action.
     * 
     * @see org.eclipse.jface.action.IAction#getImageDescriptor()
     * @return ImageDescriptor
     */
    public ImageDescriptor getImageDescriptor() {

        return SQL_EDITOR_IMAGE;
    }
    
}