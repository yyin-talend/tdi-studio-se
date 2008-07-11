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

import java.util.List;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.talend.core.model.process.EComponentCategory;
import org.talend.core.model.process.EParameterFieldType;
import org.talend.core.model.process.Element;
import org.talend.designer.core.i18n.Messages;
import org.talend.designer.core.model.components.EParameterName;
import org.talend.designer.core.model.components.ElementParameter;
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

    private List elementParameter;

    private String noteColor = "255;255;203";

    private String textColor = "0;0;0";

    /**
     * tang Note constructor comment.
     */
    public Note() {
        super();
        ElementParameter param = new ElementParameter(this);
        param = new ElementParameter(this);
        param.setName(EParameterName.NOTE_COLOR.getName());
        param.setValue(noteColor); // default note color
        param.setDisplayName(EParameterName.NOTE_COLOR.getDisplayName());
        param.setField(EParameterFieldType.COLOR);
        param.setCategory(EComponentCategory.BASIC);
        param.setNumRow(2);
        param.setReadOnly(false);
        param.setRequired(true);
        param.setShow(true);
        addElementParameter(param);
        param = new ElementParameter(this);
        param.setName(EParameterName.NOTETXT_COLOR.getName());
        param.setValue(textColor); // default note color
        param.setDisplayName(EParameterName.NOTETXT_COLOR.getDisplayName());
        param.setField(EParameterFieldType.COLOR);
        param.setCategory(EComponentCategory.BASIC);
        param.setNumRow(3);
        param.setReadOnly(false);
        param.setRequired(true);
        param.setShow(true);
        addElementParameter(param);
        param = new ElementParameter(this);
        param.setName(EParameterName.NOTETXT_LEFT.getName());
        param.setValue(false);
        param.setDisplayName(EParameterName.NOTETXT_LEFT.getDisplayName());
        param.setField(EParameterFieldType.RADIO);
        param.setCategory(EComponentCategory.BASIC);
        param.setNumRow(4);
        addElementParameter(param);
        param = new ElementParameter(this);
        param.setName(EParameterName.NOTETXT_RIGHT.getName());
        param.setValue(false);
        param.setDisplayName(EParameterName.NOTETXT_RIGHT.getDisplayName());
        param.setField(EParameterFieldType.RADIO);
        param.setCategory(EComponentCategory.BASIC);
        param.setNumRow(4);
        addElementParameter(param);
        param = new ElementParameter(this);
        param.setName(EParameterName.NOTETXT_CENTER.getName());
        param.setValue(true);
        param.setDisplayName(EParameterName.NOTETXT_CENTER.getDisplayName());
        param.setField(EParameterFieldType.RADIO);
        param.setCategory(EComponentCategory.BASIC);
        param.setNumRow(4);
        addElementParameter(param);
        param = new ElementParameter(this);
        param.setName(EParameterName.NOTELABEL_CENTER.getName());
        param.setValue(true);
        param.setDisplayName(EParameterName.NOTELABEL_CENTER.getDisplayName());
        param.setField(EParameterFieldType.RADIO);
        param.setCategory(EComponentCategory.BASIC);
        param.setNumRow(4);
        addElementParameter(param);
        param = new ElementParameter(this);
        param.setName(EParameterName.NOTETXT_TOP.getName());
        param.setValue(false);
        param.setDisplayName(EParameterName.NOTETXT_TOP.getDisplayName());
        param.setField(EParameterFieldType.RADIO);
        param.setCategory(EComponentCategory.BASIC);
        param.setNumRow(4);
        addElementParameter(param);
        param = new ElementParameter(this);
        param.setName(EParameterName.NOTETXT_BOTTOM.getName());
        param.setValue(false);
        param.setDisplayName(EParameterName.NOTETXT_BOTTOM.getDisplayName());
        param.setField(EParameterFieldType.RADIO);
        param.setCategory(EComponentCategory.BASIC);
        param.setNumRow(4);
        addElementParameter(param);
    }

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

    /**
     * Getter for elementParameter.
     * 
     * @return the elementParameter
     */
    public List getElementParameter() {
        return this.elementParameter;
    }

    /**
     * Sets the elementParameter.
     * 
     * @param elementParameter the elementParameter to set
     */
    public void setElementParameter(List elementParameter) {
        this.elementParameter = elementParameter;
    }

    /**
     * Getter for process.
     * 
     * @return the process
     */
    // public IProcess2 getProcess() {
    // return this.process;
    // }
    public void refresh() {
        firePropertyChange("", null, "");
    }
}
