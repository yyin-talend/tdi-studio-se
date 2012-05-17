package org.talend.designer.xmlmap.commands;

import java.util.List;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.Command;
import org.talend.core.model.metadata.IMetadataColumn;
import org.talend.core.model.metadata.IMetadataTable;
import org.talend.core.model.metadata.MetadataColumn;
import org.talend.core.model.process.IConnection;
import org.talend.designer.xmlmap.dnd.DropType;
import org.talend.designer.xmlmap.dnd.TransferdType;
import org.talend.designer.xmlmap.dnd.TransferedObject;
import org.talend.designer.xmlmap.model.emf.xmlmap.AbstractInOutTree;
import org.talend.designer.xmlmap.model.emf.xmlmap.AbstractNode;
import org.talend.designer.xmlmap.model.emf.xmlmap.Connection;
import org.talend.designer.xmlmap.model.emf.xmlmap.InputXmlTree;
import org.talend.designer.xmlmap.model.emf.xmlmap.LookupConnection;
import org.talend.designer.xmlmap.model.emf.xmlmap.OutputTreeNode;
import org.talend.designer.xmlmap.model.emf.xmlmap.OutputXmlTree;
import org.talend.designer.xmlmap.model.emf.xmlmap.TreeNode;
import org.talend.designer.xmlmap.model.emf.xmlmap.VarNode;
import org.talend.designer.xmlmap.model.emf.xmlmap.VarTable;
import org.talend.designer.xmlmap.model.emf.xmlmap.XmlMapData;
import org.talend.designer.xmlmap.model.emf.xmlmap.XmlmapFactory;
import org.talend.designer.xmlmap.parts.TreeNodeEditPart;
import org.talend.designer.xmlmap.parts.VarNodeEditPart;
import org.talend.designer.xmlmap.ui.tabs.MapperManager;
import org.talend.designer.xmlmap.util.XmlMapUtil;

public class InsertNewColumnCommand extends Command {

    private EditPart targetEditPart;

    private AbstractNode createdNode;

    private XmlMapData xmlMapData;

    private TransferedObject objects;

    private DropType dropType;

    private MapperManager manager;

    public InsertNewColumnCommand(TransferedObject objects, EditPart targetEditPart, AbstractNode createdNode,
            MapperManager manager, DropType dropType) {
        this.objects = objects;
        this.targetEditPart = targetEditPart;
        this.createdNode = createdNode;
        this.manager = manager;
        this.xmlMapData = manager.getCopyOfMapData();
        this.dropType = dropType;
    }

