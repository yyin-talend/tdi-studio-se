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

import java.util.ArrayList;
import java.util.List;

/**
 * bqian XML Element structure. <br/>
 * 
 * $Id: Element.java,v 1.1 2007/06/12 07:20:39 gke Exp $
 * 
 */
public class Element extends FOXTreeNode {

    private List<Attribute> attributes = new ArrayList<Attribute>();

    /**
     * Element constructor comment.
     */
    public Element() {
        super();
    }
    /**
     * Element constructor comment.
     */
    public Element(String label) {
        super(label);
    }

    public void removeChild(FOXTreeNode child) {
        child.setParent(null);
        if (child instanceof Attribute) {
            Attribute element = (Attribute) child;
            attributes.remove(element);
            return;
        }
        super.removeChild(child);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.fileoutputxml.data.FOXTreeNode#hasChildren()
     */
    @Override
    public boolean hasChildren() {
        return getChildren().size() + attributes.size() > 0;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.fileoutputxml.data.FOXTreeNode#addChild(org.talend.designer.fileoutputxml.data.FOXTreeNode)
     */
    @Override
    public void addChild(FOXTreeNode child) {
        if (child instanceof Attribute) {
            Attribute element = (Attribute) child;
            attributes.add(element);
            child.setParent(this);
            return;
        }
        super.addChild(child);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.fileoutputxml.data.FOXTreeNode#getChildren()
     */
    @Override
    public List<FOXTreeNode> getChildren() {
        List<FOXTreeNode> list = new ArrayList<FOXTreeNode>();
        list.addAll(attributes);
        list.addAll(super.getChildren());

        return list;
    }

    public List<FOXTreeNode> getElementChildren() {
        return super.getChildren();
    }
    
    public List<FOXTreeNode> getAttributeChildren() {
        List<FOXTreeNode> list = new ArrayList<FOXTreeNode>();
        list.addAll(attributes);

        return list;
    }

}
