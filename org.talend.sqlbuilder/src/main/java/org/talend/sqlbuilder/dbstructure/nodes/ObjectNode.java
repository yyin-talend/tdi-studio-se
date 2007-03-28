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
package org.talend.sqlbuilder.dbstructure.nodes;

import org.eclipse.swt.graphics.Image;


/**
 * DOC dev  class global comment. Detailled comment
 * <br/>
 *
 * $Id: ObjectNode.java,v 1.3 2006/11/01 05:40:59 peiqin.hou Exp $
 *
 */
public class ObjectNode extends AbstractNode {

    private String ptype;
    
    
    /**
     * Hidden default constructor.
     */
    private ObjectNode() {
        
    }
    
    public ObjectNode(String name, String type, INode parent, Image image) {
        ptype = type;
        pname = name;
        psessionNode = parent.getSession();
        pparent = parent;
        pimage = image;
    }
    

    /**
     * This node cannot have childnodes.
     * @return isEndNode.
     */
    public boolean isEndNode() {
        return true;
    }

    /**
     * This node cannot have childnodes.
     */
    public void loadChildren() {
        return;
    }

    /**
     * @return Type.
     */
    public String getType() {
        return ptype;
    }
    
    /**
     * @return QualifiedName.
     */
    public String getQualifiedName() {
        return "\"" + getSchemaOrCatalogName() + "\".\"" + getName() + "\""; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    }

}
