// ============================================================================
//
// Copyright (C) 2006-2007 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the  agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//   
// ============================================================================
package org.talend.repository.ui.actions.metadata;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Shell;
import org.talend.commons.exception.PersistenceException;
import org.talend.commons.ui.image.EImage;
import org.talend.commons.ui.image.ImageProvider;
import org.talend.core.model.metadata.builder.connection.AbstractMetadataObject;
import org.talend.core.model.metadata.builder.connection.Connection;
import org.talend.core.model.metadata.builder.connection.SubItemHelper;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.IRepositoryObject;
import org.talend.repository.i18n.Messages;
import org.talend.repository.model.IProxyRepositoryFactory;
import org.talend.repository.model.ProxyRepositoryFactory;
import org.talend.repository.model.RepositoryNode;
import org.talend.repository.model.RepositoryNode.ENodeType;
import org.talend.repository.model.RepositoryNode.EProperties;
import org.talend.repository.ui.actions.AContextualAction;
import org.talend.repository.ui.views.RepositoryContentProvider.ISubRepositoryObject;

/**
 * DOC tguiu class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 */
public class DeleteTableAction extends AContextualAction {

    private static final String DELETE_LOGICAL_TITLE = Messages.getString("DeleteAction.action.logicalTitle"); //$NON-NLS-1$

    private static final String DELETE_FOREVER_TITLE = Messages.getString("DeleteAction.action.foreverTitle"); //$NON-NLS-1$

    private static final String DELETE_LOGICAL_TOOLTIP = Messages.getString("DeleteAction.action.logicalToolTipText"); //$NON-NLS-1$

    private static final String DELETE_FOREVER_TOOLTIP = Messages.getString("DeleteAction.action.logicalToolTipText"); //$NON-NLS-1$

    public DeleteTableAction() {
        super();
        this.setImageDescriptor(ImageProvider.getImageDesc(EImage.DELETE_ICON));
    }

    @Override
    public void run() {
        ISelection selection = getSelection();

        Boolean confirm = null;

        //used to store the database connection object that are used to notify the sqlBuilder.
        List<IRepositoryObject> connections = new ArrayList<IRepositoryObject>();
        
        for (Object obj : ((IStructuredSelection) selection).toArray()) {
            if (obj instanceof RepositoryNode) {
                RepositoryNode node = (RepositoryNode) obj;
                ERepositoryObjectType nodeType = (ERepositoryObjectType) node.getProperties(EProperties.CONTENT_TYPE);
                if (node.getType() == ENodeType.REPOSITORY_ELEMENT && nodeType.isSubItem()) {
                    try {
                        Connection connection = null;
                        ERepositoryObjectType parentNodeType = (ERepositoryObjectType) node.getParent().getProperties(
                                EProperties.CONTENT_TYPE);
                        IProxyRepositoryFactory factory = ProxyRepositoryFactory.getInstance();
                        ConnectionItem item = (ConnectionItem) node.getObject().getProperty().getItem();
                        connection = (item).getConnection();
                        ISubRepositoryObject subRepositoryObject = (ISubRepositoryObject) node.getObject();
                        AbstractMetadataObject abstractMetadataObject = subRepositoryObject.getAbstractMetadataObject();
                        if (SubItemHelper.isDeleted(abstractMetadataObject)) {
                            if (confirm == null) {
                                String title = Messages.getString("DeleteAction.dialog.title"); //$NON-NLS-1$
                                String message = Messages.getString("DeleteAction.dialog.message1") + "\n" //$NON-NLS-1$ //$NON-NLS-2$
                                        + Messages.getString("DeleteAction.dialog.message2"); //$NON-NLS-1$
                                confirm = (MessageDialog.openQuestion(new Shell(), title, message));
                            }
                            if (confirm) {
                                subRepositoryObject.removeFromParent();
                            }
                        } else {
                            SubItemHelper.setDeleted(abstractMetadataObject, true);
                        }
                        factory.save(item);
                        connections.add(node.getObject());
                        refresh();
                    } catch (PersistenceException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        notifySQLBuilder(connections);
        // IViewPart viewPart = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().findView(
        // RepositoryView.VIEW_ID);
        // IRepositoryView repositoryView = (IRepositoryView) viewPart;

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
        for (Object o : (selection).toArray()) {
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
                    if (!nodeType.isSubItem()) {
                        canWork = false;
                        break;
                    }
                    IProxyRepositoryFactory repFactory = ProxyRepositoryFactory.getInstance();
                    boolean isLocked = false;
                    boolean isDeleted = false;
                    try {
                        isLocked = !repFactory.isPotentiallyEditable(repObj);
                        isDeleted = DeletionHelper.isDeleted(node);
                    } catch (PersistenceException e) {
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
