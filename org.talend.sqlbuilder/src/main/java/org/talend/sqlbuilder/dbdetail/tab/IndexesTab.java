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
package org.talend.sqlbuilder.dbdetail.tab;

import java.sql.ResultSet;

import net.sourceforge.squirrel_sql.fw.sql.ITableInfo;

import org.talend.core.model.metadata.builder.database.ExtractMetaDataFromDataBase;
import org.talend.sqlbuilder.Messages;
import org.talend.sqlbuilder.dataset.dataset.DataSet;
import org.talend.sqlbuilder.dbstructure.nodes.INode;
import org.talend.sqlbuilder.dbstructure.nodes.TableNode;
import org.talend.sqlbuilder.sessiontree.model.SessionTreeNode;

/**
 * @author Davy Vanherbergen
 * 
 */
public class IndexesTab extends AbstractDataSetTab {

    public String getLabelText() {
        return Messages.getString("DatabaseDetailView.Tab.Indexes"); //$NON-NLS-1$
    }

    public DataSet getDataSet() throws Exception {

        INode node = getNode();

        if (node == null) {
            return null;
        }
        if (node.getSession() == null) {
            return null;
        }
        if (node instanceof TableNode) {
            TableNode tableNode = (TableNode) node;
            ITableInfo ti = tableNode.getTableInfo();
            if (tableNode.getTableInfo() == null) {
                return null;
            }

            ResultSet resultSet = null;
            SessionTreeNode treeNode = node.getSession();

            // For synonym table, should get the corresponding table.
            if (ti.getType().equals("SYNONYM")) { //$NON-NLS-1$

                String realTableName = ExtractMetaDataFromDataBase.getTableNameBySynonym(treeNode
                        .getInteractiveConnection().getConnection(), ti.getSimpleName());

                resultSet = treeNode.getMetaData().getJDBCMetaData().getIndexInfo(ti.getCatalogName(),
                        ti.getSchemaName(), realTableName, true, true);

            } else {
                resultSet = node.getSession().getMetaData().getIndexInfo((tableNode.getTableInfo()));
            }

            DataSet dataSet = new DataSet(null, resultSet, new int[] { 4, 5, 6, 7, 8, 9, 10, 11, 12, 13 });

            resultSet.close();
            return dataSet;
        }

        return null;
    }

    public String getStatusMessage() {
        return Messages.getString("DatabaseDetailView.Tab.Indexes.status") + " " + getNode().getQualifiedName(); //$NON-NLS-1$ //$NON-NLS-2$
    }
}
