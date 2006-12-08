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
package org.talend.repository.model.actions;

import org.talend.core.model.repository.IRepositoryObject;
import org.talend.repository.model.ERepositoryStatus;
import org.talend.repository.model.IProxyRepositoryFactory;
import org.talend.repository.model.ProxyRepositoryFactory;
import org.talend.repository.model.RepositoryNode;
import org.talend.repository.model.RepositoryNode.ENodeType;

/**
 * DOC smallet class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 */
public class RestoreObjectAction {

    private static RestoreObjectAction singleton = new RestoreObjectAction();

    public static RestoreObjectAction getInstance() {
        return singleton;
    }

    public boolean validateAction(RepositoryNode sourceNode, RepositoryNode targetNode) {
        MoveObjectAction moveObjectAction = MoveObjectAction.getInstance();
        if (!moveObjectAction.validateAction(sourceNode, targetNode)) {
            return false;
        }

        if (sourceNode.getType() == ENodeType.REPOSITORY_ELEMENT) {
            IRepositoryObject objectToRestore = sourceNode.getObject();
            IProxyRepositoryFactory factory = ProxyRepositoryFactory.getInstance();
            return factory.getStatus(objectToRestore) == ERepositoryStatus.DELETED;
        } else {
            return false;
        }
    }

    public void execute(RepositoryNode sourceNode, RepositoryNode targetNode) throws Exception {
        if (!validateAction(sourceNode, targetNode)) {
            return;
        }
        MoveObjectAction moveObjectAction = MoveObjectAction.getInstance();
        moveObjectAction.execute(sourceNode, targetNode);
    }
}
