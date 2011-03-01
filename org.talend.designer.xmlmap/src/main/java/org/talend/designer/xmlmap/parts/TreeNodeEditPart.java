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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.draw2d.ChopboxAnchor;
import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.gef.ConnectionEditPart;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.NodeEditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.editparts.AbstractConnectionEditPart;
import org.eclipse.gef.editpolicies.NonResizableEditPolicy;
import org.eclipse.swt.SWT;
import org.talend.commons.ui.swt.geftree.figure.TreeBranch;
import org.talend.designer.components.lookup.common.ICommonLookup.MATCHING_MODE;
import org.talend.designer.xmlmap.figures.ExpressionFigure;
import org.talend.designer.xmlmap.figures.TreeBranchFigure;
import org.talend.designer.xmlmap.figures.TreeNodeFigure;
import org.talend.designer.xmlmap.figures.XmlTreeBranch;
import org.talend.designer.xmlmap.model.emf.xmlmap.INodeConnection;
import org.talend.designer.xmlmap.model.emf.xmlmap.InputXmlTree;
import org.talend.designer.xmlmap.model.emf.xmlmap.NodeType;
import org.talend.designer.xmlmap.model.emf.xmlmap.OutputTreeNode;
import org.talend.designer.xmlmap.model.emf.xmlmap.OutputXmlTree;
import org.talend.designer.xmlmap.model.emf.xmlmap.TreeNode;
import org.talend.designer.xmlmap.model.emf.xmlmap.VarNode;
import org.talend.designer.xmlmap.model.emf.xmlmap.XmlmapPackage;
import org.talend.designer.xmlmap.parts.directedit.XmlMapNodeCellEditorLocator;
import org.talend.designer.xmlmap.parts.directedit.XmlMapNodeDirectEditManager;
import org.talend.designer.xmlmap.policy.DragAndDropEditPolicy;
import org.talend.designer.xmlmap.policy.TreeExpandSupportEditPolicy;
import org.talend.designer.xmlmap.policy.XmlDirectEditPolicy;
import org.talend.designer.xmlmap.util.XmlMapUtil;

/**
 * wchen class global comment. Detailled comment
 */
public class TreeNodeEditPart extends AbstractNodePart implements NodeEditPart {

    // for expand and collapse
    protected IFigure rootAnchor;

    protected XmlTreeBranch treeBranchFigure;

    protected XmlMapNodeDirectEditManager directEditManager;

    @Override
    protected IFigure createFigure() {
        TreeNode model = (TreeNode) getModel();
        IFigure figure = null;

        // nodes in xml tree
        if (XmlMapUtil.getXPathLength(model.getXpath()) > 2) {
            String status = model.isLoop() ? String.valueOf(model.isLoop()) : "";
            figure = new XmlTreeBranch(new TreeBranchFigure(model), XmlTreeBranch.STYLE_ROW_HANGING);
            treeBranchFigure = (XmlTreeBranch) figure;

        }
        // normal column and tree root
        else {
            figure = new TreeNodeFigure(this);
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
        installEditPolicy(EditPolicy.DIRECT_EDIT_ROLE, new XmlDirectEditPolicy());
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
        List list = new ArrayList();
        list.addAll(model.getOutgoingConnections());
        list.addAll(model.getLookupOutgoingConnections());
        return list;
    }

    @Override
    protected List getModelTargetConnections() {
        TreeNode model = (TreeNode) getModel();
        return model.getLookupIncomingConnections();
    }

    public void expand(boolean expand) {
        if (getFigure() instanceof XmlTreeBranch) {
            ((XmlTreeBranch) this.getFigure()).doExpandCollapse((XmlTreeBranch) (getFigure()));
        }
    }

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
        return new ColumnAnchor(figure, connection);
        // return new ChopboxAnchor(getFigure());
    }

    public ConnectionAnchor getTargetConnectionAnchor(ConnectionEditPart connection) {
        IFigure figure = null;
        if (getRootAnchor() != null) {
            figure = getRootAnchor();
        } else {
            figure = getFigure();
        }
        return new ColumnAnchor(figure, connection);
        // return new ChopboxAnchor(getFigure());
    }

    public ConnectionAnchor getSourceConnectionAnchor(Request request) {
        IFigure figure = null;
        if (getRootAnchor() != null) {
            figure = getRootAnchor();
        } else {
            figure = getFigure();
        }
        return new ColumnAnchor(figure, null);
        // return new ChopboxAnchor(getFigure());
    }

    public ConnectionAnchor getTargetConnectionAnchor(Request request) {
        IFigure figure = null;
        if (getRootAnchor() != null) {
            figure = getRootAnchor();
        } else {
            figure = getFigure();
        }
        return new ColumnAnchor(figure, null);
        // return new ChopboxAnchor(getFigure());
    }

