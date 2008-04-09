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

import org.eclipse.gef.commands.Command;
import org.talend.core.model.components.ComponentUtilities;
import org.talend.core.model.components.EComponentType;
import org.talend.core.model.components.IComponent;
import org.talend.core.model.metadata.IMetadataTable;
import org.talend.core.model.metadata.MetadataTool;
import org.talend.core.model.process.EParameterFieldType;
import org.talend.core.model.process.IConnection;
import org.talend.core.model.process.IElementParameter;
import org.talend.core.model.process.INode;
import org.talend.designer.core.ui.editor.cmd.ChangeMetadataCommand;
import org.talend.designer.core.ui.editor.nodes.Node;
import org.talend.designer.core.ui.editor.process.Process;
import org.talend.designer.core.ui.editor.update.UpdateManagerHelper;

/**
 * ggu class global comment. Detailled comment
 * 
 * this function is moved from the class AbstractTalendEditor.
 */
public class UpdateJobletNodeCommand extends Command {

    private Process process;

    private PropertyChangeEvent evt;

    public UpdateJobletNodeCommand(Process process, PropertyChangeEvent evt) {
        super();
        this.process = process;
        this.evt = evt;
    }

    /**
     * this function is moved from the method updateGraphicalNodes.
     */
    @SuppressWarnings("unchecked")//$NON-NLS-1$
    @Override
    public void execute() {
        if (process == null || evt == null) {
            return;
        }
        String propertyName = evt.getPropertyName();

        if (propertyName.equals(ComponentUtilities.NORMAL)) {
            for (Node node : (List<Node>) process.getGraphicalNodes()) {
                IComponent newComponent = UpdateManagerHelper.getComponent(process, node.getComponent().getName());
                if (newComponent == null) {
                    continue;
                }
                Map<String, Object> parameters = new HashMap<String, Object>();

                if (node.getComponent().getComponentType() != EComponentType.JOBLET) {
                    if (node.getExternalData() != null) {
                        parameters.put(INode.RELOAD_PARAMETER_EXTERNAL_BYTES_DATA, node.getExternalBytesData());
                    }
                    parameters.put(INode.RELOAD_PARAMETER_METADATA_LIST, node.getMetadataList());
                    parameters.put(INode.RELAOD_PARAMETER_ELEMENT_PARAMETERS, node.getElementParameters());
                } else {
                    parameters.put(INode.RELOAD_NEW, true);
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

            for (Node node : (List<Node>) process.getGraphicalNodes()) {
                if (node.getComponent().getName().equals(oldName) || node.getLabel().contains(oldName)) {

                    IComponent newComponent = UpdateManagerHelper.getComponent(process, newName);
                    if (newComponent == null) {
                        continue;
                    }
                    Map<String, Object> parameters = new HashMap<String, Object>();
                    node.reloadComponent(newComponent, parameters);

                }
            }

        } else if (propertyName.equals(ComponentUtilities.JOBLET_SCHEMA_CHANGED)) {
            updateGraphicalNodesSchema(process, evt);
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
        Object[] newMetadataTables = (Object[]) evt.getNewValue();
        List<IMetadataTable> newInputTableList = (List<IMetadataTable>) newMetadataTables[0];

        List<IMetadataTable> newOutputTableList = (List<IMetadataTable>) newMetadataTables[1];

        for (Node node : (List<Node>) process.getGraphicalNodes()) {
            if (node.getComponent().getName().equals(componentName)) {
                IComponent newComponent = UpdateManagerHelper.getComponent(process, componentName);
                if (newComponent == null) {
                    continue;
                }
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
                for (int i = 0; i < incomingConnections.size(); i++) {
                    IConnection connection = incomingConnections.get(i);
                    Node source = (Node) connection.getSource();
                    IMetadataTable metadataTable = connection.getMetadataTable();
                    IMetadataTable newInputMetadataTable = UpdateManagerHelper.getNewInputTableForConnection(newInputTableList,
                            metadataTable.getAttachedConnector());
                    if (newInputMetadataTable != null && !metadataTable.sameMetadataAs(newInputMetadataTable)) {
                        IElementParameter elementParam = source.getElementParameterFromField(EParameterFieldType.SCHEMA_TYPE);
                        command = new ChangeMetadataCommand(source, elementParam, metadataTable, newInputMetadataTable);
                        command.execute(Boolean.FALSE);
                    }
                }
                List<? extends IConnection> outgoingConnections = node.getOutgoingConnections();
                for (int i = 0; i < outgoingConnections.size(); i++) {
                    IConnection connection = outgoingConnections.get(i);
                    Node target = (Node) connection.getTarget();
                    IMetadataTable metadataTable = connection.getMetadataTable();
                    if (metadataTable != null) {
                        IMetadataTable newOutputMetadataTable = UpdateManagerHelper.getNewOutputTableForConnection(
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
                    IMetadataTable newInputMetadataTable = UpdateManagerHelper.getNewInputTableForConnection(newInputTableList,
                            metadataTable.getAttachedConnector());
                    IMetadataTable newOutputMetadataTable = UpdateManagerHelper.getNewOutputTableForConnection(
                            newOutputTableList, metadataTable.getAttachedConnector());
                    outputElemParam = UpdateManagerHelper.getElemParam(outputElemParams, metadataTable.getAttachedConnector());

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
