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
import org.talend.designer.xmlmap.model.emf.xmlmap.XmlMapData;
import org.talend.designer.xmlmap.model.emf.xmlmap.XmlmapFactory;
import org.talend.designer.xmlmap.util.XmlMapUtil;

/**
 * DOC talend class global comment. Detailled comment
 */
public class DirectEditCommand extends Command {

    private TreeNode model;

    private String expression;

    private final String XPRESSION_PATTERN = "(\\[\\s*\\w+\\.\\w+\\s*:\\s*(/\\w+)+\\s*\\])|((?!\\[)\\s*\\w+\\.\\w+(?!\\]))";

    public DirectEditCommand(TreeNode model, String expression) {
        this.model = model;
        this.expression = expression;
    }

    @Override
    public void execute() {
        try {
            model.setExpression(expression);
            // String[] splitExpressions = XmlMapUtil.splitExpressions(expression);
            if (model instanceof OutputTreeNode) {
                OutputTreeNode outputNode = (OutputTreeNode) model;
                // match tree expression

                Pattern regex = Pattern.compile(XPRESSION_PATTERN, Pattern.CANON_EQ | Pattern.CASE_INSENSITIVE //$NON-NLS-1$
                        | Pattern.MULTILINE);
                Matcher regexMatcher = regex.matcher("[row1.newColumn:/root/person/city] fdssdfs [row2.aa:/root/person/city]");
                List<String> matched = new ArrayList<String>();
                while (regexMatcher.find()) {
                    matched.add(regexMatcher.group());
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
                                }
                            }
                        }
                        if (!found) {
                            if (mapperData == null) {
                                mapperData = getMapperData(model);
                            }

                            Connection connection = XmlmapFactory.eINSTANCE.createConnection();
                            // connection.setSource(value);
                        }
                    }
                }
            }
        } catch (PatternSyntaxException ex) {
            // Syntax error in the regular expression
        }

    }

    private XmlMapData getMapperData(TreeNode treeNode) {

        return null;
    }

    private TreeNode findSource(List<InputXmlTree> inputTrees, String xpath) {
        return null;
    }

}
