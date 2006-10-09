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
package org.talend.designer.core.ui.editor.nodes;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ObjectUtils;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.core.CorePlugin;
import org.talend.core.context.Context;
import org.talend.core.context.RepositoryContext;
import org.talend.core.model.components.IComponent;
import org.talend.core.model.components.IComponentsFactory;
import org.talend.core.model.metadata.IMetadataColumn;
import org.talend.core.model.metadata.IMetadataTable;
import org.talend.core.model.metadata.MetadataTable;
import org.talend.core.model.process.EConnectionType;
import org.talend.core.model.process.EParameterFieldType;
import org.talend.core.model.process.Element;
import org.talend.core.model.process.ElementParameterParser;
import org.talend.core.model.process.IConnection;
import org.talend.core.model.process.IElementParameter;
import org.talend.core.model.process.IExternalNode;
import org.talend.core.model.process.INode;
import org.talend.core.model.process.INodeConnector;
import org.talend.core.model.process.INodeReturn;
import org.talend.core.model.process.IProcess;
import org.talend.core.model.process.Problem;
import org.talend.core.model.process.Problem.ProblemStatus;
import org.talend.core.model.temp.ECodeLanguage;
import org.talend.designer.core.DesignerPlugin;
import org.talend.designer.core.model.components.ComponentImportNeeds;
import org.talend.designer.core.model.components.EParameterName;
import org.talend.designer.core.ui.MultiPageTalendEditor;
import org.talend.designer.core.ui.editor.connections.Connection;
import org.talend.designer.core.ui.editor.connections.EDesignerConnection;
import org.talend.designer.core.ui.editor.nodecontainer.NodeContainer;
import org.talend.designer.core.ui.editor.process.Process;
import org.talend.designer.core.ui.preferences.TalendDesignerPrefConstants;
import org.talend.designer.core.ui.views.modules.ModulesView;
import org.talend.designer.core.ui.views.problems.Problems;
import org.talend.repository.model.ExternalNodesFactory;

/**
 * Object that describes the node. All informations on nodes are stored in this class. <br/>
 * 
 * $Id$
 * 
 */
public class Node extends Element implements INode {

    // true if this node is set as a start node.
    private boolean start;

    // true if this node is activated.
    private boolean activate = true;

    private int currentStatus;

    // properties
    public static final String LOCATION = "nodeLocation"; //$NON-NLS-1$

    public static final String INPUTS = "inputs"; //$NON-NLS-1$

    public static final String OUTPUTS = "outputs"; //$NON-NLS-1$

    public static final String PERFORMANCE_DATA = "perfData"; //$NON-NLS-1$

    public static final String TRACE_DATA = "traceData"; //$NON-NLS-1$

    public static final String UPDATE_STATUS = "addStatus";

    protected Point location = new Point(0, 0);

    protected String name, label, componentName;

    private List<Connection> outputs = new ArrayList<Connection>();

    private List<Connection> inputs = new ArrayList<Connection>();

    private NodeLabel nodeLabel;

    private List<IMetadataTable> metadataList;

    protected List<? extends INodeReturn> listReturn;

    protected List<? extends INodeConnector> listConnector;

    private IComponent component;

    private boolean showHint;

    private String showHintText;

    private String labelToParse;

    private String hintToParse;

    private IExternalNode externalNode = null; // null if no external component defined

    private Object externalData = null;

    private NodeContainer nodeContainer;

    private String performanceData;

    private Process process = null;

    private String pluginFullName;

    public static final Color START_COLOR = new Color(null, new RGB(0xB0, 0xE7, 0));

    private boolean readOnly = false;

    /**
     * This constructor is called when the node is created from the palette the unique name will be determined with the
     * number of components of this type.
     * 
     * @param component
     */
    public Node(IComponent component) {
        IEditorPart editorPart = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
        if (editorPart instanceof MultiPageTalendEditor) {
            Process activeProcess = ((MultiPageTalendEditor) editorPart).getTalendEditor().getProcess();
            process = activeProcess;
        }
        currentStatus = 0;
        init(component);
    }

    public Node(IComponent component, Process process) {
        this.process = process;
        init(component);
    }

