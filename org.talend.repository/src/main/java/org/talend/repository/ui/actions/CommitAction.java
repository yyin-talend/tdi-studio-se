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

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.exception.SystemException;
import org.talend.core.ui.images.EImage;
import org.talend.core.ui.images.ImageProvider;
import org.talend.repository.model.actions.CommitObjectAction;

/**
 * Action used to restore obects that had been logically deleted.<br/>
 * 
 * $Id: RestoreAction.java 531 2006-11-16 14:56:01 +0000 (jeu., 16 nov. 2006) smallet $
 * 
 */
public class CommitAction extends AContextualAction {

    public CommitAction() {
        super();
        this.setText("Commit");
        this.setToolTipText("Commit");
        this.setImageDescriptor(ImageProvider.getImageDesc(EImage.EXPORT_ICON));
    }

    public void run() {
        ISelection selection = getSelection();
        for (Object obj : ((IStructuredSelection) selection).toArray()) {
            try {
                CommitObjectAction.getInstance().execute(obj);
                refresh();
            } catch (SystemException e) {
                ExceptionHandler.process(e);
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.ui.actions.ITreeContextualAction#init(org.eclipse.jface.viewers.TreeViewer,
     * org.eclipse.jface.viewers.IStructuredSelection)
     */
    public void init(TreeViewer viewer, IStructuredSelection selection) {
        boolean canWork = !selection.isEmpty();

        CommitObjectAction commitObjectAction = CommitObjectAction.getInstance();
        for (Object o : ((IStructuredSelection) selection).toArray()) {
            if (canWork) {
                canWork = commitObjectAction.validateAction(o);
            }
        }
        setEnabled(canWork);
    }

}
