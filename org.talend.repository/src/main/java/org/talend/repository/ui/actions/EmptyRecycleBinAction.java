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
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Shell;
import org.talend.core.model.metadata.builder.connection.Connection;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.IRepositoryObject;
import org.talend.core.ui.images.EImage;
import org.talend.core.ui.images.ImageProvider;
import org.talend.repository.i18n.Messages;
import org.talend.repository.model.ProxyRepositoryFactory;
import org.talend.repository.model.RepositoryNode;
import org.talend.repository.model.RepositoryNode.EProperties;
import org.talend.repository.ui.views.RepositoryContentProvider.MetadataTableRepositoryObject;

/**
 * Action used to empty the recycle bin.<br/>
 * 
 * $Id$
 * 
 */
public class EmptyRecycleBinAction extends AContextualAction {

    public EmptyRecycleBinAction() {
        super();
        this.setText(Messages.getString("EmptyRecycleBinAction.action.title"));
        this.setToolTipText(Messages.getString("EmptyRecycleBinAction.action.toolTipText"));
        this.setImageDescriptor(ImageProvider.getImageDesc(EImage.RECYCLE_BIN_EMPTY_ICON));
    }

    public void run() {
        ISelection selection = getSelection();
        Object obj = ((IStructuredSelection) selection).getFirstElement();
        RepositoryNode node = (RepositoryNode) obj;

        String title = Messages.getString("EmptyRecycleBinAction.dialog.title");
        String message = Messages.getString("EmptyRecycleBinAction.dialog.message1") + "\n"
                + Messages.getString("EmptyRecycleBinAction.dialog.message2");
        if (!(MessageDialog.openQuestion(new Shell(), title, message))) {
            return;
        }

        ProxyRepositoryFactory factory = ProxyRepositoryFactory.getInstance();
        for (RepositoryNode child : node.getChildren()) {
            IRepositoryObject objToDelete = child.getObject();
            try {
                if (objToDelete instanceof MetadataTableRepositoryObject) {
                    MetadataTableRepositoryObject metadataTableRepositoryObject = (MetadataTableRepositoryObject) objToDelete;
                    Connection connection = metadataTableRepositoryObject.getTable().getConnection();
                    connection.getTables().remove(metadataTableRepositoryObject.getTable());
                    factory.save(metadataTableRepositoryObject.getProperty().getItem());
                } else {
                    factory.deleteObjectPhysical(objToDelete);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        refresh();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.ui.actions.ITreeContextualAction#init(org.eclipse.jface.viewers.TreeViewer,
     * org.eclipse.jface.viewers.IStructuredSelection)
     */
    public void init(TreeViewer viewer, IStructuredSelection selection) {
        boolean canWork = !selection.isEmpty() && selection.size() == 1;
        if (canWork) {
            Object o = selection.getFirstElement();
            RepositoryNode node = (RepositoryNode) o;
            switch (node.getType()) {
            case STABLE_SYSTEM_FOLDER:
                ERepositoryObjectType type = (ERepositoryObjectType) node.getProperties(EProperties.CONTENT_TYPE);
                if (type != ERepositoryObjectType.RECYCLE_BIN || !node.hasChildren()) {
                    canWork = false;
                }
                break;
            default:
                canWork = false;
                break;
            }
        }
        setEnabled(canWork);
    }

}
