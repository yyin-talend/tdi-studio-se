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
package org.talend.repository.ui.actions.metadata;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.talend.commons.ui.image.EImage;
import org.talend.commons.ui.image.ImageProvider;
import org.talend.core.model.metadata.builder.connection.MetadataTable;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.update.RepositoryUpdateManager;
import org.talend.repository.i18n.Messages;
import org.talend.repository.model.ProxyRepositoryFactory;
import org.talend.repository.model.RepositoryNode;
import org.talend.repository.ui.actions.AContextualAction;
import org.talend.repository.ui.views.RepositoryContentProvider.ISubRepositoryObject;

/**
 * ggu class global comment. Detailled comment
 */
public class DetectedSchemaModificationAction extends AContextualAction {

    private static final String LABEL = Messages.getString("DetectedSchemaModificationAction.Label"); //$NON-NLS-1$

    /**
     * ggu DetectedModificationAction constructor comment.
     */
    public DetectedSchemaModificationAction() {
        setText(LABEL);
        setToolTipText(LABEL);
        setImageDescriptor(ImageProvider.getImageDesc(EImage.REFRESH_ICON));
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.commons.ui.swt.actions.ITreeContextualAction#init(org.eclipse.jface.viewers.TreeViewer,
     * org.eclipse.jface.viewers.IStructuredSelection)
     */
    public void init(TreeViewer viewer, IStructuredSelection selection) {
        boolean canWork = !selection.isEmpty() && selection.size() == 1;
        if (canWork) {
            if (ProxyRepositoryFactory.getInstance().isUserReadOnlyOnCurrentProject()) {
                canWork = false;
            } else {
                Object o = selection.getFirstElement();
                RepositoryNode node = (RepositoryNode) o;
                switch (node.getType()) {
                case REPOSITORY_ELEMENT:
                    if (node.getObjectType() != ERepositoryObjectType.METADATA_CON_TABLE) {
                        canWork = false;
                    }
                    break;
                default:
                    canWork = false;
                }
            }
        }
        setEnabled(canWork);

    }

    @Override
    public void run() {
        RepositoryNode node = getCurrentRepositoryNode();
        if (node.getObject() instanceof ISubRepositoryObject) {
            ISubRepositoryObject subObject = (ISubRepositoryObject) node.getObject();
            if (subObject.getAbstractMetadataObject() instanceof MetadataTable) {
                RepositoryUpdateManager.updateSchema((MetadataTable) subObject.getAbstractMetadataObject());
            }
        }

    }
}
