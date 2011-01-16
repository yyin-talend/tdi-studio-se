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

import org.eclipse.gef.EditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.AbstractEditPolicy;
import org.talend.designer.xmlmap.commands.CreateNodeAndConnectionCommand;
import org.talend.designer.xmlmap.dnd.CreateNodeConnectionRequest;

/**
 * wchen class global comment. Detailled comment
 */
public class DragAndDropEditPolicy extends AbstractEditPolicy {

    @Override
    public EditPart getTargetEditPart(Request request) {
        if (RequestConstants.REQ_CREATE.equals(request.getType())) {
            return getHost();
        }
        return null;

    }

    @Override
    public Command getCommand(Request request) {
        if (request instanceof CreateNodeConnectionRequest) {
            CreateNodeConnectionRequest rq = (CreateNodeConnectionRequest) request;
            boolean updateOrCreate = rq.getDropType() == CreateNodeConnectionRequest.DROP_EXPRESSION ? true : false;
            return new CreateNodeAndConnectionCommand(rq.getNewObject(), rq.getTargetEditPart(), updateOrCreate);
        }
        return super.getCommand(request);
    }
}
