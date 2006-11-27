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
import java.util.List;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.DragTracker;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.jface.viewers.TextCellEditor;
import org.talend.commons.utils.workbench.gef.SimpleHtmlCellEditorLocator;
import org.talend.commons.utils.workbench.gef.SimpleHtmlFigure;
import org.talend.commons.utils.workbench.gef.SimpleHtmlTextEditManager;
import org.talend.designer.core.model.components.EParameterName;
import org.talend.designer.core.ui.editor.nodecontainer.NodeContainerLayoutEditPolicy;
import org.talend.designer.core.ui.editor.nodecontainer.NodeContainerPart;

/**
 * Graphical part of the node label of Gef. <br/>
 * 
 * $Id$
 * 
 */
public class NodeLabelEditPart extends AbstractGraphicalEditPart implements PropertyChangeListener {

    SimpleHtmlTextEditManager manager = null;

    protected NodePart nodePart;

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#activate()
     */
    public void activate() {
        if (!isActive()) {
            super.activate();
            ((NodeLabel) getModel()).addPropertyChangeListener(this);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#getAdapter(java.lang.Class)
     */
    public Object getAdapter(final Class key) {
        return super.getAdapter(key);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#deactivate()
     */
    public void deactivate() {
        if (isActive()) {
            super.deactivate();
            ((NodeLabel) getModel()).removePropertyChangeListener(this);
        }
    }

    /**
     * Gives the EditPart of the node where is attached the label.
     * 
     * @return
     */
    public NodePart getNodePart() {
        return nodePart;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#createFigure()
     */
    public IFigure createFigure() {
        String text = ((NodeLabel) getModel()).getLabelText();

        SimpleHtmlFigure htmlFig = new SimpleHtmlFigure();
        htmlFig.setText(text);

        if (((NodeLabel) getModel()).isActivate()) {
            ((SimpleHtmlFigure) htmlFig).setAlpha(-1);
        } else {
            ((SimpleHtmlFigure) htmlFig).setAlpha(NodeLabel.ALPHA_VALUE);
        }
        ((NodeLabel) getModel()).setLabelSize(htmlFig.getPreferredSize());
        getParent().refresh();
        return htmlFig;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
     */
    public void propertyChange(final PropertyChangeEvent evt) {
        String request = evt.getPropertyName();

        if (request.equals(NodeLabel.OFFSET_CHANGE)) { //$NON-NLS-1$ //$NON-NLS-2$
            refreshVisuals();
            getParent().refresh();
        }
        if (request.equals(NodeLabel.TEXT_CHANGE)) {
            refreshVisuals();
            // set the new size to update the node container
            ((NodeLabel) getModel()).setLabelSize(((SimpleHtmlFigure) figure).getPreferredSize());
            getParent().refresh();
        }
        if (request.equals(NodeLabel.LOCATION)) { //$NON-NLS-1$
            refreshVisuals();
            getParent().refresh();
        }
        if (request.equals(EParameterName.ACTIVATE.getName())) {
            if (((NodeLabel) getModel()).isActivate()) {
                ((SimpleHtmlFigure) figure).setAlpha(-1);
                ((SimpleHtmlFigure) figure).repaint();
                refreshVisuals();
            } else {
                ((SimpleHtmlFigure) figure).setAlpha(Node.ALPHA_VALUE);
                ((SimpleHtmlFigure) figure).repaint();
                refreshVisuals();
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.gef.editparts.AbstractEditPart#createEditPolicies()
     */
    public void createEditPolicies() {
        installEditPolicy(EditPolicy.DIRECT_EDIT_ROLE, new NodeTextEditPolicy());
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#getDragTracker(org.eclipse.gef.Request)
     */
    public DragTracker getDragTracker(final Request request) {
        return new NodeTextTracker(this, (EditPart) this.getParent());
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.gef.editparts.AbstractEditPart#refreshVisuals()
     */
    protected void refreshVisuals() {
        if (nodePart == null) {
            nodePart = ((NodeContainerPart) getParent()).getNodePart();
        }
        if (nodePart != null) {
            String text = ((NodeLabel) getModel()).getLabelText();
            SimpleHtmlFigure htmlFig = (SimpleHtmlFigure) this.getFigure();
            htmlFig.setText(text);
            Point loc = ((Node) nodePart.getModel()).getLocation().getCopy();
            Point offset = ((NodeLabel) getModel()).getOffset();
            Point textOffset = new Point();

            NodeFigure nodeFig = (NodeFigure) nodePart.getFigure();
            Dimension size = htmlFig.getPreferredSize();

            textOffset.y = nodeFig.getNodeSize().height;
            textOffset.x = (nodeFig.getNodeSize().width - size.width) / 2;
            ((NodeLabel) getModel()).setTextOffset(textOffset);
            loc.translate(textOffset.x + offset.x, textOffset.y + offset.y);
            Rectangle rectangle = new Rectangle(loc, size);
            ((GraphicalEditPart) getParent()).setLayoutConstraint(this, getFigure(), rectangle);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.gef.editparts.AbstractEditPart#performRequest(org.eclipse.gef.Request)
     */
    public void performRequest(final Request request) {
        if (request.getType() == RequestConstants.REQ_DIRECT_EDIT) {
            performDirectEdit();
        }
    }

    /**
     * Start the manager to edit the label.
     */
    private void performDirectEdit() {
        if (manager == null) {
            manager = new SimpleHtmlTextEditManager(this, TextCellEditor.class, new SimpleHtmlCellEditorLocator(
                    (SimpleHtmlFigure) this.getFigure()));
        }
        String label = (String) ((NodeLabel) getModel()).getPropertyValue(EParameterName.LABEL.getName());
        manager.setLabelText(label);
        manager.show();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.gef.editparts.AbstractEditPart#setSelected(int)
     */
    @SuppressWarnings("unchecked")
    public void setSelected(final int value) {
        if (value != SELECTED_NONE) {
            List<EditPart> listEditParts = (List<EditPart>) this.getViewer().getSelectedEditParts();
            if (listEditParts.size() != 1) {
                getParent().removeEditPolicy(EditPolicy.LAYOUT_ROLE);
                super.setSelected(SELECTED_NONE);
                this.getViewer().deselect(this);
                fireSelectionChanged();
            } else {
                getParent().installEditPolicy(EditPolicy.LAYOUT_ROLE, new NodeContainerLayoutEditPolicy());
                super.setSelected(value);
            }
        } else {
            getParent().removeEditPolicy(EditPolicy.LAYOUT_ROLE);
            super.setSelected(value);
        }
    }

    @Override
    public boolean isSelectable() {
        return (!((NodeLabel) getModel()).getNode().isReadOnly()) || (this.getViewer().getSelection().isEmpty());
    }
}
