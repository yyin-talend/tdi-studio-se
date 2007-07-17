// ============================================================================
//
// Talend Community Edition
//
// Copyright (C) 2006-2007 Talend - www.talend.com
//
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either
// version 2.1 of the License.
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
package org.talend.designer.core.ui.editor;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.SelectionManager;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.talend.designer.core.ui.editor.connections.ConnLabelEditPart;
import org.talend.designer.core.ui.editor.connections.ConnectionPart;
import org.talend.designer.core.ui.editor.nodecontainer.NodeContainerPart;
import org.talend.designer.core.ui.editor.nodes.NodeLabelEditPart;
import org.talend.designer.core.ui.editor.process.ProcessPart;

/**
 * DOC nrousseau class global comment. Detailled comment <br/>
 * 
 */
public class TalendSelectionManager extends SelectionManager {

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.gef.SelectionManager#appendSelection(org.eclipse.gef.EditPart)
     */
    @Override
    public void appendSelection(EditPart arg0) {
        if (getSelection() instanceof StructuredSelection) {
            StructuredSelection selection = (StructuredSelection) getSelection();
            Object selected = null;
            for (Object element : selection.toArray()) {
                selected = element;
            }
            if (getSelection().isEmpty() || (selected instanceof ProcessPart)) {
                super.appendSelection(arg0);
            }

            if (!(arg0 instanceof NodeLabelEditPart) && !(arg0 instanceof ConnLabelEditPart)
                    && !(arg0 instanceof ConnectionPart)) {
                // removes old selections of labels by calling setSelection
                for (Object element : selection.toArray()) {
                    if (element instanceof NodeLabelEditPart) {
                        this.deselect(((NodeLabelEditPart) element));
                    } else if (element instanceof ConnLabelEditPart) {
                        this.deselect(((ConnLabelEditPart) element));
                    } else if (element instanceof ConnectionPart) {
                        this.deselect(((ConnectionPart) element));
                    }
                }

                super.appendSelection(arg0);
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
            StructuredSelection selection = (StructuredSelection) arg0;
            if (selection.size() != 1) {
                // if there is more than one element, remove all the selections of labels
                super.setSelection(filterSelection(selection));
            }
        } else {
            super.setSelection(arg0);
        }
    }

    private StructuredSelection filterSelection(StructuredSelection selection) {
        List newSelection = new ArrayList(selection.toList());
        for (Object element : selection.toArray()) {
            if (element instanceof NodeLabelEditPart) {
                newSelection.remove(element);
            } else if (element instanceof NodeContainerPart) {
                newSelection.remove(element);
            }
        }
        StructuredSelection newList = new StructuredSelection(newSelection);
        return newList;
    }

}
