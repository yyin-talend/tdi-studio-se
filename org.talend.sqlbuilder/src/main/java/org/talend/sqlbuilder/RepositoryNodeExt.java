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
package org.talend.sqlbuilder;

import org.talend.core.model.repository.IRepositoryObject;
import org.talend.repository.model.RepositoryNode;

/**
 * qzhang class global comment. Detailled comment <br/>
 * 
 */
public class RepositoryNodeExt extends RepositoryNode {

    /**
     * qzhang RepositoryNodeExt constructor comment.
     * 
     * @param object
     * @param parent
     * @param type
     */
    public RepositoryNodeExt(IRepositoryObject object, RepositoryNode parent, ENodeType type) {
        super(object, parent, type);
    }

    /* (non-Javadoc)
     * @see org.talend.repository.model.RepositoryNode#hashCode()
     */
    @Override
    public int hashCode() {
        return super.hashCode();
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.RepositoryNode#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        return (this == obj);
    }

}
