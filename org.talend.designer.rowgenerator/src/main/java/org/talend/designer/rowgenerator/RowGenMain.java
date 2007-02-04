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
package org.talend.designer.rowgenerator;

import java.util.List;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.talend.core.model.components.IComponent;
import org.talend.core.model.metadata.IMetadataTable;
import org.talend.designer.rowgenerator.external.data.ExternalRowGeneratorUiProperties;
import org.talend.designer.rowgenerator.i18n.Messages;
import org.talend.designer.rowgenerator.managers.RowGeneratorManager;
import org.talend.designer.rowgenerator.ui.RowGeneratorUI;

/**
 *  qzhang class global comment. Detailled comment <br/>
 * 
 * $Id: RowGenMain.java,v 1.4 2007/01/31 05:20:52 pub Exp $
 * 
 */
public class RowGenMain {

    private RowGeneratorComponent connector;

    private RowGeneratorManager generatorManager;

    /**
     *  qzhang RowGeneratorMain constructor comment.
     */
    public RowGenMain(RowGeneratorComponent connector) {
        super();
        this.connector = connector;
        this.generatorManager = new RowGeneratorManager(connector);
    }

    /**
     *  qzhang Comment method "loadInitialParamters".
     */
    public void loadInitialParamters() {

    }

    /**
     *  qzhang Comment method "loadModelFromInternalData".
     */
    public void loadModelFromInternalData() {

    }

    /**
     *  qzhang Comment method "createUI".
     * 
     * @param parent
     * @return
     */
    public void createUI(Composite parent) {
        RowGeneratorUI generatorUI = new RowGeneratorUI(parent, generatorManager);
        generatorUI.init();
    }

    /**
     *  qzhang Comment method "getMapperDialogResponse".
     * 
     * @return
     */
    public int getMapperDialogResponse() {
        // TODO Auto-generated method stub
        return generatorManager.getUiManager().getMapperResponse();
    }

    /**
     *  qzhang Comment method "getMetadataListOut".
     * 
     * @return
     */
    public List<IMetadataTable> getMetadataListOut() {
        return null;
    }

    /**
     *  qzhang Comment method "createUI".
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
        ExternalRowGeneratorUiProperties uiProperties = new ExternalRowGeneratorUiProperties();
        generatorManager.getUiManager().setUiProperties(uiProperties);
        Rectangle boundsMapper = uiProperties.getBoundsMapper();
        if (uiProperties.isShellMaximized()) {
            shell.setMaximized(uiProperties.isShellMaximized());
        } else {
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
     *  qzhang Comment method "getMapperManager".
     * 
     * @return
     */
    public RowGeneratorManager getMapperManager() {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     *  qzhang Comment method "isStandAloneMode".
     * 
     * @return
     */
    public static boolean isStandAloneMode() {
        // TODO Auto-generated method stub
        return false;
    }

}
