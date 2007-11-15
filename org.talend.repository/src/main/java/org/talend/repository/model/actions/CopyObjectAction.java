// ============================================================================
//
// Copyright (C) 2006-2007 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//   
// ============================================================================
package org.talend.repository.model.actions;

import org.eclipse.core.runtime.IPath;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.exception.PersistenceException;
import org.talend.core.model.properties.Item;
import org.talend.core.model.properties.Property;
import org.talend.core.model.properties.RoutineItem;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.IRepositoryObject;
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
        IProxyRepositoryFactory factory = ProxyRepositoryFactory.getInstance();
        try {
            objectToCopy = factory.getLastVersion(objectToCopy.getId());
            if (objectToCopy == null || factory.getStatus(objectToCopy) == ERepositoryStatus.DELETED) {
                return false;
            }
        } catch (PersistenceException e) {
            ExceptionHandler.process(e);
            return false;
        }

        // Cannot copy system routines:
        if (objectToCopy.getType() == ERepositoryObjectType.ROUTINES) {
            Property property = objectToCopy.getProperty();
            RoutineItem item = (RoutineItem) property.getItem();
            return !item.isBuiltIn();
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
        IProxyRepositoryFactory factory = ProxyRepositoryFactory.getInstance();

        if (sourceNode.getType().equals(ENodeType.REPOSITORY_ELEMENT)) {
            // Source is an repository element :
            Item originalItem = sourceNode.getObject().getProperty().getItem();
            factory.copy(originalItem, targetPath);
        }
    }
}
