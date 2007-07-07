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
package org.talend.designer.fileoutputxml.data;

/**
 * bqian XML attribute structure. <br/>
 * 
 * $Id: Attribute.java,v 1.1 2007/06/12 07:20:39 gke Exp $
 * 
 */
public class Attribute extends FOXTreeNode {

    /**
     * Attribute constructor comment.
     */
    public Attribute() {
    }
    
    /**
     * Attribute constructor comment.
     */
    public Attribute(String label) {
        setLabel(label);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.fileoutputxml.data.FOXTreeNode#hasChildren()
     */
    @Override
    public boolean hasChildren() {
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.fileoutputxml.data.FOXTreeNode#getValue()
     */
    @Override
    public String getLabelForViewer() {
        return "@" + super.getLabel(); //$NON-NLS-1$
    }

    @Override
    public boolean isGroup() {
        return false;
    }

    @Override
    public boolean isLoop() {
        return false;
    }
    
    
}
