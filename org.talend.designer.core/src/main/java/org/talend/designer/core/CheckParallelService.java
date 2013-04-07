// ============================================================================
//
// Copyright (C) 2006-2013 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.designer.core;

import org.talend.core.model.process.IConnection;
import org.talend.core.model.process.Problem.ProblemStatus;
import org.talend.designer.core.i18n.Messages;
import org.talend.designer.core.model.components.EParameterName;
import org.talend.designer.core.ui.editor.nodes.Node;
import org.talend.designer.core.ui.views.problems.Problems;
import org.talend.designer.core.utils.ParallelExecutionUtils;

/**
 * DOC talendongl class global comment. Detailled comment
 */
public class CheckParallelService implements ICheckParallelService {

    /**
     * DOC talendongl CheckParallelService constructor comment.
     */
    public CheckParallelService() {
        // TODO Auto-generated constructor stub
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.core.ICheckParallelService#checkParallel(org.talend.designer.core.ui.editor.nodes.Node)
     */
    @Override
    public void checkParallel(Node node) {
        // for the case component support partion/repar,but has a depar before.
        if (!(node.getComponent().getPartitioning().equals("AUTO") || node.getComponent().getPartitioning().equals("NONE"))) {
            for (IConnection sourceCon : node.getIncomingConnections()) {
                if (sourceCon.getElementParameter(EParameterName.DEPARTITIONER.getName()).getValue().equals(true)) {
                    String warningMessage = Messages.getString("Node.notSupportDepartition"); //$NON-NLS-1$
                    Problems.add(ProblemStatus.WARNING, node, warningMessage);
                }
            }
        }
        // for the case last row
        if (node.getComponent().getPartitioning().equals("AUTO")) {
            if (node.getOutgoingConnections().size() == 0) {
                for (IConnection sourceCon : node.getIncomingConnections()) {
                    if (sourceCon.getElementParameter(EParameterName.PARTITIONER.getName()).getValue().equals(true)) {
                        String warningMessage = Messages.getString("Node.notSupportPartition"); //$NON-NLS-1$
                        Problems.add(ProblemStatus.WARNING, node, warningMessage);
                    }
                }
            }
        }
        // for the case partitioning keys not same
        if (!(node.getComponent().getPartitioning().equals("AUTO") || node.getComponent().getPartitioning().equals("NONE"))) {
            for (IConnection sourceCon : node.getIncomingConnections()) {
                if (sourceCon.getElementParameter(EParameterName.PARTITIONER.getName()).getValue().equals(true)
                        || sourceCon.getElementParameter(EParameterName.REPARTITIONER.getName()).getValue().equals(true)) {
                    if (!ParallelExecutionUtils.compareKeyPartions(sourceCon, node)) {
                        String warningMessage = Messages.getString("Node.partitionKeysNotSame"); //$NON-NLS-1$
                        Problems.add(ProblemStatus.WARNING, node, warningMessage);
                    }
                }
            }
        }
        // for the case repeat partitioning
        if (!node.getComponent().getPartitioning().equals("NONE")) {
            for (IConnection sourceCon : node.getIncomingConnections()) {
                if (sourceCon.getElementParameter(EParameterName.PARTITIONER.getName()).getValue().equals(true)) {
                    if (ParallelExecutionUtils.existPreviousPar((Node) sourceCon.getSource())) {
                        String warningMessage = Messages.getString("Node.repeatPartition"); //$NON-NLS-1$
                        Problems.add(ProblemStatus.WARNING, node, warningMessage);
                    }
                }
            }
        }
        // for the case repeat departitioning
        if (!node.getComponent().getPartitioning().equals("NONE")) {
            for (IConnection sourceCon : node.getIncomingConnections()) {
                if (sourceCon.getElementParameter(EParameterName.DEPARTITIONER.getName()).getValue().equals(true)) {
                    if (ParallelExecutionUtils.existPreviousDepar((Node) sourceCon.getSource())) {
                        String warningMessage = Messages.getString("Node.repeatDepartition"); //$NON-NLS-1$
                        Problems.add(ProblemStatus.WARNING, node, warningMessage);
                    }
                }
            }
        }
    }
}
