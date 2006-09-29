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

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;

import org.talend.core.model.process.IElement;
import org.talend.core.model.process.IElementParameter;

/**
 * Abstract base class of elements in the model. All elements in the diagram must extends this class <br/>
 * 
 * $Id$
 * 
 */

public abstract class Element implements Cloneable, IElement {

    public static final int ALPHA_VALUE = 50;

    private List<IElementParameter> listParam = new ArrayList<IElementParameter>();

    // property change listeners
    protected PropertyChangeSupport listeners = new PropertyChangeSupport(this);

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        listeners.addPropertyChangeListener(listener);
    }

    protected void firePropertyChange(String prop, Object old, Object newValue) {
        listeners.firePropertyChange(prop, old, newValue);
    }

    protected void fireStructureChange(String prop, Object child) {
        listeners.firePropertyChange(prop, null, child);
    }

    // implemented in order to create listeners field
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        listeners = new PropertyChangeSupport(this);
    }

    public void removePropertyChangeListener(PropertyChangeListener l) {
        listeners.removePropertyChangeListener(l);
    }

    /**
     * Gives the value of the given property.
     * 
     * @param id
     * @return Object
     */
    public Object getPropertyValue(final String id) {
        for (int i = 0; i < listParam.size(); i++) {
            if (listParam.get(i).getName().equals(id)) {
                return listParam.get(i).getValue();
            }
        }
        return null;
    }

    /**
     * Set the property of the object.
     * 
     * @param id
     * @param value
     */
    public void setPropertyValue(final String id, final Object value) {
        for (int i = 0; i < listParam.size(); i++) {
            if (listParam.get(i).getName().equals(id)) {
                listParam.get(i).setValue(value);
            }
        }
    }

    public void addElementParameter(IElementParameter parameter) {
        listParam.add(parameter);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.core.ui.editor.IElement#getElementParameters()
     */
    public List<? extends IElementParameter> getElementParameters() {
        return listParam;
    }

    @SuppressWarnings("unchecked")
    public void setElementParameters(List<? extends IElementParameter> parameters) {
        this.listParam = (List<IElementParameter>) parameters;
    }

    public IElementParameter getElementParameter(String name) {
        for (IElementParameter elementParam : listParam) {
            if (elementParam.getName().equals(name)) {
                return elementParam;
            }
        }
        return null;
    }

    public abstract String getElementName();
}
