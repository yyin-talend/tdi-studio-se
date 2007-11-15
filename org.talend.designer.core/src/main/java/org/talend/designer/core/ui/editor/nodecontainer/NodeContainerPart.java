// ============================================================================
//
// Copyright (C) 2006-2007 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//   
// ============================================================================
package org.talend.designer.core.ui.editor.nodecontainer;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.talend.designer.core.model.components.EParameterName;
import org.talend.designer.core.ui.editor.nodes.Node;
import org.talend.designer.core.ui.editor.nodes.NodePart;

/**
 * EditPart linked to the NodeContainer.
 * 
 * $Id$
 * 
 */
public class NodeContainerPart extends AbstractGraphicalEditPart implements PropertyChangeListener {

    @Override
    public boolean isSelectable() {
        return false;
    }

    protected NodePart nodePart = null;

    public void activate() {
        if (!isActive()) {
            super.activate();
            Node node = ((NodeContainer) getModel()).getNode();
            node.addPropertyChangeListener(this);
        }
    }

    public void deactivate() {
        if (isActive()) {
            super.deactivate();
            Node node = ((NodeContainer) getModel()).getNode();
            node.removePropertyChangeListener(this);
        }
    }

    @Override
    public void setSelected(int value) {
        super.setSelected(SELECTED_NONE);
    }

    public void setNodePart(NodePart nodePart) {
        this.nodePart = nodePart;
    }

    public NodePart getNodePart() {
        return nodePart;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#createFigure()
     */
    @Override
    protected IFigure createFigure() {
        NodeContainerFigure nodeContainerFigure = new NodeContainerFigure((NodeContainer) this.getModel());
        Node node = ((NodeContainer) getModel()).getNode();
        if (node.isActivate()) {
            nodeContainerFigure.setAlpha(-1);
        } else {
            nodeContainerFigure.setAlpha(Node.ALPHA_VALUE);
        }
        nodeContainerFigure.updateStatus(node.getStatus());
        return nodeContainerFigure;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.gef.editparts.AbstractEditPart#createEditPolicies()
     */
    @Override
    protected void createEditPolicies() {
    }

    protected void refreshVisuals() {
        Rectangle rectangle = ((NodeContainerFigure) this.getFigure()).getNodeContainerRectangle();
        ((GraphicalEditPart) getParent()).setLayoutConstraint(this, getFigure(), rectangle);
    }

    protected List getModelChildren() {
        return ((NodeContainer) this.getModel()).getElements();
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
     */
    public void propertyChange(PropertyChangeEvent changeEvent) {
        if (changeEvent.getPropertyName().equals(Node.UPDATE_STATUS)) {
            Integer status = (Integer) changeEvent.getNewValue();
            ((NodeContainerFigure) this.getFigure()).updateStatus(status);
            refreshVisuals();
        }
        if (changeEvent.getPropertyName().equals(EParameterName.ACTIVATE.getName())) {
            Node node = ((NodeContainer) getModel()).getNode();
            if (node.isActivate()) {
                ((NodeContainerFigure) figure).setAlpha(-1);
                ((NodeContainerFigure) figure).repaint();
                refreshVisuals();
            } else {
                ((NodeContainerFigure) figure).setAlpha(Node.ALPHA_VALUE);
                ((NodeContainerFigure) figure).repaint();
                refreshVisuals();
            }
        }
        if (changeEvent.getPropertyName().equals(Node.PERFORMANCE_DATA)) {
            refreshVisuals();
        }
    }
}
