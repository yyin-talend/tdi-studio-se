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
package org.talend.sqlbuilder.erdiagram.ui.parts;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.XYLayoutEditPolicy;
import org.eclipse.gef.requests.CreateRequest;
import org.talend.sqlbuilder.erdiagram.ui.commands.EditTableCommand;
import org.talend.sqlbuilder.erdiagram.ui.nodes.Table;

/**
 * qzhang classes Edit policy of the Diagram that will allow to move the objects on it and create nodes. <br/>
 * 
 * $Id: ProcessLayoutEditPolicy.java 301 2006-11-02 13:10:03 +0000 (星期四, 02 十一月 2006) smallet $
 * 
 */
public class ErDiagramLayoutEditPolicy extends XYLayoutEditPolicy {

    /**
     * DOC admin ErDiagramLayoutEditPolicy constructor comment.
     */
    public ErDiagramLayoutEditPolicy() {

    }

    // ------------------------------------------------------------------------
    // Overridden from ConstrainedLayoutEditPolicy

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.gef.editpolicies.ConstrainedLayoutEditPolicy#createChildEditPolicy(org.eclipse.gef.EditPart)
     */
    protected EditPolicy createChildEditPolicy(final EditPart child) {
        // ProcessResizableEditPolicy policy = new ProcessResizableEditPolicy();
        // policy.setResizeDirections(0);
        return super.createChildEditPolicy(child);
    }

    // ------------------------------------------------------------------------
    // Abstract methods from ConstrainedLayoutEditPolicy

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.gef.editpolicies.ConstrainedLayoutEditPolicy#createAddCommand(org.eclipse.gef.EditPart,
     * java.lang.Object)
     */
    protected Command createAddCommand(final EditPart child, final Object constraint) {

        return null; // no support for adding
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.gef.editpolicies.ConstrainedLayoutEditPolicy#createChangeConstraintCommand(org.eclipse.gef.EditPart,
     * java.lang.Object)
     */
    public Command createChangeConstraintCommand(final EditPart child, final Object constraint) {
        // return a command to move the part to the location given by the
        // constraint
        if (child instanceof TablePart) {
            EditTableCommand locationCommand = new EditTableCommand((Table) child.getModel(), ((Rectangle) constraint));
            return locationCommand;
        } 

        return null;
    }

    // ------------------------------------------------------------------------
    // Abstract methods from LayoutEditPolicy

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.gef.editpolicies.LayoutEditPolicy#getCreateCommand(org.eclipse.gef.requests.CreateRequest)
     */
    protected Command getCreateCommand(final CreateRequest request) {
//        if (request.getNewObject() instanceof Table) {
//            Rectangle rectangle = new Rectangle(0, 0, 100, 50);
//            rectangle = (Rectangle) getConstraintFor(request);
//            CreateTableCommand cmd = new CreateTableCommand((ErDiagram) this.getHost().getModel(),
//                    (Table) request.getNewObject(), rectangle.getLocation());
//            return cmd;
//        }
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.gef.editpolicies.LayoutEditPolicy#getDeleteDependantCommand(org.eclipse.gef.Request)
     */
    protected Command getDeleteDependantCommand(final Request request) {
        return null; // no support for deleting a dependant
    }
}
