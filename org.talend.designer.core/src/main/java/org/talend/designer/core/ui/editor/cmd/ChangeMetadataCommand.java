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
package org.talend.designer.core.ui.editor.cmd;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.gef.commands.Command;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.internal.views.properties.tabbed.view.Tab;
import org.eclipse.ui.views.properties.PropertySheet;
import org.eclipse.ui.views.properties.tabbed.ISection;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;
import org.talend.core.model.components.IODataComponent;
import org.talend.core.model.components.IODataComponentContainer;
import org.talend.core.model.metadata.ColumnNameChanged;
import org.talend.core.model.metadata.IMetadataTable;
import org.talend.core.model.metadata.MetadataTool;
import org.talend.core.model.process.EConnectionType;
import org.talend.core.model.process.EParameterFieldType;
import org.talend.core.model.process.IConnection;
import org.talend.core.model.process.IElementParameter;
import org.talend.core.model.process.INode;
import org.talend.designer.core.i18n.Messages;
import org.talend.designer.core.model.components.EParameterName;
import org.talend.designer.core.model.components.EmfComponent;
import org.talend.designer.core.ui.editor.connections.Connection;
import org.talend.designer.core.ui.editor.nodes.Node;
import org.talend.designer.core.ui.editor.process.Process;
import org.talend.designer.core.ui.editor.properties.DynamicTabbedPropertySection;
import org.talend.designer.core.ui.editor.properties.controllers.ColumnListController;

/**
 * Command that will change a metadata in a node.
 * 
 * $Id$
 * 
 */
/**
 * DOC Administrator class global comment. Detailled comment <br/>
 * 
 */
public class ChangeMetadataCommand extends Command {

    private Node node, inputNode;

    protected IMetadataTable currentOutputMetadata, newOutputMetadata, oldOutputMetadata;

    private boolean outputWasRepository = false, inputWasRepository = false;

    private IMetadataTable currentInputMetadata, newInputMetadata, oldInputMetadata;

    private IODataComponentContainer inputdataContainer;

    private IODataComponentContainer outputdataContainer;

    private IODataComponent dataComponent;

    private Boolean propagate;

    private List<ChangeMetadataCommand> propagatedChange = new ArrayList<ChangeMetadataCommand>();

    private boolean internal = false;

    private boolean repositoryMode = false;

    // Default constructor.
    public ChangeMetadataCommand() {
    }

    public ChangeMetadataCommand(Node node, Node inputNode, IMetadataTable currentInputMetadata, IMetadataTable newInputMetadata,
            IMetadataTable currentOutputMetadata, IMetadataTable newOutputMetadata) {
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
        setLabel(Messages.getString("ChangeMetadataCommand.changeMetadataValues")); //$NON-NLS-1$
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

        oldOutputMetadata = currentOutputMetadata.clone(true);
        this.newOutputMetadata = newOutputMetadata.clone(true);

        // this.newOutputMetadata.setReadOnly(newOutputMetadata.isReadOnly());
        this.newOutputMetadata.setReadOnly(currentOutputMetadata.isReadOnly());
        initializeContainer();
        setLabel(Messages.getString("ChangeMetadataCommand.changeMetadataValues")); //$NON-NLS-1$
    }

    public void setRepositoryMode(boolean repositoryMode) {
        this.repositoryMode = repositoryMode;
    }

