// ============================================================================
//
// Copyright (C) 2006-2010 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.designer.xmlmap.commands;

import org.eclipse.emf.common.util.EList;
import org.eclipse.gef.commands.Command;
import org.talend.designer.xmlmap.model.emf.xmlmap.Connection;
import org.talend.designer.xmlmap.model.emf.xmlmap.OutputTreeNode;
import org.talend.designer.xmlmap.model.emf.xmlmap.TreeNode;

/**
 * wchen class global comment. Detailled comment
 */
public class CreateConnectionCommand extends Command {

    public TreeNode getSource() {
        return source;
    }

    public void setSource(TreeNode source) {
        this.source = source;
    }

    public OutputTreeNode getTarget() {
        return target;
    }

    public void setTarget(OutputTreeNode target) {
        this.target = target;
    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    private TreeNode source;

    private OutputTreeNode target;

    private Connection connection;

    public CreateConnectionCommand(Connection connection, TreeNode source, OutputTreeNode target) {
        this.connection = connection;
        this.source = source;
        this.target = target;

    }

    @Override
    public void execute() {
        connection.setSource(source);
        connection.setTarget(target);

        // attachSource
        EList<Connection> outgoingConnections = connection.getSource().getOutgoingConnections();
        if (!outgoingConnections.contains(connection)) {
            outgoingConnections.add(connection);
        }

        // attachTarget
        EList<Connection> incomingConnections = connection.getTarget().getIncomingConnections();
        if (!incomingConnections.contains(connection)) {
            incomingConnections.add(connection);
        }

    }

    @Override
    public boolean canExecute() {
        if (source == null || target == null || source.equals(target)) {
            return false;
        }
        return true;
    }

    @Override
    public void undo() {
        // connection.detachSource();
        // connection.detachTarget();
    }

    @Override
    public void redo() {
        execute();
    }

}
