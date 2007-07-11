package org.talend.designer.business.model.business.diagram.edit.policies;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gef.commands.Command;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.emf.commands.core.commands.DuplicateEObjectsCommand;
import org.eclipse.gmf.runtime.emf.type.core.commands.CreateElementCommand;
import org.eclipse.gmf.runtime.emf.type.core.requests.CreateElementRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.DuplicateElementsRequest;
import org.eclipse.gmf.runtime.notation.View;
import org.talend.designer.business.model.business.BusinessPackage;
import org.talend.designer.business.model.business.diagram.providers.BusinessElementTypes;

/**
 * @generated
 */
public class BusinessProcessItemSemanticEditPolicy extends BusinessBaseItemSemanticEditPolicy {

    /**
     * @generated
     */
    protected Command getCreateCommand(CreateElementRequest req) {
        if (BusinessElementTypes.ActionBusinessItem_1001 == req.getElementType()) {
            if (req.getContainmentFeature() == null) {
                req.setContainmentFeature(BusinessPackage.eINSTANCE.getBusinessProcess_BusinessItems());
            }
            return getMSLWrapper(new CreateActionBusinessItem_1001Command(req));
        }
        if (BusinessElementTypes.TerminalBusinessItem_1002 == req.getElementType()) {
            if (req.getContainmentFeature() == null) {
                req.setContainmentFeature(BusinessPackage.eINSTANCE.getBusinessProcess_BusinessItems());
            }
            return getMSLWrapper(new CreateTerminalBusinessItem_1002Command(req));
        }
        if (BusinessElementTypes.DocumentBusinessItem_1003 == req.getElementType()) {
            if (req.getContainmentFeature() == null) {
                req.setContainmentFeature(BusinessPackage.eINSTANCE.getBusinessProcess_BusinessItems());
            }
            return getMSLWrapper(new CreateDocumentBusinessItem_1003Command(req));
        }
        if (BusinessElementTypes.DatabaseBusinessItem_1004 == req.getElementType()) {
            if (req.getContainmentFeature() == null) {
                req.setContainmentFeature(BusinessPackage.eINSTANCE.getBusinessProcess_BusinessItems());
            }
            return getMSLWrapper(new CreateDatabaseBusinessItem_1004Command(req));
        }
        if (BusinessElementTypes.ListBusinessItem_1005 == req.getElementType()) {
            if (req.getContainmentFeature() == null) {
                req.setContainmentFeature(BusinessPackage.eINSTANCE.getBusinessProcess_BusinessItems());
            }
            return getMSLWrapper(new CreateListBusinessItem_1005Command(req));
        }
        if (BusinessElementTypes.DataBusinessItem_1006 == req.getElementType()) {
            if (req.getContainmentFeature() == null) {
                req.setContainmentFeature(BusinessPackage.eINSTANCE.getBusinessProcess_BusinessItems());
            }
            return getMSLWrapper(new CreateDataBusinessItem_1006Command(req));
        }
        if (BusinessElementTypes.InputBusinessItem_1007 == req.getElementType()) {
            if (req.getContainmentFeature() == null) {
                req.setContainmentFeature(BusinessPackage.eINSTANCE.getBusinessProcess_BusinessItems());
            }
            return getMSLWrapper(new CreateInputBusinessItem_1007Command(req));
        }
        if (BusinessElementTypes.DecisionBusinessItem_1008 == req.getElementType()) {
            if (req.getContainmentFeature() == null) {
                req.setContainmentFeature(BusinessPackage.eINSTANCE.getBusinessProcess_BusinessItems());
            }
            return getMSLWrapper(new CreateDecisionBusinessItem_1008Command(req));
        }
        if (BusinessElementTypes.ActorBusinessItem_1009 == req.getElementType()) {
            if (req.getContainmentFeature() == null) {
                req.setContainmentFeature(BusinessPackage.eINSTANCE.getBusinessProcess_BusinessItems());
            }
            return getMSLWrapper(new CreateActorBusinessItem_1009Command(req));
        }
        if (BusinessElementTypes.EllipseBusinessItem_1010 == req.getElementType()) {
            if (req.getContainmentFeature() == null) {
                req.setContainmentFeature(BusinessPackage.eINSTANCE.getBusinessProcess_BusinessItems());
            }
            return getMSLWrapper(new CreateEllipseBusinessItem_1010Command(req));
        }
        if (BusinessElementTypes.GearBusinessItem_1011 == req.getElementType()) {
            if (req.getContainmentFeature() == null) {
                req.setContainmentFeature(BusinessPackage.eINSTANCE.getBusinessProcess_BusinessItems());
            }
            return getMSLWrapper(new CreateGearBusinessItem_1011Command(req));
        }
        return super.getCreateCommand(req);
    }

    /**
     * @generated
     */
    private static class CreateActionBusinessItem_1001Command extends CreateElementCommand {

        /**
         * @generated
         */
        public CreateActionBusinessItem_1001Command(CreateElementRequest req) {
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
        protected EObject getElementToEdit() {
            EObject container = ((CreateElementRequest) getRequest()).getContainer();
            if (container instanceof View) {
                container = ((View) container).getElement();
            }
            return container;
        }
    }

