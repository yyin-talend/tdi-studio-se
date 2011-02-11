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
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.draw2d.ToolbarLayout;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.gef.ConnectionEditPart;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.NodeEditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.editparts.AbstractConnectionEditPart;
import org.eclipse.gef.editpolicies.NonResizableEditPolicy;
import org.eclipse.swt.SWT;
import org.talend.commons.ui.swt.geftree.figure.TreeBranch;
import org.talend.designer.xmlmap.figures.OutputTreeNodeFigure;
import org.talend.designer.xmlmap.figures.TreeBranchFigure;
import org.talend.designer.xmlmap.figures.TreeNodeFigure;
import org.talend.designer.xmlmap.figures.XmlTreeBranch;
import org.talend.designer.xmlmap.model.emf.xmlmap.InputXmlTree;
import org.talend.designer.xmlmap.model.emf.xmlmap.NodeType;
import org.talend.designer.xmlmap.model.emf.xmlmap.OutputXmlTree;
import org.talend.designer.xmlmap.model.emf.xmlmap.TreeNode;
import org.talend.designer.xmlmap.model.emf.xmlmap.XmlmapPackage;
import org.talend.designer.xmlmap.policy.DragAndDropEditPolicy;
import org.talend.designer.xmlmap.policy.TreeExpandSupportEditPolicy;
import org.talend.designer.xmlmap.util.XmlMapUtil;

/**
 * wchen class global comment. Detailled comment
 */
public class TreeNodeEditPart extends BaseEditPart implements NodeEditPart {

    // for expand and collapse
    protected IFigure rootAnchor;

    protected XmlTreeBranch treeBranchFigure;

    @Override
    protected IFigure createFigure() {
        TreeNode model = (TreeNode) getModel();
        IFigure figure = null;
        // nodes in xml tree
        if (XmlMapUtil.getXPathLength(model.getXpath()) > 2) {
            String status = model.isLoop() ? String.valueOf(model.isLoop()) : "";
            figure = new XmlTreeBranch(new TreeBranchFigure(model), XmlTreeBranch.STYLE_ROW_HANGING);
            // figure = new TreeBranch(new TreeBranchFigure(getTreeBranchName(model), "true"),
            // TreeBranch.STYLE_HANGING);
            treeBranchFigure = (XmlTreeBranch) figure;

            // for test
            // if (model.getChildren().size() == 1) {
            // figure.setOpaque(true);
            // figure.setBackgroundColor(ColorConstants.green);
            // }

        }
        // normal column and tree root
        else {
            figure = new TreeNodeFigure(this);
            // figure.setLayoutManager(new EqualWidthLayout());
            figure.setLayoutManager(new ToolbarLayout());
        }
        return figure;
    }

    protected String getTreeBranchName(TreeNode model) {
        String name = "";
        if (NodeType.ATTRIBUT.equals(model.getNodeType()) || NodeType.NAME_SPACE.equals(model.getNodeType())) {
            name = model.getXpath().substring(model.getXpath().lastIndexOf(XmlMapUtil.XPATH_SEPARATOR) + 1,
                    model.getXpath().length());
        } else {
            name = model.getName();
        }
        return name;
    }

    @Override
    protected void createEditPolicies() {
        installEditPolicy(EditPolicy.SELECTION_FEEDBACK_ROLE, new NonResizableEditPolicy());
        installEditPolicy("Drag and Drop", new DragAndDropEditPolicy());
        installEditPolicy(TreeExpandSupportEditPolicy.EXPAND_SUPPORT, new TreeExpandSupportEditPolicy());
        // to deleteNode
        // installEditPolicy(EditPolicy.COMPONENT_ROLE, new CustomComponentEditPolicy());
    }

    @Override
    public List getModelChildren() {
        TreeNode model = (TreeNode) getModel();
        return model.getChildren();
    }

    @Override
    protected List getModelSourceConnections() {
        TreeNode model = (TreeNode) getModel();
        return model.getOutgoingConnections();
    }

    public void expand(boolean expand) {
        if (getFigure() instanceof XmlTreeBranch) {
            ((XmlTreeBranch) this.getFigure()).doExpandCollapse((XmlTreeBranch) (getFigure()));
        }
    }

