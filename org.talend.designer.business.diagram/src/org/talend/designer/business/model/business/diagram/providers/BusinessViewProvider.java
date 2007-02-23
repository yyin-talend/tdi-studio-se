package org.talend.designer.business.model.business.diagram.providers;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gmf.runtime.diagram.core.providers.AbstractViewProvider;
import org.eclipse.gmf.runtime.emf.type.core.IElementType;
import org.eclipse.gmf.runtime.notation.View;
import org.talend.designer.business.model.business.diagram.edit.parts.ActionBusinessItemEditPart;
import org.talend.designer.business.model.business.diagram.edit.parts.ActionBusinessItemNameEditPart;
import org.talend.designer.business.model.business.diagram.edit.parts.BusinessItemRelationshipEditPart;
import org.talend.designer.business.model.business.diagram.edit.parts.BusinessItemRelationshipNameEditPart;
import org.talend.designer.business.model.business.diagram.edit.parts.BusinessProcessEditPart;
import org.talend.designer.business.model.business.diagram.edit.parts.DataBusinessItemEditPart;
import org.talend.designer.business.model.business.diagram.edit.parts.DataBusinessItemNameEditPart;
import org.talend.designer.business.model.business.diagram.edit.parts.DatabaseBusinessItemEditPart;
import org.talend.designer.business.model.business.diagram.edit.parts.DatabaseBusinessItemNameEditPart;
import org.talend.designer.business.model.business.diagram.edit.parts.DecisionBusinessItemEditPart;
import org.talend.designer.business.model.business.diagram.edit.parts.DecisionBusinessItemNameEditPart;
import org.talend.designer.business.model.business.diagram.edit.parts.DocumentBusinessItemEditPart;
import org.talend.designer.business.model.business.diagram.edit.parts.DocumentBusinessItemNameEditPart;
import org.talend.designer.business.model.business.diagram.edit.parts.InputBusinessItemEditPart;
import org.talend.designer.business.model.business.diagram.edit.parts.InputBusinessItemNameEditPart;
import org.talend.designer.business.model.business.diagram.edit.parts.ListBusinessItemEditPart;
import org.talend.designer.business.model.business.diagram.edit.parts.ListBusinessItemNameEditPart;
import org.talend.designer.business.model.business.diagram.edit.parts.TerminalBusinessItemEditPart;
import org.talend.designer.business.model.business.diagram.edit.parts.TerminalBusinessItemNameEditPart;
import org.talend.designer.business.model.business.diagram.part.BusinessVisualIDRegistry;
import org.talend.designer.business.model.business.diagram.view.factories.ActionBusinessItemNameViewFactory;
import org.talend.designer.business.model.business.diagram.view.factories.ActionBusinessItemViewFactory;
import org.talend.designer.business.model.business.diagram.view.factories.BusinessItemRelationshipNameViewFactory;
import org.talend.designer.business.model.business.diagram.view.factories.BusinessItemRelationshipViewFactory;
import org.talend.designer.business.model.business.diagram.view.factories.BusinessProcessViewFactory;
import org.talend.designer.business.model.business.diagram.view.factories.DataBusinessItemNameViewFactory;
import org.talend.designer.business.model.business.diagram.view.factories.DataBusinessItemViewFactory;
import org.talend.designer.business.model.business.diagram.view.factories.DatabaseBusinessItemNameViewFactory;
import org.talend.designer.business.model.business.diagram.view.factories.DatabaseBusinessItemViewFactory;
import org.talend.designer.business.model.business.diagram.view.factories.DecisionBusinessItemNameViewFactory;
import org.talend.designer.business.model.business.diagram.view.factories.DecisionBusinessItemViewFactory;
import org.talend.designer.business.model.business.diagram.view.factories.DocumentBusinessItemNameViewFactory;
import org.talend.designer.business.model.business.diagram.view.factories.DocumentBusinessItemViewFactory;
import org.talend.designer.business.model.business.diagram.view.factories.InputBusinessItemNameViewFactory;
import org.talend.designer.business.model.business.diagram.view.factories.InputBusinessItemViewFactory;
import org.talend.designer.business.model.business.diagram.view.factories.ListBusinessItemNameViewFactory;
import org.talend.designer.business.model.business.diagram.view.factories.ListBusinessItemViewFactory;
import org.talend.designer.business.model.business.diagram.view.factories.TerminalBusinessItemNameViewFactory;
import org.talend.designer.business.model.business.diagram.view.factories.TerminalBusinessItemViewFactory;

/**
 * @generated
 */
public class BusinessViewProvider extends AbstractViewProvider {

