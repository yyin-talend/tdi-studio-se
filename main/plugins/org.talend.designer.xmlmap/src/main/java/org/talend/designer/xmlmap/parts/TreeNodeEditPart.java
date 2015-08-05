// ============================================================================
//
// Copyright (C) 2006-2015 Talend Inc. - www.talend.com
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
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.draw2d.TreeSearch;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.gef.ConnectionEditPart;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.NodeEditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.editparts.AbstractConnectionEditPart;
import org.eclipse.gef.requests.DirectEditRequest;
import org.eclipse.swt.SWT;
import org.talend.designer.components.lookup.common.ICommonLookup.MATCHING_MODE;
import org.talend.designer.xmlmap.editor.XmlMapGraphicViewer;
import org.talend.designer.xmlmap.figures.ExpressionFigure;
import org.talend.designer.xmlmap.figures.OutputXmlTreeFigure;
import org.talend.designer.xmlmap.figures.anchors.ConnectionColumnAnchor;
import org.talend.designer.xmlmap.figures.anchors.FilterColumnAnchor;
import org.talend.designer.xmlmap.figures.anchors.LookupColumnAnchor;
import org.talend.designer.xmlmap.figures.cells.IWidgetCell;
import org.talend.designer.xmlmap.figures.treeNode.RootTreeNodeFigure;
import org.talend.designer.xmlmap.figures.treeNode.RowFigure;
import org.talend.designer.xmlmap.figures.treeNode.TreeNodeFigure;
import org.talend.designer.xmlmap.model.emf.xmlmap.AbstractInOutTree;
import org.talend.designer.xmlmap.model.emf.xmlmap.InputXmlTree;
import org.talend.designer.xmlmap.model.emf.xmlmap.NodeType;
import org.talend.designer.xmlmap.model.emf.xmlmap.OutputTreeNode;
import org.talend.designer.xmlmap.model.emf.xmlmap.TreeNode;
import org.talend.designer.xmlmap.model.emf.xmlmap.XmlmapPackage;
import org.talend.designer.xmlmap.parts.directedit.XmlMapNodeCellEditorLocator;
import org.talend.designer.xmlmap.parts.directedit.XmlMapNodeDirectEditManager;
import org.talend.designer.xmlmap.policy.DragAndDropEditPolicy;
import org.talend.designer.xmlmap.policy.RowSelectionEditPolicy;
import org.talend.designer.xmlmap.policy.TreeExpandSupportEditPolicy;
import org.talend.designer.xmlmap.policy.XmlDirectEditPolicy;
import org.talend.designer.xmlmap.ui.resource.ColorInfo;
import org.talend.designer.xmlmap.ui.resource.ColorProviderMapper;
import org.talend.designer.xmlmap.util.XmlMapUtil;

/**
 * wchen class global comment. Detailled comment
 */
public class TreeNodeEditPart extends AbstractNodePart implements NodeEditPart {

    // for expand and collapse
    protected IFigure rootAnchor;

    protected XmlMapNodeDirectEditManager directEditManager;

    @Override
    protected IFigure createFigure() {
        TreeNode model = (TreeNode) getModel();
        boolean isRoot = false;
        if (model.eContainer() instanceof AbstractInOutTree) {
            isRoot = true;
        }

        final RowFigure testRow = new RowFigure(this, !isRoot);
        TreeNodeFigure treeNodeFigure = null;
        if (isRoot) {
            treeNodeFigure = new RootTreeNodeFigure(testRow);
        } else {
            treeNodeFigure = new TreeNodeFigure(testRow);
        }

        return treeNodeFigure;
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
        installEditPolicy(EditPolicy.SELECTION_FEEDBACK_ROLE, new RowSelectionEditPolicy());
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
        list.addAll(model.getFilterOutGoingConnections());
        return list;
    }

    @Override
    protected List getModelTargetConnections() {
        TreeNode model = (TreeNode) getModel();
        return model.getLookupIncomingConnections();
    }

    public void expand(boolean expand) {
        // if (getFigure() instanceof XmlTreeBranch) {
        // ((XmlTreeBranch) this.getFigure()).doExpandCollapse((XmlTreeBranch) (getFigure()));
        // }
    }

    @Override
    public IFigure getContentPane() {
        return ((TreeNodeFigure) getFigure()).getContents();
    }

