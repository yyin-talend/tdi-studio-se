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
package org.talend.sqlbuilder.erdiagram.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * DOC yzhang class global comment. Detailled comment <br/>
 * 
 * $Id: talend.epf 1 2006-09-29 17:06:40 +0000 (ææäº, 29 ä¹æ 2006) nrousseau $
 * 
 */
public class ErElement {

    private PropertyChangeSupport listeners = new PropertyChangeSupport(this);

    /**
     * DOC yzhang Comment method "firePropertyChange".
     * 
     * @param prop
     * @param old
     * @param newValue
     */
    protected void firePropertyChange(String prop, Object old, Object newValue) {
        listeners.firePropertyChange(prop, old, newValue);
    }

    /**
     * DOC yzhang Comment method "fireStructureChange".
     * 
     * @param prop
     * @param child
     */
    protected void fireStructureChange(String prop, Object child) {
        listeners.firePropertyChange(prop, null, child);
    }

    /**
     * DOC yzhang Comment method "addPropertyChangeListener".
     * 
     * @param listener
     */
    protected void addPropertyChangeListener(PropertyChangeListener listener) {
        listeners.addPropertyChangeListener(listener);
    }

    /**
     * DOC yzhang Comment method "removePropertyChangeListener".
     * 
     * @param listener
     */
    protected void removePropertyChangeListener(PropertyChangeListener listener) {
        listeners.removePropertyChangeListener(listener);
    }

}
