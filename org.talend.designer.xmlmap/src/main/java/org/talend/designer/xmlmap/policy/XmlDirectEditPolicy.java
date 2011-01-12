package org.talend.designer.xmlmap.policy;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.DirectEditPolicy;
import org.eclipse.gef.requests.DirectEditRequest;
import org.talend.designer.xmlmap.model.emf.xmlmap.TreeNode;

public class XmlDirectEditPolicy extends DirectEditPolicy {

    @Override
    protected Command getDirectEditCommand(DirectEditRequest request) {
        if (getHost().getModel() instanceof TreeNode) {
            TreeNode treeNode = (TreeNode) getHost().getModel();
            treeNode.setExpression((String) request.getCellEditor().getValue());
        }
        return null;
    }

    @Override
    protected void showCurrentEditValue(DirectEditRequest request) {
        // TODO Auto-generated method stub

    }

}
