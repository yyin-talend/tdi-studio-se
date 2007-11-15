// ============================================================================
//
// Copyright (C) 2006-2007 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//   
// ============================================================================
package org.talend.repository.ui.actions;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.ui.image.EImage;
import org.talend.commons.ui.image.ImageProvider;
import org.talend.core.model.metadata.builder.connection.AbstractMetadataObject;
import org.talend.core.model.metadata.builder.connection.MetadataTable;
import org.talend.core.model.metadata.builder.connection.SubItemHelper;
import org.talend.core.model.metadata.builder.connection.TableHelper;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.IRepositoryObject;
import org.talend.repository.i18n.Messages;
import org.talend.repository.model.ERepositoryStatus;
import org.talend.repository.model.IProxyRepositoryFactory;
import org.talend.repository.model.ProxyRepositoryFactory;
import org.talend.repository.model.RepositoryNode;
import org.talend.repository.model.RepositoryNode.EProperties;
import org.talend.repository.model.actions.RestoreObjectAction;
import org.talend.repository.ui.views.RepositoryContentProvider.ISubRepositoryObject;
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
        //used to store the database connection object that are used to notify the sqlBuilder.
        List<IRepositoryObject> connections = new ArrayList<IRepositoryObject>();
        ISelection selection = getSelection();
        for (Object obj : ((IStructuredSelection) selection).toArray()) {
            if (obj instanceof RepositoryNode) {
                try {
                    RepositoryNode node = (RepositoryNode) obj;
                    ERepositoryObjectType nodeType = (ERepositoryObjectType) (node).getProperties(EProperties.CONTENT_TYPE);
                    if (nodeType.isSubItem()) {
                        IProxyRepositoryFactory factory = ProxyRepositoryFactory.getInstance();
                        ConnectionItem item = (ConnectionItem) node.getObject().getProperty().getItem();
                        AbstractMetadataObject abstractMetadataObject = ((ISubRepositoryObject) node.getObject()).getAbstractMetadataObject();
                        SubItemHelper.setDeleted(abstractMetadataObject, false);
                        factory.save(item);
                        connections.add(node.getObject());
                    } else {
                        RestoreObjectAction restoreObjectAction = RestoreObjectAction.getInstance();
                        restoreObjectAction.execute(node, null);
                    }
                    refresh();
                } catch (Exception e) {
                    ExceptionHandler.process(e);
                }
            }
        }
        notifySQLBuilder(connections);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.ui.actions.ITreeContextualAction#init(org.eclipse.jface.viewers.TreeViewer,
     * org.eclipse.jface.viewers.IStructuredSelection)
     */
    public void init(TreeViewer viewer, IStructuredSelection selection) {
        boolean canWork = !selection.isEmpty();
        if (ProxyRepositoryFactory.getInstance().isUserReadOnlyOnCurrentProject()) {
            canWork = false;
        }
        RestoreObjectAction restoreObjectAction = RestoreObjectAction.getInstance();
        for (Object o : ((IStructuredSelection) selection).toArray()) {
            if (canWork) {
                if (o instanceof RepositoryNode) {
                    RepositoryNode node = (RepositoryNode) o;
                    ERepositoryObjectType nodeType = (ERepositoryObjectType) node.getProperties(EProperties.CONTENT_TYPE);
                    canWork = restoreObjectAction.validateAction(node, null);    
                }
            }
        }
        setEnabled(canWork);
    }

}