    public ConnectionAnchor getSourceConnectionAnchor(ConnectionEditPart connection) {
        IFigure figure = null;
        boolean forceDarshDot = false;
        if (getRootAnchor() != null) {
            figure = getRootAnchor();
            forceDarshDot = true;
        } else {
            figure = getFigure();
        }
        if (connection instanceof LookupConnectionEditPart) {
            return new LookupColumnAnchor(figure, connection, this);
        }
        if (connection instanceof FilterConnectionEditPart) {
            return new FilterColumnAnchor(figure, connection, this);
        }
        return new ConnectionColumnAnchor(figure, connection, this, forceDarshDot);
    }

    public ConnectionAnchor getTargetConnectionAnchor(ConnectionEditPart connection) {
        IFigure figure = null;
        boolean forceDarshDot = false;
        if (getRootAnchor() != null) {
            figure = getRootAnchor();
            forceDarshDot = true;
        } else {
            figure = getFigure();
        }
        if (connection instanceof LookupConnectionEditPart) {
            return new LookupColumnAnchor(figure, connection, this);
        }
        return new ConnectionColumnAnchor(figure, connection, this, forceDarshDot);
    }

    public ConnectionAnchor getSourceConnectionAnchor(Request request) {
        return null;
    }

    public ConnectionAnchor getTargetConnectionAnchor(Request request) {
        return null;
    }

    @Override
    protected void addChildVisual(EditPart childEditPart, int index) {
        super.addChildVisual(childEditPart, index);
    }

    @Override
    protected void removeChildVisual(EditPart childEditPart) {
        super.removeChildVisual(childEditPart);
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
                            connectionEditPart.refresh();
                            // if (connectionEditPart.getFigure() instanceof PolylineConnection) {
                            // PolylineConnection connFigure = (PolylineConnection) connectionEditPart.getFigure();
                            // if (expanded) {
                            // connFigure.setLineStyle(SWT.LINE_DASHDOTDOT);
                            // } else {
                            // connFigure.setLineStyle(SWT.LINE_SOLID);
                            // }
                            // }
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
        TreeNodeFigure treeNodeFigure = (TreeNodeFigure) getFigure();

        switch (type) {
        case Notification.SET:
            switch (featureId) {
            case XmlmapPackage.TREE_NODE__LOOP:
            case XmlmapPackage.OUTPUT_TREE_NODE__GROUP:
            case XmlmapPackage.OUTPUT_TREE_NODE__AGGREGATE:
            case XmlmapPackage.TREE_NODE__OPTIONAL:
                treeNodeFigure.getElement().getBranchContent().updateStatus();
                break;
            case XmlmapPackage.TREE_NODE__NAME:
                treeNodeFigure.getElement().getBranchContent().updataNameFigure();
                break;
            case XmlmapPackage.TREE_NODE__DEFAULT_VALUE:
                treeNodeFigure.getElement().getBranchContent().updateDefaultValueFigure();
                break;
            case XmlmapPackage.TREE_NODE__TYPE:
                refreshChildren();
                break;
            case XmlmapPackage.TREE_NODE__EXPRESSION:
                if (getModel() instanceof TreeNode && !(getModel() instanceof OutputTreeNode)) {
                    TreeNode inputTreeNodeRoot = XmlMapUtil.getTreeNodeRoot((TreeNode) getModel());
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
                    if (outputFigure.getElement() != null) {
                        outputFigure.getElement().updateExpression();
                    }
                }

                if (((AbstractInOutTreeEditPart) XmlMapUtil.findTreePart(this)).getFigure() instanceof OutputXmlTreeFigure) {
                    OutputXmlTreeFigure outputXmlTreeFigure = (OutputXmlTreeFigure) ((AbstractInOutTreeEditPart) XmlMapUtil
                            .findTreePart(this)).getFigure();
                    outputXmlTreeFigure.update(XmlmapPackage.TREE_NODE__TYPE);
                }
                break;
            case XmlmapPackage.INPUT_XML_TREE__MINIMIZED:
                refreshSourceConnections();
                refreshTargetConnections();
                break;
            case XmlmapPackage.ABSTRACT_IN_OUT_TREE__MULTI_LOOPS:
                
                break;
            }
            break;

        case Notification.ADD:
        case Notification.ADD_MANY:
            switch (featureId) {
            case XmlmapPackage.TREE_NODE__CHILDREN:
                refreshChildren();
                boolean expressionEditable = XmlMapUtil.isExpressionEditable((TreeNode) getModel());
                if (!expressionEditable) {
                    ExpressionFigure expression = treeNodeFigure.getElement().getExpressionFigure();
                    if (expression != null) {
                        expression.setOpaque(true);
                        expression.setBackgroundColor(ColorProviderMapper.getColor(ColorInfo.COLOR_EXPREESION_DISABLE));

                    }
                }

                break;
            case XmlmapPackage.TREE_NODE__OUTGOING_CONNECTIONS:
            case XmlmapPackage.TREE_NODE__LOOKUP_OUTGOING_CONNECTIONS:
            case XmlmapPackage.TREE_NODE__FILTER_OUT_GOING_CONNECTIONS:
                refreshSourceConnections();
                break;
            case XmlmapPackage.OUTPUT_TREE_NODE__INCOMING_CONNECTIONS:
            case XmlmapPackage.TREE_NODE__LOOKUP_INCOMING_CONNECTIONS:
                refreshTargetConnections();
                break;
            }
            break;
        case Notification.REMOVE:
        case Notification.REMOVE_MANY:
            switch (featureId) {
            case XmlmapPackage.TREE_NODE__CHILDREN:
                refreshChildren();
                ExpressionFigure expression = treeNodeFigure.getElement().getExpressionFigure();
                if (expression != null) {
                    if (XmlMapUtil.isExpressionEditable((TreeNode) getModel())) {
                        expression.setOpaque(false);
                    }
                }
            case XmlmapPackage.TREE_NODE__OUTGOING_CONNECTIONS:
            case XmlmapPackage.TREE_NODE__LOOKUP_OUTGOING_CONNECTIONS:
            case XmlmapPackage.TREE_NODE__FILTER_OUT_GOING_CONNECTIONS:
                refreshSourceConnections();
                break;
            case XmlmapPackage.OUTPUT_TREE_NODE__INCOMING_CONNECTIONS:
            case XmlmapPackage.TREE_NODE__LOOKUP_INCOMING_CONNECTIONS:
                refreshTargetConnections();

            }
            break;
        }

    }

