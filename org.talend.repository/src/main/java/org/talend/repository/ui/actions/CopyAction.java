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

import org.eclipse.jface.util.LocalSelectionTransfer;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.talend.commons.ui.image.EImage;
import org.talend.commons.ui.image.ImageProvider;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.repository.i18n.Messages;
import org.talend.repository.model.ProxyRepositoryFactory;
import org.talend.repository.model.RepositoryNode;
import org.talend.repository.model.RepositoryNode.EProperties;
import org.talend.repository.model.actions.CopyObjectAction;

/**
 * DOC smallet class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 */
public class CopyAction extends AContextualAction {

    private static CopyAction singleton;

    public CopyAction() {
        super();
        this.setText(Messages.getString("CopyAction.thisText.copy")); //$NON-NLS-1$
        this.setImageDescriptor(ImageProvider.getImageDesc(EImage.COPY_ICON));
        this.setActionDefinitionId("copyItem"); //$NON-NLS-1$
        singleton = this;
    }

    public static CopyAction getInstance() {
        return singleton;
    }

    @Override
    public void run() {
        IStructuredSelection selection = (IStructuredSelection) getSelection();
        LocalSelectionTransfer.getTransfer().setSelection(selection);
        LocalSelectionTransfer.getTransfer().setSelectionSetTime(System.currentTimeMillis());
        refresh();
    }

    public void init(TreeViewer viewer, IStructuredSelection selection) {
        boolean canWork = true;
        RepositoryNode node = (RepositoryNode) selection.getFirstElement();
        if (ProxyRepositoryFactory.getInstance().isUserReadOnlyOnCurrentProject()) {
            canWork = false;
        }
        for (Object obj : ((StructuredSelection) selection).toArray()) {
            if (canWork) {
                RepositoryNode sourceNode = (RepositoryNode) obj;
                if (!CopyObjectAction.getInstance().validateAction(sourceNode, null)) {
                    canWork = false;
                } else if (node.getProperties(EProperties.CONTENT_TYPE) == ERepositoryObjectType.HTML_DOC) {
                    canWork = false;
                }
            }
        }
        setEnabled(canWork);
    }
}
