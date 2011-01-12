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

import org.apache.oro.text.regex.Perl5Compiler;
import org.apache.oro.text.regex.Perl5Matcher;
import org.eclipse.gef.commands.Command;
import org.talend.designer.xmlmap.model.emf.xmlmap.OutputTreeNode;
import org.talend.designer.xmlmap.model.emf.xmlmap.TreeNode;
import org.talend.designer.xmlmap.model.emf.xmlmap.XmlMapData;

/**
 * DOC talend class global comment. Detailled comment
 */
public class DirectEditCommand extends Command {

    private TreeNode model;

    private String expression;

    private Perl5Matcher matcher = new Perl5Matcher();

    private Perl5Compiler compiler = new Perl5Compiler();

    private final String REGEX_PATTERN = "\\s*(\\w+)\\s*\\.\\s*(\\w+)\\s*";

    private final String REGEX_PATTERN_TREE = "^\\[\\s*(\\w+)\\s*\\.\\s*(\\w+)\\s*\\:\\s*(\\w+)\\s*\\]$";

    public DirectEditCommand(TreeNode model, String expression) {
        this.model = model;
        this.expression = expression;
    }

    @Override
    public void execute() {
        model.setExpression(expression);
        XmlMapData mapperData = getMapperData(model);
        // String[] splitExpressions = XmlMapUtil.splitExpressions(expression);
        if (model instanceof OutputTreeNode) {
            OutputTreeNode outputNode = (OutputTreeNode) model;

            if (mapperData != null) {
                mapperData.getInputTrees();
            }
        }

    }

    private XmlMapData getMapperData(TreeNode treeNode) {
        return null;
    }

}
