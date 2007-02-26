package org.talend.designer.business.model.business.diagram.part;

import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.gmf.runtime.notation.View;
import org.talend.designer.business.diagram.i18n.Messages;
import org.talend.designer.business.model.business.ActionBusinessItem;
import org.talend.designer.business.model.business.ActorBusinessItem;
import org.talend.designer.business.model.business.BusinessItemRelationship;
import org.talend.designer.business.model.business.BusinessPackage;
import org.talend.designer.business.model.business.BusinessProcess;
import org.talend.designer.business.model.business.DataBusinessItem;
import org.talend.designer.business.model.business.DatabaseBusinessItem;
import org.talend.designer.business.model.business.DecisionBusinessItem;
import org.talend.designer.business.model.business.DocumentBusinessItem;
import org.talend.designer.business.model.business.EllipseBusinessItem;
import org.talend.designer.business.model.business.GearBusinessItem;
import org.talend.designer.business.model.business.InputBusinessItem;
import org.talend.designer.business.model.business.ListBusinessItem;
import org.talend.designer.business.model.business.TerminalBusinessItem;
import org.talend.designer.business.model.business.diagram.edit.parts.ActionBusinessItemEditPart;
import org.talend.designer.business.model.business.diagram.edit.parts.ActionBusinessItemNameEditPart;
import org.talend.designer.business.model.business.diagram.edit.parts.ActorBusinessItemEditPart;
import org.talend.designer.business.model.business.diagram.edit.parts.ActorBusinessItemNameEditPart;
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
import org.talend.designer.business.model.business.diagram.edit.parts.EllipseBusinessItemEditPart;
import org.talend.designer.business.model.business.diagram.edit.parts.EllipseBusinessItemNameEditPart;
import org.talend.designer.business.model.business.diagram.edit.parts.GearBusinessItemEditPart;
import org.talend.designer.business.model.business.diagram.edit.parts.GearBusinessItemNameEditPart;
import org.talend.designer.business.model.business.diagram.edit.parts.InputBusinessItemEditPart;
import org.talend.designer.business.model.business.diagram.edit.parts.InputBusinessItemNameEditPart;
import org.talend.designer.business.model.business.diagram.edit.parts.ListBusinessItemEditPart;
import org.talend.designer.business.model.business.diagram.edit.parts.ListBusinessItemNameEditPart;
import org.talend.designer.business.model.business.diagram.edit.parts.TerminalBusinessItemEditPart;
import org.talend.designer.business.model.business.diagram.edit.parts.TerminalBusinessItemNameEditPart;

/**
 * This registry is used to determine which type of visual object should be created for the corresponding Diagram, Node,
 * ChildNode or Link represented by a domain model object.
 * 
 * @generated
 */
public class BusinessVisualIDRegistry {

    /**
     * @generated
     */
    private static final String DEBUG_KEY = BusinessDiagramEditorPlugin.getInstance().getBundle().getSymbolicName()
            + "/debug/visualID"; //$NON-NLS-1$

    /**
     * @generated
     */
    public static int getVisualID(View view) {
        if (view instanceof Diagram) {
            if (BusinessProcessEditPart.MODEL_ID.equals(view.getType())) {
                return BusinessProcessEditPart.VISUAL_ID;
            } else {
                return -1;
            }
        }
        return getVisualID(view.getType());
    }

    /**
     * @generated
     */
    public static String getModelID(View view) {
        View diagram = view.getDiagram();
        while (view != diagram) {
            EAnnotation annotation = view.getEAnnotation("Shortcut"); //$NON-NLS-1$
            if (annotation != null) {
                return (String) annotation.getDetails().get("modelID"); //$NON-NLS-1$
            }
            view = (View) view.eContainer();
        }
        return diagram != null ? diagram.getType() : null;
    }

    /**
     * @generated NOT
     */
    public static int getVisualID(String type) {
        try {
            return Integer.parseInt(type);
        } catch (NumberFormatException e) {
            if (Boolean.TRUE.toString().equalsIgnoreCase(Platform.getDebugOption(DEBUG_KEY))) {
                BusinessDiagramEditorPlugin.getInstance().logError(
                        Messages.getString("BusinessVisualIDRegistry.ParseError") + type); //$NON-NLS-1$
            }
        }
        return -1;
    }

    /**
     * @generated
     */
    public static String getType(int visualID) {
        return String.valueOf(visualID);
    }

