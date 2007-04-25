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

import org.eclipse.jface.viewers.IFilter;
import org.talend.repository.model.RepositoryNode;
import org.talend.repository.model.RepositoryNode.ENodeType;

/**
 * DOC mhelleboid class global comment. Detailled comment <br/>
 * 
 * $Id: SectionFilter.java 295 2006-11-02 08:28:03 +0000 (jeu., 02 nov. 2006) smallet $
 * 
 */
public class ExcludeFolderSectionFilter extends SectionFilter implements IFilter {

    public boolean select(Object object) {
        RepositoryNode node = getRepositoryNode(object);

        if (node == null) {
            return false;
        }

        if (node.getType() == ENodeType.STABLE_SYSTEM_FOLDER || node.getType() == ENodeType.SYSTEM_FOLDER
                || node.getType() == ENodeType.SIMPLE_FOLDER) {
            return false;
        }

        return true;
    }

}
