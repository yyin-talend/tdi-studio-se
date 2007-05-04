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
package org.talend.designer.rowgenerator.external.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * qzhang class global comment. Detailled comment <br/>
 * 
 * $Id: ExternalRowGeneratorData.java,v 1.3 2007/01/31 05:20:52 pub Exp $
 * 
 */
public class ExternalRowGeneratorData implements Serializable {

    private List<ExternalRowGenTable> outputTables = new ArrayList<ExternalRowGenTable>();

    /**
     * 
     */
    private static final long serialVersionUID = 21232132141L;

    private ExternalRowGeneratorUiProperties uiProperties = new ExternalRowGeneratorUiProperties();

    public ExternalRowGeneratorUiProperties getUiProperties() {
        return this.uiProperties;
    }

    public void setUiProperties(ExternalRowGeneratorUiProperties uiProperties) {
        this.uiProperties = uiProperties;
    }

    public List<ExternalRowGenTable> getOutputTables() {
        return outputTables;
    }

    /**
     * Sets the outputTables.
     * 
     * @param outputTables the outputTables to set
     */
    public void setOutputTables(List<ExternalRowGenTable> outputTables) {
        this.outputTables = outputTables;
    }

}
