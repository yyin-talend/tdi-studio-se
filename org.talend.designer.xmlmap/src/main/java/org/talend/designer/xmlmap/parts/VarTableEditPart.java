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
import org.eclipse.draw2d.ImageFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.swt.graphics.Image;
import org.talend.designer.xmlmap.figures.CenterVarFigure;
import org.talend.designer.xmlmap.model.emf.xmlmap.VarTable;
import org.talend.designer.xmlmap.model.emf.xmlmap.XmlmapPackage;
import org.talend.designer.xmlmap.ui.resource.ImageInfo;
import org.talend.designer.xmlmap.ui.resource.ImageProviderMapper;

/**
 * DOC Administrator class global comment. Detailled comment
 */
public class VarTableEditPart extends BaseEditPart {

    private CenterVarFigure centerVarFigure;

    private Image restorImage = ImageProviderMapper.getImage(ImageInfo.RESTORE_ICON);

    private Image miniImage = ImageProviderMapper.getImage(ImageInfo.MINIMIZE_ICON);

    private static Label minitooltip = new Label("Minimize");

    private static Label restoretooltip = new Label("Restore");

    @Override
    protected IFigure createFigure() {
        /* Center var figure */
        VarTable model = (VarTable) getModel();
        centerVarFigure = new CenterVarFigure(model);
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
                break;
            case XmlmapPackage.VAR_TABLE__MINIMIZED:
                Boolean newStateIsMinimized = notification.getNewBooleanValue();
                ImageFigure minisize = ((CenterVarFigure) getFigure()).getImageButtonsFigure().getMiniSize();
                if (newStateIsMinimized) {
                    minisize.setImage(restorImage);
                    minisize.setToolTip(restoretooltip);
                } else {
                    minisize.setImage(miniImage);
                    minisize.setToolTip(minitooltip);
                }
                // refreshChildren();
                // refreshVisuals();
                // refresh();
            }
        }
    }
    // @Override
    // protected void refreshVisuals() {
    // // super.refreshVisuals();
    // // this.getFigure().validate();
    // for (IFigure child : (List<IFigure>) this.getFigure().getChildren()) {
    // child.validate();
    // }
    // }

}
