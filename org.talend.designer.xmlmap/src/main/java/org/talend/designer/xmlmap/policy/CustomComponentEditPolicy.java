package org.talend.designer.xmlmap.policy;

import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.ComponentEditPolicy;
import org.eclipse.gef.requests.GroupRequest;
import org.talend.designer.xmlmap.commands.DeleteTreeNodeCommand;
import org.talend.designer.xmlmap.model.emf.xmlmap.TreeNode;
import org.talend.designer.xmlmap.parts.TreeNodeEditPart;

public class CustomComponentEditPolicy extends ComponentEditPolicy {

    @Override
    public Command getCommand(Request request) {
        return super.getCommand(request);
    }

    @Override
    protected Command createDeleteCommand(GroupRequest deleteRequest) {
        Object model = ((TreeNodeEditPart) getHost()).getModel();
        DeleteTreeNodeCommand command = new DeleteTreeNodeCommand((TreeNode) model);
        return command;
    }
}
