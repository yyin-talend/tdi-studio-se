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
package org.talend.sqlbuilder.erdiagram.ui.commands;

import java.util.List;

import org.eclipse.gef.commands.Command;
import org.talend.sqlbuilder.erdiagram.ui.nodes.Column;
import org.talend.sqlbuilder.erdiagram.ui.nodes.Relation;

/**
 * DOC qzhang  class global comment. Detailled comment
 * <br/>
 *
 * $Id: talend.epf 1 2006-09-29 17:06:40 +0000 (ææäº, 29 ä¹æ 2006) nrousseau $
 *
 */
public class RelationCreateCommand extends Command {

    protected Relation relation;

    protected Column source;

    protected Column target = null;

    public RelationCreateCommand(Column source) {
        this.source = source;
    }

    public Column getSource() {
        return source;
    }

    public void setSource(Column source) {
        this.source = source;
    }

    public Column getTarget() {
        return target;
    }

    public void setTarget(Column target) {
        this.target = target;
    }

    @Override
    public boolean canExecute() {
        if (target != null) {
            if (source.equals(target) || target.getElementName().equals("*") //$NON-NLS-1$
                    || source.getTable().equals(target.getTable())) {
                return false;
            }
            List<Relation> relations = this.source.getOutputs();
            for (Relation rel : relations) {
                if (rel.getTarget().equals(target)) {
                    return false;
                }
            }
            relations = this.target.getInputs();
            for (Relation rel : relations) {
                if (rel.getSource().equals(source)) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void execute() {
        relation = new Relation(source, target);
    }

}
