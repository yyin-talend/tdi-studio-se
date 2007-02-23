package org.talend.designer.business.model.business.diagram.view.factories;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.gmf.runtime.diagram.core.util.ViewUtil;
import org.eclipse.gmf.runtime.diagram.ui.view.factories.AbstractShapeViewFactory;
import org.eclipse.gmf.runtime.notation.NotationFactory;
import org.eclipse.gmf.runtime.notation.View;
import org.talend.designer.business.model.business.diagram.edit.parts.BusinessProcessEditPart;
import org.talend.designer.business.model.business.diagram.edit.parts.TerminalBusinessItemNameEditPart;
import org.talend.designer.business.model.business.diagram.part.BusinessVisualIDRegistry;

/**
 * @generated
 */
public class TerminalBusinessItemViewFactory extends AbstractShapeViewFactory {

    /**
     * @generated
     */
    protected List createStyles(View view) {
        List styles = new ArrayList();
        styles.add(NotationFactory.eINSTANCE.createFontStyle());
        styles.add(NotationFactory.eINSTANCE.createDescriptionStyle());
        styles.add(NotationFactory.eINSTANCE.createFillStyle());
        styles.add(NotationFactory.eINSTANCE.createLineStyle());
        return styles;
    }

    /**
     * @generated
     */
    protected void decorateView(View containerView, View view, IAdaptable semanticAdapter, String semanticHint,
            int index, boolean persisted) {
        if (semanticHint == null) {
            semanticHint = BusinessVisualIDRegistry
                    .getType(org.talend.designer.business.model.business.diagram.edit.parts.TerminalBusinessItemEditPart.VISUAL_ID);
            view.setType(semanticHint);
        }
        super.decorateView(containerView, view, semanticAdapter, semanticHint, index, persisted);
        if (!BusinessProcessEditPart.MODEL_ID.equals(BusinessVisualIDRegistry.getModelID(containerView))) {
            EAnnotation shortcutAnnotation = EcoreFactory.eINSTANCE.createEAnnotation();
            shortcutAnnotation.setSource("Shortcut"); //$NON-NLS-1$
            shortcutAnnotation.getDetails().put("modelID", BusinessProcessEditPart.MODEL_ID); //$NON-NLS-1$
            view.getEAnnotations().add(shortcutAnnotation);
        }
        getViewService().createNode(semanticAdapter, view,
                BusinessVisualIDRegistry.getType(TerminalBusinessItemNameEditPart.VISUAL_ID), ViewUtil.APPEND, true,
                getPreferencesHint());
    }

}
