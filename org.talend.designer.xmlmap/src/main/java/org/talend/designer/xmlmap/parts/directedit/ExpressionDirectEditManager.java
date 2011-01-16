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
package org.talend.designer.xmlmap.parts.directedit;

import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.tools.CellEditorLocator;
import org.eclipse.gef.tools.DirectEditManager;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.talend.commons.ui.expressionbuilder.IExpressionBuilderDialogController;
import org.talend.commons.ui.swt.tableviewer.celleditor.CellEditorDialogBehavior;
import org.talend.commons.ui.swt.tableviewer.celleditor.ExtendedTextCellEditor;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.IService;
import org.talend.designer.xmlmap.model.emf.xmlmap.TreeNode;
import org.talend.expressionbuilder.IExpressionBuilderDialogService;

/**
 * DOC talend class global comment. Detailled comment
 */
public class ExpressionDirectEditManager extends DirectEditManager {

    private TreeNode model;

    private GraphicalEditPart source;

    public ExpressionDirectEditManager(GraphicalEditPart source, CellEditorLocator locator) {
        super(source, null, locator);
        this.source = source;
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
        Text text = ((ExtendedTextCellEditor) getCellEditor()).getTextControl();
        text.selectAll();
    }

    protected CellEditor createCellEditorOn(Composite composite) {
        Composite parent = (Composite) source.getViewer().getControl();
        IService expressionBuilderDialogService = GlobalServiceRegister.getDefault().getService(
                IExpressionBuilderDialogService.class);
        CellEditorDialogBehavior behavior = new CellEditorDialogBehavior();
        ExpressionCellEditor cellEditor = new ExpressionCellEditor(composite, behavior);
        cellEditor.setOwnerId(model.getXpath());
        IExpressionBuilderDialogController dialog = ((IExpressionBuilderDialogService) expressionBuilderDialogService)
                .getExpressionBuilderInstance(parent, cellEditor, null);

        behavior.setCellEditorDialog(dialog);
        return cellEditor;
    }

}
