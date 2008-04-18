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
package org.talend.designer.core.ui.editor.update.cmd;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.eclipse.gef.commands.Command;
import org.talend.core.model.components.ComponentUtilities;
import org.talend.core.model.components.EComponentType;
import org.talend.core.model.components.IComponent;
import org.talend.core.model.metadata.IMetadataColumn;
import org.talend.core.model.metadata.IMetadataTable;
import org.talend.core.model.metadata.MetadataTool;
import org.talend.core.model.process.EParameterFieldType;
import org.talend.core.model.process.IConnection;
import org.talend.core.model.process.IElementParameter;
import org.talend.core.model.process.INode;
import org.talend.core.model.update.UpdateResult;
import org.talend.designer.core.model.components.EParameterName;
import org.talend.designer.core.ui.editor.cmd.ChangeMetadataCommand;
import org.talend.designer.core.ui.editor.nodes.Node;
import org.talend.designer.core.ui.editor.process.Process;
import org.talend.designer.core.ui.editor.update.UpdateManagerUtils;
import org.talend.repository.model.ComponentsFactoryProvider;

/**
 * ggu class global comment. Detailled comment
 * 
 * this function is moved from the class AbstractTalendEditor.
 */
public class UpdateJobletNodeCommand extends Command {

    private UpdateResult result;

    public UpdateJobletNodeCommand(UpdateResult result) {
        super();
        this.result = result;
    }

    /**
     * this function is moved from the method updateGraphicalNodes.
     */
    @SuppressWarnings("unchecked")//$NON-NLS-1$
    @Override
    public void execute() {
        Object job = result.getJob();
        if (job == null) {
            return;
        }
        if (job instanceof Process) {
            Process process = (Process) job;
            Object parameter = result.getParameter();
            if (!(parameter instanceof PropertyChangeEvent)) {
                return;
            }
            PropertyChangeEvent evt = (PropertyChangeEvent) parameter;

            String propertyName = evt.getPropertyName();
            Object updateObject = result.getUpdateObject();

            List<INode> jobletNodes = null;
            if (updateObject instanceof List) {
                jobletNodes = (List<INode>) updateObject;
            }

            if (propertyName.equals(ComponentUtilities.NORMAL)) {
                for (Node node : (List<Node>) process.getGraphicalNodes()) {
                    /*
                     * if jobletNodes==null, will reload all component. Or, olny reload the fixed node.
                     */
                    if (jobletNodes != null && !jobletNodes.contains(node)) {
                        continue;
                    }

                    IComponent newComponent = ComponentsFactoryProvider.getInstance().get(node.getComponent().getName());
                    if (newComponent == null) {
                        continue;
                    }
                    Map<String, Object> parameters = new HashMap<String, Object>();

                    if (node.getComponent().getComponentType() != EComponentType.JOBLET) {
                        if (node.getExternalData() != null) {
                            parameters.put(INode.RELOAD_PARAMETER_EXTERNAL_BYTES_DATA, node.getExternalBytesData());
                        }
                        parameters.put(INode.RELOAD_PARAMETER_METADATA_LIST, node.getMetadataList());
                        parameters.put(INode.RELOAD_PARAMETER_ELEMENT_PARAMETERS, node.getElementParameters());
                    } else {
                        parameters.put(INode.RELOAD_PARAMETER_ELEMENT_PARAMETERS, node.getElementParameters());
                    }

                    parameters.put(INode.RELOAD_PARAMETER_CONNECTORS, node.getListConnector());

                    node.reloadComponent(newComponent, parameters);
                }

                for (Node node : (List<Node>) process.getGraphicalNodes()) {
                    node.checkAndRefreshNode();
                }

            } else if (propertyName.equals(ComponentUtilities.JOBLET_NAME_CHANGED)) {
                String oldName = (String) evt.getOldValue();
                String newName = (String) evt.getNewValue();
                IComponent newComponent = ComponentsFactoryProvider.getInstance().get(newName);
                if (newComponent == null) {
                    return;
                }

                for (Node node : (List<Node>) process.getGraphicalNodes()) {
                    if (node.getComponent().getName().equals(oldName) || node.getLabel().contains(oldName)) {
                        Map<String, Object> parameters = new HashMap<String, Object>();
                        node.reloadComponent(newComponent, parameters);

                    }
                }

            } else if (propertyName.equals(ComponentUtilities.JOBLET_SCHEMA_CHANGED)) {
                // updateGraphicalNodesSchema(process, evt);
                INode sourceNode = (INode) evt.getSource();
                String componentName = sourceNode.getComponent().getName();
                IComponent newComponent = ComponentsFactoryProvider.getInstance().get(componentName);
                if (newComponent == null) {
                    return;
                }
                List<Node> nodesToUpdate = new ArrayList<Node>();
                for (Node node : (List<Node>) process.getGraphicalNodes()) {
                    if (node.getComponent().getName().equals(componentName)) {
                        nodesToUpdate.add(node);
                    }
                }
                for (Node node : nodesToUpdate) {
                    Map<String, Object> parameters = new HashMap<String, Object>();

                    // if (node.getComponent().getComponentType() != EComponentType.JOBLET) {
                    // parameters.put(INode.RELOAD_PARAMETER_METADATA_LIST, node.getMetadataList());
                    parameters.put(INode.RELOAD_PARAMETER_ELEMENT_PARAMETERS, node.getElementParameters());
                    // } else {
                    // parameters.put(INode.RELOAD_NEW, true);
                    // }

                    parameters.put(INode.RELOAD_PARAMETER_CONNECTORS, node.getListConnector());

                    node.reloadComponent(newComponent, parameters);
                }

                ((Process) sourceNode.getProcess()).checkProcess();
            }
        }
    }

