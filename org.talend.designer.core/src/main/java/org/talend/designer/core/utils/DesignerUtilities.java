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
package org.talend.designer.core.utils;

import java.util.ArrayList;
import java.util.List;

import org.talend.core.model.process.INode;
import org.talend.core.model.process.IProcess;
import org.talend.core.model.process.IProcess2;
import org.talend.core.model.properties.ProcessItem;
import org.talend.designer.core.model.components.EParameterName;
import org.talend.designer.core.model.utils.emf.talendfile.NodeType;
import org.talend.designer.core.ui.editor.nodes.Node;
import org.talend.designer.core.ui.editor.process.Process;
import org.talend.designer.runprocess.ItemCacheManager;

/**
 * DOC bqian class global comment. Detailled comment
 */
public class DesignerUtilities {

    private static final String TRUN_JOB = "tRunJob"; //$NON-NLS-1$

    public static boolean isTRunJobComponent(INode node) {
        return TRUN_JOB.equals(node.getComponent().getName());
    }

    public static boolean isTRunJobComponent(NodeType node) {
        return TRUN_JOB.equals(node.getComponentName());
    }

    public static IProcess2 getCorrespondingProcessFromTRunjob(INode node) {
        if (DesignerUtilities.isTRunJobComponent(node)) {
            Node concreteNode = (Node) node;
            String processId = (String) concreteNode.getPropertyValue(EParameterName.PROCESS_TYPE_PROCESS.getName());
            if (processId != null && !"".equals(processId)) {
                ProcessItem processItem = ItemCacheManager.getProcessItem(processId);
                if (processItem != null) {
                    // TODO should use a fake Process here to replace the real Process.
                    Process loadedProcess = new Process(processItem.getProperty());
                    loadedProcess.loadXmlFile();
                    return loadedProcess;
                }
            }
        }
        return null;
    }

    public static List<INode> getTRunjobs(IProcess process) {
        List<INode> matchingNodes = new ArrayList<INode>();
        for (INode node : (List<INode>) (process.getGraphicalNodes())) {
            if (DesignerUtilities.isTRunJobComponent(node)) {
                matchingNodes.add(node);
            }
        }
        return matchingNodes;
    }
}
