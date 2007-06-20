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
package org.talend.designer.fileoutputxml;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.talend.core.model.components.IComponent;
import org.talend.designer.fileoutputxml.managers.FOXManager;
import org.talend.designer.fileoutputxml.ui.FOXUI;

/**
 * This class is responsible for contacting the RowGeneratorComponent and FOXUI. <br/>
 * 
 * $Id: FOXMain.java,v 1.1 2007/06/12 07:20:38 gke Exp $
 * 
 */
public class FOXMain {

    private boolean standAloneMode = false;

    private FOXUI generatorUI;

    private FileOutputXMLComponent connector;

    private FOXManager foxManager;

    public FOXMain(FileOutputXMLComponent connector) {
        this.connector = connector;
        this.foxManager = new FOXManager(connector);
    }

    /**
     * create UI".
     * 
     * @param parent
     * @return
     */
    public void createUI(Composite parent) {
        generatorUI = new FOXUI(parent, foxManager);
        generatorUI.init();
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
        shell.setText(connector.getUniqueName()); //$NON-NLS-1$
        Rectangle boundsRG = new Rectangle(50, 50, 800, 600);
        shell.setBounds(boundsRG);
        createUI(shell);
        shell.open();

        return shell;
    }

    /**
     * Getter for standAloneMode.
     * 
     * @return the standAloneMode
     */
    public boolean isStandAloneMode() {
        return this.standAloneMode;
    }

    /**
     * Sets the standAloneMode.
     * 
     * @param standAloneMode the standAloneMode to set
     */
    public void setStandAloneMode(boolean standAloneMode) {
        this.standAloneMode = standAloneMode;
    }

    
    public FOXManager getFoxManager() {
        return foxManager;
    }

}
