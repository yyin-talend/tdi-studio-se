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
package org.talend.designer.mapper.model;

import java.util.List;

import org.talend.designer.mapper.model.table.InputTable;
import org.talend.designer.mapper.model.table.OutputTable;
import org.talend.designer.mapper.model.table.VarsTable;

/**
 * DOC amaumont class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 */
public class MapperModel {

    private List<InputTable> inputDataMapTables;

    private List<OutputTable> outputDataMapTables;

    private List<VarsTable> varsDataMapTables;

    public MapperModel(List<InputTable> inputDataMapTables, List<OutputTable> outputDataMapTables, List<VarsTable> varsDataMapTables) {
        super();
        this.inputDataMapTables = inputDataMapTables;
        this.outputDataMapTables = outputDataMapTables;
        this.varsDataMapTables = varsDataMapTables;
    }

    public List<InputTable> getInputDataMapTables() {
        return this.inputDataMapTables;
    }

    public void setInputDataMapTables(List<InputTable> inputDataMapTables) {
        this.inputDataMapTables = inputDataMapTables;
    }

    public List<OutputTable> getOutputDataMapTables() {
        return this.outputDataMapTables;
    }

    public void setOutputDataMapTables(List<OutputTable> outputDataMapTables) {
        this.outputDataMapTables = outputDataMapTables;
    }

    public List<VarsTable> getVarsDataMapTables() {
        return this.varsDataMapTables;
    }

    public void setVarsDataMapTables(List<VarsTable> varsDataMapTables) {
        this.varsDataMapTables = varsDataMapTables;
    }

}
