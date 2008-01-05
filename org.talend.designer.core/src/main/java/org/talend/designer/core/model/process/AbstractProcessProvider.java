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
package org.talend.designer.core.model.process;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.talend.core.model.process.INode;
import org.talend.designer.core.ui.editor.nodes.Node;
import org.talend.designer.core.ui.editor.process.Process;

/**
 * DOC qzhang class global comment. Detailled comment
 */
public abstract class AbstractProcessProvider {

    public static final String EXTENSION_ID = "org.talend.designer.core.process_provider";

    public static final String ATTR_CLASS = "class";

    public static final String ATTR_PID = "pluginId";

    public abstract org.talend.designer.core.ui.editor.process.Process buildNewGraphicProcess(Node node);

    public static AbstractProcessProvider findProcessProviderFromPID(String pid) {
        IConfigurationElement[] elems = Platform.getExtensionRegistry().getConfigurationElementsFor(EXTENSION_ID);
        for (IConfigurationElement elem : elems) {
            String attribute = elem.getAttribute(ATTR_PID);
            if (attribute.equals(pid)) {
                try {
                    return (AbstractProcessProvider) elem.createExecutableExtension(ATTR_CLASS);
                } catch (CoreException ex) {
                    ex.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * DOC qzhang Comment method "openNewProcessEditor".
     */
    public void openNewProcessEditor(Node node) {
        // do nothing.
    }

    /**
     * DOC qzhang Comment method "buildNewDataProcess".
     * 
     * @param process
     * @param node
     * @param buildCheckMap
     * @param dataProcess
     */
    public List<Node> addNewDataProcess(Process process, Node node, Map<INode, INode> buildCheckMap, DataProcess dataProcess) {
        // do nothing.
        return Collections.EMPTY_LIST;
    }
}
