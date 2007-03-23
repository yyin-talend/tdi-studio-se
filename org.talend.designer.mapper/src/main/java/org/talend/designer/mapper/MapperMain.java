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
package org.talend.designer.mapper;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.talend.core.model.components.IComponent;
import org.talend.core.model.components.IODataComponent;
import org.talend.core.model.components.IODataComponentContainer;
import org.talend.core.model.metadata.IMetadataTable;
import org.talend.core.model.process.AbstractExternalNode;
import org.talend.core.model.process.IConnection;
import org.talend.designer.core.model.components.EParameterName;
import org.talend.designer.mapper.external.connection.IOConnection;
import org.talend.designer.mapper.external.converter.ExternalDataConverter;
import org.talend.designer.mapper.external.data.ExternalMapperData;
import org.talend.designer.mapper.external.data.ExternalMapperUiProperties;
import org.talend.designer.mapper.i18n.Messages;
import org.talend.designer.mapper.language.LanguageProvider;
import org.talend.designer.mapper.managers.MapperManager;
import org.talend.designer.mapper.model.MapperModel;
import org.talend.designer.mapper.model.metadata.MapperDataTestGenerator;
import org.talend.designer.mapper.model.table.InputTable;
import org.talend.designer.mapper.model.table.OutputTable;
import org.talend.designer.mapper.ui.MapperUI;

/**
 * DOC amaumont class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 */
public class MapperMain {

    private static boolean standAloneMode;

    public static void main(String[] args) {
        AbstractExternalNode mapperConnector = new MapperComponent();
        MapperMain.setStandAloneMode(true);
        MapperDataTestGenerator testGenerator = new MapperDataTestGenerator(LanguageProvider.getCurrentLanguage(), true);
        mapperConnector.setExternalData(testGenerator.getExternalData());
        mapperConnector.setIncomingConnections(testGenerator.getInputConnectionsList());
        mapperConnector.setMetadataList(testGenerator.getMetadataListOut());
        int response = mapperConnector.open(new Display());
        if (response == SWT.OK) {
            // System.out.println("Response = OK");

            // System.out.println("mapperConnector.getMetadataList()=");
            // System.out.println(mapperConnector.getMetadataList());
            // System.out.println("mapperConnector.getPersistentData()=");
            // System.out.println(mapperConnector.getExternalData(false));

        } else {
            // System.out.println("Response = CANCEL");
        }

    }

    private MapperManager mapperManager;

    private MapperModel mapperModel;

    private MapperComponent connector;

    public MapperMain(MapperComponent connector) {
        super();
        mapperManager = new MapperManager(connector);
        this.connector = connector;
    }

    /**
     * Create a shell and init the mapper into it.
     * 
     * @param display
     * @return the created shell
     */
    public Shell createUI(Display display) {
        Shell shell = new Shell(display, SWT.APPLICATION_MODAL | SWT.BORDER | SWT.RESIZE | SWT.CLOSE | SWT.MIN
                | SWT.MAX | SWT.TITLE);
        IComponent component = connector.getComponent();
        ImageDescriptor imageDescriptor = component.getIcon32();
        Image createImage = imageDescriptor.createImage();
        // Shell shell = new Shell(display);
        // shell.setImage(ImageProviderMapper.getImage(ImageInfo.MAPPER_ICON));
        shell.setImage(createImage);
        shell.setText(Messages.getString("MapperMain.title") + connector.getUniqueName()); //$NON-NLS-1$
        ExternalMapperUiProperties uiProperties = mapperManager.getUiManager().getUiProperties();
        Rectangle boundsMapper = uiProperties.getBoundsMapper();
        if (uiProperties.isShellMaximized()) {
            shell.setMaximized(uiProperties.isShellMaximized());
        } else {
            // // move shell at outer of display zone to avoid visual effect on loading
            // Rectangle tmpBoundsMapper = new Rectangle(-boundsMapper.width, boundsMapper.y, boundsMapper.width,
            // boundsMapper.height);
            // shell.setBounds(tmpBoundsMapper);
            boundsMapper = uiProperties.getBoundsMapper();
            if (boundsMapper.x < 0) {
                boundsMapper.x = 0;
            }
            if (boundsMapper.y < 0) {
                boundsMapper.y = 0;
            }
            shell.setBounds(boundsMapper);
        }
        createUI(shell);
        shell.open();
        return shell;
    }

    /**
     * Init the mapper into the passed parent <code>Component</code>.
     * 
     * @param parent
     */
    public void createUI(Composite parent) {
        MapperUI mapperUI = new MapperUI(parent, mapperManager);
        mapperUI.init(mapperModel);
    }

