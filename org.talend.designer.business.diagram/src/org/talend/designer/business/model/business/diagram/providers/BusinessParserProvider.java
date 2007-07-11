package org.talend.designer.business.model.business.diagram.providers;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.gmf.runtime.common.core.service.AbstractProvider;
import org.eclipse.gmf.runtime.common.core.service.IOperation;
import org.eclipse.gmf.runtime.common.ui.services.parser.GetParserOperation;
import org.eclipse.gmf.runtime.common.ui.services.parser.IParser;
import org.eclipse.gmf.runtime.common.ui.services.parser.IParserProvider;
import org.eclipse.gmf.runtime.notation.View;
import org.talend.designer.business.model.business.BusinessPackage;
import org.talend.designer.business.model.business.diagram.edit.parts.ActionBusinessItemNameEditPart;
import org.talend.designer.business.model.business.diagram.edit.parts.ActorBusinessItemNameEditPart;
import org.talend.designer.business.model.business.diagram.edit.parts.BidirectionalBusinessItemRelationshipNameEditPart;
import org.talend.designer.business.model.business.diagram.edit.parts.BusinessItemRelationshipNameEditPart;
import org.talend.designer.business.model.business.diagram.edit.parts.DataBusinessItemNameEditPart;
import org.talend.designer.business.model.business.diagram.edit.parts.DatabaseBusinessItemNameEditPart;
import org.talend.designer.business.model.business.diagram.edit.parts.DecisionBusinessItemNameEditPart;
import org.talend.designer.business.model.business.diagram.edit.parts.DirectionalBusinessItemRelationshipNameEditPart;
import org.talend.designer.business.model.business.diagram.edit.parts.DocumentBusinessItemNameEditPart;
import org.talend.designer.business.model.business.diagram.edit.parts.EllipseBusinessItemNameEditPart;
import org.talend.designer.business.model.business.diagram.edit.parts.GearBusinessItemNameEditPart;
import org.talend.designer.business.model.business.diagram.edit.parts.InputBusinessItemNameEditPart;
import org.talend.designer.business.model.business.diagram.edit.parts.ListBusinessItemNameEditPart;
import org.talend.designer.business.model.business.diagram.edit.parts.TerminalBusinessItemNameEditPart;
import org.talend.designer.business.model.business.diagram.part.BusinessVisualIDRegistry;

/**
 * @generated
 */
public class BusinessParserProvider extends AbstractProvider implements IParserProvider {

    /**
     * @generated
     */
    private IParser actionBusinessItemActionBusinessItemName_4001Parser;

    /**
     * @generated
     */
    private IParser getActionBusinessItemActionBusinessItemName_4001Parser() {
        if (actionBusinessItemActionBusinessItemName_4001Parser == null) {
            actionBusinessItemActionBusinessItemName_4001Parser = createActionBusinessItemActionBusinessItemName_4001Parser();
        }
        return actionBusinessItemActionBusinessItemName_4001Parser;
    }

    /**
     * @generated
     */
    protected IParser createActionBusinessItemActionBusinessItemName_4001Parser() {
        BusinessStructuralFeatureParser parser = new BusinessStructuralFeatureParser(BusinessPackage.eINSTANCE
                .getBusinessItem().getEStructuralFeature("name")); //$NON-NLS-1$
        return parser;
    }

    /**
     * @generated
     */
    private IParser terminalBusinessItemTerminalBusinessItemName_4002Parser;

    /**
     * @generated
     */
    private IParser getTerminalBusinessItemTerminalBusinessItemName_4002Parser() {
        if (terminalBusinessItemTerminalBusinessItemName_4002Parser == null) {
            terminalBusinessItemTerminalBusinessItemName_4002Parser = createTerminalBusinessItemTerminalBusinessItemName_4002Parser();
        }
        return terminalBusinessItemTerminalBusinessItemName_4002Parser;
    }

    /**
     * @generated
     */
    protected IParser createTerminalBusinessItemTerminalBusinessItemName_4002Parser() {
        BusinessStructuralFeatureParser parser = new BusinessStructuralFeatureParser(BusinessPackage.eINSTANCE
                .getBusinessItem().getEStructuralFeature("name")); //$NON-NLS-1$
        return parser;
    }

    /**
     * @generated
     */
    private IParser documentBusinessItemDocumentBusinessItemName_4003Parser;

    /**
     * @generated
     */
    private IParser getDocumentBusinessItemDocumentBusinessItemName_4003Parser() {
        if (documentBusinessItemDocumentBusinessItemName_4003Parser == null) {
            documentBusinessItemDocumentBusinessItemName_4003Parser = createDocumentBusinessItemDocumentBusinessItemName_4003Parser();
        }
        return documentBusinessItemDocumentBusinessItemName_4003Parser;
    }

