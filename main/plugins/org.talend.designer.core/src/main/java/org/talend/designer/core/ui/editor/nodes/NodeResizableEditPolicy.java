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
package org.talend.designer.core.ui.editor.nodes;

import java.util.List;

import org.eclipse.draw2d.Cursors;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.gef.DragTracker;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.Handle;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.ResizableEditPolicy;
import org.eclipse.gef.requests.ChangeBoundsRequest;
import org.eclipse.swt.graphics.Cursor;
import org.talend.designer.core.ui.editor.TalendScalableFreeformRootEditPart;
import org.talend.designer.core.ui.editor.cmd.ResizeNodeCommand;
import org.talend.designer.core.ui.editor.connections.NodeResizableHandle;
import org.talend.designer.core.ui.editor.nodecontainer.NodeContainer;

/**
 */
public class NodeResizableEditPolicy extends ResizableEditPolicy {

    @Override
    protected Command getResizeCommand(ChangeBoundsRequest request) {
        Object parent = getHost().getParent().getModel();
        if (!(parent instanceof NodeContainer)) {
            return null;
        }

        Node node = (Node) getHost().getModel();
        if (node.isReadOnly()) {
            return null;
        }

        TalendScalableFreeformRootEditPart rootEditPart = (TalendScalableFreeformRootEditPart) getHost().getRoot();
        double scale = 1 / rootEditPart.getZoomManager().getZoom();
        return new ResizeNodeCommand(node, new Dimension(node.getSize().width + request.getSizeDelta().getScaled(scale).width,
                node.getSize().height + request.getSizeDelta().getScaled(scale).height));
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.gef.editpolicies.ResizableEditPolicy#createResizeHandle(java.util.List, int)
     */
    @Override
    protected void createResizeHandle(List handles, int direction) {
        if ((getResizeDirections() & direction) == direction) {
            addHandle((GraphicalEditPart) getHost(), handles, direction, getResizeTracker(direction),
                    Cursors.getDirectionalCursor(direction, getHostFigure().isMirrored()));
        } else {
            // display 'resize' handle to allow dragging or indicate selection
            // only
            createDragHandle(handles, direction);
        }
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
    //
    // /*
    // * (non-Javadoc)
    // *
    // * @see org.eclipse.gef.editpolicies.ResizableEditPolicy#createSelectionHandles()
    // */
    // @Override
    // protected List createSelectionHandles() {
    // if (getResizeDirections() == PositionConstants.NONE) {
    // // non resizable, so delegate to super implementation
    // List list = new ArrayList();
    // createMoveHandle(list);
    // createDragHandle(list, PositionConstants.NORTH_EAST);
    // createDragHandle(list, PositionConstants.NORTH_WEST);
    // createDragHandle(list, PositionConstants.SOUTH_EAST);
    // createDragHandle(list, PositionConstants.SOUTH_WEST);
    // return list;
    // }
    //
    // // resizable in at least one direction
    // List list = new ArrayList();
    // createMoveHandle(list);
    // createResizeHandle(list, PositionConstants.NORTH);
    // createResizeHandle(list, PositionConstants.EAST);
    // createResizeHandle(list, PositionConstants.SOUTH);
    // createResizeHandle(list, PositionConstants.WEST);
    // createResizeHandle(list, PositionConstants.SOUTH_EAST);
    // createResizeHandle(list, PositionConstants.SOUTH_WEST);
    // createResizeHandle(list, PositionConstants.NORTH_WEST);
    // createResizeHandle(list, PositionConstants.NORTH_EAST);
    // return list;
    // }

}
