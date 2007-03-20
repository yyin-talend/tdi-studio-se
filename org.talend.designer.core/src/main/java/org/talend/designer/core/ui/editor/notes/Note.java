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
package org.talend.designer.core.ui.editor.notes;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.talend.core.model.process.Element;
import org.talend.designer.core.i18n.Messages;
import org.talend.designer.core.ui.editor.TalendEditor;

/**
 * 
 */
public class Note extends Element {

    private Point location;

    private Dimension size = new Dimension(TalendEditor.GRID_SIZE * 3, TalendEditor.GRID_SIZE * 2);
    
    private boolean opaque = true;

    private String text = Messages.getString("Note.DefaultText"); //$NON-NLS-1$

    private boolean readOnly;

    @Override
    public String getElementName() {
        return null;
    }
    
    public boolean isReadOnly() {
        return readOnly;
    }

    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
    }

    public Point getLocation() {
        return location;
    }

    public void setLocation(Point location) {
        this.location = location;
        firePropertyChange("", null, location); //$NON-NLS-1$
    }

    public Dimension getSize() {
        return size;
    }

    public void setSize(Dimension size) {
        this.size = size;
        firePropertyChange("", null, size); //$NON-NLS-1$
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
        firePropertyChange("", null, text); //$NON-NLS-1$
    }

    public boolean isOpaque() {
        return opaque;
    }
    
    public void setOpaque(boolean opaque) {
        this.opaque = opaque;
        firePropertyChange("", null, opaque); //$NON-NLS-1$
    }
}