    /**
     * @generated
     */
    protected IParser createDocumentBusinessItemDocumentBusinessItemName_4003Parser() {
        BusinessStructuralFeatureParser parser = new BusinessStructuralFeatureParser(BusinessPackage.eINSTANCE
                .getBusinessItem().getEStructuralFeature("name")); //$NON-NLS-1$
        return parser;
    }

    /**
     * @generated
     */
    private IParser databaseBusinessItemDatabaseBusinessItemName_4004Parser;

    /**
     * @generated
     */
    private IParser getDatabaseBusinessItemDatabaseBusinessItemName_4004Parser() {
        if (databaseBusinessItemDatabaseBusinessItemName_4004Parser == null) {
            databaseBusinessItemDatabaseBusinessItemName_4004Parser = createDatabaseBusinessItemDatabaseBusinessItemName_4004Parser();
        }
        return databaseBusinessItemDatabaseBusinessItemName_4004Parser;
    }

    /**
     * @generated
     */
    protected IParser createDatabaseBusinessItemDatabaseBusinessItemName_4004Parser() {
        BusinessStructuralFeatureParser parser = new BusinessStructuralFeatureParser(BusinessPackage.eINSTANCE
                .getBusinessItem().getEStructuralFeature("name")); //$NON-NLS-1$
        return parser;
    }

    /**
     * @generated
     */
    private IParser listBusinessItemListBusinessItemName_4005Parser;

    /**
     * @generated
     */
    private IParser getListBusinessItemListBusinessItemName_4005Parser() {
        if (listBusinessItemListBusinessItemName_4005Parser == null) {
            listBusinessItemListBusinessItemName_4005Parser = createListBusinessItemListBusinessItemName_4005Parser();
        }
        return listBusinessItemListBusinessItemName_4005Parser;
    }

    /**
     * @generated
     */
    protected IParser createListBusinessItemListBusinessItemName_4005Parser() {
        BusinessStructuralFeatureParser parser = new BusinessStructuralFeatureParser(BusinessPackage.eINSTANCE
                .getBusinessItem().getEStructuralFeature("name")); //$NON-NLS-1$
        return parser;
    }

    /**
     * @generated
     */
    private IParser dataBusinessItemDataBusinessItemName_4006Parser;

    /**
     * @generated
     */
    private IParser getDataBusinessItemDataBusinessItemName_4006Parser() {
        if (dataBusinessItemDataBusinessItemName_4006Parser == null) {
            dataBusinessItemDataBusinessItemName_4006Parser = createDataBusinessItemDataBusinessItemName_4006Parser();
        }
        return dataBusinessItemDataBusinessItemName_4006Parser;
    }

    /**
     * @generated
     */
    protected IParser createDataBusinessItemDataBusinessItemName_4006Parser() {
        BusinessStructuralFeatureParser parser = new BusinessStructuralFeatureParser(BusinessPackage.eINSTANCE
                .getBusinessItem().getEStructuralFeature("name")); //$NON-NLS-1$
        return parser;
    }

    /**
     * @generated
     */
    private IParser inputBusinessItemInputBusinessItemName_4007Parser;

    /**
     * @generated
     */
    private IParser getInputBusinessItemInputBusinessItemName_4007Parser() {
        if (inputBusinessItemInputBusinessItemName_4007Parser == null) {
            inputBusinessItemInputBusinessItemName_4007Parser = createInputBusinessItemInputBusinessItemName_4007Parser();
        }
        return inputBusinessItemInputBusinessItemName_4007Parser;
    }

    /**
     * @generated
     */
    protected IParser createInputBusinessItemInputBusinessItemName_4007Parser() {
        BusinessStructuralFeatureParser parser = new BusinessStructuralFeatureParser(BusinessPackage.eINSTANCE
                .getBusinessItem().getEStructuralFeature("name")); //$NON-NLS-1$
        return parser;
    }

    /**
     * @generated
     */
    private IParser decisionBusinessItemDecisionBusinessItemName_4008Parser;

    /**
     * @generated
     */
    private IParser getDecisionBusinessItemDecisionBusinessItemName_4008Parser() {
        if (decisionBusinessItemDecisionBusinessItemName_4008Parser == null) {
            decisionBusinessItemDecisionBusinessItemName_4008Parser = createDecisionBusinessItemDecisionBusinessItemName_4008Parser();
        }
        return decisionBusinessItemDecisionBusinessItemName_4008Parser;
    }

