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
import java.sql.Statement;

import org.talend.sqlbuilder.IConstants;
import org.talend.sqlbuilder.Messages;
import org.talend.sqlbuilder.SqlBuilderPlugin;
import org.talend.sqlbuilder.dataset.dataset.DataSet;
import org.talend.sqlbuilder.dbstructure.nodes.INode;
import org.talend.sqlbuilder.dbstructure.nodes.TableNode;

/**
 * @author Davy Vanherbergen
 * 
 */
public class PreviewTab extends AbstractDataSetTab {

    public String getLabelText() {
        return Messages.getString("DatabaseDetailView.Tab.Preview"); //$NON-NLS-1$
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
            if (tableNode.getTableInfo() == null) {
                return null;
            }
            int maxResults = SqlBuilderPlugin.getDefault().getPluginPreferences().getInt(IConstants.PRE_ROW_COUNT);
            if (maxResults == 0) {
                maxResults = 50;
            }

            Statement statement = tableNode.getSession().getInteractiveConnection().createStatement();
            statement.setMaxRows(maxResults);
            statement.execute("select * from " + tableNode.getQualifiedName()); //$NON-NLS-1$
            ResultSet resultSet = statement.getResultSet();

            DataSet dataSet = new DataSet(null, resultSet, null);

            statement.close();
            resultSet.close();
            return dataSet;
        }

        return null;
    }

    public String getStatusMessage() {
        return Messages.getString("DatabaseDetailView.Tab.Preview.status", getNode().getQualifiedName()); //$NON-NLS-1$
    }
}
