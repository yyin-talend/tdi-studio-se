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
package org.talend.repository.ui.actions.metadata;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.talend.commons.exception.PersistenceException;
import org.talend.core.model.metadata.builder.connection.Connection;
import org.talend.core.model.metadata.builder.connection.MetadataTable;
import org.talend.core.model.metadata.builder.connection.TableHelper;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.IRepositoryObject;
import org.talend.core.ui.ImageProvider;
import org.talend.core.ui.ImageProvider.EImage;
import org.talend.repository.i18n.Messages;
import org.talend.repository.model.IRepositoryFactory;
import org.talend.repository.model.RepositoryFactoryProvider;
import org.talend.repository.model.RepositoryNode;
import org.talend.repository.model.RepositoryNode.ENodeType;
import org.talend.repository.model.RepositoryNode.EProperties;
import org.talend.repository.ui.actions.AContextualAction;
import org.talend.repository.ui.views.RepositoryContentProvider.MetadataTableRepositoryObject;

/**
 * DOC smallet class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 */
public class RestoreTableAction extends AContextualAction {

    public RestoreTableAction() {
        super();
        this.setText(Messages.getString("RestoreAction.action.title")); //$NON-NLS-1$
        this.setToolTipText(Messages.getString("RestoreAction.action.toolTipText")); //$NON-NLS-1$
        this.setImageDescriptor(ImageProvider.getImageDesc(EImage.RESTORE_ICON));
    }

    public void run() {
        ISelection selection = getSelection();
        for (Object obj : ((IStructuredSelection) selection).toArray()) {
            if (obj instanceof RepositoryNode) {
                RepositoryNode node = (RepositoryNode) obj;
                if (node.getType() == ENodeType.REPOSITORY_ELEMENT) {
                    try {
                        Connection connection = null;
                        ERepositoryObjectType parentNodeType = (ERepositoryObjectType) node.getParent().getProperties(
                                EProperties.CONTENT_TYPE);
                        IRepositoryFactory factory = RepositoryFactoryProvider.getInstance(getViewPart().getRepositoryContext());
                        ConnectionItem item = (ConnectionItem) node.getObject().getProperty().getItem();
                        connection = (Connection) (item).getConnection();
                        MetadataTable metadataTable = ((MetadataTableRepositoryObject) node.getObject()).getTable();
                        TableHelper.setDeleted(metadataTable, false);
                        factory.save(item);
                    } catch (PersistenceException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
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
        boolean canWork = !selection.isEmpty();

        for (Object o : ((IStructuredSelection) selection).toArray()) {
            if (canWork) {
                RepositoryNode node = (RepositoryNode) o;
                switch (node.getType()) {
                case STABLE_SYSTEM_FOLDER:
                case SYSTEM_FOLDER:
                case SIMPLE_FOLDER:
                    canWork = false;
                    break;
                case REPOSITORY_ELEMENT:
                    IRepositoryObject repObj = node.getObject();
                    IRepositoryFactory repFactory = RepositoryFactoryProvider.getInstance(getViewPart().getRepositoryContext());
                    ERepositoryObjectType nodeType = (ERepositoryObjectType) node.getProperties(EProperties.CONTENT_TYPE);
                    try {
                        if (!ERepositoryObjectType.METADATA_CON_TABLE.equals(nodeType)
                                || !DeletionHelper.isDeleted(repFactory, node)) {
                            canWork = false;
                        }
                    } catch (PersistenceException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    break;
                default:
                    // Nothing to do
                    break;
                }
            }
        }
        setEnabled(canWork);
    }

}