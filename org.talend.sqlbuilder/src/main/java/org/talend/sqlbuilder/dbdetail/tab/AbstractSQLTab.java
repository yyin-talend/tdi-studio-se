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

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import net.sourceforge.squirrel_sql.fw.sql.SQLConnection;

import org.talend.sqlbuilder.IConstants;
import org.talend.sqlbuilder.SqlBuilderPlugin;
import org.talend.sqlbuilder.dataset.dataset.DataSet;



/**
 * DOC dev  class global comment. Detailled comment
 * <br/>
 *
 * $Id: AbstractSQLTab.java,v 1.5 2006/11/01 01:27:07 yi.zhang Exp $
 *
 */
public abstract class AbstractSQLTab extends AbstractDataSetTab {
   

    public final DataSet getDataSet() throws Exception {
        
        DataSet dataSet = null;
        int timeOut = SqlBuilderPlugin.getDefault().getPluginPreferences().getInt(IConstants.INTERACTIVE_QUERY_TIMEOUT);
        
        SQLConnection connection = getNode().getSession().getInteractiveConnection();
        ResultSet rs = null;
        Statement stmt = null;
        PreparedStatement pStmt = null;
        
        try {
                    
            Object[] params = getSQLParameters();
            if (params == null || params.length == 0) {
                
                
                // use normal statement
                stmt = connection.createStatement();
                stmt.setQueryTimeout(timeOut);
                rs = stmt.executeQuery(getSQL());
                
            } else {
                
                // use prepared statement
                pStmt = connection.prepareStatement(getSQL());
                pStmt.setQueryTimeout(timeOut);
                
                for (int i = 0; i < params.length; i++) {
                    
                    if (params[i] instanceof String) {
                        pStmt.setString(i + 1, (String) params[i]);
                    } else if (params[i] instanceof Integer) {
                        pStmt.setInt(i + 1, ((Integer) params[i]).intValue());
                    } else if (params[i] instanceof String) {
                        pStmt.setLong(i + 1, ((Long) params[i]).longValue());
                    }                     
                }
                
                rs = pStmt.executeQuery();
            }
        
            dataSet = new DataSet(null, rs, null);
            
            rs.close();
            
        } catch (Exception e) {
            
            SqlBuilderPlugin.log("Couldn't load source for: " + getNode().getName(), e);
            
        } finally {
            
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (Exception e) {
                    SqlBuilderPlugin.log("Error closing statement", e);
                }
            }
            if (pStmt != null) {
                try {
                    pStmt.close();
                } catch (Exception e) {
                    SqlBuilderPlugin.log("Error closing statement", e);
                }
            }         
        }
        return dataSet;
        
    }

    public abstract String getLabelText();
    
    public abstract String getSQL();
    
    public Object[] getSQLParameters() {
        return null;
    }
}
