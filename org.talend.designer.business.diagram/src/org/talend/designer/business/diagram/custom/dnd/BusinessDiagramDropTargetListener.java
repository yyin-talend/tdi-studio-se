// ============================================================================
//
// Talend Community Edition
//
// Copyright (C) 2006 Talend – www.talend.com
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
package org.talend.designer.business.diagram.custom.dnd;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.gef.EditPartViewer;
import org.eclipse.gmf.runtime.diagram.ui.parts.DiagramDropTargetListener;
import org.eclipse.jface.util.LocalSelectionTransfer;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.TransferData;
import org.talend.designer.business.diagram.custom.util.GmfPropertiesViewHelper;

/**
 * DOC mhelleboid class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 */
public class BusinessDiagramDropTargetListener extends DiagramDropTargetListener {

    private static final String ASSIGNMENT_TAB_ID = "org.talend.designer.business.diagram.properties.AssignmentTab";

    /**
     * DOC mhelleboid BusinessDiagramDropTargetListener constructor comment.
     * 
     * @param viewer
     */
    public BusinessDiagramDropTargetListener(EditPartViewer viewer) {
        super(viewer, LocalSelectionTransfer.getTransfer());
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.gmf.runtime.diagram.ui.parts.DiagramDropTargetListener#getObjectsBeingDropped()
     */
    @Override
    protected List getObjectsBeingDropped() {
        TransferData[] data = getCurrentEvent().dataTypes;
        List eObjects = new ArrayList();

        for (int i = 0; i < data.length; i++) {
            if (LocalSelectionTransfer.getTransfer().isSupportedType(data[i])) {

                Object nativeToJava = LocalSelectionTransfer.getTransfer().nativeToJava(data[i]);

                if (nativeToJava instanceof IStructuredSelection) {
                    IStructuredSelection structuredSelection = (IStructuredSelection) nativeToJava;
                    eObjects.addAll(structuredSelection.toList());
                }
            }
        }

        return eObjects;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.gmf.runtime.diagram.ui.parts.DiagramDropTargetListener#handleDrop()
     */
    @Override
    protected void handleDrop() {
        super.handleDrop();
        // hack to show Assignment Tab in Properties View
        if (getCurrentEvent().detail != DND.DROP_NONE) {
            new GmfPropertiesViewHelper().showTab(ASSIGNMENT_TAB_ID, getViewer(), getTargetEditPart());
        }
    }

    // /*
    // * (non-Javadoc)
    // *
    // * @see
    // org.eclipse.gmf.runtime.diagram.ui.parts.DiagramDropTargetListener#isEnabled(org.eclipse.swt.dnd.DropTargetEvent)
    // */
    // @Override
    // public boolean isEnabled(DropTargetEvent event) {
    // super.isEnabled(event);
    // return true;
    // }
    //
    // /*
    // * (non-Javadoc)
    // *
    // * @see org.eclipse.gmf.runtime.diagram.ui.parts.DiagramDropTargetListener#updateTargetEditPart()
    // */
    // @Override
    // protected void updateTargetEditPart() {
    // setTargetEditPart(otherCalculateTargetEditPart());
    // }
    //
    // private EditPart otherCalculateTargetEditPart() {
    // return getViewer().findObjectAt(getDropLocation());
    // }
}