    @Override
    protected void addChildVisual(EditPart childEditPart, int index) {
        TreeNode treeNode = (TreeNode) getModel();
        TreeNode inputTreeNodeRoot = XmlMapUtil.getInputTreeNodeRoot(treeNode);
        boolean isLookup = false;
        if (inputTreeNodeRoot != null && inputTreeNodeRoot.eContainer() instanceof InputXmlTree) {
            isLookup = ((InputXmlTree) inputTreeNodeRoot.eContainer()).isLookup();
        }
        if (isLookup) {
            TreeNodeEditPart childPart = (TreeNodeEditPart) childEditPart;
            TreeNodeEditPart parentPart = (TreeNodeEditPart) childEditPart.getParent();
            IFigure parentFigure = parentPart.getFigure();
            // when add one child node of a tree,should also add a label for firstColumn
            /* 1.should find the related root TreeNodeFigure */
            TreeNodeFigure rootTreeNodeFigure = findRootTreeNodeFigure(parentFigure);
            /* 2.add label to first column of rootTreeNodeFigure */
            if (childPart.getFigure() instanceof XmlTreeBranch && rootTreeNodeFigure != null) {
                Figure rootTreeNodeExpressionFigure = rootTreeNodeFigure.getExpressionContainer();
                ExpressionFigure expressionFigure = new ExpressionFigure();
                expressionFigure.setText(((TreeNode) childPart.getModel()).getExpression());
                XmlTreeBranch treeBranch = (XmlTreeBranch) childPart.getFigure();
                expressionFigure.setTreeBranch(treeBranch);
                expressionFigure.setTreeNodePart(childPart);

                treeBranch.setExpressionFigure(expressionFigure);
                Map<TreeNode, Integer> nodeAndIndex = new HashMap<TreeNode, Integer>();
                int expressionIndex = getExpressionIndex((TreeNode) childPart.getModel(),
                        getModelTreeRoot((TreeNode) childPart.getModel()), 0, nodeAndIndex);
                rootTreeNodeExpressionFigure.add(expressionFigure, expressionIndex);
                getViewer().getVisualPartMap().put(expressionFigure, childEditPart);
            }
        }

        super.addChildVisual(childEditPart, index);
    }

    protected TreeNodeFigure findRootTreeNodeFigure(IFigure parentFigure) {
        TreeNodeFigure rootTreeNodeFigure = null;
        if (parentFigure instanceof TreeNodeFigure) {
            rootTreeNodeFigure = (TreeNodeFigure) parentFigure;
        } else
            rootTreeNodeFigure = findRootTreeNodeFigure(parentFigure.getParent());
        return rootTreeNodeFigure;
    }

    protected int getExpressionIndex(TreeNode treeNode, TreeNode treeRoot, int index, Map<TreeNode, Integer> nodeAndIndex) {
        for (int i = 0; i < treeRoot.getChildren().size(); i++) {
            index++;
            TreeNode treeNode2 = treeRoot.getChildren().get(i);
            nodeAndIndex.put(treeNode2, new Integer(index));
            if (treeNode == treeNode2) {
                return index;
            } else if (!treeNode2.getChildren().isEmpty()) {
                index = getExpressionIndex(treeNode, (TreeNode) treeNode2, index, nodeAndIndex);
                if (nodeAndIndex.get(treeNode) != null) {
                    return index;
                }
            }

        }
        return index;

    }