    /**
     * @generated
     */
    protected Class getDiagramViewClass(IAdaptable semanticAdapter, String diagramKind) {
        EObject semanticElement = getSemanticElement(semanticAdapter);
        if (BusinessProcessEditPart.MODEL_ID.equals(diagramKind)
                && BusinessVisualIDRegistry.getDiagramVisualID(semanticElement) != -1) {
            return BusinessProcessViewFactory.class;
        }
        return null;
    }

    /**
     * @generated
     */
    protected Class getNodeViewClass(IAdaptable semanticAdapter, View containerView, String semanticHint) {
        if (containerView == null) {
            return null;
        }
        IElementType elementType = getSemanticElementType(semanticAdapter);
        if (elementType != null && !BusinessElementTypes.isKnownElementType(elementType)) {
            return null;
        }
        EClass semanticType = getSemanticEClass(semanticAdapter);
        EObject semanticElement = getSemanticElement(semanticAdapter);
        int nodeVID = BusinessVisualIDRegistry.getNodeVisualID(containerView, semanticElement, semanticType,
                semanticHint);
        switch (nodeVID) {
        case ActionBusinessItemEditPart.VISUAL_ID:
            return ActionBusinessItemViewFactory.class;
        case ActionBusinessItemNameEditPart.VISUAL_ID:
            return ActionBusinessItemNameViewFactory.class;
        case TerminalBusinessItemEditPart.VISUAL_ID:
            return TerminalBusinessItemViewFactory.class;
        case TerminalBusinessItemNameEditPart.VISUAL_ID:
            return TerminalBusinessItemNameViewFactory.class;
        case DocumentBusinessItemEditPart.VISUAL_ID:
            return DocumentBusinessItemViewFactory.class;
        case DocumentBusinessItemNameEditPart.VISUAL_ID:
            return DocumentBusinessItemNameViewFactory.class;
        case DatabaseBusinessItemEditPart.VISUAL_ID:
            return DatabaseBusinessItemViewFactory.class;
        case DatabaseBusinessItemNameEditPart.VISUAL_ID:
            return DatabaseBusinessItemNameViewFactory.class;
        case ListBusinessItemEditPart.VISUAL_ID:
            return ListBusinessItemViewFactory.class;
        case ListBusinessItemNameEditPart.VISUAL_ID:
            return ListBusinessItemNameViewFactory.class;
        case DataBusinessItemEditPart.VISUAL_ID:
            return DataBusinessItemViewFactory.class;
        case DataBusinessItemNameEditPart.VISUAL_ID:
            return DataBusinessItemNameViewFactory.class;
        case InputBusinessItemEditPart.VISUAL_ID:
            return InputBusinessItemViewFactory.class;
        case InputBusinessItemNameEditPart.VISUAL_ID:
            return InputBusinessItemNameViewFactory.class;
        case DecisionBusinessItemEditPart.VISUAL_ID:
            return DecisionBusinessItemViewFactory.class;
        case DecisionBusinessItemNameEditPart.VISUAL_ID:
            return DecisionBusinessItemNameViewFactory.class;
        case BusinessItemRelationshipNameEditPart.VISUAL_ID:
            return BusinessItemRelationshipNameViewFactory.class;
        }
        return null;
    }

    /**
     * @generated
     */
    protected Class getEdgeViewClass(IAdaptable semanticAdapter, View containerView, String semanticHint) {
        IElementType elementType = getSemanticElementType(semanticAdapter);
        if (elementType != null && !BusinessElementTypes.isKnownElementType(elementType)) {
            return null;
        }
        EClass semanticType = getSemanticEClass(semanticAdapter);
        if (semanticType == null) {
            return null;
        }
        EObject semanticElement = getSemanticElement(semanticAdapter);
        int linkVID = BusinessVisualIDRegistry.getLinkWithClassVisualID(semanticElement, semanticType);
        switch (linkVID) {
        case BusinessItemRelationshipEditPart.VISUAL_ID:
            return BusinessItemRelationshipViewFactory.class;
        }
        return getUnrecognizedConnectorViewClass(semanticAdapter, containerView, semanticHint);
    }

    /**
     * @generated
     */
    private IElementType getSemanticElementType(IAdaptable semanticAdapter) {
        if (semanticAdapter == null) {
            return null;
        }
        return (IElementType) semanticAdapter.getAdapter(IElementType.class);
    }

    /**
     * @generated
     */
    private Class getUnrecognizedConnectorViewClass(IAdaptable semanticAdapter, View containerView, String semanticHint) {
        // Handle unrecognized child node classes here
        return null;
    }

}
