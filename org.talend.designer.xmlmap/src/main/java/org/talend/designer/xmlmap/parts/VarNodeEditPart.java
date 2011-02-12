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
import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.talend.commons.ui.runtime.image.EImage;
import org.talend.commons.ui.runtime.image.ImageProvider;
import org.talend.core.model.metadata.types.JavaTypesManager;
import org.talend.designer.xmlmap.figures.TreeNodeFigure;
import org.talend.designer.xmlmap.figures.VarNodeFigure;
import org.talend.designer.xmlmap.figures.VariableContainerFigure;
import org.talend.designer.xmlmap.figures.XmlTreeBranch;
import org.talend.designer.xmlmap.model.emf.xmlmap.VarNode;
import org.talend.designer.xmlmap.model.emf.xmlmap.XmlmapPackage;
import org.talend.designer.xmlmap.parts.directedit.XmlMapNodeCellEditorLocator;
import org.talend.designer.xmlmap.parts.directedit.XmlMapNodeDirectEditManager;
import org.talend.designer.xmlmap.policy.DragAndDropEditPolicy;
import org.talend.designer.xmlmap.policy.XmlDirectEditPolicy;

/**
 * DOC hywang class global comment. Detailled comment
 */
public class VarNodeEditPart extends BaseEditPart implements NodeEditPart {

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
                String[] items = JavaTypesManager.getJavaTypesLabels();
                directEditManager = new XmlMapNodeDirectEditManager(this, ComboBoxCellEditor.class,
                        new XmlMapNodeCellEditorLocator(selectedFigure), items);
            }
            // }
            directEditManager.show();
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
        return new ColumnAnchor(figure, true);
    }

    public ConnectionAnchor getTargetConnectionAnchor(ConnectionEditPart connection) {
        IFigure figure = null;
        // if (getRootAnchor() != null) {
        // figure = getRootAnchor();
        // } else {
        figure = getFigure();
        // }
        return new ColumnAnchor(figure, false);
    }

    public ConnectionAnchor getSourceConnectionAnchor(Request request) {
        IFigure figure = null;
        figure = getFigure();
        // }
        return new ColumnAnchor(figure, true);
        // return new ChopboxAnchor(getFigure());
    }

    public ConnectionAnchor getTargetConnectionAnchor(Request request) {
        IFigure figure = null;
        figure = getFigure();
        return new ColumnAnchor(figure, false);
        // return new ChopboxAnchor(getFigure());
    }

    public void notifyChanged(Notification notification) {
        int type = notification.getEventType();
        int featureId = notification.getFeatureID(XmlmapPackage.class);
        switch (type) {
        case Notification.SET:
            switch (featureId) {
            case XmlmapPackage.VAR_NODE__EXPRESSION:
                varNodeFigure.getExpression().setText(((VarNode) getModel()).getExpression());
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
                String newString = notification.getNewStringValue();
                ((VarNodeFigure) getFigure()).getVariable().getVariableLabel().setText(newString);
                break;
            case XmlmapPackage.VAR_NODE__TYPE:
                varNodeFigure.getType().setText(((VarNode) getModel()).getType());
                refreshChildren();
                refreshVisuals();
                break;
            default:
                refreshChildren();
                refreshVisuals();
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

        public ColumnAnchor(IFigure owner, boolean isSource) {
            super(owner);
            this.isSource = isSource;
        }

        public IFigure getOwner() {
            // IFigure figure = getOwner();
            // if (figure instanceof TreeNodeFigure) {
            // if (((TreeNodeFigure) figure).getTreeBranch() != null) {
            // return ((TreeNodeFigure) figure).getTreeBranch();
            // } else {
            // return figure;
            // }
            // }

            return super.getOwner();
        }

        public Point getReferencePoint() {
            Point ref = null;
            if (getOwner() == null) {
                return null;
            } else if (getOwner() instanceof TreeNodeFigure) {
                TreeNodeFigure nodeFigure = (TreeNodeFigure) getOwner();
                // normal column
                if (nodeFigure.getTreeBranch() == null) {
                    if (isSource) {
                        ref = getOwner().getBounds().getRight();
                    } else {
                        if (nodeFigure.getColumnExpressionFigure() != null) {
                            ref = nodeFigure.getColumnExpressionFigure().getBounds().getLeft();
                        } else {
                            ref = getOwner().getBounds().getLeft();
                        }
                    }
                    getOwner().translateToAbsolute(ref);

                } else {
                    // tree root
                    // XmlTreeBranch treeBranch = nodeFigure.getTreeBranch();
                    // Rectangle elembounds = treeBranch.getElement().getBounds().getCopy();
                    // Rectangle bounds = treeBranch.getRoot().getBounds().getCopy();
                    // elembounds.x = bounds.x;
                    // elembounds.width = bounds.width;
                    // if (isSource) {
                    // ref = elembounds.getRight();
                    // } else {
                    // ref = elembounds.getLeft();
                    // }
                    // getOwner().translateToAbsolute(ref);

                }
            } else if (getOwner() instanceof XmlTreeBranch) {
                XmlTreeBranch treeBranch = (XmlTreeBranch) getOwner();
                Rectangle elembounds = treeBranch.getElement().getBounds().getCopy();
                Rectangle bounds = treeBranch.getRoot().getBounds().getCopy();
                elembounds.x = bounds.x;
                elembounds.width = bounds.width;
                if (isSource) {
                    ref = elembounds.getRight();
                } else {
                    if (treeBranch.getExpressionFigure() != null) {
                        ref = treeBranch.getExpressionFigure().getBounds().getLeft();
                    } else {
                        ref = elembounds.getLeft();
                    }

                    // OutputTreeNodeFigure findRootOutputTreeNodeFigure = findRootOutputTreeNodeFigure(treeBranch);
                    // if (findRootOutputTreeNodeFigure != null
                    // && findRootOutputTreeNodeFigure.getTreeNodeExpressionFigure() != null) {
                    // Figure expressionFigure = findRootOutputTreeNodeFigure.getTreeNodeExpressionFigure();
                    // List children = expressionFigure.getChildren();
                    // if (children != null) {
                    // for (int i = 0; i < children.size(); i++) {
                    // if (children.get(i) instanceof Label) {
                    // Label label = (Label) children.get(i);
                    // if (((TreeNode) getModel()).getExpression() != null
                    // && ((TreeNode) getModel()).getExpression().equals(label.getText())) {
                    // ref = label.getBounds().getLeft();
                    // break;
                    // }
                    // }
                    // }
                    // }
                    //
                    // }
                }
                getOwner().translateToAbsolute(ref);
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
