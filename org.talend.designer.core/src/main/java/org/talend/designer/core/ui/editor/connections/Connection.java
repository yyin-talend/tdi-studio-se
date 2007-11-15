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
package org.talend.designer.core.ui.editor.connections;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.ObjectUtils;
import org.talend.core.language.LanguageManager;
import org.talend.core.model.metadata.IMetadataTable;
import org.talend.core.model.process.EComponentCategory;
import org.talend.core.model.process.EConnectionType;
import org.talend.core.model.process.EParameterFieldType;
import org.talend.core.model.process.Element;
import org.talend.core.model.process.IConnection;
import org.talend.core.model.process.IConnectionCategory;
import org.talend.core.model.process.IElementParameter;
import org.talend.core.model.process.INode;
import org.talend.core.model.process.INodeConnector;
import org.talend.core.model.process.IPerformance;
import org.talend.designer.core.model.components.EParameterName;
import org.talend.designer.core.model.components.ElementParameter;
import org.talend.designer.core.ui.editor.nodes.Node;
import org.talend.designer.core.ui.editor.process.Process;
import org.talend.repository.model.ComponentsFactoryProvider;

/**
 * Class that define the connection. It's the model part of the Gef element <br/>
 * 
 * $Id$
 * 
 */
public class Connection extends Element implements IConnection, IPerformance {

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

    private INodeConnector sourceNodeConnector;

    private String connectorName;

    private ConnectionPerformance performance;

    // used only for copy / paste (will generate the name) && connection
    // creation
    public Connection(Node source, Node target, EConnectionType lineStyle, String connectorName, String metaName, String linkName) {
        init(source, target, lineStyle, connectorName, metaName, linkName);
    }

    // used only when loading a process && connection creation
    public Connection(Node source, Node target, EConnectionType lineStyle, String connectorName, String metaName,
            String linkName, String uniqueName) {
        this.uniqueName = uniqueName;
        init(source, target, lineStyle, connectorName, metaName, linkName);
    }

    // used only in ConnectionManager to test if we can connect or not.
    public Connection(Node source, Node target, EConnectionType lineStyle) {
        this.source = source;
        this.target = target;
        this.lineStyle = lineStyle;

        // add activate parameter
        IElementParameter param = new ElementParameter(this);
        param.setField(EParameterFieldType.CHECK);
        param.setCategory(EComponentCategory.TECHNICAL);
        param.setValue(Boolean.TRUE);
        param.setName(EParameterName.ACTIVATE.getName());
        param.setDisplayName(EParameterName.ACTIVATE.getDisplayName());
        param.setShow(false);
        param.setNumRow(1);
        addElementParameter(param);
    }

    private void init(Node source, Node target, EConnectionType lineStyle, String connectorName, String metaName, String linkName) {
        performance = new ConnectionPerformance(this);

        sourceNodeConnector = source.getConnectorFromName(connectorName);
        this.connectorName = connectorName;
        this.lineStyle = lineStyle;
        this.metaName = metaName;
        if (lineStyle.hasConnectionCategory(IConnectionCategory.FLOW)) {
            trace = new ConnectionTrace(this);
            createMeterParameters((Process) source.getProcess());
        }
        setName(linkName);
        if (trace != null) {
            trace.setOffset(label.getOffset());
        }

        reconnect(source, target, lineStyle);
        updateName();
        if (lineStyle.equals(EConnectionType.RUN_IF)) {
            IElementParameter param = new ElementParameter(this);
            switch (LanguageManager.getCurrentLanguage()) {
            case JAVA:
                param.setField(EParameterFieldType.MEMO_JAVA);
                break;
            default:
                param.setField(EParameterFieldType.MEMO_PERL);
            }
            param.setCategory(EComponentCategory.MAIN);
            param.setValue(""); //$NON-NLS-1$
            param.setNbLines(5);
            param.setName(EParameterName.CONDITION.getName());
            param.setDisplayName(EParameterName.CONDITION.getDisplayName());
            param.setShow(true);
            param.setNumRow(1);
            addElementParameter(param);
        }

        // add activate parameter
        IElementParameter param = new ElementParameter(this);
        param.setField(EParameterFieldType.CHECK);
        param.setCategory(EComponentCategory.TECHNICAL);
        param.setValue(Boolean.TRUE);
        param.setName(EParameterName.ACTIVATE.getName());
        param.setDisplayName(EParameterName.ACTIVATE.getDisplayName());
        param.setShow(false);
        param.setNumRow(1);
        addElementParameter(param);

        param = new ElementParameter(this);
        param.setName(EParameterName.UPDATE_COMPONENTS.getName());
        param.setValue(Boolean.FALSE);
        param.setDisplayName(EParameterName.UPDATE_COMPONENTS.getDisplayName());
        param.setField(EParameterFieldType.CHECK);
        param.setCategory(EComponentCategory.MAIN);
        param.setNumRow(5);
        param.setReadOnly(true);
        param.setRequired(false);
        param.setShow(false);
        addElementParameter(param);

    }

