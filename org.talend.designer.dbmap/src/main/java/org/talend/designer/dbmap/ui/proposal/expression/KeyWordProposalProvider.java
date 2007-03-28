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
package org.talend.designer.dbmap.ui.proposal.expression;

import java.util.ArrayList;
import java.util.Arrays;

import org.apache.commons.lang.ArrayUtils;
import org.eclipse.jface.fieldassist.IContentProposal;
import org.eclipse.jface.fieldassist.IContentProposalProvider;
import org.talend.commons.ui.swt.colorstyledtext.jedit.Mode;
import org.talend.commons.ui.swt.colorstyledtext.jedit.Modes;


/**
 * DOC amaumont  class global comment. Detailled comment
 * <br/>
 *
 * $Id: talend-code-templates.xml 1 2006-09-29 17:06:40Z nrousseau $
 *
 */
public class KeyWordProposalProvider implements IContentProposalProvider {

    private String languageMode;
    private String[] keyWorkKeysToLoad;

    
    
    /**
     * 
     * DOC amaumont KeyWordProposalProvider constructor comment.
     * @param languageMode
     * @param keyWorkKeysToLoad (ex: "KEYWORD1" or "KEYWORD2" or ...)
     */
    public KeyWordProposalProvider(String languageMode, String... keyWorkKeysToLoad) {
        super();
        this.languageMode = languageMode;
        this.keyWorkKeysToLoad = keyWorkKeysToLoad;
    }

    public String[] getKeyWords() {
        Mode mode = Modes.getMode(languageMode + ".xml"); //$NON-NLS-1$
        Object[] keywords = new Object[0];
        
        for (int i = 0; i < keyWorkKeysToLoad.length; i++) {
            String keywordTagName = keyWorkKeysToLoad[i];
            String[] keywordsSelected = mode.getDefaultRuleSet().getKeywords().get(keywordTagName); //$NON-NLS-1$
            keywords = ArrayUtils.addAll((Object[])keywords, (Object[])keywordsSelected);
        }
        return Arrays.asList(keywords).toArray(new String[0]);
    }

    
    /* (non-Javadoc)
     * @see org.eclipse.jface.fieldassist.IContentProposalProvider#getProposals(java.lang.String, int)
     */
    public IContentProposal[] getProposals(String contents, int position) {
        String[] keyWords = getKeyWords();
        ArrayList<IContentProposal> proposals = new ArrayList<IContentProposal>();
        for (int i = 0; i < keyWords.length; i++) {
            String keyWord = keyWords[i];
            proposals.add(new KeyWordContentProposal(keyWord));
            
        }
        return proposals.toArray(new IContentProposal[0]);
    }

}
