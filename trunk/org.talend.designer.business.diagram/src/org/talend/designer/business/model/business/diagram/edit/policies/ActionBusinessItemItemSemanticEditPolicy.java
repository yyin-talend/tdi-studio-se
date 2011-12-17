package org.talend.designer.business.model.business.diagram.edit.policies;

import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.UnexecutableCommand;
import org.eclipse.gmf.runtime.emf.type.core.commands.CreateRelationshipCommand;
import org.eclipse.gmf.runtime.emf.type.core.commands.DestroyElementCommand;
import org.eclipse.gmf.runtime.emf.type.core.requests.CreateRelationshipRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.DestroyElementRequest;
import org.eclipse.gmf.runtime.notation.View;
import org.talend.designer.business.model.business.BidirectionalBusinessItemRelationship;
import org.talend.designer.business.model.business.BusinessItemRelationship;
import org.talend.designer.business.model.business.BusinessItemShape;
import org.talend.designer.business.model.business.BusinessPackage;
import org.talend.designer.business.model.business.BusinessProcess;
import org.talend.designer.business.model.business.DirectionalBusinessItemRelationship;
import org.talend.designer.business.model.business.diagram.providers.BusinessElementTypes;

/**
 * @generated
 */
public class ActionBusinessItemItemSemanticEditPolicy extends BusinessBaseItemSemanticEditPolicy {

    /**
     * @generated
     */
    protected Command getDestroyElementCommand(DestroyElementRequest req) {
        return getMSLWrapper(new DestroyElementCommand(req) {

            protected EObject getElementToDestroy() {
                View view = (View) getHost().getModel();
                EAnnotation annotation = view.getEAnnotation("Shortcut"); //$NON-NLS-1$
                if (annotation != null) {
                    return view;
                }
                return super.getElementToDestroy();
            }

        });
    }

    /**
     * @generated
     */
    protected Command getCreateRelationshipCommand(CreateRelationshipRequest req) {
        if (BusinessElementTypes.BusinessItemRelationship_3001 == req.getElementType()) {
            return req.getTarget() == null ? getCreateStartOutgoingBusinessItemRelationship3001Command(req)
                    : getCreateCompleteIncomingBusinessItemRelationship3001Command(req);
        }
        if (BusinessElementTypes.DirectionalBusinessItemRelationship_3002 == req.getElementType()) {
            return req.getTarget() == null ? getCreateStartOutgoingDirectionalBusinessItemRelationship3002Command(req)
                    : getCreateCompleteIncomingDirectionalBusinessItemRelationship3002Command(req);
        }
        if (BusinessElementTypes.BidirectionalBusinessItemRelationship_3003 == req.getElementType()) {
            return req.getTarget() == null ? getCreateStartOutgoingBidirectionalBusinessItemRelationship3003Command(req)
                    : getCreateCompleteIncomingBidirectionalBusinessItemRelationship3003Command(req);
        }
        return super.getCreateRelationshipCommand(req);
    }

    /**
     * @generated
     */
    protected Command getCreateStartOutgoingBusinessItemRelationship3001Command(CreateRelationshipRequest req) {
        return new Command() {
        };
    }

    /**
     * @generated
     */
    protected Command getCreateCompleteIncomingBusinessItemRelationship3001Command(CreateRelationshipRequest req) {
        if (!(req.getSource() instanceof BusinessItemShape)) {
            return UnexecutableCommand.INSTANCE;
        }
        final BusinessProcess element = (BusinessProcess) getRelationshipContainer(req.getSource(),
                BusinessPackage.eINSTANCE.getBusinessProcess(), req.getElementType());
        if (element == null) {
            return UnexecutableCommand.INSTANCE;
        }
        if (req.getContainmentFeature() == null) {
            req.setContainmentFeature(BusinessPackage.eINSTANCE.getBusinessProcess_BusinessItems());
        }
        return getMSLWrapper(new CreateIncomingBusinessItemRelationship3001Command(req) {

            /**
             * @generated
             */
            protected EObject getElementToEdit() {
                return element;
            }
        });
    }

    /**
     * @generated
     */
    private static class CreateIncomingBusinessItemRelationship3001Command extends CreateRelationshipCommand {

        /**
         * @generated
         */
        public CreateIncomingBusinessItemRelationship3001Command(CreateRelationshipRequest req) {
            super(req);
        }

        /**
         * @generated
         */
        protected EClass getEClassToEdit() {
            return BusinessPackage.eINSTANCE.getBusinessProcess();
        };

        /**
         * @generated
         */
        protected void setElementToEdit(EObject element) {
            throw new UnsupportedOperationException();
        }

