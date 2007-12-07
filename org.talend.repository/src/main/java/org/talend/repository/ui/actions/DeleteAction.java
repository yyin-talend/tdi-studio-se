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

import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Shell;
import org.talend.commons.exception.BusinessException;
import org.talend.commons.exception.MessageBoxExceptionHandler;
import org.talend.commons.exception.PersistenceException;
import org.talend.commons.ui.image.EImage;
import org.talend.commons.ui.image.ImageProvider;
import org.talend.core.PluginChecker;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.IRepositoryObject;
import org.talend.expressionbuilder.ExpressionPersistance;
import org.talend.repository.i18n.Messages;
import org.talend.repository.model.ERepositoryStatus;
import org.talend.repository.model.IProxyRepositoryFactory;
import org.talend.repository.model.ProxyRepositoryFactory;
import org.talend.repository.model.RepositoryNode;
import org.talend.repository.model.RepositoryNodeUtilities;
import org.talend.repository.model.RepositoryNode.ENodeType;
import org.talend.repository.model.RepositoryNode.EProperties;
import org.talend.repository.ui.actions.metadata.DeleteTableAction;

/**
 * Action used to delete object from repository. This action manages logical and physical deletions.<br/>
 * 
 * $Id$
 * 
 */
public class DeleteAction extends AContextualAction {

    private static DeleteAction singleton;

    private static final String DELETE_LOGICAL_TITLE = Messages.getString("DeleteAction.action.logicalTitle"); //$NON-NLS-1$

    private static final String DELETE_FOREVER_TITLE = Messages.getString("DeleteAction.action.foreverTitle"); //$NON-NLS-1$

    private static final String DELETE_LOGICAL_TOOLTIP = Messages.getString("DeleteAction.action.logicalToolTipText"); //$NON-NLS-1$

    private static final String DELETE_FOREVER_TOOLTIP = Messages.getString("DeleteAction.action.logicalToolTipText"); //$NON-NLS-1$

    public DeleteAction() {
        super();
        this.setImageDescriptor(ImageProvider.getImageDesc(EImage.DELETE_ICON));
        this.setActionDefinitionId("deleteItem"); //$NON-NLS-1$
        singleton = this;
    }

    public static DeleteAction getInstance() {
        return singleton;
    }

