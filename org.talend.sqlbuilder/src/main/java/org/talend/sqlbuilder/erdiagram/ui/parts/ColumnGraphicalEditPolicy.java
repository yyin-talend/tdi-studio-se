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

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.GraphicalNodeEditPolicy;
import org.eclipse.gef.requests.CreateConnectionRequest;
import org.eclipse.gef.requests.ReconnectRequest;
import org.talend.sqlbuilder.erdiagram.ui.commands.RelationCreateCommand;
import org.talend.sqlbuilder.erdiagram.ui.commands.RelationReconnectionCommand;
import org.talend.sqlbuilder.erdiagram.ui.nodes.Column;
import org.talend.sqlbuilder.erdiagram.ui.nodes.Relation;

/**
 * DOC qzhang class global comment. Detailled comment <br/>
 * 
 * $Id: talend.epf 1 2006-09-29 17:06:40 +0000 (ææäº, 29 ä¹æ 2006)
 * nrousseau $
 * 
 */
public class ColumnGraphicalEditPolicy extends GraphicalNodeEditPolicy {

	@Override
	protected Command getConnectionCompleteCommand(
			CreateConnectionRequest request) {
		RelationCreateCommand cmd = (RelationCreateCommand) request
				.getStartCommand();
		cmd.setTarget((Column) getHost().getModel());
		return cmd;
	}

	@Override
	protected Command getConnectionCreateCommand(CreateConnectionRequest request) {
		Column source = (Column) getHost().getModel();
		RelationCreateCommand command = new RelationCreateCommand(source);
		request.setStartCommand(command);
		return command;
	}

	@Override
	protected Command getReconnectSourceCommand(ReconnectRequest request) {
		Relation relation = (Relation) request.getConnectionEditPart()
				.getModel();
		Column newSource = (Column) getHost().getModel();
		RelationReconnectionCommand command = new RelationReconnectionCommand(
				relation);
		command.setNewSource(newSource);
		return command;
	}

	@Override
	protected Command getReconnectTargetCommand(ReconnectRequest request) {
		Relation relation = (Relation) request.getConnectionEditPart()
				.getModel();
		Column newTarget = (Column) getHost().getModel();
		RelationReconnectionCommand command = new RelationReconnectionCommand(
				relation);
		command.setNewTarget(newTarget);
		return command;
	}

}
