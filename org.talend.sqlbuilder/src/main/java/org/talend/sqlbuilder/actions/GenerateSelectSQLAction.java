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

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.talend.sqlbuilder.Messages;
import org.talend.sqlbuilder.SqlBuilderPlugin;
import org.talend.sqlbuilder.dbstructure.SessionTreeNodeUtils;
import org.talend.sqlbuilder.dbstructure.nodes.ColumnNode;
import org.talend.sqlbuilder.dbstructure.nodes.INode;
import org.talend.sqlbuilder.dbstructure.nodes.TableNode;
import org.talend.sqlbuilder.sessiontree.model.SessionTreeNode;
import org.talend.sqlbuilder.ui.SQLBuilderTabComposite;
import org.talend.sqlbuilder.util.ConnectionParameters;
import org.talend.sqlbuilder.util.ImageUtil;

/**
 * Detailled comment for this class. <br/> $Id: GenerateSelectSQLAction.java,v 1.13 2006/11/09 07:24:13 tangfn Exp $
 * 
 * @author Hou Peiqin (Soyatec)
 * 
 */
public class GenerateSelectSQLAction extends Action {

    private static final ImageDescriptor SQL_EDITOR_IMAGE = ImageUtil.getDescriptor("Images.SqlEditorIcon");

    private INode[] selectedNodes;

    private SQLBuilderTabComposite editorComposite;
    private boolean isDefaultEditor;

    public GenerateSelectSQLAction(INode[] selectedNodes, SQLBuilderTabComposite editorComposite, boolean isDefaultEditor) {
        this.selectedNodes = selectedNodes;
        this.editorComposite = editorComposite;
        this.isDefaultEditor = isDefaultEditor;
    }
    
    public void setSelectedNodes(INode[] selectedNodes) {
        this.selectedNodes = selectedNodes;
    }

    /**
     * run.
     */
    @Override
    public void run() {
        try {

            String query = null;

            if (selectedNodes[0] instanceof ColumnNode) {
                query = createColumnSelect();
            }

            if (selectedNodes[0] instanceof TableNode) {
                query = createTableSelect();
            }

            if (query == null) {
                return;
            }
            List repositoryNames = SessionTreeNodeUtils.getRepositoryNames();
            SessionTreeNode node = selectedNodes[0].getSession();
            ConnectionParameters connParam = new ConnectionParameters();
            connParam.setQuery(query);
            editorComposite.openNewEditor(node, repositoryNames, connParam, isDefaultEditor);
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

            INode node = selectedNodes[i];

            if (node instanceof ColumnNode && !((ColumnNode) node).isFromRepository()) {

                ColumnNode column = (ColumnNode) node;

                if (table.length() == 0) {
                    table = column.getQualifiedParentTableName();
                }

                if (column.getQualifiedParentTableName().equals(table)) {

                    query.append(sep);
                    query.append(column.getName());
                    sep = ", ";
                }
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

        TableNode node = (TableNode) selectedNodes[0];

        StringBuffer query = new StringBuffer("select ");
        String sep = "";

        List columnNames = node.getColumnNames();
        Iterator it = columnNames.iterator();

        while (it.hasNext()) {

            query.append(sep);
            String column = (String) it.next();
            query.append(column);
            sep = ", ";
        }

        query.append(" from ");
        query.append(node.getQualifiedName());

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

    /**
     * Set the text for the menu entry.
     * 
     * @see org.eclipse.jface.action.IAction#getText()
     * @return Text
     */
    public String getText() {

        return Messages.getString("DatabaseStructureView.Actions.GenerateSelectSQL");
    }

    /**
     * Action is always available.
     * 
     * @see net.sourceforge.sqlexplorer.dbstructure.actions.AbstractDBTreeContextAction#isAvailable()
     * @return isAvailable
     */
    public boolean isAvailable() {

        if (selectedNodes.length == 0) {
            return false;
        }

        if (selectedNodes[0] instanceof ColumnNode) {
            return true;
        }

        if (selectedNodes[0] instanceof TableNode) {
            return true;
        }

        return false;
    }
}