    @Override
    public void run() {
        ISelection selection = getSelection();
        IProxyRepositoryFactory factory = ProxyRepositoryFactory.getInstance();

        for (Object obj : ((IStructuredSelection) selection).toArray()) {
            if (obj instanceof RepositoryNode) {
                RepositoryNode node = (RepositoryNode) obj;
                try {

                    if (isForbidNode(node)) {
                        continue;
                    }

                    if (node.getType() == ENodeType.REPOSITORY_ELEMENT) {
                        boolean needReturn = deleteElements(factory, node);
                        if (needReturn) {
                            return;
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
                } catch (BusinessException e) {
                    MessageBoxExceptionHandler.process(e);
                }
            }
        }
        refresh();
    }

    /**
     * ftang Comment method "isForbbidNode".
     * 
     * @param node
     * @return
     */
    private boolean isForbidNode(RepositoryNode node) {

        // Avoid to delete all related documentation node by click Key "Delete" from keyboard.
        if (node.getContentType() == ERepositoryObjectType.HTML_DOC) {
            return true;
        }

        if (node.getProperties(EProperties.CONTENT_TYPE) == ERepositoryObjectType.HTML_DOC) {
            return true;
        }

        if (node.getContentType() == ERepositoryObjectType.JOBS) {
            return true;
        }
        if (node.getContentType() == ERepositoryObjectType.GENERATED) {
            return true;
        }
        return false;
    }

    /**
     * ftang Comment method "deleteElements".
     * 
     * @param factory
     * @param currentJobNode
     * @throws PersistenceException
     * @throws BusinessException
     */
    private boolean deleteElements(IProxyRepositoryFactory factory, RepositoryNode currentJobNode) throws PersistenceException,
            BusinessException {
        Boolean confirm = null;
        boolean needReturn = false;
        IRepositoryObject objToDelete = currentJobNode.getObject();

        // To manage case of we have a subitem. This is possible using 'DEL' shortcut:
        ERepositoryObjectType nodeType = (ERepositoryObjectType) currentJobNode.getProperties(EProperties.CONTENT_TYPE);
        if (nodeType.isSubItem()) {
            new DeleteTableAction().run();
            needReturn = true;
        }

        if (factory.getStatus(objToDelete) == ERepositoryStatus.DELETED) {
            if (confirm == null) {
                String title = Messages.getString("DeleteAction.dialog.title"); //$NON-NLS-1$
                String message = Messages.getString("DeleteAction.dialog.message1") + "\n" //$NON-NLS-1$ //$NON-NLS-2$
                        + Messages.getString("DeleteAction.dialog.message2"); //$NON-NLS-1$
                confirm = (MessageDialog.openQuestion(new Shell(), title, message));
            }
            if (confirm) {
                factory.deleteObjectPhysical(objToDelete);
                ExpressionPersistance.getInstance().jobDeleted(objToDelete.getLabel());

            }
        } else {
            factory.deleteObjectLogical(objToDelete);
        }
        return needReturn;
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
        if (ProxyRepositoryFactory.getInstance().isUserReadOnlyOnCurrentProject()) {
            visible = false;
        }
        for (Object o : (selection).toArray()) {
            if (visible) {
                RepositoryNode node = (RepositoryNode) o;
                switch (node.getType()) {
                case STABLE_SYSTEM_FOLDER:
                    visible = false;
                case SYSTEM_FOLDER:
                    visible = false;
                    break;
                case SIMPLE_FOLDER:
                    if (node.getContentType() == ERepositoryObjectType.HTML_DOC) {
                        visible = false;
                    } else {
                        this.setText(DELETE_LOGICAL_TITLE);
                        this.setToolTipText(DELETE_LOGICAL_TOOLTIP);
                        if (node.hasChildren()) {
                            visible = true;
                            enabled = false;
                        }
                    }
                    break;
                case REPOSITORY_ELEMENT:
                    if (node.getProperties(EProperties.CONTENT_TYPE) == ERepositoryObjectType.HTML_DOC) {
                        visible = false;
                        break;
                    }
                    IRepositoryObject repObj = node.getObject();
                    IProxyRepositoryFactory repFactory = ProxyRepositoryFactory.getInstance();

                    ERepositoryStatus status = repFactory.getStatus(repObj);
                    boolean isEditable = status.isPotentiallyEditable() || status.isEditable();
                    boolean isDeleted = repFactory.getStatus(repObj) == ERepositoryStatus.DELETED;

                    if (isDeleted) {
                        ERepositoryObjectType nodeType = (ERepositoryObjectType) node.getProperties(EProperties.CONTENT_TYPE);
                        if (ERepositoryObjectType.METADATA_CON_TABLE.equals(nodeType)) {
                            visible = false;
                            break;
                        }
                        if (ERepositoryObjectType.METADATA_CON_QUERY.equals(nodeType)) {
                            visible = false;
                            break;
                        }

                        if (getText() == null || DELETE_FOREVER_TITLE.equals(getText())) {
                            this.setText(DELETE_FOREVER_TITLE);
                            this.setToolTipText(DELETE_FOREVER_TOOLTIP);
                        } else {
                            visible = false;
                        }
                    } else {
                        switch (repObj.getType()) {
                        case METADATA_CON_TABLE:
                        case METADATA_CON_QUERY:
                            visible = false;
                            break;
                        default:
                            if (getText() == null || DELETE_LOGICAL_TITLE.equals(getText())) {
                                this.setText(DELETE_LOGICAL_TITLE);
                                this.setToolTipText(DELETE_LOGICAL_TOOLTIP);

                                if (!isEditable) {
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
    @Override
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
