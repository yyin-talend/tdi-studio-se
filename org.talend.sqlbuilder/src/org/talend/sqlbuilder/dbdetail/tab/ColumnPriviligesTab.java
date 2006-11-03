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
package org.talend.sqlbuilder.dbdetail.tab;

import java.sql.ResultSet;

import org.talend.sqlbuilder.Messages;
import org.talend.sqlbuilder.dataset.dataset.DataSet;
import org.talend.sqlbuilder.dbstructure.nodes.INode;
import org.talend.sqlbuilder.dbstructure.nodes.TableNode;

/**
 * @author Davy Vanherbergen
 * 
 */
public class ColumnPriviligesTab extends AbstractDataSetTab {

    
    
    public String getLabelText() {
        return Messages.getString("DatabaseDetailView.Tab.ColumnPriviliges");
    }
 
    public DataSet getDataSet() throws Exception {                
        
        INode node = getNode();
        
        if (node == null) {
            return null;
        }
        
        if (node instanceof TableNode) {
            TableNode tableNode = (TableNode) node;

            ResultSet resultSet = node.getSession().getMetaData().getColumnPrivileges(tableNode.getTableInfo());   
            DataSet dataSet = new DataSet(null, resultSet, new int[] {4,5,6,7,8});
            
            resultSet.close();
            return dataSet;
        }
        
        return null;
    }
 
    public String getStatusMessage() {
        return Messages.getString("DatabaseDetailView.Tab.ColumnPriviliges.status") + " " + getNode().getQualifiedName();
    }
}
