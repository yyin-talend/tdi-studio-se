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

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Shell;
import org.talend.commons.ui.image.ImageProvider;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.IRepositoryObject;
import org.talend.core.ui.images.ECoreImage;
import org.talend.repository.i18n.Messages;
import org.talend.repository.model.BinRepositoryNode;
import org.talend.repository.model.IProxyRepositoryFactory;
import org.talend.repository.model.ProxyRepositoryFactory;
import org.talend.repository.model.RepositoryNode;
import org.talend.repository.ui.views.RepositoryContentProvider.ISubRepositoryObject;

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

    public void run() {
        ISelection selection = getSelection();
        Object obj = ((IStructuredSelection) selection).getFirstElement();
        RepositoryNode node = (RepositoryNode) obj;

        String title = Messages.getString("EmptyRecycleBinAction.dialog.title"); //$NON-NLS-1$
        String message = Messages.getString("EmptyRecycleBinAction.dialog.message1") + "\n" //$NON-NLS-1$ //$NON-NLS-2$
                + Messages.getString("EmptyRecycleBinAction.dialog.message2"); //$NON-NLS-1$
        if (!(MessageDialog.openQuestion(new Shell(), title, message))) {
            return;
        }

        IProxyRepositoryFactory factory = ProxyRepositoryFactory.getInstance();
        for (RepositoryNode child : node.getChildren()) {
            if (child.getObjectType() == ERepositoryObjectType.HTML_DOC) {
                continue;
            }
            IRepositoryObject objToDelete = child.getObject();
            try {
                if (objToDelete instanceof ISubRepositoryObject) {
                    ISubRepositoryObject subRepositoryObject = (ISubRepositoryObject) objToDelete;
                    subRepositoryObject.removeFromParent();
                    factory.save(subRepositoryObject.getProperty().getItem());
                } else {
                    factory.deleteObjectPhysical(objToDelete);
                }
            } catch (Exception e) {
                e.printStackTrace();
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
        boolean canWork = !selection.isEmpty() && selection.size() == 1;
        if (ProxyRepositoryFactory.getInstance().isUserReadOnlyOnCurrentProject()) {
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
        }
        setEnabled(canWork);
    }

}
