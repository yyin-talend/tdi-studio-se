package org.talend.repository.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.talend.core.model.properties.ProcessItem;
import org.talend.designer.core.model.utils.emf.talendfile.ElementParameterType;
import org.talend.designer.core.model.utils.emf.talendfile.NodeType;

public class EmfModelUtils {

	private EmfModelUtils() {
	}

	public static final NodeType getComponentByName(ProcessItem processItem, String... names) {
        for (Object o : processItem.getProcess().getNode()) {
            if (o instanceof NodeType) {
            	for (String name : names) {
                    if (((NodeType) o).getComponentName().equals(name)) {
                        return (NodeType) o;
                    }
            	}
            }
        }
        return null;
    }

	public static final Collection<NodeType> getComponentsByName(ProcessItem processItem, String... names) {
        List<NodeType> result = new ArrayList<NodeType>();
        for (Object o : processItem.getProcess().getNode()) {
            if (o instanceof NodeType) {
            	NodeType node = (NodeType) o;
            	// skip deactivated components
            	if (null == findElementParameterByName("ACTIVATE", node)) {
                	for (String name : names) {
                        if (node.getComponentName().equals(name)) {
                            result.add(node);
                        }
                	}
            	}
            }
        }
        return result;
    }

    /**
     * Compute Text field parameter value with a given parameter name
     *
     * @param paramName
     * @param elementParameterTypes
     * @return
     */
	public static final String computeTextElementValue(String paramName, NodeType node) {
        ElementParameterType cpType = findElementParameterByName(paramName, node);
        if (cpType == null) {
            return "";
        }
        return cpType.getValue();
    }

    /**
     * Compute check field parameter value with a given parameter name
     *
     * @param paramName
     * @param elementParameterTypes
     * @return
     */
	public static final boolean computeCheckElementValue(String paramName, NodeType node) {
        ElementParameterType cpType = findElementParameterByName(paramName, node);
        if (cpType == null) {
            return false;
        }
        return Boolean.parseBoolean(cpType.getValue());
    }

	private static final  ElementParameterType findElementParameterByName(String paramName, NodeType node) {
        for (Object obj : node.getElementParameter()) {
            ElementParameterType cpType = (ElementParameterType) obj;
            if (paramName.equals(cpType.getName())) {
                return cpType;
            }
        }
        return null;
    }


}
