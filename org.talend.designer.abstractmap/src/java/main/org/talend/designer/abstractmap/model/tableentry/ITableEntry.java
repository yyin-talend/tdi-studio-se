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
package org.talend.designer.abstractmap.model.tableentry;

import java.util.List;

import org.talend.core.model.process.Problem;
import org.talend.designer.abstractmap.model.table.IDataMapTable;

/**
 * DOC amaumont class global comment. Detailled comment <br/>
 * 
 * $Id: IDataMapTableEntry.java 3869 2007-06-15 14:52:12Z amaumont $
 * 
 */
public interface ITableEntry {

    public String getName();

    public void setName(String name);

    public String getParentName();

    public IDataMapTable getParent();

    public String getExpression();

    public void setExpression(String expression);

    public List<Problem> getProblems();

    public void setProblems(List<Problem> errorMessage);

    /**
     * Return true if this entry is displayed in a SWT Table.
     * @return true if this entry is displayed in a SWT Table.
     */
    public boolean isTableEntry(); 

    /**
     * 
     * Return true if this entry represents a column.
     * @return true if this entry represents a column.
     */
    public boolean isColumnEntry(); 
    
}
