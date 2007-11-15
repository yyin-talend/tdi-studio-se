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
package org.talend.repository.ui.actions.documentation;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.talend.commons.ui.image.ImageProvider;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.ui.images.ECoreImage;
import org.talend.repository.model.ProxyRepositoryFactory;
import org.talend.repository.model.RepositoryNode;
import org.talend.repository.model.RepositoryNode.EProperties;
import org.talend.repository.ui.actions.AContextualAction;

/**
 * DOC ftang class global comment. Detailled comment <br/>
 * 
 * $Id: talend.epf 1 2006-09-29 17:06:40 +0000 (ææäº, 29 ä¹æ 2006) nrousseau $
 * 
 */
public class ExportAllJobsDocAction extends AContextualAction {

    /**
     * Constructs a new ExportAllJobsDocAction.
     */
    public ExportAllJobsDocAction() {
        super();

        setText("Export all jobs documentation");
        setToolTipText("Export all jobs documentation");
        setImageDescriptor(ImageProvider.getImageDesc(ECoreImage.DOCUMENTATION_ICON));
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.commons.ui.swt.actions.ITreeContextualAction#init(org.eclipse.jface.viewers.TreeViewer,
     * org.eclipse.jface.viewers.IStructuredSelection)
     */
    public void init(TreeViewer viewer, IStructuredSelection selection) {
        boolean canWork = false;
        if (selection.isEmpty() || selection.size() > 1) {
            canWork = false;
        } else if (ProxyRepositoryFactory.getInstance().isUserReadOnlyOnCurrentProject()) {
            canWork = false;
        } else {
            Object o = selection.getFirstElement();
            RepositoryNode node = (RepositoryNode) o;
            Object property = node.getProperties(EProperties.CONTENT_TYPE);
            if (((ERepositoryObjectType) property) == ERepositoryObjectType.JOBS
            // avoid to add this action on the sub-folder of "Jobs" node.
                    && (node.getProperties(EProperties.LABEL).equals(ERepositoryObjectType.JOBS.toString()))) {
                canWork = true;
            }
        }
        setEnabled(canWork);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.action.Action#run()
     */
    public void run() {
        System.out.println("run.. in ExportAllJobsDocAction");
    }

}
