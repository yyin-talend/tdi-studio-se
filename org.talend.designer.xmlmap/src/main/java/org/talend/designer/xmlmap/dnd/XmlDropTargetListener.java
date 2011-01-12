// ============================================================================
//
// Copyright (C) 2006-2010 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.designer.xmlmap.dnd;

import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.Request;
import org.eclipse.gef.dnd.TemplateTransfer;
import org.eclipse.gef.dnd.TemplateTransferDropTargetListener;
import org.eclipse.gef.requests.CreateRequest;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.talend.designer.xmlmap.parts.OutputTreeNodeEditPart;

/**
 * wchen class global comment. Detailled comment
 */
public class XmlDropTargetListener extends TemplateTransferDropTargetListener {

    public XmlDropTargetListener(EditPartViewer viewer) {
        super(viewer);
    }

    @Override
    protected void updateTargetRequest() {
        ((CreateRequest) getTargetRequest()).setLocation(getDropLocation());
    }

    @Override
    protected Request createTargetRequest() {
        CreateNodeConnectionRequest request = new CreateNodeConnectionRequest(getTargetEditPart());

        request.setFactory(new NewNodeCreationFactory(TemplateTransfer.getInstance().getObject()));
        return request;
    }

    public void dragEnter(DropTargetEvent event) {
    }

    public void dragLeave(DropTargetEvent event) {

    }

    public void dragOperationChanged(DropTargetEvent event) {

    }

    @Override
    public void dragOver(DropTargetEvent event) {
        super.dragOver(event);
        Object transferedObj = TemplateTransfer.getInstance().getObject();
        if (transferedObj == null) {
            event.detail = DND.DROP_NONE;
        } else {
            if (!(getTargetEditPart() instanceof OutputTreeNodeEditPart)) {
                event.detail = DND.DROP_NONE;
            }
        }
    }

    @Override
    protected void handleDrop() {
        super.handleDrop();
    }

}