    /**
     * @generated
     */
    public static int getDiagramVisualID(EObject domainElement) {
        if (domainElement == null) {
            return -1;
        }
        EClass domainElementMetaclass = domainElement.eClass();
        return getDiagramVisualID(domainElement, domainElementMetaclass);
    }

    /**
     * @generated
     */
    private static int getDiagramVisualID(EObject domainElement, EClass domainElementMetaclass) {
        if (BusinessPackage.eINSTANCE.getBusinessProcess().isSuperTypeOf(domainElementMetaclass)
                && isDiagramBusinessProcess_79((BusinessProcess) domainElement)) {
            return BusinessProcessEditPart.VISUAL_ID;
        }
        return getUnrecognizedDiagramID(domainElement);
    }

    /**
     * @generated
     */
    public static int getNodeVisualID(View containerView, EObject domainElement) {
        if (domainElement == null) {
            return -1;
        }
        EClass domainElementMetaclass = domainElement.eClass();
        return getNodeVisualID(containerView, domainElement, domainElementMetaclass, null);
    }

    /**
     * @generated
     */
    public static int getNodeVisualID(View containerView, EObject domainElement, EClass domainElementMetaclass,
            String semanticHint) {
        String containerModelID = getModelID(containerView);
        if (!BusinessProcessEditPart.MODEL_ID.equals(containerModelID)) {
            return -1;
        }
        int containerVisualID;
        if (BusinessProcessEditPart.MODEL_ID.equals(containerModelID)) {
            containerVisualID = getVisualID(containerView);
        } else {
            if (containerView instanceof Diagram) {
                containerVisualID = BusinessProcessEditPart.VISUAL_ID;
            } else {
                return -1;
            }
        }
        int nodeVisualID = semanticHint != null ? getVisualID(semanticHint) : -1;
        switch (containerVisualID) {
        case ActionBusinessItemEditPart.VISUAL_ID:
            if (ActionBusinessItemNameEditPart.VISUAL_ID == nodeVisualID) {
                return ActionBusinessItemNameEditPart.VISUAL_ID;
            }
            return getUnrecognizedActionBusinessItem_1001ChildNodeID(domainElement, semanticHint);
        case TerminalBusinessItemEditPart.VISUAL_ID:
            if (TerminalBusinessItemNameEditPart.VISUAL_ID == nodeVisualID) {
                return TerminalBusinessItemNameEditPart.VISUAL_ID;
            }
            return getUnrecognizedTerminalBusinessItem_1002ChildNodeID(domainElement, semanticHint);
        case DocumentBusinessItemEditPart.VISUAL_ID:
            if (DocumentBusinessItemNameEditPart.VISUAL_ID == nodeVisualID) {
                return DocumentBusinessItemNameEditPart.VISUAL_ID;
            }
            return getUnrecognizedDocumentBusinessItem_1003ChildNodeID(domainElement, semanticHint);
        case DatabaseBusinessItemEditPart.VISUAL_ID:
            if (DatabaseBusinessItemNameEditPart.VISUAL_ID == nodeVisualID) {
                return DatabaseBusinessItemNameEditPart.VISUAL_ID;
            }
            return getUnrecognizedDatabaseBusinessItem_1004ChildNodeID(domainElement, semanticHint);
        case ListBusinessItemEditPart.VISUAL_ID:
            if (ListBusinessItemNameEditPart.VISUAL_ID == nodeVisualID) {
                return ListBusinessItemNameEditPart.VISUAL_ID;
            }
            return getUnrecognizedListBusinessItem_1005ChildNodeID(domainElement, semanticHint);
        case DataBusinessItemEditPart.VISUAL_ID:
            if (DataBusinessItemNameEditPart.VISUAL_ID == nodeVisualID) {
                return DataBusinessItemNameEditPart.VISUAL_ID;
            }
            return getUnrecognizedDataBusinessItem_1006ChildNodeID(domainElement, semanticHint);
        case InputBusinessItemEditPart.VISUAL_ID:
            if (InputBusinessItemNameEditPart.VISUAL_ID == nodeVisualID) {
                return InputBusinessItemNameEditPart.VISUAL_ID;
            }
            return getUnrecognizedInputBusinessItem_1007ChildNodeID(domainElement, semanticHint);
        case DecisionBusinessItemEditPart.VISUAL_ID:
            if (DecisionBusinessItemNameEditPart.VISUAL_ID == nodeVisualID) {
                return DecisionBusinessItemNameEditPart.VISUAL_ID;
            }
            return getUnrecognizedDecisionBusinessItem_1008ChildNodeID(domainElement, semanticHint);
        case ActorBusinessItemEditPart.VISUAL_ID:
            if (ActorBusinessItemNameEditPart.VISUAL_ID == nodeVisualID) {
                return ActorBusinessItemNameEditPart.VISUAL_ID;
            }
            return getUnrecognizedActorBusinessItem_1009ChildNodeID(domainElement, semanticHint);
        case EllipseBusinessItemEditPart.VISUAL_ID:
            if (EllipseBusinessItemNameEditPart.VISUAL_ID == nodeVisualID) {
                return EllipseBusinessItemNameEditPart.VISUAL_ID;
            }
            return getUnrecognizedEllipseBusinessItem_1010ChildNodeID(domainElement, semanticHint);
        case GearBusinessItemEditPart.VISUAL_ID:
            if (GearBusinessItemNameEditPart.VISUAL_ID == nodeVisualID) {
                return GearBusinessItemNameEditPart.VISUAL_ID;
            }
            return getUnrecognizedGearBusinessItem_1011ChildNodeID(domainElement, semanticHint);
        case BusinessProcessEditPart.VISUAL_ID:
            if ((semanticHint == null || ActionBusinessItemEditPart.VISUAL_ID == nodeVisualID)
                    && BusinessPackage.eINSTANCE.getActionBusinessItem().isSuperTypeOf(domainElementMetaclass)
                    && (domainElement == null || isNodeActionBusinessItem_1001((ActionBusinessItem) domainElement))) {
                return ActionBusinessItemEditPart.VISUAL_ID;
            }
            if ((semanticHint == null || TerminalBusinessItemEditPart.VISUAL_ID == nodeVisualID)
                    && BusinessPackage.eINSTANCE.getTerminalBusinessItem().isSuperTypeOf(domainElementMetaclass)
                    && (domainElement == null || isNodeTerminalBusinessItem_1002((TerminalBusinessItem) domainElement))) {
                return TerminalBusinessItemEditPart.VISUAL_ID;
            }
            if ((semanticHint == null || DocumentBusinessItemEditPart.VISUAL_ID == nodeVisualID)
                    && BusinessPackage.eINSTANCE.getDocumentBusinessItem().isSuperTypeOf(domainElementMetaclass)
                    && (domainElement == null || isNodeDocumentBusinessItem_1003((DocumentBusinessItem) domainElement))) {
                return DocumentBusinessItemEditPart.VISUAL_ID;
            }
            if ((semanticHint == null || DatabaseBusinessItemEditPart.VISUAL_ID == nodeVisualID)
                    && BusinessPackage.eINSTANCE.getDatabaseBusinessItem().isSuperTypeOf(domainElementMetaclass)
                    && (domainElement == null || isNodeDatabaseBusinessItem_1004((DatabaseBusinessItem) domainElement))) {
                return DatabaseBusinessItemEditPart.VISUAL_ID;
            }
            if ((semanticHint == null || ListBusinessItemEditPart.VISUAL_ID == nodeVisualID)
                    && BusinessPackage.eINSTANCE.getListBusinessItem().isSuperTypeOf(domainElementMetaclass)
                    && (domainElement == null || isNodeListBusinessItem_1005((ListBusinessItem) domainElement))) {
                return ListBusinessItemEditPart.VISUAL_ID;
            }
            if ((semanticHint == null || DataBusinessItemEditPart.VISUAL_ID == nodeVisualID)
                    && BusinessPackage.eINSTANCE.getDataBusinessItem().isSuperTypeOf(domainElementMetaclass)
                    && (domainElement == null || isNodeDataBusinessItem_1006((DataBusinessItem) domainElement))) {
                return DataBusinessItemEditPart.VISUAL_ID;
            }
            if ((semanticHint == null || InputBusinessItemEditPart.VISUAL_ID == nodeVisualID)
                    && BusinessPackage.eINSTANCE.getInputBusinessItem().isSuperTypeOf(domainElementMetaclass)
                    && (domainElement == null || isNodeInputBusinessItem_1007((InputBusinessItem) domainElement))) {
                return InputBusinessItemEditPart.VISUAL_ID;
            }
            if ((semanticHint == null || DecisionBusinessItemEditPart.VISUAL_ID == nodeVisualID)
                    && BusinessPackage.eINSTANCE.getDecisionBusinessItem().isSuperTypeOf(domainElementMetaclass)
                    && (domainElement == null || isNodeDecisionBusinessItem_1008((DecisionBusinessItem) domainElement))) {
                return DecisionBusinessItemEditPart.VISUAL_ID;
            }
            if ((semanticHint == null || ActorBusinessItemEditPart.VISUAL_ID == nodeVisualID)
                    && BusinessPackage.eINSTANCE.getActorBusinessItem().isSuperTypeOf(domainElementMetaclass)
                    && (domainElement == null || isNodeActorBusinessItem_1009((ActorBusinessItem) domainElement))) {
                return ActorBusinessItemEditPart.VISUAL_ID;
            }
            if ((semanticHint == null || EllipseBusinessItemEditPart.VISUAL_ID == nodeVisualID)
                    && BusinessPackage.eINSTANCE.getEllipseBusinessItem().isSuperTypeOf(domainElementMetaclass)
                    && (domainElement == null || isNodeEllipseBusinessItem_1010((EllipseBusinessItem) domainElement))) {
                return EllipseBusinessItemEditPart.VISUAL_ID;
            }
            if ((semanticHint == null || GearBusinessItemEditPart.VISUAL_ID == nodeVisualID)
                    && BusinessPackage.eINSTANCE.getGearBusinessItem().isSuperTypeOf(domainElementMetaclass)
                    && (domainElement == null || isNodeGearBusinessItem_1011((GearBusinessItem) domainElement))) {
                return GearBusinessItemEditPart.VISUAL_ID;
            }
            return getUnrecognizedBusinessProcess_79ChildNodeID(domainElement, semanticHint);
        case BusinessItemRelationshipEditPart.VISUAL_ID:
            if (BusinessItemRelationshipNameEditPart.VISUAL_ID == nodeVisualID) {
                return BusinessItemRelationshipNameEditPart.VISUAL_ID;
            }
            return getUnrecognizedBusinessItemRelationship_3001LinkLabelID(semanticHint);
        }
        return -1;
    }

