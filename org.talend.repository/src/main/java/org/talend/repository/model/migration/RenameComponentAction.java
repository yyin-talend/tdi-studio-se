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
import org.eclipse.emf.common.util.EList;
import org.talend.commons.exception.PersistenceException;
import org.talend.core.model.properties.ProcessItem;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.IRepositoryObject;
import org.talend.designer.core.model.utils.emf.talendfile.ConnectionType;
import org.talend.designer.core.model.utils.emf.talendfile.MetadataType;
import org.talend.designer.core.model.utils.emf.talendfile.NodeType;
import org.talend.repository.model.ProxyRepositoryFactory;
import org.talend.repository.model.migration.conversions.DefaultComponentConversion;
import org.talend.repository.model.migration.conversions.IComponentConversion;
import org.talend.repository.model.migration.filters.IComponentFilter;
import org.talend.repository.model.migration.filters.NameComponentFilter;

/**
 * DOC smallet class global comment. Detailled comment <br/>
 * 
 * $Id: talend.epf 1 2006-09-29 17:06:40 +0000 (ven., 29 sept. 2006) nrousseau $
 * 
 */
public class RenameComponentAction {

    public static void rename(String oldName, String newName) throws PersistenceException, IOException, CoreException {
        rename(oldName, newName, new NameComponentFilter(oldName), new DefaultComponentConversion());
    }

    public static void rename(String oldName, String newName, IComponentFilter filter, IComponentConversion conversion)
            throws PersistenceException, IOException, CoreException {
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

                    if (filter.accept(currentNode)) {
                        // TODO SML Manage with IConversion + Use List<IComponentConversion> ad param
                        currentNode.setComponentName(newName);
                        String oldNodeUniqueName = ComponentUtilities.getNodeUniqueName(currentNode);
                        String newNodeUniqueName = ComponentUtilities.generateUniqueNodeName(newName, item);
                        replaceAllInAllNodesParameterValue(item, oldNodeUniqueName, newNodeUniqueName);

                        conversion.transform(currentNode);

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
            ComponentUtilities.replaceInNodeParameterValue(nt, oldName, newName);
            EList metaList = nt.getMetadata();
            if (metaList != null) {
                if (!metaList.isEmpty()) {
                    MetadataType firstMeta = (MetadataType) metaList.get(0);
                    if (firstMeta.getName().equals(oldName)) {
                        firstMeta.setName(newName);
                    }
                }
            }
        }
        for (Object o : item.getProcess().getConnection()) {
            ConnectionType currentConnection = (ConnectionType) o;
            if (currentConnection.getSource().equals(oldName)) {
                currentConnection.setSource(newName);
            }
            if (currentConnection.getTarget().equals(oldName)) {
                currentConnection.setTarget(newName);
            }
            if (currentConnection.getMetaname().equals(oldName)) {
                currentConnection.setMetaname(newName);
            }
        }
    }

}
