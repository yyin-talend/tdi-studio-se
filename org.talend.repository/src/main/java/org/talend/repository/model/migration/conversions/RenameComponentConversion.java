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
package org.talend.repository.model.migration.conversions;

import org.eclipse.emf.common.util.EList;
import org.talend.designer.core.model.utils.emf.talendfile.ConnectionType;
import org.talend.designer.core.model.utils.emf.talendfile.MetadataType;
import org.talend.designer.core.model.utils.emf.talendfile.NodeType;
import org.talend.designer.core.model.utils.emf.talendfile.ProcessType;
import org.talend.repository.model.migration.ComponentUtilities;

/**
 * DOC smallet class global comment. Detailled comment <br/>
 * 
 * $Id: talend.epf 1 2006-09-29 17:06:40 +0000 (ven., 29 sept. 2006) nrousseau $
 * 
 */
public class RenameComponentConversion implements IComponentConversion {

    private String newName;

    public RenameComponentConversion(String newName) {
        super();
        this.newName = newName;
    }

    public void transform(NodeType node) {
        node.setComponentName(newName);
        ProcessType item = (ProcessType) node.eContainer();
        String oldNodeUniqueName = ComponentUtilities.getNodeUniqueName(node);
        String newNodeUniqueName = ComponentUtilities.generateUniqueNodeName(newName, item);
        ComponentUtilities.setNodeUniqueName(node, newNodeUniqueName);
        replaceAllInAllNodesParameterValue(item, oldNodeUniqueName, newNodeUniqueName);
    }

    private static void replaceAllInAllNodesParameterValue(ProcessType item, String oldName, String newName) {
        for (Object o : item.getNode()) {
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
        for (Object o : item.getConnection()) {
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

    public String getNewName() {
        return this.newName;
    }

    public void setNewName(String newName) {
        this.newName = newName;
    }
}
