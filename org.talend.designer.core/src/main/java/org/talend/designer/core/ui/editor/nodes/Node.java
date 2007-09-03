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
import org.apache.log4j.Logger;
import org.apache.oro.text.regex.MalformedPatternException;
import org.apache.oro.text.regex.Pattern;
import org.apache.oro.text.regex.Perl5Compiler;
import org.apache.oro.text.regex.Perl5Matcher;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.core.CorePlugin;
import org.talend.core.model.components.IComponent;
import org.talend.core.model.components.IComponentsFactory;
import org.talend.core.model.components.IODataComponent;
import org.talend.core.model.general.ILibrariesService;
import org.talend.core.model.metadata.IMetadataColumn;
import org.talend.core.model.metadata.IMetadataTable;
import org.talend.core.model.metadata.MetadataTable;
import org.talend.core.model.metadata.MetadataTool;
import org.talend.core.model.process.BlockCode;
import org.talend.core.model.process.EConnectionType;
import org.talend.core.model.process.EParameterFieldType;
import org.talend.core.model.process.Element;
import org.talend.core.model.process.ElementParameterParser;
import org.talend.core.model.process.IConnection;
import org.talend.core.model.process.IConnectionCategory;
import org.talend.core.model.process.IElementParameter;
import org.talend.core.model.process.IExternalData;
import org.talend.core.model.process.IExternalNode;
import org.talend.core.model.process.INode;
import org.talend.core.model.process.INodeConnector;
import org.talend.core.model.process.INodeReturn;
import org.talend.core.model.process.IProcess;
import org.talend.core.model.process.Problem;
import org.talend.core.model.process.Problem.ProblemStatus;
import org.talend.designer.core.DesignerPlugin;
import org.talend.designer.core.model.components.EParameterName;
import org.talend.designer.core.ui.MultiPageTalendEditor;
import org.talend.designer.core.ui.editor.connections.Connection;
import org.talend.designer.core.ui.editor.nodecontainer.NodeContainer;
import org.talend.designer.core.ui.editor.process.Process;
import org.talend.designer.core.ui.editor.properties.controllers.ColumnListController;
import org.talend.designer.core.ui.preferences.TalendDesignerPrefConstants;
import org.talend.designer.core.ui.views.problems.Problems;
import org.talend.designer.runprocess.ProcessorUtilities;
import org.talend.repository.model.ExternalNodesFactory;

/**
 * Object that describes the node. All informations on nodes are stored in this class. <br/>
 * 
 * $Id$
 * 
 */
public class Node extends Element implements INode {

    private static Logger log = Logger.getLogger(Node.class);

    // true if this node is set as a start node.
    private boolean start;

    // true if this node is activated.
    private boolean activate = true;

    private int currentStatus;

    // properties
    public static final String LOCATION = "nodeLocation"; //$NON-NLS-1$

    public static final String SIZE = "nodeSize"; //$NON-NLS-1$

    public static final String INPUTS = "inputs"; //$NON-NLS-1$

    public static final String OUTPUTS = "outputs"; //$NON-NLS-1$

    public static final String PERFORMANCE_DATA = "perfData"; //$NON-NLS-1$

    public static final String TRACE_DATA = "traceData"; //$NON-NLS-1$

    public static final String UPDATE_STATUS = "addStatus"; //$NON-NLS-1$

    public static final String MODIFY_NODELABEL = "modifyNodeLabel";

    public static final int DEFAULT_SIZE = 32;

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

    private IExternalNode externalNode = null;

    // null if no external component defined

    private IExternalData externalData = null;

    private NodeContainer nodeContainer;

    private String performanceData;

    private Process process = null;

    private String pluginFullName;

    public static final Color START_COLOR = new Color(null, new RGB(0xB0, 0xE7, 0));

    private boolean readOnly = false;

    private static final String COMPARE_STR1 = "tDBInput";

    private static final String COMPARE_STR2 = "_MySchema_";

