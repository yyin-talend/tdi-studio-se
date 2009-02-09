// ============================================================================
//
// Copyright (C) 2006-2009 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.designer.core.ui.editor.cmd;

import java.util.List;

import org.eclipse.gef.commands.Command;
import org.talend.core.model.process.IProcess;
import org.talend.core.model.process.IProcess2;
import org.talend.designer.core.i18n.Messages;
import org.talend.designer.core.ui.editor.connections.Connection;
import org.talend.designer.core.ui.editor.nodes.Node;
import org.talend.designer.core.ui.editor.process.Process;

/**
 * DOC nrousseau class global comment. Detailled comment <br/>
 * 
 */
public class ChangeMergeOrderCommand extends Command {

    private Node mergeNode;

    private List<Connection> connectionInNewOrder;

    private List<Connection> connectionInOldOrder;

    public ChangeMergeOrderCommand(Node mergeNode, List<Connection> inputConnections) {
        this.mergeNode = mergeNode;
        this.connectionInNewOrder = inputConnections;
        this.connectionInOldOrder = (List<Connection>) mergeNode.getIncomingConnections();

        if (connectionInNewOrder.size() != connectionInOldOrder.size()) {
            throw new IllegalArgumentException("new connection list should have the same size as the old one"); //$NON-NLS-1$
        }
        this.setLabel(Messages.getString("ChangeMergeOrderCommand.label")); //$NON-NLS-1$

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.gef.commands.Command#execute()
     */
    @Override
    public void execute() {

        mergeNode.setIncomingConnections(connectionInNewOrder);
        connectionInNewOrder.get(0).updateAllId();
        ((Process) mergeNode.getProcess()).checkStartNodes();

        checkProcess();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.gef.commands.Command#undo()
     */
    @Override
    public void undo() {
        mergeNode.setIncomingConnections(connectionInOldOrder);
        connectionInOldOrder.get(0).updateAllId();
        ((Process) mergeNode.getProcess()).checkStartNodes();
        checkProcess();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.gef.commands.Command#redo()
     */
    @Override
    public void redo() {
        super.redo();
        checkProcess();
    }

    /**
     * tang Comment method "checkProcess".
     */
    private void checkProcess() {
        IProcess process = mergeNode.getProcess();
        if (process instanceof IProcess2) {
            ((IProcess2) process).checkProcess();
        }
    }

}
