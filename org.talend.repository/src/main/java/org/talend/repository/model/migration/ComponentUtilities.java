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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.talend.core.model.process.UniqueNodeNameGenerator;
import org.talend.core.model.properties.ProcessItem;
import org.talend.designer.core.model.utils.emf.talendfile.ElementParameterType;
import org.talend.designer.core.model.utils.emf.talendfile.NodeType;

/**
 * DOC smallet class global comment. Detailled comment <br/>
 * 
 * TODO SML/NRO Move into org.talend.core ?
 * 
 * $Id: talend.epf 1 2006-09-29 17:06:40 +0000 (ven., 29 sept. 2006) nrousseau $
 * 
 */
public class ComponentUtilities {

    public static String getNodeProperty(NodeType node, String property) {
        for (Object o : node.getElementParameter()) {
            ElementParameterType t = (ElementParameterType) o;
            if (t.getName().equals(property)) {
                return t.getValue();
            }
        }
        return null;
    }

    public static void removeNodeProperty(NodeType node, String property) {
        EList elementParameter = node.getElementParameter();
        Iterator iterator = elementParameter.iterator();
        for (Object o = iterator.next(); iterator.hasNext(); o = iterator.next()) {
            ElementParameterType t = (ElementParameterType) o;
            if (t.getName().equals(property)) {
                iterator.remove();
            }
        }
    }

    public static String getNodeUniqueName(NodeType node) {
        return ComponentUtilities.getNodeProperty(node, "UNIQUE_NAME"); //$NON-NLS-1$
    }

    public static void replaceInNodeParameterValue(NodeType node, String oldName, String newName) {
        for (Object o : node.getElementParameter()) {
            ElementParameterType t = (ElementParameterType) o;
            String value = t.getValue();
            if (value != null) {
                t.setValue(value.replaceAll(oldName, newName));
            }
        }
    }

    public static String generateUniqueNodeName(String baseName, ProcessItem process) {
        List<String> uniqueNodeNameList = new ArrayList<String>();
        for (Object o : process.getProcess().getNode()) {
            NodeType nt = (NodeType) o;
            uniqueNodeNameList.add(getNodeUniqueName(nt));
        }
        return UniqueNodeNameGenerator.generateUniqueNodeName(baseName, uniqueNodeNameList);
    }
}