    private void createMeterParameters(Process process) {
        IElementParameter param = new ElementParameter(this);
        param.setName("MONITOR_CONNECTION");
        param.setDisplayName("Monitor this connection");
        param.setField(EParameterFieldType.CHECK);
        param.setValue(Boolean.FALSE);
        param.setCategory(EComponentCategory.MAIN);
        param.setShow(true);
        param.setNumRow(1);
        addElementParameter(param);
        Node meterAttached = new Node(ComponentsFactoryProvider.getInstance().get("tFlowMeter"), process);
        for (IElementParameter curParam : meterAttached.getElementParameters()) {
            if (curParam.getCategory() == EComponentCategory.PROPERTY) {
                curParam.setCategory(EComponentCategory.MAIN);
                curParam.setNumRow(curParam.getNumRow() + 1);
                if (curParam.getShowIf() == null || curParam.getShowIf().equals("")) {
                    curParam.setShowIf("MONITOR_CONNECTION == 'true'");
                } else {
                    curParam.setShowIf("(" + curParam.getShowIf() + " and MONITOR_CONNECTION == 'true')");
                }
                addElementParameter(curParam);
            }
        }
        param = new ElementParameter(this);
        param.setName(EParameterName.UPDATE_COMPONENTS.getName());
        param.setValue(new Boolean(false));
        param.setDisplayName(EParameterName.UPDATE_COMPONENTS.getDisplayName());
        param.setField(EParameterFieldType.CHECK);
        param.setCategory(EComponentCategory.MAIN);
        param.setReadOnly(true);
        param.setRequired(false);
        param.setShow(false);
        addElementParameter(param);
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
        boolean canModify = true;
        List connections;
        if (target != null) {
            connections = target.getIncomingConnections();
            for (int i = 0; i < connections.size(); i++) {
                if (((Connection) connections.get(i)).getName().equals(name)) {
                    canModify = false;
                }
            }
        }

        if (canModify) {

            this.name = name;
            if (!lineStyle.equals(EConnectionType.TABLE)) {
                uniqueName = name;
            }

            if (source != null && lineStyle.hasConnectionCategory(IConnectionCategory.FLOW)) {
                if (sourceNodeConnector.isBuiltIn()) {
                    IMetadataTable table = getMetadataTable();
                    table.setTableName(name);
                    metaName = name;
                }
            }

            if (source != null && (lineStyle == EConnectionType.TABLE)) {
                IMetadataTable table = getMetadataTable();
                table.setLabel(name);
            }
            if (label == null) {
                label = new ConnectionLabel(name, this);
            }
            updateName();
        }
    }

