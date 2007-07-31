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
package org.talend.expressionbuilder.ui.proposal;

import org.eclipse.jface.fieldassist.IContentProposal;
import org.eclipse.jface.fieldassist.IContentProposalProvider;
import org.talend.expressionbuilder.ui.CategoryComposite;
import org.talend.expressionbuilder.ui.ExpressionBuilderDialog;

/**
 * yzhang class global comment. Detailled comment <br/>
 * 
 * $Id: ExpressionBuilderProposalProvider.java 下午06:16:45 2007-7-27 +0000 (2007-7-27) yzhang $
 * 
 */
public class ExpressionBuilderProposalProvider implements IContentProposalProvider {

    private CategoryComposite composite;

    /**
     * yzhang ExpressionBuilderProposalProvider constructor comment.
     */
    public ExpressionBuilderProposalProvider() {
        composite = ExpressionBuilderDialog.getCategoryComposite();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.fieldassist.IContentProposalProvider#getProposals(java.lang.String, int)
     */
    public IContentProposal[] getProposals(String contents, int position) {
        String sub = contents.substring(0, position);

        if (sub.endsWith(" ") || sub.endsWith("+")) {
            sub = "*C";
        } else {
            String[] arrays = sub.split("\" \"|\\+");
            sub = arrays[arrays.length - 1].trim();
        }

        return composite.getProposals(sub, position);
    }
}
