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
package org.talend.designer.core.ui.editor.cmd;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.gef.commands.Command;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;
import org.talend.core.model.components.IODataComponent;
import org.talend.core.model.components.IODataComponentContainer;
import org.talend.core.model.metadata.IMetadataTable;
import org.talend.core.model.process.EConnectionType;
import org.talend.core.model.process.IConnection;
import org.talend.core.model.process.INode;
import org.talend.designer.core.model.components.EParameterName;
import org.talend.designer.core.model.components.EmfComponent;
import org.talend.designer.core.ui.editor.connections.Connection;
import org.talend.designer.core.ui.editor.nodes.Node;
import org.talend.designer.core.ui.editor.process.Process;

/**
 * Command that will change a metadata in a node.
 * 
 * $Id$
 * 
 */
public class ChangeMetadataCommand extends Command {

    private Node node, inputNode;

    private IMetadataTable currentOutputMetadata, newOutputMetadata, oldOutputMetadata;

    private boolean outputWasRepository = false, inputWasRepository = false;

    private IMetadataTable currentInputMetadata, newInputMetadata, oldInputMetadata;

    private IODataComponentContainer dataContainer;

    private IODataComponent dataComponent;

    private Boolean propagate;

    private List<ChangeMetadataCommand> propagatedChange = new ArrayList<ChangeMetadataCommand>();

    private boolean internal = false;
    
    private boolean repositoryMode = false;

    public ChangeMetadataCommand(Node node, Node inputNode, IMetadataTable currentInputMetadata,
            IMetadataTable newInputMetadata, IMetadataTable currentOutputMetadata, IMetadataTable newOutputMetadata) {
        this.node = node;
        this.inputNode = inputNode;
        this.currentInputMetadata = currentInputMetadata;
        if (currentInputMetadata != null) {
            oldInputMetadata = currentInputMetadata.clone();
        } else {
            oldInputMetadata = null;
        }
        this.newInputMetadata = newInputMetadata;
        this.currentOutputMetadata = currentOutputMetadata;
        if (currentOutputMetadata == null) {
            currentOutputMetadata = node.getMetadataList().get(0);
        }
        oldOutputMetadata = currentOutputMetadata.clone();
        this.newOutputMetadata = newOutputMetadata;
        initializeContainer();
        setLabel("Change Metadata values");
    }

    public ChangeMetadataCommand(Node node, IMetadataTable currentOutputMetadata, IMetadataTable newOutputMetadata) {
        this.node = node;
        this.inputNode = null;
        this.currentInputMetadata = null;
        this.newInputMetadata = null;
        oldInputMetadata = null;
        if (currentOutputMetadata == null) {
            currentOutputMetadata = node.getMetadataList().get(0);
        }
        this.currentOutputMetadata = currentOutputMetadata;
        oldOutputMetadata = currentOutputMetadata.clone();
        this.newOutputMetadata = newOutputMetadata;
        initializeContainer();
        setLabel("Change Metadata values");
    }
    
    protected void setRepositoryMode(boolean repositoryMode) {
        this.repositoryMode = repositoryMode;
    }

    private void initializeContainer() {
        dataContainer = new IODataComponentContainer();
        for (Connection connec : (List<Connection>) node.getIncomingConnections()) {
            if (connec.isActivate() && connec.getLineStyle().equals(EConnectionType.FLOW_MAIN)) {
                IODataComponent input = new IODataComponent(connec);
                dataContainer.getInputs().add(input);
            }
        }
        for (Connection connec : (List<Connection>) node.getOutgoingConnections()) {
            if (connec.isActivate()
                    && (connec.getLineStyle().equals(EConnectionType.FLOW_MAIN) || connec.getLineStyle().equals(
                            EConnectionType.FLOW_REF))) {
                IODataComponent output = new IODataComponent(connec, newOutputMetadata);
                dataContainer.getOuputs().add(output);
            }
        }
    }

    private void setInternal(boolean internal) {
        this.internal = internal;
    }

    private boolean getPropagate(Boolean returnIfNull) {
        if (propagate == null) {
            if (returnIfNull != null) {
                return returnIfNull;
            }
            propagate = MessageDialog.openQuestion(new Shell(), "Propagate", "Would you like to propagate changes ?");
        }
        return propagate;
    }

