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

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.squirrel_sql.fw.sql.ITableInfo;

import org.talend.sqlbuilder.Messages;
import org.talend.sqlbuilder.SqlBuilderPlugin;

/**
 * DOC dev  class global comment. Detailled comment
 * <br/>
 *
 * $Id: IndexFolderNode.java,v 1.4 2006/11/01 05:40:59 peiqin.hou Exp $
 *
 */
public class IndexFolderNode extends AbstractFolderNode {

    private static final int COLUMN_INDEX = 6;
    private ITableInfo ptableInfo;


    /**
     * Create new database table node.
     * 
     * @param parent node
     * @param name of this node
     * @param sessionNode session for this node
     */
    public IndexFolderNode(INode parent, ITableInfo tableInfo) {

        ptableInfo = tableInfo;
        psessionNode = parent.getSession();
        pparent = parent;
        pname = Messages.getString("DatabaseStructureView.node.Indexes");
    }


    /**
     * @return List of column names for this table.
     */
    @SuppressWarnings("unchecked")
    public List getIndexNames() {

        List indexNames = new ArrayList();

        try {
            ResultSet resultSet = psessionNode.getMetaData().getIndexInfo(ptableInfo);
            while (resultSet.next()) {
                String name = resultSet.getString(COLUMN_INDEX);
                if (!(name == null || indexNames.contains(name))) {
                    indexNames.add(name);
                }
            }

        } catch (Exception e) {
            SqlBuilderPlugin.log("Could not load index names", e);
        }
        return indexNames;
    }

    /**
     * @return Name.
     */
    public String getName() {

        return pname;
    }


    /**
     * @return Qualified table name
     */
    public String getQualifiedName() {

        return getParent().getQualifiedName() + "." + getType();
    }


    /**
     * Returns the table info type as the type for this node.
     * @return Type.
     * @see org.talend.sqlbuilder.dbstructure.nodes.INode#getType()
     */
    public String getType() {

        return "index_folder";
    }


    /**
     * 
     * 
     * @see org.talend.sqlbuilder.dbstructure.nodes.AbstractNode#loadChildren()
     */
    public void loadChildren() {

        try {
            Iterator it = getIndexNames().iterator();
            while (it.hasNext()) {
                addChildNode(new IndexNode(this, (String) it.next(), psessionNode, (TableNode) getParent()));
            }
        } catch (Exception e) {
            SqlBuilderPlugin.log("Could not create child nodes for " + getName(), e);
        }
    }

}
