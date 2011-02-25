package org.talend.designer.xmlmap.policy;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.DirectEditPolicy;
import org.eclipse.gef.requests.DirectEditRequest;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.talend.designer.xmlmap.commands.DirectEditCommand;
import org.talend.designer.xmlmap.commands.EXMLMapNodeProperty;
import org.talend.designer.xmlmap.model.emf.xmlmap.AbstractNode;
import org.talend.designer.xmlmap.model.emf.xmlmap.InputXmlTree;
import org.talend.designer.xmlmap.model.emf.xmlmap.OutputXmlTree;
import org.talend.designer.xmlmap.parts.directedit.DirectEditType;

public class XmlDirectEditPolicy extends DirectEditPolicy {

    @Override
    protected Command getDirectEditCommand(DirectEditRequest request) {
        Command command = null;
        // VarNode varNode = null;
        CellEditor editor = request.getCellEditor();
        // Object editFigure = request.getDirectEditFeature();
        // if (getHost().getModel() instanceof TreeNode && (editor instanceof ExpressionCellEditor)) {
        // TreeNode treeNode = (TreeNode) getHost().getModel();
        // // treeNode.setExpression((String) request.getCellEditor().getValue());
        // command = new DirectEditCommand(treeNode, (String) request.getCellEditor().getValue());
        // }
        // if (getHost().getModel() instanceof VarNode && (editor instanceof ExpressionCellEditor)) {
        // varNode = (VarNode) getHost().getModel();
        // // treeNode.setExpression((String) request.getCellEditor().getValue());
        // command = new DirectEditCommand(varNode, (String) editor.getValue());
        // } else if (getHost().getModel() instanceof VarNode && (editor instanceof ComboBoxCellEditor)) {
        // ComboBoxCellEditor combo = (ComboBoxCellEditor) editor;
        // int selectIndex = (Integer) combo.getValue();
        // varNode = (VarNode) getHost().getModel();
        // command = new DirectEditCommand(varNode, EXMLMapNodeProperty.VARNODE_TYPE, combo.getItems()[selectIndex],
        // false);
        // } else if (getHost().getModel() instanceof VarNode && (editor instanceof TextCellEditor)) {
        // varNode = (VarNode) getHost().getModel();
        // TextCellEditor text = (TextCellEditor) editor;
        // String variable = ((Text) text.getControl()).getText();
        // command = new DirectEditCommand(varNode, EXMLMapNodeProperty.VARNODE_VARIABLE, variable, false);
        // }

        Object directEditFeature = request.getDirectEditFeature();
        if (directEditFeature instanceof DirectEditType) {
            DirectEditType type = (DirectEditType) directEditFeature;

            if (getHost().getModel() instanceof AbstractNode) {
                AbstractNode model = (AbstractNode) getHost().getModel();
                switch (type) {
                case EXPRESSION:
                    command = new DirectEditCommand(model, (String) request.getCellEditor().getValue());
                    break;
                case NODE_NAME:
                    String variable = (String) request.getCellEditor().getValue();
                    command = new DirectEditCommand(model, EXMLMapNodeProperty.VARNODE_VARIABLE, variable, false);
                    break;
                case VAR_NODE_TYPE:
                    if ((editor instanceof ComboBoxCellEditor)) {
                        ComboBoxCellEditor combo = (ComboBoxCellEditor) editor;
                        int selectIndex = (Integer) combo.getValue();
                        command = new DirectEditCommand(model, EXMLMapNodeProperty.VARNODE_TYPE, combo.getItems()[selectIndex],
                                false);
                    }
                    break;
                default:
                    break;
                }

            } else if (getHost().getModel() instanceof InputXmlTree) {

            } else if (getHost().getModel() instanceof OutputXmlTree) {

            }
        }

        return command;
    }

    @Override
    protected void showCurrentEditValue(DirectEditRequest request) {
        // TODO Auto-generated method stub

    }

}
