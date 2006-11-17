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
package org.talend.designer.core.ui.editor.connections;

import java.util.List;

import org.eclipse.draw2d.geometry.Dimension;
import org.talend.core.model.process.Element;
import org.talend.core.model.process.IElementParameter;

/**
 * Label object of a connection. This is the model part of the Gef item. <br/>
 * 
 * $Id$
 * 
 */
public class ConnectionTrace extends Element {

    public static final String TRACE_PROP = "traceChange"; //$NON-NLS-1$

    private String trace = null; //$NON-NLS-1$

    private Connection connection = null;

    private Dimension size;

    public ConnectionTrace(Connection connection) {
        this.connection = connection;
    }

    public void setTrace(String trace) {
        this.trace = trace;
        firePropertyChange(TRACE_PROP, null, null);
    }

    public String getTrace() {
        return trace;
    }

    public Dimension getSize() {
        return this.size;
    }

    public void setSize(Dimension size) {
        this.size = size;
    }

    /**
     * Get the connection parent of the label.
     * 
     * @return Connection
     */
    public Connection getConnection() {
        return connection;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.core.ui.editor.Element#setPropertyValue(java.lang.Object, java.lang.Object)
     */
    public void setPropertyValue(String id, Object value) {
        connection.setPropertyValue(id, value);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.core.ui.editor.Element#getPropertyValue(java.lang.Object)
     */
    public Object getPropertyValue(String id) {
        return connection.getPropertyValue(id);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.core.ui.editor.Element#getElementName()
     */
    @Override
    public String getElementName() {
        return connection.getElementName();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.core.ui.editor.Element#getElementParameters()
     */
    @Override
    public List<? extends IElementParameter> getElementParameters() {
        return connection.getElementParameters();
    }

    public IElementParameter getElementParameter(String name) {
        return connection.getElementParameter(name);
    }

    public boolean isReadOnly() {
        return connection.isReadOnly();
    }

    public void setReadOnly(boolean readOnly) {
    }
}
