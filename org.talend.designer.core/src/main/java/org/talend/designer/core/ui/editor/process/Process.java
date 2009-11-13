// ============================================================================
//
// Copyright (C) 2006-2009 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
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
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.oro.text.regex.MalformedPatternException;
import org.apache.oro.text.regex.Pattern;
import org.apache.oro.text.regex.Perl5Compiler;
import org.apache.oro.text.regex.Perl5Matcher;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.emf.common.util.EList;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.SnapToGeometry;
import org.eclipse.gef.SnapToGrid;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CommandStack;
import org.eclipse.gef.commands.CommandStackEvent;
import org.eclipse.gef.commands.CommandStackEventListener;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;
import org.talend.commons.CommonsPlugin;
import org.talend.commons.emf.EmfHelper;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.exception.PersistenceException;
import org.talend.commons.utils.image.ImageUtils;
import org.talend.core.model.components.IComponent;
import org.talend.core.model.context.ContextUtils;
import org.talend.core.model.context.JobContextManager;
import org.talend.core.model.general.Project;
import org.talend.core.model.metadata.IEbcdicConstant;
import org.talend.core.model.metadata.IMetadataColumn;
import org.talend.core.model.metadata.IMetadataTable;
import org.talend.core.model.metadata.MetadataTool;
import org.talend.core.model.process.EComponentCategory;
import org.talend.core.model.process.EConnectionType;
import org.talend.core.model.process.EParameterFieldType;
import org.talend.core.model.process.Element;
import org.talend.core.model.process.IConnection;
import org.talend.core.model.process.IConnectionCategory;
import org.talend.core.model.process.IContext;
import org.talend.core.model.process.IContextManager;
import org.talend.core.model.process.IContextParameter;
import org.talend.core.model.process.IElementParameter;
import org.talend.core.model.process.INode;
import org.talend.core.model.process.INodeConnector;
import org.talend.core.model.process.IProcess2;
import org.talend.core.model.process.ISubjobContainer;
import org.talend.core.model.process.UniqueNodeNameGenerator;
import org.talend.core.model.properties.ContextItem;
import org.talend.core.model.properties.ProcessItem;
import org.talend.core.model.properties.Property;
import org.talend.core.model.properties.User;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.IRepositoryObject;
import org.talend.core.model.update.IUpdateManager;
import org.talend.core.model.utils.TalendTextUtils;
import org.talend.core.utils.KeywordsValidator;
import org.talend.designer.core.DesignerPlugin;
import org.talend.designer.core.i18n.Messages;
import org.talend.designer.core.model.components.EParameterName;
import org.talend.designer.core.model.components.ElementParameter;
import org.talend.designer.core.model.components.EmfComponent;
import org.talend.designer.core.model.metadata.MetadataEmfFactory;
import org.talend.designer.core.model.process.AbstractProcessProvider;
import org.talend.designer.core.model.process.DataNode;
import org.talend.designer.core.model.process.DataProcess;
import org.talend.designer.core.model.process.jobsettings.JobSettingsConstants;
import org.talend.designer.core.model.process.jobsettings.JobSettingsManager;
import org.talend.designer.core.model.utils.emf.talendfile.ConnectionType;
import org.talend.designer.core.model.utils.emf.talendfile.ElementParameterType;
import org.talend.designer.core.model.utils.emf.talendfile.ElementValueType;
import org.talend.designer.core.model.utils.emf.talendfile.MetadataType;
import org.talend.designer.core.model.utils.emf.talendfile.NodeType;
import org.talend.designer.core.model.utils.emf.talendfile.NoteType;
import org.talend.designer.core.model.utils.emf.talendfile.ParametersType;
import org.talend.designer.core.model.utils.emf.talendfile.ProcessType;
import org.talend.designer.core.model.utils.emf.talendfile.SubjobType;
import org.talend.designer.core.model.utils.emf.talendfile.TalendFileFactory;
import org.talend.designer.core.ui.AbstractMultiPageTalendEditor;
import org.talend.designer.core.ui.editor.connections.Connection;
import org.talend.designer.core.ui.editor.nodecontainer.NodeContainer;
import org.talend.designer.core.ui.editor.nodes.Node;
import org.talend.designer.core.ui.editor.nodes.Node.Data;
import org.talend.designer.core.ui.editor.notes.Note;
import org.talend.designer.core.ui.editor.properties.controllers.ColumnListController;
import org.talend.designer.core.ui.editor.properties.controllers.ConnectionListController;
import org.talend.designer.core.ui.editor.subjobcontainer.SubjobContainer;
import org.talend.designer.core.ui.preferences.StatsAndLogsConstants;
import org.talend.designer.core.ui.preferences.TalendDesignerPrefConstants;
import org.talend.designer.core.ui.views.problems.Problems;
import org.talend.designer.core.utils.DesignerUtilities;
import org.talend.designer.core.utils.JavaProcessUtil;
import org.talend.designer.runprocess.IProcessor;
import org.talend.designer.runprocess.IRunProcessService;
import org.talend.designer.runprocess.ItemCacheManager;
import org.talend.repository.ProjectManager;
import org.talend.repository.model.ComponentsFactoryProvider;
import org.talend.repository.model.IProxyRepositoryFactory;
import org.talend.repository.model.RepositoryNode;
import org.talend.repository.model.migration.UpdateTheJobsActionsOnTable;

/**
 * The diagram will contain all elements (nodes, connections) The xml that describes the diagram will be saved from the
 * information of this class. <br/>
 * 
 * $Id$
 * 
 */
public class Process extends Element implements IProcess2 {

    // properties
    public static final String NEED_UPDATE_JOB = "NEED_UPDATE_JOB"; //$NON-NLS-1$

    public static final String TABLE_ACTION = "TABLE_ACTION"; //$NON-NLS-1$

    public static final String DEFAULT_ROW_CONNECTION_NAME = "row"; //$NON-NLS-1$

    public static final String DEFAULT_TABLE_CONNECTION_NAME = "table"; //$NON-NLS-1$

    public static final String DEFAULT_ITERATE_CONNECTION_NAME = "iterate"; //$NON-NLS-1$

    protected List<Node> nodes = new ArrayList<Node>();

    protected List<Element> elem = new ArrayList<Element>();

    protected List<SubjobContainer> subjobContainers = new ArrayList<SubjobContainer>();

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

    public static final int INFO_STATUS = 16;

    public static final int PARALLEL_STATUS = 8;

    private Property property;

    private boolean initDone = false;

    private IProcessor processor;

    private AbstractMultiPageTalendEditor editor;

    private Map<Node, SubjobContainer> mapSubjobStarts = new HashMap<Node, SubjobContainer>();

    private boolean duplicate = false;

    protected IUpdateManager updateManager;

    protected ImageDescriptor screenshot = null;

    byte[] innerContent = null;

    public Process(Property property) {
        this.property = property;
        contextManager = new JobContextManager();
        updateManager = new ProcessUpdateManager(this);
        createProcessParameters();
        init();
    }

