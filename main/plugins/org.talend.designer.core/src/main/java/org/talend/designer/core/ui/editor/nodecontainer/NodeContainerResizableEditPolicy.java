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

import java.util.Iterator;
import java.util.List;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Cursors;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.RectangleFigure;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.DragTracker;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.Handle;
import org.eclipse.gef.LayerConstants;
import org.eclipse.gef.SharedCursors;
import org.eclipse.gef.editpolicies.ResizableEditPolicy;
import org.eclipse.gef.handles.MoveHandle;
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

    private void addHandle(GraphicalEditPart part, List handles, int direction, DragTracker tracker, Cursor cursor) {
        handles.add(createHandle(part, direction, tracker, cursor));
    }

    private Handle createHandle(GraphicalEditPart owner, int direction, DragTracker tracker, Cursor cursor) {
        NodeResizableHandle handle = new NodeResizableHandle(owner, direction);
        handle.setDragTracker(tracker);
        handle.setCursor(cursor);
        return handle;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.gef.editpolicies.NonResizableEditPolicy#createDragHandle(java.util.List, int)
     */
    @Override
    protected void createDragHandle(List handles, int direction) {
        if (isDragAllowed()) {
            // display 'resize' handles to allow dragging (drag tracker)
            addHandle((GraphicalEditPart) getHost(), handles, direction, getDragTracker(), SharedCursors.SIZEALL);
        } else {
            // display 'resize' handles to indicate selection only (selection
            // tracker)
            addHandle((GraphicalEditPart) getHost(), handles, direction, getSelectTracker(), SharedCursors.ARROW);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.gef.editpolicies.NonResizableEditPolicy#createMoveHandle(java.util.List)
     */
    @Override
    protected void createMoveHandle(List handles) {
        if (isDragAllowed()) {
            // display 'move' handle to allow dragging
            handles.add(moveHandle((GraphicalEditPart) getHost(), getDragTracker(), Cursors.SIZEALL));
        } else {
            // display 'move' handle only to indicate selection
            handles.add(moveHandle((GraphicalEditPart) getHost(), getSelectTracker(), SharedCursors.ARROW));
        }
    }

    private Handle moveHandle(GraphicalEditPart owner, DragTracker tracker, Cursor cursor) {
        MoveHandle moveHandle = new MoveHandle(owner);
        moveHandle.setForegroundColor(ColorConstants.gray);
        moveHandle.setDragTracker(tracker);
        moveHandle.setCursor(cursor);
        return moveHandle;
    }

}
