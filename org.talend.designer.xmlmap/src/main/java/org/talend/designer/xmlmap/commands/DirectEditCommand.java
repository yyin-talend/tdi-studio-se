// ============================================================================
//
// Copyright (C) 2006-2012 Talend Inc. - www.talend.com
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

import java.util.ArrayList;
import java.util.List;
import java.util.regex.PatternSyntaxException;

import org.eclipse.emf.common.util.EList;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.Command;
import org.talend.core.model.metadata.types.JavaType;
import org.talend.core.model.metadata.types.JavaTypesManager;
import org.talend.designer.xmlmap.model.emf.xmlmap.AbstractInOutTree;
import org.talend.designer.xmlmap.model.emf.xmlmap.AbstractNode;
import org.talend.designer.xmlmap.model.emf.xmlmap.Connection;
import org.talend.designer.xmlmap.model.emf.xmlmap.INodeConnection;
import org.talend.designer.xmlmap.model.emf.xmlmap.InputLoopNodesTable;
import org.talend.designer.xmlmap.model.emf.xmlmap.InputXmlTree;
import org.talend.designer.xmlmap.model.emf.xmlmap.LookupConnection;
import org.talend.designer.xmlmap.model.emf.xmlmap.NodeType;
import org.talend.designer.xmlmap.model.emf.xmlmap.OutputTreeNode;
import org.talend.designer.xmlmap.model.emf.xmlmap.OutputXmlTree;
import org.talend.designer.xmlmap.model.emf.xmlmap.TreeNode;
import org.talend.designer.xmlmap.model.emf.xmlmap.VarNode;
import org.talend.designer.xmlmap.model.emf.xmlmap.VarTable;
import org.talend.designer.xmlmap.model.emf.xmlmap.XmlMapData;
import org.talend.designer.xmlmap.model.emf.xmlmap.XmlmapFactory;
import org.talend.designer.xmlmap.parts.directedit.DirectEditType;
import org.talend.designer.xmlmap.ui.expressionutil.TableEntryLocation;
import org.talend.designer.xmlmap.ui.expressionutil.XmlMapExpressionManager;
import org.talend.designer.xmlmap.util.XmlMapUtil;

/**
 * DOC talend class global comment. Detailled comment
 */
public class DirectEditCommand extends Command {

    private AbstractNode model;

    private Object newValue;

    private DirectEditType type;

    private EditPart targetEditPart;

    private XmlMapData mapperData;

    protected XmlMapExpressionManager expressionManager = new XmlMapExpressionManager();

    public DirectEditCommand() {

    }

    public DirectEditCommand(AbstractNode model, DirectEditType type, Object newValue) {
        this.model = (AbstractNode) model;
        this.newValue = newValue;
        this.type = type;
    }

    public DirectEditCommand(EditPart targetEditPart, AbstractNode model, DirectEditType type, Object newValue) {
        this.targetEditPart = targetEditPart;
        this.model = (AbstractNode) model;
        this.newValue = newValue;
        this.type = type;
    }

