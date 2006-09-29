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
package org.talend.repository.ui.actions.folder;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.talend.core.ui.ImageProvider;
import org.talend.core.ui.ImageProvider.EImage;
import org.talend.repository.i18n.Messages;
import org.talend.repository.model.RepositoryNode;
import org.talend.repository.ui.actions.AContextualAction;

/**
 * Action used to rename a folder in repository.<br/>
 * 
 * $Id$
 * 
 */
public class RenameFolderAction extends AContextualAction {

    public RenameFolderAction() {
        super();

        this.setText(Messages.getString("RenameFolderAction.action.title")); //$NON-NLS-1$
        this.setToolTipText(Messages.getString("RenameFolderAction.action.message")); //$NON-NLS-1$
        this.setImageDescriptor(ImageProvider.getImageDesc(EImage.FOLDER_CLOSE_ICON));
    }

    public void run() {
        // PTODO SML
    }

    public void init(TreeViewer viewer, IStructuredSelection selection) {
        boolean canWork = !selection.isEmpty() && selection.size() == 1;
        if (canWork) {
            Object o = selection.getFirstElement();
            RepositoryNode node = (RepositoryNode) o;
            switch (node.getType()) {
            case SIMPLE_FOLDER:
                // Nothing to do
                break;
            default:
                canWork = false;
            }
        }
        setEnabled(canWork);
    }
}
