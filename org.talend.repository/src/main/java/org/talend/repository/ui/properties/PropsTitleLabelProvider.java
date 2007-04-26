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
package org.talend.repository.ui.properties;

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.talend.commons.ui.image.ImageProvider;
import org.talend.repository.RepositoryPlugin;
import org.talend.repository.model.RepositoryNode;

/**
 * DOC mhelleboid class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 */
public class PropsTitleLabelProvider extends LabelProvider {

    // The TabbedPropertySheetPage(RepositoryView.java row 417 ) and TalendTabbedPropertySheetPage(TalendEditor row 428)
    // will use the same TabbedPropertyRegistry(TabbedPropertyRegistryFactory.java row 69) with the same LabelProvider.
    // So the GefEditorLabelProvider and the PropsTitleLabelProvider will be confilcted.
    // See issue 973 and commitment 3231
    // BTW,I feel this adapter kind of solution is not good.
    // by bqian 26 April 2007
    private ILabelProvider gefEditorNodeLabelProvider = RepositoryPlugin.getDefault().getDesignerCoreService()
            .getGEFEditorNodeLabelProvider();

    @Override
    public String getText(Object element) {
        RepositoryNode repositoryNode = getRepositoryNode(element);
        if (repositoryNode != null) {
            return repositoryNode.getLabel();
        }
        return gefEditorNodeLabelProvider.getText(element);
    }

    @Override
    public Image getImage(Object element) {
        RepositoryNode repositoryNode = getRepositoryNode(element);
        if (repositoryNode != null) {
            return ImageProvider.getImage(repositoryNode.getIcon());
        }

        return gefEditorNodeLabelProvider.getImage(element);
    }

    private RepositoryNode getRepositoryNode(Object element) {
        if (element instanceof IStructuredSelection) {
            Object firstElement = ((IStructuredSelection) element).getFirstElement();
            return SectionFilter.getRepositoryNode(firstElement);
        }
        return null;
    }

}