        /**
         * @generated
         */
        protected EObject doDefaultElementCreation() {
            BusinessItemRelationship newElement = (BusinessItemRelationship) super.doDefaultElementCreation();
            if (newElement != null) {
                newElement.setTarget((BusinessItemShape) getTarget());
                newElement.setSource((BusinessItemShape) getSource());
            }
            return newElement;
        }
    }

    /**
     * @generated
     */
    protected Command getCreateStartOutgoingDirectionalBusinessItemRelationship3002Command(CreateRelationshipRequest req) {
        return new Command() {
        };
    }

    /**
     * @generated
     */
    protected Command getCreateCompleteIncomingDirectionalBusinessItemRelationship3002Command(
            CreateRelationshipRequest req) {
        if (!(req.getSource() instanceof BusinessItemShape)) {
            return UnexecutableCommand.INSTANCE;
        }
        final BusinessProcess element = (BusinessProcess) getRelationshipContainer(req.getSource(),
                BusinessPackage.eINSTANCE.getBusinessProcess(), req.getElementType());
        if (element == null) {
            return UnexecutableCommand.INSTANCE;
        }
        if (req.getContainmentFeature() == null) {
            req.setContainmentFeature(BusinessPackage.eINSTANCE.getBusinessProcess_BusinessItems());
        }
        return getMSLWrapper(new CreateIncomingDirectionalBusinessItemRelationship3002Command(req) {

            /**
             * @generated
             */
            protected EObject getElementToEdit() {
                return element;
            }
        });
    }

    /**
     * @generated
     */
    private static class CreateIncomingDirectionalBusinessItemRelationship3002Command extends CreateRelationshipCommand {

        /**
         * @generated
         */
        public CreateIncomingDirectionalBusinessItemRelationship3002Command(CreateRelationshipRequest req) {
            super(req);
        }

        /**
         * @generated
         */
        protected EClass getEClassToEdit() {
            return BusinessPackage.eINSTANCE.getBusinessProcess();
        };

        /**
         * @generated
         */
        protected void setElementToEdit(EObject element) {
            throw new UnsupportedOperationException();
        }

        /**
         * @generated
         */
        protected EObject doDefaultElementCreation() {
            DirectionalBusinessItemRelationship newElement = (DirectionalBusinessItemRelationship) super
                    .doDefaultElementCreation();
            if (newElement != null) {
                newElement.setTarget((BusinessItemShape) getTarget());
                newElement.setSource((BusinessItemShape) getSource());
            }
            return newElement;
        }
    }

    /**
     * @generated
     */
    protected Command getCreateStartOutgoingBidirectionalBusinessItemRelationship3003Command(
            CreateRelationshipRequest req) {
        return new Command() {
        };
    }

    /**
     * @generated
     */
    protected Command getCreateCompleteIncomingBidirectionalBusinessItemRelationship3003Command(
            CreateRelationshipRequest req) {
        if (!(req.getSource() instanceof BusinessItemShape)) {
            return UnexecutableCommand.INSTANCE;
        }
        final BusinessProcess element = (BusinessProcess) getRelationshipContainer(req.getSource(),
                BusinessPackage.eINSTANCE.getBusinessProcess(), req.getElementType());
        if (element == null) {
            return UnexecutableCommand.INSTANCE;
        }
        if (req.getContainmentFeature() == null) {
            req.setContainmentFeature(BusinessPackage.eINSTANCE.getBusinessProcess_BusinessItems());
        }
        return getMSLWrapper(new CreateIncomingBidirectionalBusinessItemRelationship3003Command(req) {

            /**
             * @generated
             */
            protected EObject getElementToEdit() {
                return element;
            }
        });
    }

    /**
     * @generated
     */
    private static class CreateIncomingBidirectionalBusinessItemRelationship3003Command extends
            CreateRelationshipCommand {

        /**
         * @generated
         */
        public CreateIncomingBidirectionalBusinessItemRelationship3003Command(CreateRelationshipRequest req) {
            super(req);
        }

        /**
         * @generated
         */
        protected EClass getEClassToEdit() {
            return BusinessPackage.eINSTANCE.getBusinessProcess();
        };

        /**
         * @generated
         */
        protected void setElementToEdit(EObject element) {
            throw new UnsupportedOperationException();
        }

        /**
         * @generated
         */
        protected EObject doDefaultElementCreation() {
            BidirectionalBusinessItemRelationship newElement = (BidirectionalBusinessItemRelationship) super
                    .doDefaultElementCreation();
            if (newElement != null) {
                newElement.setTarget((BusinessItemShape) getTarget());
                newElement.setSource((BusinessItemShape) getSource());
            }
            return newElement;
        }
    }
}
