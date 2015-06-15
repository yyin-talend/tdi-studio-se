package org.talend.designer.core.assist;

import java.util.Iterator;
import java.util.List;

import org.eclipse.jface.fieldassist.IContentProposal;
import org.eclipse.jface.fieldassist.IContentProposalProvider;
import org.talend.commons.ui.runtime.image.ECoreImage;
import org.talend.commons.ui.runtime.image.ImageProvider;
import org.talend.core.model.components.IComponent;
import org.talend.core.model.components.IComponentsFactory;
import org.talend.designer.core.model.components.DummyComponent;
import org.talend.designer.core.ui.editor.TalendEditorPaletteFactory;
import org.talend.repository.model.ComponentsFactoryProvider;

class TalendEditorComponentProposalProvider implements IContentProposalProvider {

    protected IComponentsFactory componentsFactory = ComponentsFactoryProvider.getInstance();

    protected List<IContentProposal> proposalList;

    protected TalendEditorComponentCreationAssist componentAssistant;

    /**
     * Construct a TalendEditorComponentProposalProvider whose content proposals are always the specified set of
     * Component.
     * 
     * @param components the Map of Components to be returned whenever proposals are requested.
     */
    public TalendEditorComponentProposalProvider(TalendEditorComponentCreationAssist componentAssist,
            List<IContentProposal> proposalList) {
        super();
        this.componentAssistant = componentAssist;
        this.proposalList = proposalList;
    }

    /**
     * Return an array of Objects representing the valid content proposals for a field.
     * 
     * @param contents the current contents of the field (only consulted if filtering is set to <code>true</code>)
     * @param position the current cursor position within the field (ignored)
     * @return the array of Objects that represent valid proposals for the field given its current content.
     */
    @Override
    public IContentProposal[] getProposals(String contents, int position) {

        List<IComponent> relatedComponent = TalendEditorPaletteFactory.getRelatedComponents(componentsFactory, contents);
        if (componentAssistant != null) {
            relatedComponent = componentAssistant.filterComponents(relatedComponent);
        }
        proposalList.clear();
        if (relatedComponent != null && !relatedComponent.isEmpty()) {
            Iterator<IComponent> iter = relatedComponent.iterator();
            while (iter.hasNext()) {
                IComponent component = iter.next();
                // proposalList.add(new ComponentContentProposal(component.getName(), component.getLongName(),
                // component));
                proposalList.add(new ComponentContentProposal(component));
            }
        }

        /**
         * Always add Note
         */
        DummyComponent noteComponent = new DummyComponent("Note"); //$NON-NLS-1$
        noteComponent.setIcon16(ImageProvider.getImageDesc(ECoreImage.CODE_ICON));
        noteComponent.setOriginalFamilyName("Misc"); //$NON-NLS-1$
        proposalList.add(new ComponentContentProposal(noteComponent));

        return proposalList.toArray(new IContentProposal[0]);

    }
}
