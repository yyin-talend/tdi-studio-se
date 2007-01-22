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
package org.talend.sqlbuilder.erdiagram.ui.editor;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartFactory;
import org.talend.sqlbuilder.erdiagram.ui.nodes.Column;
import org.talend.sqlbuilder.erdiagram.ui.nodes.ErDiagram;
import org.talend.sqlbuilder.erdiagram.ui.nodes.Relation;
import org.talend.sqlbuilder.erdiagram.ui.nodes.Table;
import org.talend.sqlbuilder.erdiagram.ui.parts.ColumnPart;
import org.talend.sqlbuilder.erdiagram.ui.parts.ErDiagramPart;
import org.talend.sqlbuilder.erdiagram.ui.parts.RelationPart;
import org.talend.sqlbuilder.erdiagram.ui.parts.TablePart;

/**
 * The qzhang  ErDiagramPartFactory will create an EditPart factory for each model object that is created in the diagram. <br/>
 * 
 * $Id: PartFactory.java 1 2006-09-29 17:06:40 +0000 (星期五, 29 九月 2006) nrousseau $
 * 
 */
public class ErDiagramPartFactory implements EditPartFactory {

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.gef.EditPartFactory#createEditPart(org.eclipse.gef.EditPart, java.lang.Object)
     */
    public EditPart createEditPart(EditPart context, Object model) {
        EditPart part = null;
        if (model instanceof ErDiagram) {
            part = new ErDiagramPart();
        } else if (model instanceof Column) {
            part = new ColumnPart();
        } else if (model instanceof Table) {
            part = new TablePart();
        } else if (model instanceof Relation) {
            part = new RelationPart();

        } else {
            return null;
        }
        // tell the newly created part about the model object
        part.setModel(model);

        return part;
    }
}