    @Override
    public void execute() {
        try {
            if (model != null) {
                if (DirectEditType.EXPRESSION.equals(type)) {

                    List<TableEntryLocation> matchedLocations = expressionManager.parseTableEntryLocation((String) newValue);

                    EList<? extends INodeConnection> connections = null;
                    if (model instanceof OutputTreeNode || model instanceof VarNode) {
                        connections = model.getIncomingConnections();
                    } else if (model instanceof TreeNode) {
                        connections = ((TreeNode) model).getLookupIncomingConnections();
                    }

                    List usefullConnections = new ArrayList();

                    mapperData = XmlMapUtil.getXmlMapData(model);
                    if (!matchedLocations.isEmpty()) {
                        for (int i = 0; i < matchedLocations.size(); i++) {
                            TableEntryLocation currentLocation = matchedLocations.get(i);
                            boolean found = false;
                            for (INodeConnection conn : connections) {
                                TableEntryLocation sourceLocation = null;
                                if (conn.getSource() instanceof TreeNode) {
                                    sourceLocation = expressionManager.parseTableEntryLocation(
                                            XmlMapUtil.convertToExpression(((TreeNode) conn.getSource()).getXpath())).get(0);
                                } else if (conn.getSource() instanceof VarNode) {
                                    VarNode varNode = (VarNode) conn.getSource();
                                    sourceLocation = new TableEntryLocation(((VarTable) varNode.eContainer()).getName(),
                                            varNode.getName());
                                }
                                if (currentLocation.equals(sourceLocation)) {
                                    found = true;
                                    usefullConnections.add(conn);
                                    break;
                                }
                            }
                            if (!found) {
                                if (mapperData != null) {
                                    String convertToXpath = XmlMapUtil.convertToXpath(currentLocation.toString());
                                    boolean findFromVar = false;
                                    if (model instanceof OutputTreeNode) {
                                        findFromVar = true;
                                    }

                                    AbstractNode sourceNode = findConnectionSource(mapperData, currentLocation,
                                            XmlMapUtil.getXPathLength(convertToXpath), findFromVar);
                                    if (sourceNode != null) {
                                        INodeConnection connection = null;
                                        if (model instanceof OutputTreeNode || model instanceof VarNode) {
                                            connection = XmlmapFactory.eINSTANCE.createConnection();
                                            sourceNode.getOutgoingConnections().add((Connection) connection);
                                            model.getIncomingConnections().add((Connection) connection);

                                        } else if (model instanceof TreeNode && sourceNode instanceof TreeNode) {
                                            TreeNode source = (TreeNode) sourceNode;
                                            connection = XmlmapFactory.eINSTANCE.createLookupConnection();
                                            source.getLookupOutgoingConnections().add((LookupConnection) connection);
                                            ((TreeNode) model).getLookupIncomingConnections().add((LookupConnection) connection);
                                        }

                                        //
                                        if (model instanceof OutputTreeNode && sourceNode instanceof TreeNode) {
                                            createInputLoopTable((TreeNode) sourceNode, (OutputTreeNode) model);
                                        }

                                        connection.setSource(sourceNode);
                                        connection.setTarget(model);
                                        mapperData.getConnections().add(connection);
                                        usefullConnections.add(connection);
                                    }
                                }
                            }
                        }
                    } else {
                        if (!connections.isEmpty()) {
                            if (model instanceof OutputTreeNode || model instanceof VarNode) {
                                XmlMapUtil.detachConnectionsSouce(model, mapperData, false);
                                model.getIncomingConnections().clear();
                            } else if (model instanceof TreeNode) {
                                XmlMapUtil.detachLookupSource((TreeNode) model, mapperData);
                                ((TreeNode) model).getLookupIncomingConnections().clear();
                            }
                        }
                    }
                    List<INodeConnection> copyOfConnections = new ArrayList<INodeConnection>(connections);
                    copyOfConnections.removeAll(usefullConnections);
                    if (model instanceof OutputTreeNode || model instanceof VarNode) {
                        for (INodeConnection connection : copyOfConnections) {
                            if (connection.getSource() != null) {
                                if (connection.getSource().getOutgoingConnections().contains(connection)) {
                                    connection.getSource().getOutgoingConnections().remove(connection);
                                    mapperData.getConnections().remove(connection);
                                }
                            }
                        }
                        model.getIncomingConnections().removeAll(copyOfConnections);

                    } else if (model instanceof TreeNode) {
                        for (INodeConnection connection : copyOfConnections) {
                            if (connection.getSource() != null) {
                                if (((TreeNode) connection.getSource()).getLookupOutgoingConnections().contains(connection)) {
                                    ((TreeNode) connection.getSource()).getLookupOutgoingConnections().remove(connection);
                                    mapperData.getConnections().remove(connection);
                                }
                            }
                        }
                        ((TreeNode) model).getLookupIncomingConnections().removeAll(copyOfConnections);

                    }

                    if (model instanceof OutputTreeNode) {
                        OutputTreeNode outModel = (OutputTreeNode) model;
                        if (NodeType.NAME_SPACE.equals(outModel.getNodeType()) && outModel.getExpression() != null
                                && !"".equals(model.getExpression())) {
                            outModel.setDefaultValue("");
                        }
                    }

                } else if (DirectEditType.VAR_NODE_TYPE.equals(type)) {
                    VarNode varModel = (VarNode) model;
                    JavaType javaTypeFromLabel = JavaTypesManager.getJavaTypeFromLabel((String) newValue);
                    if (javaTypeFromLabel == null) {
                        javaTypeFromLabel = JavaTypesManager.getDefaultJavaType();
                    }
                    varModel.setType(javaTypeFromLabel.getId());
                } else if (DirectEditType.NODE_NAME.equals(type)) {
                    model.setName((String) newValue);
                }

                model.setExpression((String) newValue);
            }

        } catch (PatternSyntaxException ex) {
            // Syntax error in the regular expression
        }
    }