    /**
     * @generated
     */
    public static int getLinkWithClassVisualID(EObject domainElement) {
        if (domainElement == null) {
            return -1;
        }
        EClass domainElementMetaclass = domainElement.eClass();
        return getLinkWithClassVisualID(domainElement, domainElementMetaclass);
    }

    /**
     * @generated
     */
    public static int getLinkWithClassVisualID(EObject domainElement, EClass domainElementMetaclass) {
        if (BusinessPackage.eINSTANCE.getBusinessItemRelationship().isSuperTypeOf(domainElementMetaclass)
                && (domainElement == null || isLinkWithClassBusinessItemRelationship_3001((BusinessItemRelationship) domainElement))) {
            return BusinessItemRelationshipEditPart.VISUAL_ID;
        } else {
            return getUnrecognizedLinkWithClassID(domainElement);
        }
    }

    /**
     * User can change implementation of this method to check some additional conditions here.
     * 
     * @generated
     */
    private static boolean isDiagramBusinessProcess_79(BusinessProcess element) {
        return true;
    }

    /**
     * User can change implementation of this method to handle some specific situations not covered by default logic.
     * 
     * @generated
     */
    private static int getUnrecognizedDiagramID(EObject domainElement) {
        return -1;
    }

    /**
     * User can change implementation of this method to check some additional conditions here.
     * 
     * @generated
     */
    private static boolean isNodeActionBusinessItem_1001(ActionBusinessItem element) {
        return true;
    }

