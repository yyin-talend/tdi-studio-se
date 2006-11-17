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
import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.actions.SelectionProviderAction;
import org.talend.core.model.metadata.builder.connection.DatabaseConnection;
import org.talend.core.model.metadata.builder.connection.MetadataColumn;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.repository.model.RepositoryNode;
import org.talend.repository.model.RepositoryNode.EProperties;
import org.talend.sqlbuilder.SqlBuilderPlugin;
import org.talend.sqlbuilder.dbstructure.RepositoryNodeType;
import org.talend.sqlbuilder.dbstructure.DBTreeProvider.MetadataTableRepositoryObject;
import org.talend.sqlbuilder.repository.utility.SQLBuilderRepositoryNodeManager;
import org.talend.sqlbuilder.ui.ISQLBuilderDialog;
import org.talend.sqlbuilder.ui.SQLBuilderTabComposite;
import org.talend.sqlbuilder.util.ConnectionParameters;
import org.talend.sqlbuilder.util.ImageUtil;

/**
 * Detailled comment for this class. <br/> $Id: GenerateSelectSQLAction.java,v 1.13 2006/11/09 07:24:13 tangfn Exp $
 * 
 * @author Hou Peiqin (Soyatec)
 * 
 */
public class GenerateSelectSQLAction extends SelectionProviderAction {

    private static final ImageDescriptor SQL_EDITOR_IMAGE = ImageUtil.getDescriptor("Images.SqlEditorIcon");

    private SQLBuilderRepositoryNodeManager repositoryNodeManager = new SQLBuilderRepositoryNodeManager();

    private RepositoryNode[] selectedNodes;

    private ISelectionProvider provider;

    private ISQLBuilderDialog dialog;

    private boolean isDefaultEditor;

    @SuppressWarnings("unchecked")
    public GenerateSelectSQLAction(ISelectionProvider provider, ISQLBuilderDialog dialog, boolean isDefaultEditor) {
        super(provider, "Generate Select Statement");
        this.provider = provider;
        this.dialog = dialog;
        this.isDefaultEditor = isDefaultEditor;
        init();
    }

    @Override
    public void selectionChanged(IStructuredSelection selection) {
        init();
    }

    @SuppressWarnings("unchecked")
    public void init() {
        selectedNodes = (RepositoryNode[]) ((IStructuredSelection) provider.getSelection()).toList().toArray(
                new RepositoryNode[] {});
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
    @SuppressWarnings("static-access")
    @Override
    public void run() {
        try {

            String query = null;

            RepositoryNodeType repositoryNodeType = (RepositoryNodeType) selectedNodes[0]
                    .getProperties(EProperties.CONTENT_TYPE);

            if (repositoryNodeType == RepositoryNodeType.COLUMN) {
                query = createColumnSelect();
            }
            if (repositoryNodeType == RepositoryNodeType.TABLE) {
                query = createTableSelect();
            }

            if (query == null) {
                return;
            }
            List<String> repositoryNames = repositoryNodeManager.getALLReposotoryNodeNames();
            ConnectionParameters connParam = new ConnectionParameters();
            connParam.setQuery(query);

            dialog.openEditor(repositoryNodeManager.getRoot(selectedNodes[0]), repositoryNames, connParam,
                    isDefaultEditor);
        } catch (Throwable e) {
            SqlBuilderPlugin.log("Could generate sql.", e);
        }
    }

    /**
     * @return query string for full table select
     */
    private String createColumnSelect() {

        StringBuffer query = new StringBuffer("select ");
        String fix = getPrePostfix(selectedNodes[0]);
        String tablePrefix = getTablePrefix(selectedNodes[0]);
        String sep = "";
        String table = "";

        for (int i = 0; i < selectedNodes.length; i++) {

            RepositoryNode node = selectedNodes[i];
            if (node.getParent() != selectedNodes[0].getParent()) {
                continue;
            }

            if ((RepositoryNodeType) selectedNodes[0].getProperties(EProperties.CONTENT_TYPE) == RepositoryNodeType.COLUMN) {

                if (table.length() == 0) {
                    table = node.getParent().getObject().getStatusCode();
                }

                query.append(sep);
                query.append(fix + node.getObject().getLabel() + fix);
                sep = ", ";
            }
        }

        query.append(" from ");
        query.append(tablePrefix + table);

        return query.toString();

    }

    /**
     * @return query string for full table select
     */
    private String createTableSelect() {

        RepositoryNode node = (RepositoryNode) selectedNodes[0];
        String fix = getPrePostfix(node);
        String tablePrefix = getTablePrefix(node);

        StringBuffer query = new StringBuffer("select ");
        String sep = "";

        EList columns = ((MetadataTableRepositoryObject) node.getObject()).getTable().getColumns();
        Iterator it = columns.iterator();

        while (it.hasNext()) {

            query.append(sep);
            String column = ((MetadataColumn) it.next()).getOriginalField();
            query.append(fix + column + fix);
            sep = ", ";
        }

        query.append(" from ");
        query.append(tablePrefix + node.getObject().getStatusCode());

        return query.toString();
    }

    private String getTablePrefix(RepositoryNode node) {
        RepositoryNode root = repositoryNodeManager.getRoot(node);
        DatabaseConnection connection = (DatabaseConnection) ((ConnectionItem) root.getObject().getProperty().getItem())
                .getConnection();
        if (connection.getSchema() != null && !connection.getSchema().trim().equals("")) {
            return connection.getSchema() + ".";
        } else {
            return "";
        }
    }

    /**
     * Get Prepostfix.
     * 
     * @param node the selected node
     * @return PrePostfix
     */
    private String getPrePostfix(RepositoryNode node) {
        RepositoryNode root = repositoryNodeManager.getRoot(node);
        DatabaseConnection connection = (DatabaseConnection) ((ConnectionItem) root.getObject().getProperty().getItem())
                .getConnection();
        if (connection.getDatabaseType().equals("PostgreSQL")) {
            return "\"";
        }
        return "";
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
