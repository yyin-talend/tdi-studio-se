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
package org.talend.sqlbuilder.sqlcontrol;

import org.talend.sqlbuilder.dataset.dataset.DataSet;




/**
 *
 */
public class SQLResult {

    private DataSet dataSet;
    
    private long executionTimeMillis;
    
    private String sqlStatement;

    
    
    /**
     * @return Returns the sqlStatement.
     */
    public String getSqlStatement() {
        return sqlStatement;
    }


    /**
     * @return Returns the dataSet.
     */
    public DataSet getDataSet() {
        return dataSet;
    }

    
    /**
     * @return Returns the executionTimeMillis.
     */
    public long getExecutionTimeMillis() {
        return executionTimeMillis;
    }


    
    /**
     * Sets the dataSet.
     * @param dataSet the dataSet to set
     */
    public void setDataSet(DataSet dataSet) {
        this.dataSet = dataSet;
    }


    
    /**
     * Sets the executionTimeMillis.
     * @param executionTimeMillis the executionTimeMillis to set
     */
    public void setExecutionTimeMillis(long executionTimeMillis) {
        this.executionTimeMillis = executionTimeMillis;
    }


    
    /**
     * Sets the sqlStatement.
     * @param sqlStatement the sqlStatement to set
     */
    public void setSqlStatement(String sqlStatement) {
        this.sqlStatement = sqlStatement;
    }


    
    


    
    
}