    public MapperManager getMapperManager() {
        return this.mapperManager;
    }

    public void loadModelFromInternalData() {
        this.mapperModel = new MapperModel(mapperManager.getInputTables(), mapperManager.getOutputTables(),
                mapperManager.getVarsTables());
    }

    /**
     * 
     * DOC amaumont Comment method "getExternalData".
     * 
     * @param refresh
     * @param loadFromInternalData else load from external data
     * @return
     */

    public ExternalMapperData buildExternalData() {
        ExternalDataConverter converter = new ExternalDataConverter(mapperManager);
        return converter.prepareExternalData(mapperModel, mapperManager.getUiManager().getUiProperties());

    }

    /**
     * DOC amaumont Comment method "loadFromExternalData".
     * 
     * @param incomingConnections
     * @param outgoingConnections
     * @param externalData
     * @param checkProblems TODO
     * @param metadataList
     */
    public void createModelFromExternalData(List<? extends IConnection> incomingConnections,
            List<? extends IConnection> outgoingConnections, ExternalMapperData externalData,
            List<IMetadataTable> outputMetadataTables, boolean checkProblems) {
        ArrayList<IOConnection> inputs = createIOConnections(incomingConnections);
        ArrayList<IOConnection> outputs = createIOConnections(outgoingConnections);
        createModelFromExternalData(inputs, outputs, outputMetadataTables, externalData, checkProblems);
    }

    /**
     * DOC amaumont Comment method "createIOConnections".
     * 
     * @param connections
     * @return
     */
    public ArrayList<IOConnection> createIOConnections(List<? extends IConnection> connections) {
        ArrayList<IOConnection> ioConnections = new ArrayList<IOConnection>(connections.size());
        for (IConnection connection : connections) {
            ioConnections.add(new IOConnection(connection));
        }
        return ioConnections;
    }

    public void createModelFromExternalData(IODataComponentContainer ioDataContainer,
            List<IMetadataTable> outputMetadataTables, ExternalMapperData externalData, boolean checkProblems) {
        List<IODataComponent> inputsData = ioDataContainer.getInputs();
        List<IODataComponent> ouputsData = ioDataContainer.getOuputs();

        ArrayList<IOConnection> inputs = new ArrayList<IOConnection>(inputsData.size());
        for (IODataComponent iData : inputsData) {
            inputs.add(new IOConnection(iData));
        }
        ArrayList<IOConnection> outputs = new ArrayList<IOConnection>(ouputsData.size());
        for (IODataComponent oData : ouputsData) {
            outputs.add(new IOConnection(oData));
        }
        createModelFromExternalData(inputs, outputs, outputMetadataTables, externalData, false);
    }

    public void createModelFromExternalData(List<IOConnection> inputs, List<IOConnection> outputs,
            List<IMetadataTable> outputMetadataTables, ExternalMapperData externalData, boolean checkProblems) {
        if (externalData == null) {
            externalData = new ExternalMapperData();
        }
        mapperManager.getUiManager().setUiProperties(externalData.getUiProperties());
        ExternalDataConverter converter = new ExternalDataConverter(mapperManager);
        this.mapperModel = converter.prepareModel(inputs, outputs, outputMetadataTables, externalData, checkProblems);
    }

    /**
     * DOC amaumont Comment method "getResponse".
     * 
     * @return
     */
    public int getMapperDialogResponse() {
        return mapperManager.getUiManager().getMapperResponse();
    }

    /**
     * DOC amaumont Comment method "getMetadataListIn".
     */
    public List<IMetadataTable> getMetadataListIn() {
        List<InputTable> tables = mapperManager.getInputTables();
        List<IMetadataTable> metadataTables = new ArrayList<IMetadataTable>(tables.size());
        for (InputTable table : tables) {
            metadataTables.add(table.getMetadataTable());
        }
        return metadataTables;
    }

    /**
     * DOC amaumont Comment method "getMetadataListOut".
     * 
     * @return
     */
    public List<IMetadataTable> getMetadataListOut() {
        List<OutputTable> tables = mapperManager.getOutputTables();
        List<IMetadataTable> metadataTables = new ArrayList<IMetadataTable>(tables.size());
        for (OutputTable table : tables) {
            metadataTables.add(table.getMetadataTable());
        }
        return metadataTables;
    }

    public static boolean isStandAloneMode() {
        return standAloneMode;
    }

    public static void setStandAloneMode(boolean standAloneMode) {
        MapperMain.standAloneMode = standAloneMode;
    }

    public void loadInitialParamters() {
        this.mapperManager.updateEmfParameters(EParameterName.PREVIEW.getName());
    }

}