    // @Override
    // public List getSourceConnections() {
    // TreeNode model = (TreeNode) getModel();
    // return model.getOutgoingConnections();
    // }

    @Override
    public IFigure getContentPane() {
        if (getFigure() instanceof TreeNodeFigure) {
            return ((TreeNodeFigure) getFigure()).getContentPane();
        } else if (getFigure() instanceof XmlTreeBranch) {
            return ((XmlTreeBranch) getFigure()).getContentsPane();
        }
        return super.getContentPane();
    }

    public ConnectionAnchor getSourceConnectionAnchor(ConnectionEditPart connection) {
        IFigure figure = null;
        if (getRootAnchor() != null) {
            figure = getRootAnchor();
        } else {
            figure = getFigure();
        }
        return new ColumnAnchor(figure, true);
        // return new ChopboxAnchor(getFigure());
    }

    public ConnectionAnchor getTargetConnectionAnchor(ConnectionEditPart connection) {
        IFigure figure = null;
        if (getRootAnchor() != null) {
            figure = getRootAnchor();
        } else {
            figure = getFigure();
        }
        return new ColumnAnchor(figure, false);
        // return new ChopboxAnchor(getFigure());
    }

    public ConnectionAnchor getSourceConnectionAnchor(Request request) {
        IFigure figure = null;
        if (getRootAnchor() != null) {
            figure = getRootAnchor();
        } else {
            figure = getFigure();
        }
        return new ColumnAnchor(figure, true);
        // return new ChopboxAnchor(getFigure());
    }

    public ConnectionAnchor getTargetConnectionAnchor(Request request) {
        IFigure figure = null;
        if (getRootAnchor() != null) {
            figure = getRootAnchor();
        } else {
            figure = getFigure();
        }
        return new ColumnAnchor(figure, false);
        // return new ChopboxAnchor(getFigure());
    }

    public void notifyChanged(Notification notification) {
        int type = notification.getEventType();
        int featureId = notification.getFeatureID(XmlmapPackage.class);
        switch (type) {
        case Notification.SET:
            switch (featureId) {
            case XmlmapPackage.TREE_NODE__LOOP:
            case XmlmapPackage.OUTPUT_TREE_NODE__GROUP:
                if (getFigure() instanceof TreeBranch) {
                    if (((XmlTreeBranch) getFigure()).getElement() instanceof TreeBranchFigure) {
                        TreeBranchFigure branchFigure = (TreeBranchFigure) ((XmlTreeBranch) getFigure()).getElement();
                        branchFigure.updateStatus();
                    }
                }
                break;
            case XmlmapPackage.TREE_NODE__NAME:
                if (getFigure() instanceof TreeBranch) {
                    if (((XmlTreeBranch) getFigure()).getElement() instanceof TreeBranchFigure) {
                        TreeBranchFigure branchFigure = (TreeBranchFigure) ((XmlTreeBranch) getFigure()).getElement();
                        branchFigure.updataNameFigure();
                    }
                } else if (getFigure() instanceof TreeNodeFigure) {
                    ((TreeNodeFigure) getFigure()).updateNameFigure();
                }
                break;
            case XmlmapPackage.TREE_NODE__TYPE:
                TreeNode treeNode = (TreeNode) getModel();
                if (treeNode.eContainer() instanceof InputXmlTree || treeNode.eContainer() instanceof OutputXmlTree) {
                    if (figure instanceof TreeNodeFigure) {
                        ((TreeNodeFigure) figure).refreshChildren();
                    }
                }
            }
        case Notification.ADD:
        case Notification.ADD_MANY:
            switch (featureId) {
            case XmlmapPackage.TREE_NODE__CHILDREN:
                refreshChildren();
                break;
            case XmlmapPackage.TREE_NODE__OUTGOING_CONNECTIONS:
                refreshSourceConnections();
                break;
            case XmlmapPackage.OUTPUT_TREE_NODE__INCOMING_CONNECTIONS:
                refreshTargetConnections();
                break;
            }
        case Notification.REMOVE:
        case Notification.REMOVE_MANY:
            switch (featureId) {
            case XmlmapPackage.TREE_NODE__CHILDREN:
                refreshChildren();
            case XmlmapPackage.TREE_NODE__OUTGOING_CONNECTIONS:
                refreshSourceConnections();
                break;
            case XmlmapPackage.OUTPUT_TREE_NODE__INCOMING_CONNECTIONS:
                refreshTargetConnections();

            }

        }

    }

