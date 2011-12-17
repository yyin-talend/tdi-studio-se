// ============================================================================
//
// Copyright (C) 2006-2011 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.designer.xmlmap.parts;

import java.util.List;

import org.eclipse.draw2d.ChopboxAnchor;
import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.ImageFigure;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.gef.ConnectionEditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.NodeEditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.editpolicies.NonResizableEditPolicy;
import org.eclipse.gef.requests.DirectEditRequest;
import org.talend.commons.ui.runtime.image.EImage;
import org.talend.commons.ui.runtime.image.ImageProvider;
import org.talend.designer.xmlmap.editor.XmlMapGraphicViewer;
import org.talend.designer.xmlmap.figures.VarNodeFigure;
import org.talend.designer.xmlmap.figures.VariableContainerFigure;
import org.talend.designer.xmlmap.figures.treeNode.TreeNodeFigure;
import org.talend.designer.xmlmap.model.emf.xmlmap.VarNode;
import org.talend.designer.xmlmap.model.emf.xmlmap.VarTable;
import org.talend.designer.xmlmap.model.emf.xmlmap.XmlmapPackage;
import org.talend.designer.xmlmap.parts.directedit.XmlMapNodeCellEditorLocator;
import org.talend.designer.xmlmap.parts.directedit.XmlMapNodeDirectEditManager;
import org.talend.designer.xmlmap.policy.DragAndDropEditPolicy;
import org.talend.designer.xmlmap.policy.XmlDirectEditPolicy;

/**
 * DOC hywang class global comment. Detailled comment
 */
public class VarNodeEditPart extends AbstractNodePart implements NodeEditPart {

    private VarNodeFigure varNodeFigure;

    private XmlMapNodeDirectEditManager directEditManager;

    @Override
    protected IFigure createFigure() {
        varNodeFigure = new VarNodeFigure((VarNode) getModel());
        return varNodeFigure;
    }

    @Override
    public void createEditPolicies() {
        installEditPolicy(EditPolicy.SELECTION_FEEDBACK_ROLE, new NonResizableEditPolicy());
        installEditPolicy("Drag and Drop", new DragAndDropEditPolicy());
        installEditPolicy(EditPolicy.DIRECT_EDIT_ROLE, new XmlDirectEditPolicy());
    }

    @Override
    public void performRequest(Request req) {
        IFigure figure = null;
        Figure selectedFigure = null;
        if (RequestConstants.REQ_DIRECT_EDIT.equals(req.getType())) {
            DirectEditRequest drequest = (DirectEditRequest) req;
            Point figureLocation = drequest.getLocation();
            if (getFigure() instanceof VarNodeFigure) {
                figure = ((VarNodeFigure) getFigure()).findFigureAt(figureLocation);
            }
            // if (directEditManager == null) {
            if (figure != null) {
                if (figure instanceof VariableContainerFigure) {
                    figure = ((VariableContainerFigure) figure).getVariableLabel();
                }
                selectedFigure = (Figure) figure;
                directEditManager = new XmlMapNodeDirectEditManager(this, new XmlMapNodeCellEditorLocator(selectedFigure));
            }
            // }
            directEditManager.show();
            ((XmlMapGraphicViewer) getViewer()).getMapperManager().setCurrentDirectEditManager(directEditManager);
        }
    }

    @Override
    protected List getModelSourceConnections() {
        // return ((VarNode) getModel()).getIncomingConnections();
        return ((VarNode) getModel()).getOutgoingConnections();
    }

    @Override
    protected List getModelTargetConnections() {
        return ((VarNode) getModel()).getIncomingConnections();
        // return ((VarNode) getModel()).getOutgoingConnections();
    }

    public ConnectionAnchor getSourceConnectionAnchor(ConnectionEditPart connection) {
        IFigure figure = null;
        // if (getRootAnchor() != null) {
        // figure = getRootAnchor();
        // } else {
        figure = getFigure();
        // }
        return new ColumnAnchor(this, figure, true);
    }

    public ConnectionAnchor getTargetConnectionAnchor(ConnectionEditPart connection) {
        IFigure figure = null;
        // if (getRootAnchor() != null) {
        // figure = getRootAnchor();
        // } else {
        figure = getFigure();
        // }
        return new ColumnAnchor(this, figure, false);
    }

    public ConnectionAnchor getSourceConnectionAnchor(Request request) {
        IFigure figure = null;
        figure = getFigure();
        // }
        return new ColumnAnchor(this, figure, true);
        // return new ChopboxAnchor(getFigure());
    }

    public ConnectionAnchor getTargetConnectionAnchor(Request request) {
        IFigure figure = null;
        figure = getFigure();
        return new ColumnAnchor(this, figure, false);
        // return new ChopboxAnchor(getFigure());
    }

