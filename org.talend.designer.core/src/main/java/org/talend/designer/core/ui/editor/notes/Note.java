// ============================================================================
//
// Copyright (C) 2006-2007 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
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
