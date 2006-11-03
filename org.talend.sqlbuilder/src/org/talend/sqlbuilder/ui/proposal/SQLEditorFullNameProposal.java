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
package org.talend.sqlbuilder.ui.proposal;

import org.eclipse.jface.fieldassist.IContentProposal;


/**
 * DOC dev  class global comment. Detailled comment
 * <br/>
 *
 * $Id: SQLEditorFullNameProposal.java,v 1.3 2006/11/03 07:18:21 qiang.zhang Exp $
 *
 */
public class SQLEditorFullNameProposal implements IContentProposal {

    private String tableName = ""; 
    private String simpleTableName = "";
    private String lostTableName = "";
    
    
    /**
     * DOC dev SQLEditorFullNameProposal constructor comment.
     */
    public SQLEditorFullNameProposal(String hasTableName, String tableName) {
        super();
        this.tableName = tableName;
        simpleTableName = tableName.substring(tableName.indexOf(".") + 2, tableName.length() - 1);
        lostTableName = tableName.substring(hasTableName.length());
    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.fieldassist.IContentProposal#getContent()
     */
    public String getContent() {
        return lostTableName;
    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.fieldassist.IContentProposal#getCursorPosition()
     */
    public int getCursorPosition() {
        return tableName.length() + 1;
    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.fieldassist.IContentProposal#getDescription()
     */
    public String getDescription() {
        return tableName;
    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.fieldassist.IContentProposal#getLabel()
     */
    public String getLabel() {
        
        return simpleTableName;
    }

}
