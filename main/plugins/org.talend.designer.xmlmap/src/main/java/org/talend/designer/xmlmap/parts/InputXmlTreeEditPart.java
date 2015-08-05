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

import java.util.List;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.requests.DirectEditRequest;
import org.talend.designer.xmlmap.editor.XmlMapGraphicViewer;
import org.talend.designer.xmlmap.figures.InputXmlTreeFigure;
import org.talend.designer.xmlmap.figures.OutputXmlTreeFigure;
import org.talend.designer.xmlmap.figures.cells.IWidgetCell;
import org.talend.designer.xmlmap.figures.treeNode.TreeNodeFigure;
import org.talend.designer.xmlmap.model.emf.xmlmap.InputXmlTree;
import org.talend.designer.xmlmap.model.emf.xmlmap.OutputTreeNode;
import org.talend.designer.xmlmap.model.emf.xmlmap.OutputXmlTree;
import org.talend.designer.xmlmap.model.emf.xmlmap.XmlmapPackage;
import org.talend.designer.xmlmap.parts.directedit.XmlMapNodeCellEditorLocator;
import org.talend.designer.xmlmap.parts.directedit.XmlMapNodeDirectEditManager;
import org.talend.designer.xmlmap.util.XmlMapUtil;

/**
 * wchen class global comment. Detailled comment
 */
public class InputXmlTreeEditPart extends AbstractInOutTreeEditPart {

    private InputXmlTreeFigure figure;

    private XmlMapNodeDirectEditManager directEditManager;

    @Override
    protected IFigure createFigure() {
        figure = new InputXmlTreeFigure(this);
        return figure;
    }

    @Override
    public IFigure getContentPane() {
        return ((InputXmlTreeFigure) getFigure()).getColumnContainer();
    }

    @Override
    protected List getModelChildren() {
        return ((InputXmlTree) getModel()).getNodes();
    }

    @Override
    protected void addChildVisual(EditPart childEditPart, int index) {
        super.addChildVisual(childEditPart, index);
    }

    @Override
    public void notifyChanged(Notification notification) {
        int type = notification.getEventType();
        int featureId = notification.getFeatureID(XmlmapPackage.class);
        switch (type) {
        case Notification.ADD:
        case Notification.REMOVE:
        case Notification.REMOVE_MANY:
            switch (featureId) {
            case XmlmapPackage.INPUT_XML_TREE__NODES:
                refreshChildren();
                break;
            case XmlmapPackage.ABSTRACT_IN_OUT_TREE__FILTER_INCOMING_CONNECTIONS:
                refreshTargetConnections();
                break;
            }
            break;
        case Notification.SET:
            switch (featureId) {
            case XmlmapPackage.INPUT_XML_TREE__NODES:
                refreshChildren();
                break;
            case XmlmapPackage.INPUT_XML_TREE__ACTIVATE_CONDENSED_TOOL:
                getFigure().validate();
                break;
            case XmlmapPackage.INPUT_XML_TREE__LOOKUP_MODE:
            case XmlmapPackage.INPUT_XML_TREE__MATCHING_MODE:
            case XmlmapPackage.INPUT_XML_TREE__INNER_JOIN:
            case XmlmapPackage.INPUT_XML_TREE__PERSISTENT:
            case XmlmapPackage.INPUT_XML_TREE__MINIMIZED:
            case XmlmapPackage.INPUT_XML_TREE__EXPRESSION_FILTER:
                ((InputXmlTreeFigure) getFigure()).update(featureId);
                break;
            case XmlmapPackage.ABSTRACT_IN_OUT_TREE__MULTI_LOOPS:
                if (getParent() instanceof XmlMapDataEditPart) {
                    List childPart = ((XmlMapDataEditPart) getParent()).getChildren();
                    for (Object o : childPart) {
                        if (o instanceof OutputXmlTreeEditPart) {
                            OutputXmlTreeEditPart outputPart = (OutputXmlTreeEditPart) o;
                            boolean hasDocument = XmlMapUtil.hasDocument((OutputXmlTree) outputPart.getModel());
                            if (!hasDocument) {
                                OutputXmlTreeFigure outputTreeFigure = (OutputXmlTreeFigure) outputPart.getFigure();
                                outputTreeFigure.update(XmlmapPackage.ABSTRACT_IN_OUT_TREE__MULTI_LOOPS);
                            } else {
                                List nodeChild = outputPart.getChildren();
                                for (Object nc : nodeChild) {
                                    if (nc instanceof OutputTreeNodeEditPart) {
                                        refreshOutputNodeLoopFunctionBtn((OutputTreeNodeEditPart) nc);
                                    }
                                }
                            }
                        }
                    }
                    boolean changed = clearOutputAggregateIfNeeded();
                    if (changed) {
                        ((XmlMapDataEditPart) getParent()).refresh();
                    }
                    ((XmlMapGraphicViewer) getViewer()).getMapperManager().getProblemsAnalyser().checkProblems();
                    ((XmlMapGraphicViewer) getViewer()).getMapperManager().getMapperUI().updateStatusBar();
                }
            }
        }
    }

    protected boolean clearOutputAggregateIfNeeded() {
        // clear aggregate in output table if input main and output are both multiloops
        if (((InputXmlTree) getModel()).isMultiLoops() && getParent() instanceof XmlMapDataEditPart) {
            List childPart = ((XmlMapDataEditPart) getParent()).getChildren();
            for (Object o : childPart) {
                if (o instanceof OutputXmlTreeEditPart) {
                    OutputXmlTree outputModel = (OutputXmlTree) ((OutputXmlTreeEditPart) o).getModel();
                    if (outputModel.isMultiLoops()) {
                        return cleanAggregate(outputModel.getNodes());
                    }
                }
            }
        }
        return false;
    }

    private void refreshOutputNodeLoopFunctionBtn(OutputTreeNodeEditPart outputNodePart) {
        OutputTreeNode outputNode = (OutputTreeNode) outputNodePart.getModel();
        if (outputNode.isLoop()) {
            TreeNodeFigure nodeFigure = (TreeNodeFigure) outputNodePart.getFigure();
            if (nodeFigure != null) {
                nodeFigure.getElement().getBranchContent().updateLoopButtonFigure();
            }
        } else if (!outputNodePart.getChildren().isEmpty()) {
            for (Object nc : outputNodePart.getChildren()) {
                if (nc instanceof OutputTreeNodeEditPart) {
                    refreshOutputNodeLoopFunctionBtn((OutputTreeNodeEditPart) nc);
                }
            }
        }
    }

    @Override
    public void performRequest(Request req) {
        if (RequestConstants.REQ_DIRECT_EDIT.equals(req.getType())) {
            DirectEditRequest drequest = (DirectEditRequest) req;
            Point figureLocation = drequest.getLocation();

            IFigure findFigureAt = getFigure().findFigureAt(figureLocation.x, figureLocation.y);

            if (findFigureAt instanceof IWidgetCell) {
                directEditManager = new XmlMapNodeDirectEditManager(this, new XmlMapNodeCellEditorLocator((Figure) findFigureAt));
                directEditManager.show();
            }
        }

    }

}
