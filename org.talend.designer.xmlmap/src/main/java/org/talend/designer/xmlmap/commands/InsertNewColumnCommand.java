package org.talend.designer.xmlmap.commands;

import java.util.List;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.Command;
import org.talend.designer.xmlmap.dnd.TransferdType;
import org.talend.designer.xmlmap.dnd.TransferedObject;
import org.talend.designer.xmlmap.model.emf.xmlmap.AbstractNode;
import org.talend.designer.xmlmap.model.emf.xmlmap.Connection;
import org.talend.designer.xmlmap.model.emf.xmlmap.OutputTreeNode;
import org.talend.designer.xmlmap.model.emf.xmlmap.OutputXmlTree;
import org.talend.designer.xmlmap.model.emf.xmlmap.TreeNode;
import org.talend.designer.xmlmap.model.emf.xmlmap.VarNode;
import org.talend.designer.xmlmap.model.emf.xmlmap.VarTable;
import org.talend.designer.xmlmap.model.emf.xmlmap.XmlMapData;
import org.talend.designer.xmlmap.model.emf.xmlmap.XmlmapFactory;
import org.talend.designer.xmlmap.parts.OutputTreeNodeEditPart;
import org.talend.designer.xmlmap.parts.TreeNodeEditPart;
import org.talend.designer.xmlmap.parts.VarNodeEditPart;
import org.talend.designer.xmlmap.util.XmlMapUtil;

public class InsertNewColumnCommand extends Command {

    private EditPart targetEditPart;

    private AbstractNode createdNode;

    private XmlMapData xmlMapData;

    private TransferedObject objects;

    public InsertNewColumnCommand(TransferedObject objects, EditPart targetEditPart, AbstractNode createdNode,
            XmlMapData xmlMapData) {
        this.objects = objects;
        this.targetEditPart = targetEditPart;
        this.createdNode = createdNode;
        this.xmlMapData = xmlMapData;
    }

    @Override
    public void execute() {
        if (createdNode == null || objects.getToTransfer() == null || targetEditPart == null) {
            return;
        }
        boolean isTargetOutputNode = createdNode instanceof OutputTreeNode && targetEditPart instanceof OutputTreeNodeEditPart;
        boolean isTargetVarNode = createdNode instanceof VarNode && targetEditPart instanceof VarNodeEditPart;
        Object targetModel = targetEditPart.getModel();
        AbstractNode source = null;
        for (Object obj : objects.getToTransfer()) {
            if (objects.getType() == TransferdType.INPUT) {
                TreeNodeEditPart part = (TreeNodeEditPart) obj;
                TreeNode sourceNode = (TreeNode) part.getModel();
                source = sourceNode;
                String expression = XmlMapUtil.convertToExpression(sourceNode.getXpath());
                // INPUT => OUTPUT INSERT
                if (isTargetOutputNode) {
                    OutputTreeNode treeNode = (OutputTreeNode) targetModel;
                    if (treeNode.eContainer() instanceof OutputXmlTree) {
                        OutputXmlTree outputTree = (OutputXmlTree) treeNode.eContainer();
                        OutputTreeNode outputNode = (OutputTreeNode) createdNode;
                        String name = getUniqueTableEntry(outputTree.getNodes(), sourceNode.getName());
                        outputNode.setName(name);
                        outputNode.setType(sourceNode.getType());
                        outputNode.setExpression(expression);
                        outputNode.setPattern(sourceNode.getPattern());
                        outputNode.setXpath(XmlMapUtil.getXPath(outputTree.getName(), outputNode.getName(),
                                outputNode.getNodeType()));
                        outputTree.getNodes().add(outputTree.getNodes().indexOf(treeNode), (OutputTreeNode) createdNode);
                    }
                }
                // INPUT => VAR INSERT
                else if (isTargetVarNode) {
                    VarNode targetVar = (VarNode) targetModel;
                    VarNode varNode = (VarNode) createdNode;
                    if (targetVar.eContainer() instanceof VarTable) {
                        VarTable varTable = (VarTable) targetVar.eContainer();
                        String name = getUniqueTableEntry(varTable.getNodes(), source.getName());
                        varNode.setName(name);
                        varNode.setType(source.getType());
                        varNode.setExpression(expression);
                        varTable.getNodes().add(varTable.getNodes().indexOf(targetVar), varNode);
                    }

                }
            }
            // VAR => OUTPUT INSERT
            else if (objects.getType() == TransferdType.VAR) {
                VarNodeEditPart part = (VarNodeEditPart) obj;
                VarNode sourceNode = (VarNode) part.getModel();
                source = sourceNode;

                String tableName = "Var";
                if (sourceNode.eContainer() instanceof VarTable) {
                    tableName = ((VarTable) sourceNode.eContainer()).getName();
                }
                String expression = tableName + "." + sourceNode.getName();
                if (isTargetOutputNode) {
                    OutputTreeNode treeNode = (OutputTreeNode) targetModel;
                    if (treeNode.eContainer() instanceof OutputXmlTree) {
                        OutputXmlTree outputTree = (OutputXmlTree) treeNode.eContainer();
                        OutputTreeNode outputNode = (OutputTreeNode) createdNode;
                        String name = getUniqueTableEntry(outputTree.getNodes(), sourceNode.getName());
                        outputNode.setName(name);
                        outputNode.setType(sourceNode.getType());
                        outputNode.setExpression(expression);
                        outputNode.setXpath(XmlMapUtil.getXPath(outputTree.getName(), outputNode.getName(),
                                outputNode.getNodeType()));
                        outputTree.getNodes().add(outputTree.getNodes().indexOf(treeNode), (OutputTreeNode) createdNode);
                    }

                }
            }
            createConnection(source, createdNode);

        }
    }

    private void createConnection(AbstractNode sourceNode, AbstractNode targetNode) {
        Connection conn = XmlmapFactory.eINSTANCE.createConnection();
        conn.setSource(sourceNode);
        conn.setTarget(targetNode);
        targetNode.getIncomingConnections().add(conn);
        sourceNode.getOutgoingConnections().add(conn);
        if (xmlMapData != null) {
            xmlMapData.getConnections().add(conn);
        }
    }

    private String getUniqueTableEntry(List<? extends AbstractNode> nodeExisted, String nameToCreate) {
        boolean exists = true;
        int counter = 1;
        String newName = nameToCreate;
        while (exists) {
            boolean found = false;
            for (AbstractNode node : nodeExisted) {
                if (node.getName().equals(newName)) {
                    found = true;
                    break;
                }
            }
            exists = found;
            if (!exists) {
                break;
            }
            newName = nameToCreate + "_" + counter++;
        }
        return newName;
    }

}