    /**
     * @generated
     */
    protected IParser createDecisionBusinessItemDecisionBusinessItemName_4008Parser() {
        BusinessStructuralFeatureParser parser = new BusinessStructuralFeatureParser(BusinessPackage.eINSTANCE
                .getBusinessItem().getEStructuralFeature("name")); //$NON-NLS-1$
        return parser;
    }

    /**
     * @generated
     */
    private IParser actorBusinessItemActorBusinessItemName_4010Parser;

    /**
     * @generated
     */
    private IParser getActorBusinessItemActorBusinessItemName_4010Parser() {
        if (actorBusinessItemActorBusinessItemName_4010Parser == null) {
            actorBusinessItemActorBusinessItemName_4010Parser = createActorBusinessItemActorBusinessItemName_4010Parser();
        }
        return actorBusinessItemActorBusinessItemName_4010Parser;
    }

    /**
     * @generated
     */
    protected IParser createActorBusinessItemActorBusinessItemName_4010Parser() {
        BusinessStructuralFeatureParser parser = new BusinessStructuralFeatureParser(BusinessPackage.eINSTANCE
                .getBusinessItem().getEStructuralFeature("name")); //$NON-NLS-1$
        return parser;
    }

    /**
     * @generated
     */
    private IParser ellipseBusinessItemEllipseBusinessItemName_4011Parser;

    /**
     * @generated
     */
    private IParser getEllipseBusinessItemEllipseBusinessItemName_4011Parser() {
        if (ellipseBusinessItemEllipseBusinessItemName_4011Parser == null) {
            ellipseBusinessItemEllipseBusinessItemName_4011Parser = createEllipseBusinessItemEllipseBusinessItemName_4011Parser();
        }
        return ellipseBusinessItemEllipseBusinessItemName_4011Parser;
    }

    /**
     * @generated
     */
    protected IParser createEllipseBusinessItemEllipseBusinessItemName_4011Parser() {
        BusinessStructuralFeatureParser parser = new BusinessStructuralFeatureParser(BusinessPackage.eINSTANCE
                .getBusinessItem().getEStructuralFeature("name")); //$NON-NLS-1$
        return parser;
    }

    /**
     * @generated
     */
    private IParser gearBusinessItemGearBusinessItemName_4012Parser;

    /**
     * @generated
     */
    private IParser getGearBusinessItemGearBusinessItemName_4012Parser() {
        if (gearBusinessItemGearBusinessItemName_4012Parser == null) {
            gearBusinessItemGearBusinessItemName_4012Parser = createGearBusinessItemGearBusinessItemName_4012Parser();
        }
        return gearBusinessItemGearBusinessItemName_4012Parser;
    }

    /**
     * @generated
     */
    protected IParser createGearBusinessItemGearBusinessItemName_4012Parser() {
        BusinessStructuralFeatureParser parser = new BusinessStructuralFeatureParser(BusinessPackage.eINSTANCE
                .getBusinessItem().getEStructuralFeature("name")); //$NON-NLS-1$
        return parser;
    }

    /**
     * @generated
     */
    private IParser businessItemRelationshipBusinessItemRelationshipName_4009Parser;

    /**
     * @generated
     */
    private IParser getBusinessItemRelationshipBusinessItemRelationshipName_4009Parser() {
        if (businessItemRelationshipBusinessItemRelationshipName_4009Parser == null) {
            businessItemRelationshipBusinessItemRelationshipName_4009Parser = createBusinessItemRelationshipBusinessItemRelationshipName_4009Parser();
        }
        return businessItemRelationshipBusinessItemRelationshipName_4009Parser;
    }

    /**
     * @generated
     */
    protected IParser createBusinessItemRelationshipBusinessItemRelationshipName_4009Parser() {
        BusinessStructuralFeatureParser parser = new BusinessStructuralFeatureParser(BusinessPackage.eINSTANCE
                .getBusinessItem().getEStructuralFeature("name")); //$NON-NLS-1$
        return parser;
    }

    /**
     * @generated
     */
    private IParser directionalBusinessItemRelationshipDirectionalBusinessItemRelationshipName_4013Parser;

    /**
     * @generated
     */
    private IParser getDirectionalBusinessItemRelationshipDirectionalBusinessItemRelationshipName_4013Parser() {
        if (directionalBusinessItemRelationshipDirectionalBusinessItemRelationshipName_4013Parser == null) {
            directionalBusinessItemRelationshipDirectionalBusinessItemRelationshipName_4013Parser = createDirectionalBusinessItemRelationshipDirectionalBusinessItemRelationshipName_4013Parser();
        }
        return directionalBusinessItemRelationshipDirectionalBusinessItemRelationshipName_4013Parser;
    }

