// ============================================================================
//
// Copyright (C) 2006-2010 Talend Inc. - www.talend.com
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
import org.eclipse.emf.common.notify.Notification;
import org.talend.designer.xmlmap.figures.OutputXmlTreeFigure;
import org.talend.designer.xmlmap.model.emf.xmlmap.OutputXmlTree;
import org.talend.designer.xmlmap.model.emf.xmlmap.XmlmapPackage;

/**
 * wchen class global comment. Detailled comment
 */
public class OutputXmlTreeEditPart extends BaseEditPart {

    OutputXmlTreeFigure figure;

    @Override
    protected IFigure createFigure() {
        figure = new OutputXmlTreeFigure((OutputXmlTree) getModel());
        return figure;
    }

    @Override
    public IFigure getContentPane() {
        return ((OutputXmlTreeFigure) getFigure()).getColumnContainer();
    }

    @Override
    protected List getModelChildren() {
        return ((OutputXmlTree) getModel()).getNodes();
    }

    @Override
    public void notifyChanged(Notification notification) {
        int type = notification.getEventType();
        int featureId = notification.getFeatureID(XmlmapPackage.class);
        switch (type) {
        case Notification.ADD:
        case Notification.REMOVE:
            switch (featureId) {
            case XmlmapPackage.OUTPUT_XML_TREE__NODES:
                refreshChildren();
                break;
            }
        case Notification.SET:
            switch (featureId) {
            case XmlmapPackage.OUTPUT_XML_TREE__NODES:
                refreshChildren();
            }
        }
    }

}
