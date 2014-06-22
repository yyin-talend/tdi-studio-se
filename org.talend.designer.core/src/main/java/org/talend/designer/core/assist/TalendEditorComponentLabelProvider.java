package org.talend.designer.core.assist;

import java.util.Map;

import org.eclipse.jface.fieldassist.IContentProposal;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.talend.core.model.components.IComponent;
import org.talend.core.ui.images.CoreImageProvider;

class TalendEditorComponentLabelProvider extends LabelProvider {

    private Map<String, IComponent> components;

    public TalendEditorComponentLabelProvider(Map<String, IComponent> components) {
        this.components = components;
    }

    /*
     * return the icon of each proposal(non-Javadoc)
     * @see org.eclipse.jface.viewers.LabelProvider#getImage(java.lang.Object)
     */
    public Image getImage(Object element) {
        if (element != null && element instanceof IContentProposal) {
            IContentProposal proposal = (IContentProposal) element;
            IComponent iComponent = components.get(proposal.getLabel());
            if(iComponent != null && iComponent.getIcon16() != null){
                return CoreImageProvider.getComponentImageFromDesc(iComponent.getIcon16());
            }
        }
        return super.getImage(element);
    }
    
    /*
     * return the label of each proposal(non-Javadoc)
     * @see org.eclipse.jface.viewers.LabelProvider#getText(java.lang.Object)
     */
    public String getText(Object element) {
        if (element != null && element instanceof IContentProposal) {
            IContentProposal proposal = (IContentProposal) element;
            String label = proposal.getLabel();
            IComponent iComponent = components.get(label);
            if(iComponent != null){
                return label +" - "+iComponent.getOriginalFamilyName();
            }else{
                return label;
            }
        }
        return super.getText(element);
    }

}
