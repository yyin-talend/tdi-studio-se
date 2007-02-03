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

package org.talend.designer.core.ui.editor.process;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.apache.oro.text.regex.MalformedPatternException;
import org.apache.oro.text.regex.Pattern;
import org.apache.oro.text.regex.Perl5Compiler;
import org.apache.oro.text.regex.Perl5Matcher;
import org.eclipse.core.resources.IFile;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.SnapToGeometry;
import org.eclipse.gef.SnapToGrid;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.views.properties.PropertySheet;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;
import org.talend.commons.exception.PersistenceException;
import org.talend.core.context.RepositoryContext;
import org.talend.core.model.components.IComponent;
import org.talend.core.model.metadata.EMetadataType;
import org.talend.core.model.metadata.IMetadataTable;
import org.talend.core.model.metadata.builder.ConvertionHelper;
import org.talend.core.model.metadata.builder.connection.MetadataTable;
import org.talend.core.model.metadata.designerproperties.RepositoryToComponentProperty;
import org.talend.core.model.process.EComponentCategory;
import org.talend.core.model.process.EConnectionType;
import org.talend.core.model.process.EParameterFieldType;
import org.talend.core.model.process.Element;
import org.talend.core.model.process.IConnection;
import org.talend.core.model.process.IContext;
import org.talend.core.model.process.IContextManager;
import org.talend.core.model.process.IContextParameter;
import org.talend.core.model.process.IElementParameter;
import org.talend.core.model.process.INode;
import org.talend.core.model.process.INodeConnector;
import org.talend.core.model.process.IProcess;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.core.model.properties.Property;
import org.talend.core.model.properties.User;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.IRepositoryObject;
import org.talend.core.model.temp.ECodeLanguage;
import org.talend.designer.core.DesignerPlugin;
import org.talend.designer.core.i18n.Messages;
import org.talend.designer.core.model.components.EParameterName;
import org.talend.designer.core.model.components.ElementParameter;
import org.talend.designer.core.model.components.EmfComponent;
import org.talend.designer.core.model.context.Context;
import org.talend.designer.core.model.context.ContextManager;
import org.talend.designer.core.model.context.ContextParameter;
import org.talend.designer.core.model.metadata.MetadataEmfFactory;
import org.talend.designer.core.model.process.DataProcess;
import org.talend.designer.core.model.utils.emf.talendfile.ConnectionType;
import org.talend.designer.core.model.utils.emf.talendfile.ContextParameterType;
import org.talend.designer.core.model.utils.emf.talendfile.ContextType;
import org.talend.designer.core.model.utils.emf.talendfile.DocumentRoot;
import org.talend.designer.core.model.utils.emf.talendfile.ElementParameterType;
import org.talend.designer.core.model.utils.emf.talendfile.ElementValueType;
import org.talend.designer.core.model.utils.emf.talendfile.JobType;
import org.talend.designer.core.model.utils.emf.talendfile.MetadataType;
import org.talend.designer.core.model.utils.emf.talendfile.NodeType;
import org.talend.designer.core.model.utils.emf.talendfile.ProcessType;
import org.talend.designer.core.model.utils.emf.talendfile.RequiredType;
import org.talend.designer.core.model.utils.emf.talendfile.TalendFileFactory;
import org.talend.designer.core.model.utils.emf.talendfile.util.TalendFileResourceImpl;
import org.talend.designer.core.ui.MultiPageTalendEditor;
import org.talend.designer.core.ui.editor.connections.Connection;
import org.talend.designer.core.ui.editor.nodecontainer.NodeContainer;
import org.talend.designer.core.ui.editor.nodes.Node;
import org.talend.designer.core.ui.editor.nodes.Node.Data;
import org.talend.designer.core.ui.preferences.TalendDesignerPrefConstants;
import org.talend.designer.core.ui.views.problems.Problems;
import org.talend.repository.model.ComponentsFactoryProvider;
import org.talend.repository.model.IProxyRepositoryFactory;

/**
 * The diagram will contain all elements (nodes, connections) The xml that describes the diagram will be saved from the
 * information of this class. <br/>
 * 
 * $Id$
 * 
 */
public class Process extends Element implements IProcess {

    private static final Integer MAX_LOG_LEVEL = new Integer(100);

    // properties
    public static final String NODES = "nodes"; //$NON-NLS-1$

    public static final String DEFAULT_CONNECTION_NAME = Messages.getString("Process.1"); //$NON-NLS-1$

    protected List<Node> nodes = new ArrayList<Node>();

    protected List<Element> elem = new ArrayList<Element>();

    private String name = new String(Messages.getString("Process.2")); //$NON-NLS-1$

    private boolean activate = true;

    // list where is stored each unique name for the connections
    private List<String> uniqueConnectionNameList = new ArrayList<String>();

    // list where is stored each unique name for the nodes
    private List<String> uniqueNodeNameList = new ArrayList<String>();

    private boolean readOnly;

    private GraphicalViewer viewer = null;

    public static final Color READ_ONLY_COLOR = new Color(null, new RGB(0xE7, 0xE7, 0xE7));

    public static final Color READ_WRITE_COLOR = new Color(null, new RGB(255, 255, 255));

    private IContextManager contextManager;

    private ECodeLanguage processLanguage;

    public static final int BREAKPOINT_STATUS = 1;

    public static final int ERROR_STATUS = 2;

    public static final int WARNING_STATUS = 4;

    private Property property;

    private boolean initDone = false;

    public Process() {
        // PTODO NRO save the language in the process and load/test it.

        processLanguage = ((RepositoryContext) org.talend.core.CorePlugin.getContext().getProperty(
                org.talend.core.context.Context.REPOSITORY_CONTEXT_KEY)).getProject().getLanguage();

        contextManager = new ContextManager(processLanguage);
        createProcessParameters();
    }

    public Process(Property property) {
        this();
        this.property = property;
        init();
    }

