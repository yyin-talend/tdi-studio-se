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

import org.eclipse.core.runtime.IPath;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.exception.PersistenceException;
import org.talend.core.model.properties.Item;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.IRepositoryObject;
import org.talend.repository.model.ProxyRepositoryFactory;
import org.talend.repository.model.RepositoryNode;
import org.talend.repository.model.RepositoryNodeUtilities;
import org.talend.repository.model.RepositoryStatus;
import org.talend.repository.model.RepositoryNode.ENodeType;
import org.talend.repository.model.RepositoryNode.EProperties;

/**
 * DOC smallet class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 */
public class CopyObjectAction {

    private static CopyObjectAction singleton = new CopyObjectAction();

    public static CopyObjectAction getInstance() {
        return singleton;
    }

    public boolean validateAction(RepositoryNode sourceNode, RepositoryNode targetNode) {
        if (sourceNode == null) {
            return false;
        }

        // Cannot copy folder or system folder :
        if (sourceNode.getType() != ENodeType.REPOSITORY_ELEMENT) {
            return false;
        }

        IRepositoryObject objectToCopy = sourceNode.getObject();

        // Cannot move logically deleted objects :
        ProxyRepositoryFactory factory = ProxyRepositoryFactory.getInstance();
        try {
            objectToCopy = factory.getLastVersion(objectToCopy.getId());
            if (objectToCopy == null || factory.getStatus(objectToCopy) == RepositoryStatus.DELETED) {
                return false;
            }
        } catch (PersistenceException e) {
            ExceptionHandler.process(e);
            return false;
        }

        // Special rule : temp ?
        if (targetNode == null) {
            return true;
        }

        switch (targetNode.getType()) {
        case SIMPLE_FOLDER:
        case SYSTEM_FOLDER:
            return ((ERepositoryObjectType) targetNode.getProperties(EProperties.CONTENT_TYPE)) == objectToCopy.getType();
        default:
            return false;
        }
    }

    public void execute(RepositoryNode sourceNode, RepositoryNode targetNode) throws Exception {
        if (!validateAction(sourceNode, targetNode)) {
            return;
        }

        IPath targetPath = RepositoryNodeUtilities.getPath(targetNode);
        ProxyRepositoryFactory factory = ProxyRepositoryFactory.getInstance();

        if (sourceNode.getType().equals(ENodeType.REPOSITORY_ELEMENT)) {
            // Source is an repository element :
            Item originalItem = sourceNode.getObject().getProperty().getItem();
            factory.copy(originalItem, targetPath);
        }
    }
}