    private Dimension size;

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
        IElementParameter param = getElementParameter(EParameterName.REPOSITORY_ALLOW_AUTO_SWITCH.getName());
        if (param != null) {
            param.setValue(Boolean.TRUE);
        }
    }

    public Node(IComponent component, Process process) {
        this.process = process;
        init(component);
    }

    private void init(IComponent newComponent) {
        this.component = newComponent;
        this.label = component.getTranslatedName();
        this.componentName = this.label;

        IPreferenceStore store = DesignerPlugin.getDefault().getPreferenceStore();

        labelToParse = store.getString(TalendDesignerPrefConstants.DEFAULT_LABEL);
        hintToParse = store.getString(TalendDesignerPrefConstants.DEFAULT_HINT);
        showHint = store.getBoolean(TalendDesignerPrefConstants.DEFAULT_HINT_USED);

        nodeLabel = new NodeLabel(label, this);

        listConnector = this.component.createConnectors();
        metadataList = new ArrayList<IMetadataTable>();

        boolean hasMetadata = false;

        for (INodeConnector curConnector : getListConnector()) {
            if (curConnector.getDefaultConnectionType().hasConnectionCategory(IConnectionCategory.DATA)) {
                if (!curConnector.isBuiltIn()
                        && (curConnector.getMaxLinkInput() != 0 || curConnector.getMaxLinkOutput() != 0)) {
                    hasMetadata = true;
                    break;
                }
            }
        }

        setElementParameters(component.createElementParameters(this));

        // if (hasMetadata) {
        boolean hasSchemaType = false;
        for (IElementParameter param : getElementParameters()) {
            if (param.getField().equals(EParameterFieldType.SCHEMA_TYPE)) {
                IMetadataTable table = new MetadataTable();
                table.setAttachedConnector(param.getContext());
                metadataList.add(table);
                hasSchemaType = true;
            }
        }
        if (hasMetadata && !hasSchemaType) {
            // add a default metadata on the current component
            String mainConnector;
            if (isELTComponent()) {
                mainConnector = EConnectionType.TABLE.getName();
            } else {
                mainConnector = EConnectionType.FLOW_MAIN.getName();
            }
            IMetadataTable table = new MetadataTable();
            table.setAttachedConnector(mainConnector);
            metadataList.add(table);
        }
        // }

        listReturn = this.component.createReturns();
        String uniqueName = ((Process) getProcess()).generateUniqueNodeName(this);
        ((Process) getProcess()).addUniqueNodeName(uniqueName);
        setPropertyValue(EParameterName.UNIQUE_NAME.getName(), uniqueName);

        IElementParameter mappingParameter = MetadataTool.getMappingParameter((List<IElementParameter>) this
                .getElementParameters());

        for (IMetadataTable table : metadataList) {
            if (table.getAttachedConnector().equals(EConnectionType.FLOW_MAIN.getName())
                    || table.getAttachedConnector().equals(EConnectionType.TABLE.getName())) {
                table.setTableName(uniqueName);
            } else {
                table.setTableName(table.getAttachedConnector());
            }
            if (mappingParameter != null) {
                if (mappingParameter.getValue() != null && (!mappingParameter.getValue().equals(""))) {
                    table.setDbms((String) mappingParameter.getValue());
                }
            }

            for (int i = 0; i < getElementParameters().size(); i++) {
                IElementParameter param = getElementParameters().get(i);
                if (param.getField().equals(EParameterFieldType.MAPPING_TYPE)) {
                    table.setDbms((String) param.getValue());
                }
                if (param.getField().equals(EParameterFieldType.SCHEMA_TYPE)
                        && param.getContext().equals(table.getAttachedConnector())) {
                    if (param.getValue() instanceof IMetadataTable) {
                        IMetadataTable paramTable = (IMetadataTable) param.getValue();
                        table.getListColumns().addAll(paramTable.getListColumns());
                        table.setReadOnly(paramTable.isReadOnly());
                    }
                }
            }
        }
        setPropertyValue(EParameterName.LABEL.getName(), labelToParse);
        setPropertyValue(EParameterName.HINT.getName(), hintToParse);
        setPropertyValue(EParameterName.SHOW_HINT.getName(), new Boolean(showHint));
        pluginFullName = newComponent.getPluginFullName();
        if (pluginFullName != IComponentsFactory.COMPONENTS_LOCATION) {
            externalNode = ExternalNodesFactory.getInstance(pluginFullName);
        }

        if (isExternalNode()) {
            getExternalNode().initialize();
        }

        size = new Dimension();
        size.height = getIcon32().getImageData().height;
        size.width = getIcon32().getImageData().width;
    }

    public IProcess getProcess() {
        return process;
    }

    /**
     * 
     * Note that if there is several connectors of the same type, it will return the first one.
     * 
     * @param connType
     * @return
     */
    public INodeConnector getConnectorFromType(final EConnectionType connType) {
        INodeConnector nodeConnector = null;
        List<INodeConnector> listConnectors = new ArrayList<INodeConnector>();
        int nbConn = 0;

        EConnectionType testedType;

        if (connType.hasConnectionCategory(IConnectionCategory.FLOW)) {
            testedType = EConnectionType.FLOW_MAIN;
        } else {
            testedType = connType;
        }

        while ((nodeConnector == null) && (nbConn < listConnector.size())) {
            if (listConnector.get(nbConn).getDefaultConnectionType() == testedType) {
                nodeConnector = listConnector.get(nbConn);
                listConnectors.add(nodeConnector);
            }
            nbConn++;
        }
        return nodeConnector;
    }

    /**
     * 
     * Note that if there is several connectors the same name, it will return the first one.
     * 
     * @param connName
     * @return
     */
    public INodeConnector getConnectorFromName(final String connName) {
        INodeConnector nodeConnector = null;
        int nbConn = 0;

        while ((nodeConnector == null) && (nbConn < listConnector.size())) {
            if (listConnector.get(nbConn).getName().equals(connName)) {
                nodeConnector = listConnector.get(nbConn);
            }
            nbConn++;
        }
        return nodeConnector;
    }

    public List<INodeConnector> getConnectorsFromType(final EConnectionType connType) {
        INodeConnector nodeConnector = null;
        List<INodeConnector> listConnectors = new ArrayList<INodeConnector>();
        int nbConn = 0;

        EConnectionType testedType;

        if (connType.hasConnectionCategory(IConnectionCategory.FLOW)) {
            testedType = EConnectionType.FLOW_MAIN;
        } else {
            testedType = connType;
        }

        while (nbConn < listConnector.size()) {
            if (listConnector.get(nbConn).getDefaultConnectionType() == testedType) {
                nodeConnector = listConnector.get(nbConn);
                listConnectors.add(nodeConnector);
            }
            nbConn++;
        }
        return listConnectors;
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
    public ImageDescriptor getIcon32() {
        return component.getIcon32();
    }

    public ImageDescriptor getIcon24() {
        return component.getIcon24();
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
        firePropertyChange(MODIFY_NODELABEL, null, null);
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
        INodeConnector mainConnector;
        if (isELTComponent()) {
            mainConnector = this.getConnectorFromType(EConnectionType.TABLE);
        } else {
            mainConnector = this.getConnectorFromType(EConnectionType.FLOW_MAIN);
        }
        if (!mainConnector.isBuiltIn()
                && component.isSchemaAutoPropagated()
                && (connection.getLineStyle() == EConnectionType.FLOW_MAIN
                        || (connection.getLineStyle() == EConnectionType.TABLE) || ((connection.getLineStyle() == EConnectionType.FLOW_MERGE) && (connection
                        .getInputId() == 1))) && ((Process) getProcess()).isActivate()) {
            IMetadataTable inputTable = connection.getMetadataTable();

            for (INodeConnector connector : getListConnector()) {
                if (mainConnector.getName().equals(connector.getBaseSchema())) {
                    IMetadataTable targetTable = this.getMetadataFromConnector(connector.getName());
                    boolean customFound = false;
                    for (int i = 0; i < targetTable.getListColumns().size(); i++) {
                        IMetadataColumn column = targetTable.getListColumns().get(i);
                        if (column.isCustom()) {
                            customFound = true;
                            break;
                        }
                    }
                    if (((customFound && targetTable.isReadOnly()) || (outputs.size() == 0) || (connection
                            .getLineStyle() == EConnectionType.FLOW_MERGE))
                            && (inputTable.getListColumns().size() != 0)) {
                        // For the auto propagate.
                        MetadataTool.copyTable(inputTable, targetTable);
                        ColumnListController.updateColumnList(this, null);
                    }
                }
            }
        }
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

    public void setIncomingConnections(List<Connection> connections) {
        this.inputs = connections;
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
        if (!this.getConnectorFromType(EConnectionType.FLOW_MAIN).isBuiltIn() && component.isSchemaAutoPropagated()
                && (connection.getLineStyle() == EConnectionType.FLOW_MAIN)) {
            if (getMetadataList().get(0).isReadOnly()) {
                IMetadataTable originTable = getMetadataList().get(0);
                List<IMetadataColumn> columnToSave = new ArrayList<IMetadataColumn>();
                for (IMetadataColumn column : originTable.getListColumns()) {
                    if (column.isCustom()) {
                        columnToSave.add(column);
                    }
                }
                originTable.getListColumns().clear();
                originTable.getListColumns().addAll(columnToSave);
                originTable.sortCustomColumns();
            }
        }

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
        IElementParameter parameter = getElementParameter(id);
        if (parameter == null) { // in case we try to set a value to a
            // parameter that doesn't exists
            return;
        }
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
        if (id.equals(EParameterName.SCHEMA_TYPE.getName()) || id.equals(EParameterName.QUERYSTORE_TYPE.getName())
                || id.equals(EParameterName.PROPERTY_TYPE.getName())
                || id.equals(EParameterName.PROCESS_TYPE_PROCESS.getName())
                || id.equals(EParameterName.ENCODING_TYPE.getName())) {
            setPropertyValue(EParameterName.UPDATE_COMPONENTS.getName(), Boolean.TRUE);
        }

        if (id.equals(EParameterName.PROCESS_TYPE_CONTEXT.getName())) {
            IEditorPart part = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
            if (part instanceof MultiPageTalendEditor) {
                if (process.isActivate() && ((MultiPageTalendEditor) part).getProcess().equals(process)) {
                    ProcessorUtilities.generateCode((String) getPropertyValue(EParameterName.PROCESS_TYPE_PROCESS
                            .getName()), (String) value, false, false, ProcessorUtilities.GENERATE_MAIN_ONLY);
                    ((MultiPageTalendEditor) part).updateChildrens();
                }
            }
        }

        if (parameter.getField().equals(EParameterFieldType.MAPPING_TYPE)) {
            for (IMetadataTable table : getMetadataList()) {
                table.setDbms((String) value);
            }
        }

        parameter.setValue(value);
        updateVisibleData();
    }

    public List<IMetadataTable> getMetadataList() {
        return this.metadataList;
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
            List<IMetadataTable> copyOfMetadataList = new ArrayList<IMetadataTable>();
            for (IMetadataTable metaTable : getMetadataList()) {
                copyOfMetadataList.add(metaTable.clone());
            }
            externalNode.setMetadataList(copyOfMetadataList);
            externalNode.setIncomingConnections(inputs);
            externalNode.setOutgoingConnections(outputs);
            externalNode.setPluginFullName(getPluginFullName());
            externalNode.setElementParameters(getElementParameters());
            externalNode.setUniqueName(getUniqueName());
            externalNode.setSubProcessStart(isSubProcessStart());
            externalNode.setProcess(getProcess());
            externalNode.setComponent(getComponent());
        }
        return this.externalNode;
    }

    public void setExternalNode(final IExternalNode externalNode) {
        this.externalNode = externalNode;
    }

    public IExternalData getExternalData() {
        return this.externalData;
    }

    public void setExternalData(final IExternalData persistantData) {
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

    @SuppressWarnings("unchecked")//$NON-NLS-1$
    private void setActivate(final boolean activate) {
        this.activate = activate;
        nodeLabel.setActivate(activate);
        List<Connection> listConnections = (List<Connection>) this.getOutgoingConnections();
        for (int i = 0; i < listConnections.size(); i++) {
            if (listConnections.get(i).getTarget().isActivate()) {
                listConnections.get(i).setPropertyValue(EParameterName.ACTIVATE.getName(), activate);
            }
        }
        listConnections = (List<Connection>) this.getIncomingConnections();
        for (int i = 0; i < listConnections.size(); i++) {
            if (listConnections.get(i).getSource().isActivate()) {
                listConnections.get(i).setPropertyValue(EParameterName.ACTIVATE.getName(), activate);
            }
        }
        firePropertyChange(EParameterName.ACTIVATE.getName(), null, null);
    }

    public boolean hasRunIfLink() {
        boolean runIf = false;
        Connection connec;
        if (isActivate()) {
            for (int j = 0; j < getIncomingConnections().size() && !runIf; j++) {
                connec = (Connection) getIncomingConnections().get(j);
                if (connec.isActivate()) {
                    if ((connec.getLineStyle().equals(EConnectionType.RUN_IF)
                            || connec.getLineStyle().equals(EConnectionType.RUN_IF_ERROR) || connec.getLineStyle()
                            .equals(EConnectionType.RUN_IF_OK))) {
                        runIf = true;
                    }
                    if (!runIf) {
                        runIf = connec.getSource().hasRunIfLink();
                    }
                }
            }
        }
        return runIf;
    }

    public boolean isSubProcessStart() {
        Connection connec;
        if (isActivate()) {
            if (!isELTComponent()) {
                for (int j = 0; j < getIncomingConnections().size(); j++) {
                    connec = (Connection) getIncomingConnections().get(j);
                    if (connec.isActivate()) {
                        if (connec.getLineStyle().hasConnectionCategory(IConnectionCategory.MAIN)) {
                            return false;
                        }
                    }
                }
            } else {
                if (!(Boolean) getPropertyValue(EParameterName.STARTABLE.getName())) {
                    return false;
                }
            }
        }
        return true;
    }

    public IMetadataTable getMetadataTable(String metaName) {
        for (int i = 0; i < metadataList.size(); i++) {
            if (metadataList.get(i).getTableName().equals(metaName)/*
             * &&
             * metadataList.get(i).getAttachedConnector().equals(connectorName)
             */) {
                return metadataList.get(i);
            }
        }
        return null;
    }

    public IMetadataTable getMetadataFromConnector(String connector) {
        for (IMetadataTable table : metadataList) {
            if (table.getAttachedConnector().equals(connector)) {
                return table;
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
     * @see org.talend.core.model.process.INode#hasConditionnalOutputs()
     */
    public Boolean hasConditionalOutputs() {
        return component.hasConditionalOutputs();
    }

    public Boolean isMultiplyingOutputs() {
        return component.isMultiplyingOutputs();
    }

    public List<BlockCode> getBlocksCodeToClose() {
        return null;
    }

    /**
     * Will return the first item of the subprocess. If "withCondition" is true, if there is links from type RunIf /
     * RunAfter / RunBefore, it will return the first element found. If "withCondition" is false, it will return the
     * first element with no active link from type Main/Ref/Iterate.<br>
     * <i><b>Note:</b></i> This function doesn't work if the node has several start points (will return a random
     * start node).
     * 
     * @param withCondition
     * @return Start Node found.
     */
    public Node getSubProcessStartNode(boolean withConditions) {
        if (!withConditions) {
            // if ((getCurrentActiveLinksNbInput(EConnectionType.FLOW_MAIN) ==
            // 0)
            // // && (getCurrentActiveLinksNbInput(EConnectionType.FLOW_REF) ==
            // 0)
            // && (getCurrentActiveLinksNbInput(EConnectionType.ITERATE) == 0))
            // {
            // return this;
            // }
            // PTODO MHI / Modif à revoir avec NRO
            if ((getCurrentActiveLinksNbInput(IConnectionCategory.MAIN) == 0)) {
                return this;
            }
        } else {
            int nb = 0;
            for (Connection connection : inputs) {
                if (connection.isActivate()) {
                    nb++;
                }
            }
            if (nb == 0) {
                return this;
            }
        }
        Connection connec;

        for (int j = 0; j < getIncomingConnections().size(); j++) {
            connec = (Connection) getIncomingConnections().get(j);
            if (!connec.getLineStyle().hasConnectionCategory(IConnectionCategory.USE_HASH)) {
                return connec.getSource().getSubProcessStartNode(withConditions);
            }
        }
        return null;
    }

    private Node getMainBranch() {
        Node targetWithRef = null;
        for (int i = 0; i < getOutgoingConnections().size() && targetWithRef == null; i++) {
            IConnection connection = getOutgoingConnections().get(i);
            Node nodeTmp = (Node) connection.getTarget();
            if (connection.getLineStyle().hasConnectionCategory(IConnectionCategory.USE_HASH)) {
                // System.out.println(" ** Ref Link Found in:" + nodeTmp + "
                // from:" + this);
                targetWithRef = nodeTmp;
            } else {
                if (this.process.isThereLinkWithHash(nodeTmp)) {
                    // System.out.println(" ** Ref Link Found in:" + nodeTmp + "
                    // from:" + this);
                    targetWithRef = nodeTmp;
                }
            }
        }
        if (targetWithRef == null) {
            // System.out.println(" ** No Ref Links found from:" + this);
            return this;
        } else {
            // System.out.println(" ** Check Ref Links in:" + targetWithRef + "
            // from:" + this);
            return targetWithRef.getMainBranch();
        }
    }

    public Node getProcessStartNode(boolean withConditions) {
        // System.out.println(" --- Checking :" + this + " ---");
        return getMainBranch().getSubProcessStartNode(withConditions);
    }

    public boolean sameProcessAs(Node node, boolean withConditions) {
        // System.out.println("from:" + this + " -- to:" + node);

        Node currentNode = getSubProcessStartNode(withConditions);
        if (!currentNode.isStart()) {
            currentNode = currentNode.getProcessStartNode(withConditions);
        }
        Node otherNode = node.getSubProcessStartNode(withConditions);
        if (!otherNode.isStart()) {
            otherNode = otherNode.getProcessStartNode(withConditions);
        }
        // System.out.println("source start:" + currentNode + " -- target
        // start:" + otherNode);
        return currentNode.equals(otherNode);
    }

    public boolean isReadOnly() {
        return this.readOnly;
    }

    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
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

    public void updateStatus() {
        firePropertyChange(UPDATE_STATUS, null, new Integer(this.currentStatus));
    }

    public void addStatus(int status) {
        if ((this.currentStatus & status) != 0) {
            return;
        }
        this.currentStatus = this.currentStatus | status;
        updateStatus();
    }

    public void removeStatus(int status) {
        if ((this.currentStatus & status) != 0) {
            this.currentStatus = this.currentStatus ^ status;
        }
        updateStatus();
    }

    public int getStatus() {
        return currentStatus;
    }

    @SuppressWarnings("unchecked")//$NON-NLS-1$
    private void checkParameters() {
        for (IElementParameter param : this.getElementParameters()) {
            // if the parameter is required but empty, an error will be added
            if (param.isRequired() && param.isShow(getElementParameters())) {
                EParameterFieldType fieldType = param.getField();
                String value;
                switch (fieldType) {
                case TABLE:
                    List<Map<String, String>> tableValues = (List<Map<String, String>>) param.getValue();
                    if (tableValues.size() == 0) {
                        String errorMessage = "Parameter (" + param.getDisplayName()
                                + ") must have at least one value.";
                        Problems.add(ProblemStatus.ERROR, this, errorMessage);
                    }
                    break;
                case CHECK:
                    break;
                case SCHEMA_TYPE:
                    break;
                case MEMO_SQL:
                    String errMessage = "Parameter (" + param.getDisplayName()
                            + "): schema is different from the query.";
                    String currentQuery = param.getValue().toString();

                    // Checks if current query is empty.
                    if (currentQuery.equals("")) {
                        Problems.add(ProblemStatus.WARNING, this, errMessage);
                        break;
                    }

                    // Checks current query was generated by clicking "Guess
                    // Query" button.
                    if (currentQuery.indexOf(COMPARE_STR1) != -1 || currentQuery.indexOf(COMPARE_STR2) != -1) {
                        Problems.add(ProblemStatus.WARNING, this, errMessage);
                        break;
                    }
                    currentQuery = currentQuery.toUpperCase();
                    if (currentQuery.contains("SELECT") && currentQuery.contains("FROM")) {
                        currentQuery = currentQuery.substring(currentQuery.indexOf("SELECT") + "SELECT".length(),
                                currentQuery.indexOf("FROM"));
                        String[] columnArray = currentQuery.split(",");

                        int changedColumnSize = columnArray.length;
                        String compareStr = columnArray[0].trim().replaceAll("\n", "");

                        // Checks if nothing between "SELECT" and "FROM", show
                        // warning information on component.
                        if (changedColumnSize == 1 && compareStr.equals("")) {
                            Problems.add(ProblemStatus.WARNING, this, errMessage);
                            break;
                        }

                        if (this.getMetadataList().size() == 0) {
                            break;
                        }
                        IMetadataTable metaTable = this.getMetadataList().get(0);
                        if (metaTable == null || metaTable.getListColumns() == null) {
                            break;
                        }
                        int originColumnSize = metaTable.getListColumns().size();

                        if (changedColumnSize != originColumnSize && (compareStr != null && (!compareStr.equals("*")))) {

                            Problems.add(ProblemStatus.WARNING, this, errMessage);

                        }
                    }
                    break;
                case CLOSED_LIST:
                    value = (String) param.getValue();
                    if (value.equals("")) { //$NON-NLS-1$
                        String errorMessage = "Parameter (" + param.getDisplayName() + ") is empty but is required.";
                        Problems.add(ProblemStatus.ERROR, this, errorMessage);
                    } else {
                        if (param.getListItemsValue().length != 0) {
                            boolean found = false;
                            for (int i = 0; i < param.getListItemsValue().length && !found; i++) {
                                if (param.getListItemsValue()[i].equals(value)) {
                                    found = true;
                                }
                            }
                            if (!found) {
                                String errorMessage = "Parameter (" + param.getDisplayName() + ") has a value ("
                                        + value + ") that doesn't exist anymore.";
                                Problems.add(ProblemStatus.ERROR, this, errorMessage);
                            }
                        }
                    }
                    break;
                default:
                    value = (String) param.getValue();
                    if (value.equals("")) { //$NON-NLS-1$
                        String errorMessage = "Parameter (" + param.getDisplayName() + ") is empty but is required.";
                        Problems.add(ProblemStatus.ERROR, this, errorMessage);
                    }
                }
            }
        }
    }

    public int getCurrentActiveLinksNbInput(EConnectionType type) {
        int nb = 0;
        for (Connection connection : inputs) {
            if (connection.isActivate() && connection.getLineStyle().equals(type)) {
                nb++;
            }
        }
        return nb;
    }

    // PTODO MHIRT: Modif à revoir avec NRO
    public int getCurrentActiveLinksNbInput(int connCategory) {
        int nb = 0;
        for (Connection connection : inputs) {
            if (connection.isActivate() && connection.getLineStyle().hasConnectionCategory(connCategory)) {
                nb++;
            }
        }
        return nb;
    }

    public int getCurrentActiveLinksNbOutput(EConnectionType type) {
        int nb = 0;
        for (Connection connection : outputs) {
            if (connection.isActivate() && connection.getLineStyle().equals(type)) {
                nb++;
            }
        }
        return nb;
    }

    private void checkModules() {
        ILibrariesService moduleService = CorePlugin.getDefault().getLibrariesService();
        Problems.addAll(moduleService.getProblems(this, this));
    }

    private void checkLinks() {
        // check not startable components not linked
        if (!(Boolean) getPropertyValue(EParameterName.STARTABLE.getName())) {
            if ((getCurrentActiveLinksNbInput(EConnectionType.FLOW_MAIN) == 0)
                    && (getConnectorFromType(EConnectionType.FLOW_MAIN).getMinLinkInput() == 0)
                    & (getConnectorFromType(EConnectionType.FLOW_MAIN).getMaxLinkInput() != 0)) {
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
        if ((getConnectorFromType(EConnectionType.FLOW_MAIN).getMaxLinkInput() == 0)
                && (getConnectorFromType(EConnectionType.FLOW_MAIN).getMaxLinkOutput() != 0)) {
            if ((getCurrentActiveLinksNbOutput(EConnectionType.FLOW_MAIN) == 0)
                    && (getCurrentActiveLinksNbOutput(EConnectionType.FLOW_MERGE) == 0)
                    && (getCurrentActiveLinksNbOutput(EConnectionType.FLOW_REF) == 0)
                    && (getCurrentActiveLinksNbOutput(EConnectionType.ITERATE) == 0)) {
                String errorMessage = "This component should have outputs linked.";
                Problems.add(ProblemStatus.WARNING, this, errorMessage);
            }
        }

        // Check if there's an output run after / before on a component that is
        // not a sub process start
        if (!isSubProcessStart() || (!(Boolean) getPropertyValue(EParameterName.STARTABLE.getName()))) {
            if (/*
             * (getCurrentActiveLinksNbOutput(EConnectionType.RUN_AFTER) > 0) ||
             * (getCurrentActiveLinksNbOutput(EConnectionType.RUN_BEFORE) > 0)||
             */
            (getCurrentActiveLinksNbOutput(EConnectionType.THEN_RUN) > 0)) {
                String errorMessage = "A component that is not a sub process start can not have any link run after / run before in output.";
                Problems.add(ProblemStatus.ERROR, this, errorMessage);
            }
        }

        // Check if there's an input run after / before on a component that is
        // not a sub process start
        if ((!isELTComponent() && !isSubProcessStart())
                || (!(Boolean) getPropertyValue(EParameterName.STARTABLE.getName()))) {
            if (/*
             * (getCurrentActiveLinksNbInput(EConnectionType.RUN_AFTER) > 0) ||
             * (getCurrentActiveLinksNbInput(EConnectionType.RUN_BEFORE) > 0) ||
             */(getCurrentActiveLinksNbInput(EConnectionType.THEN_RUN) > 0)
                    || (getCurrentActiveLinksNbInput(EConnectionType.RUN_IF) > 0)
                    || (getCurrentActiveLinksNbInput(EConnectionType.RUN_IF_OK) > 0)
                    || (getCurrentActiveLinksNbInput(EConnectionType.RUN_IF_ERROR) > 0)) {
                String errorMessage = "A component that is not a sub process start can only have a data link or iterate link in input.";
                Problems.add(ProblemStatus.ERROR, this, errorMessage);
            }
        }

        for (INodeConnector nodeConnector : listConnector) {
            if (!nodeConnector.getDefaultConnectionType().hasConnectionCategory(IConnectionCategory.USE_HASH)
                    && nodeConnector.getDefaultConnectionType() != EConnectionType.FLOW_MERGE) {
                int nbMaxOut;
                nbMaxOut = nodeConnector.getMaxLinkOutput();
                int nbMaxIn;
                nbMaxIn = nodeConnector.getMaxLinkInput();
                int nbMinOut;
                nbMinOut = nodeConnector.getMinLinkOutput();
                int nbMinIn;
                nbMinIn = nodeConnector.getMinLinkInput();
                int curLinkOut;
                curLinkOut = nodeConnector.getCurLinkNbOutput();
                int curLinkIn;
                curLinkIn = nodeConnector.getCurLinkNbInput();
                String typeName = nodeConnector.getMenuName();
                if (nodeConnector.getDefaultConnectionType() == EConnectionType.FLOW_MAIN) {
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
        INodeConnector mainConnector;
        if (isELTComponent()) {
            mainConnector = this.getConnectorFromType(EConnectionType.TABLE);
        } else {
            mainConnector = this.getConnectorFromType(EConnectionType.FLOW_MAIN);
        }

        if (!isExternalNode()) {
            if (canEditSchema) {
                if ((mainConnector.getMaxLinkInput() == 0) && (mainConnector.getMaxLinkOutput() != 0)) {
                    if (getMetadataFromConnector(mainConnector.getName()).getListColumns().size() == 0) {
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

        // test empty schema in built in connections (several outputs with
        // different schema)
        if (!noSchema && (!canEditSchema || isExternalNode())) {
            if (mainConnector.isBuiltIn()) {
                if (getMetadataList() != null) {
                    for (IMetadataTable meta : getMetadataList()) {
                        if (meta.getListColumns().size() == 0) {
                            String tableLabel = meta.getTableName();
                            if (meta.getLabel() != null) {
                                tableLabel = meta.getLabel();
                            }
                            String errorMessage = "The output schema/link named \"" + tableLabel
                                    + "\" has no column defined, please check it.";
                            Problems.add(ProblemStatus.ERROR, this, errorMessage);
                        }
                    }
                }
            }
        }

        // test if the columns can be checked or not
        if (component.isSchemaAutoPropagated() && (getMetadataList().size() != 0)) {
            Connection inputConnecion = null;
            IMetadataTable inputMeta = null, outputMeta = getMetadataList().get(0);
            for (Connection connection : inputs) {
                if (connection.isActivate()
                        && (connection.getLineStyle().equals(EConnectionType.FLOW_MAIN) || connection.getLineStyle()
                                .equals(EConnectionType.TABLE))) {
                    inputMeta = connection.getMetadataTable();
                    inputConnecion = connection;
                }
            }

            if (inputMeta != null) {
                if (!outputMeta.sameMetadataAs(inputMeta, IMetadataColumn.OPTIONS_IGNORE_KEY
                        | IMetadataColumn.OPTIONS_IGNORE_NULLABLE | IMetadataColumn.OPTIONS_IGNORE_COMMENT
                        | IMetadataColumn.OPTIONS_IGNORE_PATTERN | IMetadataColumn.OPTIONS_IGNORE_DBCOLUMNNAME)) {
                    String errorMessage = "The schema from the input link \"" + inputConnecion.getName()
                            + "\" is different from the schema defined in the component.";
                    Problems.add(ProblemStatus.ERROR, this, errorMessage);
                }
            }
        }

        if (component.useMerge()) {
            if (getMetadataList().get(0).getListColumns().size() == 0) {
                String errorMessage = "No schema has been defined yet.";
                Problems.add(ProblemStatus.ERROR, this, errorMessage);
            } else {
                IMetadataTable firstSchema = inputs.get(0).getMetadataTable();
                boolean isSame = firstSchema.sameMetadataAs(getMetadataList().get(0));
                if (!isSame) {
                    String warningMessage = "The schema on the first input link of the merge component \""
                            + getUniqueName() + "\" is different from the schema defined in the component.";
                    Problems.add(ProblemStatus.WARNING, this, warningMessage);
                }
            }

            if (inputs.size() > 1) {
                IMetadataTable firstSchema = inputs.get(0).getMetadataTable();
                boolean isSame = true;
                for (int i = 1; i < inputs.size(); i++) {
                    if (!firstSchema.sameMetadataAs(inputs.get(i).getMetadataTable())) {
                        isSame = false;
                        break;
                    }
                }
                if (!isSame) {
                    String warningMessage = "The schemas on the input links of the merge component \""
                            + getUniqueName() + "\" are different, they should be the same.";
                    Problems.add(ProblemStatus.WARNING, this, warningMessage);
                }
            }
        }
    }

    public void checkAndRefreshNode() {
        Problems.clearAll(this);
        checkNode();
        Problems.refreshView();
    }

    public void checkNode() {
        if (isActivate()) {
            checkParameters();
            checkSchema();
            checkLinks();
            checkModules();
            if (externalNode != null) {
                List<Problem> problems = externalNode.getProblems();
                if (problems != null) {
                    for (Problem current : problems) {
                        current.setElement(this);
                        Problems.add(current);
                    }
                }
            }
        }
    }

    public IComponent getComponent() {
        return this.component;
    }

    public void setComponent(IComponent component) {
        this.component = component;
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

    public boolean canModifySchema() {
        boolean canModifySchema = false;
        List<? extends IElementParameter> listParam = this.getElementParameters();
        for (int i = 0; i < listParam.size(); i++) {
            IElementParameter param = listParam.get(i);
            if (param.isShow(listParam)) {
                if (param.getField().equals(EParameterFieldType.SCHEMA_TYPE)) {
                    canModifySchema = true;
                }
            }
        }
        return canModifySchema;
    }

    @Override
    public String toString() {
        StringBuffer buff = new StringBuffer();
        buff.append(getUniqueName() + " - ");
        buff.append("inputs:(");
        for (int i = 0; i < inputs.size(); i++) {
            buff.append(inputs.get(i).getName());
            if (i < (inputs.size() - 1)) {
                buff.append(",");
            }
        }
        buff.append(") ");
        buff.append("outputs:(");
        for (int i = 0; i < outputs.size(); i++) {
            buff.append(outputs.get(i).getName());
            if (i < (outputs.size() - 1)) {
                buff.append(",");
            }
        }
        buff.append(")");
        return buff.toString();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.INode#renameMetadataColumnName(java.lang.String, java.lang.String,
     * java.lang.String)
     */
    public void metadataInputChanged(IODataComponent dataComponent, String connectionToApply) {
        log.trace("InputChanged : Node=" + this + ", IOData=[" + dataComponent + "] on " + connectionToApply); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        if (externalNode != null) {
            externalNode.metadataInputChanged(dataComponent, connectionToApply);
        }
    }

    public void metadataOutputChanged(IODataComponent dataComponent, String connectionToApply) {
        log.trace("OutputChanged : Node=" + this + ", IOData=[" + dataComponent + "] on " + connectionToApply); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        if (externalNode != null) {
            externalNode.metadataOutputChanged(dataComponent, connectionToApply);
        }
    }

    public boolean isELTComponent() {
        return getComponent().getFamily().startsWith("ELT");
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.INode#isThereLinkWithHash()
     */
    public boolean isThereLinkWithHash() {
        return process.isThereLinkWithHash(this);
    }

    public List<? extends IConnection> getOutgoingSortedConnections() {
        return org.talend.core.model.utils.NodeUtil.getOutgoingSortedConnections(this);
    }

    public List<? extends IConnection> getMainOutgoingConnections() {
        return org.talend.core.model.utils.NodeUtil.getMainOutgoingConnections(this);
    }

    public List<? extends IConnection> getOutgoingConnections(EConnectionType connectionType) {
        return org.talend.core.model.utils.NodeUtil.getOutgoingConnections(this, connectionType);
    }

    public List<? extends IConnection> getOutgoingConnections(String connectorName) {
        return org.talend.core.model.utils.NodeUtil.getOutgoingConnections(this, connectorName);
    }

    public void renameData(String oldName, String newName) {
        if (oldName.equals(newName)) {
            return;
        }
        if (isExternalNode()) {
            getExternalNode().renameData(oldName, newName);
            return;
        }

        for (IElementParameter param : this.getElementParameters()) {
            if (param.getValue() instanceof String) { // for TEXT / MEMO etc..
                String value = (String) param.getValue();
                if (value.contains(oldName)) {
                    param.setValue(value.replaceAll(oldName, newName));
                }
            } else if (param.getValue() instanceof List) { // for TABLE
                List<Map<String, Object>> tableValues = (List<Map<String, Object>>) param.getValue();
                for (Map<String, Object> line : tableValues) {
                    for (String key : line.keySet()) {
                        Object cellValue = line.get(key);
                        if (cellValue instanceof String) { // cell is text so
                            // rename data if
                            // needed
                            String value = (String) cellValue;
                            if (value.contains(oldName)) {
                                line.put(key, value.replaceAll(oldName, newName));
                            }
                        }
                    }
                }
            }
        }
    }

    private boolean valueContains(String value, String toTest) {
        if (value.contains(toTest)) {
            Perl5Matcher matcher = new Perl5Matcher();
            Perl5Compiler compiler = new Perl5Compiler();
            Pattern pattern;

            try {
                pattern = compiler.compile("(.*" + toTest + "[^0-9]+.*)"); //$NON-NLS-1$
                if (matcher.contains(value, pattern)) {
                    return true;
                }
            } catch (MalformedPatternException e) {
                throw new RuntimeException(e);
            }
        }
        return false;
    }

    public boolean useData(String name) {
        if (isExternalNode()) {
            return getExternalNode().useData(name);
        }
        for (IElementParameter param : this.getElementParameters()) {
            if (param.getValue() instanceof String) { // for TEXT / MEMO etc..
                String value = (String) param.getValue();
                if (valueContains(value, name)) {
                    return true;
                }
            } else if (param.getValue() instanceof List) { // for TABLE
                List<Map<String, Object>> tableValues = (List<Map<String, Object>>) param.getValue();
                for (Map<String, Object> line : tableValues) {
                    for (String key : line.keySet()) {
                        Object cellValue = line.get(key);
                        if (cellValue instanceof String) { // cell is text so
                            // test data
                            if (valueContains((String) cellValue, name)) {
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    public boolean isThereLinkWithMerge() {
        // the merge order start from 1
        return process.getMergelinkOrder(this) >= 1;
    }

    public Map<INode, Integer> getLinkedMergeInfo() {
        return process.getLinkedMergeInfo(this);
    }

    public List<? extends IConnection> getIncomingConnections(EConnectionType connectionType) {
        return org.talend.core.model.utils.NodeUtil.getIncomingConnections(this, connectionType);
    }

    /**
     * Getter for size.
     * 
     * @return the size
     */
    public Dimension getSize() {
        return size;
    }

    /**
     * Sets the size.
     * 
     * @param size the size to set
     */
    public void setSize(Dimension size) {
        this.size = size;
        firePropertyChange(SIZE, null, null);
    }

    /**
     * Getter for listConnector.
     * 
     * @return the listConnector
     */
    public List<? extends INodeConnector> getListConnector() {
        return listConnector;
    }
}
