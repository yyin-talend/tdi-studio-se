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
package org.talend.designer.core.ui.editor;

import org.eclipse.gef.requests.CreationFactory;

/**
 * Factory used to create elements in the diagram. <br/>
 * 
 * $Id$
 * 
 */
public class ElementFactory implements CreationFactory {

    private Object template;

    public ElementFactory(Object template) {
        this.template = template;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.gef.requests.CreationFactory#getNewObject()
     */
    public Object getNewObject() {
        try {
            return ((Class) template).newInstance();
        } catch (Exception e) {
            return null;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.gef.requests.CreationFactory#getObjectType()
     */
    public Object getObjectType() {
        return template;
    }
}
