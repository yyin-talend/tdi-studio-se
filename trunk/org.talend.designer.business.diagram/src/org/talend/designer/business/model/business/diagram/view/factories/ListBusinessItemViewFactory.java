package org.talend.designer.business.model.business.diagram.view.factories;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.gmf.runtime.diagram.core.util.ViewUtil;
import org.eclipse.gmf.runtime.diagram.ui.view.factories.AbstractShapeViewFactory;
import org.eclipse.gmf.runtime.draw2d.ui.figures.FigureUtilities;
import org.eclipse.gmf.runtime.notation.FillStyle;
import org.eclipse.gmf.runtime.notation.NotationFactory;
import org.eclipse.gmf.runtime.notation.NotationPackage;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.talend.designer.business.model.business.diagram.edit.parts.BusinessProcessEditPart;
import org.talend.designer.business.model.business.diagram.edit.parts.ListBusinessItemNameEditPart;
import org.talend.designer.business.model.business.diagram.part.BusinessVisualIDRegistry;

/**
 * @generated
 */
public class ListBusinessItemViewFactory extends AbstractShapeViewFactory {

    /**
     * @generated NOT
     */
    private static final Color DEFAULT_COLOR = new Color(null, new RGB(255, 153, 18));

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
    protected void decorateView(View containerView, View view, IAdaptable semanticAdapter, String semanticHint, int index,
            boolean persisted) {
        if (semanticHint == null) {
            semanticHint = BusinessVisualIDRegistry
                    .getType(org.talend.designer.business.model.business.diagram.edit.parts.ListBusinessItemEditPart.VISUAL_ID);
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
                BusinessVisualIDRegistry.getType(ListBusinessItemNameEditPart.VISUAL_ID), ViewUtil.APPEND, true,
                getPreferencesHint());
    }

    /**
     * @generated NOT
     */
    protected void initializeFromPreferences(View view) {
        super.initializeFromPreferences(view);
        ((FillStyle) view.getStyle(NotationPackage.Literals.FILL_STYLE)).setFillColor(FigureUtilities
                .colorToInteger(DEFAULT_COLOR));
    }

}
