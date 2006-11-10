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

import net.sourceforge.squirrel_sql.fw.sql.ITableInfo;

import org.talend.sqlbuilder.Messages;
import org.talend.sqlbuilder.dataset.dataset.DataSet;
import org.talend.sqlbuilder.dbstructure.nodes.INode;
import org.talend.sqlbuilder.dbstructure.nodes.TableNode;

/**
 * 
 * Table information show in DB detail composite.
 * 
 * @author yzhang
 * 
 */
public class TableInfoTab extends AbstractDataSetTab {

    
    
    public String getLabelText() {
        return Messages.getString("DatabaseDetailView.Tab.Info");
    }
 
    public DataSet getDataSet() throws Exception {                
        
        INode node = getNode();
        
        if (node == null) {
            return null;
        }
        if(node.getSession() == null)
        {
            return null;
        }
        if (node instanceof TableNode) {
            TableNode tableNode = (TableNode) node;
            
            ITableInfo tableInfo = tableNode.getTableInfo();
            if(tableInfo == null)
            {
                return null;
            }
            String[] header = new String[2];
            header[0] = Messages.getString("DatabaseDetailView.Tab.Info.Property");
            header[1] = Messages.getString("DatabaseDetailView.Tab.Info.Value");
            
            String[][] data = new String[6][2];
            
            data[0][0] = Messages.getString("DatabaseDetailView.Tab.Info.Name");
            data[0][1] = tableInfo.getSimpleName();
            data[1][0] = Messages.getString("DatabaseDetailView.Tab.Info.QualifiedName");
            data[1][1] = tableInfo.getQualifiedName();
            data[2][0] = Messages.getString("DatabaseDetailView.Tab.Info.Catalog");
            data[2][1] = tableInfo.getCatalogName();
            data[3][0] = Messages.getString("DatabaseDetailView.Tab.Info.Schema");
            data[3][1] = tableInfo.getSchemaName();
            data[4][0] = Messages.getString("DatabaseDetailView.Tab.Info.Type");
            data[4][1] = tableInfo.getType();
            data[5][0] = Messages.getString("DatabaseDetailView.Tab.Info.Remarks");
            data[5][1] = tableInfo.getRemarks();
            
            int[] types = new int[2];
            types[0] = DataSet.TYPE_STRING;
            types[1] = DataSet.TYPE_STRING;
            
            DataSet dataSet = new DataSet(header, data, types);
                       
            return dataSet;
        }
        
        return null;
    }
    
    
    public String getStatusMessage() {
        return Messages.getString("DatabaseDetailView.Tab.Info.status") + " " + getNode().getQualifiedName();
    }
}
