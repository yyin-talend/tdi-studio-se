package org.talend.designer.business.model.business.diagram.edit.parts;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.StackLayout;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.LayoutEditPolicy;
import org.eclipse.gef.editpolicies.NonResizableEditPolicy;
import org.eclipse.gef.requests.CreateRequest;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.EditPolicyRoles;
import org.eclipse.gmf.runtime.draw2d.ui.figures.ConstrainedToolbarLayout;
import org.eclipse.gmf.runtime.gef.ui.figures.DefaultSizeNodeFigure;
import org.eclipse.gmf.runtime.gef.ui.figures.NodeFigure;
import org.eclipse.gmf.runtime.notation.View;
import org.talend.designer.business.diagram.custom.edit.parts.BusinessItemShapeEditPart;
import org.talend.designer.business.diagram.custom.figures.BusinessItemNameFigure;
import org.talend.designer.business.model.business.diagram.edit.policies.InputBusinessItemCanonicalEditPolicy;
import org.talend.designer.business.model.business.diagram.edit.policies.InputBusinessItemGraphicalNodeEditPolicy;
import org.talend.designer.business.model.business.diagram.edit.policies.InputBusinessItemItemSemanticEditPolicy;
import org.talend.designer.business.model.business.diagram.part.BusinessVisualIDRegistry;

public class InputBusinessItemEditPart extends BusinessItemShapeEditPart {

    /**
     * @generated
     */
    public static final int VISUAL_ID = 1007;

    /**
     * @generated
     */
    protected IFigure contentPane;

    /**
     * @generated
     */
    protected IFigure primaryShape;

    /**
     * @generated
     */
    public InputBusinessItemEditPart(View view) {
        super(view);
    }

    /**
     * @generated
     */
    protected void createDefaultEditPolicies() {
        super.createDefaultEditPolicies();
        installEditPolicy(EditPolicyRoles.SEMANTIC_ROLE, new InputBusinessItemItemSemanticEditPolicy());
        installEditPolicy(EditPolicy.GRAPHICAL_NODE_ROLE, new InputBusinessItemGraphicalNodeEditPolicy());
        installEditPolicy(EditPolicyRoles.CANONICAL_ROLE, new InputBusinessItemCanonicalEditPolicy());
        installEditPolicy(EditPolicy.LAYOUT_ROLE, createLayoutEditPolicy());
    }

    /**
     * @generated
     */
    protected LayoutEditPolicy createLayoutEditPolicy() {
        LayoutEditPolicy lep = new LayoutEditPolicy() {

            protected EditPolicy createChildEditPolicy(EditPart child) {
                EditPolicy result = child.getEditPolicy(EditPolicy.PRIMARY_DRAG_ROLE);
                if (result == null) {
                    result = new NonResizableEditPolicy();
                }
                return result;
            }

            protected Command getMoveChildrenCommand(Request request) {
                return null;
            }

            protected Command getCreateCommand(CreateRequest request) {
                return null;
            }
        };
        return lep;
    }

    /**
     * @generated
     */
    protected IFigure createNodeShape() {
        InputBusinessItemFigure figure = new InputBusinessItemFigure();
        return primaryShape = figure;
    }

    /**
     * @generated
     */
    public InputBusinessItemFigure getPrimaryShape() {
        return (InputBusinessItemFigure) primaryShape;
    }

    /**
     * @generated
     */
    protected boolean addFixedChild(EditPart childEditPart) {
        if (childEditPart instanceof InputBusinessItemNameEditPart) {
            ((InputBusinessItemNameEditPart) childEditPart).setLabel(getPrimaryShape().getFigureInputBusinessItemNameFigure());
            return true;
        }
        return false;
    }

    /**
     * @generated
     */
    protected boolean removeFixedChild(EditPart childEditPart) {
        return false;
    }

    /**
     * @generated
     */
    protected NodeFigure createNodePlate() {
        return new DefaultSizeNodeFigure(getMapMode().DPtoLP(40), getMapMode().DPtoLP(40));
    }

    /**
     * Creates figure for this edit part.
     * 
     * Body of this method does not depend on settings in generation model so you may safely remove <i>generated</i>
     * tag and modify it.
     * 
     * @generated
     */
    protected NodeFigure createNodeFigure() {
        NodeFigure figure = createNodePlate();
        figure.setLayoutManager(new StackLayout());
        IFigure shape = createNodeShape();
        figure.add(shape);
        contentPane = setupContentPane(shape);
        return figure;
    }

    /**
     * Default implementation treats passed figure as content pane. Respects layout one may have set for generated
     * figure.
     * 
     * @param nodeShape instance of generated figure class
     * @generated
     */
    protected IFigure setupContentPane(IFigure nodeShape) {
        if (nodeShape.getLayoutManager() == null) {
            ConstrainedToolbarLayout layout = new ConstrainedToolbarLayout();
            layout.setSpacing(getMapMode().DPtoLP(5));
            nodeShape.setLayoutManager(layout);
        }
        return nodeShape; // use nodeShape itself as contentPane
    }

    /**
     * @generated
     */
    public IFigure getContentPane() {
        if (contentPane != null) {
            return contentPane;
        }
        return super.getContentPane();
    }

    /**
     * @generated
     */
    public EditPart getPrimaryChildEditPart() {
        return getChildBySemanticHint(BusinessVisualIDRegistry.getType(InputBusinessItemNameEditPart.VISUAL_ID));
    }

    /**
     * @generated
     */
    protected void addChildVisual(EditPart childEditPart, int index) {
        if (addFixedChild(childEditPart)) {
            return;
        }
        super.addChildVisual(childEditPart, -1);
    }

    /**
     * @generated
     */
    protected void removeChildVisual(EditPart childEditPart) {
        if (removeFixedChild(childEditPart)) {
            return;
        }
        super.removeChildVisual(childEditPart);
    }

    /**
     * @generated
     */
    public class InputBusinessItemFigure extends org.talend.designer.business.diagram.custom.figures.InputBusinessItemShapeFigure {

        /**
         * @generated
         */
        public InputBusinessItemFigure() {

            org.eclipse.draw2d.StackLayout myGenLayoutManager = new org.eclipse.draw2d.StackLayout();

            this.setLayoutManager(myGenLayoutManager);

            createContents();
        }

        private void createContents() {
            BusinessItemNameFigure fig_0 = new BusinessItemNameFigure();

            setFigureInputBusinessItemNameFigure(fig_0);

            this.add(fig_0, null);
        }

        /**
         * @generated
         */
        private org.talend.designer.business.diagram.custom.figures.BusinessItemNameFigure fInputBusinessItemNameFigure;

        /**
         * @generated
         */
        public org.talend.designer.business.diagram.custom.figures.BusinessItemNameFigure getFigureInputBusinessItemNameFigure() {
            return fInputBusinessItemNameFigure;
        }

        /**
         * @generated
         */
        private void setFigureInputBusinessItemNameFigure(
                org.talend.designer.business.diagram.custom.figures.BusinessItemNameFigure fig) {
            fInputBusinessItemNameFigure = fig;
        }

        /**
         * @generated
         */
        private boolean myUseLocalCoordinates = false;

        /**
         * @generated
         */
        protected boolean useLocalCoordinates() {
            return myUseLocalCoordinates;
        }

        /**
         * @generated
         */
        protected void setUseLocalCoordinates(boolean useLocalCoordinates) {
            myUseLocalCoordinates = useLocalCoordinates;
        }

    }

}
