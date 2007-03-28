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
package org.talend.sqlbuilder.dbstructure.nodes;

import java.sql.ResultSet;
import java.util.Comparator;


import org.talend.sqlbuilder.Messages;
import org.talend.sqlbuilder.SqlBuilderPlugin;
import org.talend.sqlbuilder.sessiontree.model.SessionTreeNode;

/**
 * @author Davy Vanherbergen
 * 
 */
public class IndexNode extends AbstractNode {

    private static final int SORT_INDEX = 10;
    private static final int COLUMN_NAME_INDEX = 9;
    private static final int INDEX_NAME_INDEX = 6;
    private TableNode pparentTable;


    public IndexNode(INode node, String name, SessionTreeNode session, TableNode parentTable) throws Exception {

        pparentTable = parentTable;
        pparent = node;
        psessionNode = session;
        pname = name;
        pimageKey = "Images.IndexIcon"; //$NON-NLS-1$
    }

    /**
     * @return Comparator.
     */
    public Comparator getComparator() {

        // we don't want any sorting here.
        return null;
    }

    /**
     * @return QualifierName.
     */
    public String getQualifiedName() {

        return pparent.getParent().getName() + "." + pname; //$NON-NLS-1$
    }

    /**
     * @return Type.
     */
    public String getType() {

        return "index"; //$NON-NLS-1$
    }


    /**
     * @return UniqueIdentifier.
     */
    public String getUniqueIdentifier() {

        return pparent.getParent().getQualifiedName() + "." + pname; //$NON-NLS-1$
    }


    /**
     * LoadChildren.
     */
    public void loadChildren() {

        try {
            ResultSet resultSet = psessionNode.getMetaData().getIndexInfo(pparentTable.getTableInfo());
            while (resultSet.next()) {

                String indexName = resultSet.getString(INDEX_NAME_INDEX);
                String columnName = resultSet.getString(COLUMN_NAME_INDEX);
                String sort = resultSet.getString(SORT_INDEX);
                                
                if (indexName != null && indexName.equalsIgnoreCase(pname)) {
                    ColumnNode col = new ColumnNode(this, columnName, psessionNode, pparentTable, true);
                    if (sort == null || sort.equalsIgnoreCase("A")) { //$NON-NLS-1$
                        col.setLabelDecoration("ASC"); //$NON-NLS-1$
                    } else {
                        col.setLabelDecoration("DESC"); //$NON-NLS-1$
                    }
                    addChildNode(col);
                }
            }

        } catch (Exception e) {
            SqlBuilderPlugin.log(Messages.getString("IndexNode.logMessage"), e); //$NON-NLS-1$
        }

    }

}