    private void init(IComponent newComponent) {
        this.component = newComponent;
        this.label = component.getName();
        this.componentName = this.label;

        IPreferenceStore store = DesignerPlugin.getDefault().getPreferenceStore();

        labelToParse = store.getString(TalendDesignerPrefConstants.DEFAULT_LABEL);
        hintToParse = store.getString(TalendDesignerPrefConstants.DEFAULT_HINT);
        showHint = store.getBoolean(TalendDesignerPrefConstants.DEFAULT_HINT_USED);

        nodeLabel = new NodeLabel(label, this);
        setElementParameters(component.createParameters());
        listReturn = this.component.createReturns();
        listConnector = this.component.createConnectors();
        metadataList = new ArrayList<IMetadataTable>();
        generateUniqueName();
        setPropertyValue(EParameterName.LABEL.getName(), labelToParse);
        setPropertyValue(EParameterName.HINT.getName(), hintToParse);
        setPropertyValue(EParameterName.SHOW_HINT.getName(), new Boolean(showHint));
        if (!getConnectorFromType(EConnectionType.FLOW_MAIN).isBuiltIn()) {
            IMetadataTable meta = new MetadataTable();
            meta.setTableName(getUniqueName());
            metadataList.add(meta);
        }
        pluginFullName = newComponent.getPluginFullName();
        if (pluginFullName != IComponentsFactory.COMPONENTS_LOCATION) {
            externalNode = ExternalNodesFactory.getInstance(pluginFullName);
        }
    }

    public IProcess getProcess() {
        return process;
    }

    public INodeConnector getConnectorFromType(final EConnectionType connType) {
        INodeConnector nodeConnector = null;
        int nbConn = 0;

        EConnectionType testedType;

        if (connType.equals(EConnectionType.FLOW_REF)) {
            testedType = EConnectionType.FLOW_MAIN;
        } else {
            testedType = connType;
        }

        while ((nodeConnector == null) && (nbConn < listConnector.size())) {
            if (listConnector.get(nbConn).getConnectionType() == testedType) {
                nodeConnector = listConnector.get(nbConn);
            }
            nbConn++;
        }
        return nodeConnector;
    }

    public void setShowHint(final Boolean showHint) {
        this.showHint = showHint;
        firePropertyChange(EParameterName.HINT.getName(), null, null);

        IElementParameter param = getElementParameter(EParameterName.SHOW_HINT.getName());
        param.setValue(new Boolean(showHint));
    }

    public boolean isSetShowHint() {
        return showHint;
    }

    public void setShowHintText(final String showHintText) {
        this.showHintText = showHintText;
        firePropertyChange(EParameterName.HINT.getName(), null, null);
    }

    public String getShowHintText() {
        return showHintText;
    }

    /**
     * Get the ImageDescriptor of the component which is taken from the xml.
     * 
     * @return ImageDescriptor
     */
    public ImageDescriptor getImageDescriptor() {
        return component.getImageDescriptor();
    }

    /**
     * Manage to find a unique name with the given name.
     * 
     * @param titleName
     */
    protected String generateUniqueName() {
        String uniqueName = ((Process) getProcess()).generateUniqueNodeName(this);
        ((Process) getProcess()).addUniqueNodeName(uniqueName);
        setPropertyValue(EParameterName.UNIQUE_NAME.getName(), uniqueName);
        return uniqueName;
    }

    /**
     * Gives the unique name of the node.
     * 
     * @return unique name
     */
    public String getUniqueName() {
        String uniqueName = null;
        IElementParameter param = getElementParameter(EParameterName.UNIQUE_NAME.getName());
        uniqueName = (String) param.getValue();
        return uniqueName;
    }

    // can only be set with the properties
    private void setUniqueName(String uniqueName) {
        ((Process) getProcess()).removeUniqueNodeName(getUniqueName());
        ((Process) getProcess()).addUniqueNodeName(uniqueName);
    }

    public List<? extends INodeReturn> getReturns() {
        return this.listReturn;
    }

