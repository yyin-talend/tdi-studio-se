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
package org.talend.designer.dbmap.model;

import java.util.List;

import org.talend.designer.dbmap.model.table.InputTable;
import org.talend.designer.dbmap.model.table.OutputTable;

/**
 * DOC amaumont class global comment. Detailled comment <br/>
 * 
 * $Id: MapperModel.java 898 2006-12-07 11:06:17Z amaumont $
 * 
 */
public class MapperModel {

    private List<InputTable> inputDataMapTables;

    private List<OutputTable> outputDataMapTables;

    public MapperModel(List<InputTable> inputDataMapTables, List<OutputTable> outputDataMapTables) {
        super();
        this.inputDataMapTables = inputDataMapTables;
        this.outputDataMapTables = outputDataMapTables;
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

}
