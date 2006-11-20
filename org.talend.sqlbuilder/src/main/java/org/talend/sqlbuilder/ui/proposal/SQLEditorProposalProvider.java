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
import org.eclipse.jface.fieldassist.IContentProposalProvider;
import org.talend.repository.model.RepositoryNode;


/**
 * DOC dev  class global comment. Detailled comment
 * <br/>
 *
 * $Id: SQLEditorProposalProvider.java,v 1.1 2006/11/06 11:41:16 qiang.zhang Exp $
 *
 */
public class SQLEditorProposalProvider implements IContentProposalProvider {

//    private SessionTreeNode sessionTreeNode;
    private String language;
    private RepositoryNode node;
    
    /**
     * DOC dev SQLEditorContentProposalProvider constructor comment.
     */
    public SQLEditorProposalProvider(RepositoryNode node, String language) {
        this.language = language;
        this.node = node;
    }
    
    
    /**
     * Get IContentProposal[] will showed in PopupDialog.
     * @param contents colorText's all Content
     * @param cousor current position
     * @return contentProposal[] 
     */
    public IContentProposal[] getProposals(String contents, int position) {
        //TODO
        return new SQLEditorProposalUtil(node, language).getSQLEditorContentProposals(contents, position);
    }
}
