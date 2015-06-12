package org.talend.designer.core.assist;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.talend.core.model.components.IComponent;
import org.talend.core.ui.images.CoreImageProvider;

class TalendEditorComponentLabelProvider extends LabelProvider {

    /*
     * return the icon of each proposal(non-Javadoc)
     * 
     * @see org.eclipse.jface.viewers.LabelProvider#getImage(java.lang.Object)
     */
    @Override
    public Image getImage(Object element) {
        if (element != null && element instanceof ComponentContentProposal) {
            ComponentContentProposal proposal = (ComponentContentProposal) element;
            IComponent iComponent = proposal.getComponent();
            if (iComponent != null && iComponent.getIcon16() != null) {
                return CoreImageProvider.getComponentImageFromDesc(iComponent.getIcon16());
            }
        }
        return super.getImage(element);
    }

    /*
     * return the label of each proposal(non-Javadoc)
     * 
     * @see org.eclipse.jface.viewers.LabelProvider#getText(java.lang.Object)
     */
    @Override
    public String getText(Object element) {
        if (element != null && element instanceof ComponentContentProposal) {
            ComponentContentProposal proposal = (ComponentContentProposal) element;
            String label = proposal.getLabel();
            IComponent iComponent = proposal.getComponent();
            if (iComponent != null) {
                return label + " - " + iComponent.getOriginalFamilyName(); //$NON-NLS-1$
            } else {
                return label;
            }
        }
        return super.getText(element);
    }

}
