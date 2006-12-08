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

import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Shell;
import org.talend.commons.exception.MessageBoxExceptionHandler;
import org.talend.commons.exception.PersistenceException;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.IRepositoryObject;
import org.talend.core.ui.images.EImage;
import org.talend.core.ui.images.ImageProvider;
import org.talend.repository.i18n.Messages;
import org.talend.repository.model.ERepositoryStatus;
import org.talend.repository.model.IProxyRepositoryFactory;
import org.talend.repository.model.ProxyRepositoryFactory;
import org.talend.repository.model.RepositoryNode;
import org.talend.repository.model.RepositoryNodeUtilities;
import org.talend.repository.model.RepositoryNode.ENodeType;
import org.talend.repository.model.RepositoryNode.EProperties;

/**
 * Action used to delete object from repository. This action manages logical and physical deletions.<br/>
 * 
 * $Id$
 * 
 */
public class DeleteAction extends AContextualAction {

    private static final String DELETE_LOGICAL_TITLE = Messages.getString("DeleteAction.action.logicalTitle");

    private static final String DELETE_FOREVER_TITLE = Messages.getString("DeleteAction.action.foreverTitle");

    private static final String DELETE_LOGICAL_TOOLTIP = Messages.getString("DeleteAction.action.logicalToolTipText");

    private static final String DELETE_FOREVER_TOOLTIP = Messages.getString("DeleteAction.action.logicalToolTipText");

    public DeleteAction() {
        super();
        this.setImageDescriptor(ImageProvider.getImageDesc(EImage.DELETE_ICON));
        this.setActionDefinitionId("deleteItem");
    }

    public void run() {
        ISelection selection = getSelection();
        Boolean confirm = null;
        IProxyRepositoryFactory factory = ProxyRepositoryFactory.getInstance();

        for (Object obj : ((IStructuredSelection) selection).toArray()) {
            if (obj instanceof RepositoryNode) {
                RepositoryNode node = (RepositoryNode) obj;
                try {
                    if (node.getType() == ENodeType.REPOSITORY_ELEMENT) {
                        IRepositoryObject objToDelete = node.getObject();
                        if (factory.getStatus(objToDelete) == ERepositoryStatus.DELETED) {
                            if (confirm == null) {
                                String title = Messages.getString("DeleteAction.dialog.title");
                                String message = Messages.getString("DeleteAction.dialog.message1") + "\n"
                                        + Messages.getString("DeleteAction.dialog.message2");
                                confirm = (MessageDialog.openQuestion(new Shell(), title, message));
                            }
                            if (confirm) {
                                factory.deleteObjectPhysical(objToDelete);
                            }
                        } else {
                            factory.deleteObjectLogical(objToDelete);
                        }
                    } else if (node.getType() == ENodeType.SIMPLE_FOLDER) {
                        if (!node.hasChildren()) {
                            IPath path = RepositoryNodeUtilities.getPath(node);
                            ERepositoryObjectType objectType = (ERepositoryObjectType) node
                                    .getProperties(EProperties.CONTENT_TYPE);
                            factory.deleteFolder(objectType, path);
                        }
                    }
                } catch (PersistenceException e) {
                    MessageBoxExceptionHandler.process(e);
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
        visible = !selection.isEmpty();
        boolean enabled = true;
        this.setText(null);

        for (Object o : ((IStructuredSelection) selection).toArray()) {
            if (visible) {
                RepositoryNode node = (RepositoryNode) o;
                switch (node.getType()) {
                case STABLE_SYSTEM_FOLDER:
                case SYSTEM_FOLDER:
                    visible = false;
                    break;
                case SIMPLE_FOLDER:
                    this.setText(DELETE_LOGICAL_TITLE);
                    this.setToolTipText(DELETE_LOGICAL_TOOLTIP);
                    if (node.hasChildren()) {
                        visible = true;
                        enabled = false;
                    }
                    break;
                case REPOSITORY_ELEMENT:
                    IRepositoryObject repObj = node.getObject();
                    IProxyRepositoryFactory repFactory = ProxyRepositoryFactory.getInstance();

                    boolean isPotentiallyEditable = repFactory.isPotentiallyEditable(repObj);
                    boolean idDeleted = repFactory.getStatus(repObj) == ERepositoryStatus.DELETED;

                    if (idDeleted) {
                        ERepositoryObjectType nodeType = (ERepositoryObjectType) node.getProperties(EProperties.CONTENT_TYPE);
                        if (ERepositoryObjectType.METADATA_CON_TABLE.equals(nodeType)) {
                            visible = false;
                            break;
                        }

                        if (getText() == null || DELETE_FOREVER_TITLE.equals(getText())) {
                            this.setText(DELETE_FOREVER_TITLE);
                            this.setToolTipText(DELETE_FOREVER_TOOLTIP);

                            // if (!isPotentiallyEditable) {
                            // visible = true;
                            // enabled = false;
                            // }
                        } else {
                            visible = false;
                        }
                    } else {
                        switch (repObj.getType()) {
                        case METADATA_CON_TABLE:
                        case ROUTINES:
                            visible = false;
                            break;
                        default:
                            if (getText() == null || DELETE_LOGICAL_TITLE.equals(getText())) {
                                this.setText(DELETE_LOGICAL_TITLE);
                                this.setToolTipText(DELETE_LOGICAL_TOOLTIP);

                                if (!isPotentiallyEditable) {
                                    visible = true;
                                    enabled = false;
                                }
                            } else {
                                visible = false;
                            }
                            break;
                        }
                    }
                    break;
                default:
                    // Nothing to do
                    break;
                }
            }
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
