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
 * $Id: SQLEditorAllProposal.java,v 1.5 2006/11/03 08:11:14 qiang.zhang Exp $
 *
 */
public class SQLEditorAllProposal implements IContentProposal {

    private String content;
    private String label;
    private String description;
    /**
     * DOC dev SQLEditorAllProposal constructor comment.
     */
    public SQLEditorAllProposal(String hasString, String allString) {
        super();
        label = allString;
        int index = allString.indexOf(".");
        if (index != -1) {
            label = allString.substring(index + 2, allString.length() - 1);
        }
        content = label.substring(hasString.length());
        description = allString;
        
    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.fieldassist.IContentProposal#getContent()
     */
    public String getContent() {
        // TODO Auto-generated method stub
        return content;
    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.fieldassist.IContentProposal#getCursorPosition()
     */
    public int getCursorPosition() {
        // TODO Auto-generated method stub
        return 0;
    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.fieldassist.IContentProposal#getDescription()
     */
    public String getDescription() {
        // TODO Auto-generated method stub
        return description;
    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.fieldassist.IContentProposal#getLabel()
     */
    public String getLabel() {
        return label;
    }

}
