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
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.talend.commons.exception.PersistenceException;
import org.talend.commons.ui.image.EImage;
import org.talend.commons.ui.image.ImageProvider;
import org.talend.core.model.components.ComponentUtilities;
import org.talend.core.model.metadata.builder.connection.AbstractMetadataObject;
import org.talend.core.model.metadata.builder.connection.SubItemHelper;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.IRepositoryObject;
import org.talend.core.model.repository.RepositoryManager;
import org.talend.repository.ProjectManager;
import org.talend.repository.i18n.Messages;
import org.talend.repository.model.IProxyRepositoryFactory;
import org.talend.repository.model.ISubRepositoryObject;
import org.talend.repository.model.ProxyRepositoryFactory;
import org.talend.repository.model.RepositoryNode;
import org.talend.repository.model.RepositoryNode.EProperties;
import org.talend.repository.model.actions.RestoreObjectAction;

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

    @Override
    public void run() {
        // used to store the database connection object that are used to notify the sqlBuilder.
        List<IRepositoryObject> connections = new ArrayList<IRepositoryObject>();
        ISelection selection = getSelection();
        boolean needToUpdatePalette = false;
        Set<ERepositoryObjectType> types = new HashSet<ERepositoryObjectType>();
        RestoreFolderUtil restoreFolder = new RestoreFolderUtil();
        for (Object obj : ((IStructuredSelection) selection).toArray()) {
            if (obj instanceof RepositoryNode) {
                try {
                    RepositoryNode node = (RepositoryNode) obj;
                    ERepositoryObjectType nodeType = (ERepositoryObjectType) (node).getProperties(EProperties.CONTENT_TYPE);
                    if (nodeType.isSubItem()) {
                        IProxyRepositoryFactory factory = ProxyRepositoryFactory.getInstance();
                        ConnectionItem item = (ConnectionItem) node.getObject().getProperty().getItem();
                        AbstractMetadataObject abstractMetadataObject = ((ISubRepositoryObject) node.getObject())
                                .getAbstractMetadataObject();
                        SubItemHelper.setDeleted(abstractMetadataObject, false);
                        factory.save(item);
                        connections.add(node.getObject());
                    } else {
                        IPath path = restoreFolder.resotreFolderIfNotExists(nodeType, node);
                        RestoreObjectAction restoreObjectAction = RestoreObjectAction.getInstance();
                        restoreObjectAction.execute(node, null, path);
                    }
                    if (nodeType == ERepositoryObjectType.JOBLET) {
                        needToUpdatePalette = true;
                    }
                    if (nodeType.isSubItem()) {
                        RepositoryNode parent = node.getParent();
                        if (parent.getObject() == null) { // db
                            parent = parent.getParent();
                        }
                        nodeType = parent.getObjectType();
                    }
                    types.add(nodeType);
                    RepositoryManager.refreshDeletedNode(types);
                } catch (Exception e) {
                    // ExceptionHandler.process(e);
                    e.printStackTrace();
                }
            }
        }

        if (needToUpdatePalette) {
            ComponentUtilities.updatePalette();
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
        IProxyRepositoryFactory factory = ProxyRepositoryFactory.getInstance();
        if (factory.isUserReadOnlyOnCurrentProject()) {
            canWork = false;
        }
        RestoreObjectAction restoreObjectAction = RestoreObjectAction.getInstance();
        for (Object o : (selection).toArray()) {
            if (canWork) {
                if (o instanceof RepositoryNode) {
                    RepositoryNode node = (RepositoryNode) o;
                    canWork = restoreObjectAction.validateAction(node, null);
                    if (canWork && !ProjectManager.getInstance().isInCurrentMainProject(node)) {
                        canWork = false;
                    }
                    if (!canWork) {
                        break;
                    }
                }
            }
        }
        setEnabled(canWork);
    }

    /**
     * 
     * Helper class for restoring folders.
     */
    class RestoreFolderUtil {

        Map<ERepositoryObjectType, Set<String>> foldersMap = new HashMap<ERepositoryObjectType, Set<String>>();

        IProxyRepositoryFactory factory = ProxyRepositoryFactory.getInstance();

        IPath resotreFolderIfNotExists(ERepositoryObjectType type, RepositoryNode node) throws PersistenceException {
            Set<String> folders = getFolders(type);
            String oldPath = node.getObject().getProperty().getItem().getState().getPath();
            return createFolders(folders, new Path(oldPath), type);
        }

        private IPath createFolders(Set<String> folders, IPath path, ERepositoryObjectType type) throws PersistenceException {
            if (folders.contains(path.toString())) {
                return path;
            }

            String lastSegment = path.lastSegment();
            if (lastSegment != null) {
                // create parent folder
                IPath parent = createFolders(folders, path.removeLastSegments(1), type);
                factory.createFolder(type, parent, lastSegment);
                return path;
            } else {
                return new Path("");
            }
        }

        private Set<String> getFolders(ERepositoryObjectType type) throws PersistenceException {
            Set<String> folders = foldersMap.get(type);
            if (folders == null) {
                folders = new HashSet<String>(factory.getFolders(type));
                foldersMap.put(type, folders);
            }
            return folders;
        }

    }
}
