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
package org.talend.designer.xmlmap.parts;

import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.tools.CellEditorLocator;
import org.eclipse.gef.tools.DirectEditManager;
import org.eclipse.swt.widgets.Text;
import org.talend.designer.xmlmap.model.emf.xmlmap.TreeNode;

/**
 * DOC talend class global comment. Detailled comment
 */
public class ExpressionDirectEditManager extends DirectEditManager {

    private TreeNode model;

    public ExpressionDirectEditManager(GraphicalEditPart source, Class editorType, CellEditorLocator locator) {
        super(source, editorType, locator);
        model = (TreeNode) source.getModel();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.gef.tools.DirectEditManager#initCellEditor()
     */
    @Override
    protected void initCellEditor() {
        getCellEditor().setValue(model.getExpression());
        Text text = (Text) getCellEditor().getControl();
        text.selectAll();
    }

}
