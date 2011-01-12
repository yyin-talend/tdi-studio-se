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
package org.talend.designer.xmlmap.policy;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.GraphicalNodeEditPolicy;
import org.eclipse.gef.requests.CreateConnectionRequest;
import org.eclipse.gef.requests.ReconnectRequest;
import org.talend.designer.xmlmap.commands.CreateConnectionCommand;

/**
 * wchen class global comment. Detailled comment
 */
public class CustomGraphicalNodeEditPolicy extends GraphicalNodeEditPolicy {

    @Override
    protected Command getConnectionCompleteCommand(CreateConnectionRequest request) {
        CreateConnectionCommand command = (CreateConnectionCommand) request.getStartCommand();
        // command.setTarget(getHost().getModel());
        return command;
    }

    @Override
    protected Command getConnectionCreateCommand(CreateConnectionRequest request) {
        // CreateConnectionCommand command = new CreateConnectionCommand();
        // command.setConnection(request.getNewObject());
        // command.setSource(getHost().getModel());
        // request.setStartCommand(command);
        return null;
    }

    @Override
    protected Command getReconnectSourceCommand(ReconnectRequest request) {
        // ReconnectConnectionCommand command = new ReconnectConnectionCommand();
        // command.setConnection(request.getConnectionEditPart().getModel());
        // command.setSourceNode(getHost().getModel());
        return null;
    }

    @Override
    protected Command getReconnectTargetCommand(ReconnectRequest request) {
        // ReconnectConnectionCommand command = new ReconnectConnectionCommand();
        // command.setConnection(request.getConnectionEditPart().getModel());
        // command.setTargetNode(getHost().getModel());
        return null;
    }

}
