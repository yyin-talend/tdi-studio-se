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
package org.talend.designer.mapper.external.data;

import java.util.ArrayList;
import java.util.List;

import org.talend.core.model.process.IExternalData;

/**
 * DOC amaumont class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 */
public class ExternalMapperData implements IExternalData {

    /**
     * 
     */
    private static final long serialVersionUID = 4877887839142463534L;

    private ExternalMapperUiProperties uiProperties = new ExternalMapperUiProperties();

    private List<ExternalMapperTable> inputTables = new ArrayList<ExternalMapperTable>(0);

    private List<ExternalMapperTable> outputTables = new ArrayList<ExternalMapperTable>(0);

    private List<ExternalMapperTable> varsTables = new ArrayList<ExternalMapperTable>(0);

    public List<ExternalMapperTable> getInputTables() {
        return this.inputTables;
    }

    public void setInputTables(List<ExternalMapperTable> tables) {
        this.inputTables = tables;
    }

    public List<ExternalMapperTable> getOutputTables() {
        return this.outputTables;
    }

    public void setOutputTables(List<ExternalMapperTable> outputTables) {
        this.outputTables = outputTables;
    }

    public List<ExternalMapperTable> getVarsTables() {
        return this.varsTables;
    }

    public void setVarsTables(List<ExternalMapperTable> varsTables) {
        this.varsTables = varsTables;
    }

    public ExternalMapperUiProperties getUiProperties() {
        return this.uiProperties;
    }

    public void setUiProperties(ExternalMapperUiProperties layoutProperties) {
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
        ExternalMapperData cloned = (ExternalMapperData) super.clone();
        cloned.uiProperties = (ExternalMapperUiProperties) uiProperties.clone();
        cloned.inputTables = (List<ExternalMapperTable>) ((ArrayList) inputTables).clone();
        int inputTablesListSize = inputTables.size();
        for (int i = 0; i < inputTablesListSize; i++) {
            cloned.inputTables.set(i, (ExternalMapperTable) cloned.inputTables.get(i).clone());
        }
        cloned.outputTables = (List<ExternalMapperTable>) ((ArrayList) outputTables).clone();
        int listSizeoutputTables = outputTables.size();
        for (int i = 0; i < listSizeoutputTables; i++) {
            cloned.outputTables.set(i, (ExternalMapperTable) cloned.outputTables.get(i).clone());
        }
        cloned.varsTables = (List<ExternalMapperTable>) ((ArrayList) varsTables).clone();
        int listSizevarsTables = varsTables.size();
        for (int i = 0; i < listSizevarsTables; i++) {
            cloned.varsTables.set(i, (ExternalMapperTable) cloned.varsTables.get(i).clone());
        }
        return cloned;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.inputTables == null) ? 0 : this.inputTables.hashCode());
        result = prime * result + ((this.outputTables == null) ? 0 : this.outputTables.hashCode());
        result = prime * result + ((this.varsTables == null) ? 0 : this.varsTables.hashCode());
        return result;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final ExternalMapperData other = (ExternalMapperData) obj;
        if (this.inputTables == null) {
            if (other.inputTables != null)
                return false;
        } else if (!this.inputTables.equals(other.inputTables))
            return false;
        if (this.outputTables == null) {
            if (other.outputTables != null)
                return false;
        } else if (!this.outputTables.equals(other.outputTables))
            return false;
        if (this.varsTables == null) {
            if (other.varsTables != null)
                return false;
        } else if (!this.varsTables.equals(other.varsTables))
            return false;
        return true;
    }

    
    
}