    private void createInputLoopTable(TreeNode sourceNode, OutputTreeNode targetOutputNode) {

        InputLoopNodesTable inputLoopNodesTable = null;
        AbstractInOutTree abstractTree = XmlMapUtil.getAbstractInOutTree(targetOutputNode);
        if (abstractTree != null && abstractTree instanceof OutputXmlTree) {
            List<InputLoopNodesTable> listInputLoopNodesTablesEntry = ((OutputXmlTree) abstractTree).getInputLoopNodesTables();
            if (!XmlMapUtil.hasDocument(abstractTree)) {
                if (listInputLoopNodesTablesEntry != null && listInputLoopNodesTablesEntry.size() == 0) {
                    inputLoopNodesTable = XmlmapFactory.eINSTANCE.createInputLoopNodesTable();
                    listInputLoopNodesTablesEntry.add(inputLoopNodesTable);
                } else if (listInputLoopNodesTablesEntry != null && listInputLoopNodesTablesEntry.size() == 1) {
                    inputLoopNodesTable = listInputLoopNodesTablesEntry.get(0);
                }
            } else {
                OutputTreeNode loopParentOutputTreeNode = (OutputTreeNode) XmlMapUtil.getLoopParentNode(targetOutputNode);
                if (loopParentOutputTreeNode != null) {
                    inputLoopNodesTable = loopParentOutputTreeNode.getInputLoopNodesTable();
                    if (inputLoopNodesTable == null) {
                        inputLoopNodesTable = XmlmapFactory.eINSTANCE.createInputLoopNodesTable();
                        loopParentOutputTreeNode.setInputLoopNodesTable(inputLoopNodesTable);
                        listInputLoopNodesTablesEntry.add(inputLoopNodesTable);
                    }
                }
            }
            if (inputLoopNodesTable == null) {
                return;
            }

            // fix for TDI-20360
            InputXmlTree inputTree = (InputXmlTree) XmlMapUtil.getAbstractInOutTree(sourceNode);
            TreeNode loopParentTreeNode = null;
            List<TreeNode> soruceLoops = new ArrayList<TreeNode>();

            if (inputTree != null) {
                if (!inputTree.isLookup() && inputTree.isMultiLoops()) {
                    loopParentTreeNode = XmlMapUtil.getLoopParentNode((TreeNode) sourceNode);
                    if (loopParentTreeNode != null) {
                        soruceLoops.add(loopParentTreeNode);
                    } else if (inputLoopNodesTable.getInputloopnodes().isEmpty()) {
                        loopParentTreeNode = XmlMapUtil.getFirstLoopOfATree(inputTree);
                        if (loopParentTreeNode != null) {
                            soruceLoops.add(loopParentTreeNode);
                        }
                    }
                } else {
                    // if lookup node connected with main table and the source node is under loop , add this loop to
                    // InputLoopTable for output
                    inputTree = null;
                    for (InputXmlTree input : mapperData.getInputTrees()) {
                        if (!input.isLookup()) {
                            inputTree = input;
                            break;
                        }
                    }
                    if (inputTree != null && inputTree.isMultiLoops()) {
                        EList<LookupConnection> lookupIncomingConnections = sourceNode.getLookupIncomingConnections();
                        boolean atLeastOneLookupFromMain = false;
                        for (LookupConnection lookupConn : lookupIncomingConnections) {
                            TreeNode sourceTreeNode = (TreeNode) lookupConn.getSource();
                            AbstractInOutTree sourceTree = XmlMapUtil.getAbstractInOutTree(sourceTreeNode);
                            // only check when source tree is main , for other case need to do it latter...
                            if (sourceTree == inputTree) {
                                atLeastOneLookupFromMain = true;
                                loopParentTreeNode = XmlMapUtil.getLoopParentNode(sourceTreeNode);
                                if (loopParentTreeNode != null && !soruceLoops.contains(loopParentTreeNode)) {
                                    soruceLoops.add(loopParentTreeNode);
                                }
                            }
                        }
                        if (soruceLoops.isEmpty() && atLeastOneLookupFromMain) {
                            loopParentTreeNode = XmlMapUtil.getFirstLoopOfATree(inputTree);
                            if (loopParentTreeNode != null) {
                                soruceLoops.add(loopParentTreeNode);
                            }
                        }
                    }
                }
            }

            for (TreeNode sourceLoop : soruceLoops) {
                if (!inputLoopNodesTable.getInputloopnodes().contains(sourceLoop)) {
                    inputLoopNodesTable.getInputloopnodes().add(sourceLoop);
                }
            }
        }
    }

