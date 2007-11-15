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
package org.talend.designer.core.ui.editor.connections;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.DragTracker;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.talend.core.model.process.Element;

/**
 * EditPart of the NodeTrace.
 * 
 * $Id$
 * 
 */
public class ConnectionTraceEditPart extends AbstractGraphicalEditPart implements PropertyChangeListener {

    @Override
    public boolean isSelectable() {
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#activate()
     */
    public void activate() {
        if (!isActive()) {
            super.activate();
            ((Element) getModel()).addPropertyChangeListener(this);
        }
    }

    protected void addChildVisual(EditPart childEditPart, int index) {
        IFigure child = ((GraphicalEditPart) childEditPart).getFigure();
        getContentPane().add(child, index);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#deactivate()
     */
    public void deactivate() {
        if (isActive()) {
            super.deactivate();
            ((Element) getModel()).removePropertyChangeListener(this);
        }
    }

    public ConnectionTraceEditPart() {
        super();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#createFigure()
     */
    @Override
    protected IFigure createFigure() {
        ConnectionTraceFigure fig = new ConnectionTraceFigure((Connection) this.getParent().getModel(), true);
        return fig;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.gef.editparts.AbstractEditPart#createEditPolicies()
     */
    @Override
    protected void createEditPolicies() {
        installEditPolicy(EditPolicy.GRAPHICAL_NODE_ROLE, new ConnTextMovePolicy());
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#getDragTracker(org.eclipse.gef.Request)
     */
    @Override
    public DragTracker getDragTracker(Request request) {
        return new ConnTextTracker(this, (ConnectionPart) getParent());
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.gef.editparts.AbstractEditPart#refreshVisuals()
     */
    @Override
    protected void refreshVisuals() {
        ConnectionPart connectionPart;
        connectionPart = (ConnectionPart) getParent();

        ConnectionTraceFigure htmlFigure = (ConnectionTraceFigure) getFigure();

        ConnectionTrace connectionTrace = (ConnectionTrace) getModel();
        String trace = connectionTrace.getTrace();
        htmlFigure.setTraceData(trace);

        Dimension size = htmlFigure.getPreferredSize();
        connectionTrace.setSize(size);
        String connectionName = ((Connection) connectionPart.getModel()).getConnectionLabel().getLabelText();
        Point offset = ((ConnectionTrace) getModel()).getOffset();

        ConnectionTraceConstraint constraint = new ConnectionTraceConstraint(connectionName, size, "center", offset, //$NON-NLS-1$
                (PolylineConnection) connectionPart.getFigure());

        connectionPart.setLayoutConstraint(this, getFigure(), constraint);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
     */
    public void propertyChange(PropertyChangeEvent evt) {
        String request = evt.getPropertyName();
        if (request.equals("positionChange") || request.equals(ConnectionTrace.TRACE_PROP)) { //$NON-NLS-1$ //$NON-NLS-2$
            refreshVisuals();
        }
    }
}
