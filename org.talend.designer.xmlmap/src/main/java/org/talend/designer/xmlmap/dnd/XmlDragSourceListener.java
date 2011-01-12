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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.dnd.AbstractTransferDragSourceListener;
import org.eclipse.gef.dnd.TemplateTransfer;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.Transfer;
import org.talend.designer.xmlmap.parts.OutputTreeNodeEditPart;
import org.talend.designer.xmlmap.parts.TreeNodeEditPart;

/**
 * wchen class global comment. Detailled comment
 */
public class XmlDragSourceListener extends AbstractTransferDragSourceListener {

    public XmlDragSourceListener(EditPartViewer viewer) {
        super(viewer, TemplateTransfer.getInstance());
    }

    public void dragStart(DragSourceEvent event) {
        Object template = getTemplate();
        if (template == null)
            event.doit = false;
        TemplateTransfer.getInstance().setTemplate(template);
    }

    protected List getTemplate() {
        List selection = getViewer().getSelectedEditParts();
        if (selection == null) {
            return null;
        }
        List toTransfer = new ArrayList();
        for (Object o : selection) {
            if (o instanceof TreeNodeEditPart && !(o instanceof OutputTreeNodeEditPart)) {
                TreeNodeEditPart nodePart = ((TreeNodeEditPart) o);
                if (nodePart.getModelChildren().isEmpty()) {
                    toTransfer.add(nodePart);
                } else {
                    return null;
                }
            } else {
                return null;
            }
        }

        return toTransfer;
    }

    public void dragSetData(DragSourceEvent event) {
        event.data = getTemplate();
    }

    @Override
    protected void setTransfer(Transfer xfer) {
        // TODO Auto-generated method stub
        super.setTransfer(xfer);
    }

}
