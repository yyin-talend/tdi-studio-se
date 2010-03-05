// ============================================================================
//
// Copyright (C) 2006-2010 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.designer.hl7.managers;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.talend.designer.hl7.ui.HL7UI;

/**
 * UI Manager<br/>
 * 
 * $Id: UIManager.java,v 1.1 2007/06/12 07:20:38 gke Exp $
 * 
 */
public class UIManager {

    protected HL7UI hl7UI;

    private int uiResponse = SWT.NONE;

    protected HL7Manager hl7Manager;

    public UIManager(HL7Manager hl7Manager) {
        this.hl7Manager = hl7Manager;
    }

    public HL7Manager getHl7Manager() {
        return this.hl7Manager;
    }

    public void setHl7Manager(HL7Manager hl7Manager) {
        this.hl7Manager = hl7Manager;
    }

    public HL7UI getHl7UI() {
        return this.hl7UI;
    }

    public void setHl7UI(HL7UI hl7ui) {
        this.hl7UI = hl7ui;
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
    public void closeHL7(int response) {

        if (response == SWT.CANCEL) {
            setUiResponse(SWT.CANCEL);
        }

        if (response == SWT.OK) {
            if (saveAllData()) {
                setUiResponse(SWT.OK);
            } else {
                setUiResponse(SWT.CANCEL);
            }
        }
        Composite parent = hl7UI.getHl7UIParent();
        if (parent instanceof Shell) {
            ((Shell) parent).close();
        }

    }

    /**
     * 
     * DOC xzhang Comment method "autoMap".
     */
    // public void autoMap() {
    // HL7TreeNode root = this.foxManager.getTreeData().get(0);
    // List<HL7TreeNode> mappableNodes = new ArrayList<HL7TreeNode>();
    // getMappableNode((Element) root, mappableNodes);
    // List<IMetadataColumn> schemaData = this.foxManager.getSchemaData();
    //
    // for (IMetadataColumn column : schemaData) {
    // for (HL7TreeNode node : mappableNodes) {
    // if (node.getLabel().equals(column.getLabel())) {
    // node.setDefaultValue(null);
    // node.setColumn(column);
    // break;
    // }
    // }
    // }
    // // this.hl7UI.refreshXMLViewer(root);
    // this.hl7UI.redrawLinkers();
    // }

    private boolean saveAllData() {
        return hl7Manager.saveDataToComponent();

    }

}
