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
import org.eclipse.jface.util.LocalSelectionTransfer;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Shell;
import org.talend.commons.exception.BusinessException;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.exception.MessageBoxExceptionHandler;
import org.talend.commons.ui.image.EImage;
import org.talend.commons.ui.image.ImageProvider;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.repository.i18n.Messages;
import org.talend.repository.model.RepositoryNode;
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
        this.setText(Messages.getString("PasteAction.thisText.paste")); //$NON-NLS-1$
        this.setImageDescriptor(ImageProvider.getImageDesc(EImage.PASTE_ICON));
        this.setActionDefinitionId("pasteItem"); //$NON-NLS-1$
        singleton = this;
    }

    public static PasteAction getInstance() {
        return singleton;
    }

    @Override
    public void run() {
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

            refresh();
        }
    }

    public void init(TreeViewer viewer, IStructuredSelection selection) {
        boolean enabled = true;
        RepositoryNode target = (RepositoryNode) selection.getFirstElement();
        TreeSelection selectionInClipboard = (TreeSelection) LocalSelectionTransfer.getTransfer().getSelection();

        if (target.getContentType() == ERepositoryObjectType.JOBS || target.getContentType() == ERepositoryObjectType.GENERATED) {
            visible = false;
            enabled = false;
        }

        else if (selectionInClipboard != null) {
            for (Object obj : ((StructuredSelection) selectionInClipboard).toArray()) {
                if (enabled) {
                    RepositoryNode sourceNode = (RepositoryNode) obj;

                    if (!CopyObjectAction.getInstance().validateAction(sourceNode, target)) {
                        visible = true;
                        enabled = false;
                    } else {
                        visible = true;
                        enabled = true;
                    }
                }
            }
        } else {
            enabled = false;
            visible = false;
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
