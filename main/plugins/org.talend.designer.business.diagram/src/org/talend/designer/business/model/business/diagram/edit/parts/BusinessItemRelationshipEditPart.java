package org.talend.designer.business.model.business.diagram.edit.parts;

import org.eclipse.draw2d.Connection;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.EditPolicyRoles;
import org.eclipse.gmf.runtime.notation.View;
import org.talend.designer.business.diagram.custom.edit.parts.BaseBusinessItemRelationShipEditPart;
import org.talend.designer.business.model.business.diagram.edit.policies.BusinessItemRelationshipItemSemanticEditPolicy;

/**
 * @generated NOT
 */
public class BusinessItemRelationshipEditPart extends BaseBusinessItemRelationShipEditPart {

    /**
     * @generated
     */
    public static final int VISUAL_ID = 3001;

    /**
     * @generated
     */
    public BusinessItemRelationshipEditPart(View view) {
        super(view);
    }

    /**
     * @generated
     */
    protected void createDefaultEditPolicies() {
        super.createDefaultEditPolicies();
        installEditPolicy(EditPolicyRoles.SEMANTIC_ROLE, new BusinessItemRelationshipItemSemanticEditPolicy());
    }

    /**
     * Creates figure for this edit part.
     * 
     * Body of this method does not depend on settings in generation model so you may safely remove <i>generated</i>
     * tag and modify it.
     * 
     * @generated
     */
    protected Connection createConnectionFigure() {
        return new BusinessItemRelationshipFigure();
    }

    /**
     * @generated
     */
    public class BusinessItemRelationshipFigure extends org.eclipse.gmf.runtime.draw2d.ui.figures.PolylineConnectionEx {

        /**
         * @generated
         */
        public BusinessItemRelationshipFigure() {

        }

    }

}