    private void init() {
        if (!initDone) {
            setId(property.getId());
            setLabel(property.getLabel());
            setVersion(property.getVersion());
            setAuthor(property.getAuthor());
            setStatusCode(property.getStatusCode());
            if (getStatusCode() == null) {
                setStatusCode(""); //$NON-NLS-1$
            }
            initDone = true;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.getId() == null) ? 0 : this.getId().hashCode());
        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Process other = (Process) obj;
        if (!this.getId().equals(other.getId())) {
            return false;
        }
        return true;
    }

    /**
     * Add all parameters for a process.
     */
    private void createProcessParameters() {
        ElementParameter param;

        param = new ElementParameter(this);
        param.setName(EParameterName.NAME.getName());
        param.setCategory(EComponentCategory.MAIN);
        param.setField(EParameterFieldType.TEXT);
        param.setDisplayName(EParameterName.NAME.getDisplayName());
        param.setNumRow(1);
        param.setValue(new String());
        param.setRequired(true);
        addElementParameter(param);

        param = new ElementParameter(this);
        param.setName(EParameterName.AUTHOR.getName());
        param.setCategory(EComponentCategory.MAIN);
        param.setField(EParameterFieldType.TEXT);
        param.setDisplayName(EParameterName.AUTHOR.getDisplayName());
        param.setNumRow(2);
        param.setValue(new String());
        param.setRequired(true);
        param.setReadOnly(true);
        addElementParameter(param);

        param = new ElementParameter(this);
        param.setName(EParameterName.VERSION.getName());
        param.setCategory(EComponentCategory.MAIN);
        param.setField(EParameterFieldType.VERSION);
        param.setDisplayName(EParameterName.VERSION.getDisplayName());
        param.setNumRow(2);
        param.setValue(new String());
        addElementParameter(param);

        param = new ElementParameter(this);
        param.setName(EParameterName.STATUS.getName());
        param.setCategory(EComponentCategory.MAIN);
        param.setField(EParameterFieldType.TEXT);
        param.setDisplayName(EParameterName.STATUS.getDisplayName());
        param.setNumRow(2);
        param.setValue(new String());
        addElementParameter(param);

        param = new ElementParameter(this);
        param.setName(EParameterName.PURPOSE.getName());
        param.setCategory(EComponentCategory.MAIN);
        param.setField(EParameterFieldType.TEXT);
        param.setDisplayName(EParameterName.PURPOSE.getDisplayName());
        param.setNumRow(3);
        param.setValue(new String());
        addElementParameter(param);

        param = new ElementParameter(this);
        param.setName(EParameterName.DESCRIPTION.getName());
        param.setCategory(EComponentCategory.MAIN);
        param.setField(EParameterFieldType.MEMO);
        param.setNbLines(5);
        param.setDisplayName(EParameterName.DESCRIPTION.getDisplayName());
        param.setNumRow(4);
        param.setValue(new String());
        addElementParameter(param);

        param = new ElementParameter(this);
        param.setName(EParameterName.LEVEL_LOG_TO_FILE.getName());
        param.setCategory(EComponentCategory.LOGS);
        param.setField(EParameterFieldType.CLOSED_LIST);
        param.setDisplayName(EParameterName.LEVEL_LOG_TO_FILE.getDisplayName());
        String[] levelValues = new String[MAX_LOG_LEVEL];
        for (Integer i = new Integer(1); i <= MAX_LOG_LEVEL; i++) {
            levelValues[i - 1] = new String(i.toString());
        }
        param.setListItemsValue(levelValues);
        param.setListItemsDisplayName(levelValues);
        param.setValue(MAX_LOG_LEVEL.toString());
        param.setNumRow(1);
        addElementParameter(param);

        param = new ElementParameter(this);
        param.setName(EParameterName.LOG_TO_FILE.getName());
        param.setCategory(EComponentCategory.LOGS);
        param.setField(EParameterFieldType.CHECK);
        param.setDisplayName(EParameterName.LOG_TO_FILE.getDisplayName());
        param.setNumRow(1);
        param.setValue(new Boolean(false));
        addElementParameter(param);

        param = new ElementParameter(this);
        param.setName(EParameterName.LOG_FILENAME.getName());
        param.setCategory(EComponentCategory.LOGS);
        param.setField(EParameterFieldType.FILE);
        param.setDisplayName(EParameterName.LOG_FILENAME.getDisplayName());
        param.setNumRow(1);
        param.setValue(new String());
        addElementParameter(param);

        param = new ElementParameter(this);
        param.setName(EParameterName.LEVEL_LOG_TO_DB.getName());
        param.setCategory(EComponentCategory.LOGS);
        param.setField(EParameterFieldType.CLOSED_LIST);
        param.setDisplayName(EParameterName.LEVEL_LOG_TO_DB.getDisplayName());
        param.setListItemsValue(levelValues);
        param.setListItemsDisplayName(levelValues);
        param.setValue(MAX_LOG_LEVEL.toString());
        param.setNumRow(2);
        addElementParameter(param);

        param = new ElementParameter(this);
        param.setName(EParameterName.LOG_TO_DB.getName());
        param.setCategory(EComponentCategory.LOGS);
        param.setField(EParameterFieldType.CHECK);
        param.setDisplayName(EParameterName.LOG_TO_DB.getDisplayName());
        param.setNumRow(2);
        param.setValue(new Boolean(false));
        addElementParameter(param);

        param = new ElementParameter(this);
        param.setName(EParameterName.LEVEL_LOG_TO_STDOUT.getName());
        param.setCategory(EComponentCategory.LOGS);
        param.setField(EParameterFieldType.CLOSED_LIST);
        param.setDisplayName(EParameterName.LEVEL_LOG_TO_STDOUT.getDisplayName());
        param.setListItemsValue(levelValues);
        param.setListItemsDisplayName(levelValues);
        param.setValue(MAX_LOG_LEVEL.toString());
        param.setNumRow(3);
        addElementParameter(param);

        param = new ElementParameter(this);
        param.setName(EParameterName.LOG_TO_STDOUT.getName());
        param.setCategory(EComponentCategory.LOGS);
        param.setField(EParameterFieldType.CHECK);
        param.setDisplayName(EParameterName.LOG_TO_STDOUT.getDisplayName());
        param.setNumRow(3);
        param.setValue(new Boolean(false));
        addElementParameter(param);

        param = new ElementParameter(this);
        param.setName(EParameterName.COMP_DEFAULT_FILE_DIR.getName());
        param.setCategory(EComponentCategory.MAIN);
        param.setField(EParameterFieldType.DIRECTORY);
        param.setDisplayName(EParameterName.COMP_DEFAULT_FILE_DIR.getDisplayName());
        param.setNumRow(99);
        param.setShow(false);
        param.setValue(DesignerPlugin.getDefault().getPluginPreferences().getString(
                TalendDesignerPrefConstants.COMP_DEFAULT_FILE_DIR));
        addElementParameter(param);
    }

    /**
     * Add a new node to the diagram.
     * 
     * @param node
     */
    public void addNodeContainer(final NodeContainer nodeContainer) {
        elem.add(nodeContainer);
        elem.add(nodeContainer.getNode());
        nodes.add(nodeContainer.getNode());
        fireStructureChange(NODES, elem);
    }

    /**
     * Remove a node to the diagram.
     * 
     * @param node
     */
    public void removeNodeContainer(final NodeContainer nodeContainer) {
        removeUniqueNodeName(nodeContainer.getNode().getUniqueName());
        nodes.remove(nodeContainer.getNode());
        elem.remove(nodeContainer.getNode());
        elem.remove(nodeContainer);
        fireStructureChange(NODES, elem);
    }

    /**
     * Get the list of all elements, Node and Connection.
     * 
     * @return
     */
    public List getElements() {
        return this.elem;
    }

    public List<? extends INode> getGraphicalNodes() {
        return this.nodes;
    }

    public List<? extends INode> getGeneratingNodes() {
        DataProcess.buildFromGraphicalProcess(nodes);
        return DataProcess.getNodeList();
    }

    /*
     * public double getZoom() { return zoom; }
     */
    private void retrieveAttachedViewer() {
        IEditorPart editorPart = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
        if (editorPart instanceof MultiPageTalendEditor) {
            viewer = ((MultiPageTalendEditor) editorPart).getTalendEditor().getViewer();
        }
    }

    public void setViewer(GraphicalViewer viewer) {
        this.viewer = viewer;
    }

    /**
     * Returns true if the grid is enabled.
     * 
     * @return
     */
    public boolean isGridEnabled() {
        if (viewer == null) {
            retrieveAttachedViewer();
            if (viewer == null) {
                return (Boolean) viewer.getProperty(SnapToGrid.PROPERTY_GRID_ENABLED);
            }
        } else {
            return (Boolean) viewer.getProperty(SnapToGrid.PROPERTY_GRID_ENABLED);
        }
        return false;
    }

    /**
     * Returns true if the SnapToGeometry is enabled.
     * 
     * @return
     */
    public boolean isSnapToGeometryEnabled() {
        if (viewer == null) {
            retrieveAttachedViewer();
            if (viewer == null) {
                return (Boolean) viewer.getProperty(SnapToGeometry.PROPERTY_SNAP_ENABLED);
            }
        } else {
            return (Boolean) viewer.getProperty(SnapToGeometry.PROPERTY_SNAP_ENABLED);
        }
        return false;
    }

    public static IMetadataTable getMetadataFromRepository(String metaRepositoryName) {
        IProxyRepositoryFactory factory = DesignerPlugin.getDefault().getProxyRepositoryFactory();
        List<ConnectionItem> metadataConnectionsItem = null;
        List<String> repositoryList = new ArrayList<String>();
        IMetadataTable metaToReturn = null;
        try {
            metadataConnectionsItem = factory.getMetadataConnectionsItem();
        } catch (PersistenceException e) {
            throw new RuntimeException(e);
        }
        if (metadataConnectionsItem != null) {
            for (ConnectionItem connectionItem : metadataConnectionsItem) {
                org.talend.core.model.metadata.builder.connection.Connection connection;
                connection = (org.talend.core.model.metadata.builder.connection.Connection) connectionItem
                        .getConnection();
                for (Object tableObj : connection.getTables()) {
                    MetadataTable table = (MetadataTable) tableObj;
                    if (!factory.isDeleted(table)) {
                        String name = connectionItem.getProperty().getId() + " - " + table.getLabel(); //$NON-NLS-1$
                        if (name.equals(metaRepositoryName)) {
                            metaToReturn = ConvertionHelper.convert(table);
                        }
                        repositoryList.add(name);
                    }
                }
            }
        }
        return metaToReturn;
    }

    @SuppressWarnings("unchecked")//$NON-NLS-1$
    private void saveElementParameters(TalendFileFactory fileFact, List<? extends IElementParameter> paramList,
            EList listParamType, ProcessType process) {
        IElementParameter param;
        ElementParameterType pType;

        for (int j = 0; j < paramList.size(); j++) {
            param = paramList.get(j);
            if (param.getName().equals(EParameterName.PROCESS_TYPE_PROCESS.getName())) {
                // if this parameter is defined in a component, then we add a dependancy to this job.
                RequiredType rType = process.getRequired();
                if (rType == null) {
                    rType = fileFact.createRequiredType();
                    process.setRequired(rType);
                }
                JobType jType = fileFact.createJobType();
                String jobName;
                jobName = ((String) param.getValue()).replace("'", ""); //$NON-NLS-1$ //$NON-NLS-2$
                jType.setName(jobName);
                String contextName = ""; //$NON-NLS-1$
                boolean found = false;
                for (int i = 0; i < paramList.size() && !found; i++) {
                    IElementParameter contextParam = paramList.get(i);
                    if (contextParam.getName().equals(EParameterName.PROCESS_TYPE_CONTEXT.getName())) {
                        contextName = ((String) contextParam.getValue()).replace("'", ""); //$NON-NLS-1$ //$NON-NLS-2$
                        found = true;
                    }
                }
                jType.setContext(contextName);
                rType.getJob().add(jType);
            }
            if ((!param.isReadOnly()) || param.getName().equals(EParameterName.UNIQUE_NAME.getName())
                    || param.getName().equals(EParameterName.VERSION.getName())) {
                pType = fileFact.createElementParameterType();
                pType.setName(param.getName());
                pType.setField(param.getField().getName());
                Object value = param.getValue();
                if (param.getField().equals(EParameterFieldType.TABLE)) {
                    List<Map<String, Object>> tableValues = (List<Map<String, Object>>) value;
                    for (Map<String, Object> currentLine : tableValues) {
                        for (int i = 0; i < currentLine.size(); i++) {
                            ElementValueType elementValue = fileFact.createElementValueType();
                            elementValue.setElementRef(param.getListItemsDisplayCodeName()[i]);
                            Object o = currentLine.get(param.getListItemsDisplayCodeName()[i]);
                            String strValue = ""; //$NON-NLS-1$
                            if (o instanceof Integer) {
                                IElementParameter tmpParam = (IElementParameter) param.getListItemsValue()[i];
                                if (tmpParam.getListItemsValue().length == 0) {
                                    strValue = ""; //$NON-NLS-1$
                                } else {
                                    strValue = (String) tmpParam.getListItemsValue()[(Integer) o];
                                }
                            } else {
                                if (o instanceof String) {
                                    strValue = (String) o;
                                } else {
                                    if (o instanceof Boolean) {
                                        strValue = (String) ((Boolean) o).toString();
                                    }
                                }
                            }
                            elementValue.setValue(strValue);
                            pType.getElementValue().add(elementValue);
                        }
                    }
                } else {
                    if (value == null) {
                        pType.setValue(""); //$NON-NLS-1$
                    } else {
                        if (value instanceof Boolean) {
                            pType.setValue((String) ((Boolean) value).toString());
                        } else {
                            if (value instanceof String) {
                                pType.setValue((String) value);
                            }
                        }
                    }
                }
                listParamType.add(pType);
            }
        }
    }

    @SuppressWarnings("unchecked")//$NON-NLS-1$
    private void loadElementParameters(Element elemParam, EList listParamType) {
        ElementParameterType pType;

        for (int j = 0; j < listParamType.size(); j++) {
            pType = (ElementParameterType) listParamType.get(j);

            if (pType != null) {
                IElementParameter param = elemParam.getElementParameter(pType.getName());
                if (param != null) {
                    if (param.getField().equals(EParameterFieldType.CHECK)) {
                        elemParam.setPropertyValue(pType.getName(), new Boolean(pType.getValue()));
                    } else if (param.getField().equals(EParameterFieldType.TABLE)) {
                        List<Map<String, Object>> tableValues = new ArrayList<Map<String, Object>>();
                        String[] codeList = param.getListItemsDisplayCodeName();
                        Map<String, Object> lineValues = null;
                        for (ElementValueType elementValue : (List<ElementValueType>) pType.getElementValue()) {
                            boolean found = false;
                            for (int i = 0; i < codeList.length && !found; i++) {
                                if (codeList[i].equals(elementValue.getElementRef())) {
                                    found = true;
                                }
                            }
                            if (found) {
                                if ((lineValues == null) || (lineValues.get(elementValue.getElementRef()) != null)) {
                                    lineValues = new HashMap<String, Object>();
                                    tableValues.add(lineValues);
                                }
                                lineValues.put(elementValue.getElementRef(), elementValue.getValue());
                            }
                        }
                        elemParam.setPropertyValue(pType.getName(), tableValues);
                    } else {
                        elemParam.setPropertyValue(pType.getName(), pType.getValue());
                    }
                }
            }
        }
    }

    /**
     * Save the diagram in a Xml File.
     * 
     * @param file
     * @return
     * @throws IOException
     */
    @SuppressWarnings("unchecked")//$NON-NLS-1$
    public ProcessType saveXmlFile(final IFile file) throws IOException {
        String fileName;

        init();
        fileName = file.getLocationURI().toString();
        URI uri = URI.createURI(fileName);
        Resource res = new TalendFileResourceImpl(uri);

        TalendFileFactory fileFact = TalendFileFactory.eINSTANCE;
        DocumentRoot xmlDoc;
        xmlDoc = fileFact.createDocumentRoot();

        ProcessType process = fileFact.createProcessType();
        xmlDoc.setProcess(process);

        EList nList = process.getNode();
        EList cList = process.getConnection();

        Node node;
        NodeType nType;
        List<Connection> connList;
        Connection connec;
        ConnectionType cType;

        List<? extends IElementParameter> paramList;
        List<IMetadataTable> listMetaData;
        EList listParamType, listMetaType;
        IMetadataTable metaData;
        MetadataEmfFactory factory = new MetadataEmfFactory();
        for (int i = 0; i < nodes.size(); i++) {
            node = nodes.get(i);
            nType = fileFact.createNodeType();
            nType.setComponentVersion(node.getComponent().getVersion());
            nType.setComponentName(node.getComponent().getName());
            nType.setPosX(node.getLocation().x);
            nType.setPosY(node.getLocation().y);
            nType.setOffsetLabelX(node.getNodeLabel().getOffset().x);
            nType.setOffsetLabelY(node.getNodeLabel().getOffset().y);
            if (node.getExternalNode() != null) {
                if (node.getExternalData() != null) {
                    Data data = node.getExternalBytesData();
                    nType.setBinaryData(data.getBytesData());
                    nType.setStringData(data.getStringData());
                }
            }
            listParamType = nType.getElementParameter();
            paramList = node.getElementParameters();
            saveElementParameters(fileFact, paramList, listParamType, process);
            listMetaType = nType.getMetadata();
            listMetaData = node.getMetadataList();
            for (int j = 0; j < listMetaData.size(); j++) {
                metaData = listMetaData.get(j);
                factory.setMetadataTable(metaData);
                listMetaType.add(factory.getMetadataType());
            }

            connList = (List<Connection>) node.getOutgoingConnections();
            for (int j = 0; j < connList.size(); j++) {
                connec = connList.get(j);
                cType = fileFact.createConnectionType();
                cType.setSource(node.getUniqueName());
                cType.setTarget(connec.getTarget().getUniqueName());
                cType.setLabel(connec.getName());
                cType.setLineStyle(connec.getLineStyleId());
                cType.setOffsetLabelX(connec.getConnectionLabel().getOffset().x); //$NON-NLS-1$
                cType.setOffsetLabelY(connec.getConnectionLabel().getOffset().y); //$NON-NLS-1$
                cType.setMetaname(connec.getMetaName());
                listParamType = cType.getElementParameter();
                paramList = connec.getElementParameters();
                saveElementParameters(fileFact, paramList, listParamType, process);
                cList.add(cType);
            }
            nList.add(nType);
        }

        /**
         * Save the contexts informations
         */
        process.setDefaultContext(contextManager.getDefaultContext().getName());

        EList contextTypeList = process.getContext();
        ContextType contextType;
        IContext context;

        EList contextTypeParamList;
        ContextParameterType contextParamType;
        IContextParameter contextParam;

        for (int i = 0; i < contextManager.getListContext().size(); i++) {
            contextType = fileFact.createContextType();
            context = contextManager.getListContext().get(i);
            contextType.setName(context.getName());
            contextType.setConfirmationNeeded(context.isConfirmationNeeded());
            contextTypeParamList = contextType.getContextParameter();

            if (context.getContextParameterList() != null) {
                for (int j = 0; j < context.getContextParameterList().size(); j++) {
                    contextParamType = fileFact.createContextParameterType();
                    contextParam = context.getContextParameterList().get(j);
                    contextParamType.setName(contextParam.getName());
                    contextParamType.setPrompt(contextParam.getPrompt());
                    contextParamType.setType(contextParam.getType().getName());
                    contextParamType.setValue(contextParam.getValue());
                    contextParamType.setPromptNeeded(contextParam.isPromptNeeded());

                    contextTypeParamList.add(contextParamType);
                }
            }

            contextTypeList.add(contextType);
        }

        res.getContents().add(xmlDoc);

        HashMap options = new HashMap(1);
        options.put(XMLResource.OPTION_ENCODING, "UTF-8"); //$NON-NLS-1$

        res.save(options);

        return process;
    }

    /**
     * DOC mhelleboid Comment method "loadXmlFile".
     * 
     * @param process
     */
    public void loadXmlFile(ProcessType process) {
        init();
        Hashtable<String, Node> nodesHashtable = new Hashtable<String, Node>();

        setActivate(false);
        loadProcessProperties(process);

        try {
            loadNodes(process, nodesHashtable);
        } catch (PersistenceException e) {
            // there are some components unloaded.
            return;
        }

        loadConnections(process, nodesHashtable);
        loadContexts(process);
        initExternalComponents();
        setActivate(true);
        checkStartNodes();
        // checkProcess();
    }

    private void initExternalComponents() {
        for (Node node : nodes) {
            if (node.isExternalNode()) {
                node.getExternalNode().initialize();
            }
        }
    }

    private void loadProcessProperties(ProcessType process) {
        // setPropertyValue(EParameterName.NAME.getName(), process.getName());
        // setPropertyValue(EParameterName.AUTHOR.getName(),
        // process.getAuthor());
        // setPropertyValue(EParameterName.STATUS.getName(),
        // process.getStatus());
        // setPropertyValue(EParameterName.VERSION.getName(), new
        // Version(process.getVersion()));
        // setPropertyValue(EParameterName.PURPOSE.getName(),
        // process.getPurpose());
        // setPropertyValue(EParameterName.DESCRIPTION.getName(),
        // process.getDescription());

        // logs are not used anymore
        // LogsType lType = process.getLogs();
        // LogToFileType lFileType = lType.getLogToFile();
        // setPropertyValue(EParameterName.LOG_FILENAME.getName(), lFileType.getFilename());
        // setPropertyValue(EParameterName.LEVEL_LOG_TO_FILE.getName(), new Integer(lFileType.getLevel()).toString());
        // setPropertyValue(EParameterName.LOG_TO_FILE.getName(), new Boolean(lFileType.isSelected()));
        // lType.setLogToFile(lFileType);
        // LogToDatabaseType lDBType = lType.getLogToDatabase();
        // setPropertyValue(EParameterName.LEVEL_LOG_TO_DB.getName(), new Integer(lDBType.getLevel()).toString());
        // setPropertyValue(EParameterName.LOG_TO_DB.getName(), new Boolean(lDBType.isSelected()));
        // lType.setLogToDatabase(lDBType);
        // LogToStdOutType lStdOutType = lType.getLogToStdOut();
        // setPropertyValue(EParameterName.LEVEL_LOG_TO_STDOUT.getName(), new
        // Integer(lStdOutType.getLevel()).toString());
        // setPropertyValue(EParameterName.LOG_TO_STDOUT.getName(), new Boolean(lStdOutType.isSelected()));
        // lType.setLogToStdOut(lStdOutType);
        // process.setLogs(lType);
    }

    private List<String> uploadedNodeNames = null;

    private void loadNodes(ProcessType process, Hashtable<String, Node> nodesHashtable) throws PersistenceException {
        EList nodeList;
        NodeType nType;
        nodeList = process.getNode();
        Node nc;

        EList listParamType;
        uploadedNodeNames = new ArrayList<String>();
        for (int i = 0; i < nodeList.size(); i++) {
            nType = (NodeType) nodeList.get(i);
            listParamType = nType.getElementParameter();
            IComponent component = ComponentsFactoryProvider.getInstance().get(nType.getComponentName());
            if (component == null) {
                uploadedNodeNames.add(nType.getComponentName());
                continue;
            }
            nc = new Node(component, this);
            nc.setLocation(new Point(nType.getPosX(), nType.getPosY()));
            Point offset = new Point(nType.getOffsetLabelX(), nType.getOffsetLabelY());
            nc.getNodeLabel().setOffset(offset);

            loadElementParameters(nc, listParamType);

            nc.setData(nType.getBinaryData(), nType.getStringData());

            loadSchema(nc, nType);

            addNodeContainer(new NodeContainer(nc));
            nodesHashtable.put(nc.getUniqueName(), nc);
        }

        if (!uploadedNodeNames.isEmpty()) {
            throw new PersistenceException(Messages.getString("Process.componentsUnloaded")); //$NON-NLS-1$
        }
    }

    /**
     * Checks if there are unloaded nodes.If there are some nodes unloaded, throws PersistenceException.
     * 
     * @throws PersistenceException PersistenceException
     */
    public void checkLoadNodes() throws PersistenceException {
        if (uploadedNodeNames == null || uploadedNodeNames.isEmpty()) {
            return;
        }
        StringBuilder sb = new StringBuilder();
        if (uploadedNodeNames.size() == 1) {
            sb.append(Messages.getString("Process.component.notloaded")); //$NON-NLS-1$
        } else {
            sb.append(Messages.getString("Process.components.notloaded")); //$NON-NLS-1$
        }
        PersistenceException ex = new PersistenceException(sb.toString());
        throw ex;
    }

    private void loadSchema(Node nc, NodeType nType) {
        MetadataEmfFactory factory = new MetadataEmfFactory();
        MetadataType mType;
        EList listMetaType;

        List<IMetadataTable> listMetaData;
        listMetaType = nType.getMetadata();
        IMetadataTable metadataTable;
        listMetaData = new ArrayList<IMetadataTable>();
        // boolean metadataInitialiazed = checkNodeSchemaFromRepository(nc, metadataTable);
        // if (!metadataInitialiazed) {
        for (int j = 0; j < listMetaType.size(); j++) {
            mType = (MetadataType) listMetaType.get(j);
            factory.setMetadataType(mType);
            metadataTable = factory.getMetadataTable();
            listMetaData.add(metadataTable);
            if (nc.getConnectorFromType(EConnectionType.FLOW_MAIN).isBuiltIn()) {
                addUniqueConnectionName(metadataTable.getTableName());
            }
        }
        // }
        nc.setMetadataList(listMetaData);
    }

    /**
     * 
     * DOC nrousseau Comment method "checkNodeSchemaFromRepository".
     * 
     * @param nc
     * @param metadataTable
     * @return true if the data have been modified
     */
    private boolean checkNodeSchemaFromRepository(final Node node) {
        boolean modified = false;

        final IMetadataTable metadataTable = node.getMetadataTable(node.getUniqueName());

        final String uniqueName = node.getUniqueName();

        // check the metadata from the repository to see if it's up to date.
        String schemaType = (String) node.getPropertyValue(EParameterName.SCHEMA_TYPE.getName());
        if (schemaType != null) {
            if (schemaType.equals(EmfComponent.REPOSITORY)) {
                String metaRepositoryName = (String) node.getPropertyValue(EParameterName.REPOSITORY_SCHEMA_TYPE
                        .getName());
                IMetadataTable repositoryMetadata = getMetadataFromRepository(metaRepositoryName);
                if (repositoryMetadata != null) {
                    repositoryMetadata = repositoryMetadata.clone();
                    final IMetadataTable copyOfrepositoryMetadata = repositoryMetadata;
                    copyOfrepositoryMetadata.setTableName(uniqueName);

                    if (!copyOfrepositoryMetadata.sameMetadataAs(metadataTable)) {

                        final Shell shell = PlatformUI.getWorkbench().getDisplay().getActiveShell();
                        shell.getDisplay().asyncExec(new Runnable() {

                            public void run() {
                                MessageBox mBox = new MessageBox(shell, SWT.YES | SWT.NO | SWT.ICON_QUESTION);
                                String message = Messages.getString("Process.IfToUpgradeMetadata", uniqueName); //$NON-NLS-1$
                                mBox.setText(Messages.getString("Process.metadataModificationDetected")); //$NON-NLS-1$
                                mBox.setMessage(message);
                                int value = mBox.open();
                                if (value == SWT.YES) {
                                    node.getMetadataList().remove(metadataTable);
                                    node.getMetadataList().add(copyOfrepositoryMetadata);
                                } else {
                                    node.setPropertyValue(EParameterName.SCHEMA_TYPE.getName(), EmfComponent.BUILTIN);
                                }
                                refreshPropertyView();
                            }
                        });

                        modified = true;
                    }
                } else {
                    final Shell shell = PlatformUI.getWorkbench().getDisplay().getActiveShell();
                    shell.getDisplay().asyncExec(new Runnable() {

                        public void run() {
                            MessageBox mBox = new MessageBox(shell);
                            String message = Messages.getString("Process.schemaChangeToBuilt-in", node.getUniqueName()); //$NON-NLS-1$
                            mBox.setMessage(message);
                            mBox.open();
                            node.setPropertyValue(EParameterName.SCHEMA_TYPE.getName(), EmfComponent.BUILTIN);
                            refreshPropertyView();
                        }
                    });
                    // if the repository connection doesn't exists then set to built-in
                    modified = true;
                }
            }
        }
        return modified;
    }

    private void refreshPropertyView() {
        IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
        IViewPart view = page.findView("org.eclipse.ui.views.PropertySheet"); //$NON-NLS-1$
        PropertySheet sheet = (PropertySheet) view;
        TabbedPropertySheetPage tabbedPropertySheetPage = (TabbedPropertySheetPage) sheet.getCurrentPage();
        tabbedPropertySheetPage.refresh();
    }

    /**
     * 
     * DOC nrousseau Comment method "checkNodePropertiesFromRepository".
     * 
     * @param node
     * @return true if the data have been modified
     */
    private boolean checkNodePropertiesFromRepository(final Node node) {
        boolean modified = false;

        String propertyType = (String) node.getPropertyValue(EParameterName.PROPERTY_TYPE.getName());
        if (propertyType != null) {
            if (propertyType.equals(EmfComponent.REPOSITORY)) {
                IProxyRepositoryFactory factory = DesignerPlugin.getDefault().getProxyRepositoryFactory();
                List<ConnectionItem> metadataConnectionsItem = null;
                try {
                    metadataConnectionsItem = factory.getMetadataConnectionsItem();
                } catch (PersistenceException e) {
                    throw new RuntimeException(e);
                }
                org.talend.core.model.metadata.builder.connection.Connection tmpRepositoryConnection = null;
                if (metadataConnectionsItem != null) {
                    for (ConnectionItem connectionItem : metadataConnectionsItem) {
                        String value = connectionItem.getProperty().getId() + ""; //$NON-NLS-1$
                        if (value.equals((String) node.getPropertyValue(EParameterName.REPOSITORY_PROPERTY_TYPE
                                .getName()))) {
                            tmpRepositoryConnection = (org.talend.core.model.metadata.builder.connection.Connection) connectionItem
                                    .getConnection();
                        }
                    }
                }
                final org.talend.core.model.metadata.builder.connection.Connection repositoryConnection = tmpRepositoryConnection;

                if (repositoryConnection != null) {
                    boolean sameValues = true;
                    // if the repository connection exists then test the values
                    for (IElementParameter param : node.getElementParameters()) {
                        String repositoryValue = param.getRepositoryValue();
                        if (param.isShow(node.getElementParameters()) && (repositoryValue != null)) {
                            Object objectValue = (Object) RepositoryToComponentProperty.getValue(repositoryConnection,
                                    repositoryValue);

                            if (objectValue != null) {
                                if (param.getField().equals(EParameterFieldType.CLOSED_LIST)
                                        && param.getRepositoryValue().equals("TYPE")) { //$NON-NLS-1$
                                    boolean found = false;
                                    String[] list = param.getListRepositoryItems();
                                    for (int i = 0; (i < list.length) && (!found); i++) {
                                        if (objectValue.equals(list[i])) {
                                            found = true;
                                        }
                                    }
                                    if (!found) {
                                        sameValues = false;
                                    }
                                } else {
                                    // check the value
                                    if (!param.getValue().equals(objectValue)) {
                                        sameValues = false;
                                    }
                                }
                            }
                        }
                    }
                    if (!sameValues) {
                        final Shell shell = PlatformUI.getWorkbench().getDisplay().getActiveShell();
                        shell.getDisplay().asyncExec(new Runnable() {

                            public void run() {
                                String message = Messages
                                        .getString("Process.IfToUpgradeProperty", node.getUniqueName()); //$NON-NLS-1$
                                MessageBox mBox = new MessageBox(shell, SWT.YES | SWT.NO | SWT.ICON_QUESTION);
                                mBox.setText(Messages.getString("Process.propertyModificationDetected")); //$NON-NLS-1$
                                mBox.setMessage(message);
                                int value = mBox.open();
                                if (value == SWT.YES) {
                                    // upgrade from repository
                                    for (IElementParameter param : node.getElementParameters()) {
                                        String repositoryValue = param.getRepositoryValue();
                                        if (param.isShow(node.getElementParameters()) && (repositoryValue != null)
                                                && (!param.getName().equals(EParameterName.PROPERTY_TYPE.getName()))) {
                                            Object objectValue = (Object) RepositoryToComponentProperty.getValue(
                                                    repositoryConnection, repositoryValue);
                                            if (objectValue != null) {
                                                if (param.getField().equals(EParameterFieldType.CLOSED_LIST)
                                                        && param.getRepositoryValue().equals("TYPE")) { //$NON-NLS-1$
                                                    boolean found = false;
                                                    String[] list = param.getListRepositoryItems();
                                                    for (int i = 0; (i < list.length) && (!found); i++) {
                                                        if (objectValue.equals(list[i])) {
                                                            found = true;
                                                            node.setPropertyValue(param.getName(), param
                                                                    .getListItemsValue()[i]);
                                                        }
                                                    }
                                                } else {
                                                    node.setPropertyValue(param.getName(), objectValue);
                                                }
                                                param.setRepositoryValueUsed(true);
                                            }
                                        }
                                    }
                                } else {
                                    // don't upgrade so set to builtin
                                    node.setPropertyValue(EParameterName.PROPERTY_TYPE.getName(), EmfComponent.BUILTIN);
                                    for (IElementParameter param : node.getElementParameters()) {
                                        String repositoryValue = param.getRepositoryValue();
                                        if (param.isShow(node.getElementParameters()) && (repositoryValue != null)) {
                                            param.setRepositoryValueUsed(false);
                                        }
                                    }
                                }
                                refreshPropertyView();
                            }
                        });

                        modified = true;
                    } else {
                        for (IElementParameter param : node.getElementParameters()) {
                            String repositoryValue = param.getRepositoryValue();
                            if (param.isShow(node.getElementParameters()) && (repositoryValue != null)
                                    && (!param.getName().equals(EParameterName.PROPERTY_TYPE.getName()))) {
                                param.setRepositoryValueUsed(true);
                            }
                        }
                    }
                } else {
                    final Shell shell = PlatformUI.getWorkbench().getDisplay().getActiveShell();
                    shell.getDisplay().asyncExec(new Runnable() {

                        public void run() {
                            MessageBox mBox = new MessageBox(shell);
                            String message = Messages.getString(
                                    "Process.propertyChangeToBuild-in", node.getUniqueName()); //$NON-NLS-1$
                            mBox.setMessage(message);
                            mBox.open();
                            node.setPropertyValue(EParameterName.PROPERTY_TYPE.getName(), EmfComponent.BUILTIN);
                            refreshPropertyView();
                        }
                    });
                    modified = true;
                }
            }
        }
        return modified;
    }

    /**
     * 
     * DOC nrousseau Comment method "checkDifferenceWithRepository".
     * 
     * @return true if a difference has been detected
     */
    public boolean checkDifferenceWithRepository() {
        boolean modified = false;
        for (Node node : nodes) {
            if (checkNodePropertiesFromRepository(node)) {
                modified = true;
            }
            if (checkNodeSchemaFromRepository(node)) {
                modified = true;
            }
        }
        return modified;
    }

    private void loadConnections(ProcessType process, Hashtable<String, Node> nodesHashtable) {
        EList listParamType;
        EList connecList;
        ConnectionType cType;
        connecList = process.getConnection();
        Connection connec;
        Node source, target;

        for (int i = 0; i < connecList.size(); i++) {
            cType = (ConnectionType) connecList.get(i);
            source = (Node) nodesHashtable.get(cType.getSource());
            target = (Node) nodesHashtable.get(cType.getTarget());
            Integer lineStyleId = new Integer(cType.getLineStyle());
            connec = new Connection(source, target, EConnectionType.getTypeFromId(lineStyleId), cType.getMetaname(),
                    cType.getLabel());
            if ((!source.isActivate()) || (!target.isActivate())) {
                connec.setActivate(false);
            }
            listParamType = cType.getElementParameter();
            loadElementParameters(connec, listParamType);

            Point offset = new Point(cType.getOffsetLabelX(), cType.getOffsetLabelY());
            INodeConnector nodeConnectorSource = source.getConnectorFromType(connec.getLineStyle());
            nodeConnectorSource.setCurLinkNbOutput(nodeConnectorSource.getCurLinkNbOutput() + 1);
            INodeConnector nodeConnectorTarget = target.getConnectorFromType(connec.getLineStyle());
            nodeConnectorTarget.setCurLinkNbInput(nodeConnectorTarget.getCurLinkNbInput() + 1);
            connec.getConnectionLabel().setOffset(offset); //$NON-NLS-1$
        }
    }

    private void loadContexts(ProcessType process) {
        /**
         * Load the contexts informations
         */
        String defaultContextToLoad;
        defaultContextToLoad = process.getDefaultContext();

        List<IContext> listContext = new ArrayList<IContext>();
        EList contextTypeList = process.getContext();
        ContextType contextType;
        IContext context;

        List<IContextParameter> contextParamList;
        EList contextTypeParamList;
        ContextParameterType contextParamType;
        ContextParameter contextParam;

        for (int i = 0; i < contextTypeList.size(); i++) {
            contextType = (ContextType) contextTypeList.get(i);
            context = new Context(contextType.getName());
            context.setConfirmationNeeded(contextType.isConfirmationNeeded());
            contextParamList = new ArrayList<IContextParameter>();
            contextTypeParamList = contextType.getContextParameter();

            for (int j = 0; j < contextTypeParamList.size(); j++) {
                contextParamType = (ContextParameterType) contextTypeParamList.get(j);
                contextParam = new ContextParameter();
                contextParam.setName(contextParamType.getName());
                contextParam.setPrompt(contextParamType.getPrompt());
                contextParam.setType(EMetadataType.getTypeByName(contextParamType.getType()));
                contextParam.setValue(contextParamType.getValue());
                contextParam.setPromptNeeded(contextParamType.isPromptNeeded());

                contextParamList.add(contextParam);
            }
            context.setContextParameterList(contextParamList);

            if (context.getName().equals(defaultContextToLoad)) {
                contextManager.setDefaultContext(context);
            }
            listContext.add(context);
        }
        contextManager.setListContext(listContext);
    }

    public boolean isReadOnly() {
        return readOnly;
    }

    public boolean checkReadOnly() {
        IProxyRepositoryFactory repFactory = DesignerPlugin.getDefault().getProxyRepositoryFactory();
        boolean readOnlyLocal = !repFactory.isEditableAndLockIfPossible(property.getItem());
        this.setReadOnly(readOnlyLocal);
        return readOnlyLocal;
    }

    @SuppressWarnings("unchecked")//$NON-NLS-1$
    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;

        for (Node node : nodes) {
            node.setReadOnly(readOnly);
            for (Connection connec : (List<Connection>) node.getOutgoingConnections()) {
                connec.setReadOnly(readOnly);
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.core.ui.editor.Element#getElementName()
     */
    @Override
    public String getElementName() {
        return name;
    }

    public String getName() {
        return this.getProperty().getLabel();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IRepositoryProcess#getAuthor()
     */
    public User getAuthor() {
        return getProperty().getAuthor();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IRepositoryProcess#getId()
     */
    public String getId() {
        return getProperty().getId();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IRepositoryProcess#getLabel()
     */
    public String getLabel() {
        return getProperty().getLabel();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IRepositoryProcess#getStatus()
     */
    public String getStatusCode() {
        return getProperty().getStatusCode();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IRepositoryProcess#getVersion()
     */
    public String getVersion() {
        return getProperty().getVersion();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IRepositoryProcess#setAuthor(org.talend.core.model.temp.User)
     */
    public void setAuthor(User author) {
        getProperty().setAuthor(author);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IRepositoryProcess#setId(int)
     */
    public void setId(String id) {
        getProperty().setId(id);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IRepositoryProcess#setLabel(java.lang.String)
     */
    public void setLabel(String label) {
        getProperty().setLabel(label);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IRepositoryProcess#setStatus(org.talend.core.model.process.EProcessStatus)
     */
    public void setStatusCode(String statusCode) {
        setPropertyValue(EParameterName.STATUS.getName(), statusCode);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IRepositoryProcess#setVersion(int)
     */
    public void setVersion(String version) {
        getProperty().setVersion(version);
    }

    // private InputStream content;
    private byte[] content;

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.temp.IXmlSerializable#getXmlStream()
     */
    public InputStream getXmlStream() {
        return new ByteArrayInputStream(content);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.temp.IXmlSerializable#setXmlStream(java.io.InputStream)
     */
    public void setXmlStream(InputStream xmlStream) {
        ByteArrayOutputStream st = new ByteArrayOutputStream();

        int byteLu;
        try {
            while ((byteLu = xmlStream.read()) != -1) {
                st.write(byteLu);
            }
        } catch (IOException e) {
            // TODO SML Auto-generated catch block
            e.printStackTrace();
        } finally {
            try {
                xmlStream.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        this.content = st.toByteArray();
    }

    public boolean isActivate() {
        return activate;
    }

    public void setActivate(boolean activate) {
        this.activate = activate;
    }

    /**
     * Check if the given name will be unique in the process. If another link already exists with that name, false will
     * be returned.
     * 
     * @param uniqueName
     * @param checkEsists
     * @return true if the name is unique
     */
    public boolean checkValidConnectionName(String connectionName, boolean checkExists) {
        if (checkExists && uniqueConnectionNameList.contains(connectionName)) {
            return false;
        }
        Perl5Matcher matcher = new Perl5Matcher();
        Perl5Compiler compiler = new Perl5Compiler();
        Pattern pattern;

        switch (processLanguage) {
        case PERL:
            try {
                pattern = compiler.compile("^[A-Za-z_][A-Za-z0-9_]*$"); //$NON-NLS-1$
                if (!matcher.matches(connectionName, pattern)) {
                    return false;
                }
            } catch (MalformedPatternException e) {
                throw new RuntimeException(e);
            }
        default:
        }
        return true;
    }

    /**
     * Check if the given name will be unique in the process. If another link already exists with that name, false will
     * be returned.
     * 
     * @param uniqueName
     * @return true if the name is unique
     */
    public boolean checkValidConnectionName(String connectionName) {
        return checkValidConnectionName(connectionName, true);
    }

    /**
     * Manage to find a unique name with the given name.
     * 
     * @param titleName
     */
    public String generateUniqueConnectionName(String baseName) {
        if (baseName == null) {
            throw new IllegalArgumentException("baseName can't be null"); //$NON-NLS-1$
        }
        String uniqueName = baseName + 1;

        int counter = 1;
        boolean exists = true;
        while (exists) {
            exists = !checkValidConnectionName(uniqueName);
            if (!exists) {
                break;
            }
            uniqueName = baseName + counter++;
        }
        return uniqueName;
    }

    public void addUniqueConnectionName(String uniqueConnectionName) {
        if (uniqueConnectionName != null) {
            if (checkValidConnectionName(uniqueConnectionName)) {
                uniqueConnectionNameList.add(uniqueConnectionName);
            } else {
                throw new IllegalArgumentException("The name of the connection is not valid: " + uniqueConnectionName); //$NON-NLS-1$
            }
        }
    }

    public void removeUniqueConnectionName(String uniqueConnectionName) {
        if (uniqueConnectionName != null) {
            uniqueConnectionNameList.remove(uniqueConnectionName);
        }
    }

    public String generateUniqueNodeName(INode node) {
        String baseName = node.getComponent().getName();
        if (baseName == null) {
            throw new IllegalArgumentException("Component name can't be null"); //$NON-NLS-1$
        }
        String uniqueName = baseName + "_" + 1; //$NON-NLS-1$

        int counter = 1;
        boolean exists = true;
        while (exists) {
            exists = uniqueNodeNameList.contains(uniqueName);
            if (!exists) {
                break;
            }
            uniqueName = baseName + "_" + counter++; //$NON-NLS-1$
        }
        return uniqueName;
    }

    /**
     * This function will take a unique name and update the list with the given name. This function should be private
     * only and should be called only when the xml file is loaded.
     * 
     * @param uniqueName
     */
    public void addUniqueNodeName(final String uniqueName) {
        if (!uniqueNodeNameList.contains(uniqueName)) {
            uniqueNodeNameList.add(uniqueName);
        }
    }

    public void removeUniqueNodeName(final String uniqueName) {
        if (!uniqueName.equals("")) { //$NON-NLS-1$
            uniqueNodeNameList.remove(uniqueName);
        }
    }

    @SuppressWarnings("unchecked")//$NON-NLS-1$
    private void setActivate(Node node, boolean active, Node activateNode) {
        Node mainSubProcess = node.getSubProcessStartNode(false);

        // if the selected node is the start node, then everything will be
        // desacticated
        if (activateNode.isStart()) {
            for (Connection connec : (List<Connection>) node.getIncomingConnections()) {
                if (connec.getSource().isActivate() != active) {
                    if (connec.getLineStyle().equals(EConnectionType.FLOW_REF)) {
                        if (connec.getSource().getSubProcessStartNode(false).isActivate() != active) {
                            setActivate(connec.getSource().getSubProcessStartNode(false), active, activateNode);
                        }
                    }
                }
            }
            node.setPropertyValue(EParameterName.ACTIVATE.getName(), new Boolean(active));
            for (Connection connec : (List<Connection>) node.getOutgoingConnections()) {
                if (connec.getTarget().isActivate() != active) {
                    setActivate(connec.getTarget(), active, activateNode);
                }
            }
        } else {
            if (node.getSubProcessStartNode(false).equals(mainSubProcess)) {
                node.setPropertyValue(EParameterName.ACTIVATE.getName(), new Boolean(active));
                for (Connection connec : (List<Connection>) node.getIncomingConnections()) {
                    if (connec.getLineStyle().equals(EConnectionType.FLOW_MAIN)
                            || connec.getLineStyle().equals(EConnectionType.FLOW_REF)) {
                        if (connec.getSource().isActivate() != active) {
                            setActivate(connec.getSource(), active, activateNode);
                        }
                    }
                }
                for (Connection connec : (List<Connection>) node.getOutgoingConnections()) {
                    if (connec.getLineStyle().equals(EConnectionType.FLOW_MAIN)
                            || connec.getLineStyle().equals(EConnectionType.FLOW_REF)) {
                        if (connec.getTarget().isActivate() != active) {
                            setActivate(connec.getTarget(), active, activateNode);
                        }
                    }
                }
            }
            node.setPropertyValue(EParameterName.ACTIVATE.getName(), new Boolean(active));
        }
    }

    public void setActivate(Node node, boolean active) {
        // desactive first the process to avoid to check the process during the
        // activation / desactivation
        setActivate(false);
        setActivate(node, active, node);
        // now that everything is set, reactivate the process
        setActivate(true);
    }

    /**
     * Check all active nodes and set start if necessary.
     */
    public void checkStartNodes() {
        // check

        for (Node node : nodes) {
            if ((Boolean) node.getPropertyValue(EParameterName.STARTABLE.getName())) {
                if (node.isActivate()) {
                    node.setStart(false);
                    boolean isActivatedConnection = false;
                    for (int j = 0; j < node.getIncomingConnections().size() && !isActivatedConnection; j++) {
                        if (((Connection) node.getIncomingConnections().get(j)).isActivate()) {
                            isActivatedConnection = true;
                        }
                    }
                    if (!isActivatedConnection) {
                        if (!isThereRefLink(node)) {
                            node.setStart(true);
                        }
                    } else {
                        if (node.getIncomingConnections().size() == 0) {
                            if (!isThereRefLink(node)) {
                                node.setStart(true);
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * This function check if in this subprocess there should be a start or not depends on the ref links. If in this
     * subprocess there is only one main flow and one ref then this function will return true. If there is several flow
     * in the output of one component in this subprocess,it'll return false.
     * 
     * @param node
     * @return
     */
    private boolean isThereRefLink(final INode node) {
        boolean refLink = false;

        for (int i = 0; i < node.getOutgoingConnections().size() && !refLink; i++) {
            IConnection connec = node.getOutgoingConnections().get(i);
            if (connec.isActivate()) {
                if (connec.getLineStyle().equals(EConnectionType.FLOW_REF)) {
                    refLink = true;
                } else {
                    if (connec.getLineStyle().equals(EConnectionType.FLOW_MAIN)
                            || connec.getLineStyle().equals(EConnectionType.ITERATE)
                            || connec.getLineStyle().equals(EConnectionType.RUN_BEFORE)
                            || connec.getLineStyle().equals(EConnectionType.RUN_AFTER)) {
                        refLink = isThereRefLink(connec.getTarget());
                    }
                }
            }
        }
        return refLink;
    }

    /**
     * DOC nrousseau Comment method "checkProcess".
     * 
     * @param propagate
     */
    public void checkProcess() {
        if (isActivate()) {
            checkProblems();
        }
    }

    private void checkProblems() {
        Problems.clearAll();

        for (Node node : nodes) {
            if (node.isActivate()) {
                node.checkNode();
            }
        }
        Problems.refreshView();
    }

    /**
     * 
     * DOC check the problems of node.compare with the checkProblems(),this method can't refresh problems view.
     */
    public void checkNodeProblems() {
        if (isActivate()) {
            Problems.clearAll();

            for (Node node : nodes) {
                if (node.isActivate()) {
                    node.checkNode();
                }
            }
        }
    }

    public String toString() {
        return "Process:" + getLabel(); //$NON-NLS-1$
    }

    public ERepositoryObjectType getType() {
        return ERepositoryObjectType.PROCESS;
    }

    public IContextManager getContextManager() {
        return contextManager;
    }

    // PTODO MHE remove
    public Date getCreationDate() {
        return null;
    }

    public String getDescription() {
        return null;
    }

    public Date getModificationDate() {
        return null;
    }

    public String getPurpose() {
        return null;
    }

    public void setCreationDate(Date value) {
    }

    public void setDescription(String value) {
    }

    public void setModificationDate(Date value) {
    }

    public void setPurpose(String value) {
    }

    @Override
    public void setPropertyValue(String propertyName, Object value) {
        super.setPropertyValue(propertyName, value);
    }

    public Property getProperty() {
        return property;
    }

    public void setProperty(Property property) {
        this.property = property;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.repository.IRepositoryObject#getChildren()
     */
    public List<IRepositoryObject> getChildren() {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * Return all Nodes of Component type componentName.
     * 
     * @param componentName the component name
     * @return all the activated matching nodes in the process
     */
    public List<? extends INode> getNodesOfType(String componentName) {
        List<Node> matchingNodes = new ArrayList<Node>();
        for (Node node : nodes) {
            if ((node.isActivate()) && (node.getComponent().getName() != null)
                    && (node.getComponent().getName().compareTo(componentName)) == 0) {
                matchingNodes.add(node);
            }
        }
        return matchingNodes;
    }
}
