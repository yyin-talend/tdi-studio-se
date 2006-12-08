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
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.views.properties.PropertySheet;
import org.eclipse.ui.views.properties.tabbed.ISection;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;
import org.talend.core.model.components.IODataComponent;
import org.talend.core.model.components.IODataComponentContainer;
import org.talend.core.model.metadata.IMetadataTable;
import org.talend.core.model.process.IConnection;
import org.talend.core.model.process.IExternalNode;
import org.talend.core.model.process.INodeConnector;
import org.talend.designer.core.model.components.EParameterName;
import org.talend.designer.core.model.components.EmfComponent;
import org.talend.designer.core.ui.editor.connections.Connection;
import org.talend.designer.core.ui.editor.nodes.Node;
import org.talend.designer.core.ui.editor.process.Process;
import org.talend.designer.core.ui.editor.properties.DynamicTabbedPropertySection;

/**
 * Command that will change the datas stored for an external node.
 * 
 * $Id$
 * 
 */
public class ExternalNodeChangeCommand extends Command {

    private Node node;

    Object oldExternalData;

    String oldGeneratedCode;

    List<IMetadataTable> oldMetaDataList;

    Object newExternalData;

    String newGeneratedCode;

    List<IMetadataTable> newMetaDataList;

    List<Connection> connectionsToDelete;

    private List<ChangeMetadataCommand> metadataChanges = new ArrayList<ChangeMetadataCommand>();

    private IODataComponentContainer inAndOut;

    private Boolean propagate;

    @SuppressWarnings("unchecked")
    public ExternalNodeChangeCommand(Node node, IExternalNode externalNode) {
        this.node = node;

        this.inAndOut = externalNode.getIODataComponents();

        oldExternalData = node.getExternalData();
        oldMetaDataList = node.getMetadataList();

        newExternalData = externalNode.getExternalData();
        newMetaDataList = externalNode.getMetadataList();

        connectionsToDelete = new ArrayList<Connection>();

        for (Connection connection : (List<Connection>) node.getOutgoingConnections()) {
            boolean metadataExists = false;
            for (IMetadataTable metadata : newMetaDataList) {
                if (connection.getMetadataTable().getTableName().equals(metadata.getTableName())) {
                    metadataExists = true;
                }
            }
            if (!metadataExists) {
                connectionsToDelete.add(connection);
            }
        }

        for (Connection connection : (List<Connection>) node.getIncomingConnections()) {
            String schemaType = (String) connection.getSource().getPropertyValue(EParameterName.SCHEMA_TYPE.getName());
            if (schemaType != null) {
                if (schemaType.equals(EmfComponent.REPOSITORY)) {
                    String metaRepositoryName = (String) connection.getSource().getPropertyValue(
                            EParameterName.REPOSITORY_SCHEMA_TYPE.getName());
                    IMetadataTable repositoryMetadata = Process.getMetadataFromRepository(metaRepositoryName);
                    if (repositoryMetadata == null) {
                        connection.getSource().setPropertyValue(EParameterName.SCHEMA_TYPE.getName(),
                                EmfComponent.BUILTIN);
                    } else {
                        repositoryMetadata = repositoryMetadata.clone();
                        repositoryMetadata.setTableName(connection.getSource().getUniqueName());
                        if (!repositoryMetadata.sameMetadataAs(connection.getMetadataTable())) {
                            connection.getSource().setPropertyValue(EParameterName.SCHEMA_TYPE.getName(),
                                    EmfComponent.BUILTIN);
                        }
                    }
                }
            }
        }

        setLabel("Modification from " + node.getComponentName());
    }

    private void refreshSectionsPropertyView() {
        IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
        IViewPart view = page.findView("org.eclipse.ui.views.PropertySheet");
        PropertySheet sheet = (PropertySheet) view;
        TabbedPropertySheetPage tabbedPropertySheetPage = (TabbedPropertySheetPage) sheet.getCurrentPage();
        ISection[] sections = tabbedPropertySheetPage.getCurrentTab().getSections();
        for (int i = 0; i < sections.length; i++) {
            if (sections[i] instanceof DynamicTabbedPropertySection) {
                DynamicTabbedPropertySection currentSection = (DynamicTabbedPropertySection) sections[i];
                if (currentSection.getElement().equals(node)) {
                    currentSection.addComponents();
                    currentSection.refresh();
                }
            }
        }
    }

    private boolean getPropagate() {
        if (propagate == null) {
            propagate = MessageDialog.openQuestion(new Shell(), "Propagate", "Would you like to propagate changes ?");
        }
        return propagate;
    }

    @Override
    public void execute() {
        for (Connection connection : (List<Connection>) node.getIncomingConnections()) {
            String schemaType = (String) connection.getSource().getPropertyValue(EParameterName.SCHEMA_TYPE.getName());
            if (schemaType != null) {
                IMetadataTable repositoryMetadata = inAndOut.getTable(connection);
                repositoryMetadata.setTableName(connection.getSource().getUniqueName());
                if (!repositoryMetadata.sameMetadataAs(connection.getMetadataTable())) {
                    if (schemaType.equals(EmfComponent.REPOSITORY)) {
                        connection.getSource().setPropertyValue(EParameterName.SCHEMA_TYPE.getName(),
                                EmfComponent.BUILTIN);
                    }

                    connection.getSource().getMetadataTable(connection.getMetaName()).setListColumns(
                            repositoryMetadata.getListColumns());
                    connection.getSource().getMetadataTable(connection.getMetaName()).setTableName(
                            repositoryMetadata.getTableName());
                }
            }
        }
        metadataChanges.clear();
        for (IConnection connection : node.getOutgoingConnections()) {
            IODataComponent dataComponent = inAndOut.getDataComponent(connection);
            if (!connection.getMetadataTable().sameMetadataAs(dataComponent.getTable())) {
                if (getPropagate()) {
                    ChangeMetadataCommand cmd = new ChangeMetadataCommand((Node) connection.getSource(), connection
                            .getMetadataTable(), dataComponent.getTable());
                    cmd.execute(true);
                    metadataChanges.add(cmd);
                }
            }
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
        node.checkAndRefreshNode();
        refreshSectionsPropertyView();
    }

    @Override
    public void undo() {
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
        for (ChangeMetadataCommand cmd : metadataChanges) {
            cmd.undo();
        }
        node.checkAndRefreshNode();
        refreshSectionsPropertyView();
    }
}
