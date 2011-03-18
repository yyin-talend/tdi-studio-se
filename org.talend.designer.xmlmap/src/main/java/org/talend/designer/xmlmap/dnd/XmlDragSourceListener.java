// ============================================================================
//
// Copyright (C) 2006-2011 Talend Inc. - www.talend.com
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
import org.talend.designer.xmlmap.parts.InputXmlTreeEditPart;
import org.talend.designer.xmlmap.parts.OutputTreeNodeEditPart;
import org.talend.designer.xmlmap.parts.OutputXmlTreeEditPart;
import org.talend.designer.xmlmap.parts.TreeNodeEditPart;
import org.talend.designer.xmlmap.parts.VarNodeEditPart;

/**
 * wchen class global comment. Detailled comment
 */
public class XmlDragSourceListener extends AbstractTransferDragSourceListener {

    public XmlDragSourceListener(EditPartViewer viewer) {
        super(viewer, TemplateTransfer.getInstance());
    }

    public void dragStart(DragSourceEvent event) {
        Object template = getTemplate();
        TemplateTransfer.getInstance().setTemplate(template);
    }

    protected TransferedObject getTemplate() {
        List selection = getViewer().getSelectedEditParts();
        if (selection == null || selection.isEmpty()) {
            return null;
        }
        TransferedObject object = null;

        Object lastSelection = getViewer().getSelectedEditParts().get(getViewer().getSelectedEditParts().size() - 1);

        TransferdType type = null;

        List toTransfer = new ArrayList();
        for (Object o : selection) {
            // all selected parts in the same zone should be in the same tree , or clean toTransfer list can't drag
            if (lastSelection instanceof OutputTreeNodeEditPart) {
                type = TransferdType.OUTPUT;
                // can't drag if there is a tree selection in the same zone
                if (o instanceof OutputXmlTreeEditPart) {
                    toTransfer.clear();
                    break;
                }
                if (o instanceof OutputTreeNodeEditPart) {
                    OutputTreeNodeEditPart nodePart = (OutputTreeNodeEditPart) o;
                    if (nodePart.getModelChildren().isEmpty()) {
                        toTransfer.add(o);
                    } else {
                        toTransfer.clear();
                        break;
                    }
                }

            } else if (lastSelection instanceof TreeNodeEditPart) {
                type = TransferdType.INPUT;
                if (o instanceof InputXmlTreeEditPart) {
                    toTransfer.clear();
                    break;
                }
                if (o instanceof TreeNodeEditPart) {
                    TreeNodeEditPart nodePart = (TreeNodeEditPart) o;
                    if (nodePart.getModelChildren().isEmpty()) {
                        toTransfer.add(o);
                    } else {
                        toTransfer.clear();
                        break;
                    }
                }

            } else if (lastSelection instanceof VarNodeEditPart) {
                type = TransferdType.VAR;
                if (o instanceof VarNodeEditPart) {
                    toTransfer.add(o);
                }

            }

        }

        if (toTransfer.isEmpty()) {
            object = new TransferedObject(null, null);
        } else {
            object = new TransferedObject(toTransfer, type);
        }

        return object;
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
