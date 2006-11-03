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

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.PlatformUI;
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
import org.talend.repository.ui.views.IRepositoryView;
import org.talend.repository.ui.views.RepositoryView;
import org.talend.repository.ui.views.RepositoryContentProvider.MetadataTableRepositoryObject;

/**
 * DOC tguiu class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 */
public class DeleteTableAction extends AContextualAction {

    private static final String DELETE_LOGICAL_TITLE = Messages.getString("DeleteAction.action.logicalTitle");

    private static final String DELETE_FOREVER_TITLE = Messages.getString("DeleteAction.action.foreverTitle");

    private static final String DELETE_LOGICAL_TOOLTIP = Messages.getString("DeleteAction.action.logicalToolTipText");

    private static final String DELETE_FOREVER_TOOLTIP = Messages.getString("DeleteAction.action.logicalToolTipText");

    public DeleteTableAction() {
        super();
        this.setImageDescriptor(ImageProvider.getImageDesc(EImage.DELETE_ICON));
    }

    public void run() {
        ISelection selection = getSelection();

        Boolean confirm = null;

        for (Object obj : ((IStructuredSelection) selection).toArray()) {
            if (obj instanceof RepositoryNode) {
                RepositoryNode node = (RepositoryNode) obj;
                ERepositoryObjectType nodeType = (ERepositoryObjectType) node.getProperties(EProperties.CONTENT_TYPE);
                if (node.getType() == ENodeType.REPOSITORY_ELEMENT && ERepositoryObjectType.METADATA_CON_TABLE.equals(nodeType)) {
                    try {
                        Connection connection = null;
                        ERepositoryObjectType parentNodeType = (ERepositoryObjectType) node.getParent().getProperties(
                                EProperties.CONTENT_TYPE);
                        IRepositoryFactory factory = RepositoryFactoryProvider.getInstance(getViewPart().getRepositoryContext());
                        ConnectionItem item = (ConnectionItem) node.getObject().getProperty().getItem();
                        connection = (Connection) (item).getConnection();
                        MetadataTable metadataTable = ((MetadataTableRepositoryObject) node.getObject()).getTable();
                        if (TableHelper.isDeleted(metadataTable)) {
                            if (confirm == null) {
                                String title = Messages.getString("DeleteAction.dialog.title");
                                String message = Messages.getString("DeleteAction.dialog.message1") + "\n"
                                        + Messages.getString("DeleteAction.dialog.message2");
                                confirm = (MessageDialog.openQuestion(new Shell(), title, message));
                            }
                            if (confirm)
                                connection.getTables().remove(metadataTable);
                        } else
                            TableHelper.setDeleted(metadataTable, true);
                        factory.save(item);
                        refresh();
                    } catch (PersistenceException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        }
        IViewPart viewPart = (IViewPart) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().findView(
                RepositoryView.VIEW_ID);
        IRepositoryView repositoryView = (IRepositoryView) viewPart;

        // // Find Metadata node
        // RepositoryNode recycleBinNode = repositoryView.getRoot().getChildren().get(8);
        //
        // // Force focus to the repository View ans erase the current user selection
        // viewPart.setFocus();
        // repositoryView.getViewSite().getSelectionProvider().setSelection(null);
        // repositoryView.expand(recycleBinNode, true);

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.ui.actions.ITreeContextualAction#init(org.eclipse.jface.viewers.TreeViewer,
     * org.eclipse.jface.viewers.IStructuredSelection)
     */
    public void init(TreeViewer viewer, IStructuredSelection selection) {
        boolean canWork = !selection.isEmpty();
        setText(null);
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

                    ERepositoryObjectType nodeType = (ERepositoryObjectType) node.getProperties(EProperties.CONTENT_TYPE);
                    if (!ERepositoryObjectType.METADATA_CON_TABLE.equals(nodeType)) {
                        canWork = false;
                        break;
                    }
                    IRepositoryFactory repFactory = RepositoryFactoryProvider.getInstance(getViewPart().getRepositoryContext());
                    boolean isLocked = false;
                    boolean isDeleted = false;
                    try {
                        isLocked = repFactory.isLocked(repObj);
                        isDeleted = DeletionHelper.isDeleted(repFactory, node);
                    } catch (PersistenceException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    if (isLocked) {
                        canWork = false;
                    } else if (isDeleted) {
                        if (getText() == null || DELETE_FOREVER_TITLE.equals(getText())) {
                            this.setText(DELETE_FOREVER_TITLE);
                            this.setToolTipText(DELETE_FOREVER_TOOLTIP);
                        } else {
                            canWork = false;
                        }
                    } else {
                        setText(DELETE_LOGICAL_TITLE);
                        setToolTipText(DELETE_LOGICAL_TOOLTIP);
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
