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
package org.talend.designer.core.model.process;

import java.util.ArrayList;
import java.util.List;

import org.talend.core.model.components.IComponent;
import org.talend.core.model.components.IODataComponent;
import org.talend.core.model.metadata.IMetadataTable;
import org.talend.core.model.metadata.MetadataTable;
import org.talend.core.model.process.AbstractNode;

/**
 * Virtual node that will be used for the generated code.
 * 
 * $Id$
 * 
 */
public class DataNode extends AbstractNode {

    public DataNode(IComponent component, String uniqueName) {
        setComponentName(component.getName());
        setPluginFullName(component.getPluginFullName());
        List<IMetadataTable> metaList = new ArrayList<IMetadataTable>();
        IMetadataTable metaTable = new MetadataTable();
        metaTable.setTableName(uniqueName);
        metaList.add(metaTable);
        setMetadataList(metaList);
        setComponent(component);
        setElementParameters(component.createElementParameters(this));
        setUniqueName(uniqueName);
    }

    public DataNode() {
        // nothing
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.INode#renameMetadataColumnName(java.lang.String, java.lang.String,
     * java.lang.String)
     */
    public void metadataInputChanged(IODataComponent dataComponent, String connectionToApply) {
        // TODO Auto-generated method stub

    }

    public void metadataOutputChanged(IODataComponent dataComponent, String connectionToApply) {
        // TODO Auto-generated method stub

    }
 }
