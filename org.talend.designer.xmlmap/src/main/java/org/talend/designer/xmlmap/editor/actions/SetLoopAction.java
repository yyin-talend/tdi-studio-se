package org.talend.designer.xmlmap.editor.actions;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbenchPart;
import org.talend.designer.xmlmap.i18n.Messages;
import org.talend.designer.xmlmap.model.emf.xmlmap.AbstractInOutTree;
import org.talend.designer.xmlmap.model.emf.xmlmap.Connection;
import org.talend.designer.xmlmap.model.emf.xmlmap.InputLoopNodesTable;
import org.talend.designer.xmlmap.model.emf.xmlmap.NodeType;
import org.talend.designer.xmlmap.model.emf.xmlmap.OutputTreeNode;
import org.talend.designer.xmlmap.model.emf.xmlmap.OutputXmlTree;
import org.talend.designer.xmlmap.model.emf.xmlmap.TreeNode;
import org.talend.designer.xmlmap.model.emf.xmlmap.XmlmapFactory;
import org.talend.designer.xmlmap.parts.TreeNodeEditPart;
import org.talend.designer.xmlmap.ui.tabs.MapperManager;
import org.talend.designer.xmlmap.util.XmlMapUtil;

public class SetLoopAction extends SelectionAction {

    public static String ID = "xml map set as loop action";

    private MapperManager mapperManager;

    private boolean input;

    List<TreeNode> loopNodeList = new ArrayList<TreeNode>();

    // private List<TreeNode> nodesNeedToChangeMainStatus = new ArrayList<TreeNode>();

    public SetLoopAction(IWorkbenchPart part) {
        super(part);
        setId(ID);
        setText("As loop element");
    }

    @Override
    protected boolean calculateEnabled() {
        // nodesNeedToChangeMainStatus.clear();
        if (getSelectedObjects().isEmpty()) {
            return false;
        }
        if (getSelectedObjects().get(0) instanceof TreeNodeEditPart) {
            TreeNodeEditPart nodePart = (TreeNodeEditPart) getSelectedObjects().get(0);
            TreeNode model = (TreeNode) nodePart.getModel();

            if (NodeType.ATTRIBUT.equals(model.getNodeType()) || NodeType.NAME_SPACE.equals(model.getNodeType())
                    || !(model.eContainer() instanceof TreeNode)) {
                return false;
            }

            if (model.isLoop()) {
                return false;
            }

        } else {
            return false;
        }

        return true;
    }

    public void update(Object selection) {
        setSelection(new StructuredSelection(selection));
    }

    @Override
    public void run() {
        TreeNodeEditPart nodePart = (TreeNodeEditPart) getSelectedObjects().get(0);
        TreeNode model = (TreeNode) nodePart.getModel();

        AbstractInOutTree abstractTree = null;
        TreeNode docRoot = null;

        // remove old loop
        if (model instanceof OutputTreeNode) {
            OutputTreeNode outputNode = (OutputTreeNode) model;
            docRoot = (OutputTreeNode) XmlMapUtil.getTreeNodeRoot(outputNode);
            if (docRoot != null) {
                XmlMapUtil.cleanSubGroup(outputNode);
                List<TreeNode> newLoopUpGroups = new ArrayList<TreeNode>();
                findUpGroupNode(newLoopUpGroups, outputNode);
                // clean all groups except the ancestor of new loop
                XmlMapUtil.cleanSubGroup(docRoot, newLoopUpGroups);

                // reset the group in case some element ancestor of loop element are not group but under the group
                if (!newLoopUpGroups.isEmpty()) {
                    TreeNode rootGroup = newLoopUpGroups.get(newLoopUpGroups.size() - 1);
                    upsetGroup(outputNode, rootGroup);
                }
                if (docRoot.eContainer() instanceof AbstractInOutTree) {
                    abstractTree = (AbstractInOutTree) docRoot.eContainer();
                }
            }
        } else if (model instanceof TreeNode) {
            docRoot = XmlMapUtil.getTreeNodeRoot(model);
            if (docRoot.eContainer() instanceof AbstractInOutTree) {
                abstractTree = (AbstractInOutTree) docRoot.eContainer();
            }
        }
        // TDI-20147
        List<TreeNode> loopNodes = new ArrayList<TreeNode>();
        checkSubElementIsLoop(model, loopNodes);
        checkParentElementIsLoop(model, loopNodes);
        if (!loopNodes.isEmpty()) {
            if (MessageDialog.openConfirm(Display.getDefault().getActiveShell(),
                    Messages.getString("SetLoopAction.cleanSubLoopTitle"),
                    Messages.getString("SetLoopAction.cleanSubLoopMessages"))) {
            } else {
                return;
            }
        }

        for (TreeNode treeNode : loopNodes) {
            treeNode.setLoop(false);
        }

        if (!input) {
            // clean the InputLoopNodesTable for the old loops
            for (TreeNode treeNode : loopNodes) {
                InputLoopNodesTable inputLoopNodesTable = ((OutputTreeNode) treeNode).getInputLoopNodesTable();
                if (inputLoopNodesTable != null && abstractTree != null) {
                    ((OutputXmlTree) abstractTree).getInputLoopNodesTables().remove(inputLoopNodesTable);
                    inputLoopNodesTable.getInputloopnodes().clear();
                }
                ((OutputTreeNode) treeNode).setInputLoopNodesTable(null);
            }
            // find input loop node and add to InputLoopNodesTable
            List<TreeNode> sourceLoopNodes = new ArrayList<TreeNode>();
            findChildSourceLoop(model, sourceLoopNodes);
            if (!sourceLoopNodes.isEmpty()) {
                InputLoopNodesTable createInputLoopNodesTable = XmlmapFactory.eINSTANCE.createInputLoopNodesTable();
                createInputLoopNodesTable.eAdapters().add(nodePart);
                ((OutputTreeNode) model).setInputLoopNodesTable(createInputLoopNodesTable);
                createInputLoopNodesTable.getInputloopnodes().addAll(sourceLoopNodes);
            }
        }
        model.setLoop(true);

        XmlMapUtil.clearMainNode(model);
        XmlMapUtil.upsetMainNode(model);

        if (XmlMapUtil.hasDocument(abstractTree)) {
            loopNodeList.clear();
            getLoopNode(docRoot);
            if (loopNodeList != null && loopNodeList.size() > 1) {
                abstractTree.setMultiLoops(true);
            } else {
                abstractTree.setMultiLoops(false);
            }
        }
        if (abstractTree != null) {
            mapperManager.getProblemsAnalyser().checkLoopProblems(abstractTree);
            mapperManager.getMapperUI().updateStatusBar();
        }
    }

