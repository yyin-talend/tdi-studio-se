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
package org.talend.sqlbuilder.dbstructure.nodes;


import java.util.ArrayList;
import java.util.List;

import net.sourceforge.sqlexplorer.SQLAlias;
import net.sourceforge.squirrel_sql.fw.sql.ITableInfo;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.talend.core.model.metadata.builder.connection.DatabaseConnection;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.ui.images.CoreImageProvider;
import org.talend.sqlbuilder.SqlBuilderPlugin;
import org.talend.sqlbuilder.sessiontree.model.SessionTreeNode;

/**
 * Database catalog node.
 * 
 * @author Davy Vanherbergen
 * 
 */
public class CatalogNode extends AbstractNode {

    private List pchildNames = new ArrayList();

    private String[] pfilteredNames;

    /**
     * Create new database Catalog node.
     * 
     * @param parent node
     * @param name of this node
     * @param sessionNode session for this node
     */
    public CatalogNode(INode parent, String name, SessionTreeNode sessionNode) {

        psessionNode = sessionNode;
        pparent = parent;
        pname = name;

        pimageKey = "Images.CatalogNodeIcon";
    }

    /**
     * @return ChildNames.
     */
    @SuppressWarnings("unchecked")
    public String[] getChildNames() {

        if (pchildNames.size() == 0) {
            getChildNodes();
        }
        return (String[]) pchildNames.toArray(new String[] {});
    }

    /**
     * Returns "catalog" as the type for this node.
     * 
     * @return Type.
     * @see org.talend.sqlbuilder.dbstructure.nodes.INode#getType()
     */
    public String getType() {

        return "catalog";
    }

    /**
     * @return UniqueIdentifier.
     */
    public String getUniqueIdentifier() {

        return getQualifiedName();
    }

    /**
     * Checks if a node name should be filtered.
     * 
     * @param name to check for filtering
     * @return true if the name should be filtered
     */
    protected boolean isExcludedByFilter(String name) {

        if (pfilteredNames == null) {
            String filter = ((SQLAlias) getSession().getAlias()).getFolderFilterExpression();
            if (filter != null) {
                pfilteredNames = filter.split(",");
            }
        }
        if (pfilteredNames == null || pfilteredNames.length == 0) {
            // no active filter
            return false;
        }

        for (int i = 0; i < pfilteredNames.length; i++) {

            if (pfilteredNames[i].equalsIgnoreCase(name)) {
                // we have a match, exclude node..
                return false;
            }
        }

        // no match found
        return true;

    }

    /**
     * LoadChildren.
     */
    @SuppressWarnings("unchecked")
    public void loadChildren() {

        if (psessionNode.getInteractiveConnection() == null) {
            TableFolderNode node = new TableFolderNode(this, "TABLE", psessionNode, new ITableInfo[0]);
            addChildNode(node);
            return;
        }

        pchildNames = new ArrayList();

        try {

            ITableInfo[] tables = null;
            // String[] tableTypes = psessionNode.getMetaData().getTableTypes();
            String[] tableTypes = { "TABLE", "VIEW" };

            try {
                tables = psessionNode.getMetaData().getTables(null, getSchemaName(), "%", tableTypes);
            } catch (Throwable e) {
                SqlBuilderPlugin.log("Loading all tables at once is not supported", e);
            }

            // for (int i = 0; i < tableTypes.length; ++i) {
            // }
            TableFolderNode node = new TableFolderNode(this, "TABLE", psessionNode, tables);
            pchildNames.add(node.getLabelText());
            if (!isExcludedByFilter(node.getLabelText())) {
                addChildNode(node);
            }

        } catch (Throwable e) {
            SqlBuilderPlugin.log("Could not load childnodes for " + pname, e);
        }
    }

    /**
     * @return Image.
     */
    public Image getImage() {
        return CoreImageProvider.getImage(ERepositoryObjectType.METADATA_CONNECTIONS);
    }

    /**
     * @return Column label.
     * @param columnIndex column index.
     */
    @Override
    public String getLabelAtColumn(int columnIndex) {
        if (columnIndex == 0) {
            if (getSchemaName() != null && !getSchemaName().trim().equals("") && getLabelText() != null
                    && !getLabelText().trim().equals("")) {
                return getLabelText() + "." + getSchemaName();
            } else {
                return getLabelText();
            }
        } else if (columnIndex == 1) {
            return psessionNode.getRepositoryNode() == null ? null : psessionNode.getRepositoryName();
        }
        return null;
    }

    /**
     * @return SchemaName.
     */
    public String getSchemaName() {

        DatabaseConnection databaseConnection = getSession().getDatabaseConnection();
        if (databaseConnection == null) {
            return null;
        }
        String schema = databaseConnection.getSchema();
        if (schema != null && schema.length() == 0) {
            return null;
        }
        return schema;
    }

    /**
     * @return Background color.
     */
    @Override
    public Color getBackground() {
        if (getLabelText() == null || getLabelText().trim().equals("")) {
            return Display.getDefault().getSystemColor(SWT.COLOR_RED);
        } else {
            return super.getBackground();
        }
    }

    /**
     * @return Foreground color.
     */
    @Override
    public Color getForeground() {
        if (getLabelText() == null || getLabelText().trim().equals("")) {
            return Display.getDefault().getSystemColor(SWT.COLOR_WHITE);
        } else {
            return super.getBackground();
        }
    }

    /**
     * @return ChildNodes.
     */
    @Override
    public INode[] getChildNodes() {
        INode[] nodes = super.getChildNodes();

        if (nodes == null || nodes.length < 1) {
            return nodes;
        }
        return nodes[0].getChildNodes();
    }

}
