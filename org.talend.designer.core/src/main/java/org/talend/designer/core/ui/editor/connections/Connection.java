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

import org.apache.commons.lang.ObjectUtils;
import org.talend.core.model.metadata.IMetadataTable;
import org.talend.core.model.process.EComponentCategory;
import org.talend.core.model.process.EConnectionType;
import org.talend.core.model.process.EParameterFieldType;
import org.talend.core.model.process.Element;
import org.talend.core.model.process.IConnection;
import org.talend.core.model.process.IElementParameter;
import org.talend.designer.core.model.components.EParameterName;
import org.talend.designer.core.model.components.ElementParameter;
import org.talend.designer.core.ui.editor.nodes.Node;

/**
 * Class that define the connection. It's the model part of the Gef element <br/>
 * 
 * $Id$
 * 
 */
public class Connection extends Element implements IConnection {

    public static final String NAME = "name"; //$NON-NLS-1$

    public static final String LINESTYLE_PROP = "LineStyle"; //$NON-NLS-1$

    private EConnectionType lineStyle = EConnectionType.FLOW_MAIN;

    private boolean isConnected;

    private Node target;

    private Node source;

    protected String name;

    private ConnectionLabel label;

    private ConnectionTrace trace;

    private String metaName;

    // true if this connection is activated.
    private boolean activate = true;

    private boolean readOnly = false;

    private String traceData;

    /**
     * The connection can be created with a given node source, target and line style The line style are described in the
     * IConnectionType interface.
     * 
     * @see org.talend.designer.core.ui.editor.connections.EConnectionType
     * @param source
     * @param target
     * @param lineStyle
     * @param meta
     */
    public Connection(Node source, Node target, EConnectionType lineStyle, String metaName, String linkName) {
        this.lineStyle = lineStyle;
        this.metaName = metaName;
        if (lineStyle.equals(EConnectionType.FLOW_MAIN) || lineStyle.equals(EConnectionType.FLOW_REF)) {
            trace = new ConnectionTrace(this);
        }
        setName(linkName);
        reconnect(source, target);
        if (lineStyle.equals(EConnectionType.RUN_IF)) {
            IElementParameter param = new ElementParameter(this);
            param.setField(EParameterFieldType.MEMO_PERL);
            param.setCategory(EComponentCategory.MAIN);
            param.setValue(""); //$NON-NLS-1$
            param.setNbLines(5);
            param.setName(EParameterName.CONDITION.getName());
            param.setDisplayName(EParameterName.CONDITION.getDisplayName());
            param.setShow(true);
            param.setNumRow(1);
            addElementParameter(param);
        }
    }

    @Override
    public String toString() {
        return "Name=" + getName() + ", Table=" + getMetadataTable(); //$NON-NLS-1$ //$NON-NLS-2$
    }

    /**
     * Get the name/label of the connection.
     * 
     * @return Connection Name
     */
    public String getName() {
        return name;
    }

    /**
     * Set the name/label of the connection.
     * 
     * @param name
     */
    public void setName(String name) {
        String labelText;

        this.name = name;

        labelText = name;
        if (getLineStyle().equals(EConnectionType.FLOW_MAIN)) {
            labelText += " (" + EDesignerConnection.FLOW_MAIN.getLinkName() + ")"; //$NON-NLS-1$ //$NON-NLS-2$
        }
        if (getLineStyle().equals(EConnectionType.FLOW_REF)) {
            labelText += " (" + EDesignerConnection.FLOW_REF.getLinkName() + ")"; //$NON-NLS-1$ //$NON-NLS-2$
        }
        if (getLineStyle().equals(EConnectionType.TABLE)) {
            labelText += " (" + EDesignerConnection.TABLE.getLinkName() + ")"; //$NON-NLS-1$ //$NON-NLS-2$
        }
        if (label == null) {
            label = new ConnectionLabel(labelText, this);
        } else {
            if (!label.getLabelText().equals(labelText)) {
                label.setLabelText(labelText);
            }
        }

        if (source != null
                && (lineStyle == EConnectionType.FLOW_MAIN || lineStyle == EConnectionType.FLOW_REF || lineStyle == EConnectionType.TABLE)) {
            if (source.getConnectorFromType(lineStyle).isBuiltIn()) {
                IMetadataTable table = getMetadataTable();
                table.setTableName(name);
                metaName = name;
            }
        }

        firePropertyChange(NAME, null, name);
    }

    public ConnectionTrace getConnectionTrace() {
        return trace;
    }

