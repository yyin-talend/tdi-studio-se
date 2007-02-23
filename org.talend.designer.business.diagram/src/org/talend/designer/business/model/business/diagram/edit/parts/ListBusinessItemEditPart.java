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
import org.talend.designer.business.model.business.diagram.edit.policies.ListBusinessItemCanonicalEditPolicy;
import org.talend.designer.business.model.business.diagram.edit.policies.ListBusinessItemGraphicalNodeEditPolicy;
import org.talend.designer.business.model.business.diagram.edit.policies.ListBusinessItemItemSemanticEditPolicy;
import org.talend.designer.business.model.business.diagram.part.BusinessVisualIDRegistry;

public class ListBusinessItemEditPart extends BusinessItemShapeEditPart {

    /**
     * @generated
     */
    public static final int VISUAL_ID = 1005;

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
    public ListBusinessItemEditPart(View view) {
        super(view);
    }

    /**
     * @generated
     */
    protected void createDefaultEditPolicies() {
        super.createDefaultEditPolicies();
        installEditPolicy(EditPolicyRoles.SEMANTIC_ROLE, new ListBusinessItemItemSemanticEditPolicy());
        installEditPolicy(EditPolicy.GRAPHICAL_NODE_ROLE, new ListBusinessItemGraphicalNodeEditPolicy());
        installEditPolicy(EditPolicyRoles.CANONICAL_ROLE, new ListBusinessItemCanonicalEditPolicy());
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
        ListBusinessItemFigure figure = new ListBusinessItemFigure();
        return primaryShape = figure;
    }

    /**
     * @generated
     */
    public ListBusinessItemFigure getPrimaryShape() {
        return (ListBusinessItemFigure) primaryShape;
    }

    /**
     * @generated
     */
    protected boolean addFixedChild(EditPart childEditPart) {
        if (childEditPart instanceof ListBusinessItemNameEditPart) {
            ((ListBusinessItemNameEditPart) childEditPart).setLabel(getPrimaryShape()
                    .getFigureListBusinessItemNameFigure());
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
        return getChildBySemanticHint(BusinessVisualIDRegistry.getType(ListBusinessItemNameEditPart.VISUAL_ID));
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
    public class ListBusinessItemFigure extends
            org.talend.designer.business.diagram.custom.figures.ListBusinessItemShapeFigure {

        /**
         * @generated
         */
        public ListBusinessItemFigure() {

            org.eclipse.draw2d.StackLayout myGenLayoutManager = new org.eclipse.draw2d.StackLayout();

            this.setLayoutManager(myGenLayoutManager);

            createContents();
        }

        /**
         * @generated
         */
        private void createContents() {
            org.talend.designer.business.diagram.custom.figures.BusinessItemNameFigure fig_0 = new org.talend.designer.business.diagram.custom.figures.BusinessItemNameFigure();

            setFigureListBusinessItemNameFigure(fig_0);

            Object layData0 = null;

            this.add(fig_0, layData0);
        }

        /**
         * @generated
         */
        private org.talend.designer.business.diagram.custom.figures.BusinessItemNameFigure fListBusinessItemNameFigure;

        /**
         * @generated
         */
        public org.talend.designer.business.diagram.custom.figures.BusinessItemNameFigure getFigureListBusinessItemNameFigure() {
            return fListBusinessItemNameFigure;
        }

        /**
         * @generated
         */
        private void setFigureListBusinessItemNameFigure(
                org.talend.designer.business.diagram.custom.figures.BusinessItemNameFigure fig) {
            fListBusinessItemNameFigure = fig;
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
