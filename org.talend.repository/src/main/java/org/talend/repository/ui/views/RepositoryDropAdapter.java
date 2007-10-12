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
package org.talend.repository.ui.views;

import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.TransferData;
import org.eclipse.ui.part.PluginDropAdapter;
import org.talend.commons.exception.BusinessException;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.exception.MessageBoxExceptionHandler;
import org.talend.repository.model.RepositoryNode;
import org.talend.repository.model.actions.CopyObjectAction;
import org.talend.repository.model.actions.MoveObjectAction;

/**
 * DOC smallet class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 */
public class RepositoryDropAdapter extends PluginDropAdapter {

    public RepositoryDropAdapter(StructuredViewer viewer) {
        super(viewer);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.part.PluginDropAdapter#performDrop(java.lang.Object)
     */
    @Override
    public boolean performDrop(Object data) {
        int operation = getCurrentOperation();
        RepositoryNode targetNode = (RepositoryNode) getCurrentTarget();
        boolean toReturn = true;

        for (Object obj : ((StructuredSelection) data).toArray()) {
            RepositoryNode sourceNode = (RepositoryNode) obj;
            try {
                switch (operation) {
                case DND.DROP_COPY:
                    CopyObjectAction.getInstance().execute(sourceNode, targetNode);
                    break;
                case DND.DROP_MOVE:
                    MoveObjectAction.getInstance().execute(sourceNode, targetNode);
                    break;
                default:
                    // Nothing to do
                }
            } catch (BusinessException e) {
                MessageBoxExceptionHandler.process(e);
            } catch (Exception e) {
                ExceptionHandler.process(e);
            }
        }
        return toReturn;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.part.PluginDropAdapter#validateDrop(java.lang.Object, int, org.eclipse.swt.dnd.TransferData)
     */
    @Override
    public boolean validateDrop(Object target, int operation, TransferData transferType) {
        super.validateDrop(target, operation, transferType);

        boolean isValid = true;
        for (Object obj : ((StructuredSelection) getViewer().getSelection()).toArray()) {
            RepositoryNode sourceNode = (RepositoryNode) obj;
            switch (operation) {
            case DND.DROP_COPY:
                isValid = CopyObjectAction.getInstance().validateAction(sourceNode, (RepositoryNode) target);
                break;
            case DND.DROP_NONE:
            case DND.DROP_MOVE:
                isValid = MoveObjectAction.getInstance().validateAction(sourceNode, (RepositoryNode) target);
                break;
            case DND.DROP_DEFAULT:
            default:
                isValid = false;
            }

            if (!isValid) {
                return isValid;
            }
        }

        return isValid;
    }

}