    public void updateName() {
        if (source == null) {
            return;
        }
        String labelText = name;
        int outputId = getOutputId(getLineStyle());

        boolean updateName = false;
        if (getLineStyle().equals(EConnectionType.TABLE)) {
            if (outputId >= 0) {
                labelText += " (" + metaName + ", order:" + outputId + ")";
            } else {
                labelText += " (" + sourceNodeConnector.getLinkName() + ")"; //$NON-NLS-1$ //$NON-NLS-2$
            }
            updateName = true;
        } else if (getLineStyle().equals(EConnectionType.FLOW_MAIN) || getLineStyle().equals(EConnectionType.FLOW_REF)) {
            if (sourceNodeConnector.getDefaultConnectionType().equals(getLineStyle())) { // if it's the standard
                // link
                if (outputId >= 0) {
                    labelText += " (" + sourceNodeConnector.getLinkName() + " order:" + outputId + ")";
                } else {
                    labelText += " (" + sourceNodeConnector.getLinkName() + ")";
                }
            } else if (sourceNodeConnector.getName().equals(EConnectionType.FLOW_MAIN.getName())) {
                // link
                if (outputId >= 0) {
                    labelText += " (" + getLineStyle().getDefaultLinkName() + " order:" + outputId + ")";
                } else {
                    labelText += " (" + getLineStyle().getDefaultLinkName() + ")";
                }
            } else {
                if (outputId >= 0) {
                    labelText += " (" + getLineStyle().getDefaultLinkName() + ", " + sourceNodeConnector.getLinkName()
                            + " order:" + outputId + ")";
                } else {
                    labelText += " (" + getLineStyle().getDefaultLinkName() + ", " + sourceNodeConnector.getLinkName() + ")";
                }
            }
            updateName = true;
        } else if (getLineStyle().equals(EConnectionType.FLOW_MERGE)) {
            int inputId = getInputId();
            if (outputId >= 0) {
                labelText += " (Main order:" + outputId + ", " + getLineStyle().getDefaultLinkName() + " order:" + inputId + ")";
            } else {
                labelText += " (" + getLineStyle().getDefaultLinkName() + " order:" + inputId + ")";
            }
            updateName = true;
        } else if (getLineStyle().equals(EConnectionType.RUN_IF) && (!sourceNodeConnector.getLinkName().equals(name))) {
            // if "RunIf" got a custom name
            labelText = sourceNodeConnector.getLinkName() + " (" + name + ")";
            updateName = true;
        } else {
            if (outputId >= 0) {
                labelText += " (" + "order:" + outputId + ")";
            }
            updateName = true;
        } /*
             * else if (getLineStyle().equals(EConnectionType.LOOKUP)) { labelText += " (" + nodeConnector.getLinkName() +
             * ")"; updateName = true; }
             */

        if (updateName) {

            if (!label.getLabelText().equals(labelText)) {

                label.setLabelText(labelText);

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
            if ((lineStyle.equals(EConnectionType.TABLE) && sourceNodeConnector.isBuiltIn())
                    || lineStyle.hasConnectionCategory(IConnectionCategory.UNIQUE_NAME)) {
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
            if (!sourceNodeConnector.isBuiltIn()) {
                if (lineStyle.hasConnectionCategory(IConnectionCategory.CUSTOM_NAME)) {
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
    public void reconnect(Node newSource, Node newTarget, EConnectionType newLineStyle) {
        disconnect();
        this.source = newSource;
        this.target = newTarget;
        this.lineStyle = newLineStyle;
        sourceNodeConnector = source.getConnectorFromName(connectorName);
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
        updateName();
        firePropertyChange(LINESTYLE_PROP, null, connectorName);
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
    @Override
    public void setPropertyValue(String id, Object value) {
        if (id.equals(EParameterName.ACTIVATE.getName())) {
            setActivate((Boolean) value);
        }

        if (id.equals(LINESTYLE_PROP)) {
            // setLineStyle((EConnectionType) value);
            setConnectorName((String) value);
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
    @Override
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
        if (source != null && this.getLineStyle().hasConnectionCategory(IConnectionCategory.DATA)) {
            if (sourceNodeConnector.isBuiltIn()) {
                return source.getMetadataTable(metaName);
            } else {
                return source.getMetadataFromConnector(sourceNodeConnector.getName());
            }
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
        return getUniqueName();
    }

    public boolean isActivate() {
        return this.activate;
    }

    private void setActivate(boolean activate) {
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
                if (connection.getLineStyle().hasConnectionCategory(IConnectionCategory.DATA)
                        && connection.getMetadataTable().getTableName().equals(tableName)
                        && connection.getConnectorName().equals(table.getAttachedConnector())) {
                    connectionList.add(connection);
                }
            }
        }
        // add connections without metadata
        for (IConnection connection : tmpList) {
            if (!connectionList.contains(connection)) {
                connectionList.add(connection);
            }
        }
    }

    public void updateAllId() {
        if (source != null) {
            orderConnectionsByMetadata();
            for (int i = 0; i < source.getOutgoingConnections().size(); i++) {
                Connection connection = (Connection) source.getOutgoingConnections().get(i);
                connection.updateName();
            }
        }
        if (target != null) {
            for (int i = 0; i < target.getIncomingConnections().size(); i++) {
                Connection connection = (Connection) target.getIncomingConnections().get(i);
                connection.updateName();
            }
        }
    }

    public int getOutputId(EConnectionType type) {
        int outputId = 0;
        int totalOutput = 0;
        boolean sameKind;
        switch (type) {
        case FLOW_MAIN:
        case FLOW_REF:
        case FLOW_MERGE:
            if (!sourceNodeConnector.isBuiltIn()) {
                return -1;
            }
        case THEN_RUN:
            boolean isExecutionOrder = type.hasConnectionCategory(IConnectionCategory.EXECUTION_ORDER);
            boolean isTestedConnectionExecutionOrder;
            if (source != null) {
                for (int i = 0; i < source.getOutgoingConnections().size(); i++) {
                    IConnection connection = source.getOutgoingConnections().get(i);
                    isTestedConnectionExecutionOrder = connection.getLineStyle().hasConnectionCategory(
                            IConnectionCategory.EXECUTION_ORDER);
                    sameKind = false;
                    if ((isExecutionOrder && isTestedConnectionExecutionOrder)
                            || (!isExecutionOrder && !isTestedConnectionExecutionOrder)) {
                        sameKind = true;
                    }
                    if (sameKind) {
                        totalOutput++;
                    }
                    if (connection.equals(this) && sameKind) {
                        outputId = totalOutput;
                    }
                }
            }
            if (totalOutput > 1) {
                return outputId;
            }
            break;
        default:
            return -1;
        }
        return -1;
    }

    public int getInputId() {
        if (target != null) {
            for (int i = 0; i < target.getIncomingConnections().size(); i++) {
                if (target.getIncomingConnections().get(i).equals(this)) {
                    return i + 1;
                }
            }
        }
        return -1;
    }

    /**
     * This function will change the merge order of the connection.
     * 
     * @param id
     */
    public void setInputId(int id) {
        int newId = id - 1;
        int curId = 0;
        if (target != null) {
            if (target.getIncomingConnections().size() < newId) {
                throw new IllegalArgumentException("Input Id is not valid");
            }
            if (target.getIncomingConnections().get(newId).equals(this)) {
                return; // id is already set
            }
            for (int i = 0; i < target.getIncomingConnections().size(); i++) {
                if (target.getIncomingConnections().get(i).equals(this)) {
                    curId = i;
                    break;
                }
            }
            Collections.swap(target.getIncomingConnections(), curId, newId);
        }
    }

    /**
     * Getter for nodeConnector.
     * 
     * @return the nodeConnector
     */
    public INodeConnector getSourceNodeConnector() {
        return sourceNodeConnector;
    }

    public INodeConnector getTargetNodeConnector() {
        // INodeConnector targetNodeConnector =
        // target.getConnectorFromName(connectorName);
        // if (targetNodeConnector != null) {
        // return targetNodeConnector;
        // }
        return target.getConnectorFromType(this.getLineStyle());
    }

    /**
     * Getter for connectionType.
     * 
     * @return the connectionType
     */
    public String getConnectorName() {
        return connectorName;
    }

    /**
     * Sets the connectionType.
     * 
     * @param connectionType the connectionType to set
     */
    public void setConnectorName(String connectorName) {
        this.connectorName = connectorName;
        sourceNodeConnector = source.getConnectorFromName(connectorName);
        updateName();
        firePropertyChange(LINESTYLE_PROP, null, connectorName);
    }

    /**
     * Getter for performance.
     * 
     * @return the performance
     */
    public ConnectionPerformance getPerformance() {
        return this.performance;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IPerformance#setPerformanceData(java.lang.String)
     */
    public void setPerformanceData(String pefData) {
        performance.setLabel(pefData);

    }

    public boolean isUseByMetter() {
        INode sourceNode = this.getSource();
        List<INode> metterNodes = (List<INode>) sourceNode.getProcess().getNodesOfType("tFlowMeter");
        if (metterNodes.size() > 0) {

            Iterator<INode> it = metterNodes.iterator();
            while (it.hasNext()) {
                INode node = it.next();

                String absolute = (String) node.getElementParameter("ABSOLUTE").getValue();
                String reference = (String) node.getElementParameter("CONNECTIONS").getValue();

                if (absolute.equals("false") && reference.equals(this.getUniqueName())) {
                    return true;
                }
            }
        }
        return false;
    }
}
