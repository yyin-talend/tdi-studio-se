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

import org.eclipse.draw2d.IFigure;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.EList;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.talend.designer.xmlmap.figures.CenterVarFigure;
import org.talend.designer.xmlmap.figures.treetools.VarToolBarFigure;
import org.talend.designer.xmlmap.model.emf.xmlmap.VarNode;
import org.talend.designer.xmlmap.model.emf.xmlmap.VarTable;
import org.talend.designer.xmlmap.model.emf.xmlmap.XmlmapPackage;

/**
 * DOC Administrator class global comment. Detailled comment
 */
public class VarTableEditPart extends BaseEditPart {

    private CenterVarFigure centerVarFigure;

    private ISelectionChangedListener selectionListener;

    @Override
    protected IFigure createFigure() {
        /* Center var figure */
        VarTable model = (VarTable) getModel();
        centerVarFigure = new CenterVarFigure(this);
        return centerVarFigure;
    }

    @Override
    public IFigure getContentPane() {
        return centerVarFigure.getTableItemContainer();
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
                refreshChildren();
            }
        }
    }

    @Override
    public void activate() {
        super.activate();

        selectionListener = new ISelectionChangedListener() {

            public void selectionChanged(SelectionChangedEvent event) {
                List selectedEditParts = getViewer().getSelectedEditParts();

                boolean remove = false;
                boolean moveUp = false;
                boolean moveDown = false;

                boolean disableMoveUp = false;
                boolean disableMoveDown = false;

                for (Object obj : selectedEditParts) {
                    if (obj instanceof VarNodeEditPart) {
                        remove = true;
                        VarNode selectedNode = (VarNode) ((VarNodeEditPart) obj).getModel();
                        EList<VarNode> nodes = ((VarTable) getModel()).getNodes();

                        int indexOf = nodes.indexOf(selectedNode);

                        if (!disableMoveUp) {
                            if (indexOf != -1 && indexOf > 0) {
                                moveUp = true;
                            } else {
                                moveUp = false;
                                disableMoveUp = true;
                            }
                        }

                        if (!disableMoveDown) {
                            if (indexOf != -1 && indexOf < nodes.size() - 1) {
                                moveDown = true;
                            } else {
                                moveDown = false;
                                disableMoveDown = true;
                            }
                        }

                    }
                }
                VarToolBarFigure toolBarFigure = ((CenterVarFigure) getFigure()).getToolBarFigure();
                toolBarFigure.setRemoveEnable(remove);
                toolBarFigure.setMoveUpEnable(moveUp);
                toolBarFigure.setMoveDownEnable(moveDown);

            }

        };

        getViewer().addSelectionChangedListener(selectionListener);
    }

    @Override
    public void deactivate() {
        super.deactivate();
        getViewer().removeSelectionChangedListener(selectionListener);
    }

}
