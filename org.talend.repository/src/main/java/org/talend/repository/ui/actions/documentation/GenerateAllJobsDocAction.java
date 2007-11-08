// ============================================================================
//
// Talend Community Edition
//
// Copyright (C) 2006-2007 Talend - www.talend.com
//
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either
// version 2.1 of the License.
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
package org.talend.repository.ui.actions.documentation;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.talend.commons.ui.image.ImageProvider;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.ui.images.ECoreImage;
import org.talend.repository.model.ProxyRepositoryFactory;
import org.talend.repository.model.RepositoryNode;
import org.talend.repository.model.RepositoryNode.ENodeType;
import org.talend.repository.model.RepositoryNode.EProperties;
import org.talend.repository.ui.actions.AContextualAction;

/**
 * DOC ftang class global comment. Detailled comment <br/>
 * 
 * $Id: talend.epf 1 2006-09-29 17:06:40 +0000 (ææäº, 29 ä¹æ 2006) nrousseau $
 * 
 */
public class GenerateAllJobsDocAction extends AContextualAction {

    /**
     * Constructs a new ExportAllJobsDocAction.
     */
    public GenerateAllJobsDocAction() {
        super();

        setText("Generate all jobs documentation");
        setToolTipText("Generate all jobs documentation");
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

    public void run() {
        System.out.println("run.. in GenerateAllJobsDocAction");
    }

}