    /**
     * User can change implementation of this method to check some additional conditions here.
     * 
     * @generated
     */
    private static boolean isNodeTerminalBusinessItem_1002(TerminalBusinessItem element) {
        return true;
    }

    /**
     * User can change implementation of this method to check some additional conditions here.
     * 
     * @generated
     */
    private static boolean isNodeDocumentBusinessItem_1003(DocumentBusinessItem element) {
        return true;
    }

    /**
     * User can change implementation of this method to check some additional conditions here.
     * 
     * @generated
     */
    private static boolean isNodeDatabaseBusinessItem_1004(DatabaseBusinessItem element) {
        return true;
    }

    /**
     * User can change implementation of this method to check some additional conditions here.
     * 
     * @generated
     */
    private static boolean isNodeListBusinessItem_1005(ListBusinessItem element) {
        return true;
    }

    /**
     * User can change implementation of this method to check some additional conditions here.
     * 
     * @generated
     */
    private static boolean isNodeDataBusinessItem_1006(DataBusinessItem element) {
        return true;
    }

    /**
     * User can change implementation of this method to check some additional conditions here.
     * 
     * @generated
     */
    private static boolean isNodeInputBusinessItem_1007(InputBusinessItem element) {
        return true;
    }

    /**
     * User can change implementation of this method to check some additional conditions here.
     * 
     * @generated
     */
    private static boolean isNodeDecisionBusinessItem_1008(DecisionBusinessItem element) {
        return true;
    }

