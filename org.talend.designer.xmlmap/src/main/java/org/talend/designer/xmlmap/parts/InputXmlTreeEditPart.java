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
import org.eclipse.draw2d.MouseEvent;
import org.eclipse.draw2d.MouseListener;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.editpolicies.NonResizableEditPolicy;
import org.talend.designer.xmlmap.figures.ExpressionFigure;
import org.talend.designer.xmlmap.figures.InputXmlTreeFigure;
import org.talend.designer.xmlmap.model.emf.xmlmap.InputXmlTree;
import org.talend.designer.xmlmap.model.emf.xmlmap.XmlmapPackage;
import org.talend.designer.xmlmap.policy.XmlDirectEditPolicy;

/**
 * wchen class global comment. Detailled comment
 */
public class InputXmlTreeEditPart extends BaseEditPart {

    private InputXmlTreeFigure figure;

    private ExpressionFigure oldExpression;

    @Override
    protected IFigure createFigure() {
        figure = new InputXmlTreeFigure((InputXmlTree) getModel());
        addFigureListener(figure);
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
            }
        }
    }

    @Override
    protected void createEditPolicies() {
        super.createEditPolicies();
        installEditPolicy(EditPolicy.SELECTION_FEEDBACK_ROLE, new NonResizableEditPolicy());
        installEditPolicy(EditPolicy.DIRECT_EDIT_ROLE, new XmlDirectEditPolicy());
    }

    private void addFigureListener(final IFigure figure) {
        figure.addMouseListener(new MouseListener() {

            public void mousePressed(MouseEvent me) {
                IFigure findFigureAt = figure.findFigureAt(me.x, me.y);
                if (findFigureAt instanceof ExpressionFigure) {
                    ExpressionFigure expressionFigure = (ExpressionFigure) findFigureAt;
                    if (oldExpression == null) {

                    }
                } else {

                }
            }

            public void mouseReleased(MouseEvent me) {
            }

            public void mouseDoubleClicked(MouseEvent me) {
            }

        });
    }

}
