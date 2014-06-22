package org.talend.designer.business.model.business.diagram.edit.parts;

import org.eclipse.draw2d.Connection;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.EditPolicyRoles;
import org.eclipse.gmf.runtime.notation.View;
import org.talend.designer.business.diagram.custom.edit.parts.BaseBusinessItemRelationShipEditPart;
import org.talend.designer.business.model.business.diagram.edit.policies.DirectionalBusinessItemRelationshipItemSemanticEditPolicy;

/**
 * @generated NOT
 */
public class DirectionalBusinessItemRelationshipEditPart extends BaseBusinessItemRelationShipEditPart {

    /**
     * @generated
     */
    public static final int VISUAL_ID = 3002;

    /**
     * @generated
     */
    public DirectionalBusinessItemRelationshipEditPart(View view) {
        super(view);
    }

    /**
     * @generated
     */
    protected void createDefaultEditPolicies() {
        super.createDefaultEditPolicies();
        installEditPolicy(EditPolicyRoles.SEMANTIC_ROLE,
                new DirectionalBusinessItemRelationshipItemSemanticEditPolicy());
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
        return new DirectionalBusinessItemRelationshipFigure();
    }

    /**
     * @generated
     */
    public class DirectionalBusinessItemRelationshipFigure extends
            org.eclipse.gmf.runtime.draw2d.ui.figures.PolylineConnectionEx {

        /**
         * @generated
         */
        public DirectionalBusinessItemRelationshipFigure() {

            setTargetDecoration(createTargetDecoration());
        }

        /**
         * @generated
         */
        private org.eclipse.draw2d.PolylineDecoration createTargetDecoration() {
            org.eclipse.draw2d.PolylineDecoration df = new org.eclipse.draw2d.PolylineDecoration();
            // dispatchNext?

            org.eclipse.draw2d.geometry.PointList pl = new org.eclipse.draw2d.geometry.PointList();
            pl.addPoint(-1, 1);
            pl.addPoint(0, 0);
            pl.addPoint(-1, -1);
            df.setTemplate(pl);
            df.setScale(getMapMode().DPtoLP(7), getMapMode().DPtoLP(3));

            return df;
        }

    }

}
