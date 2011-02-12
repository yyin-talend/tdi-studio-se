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

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.editpolicies.NonResizableEditPolicy;
import org.talend.commons.ui.runtime.exception.ExceptionHandler;
import org.talend.designer.xmlmap.figures.InputXmlTreeFigure;
import org.talend.designer.xmlmap.model.emf.xmlmap.InputXmlTree;
import org.talend.designer.xmlmap.model.emf.xmlmap.XmlmapPackage;

/**
 * wchen class global comment. Detailled comment
 */
public class InputXmlTreeEditPart extends BaseEditPart {

    InputXmlTreeFigure figure;

    @Override
    protected IFigure createFigure() {
        figure = new InputXmlTreeFigure((InputXmlTree) getModel());
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
        try {
            super.addChildVisual(childEditPart, index);
            IFigure child = ((GraphicalEditPart) childEditPart).getFigure();
            int childHeight = child.getBounds().height;
            IFigure parent = ((GraphicalEditPart) childEditPart.getParent()).getFigure();
            int parentHeight = parent.getBounds().height;
            int parentWidth = parent.getBounds().width;
            int parentX = parent.getBounds().x;
            int parentY = parent.getBounds().y;
            parentHeight += childHeight;
            Rectangle newBounds = new Rectangle(parentX, parentY, parentWidth, parentHeight);
            parent.setBounds(newBounds);
        } catch (Exception e) {
            ExceptionHandler.process(e);
        }
    }

    @Override
    public void notifyChanged(Notification notification) {
        int type = notification.getEventType();
        int featureId = notification.getFeatureID(XmlmapPackage.class);
        switch (type) {
        case Notification.ADD:
        case Notification.REMOVE:
            switch (featureId) {
            case XmlmapPackage.INPUT_XML_TREE__NODES:
                refreshChildren();
                break;
            }

        case Notification.SET:
            switch (featureId) {
            case XmlmapPackage.INPUT_XML_TREE__NODES:
                refreshChildren();
            }
        }
    }

    @Override
    protected void createEditPolicies() {
        super.createEditPolicies();
        installEditPolicy(EditPolicy.SELECTION_FEEDBACK_ROLE, new NonResizableEditPolicy());
    }

}
