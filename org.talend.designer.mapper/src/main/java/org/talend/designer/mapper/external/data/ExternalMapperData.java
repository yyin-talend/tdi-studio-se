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
package org.talend.designer.mapper.external.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * DOC amaumont class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 */
public class ExternalMapperData implements Serializable {

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

}
