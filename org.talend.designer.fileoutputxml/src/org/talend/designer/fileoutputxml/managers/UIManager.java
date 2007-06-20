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
package org.talend.designer.fileoutputxml.managers;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.talend.designer.fileoutputxml.ui.FOXUI;

/**
 * UI Manager<br/>
 * 
 * $Id: UIManager.java,v 1.1 2007/06/12 07:20:38 gke Exp $
 * 
 */
public class UIManager {

    private FOXUI foxUI;

    private int uiResponse = SWT.NONE;

    private FOXManager foxManager;

    /**
     * UIManager constructor .
     * 
     * @param foxManager
     */
    public UIManager(FOXManager foxManager) {
        this.foxManager = foxManager;
    }

    /**
     * Getter for foxManager.
     * 
     * @return the foxManager
     */
    public FOXManager getFoxManager() {
        return this.foxManager;
    }

    /**
     * Sets the foxManager.
     * 
     * @param foxManager the foxManager to set
     */
    public void setFoxManager(FOXManager foxManager) {
        this.foxManager = foxManager;
    }

    /**
     * Getter for foxUI.
     * 
     * @return the foxUI
     */
    public FOXUI getFoxUI() {
        return this.foxUI;
    }

    /**
     * Sets the foxUI.
     * 
     * @param foxUI the foxUI to set
     */
    public void setFoxUI(FOXUI foxUI) {
        this.foxUI = foxUI;
    }

    /**
     * Getter for uiResponse.
     * 
     * @return the uiResponse
     */
    public int getUiResponse() {
        return this.uiResponse;
    }

    /**
     * Sets the uiResponse.
     * 
     * @param uiResponse the uiResponse to set
     */
    public void setUiResponse(int uiResponse) {
        this.uiResponse = uiResponse;
    }

    /**
     * DOC qiang.zhang Comment method "closeFOX".
     * 
     * @param reponse
     */
    public void closeFOX(int response) {
        
        saveCurrentUIProperties();
        if (response == SWT.CANCEL) {
            //do nothing.....
        }

        if (response == SWT.OK) {
            saveAllData();
        }
        Composite parent = foxUI.getFoxUIParent();
        if (parent instanceof Shell) {
            ((Shell) parent).close();
        }

    }

    /**
     * DOC qiang.zhang Comment method "saveCurrentUIProperties".
     */
    private void saveCurrentUIProperties() {
        
    }
    
    /**
     * DOC gke Comment method "saveAllData".
     */
    private void saveAllData() {
        foxManager.saveDataToComponent();

    }

}
