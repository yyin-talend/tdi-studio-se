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

import java.util.regex.PatternSyntaxException;

import org.eclipse.gef.commands.Command;
import org.talend.designer.xmlmap.model.emf.xmlmap.InputXmlTree;
import org.talend.designer.xmlmap.model.emf.xmlmap.OutputXmlTree;
import org.talend.designer.xmlmap.parts.directedit.DirectEditType;

/**
 * DOC talend class global comment. Detailled comment
 */
public class TreeSettingDirectEditCommand extends Command {

    private Object model;

    private final String XPRESSION_PATTERN = "(\\[\\s*\\w+\\.\\w+\\s*:\\s*(/\\w+)+(/@\\w+)*\\s*\\])|((?!\\[)\\s*\\w+\\.\\w+(?!\\]))";

    private Object newValue;

    private DirectEditType type;

    public TreeSettingDirectEditCommand(Object model, DirectEditType type, Object newValue) {
        this.model = model;
        this.newValue = newValue;
        this.type = type;
    }

    @Override
    public void execute() {
        try {
            if (model instanceof InputXmlTree) {
                InputXmlTree inputTree = (InputXmlTree) model;
                if (type != null) {
                    switch (type) {
                    case LOOKUP_MODEL:
                        inputTree.setLookupMode((String) newValue);
                        break;
                    case MATCH_MODEL:
                        inputTree.setMatchingMode((String) newValue);
                        break;
                    case JOIN_MODEL:
                        inputTree.setInnerJoin(Boolean.valueOf((String) newValue));
                        break;
                    case PERSISTENT_MODEL:
                        inputTree.setPersistent(Boolean.valueOf((String) newValue));
                        break;
                    case EXPRESSION_FILTER:
                        inputTree.setExpressionFilter((String) newValue);
                        break;
                    default:
                        break;
                    }
                }
            } else if (model instanceof OutputXmlTree) {
                OutputXmlTree outputTree = (OutputXmlTree) model;
                if (type != null) {
                    switch (type) {
                    case OUTPUT_REJECT:
                        outputTree.setReject(Boolean.valueOf((String) newValue));
                        break;
                    case LOOK_UP_INNER_JOIN_REJECT:
                        outputTree.setRejectInnerJoin(Boolean.valueOf((String) newValue));
                        break;
                    case EXPRESSION_FILTER:
                        outputTree.setExpressionFilter((String) newValue);
                        break;
                    }
                }
            }

        } catch (PatternSyntaxException ex) {
            // Syntax error in the regular expression
        }
    }

}
