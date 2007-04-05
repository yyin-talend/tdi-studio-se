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
package org.talend.designer.rowgenerator;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.talend.core.model.components.IComponent;
import org.talend.core.model.components.IODataComponent;
import org.talend.core.model.components.IODataComponentContainer;
import org.talend.core.model.metadata.IMetadataTable;
import org.talend.core.model.process.IConnection;
import org.talend.designer.rowgenerator.external.data.ExternalRowGenTable;
import org.talend.designer.rowgenerator.external.data.ExternalRowGeneratorData;
import org.talend.designer.rowgenerator.external.data.ExternalRowGeneratorUiProperties;
import org.talend.designer.rowgenerator.external.data.IOConnection;
import org.talend.designer.rowgenerator.i18n.Messages;
import org.talend.designer.rowgenerator.managers.RowGeneratorManager;
import org.talend.designer.rowgenerator.ui.RowGeneratorUI;
import org.talend.designer.rowgenerator.ui.editor.MetadataTableEditorViewExt;

/**
 * qzhang class global comment. Detailled comment <br/>
 * 
 * $Id: RowGenMain.java,v 1.4 2007/01/31 05:20:52 pub Exp $
 * 
 */
public class RowGenMain {

    private RowGeneratorComponent connector;

    private RowGeneratorManager generatorManager;

    private RowGeneratorUI generatorUI;

    private List<ExternalRowGenTable> outputTables;

    /**
     * qzhang RowGeneratorMain constructor comment.
     */
    public RowGenMain(RowGeneratorComponent connector) {
        super();
        this.connector = connector;
        this.generatorManager = new RowGeneratorManager(connector);
    }

    /**
     * qzhang Comment method "loadInitialParamters".
     */
    public void loadInitialParamters() {

    }

    /**
     * qzhang Comment method "loadModelFromInternalData".
     */
    public void loadModelFromInternalData() {

    }

    /**
     * qzhang Comment method "createUI".
     * 
     * @param parent
     * @return
     */
    public void createUI(Composite parent) {
        generatorUI = new RowGeneratorUI(parent, generatorManager);
        generatorUI.init();
    }

    /**
     * qzhang Comment method "getMapperDialogResponse".
     * 
     * @return
     */
    public int getMapperDialogResponse() {
        return generatorManager.getUiManager().getRowGenResponse();
    }

    /**
     * qzhang Comment method "getMetadataListOut".
     * 
     * @return
     */
    public List<IMetadataTable> getMetadataListOut() {
        return null;
    }

    /**
     * qzhang Comment method "createUI".
     * 
     * @param display
     * @return
     */
    public Shell createUI(Display display) {

        Shell shell = new Shell(display, SWT.APPLICATION_MODAL | SWT.BORDER | SWT.RESIZE | SWT.CLOSE | SWT.MIN | SWT.MAX
                | SWT.TITLE);
        IComponent component = connector.getComponent();
        ImageDescriptor imageDescriptor = component.getIcon32();
        Image createImage = imageDescriptor.createImage();
        shell.setImage(createImage);
        shell.setText(Messages.getString("RowGenMain.MainShellText", connector.getUniqueName())); //$NON-NLS-1$
        Rectangle boundsRG = ExternalRowGeneratorUiProperties.getBoundsRowGen();
        if (ExternalRowGeneratorUiProperties.isShellMaximized()) {
            shell.setMaximized(ExternalRowGeneratorUiProperties.isShellMaximized());
        } else {
            boundsRG = ExternalRowGeneratorUiProperties.getBoundsRowGen();
            if (boundsRG.x < 0) {
                boundsRG.x = 0;
            }
            if (boundsRG.y < 0) {
                boundsRG.y = 0;
            }
            shell.setBounds(boundsRG);
        }
        createUI(shell);
        generatorUI.getDataTableView().getExtendedToolbar().updateComponentsSize();
        shell.open();
        shell.addControlListener(new ControlListener() {

            public void controlMoved(ControlEvent e) {
                generatorUI.getDataTableView().attachLabelPosition();
            }

            public void controlResized(ControlEvent e) {
                generatorUI.getDataTableView().attachLabelPosition();
                generatorUI.getDataTableView().fixedLabelSize();
            }

        });

        addAllControlForKeyListener(shell);
        // shell.moveAbove(null);
        generatorUI.getDataTableView().updateHeader(ExternalRowGeneratorUiProperties.getShowColumnsList());
        return shell;
    }