    public void updateProperties() {
        try {
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
        } catch (Exception ex) {
            ExceptionHandler.process(ex);
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
        createJobSettingsParameters();
    }

    /**
     * create parameters for tabbed page 'Job Settings'.
     */
    private void createJobSettingsParameters() {
        JobSettingsManager.createJobSettingsParemeters(this);
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
        param.setName(EParameterName.COMP_DEFAULT_PROJECT_DIR.getName());
        param.setCategory(EComponentCategory.MAIN);
        param.setField(EParameterFieldType.DIRECTORY);
        param.setDisplayName(EParameterName.COMP_DEFAULT_PROJECT_DIR.getDisplayName());
        param.setNumRow(99);
        param.setShow(false);
        param.setValue(DesignerPlugin.getDefault().getPluginPreferences().getString(
                TalendDesignerPrefConstants.COMP_DEFAULT_PROJECT_DIR));
        param.setReadOnly(true);
        addElementParameter(param);

        param = new ElementParameter(this);
        param.setName(EParameterName.JOB_RUN_VM_ARGUMENTS.getName());
        param.setCategory(EComponentCategory.MAIN);
        param.setField(EParameterFieldType.TEXT);
        param.setDisplayName(EParameterName.JOB_RUN_VM_ARGUMENTS.getDisplayName());
        param.setNumRow(99);
        param.setShow(false);
        IRunProcessService service = DesignerPlugin.getDefault().getRunProcessService();
        if (service != null) {
            param.setValue(service.getPreferenceStore().getString("vmarguments"));
        }
        addElementParameter(param);

        param = new ElementParameter(this);
        param.setName(TalendDesignerPrefConstants.DISPLAY_SUBJOBS);
        param.setCategory(EComponentCategory.TECHNICAL);
        param.setField(EParameterFieldType.CHECK);
        param.setDisplayName(TalendDesignerPrefConstants.DISPLAY_SUBJOBS);
        param.setNumRow(99);
        param.setShow(false);
        param
                .setValue(DesignerPlugin.getDefault().getPluginPreferences().getBoolean(
                        TalendDesignerPrefConstants.DISPLAY_SUBJOBS));
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

        param = new ElementParameter(this);
        param.setName(SCREEN_OFFSET_X);
        param.setCategory(EComponentCategory.TECHNICAL);
        param.setField(EParameterFieldType.TEXT);
        param.setShow(false);
        param.setReadOnly(false);
        addElementParameter(param);

        param = new ElementParameter(this);
        param.setName(SCREEN_OFFSET_Y);
        param.setCategory(EComponentCategory.TECHNICAL);
        param.setField(EParameterFieldType.TEXT);
        param.setShow(false);
        param.setReadOnly(false);
        addElementParameter(param);

        param = new ElementParameter(this);
        param.setName(EParameterName.SCHEMA_OPTIONS.getName());
        param.setCategory(EComponentCategory.MAIN);
        param.setField(EParameterFieldType.TEXT);
        param.setDisplayName(EParameterName.SCHEMA_OPTIONS.getDisplayName());
        param.setShow(false);
        param.setValue(DesignerPlugin.getDefault().getPluginPreferences().getString(TalendDesignerPrefConstants.SCHEMA_OPTIONS));
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
        nodes.add(nodeContainer.getNode());

        // fireStructureChange(NEED_UPDATE_JOB, elem);
    }

    /**
     * Remove a node to the diagram.
     * 
     * @param node
     */
    public void removeNodeContainer(final NodeContainer nodeContainer) {
        removeUniqueNodeName(nodeContainer.getNode().getUniqueName());
        nodes.remove(nodeContainer.getNode());
        Element toRemove = nodeContainer;
        List<Element> toAdd = new ArrayList<Element>();
        for (Object o : elem) {
            if (o instanceof SubjobContainer) {
                SubjobContainer sjc = (SubjobContainer) o;
                if (sjc.deleteNodeContainer(nodeContainer)) {
                    if (nodeContainer.getNode().isDesignSubjobStartNode()) {
                        subjobContainers.remove(sjc);
                        toAdd.addAll(sjc.getNodeContainers());
                        toRemove = sjc;
                        break;
                    }
                }
            }
        }

        elem.remove(toRemove);
        elem.addAll(toAdd);

        // fireStructureChange(NEED_UPDATE_JOB, elem);
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

    DataProcess generatingProcess = null;

    public List<? extends INode> getGeneratingNodes() {
        if (generatingProcess == null) {
            generatingProcess = new DataProcess(this);
        }
        List<INode> generatedNodeList = generatingProcess.getNodeList();
        if (isProcessModified()) {
            List<Node> sortedFlow = sortNodes(nodes);
            if (sortedFlow.size() != nodes.size()) {
                sortedFlow = nodes;
            }
            generatingProcess.buildFromGraphicalProcess(sortedFlow);
            generatedNodeList = generatingProcess.getNodeList();
            processModified = false;
        }
        return generatedNodeList;
    }

    /**
     * 
     * DOC yexiaowei Comment method "sortNodes".
     * 
     * @param nodes
     * @return
     */
    private List<Node> sortNodes(List<Node> nodes) {

        if (nodes == null || nodes.size() <= 1) {
            return nodes;
        }

        List<Node> res = new ArrayList<Node>();

        List<List<Node>> mainStart = new ArrayList<List<Node>>();

        List<List<Node>> notMainStart = new ArrayList<List<Node>>();

        List<Node> starts = new ArrayList<Node>();

        for (Node node : nodes) {
            if (node.isStart() || node.isSubProcessStart()) {
                starts.add(node);
            }
        }

        for (Node node : starts) {
            List<Node> branch = new ArrayList<Node>();
            branch.add(node);
            findTargetAll(branch, node);
            if (node.isStart() && node.isSubProcessStart()) {
                mainStart.add(branch);
            } else {
                notMainStart.add(branch);
            }

        }

        // Must sort the mainStart first...
        List<List<Node>> tempStart = new ArrayList<List<Node>>();
        tempStart.addAll(mainStart);
        for (List<Node> preview : mainStart) {
            for (List<Node> now : mainStart) {
                if (!preview.equals(now) && now.contains(preview.get(0))) {
                    tempStart.remove(preview);
                    tempStart.add(tempStart.indexOf(now) + 1, preview);
                }
            }
        }

        for (List<Node> branch : tempStart) {
            for (Node n : branch) {
                if (!res.contains(n)) {
                    res.add(n);
                }
            }

            for (List<Node> ns : notMainStart) {

                for (Node node : ns) {
                    if (branch.contains(node)) {
                        for (Node nodeadd : ns) {
                            if (!res.contains(nodeadd)) {
                                res.add(nodeadd);
                            }
                            break;
                        }
                    }
                }

            }
        }
        return res;
    }

    private void findTargetAll(List<Node> res, Node current) {

        List conns = current.getOutgoingConnections();

        if (conns == null || conns.size() == 0) {
            return;
        } else {
            for (Object obj : conns) {
                IConnection con = (IConnection) obj;
                Node target = (Node) con.getTarget();
                if (!res.contains(target)) {
                    res.add(target);
                    findTargetAll(res, target);
                }
            }
        }
    }

    boolean processModified = true;

    public boolean isProcessModified() {
        if (generatingProcess == null) {
            return true;
        }
        List<INode> generatedNodeList = generatingProcess.getNodeList();
        if (generatedNodeList == null || generatedNodeList.isEmpty() || (this.getEditor() == null && processModified)) {
            return true;
        }
        return processModified;
    }

    /*
     * public double getZoom() { return zoom; }
     */
    private void retrieveAttachedViewer() {
        IEditorPart editorPart = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
        if (editorPart instanceof AbstractMultiPageTalendEditor) {
            viewer = ((AbstractMultiPageTalendEditor) editorPart).getTalendEditor().getViewer();
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
            if (viewer != null) {
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
            if (viewer != null) {
                return (Boolean) viewer.getProperty(SnapToGeometry.PROPERTY_SNAP_ENABLED);
            }
        } else {
            return (Boolean) viewer.getProperty(SnapToGeometry.PROPERTY_SNAP_ENABLED);
        }
        return false;
    }

    @SuppressWarnings("unchecked")
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

        if (param.getField().equals(EParameterFieldType.SCHEMA_TYPE)
                || param.getField().equals(EParameterFieldType.PROPERTY_TYPE)
                || param.getName().equals(EParameterName.UPDATE_COMPONENTS.getName())) {
            return;
        }
        if (param.getParentParameter() != null) {
            if (param.getParentParameter().getField().equals(EParameterFieldType.PROPERTY_TYPE)) {
                IElementParameter paramBuiltInRepository = param.getParentParameter().getChildParameters().get(
                        EParameterName.PROPERTY_TYPE.getName());
                if (paramBuiltInRepository.getValue().equals(EmfComponent.BUILTIN)) {
                    return;
                }
            }
            if (param.getParentParameter().getField().equals(EParameterFieldType.SCHEMA_TYPE)) {
                IElementParameter paramBuiltInRepository = param.getParentParameter().getChildParameters().get(
                        EParameterName.SCHEMA_TYPE.getName());
                if (paramBuiltInRepository.getValue().equals(EmfComponent.BUILTIN)) {
                    return;
                }
            }
        }

        if (param.getElement() instanceof Process) {
            // achen modify to fix bug 0006107
            // Process tempProcess = new Process(this.property);
            // IElementParameter tmpParam = tempProcess.getElementParameter(param.getName());
            // IElementParameter tmpParam = param.getElement().getElementParameter(param.getName());
            // if (tmpParam != null && tmpParam.getValue() != null && tmpParam.getValue().equals(param.getValue())) {
            // return;
            // }
            boolean isJoblet = AbstractProcessProvider.isExtensionProcessForJoblet(this);
            if (isJoblet) {
                if (param != null && !(param.getName().equals(EParameterName.STARTABLE.getName()))) {
                    return;
                }
            }
        }
        if (param.getElement() instanceof Node) {
            Node curNode = (Node) param.getElement();
            IComponent component = ComponentsFactoryProvider.getInstance().get(curNode.getComponent().getName());
            if (param != null && param.getName().equals(EParameterName.REPOSITORY_ALLOW_AUTO_SWITCH.getName())) {
                return;
            }
            if (component instanceof EmfComponent) {
                DataNode tempNode = new DataNode();
                tempNode.setElementParameters(new ArrayList<IElementParameter>());
                ((EmfComponent) component).addMainParameters((List<ElementParameter>) tempNode.getElementParameters(), tempNode);
                ((EmfComponent) component).addViewParameters((List<ElementParameter>) tempNode.getElementParameters(), tempNode);
                IElementParameter tmpParam1 = tempNode.getElementParameter(param.getName());
                if (tmpParam1 != null && tmpParam1.getValue() != null && tmpParam1.getValue().equals(param.getValue())) {
                    return;
                }
                if (tmpParam1 != null
                        && StringUtils.equals(tmpParam1.getValue() == null ? null : tmpParam1.getValue().toString(), param
                                .getValue() == null ? null : param.getValue().toString())) {

                    return;
                }
            }
        }
        if (param.getElement() instanceof SubjobContainer) {
            SubjobContainer subjob = new SubjobContainer(this);
            IElementParameter subjobParam = subjob.getElementParameter(param.getName());
            if (subjobParam != null && subjobParam.getValue() != null && subjobParam.getValue().equals(param.getValue())) {
                // don't save this parameter as this parameter got the default value for the component
                return;
            }
        }
        // always save the connections parameters.

        // if (param.getElement() instanceof Connection) {
        // Connection connection = (Connection) param.getElement();
        // IElementParameter connectionParam = connection.getElementParameter(param.getName());
        // if (connectionParam != null && connectionParam.getValue() != null
        // && connectionParam.getValue().equals(param.getValue())) {
        // return;
        // }
        // }
        pType = fileFact.createElementParameterType();
        if (param.getParentParameter() != null) {
            pType.setName(param.getParentParameter().getName() + ":" + param.getName()); //$NON-NLS-1$
        } else {
            pType.setName(param.getName());
        }
        pType.setField(param.getField().getName());
        pType.setContextMode(param.isContextMode());
        Object value = param.getValue();
        if (param.getField().equals(EParameterFieldType.TABLE) && value != null) {
            List<Map<String, Object>> tableValues = (List<Map<String, Object>>) value;
            for (Map<String, Object> currentLine : tableValues) {
                for (int i = 0; i < param.getListItemsDisplayCodeName().length; i++) {
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
                    //
                    Object object = currentLine.get(param.getListItemsDisplayCodeName()[i] + IEbcdicConstant.REF_TYPE);
                    if (object != null) {
                        elementValue.setType((String) object);
                    }
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

    @SuppressWarnings("unchecked")
    private void loadElementParameters(Element elemParam, EList listParamType) {
        ElementParameterType pType;

        for (int j = 0; j < listParamType.size(); j++) {
            pType = (ElementParameterType) listParamType.get(j);
            if (pType != null) {
                IElementParameter param = elemParam.getElementParameter(pType.getName());
                if (param != null) {
                    param.setContextMode(pType.isContextMode());
                    if (param.isReadOnly()
                            && !(param.getName().equals(EParameterName.UNIQUE_NAME.getName()) || param.getName().equals(
                                    EParameterName.VERSION.getName()))) {
                        continue; // if the parameter is read only, don't load
                        // it (this will prevent to overwrite the
                        // value)
                    }
                    String value = pType.getValue();
                    if (param.getField().equals(EParameterFieldType.CHECK) || param.getField().equals(EParameterFieldType.RADIO)) {
                        if ("false".equalsIgnoreCase(value) || "true".equalsIgnoreCase(value) || !pType.isContextMode()) { //$NON-NLS-1$ //$NON-NLS-2$
                            Boolean boolean1 = new Boolean(value);
                            elemParam.setPropertyValue(pType.getName(), boolean1);
                        } else {
                            elemParam.setPropertyValue(pType.getName(), value);
                        }
                        // if (EParameterName.ACTIVATE.getName().equals(param.getName())) {
                        // if ((elemParam instanceof Node) && !boolean1) {
                        // ((Node) elemParam).setDummy(!boolean1);
                        // }
                        // }
                    } else if (param.getField().equals(EParameterFieldType.CLOSED_LIST)) {
                        boolean valueSet = false;
                        if (!ArrayUtils.contains(param.getListItemsValue(), value)) {
                            if (ArrayUtils.contains(param.getListItemsDisplayName(), value)) {
                                valueSet = true;
                                int index = ArrayUtils.indexOf(param.getListItemsDisplayName(), value);
                                elemParam.setPropertyValue(pType.getName(), param.getListItemsValue()[index]);
                            }
                        }
                        if (!valueSet) {
                            elemParam.setPropertyValue(pType.getName(), value);
                        }
                        if (param.getName().equals(EParameterName.DB_TYPE.getName())) {
                            IElementParameter elementParameter = elemParam.getElementParameter(EParameterName.DB_VERSION
                                    .getName());
                            setDbVersion(elementParameter, value);
                            IElementParameter elementParameter2 = elemParam.getElementParameter(EParameterName.SCHEMA_DB
                                    .getName());
                            DesignerUtilities.setSchemaDB(elementParameter2, param.getValue());
                        } else if (param.getName().equals(
                                JobSettingsConstants.getExtraParameterName(EParameterName.DB_TYPE.getName()))) {
                            IElementParameter elementParameter = elemParam.getElementParameter(JobSettingsConstants
                                    .getExtraParameterName(EParameterName.DB_VERSION.getName()));
                            setDbVersion(elementParameter, value);
                            IElementParameter elementParameter2 = elemParam.getElementParameter(JobSettingsConstants
                                    .getExtraParameterName(EParameterName.SCHEMA_DB.getName()));
                            DesignerUtilities.setSchemaDB(elementParameter2, param.getValue());
                        }
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
                                if (elementValue.getType() != null) {
                                    lineValues.put(elementValue.getElementRef() + IEbcdicConstant.REF_TYPE, elementValue
                                            .getType());
                                }
                            }
                        }
                        // check missing codes in the table to have the default values.
                        for (Map<String, Object> line : tableValues) {
                            for (int i = 0; i < codeList.length; i++) {
                                if (!line.containsKey(codeList[i])) {
                                    IElementParameter itemParam = (IElementParameter) param.getListItemsValue()[i];
                                    line.put(codeList[i], itemParam.getValue());
                                }
                            }
                        }

                        elemParam.setPropertyValue(pType.getName(), tableValues);
                    } else if (param.getField().equals(EParameterFieldType.ENCODING_TYPE)) {
                        // fix for bug 2193
                        boolean setToCustom = false;
                        if (EmfComponent.REPOSITORY.equals(elemParam.getPropertyValue(EParameterName.PROPERTY_TYPE.getName()))
                                && param.getRepositoryValue() != null && param.getRepositoryValue().equals("ENCODING")) { //$NON-NLS-1$
                            setToCustom = true;
                        }
                        String tempValue = (String) param.getChildParameters().get(EParameterName.ENCODING_TYPE.getName())
                                .getValue();
                        if (!tempValue.equals(EmfComponent.ENCODING_TYPE_CUSTOM)) {
                            tempValue = tempValue.replaceAll("'", ""); //$NON-NLS-1$ //$NON-NLS-2$
                            tempValue = tempValue.replaceAll("\"", ""); //$NON-NLS-1$ //$NON-NLS-2$
                            tempValue = TalendTextUtils.addQuotes(tempValue);
                            if (!tempValue.equals(value)) {
                                setToCustom = true;
                            }
                        }

                        if (setToCustom) {
                            param.getChildParameters().get(EParameterName.ENCODING_TYPE.getName()).setValue(
                                    EmfComponent.ENCODING_TYPE_CUSTOM);
                        }
                        elemParam.setPropertyValue(pType.getName(), value);
                        // end of fix for bug 2193
                    } else if (!param.getField().equals(EParameterFieldType.SCHEMA_TYPE)) {
                        elemParam.setPropertyValue(pType.getName(), value);
                    }
                } else if (UpdateTheJobsActionsOnTable.isClear && "CLEAR_TABLE".equals(pType.getName()) //$NON-NLS-1$
                        && "true".equals(pType.getValue()) //$NON-NLS-1$
                        && "NONE".equals(elemParam.getElementParameter(Process.TABLE_ACTION).getValue())) { //$NON-NLS-1$
                    elemParam.setPropertyValue(Process.TABLE_ACTION, "CLEAR"); //$NON-NLS-1$
                    UpdateTheJobsActionsOnTable.isClear = false;
                }
            }
        }
    }

    private void setDbVersion(IElementParameter elementParameter, String value) {
        if (value.indexOf("Access") != -1) {//$NON-NLS-1$
            elementParameter.setValue(StatsAndLogsConstants.ACCESS_VERSION_DRIVER[1]);
            elementParameter.setListItemsDisplayName(StatsAndLogsConstants.ACCESS_VERSION_DISPLAY);
            elementParameter.setListItemsValue(StatsAndLogsConstants.ACCESS_VERSION_DRIVER);
        } else if (value.indexOf("Oracle") != -1) {//$NON-NLS-1$
            elementParameter.setValue(StatsAndLogsConstants.ORACLE_VERSION_DRIVER[1]);
            elementParameter.setListItemsDisplayName(StatsAndLogsConstants.ORACLE_VERSION_DISPLAY);
            elementParameter.setListItemsValue(StatsAndLogsConstants.ORACLE_VERSION_DRIVER);
        } else if (value.indexOf("AS400") != -1) {//$NON-NLS-1$
            elementParameter.setValue(StatsAndLogsConstants.AS400_VERSION_DRIVER[1]);
            elementParameter.setListItemsDisplayName(StatsAndLogsConstants.AS400_VERSION_DISPLAY);
            elementParameter.setListItemsValue(StatsAndLogsConstants.AS400_VERSION_DRIVER);
        }
    }

    protected ProcessType createProcessType(TalendFileFactory fileFact) {
        return fileFact.createProcessType();
    }

    /**
     * Save the diagram in a Xml File.
     * 
     * @param file
     * @return
     * @throws IOException
     */
    public ProcessType saveXmlFile() throws IOException {
        init();

        TalendFileFactory fileFact = TalendFileFactory.eINSTANCE;

        ProcessType processType = createProcessType(fileFact);

        ParametersType params = fileFact.createParametersType();
        processType.setParameters(params);

        saveElementParameters(fileFact, this.getElementParameters(), processType.getParameters().getElementParameter(),
                processType);

        EList nList = processType.getNode();
        EList cList = processType.getConnection();
        MetadataEmfFactory factory = new MetadataEmfFactory();

        // save according to elem order to keep zorder (children insertion) in
        // diagram
        for (Element element : elem) {
            if (element instanceof SubjobContainer) {
                saveSubjob(fileFact, processType, (SubjobContainer) element);
                for (NodeContainer container : ((SubjobContainer) element).getNodeContainers()) {
                    saveNode(fileFact, processType, nList, cList, container.getNode(), factory);
                }
            }
            if (element instanceof NodeContainer) {
                saveNode(fileFact, processType, nList, cList, ((NodeContainer) element).getNode(), factory);
            } else if (element instanceof Note) {
                saveNote(fileFact, processType, (Note) element);
            }
        }

        /**
         * Save the contexts informations
         */
        processType.setDefaultContext(contextManager.getDefaultContext().getName());
        byte[] saveImageToData = ImageUtils.saveImageToData(getScreenshot());
        processType.setScreenshot(saveImageToData);
        contextManager.saveToEmf(processType.getContext());
        return processType;
    }

    private void saveSubjob(TalendFileFactory fileFact, ProcessType process, SubjobContainer subjobContainer) {
        SubjobType sj = fileFact.createSubjobType();

        process.getSubjob().add(sj);

        List<? extends IElementParameter> paramList = subjobContainer.getElementParameters();
        saveElementParameters(fileFact, paramList, sj.getElementParameter(), process);
    }

    private void saveNote(TalendFileFactory fileFact, ProcessType process, Note note) {
        NoteType noteType = fileFact.createNoteType();
        noteType.setPosX(note.getLocation().x);
        noteType.setPosY(note.getLocation().y);
        noteType.setSizeWidth(note.getSize().width);
        noteType.setSizeHeight(note.getSize().height);
        noteType.setOpaque(note.isOpaque());
        noteType.setText(note.getText());
        List<? extends IElementParameter> paramList = note.getElementParameters();
        saveElementParameters(fileFact, paramList, noteType.getElementParameter(), process);
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
        nType = createNodeType(fileFact, process, nList, node);
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

        List<? extends IConnection> outgoingConnections = node.getOutgoingConnections();
        connList = new ArrayList<Connection>();
        for (IConnection connection : outgoingConnections) {
            if (connection instanceof Connection) {
                connList.add((Connection) connection);
            }
        }
        // connList = (List<Connection>) outgoingConnections;
        for (int j = 0; j < connList.size(); j++) {
            connec = connList.get(j);
            cType = fileFact.createConnectionType();
            cType.setSource(node.getUniqueName());
            cType.setTarget(connec.getTarget().getUniqueName());
            cType.setLabel(connec.getName());
            cType.setLineStyle(connec.getLineStyleId());
            cType.setConnectorName(connec.getConnectorName());
            cType.setOffsetLabelX(connec.getConnectionLabel().getOffset().x);
            cType.setOffsetLabelY(connec.getConnectionLabel().getOffset().y);
            cType.setMetaname(connec.getMetaName());
            int id = connec.getOutputId();
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

        if (node.getExternalNode() != null && node.getExternalNode().getScreenshot() != null) {
            byte[] saveImageToData = ImageUtils.saveImageToData(node.getExternalNode().getScreenshot());
            nType.setScreenshot(saveImageToData);
        }
    }

    /**
     * DOC qzhang Comment method "createNodeType".
     * 
     * @param fileFact
     * @param process
     * @param nList
     * @param node
     * @return
     */
    protected NodeType createNodeType(TalendFileFactory fileFact, ProcessType process, EList nList, Node node) {
        NodeType nType;
        nType = fileFact.createNodeType();
        nList.add(nType);
        return nType;
    }

    protected ProcessType getProcessType() {
        ProcessItem item = (ProcessItem) property.getItem();
        ProcessType processType = item.getProcess();
        return processType;
    }

    /**
     * DOC mhelleboid Comment method "loadXmlFile".
     * 
     * @param process
     */
    public void loadXmlFile() {
        init();
        Hashtable<String, Node> nodesHashtable = new Hashtable<String, Node>();

        setActivate(false);

        ProcessType processType = getProcessType();
        EmfHelper.visitChilds(processType);
        if (processType.getParameters() != null) {
            loadElementParameters(this, processType.getParameters().getElementParameter());
        }

        try {
            loadNodes(processType, nodesHashtable);
        } catch (PersistenceException e) {
            // there are some components unloaded.
            return;
        }

        repositoryId = processType.getRepositoryContextId();

        loadConnections(processType, nodesHashtable);
        loadContexts(processType);
        // feature 7410
        loadScreenshots();
        loadNotes(processType);
        loadSubjobs(processType);
        initExternalComponents();
        setActivate(true);
        checkStartNodes();
        // (bug 5365)
        checkNodeTableParameters();
        // bug 6158
        this.updateManager.retrieveRefInformation();
    }

    protected void loadScreenshots() {
        if (CommonsPlugin.isHeadless())
            return;
        innerContent = getProcessType().getScreenshot();
        setScreenshot(ImageUtils.createImageFromData(innerContent));
    }

    @SuppressWarnings("unchecked")
    private void checkNodeTableParameters() {
        for (INode node : getGraphicalNodes()) {
            ColumnListController.updateColumnList(node, null);

            for (IElementParameter param : node.getElementParameters()) {
                if (param.getField() == EParameterFieldType.TABLE) {
                    Object[] itemsValue = param.getListItemsValue();
                    if (itemsValue != null && param.getValue() != null && param.getValue() instanceof List) {
                        List<Map<String, Object>> values = (List<Map<String, Object>>) param.getValue();
                        for (int i = 0; i < itemsValue.length; i++) {
                            if (itemsValue[i] instanceof IElementParameter) {
                                IElementParameter columnParam = (IElementParameter) itemsValue[i];
                                if (columnParam.getField() == EParameterFieldType.COLUMN_LIST
                                        || columnParam.getField() == EParameterFieldType.PREV_COLUMN_LIST
                                        || columnParam.getField() == EParameterFieldType.LOOKUP_COLUMN_LIST) {
                                    for (Map<String, Object> columnMap : values) {
                                        Object column = columnMap.get(columnParam.getName());
                                        if (column == null || "".equals(column)) { //$NON-NLS-1$
                                            columnMap.put(columnParam.getName(), columnParam.getDefaultClosedListValue());
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * DOC nrousseau Comment method "loadSubjobs".
     * 
     * @param processType
     */
    private void loadSubjobs(ProcessType processType) {
        for (Iterator iter = processType.getSubjob().iterator(); iter.hasNext();) {
            SubjobType subjobType = (SubjobType) iter.next();

            SubjobContainer subjobContainer = new SubjobContainer(this);
            loadElementParameters(subjobContainer, subjobType.getElementParameter());
            // look for the related node
            Node subjobStartNode = subjobContainer.getSubjobStartNode();
            if (subjobStartNode != null) {
                subjobContainer.addNodeContainer(subjobStartNode.getNodeContainer());
                subjobContainers.add(subjobContainer);
                elem.remove(subjobStartNode.getNodeContainer());
                elem.add(subjobContainer);
                mapSubjobStarts.put(subjobStartNode, subjobContainer);
            }
        }
    }

    private void loadNotes(ProcessType process) {
        for (Iterator iter = process.getNote().iterator(); iter.hasNext();) {
            NoteType noteType = (NoteType) iter.next();
            Note note = new Note();
            note.setLocation(new Point(noteType.getPosX(), noteType.getPosY()));
            note.setSize(new Dimension(noteType.getSizeWidth(), noteType.getSizeHeight()));
            note.setOpaque(noteType.isOpaque());
            note.setText(noteType.getText());
            note.setProcess(this);
            loadElementParameters(note, noteType.getElementParameter());
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

    protected void loadNodes(ProcessType process, Hashtable<String, Node> nodesHashtable) throws PersistenceException {
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
            nc = loadNode(nType, component, nodesHashtable, listParamType);
        }
        if (!unloadedNodeNames.isEmpty()) {
            throw new PersistenceException(Messages.getString("Process.componentsUnloaded")); //$NON-NLS-1$
        }
    }

    /**
     * DOC qzhang Comment method "loadNode".
     * 
     * @param nType
     * @param component
     * @return
     */
    protected Node loadNode(NodeType nType, IComponent component, Hashtable<String, Node> nodesHashtable, EList listParamType) {
        Node nc;
        nc = new Node(component, this);
        nc.setLocation(new Point(nType.getPosX(), nType.getPosY()));
        Point offset = new Point(nType.getOffsetLabelX(), nType.getOffsetLabelY());
        nc.getNodeLabel().setOffset(offset);
        if (nType.isSetSizeX()) {
            nc.setSize(new Dimension(nType.getSizeX(), nType.getSizeY()));
        }

        loadElementParameters(nc, listParamType);

        // update the value of process type
        IElementParameter processParam = nc.getElementParameterFromField(EParameterFieldType.PROCESS_TYPE);

        if (processParam != null) {
            IElementParameter processIdParam = processParam.getChildParameters().get(
                    EParameterName.PROCESS_TYPE_PROCESS.getName());
            IElementParameter processVersionParam = processParam.getChildParameters().get(
                    EParameterName.PROCESS_TYPE_VERSION.getName());
            ProcessItem processItem = null;
            if (processVersionParam != null) {
                processItem = ItemCacheManager.getProcessItem((String) processIdParam.getValue(), (String) processVersionParam
                        .getValue());
            } else {
                processItem = ItemCacheManager.getProcessItem((String) processIdParam.getValue());
            }
            if (processItem != null) {
                nc.setPropertyValue(processParam.getName(), processItem.getProperty().getLabel());
                processIdParam.setLinkedRepositoryItem(processItem);
            }
        }
        nc.setData(nType.getBinaryData(), nType.getStringData());
        loadSchema(nc, nType);

        loadColumnsBasedOnSchema(nc, listParamType);

        addNodeContainer(new NodeContainer(nc));
        nodesHashtable.put(nc.getUniqueName(), nc);
        updateAllMappingTypes();

        byte[] innerContent = nType.getScreenshot();
        if (nc.getExternalNode() != null && !CommonsPlugin.isHeadless()) {
            nc.getExternalNode().setScreenshot(ImageUtils.createImageFromData(innerContent));
        }

        return nc;
    }

    private void loadColumnsBasedOnSchema(Node nc, EList listParamType) {
        List<IMetadataTable> metadataList = nc.getMetadataList();
        for (IElementParameter parameter : nc.getElementParameters()) {
            if (parameter.getField().equals(EParameterFieldType.TABLE) && parameter.isColumnsBasedOnSchema()) {

                if (metadataList == null || metadataList.size() == 0) {
                    return;
                }

                List<IMetadataColumn> listColumns = metadataList.get(0).getListColumns();
                if (listColumns == null) {
                    return;
                }
                String[] listItemsDisplayValue = new String[listColumns.size()];
                String[] listItemsDisplayCodeValue = new String[listColumns.size()];
                Object[] listItemsValue = new Object[listColumns.size()];
                ElementParameter newParam = null;
                for (int i = 0; i < listColumns.size(); i++) {
                    IMetadataColumn column = listColumns.get(i);
                    listItemsDisplayCodeValue[i] = column.getLabel();
                    listItemsDisplayValue[i] = column.getLabel();
                    newParam = new ElementParameter(nc);
                    newParam.setName(column.getLabel()); //$NON-NLS-1$
                    newParam.setDisplayName(""); //$NON-NLS-1$
                    newParam.setField(EParameterFieldType.TEXT);
                    newParam.setValue(""); //$NON-NLS-1$
                    listItemsValue[i] = newParam;
                }
                parameter.setListItemsDisplayName(listItemsDisplayValue);
                parameter.setListItemsDisplayCodeName(listItemsDisplayCodeValue);
                parameter.setListItemsValue(listItemsValue);

                ElementParameterType pType = null;
                for (int j = 0; j < listParamType.size(); j++) {
                    pType = (ElementParameterType) listParamType.get(j);
                    if (pType != null) {
                        IElementParameter param = nc.getElementParameter(pType.getName());
                        if (param == null) {
                            continue;
                        }
                        if (param.getField().equals(EParameterFieldType.TABLE)) {
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
                                    if (elementValue.getType() != null) {
                                        lineValues.put(elementValue.getElementRef() + IEbcdicConstant.REF_TYPE, elementValue
                                                .getType());
                                    }
                                }
                            }
                            // check missing codes in the table to have the default values.
                            for (Map<String, Object> line : tableValues) {
                                for (int i = 0; i < codeList.length; i++) {
                                    if (!line.containsKey(codeList[i])) {
                                        IElementParameter itemParam = (IElementParameter) param.getListItemsValue()[i];
                                        line.put(codeList[i], itemParam.getValue());
                                    }
                                }
                            }

                            nc.setPropertyValue(pType.getName(), tableValues);
                        }
                    }
                }

            }

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
        // bug 6086
        Set<String> listNames = new HashSet<String>();

        for (int j = 0; j < listMetaType.size(); j++) {
            mType = (MetadataType) listMetaType.get(j);
            factory.setMetadataType(mType);
            metadataTable = factory.getMetadataTable();
            // add by wzhang
            // if a schema exist in node,won't add it again
            if (!listNames.contains(metadataTable.getTableName())) {
                listNames.add(metadataTable.getTableName());
                listMetaData.add(metadataTable);
                if (nc.getConnectorFromType(EConnectionType.FLOW_MAIN).isMultiSchema()
                        && checkValidConnectionName(metadataTable.getTableName())) {
                    addUniqueConnectionName(metadataTable.getTableName());
                } else {
                    if (metadataTable.getTableName() == null) {
                        metadataTable.setTableName(nc.getUniqueName());
                    }
                }
                MetadataTool.initilializeSchemaFromElementParameters(metadataTable, (List<IElementParameter>) nc
                        .getElementParameters());
            }
        }
        List<IMetadataTable> oldComponentMetadataList = new ArrayList<IMetadataTable>(nc.getMetadataList());
        nc.setMetadataList(listMetaData);

        for (IMetadataTable table : oldComponentMetadataList) {
            if (nc.getMetadataFromConnector(table.getAttachedConnector()) == null) {
                // if there is any new connector, then add the table to the
                // list.
                String baseSchema = nc.getConnectorFromName(table.getAttachedConnector()).getBaseSchema();
                IMetadataTable metadataFromConnector = nc.getMetadataFromConnector(baseSchema);
                if (!table.getAttachedConnector().equals(baseSchema) && metadataFromConnector != null) {
                    MetadataTool.copyTable(metadataFromConnector, table);
                }
                nc.getMetadataList().add(table);
            }
        }
    }

    /**
     * have moved to UpdateManager(feature 3232).
     * 
     * DOC nrousseau Comment method "checkNodeSchemaFromRepository".
     * 
     * @param nc
     * @param metadataTable
     * @return true if the data have been modified
     */
    // private boolean checkNodeSchemaFromRepository(final Node node, final List<MetadataUpdateCheckResult> resultList)
    // {
    // boolean modified = false;
    //
    // final String uniqueName = node.getUniqueName();
    //
    // // check the metadata from the repository to see if it's up to date.
    // // String schemaType = (String)
    // // node.getPropertyValue(EParameterName.SCHEMA_TYPE.getName());
    // for (IElementParameter currentParam : node.getElementParameters()) {
    // if (currentParam.getField().equals(EParameterFieldType.SCHEMA_TYPE)) {
    // IElementParameter schemaParam = currentParam.getChildParameters().get(EParameterName.SCHEMA_TYPE.getName());
    // if (schemaParam != null && ((ElementParameter) currentParam).isDisplayedByDefault()) {
    // if (schemaParam.getValue().equals(EmfComponent.REPOSITORY)) {
    // String metaRepositoryName = (String) currentParam.getChildParameters().get(
    // EParameterName.REPOSITORY_SCHEMA_TYPE.getName()).getValue();
    // IMetadataTable repositoryMetadata = MetadataTool.getMetadataFromRepository(metaRepositoryName);
    //
    // MetadataUpdateCheckResult result = new MetadataUpdateCheckResult(node);
    //
    // if (repositoryMetadata != null) {
    // repositoryMetadata = repositoryMetadata.clone();
    // final IMetadataTable copyOfrepositoryMetadata = repositoryMetadata;
    // copyOfrepositoryMetadata.setTableName(uniqueName);
    // copyOfrepositoryMetadata.setAttachedConnector(currentParam.getContext());
    //
    // IMetadataTable metadataTable = node.getMetadataFromConnector(currentParam.getContext());
    // if (!metadataTable.sameMetadataAs(copyOfrepositoryMetadata, IMetadataColumn.OPTIONS_NONE)) {
    //
    // result.setResult(MetadataUpdateCheckResult.RepositoryType.schema,
    // MetadataUpdateCheckResult.ResultType.change, copyOfrepositoryMetadata);
    //
    // modified = true;
    // }
    // } else {
    //
    // result.setResult(MetadataUpdateCheckResult.RepositoryType.schema,
    // MetadataUpdateCheckResult.ResultType.delete, null);
    // // if the repository connection doesn't exists then
    // // set to built-in
    // modified = true;
    // }
    //
    // // add the check result to resultList, hold the value.
    // if (result.getResultType() != null) {
    // resultList.add(result);
    // }
    // }
    // }
    // }
    // }
    // return modified;
    // }
    /**
     * have moved to UpdateManager(feature 3232).
     * 
     * DOC nrousseau Comment method "checkNodePropertiesFromRepository".
     * 
     * @param node
     * @return true if the data have been modified
     */
    // private boolean checkNodePropertiesFromRepository(final Node node, final List<MetadataUpdateCheckResult>
    // resultList) {
    // boolean modified = false;
    //
    // String propertyType = (String) node.getPropertyValue(EParameterName.PROPERTY_TYPE.getName());
    // if (propertyType != null) {
    // if (propertyType.equals(EmfComponent.REPOSITORY)) {
    // String propertyValue = (String) node.getPropertyValue(EParameterName.REPOSITORY_PROPERTY_TYPE.getName());
    // if (propertyValue == null || "".equals(propertyValue)) {
    // return false;
    // }
    // org.talend.core.model.metadata.builder.connection.Connection repositoryConnection = null;
    // IProxyRepositoryFactory factory = DesignerPlugin.getDefault().getProxyRepositoryFactory();
    // try {
    // IRepositoryObject lastVersion = factory.getLastVersion(propertyValue);
    // if (lastVersion != null) {
    // final Item item = lastVersion.getProperty().getItem();
    // if (item != null && item instanceof ConnectionItem) {
    // repositoryConnection = ((ConnectionItem) item).getConnection();
    // }
    // }
    // } catch (PersistenceException e) {
    // throw new RuntimeException(e);
    // }
    //
    // MetadataUpdateCheckResult result = new MetadataUpdateCheckResult(node);
    //
    // if (repositoryConnection != null) {
    // boolean sameValues = true;
    // // if the repository connection exists then test the values
    // for (IElementParameter param : node.getElementParameters()) {
    // String repositoryValue = param.getRepositoryValue();
    // if (param.isShow(node.getElementParameters()) && (repositoryValue != null)) {
    // Object objectValue = RepositoryToComponentProperty.getValue(repositoryConnection, repositoryValue);
    //
    // if (objectValue != null) {
    // if ((param.getField().equals(EParameterFieldType.CLOSED_LIST) && "TYPE".equals(param
    // .getRepositoryValue()))) {
    // boolean found = false;
    // String[] list = param.getListRepositoryItems();
    // for (int i = 0; (i < list.length) && (!found); i++) {
    // if (objectValue.equals(list[i])) {
    // found = true;
    // }
    // }
    // if (!found) {
    // sameValues = false;
    // }
    //
    // } else if ((param.getField().equals(EParameterFieldType.CLOSED_LIST) && "SERVER_NAME"
    // .equals(param.getRepositoryValue()))
    // || (param.getField().equals(EParameterFieldType.TEXT) && "SERVER_NAME".equals(param
    // .getRepositoryValue()))) {
    // String connectQuery = null;
    // boolean flag = false;
    //
    // for (IElementParameter elementParameter : node.getElementParametersWithChildrens()) {
    // if ("QUERYSTORE_TYPE".equals(elementParameter.getName())) {
    // if ("BUILT_IN".equals(elementParameter.getValue())) {
    // flag = true;
    // break;
    // }
    // }
    // }
    //
    // if (!flag) {
    // for (IElementParameter elementParameter : node.getElementParametersWithChildrens()) {
    // if ("REPOSITORY_QUERYSTORE_TYPE".equals(elementParameter.getName())) {
    // String names[] = ((String) elementParameter.getValue()).split(" - ");
    // if (names.length != 2) {
    // continue;
    // }
    // String queryName = names[1];
    // for (QueryImpl queryImpl : (EList<QueryImpl>) repositoryConnection.getQueries()
    // .getQuery()) {
    // if (queryImpl.getLabel().equals(queryName)) {
    // connectQuery = queryImpl.getValue().replaceAll("\\s", " ").replaceAll(
    // " {2,}", " ");
    // }
    // }
    // }
    // }
    //
    // if (connectQuery != null) {
    // for (IElementParameter elementParameter : node.getElementParameters()) {
    // if ("QUERY".equals(elementParameter.getName())) {
    // if (!(connectQuery.equals(((String) elementParameter.getValue()).substring(1,
    // ((String) elementParameter.getValue()).length() - 1).replaceAll(
    // "\\s", " ").replaceAll(" {2,}", " ")))) {
    // node.setPropertyValue(param.getName(), objectValue);
    // CompoundCommand cc = new CompoundCommand();
    // PropertyChangeCommand pcc = new PropertyChangeCommand(node,
    // EParameterName.UPDATE_COMPONENTS.getName(), Boolean.TRUE);
    // cc.add(pcc);
    // if (!cc.isEmpty()) {
    // getCommandStack().execute(cc);
    // }
    // }
    // elementParameter.setRepositoryValueUsed(true);
    // elementParameter.setReadOnly(true);
    // }
    // }
    // }
    // }
    // } else {
    // // check the value
    // if (!param.getValue().equals(objectValue)) {
    // sameValues = false;
    // }
    // }
    // } else if (param.getField().equals(EParameterFieldType.TABLE)
    // && "XML_MAPPING".equals(repositoryValue)) {
    // List<Map<String, Object>> newMaps = RepositoryToComponentProperty.getXMLMappingValue(
    // repositoryConnection, node.getMetadataList().get(0));
    // if ((param.getValue() instanceof List) && newMaps != null) {
    // List<Map<String, Object>> oldMaps = (List<Map<String, Object>>) param.getValue();
    // // sameValues = oldMaps.size() == newMaps.size();
    // for (int i = 0; i < newMaps.size() && sameValues; i++) {
    // Map<String, Object> newmap = newMaps.get(i);
    // Map<String, Object> oldmap = null; // oldMaps.get(i);
    // if (i < oldMaps.size()) {
    // oldmap = oldMaps.get(i);
    // }
    // // for (int j = 0; j < oldMaps.size(); j++) {
    // // if (oldMaps.get(j).get("SCHEMA_COLUMN").equals(
    // // newmap.get("SCHEMA_COLUMN"))) {
    // // oldmap = oldMaps.get(j);
    // // }
    // // }
    // if (oldmap != null && sameValues) {
    // Object o = newmap.get("QUERY");
    // if (o != null) {
    // sameValues = newmap.get("QUERY").equals(oldmap.get("QUERY"));
    // } else {
    // sameValues = oldmap.get("QUERY") == null;
    // }
    // }
    // }
    // if (oldMaps.size() > newMaps.size()) {
    // int size = newMaps.size();
    // for (int i = size; i < oldMaps.size(); i++) {
    // Map<String, Object> map = new HashMap<String, Object>();
    // map.put("QUERY", "");
    // newMaps.add(map);
    // }
    // sameValues = false;
    // }
    // }
    // }
    // }
    // }
    // if (!sameValues) {
    //
    // result.setResult(MetadataUpdateCheckResult.RepositoryType.property,
    // MetadataUpdateCheckResult.ResultType.change, repositoryConnection);
    //
    // modified = true;
    // } else {
    // for (IElementParameter param : node.getElementParameters()) {
    // String repositoryValue = param.getRepositoryValue();
    // if (param.isShow(node.getElementParameters()) && (repositoryValue != null)
    // && (!param.getName().equals(EParameterName.PROPERTY_TYPE.getName()))) {
    // param.setRepositoryValueUsed(true);
    // }
    // }
    // }
    // } else {
    //
    // result.setResult(MetadataUpdateCheckResult.RepositoryType.property,
    // MetadataUpdateCheckResult.ResultType.delete, null);
    // modified = true;
    // }
    //
    // // add the check result to resultList, hold the value.
    // if (result.getResultType() != null) {
    // resultList.add(result);
    // }
    // }
    // }
    // return modified;
    // }
    /**
     * 
     * DOC nrousseau Comment method "checkDifferenceWithRepository".
     * 
     * @return true if a difference has been detected
     */
    public boolean checkDifferenceWithRepository() {
        return getUpdateManager().updateAll();
        // List<MetadataUpdateCheckResult> resultList = new ArrayList<MetadataUpdateCheckResult>();
        // boolean modified = false;
        // for (Node node : nodes) {
        // if (checkNodeSchemaFromRepository(node, resultList)) {
        // modified = true;
        // }
        // if (checkNodePropertiesFromRepository(node, resultList)) {
        // modified = true;
        // }
        // }
        //
        // // when modified == true, then resultList.size() > 0
        // if (resultList.size() > 0) {
        // MetadataUpdateCheckDialog checkDlg = new MetadataUpdateCheckDialog(PlatformUI.getWorkbench().getDisplay()
        // .getActiveShell(), resultList, Messages.getString("Process.IfToUpgradeMetadata")); //$NON-NLS-1$
        // checkDlg.setTitle(Messages.getString("Process.metadataModificationDetected")); //$NON-NLS-1$
        //
        // checkDlg.setInputElement(resultList);
        // int ret = checkDlg.open();
        // if (ret == IDialogConstants.OK_ID) {
        // List<Object> selectResult = Arrays.asList(checkDlg.getResult());
        //
        // updateNodeswithMetadata(selectResult);
        //
        // modified = true;
        //
        // } else { // IDialogConstants.CANCEL_ID
        // modified = false;
        // }
        // }

        // return modified;
    }

    //
    // private void updateNodeswithMetadata(final List<Object> list) {
    // CompoundCommand cc = new CompoundCommand();
    //
    // for (int k = 0; k < list.size(); k++) {
    //
    // MetadataUpdateCheckResult result = (MetadataUpdateCheckResult) list.get(k);
    //
    // Node node = result.getNode();
    //
    // if (result.getRepositoryType() == MetadataUpdateCheckResult.RepositoryType.property) {
    //
    // if (result.getResultType() == MetadataUpdateCheckResult.ResultType.change) {
    //
    // // upgrade from repository
    // if (result.isChecked()) {
    // for (IElementParameter param : node.getElementParameters()) {
    // String repositoryValue = param.getRepositoryValue();
    // if (param.isShow(node.getElementParameters()) && (repositoryValue != null)
    // && (!param.getName().equals(EParameterName.PROPERTY_TYPE.getName()))) {
    // Object objectValue = RepositoryToComponentProperty.getValue(
    // (org.talend.core.model.metadata.builder.connection.Connection) result.getParameter(),
    // repositoryValue);
    // if (objectValue != null) {
    // if (param.getField().equals(EParameterFieldType.CLOSED_LIST)
    // && param.getRepositoryValue().equals("TYPE")) { //$NON-NLS-1$
    // boolean found = false;
    // String[] items = param.getListRepositoryItems();
    // for (int i = 0; (i < items.length) && (!found); i++) {
    // if (objectValue.equals(items[i])) {
    // found = true;
    // node.setPropertyValue(param.getName(), param.getListItemsValue()[i]);
    // }
    // }
    // } else {
    // node.setPropertyValue(param.getName(), objectValue);
    // }
    // } else if (param.getField().equals(EParameterFieldType.TABLE)
    // && "XML_MAPPING".equals(repositoryValue)) {
    // RepositoryToComponentProperty.getTableXMLMappingValue(
    // (org.talend.core.model.metadata.builder.connection.Connection) result.getParameter(),
    // (List<Map<String, Object>>) param.getValue(), node.getMetadataList().get(0));
    // }
    // param.setRepositoryValueUsed(true);
    // }
    // }
    // } else { // result.isChecked() == false
    // // don't upgrade so set to builtin
    // node.setPropertyValue(EParameterName.PROPERTY_TYPE.getName(), EmfComponent.BUILTIN);
    // for (IElementParameter param : node.getElementParameters()) {
    // String repositoryValue = param.getRepositoryValue();
    // if (param.isShow(node.getElementParameters()) && (repositoryValue != null)) {
    // param.setRepositoryValueUsed(false);
    // }
    // }
    // }
    // } else { // MetadataUpdateCheckResult.ResultType.delete
    // node.setPropertyValue(EParameterName.PROPERTY_TYPE.getName(), EmfComponent.BUILTIN);
    // }
    //
    // } else if (result.getRepositoryType() == MetadataUpdateCheckResult.RepositoryType.schema) {
    //
    // if (result.getResultType() == MetadataUpdateCheckResult.ResultType.change) {
    //
    // if (result.isChecked()) {
    // IMetadataTable newTable = ((IMetadataTable) result.getParameter());
    // // node.getMetadataFromConnector(newTable.getAttachedConnector()).setListColumns(newTable.getListColumns());
    // for (INodeConnector nodeConnector : node.getListConnector()) {
    // if (nodeConnector.getBaseSchema().equals(newTable.getAttachedConnector())) {
    // MetadataTool.copyTable(newTable, node.getMetadataFromConnector(nodeConnector.getName()));
    // }
    // }
    // } else { // result.isChecked()==false
    // node.setPropertyValue(EParameterName.SCHEMA_TYPE.getName(), EmfComponent.BUILTIN);
    // }
    // } else { // MetadataUpdateCheckResult.ResultType.delete
    //
    // node.setPropertyValue(EParameterName.SCHEMA_TYPE.getName(), EmfComponent.BUILTIN);
    //
    // }
    //
    // } else if (result.getRepositoryType() == MetadataUpdateCheckResult.RepositoryType.query) {
    // // here need to add the code the do the "query"
    // }
    // PropertyChangeCommand pcc = new PropertyChangeCommand(node, EParameterName.UPDATE_COMPONENTS.getName(),
    // Boolean.TRUE);
    // cc.add(pcc);
    // }
    //
    // if (!cc.isEmpty()) { // if there is any change, send a command to apply them
    // getCommandStack().execute(cc);
    // }
    // }

    public CommandStack getCommandStack() {
        if (getEditor() != null) {
            Object adapter = getEditor().getTalendEditor().getAdapter(CommandStack.class);
            return (CommandStack) adapter;
        } else {
            return new CommandStack() {

                /*
                 * (non-Javadoc)
                 * 
                 * @see org.eclipse.gef.commands.CommandStack#execute(org.eclipse.gef.commands.Command)
                 */
                @Override
                public void execute(Command command) {
                    command.execute();
                }
            };
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
            // see the feature 6294
            // qli
            if (source == null || target == null) {
                continue;
            }
            Integer lineStyleId = new Integer(cType.getLineStyle());
            String connectorName = cType.getConnectorName();
            boolean connectionTypeFound = false;
            if (connectorName != null) {
                // check if the connector exists and if the line style is
                // correct
                // (used for automatic component upgrade, to avoid migration
                // each time)
                if (source.getConnectorFromName(connectorName) != null
                        && (source.getConnectorFromName(connectorName).getConnectionProperty(
                                EConnectionType.getTypeFromId(lineStyleId)) != null)) {
                    connectionTypeFound = true;
                }
            }

            // fix to correct the bug of the metaname after renaming the output of a tMap
            String metaname = cType.getMetaname();
            if ((source.getComponent().getName().equals("tMap")) && (source.getMetadataTable(metaname) == null)) { //$NON-NLS-1$
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

            boolean monitorConnection = getConnectionMonitorProperty(cType);
            if (connectionTypeFound) {
                connec = new Connection(source, target, EConnectionType.getTypeFromId(lineStyleId), connectorName, metaname,
                        cType.getLabel(), cType.getMetaname(), monitorConnection);
            } else {
                EConnectionType type = EConnectionType.getTypeFromId(lineStyleId);
                connec = new Connection(source, target, type, source.getConnectorFromType(type).getName(), metaname, cType
                        .getLabel(), cType.getMetaname(), monitorConnection);
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
            if (!CommonsPlugin.isHeadless()) {
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

    /**
     * 
     * DOC YeXiaowei Comment method "getConnectionMonitorProperty".
     * 
     * @param type
     * @return
     */
    @SuppressWarnings("unchecked")
    private boolean getConnectionMonitorProperty(ConnectionType type) {
        EList params = type.getElementParameter();
        if (params == null || params.isEmpty()) {
            return false;
        }
        for (int i = 0, size = params.size(); i < size; i++) {
            ElementParameterType param = (ElementParameterType) params.get(i);
            if (param != null) {
                if (param.getName().equals(EParameterName.MONITOR_CONNECTION.getName())) {
                    return Boolean.valueOf(param.getValue());
                }
            }
        }

        return false;
    }

    private void loadContexts(ProcessType process) {
        /**
         * Load the contexts informations
         */
        String defaultContextToLoad;
        defaultContextToLoad = process.getDefaultContext();

        EList contextList = process.getContext();
        if (contextList == null || contextList.isEmpty()) {
            contextManager = new JobContextManager();
        } else {
            contextManager = new JobContextManager(contextList, defaultContextToLoad);
        }
        updateContextBefore(contextManager);
    }

    /**
     * 
     * this method work for the repositoryId existed in process before v2.2.
     * 
     */
    private void updateContextBefore(IContextManager contextManager) {
        if (repositoryId != null && !"".equals(repositoryId)) { //$NON-NLS-1$

            ContextItem item = ContextUtils.getContextItemById(ContextUtils.getAllContextItem(), repositoryId);

            for (IContext context : contextManager.getListContext()) {
                for (IContextParameter param : context.getContextParameterList()) {
                    if (item != null && ContextUtils.updateParameterFromRepository(item, param, context.getName())) {
                        param.setSource(item.getProperty().getLabel());
                    } else {
                        param.setSource(IContextParameter.BUILT_IN);
                    }
                }
            }
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

    @SuppressWarnings("unchecked")
    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;

        for (Node node : nodes) {
            node.setReadOnly(readOnly);
            for (IConnection connec : (List<IConnection>) node.getOutgoingConnections()) {
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
        if (getProperty().getStatusCode() == null && statusCode != null || getProperty().getStatusCode() != null
                && !getProperty().getStatusCode().equals(statusCode)) {
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
            // e.printStackTrace();
            ExceptionHandler.process(e);
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
        // test if name already exist but with ignore case (contains test only with same case)

        if (checkExists && checkIgnoreCase(connectionName)) {
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

        return !KeywordsValidator.isKeyword(connectionName);
    }

    // hshen
    // qli modified to fix the bug "7312".
    public boolean checkIgnoreCase(String connectionName) {
        if (connectionName.equals("")) {//$NON-NLS-1$
            return true;
        }
        if (uniqueConnectionNameList != null) {
            for (String value : uniqueConnectionNameList) {
                if (value.equals(connectionName)) {
                    return true;
                }
            }
        }

        return false;
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
        if (uniqueName != null && !uniqueName.equals("")) { //$NON-NLS-1$
            uniqueNodeNameList.remove(uniqueName);
        }
    }

    @SuppressWarnings("unchecked")
    private void setActivateSubjob(INode node, boolean active, INode activateNode, boolean oneComponent) {
        INode mainSubProcess = node.getSubProcessStartNode(false);

        // if the selected node is the start node, then everything will be
        // desacticated
        if (activateNode.isStart()) {
            for (Connection connec : (List<Connection>) node.getIncomingConnections()) {
                if (connec.getSource().isActivate() != active) {
                    if (connec.getLineStyle().hasConnectionCategory(IConnectionCategory.DATA)
                            || connec.getLineStyle().hasConnectionCategory(IConnectionCategory.USE_ITERATE)) {
                        if (connec.getSource().getSubProcessStartNode(false).isActivate() != active) {
                            setActivateSubjob(connec.getSource().getSubProcessStartNode(false), active, activateNode,
                                    oneComponent);
                        }
                    }
                }
            }
            ((Element) node).setPropertyValue(EParameterName.ACTIVATE.getName(), new Boolean(active));
            for (Connection connec : (List<Connection>) node.getOutgoingConnections()) {
                if (!oneComponent) {
                    if (connec.getTarget().isActivate() != active) {
                        setActivateSubjob(connec.getTarget(), active, activateNode, oneComponent);
                    }
                } else {
                    if (connec.getLineStyle().hasConnectionCategory(IConnectionCategory.DATA)
                            || connec.getLineStyle().hasConnectionCategory(IConnectionCategory.USE_ITERATE)) {
                        if (connec.getTarget().isActivate() != active) {
                            setActivateSubjob(connec.getTarget(), active, activateNode, oneComponent);
                        }
                    }
                }

            }

        } else {
            if (node.getSubProcessStartNode(false).equals(mainSubProcess)) {
                ((Element) node).setPropertyValue(EParameterName.ACTIVATE.getName(), new Boolean(active));
                for (Connection connec : (List<Connection>) node.getIncomingConnections()) {
                    if (connec.getLineStyle().hasConnectionCategory(IConnectionCategory.DATA)
                            || connec.getLineStyle().hasConnectionCategory(IConnectionCategory.USE_ITERATE)) {
                        if (connec.getSource().isActivate() != active) {
                            setActivateSubjob(connec.getSource(), active, activateNode, oneComponent);
                        }
                    }
                }
                for (Connection connec : (List<Connection>) node.getOutgoingConnections()) {
                    if (connec.getLineStyle().hasConnectionCategory(IConnectionCategory.DATA)
                            || connec.getLineStyle().hasConnectionCategory(IConnectionCategory.USE_ITERATE)) {
                        if (connec.getTarget().isActivate() != active) {
                            setActivateSubjob(connec.getTarget(), active, activateNode, oneComponent);
                        }
                    }
                }
            }
            ((Element) node).setPropertyValue(EParameterName.ACTIVATE.getName(), new Boolean(active));
        }
    }

    public void setActivateSubjob(Node node, boolean active, boolean oneComponent) {
        // desactive first the process to avoid to check the process during the
        // activation / desactivation
        setActivate(false);
        setActivateSubjob(node, active, node, oneComponent);
        // now that everything is set, reactivate the process
        setActivate(true);
    }

    public void checkStartNodes() {
        for (Node node : nodes) {
            if ((Boolean) node.getPropertyValue(EParameterName.STARTABLE.getName())) {
                if (node.isActivate()) {
                    boolean checkIfCanBeStart = node.checkIfCanBeStart();
                    node.setStart(checkIfCanBeStart);
                }
            }
        }
        if (!isDuplicate()) {
            ConnectionListController.updateConnectionList(this);
            updateSubjobContainers();
        }
    }

    public int getMergelinkOrder(final INode node) {
        return getMergelinkOrder(node, new HashSet<INode>());
    }

    /**
     * If the node link with the merge node, it will return the merge link order, or it will return -1 Purpose: only in
     * the branch of the first merge link can be as a start node.
     * 
     * @param node
     * @return
     */
    private int getMergelinkOrder(final INode node, final Set<INode> checkedNode) {

        List<? extends IConnection> outgoingConnections = node.getOutgoingConnections();
        int returnValue = -1;
        checkedNode.add(node);
        for (int i = 0; (i < outgoingConnections.size()) && (returnValue == -1); i++) {
            IConnection connec = outgoingConnections.get(i);
            if (connec.isActivate()) {
                if (connec.getLineStyle().hasConnectionCategory(EConnectionType.MERGE)) {
                    returnValue = connec.getInputId();
                    break;
                } else if (connec.getLineStyle().hasConnectionCategory(EConnectionType.MAIN) && connec.getTarget() != null
                        && (!connec.getTarget().equals(node)) && (!checkedNode.contains(connec.getTarget()))) {
                    returnValue = getMergelinkOrder(connec.getTarget(), checkedNode);
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
        Map<INode, Integer> map = new HashMap<INode, Integer>();

        getLinkedMergeInfo(node, map);

        return map;
    }

    private void getLinkedMergeInfo(final INode node, final Map<INode, Integer> map) {
        List<? extends IConnection> outgoingConnections = node.getOutgoingConnections();
        for (int i = 0; (i < outgoingConnections.size()); i++) {
            IConnection connec = outgoingConnections.get(i);
            if (connec.isActivate()) {
                if (connec.getLineStyle().hasConnectionCategory(EConnectionType.MERGE)) {
                    map.put(connec.getTarget(), connec.getInputId());
                    getLinkedMergeInfo(connec.getTarget(), map);
                } else if (connec.getLineStyle().hasConnectionCategory(EConnectionType.MAIN) && connec.getTarget() != null) {
                    getLinkedMergeInfo(connec.getTarget(), map);
                }
            }
        }
    }

    public boolean isThereLinkWithHash(final INode node) {
        return isThereLinkWithHash(node, new HashSet<INode>());
    }

    /**
     * This function check if in this subprocess there should be a start or not depends on the ref links. If in this
     * subprocess there is only one main flow and one ref then this function will return true. If there is several flow
     * in the output of one component in this subprocess,it'll return false.
     * 
     * @param node
     * @return
     */
    public boolean isThereLinkWithHash(final INode node, final Set<INode> checkedNode) {
        boolean refLink = false;

        if (checkedNode.contains(node)) {
            return false;
        }
        checkedNode.add(node);

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
                        refLink = isThereLinkWithHash(connec.getTarget(), checkedNode);
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
        Problems.refreshProcessAllNodesStatus(this);
        Problems.refreshProblemTreeView();
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
        return getProperty().getCreationDate();
    }

    public String getDescription() {
        return getProperty().getDescription();
    }

    public Date getModificationDate() {
        return getProperty().getModificationDate();
    }

    public String getPurpose() {
        return getProperty().getPurpose();
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
                || id.equals(EParameterName.PROPERTY_TYPE.getName())
                // || id.equals(JobSettingsConstants.getExtraParameterName(EParameterName.PROPERTY_TYPE.getName()))
                || id.equals(EParameterName.PROCESS_TYPE_PROCESS.getName())) {
            String updataComponentParamName = null;
            // if (JobSettingsConstants.isExtraParameter(id)) {
            // updataComponentParamName =
            // JobSettingsConstants.getExtraParameterName(EParameterName.UPDATE_COMPONENTS.getName());
            // } else {
            updataComponentParamName = EParameterName.UPDATE_COMPONENTS.getName();
            // }
            setPropertyValue(updataComponentParamName, Boolean.TRUE);
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
        List<INode> generatingNodes = (List<INode>) getGeneratingNodes();
        List<INode> matchingNodes = new ArrayList<INode>();
        for (INode node : generatingNodes) {
            if (node.isActivate()) {
                if (componentName == null) { // means all nodes will be
                    // returned
                    matchingNodes.add(node);
                } else if (componentName.startsWith("FAMILY:")) { //$NON-NLS-1$
                    String familly = componentName.substring("FAMILY:".length()); //$NON-NLS-1$
                    if (node.getComponent().getOriginalFamilyName().startsWith(familly)) {
                        matchingNodes.add(node);
                    }
                } else if (componentName.startsWith("REGEXP:")) { //$NON-NLS-1$
                    Perl5Matcher matcher = new Perl5Matcher();
                    Perl5Compiler compiler = new Perl5Compiler();
                    Pattern pattern;

                    String regexp = componentName.substring("REGEXP:".length()); //$NON-NLS-1$
                    try {
                        pattern = compiler.compile(regexp);
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

        if ((filter != null) && (filter.startsWith("TYPE:"))) { //$NON-NLS-1$
            // construct filter array
            String[] f = filter.substring("TYPE:".length()).split("\\|"); //$NON-NLS-1$ //$NON-NLS-2$
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
        return ProjectManager.getInstance().getCurrentProject();
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
            // e.printStackTrace();
            ExceptionHandler.process(e);
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
        fireStructureChange(NEED_UPDATE_JOB, elem);
    }

    public void removeNote(Note note) {
        elem.remove(note);
        notes.remove(note);
        fireStructureChange(NEED_UPDATE_JOB, elem);
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
        return JavaProcessUtil.getNeededLibraries(this, withChildrens);
    }

    /**
     * Getter for notes.
     * 
     * @return the notes
     */
    public List<Note> getNotes() {
        return notes;
    }

    /**
     * Getter for editor.
     * 
     * @return the editor
     */
    public AbstractMultiPageTalendEditor getEditor() {
        if (this.editor instanceof AbstractMultiPageTalendEditor) {
            return this.editor;
        }
        return null;
    }

    CommandStackEventListener commandStackEventListener = new CommandStackEventListener() {

        public void stackChanged(CommandStackEvent event) {
            processModified = true;
            setNeedRegenerateCode(true);
        }
    };

    private IContext lastRunContext;

    private boolean needRegenerateCode;

    private Map<Node, SubjobContainer> copySubjobMap;

    /**
     * Sets the editor.
     * 
     * @param editor the editor to set
     */
    public void setEditor(AbstractMultiPageTalendEditor editor) {
        this.editor = editor;
        if (editor != null && !duplicate) {
            CommandStack commandStack = (CommandStack) editor.getTalendEditor().getAdapter(CommandStack.class);
            commandStack.addCommandStackEventListener(commandStackEventListener);
            getUpdateManager().updateAll();

        }
    }

    public void dispose() {
        generatingProcess = null;
        editor = null;
        viewer = null;
        ImageUtils.disposeImages(innerContent);
    }

    // public void dispose() {
    // for (Node curNode : nodes) {
    // List<Connection> connList = new ArrayList<Connection>((List<Connection>) curNode.getOutgoingConnections());
    // for (Connection curConnection : connList) {
    // curConnection.disconnect();
    // for (IElementParameter param : curConnection.getElementParametersWithChildrens()) {
    // param.setElement(null);
    // }
    // curConnection.setElementParameters(null);
    // }
    // }
    // for (Node curNode : new ArrayList<Node>(nodes)) {
    // // removeNodeContainer(curNode.getNodeContainer());
    // curNode.getNodeContainer().setSubjobContainer(null);
    // curNode.getNodeContainer().setNode(null);
    // curNode.getNodeContainer().setNodeLabel(null);
    // }
    //
    // for (Node curNode : nodes) {
    // if (curNode.getElementParametersWithChildrens() != null) {
    // for (IElementParameter param : curNode.getElementParametersWithChildrens()) {
    // param.setElement(null);
    // }
    // }
    // curNode.setElementParameters(null);
    // curNode.setProcess(null);
    // }
    // // for (SubjobContainer curSubjob : subjobContainers) {
    // // // curSubjob.dispose();
    // // }
    // if (getElementParametersWithChildrens() != null) {
    // for (IElementParameter param : getElementParametersWithChildrens()) {
    // param.setElement(null);
    // }
    // }
    // if (copySubjobMap != null) {
    // copySubjobMap.clear();
    // }
    // // if (mapSubjobStarts != null) {
    // // mapSubjobStarts.clear();
    // // }
    // setElementParameters(null);
    // // subjobContainers = null;
    // // nodes = null;
    //
    // elem = null;
    // notes = null;
    // processor = null;
    // // don't empty the context manager, or can cause a NPE when close a read only job
    // // contextManager = null;
    // // mapSubjobStarts = null;
    // if (this.editor != null) {
    // CommandStack commandStack = (CommandStack) this.editor.getTalendEditor().getAdapter(CommandStack.class);
    // if (commandStack != null) {
    // commandStack.removeCommandStackEventListener(commandStackEventListener);
    // }
    // }
    // // if ((generatingProcess != null) && (generatingProcess.getDuplicatedProcess() != null)
    // // // && (generatingProcess.getDuplicatedProcess().getGraphicalNodes() != null )
    // // ) {
    // // generatingProcess.getDuplicatedProcess().dispose();
    // // }
    //
    //       
    // }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IProcess2#disableRunJobView()
     */
    public boolean disableRunJobView() {
        return false;
    }

    /**
     * Sets the processModified.
     * 
     * @param processModified the processModified to set
     */
    public void setProcessModified(boolean processModified) {
        this.processModified = processModified;
    }

    /**
     * Sets the contextManager.
     * 
     * @param contextManager the contextManager to set
     */
    public void setContextManager(IContextManager contextManager) {
        this.contextManager = contextManager;
    }

    /**
     * Sets the generatingProcess.
     * 
     * @param generatingProcess the generatingProcess to set
     */
    public void setGeneratingProcess(DataProcess generatingProcess) {
        this.generatingProcess = generatingProcess;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IProcess#getNodesWithImport()
     */
    public List<INode> getNodesWithImport() {
        List<INode> nodesWithImport = new ArrayList<INode>();
        for (INode node : nodes) {
            if (node.getComponent().useImport()) {
                nodesWithImport.add(node);
            }
        }
        return nodesWithImport;
    }

    public void updateSubjobContainers() {
        // check all old subjobStart to see if their status changed (to remove the subjob if needed)
        Set<SubjobContainer> updatedSubjobContainers = new HashSet<SubjobContainer>();
        for (SubjobContainer sjc : new ArrayList<SubjobContainer>(subjobContainers)) {
            Node node = sjc.getSubjobStartNode();
            // if this node is not anymore a subjob start, then set it back to the element list.
            // this one will be reaffected to a new subjob after
            if (node == null || !node.isDesignSubjobStartNode()) {
                elem.addAll(sjc.getNodeContainers());
                sjc.getNodeContainers().clear();
                elem.remove(sjc);
                subjobContainers.remove(sjc);
                // subjob are never removed from the map, so if the user do any "undo"
                // the name of the subjob or configuration will be kept.
            } else {
                for (NodeContainer nodeContainer : new ArrayList<NodeContainer>(sjc.getNodeContainers())) {
                    if (!nodeContainer.getNode().getDesignSubjobStartNode().equals(node)) {
                        sjc.getNodeContainers().remove(nodeContainer);
                        elem.add(nodeContainer);
                        updatedSubjobContainers.add(sjc);
                    }
                }
            }
        }

        // make one loop first for the subjob starts
        // once all the subjobs are created, make another loop for the other nodes
        for (Element element : new ArrayList<Element>(elem)) {
            // if there is any NodeContainer, need to reaffect them to a new subjob
            if (element instanceof NodeContainer) {
                Node node = ((NodeContainer) element).getNode();
                if (node.isDesignSubjobStartNode()) {
                    // if the subjob already exist in the list take it, or if not exist create a new one.
                    SubjobContainer sjc = mapSubjobStarts.get(node);
                    if (sjc == null) {
                        sjc = new SubjobContainer(this);
                        sjc.setSubjobStartNode(node);
                        fillSubjobTitle(node, sjc);
                        mapSubjobStarts.put(node, sjc);
                    }
                    sjc.getNodeContainers().clear();
                    sjc.addNodeContainer(node.getNodeContainer());
                    subjobContainers.add(sjc);
                    updatedSubjobContainers.add(sjc);
                    elem.remove(node.getNodeContainer());
                    elem.add(sjc);
                }
            }
        }

        // if there is any NodeContainer, need to reaffect them to an existing subjob
        for (Element element : new ArrayList<Element>(elem)) {
            if (element instanceof NodeContainer) {
                Node node = ((NodeContainer) element).getNode();
                SubjobContainer sjc = mapSubjobStarts.get(node.getDesignSubjobStartNode());
                if (sjc != null) {
                    sjc.addNodeContainer(node.getNodeContainer());
                    elem.remove(node.getNodeContainer());
                    updatedSubjobContainers.add(sjc);
                }
            }
        }
        fireStructureChange(NEED_UPDATE_JOB, elem);
        // update modified subjobs
        int i = updatedSubjobContainers.size();
        for (SubjobContainer sjc : updatedSubjobContainers) {
            sjc.updateSubjobContainer();
        }

        // at the end, there should be no Node / NodeContainer without SubjobContainer
    }

    /**
     * DOC bqian Comment method "fillSubjobTitle".
     * 
     * @param node
     * @param sjc
     */
    private void fillSubjobTitle(Node node, SubjobContainer sjc) {
        if (copySubjobMap == null) {
            return;
        }
        SubjobContainer original = copySubjobMap.get(node);
        if (original != null) {
            sjc.getElementParameter(EParameterName.COLLAPSED.getName()).setValue(
                    original.getElementParameter(EParameterName.COLLAPSED.getName()).getValue());
            sjc.getElementParameter(EParameterName.SHOW_SUBJOB_TITLE.getName()).setValue(
                    original.getElementParameter(EParameterName.SHOW_SUBJOB_TITLE.getName()).getValue());
            sjc.getElementParameter(EParameterName.SUBJOB_TITLE.getName()).setValue(
                    original.getElementParameter(EParameterName.SUBJOB_TITLE.getName()).getValue());

            sjc.getElementParameter(EParameterName.SUBJOB_TITLE_COLOR.getName()).setValue(
                    original.getElementParameter(EParameterName.SUBJOB_TITLE_COLOR.getName()).getValue());

            sjc.getElementParameter(EParameterName.SUBJOB_COLOR.getName()).setValue(
                    original.getElementParameter(EParameterName.SUBJOB_COLOR.getName()).getValue());
        }
    }

    public List<NodeContainer> getAllNodeContainers() {
        List<NodeContainer> list = new ArrayList<NodeContainer>();
        for (SubjobContainer sjc : subjobContainers) {
            list.addAll(sjc.getNodeContainers());
        }
        return list;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IProcess#getLastRunContext()
     */
    public IContext getLastRunContext() {
        return lastRunContext;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IProcess#setLastRunContext(org.talend.core.model.process.IContext)
     */
    public void setLastRunContext(IContext context) {
        this.lastRunContext = context;

    }

    /**
     * Getter for duplicate.
     * 
     * @return the duplicate
     */
    public boolean isDuplicate() {
        return this.duplicate;
    }

    /**
     * Sets the duplicate.
     * 
     * @param duplicate the duplicate to set
     */
    public void setDuplicate(boolean duplicate) {
        this.duplicate = duplicate;
    }

    /**
     * Getter for subjobContainers.
     * 
     * @return the subjobContainers
     */
    public List<? extends ISubjobContainer> getSubjobContainers() {
        return this.subjobContainers;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IProcess2#getUpdateManager()
     */
    public IUpdateManager getUpdateManager() {
        return this.updateManager;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IProcess2#isNeedRegenerateCode()
     */
    public boolean isNeedRegenerateCode() {
        if (editor == null) {
            // if no editor linked, we just consider same as if there was all the time a modification
            return true;
        }
        return needRegenerateCode;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IProcess2#setNeedRegenerateCode(boolean)
     */
    public void setNeedRegenerateCode(boolean regenerateCode) {
        this.needRegenerateCode = regenerateCode;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.repository.IRepositoryObject#getRepositoryNode()
     */
    public RepositoryNode getRepositoryNode() {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.core.model.repository.IRepositoryObject#setRepositoryNode(org.talend.repository.model.RepositoryNode)
     */
    public void setRepositoryNode(RepositoryNode node) {
        // TODO Auto-generated method stub

    }

    /**
     * <br>
     * see bug 0004882: Subjob title is not copied when copying/pasting subjobs from one job to another
     * 
     * @param mapping
     */
    public void setCopyPasteSubjobMappings(Map<Node, SubjobContainer> mapping) {
        copySubjobMap = mapping;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IProcess#getOutputMetadataTable()
     */

    // this function is create for feature 0006265
    public IMetadataTable getOutputMetadataTable() {
        List<? extends Node> nodes = (List<? extends Node>) this.getGeneratingNodes();
        for (Node node : nodes) {
            String name = node.getComponent().getName();
            if (name.equals("tBufferOutput")) { //$NON-NLS-1$
                return node.getMetadataTable(node.getUniqueName());
            }
        }
        return null;

    }

    public ImageDescriptor getScreenshot() {
        return this.screenshot;
    }

    public void setScreenshot(ImageDescriptor imagedes) {
        this.screenshot = imagedes;
    }

}