    private boolean getPropagate() {
        return getPropagate(null);
    }

    public void execute(Boolean propagateP) {
        this.propagate = propagateP;
        if (currentOutputMetadata == null) {
            currentOutputMetadata = node.getMetadataList().get(0);
        }
        execute();
    }

    private void propagateDatas(boolean isExecute) {
        // Propagate :
        if (dataContainer != null && (!dataContainer.getInputs().isEmpty() || !dataContainer.getOuputs().isEmpty())) {
            for (IODataComponent currentIO : dataContainer.getInputs()) {
                if (currentIO.hasChanged()) {
                    if (getPropagate()) {
                        currentIO.getSource().metadataOutputChanged(currentIO, currentIO.getName());
                    }
                }
            }
            for (IODataComponent currentIO : dataContainer.getOuputs()) {
                if (currentIO.hasChanged()) {
                    if (getPropagate()) {
                        INode targetNode = currentIO.getTarget();
                        targetNode.metadataInputChanged(currentIO, currentIO.getName());
                        if (isExecute) {
                            if (targetNode instanceof Node) {
                                if (!((Node) targetNode).isExternalNode()
                                        && ((Node) targetNode).getComponent().isSchemaAutoPropagated()) {
                                    ChangeMetadataCommand cmd = new ChangeMetadataCommand((Node) targetNode, null,
                                            newOutputMetadata);
                                    cmd.setInternal(true);
                                    cmd.execute(true);
                                    propagatedChange.add(cmd);
                                }
                            }
                            currentIO.setTable(oldOutputMetadata);
                        } else {
                            currentIO.setTable(newOutputMetadata);
                        }
                    }
                }
            }
        } else if (dataComponent != null) {
            for (IConnection outgoingConnection : node.getOutgoingConnections()) {
                outgoingConnection.getTarget().metadataInputChanged(dataComponent, outgoingConnection.getName());
            }
        }
        // End propagate
    }

    @Override
    public void execute() {
        propagatedChange.clear();

        propagateDatas(true);

        if (currentInputMetadata != null) {
            if (!currentInputMetadata.sameMetadataAs(newInputMetadata)) {
                currentInputMetadata.setListColumns(newInputMetadata.getListColumns());
                String type = (String) inputNode.getPropertyValue(EParameterName.SCHEMA_TYPE.getName());
                if (type != null) {
                    if (type.equals(EmfComponent.REPOSITORY)) {
                        inputWasRepository = true;
                        inputNode.setPropertyValue(EParameterName.SCHEMA_TYPE.getName(), EmfComponent.BUILTIN);
                    }
                }
            }
        }

        if (!currentOutputMetadata.sameMetadataAs(newOutputMetadata)) {
            currentOutputMetadata.setListColumns(newOutputMetadata.getListColumns());

            String type = (String) node.getPropertyValue(EParameterName.SCHEMA_TYPE.getName());
            if (type != null && type.equals(EmfComponent.REPOSITORY) && !repositoryMode) {
                outputWasRepository = true;
                node.setPropertyValue(EParameterName.SCHEMA_TYPE.getName(), EmfComponent.BUILTIN);
            }
        }
        if (!internal) {
            ((Process) node.getProcess()).checkProcess();
        }
    }

    @Override
    public void undo() {
        propagateDatas(false);

        if (currentInputMetadata != null) {
            if (!currentInputMetadata.sameMetadataAs(oldInputMetadata)) {
                currentInputMetadata.setListColumns(oldInputMetadata.getListColumns());
                if (inputWasRepository) {
                    inputNode.setPropertyValue(EParameterName.SCHEMA_TYPE.getName(), EmfComponent.REPOSITORY);
                }
            }
        }
        if (!currentOutputMetadata.sameMetadataAs(oldOutputMetadata)) {
            currentOutputMetadata.setListColumns(oldOutputMetadata.getListColumns());
        }
        if (outputWasRepository) {
            node.setPropertyValue(EParameterName.SCHEMA_TYPE.getName(), EmfComponent.REPOSITORY);
        }
        for (ChangeMetadataCommand cmd : propagatedChange) {
            cmd.undo();
        }
        if (!internal) {
            ((Process) node.getProcess()).checkProcess();
        }
    }
}