    @Override
    protected void addChildVisual(EditPart childEditPart, int index) {
        super.addChildVisual(childEditPart, index);
    }

    public void refreshChildrenSourceConnections(TreeNodeEditPart rootPart, boolean expanded) {
        for (Object obj : getChildren()) {
            if (obj instanceof TreeNodeEditPart) {
                TreeNodeEditPart part = (TreeNodeEditPart) obj;
                if (expanded) {
                    // do collapse
                    part.setRootAnchor(rootPart.getFigure());
                } else {
                    part.setRootAnchor(null);
                }
                if (part.getSourceConnections() != null) {
                    for (Object conn : part.getSourceConnections()) {
                        if (conn instanceof AbstractConnectionEditPart) {
                            AbstractConnectionEditPart connectionEditPart = (AbstractConnectionEditPart) conn;
                            if (connectionEditPart.getFigure() instanceof PolylineConnection) {
                                PolylineConnection connFigure = (PolylineConnection) connectionEditPart.getFigure();
                                if (expanded) {
                                    connFigure.setLineStyle(SWT.LINE_DASHDOTDOT);
                                } else {
                                    connFigure.setLineStyle(SWT.LINE_SOLID);
                                }
                            }
                            connectionEditPart.refresh();
                        }
                    }
                }
                part.refreshChildrenSourceConnections(rootPart, expanded);
            }
        }

    }

    public void refreshChildrenTargetConnections(TreeNodeEditPart rootPart, boolean expanded) {
        for (Object obj : getChildren()) {
            if (obj instanceof TreeNodeEditPart) {
                TreeNodeEditPart part = (TreeNodeEditPart) obj;
                if (expanded) {
                    // do collapse
                    part.setRootAnchor(rootPart.getFigure());
                } else {
                    part.setRootAnchor(null);
                }
                if (part.getTargetConnections() != null) {
                    for (Object conn : part.getTargetConnections()) {
                        if (conn instanceof AbstractConnectionEditPart) {
                            AbstractConnectionEditPart connectionEditPart = (AbstractConnectionEditPart) conn;
                            if (connectionEditPart.getFigure() instanceof PolylineConnection) {
                                PolylineConnection connFigure = (PolylineConnection) connectionEditPart.getFigure();
                                if (part.getTreeBranchFigure() != null) {
                                    if (expanded) {
                                        connFigure.setLineStyle(SWT.LINE_DASHDOTDOT);
                                    } else {
                                        connFigure.setLineStyle(SWT.LINE_SOLID);
                                    }
                                }
                            }
                            connectionEditPart.refresh();
                        }
                    }
                }
                part.refreshChildrenTargetConnections(rootPart, expanded);
            }
        }
    }

    protected XmlTreeBranch getTreeBranchFigure() {
        return this.treeBranchFigure;

    }

    protected IFigure getRootAnchor() {
        return rootAnchor;
    }

    protected void setRootAnchor(IFigure rootAnchor) {
        this.rootAnchor = rootAnchor;
    }

    public OutputTreeNodeFigure findRootOutputTreeNodeFigure(IFigure parentFigure) {
        OutputTreeNodeFigure rootOutputTreeNodeFigure = null;
        if (parentFigure instanceof OutputTreeNodeFigure) {
            rootOutputTreeNodeFigure = (OutputTreeNodeFigure) parentFigure;
        } else
            rootOutputTreeNodeFigure = findRootOutputTreeNodeFigure(parentFigure.getParent());
        return rootOutputTreeNodeFigure;
    }

    class ColumnAnchor extends ChopboxAnchor {

        private boolean isSource;

        public ColumnAnchor(IFigure owner, boolean isSource) {
            super(owner);
            this.isSource = isSource;
        }

        public IFigure getOwner() {
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
