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

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IPath;
import org.eclipse.emf.common.util.EList;
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
import org.talend.core.model.components.ComponentUtilities;
import org.talend.core.model.properties.Item;
import org.talend.core.model.properties.JobletProcessItem;
import org.talend.core.model.properties.ProcessItem;
import org.talend.core.model.properties.Property;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.IRepositoryObject;
import org.talend.designer.core.model.utils.emf.talendfile.NodeType;
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

        boolean needToUpdataPalette = false;

        for (Object obj : ((IStructuredSelection) selection).toArray()) {
            if (obj instanceof RepositoryNode) {
                RepositoryNode node = (RepositoryNode) obj;
                try {

                    if (isForbidNode(node)) {
                        continue;
                    }

                    if (node.getType() == ENodeType.REPOSITORY_ELEMENT) {
                        boolean needReturn = deleteElements(factory, node);
                        if (node.getProperties(EProperties.CONTENT_TYPE) == ERepositoryObjectType.JOBLET) {
                            needToUpdataPalette = true;
                        }
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
        if (needToUpdataPalette) {
            ComponentUtilities.updatePalette();
        }
        refresh();
    }

    /**
     * DOC qzhang Comment method "checkRepository".
     * 
     * @param factory
     * @param currentJobNode
     * @return
     */
    private List<String> checkRepositoryNodeFromProcess(IProxyRepositoryFactory factory, RepositoryNode currentJobNode) {
        IRepositoryObject object = currentJobNode.getObject();
        List<String> list = new ArrayList<String>();
        if (object != null) {
            Property property = object.getProperty();
            if (property != null) {
                Item item = property.getItem();
                if (item instanceof JobletProcessItem) {
                    String label = property.getLabel();
                    String version = property.getVersion();
                    try {
                        List<IRepositoryObject> repositoryObjects = new ArrayList<IRepositoryObject>();
                        List<IRepositoryObject> all = factory.getAll(ERepositoryObjectType.PROCESS, true);
                        repositoryObjects.addAll(all);
                        all = factory.getAll(ERepositoryObjectType.JOBLET, true);
                        repositoryObjects.addAll(all);
                        String prefix = "";
                        for (IRepositoryObject repositoryObject : repositoryObjects) {
                            Property property2 = repositoryObject.getProperty();
                            EList node = null;
                            if (property2.getItem() instanceof ProcessItem) {
                                ProcessItem item2 = (ProcessItem) property2.getItem();
                                node = item2.getProcess().getNode();
                                prefix = ERepositoryObjectType.PROCESS.toString();
                            } else if (property2.getItem() instanceof JobletProcessItem) {
                                JobletProcessItem item2 = (JobletProcessItem) property2.getItem();
                                node = item2.getJobletProcess().getNode();
                                prefix = ERepositoryObjectType.JOBLET.toString();
                            }
                            if (node != null) {
                                for (Object object2 : node) {
                                    if (object2 instanceof NodeType) {
                                        NodeType nodeType = (NodeType) object2;
                                        nodeType.getElementParameter();
                                        boolean equals = nodeType.getComponentName().equals(label)
                                                && nodeType.getComponentVersion().equals(version);
                                        if (equals) {
                                            String path = property2.getItem().getState().getPath();
                                            if (path.length() > 0) {
                                                path = path + File.separator;
                                            }
                                            list.add(prefix + " : " + path + property2.getLabel() + " " + property2.getVersion()
                                                    + "\n");
                                        }
                                    }
                                }
                            }
                        }
                    } catch (PersistenceException ex) {
                        ex.printStackTrace();
                    }

                }
            }
        }
        return list;
    }

    /**
     * ftang Comment method "isForbbidNode".
     * 
     * @param node
     * @return
     */
    private boolean isForbidNode(RepositoryNode node) {

        // Avoid to delete all related documentation node by click Key "Delete" from keyboard.
        if (node.getContentType() == ERepositoryObjectType.JOB_DOC) {
            return true;
        }

        if (node.getProperties(EProperties.CONTENT_TYPE) == ERepositoryObjectType.JOB_DOC) {
            return true;
        }

        if (node.getContentType() == ERepositoryObjectType.JOBLET_DOC) {
            return true;
        }

        if (node.getProperties(EProperties.CONTENT_TYPE) == ERepositoryObjectType.JOBLET_DOC) {
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

        List<String> checkRepository = checkRepositoryNodeFromProcess(factory, currentJobNode);
        if (checkRepository.size() > 0) {
            StringBuffer buffer = new StringBuffer();
            for (String string : checkRepository) {
                buffer.append(string);
            }
            MessageDialog.openInformation(new Shell(), "Delete the node Failure", "Node : '"
                    + objToDelete.getProperty().getLabel() + " " + objToDelete.getProperty().getVersion()
                    + "' is referenced from:\n" + buffer.toString());
            return true;
        }
        // To manage case of we have a subitem. This is possible using 'DEL' shortcut:
        ERepositoryObjectType nodeType = (ERepositoryObjectType) currentJobNode.getProperties(EProperties.CONTENT_TYPE);
        if (nodeType.isSubItem()) {
            final DeleteTableAction deleteTableAction = new DeleteTableAction();
            deleteTableAction.setWorkbenchPart(getWorkbenchPart());
            deleteTableAction.run();
            needReturn = true;
        } else {
            if (factory.getStatus(objToDelete) == ERepositoryStatus.DELETED) {
                if (confirm == null) {
                    String title = Messages.getString("DeleteAction.dialog.title"); //$NON-NLS-1$
                    String message = currentJobNode.getProperties(EProperties.LABEL)
                            + " " + Messages.getString("DeleteAction.dialog.message1") + "\n" //$NON-NLS-1$ //$NON-NLS-2$
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
        if (selection.isEmpty()) {
            setEnabled(false);
            return;
        }

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
                    if (node.getContentType() == ERepositoryObjectType.JOB_DOC
                            || node.getContentType() == ERepositoryObjectType.JOBLET_DOC) {
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
                    if (node.getProperties(EProperties.CONTENT_TYPE) == ERepositoryObjectType.JOB_DOC
                            || node.getProperties(EProperties.CONTENT_TYPE) == ERepositoryObjectType.JOBLET_DOC) {
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
