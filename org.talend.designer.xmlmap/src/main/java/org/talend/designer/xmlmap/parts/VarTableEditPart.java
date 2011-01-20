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
import org.talend.designer.xmlmap.figures.CenterVarFigure;
import org.talend.designer.xmlmap.model.emf.xmlmap.VarTable;
import org.talend.designer.xmlmap.model.emf.xmlmap.XmlmapPackage;

/**
 * DOC Administrator class global comment. Detailled comment
 */
public class VarTableEditPart extends BaseEditPart {

    private CenterVarFigure centerVarFigure;

    @Override
    protected IFigure createFigure() {
        /* Center var figure */
        VarTable model = (VarTable) getModel();
        centerVarFigure = new CenterVarFigure(model);
        centerVarFigure.getHeader().getVarText().setText(model.getName());
        return centerVarFigure;
    }

    @Override
    public IFigure getContentPane() {
        return centerVarFigure.getVarTableContainerFigure().getColumnsContainer();
    }

    @Override
    protected List getModelChildren() {

        return ((VarTable) getModel()).getNodes();
    }

    @Override
    public void notifyChanged(Notification notification) {
        int type = notification.getEventType();
        int featureId = notification.getFeatureID(XmlmapPackage.class);
        switch (type) {
        case Notification.ADD:
        case Notification.REMOVE:
            switch (featureId) {
            case XmlmapPackage.VAR_NODE:
                refreshSourceConnections();
                refreshTargetConnections();
            case XmlmapPackage.VAR_TABLE__NODES:
                refreshChildren();
                refreshSourceConnections();
                refreshTargetConnections();
                break;
            }

        case Notification.SET:
            switch (featureId) {
            case XmlmapPackage.VAR_TABLE__NODES:
                refreshChildren();
            }
        }
    }

}
