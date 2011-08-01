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
package org.talend.designer.core.ui.editor.properties.controllers;

import java.util.Map;

import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.talend.core.model.process.IElementParameter;
import org.talend.designer.core.ui.editor.nodes.Node;

/**
 * DOC klliu class global comment. Detailled comment
 */
public class ImportRulesFromRepository implements SelectionListener {

    private Node node;

    /**
     * DOC klliu ExportRulesToRepository constructor comment.
     * 
     * @param generateGrammarController
     */
    public ImportRulesFromRepository(GenerateGrammarController generateGrammarController) {
        node = (Node) generateGrammarController.elem;
    }

    @Override
    public void widgetSelected(SelectionEvent e) {

    }

    @Override
    public void widgetDefaultSelected(SelectionEvent e) {
        IElementParameter elementParameter = node.getElementParameter("RULE_TABLE");
        Map<String, IElementParameter> childParameters = elementParameter.getChildParameters();
    }
}
