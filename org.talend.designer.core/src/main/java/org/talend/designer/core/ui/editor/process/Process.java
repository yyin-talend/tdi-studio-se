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

package org.talend.designer.core.ui.editor.process;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.oro.text.regex.MalformedPatternException;
import org.apache.oro.text.regex.Pattern;
import org.apache.oro.text.regex.Perl5Compiler;
import org.apache.oro.text.regex.Perl5Matcher;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.SnapToGeometry;
import org.eclipse.gef.SnapToGrid;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;
import org.talend.commons.exception.PersistenceException;
import org.talend.core.CorePlugin;
import org.talend.core.model.components.IComponent;
import org.talend.core.model.context.JobContextManager;
import org.talend.core.model.general.ModuleNeeded;
import org.talend.core.model.general.Project;
import org.talend.core.model.metadata.IMetadataColumn;
import org.talend.core.model.metadata.IMetadataTable;
import org.talend.core.model.metadata.MetadataTool;
import org.talend.core.model.metadata.designerproperties.RepositoryToComponentProperty;
import org.talend.core.model.process.EComponentCategory;
import org.talend.core.model.process.EConnectionType;
import org.talend.core.model.process.EParameterFieldType;
import org.talend.core.model.process.Element;
import org.talend.core.model.process.IConnection;
import org.talend.core.model.process.IConnectionCategory;
import org.talend.core.model.process.IContextManager;
import org.talend.core.model.process.IElementParameter;
import org.talend.core.model.process.INode;
import org.talend.core.model.process.INodeConnector;
import org.talend.core.model.process.IProcess;
import org.talend.core.model.process.UniqueNodeNameGenerator;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.core.model.properties.ProcessItem;
import org.talend.core.model.properties.Property;
import org.talend.core.model.properties.User;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.IRepositoryObject;
import org.talend.designer.core.DesignerPlugin;
import org.talend.designer.core.i18n.Messages;
import org.talend.designer.core.model.components.EParameterName;
import org.talend.designer.core.model.components.ElementParameter;
import org.talend.designer.core.model.components.EmfComponent;
import org.talend.designer.core.model.metadata.MetadataEmfFactory;
import org.talend.designer.core.model.process.DataProcess;
import org.talend.designer.core.model.process.statsandlogs.StatsAndLogsManager;
import org.talend.designer.core.model.utils.emf.talendfile.ConnectionType;
import org.talend.designer.core.model.utils.emf.talendfile.DocumentRoot;
import org.talend.designer.core.model.utils.emf.talendfile.ElementParameterType;
import org.talend.designer.core.model.utils.emf.talendfile.ElementValueType;
import org.talend.designer.core.model.utils.emf.talendfile.JobType;
import org.talend.designer.core.model.utils.emf.talendfile.MetadataType;
import org.talend.designer.core.model.utils.emf.talendfile.NodeType;
import org.talend.designer.core.model.utils.emf.talendfile.NoteType;
import org.talend.designer.core.model.utils.emf.talendfile.ParametersType;
import org.talend.designer.core.model.utils.emf.talendfile.ProcessType;
import org.talend.designer.core.model.utils.emf.talendfile.RequiredType;
import org.talend.designer.core.model.utils.emf.talendfile.TalendFileFactory;
import org.talend.designer.core.model.utils.emf.talendfile.util.TalendFileResourceImpl;
import org.talend.designer.core.ui.MultiPageTalendEditor;
import org.talend.designer.core.ui.editor.connections.Connection;
import org.talend.designer.core.ui.editor.nodecontainer.NodeContainer;
import org.talend.designer.core.ui.editor.nodes.Node;
import org.talend.designer.core.ui.editor.nodes.Node.Data;
import org.talend.designer.core.ui.editor.notes.Note;
import org.talend.designer.core.ui.editor.properties.DynamicTabbedPropertySection;
import org.talend.designer.core.ui.preferences.TalendDesignerPrefConstants;
import org.talend.designer.core.ui.views.problems.Problems;
import org.talend.designer.runprocess.IProcessor;
import org.talend.designer.runprocess.ProcessorUtilities;
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

    // properties
    public static final String NODES = "nodes"; //$NON-NLS-1$

    public static final String NOTES = "notes"; //$NON-NLS-1$

    public static final String DEFAULT_CONNECTION_NAME = "row"; //$NON-NLS-1$

    protected List<Node> nodes = new ArrayList<Node>();

    protected List<Element> elem = new ArrayList<Element>();

    protected List<Note> notes = new ArrayList<Note>();

    private final String name = new String(Messages.getString("Process.Job")); //$NON-NLS-1$

    private boolean activate = true;

    // list where is stored each unique name for the connections
    private final List<String> uniqueConnectionNameList = new ArrayList<String>();

    // list where is stored each unique name for the nodes
    private final List<String> uniqueNodeNameList = new ArrayList<String>();

    private boolean readOnly;

    private GraphicalViewer viewer = null;

    private IContextManager contextManager;

    public static final int BREAKPOINT_STATUS = 1;

    public static final int ERROR_STATUS = 2;

    public static final int WARNING_STATUS = 4;

    private Property property;

    private boolean initDone = false;

    private IProcessor processor;

    public Process() {
        contextManager = new JobContextManager();
        createProcessParameters();
    }

    public Process(Property property) {
        this();
        this.property = property;
        init();
    }

    public void updateProperties() {
        setId(property.getId());
        setLabel(property.getLabel());
        setVersion(property.getVersion());
        setAuthor(property.getAuthor());
        setStatusCode(property.getStatusCode());
        setDescription(property.getDescription());
        setPurpose(property.getPurpose());
        if (getStatusCode() == null) {
            setStatusCode(""); //$NON-NLS-1$
        }
    }

    private void init() {
        if (!initDone) {
            updateProperties();
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
        createMainParameters();
        createStatsAndLogsParameters();
    }

    /**
     * create parameters for tabbed page 'Stats & Logs'.
     */
    private void createStatsAndLogsParameters() {
        StatsAndLogsManager.createStatsAndLogsParameters(this);
    }

    /**
     * Creates parameters for tabbed page 'Main'.
     */
    private void createMainParameters() {
        ElementParameter param;

        param = new ElementParameter(this);
        param.setName(EParameterName.COMP_DEFAULT_FILE_DIR.getName());
        param.setCategory(EComponentCategory.MAIN);
        param.setField(EParameterFieldType.DIRECTORY);
        param.setDisplayName(EParameterName.COMP_DEFAULT_FILE_DIR.getDisplayName());
        param.setNumRow(99);
        param.setShow(false);
        param.setValue(DesignerPlugin.getDefault().getPluginPreferences().getString(
                TalendDesignerPrefConstants.COMP_DEFAULT_FILE_DIR));
        param.setReadOnly(true);
        addElementParameter(param);

        param = new ElementParameter(this);
        param.setName(EParameterName.AUTHOR.getName());
        param.setCategory(EComponentCategory.TECHNICAL);
        param.setField(EParameterFieldType.TEXT);
        param.setDisplayName(EParameterName.AUTHOR.getDisplayName());
        param.setShow(false);
        param.setReadOnly(true);
        addElementParameter(param);

        param = new ElementParameter(this);
        param.setName(EParameterName.STATUS.getName());
        param.setCategory(EComponentCategory.TECHNICAL);
        param.setField(EParameterFieldType.TEXT);
        param.setDisplayName(EParameterName.STATUS.getDisplayName());
        param.setShow(false);
        param.setReadOnly(true);
        addElementParameter(param);

        param = new ElementParameter(this);
        param.setName(EParameterName.NAME.getName());
        param.setCategory(EComponentCategory.TECHNICAL);
        param.setField(EParameterFieldType.TEXT);
        param.setDisplayName(EParameterName.NAME.getDisplayName());
        param.setShow(false);
        param.setReadOnly(true);
        addElementParameter(param);

        param = new ElementParameter(this);
        param.setName(EParameterName.VERSION.getName());
        param.setCategory(EComponentCategory.TECHNICAL);
        param.setField(EParameterFieldType.TEXT);
        param.setDisplayName(EParameterName.VERSION.getDisplayName());
        param.setShow(false);
        param.setReadOnly(true);
        addElementParameter(param);

        param = new ElementParameter(this);
        param.setName(EParameterName.PURPOSE.getName());
        param.setCategory(EComponentCategory.TECHNICAL);
        param.setField(EParameterFieldType.TEXT);
        param.setDisplayName(EParameterName.PURPOSE.getDisplayName());
        param.setShow(false);
        param.setReadOnly(true);
        addElementParameter(param);

        param = new ElementParameter(this);
        param.setName(EParameterName.DESCRIPTION.getName());
        param.setCategory(EComponentCategory.TECHNICAL);
        param.setField(EParameterFieldType.TEXT);
        param.setDisplayName(EParameterName.DESCRIPTION.getDisplayName());
        param.setShow(false);
        param.setReadOnly(true);
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
        DataProcess.buildFromGraphicalProcess(this, nodes);
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

    @SuppressWarnings("unchecked")//$NON-NLS-1$
    private void saveElementParameters(TalendFileFactory fileFact, List<? extends IElementParameter> paramList,
            EList listParamType, ProcessType process) {
        IElementParameter param;
        for (int j = 0; j < paramList.size(); j++) {
            param = paramList.get(j);
            saveElementParameter(param, process, fileFact, paramList, listParamType);
            for (String key : param.getChildParameters().keySet()) {
                saveElementParameter(param.getChildParameters().get(key), process, fileFact, paramList, listParamType);
            }
            // accept only one level of child parameters.
        }
    }

    private void saveElementParameter(IElementParameter param, ProcessType process, TalendFileFactory fileFact,
            List<? extends IElementParameter> paramList, EList listParamType) {
        ElementParameterType pType;

        if (param.getField().equals(EParameterFieldType.SCHEMA_TYPE)) {
            return;
        }
        if (param.getName().equals(EParameterName.PROCESS_TYPE_PROCESS.getName())) {
            // if this parameter is defined in a component, then we add a
            // dependancy to this job.
            String jobName;
            jobName = ((String) param.getValue());
            // if there is no job selected in the tRunJob, no need to add any
            // relationship in "required".
            if (jobName.length() != 0) {
                RequiredType rType = process.getRequired();
                if (rType == null) {
                    rType = fileFact.createRequiredType();
                    process.setRequired(rType);
                }
                JobType jType = fileFact.createJobType();
                jType.setName(jobName);
                String contextName = ""; //$NON-NLS-1$
                boolean found = false;
                for (int i = 0; i < paramList.size() && !found; i++) {
                    IElementParameter contextParam = paramList.get(i);
                    if (contextParam.getName().equals(EParameterName.PROCESS_TYPE_CONTEXT.getName())) {
                        contextName = ((String) contextParam.getValue());
                        found = true;
                    }
                }
                jType.setContext(contextName);
                rType.getJob().add(jType);
            }
        }
        if ((!param.isReadOnly()) || param.getName().equals(EParameterName.UNIQUE_NAME.getName())
                || param.getName().equals(EParameterName.VERSION.getName())) {
            pType = fileFact.createElementParameterType();
            if (param.getParentParameter() != null) {
                pType.setName(param.getParentParameter().getName() + ":" + param.getName());
            } else {
                pType.setName(param.getName());
            }
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
                                    strValue = ((Boolean) o).toString();
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
                        pType.setValue(((Boolean) value).toString());
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

    @SuppressWarnings("unchecked")//$NON-NLS-1$
    private void loadElementParameters(Element elemParam, EList listParamType) {
        ElementParameterType pType;

        for (int j = 0; j < listParamType.size(); j++) {
            pType = (ElementParameterType) listParamType.get(j);

            if (pType != null) {
                IElementParameter param = elemParam.getElementParameter(pType.getName());
                if (param != null) {
                    if (param.isReadOnly()
                            && !(param.getName().equals(EParameterName.UNIQUE_NAME.getName()) || param.getName().equals(
                                    EParameterName.VERSION.getName()))) {
                        continue; // if the parameter is read only, don't load
                        // it (this will prevent to overwrite the
                        // value)
                    }
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
                    } else if (!param.getField().equals(EParameterFieldType.SCHEMA_TYPE)) {
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

        ParametersType params = fileFact.createParametersType();
        process.setParameters(params);

        saveElementParameters(fileFact, this.getElementParameters(), process.getParameters().getElementParameter(), process);

        EList nList = process.getNode();
        EList cList = process.getConnection();
        MetadataEmfFactory factory = new MetadataEmfFactory();

        // save according to elem order to keep zorder (children insertion) in
        // diagram
        for (Element element : elem) {
            if (element instanceof Node) {
                saveNode(fileFact, process, nList, cList, (Node) element, factory);
            } else if (element instanceof Note) {
                saveNote(fileFact, process, (Note) element);
            }
        }

        /**
         * Save the contexts informations
         */
        process.setDefaultContext(contextManager.getDefaultContext().getName());
        if (repositoryId != null) {
            process.setRepositoryContextId(repositoryId);
        }

        contextManager.saveToEmf(process.getContext());

        res.getContents().add(xmlDoc);

        HashMap options = new HashMap(2);
        options.put(XMLResource.OPTION_ENCODING, "UTF-8"); //$NON-NLS-1$
        options.put(XMLResource.OPTION_XML_VERSION, "1.1"); //$NON-NLS-1$
        res.save(options);
        return process;
    }

    private void saveNote(TalendFileFactory fileFact, ProcessType process, Note note) {
        NoteType noteType = fileFact.createNoteType();
        noteType.setPosX(note.getLocation().x);
        noteType.setPosY(note.getLocation().y);
        noteType.setSizeWidth(note.getSize().width);
        noteType.setSizeHeight(note.getSize().height);
        noteType.setOpaque(note.isOpaque());
        noteType.setText(note.getText());

        process.getNote().add(noteType);
    }

    private void saveNode(TalendFileFactory fileFact, ProcessType process, EList nList, EList cList, Node node,
            MetadataEmfFactory factory) {
        NodeType nType;
        List<Connection> connList;
        Connection connec;
        ConnectionType cType;
        List<? extends IElementParameter> paramList;
        List<IMetadataTable> listMetaData;
        EList listParamType;
        EList listMetaType;
        IMetadataTable metaData;
        nType = fileFact.createNodeType();
        nType.setComponentVersion(node.getComponent().getVersion());
        nType.setComponentName(node.getComponent().getName());
        nType.setPosX(node.getLocation().x);
        nType.setPosY(node.getLocation().y);
        nType.setOffsetLabelX(node.getNodeLabel().getOffset().x);
        nType.setOffsetLabelY(node.getNodeLabel().getOffset().y);
        if ((node.getSize().width != Node.DEFAULT_SIZE) || (node.getSize().height != Node.DEFAULT_SIZE)) {
            nType.setSizeX(node.getSize().width);
            nType.setSizeY(node.getSize().height);
        }
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
            cType.setConnectorName(connec.getConnectorName());
            cType.setOffsetLabelX(connec.getConnectionLabel().getOffset().x); //$NON-NLS-1$
            cType.setOffsetLabelY(connec.getConnectionLabel().getOffset().y); //$NON-NLS-1$
            cType.setMetaname(connec.getMetaName());
            int id = connec.getOutputId(connec.getLineStyle());
            if (id >= 0) {
                cType.setOutputId(id);
            }
            if (connec.getTarget().getComponent().useMerge()) {
                cType.setMergeOrder(connec.getInputId());
            }
            listParamType = cType.getElementParameter();
            paramList = connec.getElementParameters();
            saveElementParameters(fileFact, paramList, listParamType, process);
            cList.add(cType);
        }
        nList.add(nType);
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

        if (process.getParameters() != null) {
            loadElementParameters(this, process.getParameters().getElementParameter());
        }

        try {
            loadNodes(process, nodesHashtable);
        } catch (PersistenceException e) {
            // there are some components unloaded.
            return;
        }

        repositoryId = process.getRepositoryContextId();

        loadConnections(process, nodesHashtable);
        loadContexts(process);
        loadNotes(process);
        initExternalComponents();
        setActivate(true);
        checkStartNodes();
        // checkProcess();
    }

    private void loadNotes(ProcessType process) {
        for (Iterator iter = process.getNote().iterator(); iter.hasNext();) {
            NoteType noteType = (NoteType) iter.next();

            Note note = new Note();
            note.setLocation(new Point(noteType.getPosX(), noteType.getPosY()));
            note.setSize(new Dimension(noteType.getSizeWidth(), noteType.getSizeHeight()));
            note.setOpaque(noteType.isOpaque());
            note.setText(noteType.getText());

            addNote(note);
        }
    }

    private void initExternalComponents() {
        for (Node node : nodes) {
            if (node.isExternalNode()) {
                node.getExternalNode().initialize();
            }
        }
    }

    private List<String> unloadedNodeNames = null;

    private void loadNodes(ProcessType process, Hashtable<String, Node> nodesHashtable) throws PersistenceException {
        EList nodeList;
        NodeType nType;
        nodeList = process.getNode();
        Node nc;

        EList listParamType;
        unloadedNodeNames = new ArrayList<String>();
        for (int i = 0; i < nodeList.size(); i++) {
            nType = (NodeType) nodeList.get(i);
            listParamType = nType.getElementParameter();
            IComponent component = ComponentsFactoryProvider.getInstance().get(nType.getComponentName());
            if (component == null) {
                unloadedNodeNames.add(nType.getComponentName());
                continue;
            }
            nc = new Node(component, this);
            nc.setLocation(new Point(nType.getPosX(), nType.getPosY()));
            Point offset = new Point(nType.getOffsetLabelX(), nType.getOffsetLabelY());
            nc.getNodeLabel().setOffset(offset);
            if (nType.isSetSizeX()) {
                nc.setSize(new Dimension(nType.getSizeX(), nType.getSizeY()));
            }

            loadElementParameters(nc, listParamType);

            nc.setData(nType.getBinaryData(), nType.getStringData());

            loadSchema(nc, nType);

            addNodeContainer(new NodeContainer(nc));
            nodesHashtable.put(nc.getUniqueName(), nc);
            updateAllMappingTypes();
        }

        if (!unloadedNodeNames.isEmpty()) {
            throw new PersistenceException(Messages.getString("Process.componentsUnloaded")); //$NON-NLS-1$
        }
    }

    /**
     * to optimize.
     */
    private void updateAllMappingTypes() {
        for (INode node : this.getGraphicalNodes()) {
            for (IElementParameter param : node.getElementParameters()) {
                if (param.getField().equals(EParameterFieldType.MAPPING_TYPE)) {
                    for (IMetadataTable table : node.getMetadataList()) {
                        table.setDbms((String) param.getValue());
                    }
                }
            }
        }
    }

    /**
     * Checks if there are unloaded nodes.If there are some nodes unloaded, throws PersistenceException.
     * 
     * @throws PersistenceException PersistenceException
     */
    public void checkLoadNodes() throws PersistenceException {
        if (unloadedNodeNames == null || unloadedNodeNames.isEmpty()) {
            return;
        }
        String errorMessage = null;
        if (unloadedNodeNames.size() == 1) {
            errorMessage = Messages.getString("Process.component.notloaded", unloadedNodeNames.get(0)); //$NON-NLS-1$
        } else {
            StringBuilder curentName = new StringBuilder();
            for (String componentName : unloadedNodeNames) {
                curentName.append(componentName).append(","); //$NON-NLS-1$
            }
            curentName.deleteCharAt(curentName.length() - 1);

            errorMessage = Messages.getString("Process.components.notloaded", curentName.toString()); //$NON-NLS-1$

        }
        PersistenceException ex = new PersistenceException(errorMessage);
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

        for (int j = 0; j < listMetaType.size(); j++) {
            mType = (MetadataType) listMetaType.get(j);
            factory.setMetadataType(mType);
            metadataTable = factory.getMetadataTable();
            listMetaData.add(metadataTable);
            if (nc.getConnectorFromType(EConnectionType.FLOW_MAIN).isBuiltIn()) {
                addUniqueConnectionName(metadataTable.getTableName());
            }
            if (nc.isELTComponent()) {
                metadataTable.setAttachedConnector(EConnectionType.TABLE.getName());
            }
            MetadataTool.initilializeSchemaFromElementParameters(metadataTable, (List<IElementParameter>) nc
                    .getElementParameters());
        }
        List<IMetadataTable> oldComponentMetadataList = new ArrayList<IMetadataTable>(nc.getMetadataList());
        nc.setMetadataList(listMetaData);
        for (IMetadataTable table : oldComponentMetadataList) {
            if (nc.getMetadataFromConnector(table.getAttachedConnector()) == null) {
                // if there is any new connector, then add the table to the
                // list.
                String baseSchema = nc.getConnectorFromName(table.getAttachedConnector()).getBaseSchema();
                if (!table.getAttachedConnector().equals(baseSchema)) {
                    MetadataTool.copyTable(nc.getMetadataFromConnector(baseSchema), table);
                }
                nc.getMetadataList().add(table);
            }
        }
    }

    /**
     * 
     * DOC nrousseau Comment method "checkNodeSchemaFromRepository".
     * 
     * @param nc
     * @param metadataTable
     * @return true if the data have been modified
     */
    private boolean checkNodeSchemaFromRepository(final Node node, final List<MetadataUpdateCheckResult> resultList) {
        boolean modified = false;

        final String uniqueName = node.getUniqueName();

        // check the metadata from the repository to see if it's up to date.
        // String schemaType = (String)
        // node.getPropertyValue(EParameterName.SCHEMA_TYPE.getName());
        for (IElementParameter currentParam : node.getElementParameters()) {
            if (currentParam.getField().equals(EParameterFieldType.SCHEMA_TYPE)) {
                IElementParameter schemaParam = currentParam.getChildParameters().get(EParameterName.SCHEMA_TYPE.getName());
                if (schemaParam != null && ((ElementParameter) schemaParam).isDisplayedByDefault()) {
                    if (schemaParam.getValue().equals(EmfComponent.REPOSITORY)) {
                        String metaRepositoryName = (String) currentParam.getChildParameters().get(
                                EParameterName.REPOSITORY_SCHEMA_TYPE.getName()).getValue();
                        IMetadataTable repositoryMetadata = MetadataTool.getMetadataFromRepository(metaRepositoryName);

                        MetadataUpdateCheckResult result = new MetadataUpdateCheckResult(node);

                        if (repositoryMetadata != null) {
                            repositoryMetadata = repositoryMetadata.clone();
                            final IMetadataTable copyOfrepositoryMetadata = repositoryMetadata;
                            copyOfrepositoryMetadata.setTableName(uniqueName);
                            copyOfrepositoryMetadata.setAttachedConnector(currentParam.getContext());

                            IMetadataTable metadataTable = node.getMetadataFromConnector(currentParam.getContext());
                            if (!metadataTable.sameMetadataAs(copyOfrepositoryMetadata, IMetadataColumn.OPTIONS_NONE)) {

                                result.setResult(MetadataUpdateCheckResult.RepositoryType.schema,
                                        MetadataUpdateCheckResult.ResultType.change, copyOfrepositoryMetadata);

                                modified = true;
                            }
                        } else {

                            result.setResult(MetadataUpdateCheckResult.RepositoryType.schema,
                                    MetadataUpdateCheckResult.ResultType.delete, null);
                            // if the repository connection doesn't exists then
                            // set to built-in
                            modified = true;
                        }

                        // add the check result to resultList, hold the value.
                        if (result.getResultType() != null) {
                            resultList.add(result);
                        }
                    }
                }
            }
        }
        return modified;
    }

    private void refreshPropertyView() {

        // IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
        // IViewPart view = page.findView("org.eclipse.ui.views.PropertySheet"); //$NON-NLS-1$
        // PropertySheet sheet = (PropertySheet) view;
        // if (sheet.getCurrentPage() instanceof TabbedPropertySheetPage) {
        // TabbedPropertySheetPage tabbedPropertySheetPage = (TabbedPropertySheetPage) sheet.getCurrentPage();
        // if (tabbedPropertySheetPage.getCurrentTab() != null) {
        // tabbedPropertySheetPage.refresh();
        // }
        // }
        if (DynamicTabbedPropertySection.getLastPropertyUsed() != null) {
            // DynamicTabbedPropertySection.getLastPropertyUsed().addComponents(true)
            DynamicTabbedPropertySection.getLastPropertyUsed().refresh();
        }
    }

    /**
     * 
     * DOC nrousseau Comment method "checkNodePropertiesFromRepository".
     * 
     * @param node
     * @return true if the data have been modified
     */
    private boolean checkNodePropertiesFromRepository(final Node node, final List<MetadataUpdateCheckResult> resultList) {
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
                        if (value.equals(node.getPropertyValue(EParameterName.REPOSITORY_PROPERTY_TYPE.getName()))) {
                            tmpRepositoryConnection = connectionItem.getConnection();
                        }
                    }
                }
                final org.talend.core.model.metadata.builder.connection.Connection repositoryConnection = tmpRepositoryConnection;

                MetadataUpdateCheckResult result = new MetadataUpdateCheckResult(node);

                if (repositoryConnection != null) {
                    boolean sameValues = true;
                    // if the repository connection exists then test the values
                    for (IElementParameter param : node.getElementParameters()) {
                        String repositoryValue = param.getRepositoryValue();
                        if (param.isShow(node.getElementParameters()) && (repositoryValue != null)) {
                            Object objectValue = RepositoryToComponentProperty.getValue(repositoryConnection, repositoryValue);

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
                                    if (param.getField().equals(EParameterFieldType.TABLE)) {
                                        if ((param.getValue() instanceof List) && (objectValue instanceof List)) {
                                            List<Map<String, Object>> oldMaps = (List<Map<String, Object>>) param.getValue();
                                            List<Map<String, Object>> newMaps = (List<Map<String, Object>>) objectValue;

                                            // sameValues = oldMaps.size() == newMaps.size();
                                            for (int i = 0; i < newMaps.size() && sameValues; i++) {
                                                Map<String, Object> newmap = newMaps.get(i);
                                                Map<String, Object> oldmap = null; // oldMaps.get(i);
                                                if (i < oldMaps.size()) {
                                                    oldmap = oldMaps.get(i);
                                                }
                                                // for (int j = 0; j < oldMaps.size(); j++) {
                                                // if (oldMaps.get(j).get("SCHEMA_COLUMN").equals(
                                                // newmap.get("SCHEMA_COLUMN"))) {
                                                // oldmap = oldMaps.get(j);
                                                // }
                                                // }
                                                if (oldmap != null && sameValues) {
                                                    sameValues = newmap.get("QUERY").equals(oldmap.get("QUERY"));
                                                }
                                            }
                                        }
                                    } else if (!param.getValue().equals(objectValue)) {
                                        sameValues = false;
                                    }
                                }
                            }
                        }
                    }
                    if (!sameValues) {

                        result.setResult(MetadataUpdateCheckResult.RepositoryType.property,
                                MetadataUpdateCheckResult.ResultType.change, repositoryConnection);

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

                    result.setResult(MetadataUpdateCheckResult.RepositoryType.property,
                            MetadataUpdateCheckResult.ResultType.delete, null);
                    modified = true;
                }

                // add the check result to resultList, hold the value.
                if (result.getResultType() != null) {
                    resultList.add(result);
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
        List<MetadataUpdateCheckResult> resultList = new ArrayList<MetadataUpdateCheckResult>();
        boolean modified = false;
        for (Node node : nodes) {
            if (checkNodeSchemaFromRepository(node, resultList)) {
                modified = true;
            }
            if (checkNodePropertiesFromRepository(node, resultList)) {
                modified = true;
            }
        }

        // when modified == true, then resultList.size() > 0
        if (resultList.size() > 0) {
            MetadataUpdateCheckDialog checkDlg = new MetadataUpdateCheckDialog(PlatformUI.getWorkbench().getDisplay()
                    .getActiveShell(), resultList, Messages.getString("Process.IfToUpgradeMetadata")); //$NON-NLS-1$
            checkDlg.setTitle(Messages.getString("Process.metadataModificationDetected")); //$NON-NLS-1$

            checkDlg.setInputElement(resultList);
            int ret = checkDlg.open();
            if (ret == IDialogConstants.OK_ID) {
                List<Object> selectResult = Arrays.asList(checkDlg.getResult());

                updateNodeswithMetadata(selectResult);

                refreshPropertyView();

                modified = true;

            } else { // IDialogConstants.CANCEL_ID
                modified = false;
            }
        }

        return modified;
    }

    private void updateNodeswithMetadata(final List<Object> list) {

        for (int k = 0; k < list.size(); k++) {

            MetadataUpdateCheckResult result = (MetadataUpdateCheckResult) list.get(k);

            Node node = result.getNode();

            if (result.getRepositoryType() == MetadataUpdateCheckResult.RepositoryType.property) {

                if (result.getResultType() == MetadataUpdateCheckResult.ResultType.change) {

                    // upgrade from repository
                    if (result.isChecked()) {
                        for (IElementParameter param : node.getElementParameters()) {
                            String repositoryValue = param.getRepositoryValue();
                            if (param.isShow(node.getElementParameters()) && (repositoryValue != null)
                                    && (!param.getName().equals(EParameterName.PROPERTY_TYPE.getName()))) {
                                Object objectValue = RepositoryToComponentProperty.getValue(
                                        (org.talend.core.model.metadata.builder.connection.Connection) result.getParameter(),
                                        repositoryValue);
                                if (objectValue != null) {
                                    if (param.getField().equals(EParameterFieldType.CLOSED_LIST)
                                            && param.getRepositoryValue().equals("TYPE")) { //$NON-NLS-1$
                                        boolean found = false;
                                        String[] items = param.getListRepositoryItems();
                                        for (int i = 0; (i < items.length) && (!found); i++) {
                                            if (objectValue.equals(items[i])) {
                                                found = true;
                                                node.setPropertyValue(param.getName(), param.getListItemsValue()[i]);
                                            }
                                        }
                                    } else {
                                        node.setPropertyValue(param.getName(), objectValue);
                                    }
                                    param.setRepositoryValueUsed(true);
                                }
                            }
                        }
                    } else { // result.isChecked() == false
                        // don't upgrade so set to builtin
                        node.setPropertyValue(EParameterName.PROPERTY_TYPE.getName(), EmfComponent.BUILTIN);
                        for (IElementParameter param : node.getElementParameters()) {
                            String repositoryValue = param.getRepositoryValue();
                            if (param.isShow(node.getElementParameters()) && (repositoryValue != null)) {
                                param.setRepositoryValueUsed(false);
                            }
                        }
                    }
                } else { // MetadataUpdateCheckResult.ResultType.delete

                    node.setPropertyValue(EParameterName.PROPERTY_TYPE.getName(), EmfComponent.BUILTIN);
                }

            } else if (result.getRepositoryType() == MetadataUpdateCheckResult.RepositoryType.schema) {

                if (result.getResultType() == MetadataUpdateCheckResult.ResultType.change) {

                    if (result.isChecked()) {
                        IMetadataTable newTable = ((IMetadataTable) result.getParameter());
                        // node.getMetadataFromConnector(newTable.getAttachedConnector()).setListColumns(newTable.getListColumns());
                        MetadataTool.copyTable(newTable, node.getMetadataFromConnector(newTable.getAttachedConnector()));
                    } else { // result.isChecked()==false
                        node.setPropertyValue(EParameterName.SCHEMA_TYPE.getName(), EmfComponent.BUILTIN);
                    }
                } else { // MetadataUpdateCheckResult.ResultType.delete

                    node.setPropertyValue(EParameterName.SCHEMA_TYPE.getName(), EmfComponent.BUILTIN);

                }

            } else if (result.getRepositoryType() == MetadataUpdateCheckResult.RepositoryType.query) {
                // here need to add the code the do the "query"
            }
        }

    }

    private void loadConnections(ProcessType process, Hashtable<String, Node> nodesHashtable) {
        EList listParamType;
        EList connecList;
        ConnectionType cType;
        connecList = process.getConnection();
        Connection connec;
        Node source, target;

        List<String> connectionsProblems = new ArrayList<String>();

        Hashtable<ConnectionType, Connection> connectionsHashtable = new Hashtable<ConnectionType, Connection>();

        for (int i = 0; i < connecList.size(); i++) {
            cType = (ConnectionType) connecList.get(i);
            source = nodesHashtable.get(cType.getSource());
            target = nodesHashtable.get(cType.getTarget());
            Integer lineStyleId = new Integer(cType.getLineStyle());
            String connectorName = cType.getConnectorName();
            boolean connectionTypeFound = false;
            if (connectorName != null) {
                // check if the connector exists and if the line style is
                // correct
                // (used for automatic component upgrade, to avoid migration
                // each time)
                if (source.getConnectorFromName(connectorName) != null
                        && (source.getConnectorFromName(connectorName).getDefaultConnectionType().getId() == lineStyleId)) {
                    connectionTypeFound = true;
                }
            }

            // fix to correct the bug of the metaname after renaming the output of a tMap
            String metaname = cType.getMetaname();
            if ((source.getComponent().getName().equals("tMap")) && (source.getMetadataTable(metaname) == null)) {
                metaname = cType.getLabel();
                // the label should be the original name of the metadata
                if (source.getMetadataTable(metaname) == null) {
                    // this problem should never appear, just in case.
                    if (source.getMetadataList().size() > 0) {
                        metaname = source.getMetadataList().get(0).getTableName();
                    }
                    connectionsProblems.add(cType.getLabel());
                }
            }
            // end of fix

            if (connectionTypeFound) {
                connec = new Connection(source, target, EConnectionType.getTypeFromId(lineStyleId), connectorName, metaname,
                        cType.getLabel(), cType.getMetaname());
            } else {
                EConnectionType type = EConnectionType.getTypeFromId(lineStyleId);
                connec = new Connection(source, target, type, source.getConnectorFromType(type).getName(), metaname, cType
                        .getLabel(), cType.getMetaname());
            }
            // if ((!source.isActivate()) || (!target.isActivate())) {
            // connec.setActivate(false);
            // }
            connectionsHashtable.put(cType, connec);
            listParamType = cType.getElementParameter();
            loadElementParameters(connec, listParamType);

            Point offset = new Point(cType.getOffsetLabelX(), cType.getOffsetLabelY());
            INodeConnector nodeConnectorSource = connec.getSourceNodeConnector();
            nodeConnectorSource.setCurLinkNbOutput(nodeConnectorSource.getCurLinkNbOutput() + 1);
            INodeConnector nodeConnectorTarget = connec.getTargetNodeConnector();
            nodeConnectorTarget.setCurLinkNbInput(nodeConnectorTarget.getCurLinkNbInput() + 1);
            connec.getConnectionLabel().setOffset(offset);
        }

        for (INode node : nodes) {
            if (node.getComponent().useMerge()) {
                for (int i = 0; i < connecList.size(); i++) {
                    cType = (ConnectionType) connecList.get(i);
                    if (cType.getTarget().equals(node.getUniqueName())) {
                        if (cType.isSetMergeOrder()) {
                            Connection connection = connectionsHashtable.get(cType);
                            connection.setInputId(cType.getMergeOrder());
                            connection.updateName();
                        }
                    }
                }
            }
        }

        if (connectionsProblems.size() > 0) {
            String message = Messages.getString("Process.errorLoadingConnectionMessage"); //$NON-NLS-1$
            for (int i = 0; i < connectionsProblems.size(); i++) {
                message += connectionsProblems.get(i);
                if (i < (connectionsProblems.size() - 1)) {
                    message += ","; //$NON-NLS-1$
                }
            }
            if (PlatformUI.isWorkbenchRunning()) {
                MessageBox mb = new MessageBox(PlatformUI.getWorkbench().getDisplay().getActiveShell(), SWT.ICON_ERROR);
                mb.setText(Messages.getString("Process.errorLoadingConnectionTitle")); //$NON-NLS-1$
                mb.setMessage(message);
                mb.open();
            } else {
                IStatus status = new Status(IStatus.WARNING, DesignerPlugin.ID, message);
                DesignerPlugin.getDefault().getLog().log(status);
            }
        }
    }

    private void loadContexts(ProcessType process) {
        /**
         * Load the contexts informations
         */
        String defaultContextToLoad;
        defaultContextToLoad = process.getDefaultContext();

        if (process.getContext() == null) {
            contextManager = new JobContextManager();
        } else {
            contextManager = new JobContextManager(process.getContext(), defaultContextToLoad);
        }
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

        for (Note note : notes) {
            note.setReadOnly(readOnly);
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
        if (!getProperty().getAuthor().equals(author)) {
            getProperty().setAuthor(author);
        }
        setPropertyValue(EParameterName.AUTHOR.getName(), author.toString());
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IRepositoryProcess#setId(int)
     */
    public void setId(String id) {
        if (!getProperty().getId().equals(id)) {
            getProperty().setId(id);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IRepositoryProcess#setLabel(java.lang.String)
     */
    public void setLabel(String label) {
        if (!getProperty().getLabel().equals(label)) {
            getProperty().setLabel(label);
        }
        setPropertyValue(EParameterName.NAME.getName(), label);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IRepositoryProcess#setStatus(org.talend.core.model.process.EProcessStatus)
     */
    public void setStatusCode(String statusCode) {
        if (!getProperty().getStatusCode().equals(statusCode)) {
            getProperty().setStatusCode(statusCode);
        }
        setPropertyValue(EParameterName.STATUS.getName(), statusCode);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IRepositoryProcess#setVersion(int)
     */
    public void setVersion(String version) {
        if (!getProperty().getVersion().equals(version)) {
            getProperty().setVersion(version);
        }
        setPropertyValue(EParameterName.VERSION.getName(), version);
    }

    // private InputStream content;
    private byte[] content;

    private String repositoryId;

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

        try {
            pattern = compiler.compile("^[A-Za-z_][A-Za-z0-9_]*$"); //$NON-NLS-1$
            if (!matcher.matches(connectionName, pattern)) {
                return false;
            }
        } catch (MalformedPatternException e) {
            throw new RuntimeException(e);
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
        return UniqueNodeNameGenerator.generateUniqueNodeName(baseName, uniqueNodeNameList);
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
    private void setActivateSubjob(Node node, boolean active, Node activateNode) {
        Node mainSubProcess = node.getSubProcessStartNode(false);

        // if the selected node is the start node, then everything will be
        // desacticated
        if (activateNode.isStart()) {
            for (Connection connec : (List<Connection>) node.getIncomingConnections()) {
                if (connec.getSource().isActivate() != active) {
                    if (connec.getLineStyle().hasConnectionCategory(IConnectionCategory.DATA)) {
                        if (connec.getSource().getSubProcessStartNode(false).isActivate() != active) {
                            setActivateSubjob(connec.getSource().getSubProcessStartNode(false), active, activateNode);
                        }
                    }
                }
            }
            node.setPropertyValue(EParameterName.ACTIVATE.getName(), new Boolean(active));
            for (Connection connec : (List<Connection>) node.getOutgoingConnections()) {
                if (connec.getTarget().isActivate() != active) {
                    setActivateSubjob(connec.getTarget(), active, activateNode);
                }
            }

        } else {
            if (node.getSubProcessStartNode(false).equals(mainSubProcess)) {
                node.setPropertyValue(EParameterName.ACTIVATE.getName(), new Boolean(active));
                for (Connection connec : (List<Connection>) node.getIncomingConnections()) {
                    if (connec.getLineStyle().hasConnectionCategory(IConnectionCategory.DATA)) {
                        if (connec.getSource().isActivate() != active) {
                            setActivateSubjob(connec.getSource(), active, activateNode);
                        }
                    }
                }
                for (Connection connec : (List<Connection>) node.getOutgoingConnections()) {
                    if (connec.getLineStyle().hasConnectionCategory(IConnectionCategory.DATA)) {
                        if (connec.getTarget().isActivate() != active) {
                            setActivateSubjob(connec.getTarget(), active, activateNode);
                        }
                    }
                }
            }
            node.setPropertyValue(EParameterName.ACTIVATE.getName(), new Boolean(active));
        }
    }

    public void setActivateSubjob(Node node, boolean active) {
        // desactive first the process to avoid to check the process during the
        // activation / desactivation
        setActivate(false);
        setActivateSubjob(node, active, node);
        // now that everything is set, reactivate the process
        setActivate(true);
    }

    public void checkStartNodes() {
        for (Node node : nodes) {
            if ((Boolean) node.getPropertyValue(EParameterName.STARTABLE.getName())) {
                if (node.isActivate()) {
                    node.setStart(node.checkIfCanBeStart());
                }
            }
        }
    }

    /**
     * If the node link with the merge node, it will return the merge link order, or it will return -1 Purpose: only in
     * the branch of the first merge link can be as a start node.
     * 
     * @param node
     * @return
     */
    public int getMergelinkOrder(final INode node) {

        List<? extends IConnection> outgoingConnections = node.getOutgoingConnections();
        int returnValue = -1;
        for (int i = 0; (i < outgoingConnections.size()) && (returnValue == -1); i++) {
            IConnection connec = outgoingConnections.get(i);
            if (connec.isActivate()) {
                if (connec.getLineStyle().hasConnectionCategory(EConnectionType.MERGE)) {
                    returnValue = connec.getInputId();
                    break;
                } else if (connec.getLineStyle().hasConnectionCategory(EConnectionType.MAIN) && connec.getTarget() != null) {
                    returnValue = getMergelinkOrder(connec.getTarget());
                }
            }
        }

        return returnValue;
    }

    /**
     * just like the getMergelinkOrder(), there will return more info the key is the Merge node, and value is inputId.
     * if don't link with merge, it will return null. Notice: make sure there only link with one merge node. It can't
     * support to link with more merge node.
     * 
     * @param node
     * @return
     */
    public Map<INode, Integer> getLinkedMergeInfo(final INode node) {

        List<? extends IConnection> outgoingConnections = node.getOutgoingConnections();
        Map<INode, Integer> map = null;
        for (int i = 0; (i < outgoingConnections.size()) && (map == null); i++) {
            IConnection connec = outgoingConnections.get(i);
            if (connec.isActivate()) {
                if (connec.getLineStyle().hasConnectionCategory(EConnectionType.MERGE)) {
                    map = new HashMap<INode, Integer>();
                    map.put(connec.getTarget(), connec.getInputId());
                    break;
                } else if (connec.getLineStyle().hasConnectionCategory(EConnectionType.MAIN) && connec.getTarget() != null) {
                    map = getLinkedMergeInfo(connec.getTarget());
                }
            }
        }

        return map;
    }

    /**
     * This function check if in this subprocess there should be a start or not depends on the ref links. If in this
     * subprocess there is only one main flow and one ref then this function will return true. If there is several flow
     * in the output of one component in this subprocess,it'll return false.
     * 
     * @param node
     * @return
     */
    public boolean isThereLinkWithHash(final INode node) {
        boolean refLink = false;

        for (int i = 0; i < node.getOutgoingConnections().size() && !refLink; i++) {
            IConnection connec = node.getOutgoingConnections().get(i);
            if (connec.isActivate()) {
                if (connec.getLineStyle().hasConnectionCategory(IConnectionCategory.USE_HASH)) {
                    refLink = true;
                } else {
                    if (connec.getLineStyle().equals(EConnectionType.FLOW_MAIN)
                            || connec.getLineStyle().equals(EConnectionType.FLOW_MERGE)
                            || connec.getLineStyle().equals(EConnectionType.ITERATE)
                            || connec.getLineStyle().hasConnectionCategory(IConnectionCategory.EXECUTION_ORDER)) {
                        refLink = isThereLinkWithHash(connec.getTarget());
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
        Problems.removeProblemsByProcess(this);

        for (Node node : nodes) {
            if (node.isActivate()) {
                node.checkNode();
            }
        }
        Problems.refreshView();
    }

    /**
     * check the problems of node.compare with the checkProblems(),this method can't refresh problems view.
     */
    public void checkNodeProblems() {
        if (isActivate()) {
            Problems.removeProblemsByProcess(this);

            for (Node node : nodes) {
                if (node.isActivate()) {
                    node.checkNode();
                }
            }
        }
    }

    @Override
    public String toString() {
        return "Process:" + getLabel(); //$NON-NLS-1$
    }

    public ERepositoryObjectType getType() {
        return ERepositoryObjectType.PROCESS;
    }

    public IContextManager getContextManager() {
        return contextManager;
    }

    // PTODO mhelleboid remove
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
        if (getProperty().getDescription() == null) {
            if (value != null) {
                getProperty().setDescription(value);
            }
        } else {
            if (!getProperty().getDescription().equals(value)) {
                getProperty().setDescription(value);
            }
        }
        setPropertyValue(EParameterName.DESCRIPTION.getName(), value);
    }

    public void setModificationDate(Date value) {
    }

    public void setPurpose(String value) {
        if (getProperty().getPurpose() == null) {
            if (value != null) {
                getProperty().setPurpose(value);
            }
        } else {
            if (!getProperty().getPurpose().equals(value)) {
                getProperty().setPurpose(value);
            }
        }
        setPropertyValue(EParameterName.PURPOSE.getName(), value);
    }

    @Override
    public void setPropertyValue(String id, Object value) {
        if (id.equals(EParameterName.SCHEMA_TYPE.getName()) || id.equals(EParameterName.QUERYSTORE_TYPE.getName())
                || id.equals(EParameterName.PROPERTY_TYPE.getName()) || id.equals(EParameterName.PROCESS_TYPE_PROCESS.getName())
                || id.equals(EParameterName.ENCODING_TYPE.getName())) {
            setPropertyValue(EParameterName.UPDATE_COMPONENTS.getName(), Boolean.TRUE);
        }

        super.setPropertyValue(id, value);
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
    public List<INode> getNodesOfType(String componentName) {
        List<INode> matchingNodes = new ArrayList<INode>();
        for (INode node : getGeneratingNodes()) {
            if (node.isActivate()) {
                if (componentName == null) { // means all nodes will be
                    // returned
                    matchingNodes.add(node);
                } else if (componentName.startsWith("FAMILY:")) {
                    String familly = componentName.substring("FAMILY:".length());
                    if (node.getComponent().getFamily().startsWith(familly)) {
                        matchingNodes.add(node);
                    }
                } else if (componentName.startsWith("REGEXP:")) {
                    Perl5Matcher matcher = new Perl5Matcher();
                    Perl5Compiler compiler = new Perl5Compiler();
                    Pattern pattern;

                    String regexp = componentName.substring("REGEXP:".length());
                    try {
                        pattern = compiler.compile(regexp); //$NON-NLS-1$
                        if (matcher.matches(node.getComponent().getName(), pattern)) {
                            matchingNodes.add(node);
                        }
                    } catch (MalformedPatternException e) {
                        throw new RuntimeException(e);
                    }
                } else if ((node.getComponent().getName() != null)
                        && (node.getComponent().getName().compareTo(componentName)) == 0) {
                    matchingNodes.add(node);
                }
            }
        }
        return matchingNodes;
    }

    /**
     * Comment method "getAllConnections".
     * 
     * @param filter only return the filter matched connections
     * @return
     */
    public IConnection[] getAllConnections(String filter) {
        List<? extends INode> nodes = getGraphicalNodes();
        Set<IConnection> conns = new HashSet<IConnection>();

        for (INode node : nodes) {
            if (node.isActivate()) {
                conns.addAll(node.getIncomingConnections());
                conns.addAll(node.getOutgoingConnections());
            }
        }

        if (filter != null) {
            // construct filter array
            String[] f = filter.split("\\|"); //$NON-NLS-1$
            List<String> filterArray = new ArrayList<String>(f.length);
            for (int i = 0; i < f.length; i++) {
                filterArray.add(f[i].trim());
            }

            for (Iterator<IConnection> iter = conns.iterator(); iter.hasNext();) {
                IConnection con = iter.next();
                if (!filterArray.contains(con.getLineStyle().toString())) {
                    iter.remove();
                }
            }
        }
        return conns.toArray(new IConnection[conns.size()]);
    }

    public Project getProject() {
        return ((org.talend.core.context.RepositoryContext) CorePlugin.getContext().getProperty(
                org.talend.core.context.Context.REPOSITORY_CONTEXT_KEY)).getProject();
    }

    public void setAsMasterJob() {
        getProject().setMasterJobId(this.getId());
    }

    public ProcessItem getMasterJob() {
        ProcessItem item = null;
        IProxyRepositoryFactory factory = DesignerPlugin.getDefault().getProxyRepositoryFactory();

        try {
            IRepositoryObject repositoryObject = factory.getProcess().getMember(getProject().getMasterJobId());
            if (repositoryObject.getType() == ERepositoryObjectType.PROCESS) {
                item = (ProcessItem) repositoryObject.getProperty().getItem();
            }
        } catch (PersistenceException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return item;
    }

    public String getRepositoryId() {
        return repositoryId;
    }

    public void setRepositoryId(String repositoryId) {
        this.repositoryId = repositoryId;
    }

    public void addNote(Note note) {
        elem.add(note);
        notes.add(note);
        fireStructureChange(NOTES, elem);
    }

    public void removeNote(Note note) {
        elem.remove(note);
        notes.remove(note);
        fireStructureChange(NOTES, elem);
    }

    /**
     * Getter for processor.
     * 
     * @return the processor
     */
    public IProcessor getProcessor() {
        return processor;
    }

    /**
     * Sets the processor.
     * 
     * @param processor the processor to set
     */
    public void setProcessor(IProcessor processor) {
        this.processor = processor;
    }

    public Set<String> getNeededLibraries(boolean withChildrens) {
        Set<String> neededLibraries = new HashSet<String>();
        List<? extends INode> nodeList = getGeneratingNodes();
        for (INode node : nodeList) {
            List<ModuleNeeded> moduleList = node.getComponent().getModulesNeeded();
            for (ModuleNeeded needed : moduleList) {
                neededLibraries.add(needed.getModuleName());
            }
            for (IElementParameter curParam : node.getElementParameters()) {
                if (curParam.getField().equals(EParameterFieldType.MODULE_LIST)) {
                    if (!"".equals(curParam.getValue())) { // if the parameter
                        // is not empty.
                        neededLibraries.add((String) curParam.getValue());
                    }
                }
            }
        }
        if (withChildrens) {
            Set<String> childrensList = new HashSet<String>(); // in case the
            // same children
            // is used
            // several time
            ProcessItem processItem = (ProcessItem) this.property.getItem();
            if (processItem.getProcess().getRequired() != null) {
                EList jobList = processItem.getProcess().getRequired().getJob();
                for (int j = 0; j < jobList.size(); j++) {
                    JobType jType = (JobType) jobList.get(j);
                    if (!childrensList.contains(jType.getName())) {
                        // check if we already have the libraries of this job
                        childrensList.add(jType.getName());
                        ProcessItem childItem = ProcessorUtilities.getProcessItem(jType.getName());
                        if (childItem == null) {
                            continue;
                        }
                        Process child = new Process(childItem.getProperty());
                        child.loadXmlFile(childItem.getProcess());
                        neededLibraries.addAll(child.getNeededLibraries(true));
                    }
                }
            }
        }
        return neededLibraries;
    }

    /**
     * Get sub jobs under this process
     * 
     * yzhang Comment method "getSubJobs".
     * 
     * @return
     */
    public Set<String> getSubJobs(String processName) {
        Set<String> curSubJobs = new HashSet<String>();
        Process currentProcess;
        if (processName == null) {
            currentProcess = this;
        } else {
            ProcessItem pi = ProcessorUtilities.getProcessItem(processName);
            if (pi == null) { // if the job is not valid, then return empty
                // childs
                return curSubJobs;
            }
            currentProcess = new Process(pi.getProperty());
            currentProcess.loadXmlFile(pi.getProcess());
        }
        for (Iterator iter = currentProcess.getGraphicalNodes().iterator(); iter.hasNext();) {
            Node node = (Node) iter.next();
            if ((node != null) && node.getComponent().getName().equals("tRunJob")) {
                String curProcessName = (String) node.getPropertyValue(EParameterName.PROCESS_TYPE_PROCESS.getName());
                curSubJobs.add(curProcessName);
                curSubJobs.addAll(getSubJobs(curProcessName));
            }
        }
        return curSubJobs;
    }

    /**
     * Getter for notes.
     * 
     * @return the notes
     */
    public List<Note> getNotes() {
        return notes;
    }
}
