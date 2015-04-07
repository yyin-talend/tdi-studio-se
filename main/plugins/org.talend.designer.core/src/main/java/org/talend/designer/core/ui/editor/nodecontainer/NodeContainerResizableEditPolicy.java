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
package org.talend.designer.core.ui.editor.nodecontainer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.RectangleFigure;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.DragTracker;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.Handle;
import org.eclipse.gef.LayerConstants;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.ResizableEditPolicy;
import org.eclipse.swt.graphics.Cursor;
import org.talend.designer.core.ui.editor.connections.NodeResizableHandle;
import org.talend.designer.core.ui.editor.nodes.Node;
import org.talend.designer.core.ui.editor.nodes.NodeFigure;

/**
 * This class describes the figures that will be used for drag and drop each objects on the diagram. <br/>
 * 
 * $Id$
 * 
 */
public class NodeContainerResizableEditPolicy extends ResizableEditPolicy {

    /**
     * Creates the figure used for feedback.
     * 
     * @return the new feedback figure
     */
    @Override
    protected IFigure createDragSourceFeedbackFigure() {
        IFigure figure = createFigure((GraphicalEditPart) getHost(), null);

        figure.setBounds(getInitialFeedbackBounds());
        addFeedback(figure);
        return figure;
    }

    protected IFigure createFigure(GraphicalEditPart part, IFigure parent) {
        IFigure child = getCustomFeedbackFigure(part.getModel());

        if (parent != null) {
            parent.add(child);
        }

        Rectangle childBounds = part.getFigure().getBounds().getCopy();

        IFigure walker = part.getFigure().getParent();

        while (walker != ((GraphicalEditPart) part.getParent()).getFigure()) {
            walker.translateToParent(childBounds);
            walker = walker.getParent();
        }

        child.setBounds(childBounds);

        Iterator i = part.getChildren().iterator();

        while (i.hasNext()) {
            createFigure((GraphicalEditPart) i.next(), child);
        }

        return child;
    }

    /**
     * This will take the figure of the node and set it as feedback figure.
     * 
     * @param modelPart
     * @return
     */
    protected IFigure getCustomFeedbackFigure(Object modelPart) {
        IFigure figure;

        if (modelPart instanceof Node) {
            Node node = (Node) modelPart;
            figure = new Figure();
            figure.setOpaque(false);
            NodeFigure nodeFigure = new NodeFigure(node);
            figure.add(nodeFigure);
            nodeFigure.setLocation(new Point(32, 32));
            if (node.isStart()) {
                nodeFigure.setBackgroundColor(NodeFigure.START_COLOR);
            } else {
                nodeFigure.setOpaque(false);
            }
        } else {
            figure = new RectangleFigure();
            ((RectangleFigure) figure).setXOR(true);
            ((RectangleFigure) figure).setFill(true);
            figure.setBackgroundColor(ColorConstants.darkGreen);
            figure.setForegroundColor(ColorConstants.white);
        }

        return figure;
    }

    /**
     * Returns the layer used for displaying feedback.
     * 
     * @return the feedback layer
     */
    @Override
    protected IFigure getFeedbackLayer() {
        return getLayer(LayerConstants.SCALED_FEEDBACK_LAYER);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.gef.editpolicies.NonResizableEditPolicy#getInitialFeedbackBounds()
     */
    @Override
    protected Rectangle getInitialFeedbackBounds() {
        return getHostFigure().getBounds().getExpanded(32, 32);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.gef.editpolicies.NonResizableEditPolicy#getOrphanCommand(org.eclipse.gef.Request)
     */
    @Override
    protected Command getOrphanCommand(Request req) {
        // if (req instanceof ChangeBoundsRequest) {
        // ChangeBoundsRequest cbr = (ChangeBoundsRequest) req;
        // EditPart editPart = (EditPart) cbr.getEditParts().get(0);
        // if (editPart instanceof NodePart) {
        // MoveNodeCommand locationCommand = new MoveNodeCommand((Node) editPart.getModel(), cbr.getLocation());
        // return locationCommand;
        // }
        // }
        return super.getOrphanCommand(req);
    }

    // /*
    // * (non-Javadoc)
    // *
    // * @see org.eclipse.gef.editpolicies.ResizableEditPolicy#createResizeHandle(java.util.List, int)
    // */
    // @Override
    // protected void createResizeHandle(List handles, int direction) {
    // if ((getResizeDirections() & direction) == direction) {
    // addHandle((GraphicalEditPart) getHost(), handles, direction, getResizeTracker(direction),
    // Cursors.getDirectionalCursor(direction, getHostFigure().isMirrored()));
    // } else {
    // // display 'resize' handle to allow dragging or indicate selection
    // // only
    // createDragHandle(handles, direction);
    // }
    // }

    private void addHandle(GraphicalEditPart part, List handles, int direction, DragTracker tracker, Cursor cursor) {
        handles.add(createHandle(part, direction, tracker, cursor));
    }

    private Handle createHandle(GraphicalEditPart owner, int direction, DragTracker tracker, Cursor cursor) {
        NodeResizableHandle handle = new NodeResizableHandle(owner, direction);
        handle.setDragTracker(tracker);
        handle.setCursor(cursor);
        return handle;
    }

    // /*
    // * (non-Javadoc)
    // *
    // * @see org.eclipse.gef.editpolicies.NonResizableEditPolicy#createDragHandle(java.util.List, int)
    // */
    // @Override
    // protected void createDragHandle(List handles, int direction) {
    // if (isDragAllowed()) {
    // // display 'resize' handles to allow dragging (drag tracker)
    // addHandle((GraphicalEditPart) getHost(), handles, direction, getDragTracker(), SharedCursors.SIZEALL);
    // } else {
    // // display 'resize' handles to indicate selection only (selection
    // // tracker)
    // addHandle((GraphicalEditPart) getHost(), handles, direction, getSelectTracker(), SharedCursors.ARROW);
    // }
    // }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.gef.editpolicies.ResizableEditPolicy#createSelectionHandles()
     */
    @Override
    protected List createSelectionHandles() {
        if (getResizeDirections() == PositionConstants.NONE) {
            // non resizable, so delegate to super implementation
            List list = new ArrayList();
            createMoveHandle(list);
            createDragHandle(list, PositionConstants.NORTH_EAST);
            createDragHandle(list, PositionConstants.NORTH_WEST);
            createDragHandle(list, PositionConstants.SOUTH_EAST);
            createDragHandle(list, PositionConstants.SOUTH_WEST);
            return list;
        }

        // resizable in at least one direction
        List list = new ArrayList();
        createMoveHandle(list);
        createResizeHandle(list, PositionConstants.NORTH);
        createResizeHandle(list, PositionConstants.EAST);
        createResizeHandle(list, PositionConstants.SOUTH);
        createResizeHandle(list, PositionConstants.WEST);
        createResizeHandle(list, PositionConstants.SOUTH_EAST);
        createResizeHandle(list, PositionConstants.SOUTH_WEST);
        createResizeHandle(list, PositionConstants.NORTH_WEST);
        createResizeHandle(list, PositionConstants.NORTH_EAST);
        return list;
    }
}
