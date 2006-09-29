package org.talend.designer.business.model.business.diagram.edit.parts;

import org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.EditPolicyRoles;
import org.eclipse.gmf.runtime.notation.View;
import org.talend.designer.business.diagram.custom.edit.policies.BusinessItemDragDropEditPolicy;
import org.talend.designer.business.model.business.diagram.edit.policies.BusinessProcessCanonicalEditPolicy;
import org.talend.designer.business.model.business.diagram.edit.policies.BusinessProcessItemSemanticEditPolicy;

/**
 * @generated
 */
public class BusinessProcessEditPart extends DiagramEditPart {

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
}
