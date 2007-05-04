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
package org.talend.repository.model.migration;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.talend.core.model.process.UniqueNodeNameGenerator;
import org.talend.designer.core.model.utils.emf.talendfile.ColumnType;
import org.talend.designer.core.model.utils.emf.talendfile.ElementParameterType;
import org.talend.designer.core.model.utils.emf.talendfile.ElementValueType;
import org.talend.designer.core.model.utils.emf.talendfile.MetadataType;
import org.talend.designer.core.model.utils.emf.talendfile.NodeType;
import org.talend.designer.core.model.utils.emf.talendfile.ProcessType;
import org.talend.designer.core.model.utils.emf.talendfile.TalendFileFactory;

/**
 * DOC smallet class global comment. Detailled comment <br/>
 * 
 * TODO SML/NRO Move into org.talend.core ?
 * 
 * $Id: talend.epf 1 2006-09-29 17:06:40 +0000 (ven., 29 sept. 2006) nrousseau $
 * 
 */
public class ComponentUtilities {

    private static final String CASE_SENSITIVE = "CASE_SENSITIVE"; //$NON-NLS-1$

    private static final String KEY_ATTRIBUTE = "KEY_ATTRIBUTE"; //$NON-NLS-1$

    private static final String SCHEMA_COLUMN = "SCHEMA_COLUMN"; //$NON-NLS-1$

    private static final String UNIQUE_NAME = "UNIQUE_NAME"; //$NON-NLS-1$

    public static String getNodePropertyValue(NodeType node, String property) {
        ElementParameterType prop = getNodeProperty(node, property);
        if (prop == null) {
            return null;
        } else {
            return prop.getValue();
        }
    }

    private static ElementParameterType getNodeProperty(NodeType node, String property) {
        for (Object o : node.getElementParameter()) {
            ElementParameterType t = (ElementParameterType) o;
            if (t.getName().equals(property)) {
                return t;
            }
        }
        return null;
    }

    public static void addNodeProperty(NodeType node, String name, String field) {
        ElementParameterType property = TalendFileFactory.eINSTANCE.createElementParameterType();
        property.setName(name);
        property.setField(field);
        node.getElementParameter().add(property);
    }

    public static void setNodeProperty(NodeType node, String name, List<ElementValueType> values) {
        ElementParameterType property = getNodeProperty(node, name);

        for (ElementValueType value : values) {
            property.getElementValue().add(value);
        }
    }

    public static void setNodeValue(NodeType node, String name, String value) {
        ElementParameterType property = getNodeProperty(node, name);
        property.setValue(value);
    }

    public static void removeNodeProperty(NodeType node, String property) {
        // TODO SML Use getNodeProperty
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
        return ComponentUtilities.getNodePropertyValue(node, UNIQUE_NAME); //$NON-NLS-1$
    }

    public static void setNodeUniqueName(NodeType node, String newName) {
        ElementParameterType t = getNodeProperty(node, UNIQUE_NAME);
        t.setValue(newName);
    }

    public static void replaceInNodeParameterValue(NodeType node, String oldName, String newName) {
        String oldName2 = "\\b" + oldName + "\\b"; //$NON-NLS-1$ //$NON-NLS-2$
        for (Object o : node.getElementParameter()) {
            ElementParameterType t = (ElementParameterType) o;
            String value = t.getValue();
            if (value != null) {
                String replaceAll = value.replaceAll(oldName2, newName);
                t.setValue(replaceAll);
            }
        }
    }

    public static String generateUniqueNodeName(String baseName, ProcessType process) {
        List<String> uniqueNodeNameList = new ArrayList<String>();
        for (Object o : process.getNode()) {
            NodeType nt = (NodeType) o;
            uniqueNodeNameList.add(getNodeUniqueName(nt));
        }
        return UniqueNodeNameGenerator.generateUniqueNodeName(baseName, uniqueNodeNameList);
    }

    public static List<ElementValueType> createtUniqRowV2UniqueKey(NodeType node) {
        // TODO SML Move in a more specific class
        List<ElementValueType> values = new ArrayList<ElementValueType>();
        TalendFileFactory fileFact = TalendFileFactory.eINSTANCE;

        MetadataType object = (MetadataType) node.getMetadata().get(0);

        for (Object o : object.getColumn()) {
            ColumnType tagada = (ColumnType) o;
            ElementValueType elementValue = fileFact.createElementValueType();
            elementValue.setElementRef(SCHEMA_COLUMN);
            elementValue.setValue(tagada.getName());
            values.add(elementValue);

            ElementValueType elementValue2 = fileFact.createElementValueType();
            elementValue2.setElementRef(KEY_ATTRIBUTE);
            elementValue2.setValue(new Boolean(tagada.isKey()).toString());
            values.add(elementValue2);

            ElementValueType elementValue3 = fileFact.createElementValueType();
            elementValue3.setElementRef(CASE_SENSITIVE);
            elementValue3.setValue("false"); //$NON-NLS-1$
            values.add(elementValue3);
        }
        return values;
    }
}