    public void notifyChanged(Notification notification) {
        int type = notification.getEventType();
        int featureId = notification.getFeatureID(XmlmapPackage.class);
        switch (type) {
        case Notification.SET:
            switch (featureId) {
            case XmlmapPackage.VAR_NODE__EXPRESSION:
                ((VarNodeFigure) getFigure()).getExpression().setText(((VarNode) getModel()).getExpression());
                break;
            case XmlmapPackage.VAR_NODE__NULLABLE:
                boolean newBoolean = notification.getNewBooleanValue();
                ImageFigure checkImage = ((VarNodeFigure) getFigure()).getVariable().getCheckImage();
                if (newBoolean) {
                    checkImage.setImage(ImageProvider.getImage(EImage.CHECKED_ICON));
                } else if (!newBoolean) {
                    checkImage.setImage(ImageProvider.getImage(EImage.UNCHECKED_ICON));
                }
                break;
            case XmlmapPackage.VAR_NODE__NAME:
                ((VarNodeFigure) getFigure()).getVariable().getVariableLabel().setText(((VarNode) getModel()).getName());
                break;
            case XmlmapPackage.VAR_NODE__TYPE:
                ((VarNodeFigure) getFigure()).updateVarNodeType();
                // refreshChildren();
                // refreshVisuals();
                break;
            default:
                // refreshChildren();
                // refreshVisuals();
            }
        case Notification.ADD:
            switch (featureId) {
            case XmlmapPackage.VAR_NODE__INCOMING_CONNECTIONS:
                refreshTargetConnections();
                break;
            case XmlmapPackage.VAR_NODE__OUTGOING_CONNECTIONS:
                refreshSourceConnections();
                break;
            }
        case Notification.REMOVE:
            switch (featureId) {
            case XmlmapPackage.VAR_NODE__INCOMING_CONNECTIONS:
                refreshTargetConnections();
                break;
            case XmlmapPackage.VAR_NODE__OUTGOING_CONNECTIONS:
                refreshSourceConnections();
                break;
            }
        }

    }

    class ColumnAnchor extends ChopboxAnchor {

        private boolean isSource;

        private VarNodeEditPart varNodePart;

        public ColumnAnchor(VarNodeEditPart varNodePart, IFigure owner, boolean isSource) {
            super(owner);
            this.isSource = isSource;
            this.varNodePart = varNodePart;
        }

        public IFigure getOwner() {
            return super.getOwner();
        }

        public Point getReferencePoint() {
            Point ref = null;

            final VarTableEditPart varTablePart = (VarTableEditPart) varNodePart.getParent();

            if (((VarTable) varTablePart.getModel()).isMinimized()) {
                if (isSource) {
                    ref = varTablePart.getFigure().getBounds().getRight();
                } else {
                    ref = varTablePart.getFigure().getBounds().getLeft();
                }
            } else if (getOwner() instanceof TreeNodeFigure) {
                TreeNodeFigure nodeFigure = (TreeNodeFigure) getOwner();
                // normal column
                if (nodeFigure.getTreeBranch() == null) {
                    if (isSource) {
                        ref = getOwner().getBounds().getRight();
                    } else {
                        if (nodeFigure.getElement() != null) {
                            ref = nodeFigure.getElement().getBounds().getLeft();
                        } else {
                            ref = getOwner().getBounds().getLeft();
                        }
                    }
                    getOwner().translateToAbsolute(ref);

                }
            } else if (getOwner() instanceof VarNodeFigure) {
                VarNodeFigure varNodeFigure = (VarNodeFigure) getOwner();
                if (isSource) {
                    ref = getOwner().getBounds().getRight();
                } else {
                    if (varNodeFigure.getExpression() != null) {
                        ref = varNodeFigure.getExpression().getBounds().getLeft();
                    } else {
                        ref = getOwner().getBounds().getLeft();
                    }
                }
                getOwner().translateToAbsolute(ref);

            } else {
                ref = getOwner().getBounds().getCenter();
                getOwner().translateToAbsolute(ref);
            }
            return ref;

        }

        /*
         * (non-Javadoc)
         * 
         * @see org.eclipse.draw2d.ChopboxAnchor#getLocation(org.eclipse.draw2d.geometry.Point)
         */
        @Override
        public Point getLocation(Point reference) {
            Point referencePoint = getReferencePoint();
            return new Point(referencePoint.x, referencePoint.y);

        }

        @Override
        protected Rectangle getBox() {
            Rectangle copy = getOwner().getBounds().getCopy();

            return super.getBox();
        }
    }

}
