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
import org.talend.sqlbuilder.util.UIUtils;

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
    @Override
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
    @Override
    public Object getAdapter(final Class key) {
        return super.getAdapter(key);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#deactivate()
     */
    @Override
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
    @Override
    public IFigure createFigure() {
        String text = ((NodeLabel) getModel()).getLabelText();

        SimpleHtmlFigure htmlFig = new SimpleHtmlFigure();
        htmlFig.setText(text);

        if (((NodeLabel) getModel()).isActivate()) {
            (htmlFig).setAlpha(-1);
        } else {
            (htmlFig).setAlpha(NodeLabel.ALPHA_VALUE);
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
            NodeLabel label = (NodeLabel) getModel();
            UIUtils.updateSqlBuilderDialogTitle(label.getLabelText(), label.getNode().getProcess().getName(), label.getNode()
                    .getUniqueName());
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
    @Override
    public void createEditPolicies() {
        installEditPolicy(EditPolicy.DIRECT_EDIT_ROLE, new NodeTextEditPolicy());
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#getDragTracker(org.eclipse.gef.Request)
     */
    @Override
    public DragTracker getDragTracker(final Request request) {
        return new NodeTextTracker(this, this.getParent());
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
            String text = ((NodeLabel) getModel()).getLabelText();
            SimpleHtmlFigure htmlFig = (SimpleHtmlFigure) this.getFigure();
            htmlFig.setText(text);
            Node node = (Node) nodePart.getModel();
            Point loc = node.getLocation().getCopy();
            Point offset = ((NodeLabel) getModel()).getOffset();
            Point textOffset = new Point();

            Dimension size = htmlFig.getPreferredSize();

            textOffset.y = node.getSize().height;
            textOffset.x = (node.getSize().width - size.width) / 2;
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
    @Override
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

    /**
     * yzhang Comment method "getDirectEditManager".
     * 
     * @return
     */
    public SimpleHtmlTextEditManager getDirectEditManager() {
        return this.manager;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.gef.editparts.AbstractEditPart#setSelected(int)
     */
    @Override
    @SuppressWarnings("unchecked")//$NON-NLS-1$
    public void setSelected(final int value) {
        if (value != SELECTED_NONE) {
            List<EditPart> listEditParts = this.getViewer().getSelectedEditParts();
            if (listEditParts.size() != 1) {
                getParent().removeEditPolicy(EditPolicy.LAYOUT_ROLE);
                // super.setSelected(SELECTED_NONE);
                // this.getViewer().deselect(this);
                // fireSelectionChanged();
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
