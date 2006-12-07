// ============================================================================
//
// Talend Community Edition
//
// Copyright (C) 2006 Talend - www.talend.com
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
package org.talend.repository.ui.properties;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.talend.core.ui.images.ImageProvider;
import org.talend.repository.model.RepositoryNode;

/**
 * DOC mhelleboid class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 */
public class PropsTitleLabelProvider extends LabelProvider {

    @Override
    public String getText(Object element) {
        RepositoryNode repositoryNode = getRepositoryNode(element);
        return repositoryNode.getLabel();
    }

    @Override
    public Image getImage(Object element) {
        RepositoryNode repositoryNode = getRepositoryNode(element);
        return ImageProvider.getImage(repositoryNode.getIcon());
    }

    private RepositoryNode getRepositoryNode(Object element) {
        if (element instanceof IStructuredSelection) {
            Object firstElement = ((IStructuredSelection) element).getFirstElement();
            if (firstElement instanceof RepositoryNode) {
                return (RepositoryNode) firstElement;
            }
        }
        return null;
    }

}
