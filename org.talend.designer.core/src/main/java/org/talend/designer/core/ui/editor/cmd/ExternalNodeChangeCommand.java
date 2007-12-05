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
package org.talend.designer.core.ui.editor.cmd;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.gef.commands.Command;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.talend.core.model.components.IODataComponent;
import org.talend.core.model.components.IODataComponentContainer;
import org.talend.core.model.metadata.IMetadataTable;
import org.talend.core.model.metadata.MetadataTool;
import org.talend.core.model.process.EConnectionType;
import org.talend.core.model.process.EParameterFieldType;
import org.talend.core.model.process.IConnection;
import org.talend.core.model.process.IConnectionCategory;
import org.talend.core.model.process.IElementParameter;
import org.talend.core.model.process.IExternalData;
import org.talend.core.model.process.IExternalNode;
import org.talend.core.model.process.INode;
import org.talend.core.model.process.INodeConnector;
import org.talend.designer.core.i18n.Messages;
import org.talend.designer.core.model.components.EParameterName;
import org.talend.designer.core.model.components.EmfComponent;
import org.talend.designer.core.ui.editor.connections.Connection;
import org.talend.designer.core.ui.editor.nodes.Node;
import org.talend.designer.core.ui.editor.process.Process;
import org.talend.designer.core.ui.views.CodeView;

/**
 * Command that will change the datas stored for an external node.
 * 
 * $Id$
 * 
 */
public class ExternalNodeChangeCommand extends Command {

    private Node node;

    private IExternalData oldExternalData;

    private List<IMetadataTable> oldMetaDataList;

    private IExternalData newExternalData;

    private List<IMetadataTable> newMetaDataList;

    private List<Connection> connectionsToDelete;

    private List<ChangeMetadataCommand> metadataOutputChanges = new ArrayList<ChangeMetadataCommand>();

    private Map<Connection, IMetadataTable> metadataInputChanges = new HashMap<Connection, IMetadataTable>();

    private Map<Connection, Boolean> metadataInputWasRepository = new HashMap<Connection, Boolean>();

    private IODataComponentContainer inAndOut;

    private Boolean propagate;

    @SuppressWarnings("unchecked")//$NON-NLS-1$
    public ExternalNodeChangeCommand(Node node, IExternalNode externalNode) {
        this.node = node;

        this.inAndOut = externalNode.getIODataComponents();

        oldExternalData = node.getExternalData();
        oldMetaDataList = node.getMetadataList();

        newExternalData = externalNode.getExternalData();
        newMetaDataList = externalNode.getMetadataList();

        connectionsToDelete = new ArrayList<Connection>();

        for (IODataComponent dataComponent : (List<IODataComponent>) inAndOut.getOuputs()) {
            IConnection connection = dataComponent.getConnection();
            boolean metadataExists = false;
            for (IMetadataTable metadata : newMetaDataList) {
                if (connection.getMetadataTable().getTableName().equals(metadata.getTableName())) {
                    metadataExists = true;
                }
            }
            if (!metadataExists && (connection instanceof Connection)) {
                connectionsToDelete.add((Connection) connection);
            }
        }

        for (Connection connection : (List<Connection>) node.getIncomingConnections()) {
            String schemaType = (String) connection.getSource().getPropertyValue(EParameterName.SCHEMA_TYPE.getName());
            if (schemaType != null) {
                if (schemaType.equals(EmfComponent.REPOSITORY)) {
                    String metaRepositoryName = (String) connection.getSource().getPropertyValue(
                            EParameterName.REPOSITORY_SCHEMA_TYPE.getName());
                    IMetadataTable repositoryMetadata = MetadataTool.getMetadataFromRepository(metaRepositoryName);
                    if (repositoryMetadata == null) {
                        connection.getSource().setPropertyValue(EParameterName.SCHEMA_TYPE.getName(), EmfComponent.BUILTIN);
                    } else {
                        repositoryMetadata = repositoryMetadata.clone();
                        repositoryMetadata.setTableName(connection.getSource().getUniqueName());
                        if (!repositoryMetadata.sameMetadataAs(connection.getMetadataTable())) {
                            connection.getSource().setPropertyValue(EParameterName.SCHEMA_TYPE.getName(), EmfComponent.BUILTIN);
                        }
                    }
                }
            }
        }

        setLabel(Messages.getString("ExternalNodeChangeCommand.modifaicationFrom") + node.getUniqueName()); //$NON-NLS-1$
    }

    private void refreshCodeView() {
        IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
        IViewPart view = page.findView(CodeView.ID);
        if (view != null) {
            CodeView codeView = (CodeView) view;
            codeView.refresh();
        }
    }

    private boolean getPropagate() {
        if (propagate == null) {
            propagate = MessageDialog
                    .openQuestion(
                            new Shell(),
                            Messages.getString("ExternalNodeChangeCommand.propagate"), Messages.getString("ExternalNodeChangeCommand.propagateMessage")); //$NON-NLS-1$ //$NON-NLS-2$
        }
        return propagate;
    }

