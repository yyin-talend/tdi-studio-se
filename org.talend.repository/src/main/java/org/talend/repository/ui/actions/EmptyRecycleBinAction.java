// ============================================================================
//
// Copyright (C) 2006-2010 Talend Inc. - www.talend.com
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

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.talend.commons.exception.MessageBoxExceptionHandler;
import org.talend.commons.ui.image.ImageProvider;
import org.talend.core.CorePlugin;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.IRepositoryObject;
import org.talend.core.model.repository.RepositoryManager;
import org.talend.core.ui.images.ECoreImage;
import org.talend.repository.ProjectManager;
import org.talend.repository.i18n.Messages;
import org.talend.repository.model.BinRepositoryNode;
import org.talend.repository.model.ERepositoryStatus;
import org.talend.repository.model.IProxyRepositoryFactory;
import org.talend.repository.model.ISubRepositoryObject;
import org.talend.repository.model.ProxyRepositoryFactory;
import org.talend.repository.model.RepositoryNode;

/**
 * Action used to empty the recycle bin.<br/>
 * 
 * $Id$
 * 
 */
public class EmptyRecycleBinAction extends AContextualAction {

    public EmptyRecycleBinAction() {
        super();
        this.setText(Messages.getString("EmptyRecycleBinAction.action.title")); //$NON-NLS-1$
        this.setToolTipText(Messages.getString("EmptyRecycleBinAction.action.toolTipText")); //$NON-NLS-1$
        this.setImageDescriptor(ImageProvider.getImageDesc(ECoreImage.RECYCLE_BIN_EMPTY_ICON));
    }

    protected void doRun() {
        ISelection selection = getSelection();
        Object obj = ((IStructuredSelection) selection).getFirstElement();
        RepositoryNode node = (RepositoryNode) obj;

        String title = Messages.getString("EmptyRecycleBinAction.dialog.title"); //$NON-NLS-1$
        String message = null;

        if (node.getChildren().size() == 0) {
            return;
        } else if (node.getChildren().size() > 1) {
            message = Messages.getString("DeleteAction.dialog.messageAllElements") + "\n" + //$NON-NLS-1$ //$NON-NLS-2$
                    Messages.getString("DeleteAction.dialog.message2"); //$NON-NLS-1$;
        } else {
            message = Messages.getString("DeleteAction.dialog.message1") + "\n" //$NON-NLS-1$ //$NON-NLS-2$
                    + Messages.getString("DeleteAction.dialog.message2"); //$NON-NLS-1$
        }
        if (!(MessageDialog.openQuestion(new Shell(), title, message))) {
            return;
        }

        IProxyRepositoryFactory factory = ProxyRepositoryFactory.getInstance();
        for (RepositoryNode child : node.getChildren()) {
            IRepositoryObject objToDelete = child.getObject();
            try {
                if (objToDelete instanceof ISubRepositoryObject) {
                    ISubRepositoryObject subRepositoryObject = (ISubRepositoryObject) objToDelete;
                    if (!isRootNodeDeleted(child)) {
                        subRepositoryObject.removeFromParent();
                        factory.save(subRepositoryObject.getProperty().getItem());
                    }
                } else {
                    IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
                    for (IEditorReference editors : page.getEditorReferences()) {
                        String nameInEditor = editors.getName();
                        if (objToDelete.getLabel().equals(nameInEditor.substring(nameInEditor.indexOf(" ") + 1))) { //$NON-NLS-1$
                            page.closeEditor(editors.getEditor(false), false);
                        }
                    }
                    if (objToDelete.getType() != ERepositoryObjectType.JOB_DOC
                            && objToDelete.getType() != ERepositoryObjectType.JOBLET_DOC)
                        factory.deleteObjectPhysical(objToDelete);
                }
            } catch (Exception e) {
                MessageBoxExceptionHandler.process(e);
            }
        }
        RepositoryManager.refreshDeletedNode(null);
        IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
        for (IEditorReference editors : page.getEditorReferences()) {
            CorePlugin.getDefault().getDiagramModelService().refreshBusinessModel(editors);
        }
    }

    /**
     * DOC qzhang Comment method "getRootNode".
     * 
     * @param child
     * @return
     */
    private boolean isRootNodeDeleted(RepositoryNode child) {
        boolean isDeleted = false;
        if (child != null) {
            RepositoryNode parent = child.getParent();
            if (parent != null) {
                IRepositoryObject object = parent.getObject();
                if (object != null) {
                    ProxyRepositoryFactory factory = ProxyRepositoryFactory.getInstance();

                    isDeleted = factory.getStatus(object) == ERepositoryStatus.DELETED;
                }

                if (!isDeleted) {
                    isDeleted = isRootNodeDeleted(parent);
                }
            }
        }
        return isDeleted;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.ui.actions.ITreeContextualAction#init(org.eclipse.jface.viewers.TreeViewer,
     * org.eclipse.jface.viewers.IStructuredSelection)
     */
    public void init(TreeViewer viewer, IStructuredSelection selection) {
        boolean canWork = !selection.isEmpty() && selection.size() == 1;
        IProxyRepositoryFactory factory = ProxyRepositoryFactory.getInstance();
        if (factory.isUserReadOnlyOnCurrentProject()) {
            canWork = false;
        }
        if (canWork) {
            Object o = selection.getFirstElement();
            RepositoryNode node = (RepositoryNode) o;
            switch (node.getType()) {
            case STABLE_SYSTEM_FOLDER:
                if (!(node instanceof BinRepositoryNode) || !node.hasChildren()) {
                    canWork = false;
                }
                break;
            default:
                canWork = false;
                break;
            }
            if (canWork && !ProjectManager.getInstance().isInCurrentMainProject(node)) {
                canWork = false;
            }
        }
        setEnabled(canWork);
    }

}