    @Override
    public void execute() {
        if (createdNode == null || objects.getToTransfer() == null || targetEditPart == null || dropType == null) {
            return;
        }
        Object targetModel = targetEditPart.getModel();
        for (Object obj : objects.getToTransfer()) {
            // INPUT => OUTPUT INSERT
            if (objects.getType() == TransferdType.INPUT) {
                TreeNodeEditPart part = (TreeNodeEditPart) obj;
                TreeNode sourceNode = (TreeNode) part.getModel();
                String expression = XmlMapUtil.convertToExpression(sourceNode.getXpath());
                switch (dropType) {
                case DROP_INSERT_OUTPUT:
                    int index = -1;
                    OutputXmlTree outputTree = null;
                    if (targetModel instanceof OutputTreeNode) {
                        OutputTreeNode targetNode = (OutputTreeNode) targetModel;
                        if (targetNode.eContainer() instanceof OutputXmlTree) {
                            outputTree = (OutputXmlTree) targetNode.eContainer();
                            index = outputTree.getNodes().indexOf(targetNode);
                        }
                    } else if (targetModel instanceof OutputXmlTree) {
                        outputTree = (OutputXmlTree) targetModel;
                    }
                    if (outputTree != null) {
                        fillTreeNode(outputTree.getNodes(), sourceNode, outputTree.getName(), expression,
                                (OutputTreeNode) createdNode);
                        if (index != -1) {
                            outputTree.getNodes().add(index, (OutputTreeNode) createdNode);
                        } else {
                            outputTree.getNodes().add((OutputTreeNode) createdNode);
                        }
                        createConnection(sourceNode, createdNode);
                        AbstractInOutTree abstractTree = XmlMapUtil.getAbstractInOutTree(sourceNode);
                        createOutputMetadataColumn(abstractTree.getName(), outputTree.getName(), sourceNode.getName(),
                                createdNode.getName(), index);
                    }
                    break;
                case DROP_INSERT_VAR:
                    VarNode targetVar = (VarNode) targetModel;
                    VarNode varNode = (VarNode) createdNode;
                    if (targetVar.eContainer() instanceof VarTable) {
                        VarTable varTable = (VarTable) targetVar.eContainer();
                        String name = getUniqueTableEntry(varTable.getNodes(), sourceNode.getName());
                        varNode.setName(name);
                        varNode.setType(sourceNode.getType());
                        varNode.setExpression(expression);
                        varTable.getNodes().add(varTable.getNodes().indexOf(targetVar), varNode);
                        createConnection(sourceNode, createdNode);
                    }

                    break;
                case DROP_INSERT_INPUT:
                    if (targetModel instanceof InputXmlTree) {
                        InputXmlTree inputTree = (InputXmlTree) targetModel;
                        fillTreeNode(inputTree.getNodes(), sourceNode, inputTree.getName(), expression, (TreeNode) createdNode);
                        inputTree.getNodes().add((TreeNode) createdNode);
                        createLookupConnection(sourceNode, (TreeNode) createdNode);
                        AbstractInOutTree abstractTree = XmlMapUtil.getAbstractInOutTree(sourceNode);
                        createInputMetadataColumn(abstractTree.getName(), inputTree.getName(), sourceNode.getName(),
                                createdNode.getName(), -1);
                    }
                default:
                    break;
                }
            }
            // VAR => OUTPUT INSERT
            else if (objects.getType() == TransferdType.VAR) {
                VarNodeEditPart part = (VarNodeEditPart) obj;
                VarNode sourceNode = (VarNode) part.getModel();

                String tableName = "Var";
                if (sourceNode.eContainer() instanceof VarTable) {
                    tableName = ((VarTable) sourceNode.eContainer()).getName();
                }
                String expression = tableName + "." + sourceNode.getName();
                if (dropType == DropType.DROP_INSERT_OUTPUT) {
                    int index = -1;
                    OutputXmlTree outputTree = null;
                    if (targetModel instanceof OutputTreeNode) {
                        OutputTreeNode targetNode = (OutputTreeNode) targetModel;
                        if (targetNode.eContainer() instanceof OutputXmlTree) {
                            outputTree = (OutputXmlTree) targetNode.eContainer();
                            index = outputTree.getNodes().indexOf(targetNode);
                        }
                    } else if (targetModel instanceof OutputXmlTree) {
                        outputTree = (OutputXmlTree) targetModel;
                    }
                    if (outputTree != null) {
                        OutputTreeNode outputNode = (OutputTreeNode) createdNode;
                        String name = getUniqueTableEntry(outputTree.getNodes(), sourceNode.getName());
                        outputNode.setName(name);
                        outputNode.setType(sourceNode.getType());
                        outputNode.setExpression(expression);
                        outputNode.setXpath(XmlMapUtil.getXPath(outputTree.getName(), outputNode.getName(),
                                outputNode.getNodeType()));
                        if (index != -1) {
                            outputTree.getNodes().add(index, (OutputTreeNode) createdNode);
                        } else {
                            outputTree.getNodes().add((OutputTreeNode) createdNode);
                        }
                        createConnection(sourceNode, createdNode);
                        List<IMetadataTable> metadataTargets = manager.getMapperComponent().getMetadataList();
                        IMetadataTable metadataTarget = null;
                        if (metadataTargets != null) {
                            for (IMetadataTable target : metadataTargets) {
                                if (target.getTableName().equals(outputTree.getName())) {
                                    metadataTarget = target;
                                }
                            }
                            if (metadataTarget != null) {
                                IMetadataColumn createNewColumn = new MetadataColumn();
                                createNewColumn.setLabel(outputNode.getName());
                                // createNewColumn.setType(outputNode.getType());
                                createNewColumn.setTalendType(outputNode.getType());
                                createNewColumn.setNullable(true);
                                metadataTarget.getListColumns().add(createNewColumn);
                                manager.getMapperUI().getTabFolderEditors().getOutputMetaEditorView().getTableViewerCreator()
                                        .refresh();
                            }
                        }
                    }

                }
            }

        }
    }

