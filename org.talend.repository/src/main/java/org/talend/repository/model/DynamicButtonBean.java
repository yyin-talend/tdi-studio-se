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
package org.talend.repository.model;

import org.eclipse.swt.events.SelectionListener;

/**
 */
public class DynamicButtonBean {

    private String id;
    private String name;
    private SelectionListener listener;

    public DynamicButtonBean(String id, String name, SelectionListener listener) {
        this.id = id;
        this.name = name;
        this.listener = listener;
    }

    
    public String getId() {
        return id;
    }

    
    public void setId(String id) {
        this.id = id;
    }

    
    public String getName() {
        return name;
    }

    
    public void setName(String name) {
        this.name = name;
    }


    
    public SelectionListener getListener() {
        return listener;
    }


    
    public void setListener(SelectionListener listener) {
        this.listener = listener;
    }
    
    

}
