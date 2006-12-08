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

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.exception.PersistenceException;
import org.talend.commons.utils.workbench.resources.ResourceUtils;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.IRepositoryObject;
import org.talend.repository.model.BinRepositoryNode;
import org.talend.repository.model.ERepositoryStatus;
import org.talend.repository.model.IProxyRepositoryFactory;
import org.talend.repository.model.ProxyRepositoryFactory;
import org.talend.repository.model.RepositoryNode;
import org.talend.repository.model.RepositoryNodeUtilities;
import org.talend.repository.model.RepositoryNode.ENodeType;
import org.talend.repository.model.RepositoryNode.EProperties;

/**
 * DOC smallet class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 */
public class MoveObjectAction {

    private static Logger log = Logger.getLogger(MoveObjectAction.class);

    private static MoveObjectAction singleton = new MoveObjectAction();

    public static MoveObjectAction getInstance() {
        return singleton;
    }

    public boolean validateAction(RepositoryNode sourceNode, RepositoryNode targetNode) {
        if (sourceNode == null) {
            return false;
        }

        if (targetNode == null) {
            switch (sourceNode.getType()) {
            case REPOSITORY_ELEMENT:
            case SIMPLE_FOLDER:
                return true;
            default:
                return false;
            }
        }
        if (sourceNode.equals(targetNode)) {
            return false;
        }

        IPath targetPath = RepositoryNodeUtilities.getPath(targetNode);
        IPath sourcePath = RepositoryNodeUtilities.getPath(sourceNode);
        if (sourceNode.getType() == ENodeType.REPOSITORY_ELEMENT) {
            IRepositoryObject objectToCopy = sourceNode.getObject();

            if (!ResourceUtils.isCorrectDestination(sourcePath, targetPath, false)) {
                return false;
            }

            switch (targetNode.getType()) {
            case SYSTEM_FOLDER:
            case SIMPLE_FOLDER:
                return ((ERepositoryObjectType) targetNode.getProperties(EProperties.CONTENT_TYPE)) == objectToCopy.getType();
            case STABLE_SYSTEM_FOLDER:
                return targetNode instanceof BinRepositoryNode;
            default:
                return false;
            }
        } else if (sourceNode.getType() == ENodeType.SIMPLE_FOLDER) {
            if (targetNode.getType() != ENodeType.SIMPLE_FOLDER && targetNode.getType() != ENodeType.SYSTEM_FOLDER) {
                return false;
            }

            ERepositoryObjectType sourceType = (ERepositoryObjectType) sourceNode.getProperties(EProperties.CONTENT_TYPE);
            if (((ERepositoryObjectType) targetNode.getProperties(EProperties.CONTENT_TYPE)) != sourceType) {
                return false;
            }

            if (!ResourceUtils.isCorrectDestination(sourcePath, targetPath, true)) {
                return false;
            }

            IProxyRepositoryFactory factory = ProxyRepositoryFactory.getInstance();
            try {
                if (!factory.isPathValid(sourceType, targetPath, sourcePath.lastSegment())) {
                    return false;
                }
            } catch (PersistenceException e) {
                ExceptionHandler.process(e);
                return false;
            }

            return true;
        } else {
            return false;
        }
    }

    public void execute(RepositoryNode sourceNode, RepositoryNode targetNode) throws Exception {
        if (!validateAction(sourceNode, targetNode)) {
            log.debug("Cannot move [" + sourceNode + "] to " + targetNode);
            return;
        }

        IPath targetPath = (targetNode == null ? new Path("") : RepositoryNodeUtilities.getPath(targetNode));
        IPath sourcePath = RepositoryNodeUtilities.getPath(sourceNode);

        IProxyRepositoryFactory factory = ProxyRepositoryFactory.getInstance();

        if (sourceNode.getType().equals(ENodeType.REPOSITORY_ELEMENT)) {
            // Source is an repository element :
            IRepositoryObject objectToMove = sourceNode.getObject();

            if (targetNode instanceof BinRepositoryNode) {
                // Move in the recycle bin :
                factory.deleteObjectLogical(objectToMove);
            } else {
                if (factory.getStatus(objectToMove) == ERepositoryStatus.DELETED) {
                    // Restore :
                    factory.restoreObject(objectToMove, targetPath);
                } else {
                    // Move :
                    factory.moveObject(objectToMove, targetPath);
                }
            }
        } else if (sourceNode.getType().equals(ENodeType.SIMPLE_FOLDER)) {
            // Source is a folder :
            ERepositoryObjectType sourceType = (ERepositoryObjectType) sourceNode.getProperties(EProperties.CONTENT_TYPE);
            factory.moveFolder(sourceType, sourcePath, targetPath);
        }
    }
}
