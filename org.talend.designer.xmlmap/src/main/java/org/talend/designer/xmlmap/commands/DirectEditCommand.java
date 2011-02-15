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
import org.talend.designer.xmlmap.model.emf.xmlmap.AbstractNode;
import org.talend.designer.xmlmap.model.emf.xmlmap.Connection;
import org.talend.designer.xmlmap.model.emf.xmlmap.IConnection;
import org.talend.designer.xmlmap.model.emf.xmlmap.InputXmlTree;
import org.talend.designer.xmlmap.model.emf.xmlmap.LookupConnection;
import org.talend.designer.xmlmap.model.emf.xmlmap.OutputTreeNode;
import org.talend.designer.xmlmap.model.emf.xmlmap.TreeNode;
import org.talend.designer.xmlmap.model.emf.xmlmap.VarNode;
import org.talend.designer.xmlmap.model.emf.xmlmap.XmlMapData;
import org.talend.designer.xmlmap.model.emf.xmlmap.XmlmapFactory;
import org.talend.designer.xmlmap.util.XmlMapUtil;

/**
 * DOC talend class global comment. Detailled comment
 */
public class DirectEditCommand extends Command {

    private AbstractNode model;

    private String expression;

    private final String XPRESSION_PATTERN = "(\\[\\s*\\w+\\.\\w+\\s*:\\s*(/\\w+)+(/@\\w+)*\\s*\\])|((?!\\[)\\s*\\w+\\.\\w+(?!\\]))";

    private EXMLMapNodeProperty property;

    private Object newValue;

    private boolean isExpression = true;

    public DirectEditCommand(AbstractNode model, String expression) {
        this.model = (AbstractNode) model;
        this.expression = expression;
    }

    public DirectEditCommand(AbstractNode model, EXMLMapNodeProperty property, Object newValue, boolean isExpression) {
        this.model = (AbstractNode) model;
        this.property = property;
        this.newValue = newValue;
        this.isExpression = isExpression;
    }

    @Override
    public void execute() {
        try {
            if (model != null) {
                if (isExpression) {
                    model.setExpression(expression);

                    Pattern regex = Pattern.compile(XPRESSION_PATTERN, Pattern.CANON_EQ | Pattern.CASE_INSENSITIVE //$NON-NLS-1$
                            | Pattern.MULTILINE);
                    Matcher regexMatcher = regex.matcher(expression);
                    List<String> matched = new ArrayList<String>();
                    while (regexMatcher.find()) {
                        matched.add(regexMatcher.group().trim());
                    }
                    EList<? extends IConnection> connections = null;
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
                            for (IConnection conn : connections) {
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
                                    TreeNode sourceNode = findConnectionSource(mapperData.getInputTrees(), convertToXpath);
                                    if (sourceNode != null) {
                                        IConnection connection = null;
                                        if (model instanceof OutputTreeNode || model instanceof VarNode) {
                                            connection = XmlmapFactory.eINSTANCE.createConnection();
                                            sourceNode.getOutgoingConnections().add((Connection) connection);
                                            model.getIncomingConnections().add((Connection) connection);

                                        } else if (model instanceof TreeNode) {
                                            connection = XmlmapFactory.eINSTANCE.createLookupConnection();
                                            sourceNode.getLookupOutgoingConnections().add((LookupConnection) connection);
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
                    List<IConnection> copyOfConnections = new ArrayList<IConnection>(connections);
                    copyOfConnections.removeAll(usefullConnections);
                    if (model instanceof OutputTreeNode || model instanceof VarNode) {
                        for (IConnection connection : copyOfConnections) {
                            if (connection.getSource() != null) {
                                if (connection.getSource().getOutgoingConnections().contains(connection)) {
                                    connection.getSource().getOutgoingConnections().remove(connection);
                                    mapperData.getConnections().remove(connection);
                                }
                            }
                        }
                        model.getIncomingConnections().removeAll(copyOfConnections);

                    } else if (model instanceof TreeNode) {
                        for (IConnection connection : copyOfConnections) {
                            if (connection.getSource() != null) {
                                if (((TreeNode) connection.getSource()).getLookupOutgoingConnections().contains(connection)) {
                                    ((TreeNode) connection.getSource()).getLookupOutgoingConnections().remove(connection);
                                    mapperData.getConnections().remove(connection);
                                }
                            }
                        }
                        ((TreeNode) model).getLookupIncomingConnections().removeAll(copyOfConnections);

                    }

                } else {
                    if (model instanceof VarNode) {
                        VarNode varModel = (VarNode) model;
                        switch (property) {
                        case VARNODE_TYPE:
                            varModel.setType((String) newValue);
                            break;
                        case VARNODE_VARIABLE:
                            varModel.setName((String) newValue);
                            break;
                        }
                    }
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

    private TreeNode findConnectionSource(List<InputXmlTree> inputTrees, String xpath) {
        if (xpath == null) {
            return null;
        }
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