    private void initializeContainer() {
        outputdataContainer = new IODataComponentContainer();
        for (Connection connec : (List<Connection>) node.getIncomingConnections()) {
            if (connec.isActivate() && connec.getLineStyle().equals(EConnectionType.FLOW_MAIN)) {
                IODataComponent input = null;
                if (newInputMetadata == null) {
                    input = new IODataComponent(connec);
                } else {
                    if (connec.getMetaName().equals(newInputMetadata.getTableName())) {
                        input = new IODataComponent(connec, newInputMetadata);
                    }
                }
                if (input != null) {
                    outputdataContainer.getInputs().add(input);
                }

            }
        }
        for (Connection connec : (List<Connection>) node.getOutgoingConnections()) {
            if (connec.isActivate()
                    && (connec.getLineStyle().equals(EConnectionType.FLOW_MAIN) || connec.getLineStyle().equals(
                            EConnectionType.FLOW_MERGE))) {
                if ((!connec.getSource().getConnectorFromType(connec.getLineStyle()).isBuiltIn())
                        || (connec.getMetaName().equals(newOutputMetadata.getTableName()))) {
                    IODataComponent output = new IODataComponent(connec, newOutputMetadata);
                    outputdataContainer.getOuputs().add(output);
                }
            }
        }

        if (inputNode != null) {
            inputdataContainer = new IODataComponentContainer();
            for (Connection connec : (List<Connection>) inputNode.getOutgoingConnections()) {
                if (connec.isActivate() && (connec.getTarget().equals(node))) {
                    if ((!connec.getSource().getConnectorFromType(connec.getLineStyle()).isBuiltIn())
                            || (connec.getMetaName().equals(newInputMetadata.getTableName()))) {
                        IODataComponent output = new IODataComponent(connec, newInputMetadata);
                        inputdataContainer.getOuputs().add(output);
                    }
                }
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
            propagate = MessageDialog
                    .openQuestion(
                            new Shell(),
                            Messages.getString("ChangeMetadataCommand.messageDialog.propagate"), Messages.getString("ChangeMetadataCommand.messageDialog.questionMessage")); //$NON-NLS-1$ //$NON-NLS-2$
        }
        return propagate;
    }

    private boolean getPropagate() {
        return getPropagate(null);
    }

    @SuppressWarnings("unchecked")
    protected void updateColumnList(IMetadataTable oldTable, IMetadataTable newTable) {
        IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
        IViewPart view = page.findView("org.eclipse.ui.views.PropertySheet"); //$NON-NLS-1$
        PropertySheet sheet = (PropertySheet) view;
        TabbedPropertySheetPage tabbedPropertySheetPage = (TabbedPropertySheetPage) sheet.getCurrentPage();
        Tab currentTab = tabbedPropertySheetPage.getCurrentTab();
        if (currentTab == null) {
            return;
        }
        ISection[] sections = currentTab.getSections();
        final List<ColumnNameChanged> columnNameChanged = MetadataTool.getColumnNameChanged(oldTable, newTable);
        for (int i = 0; i < sections.length; i++) {
            if (sections[i] instanceof DynamicTabbedPropertySection) {
                DynamicTabbedPropertySection currentSection = (DynamicTabbedPropertySection) sections[i];
                if (currentSection.getElement().equals(node)) {
                    currentSection.refresh();
                }
            }
        }
        if (inputNode != null) {
            List<IElementParameter> eps = (List<IElementParameter>) inputNode.getElementParameters();
            if (eps != null) {
                boolean end = false;
                for (int i = 0; i < eps.size() && !end; i++) {
                    IElementParameter parameter = eps.get(i);
                    if (parameter.getField() == EParameterFieldType.TABLE) {
                        end = true;
                        if (parameter != null) {
                            List<Map<String, Object>> map2 = (List<Map<String, Object>>) parameter.getValue();
                            if (map2 != null && inputNode.getMetadataList().get(0).getListColumns().size() != map2.size()) {
                                ColumnListController.updateColumnList(inputNode, columnNameChanged);
                            }
                        }
                    }
                }
            }
        }
    }

    public void execute(Boolean propagateP) {
        this.propagate = propagateP;
        if (currentOutputMetadata == null) {
            currentOutputMetadata = node.getMetadataList().get(0);
        }
        setInternal(true);
        execute();
    }

    private void propagateDatas(boolean isExecute) {
        // Propagate :
        if (outputdataContainer != null
                && (!outputdataContainer.getInputs().isEmpty() || !outputdataContainer.getOuputs().isEmpty())) {
            for (IODataComponent currentIO : outputdataContainer.getInputs()) {
                INode sourceNode = currentIO.getSource();
                if (currentIO.hasChanged()) {
                    sourceNode.metadataOutputChanged(currentIO, currentIO.getName());
                    if (isExecute) {
                        currentIO.setTable(oldInputMetadata);
                        currentIO.setColumnNameChanged(null);
                    } else {
                        currentIO.setTable(newInputMetadata);
                        currentIO.setColumnNameChanged(null);
                    }
                }
            }
            for (IODataComponent currentIO : outputdataContainer.getOuputs()) {
                if (currentIO.hasChanged()) {
                    INode targetNode = currentIO.getTarget();
                    targetNode.metadataInputChanged(currentIO, currentIO.getUniqueName());
                    if (isExecute) {
                        if (targetNode instanceof Node) {
                            if (((Node) targetNode).getComponent().isSchemaAutoPropagated() && getPropagate()
                                    && targetNode.getMetadataList().size() > 0) {
                                IMetadataTable toCopy = newOutputMetadata.clone();
                                IMetadataTable copy = targetNode.getMetadataList().get(0).clone(true);
                                MetadataTool.copyTable(toCopy, copy);
                                ChangeMetadataCommand cmd = new ChangeMetadataCommand((Node) targetNode, null, copy);
                                if (outputdataContainer.getOuputs().size() > 0) {
                                    List<ColumnNameChanged> columnNameChanged = outputdataContainer.getOuputs().get(0)
                                            .getColumnNameChanged();
                                    for (IODataComponent dataComp : cmd.outputdataContainer.getOuputs()) {
                                        dataComp.setColumnNameChanged(columnNameChanged);
                                    }
                                }
                                cmd.execute(true);
                                propagatedChange.add(cmd);
                            }
                        }
                        currentIO.setTable(oldOutputMetadata);
                        currentIO.setColumnNameChanged(null);
                    } else {
                        if (targetNode instanceof Node) {
                            if (!((Node) targetNode).getConnectorFromType(currentIO.getConnection().getLineStyle()).isBuiltIn()
                                    && getPropagate()) {
                                if (((Node) targetNode).getComponent().isSchemaAutoPropagated()) {
                                    if (outputdataContainer.getOuputs().size() > 0) {
                                        List<ColumnNameChanged> columnNameChanged = outputdataContainer.getOuputs().get(0)
                                                .getColumnNameChanged();
                                        for (ChangeMetadataCommand cmd : propagatedChange) {
                                            for (IODataComponent dataComp : cmd.outputdataContainer.getOuputs()) {
                                                dataComp.setColumnNameChanged(columnNameChanged);
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        currentIO.setTable(newOutputMetadata);
                        currentIO.setColumnNameChanged(null);
                    }
                }
            }

        } else if (dataComponent != null) {
            for (IConnection outgoingConnection : node.getOutgoingConnections()) {
                outgoingConnection.getTarget().metadataInputChanged(dataComponent, outgoingConnection.getName());
            }
        }

        if (inputdataContainer != null) {
            for (IODataComponent currentIO : inputdataContainer.getOuputs()) {
                if (currentIO.hasChanged()) {
                    INode targetNode = currentIO.getTarget();
                    targetNode.metadataInputChanged(currentIO, currentIO.getUniqueName());
                    if (isExecute) {
                        currentIO.setTable(oldInputMetadata);
                        currentIO.setColumnNameChanged(null);
                    } else {
                        currentIO.setTable(newInputMetadata);
                        currentIO.setColumnNameChanged(null);
                    }
                }
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

        List<ColumnNameChanged> columnNameChanged = MetadataTool.getColumnNameChanged(oldOutputMetadata, newOutputMetadata);
        ColumnListController.updateColumnList(node, columnNameChanged, true);

        if (!internal) {
            updateColumnList(oldOutputMetadata, newOutputMetadata);
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

        List<ColumnNameChanged> columnNameChanged = MetadataTool.getColumnNameChanged(newOutputMetadata, oldOutputMetadata);
        ColumnListController.updateColumnList(node, columnNameChanged, true);

        if (!internal) {
            updateColumnList(newOutputMetadata, oldOutputMetadata);
            ((Process) node.getProcess()).checkProcess();
        }
    }

    /**
     * Refresh property view.
     */
    public void refreshPropertyView() {
        IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
        IViewPart view = page.findView("org.eclipse.ui.views.PropertySheet"); //$NON-NLS-1$
        PropertySheet sheet = (PropertySheet) view;
        TabbedPropertySheetPage tabbedPropertySheetPage = (TabbedPropertySheetPage) sheet.getCurrentPage();
        if (tabbedPropertySheetPage.getCurrentTab() != null) {
            tabbedPropertySheetPage.refresh();
        }
    }
}
