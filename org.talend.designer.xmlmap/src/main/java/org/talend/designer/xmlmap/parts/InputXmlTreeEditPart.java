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

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.editpolicies.NonResizableEditPolicy;
import org.eclipse.gef.requests.DirectEditRequest;
import org.talend.designer.xmlmap.figures.InputXmlTreeFigure;
import org.talend.designer.xmlmap.figures.cells.IWidgetCell;
import org.talend.designer.xmlmap.model.emf.xmlmap.InputXmlTree;
import org.talend.designer.xmlmap.model.emf.xmlmap.XmlmapPackage;
import org.talend.designer.xmlmap.parts.directedit.XmlMapNodeCellEditorLocator;
import org.talend.designer.xmlmap.parts.directedit.XmlMapNodeDirectEditManager;
import org.talend.designer.xmlmap.policy.XmlDirectEditPolicy;

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
            }

        case Notification.SET:
            switch (featureId) {
            case XmlmapPackage.INPUT_XML_TREE__NODES:
                refreshChildren();
                break;
            case XmlmapPackage.INPUT_XML_TREE__ACTIVATE_CONDENSED_TOOL:
                getFigure().validate();
            case XmlmapPackage.INPUT_XML_TREE__LOOKUP_MODE:
            case XmlmapPackage.INPUT_XML_TREE__MATCHING_MODE:
            case XmlmapPackage.INPUT_XML_TREE__INNER_JOIN:
            case XmlmapPackage.INPUT_XML_TREE__PERSISTENT:
            case XmlmapPackage.INPUT_XML_TREE__MINIMIZED:
                ((InputXmlTreeFigure) getFigure()).update(featureId);
            }
        }
    }

    @Override
    protected void createEditPolicies() {
        super.createEditPolicies();
        installEditPolicy(EditPolicy.SELECTION_FEEDBACK_ROLE, new NonResizableEditPolicy());
        installEditPolicy(EditPolicy.DIRECT_EDIT_ROLE, new XmlDirectEditPolicy());
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