    /**
     * qzhang Comment method "updateGraphicalNodesSchema".
     * 
     * this method is moved from class AbstractTalendEditor.
     * 
     * @param evt
     */
    @SuppressWarnings("unchecked")//$NON-NLS-1$
    private void updateGraphicalNodesSchema(Process process, PropertyChangeEvent evt) {
        if (!(evt.getSource() instanceof INode)) {
            return;
        }
        INode sourceNode = (INode) evt.getSource();

        String componentName = sourceNode.getComponent().getName();
        IComponent newComponent = ComponentsFactoryProvider.getInstance().get(componentName);
        if (newComponent == null) {
            return;
        }
        Object[] newMetadataTables = (Object[]) evt.getNewValue();
        List<IMetadataTable> newInputTableList = (List<IMetadataTable>) newMetadataTables[0];

        List<IMetadataTable> newOutputTableList = (List<IMetadataTable>) newMetadataTables[1];

        for (Node node : (List<Node>) process.getGraphicalNodes()) {
            if (node.getComponent().getName().equals(componentName)) {
                List<IElementParameter> outputElemParams = new ArrayList<IElementParameter>();

                IElementParameter outputElemParam = null;

                List<? extends IElementParameter> elementParameters = node.getElementParameters();
                for (IElementParameter elementParameter : elementParameters) {
                    if (EParameterFieldType.SCHEMA_TYPE.equals(elementParameter.getField())) {
                        outputElemParams.add(elementParameter);
                    }
                }
                ChangeMetadataCommand command;
                List<? extends IConnection> incomingConnections = node.getIncomingConnections();
                if (incomingConnections.size() <= 1) {
                    for (int i = 0; i < incomingConnections.size(); i++) {
                        IConnection connection = incomingConnections.get(i);
                        Node source = (Node) connection.getSource();
                        IMetadataTable metadataTable = connection.getMetadataTable();
                        IMetadataTable newInputMetadataTable = UpdateManagerUtils.getNewInputTableForConnection(
                                newInputTableList, metadataTable.getAttachedConnector());
                        if (newInputMetadataTable != null && !metadataTable.sameMetadataAs(newInputMetadataTable)) {
                            IElementParameter elementParam = source.getElementParameterFromField(EParameterFieldType.SCHEMA_TYPE);
                            command = new ChangeMetadataCommand(source, elementParam, metadataTable, newInputMetadataTable);
                            command.execute(Boolean.FALSE);
                        }
                    }
                } else {
                    for (IElementParameter param : node.getElementParameters()) {
                        if (param.isShow(node.getElementParameters()) && param.getField().equals(EParameterFieldType.SCHEMA_TYPE)) {
                            IMetadataTable table = node.getMetadataFromConnector(param.getContext());
                            IElementParameter connParam = param.getChildParameters().get(EParameterName.CONNECTION.getName());
                            if (table != null && connParam != null && !StringUtils.isEmpty((String) connParam.getValue())) {
                                for (IConnection connection : incomingConnections) {
                                    if (connection.isActivate() && connection.getName().equals(connParam.getValue())) {
                                        if (!table.sameMetadataAs(connection.getMetadataTable(),
                                                IMetadataColumn.OPTIONS_IGNORE_KEY | IMetadataColumn.OPTIONS_IGNORE_NULLABLE
                                                        | IMetadataColumn.OPTIONS_IGNORE_COMMENT
                                                        | IMetadataColumn.OPTIONS_IGNORE_PATTERN
                                                        | IMetadataColumn.OPTIONS_IGNORE_DBCOLUMNNAME
                                                        | IMetadataColumn.OPTIONS_IGNORE_DBTYPE
                                                        | IMetadataColumn.OPTIONS_IGNORE_DEFAULT
                                                        | IMetadataColumn.OPTIONS_IGNORE_BIGGER_SIZE)) {
                                            Node source = (Node) connection.getSource();
                                            IMetadataTable metadataTable = connection.getMetadataTable();
                                            IElementParameter elementParam = source
                                                    .getElementParameterFromField(EParameterFieldType.SCHEMA_TYPE);
                                            command = new ChangeMetadataCommand(source, elementParam, metadataTable, table);
                                            command.execute(Boolean.FALSE);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                List<? extends IConnection> outgoingConnections = node.getOutgoingConnections();
                for (int i = 0; i < outgoingConnections.size(); i++) {
                    IConnection connection = outgoingConnections.get(i);
                    Node target = (Node) connection.getTarget();
                    IMetadataTable metadataTable = connection.getMetadataTable();
                    if (metadataTable != null) {
                        IMetadataTable newOutputMetadataTable = UpdateManagerUtils.getNewOutputTableForConnection(
                                newOutputTableList, metadataTable.getAttachedConnector());
                        if (newOutputMetadataTable != null && !metadataTable.sameMetadataAs(newOutputMetadataTable)) {
                            IElementParameter elementParam = target.getElementParameterFromField(EParameterFieldType.SCHEMA_TYPE);
                            command = new ChangeMetadataCommand(target, elementParam, target
                                    .getMetadataFromConnector(metadataTable.getAttachedConnector()), newOutputMetadataTable);
                            command.execute(Boolean.FALSE);
                        }
                    }
                }

                List<IMetadataTable> metadataList = node.getMetadataList();
                for (IMetadataTable metadataTable : metadataList) {
                    IMetadataTable newInputMetadataTable = UpdateManagerUtils.getNewInputTableForConnection(newInputTableList,
                            metadataTable.getAttachedConnector());
                    IMetadataTable newOutputMetadataTable = UpdateManagerUtils.getNewOutputTableForConnection(newOutputTableList,
                            metadataTable.getAttachedConnector());
                    outputElemParam = UpdateManagerUtils.getElemParam(outputElemParams, metadataTable.getAttachedConnector());

                    if (outputElemParam != null && newInputMetadataTable != null) {
                        command = new ChangeMetadataCommand(node, outputElemParam, (IMetadataTable) outputElemParam.getValue(),
                                newInputMetadataTable);
                        command.execute(Boolean.FALSE);
                        IMetadataTable metadataFromConnector = node.getMetadataFromConnector(outputElemParam.getContext());
                        MetadataTool.copyTable(newInputMetadataTable, metadataFromConnector);
                    } else if (outputElemParam != null && newOutputMetadataTable != null) {
                        command = new ChangeMetadataCommand(node, outputElemParam, (IMetadataTable) outputElemParam.getValue(),
                                newOutputMetadataTable);
                        command.execute(Boolean.FALSE);
                        IMetadataTable metadataFromConnector = node.getMetadataFromConnector(outputElemParam.getContext());
                        MetadataTool.copyTable(newOutputMetadataTable, metadataFromConnector);
                    }
                }
            }
        }
    }

}
