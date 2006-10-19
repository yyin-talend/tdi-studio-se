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

import org.eclipse.gef.commands.Command;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.views.properties.PropertySheet;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;
import org.talend.core.model.components.IODataComponent;
import org.talend.core.model.components.IODataComponentContainer;
import org.talend.core.model.metadata.IMetadataTable;
import org.talend.core.model.process.IConnection;
import org.talend.designer.core.model.components.EParameterName;
import org.talend.designer.core.model.components.EmfComponent;
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

    private IMetadataTable currentOutputMetadata, newOutputMetadata;

    private boolean outputWasRepository = false, inputWasRepository = false;

    private IMetadataTable currentInputMetadata, newInputMetadata;

    private IODataComponentContainer dataContainer;

    private IODataComponent dataComponent;

    private Boolean propagate;

    public ChangeMetadataCommand(Node node, Node inputNode, IMetadataTable currentInputMetadata, IMetadataTable newInputMetadata,
            IMetadataTable currentOutputMetadata, IMetadataTable newOutputMetadata, IODataComponentContainer dataContainer) {
        this.node = node;
        this.inputNode = inputNode;
        this.currentInputMetadata = currentInputMetadata;
        this.newInputMetadata = newInputMetadata;
        this.currentOutputMetadata = currentOutputMetadata;
        this.newOutputMetadata = newOutputMetadata;
        this.dataContainer = dataContainer;
        setLabel("Change Metadata values");
    }

    public ChangeMetadataCommand(Node node, IMetadataTable currentOutputMetadata, IMetadataTable newOutputMetadata,
            IODataComponent dataComponent) {
        this.node = node;
        this.inputNode = null;
        this.currentInputMetadata = null;
        this.newInputMetadata = null;
        this.currentOutputMetadata = currentOutputMetadata;
        this.newOutputMetadata = newOutputMetadata;
        if (dataComponent != null) {
            this.newOutputMetadata = dataComponent.getTable();
            this.dataComponent = dataComponent;
        }
        setLabel("Change Metadata values");
    }

    private void refreshPropertyView() {
        IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
        IViewPart view = page.findView("org.eclipse.ui.views.PropertySheet");
        PropertySheet sheet = (PropertySheet) view;
        TabbedPropertySheetPage tabbedPropertySheetPage = (TabbedPropertySheetPage) sheet.getCurrentPage();
        tabbedPropertySheetPage.refresh();
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
        execute();
    }

    @Override
    public void execute() {
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
                        currentIO.getTarget().metadataInputChanged(currentIO, currentIO.getName());
                    }
                }
            }
        } else if (dataComponent != null) {
            for (IConnection outgoingConnection : node.getOutgoingConnections()) {
                outgoingConnection.getTarget().metadataInputChanged(dataComponent, outgoingConnection.getName());
            }
        }
        // End propagate

        if (currentInputMetadata != null) {
            // inputNode.getMetadataList().remove(currentInputMetadata);
            // inputNode.getMetadataList().add(newInputMetadata);
            currentInputMetadata.setListColumns(newInputMetadata.getListColumns());
            String type = (String) inputNode.getPropertyValue(EParameterName.SCHEMA_TYPE.getName());
            if (type != null) {
                if (type.equals(EmfComponent.REPOSITORY)) {
                    inputWasRepository = true;
                    inputNode.setPropertyValue(EParameterName.SCHEMA_TYPE.getName(), EmfComponent.BUILTIN);
                }
            }
        }

        if (currentOutputMetadata == null) {
            currentOutputMetadata = node.getMetadataList().get(0);
        }
        if (!currentOutputMetadata.sameMetadataAs(newOutputMetadata)) {
            currentOutputMetadata.setListColumns(newOutputMetadata.getListColumns());

            String type = (String) node.getPropertyValue(EParameterName.SCHEMA_TYPE.getName());
            if (type.equals(EmfComponent.REPOSITORY)) {
                outputWasRepository = true;
                node.setPropertyValue(EParameterName.SCHEMA_TYPE.getName(), EmfComponent.BUILTIN);
            }
        }

        refreshPropertyView();
        ((Process) node.getProcess()).checkProcess(getPropagate(true));
    }

    @Override
    public void undo() {
        if (currentInputMetadata != null) {
            inputNode.getMetadataList().remove(newInputMetadata);
            inputNode.getMetadataList().add(currentInputMetadata);
            if (inputWasRepository) {
                inputNode.setPropertyValue(EParameterName.SCHEMA_TYPE.getName(), EmfComponent.REPOSITORY);
            }
        }
        node.getMetadataList().remove(newOutputMetadata);
        node.getMetadataList().add(currentOutputMetadata);
        if (outputWasRepository) {
            node.setPropertyValue(EParameterName.SCHEMA_TYPE.getName(), EmfComponent.REPOSITORY);
        }
        refreshPropertyView();
        ((Process) node.getProcess()).checkProcess();
    }
}
