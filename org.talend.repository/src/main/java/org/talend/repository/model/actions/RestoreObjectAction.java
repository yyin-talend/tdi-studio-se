// ============================================================================
//
// Copyright (C) 2006-2007 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the  agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
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
