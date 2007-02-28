// ============================================================================
//
// Talend Community Edition
//
// Copyright (C) 2006 Talend – www.talend.com
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
package org.talend.designer.business.diagram.custom.edit.policies;

import java.util.Iterator;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.UnexecutableCommand;
import org.eclipse.gmf.runtime.diagram.ui.commands.ICommandProxy;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.DiagramDragDropEditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.requests.DropObjectsRequest;
import org.talend.designer.business.diagram.custom.commands.CreateAssignmentCommand;
import org.talend.designer.business.diagram.custom.util.ElementHelper;
import org.talend.designer.business.model.business.BusinessItem;

/**
 * DOC mhelleboid class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 */
public class BusinessItemDragDropEditPolicy extends DiagramDragDropEditPolicy {

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.gmf.runtime.diagram.ui.editpolicies.DiagramDragDropEditPolicy#getDropObjectsCommand(org.eclipse.gmf.runtime.diagram.ui.requests.DropObjectsRequest)
     */
    @Override
    public Command getDropObjectsCommand(DropObjectsRequest dropObjectsRequest) {
        BusinessItem businessItem = new ElementHelper().getElement(getHost());

        if (businessItem != null) {
            CreateAssignmentCommand createAssignmentCommand = new CreateAssignmentCommand(
                    ((IGraphicalEditPart) getHost()).getEditingDomain());
            createAssignmentCommand.setBusinessItem(businessItem);

            // PTODO mhelleboid create multiples commands
            for (Iterator iter = dropObjectsRequest.getObjects().iterator(); iter.hasNext();) {
                Object talendItem = (Object) iter.next();
                createAssignmentCommand.setItem(talendItem);
            }

            return new ICommandProxy(createAssignmentCommand);
        }

        return UnexecutableCommand.INSTANCE;
    }
}