    /**
     * User can change implementation of this method to check some additional conditions here.
     * 
     * @generated
     */
    private static boolean isNodeActorBusinessItem_1009(ActorBusinessItem element) {
        return true;
    }

    /**
     * User can change implementation of this method to check some additional conditions here.
     * 
     * @generated
     */
    private static boolean isNodeEllipseBusinessItem_1010(EllipseBusinessItem element) {
        return true;
    }

    /**
     * User can change implementation of this method to check some additional conditions here.
     * 
     * @generated
     */
    private static boolean isNodeGearBusinessItem_1011(GearBusinessItem element) {
        return true;
    }

    /**
     * User can change implementation of this method to handle some specific situations not covered by default logic.
     * 
     * @generated
     */
    private static int getUnrecognizedActionBusinessItem_1001ChildNodeID(EObject domainElement, String semanticHint) {
        return -1;
    }

    /**
     * User can change implementation of this method to handle some specific situations not covered by default logic.
     * 
     * @generated
     */
    private static int getUnrecognizedTerminalBusinessItem_1002ChildNodeID(EObject domainElement, String semanticHint) {
        return -1;
    }

    /**
     * User can change implementation of this method to handle some specific situations not covered by default logic.
     * 
     * @generated
     */
    private static int getUnrecognizedDocumentBusinessItem_1003ChildNodeID(EObject domainElement, String semanticHint) {
        return -1;
    }

    /**
     * User can change implementation of this method to handle some specific situations not covered by default logic.
     * 
     * @generated
     */
    private static int getUnrecognizedDatabaseBusinessItem_1004ChildNodeID(EObject domainElement, String semanticHint) {
        return -1;
    }

    /**
     * User can change implementation of this method to handle some specific situations not covered by default logic.
     * 
     * @generated
     */
    private static int getUnrecognizedListBusinessItem_1005ChildNodeID(EObject domainElement, String semanticHint) {
        return -1;
    }

    /**
     * User can change implementation of this method to handle some specific situations not covered by default logic.
     * 
     * @generated
     */
    private static int getUnrecognizedDataBusinessItem_1006ChildNodeID(EObject domainElement, String semanticHint) {
        return -1;
    }

    /**
     * User can change implementation of this method to handle some specific situations not covered by default logic.
     * 
     * @generated
     */
    private static int getUnrecognizedInputBusinessItem_1007ChildNodeID(EObject domainElement, String semanticHint) {
        return -1;
    }

    /**
     * User can change implementation of this method to handle some specific situations not covered by default logic.
     * 
     * @generated
     */
    private static int getUnrecognizedDecisionBusinessItem_1008ChildNodeID(EObject domainElement, String semanticHint) {
        return -1;
    }

    /**
     * User can change implementation of this method to handle some specific situations not covered by default logic.
     * 
     * @generated
     */
    private static int getUnrecognizedActorBusinessItem_1009ChildNodeID(EObject domainElement, String semanticHint) {
        return -1;
    }

    /**
     * User can change implementation of this method to handle some specific situations not covered by default logic.
     * 
     * @generated
     */
    private static int getUnrecognizedEllipseBusinessItem_1010ChildNodeID(EObject domainElement, String semanticHint) {
        return -1;
    }

    /**
     * User can change implementation of this method to handle some specific situations not covered by default logic.
     * 
     * @generated
     */
    private static int getUnrecognizedGearBusinessItem_1011ChildNodeID(EObject domainElement, String semanticHint) {
        return -1;
    }

    /**
     * User can change implementation of this method to handle some specific situations not covered by default logic.
     * 
     * @generated
     */
    private static int getUnrecognizedBusinessProcess_79ChildNodeID(EObject domainElement, String semanticHint) {
        return -1;
    }

    /**
     * User can change implementation of this method to handle some specific situations not covered by default logic.
     * 
     * @generated
     */
    private static int getUnrecognizedBusinessItemRelationship_3001LinkLabelID(String semanticHint) {
        return -1;
    }

    /**
     * User can change implementation of this method to handle some specific situations not covered by default logic.
     * 
     * @generated
     */
    private static int getUnrecognizedLinkWithClassID(EObject domainElement) {
        return -1;
    }

    /**
     * User can change implementation of this method to check some additional conditions here.
     * 
     * @generated
     */
    private static boolean isLinkWithClassBusinessItemRelationship_3001(BusinessItemRelationship element) {
        return true;
    }
}
