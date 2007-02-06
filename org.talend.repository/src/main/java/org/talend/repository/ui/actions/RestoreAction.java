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
import org.talend.commons.ui.image.EImage;
import org.talend.commons.ui.image.ImageProvider;
import org.talend.core.model.metadata.builder.connection.MetadataTable;
import org.talend.core.model.metadata.builder.connection.TableHelper;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.repository.i18n.Messages;
import org.talend.repository.model.IProxyRepositoryFactory;
import org.talend.repository.model.ProxyRepositoryFactory;
import org.talend.repository.model.RepositoryNode;
import org.talend.repository.model.RepositoryNode.EProperties;
import org.talend.repository.model.actions.RestoreObjectAction;
import org.talend.repository.ui.views.RepositoryContentProvider.MetadataTableRepositoryObject;

/**
 * Action used to restore obects that had been logically deleted.<br/>
 * 
 * $Id$
 * 
 */
public class RestoreAction extends AContextualAction {

    public RestoreAction() {
        super();
        this.setText(Messages.getString("RestoreAction.action.title")); //$NON-NLS-1$
        this.setToolTipText(Messages.getString("RestoreAction.action.toolTipText")); //$NON-NLS-1$
        this.setImageDescriptor(ImageProvider.getImageDesc(EImage.RESTORE_ICON));
        this.setActionDefinitionId("restoreItem"); //$NON-NLS-1$
    }

    public void run() {
        ISelection selection = getSelection();
        for (Object obj : ((IStructuredSelection) selection).toArray()) {
            if (obj instanceof RepositoryNode) {
                try {
                    RepositoryNode node = (RepositoryNode) obj;
                    ERepositoryObjectType nodeType = (ERepositoryObjectType) (node).getProperties(EProperties.CONTENT_TYPE);
                    if (ERepositoryObjectType.METADATA_CON_TABLE.equals(nodeType)) {
                        IProxyRepositoryFactory factory = ProxyRepositoryFactory.getInstance();
                        ConnectionItem item = (ConnectionItem) node.getObject().getProperty().getItem();
                        MetadataTable metadataTable = ((MetadataTableRepositoryObject) node.getObject()).getTable();
                        TableHelper.setDeleted(metadataTable, false);
                        factory.save(item);
                    } else {
                        RestoreObjectAction restoreObjectAction = RestoreObjectAction.getInstance();
                        restoreObjectAction.execute(node, null);
                    }
                    refresh();
                } catch (Exception e) {
                    e.printStackTrace();
                }
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

        RestoreObjectAction restoreObjectAction = RestoreObjectAction.getInstance();
        for (Object o : ((IStructuredSelection) selection).toArray()) {
            if (canWork) {
                if (o instanceof RepositoryNode) {
                    canWork = restoreObjectAction.validateAction((RepositoryNode) o, null);
                }
            }
        }
        setEnabled(canWork);
    }

}
