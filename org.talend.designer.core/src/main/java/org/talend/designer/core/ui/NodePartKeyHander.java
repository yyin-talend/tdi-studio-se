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
package org.talend.designer.core.ui;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.PositionConstants;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.ui.parts.GraphicalViewerKeyHandler;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.talend.designer.core.ui.editor.nodes.NodePart;
import org.talend.designer.core.ui.editor.process.ProcessPart;
import org.talend.designer.core.ui.editor.subjobcontainer.SubjobContainer;
import org.talend.designer.core.ui.editor.subjobcontainer.SubjobContainerPart;

/**
 * DOC Administrator class global comment. Detailled comment
 */
public class NodePartKeyHander extends GraphicalViewerKeyHandler {

    /**
     * DOC Administrator NodePartKeyHander constructor comment.
     * 
     * @param viewer
     */
    public NodePartKeyHander(GraphicalViewer viewer) {
        super(viewer);
        // TODO Auto-generated constructor stub
    }

    @Override
    protected void navigateTo(EditPart part, KeyEvent event) {
        // PTODO need be removed
        SubjobContainerPart subPart = null;
        boolean displayVa = true;
        if (part instanceof SubjobContainerPart) {
            // check the flag for display subjobcontainer at first.
            // NodeContainerPart
            SubjobContainerPart focusPart = (SubjobContainerPart) part;
            List subList = focusPart.getParent().getChildren();
            for (int j = 0; j < subList.size(); j++) {
                subPart = (SubjobContainerPart) subList.get(j);
                SubjobContainer subContainer = (SubjobContainer) subPart.getModel();
                if (subContainer.isDisplayed() == false) {
                    displayVa = false;
                }
            }
            if (displayVa == false) {
                part = (EditPart) part.getChildren().get(0);
                if (part != null) {
                    // NodePart
                    part = (EditPart) part.getChildren().get(0);
                }
                if (part == null) {
                    return;
                }
            }

        }

        super.navigateTo(part, event);
    }

    @Override
    protected List getNavigationSiblings() {
        EditPart focusPart = getFocusEditPart();
        boolean displayVa = true;
        if (focusPart.getParent() != null) {

            if (focusPart instanceof SubjobContainerPart) {
                // TODO check flag for subjob.
                // return getNodePart((SubjobContainerPart) focusPart);
                SubjobContainerPart subConPart = (SubjobContainerPart) focusPart;
                List subList = focusPart.getParent().getChildren();
                for (int j = 0; j < subList.size(); j++) {
                    subConPart = (SubjobContainerPart) subList.get(j);
                    SubjobContainer subContainer = (SubjobContainer) subConPart.getModel();
                    if (subContainer.isDisplayed() == false) {
                        displayVa = false;
                    }
                }
                if (displayVa == false) {
                    return getNodePart((ProcessPart) focusPart.getParent());
                }

            } else if (focusPart instanceof NodePart) {
                // get all node part for a job.
                return getNodePart((ProcessPart) focusPart.getParent().getParent().getParent());

                // return getNodePart((SubjobContainerPart) focusPart.getParent().getParent());

            }

            return focusPart.getParent().getChildren();
        }
        List list = new ArrayList();
        list.add(focusPart);
        return list;
    }

    private List<EditPart> getNodePart(ProcessPart part) {
        List<EditPart> nodePartList = new ArrayList<EditPart>();
        for (EditPart child : (List<EditPart>) part.getChildren()) {
            for (EditPart c : (List<EditPart>) child.getChildren()) {
                for (EditPart n : (List<EditPart>) c.getChildren()) {
                    if (n instanceof NodePart) {
                        nodePartList.add(n);
                    }
                }
            }
        }
        return nodePartList;
    }