    /**
     * qzhang Comment method "addAllControlForKeyListener".
     * 
     * @param parent
     * @param keyAdapter
     * @return
     */
    private void addAllControlForKeyListener(Control parent) {
        if (parent instanceof Composite) {
            KeyAdapter keyAdapter = new KeyAdapter() {

                /*
                 * (non-Javadoc)
                 * 
                 * @see org.eclipse.swt.events.KeyAdapter#keyReleased(org.eclipse.swt.events.KeyEvent)
                 */
                public void keyReleased(KeyEvent e) {
                    if (e.keyCode == SWT.F5) {
                        MetadataTableEditorViewExt viewExt = generatorUI.getDataTableView();
                        viewExt.refreshPreviewColumn();
                    }
                }
            };
            Composite composite = (Composite) parent;
            composite.addKeyListener(keyAdapter);
            Control[] children = composite.getChildren();
            for (Control control : children) {
                if (control != null && !control.isDisposed()) {
                    addAllControlForKeyListener(control);
                }
            }
        }

    }

    /**
     * qzhang Comment method "getRowGenManager".
     * 
     * @return
     */
    public RowGeneratorManager getRowGenManager() {
        return generatorManager;
    }

    /**
     * qzhang Comment method "isStandAloneMode".
     * 
     * @return
     */
    public static boolean isStandAloneMode() {
        return false;
    }

    /**
     * qzhang Comment method "buildExternalData".
     * 
     * @return
     */
    public ExternalRowGeneratorData buildExternalData() {
        ExternalRowGeneratorData externalData = new ExternalRowGeneratorData();
        outputTables = new ArrayList<ExternalRowGenTable>();
        externalData.setOutputTables(outputTables);
        externalData.setUiProperties(generatorManager.getUiManager().getUiProperties());
        return externalData;
    }

    /**
     * amaumont Comment method "loadFromExternalData".
     * 
     * @param incomingConnections
     * @param outgoingConnections
     * @param externalData
     * @param checkProblems
     * @param metadataList
     */
    public void createModelFromExternalData(List<? extends IConnection> incomingConnections,
            List<? extends IConnection> outgoingConnections, ExternalRowGeneratorData externalData,
            List<IMetadataTable> outputMetadataTables, boolean checkProblems) {
        ArrayList<IOConnection> inputs = createIOConnections(incomingConnections);
        ArrayList<IOConnection> outputs = createIOConnections(outgoingConnections);
        createModelFromExternalData(inputs, outputs, outputMetadataTables, externalData, checkProblems);
    }

    public ArrayList<IOConnection> createIOConnections(List<? extends IConnection> connections) {
        ArrayList<IOConnection> ioConnections = new ArrayList<IOConnection>(connections.size());
        for (IConnection connection : connections) {
            ioConnections.add(new IOConnection(connection));
        }
        return ioConnections;
    }

    /**
     * qzhang Comment method "createModelFromExternalData".
     * 
     * @param dataComponents
     * @param metadataList
     * @param externalData
     * @param b
     */
    public void createModelFromExternalData(IODataComponentContainer dataComponents, List<IMetadataTable> metadataList,
            ExternalRowGeneratorData externalData, boolean b) {
        List<IODataComponent> inputsData = dataComponents.getInputs();
        List<IODataComponent> ouputsData = dataComponents.getOuputs();

        ArrayList<IOConnection> inputs = new ArrayList<IOConnection>(inputsData.size());
        for (IODataComponent iData : inputsData) {
            inputs.add(new IOConnection(iData));
        }
        ArrayList<IOConnection> outputs = new ArrayList<IOConnection>(ouputsData.size());
        for (IODataComponent oData : ouputsData) {
            outputs.add(new IOConnection(oData));
        }
        createModelFromExternalData(inputs, outputs, metadataList, externalData, false);
    }

    public void createModelFromExternalData(List<IOConnection> inputs, List<IOConnection> outputs,
            List<IMetadataTable> outputMetadataTables, ExternalRowGeneratorData externalData, boolean checkProblems) {
        if (externalData == null) {
            externalData = new ExternalRowGeneratorData();
        }
        generatorManager.getUiManager().setUiProperties(externalData.getUiProperties());
    }

}