    public void setTraceData(String traceData) {
        String oldData = this.traceData;
        if (!ObjectUtils.equals(oldData, traceData)) {
            this.traceData = traceData;
            trace.setTrace(traceData);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.core.ui.editor.connections.IDesignerConnection#getTarget()
     */
    public Node getTarget() {
        return this.target;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.core.ui.editor.connections.IDesignerConnection#getSource()
     */
    public Node getSource() {
        return this.source;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.core.ui.editor.connections.IDesignerConnection#getConnectionLabel(java.lang.String)
     */
    public ConnectionLabel getConnectionLabel() {
        return label;
    }

    /**
     * Reconnect the connection Used after delete a connection, for the undo command and when the connection is
     * connected to new source or target.
     */
    public void reconnect() {
        if (!isConnected) {
            // if (!source.getConnectorFromType(EConnectionType.FLOW_MAIN).isBuiltIn()) {
            if (lineStyle.equals(EConnectionType.FLOW_MAIN) || lineStyle.equals(EConnectionType.FLOW_REF)) {
                if (source.getProcess().checkValidConnectionName(name)) {
                    source.getProcess().addUniqueConnectionName(this.name);
                }
                // }
            }
            source.addOutput(this);
            target.addInput(this);
            isConnected = true;
        }
    }

    /**
     * Disconnect the connection This function is used before delete or reconnect a connection.
     */
    public void disconnect() {
        if (isConnected) {
            if (!source.getConnectorFromType(EConnectionType.FLOW_MAIN).isBuiltIn()) {
                if (lineStyle.equals(EConnectionType.FLOW_MAIN) || lineStyle.equals(EConnectionType.FLOW_REF)) {
                    source.getProcess().removeUniqueConnectionName(this.name);
                }
            }
            source.removeOutput(this);
            target.removeInput(this);
            isConnected = false;
        }
    }

    /**
     * Reconnect a connection to a new source and target.
     * 
     * @param newSource
     * @param newTarget
     */
    public void reconnect(Node newSource, Node newTarget) {
        disconnect();
        this.source = newSource;
        this.target = newTarget;
        reconnect();
    }

    /**
     * Set a new style for a given line.
     * 
     * @see org.talend.designer.core.ui.editor.connections.IConnectionType
     * @param lineStyle
     */
    public void setLineStyle(EConnectionType lineStyle) {
        this.lineStyle = lineStyle;
        setName(name);
        firePropertyChange(LINESTYLE_PROP, null, lineStyle);
    }

    /**
     * Return the given style of the connection.
     * 
     * @see org.talend.designer.core.ui.editor.connections.EConnectionType
     * @return int value of the style
     */
    public int getLineStyleId() {
        return lineStyle.getId();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.core.ui.editor.connections.IDesignerConnection#getLineStyle()
     */
    public EConnectionType getLineStyle() {
        return lineStyle;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.core.ui.editor.Element#setPropertyValue(java.lang.Object, java.lang.Object)
     */
    public void setPropertyValue(String id, Object value) {
        if (id.equals(LINESTYLE_PROP)) {
            setLineStyle((EConnectionType) value);
        } else {
            if (id.equals(NAME)) {
                setName((String) value);
            } else {
                super.setPropertyValue(id, value);
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.core.ui.editor.Element#getPropertyValue(java.lang.Object)
     */
    public Object getPropertyValue(String id) {
        if (id.equals(LINESTYLE_PROP)) {
            return getLineStyle();
        } else if (NAME.equals(id)) {
            return getName();
        }
        return super.getPropertyValue(id);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.core.ui.editor.connections.IDesignerConnection#getMetadataTable()
     */
    public IMetadataTable getMetadataTable() {
        if (source != null) {
            return source.getMetadataTable(metaName);
        }
        return null;
    }

    public void setMetaName(String metaName) {
        this.metaName = metaName;
    }

    public String getMetaName() {
        return metaName;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.core.ui.editor.Element#getElementName()
     */
    @Override
    public String getElementName() {
        return lineStyle.getName();
    }

    public boolean isActivate() {
        return this.activate;
    }

    public void setActivate(boolean activate) {
        this.activate = activate;
        /*
         * if (target.getProcess() != null) { if (target.getProcess().isActivate()) { if (target.isActivate() !=
         * activate) { target.setActivate(activate); } } if (source.isActivate() != activate) { if
         * (lineStyle.equals(EConnectionType.FLOW_MAIN) || lineStyle.equals(EConnectionType.FLOW_REF)) {
         * source.setActivate(activate); } } }
         */
        firePropertyChange(EParameterName.ACTIVATE.getName(), null, null);
    }

    public String getCondition() {
        if (lineStyle.equals(EConnectionType.RUN_IF)) {
            return (String) getPropertyValue(EParameterName.CONDITION.getName());
        } else {
            return null;
        }

    }

    public boolean isReadOnly() {
        return this.readOnly;
    }

    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
    }
}