    private void propagateInput() {
        for (Connection connection : (List<Connection>) node.getIncomingConnections()) {
            if (connection.getLineStyle().hasConnectionCategory(IConnectionCategory.DATA)) {
                IODataComponent currentIO = inAndOut.getDataComponent(connection);
                if (currentIO.hasChanged()) {
                    IMetadataTable metadata = inAndOut.getTable(connection);
                    INode sourceNode = currentIO.getSource();
                    sourceNode.metadataOutputChanged(currentIO, currentIO.getName());
                    IMetadataTable oldMetadata = connection.getMetadataTable().clone();
                    currentIO.setTable(oldMetadata);
                    String schemaType = (String) connection.getSource().getPropertyValue(EParameterName.SCHEMA_TYPE.getName());
                    if (schemaType != null) {
                        // if there is a SCHEMA_TYPE, then switch it to BUILT_IN if REPOSITORY is set.
                        if (schemaType.equals(EmfComponent.REPOSITORY)) {
                            connection.getSource().setPropertyValue(EParameterName.SCHEMA_TYPE.getName(), EmfComponent.BUILTIN);
                            metadataInputWasRepository.put(connection, Boolean.TRUE);
                        }
                    }
                    connection.getMetadataTable().setListColumns(metadata.getListColumns());
                }
            }
        }
    }

    @Override
    public void execute() {
        propagateInput();

        metadataOutputChanges.clear();
        for (IConnection connection : node.getOutgoingConnections()) {
            if (connection.getLineStyle().hasConnectionCategory(IConnectionCategory.DATA)) {
                IODataComponent dataComponent = inAndOut.getDataComponent(connection);
                if (!connection.getMetadataTable().sameMetadataAs(dataComponent.getTable())) {
                    if (getPropagate()) {
                        IElementParameter schemaParam = null;
                        for (IElementParameter param : ((Node) connection.getSource()).getElementParameters()) {
                            if (param.getField().equals(EParameterFieldType.SCHEMA_TYPE)
                                    && param.getContext().equals(connection.getConnectorName())) {
                                schemaParam = param;
                            }
                        }
                        ChangeMetadataCommand cmd = new ChangeMetadataCommand((Node) connection.getSource(), schemaParam,
                                connection.getMetadataTable(), dataComponent.getTable());
                        cmd.execute(true);
                        metadataOutputChanges.add(cmd);
                    }
                }
                if (connection instanceof Connection) {
                    ((Connection) connection).updateName();
                }
            }
        }
        if (!oldMetaDataList.isEmpty() && !newMetaDataList.isEmpty()
                && !oldMetaDataList.get(0).sameMetadataAs(newMetaDataList.get(0))) {
            node.setPropertyValue(EParameterName.SCHEMA_TYPE.getName(), EmfComponent.BUILTIN);
        }
        node.setExternalData(newExternalData);
        node.setMetadataList(newMetaDataList);
        for (Connection connection : connectionsToDelete) {
            connection.disconnect();
            Node prevNode = connection.getSource();
            INodeConnector nodeConnectorSource, nodeConnectorTarget;
            nodeConnectorSource = prevNode.getConnectorFromType(connection.getLineStyle());
            nodeConnectorSource.setCurLinkNbOutput(nodeConnectorSource.getCurLinkNbOutput() - 1);

            Node nextNode = connection.getTarget();
            nodeConnectorTarget = nextNode.getConnectorFromType(connection.getLineStyle());
            nodeConnectorTarget.setCurLinkNbInput(nodeConnectorTarget.getCurLinkNbInput() - 1);
        }
        ((Process) node.getProcess()).checkProcess();
        refreshCodeView();
    }

    @Override
    public void undo() {
        for (Connection connection : (List<Connection>) node.getIncomingConnections()) {
            if (connection.getLineStyle().equals(EConnectionType.FLOW_MAIN)
                    || connection.getLineStyle().equals(EConnectionType.FLOW_REF)
                    || connection.getLineStyle().equals(EConnectionType.TABLE)) {
                IODataComponent currentIO = inAndOut.getDataComponent(connection);
                if (currentIO.hasChanged()) {
                    IMetadataTable metadata = inAndOut.getTable(connection);
                    INode sourceNode = currentIO.getSource();
                    sourceNode.metadataOutputChanged(currentIO, currentIO.getName());
                    IMetadataTable oldMetadata = connection.getMetadataTable().clone();
                    currentIO.setTable(oldMetadata);
                    connection.getMetadataTable().setListColumns(metadata.getListColumns());
                    if (metadataInputWasRepository.get(connection) != null) {
                        connection.getSource().setPropertyValue(EParameterName.SCHEMA_TYPE.getName(), EmfComponent.REPOSITORY);
                    }
                }
            }
        }
        metadataInputWasRepository.clear();
        node.setExternalData(oldExternalData);
        node.setMetadataList(oldMetaDataList);
        for (Connection connection : connectionsToDelete) {
            connection.reconnect();
            Node prevNode = (Node) connection.getSource();
            INodeConnector nodeConnectorSource, nodeConnectorTarget;
            nodeConnectorSource = prevNode.getConnectorFromType(connection.getLineStyle());
            nodeConnectorSource.setCurLinkNbOutput(nodeConnectorSource.getCurLinkNbOutput() + 1);

            Node nextNode = (Node) connection.getTarget();
            nodeConnectorTarget = nextNode.getConnectorFromType(connection.getLineStyle());
            nodeConnectorTarget.setCurLinkNbInput(nodeConnectorTarget.getCurLinkNbInput() + 1);
        }
        for (ChangeMetadataCommand cmd : metadataOutputChanges) {
            cmd.undo();
        }
        ((Process) node.getProcess()).checkProcess();
        refreshCodeView();
    }
}