    @Override
    public boolean keyPressed(KeyEvent event) {
        if (event.character == ' ') {
            processSelect(event);
            return true;
        } else if (acceptIntoContainer(event)) {
            navigateIntoContainer(event);
            return true;
        } else if (acceptOutOf(event)) {
            navigateOut(event);
            return true;
        } else if (acceptConnection(event)) {
            navigateConnections(event);
            return true;
        } else if (acceptScroll(event)) {
            scrollViewer(event);
            return true;
        } else if (acceptLeaveConnection(event)) {
            navigateOutOfConnection(event);
            return true;
        } else if (acceptLeaveContents(event)) {
            navigateIntoContainer(event);
            return true;
        }
        switch (event.keyCode) {
        case SWT.ARROW_LEFT:
            if (navigateNextSibling(event, isViewerMirrored() ? PositionConstants.EAST : PositionConstants.WEST))
                return true;
            break;
        case SWT.ARROW_RIGHT:
            if (navigateNextSibling(event, isViewerMirrored() ? PositionConstants.WEST : PositionConstants.EAST))
                return true;
            break;
        case SWT.ARROW_UP:
            if (navigateNextSibling(event, PositionConstants.NORTH))
                return true;
            break;
        case SWT.ARROW_DOWN:
            if (navigateNextSibling(event, PositionConstants.SOUTH))
                return true;
            break;

        case SWT.HOME:
            if (navigateJumpSibling(event, PositionConstants.WEST))
                return true;
            break;
        case SWT.END:
            if (navigateJumpSibling(event, PositionConstants.EAST))
                return true;
            break;
        case SWT.PAGE_DOWN:
            if (navigateJumpSibling(event, PositionConstants.SOUTH))
                return true;
            break;
        case SWT.PAGE_UP:
            if (navigateJumpSibling(event, PositionConstants.NORTH))
                return true;
        }
        return super.keyPressed(event);
        // return super.keyPressed(event);
    }

    //
    // @Override
    // protected void navigateIntoContainer(KeyEvent event) {
    // // TODO Auto-generated method stub
    // super.navigateIntoContainer(event);
    //
    // GraphicalEditPart focus = getFocusEditPart();
    // List childList = focus.getChildren();
    // Point tl = focus.getContentPane().getBounds().getTopLeft();
    // Rectangle childBounds = null;
    // int minimum = Integer.MAX_VALUE;
    // int current;
    // GraphicalEditPart closestPart = null;
    // NodePart part = null;
    // for (int i = 0; i < childList.size(); i++) {
    // System.out.println("NNNNNNNNNNNNN" + childList.get(i).getClass());// SubjobContainerPart
    // SubjobContainerPart subPart = (SubjobContainerPart) childList.get(i);
    // SubjobContainer subContainer = (SubjobContainer) subPart.getModel();
    //
    // if (!subPart.isSelectable())
    // continue;
    //
    // boolean diaplay = subContainer.isDisplayed();
    // if (diaplay == false) {
    // List subjobList = subPart.getChildren();
    //
    // NodeContainerPart noteContain = (NodeContainerPart) subjobList.get(0);
    // // childBounds = noteContain.getFigure().getBounds();
    // List nodeList = noteContain.getChildren();
    // part = (NodePart) nodeList.get(0);
    // childBounds = part.getFigure().getBounds();
    // } else {
    // childBounds = subPart.getFigure().getBounds();
    // }
    // current = (childBounds.x - tl.x) + (childBounds.y - tl.y);
    // if (current < minimum) {
    // minimum = current;
    // if (diaplay == false) {
    // closestPart = part;
    // } else {
    // closestPart = subPart;
    // }
    // }
    // }
    // if (closestPart != null)
    // navigateTo(closestPart, event);
    //
    // }
    //
    // @Override
    // protected List getNavigationSiblings() {
    // // TODO Auto-generated method stub
    // // return super.getNavigationSiblings();
    // NodePart part = null;
    // boolean display = true;
    // List list = new ArrayList();
    // SubjobContainerPart subPart = null;
    // EditPart focusPart = getFocusEditPart();
    // List subList = focusPart.getParent().getChildren();
    // for (int j = 0; j < subList.size(); j++) {
    // System.out.println("GGGGGGGGGGGGG" + subList.get(j).getClass());// NodePart//SubjobContainerPart
    // subPart = (SubjobContainerPart) subList.get(j);
    // SubjobContainer subContainer = (SubjobContainer) subPart.getModel();
    // if (subContainer.isDisplayed() == false) {
    // display = false;
    // }
    // }
    //
    // if (display == false) {
    // List nodejobList = focusPart.getChildren();
    // for (int i = 0; i < nodejobList.size(); i++) {
    // NodeContainerPart noteContain = (NodeContainerPart) nodejobList.get(i);
    // List nodeList = noteContain.getChildren();
    // part = (NodePart) nodeList.get(0);
    // list.add(part);
    // }
    // } else {
    // if (focusPart.getParent() != null)
    // return focusPart.getParent().getChildren();
    // list.add(focusPart);
    // }
    //
    // return list;
    // }

}