    /**
     * @generated
     */
    private static class CreateTerminalBusinessItem_1002Command extends CreateElementCommand {

        /**
         * @generated
         */
        public CreateTerminalBusinessItem_1002Command(CreateElementRequest req) {
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
        protected EObject getElementToEdit() {
            EObject container = ((CreateElementRequest) getRequest()).getContainer();
            if (container instanceof View) {
                container = ((View) container).getElement();
            }
            return container;
        }
    }

    /**
     * @generated
     */
    private static class CreateDocumentBusinessItem_1003Command extends CreateElementCommand {

        /**
         * @generated
         */
        public CreateDocumentBusinessItem_1003Command(CreateElementRequest req) {
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
        protected EObject getElementToEdit() {
            EObject container = ((CreateElementRequest) getRequest()).getContainer();
            if (container instanceof View) {
                container = ((View) container).getElement();
            }
            return container;
        }
    }

    /**
     * @generated
     */
    private static class CreateDatabaseBusinessItem_1004Command extends CreateElementCommand {

        /**
         * @generated
         */
        public CreateDatabaseBusinessItem_1004Command(CreateElementRequest req) {
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
        protected EObject getElementToEdit() {
            EObject container = ((CreateElementRequest) getRequest()).getContainer();
            if (container instanceof View) {
                container = ((View) container).getElement();
            }
            return container;
        }
    }

    /**
     * @generated
     */
    private static class CreateListBusinessItem_1005Command extends CreateElementCommand {

        /**
         * @generated
         */
        public CreateListBusinessItem_1005Command(CreateElementRequest req) {
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
        protected EObject getElementToEdit() {
            EObject container = ((CreateElementRequest) getRequest()).getContainer();
            if (container instanceof View) {
                container = ((View) container).getElement();
            }
            return container;
        }
    }

    /**
     * @generated
     */
    private static class CreateDataBusinessItem_1006Command extends CreateElementCommand {

        /**
         * @generated
         */
        public CreateDataBusinessItem_1006Command(CreateElementRequest req) {
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
        protected EObject getElementToEdit() {
            EObject container = ((CreateElementRequest) getRequest()).getContainer();
            if (container instanceof View) {
                container = ((View) container).getElement();
            }
            return container;
        }
    }

    /**
     * @generated
     */
    private static class CreateInputBusinessItem_1007Command extends CreateElementCommand {

        /**
         * @generated
         */
        public CreateInputBusinessItem_1007Command(CreateElementRequest req) {
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
        protected EObject getElementToEdit() {
            EObject container = ((CreateElementRequest) getRequest()).getContainer();
            if (container instanceof View) {
                container = ((View) container).getElement();
            }
            return container;
        }
    }

    /**
     * @generated
     */
    private static class CreateDecisionBusinessItem_1008Command extends CreateElementCommand {

        /**
         * @generated
         */
        public CreateDecisionBusinessItem_1008Command(CreateElementRequest req) {
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
        protected EObject getElementToEdit() {
            EObject container = ((CreateElementRequest) getRequest()).getContainer();
            if (container instanceof View) {
                container = ((View) container).getElement();
            }
            return container;
        }
    }

    /**
     * @generated
     */
    private static class CreateActorBusinessItem_1009Command extends CreateElementCommand {

        /**
         * @generated
         */
        public CreateActorBusinessItem_1009Command(CreateElementRequest req) {
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
        protected EObject getElementToEdit() {
            EObject container = ((CreateElementRequest) getRequest()).getContainer();
            if (container instanceof View) {
                container = ((View) container).getElement();
            }
            return container;
        }
    }

    /**
     * @generated
     */
    private static class CreateEllipseBusinessItem_1010Command extends CreateElementCommand {

        /**
         * @generated
         */
        public CreateEllipseBusinessItem_1010Command(CreateElementRequest req) {
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
        protected EObject getElementToEdit() {
            EObject container = ((CreateElementRequest) getRequest()).getContainer();
            if (container instanceof View) {
                container = ((View) container).getElement();
            }
            return container;
        }
    }

    /**
     * @generated
     */
    private static class CreateGearBusinessItem_1011Command extends CreateElementCommand {

        /**
         * @generated
         */
        public CreateGearBusinessItem_1011Command(CreateElementRequest req) {
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
        protected EObject getElementToEdit() {
            EObject container = ((CreateElementRequest) getRequest()).getContainer();
            if (container instanceof View) {
                container = ((View) container).getElement();
            }
            return container;
        }
    }

    /**
     * @generated
     */
    protected Command getDuplicateCommand(DuplicateElementsRequest req) {
        TransactionalEditingDomain editingDomain = ((IGraphicalEditPart) getHost()).getEditingDomain();
        return getMSLWrapper(new DuplicateAnythingCommand(editingDomain, req));
    }

    /**
     * @generated
     */
    private static class DuplicateAnythingCommand extends DuplicateEObjectsCommand {

        /**
         * @generated
         */
        public DuplicateAnythingCommand(TransactionalEditingDomain editingDomain, DuplicateElementsRequest req) {
            super(editingDomain, req.getLabel(), req.getElementsToBeDuplicated(), req.getAllDuplicatedElementsMap());
        }
    }
}
