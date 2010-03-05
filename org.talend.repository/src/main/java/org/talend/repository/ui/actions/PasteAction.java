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
import org.eclipse.jface.util.LocalSelectionTransfer;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.actions.ActionFactory;
import org.talend.commons.exception.BusinessException;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.exception.MessageBoxExceptionHandler;
import org.talend.commons.ui.image.EImage;
import org.talend.commons.ui.image.ImageProvider;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.PluginChecker;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.Folder;
import org.talend.core.model.repository.IRepositoryObject;
import org.talend.core.model.repository.RepositoryManager;
import org.talend.core.ui.ICDCProviderService;
import org.talend.repository.i18n.Messages;
import org.talend.repository.model.BinRepositoryNode;
import org.talend.repository.model.ERepositoryStatus;
import org.talend.repository.model.IProxyRepositoryFactory;
import org.talend.repository.model.ProxyRepositoryFactory;
import org.talend.repository.model.RepositoryNode;
import org.talend.repository.model.RepositoryNode.ENodeType;
import org.talend.repository.model.RepositoryNode.EProperties;
import org.talend.repository.model.actions.CopyObjectAction;

/**
 * DOC smallet class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 */
public class PasteAction extends AContextualAction {

    private static PasteAction singleton;

    public PasteAction() {
        super();
        setId(ActionFactory.PASTE.getId());
        this.setText(Messages.getString("PasteAction.thisText.paste")); //$NON-NLS-1$
        this.setImageDescriptor(ImageProvider.getImageDesc(EImage.PASTE_ICON));
        //        this.setActionDefinitionId("pasteItem"); //$NON-NLS-1$
        singleton = this;
    }

    public static PasteAction getInstance() {
        return singleton;
    }

    @Override
    protected void doRun() {
        IStructuredSelection selection = (IStructuredSelection) getSelection();
        RepositoryNode target = (RepositoryNode) selection.getFirstElement();

        CopyObjectAction copyObjectAction = CopyObjectAction.getInstance();

        TreeSelection selectionInClipboard = (TreeSelection) LocalSelectionTransfer.getTransfer().getSelection();
        if (selectionInClipboard != null) {
            for (Object currentSource : selectionInClipboard.toArray()) {
                try {
                    if (copyObjectAction.validateAction((RepositoryNode) currentSource, target)) {
                        copyObjectAction.execute((RepositoryNode) currentSource, target);
                    } else {
                        MessageDialog.openWarning(new Shell(), Messages.getString("PasteObjectAction.error.title"), Messages //$NON-NLS-1$
                                .getString("PasteObjectAction.error.labelAlreadyExists")); //$NON-NLS-1$
                    }
                } catch (BusinessException e) {
                    MessageBoxExceptionHandler.process(e);
                } catch (Exception e) {
                    ExceptionHandler.process(e);
                }
            }

            ERepositoryObjectType contentType = target.getContentType();
            if (contentType == null) {
                contentType = target.getObjectType();
            }
            RepositoryManager.refreshCreatedNode(contentType);
            RepositoryManager.refreshCreatedNode(target.getContentType());
        }
    }

    public void init(TreeViewer viewer, IStructuredSelection selection) {
        boolean enabled = true;
        if (selection.isEmpty()) {
            setEnabled(false);
            return;
        }
        RepositoryNode target = (RepositoryNode) selection.getFirstElement();
        if (!(LocalSelectionTransfer.getTransfer().getSelection() instanceof TreeSelection)) {
            setEnabled(false);
            return;
        }

        if (target instanceof BinRepositoryNode) {
            setEnabled(false);
            return;
        }
        TreeSelection selectionInClipboard = (TreeSelection) LocalSelectionTransfer.getTransfer().getSelection();
        IProxyRepositoryFactory proxyFactory = ProxyRepositoryFactory.getInstance();
        IRepositoryObject object = target.getObject();

        if (target.getContentType() == ERepositoryObjectType.JOBS || target.getContentType() == ERepositoryObjectType.JOBLETS
                || target.getContentType() == ERepositoryObjectType.GENERATED
                || target.getProperties(EProperties.CONTENT_TYPE) == ERepositoryObjectType.JOB_DOC
                || target.getProperties(EProperties.CONTENT_TYPE) == ERepositoryObjectType.JOBLET_DOC
                || target.getProperties(EProperties.CONTENT_TYPE) == ERepositoryObjectType.REFERENCED_PROJECTS) {
            visible = false;
            enabled = false;
        } else if (object != null) {
            if (target != null && proxyFactory.getStatus(object) == ERepositoryStatus.READ_ONLY) {
                enabled = false;
                visible = false;
            } else if (target != null && object instanceof Folder && enabled == true) {
                visible = true;
            }
        } else if (isReferencedProject(target)) {
            visible = false;
            enabled = false;
        } else if (selectionInClipboard != null) {
            for (Object obj : ((StructuredSelection) selectionInClipboard).toArray()) {
                if (enabled) {
                    RepositoryNode sourceNode = (RepositoryNode) obj;
                    if (!CopyObjectAction.getInstance().validateAction(sourceNode, target)) {
                        visible = true;
                        enabled = false;
                    } else {
                        if (sourceNode.getObjectType().getKey().equals("repository.routines")) { //$NON-NLS-1$
                            visible = true;
                            enabled = false;
                        } else {
                            visible = true;
                            enabled = true;
                        }
                    }

                }
            }
        } else {
            enabled = false;
            visible = false;
        }
        // for cdc
        if (PluginChecker.isCDCPluginLoaded()) {
            ICDCProviderService cdcService = (ICDCProviderService) GlobalServiceRegister.getDefault().getService(
                    ICDCProviderService.class);
            if (cdcService != null && cdcService.isSubscriberTableNode(target)) {
                enabled = false;
                visible = false;
            }
        }
        setEnabled(enabled);
    }

    public boolean isReferencedProject(RepositoryNode node) {
        if (node.getType() == ENodeType.REFERENCED_PROJECT) {
            return true;
        } else if (node.getParent() != null) {
            node = node.getParent();
            if (isReferencedProject(node)) {
                return true;
            }
        }
        return false;
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
