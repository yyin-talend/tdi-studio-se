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
package org.talend.sqlbuilder.erdiagram.ui.commands;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.gef.commands.Command;
import org.talend.sqlbuilder.erdiagram.ui.nodes.Column;
import org.talend.sqlbuilder.erdiagram.ui.nodes.ErDiagram;
import org.talend.sqlbuilder.erdiagram.ui.nodes.Relation;
import org.talend.sqlbuilder.erdiagram.ui.nodes.Table;

/**
 * DOC qzhang class global comment. Detailled comment <br/>
 * 
 * $Id: talend.epf 1 2006-09-29 17:06:40 +0000 (ææäº, 29 ä¹æ 2006) nrousseau $
 * 
 */
public class DeleteTableCommand extends Command {

    private ErDiagram erDiagram;

    private List<Table> tables;

    private List<Column> columns;

    /**
     * DOC admin DeleteTableCommand constructor comment.
     */
    public DeleteTableCommand(ErDiagram erDiagram, List<Table> tables) {
        this.erDiagram = erDiagram;
        this.tables = tables;
        columns = new ArrayList<Column>();
        for (Table table : tables) {
            for (Object obj : table.getColumns()) {
                if (obj instanceof Column) {
                    if (!((Column) obj).getElementName().equals("*")) {
                        columns.add((Column) obj);
                    }
                }
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.gef.commands.Command#execute()
     */
    @Override
    public void execute() {
        for (Table table : this.tables) {
            erDiagram.removeTable(table);
            for (Object obj : table.getColumns()) {
                if (obj instanceof Column) {
                    List<Relation> inputs = ((Column) obj).getInputs();
                    List<Relation> outputs = ((Column) obj).getOutputs();
                    for (Relation relation : inputs) {
                        Column preColumn = relation.getSource();
                        if (!columns.contains(preColumn)) {
                            preColumn.removeOutputRelation(relation);
                        }
                    }
                    for (Relation relation : outputs) {
                        Column preColumn = relation.getTarget();
                        if (!columns.contains(preColumn)) {
                            preColumn.removeInputRelation(relation);
                        }
                    }
                }

            }
        }
    }
}
