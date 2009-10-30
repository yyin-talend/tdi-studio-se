// ============================================================================
//
// Copyright (C) 2006-2009 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.designer.core.ui.editor;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.SelectionManager;
import org.eclipse.gef.editparts.AbstractConnectionEditPart;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.talend.designer.core.ui.editor.connections.ConnLabelEditPart;
import org.talend.designer.core.ui.editor.connections.ConnectionPart;
import org.talend.designer.core.ui.editor.nodecontainer.NodeContainerPart;
import org.talend.designer.core.ui.editor.nodes.NodeLabelEditPart;
import org.talend.designer.core.ui.editor.nodes.NodePart;
import org.talend.designer.core.ui.editor.notes.NoteEditPart;
import org.talend.designer.core.ui.editor.process.ProcessPart;
import org.talend.designer.core.ui.editor.subjobcontainer.SubjobContainerPart;

/**
 * DOC nrousseau class global comment. Detailled comment <br/>
 * 
 */
public class TalendSelectionManager extends SelectionManager {

    private ETalendSelectionType selectionType;

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.gef.SelectionManager#appendSelection(org.eclipse.gef.EditPart)
     */
    @Override
    public void appendSelection(EditPart arg0) {
        // judge whether the refresh operation is executed.
        // see bug 3315.
        boolean needRefresh = false;

        if (getSelection() instanceof StructuredSelection) {
            StructuredSelection selection = (StructuredSelection) getSelection();
            Object selected = null;
            for (Object element : selection.toArray()) {
                selected = element;
            }
            if (getSelection().isEmpty() || (selected instanceof ProcessPart)) {
                this.selectionType = ETalendSelectionType.SINGLE;
                needRefresh = true;
            }
            if (!(arg0 instanceof NodeLabelEditPart) && !(arg0 instanceof ConnLabelEditPart) && !(arg0 instanceof ConnectionPart)) {
                // removes old selections of labels by calling setSelection
                for (Object element : selection.toArray()) {
                    if (element instanceof NodeLabelEditPart) {
                        this.deselect(((AbstractGraphicalEditPart) element));
                    } else if (element instanceof ConnLabelEditPart) {
                        this.deselect(((AbstractGraphicalEditPart) element));
                    } else if (element instanceof ConnectionPart) {
                        this.deselect(((AbstractConnectionEditPart) element));
                    }
                }
                needRefresh = true;
            }

            if (needRefresh) {
                super.appendSelection(arg0);
                needRefresh = false;
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.gef.SelectionManager#setSelection(org.eclipse.jface.viewers.ISelection)
     */
    @Override
    public void setSelection(ISelection arg0) {
        if (arg0 instanceof StructuredSelection) {
            StructuredSelection selection = filterSelection((StructuredSelection) arg0);
            if (selection.size() != 1) {
                // if there is more than one element, remove all the selections of labels
                if (selection.size() > 1) {
                    this.selectionType = ETalendSelectionType.MULTIPLE;
                } else {
                    this.selectionType = ETalendSelectionType.NONE;
                }
                super.setSelection(selection);
            } else {
                this.selectionType = ETalendSelectionType.SINGLE;
                super.setSelection(selection);
            }
        } else {
            this.selectionType = ETalendSelectionType.SINGLE;
            super.setSelection(arg0);
        }
    }

    private StructuredSelection filterSelection(StructuredSelection selection) {
        List newSelection = new ArrayList(selection.toList());
        for (Object element : selection.toArray()) {
            if (element instanceof SubjobContainerPart) {
                // childrens are NodeContainer part
                newSelection.remove(element);
                List<NodeContainerPart> nodeContainerParts = ((SubjobContainerPart) element).getChildren();
                for (NodeContainerPart nodeContainerPart : nodeContainerParts) {
                    for (Object object : nodeContainerPart.getChildren()) {
                        if (object instanceof NodePart) {
                            newSelection.add(object);
                        }
                    }
                }
            } else if (element instanceof NoteEditPart) {
                newSelection.add(element);
            } else if (!(element instanceof NodePart)) {
                newSelection.remove(element);
            }
        }
        StructuredSelection newList = new StructuredSelection(newSelection);
        return newList;
    }

    /**
     * yzhang Comment method "getSelectionType".
     * 
     * @return
     */
    public ETalendSelectionType getSelectionType() {
        return this.selectionType;
    }

}
