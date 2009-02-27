package org.talend.designer.business.model.business.diagram.edit.parts;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.EditPolicyRoles;
import org.eclipse.gmf.runtime.notation.NotationPackage;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.gmf.runtime.notation.impl.NodeImpl;
import org.talend.designer.business.diagram.custom.edit.policies.BusinessItemDragDropEditPolicy;
import org.talend.designer.business.model.business.diagram.edit.policies.BusinessProcessCanonicalEditPolicy;
import org.talend.designer.business.model.business.diagram.edit.policies.BusinessProcessItemSemanticEditPolicy;

/**
 * @generated
 */
public class BusinessProcessEditPart extends DiagramEditPart {

    private static Object lastAddedItem;

    /**
     * @generated
     */
    public static String MODEL_ID = "Business"; //$NON-NLS-1$

    /**
     * @generated
     */
    public static final int VISUAL_ID = 79;

    /**
     * @generated
     */
    public BusinessProcessEditPart(View view) {
        super(view);
    }

    protected void createDefaultEditPolicies() {
        super.createDefaultEditPolicies();
        installEditPolicy(EditPolicyRoles.SEMANTIC_ROLE, new BusinessProcessItemSemanticEditPolicy());
        installEditPolicy(EditPolicyRoles.CANONICAL_ROLE, new BusinessProcessCanonicalEditPolicy());
        installEditPolicy(EditPolicyRoles.DRAG_DROP_ROLE, new BusinessItemDragDropEditPolicy());
    }

    @Override
    protected void handleNotificationEvent(Notification event) {
        if (NotationPackage.Literals.VIEW__PERSISTED_CHILDREN.equals(event.getFeature())
                || NotationPackage.Literals.VIEW__TRANSIENT_CHILDREN.equals(event.getFeature())) {
            if (event.getNewValue() instanceof NodeImpl) {
                setLastAddedItem(((NodeImpl) event.getNewValue()));
            }
        }

        super.handleNotificationEvent(event);
    }

    public static Object getLastAddedItem() {
        return lastAddedItem;
    }

    public static void setLastAddedItem(Object lastAddedItem) {
        BusinessProcessEditPart.lastAddedItem = lastAddedItem;
    }
}