    protected AbstractNode findConnectionSource(XmlMapData mapperData, TableEntryLocation matchedLocation, int xpathLength,
            boolean findFromVar) {
        if (mapperData == null) {
            return null;
        }
        AbstractNode node = findConnectionSource(mapperData.getInputTrees(), matchedLocation, xpathLength);

        if (node == null && findFromVar) {
            if (xpathLength == 2) {
                VarTable varTable = mapperData.getVarTables().get(0);
                if (varTable.getName() != null && varTable.getName().equals(matchedLocation.getTableName())) {
                    EList<VarNode> nodes = varTable.getNodes();
                    for (VarNode varNode : nodes) {
                        if (varNode.getName() != null && varNode.getName().equals(matchedLocation.getColumnValue())) {
                            node = varNode;
                        }
                    }
                }
            }
        }
        if (node instanceof TreeNode) {
            if (((TreeNode) node).getChildren().isEmpty()) {
                return node;
            } else {
                return null;
            }
        }
        return node;
    }

    private TreeNode findConnectionSource(List<InputXmlTree> inputTrees, TableEntryLocation matchedLocation, int xpathLength) {
        for (InputXmlTree inputTree : inputTrees) {
            for (TreeNode node : inputTree.getNodes()) {
                TreeNode source = findConnectionSource(node, matchedLocation, xpathLength);
                if (source != null) {
                    return source;
                }
            }
        }

        return null;
    }

    private TreeNode findConnectionSource(TreeNode treeNode, TableEntryLocation matchedLocation, int xpathLength) {
        int sourceXpathLength = XmlMapUtil.getXPathLength(treeNode.getXpath());
        if (xpathLength == sourceXpathLength) {
            TableEntryLocation sourceLocation = expressionManager.parseTableEntryLocation(
                    XmlMapUtil.convertToExpression(treeNode.getXpath())).get(0);
            if (matchedLocation.equals(sourceLocation)) {
                return treeNode;
            }
        } else if (xpathLength > sourceXpathLength) {
            for (TreeNode child : treeNode.getChildren()) {
                TreeNode source = findConnectionSource(child, matchedLocation, xpathLength);
                if (source != null) {
                    return source;
                }
            }
        }
        return null;
    }

}
