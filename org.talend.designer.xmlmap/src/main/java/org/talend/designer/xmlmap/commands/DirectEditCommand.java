// ============================================================================
//
// Copyright (C) 2006-2011 Talend Inc. - www.talend.com
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.eclipse.emf.common.util.EList;
import org.eclipse.gef.commands.Command;
import org.talend.core.model.metadata.types.JavaType;
import org.talend.core.model.metadata.types.JavaTypesManager;
import org.talend.designer.xmlmap.model.emf.xmlmap.AbstractNode;
import org.talend.designer.xmlmap.model.emf.xmlmap.Connection;
import org.talend.designer.xmlmap.model.emf.xmlmap.INodeConnection;
import org.talend.designer.xmlmap.model.emf.xmlmap.InputXmlTree;
import org.talend.designer.xmlmap.model.emf.xmlmap.LookupConnection;
import org.talend.designer.xmlmap.model.emf.xmlmap.NodeType;
import org.talend.designer.xmlmap.model.emf.xmlmap.OutputTreeNode;
import org.talend.designer.xmlmap.model.emf.xmlmap.TreeNode;
import org.talend.designer.xmlmap.model.emf.xmlmap.VarNode;
import org.talend.designer.xmlmap.model.emf.xmlmap.VarTable;
import org.talend.designer.xmlmap.model.emf.xmlmap.XmlMapData;
import org.talend.designer.xmlmap.model.emf.xmlmap.XmlmapFactory;
import org.talend.designer.xmlmap.parts.directedit.DirectEditType;
import org.talend.designer.xmlmap.util.XmlMapUtil;

/**
 * DOC talend class global comment. Detailled comment
 */
public class DirectEditCommand extends Command {

    private AbstractNode model;

    private final String XPRESSION_PATTERN = "(\\[\\s*\\w+\\.\\w+\\s*:\\s*(/\\w+)+(/@\\w+)*\\s*\\])|((?!\\[)\\s*\\w+\\.\\w+(?!\\]))";

    private Object newValue;

    private DirectEditType type;

    public DirectEditCommand(AbstractNode model, DirectEditType type, Object newValue) {
        this.model = (AbstractNode) model;
        this.newValue = newValue;
        this.type = type;
    }

    @Override
    public void execute() {
        try {
            if (model != null) {
                if (DirectEditType.EXPRESSION.equals(type)) {
                    model.setExpression((String) newValue);

                    Pattern regex = Pattern.compile(XPRESSION_PATTERN, Pattern.CANON_EQ | Pattern.CASE_INSENSITIVE //$NON-NLS-1$
                            | Pattern.MULTILINE);
                    Matcher regexMatcher = regex.matcher((String) newValue);
                    List<String> matched = new ArrayList<String>();
                    while (regexMatcher.find()) {
                        matched.add(regexMatcher.group().trim());
                    }
                    EList<? extends INodeConnection> connections = null;
                    if (model instanceof OutputTreeNode || model instanceof VarNode) {
                        connections = model.getIncomingConnections();
                    } else if (model instanceof TreeNode) {
                        connections = ((TreeNode) model).getLookupIncomingConnections();
                    }

                    List usefullConnections = new ArrayList();

                    XmlMapData mapperData = getMapperData(model);
                    if (!matched.isEmpty()) {
                        for (int i = 0; i < matched.size(); i++) {
                            String convertToXpath = XmlMapUtil.convertToXpath(matched.get(i));
                            boolean found = false;
                            for (INodeConnection conn : connections) {
                                if (conn.getSource() instanceof TreeNode) {
                                    if (convertToXpath != null && convertToXpath.equals(((TreeNode) conn.getSource()).getXpath())) {
                                        found = true;
                                        usefullConnections.add(conn);
                                        break;
                                    }
                                }
                            }
                            if (!found) {
                                if (mapperData != null) {
                                    boolean findFromVar = false;
                                    if (model instanceof OutputTreeNode) {
                                        findFromVar = true;
                                    }
                                    AbstractNode sourceNode = findConnectionSource(mapperData, convertToXpath, findFromVar);
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
                                XmlMapUtil.detachConnectionsSouce(model, mapperData);
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
            }

        } catch (PatternSyntaxException ex) {
            // Syntax error in the regular expression
        }
    }

    private XmlMapData getMapperData(AbstractNode treeNode) {
        AbstractNode rootNode = null;
        if (treeNode instanceof OutputTreeNode) {
            rootNode = XmlMapUtil.getOutputTreeNodeRoot((OutputTreeNode) treeNode);
        } else if (treeNode instanceof TreeNode) {
            rootNode = XmlMapUtil.getInputTreeNodeRoot((TreeNode) treeNode);
        } else if (treeNode instanceof VarNode) {
            return (XmlMapData) treeNode.eContainer().eContainer();
        }
        if (rootNode != null && rootNode.eContainer() != null && rootNode.eContainer().eContainer() instanceof XmlMapData) {
            return (XmlMapData) rootNode.eContainer().eContainer();
        }
        return null;
    }

    private AbstractNode findConnectionSource(XmlMapData mapperData, String xpath, boolean findFromVar) {
        if (xpath == null || mapperData == null) {
            return null;
        }
        AbstractNode node = findConnectionSource(mapperData.getInputTrees(), xpath);

        if (node == null && findFromVar) {
            String[] split = xpath.split(XmlMapUtil.XPATH_SEPARATOR);
            if (split.length == 2) {
                VarTable varTable = mapperData.getVarTables().get(0);
                if (varTable.getName() != null && varTable.getName().equals(split[0])) {
                    EList<VarNode> nodes = varTable.getNodes();
                    for (VarNode varNode : nodes) {
                        if (varNode.getName() != null && varNode.getName().equals(split[1])) {
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

    private TreeNode findConnectionSource(List<InputXmlTree> inputTrees, String xpath) {
        int xPathLength = XmlMapUtil.getXPathLength(xpath);
        TreeNode source = null;
        for (InputXmlTree inputTree : inputTrees) {
            for (TreeNode node : inputTree.getNodes()) {
                if (xpath.equals(node.getXpath())) {
                    return node;
                } else if (xPathLength > XmlMapUtil.getXPathLength(node.getXpath())) {
                    source = findConnectionSource(node, xpath, xPathLength);
                    if (source != null) {
                        return source;
                    }
                }
            }
        }

        return null;
    }

    private TreeNode findConnectionSource(TreeNode treeNode, String xpath, int xPathLength) {
        TreeNode source = null;
        for (TreeNode node : treeNode.getChildren()) {
            if (xpath.equals(node.getXpath())) {
                return node;
            } else if (xPathLength > XmlMapUtil.getXPathLength(node.getXpath())) {
                source = findConnectionSource(node, xpath, xPathLength);
                if (source != null) {
                    return source;
                }
            }
        }
        return null;
    }

}