    /**
     * Set this node as the start of the diagram.
     * 
     * @param start boolean that will give the status
     */
    public void setStart(final boolean start) {
        this.start = start;
        firePropertyChange(EParameterName.START.getName(), null, null);

        IElementParameter param = getElementParameter(EParameterName.START.getName());
        param.setValue(new Boolean(start));
    }

    /**
     * Return the start status of this node.
     * 
     * @return
     */
    public boolean isStart() {
        return start;
    }

    /**
     * Set the location of the node.
     * 
     * @param location Point
     */
    public void setLocation(final Point location) {
        if (this.location.equals(location)) {
            return;
        }
        this.location = location;
        nodeLabel.setLocation(location);
        firePropertyChange(LOCATION, null, location);
    }

    /**
     * Gives the location of the node.
     * 
     * @return Point
     */
    public Point getLocation() {
        return location;
    }

    /**
     * Set the label of a node. <br/><b> /!\ This is the text of the label, not the name of the component</b>
     * 
     * @param titleName
     */
    public void setLabel(final String label) {
        this.label = label;
        if (nodeLabel.getLabelText() != label) {
            nodeLabel.setLabelText(label);
        }
    }

    public void updateVisibleData() {
        String newLabel = ElementParameterParser.parse(this, labelToParse);
        if (!newLabel.equals(label)) {
            setLabel(newLabel);
        }

        String newshowHintText = ElementParameterParser.parse(this, hintToParse);
        if (!newshowHintText.equals(showHintText)) {
            setShowHintText(newshowHintText);
        }
    }

    /**
     * Gives the label of the node.
     * 
     * @return
     */
    public String getLabel() {
        return label;
    }

    /**
     * Set the name of the node. Typically it's the name of the component type.
     * 
     * @param titleName
     */
    public void setComponentName(final String componentName) {
        this.componentName = componentName;
    }

    /**
     * Gives the name of the node. Typically it's the name of the component type.
     * 
     * @return
     */
    public String getComponentName() {
        return componentName;
    }

    /**
     * Gives the object of the model part for the label.
     * 
     * @return
     */
    public NodeLabel getNodeLabel() {
        return nodeLabel;
    }

    /**
     * Add a new connection input to the node.
     * 
     * @param connection
     */
    public void addInput(final Connection connection) {
        this.inputs.add(connection);
        fireStructureChange(INPUTS, connection);
    }

    /**
     * Add a new connection output to the node.
     * 
     * @param connection
     */
    public void addOutput(final Connection connection) {
        this.outputs.add(connection);
        fireStructureChange(OUTPUTS, connection);
    }

    /**
     * Gives all incoming connections (only).
     * 
     * @return List of Connection
     */
    public List<? extends IConnection> getIncomingConnections() {
        return this.inputs;
    }

    /**
     * Gives all outgoing connections (only).
     * 
     * @return List of Connection
     */
    public List<? extends IConnection> getOutgoingConnections() {
        return this.outputs;
    }

    /**
     * Remove a connection input.
     * 
     * @param connection
     */
    public void removeInput(final Connection connection) {
        this.inputs.remove(connection);
        fireStructureChange(INPUTS, connection);
    }

