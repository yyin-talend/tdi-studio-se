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
package org.talend.designer.core.ui.editor.connections;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.DragTracker;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.gef.editpolicies.ConnectionEditPolicy;
import org.eclipse.gef.requests.GroupRequest;
import org.eclipse.jface.viewers.TextCellEditor;
import org.talend.commons.utils.workbench.gef.LabelCellEditorLocator;
import org.talend.commons.utils.workbench.gef.TextEditManager;
import org.talend.core.model.process.EConnectionType;
import org.talend.core.model.process.Element;
import org.talend.designer.core.ui.editor.cmd.ConnectionDeleteCommand;

/**
 * Graphical part of the Gef object for the connection label. <br/>
 * 
 * $Id$
 * 
 */
public class ConnLabelEditPart extends AbstractGraphicalEditPart implements PropertyChangeListener {

    TextEditManager manager = null;

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

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#createFigure()
     */
    public IFigure createFigure() {
        String text = ((ConnectionLabel) getModel()).getLabelText();
        Label label = new Label();
        label.setText(text);
        return label;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
     */
    public void propertyChange(PropertyChangeEvent evt) {
        String request = evt.getPropertyName();
        if (request.equals("positionChange") || request.equals("textChange")) { //$NON-NLS-1$ //$NON-NLS-2$
            refreshVisuals();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.gef.editparts.AbstractEditPart#createEditPolicies()
     */
    public void createEditPolicies() {
        installEditPolicy(EditPolicy.GRAPHICAL_NODE_ROLE, new ConnTextMovePolicy());
        installEditPolicy(EditPolicy.DIRECT_EDIT_ROLE, new ConnTextEditPolicy());
        installEditPolicy(EditPolicy.COMPONENT_ROLE, new ConnectionEditPolicy() {

            protected Command getDeleteCommand(GroupRequest request) {
                if (((Connection) getHost().getParent().getModel()).isReadOnly()) {
                    return null;
                }
                return new ConnectionDeleteCommand(((ConnectionLabel) this.getHost().getModel()).getConnection());
            }
        });
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#getDragTracker(org.eclipse.gef.Request)
     */
    public DragTracker getDragTracker(Request request) {
        return new ConnTextTracker(this, (ConnectionPart) getParent());
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.gef.editparts.AbstractEditPart#refreshVisuals()
     */
    protected void refreshVisuals() {
        String text = ((ConnectionLabel) getModel()).getLabelText();
        Point offset = ((ConnectionLabel) getModel()).getOffset();
        Label figure = (Label) getFigure();
        figure.setText(text);
        ConnectionPart parent = (ConnectionPart) getParent();
        PolylineConnection connFigure = (PolylineConnection) parent.getFigure();
        ConnLabelConstraint constraint = new ConnLabelConstraint(text, "center", offset, connFigure);
        parent.setLayoutConstraint(this, getFigure(), constraint);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.gef.editparts.AbstractEditPart#performRequest(org.eclipse.gef.Request)
     */
    public void performRequest(Request request) {
        Connection connectionParent = (Connection) getParent().getModel();

        if (((Connection) getParent().getModel()).isReadOnly()) {
            return;
        }
        if (request.getType() == RequestConstants.REQ_DIRECT_EDIT
                && (connectionParent.getLineStyle().equals(EConnectionType.FLOW_MAIN) || connectionParent
                        .getLineStyle().equals(EConnectionType.FLOW_REF))) {
            performDirectEdit();
        }
    }

    /**
     * Start the manager to edit the label.
     */
    private void performDirectEdit() {
        ((Label) getFigure()).setText(((Connection) getParent().getModel()).getName());
        if (manager == null) {
            manager = new TextEditManager(this, TextCellEditor.class, new LabelCellEditorLocator((Label) getFigure()));
        }
        manager.show();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.gef.editparts.AbstractEditPart#setSelected(int)
     */
    public void setSelected(int value) {
        super.setSelected(value);
        if (this.getParent().getSelected() != value) {
            this.getParent().setSelected(value);
        }
    }
}
