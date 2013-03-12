package org.talend.designer.core.ui.action;

import java.util.List;

import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.ui.IWorkbenchPart;
import org.talend.core.model.process.EConnectionType;
import org.talend.core.model.process.IConnection;
import org.talend.core.model.process.IConnectionCategory;
import org.talend.core.model.process.INode;
import org.talend.designer.core.i18n.Messages;
import org.talend.designer.core.model.components.EParameterName;
import org.talend.designer.core.ui.editor.cmd.DisableParallelizationCommand;
import org.talend.designer.core.ui.editor.nodecontainer.NodeContainer;
import org.talend.designer.core.ui.editor.nodecontainer.NodeContainerPart;
import org.talend.designer.core.ui.editor.nodes.Node;
import org.talend.designer.core.ui.editor.nodes.NodePart;
import org.talend.designer.core.ui.editor.subjobcontainer.SubjobContainer;
import org.talend.designer.core.ui.editor.subjobcontainer.SubjobContainerPart;

public class DisableParallelizationAction extends SelectionAction {

    public static final String ID = "org.talend.designer.core.ui.editor.action.DisableParallelizationAction"; //$NON-NLS-1$

    IWorkbenchPart part;

    public DisableParallelizationAction(IWorkbenchPart part) {
        super(part);
        this.part = part;
        setId(ID);
        setText(Messages.getString("PropertiesContextAction.disableParallelization"));
    }

    @Override
    protected boolean calculateEnabled() {
        List parts = getSelectedObjects();
        if (parts.isEmpty()) {
            return false;
        }
        if (parts.size() == 1) {
            Object o = parts.get(0);
            if (o instanceof SubjobContainerPart) {
                SubjobContainerPart part = (SubjobContainerPart) o;
                SubjobContainer subjob = (SubjobContainer) part.getModel();
                if (subjob.isDisplayed()) {
                    return true;
                } else {
                    return false;
                }
            } else if (o instanceof NodePart) {
                NodePart part = (NodePart) o;
                Node node = (Node) part.getModel();
                if (node.isStart()) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        }
        return false;
    }

    @Override
    public void run() {
        List editparts = getSelectedObjects();
        if (editparts.size() == 1) {
            Object o = editparts.get(0);
            if (o instanceof NodePart) {
                NodePart part = (NodePart) o;
                Node node = (Node) part.getModel();
                getCommandStack().execute(new DisableParallelizationCommand(node));
            } else if (o instanceof SubjobContainerPart) {
                boolean hasStartNode = false;
                List<NodeContainerPart> childNodes = ((SubjobContainerPart) o).getChildren();
                for (NodeContainerPart childNode : childNodes) {
                    NodeContainerPart part = (NodeContainerPart) childNode;
                    NodeContainer node = (NodeContainer) part.getModel();
                    if (node.getNode().isStart()) {
                        hasStartNode = true;
                        getCommandStack().execute(new DisableParallelizationCommand(node.getNode()));
                    }
                }
                if (!hasStartNode) {
                    for (NodeContainerPart childNode : childNodes) {
                        NodeContainerPart part = (NodeContainerPart) childNode;
                        NodeContainer node = (NodeContainer) part.getModel();
                        if (node.getNode().isSubProcessStart()) {
                            getCommandStack().execute(new DisableParallelizationCommand(node.getNode()));
                        }
                    }
                }
            }
        }
    }

    private void disableParallelization(INode node) {
        if (node.getOutgoingConnections().size() > 0) {
            for (IConnection con : node.getOutgoingConnections()) {
                EConnectionType lineStyle = con.getLineStyle();
                if (lineStyle.hasConnectionCategory(IConnectionCategory.MAIN)
                        || lineStyle.hasConnectionCategory(IConnectionCategory.MERGE)) {
                    con.getElementParameter(EParameterName.DEPARTITIONER.getName()).setValue(Boolean.FALSE);
                    con.getElementParameter(EParameterName.PARTITIONER.getName()).setValue(Boolean.FALSE);
                    con.setPropertyValue(EParameterName.NONE.getName(), Boolean.TRUE);

                    if (con.getTarget() != null) {
                        disableParallelization(con.getTarget());
                    }
                } else {
                    node = con.getTarget();
                    disableParallelization(node);
                }
            }
        }
    }
}
