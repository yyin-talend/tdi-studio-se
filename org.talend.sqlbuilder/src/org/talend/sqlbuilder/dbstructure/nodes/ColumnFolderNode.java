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

import java.util.Comparator;
import java.util.Iterator;

import net.sourceforge.squirrel_sql.fw.sql.ITableInfo;

import org.talend.sqlbuilder.Messages;
import org.talend.sqlbuilder.SqlBuilderPlugin;

/**
 * DOC dev  class global comment. Detailled comment
 * <br/>
 *
 * $Id: ColumnFolderNode.java,v 1.4 2006/11/01 05:40:59 peiqin.hou Exp $
 *
 */
public class ColumnFolderNode extends AbstractFolderNode {

    /**
     * Create new database table node.
     * 
     * @param parent node
     * @param name of this node
     * @param sessionNode session for this node
     */
    public ColumnFolderNode(INode parent, ITableInfo tableInfo) {

        psessionNode = parent.getSession();
        pparent = parent;
        pname = Messages.getString("DatabaseStructureView.node.Columns");
    }


    /**
     * Sort columns: PK - FK - Name..
     * @return Comparator.
     */
    public Comparator getComparator() {

        return new Comparator() {

            public int compare(Object arg0, Object arg1) {

                if (arg0 == null || arg1 == null) {
                    return 0;
                }
                ColumnNode node1 = (ColumnNode) arg0;
                ColumnNode node2 = (ColumnNode) arg1;

                if (node1.isPrimaryKey() && !node2.isPrimaryKey()) {
                    return -1;
                }
                if (!node1.isPrimaryKey() && node2.isPrimaryKey()) {
                    return 1;
                }
                if (node1.isForeignKey() && !node2.isForeignKey()) {
                    return -1;
                }
                if (!node1.isForeignKey() && node2.isForeignKey()) {
                    return 1;
                }

                return node1.getName().compareTo(node2.getName());
            }

        };
    }

    /**
     * @return name.
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

        return "column_folder";
    }


    /**
     * 
     * 
     * @see org.talend.sqlbuilder.dbstructure.nodes.AbstractNode#loadChildren()
     */
    public void loadChildren() {

        try {
            Iterator it = ((TableNode) pparent).getColumnNames().iterator();
            while (it.hasNext()) {
                addChildNode(new ColumnNode(this, (String) it.next(), psessionNode, (TableNode) pparent, true));
            }
        } catch (Exception e) {
            SqlBuilderPlugin.log("Could not create child nodes for " + getName(), e);
        }
    }

}