    protected TreeNode getModelTreeRoot(TreeNode treeNode) {
        if (treeNode.eContainer() instanceof OutputXmlTree || treeNode.eContainer() instanceof InputXmlTree) {
            return treeNode;
        } else {
            return getModelTreeRoot((TreeNode) treeNode.eContainer());
        }

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
                        if (conn instanceof BaseConnectionEditPart) {
                            BaseConnectionEditPart connectionEditPart = (BaseConnectionEditPart) conn;
                            if (connectionEditPart.getFigure() instanceof PolylineConnection) {
                                PolylineConnection connFigure = (PolylineConnection) connectionEditPart.getFigure();
                                if (expanded) {
                                    connectionEditPart.setNodeCollapsed(true);
                                    connFigure.setLineStyle(SWT.LINE_DASHDOTDOT);
                                } else {
                                    connectionEditPart.setNodeCollapsed(false);
                                    if (!connectionEditPart.isDOTStyle()) {
                                        connFigure.setLineStyle(SWT.LINE_SOLID);
                                    } else {
                                        connFigure.setLineStyle(SWT.LINE_DASHDOTDOT);
                                    }
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
            case XmlmapPackage.TREE_NODE__EXPRESSION:
                if (getModel() instanceof TreeNode && !(getModel() instanceof OutputTreeNode)) {
                    TreeNode inputTreeNodeRoot = XmlMapUtil.getInputTreeNodeRoot((TreeNode) getModel());
                    if (inputTreeNodeRoot != null && inputTreeNodeRoot.eContainer() instanceof InputXmlTree) {
                        InputXmlTree inputTree = (InputXmlTree) inputTreeNodeRoot.eContainer();
                        if (inputTree.isLookup()) {
                            if (((TreeNode) getModel()).getExpression() != null
                                    && !((TreeNode) getModel()).getExpression().trim().equals("")) {
                                if (MATCHING_MODE.ALL_ROWS.toString().equals(inputTree.getMatchingMode())) {
                                    inputTree.setMatchingMode(MATCHING_MODE.UNIQUE_MATCH.toString());
                                }
                            } else {
                                boolean hasAtLeastOneHashKey = XmlMapUtil.hasAtLeastOneHashKey(inputTree);
                                if (!hasAtLeastOneHashKey) {
                                    if (!MATCHING_MODE.ALL_ROWS.toString().equals(inputTree.getMatchingMode())) {
                                        inputTree.setMatchingMode(MATCHING_MODE.ALL_ROWS.toString());
                                    }
                                }
                            }
                        }
                    }
                }
                if (getFigure() instanceof TreeNodeFigure) {
                    TreeNodeFigure outputFigure = (TreeNodeFigure) getFigure();
                    if (outputFigure.getColumnExpressionFigure() != null) {
                        outputFigure.getColumnExpressionFigure().setText(((TreeNode) getModel()).getExpression());
                    }

                } else if (getFigure() instanceof XmlTreeBranch) {
                    ExpressionFigure expressionFigure = ((XmlTreeBranch) getFigure()).getExpressionFigure();
                    if (expressionFigure != null) {
                        expressionFigure.setText(((TreeNode) getModel()).getExpression());
                    }
                }
                break;

            }
        case Notification.ADD:
        case Notification.ADD_MANY:
            switch (featureId) {
            case XmlmapPackage.TREE_NODE__CHILDREN:
                refreshChildren();
                break;
            case XmlmapPackage.TREE_NODE__OUTGOING_CONNECTIONS:
            case XmlmapPackage.TREE_NODE__LOOKUP_OUTGOING_CONNECTIONS:
                refreshSourceConnections();
                break;
            case XmlmapPackage.OUTPUT_TREE_NODE__INCOMING_CONNECTIONS:
            case XmlmapPackage.TREE_NODE__LOOKUP_INCOMING_CONNECTIONS:
                refreshTargetConnections();
                break;
            }
        case Notification.REMOVE:
        case Notification.REMOVE_MANY:
            switch (featureId) {
            case XmlmapPackage.TREE_NODE__CHILDREN:
                refreshChildren();
            case XmlmapPackage.TREE_NODE__OUTGOING_CONNECTIONS:
            case XmlmapPackage.TREE_NODE__LOOKUP_OUTGOING_CONNECTIONS:
                refreshSourceConnections();
                break;
            case XmlmapPackage.OUTPUT_TREE_NODE__INCOMING_CONNECTIONS:
            case XmlmapPackage.TREE_NODE__LOOKUP_INCOMING_CONNECTIONS:
                refreshTargetConnections();

            }

        }

    }

    @Override
    public void performRequest(Request req) {
        if (RequestConstants.REQ_DIRECT_EDIT.equals(req.getType())) {
            if (directEditManager == null) {
                Figure figure = null;
                if (getFigure() instanceof TreeNodeFigure) {
                    figure = ((TreeNodeFigure) getFigure()).getColumnExpressionFigure();
                } else if (getFigure() instanceof XmlTreeBranch) {
                    figure = ((XmlTreeBranch) getFigure()).getExpressionFigure();
                }
                if (figure != null) {
                    directEditManager = new XmlMapNodeDirectEditManager(this, new XmlMapNodeCellEditorLocator(figure));
                }
            }
            if (directEditManager != null) {
                TreeNode outputTreeNode = (TreeNode) getModel();
                if (outputTreeNode.getChildren().isEmpty()) {
                    directEditManager.show();
                }
            }
        }
        super.performRequest(req);
    }

    private XmlMapDataEditPart getMapDataEditPart() {
        List children2 = getViewer().getRootEditPart().getChildren();
        if (children2.size() == 1 && children2.get(0) instanceof XmlMapDataEditPart) {
            return (XmlMapDataEditPart) children2.get(0);
        }
        return null;
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

    class ColumnAnchor extends ChopboxAnchor {

        private ConnectionEditPart connectionPart;

        public ColumnAnchor(IFigure owner, ConnectionEditPart connectionPart) {
            super(owner);
            this.connectionPart = connectionPart;
        }

        public IFigure getOwner() {
            return super.getOwner();
        }

        public Point getReferencePoint() {
            if (connectionPart == null) {
                return getOwner().getBounds().getLeft();
            }
            XmlMapDataEditPart mapDataEditPart = getMapDataEditPart();

            IFigure containerFigure = null;

            Object model = TreeNodeEditPart.this.getModel();
            boolean loctionRight = false;
            INodeConnection connection = (INodeConnection) connectionPart.getModel();
            // if current is input tree node or var node ,get figure right location
            if (model == connection.getSource()
                    && (connection.getTarget() instanceof OutputTreeNode || connection.getTarget() instanceof VarNode)) {
                loctionRight = true;
                containerFigure = mapDataEditPart.getLeftFigure();
            } else {
                containerFigure = mapDataEditPart.getRightFigure();
            }

            Point ref = null;
            if (getOwner() == null) {
                return null;
            } else if (getOwner() instanceof TreeNodeFigure) {
                TreeNodeFigure nodeFigure = (TreeNodeFigure) getOwner();
                // normal column
                if (nodeFigure.getTreeBranch() == null) {
                    if (loctionRight) {
                        ref = getOwner().getBounds().getRight();
                    } else {
                        if (nodeFigure.getColumnExpressionFigure() != null) {
                            ref = nodeFigure.getColumnExpressionFigure().getBounds().getLeft();
                        } else {
                            ref = getOwner().getBounds().getLeft();
                        }
                    }
                    getOwner().translateToAbsolute(ref);

                }
            } else if (getOwner() instanceof XmlTreeBranch) {
                XmlTreeBranch treeBranch = (XmlTreeBranch) getOwner();
                Rectangle elembounds = treeBranch.getElement().getBounds().getCopy();
                Rectangle bounds = treeBranch.getRoot().getBounds().getCopy();
                elembounds.x = bounds.x;
                elembounds.width = bounds.width;
                if (loctionRight) {
                    ref = elembounds.getRight();
                } else {
                    if (treeBranch.getExpressionFigure() != null) {
                        ref = treeBranch.getExpressionFigure().getBounds().getLeft();
                    } else {
                        ref = elembounds.getLeft();
                    }
                }
                getOwner().translateToAbsolute(ref);
            } else {
                ref = getOwner().getBounds().getCenter();
                getOwner().translateToAbsolute(ref);
            }

            if (connectionPart instanceof BaseConnectionEditPart && connectionPart.getFigure() instanceof PolylineConnection) {
                BaseConnectionEditPart baseConnectionPart = (BaseConnectionEditPart) connectionPart;
                PolylineConnection connFigure = (PolylineConnection) connectionPart.getFigure();

                org.eclipse.swt.graphics.Point avilableSize = getViewer().getControl().getSize();

                if (ref != null) {
                    if (ref.y < 0) {
                        if (!(baseConnectionPart instanceof LookupConnectionEditPart)) {
                            ref.y = 0;
                        }
                        if (loctionRight) {
                            baseConnectionPart.setSourceConcained(false);
                        } else {
                            baseConnectionPart.setTargetContained(false);
                        }
                        if (baseConnectionPart.isDOTStyle()) {
                            connFigure.setLineStyle(SWT.LINE_DASHDOTDOT);
                        } else {
                            connFigure.setLineStyle(SWT.LINE_SOLID);
                        }
                    } else if (ref.y > avilableSize.y) {
                        if (!(baseConnectionPart instanceof LookupConnectionEditPart)) {
                            ref.y = avilableSize.y;
                        }
                        if (loctionRight) {
                            baseConnectionPart.setSourceConcained(false);
                        } else {
                            baseConnectionPart.setTargetContained(false);
                        }
                        if (baseConnectionPart.isDOTStyle()) {
                            connFigure.setLineStyle(SWT.LINE_DASHDOTDOT);
                        } else {
                            connFigure.setLineStyle(SWT.LINE_SOLID);
                        }
                    } else {
                        if (loctionRight) {
                            baseConnectionPart.setSourceConcained(true);
                        } else {
                            baseConnectionPart.setTargetContained(true);
                        }
                        if (!baseConnectionPart.isDOTStyle()) {
                            connFigure.setLineStyle(SWT.LINE_SOLID);
                        }
                    }
                }
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
            if (referencePoint != null) {
                return new Point(referencePoint.x, referencePoint.y);
            } else {
                return new Point(0, 0);
            }

        }

        @Override
        protected Rectangle getBox() {
            return super.getBox();
        }
    }

}
