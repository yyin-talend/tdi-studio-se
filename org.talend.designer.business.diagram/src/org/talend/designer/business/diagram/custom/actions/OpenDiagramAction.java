// ============================================================================
//
// Talend Community Edition
//
// Copyright (C) 2006 Talend – www.talend.com
//
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either
// version 2.1 of the License, or (at your option) any later version.
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
package org.talend.designer.business.diagram.custom.actions;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.talend.commons.ui.image.ImageProvider;
import org.talend.core.model.properties.BusinessProcessItem;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.IRepositoryObject;
import org.talend.core.model.repository.RepositoryObject;
import org.talend.core.ui.images.ECoreImage;
import org.talend.repository.model.RepositoryNode;
import org.talend.repository.model.RepositoryNode.EProperties;
import org.talend.repository.ui.actions.AContextualAction;

/**
 * DOC mhelleboid class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 */
public class OpenDiagramAction extends AContextualAction {

    public OpenDiagramAction() {
        super();
        setImageDescriptor(ImageProvider.getImageDesc(ECoreImage.BUSINESS_PROCESS_ICON));
        setText("Edit Business Model");
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.action.Action#run()
     */
    public void run() {
        ISelection selection = getSelection();
        Object obj = ((IStructuredSelection) selection).getFirstElement();
        if (obj instanceof RepositoryNode) {
            RepositoryNode repositoryNode = (RepositoryNode) obj;
            IRepositoryObject repositoryObject = repositoryNode.getObject();

            if (repositoryObject instanceof RepositoryObject) {
                RepositoryObject abstractRepositoryObject = (RepositoryObject) repositoryObject;

                BusinessProcessItem businessProcessItem = (BusinessProcessItem) abstractRepositoryObject.getProperty().getItem();
                DiagramResourceManager diagramResourceManager = new DiagramResourceManager(getActivePage(),
                        new NullProgressMonitor());
                IFile file = diagramResourceManager.createDiagramFile();
                diagramResourceManager.updateResource(businessProcessItem, file);
                diagramResourceManager.openEditor(businessProcessItem, file);
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.ui.actions.ITreeContextualAction#init(org.eclipse.jface.viewers.TreeViewer,
     * org.eclipse.jface.viewers.IStructuredSelection)
     */
    public void init(TreeViewer viewer, IStructuredSelection selection) {
        boolean enabled = false;

        if (!selection.isEmpty() && selection.size() == 1) {
            Object object = selection.getFirstElement();
            if (object instanceof RepositoryNode) {
                RepositoryNode repositoryNode = (RepositoryNode) object;
                ERepositoryObjectType nodeType = (ERepositoryObjectType) repositoryNode.getProperties(EProperties.CONTENT_TYPE);
                if (repositoryNode.getType() == RepositoryNode.ENodeType.REPOSITORY_ELEMENT) {
                    if (nodeType == ERepositoryObjectType.BUSINESS_PROCESS) {
                        enabled = true;
                    }
                }
            }
        }
        setEnabled(enabled);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.ui.actions.AContextualView#getClassForDoubleClick()
     */
    @Override
    public Class getClassForDoubleClick() {
        return BusinessProcessItem.class;
    }
}
