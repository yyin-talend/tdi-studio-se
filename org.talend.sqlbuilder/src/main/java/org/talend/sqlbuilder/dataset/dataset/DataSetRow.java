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
package org.talend.sqlbuilder.dataset.dataset;


/**
 * DataSetRow, represents one row in a dataSet.
 * 
 * @author Davy Vanherbergen
 */
public class DataSetRow {

    private Object[] pvalues;

    /**
     * Create new DataSetRow with columnCount values.
     * 
     * @param columnCount number of columns
     */
    public DataSetRow(int columnCount) {
        pvalues = new Object[columnCount];
    }


    /**
     * Create initialized dataSetRow.
     * 
     * @param values
     */
    public DataSetRow(String[] values) {
        pvalues = values;
    }


    /**
     * Returns value of given column.
     * 
     * @param column first column is 0
     */
    public Object getObjectValue(int column) {

        Object tmp = pvalues[column];
        if (tmp != null) {
            return tmp;
        }
        return "<null>"; //$NON-NLS-1$
    }
    

    /**
     * Set the value for a given column.
     * 
     * @param column first column is 0
     * @param value
     */
    public void setValue(int column, Object value) {
        pvalues[column] = value;
    }

    
    /**
     * @return number of columns in this row
     */
    public int length() {
        if (pvalues == null) {
            return 0;
        }
        return pvalues.length;
    }
}
