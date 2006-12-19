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

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.util.LocalSelectionTransfer;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Shell;
import org.talend.commons.ui.image.EImage;
import org.talend.commons.ui.image.ImageProvider;
import org.talend.repository.i18n.Messages;
import org.talend.repository.model.RepositoryNode;
import org.talend.repository.model.actions.CopyObjectAction;

/**
 * DOC smallet class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 */
public class PasteAction extends AContextualAction {

    public PasteAction() {
        super();
        this.setText("Paste");
        this.setImageDescriptor(ImageProvider.getImageDesc(EImage.PASTE_ICON));
        this.setActionDefinitionId("pasteItem");
    }

    @Override
    public void run() {
        IStructuredSelection selection = (IStructuredSelection) getSelection();
        RepositoryNode target = (RepositoryNode) selection.getFirstElement();

        CopyObjectAction copyObjectAction = CopyObjectAction.getInstance();

        TreeSelection selectionInClipboard = (TreeSelection) LocalSelectionTransfer.getTransfer().getSelection();
        if (selectionInClipboard != null) {
            for (Object currentSource : selectionInClipboard.toArray()) {
                try {
                    if (copyObjectAction.validateAction((RepositoryNode) currentSource, target)) {
                        copyObjectAction.execute((RepositoryNode) currentSource, target);
                    } else {
                        MessageDialog.openWarning(new Shell(), Messages.getString("PasteObjectAction.error.title"), Messages
                                .getString("PasteObjectAction.error.labelAlreadyExists"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            refresh();
        }
    }

    public void init(TreeViewer viewer, IStructuredSelection selection) {
        boolean enabled = true;
        RepositoryNode target = (RepositoryNode) selection.getFirstElement();
        TreeSelection selectionInClipboard = (TreeSelection) LocalSelectionTransfer.getTransfer().getSelection();

        if (selectionInClipboard != null) {
            for (Object obj : ((StructuredSelection) selectionInClipboard).toArray()) {
                if (enabled) {
                    RepositoryNode sourceNode = (RepositoryNode) obj;

                    if (!CopyObjectAction.getInstance().validateAction(sourceNode, target)) {
                        visible = true;
                        enabled = false;
                    } else {
                        visible = true;
                        enabled = true;
                    }
                }
            }
        } else {
            enabled = false;
            visible = false;
        }
        setEnabled(enabled);
    }

    private boolean visible;

    /**
     * Getter for visible.
     * 
     * @return the visible
     */
    public boolean isVisible() {
        return this.visible;
    }

    /**
     * Sets the visible.
     * 
     * @param visible the visible to set
     */
    public void setVisible(boolean visible) {
        this.visible = visible;
    }
}
