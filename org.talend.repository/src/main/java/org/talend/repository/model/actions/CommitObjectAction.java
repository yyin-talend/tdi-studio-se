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

import org.talend.commons.exception.SystemException;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.IRepositoryObject;
import org.talend.repository.model.ProxyRepositoryFactory;
import org.talend.repository.model.RepositoryNode;
import org.talend.repository.model.RepositoryStatus;

/**
 * DOC smallet class global comment. Detailled comment <br/>
 * 
 * $Id: RestoreObjectAction.java 1 2006-09-29 17:06:40 +0000 (ven., 29 sept. 2006) nrousseau $
 * 
 */
public class CommitObjectAction {

    private static CommitObjectAction singleton = new CommitObjectAction();

    public static CommitObjectAction getInstance() {
        return singleton;
    }

    public boolean validateAction(Object object) {
        if (!(object instanceof RepositoryNode)) {
            return false;
        }

        RepositoryNode sourceNode = (RepositoryNode) object;
        switch (sourceNode.getType()) {
        case REPOSITORY_ELEMENT:
            IRepositoryObject repositoryObject = sourceNode.getObject();
            if (repositoryObject.getType() == ERepositoryObjectType.METADATA_CON_TABLE) {
                return false;
            }
            ProxyRepositoryFactory proxyRepositoryFactory = ProxyRepositoryFactory.getInstance();
            return (proxyRepositoryFactory.getStatus(repositoryObject) == RepositoryStatus.LOCK_BY_USER);
        default:
            return false;
        }
    }

    public void execute(Object object) throws SystemException {
        if (object instanceof RepositoryNode) {
            RepositoryNode sourceNode = (RepositoryNode) object;
            ProxyRepositoryFactory proxyRepositoryFactory = ProxyRepositoryFactory.getInstance();
            proxyRepositoryFactory.commit(sourceNode.getObject().getProperty().getItem());
        }
    }
}