    /**
     * Remove a connection output.
     * 
     * @param connection
     */
    public void removeOutput(final Connection connection) {
        this.outputs.remove(connection);
        fireStructureChange(OUTPUTS, connection);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.core.ui.editor.Element#getPropertyValue(java.lang.Object)
     */
    public Object getPropertyValue(final String id) {
        if (id.equals(EParameterName.UNIQUE_NAME.getName())) {
            return getUniqueName();
        }

        return super.getPropertyValue(id);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.core.ui.editor.Element#setPropertyValue(java.lang.Object, java.lang.Object)
     */
    public void setPropertyValue(final String id, final Object value) {
        if (id.equals(EParameterName.LABEL.getName())) {
            labelToParse = (String) value;
            String newValue = ElementParameterParser.parse(this, labelToParse);

            setLabel(newValue);
        }
        if (id.equals(EParameterName.START.getName())) {
            setStart((Boolean) value);
        }

        if (id.equals(EParameterName.ACTIVATE.getName())) {
            setActivate((Boolean) value);
        }
        if (id.equals(EParameterName.HINT.getName())) {
            hintToParse = (String) value;
            String newValue = ElementParameterParser.parse(this, hintToParse);
            setShowHintText(newValue);
        }
        // unique name can only be set when the process is loaded
        if (id.equals(EParameterName.UNIQUE_NAME.getName())) {
            setUniqueName((String) value);
        }
        if (id.equals(EParameterName.SHOW_HINT.getName())) {
            setShowHint((Boolean) value);
        }
        super.setPropertyValue(id, value);
        updateVisibleData();
    }

    public List<IMetadataTable> getMetadataList() {
        return metadataList;
    }

    public void setMetadataList(final List<IMetadataTable> metaDataList) {
        this.metadataList = metaDataList;
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

    public boolean isExternalNode() {
        if (externalNode != null) {
            return true;
        }
        return false;
    }

    public IExternalNode getExternalNode() {
        if (externalNode != null) {
            externalNode.setActivate(isActivate());
            externalNode.setStart(isStart());
            externalNode.setComponentName(getComponentName());
            externalNode.setExternalData(getExternalData());
            List<IMetadataTable> copyOfMetadataList = new ArrayList<IMetadataTable>();
            for (IMetadataTable metaTable : metadataList) {
                copyOfMetadataList.add(metaTable.clone());
            }
            externalNode.setMetadataList(copyOfMetadataList);
            externalNode.setIncomingConnections(getIncomingConnections());
            externalNode.setOutgoingConnections(getOutgoingConnections());
            externalNode.setPluginFullName(getPluginFullName());
            externalNode.setElementParameters(getElementParameters());
            externalNode.setUniqueName(getUniqueName());
            externalNode.setSubProcessStart(isSubProcessStart());
            externalNode.setProcess(getProcess());
        }
        return this.externalNode;
    }

    public void setExternalNode(final IExternalNode externalNode) {
        this.externalNode = externalNode;
    }

    public Object getExternalData() {
        return this.externalData;
    }

    public void setExternalData(final Object persistantData) {
        this.externalData = persistantData;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public String getPluginFullName() {
        return pluginFullName;
    }

    public void setPluginFullName(String pluginFullName) {
        this.pluginFullName = pluginFullName;
    }

    public NodeContainer getNodeContainer() {
        return this.nodeContainer;
    }

    public void setNodeContainer(NodeContainer nodeContainer) {
        this.nodeContainer = nodeContainer;
    }

    /**
     * @see org.talend.core.model.process.INode#setPerformanceData(java.lang.String)
     */
    public void setPerformanceData(String perfData) {
        String oldData = this.performanceData;
        if (!ObjectUtils.equals(oldData, perfData)) {
            this.performanceData = perfData;
            this.nodeContainer.getNodePerformance().setPerfData(perfData);
            firePropertyChange(PERFORMANCE_DATA, oldData, perfData);
        }
    }

    /**
     * Getter for performanceData.
     * 
     * @return the performanceData
     */
    public String getPerformanceData() {
        return this.performanceData;
    }

    public boolean isActivate() {
        return this.activate;
    }

    @SuppressWarnings("unchecked")
    public void setActivate(final boolean activate) {
        this.activate = activate;
        nodeLabel.setActivate(activate);
        List<Connection> listConnections = (List<Connection>) this.getOutgoingConnections();
        for (int i = 0; i < listConnections.size(); i++) {
            if (listConnections.get(i).getTarget().isActivate()) {
                listConnections.get(i).setActivate(activate);
            }
        }
        listConnections = (List<Connection>) this.getIncomingConnections();
        for (int i = 0; i < listConnections.size(); i++) {
            if (listConnections.get(i).getSource().isActivate()) {
                listConnections.get(i).setActivate(activate);
            }
        }
        firePropertyChange(EParameterName.ACTIVATE.getName(), null, null);
    }

    public boolean isSubProcessStart() {
        Connection connec;
        if (isActivate()) {
            for (int j = 0; j < getIncomingConnections().size(); j++) {
                connec = (Connection) getIncomingConnections().get(j);
                if (connec.isActivate()
                        && ((connec.getLineStyle().equals(EConnectionType.FLOW_MAIN)
                                || connec.getLineStyle().equals(EConnectionType.FLOW_REF) || connec.getLineStyle().equals(
                                EConnectionType.ITERATE)))) {
                    return false;
                }
            }
        }
        return true;
    }

    public IMetadataTable getMetadataTable(String metaName) {
        for (int i = 0; i < metadataList.size(); i++) {
            if (metadataList.get(i).getTableName().equals(metaName)) {
                return metadataList.get(i);
            }
        }
        return null;
    }

    public Node.Data getExternalBytesData() {
        if (externalNode == null) {
            return null;
        }

        Data data = new Data();
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            Writer writer = new StringWriter();
            externalNode.loadDataOut(out, writer);
            data.setBytesData(out.toByteArray());
            data.setStringData(writer.toString());
        } catch (IOException e) {
            ExceptionHandler.process(e);
        }
        return data;
    }

    public void setData(byte[] bytesData, String stringData) {
        ByteArrayInputStream inputStream = null;
        StringReader reader = null;

        if (externalNode == null) {
            return;
        }
        if (bytesData != null) {
            inputStream = new ByteArrayInputStream(bytesData);
        }
        if (stringData != null) {
            reader = new StringReader(stringData);
        }

        try {
            externalNode.loadDataIn(inputStream, reader);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        setExternalData(externalNode.getExternalData());
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.INode#isMultipleMethods(org.talend.core.model.temp.ECodeLanguage)
     */
    public boolean isMultipleMethods() {
        ECodeLanguage currentLanguage = ((RepositoryContext) CorePlugin.getContext().getProperty(Context.REPOSITORY_CONTEXT_KEY))
                .getProject().getLanguage();
        return component.isMultipleMethods(currentLanguage);
    }

    // doesn't work if the node has several start points (will return a random start node)
    public Node getSubProcessStartNode() {
        if ((getCurrentActiveLinksNbInput(EConnectionType.FLOW_MAIN) == 0)
                && (getCurrentActiveLinksNbInput(EConnectionType.FLOW_REF) == 0)
                && (getCurrentActiveLinksNbInput(EConnectionType.ITERATE) == 0)) {
            return this;
        }
        Connection connec;

        for (int j = 0; j < getIncomingConnections().size(); j++) {
            connec = (Connection) getIncomingConnections().get(j);
            if (!connec.getLineStyle().equals(EConnectionType.FLOW_REF)) {
                return connec.getSource().getSubProcessStartNode();
            }
        }
        return null;
    }

    // not finished and maybe not needed.
    // public Node getProcessStartNode() {
    // Node node = getSubProcessStartNode();
    //
    // if (node.isStart()) {
    // return node;
    // } else {
    // return getIndirectProcessStartNode();
    // }
    // }
    //    
    // private Node getIndirectProcessStartNode() {
    // return null;
    // }

    public boolean isReadOnly() {
        return this.readOnly;
    }

    public void setReadOnly() {
        this.readOnly = true;

        for (IElementParameter param : this.getElementParameters()) {
            param.setReadOnly(true);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.INode#setProcess(org.talend.core.model.process.IProcess)
     */
    public void setProcess(IProcess process) {
        if (process instanceof Process) {
            this.process = (Process) process;
        }
    }

    public void addStatus(int status) {
        this.currentStatus = this.currentStatus | status;
        firePropertyChange(UPDATE_STATUS, null, new Integer(this.currentStatus));
    }

    public void removeStatus(int status) {
        if ((this.currentStatus & status) != 0) {
            this.currentStatus = this.currentStatus ^ status;
        }
        firePropertyChange(UPDATE_STATUS, null, new Integer(this.currentStatus));
    }

    public int getStatus() {
        return currentStatus;
    }

    @SuppressWarnings("unchecked")
    private void checkParameters() {
        for (IElementParameter param : this.getElementParameters()) {
            // if the parameter is required but empty, an error will be added
            if (param.isRequired() && param.isShow(getElementParameters())) {
                EParameterFieldType fieldType = param.getField();
                switch (fieldType) {
                case TABLE:
                    List<Map<String, String>> tableValues = (List<Map<String, String>>) param.getValue();
                    if (tableValues.size() == 0) {
                        String errorMessage = "Parameter (" + param.getDisplayName() + ") must have at least one value.";
                        Problems.add(ProblemStatus.ERROR, this, errorMessage);
                    }
                    break;
                case CHECK:
                    break;
                case SCHEMA_TYPE:
                    break;
                default:
                    String value = (String) param.getValue();
                    if (value.equals("")) {
                        String errorMessage = "Parameter (" + param.getDisplayName() + ") is empty but is required.";
                        Problems.add(ProblemStatus.ERROR, this, errorMessage);
                    }
                }
            }
        }
    }

    private int getCurrentActiveLinksNbInput(EConnectionType type) {
        int nb = 0;
        for (Connection connection : inputs) {
            if (connection.isActivate() && connection.getLineStyle().equals(type)) {
                nb++;
            }
        }
        return nb;
    }

    private int getCurrentActiveLinksNbOutput(EConnectionType type) {
        int nb = 0;
        for (Connection connection : outputs) {
            if (connection.isActivate() && connection.getLineStyle().equals(type)) {
                nb++;
            }
        }
        return nb;
    }

    private void checkModules() {
        List<ComponentImportNeeds> list = ModulesView.getImports(getComponentName());
        for (ComponentImportNeeds current : list) {
            Problem problem = getProblem(current);
            if (problem != null) {
                Problems.add(problem);
            }
        }
    }

    private Problem getProblem(ComponentImportNeeds componentImportNeeds) {
        if (componentImportNeeds.getStatus() == ComponentImportNeeds.INSTALLED) {
            return null;
        }
        if (componentImportNeeds.getStatus() == ComponentImportNeeds.NOT_INSTALLED && componentImportNeeds.isRequired()) {
            return new Problem(this, "Module " + componentImportNeeds.getName() + " required", ProblemStatus.ERROR);
        }

        return null;
    }

    private void checkLinks() {
        // check not startable components not linked
        if (!(Boolean) getPropertyValue(EParameterName.STARTABLE.getName())) {
            if ((getCurrentActiveLinksNbInput(EConnectionType.FLOW_MAIN) == 0)
                    && (getConnectorFromType(EConnectionType.FLOW_MAIN).getMinLinkInput() == 0)) {
                String errorMessage = "This component should have input link(s).";
                Problems.add(ProblemStatus.WARNING, this, errorMessage);
            }
            if ((getCurrentActiveLinksNbInput(EConnectionType.FLOW_MAIN) == 0)
                    && (getCurrentActiveLinksNbInput(EConnectionType.FLOW_REF) > 0)) {
                String errorMessage = "This component should have at least a Row Main link.";
                Problems.add(ProblemStatus.WARNING, this, errorMessage);
            }
        }

        // check not startable components not linked
        if (getConnectorFromType(EConnectionType.FLOW_MAIN).getMaxLinkInput() == 0) {
            if ((getCurrentActiveLinksNbOutput(EConnectionType.FLOW_MAIN) == 0)
                    && (getCurrentActiveLinksNbOutput(EConnectionType.FLOW_REF) == 0)
                    && (getCurrentActiveLinksNbOutput(EConnectionType.ITERATE) == 0)) {
                String errorMessage = "This component should have outputs linked.";
                Problems.add(ProblemStatus.WARNING, this, errorMessage);
            }
        }

        // Check if there's an output run after / before on a component that is not a sub process start
        if (!isSubProcessStart() || (!(Boolean) getPropertyValue(EParameterName.STARTABLE.getName()))) {
            if ((getCurrentActiveLinksNbOutput(EConnectionType.RUN_AFTER) > 0)
                    || (getCurrentActiveLinksNbOutput(EConnectionType.RUN_BEFORE) > 0)) {
                String errorMessage = "A component that is not a sub process start can not have any link run after / run before in output.";
                Problems.add(ProblemStatus.ERROR, this, errorMessage);
            }
        }

        // Check if there's an input run after / before on a component that is not a sub process start
        if (!isSubProcessStart() || (!(Boolean) getPropertyValue(EParameterName.STARTABLE.getName()))) {
            if ((getCurrentActiveLinksNbInput(EConnectionType.RUN_AFTER) > 0)
                    || (getCurrentActiveLinksNbInput(EConnectionType.RUN_BEFORE) > 0)
                    || (getCurrentActiveLinksNbInput(EConnectionType.RUN_IF) > 0)
                    || (getCurrentActiveLinksNbInput(EConnectionType.RUN_IF_OK) > 0)
                    || (getCurrentActiveLinksNbInput(EConnectionType.RUN_IF_ERROR) > 0)) {
                String errorMessage = "A component that is not a sub process start can only have a data link or iterate link in input.";
                Problems.add(ProblemStatus.ERROR, this, errorMessage);
            }
        }

        for (EConnectionType type : EConnectionType.values()) {
            if (type != EConnectionType.FLOW_REF) {
                int nbMaxOut;
                nbMaxOut = getConnectorFromType(type).getMaxLinkOutput();
                int nbMaxIn;
                nbMaxIn = getConnectorFromType(type).getMaxLinkInput();
                int nbMinOut;
                nbMinOut = getConnectorFromType(type).getMinLinkOutput();
                int nbMinIn;
                nbMinIn = getConnectorFromType(type).getMinLinkInput();
                int curLinkOut;
                curLinkOut = getConnectorFromType(type).getCurLinkNbOutput();
                int curLinkIn;
                curLinkIn = getConnectorFromType(type).getCurLinkNbInput();
                String typeName = EDesignerConnection.getConnection(type).getMenuName();
                if (type == EConnectionType.FLOW_MAIN) {
                    typeName = "Row";
                }

                if (nbMaxOut != -1) {
                    if (curLinkOut > nbMaxOut) {
                        String errorMessage = "This component has too much \"" + typeName + "\" type outputs.";
                        Problems.add(ProblemStatus.WARNING, this, errorMessage);
                    }
                }

                if (nbMaxIn != -1) {
                    if (curLinkIn > nbMaxIn) {
                        String errorMessage = "This component has too much \"" + typeName + "\" type inputs.";
                        Problems.add(ProblemStatus.WARNING, this, errorMessage);
                    }
                }

                if (nbMinOut != 0) {
                    if (curLinkOut < nbMinOut) {
                        String errorMessage = "This component has not enough \"" + typeName + "\" type outputs.";
                        Problems.add(ProblemStatus.WARNING, this, errorMessage);
                    }
                }

                if (nbMinIn != 0) {
                    if (curLinkIn < nbMinIn) {
                        String errorMessage = "This component has not enough \"" + typeName + "\" type inputs.";
                        Problems.add(ProblemStatus.WARNING, this, errorMessage);
                    }
                }
            }
        }
    }

    private void checkSchema() {
        boolean canEditSchema = false;
        boolean noSchema = false;
        for (IElementParameter param : this.getElementParameters()) {
            if (param.isShow(getElementParameters()) && param.getField().equals(EParameterFieldType.SCHEMA_TYPE)) {
                canEditSchema = true;
            }
        }
        if (!isExternalNode()) {
            if (canEditSchema) {
                if (getConnectorFromType(EConnectionType.FLOW_MAIN).getMaxLinkInput() == 0) {
                    if (metadataList.get(0).getListColumns().size() == 0) {
                        String errorMessage = "No schema has been defined yet.";
                        Problems.add(ProblemStatus.ERROR, this, errorMessage);
                        noSchema = true;
                    }
                }
            } else {
                if (getCurrentActiveLinksNbInput(EConnectionType.FLOW_MAIN) == 0) {
                    if ((getCurrentActiveLinksNbOutput(EConnectionType.FLOW_MAIN) > 0)
                            || (getCurrentActiveLinksNbOutput(EConnectionType.FLOW_REF) > 0)) {
                        String errorMessage = "If this component has output, there must be an input link to propagate the data.";
                        Problems.add(ProblemStatus.ERROR, this, errorMessage);
                    }
                }
            }
        }

        // test empty schema in built in connections (several outputs with different schema)
        if (!noSchema && (!canEditSchema || isExternalNode())) {
            if (getConnectorFromType(EConnectionType.FLOW_MAIN).isBuiltIn()) {
                if (metadataList != null) {
                    for (IMetadataTable meta : metadataList) {
                        if (meta.getListColumns().size() == 0) {
                            String errorMessage = "The output schema/link named \"" + meta.getTableName()
                                    + "\" has no column defined, please check it.";
                            Problems.add(ProblemStatus.ERROR, this, errorMessage);
                        }
                    }
                }
            }
        }

        // test if the columns can be checked or not
        if (component.isCheckColumns()) {
            IMetadataTable inputMeta = null, outputMeta = metadataList.get(0);
            for (Connection connection : inputs) {
                if (connection.isActivate() && connection.getLineStyle().equals(EConnectionType.FLOW_MAIN)) {
                    inputMeta = connection.getMetadataTable();
                }
            }

            if (inputMeta != null) {
                boolean equal = true;

                if (inputMeta.getListColumns().size() != outputMeta.getListColumns().size()) {
                    equal = false;
                }
                if (equal) {
                    for (int i = 0; i < inputMeta.getListColumns().size(); i++) {
                        IMetadataColumn inputCol = inputMeta.getListColumns().get(i);
                        IMetadataColumn outputCol = outputMeta.getListColumns().get(i);
                        if (!inputCol.sameMetacolumnAs(outputCol)) {
                            if (inputCol.getLabel() == null) {
                                if (outputCol.getLabel() != null) {
                                    equal = false;
                                }
                            } else if (!inputCol.getLabel().equals(outputCol.getLabel())) {
                                equal = false;
                            }
                            if (inputCol.getLength() == null) {
                                if (outputCol.getLength() != null) {
                                    equal = false;
                                }
                            } else if (!inputCol.getLength().equals(outputCol.getLength())) {
                                equal = false;
                            }

                            if (inputCol.getPrecision() == null) {
                                if (outputCol.getPrecision() != null) {
                                    equal = false;
                                }
                            } else if (!inputCol.getPrecision().equals(outputCol.getPrecision())) {
                                equal = false;
                            }
                            if (inputCol.getTalendType() == null) {
                                if (outputCol.getTalendType() != null) {
                                    equal = false;
                                }
                            } else if (!inputCol.getTalendType().equals(outputCol.getTalendType())) {
                                equal = false;
                            }
                        }
                    }
                }

                if (!equal) {
                    String errorMessage = "The schema in the input link \"" + inputMeta.getTableName()
                            + "\" is different from the schema defined in the component.";
                    Problems.add(ProblemStatus.ERROR, this, errorMessage);
                }
            }
        }
    }

    public void checkNode() {
        checkParameters();
        checkSchema();
        checkLinks();
        checkModules();
        if (externalNode != null) {
            List<Problem> problems = externalNode.getProblems();
            for (Problem current : problems) {
                current.setElement(this);
                Problems.add(current);
            }
        }
    }

    public IComponent getComponent() {
        return this.component;
    }

    /**
     * 
     * DOC amaumont Node class global comment. Detailled comment <br/>
     * 
     * $Id$
     * 
     */
    public class Data {

        byte[] bytesData = new byte[0];

        String stringData = null;

        /**
         * DOC amaumont Data constructor comment.
         */
        public Data() {
            super();
        }

        /**
         * DOC amaumont Data constructor comment.
         * 
         * @param bytesData
         * @param stringData
         */
        public Data(byte[] bytesData, String stringData) {
            super();
            this.bytesData = bytesData;
            this.stringData = stringData;
        }

        public byte[] getBytesData() {
            return this.bytesData;
        }

        public void setBytesData(byte[] bytesData) {
            this.bytesData = bytesData;
        }

        public String getStringData() {
            return this.stringData;
        }

        public void setStringData(String stringData) {
            this.stringData = stringData;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.INode#renameMetadataColumnName(java.lang.String, java.lang.String,
     * java.lang.String)
     */
    public void renameMetadataColumnName(String conectionName, String oldColumnName, String newColumnName) {
        // TODO Auto-generated method stub
        System.out.println("Name=" + getComponentName() + ", " + conectionName + " " + oldColumnName + " " + newColumnName);
        
        if (externalNode!=null)
            externalNode.renameMetadataColumnName(conectionName, oldColumnName, newColumnName);
    }

}
