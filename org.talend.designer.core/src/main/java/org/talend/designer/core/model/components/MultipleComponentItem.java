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
package org.talend.designer.core.model.components;

import java.util.ArrayList;
import java.util.List;

import org.talend.core.model.components.IMultipleComponentConnection;
import org.talend.core.model.components.IMultipleComponentItem;

/**
 * DOC nrousseau class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 */
public class MultipleComponentItem implements IMultipleComponentItem {

    String name;

    String component;

    List<IMultipleComponentConnection> outputConnections = new ArrayList<IMultipleComponentConnection>();

    List<IMultipleComponentConnection> inputConnections = new ArrayList<IMultipleComponentConnection>();

    public List<IMultipleComponentConnection> getOutputConnections() {
        return this.outputConnections;
    }

    public List<IMultipleComponentConnection> getInputConnections() {
        return this.inputConnections;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.core.model.components.IMultipleComponentItem#getName()
     */
    public String getName() {
        return this.name;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.core.model.components.IMultipleComponentItem#setName(java.lang.String)
     */
    public void setName(String name) {
        this.name = name;
    }

    public String getComponent() {
        return this.component;
    }

    public void setComponent(String component) {
        this.component = component;
    }
}