    private void findChildSourceLoop(TreeNode treeNode, List<TreeNode> sourceLoopNodes) {
        if (treeNode.getExpression() != null) {
            EList<Connection> incomingConnections = treeNode.getIncomingConnections();
            for (Connection connection : incomingConnections) {
                if (connection.getSource() instanceof TreeNode) {
                    TreeNode loopParentNode = XmlMapUtil.getLoopParentNode((TreeNode) connection.getSource());
                    if (loopParentNode != null && !sourceLoopNodes.contains(loopParentNode)) {
                        sourceLoopNodes.add(loopParentNode);
                    }
                }
            }

        }
        if (!treeNode.getChildren().isEmpty()) {
            for (TreeNode child : treeNode.getChildren()) {
                findChildSourceLoop(child, sourceLoopNodes);
            }
        }

    }

    private void cleanSubLoop(TreeNode docRoot) {
        for (TreeNode treeNode : docRoot.getChildren()) {
            if (treeNode.isLoop()) {
                treeNode.setLoop(false);
            }
            cleanSubLoop(treeNode);
        }
    }

    private void checkSubElementIsLoop(TreeNode subTreeNode, List<TreeNode> subLoops) {
        if (subTreeNode == null) {
            return;
        }
        TreeNode e = subTreeNode;
        if (e.isLoop()) {
            subLoops.add(e);
        }
        for (TreeNode treeNode : subTreeNode.getChildren()) {
            checkSubElementIsLoop(treeNode, subLoops);
        }
    }

    private void checkParentElementIsLoop(TreeNode subTreeNode, List<TreeNode> subLoops) {
        if (subTreeNode == null) {
            return;
        }
        TreeNode e = subTreeNode;
        if (e.isLoop()) {
            subLoops.add(e);
        }
        if (e.eContainer() instanceof TreeNode) {
            checkParentElementIsLoop((TreeNode) e.eContainer(), subLoops);
        }
    }

    private void getLoopNode(TreeNode pNode) {
        if (pNode == null) {
            return;
        }
        TreeNode e = pNode;
        if (e.isLoop()) {
            loopNodeList.add(e);
        }
        for (TreeNode treeNode : pNode.getChildren()) {
            getLoopNode(treeNode);
        }
    }

    private void findUpGroupNode(List<TreeNode> newLoopUpGroups, OutputTreeNode node) {
        if (node.eContainer() instanceof OutputTreeNode) {
            OutputTreeNode parent = (OutputTreeNode) node.eContainer();
            if (parent.isGroup()) {
                newLoopUpGroups.add(parent);
            }
            findUpGroupNode(newLoopUpGroups, parent);
        }
    }

    private void upsetGroup(TreeNode node, TreeNode rootGroup) {
        if (node.eContainer() instanceof TreeNode && rootGroup != node.eContainer()) {
            TreeNode parent = (TreeNode) node.eContainer();
            if (!parent.isGroup()) {
                parent.setGroup(true);
            }
        }
    }

    public void setMapperManager(MapperManager mapperManager) {
        this.mapperManager = mapperManager;
    }

    public boolean isInput() {
        return input;
    }

    public void setInput(boolean input) {
        this.input = input;
    }

}
