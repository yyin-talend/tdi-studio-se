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
public class RelationReconnectionCommand extends Command {

	private Column newSource;

    private Column newTarget;

    private Column oldSource;

    private Column oldTarget;
    
	public RelationReconnectionCommand(Relation relation) {
		
	}

	public Column getNewSource() {
		return newSource;
	}

	public void setNewSource(Column newSource) {
		this.newSource = newSource;
	}

	public Column getNewTarget() {
		return newTarget;
	}

	public void setNewTarget(Column newTarget) {
		this.newTarget = newTarget;
	}

	public Column getOldSource() {
		return oldSource;
	}

	public void setOldSource(Column oldSource) {
		this.oldSource = oldSource;
	}

	public Column getOldTarget() {
		return oldTarget;
	}

	public void setOldTarget(Column oldTarget) {
		this.oldTarget = oldTarget;
	}
}