    /**
     * @generated
     */
    protected IParser createDirectionalBusinessItemRelationshipDirectionalBusinessItemRelationshipName_4013Parser() {
        BusinessStructuralFeatureParser parser = new BusinessStructuralFeatureParser(BusinessPackage.eINSTANCE
                .getBusinessItem().getEStructuralFeature("name")); //$NON-NLS-1$
        return parser;
    }

    /**
     * @generated
     */
    private IParser bidirectionalBusinessItemRelationshipBidirectionalBusinessItemRelationshipName_4014Parser;

    /**
     * @generated
     */
    private IParser getBidirectionalBusinessItemRelationshipBidirectionalBusinessItemRelationshipName_4014Parser() {
        if (bidirectionalBusinessItemRelationshipBidirectionalBusinessItemRelationshipName_4014Parser == null) {
            bidirectionalBusinessItemRelationshipBidirectionalBusinessItemRelationshipName_4014Parser = createBidirectionalBusinessItemRelationshipBidirectionalBusinessItemRelationshipName_4014Parser();
        }
        return bidirectionalBusinessItemRelationshipBidirectionalBusinessItemRelationshipName_4014Parser;
    }

    /**
     * @generated
     */
    protected IParser createBidirectionalBusinessItemRelationshipBidirectionalBusinessItemRelationshipName_4014Parser() {
        BusinessStructuralFeatureParser parser = new BusinessStructuralFeatureParser(BusinessPackage.eINSTANCE
                .getBusinessItem().getEStructuralFeature("name")); //$NON-NLS-1$
        return parser;
    }

    /**
     * @generated
     */
    protected IParser getParser(int visualID) {
        switch (visualID) {
        case ActionBusinessItemNameEditPart.VISUAL_ID:
            return getActionBusinessItemActionBusinessItemName_4001Parser();
        case TerminalBusinessItemNameEditPart.VISUAL_ID:
            return getTerminalBusinessItemTerminalBusinessItemName_4002Parser();
        case DocumentBusinessItemNameEditPart.VISUAL_ID:
            return getDocumentBusinessItemDocumentBusinessItemName_4003Parser();
        case DatabaseBusinessItemNameEditPart.VISUAL_ID:
            return getDatabaseBusinessItemDatabaseBusinessItemName_4004Parser();
        case ListBusinessItemNameEditPart.VISUAL_ID:
            return getListBusinessItemListBusinessItemName_4005Parser();
        case DataBusinessItemNameEditPart.VISUAL_ID:
            return getDataBusinessItemDataBusinessItemName_4006Parser();
        case InputBusinessItemNameEditPart.VISUAL_ID:
            return getInputBusinessItemInputBusinessItemName_4007Parser();
        case DecisionBusinessItemNameEditPart.VISUAL_ID:
            return getDecisionBusinessItemDecisionBusinessItemName_4008Parser();
        case ActorBusinessItemNameEditPart.VISUAL_ID:
            return getActorBusinessItemActorBusinessItemName_4010Parser();
        case EllipseBusinessItemNameEditPart.VISUAL_ID:
            return getEllipseBusinessItemEllipseBusinessItemName_4011Parser();
        case GearBusinessItemNameEditPart.VISUAL_ID:
            return getGearBusinessItemGearBusinessItemName_4012Parser();
        case BusinessItemRelationshipNameEditPart.VISUAL_ID:
            return getBusinessItemRelationshipBusinessItemRelationshipName_4009Parser();
        case DirectionalBusinessItemRelationshipNameEditPart.VISUAL_ID:
            return getDirectionalBusinessItemRelationshipDirectionalBusinessItemRelationshipName_4013Parser();
        case BidirectionalBusinessItemRelationshipNameEditPart.VISUAL_ID:
            return getBidirectionalBusinessItemRelationshipBidirectionalBusinessItemRelationshipName_4014Parser();
        }
        return null;
    }

    /**
     * @generated
     */
    public IParser getParser(IAdaptable hint) {
        String vid = (String) hint.getAdapter(String.class);
        if (vid != null) {
            return getParser(BusinessVisualIDRegistry.getVisualID(vid));
        }
        View view = (View) hint.getAdapter(View.class);
        if (view != null) {
            return getParser(BusinessVisualIDRegistry.getVisualID(view));
        }
        return null;
    }

    /**
     * @generated
     */
    public boolean provides(IOperation operation) {
        if (operation instanceof GetParserOperation) {
            IAdaptable hint = ((GetParserOperation) operation).getHint();
            if (BusinessElementTypes.getElement(hint) == null) {
                return false;
            }
            return getParser(hint) != null;
        }
        return false;
    }
}
