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
package org.talend.designer.core.ui.editor.cmd;

import org.talend.core.model.metadata.IMetadataTable;
import org.talend.designer.core.ui.editor.nodes.Node;
import org.talend.designer.core.ui.editor.process.Process;

/**
 * Command that will change the schema in the property. <br/>
 * 
 * $Id$
 * @deprecated
 */
public class SchemaPropertyChangeCommand extends PropertyChangeCommand {

    IMetadataTable repositoryMetadataTable;

    IMetadataTable nodeMetadataTable, oldNodeMetadataTable;

    Node node;

    public SchemaPropertyChangeCommand(Node node, String propName, Object propValue,
            IMetadataTable repositoryMetadataTable) {
        super(node, propName, propValue);
        this.node = node;
        this.repositoryMetadataTable = repositoryMetadataTable;
        nodeMetadataTable = node.getMetadataList().get(0);
        oldNodeMetadataTable = nodeMetadataTable.clone();
    }

    @Override
    public void execute() {
        nodeMetadataTable.setListColumns(repositoryMetadataTable.getListColumns());
        nodeMetadataTable.setDescription(repositoryMetadataTable.getDescription());
        super.execute();
        ((Process) node.getProcess()).checkProcess();
    }

    @Override
    public void undo() {
        nodeMetadataTable.setListColumns(oldNodeMetadataTable.getListColumns());
        nodeMetadataTable.setDescription(oldNodeMetadataTable.getDescription());
        super.undo();
        ((Process) node.getProcess()).checkProcess();
    }
}
