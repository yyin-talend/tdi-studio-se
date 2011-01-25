// ============================================================================
//
// Copyright (C) 2006-2010 Talend Inc. - www.talend.com
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
import org.talend.designer.xmlmap.model.emf.xmlmap.Connection;
import org.talend.designer.xmlmap.model.emf.xmlmap.InputXmlTree;
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

    private TreeNode model;

    private VarNode varModel;

    private String expression;

    private final String XPRESSION_PATTERN = "(\\[\\s*\\w+\\.\\w+\\s*:\\s*(/\\w+)+\\s*\\])|((?!\\[)\\s*\\w+\\.\\w+(?!\\]))";

    private EXMLMapNodeProperty property;

    private Object newValue;

    private boolean isModifyCell;

    public DirectEditCommand(Object model, String expression) {
        if (model instanceof TreeNode) {
            this.model = (TreeNode) model;
        }
        if (model instanceof VarNode) {
            this.varModel = (VarNode) model;
        }
        this.expression = expression;
    }

    public DirectEditCommand(Object model, EXMLMapNodeProperty property, Object newValue, boolean isModifyCell) {
        if (model instanceof TreeNode) {
            this.model = (TreeNode) model;
        }
        if (model instanceof VarNode) {
            this.varModel = (VarNode) model;
        }
        this.property = property;
        this.newValue = newValue;
        this.isModifyCell = isModifyCell;
    }

    @Override
    public void execute() {
        try {
            if (model != null) {
                model.setExpression(expression);
                // String[] splitExpressions = XmlMapUtil.splitExpressions(expression);
                if (model instanceof OutputTreeNode) {
                    OutputTreeNode outputNode = (OutputTreeNode) model;
                    // match tree expression

                    Pattern regex = Pattern.compile(XPRESSION_PATTERN, Pattern.CANON_EQ | Pattern.CASE_INSENSITIVE //$NON-NLS-1$
                            | Pattern.MULTILINE);
                    Matcher regexMatcher = regex.matcher(expression);
                    List<String> matched = new ArrayList<String>();
                    while (regexMatcher.find()) {
                        matched.add(regexMatcher.group().trim());
                    }
                    EList<Connection> incomingConnections = ((OutputTreeNode) model).getIncomingConnections();

                    if (!matched.isEmpty()) {
                        XmlMapData mapperData = null;
                        for (int i = 0; i < matched.size(); i++) {
                            String convertToXpath = XmlMapUtil.convertToXpath(matched.get(i));
                            boolean found = false;
                            for (Connection conn : incomingConnections) {
                                if (conn.getSource() instanceof TreeNode) {
                                    if (convertToXpath != null && convertToXpath.equals(((TreeNode) conn.getSource()).getXpath())) {
                                        found = true;
                                        break;
                                    }
                                }
                            }
                            if (!found) {
                                if (mapperData == null) {
                                    mapperData = getMapperData(model);
                                }
                                if (mapperData != null) {
                                    TreeNode sourceNode = findConnectionSource(mapperData.getInputTrees(), convertToXpath);
                                    if (sourceNode != null) {
                                        Connection connection = XmlmapFactory.eINSTANCE.createConnection();
                                        sourceNode.getOutgoingConnections().add(connection);
                                        outputNode.getIncomingConnections().add(connection);
                                        mapperData.getConnections().add(connection);
                                    }
                                }
                                // connection.setSource(value);
                            }
                        }
                    }
                }
            } else if (varModel != null) {
                if (isModifyCell) {
                    switch (property) {
                    case VARNODE_TYPE:
                        varModel.setType((String) newValue);
                        break;
                    case VARNODE_VARIABLE:
                        varModel.setVariable((String) newValue);
                        break;
                    }
                }
            }

        } catch (PatternSyntaxException ex) {
            // Syntax error in the regular expression
        }

    }

    private XmlMapData getMapperData(TreeNode treeNode) {
        TreeNode rootNode = null;
        if (treeNode instanceof OutputTreeNode) {
            rootNode = XmlMapUtil.getOutputTreeNodeRoot((OutputTreeNode) treeNode);
        } else if (treeNode instanceof TreeNode) {
            rootNode = XmlMapUtil.getInputTreeNodeRoot(treeNode);
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
