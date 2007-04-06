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
package org.talend.designer.core.ui.editor.connections;

import java.util.ArrayList;
import java.util.List;

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

    private String uniqueName;

    // true if this connection is activated.
    private boolean activate = true;

    private boolean readOnly = false;

    private String traceData;

    public Connection(Node source, Node target, EConnectionType lineStyle, String metaName, String linkName,
            String uniqueName) {
        this.uniqueName = uniqueName;
        init(source, target, lineStyle, metaName, linkName);
    }

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
        init(source, target, lineStyle, metaName, linkName);
    }

    private void init(Node source, Node target, EConnectionType lineStyle, String metaName, String linkName) {
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
     * 
     * Only works for FLOW_MAIN, FLOW_REF or TABLE link.
     * 
     * @return
     */
    public String getUniqueName() {
        // if (source != null) {
        // if (source.getConnectorFromType(lineStyle).isBuiltIn()) {
        // return metaName;
        // }
        // }
        return uniqueName;
    }

    public void setUniqueName(String uniqueName) {
        this.uniqueName = uniqueName;
    }

    /**
     * Set the name/label of the connection.
     * 
     * @param name
     */
    public void setName(String name) {
        String labelText;

        this.name = name;
        if (!lineStyle.equals(EConnectionType.TABLE)) {
            uniqueName = name;
        }

        labelText = name;
        if (getLineStyle().equals(EConnectionType.FLOW_MAIN)) {
            labelText += " (" + EDesignerConnection.FLOW_MAIN.getLinkName() + ")"; //$NON-NLS-1$ //$NON-NLS-2$
        }
        if (getLineStyle().equals(EConnectionType.FLOW_REF)) {
            labelText += " (" + EDesignerConnection.FLOW_REF.getLinkName() + ")"; //$NON-NLS-1$ //$NON-NLS-2$
        }
        if (source != null) {
            if (getLineStyle().equals(EConnectionType.TABLE)) {
                if (getOutputId() >= 0) {
                    labelText += " (" + EDesignerConnection.TABLE.getLinkName() + " :" + getOutputId() + ")"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                } else {
                    labelText += " (" + EDesignerConnection.TABLE.getLinkName() + ")"; //$NON-NLS-1$ //$NON-NLS-2$
                }
            }
        }
        if (label == null) {
            label = new ConnectionLabel(labelText, this);
        } else {
            if (!label.getLabelText().equals(labelText)) {
                label.setLabelText(labelText);
            }
        }

        if (source != null && (lineStyle == EConnectionType.FLOW_MAIN || lineStyle == EConnectionType.FLOW_REF)) {
            if (source.getConnectorFromType(lineStyle).isBuiltIn()) {
                IMetadataTable table = getMetadataTable();
                table.setTableName(name);
                // metaName = name;
            }
        }

        if (source != null && (lineStyle == EConnectionType.TABLE)) {
            IMetadataTable table = getMetadataTable();
            table.setLabel(name);
        }

        firePropertyChange(NAME, null, name);
    }

    public void updateName() {
        if (source == null) {
            return;
        }
        String labelText = name;
        int id = getOutputId();

        boolean updateName = false;
        if (getLineStyle().equals(EConnectionType.TABLE)) {
            if (id >= 0) {
                labelText += " (" + metaName + ", order:" + id + ")";
            } else {
                labelText += " (" + EDesignerConnection.TABLE.getLinkName() + ")"; //$NON-NLS-1$ //$NON-NLS-2$
            }
            updateName = true;
        } else if (getLineStyle().equals(EConnectionType.FLOW_MAIN)) {
            if (id >= 0) {
                labelText += " (" + EDesignerConnection.FLOW_MAIN.getLinkName() + ", order:" + id + ")";
            } else {
                labelText += " (" + EDesignerConnection.FLOW_MAIN.getLinkName() + ")";
            }
            updateName = true;
        } else if (getLineStyle().equals(EConnectionType.FLOW_REF)) {
            if (id >= 0) {
                labelText += " (" + EDesignerConnection.FLOW_REF.getLinkName() + ", order:" + id + ")";
            } else {
                labelText += " (" + EDesignerConnection.FLOW_REF.getLinkName() + ")";
            }
            updateName = true;
        }
        if (updateName) {
            if (label == null) {
                label = new ConnectionLabel(labelText, this);
            } else {
                if (!label.getLabelText().equals(labelText)) {
                    label.setLabelText(labelText);
                }
            }
            firePropertyChange(NAME, null, name);
        }
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
            if (lineStyle.equals(EConnectionType.TABLE)) {
                if (uniqueName == null) {
                    uniqueName = source.getProcess().generateUniqueConnectionName("table");
                }
                // if (source.getConnectorFromType(lineStyle).isBuiltIn()) {
                IMetadataTable table = getMetadataTable();
                table.setTableName(uniqueName);
                if (table.getLabel() == null) {
                    table.setLabel(name);
                }
                metaName = uniqueName;
                // }
            }
            if (lineStyle.equals(EConnectionType.TABLE) || lineStyle.equals(EConnectionType.FLOW_MAIN)
                    || lineStyle.equals(EConnectionType.FLOW_REF)) {
                if (source.getProcess().checkValidConnectionName(uniqueName)) {
                    source.getProcess().addUniqueConnectionName(uniqueName);
                }
            }
            source.addOutput(this);
            target.addInput(this);
            updateAllId();
            isConnected = true;
        }
    }

    /**
     * Disconnect the connection This function is used before delete or reconnect a connection.
     */
    public void disconnect() {
        if (isConnected) {
            if (!source.getConnectorFromType(lineStyle).isBuiltIn()) {
                if (lineStyle.equals(EConnectionType.TABLE) || lineStyle.equals(EConnectionType.FLOW_MAIN)
                        || lineStyle.equals(EConnectionType.FLOW_REF)) {
                    source.getProcess().removeUniqueConnectionName(uniqueName);
                }
            }
            source.removeOutput(this);
            target.removeInput(this);
            updateAllId();
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

    private void orderConnectionsByMetadata() {
        List<IMetadataTable> tableList = source.getMetadataList();
        List<IConnection> connectionList = (List<IConnection>) source.getOutgoingConnections();
        List<IConnection> tmpList = new ArrayList<IConnection>(connectionList);
        connectionList.clear();
        for (IMetadataTable table : tableList) {
            String tableName = table.getTableName();
            for (IConnection connection : tmpList) {
                if (connection.getMetadataTable().getTableName().equals(tableName)) {
                    connectionList.add(connection);
                }
            }
        }
    }

    private void updateAllId() {
        if (source != null && source.getConnectorFromType(lineStyle).isBuiltIn()) {
            orderConnectionsByMetadata();
            for (int i = 0; i < source.getOutgoingConnections().size(); i++) {
                Connection connection = (Connection) source.getOutgoingConnections().get(i);
                connection.updateName();
            }
        }
    }

    public int getOutputId() {
        if (source != null && source.getConnectorFromType(lineStyle).isBuiltIn()) {
            for (int i = 0; i < source.getOutgoingConnections().size(); i++) {
                if (source.getOutgoingConnections().get(i).equals(this)) {
                    return i;
                }
            }
        }
        return -1;
    }
}
