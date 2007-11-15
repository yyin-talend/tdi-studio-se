// ============================================================================
//
// Copyright (C) 2006-2007 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.designer.dbmap.external.data;

import java.util.ArrayList;
import java.util.List;

import org.talend.core.model.process.IExternalData;

/**
 * DOC amaumont class global comment. Detailled comment <br/>
 * 
 * $Id: ExternalMapperData.java 1 2006-09-29 17:06:40Z nrousseau $
 * 
 */
public class ExternalDbMapData implements IExternalData {

    /**
     * 
     */
    private static final long serialVersionUID = 4877887839142463534L;

    private ExternalDbMapUiProperties uiProperties = new ExternalDbMapUiProperties();

    private List<ExternalDbMapTable> inputTables = new ArrayList<ExternalDbMapTable>(0);

    private List<ExternalDbMapTable> outputTables = new ArrayList<ExternalDbMapTable>(0);

    /** not used ... */
    private List<ExternalDbMapTable> varsTables = new ArrayList<ExternalDbMapTable>(0);

    public List<ExternalDbMapTable> getInputTables() {
        return this.inputTables;
    }

    public void setInputTables(List<ExternalDbMapTable> tables) {
        this.inputTables = tables;
    }

    public List<ExternalDbMapTable> getOutputTables() {
        return this.outputTables;
    }

    public void setOutputTables(List<ExternalDbMapTable> outputTables) {
        this.outputTables = outputTables;
    }

    /** not used ... */
    public List<ExternalDbMapTable> getVarsTables() {
        return this.varsTables;
    }

    /** not used ... */
    public void setVarsTables(List<ExternalDbMapTable> varsTables) {
        this.varsTables = varsTables;
    }

    public ExternalDbMapUiProperties getUiProperties() {
        return this.uiProperties;
    }

    public void setUiProperties(ExternalDbMapUiProperties layoutProperties) {
        this.uiProperties = layoutProperties;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#clone()
     */
    @SuppressWarnings("unchecked")
    @Override
    public IExternalData clone() throws CloneNotSupportedException {
        ExternalDbMapData cloned = (ExternalDbMapData) super.clone();
        cloned.uiProperties = (ExternalDbMapUiProperties) uiProperties.clone();
        cloned.inputTables = (List<ExternalDbMapTable>) ((ArrayList) inputTables).clone();
        cloned.outputTables = (List<ExternalDbMapTable>) ((ArrayList) outputTables).clone();
        cloned.varsTables = (List<ExternalDbMapTable>) ((ArrayList) varsTables).clone();
        return cloned;
    }

}
