package org.talend.designer.business.model.business.diagram.edit.parts;

import org.eclipse.draw2d.Connection;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.EditPolicyRoles;
import org.eclipse.gmf.runtime.notation.View;
import org.talend.designer.business.diagram.custom.edit.parts.BaseBusinessItemRelationShipEditPart;
import org.talend.designer.business.model.business.diagram.edit.policies.BidirectionalBusinessItemRelationshipItemSemanticEditPolicy;

/**
 * @generated NOT
 */
public class BidirectionalBusinessItemRelationshipEditPart extends BaseBusinessItemRelationShipEditPart {

    /**
     * @generated
     */
    public static final int VISUAL_ID = 3003;

    /**
     * @generated
     */
    public BidirectionalBusinessItemRelationshipEditPart(View view) {
        super(view);
    }

    /**
     * @generated
     */
    protected void createDefaultEditPolicies() {
        super.createDefaultEditPolicies();
        installEditPolicy(EditPolicyRoles.SEMANTIC_ROLE,
                new BidirectionalBusinessItemRelationshipItemSemanticEditPolicy());
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
        return new BidirectionalBusinessItemRelationshipFigure();
    }

    /**
     * @generated
     */
    public class BidirectionalBusinessItemRelationshipFigure extends
            org.eclipse.gmf.runtime.draw2d.ui.figures.PolylineConnectionEx {

        /**
         * @generated
         */
        public BidirectionalBusinessItemRelationshipFigure() {

            setSourceDecoration(createSourceDecoration());
            setTargetDecoration(createTargetDecoration());
        }

        /**
         * @generated
         */
        private org.eclipse.draw2d.PolylineDecoration createSourceDecoration() {
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