    private void createInputMetadataColumn(String sourceTreeName, String targetTreeName, String sourceNodeName,
            String targetNodeName, int index) {
        IMetadataTable metadataTarget = null;
        List<? extends IConnection> connections = manager.getMapperComponent().getIncomingConnections();
        for (IConnection incoming : connections) {
            if (incoming.getName().equals(targetTreeName)) {
                metadataTarget = incoming.getMetadataTable();
            }
        }

        if (metadataTarget != null && sourceTreeName != null) {
            IMetadataColumn columnSource = getSourceColumn(sourceTreeName, sourceNodeName);
            if (columnSource != null) {
                IMetadataColumn createNewColumn = new MetadataColumn(columnSource);
                createNewColumn.setLabel(targetNodeName);
                if (index != -1) {
                    metadataTarget.getListColumns().add(index, createNewColumn);
                } else {
                    metadataTarget.getListColumns().add(createNewColumn);
                }
                manager.getMapperUI().getTabFolderEditors().getInputMetaEditorView().getTableViewerCreator().refresh();
            }

        }
    }

    private void createOutputMetadataColumn(String sourceTreeName, String targetTreeName, String sourceNodeName,
            String targetNodeName, int index) {
        IMetadataTable metadataTarget = null;
        List<IMetadataTable> metadataTargets = manager.getMapperComponent().getMetadataList();
        if (metadataTargets != null) {
            for (IMetadataTable target : metadataTargets) {
                if (target.getTableName().equals(targetTreeName)) {
                    metadataTarget = target;
                }
            }
        }

        if (metadataTarget != null && sourceTreeName != null) {
            IMetadataColumn columnSource = getSourceColumn(sourceTreeName, sourceNodeName);
            if (columnSource != null) {
                IMetadataColumn createNewColumn = new MetadataColumn(columnSource);
                createNewColumn.setLabel(targetNodeName);
                if (index != -1) {
                    metadataTarget.getListColumns().add(index, createNewColumn);
                } else {
                    metadataTarget.getListColumns().add(createNewColumn);
                }
                manager.getMapperUI().getTabFolderEditors().getOutputMetaEditorView().getTableViewerCreator().refresh();
            }

        }
    }

    private IMetadataColumn getSourceColumn(String sourceTreeName, String sourceNodeName) {
        List<? extends IConnection> connections = manager.getMapperComponent().getIncomingConnections();
        IMetadataColumn columnSource = null;
        for (IConnection incoming : connections) {
            if (incoming.getName().equals(sourceTreeName)) {
                IMetadataTable metadataSource = incoming.getMetadataTable();
                for (IMetadataColumn column : metadataSource.getListColumns()) {
                    if (column.getLabel().equals(sourceNodeName)) {
                        columnSource = column;
                        break;
                    }
                }
                break;
            }
        }
        return columnSource;

    }

    private void fillTreeNode(List<? extends AbstractNode> validationList, TreeNode sourceNode, String sourceTableName,
            String expression, TreeNode target) {
        String name = getUniqueTableEntry(validationList, sourceNode.getName());
        target.setName(name);
        target.setType(sourceNode.getType());
        target.setExpression(expression);
        target.setPattern(sourceNode.getPattern());
        target.setXpath(XmlMapUtil.getXPath(sourceTableName, target.getName(), target.getNodeType()));
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

    private void createLookupConnection(TreeNode sourceNode, TreeNode targetNode) {
        LookupConnection conn = XmlmapFactory.eINSTANCE.createLookupConnection();
        conn.setSource(sourceNode);
        conn.setTarget(targetNode);
        targetNode.getLookupIncomingConnections().add(conn);
        sourceNode.getLookupOutgoingConnections().add(conn);
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