    @Override
    public void performRequest(Request req) {
        if (RequestConstants.REQ_DIRECT_EDIT.equals(req.getType())) {
            Figure figure = null;
            DirectEditRequest drequest = (DirectEditRequest) req;
            Point figureLocation = drequest.getLocation();
            if (getFigure() instanceof TreeNodeFigure) {
                TreeNodeFigure treeNodeFigure = (TreeNodeFigure) getFigure();
                ArrayList collection = new ArrayList();
                collection.add(treeNodeFigure.getElement().getExpressionFigure());
                collection.add(treeNodeFigure.getElement().getBranchContent());
                figure = (Figure) treeNodeFigure.findFigureAt(figureLocation.x, figureLocation.y, new FigureSearch(collection));
            }
            if (figure instanceof IWidgetCell) {
                directEditManager = new XmlMapNodeDirectEditManager(this, new XmlMapNodeCellEditorLocator(figure, this));
            }
            if (directEditManager != null) {
                TreeNode outputTreeNode = (TreeNode) getModel();
                if (figure instanceof ExpressionFigure) {
                    if (XmlMapUtil.isExpressionEditable(outputTreeNode)) {
                        Point location = drequest.getLocation();
                        if (figure.containsPoint(location)) {
                            directEditManager.show();
                            ((XmlMapGraphicViewer) getViewer()).getMapperManager().setCurrentDirectEditManager(directEditManager);
                        }
                    }
                } else if (!(((TreeNode) getModel()).eContainer() instanceof AbstractInOutTree)) {
                    // disable for RC2
                    // directEditManager.show();
                    // ((XmlMapGraphicViewer)
                    // getViewer()).getMapperManager().setCurrentDirectEditManager(directEditManager);
                }
            }
        }
        super.performRequest(req);
    }

    public XmlMapDataEditPart getMapDataEditPart() {
        List children2 = getViewer().getRootEditPart().getChildren();
        if (children2.size() == 1 && children2.get(0) instanceof XmlMapDataEditPart) {
            return (XmlMapDataEditPart) children2.get(0);
        }
        return null;
    }

    public AbstractInOutTreeEditPart getInOutTreeEditPart(EditPart part) {
        if (part.getParent() instanceof AbstractInOutTreeEditPart) {
            return (AbstractInOutTreeEditPart) part.getParent();
        } else if (part.getParent() instanceof TreeNodeEditPart) {
            return getInOutTreeEditPart(part.getParent());
        }

        return null;
    }

    protected IFigure getRootAnchor() {
        return rootAnchor;
    }

    protected void setRootAnchor(IFigure rootAnchor) {
        this.rootAnchor = rootAnchor;
    }

    class FigureSearch implements TreeSearch {

        final Collection collection;

        public FigureSearch(Collection collection) {
            this.collection = collection;
        }

        public boolean accept(IFigure figure) {
            return collection.contains(figure);
        }

        public boolean prune(IFigure figure) {
            // TODO Auto-generated method stub
            return false;
        }

    }

}
