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
package org.talend.repository.model.migration;

import java.io.IOException;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.talend.commons.exception.PersistenceException;
import org.talend.core.model.properties.ProcessItem;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.IRepositoryObject;
import org.talend.designer.core.model.utils.emf.talendfile.ConnectionType;
import org.talend.designer.core.model.utils.emf.talendfile.ElementParameterType;
import org.talend.designer.core.model.utils.emf.talendfile.NodeType;
import org.talend.repository.model.ProxyRepositoryFactory;

/**
 * DOC smallet class global comment. Detailled comment <br/>
 * 
 * $Id: talend.epf 1 2006-09-29 17:06:40 +0000 (ven., 29 sept. 2006) nrousseau $
 * 
 */
public class RenameComponentAction {

    public static void rename(String oldName, String newName) throws PersistenceException, IOException, CoreException {
        ProxyRepositoryFactory factory = ProxyRepositoryFactory.getInstance();

        List<IRepositoryObject> list = factory.getAll(ERepositoryObjectType.PROCESS, true);

        boolean modified;
        for (IRepositoryObject mainobject : list) {
            List<IRepositoryObject> allVersion = factory.getAllVersion(mainobject.getId());
            for (IRepositoryObject object : allVersion) {
                modified = false;
                ProcessItem item = (ProcessItem) object.getProperty().getItem();
                for (Object o : item.getProcess().getNode()) {
                    NodeType currentNode = (NodeType) o;
                    if (currentNode.getComponentName().equals(oldName)) {
                        currentNode.setComponentName(newName);
                        String oldNodeUniqueName = getNodeUniqueName(currentNode);
                        String newNodeUniqueName = oldNodeUniqueName.replaceFirst(oldName, newName);
                        replaceAllInAllNodesParameterValue(item, oldNodeUniqueName, newNodeUniqueName);
                        modified = true;
                    }
                }
                if (modified) {
                    factory.save(item);
                }
            }
        }
    }

    private static void replaceAllInAllNodesParameterValue(ProcessItem item, String oldName, String newName) {
        for (Object o : item.getProcess().getNode()) {
            NodeType nt = (NodeType) o;
            replaceInNodeParameterValue(nt, oldName, newName);
        }
        for (Object o : item.getProcess().getConnection()) {
            ConnectionType currentConnection = (ConnectionType) o;
            if (currentConnection.getSource().equals(oldName)) {
                currentConnection.setSource(newName);
            }
            if (currentConnection.getTarget().equals(oldName)) {
                currentConnection.setTarget(newName);
            }
        }
    }

    private static void replaceInNodeParameterValue(NodeType node, String oldName, String newName) {
        for (Object o : node.getElementParameter()) {
            ElementParameterType t = (ElementParameterType) o;
            t.setValue(t.getValue().replaceAll(oldName, newName));
        }
    }

    private static String getNodeUniqueName(NodeType node) {
        for (Object o : node.getElementParameter()) {
            ElementParameterType t = (ElementParameterType) o;
            if (t.getName().equals("UNIQUE_NAME")) {
                return t.getValue();
            }
        }
        return null;
    }

}
