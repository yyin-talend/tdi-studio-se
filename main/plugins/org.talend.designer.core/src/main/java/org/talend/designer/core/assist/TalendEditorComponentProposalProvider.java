package org.talend.designer.core.assist;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import org.eclipse.jface.fieldassist.ContentProposal;
import org.eclipse.jface.fieldassist.IContentProposal;
import org.eclipse.jface.fieldassist.IContentProposalProvider;
import org.talend.core.model.components.IComponent;

class TalendEditorComponentProposalProvider implements IContentProposalProvider {

    /*
     * The proposals provided.
     */
    private Set<String> proposals;

    private Map<String, IComponent> components;

    /**
     * Construct a TalendEditorComponentProposalProvider whose content proposals are always the specified set of
     * Component.
     * 
     * @param components the Map of Components to be returned whenever proposals are requested.
     */
    public TalendEditorComponentProposalProvider(Map<String, IComponent> components) {
        super();
        this.components = components;
        this.proposals = components.keySet();
    }

    /**
     * Return an array of Objects representing the valid content proposals for a field.
     * 
     * @param contents the current contents of the field (only consulted if filtering is set to <code>true</code>)
     * @param position the current cursor position within the field (ignored)
     * @return the array of Objects that represent valid proposals for the field given its current content.
     */
    public IContentProposal[] getProposals(String contents, int position) {
        contents = contents.toLowerCase();
        
        //judge the regex should be used or not
        boolean hasStarChar = contents.indexOf('*') == -1 ? false : true;
        if (hasStarChar) {
            contents = "(.)*" + contents.replace("*", "(.)*") + "(.)*";
        }
        
        ArrayList<IContentProposal> list = new ArrayList<IContentProposal>();
        for (String proposal : proposals) {
            if (proposal == null) {
                continue;
            }
            String tmp = proposal.toLowerCase();
            if (!hasStarChar) {
                if (tmp.contains(contents) && components.get(proposal) != null) {
                    list.add(new ContentProposal(proposal, components.get(proposal).getLongName()));
                }
            } else if (Pattern.matches(contents, tmp) && components.get(proposal) != null) {
                list.add(new ContentProposal(proposal, components.get(proposal).getLongName()));
            }
        }
        return list.toArray(new IContentProposal[0]);
    }
}
