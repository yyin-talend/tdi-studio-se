// ============================================================================
//
// Talend Community Edition
//
// Copyright (C) 2006 Talend - www.talend.com
//
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either
// version 2.1 of the License, or (at your option) any later version.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
// Lesser General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with this program; if not, write to the Free Software
// Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
//
// ============================================================================
package org.talend.designer.core.ui.editor.nodes;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.talend.commons.utils.workbench.gef.SimpleHtmlFigure;
import org.talend.designer.core.ui.editor.nodecontainer.NodeContainerPart;

/**
 * EditPart of the NodePerformance.
 * 
 * $Id$
 * 
 */
public class NodePerformanceEditPart extends AbstractGraphicalEditPart implements PropertyChangeListener {

    protected NodePart nodePart;

    @Override
    public boolean isSelectable() {
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#activate()
     */
    public void activate() {
        if (!isActive()) {
            super.activate();
            ((NodePerformance) getModel()).getNodeContainer().getNode().addPropertyChangeListener(this);
            ((NodePerformance) getModel()).getNodeContainer().getNodeLabel().addPropertyChangeListener(this);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#deactivate()
     */
    public void deactivate() {
        if (isActive()) {
            super.deactivate();
            ((NodePerformance) getModel()).getNodeContainer().getNode().removePropertyChangeListener(this);
            ((NodePerformance) getModel()).getNodeContainer().getNodeLabel().removePropertyChangeListener(this);
        }
    }

    public NodePerformanceEditPart() {
        super();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#createFigure()
     */
    @Override
    protected IFigure createFigure() {
        SimpleHtmlFigure label = new SimpleHtmlFigure();
        return label;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.gef.editparts.AbstractEditPart#createEditPolicies()
     */
    @Override
    protected void createEditPolicies() {
        // Do nothing
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.gef.editparts.AbstractEditPart#refreshVisuals()
     */
    @Override
    protected void refreshVisuals() {
        if (nodePart == null) {
            nodePart = ((NodeContainerPart) getParent()).getNodePart();
        }

        if (nodePart != null) {

            SimpleHtmlFigure label = (SimpleHtmlFigure) getFigure();

            NodePerformance nodePerf = (NodePerformance) getModel();
            label.setText(nodePerf.getLabel());
            Dimension size = label.getPreferredSize();
            nodePerf.setSize(size);
            Point loc = nodePerf.getLocation();

            Rectangle rectangle = new Rectangle(loc, size);
            ((GraphicalEditPart) getParent()).setLayoutConstraint(this, label, rectangle);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
     */
    public void propertyChange(PropertyChangeEvent evt) {
        String propName = evt.getPropertyName();
        if (propName.equals(Node.PERFORMANCE_DATA)) {
            refreshVisuals();
        } else if (propName.equals(Node.LOCATION)) {
            refreshVisuals();
        } else if (propName.equals(NodeLabel.OFFSET_CHANGE)) {
            refreshVisuals();
        }
    }
}
