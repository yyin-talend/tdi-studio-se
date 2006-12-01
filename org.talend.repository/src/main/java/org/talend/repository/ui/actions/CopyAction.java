// ============================================================================
//
// Talend Community Edition
//
// Copyright (C) 2006 Talend - www.talend.com
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
package org.talend.repository.ui.actions;

import org.eclipse.jface.util.LocalSelectionTransfer;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.talend.core.ui.images.EImage;
import org.talend.core.ui.images.ImageProvider;
import org.talend.repository.model.RepositoryNode;
import org.talend.repository.model.actions.CopyObjectAction;

/**
 * DOC smallet class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 */
public class CopyAction extends AContextualAction {

    public CopyAction() {
        super();
        this.setText("Copy");
        this.setImageDescriptor(ImageProvider.getImageDesc(EImage.COPY_ICON));
        this.setActionDefinitionId("copyItem");
    }

    @Override
    public void run() {
        IStructuredSelection selection = (IStructuredSelection) getSelection();
        LocalSelectionTransfer.getTransfer().setSelection(selection);
        LocalSelectionTransfer.getTransfer().setSelectionSetTime(System.currentTimeMillis());
        refresh();
    }

    public void init(TreeViewer viewer, IStructuredSelection selection) {
        boolean canWork = true;
        for (Object obj : ((StructuredSelection) selection).toArray()) {
            if (canWork) {
                RepositoryNode sourceNode = (RepositoryNode) obj;

                if (!CopyObjectAction.getInstance().validateAction(sourceNode, null)) {
                    canWork = false;
                }
            }
        }
        setEnabled(canWork);
    }
}
