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
package org.talend.designer.xmlmap.parts.directedit;

import java.lang.reflect.Constructor;

import org.eclipse.draw2d.Figure;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.tools.CellEditorLocator;
import org.eclipse.gef.tools.DirectEditManager;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;
import org.talend.commons.ui.expressionbuilder.IExpressionBuilderDialogController;
import org.talend.commons.ui.swt.tableviewer.celleditor.CellEditorDialogBehavior;
import org.talend.commons.ui.swt.tableviewer.celleditor.ExtendedTextCellEditor;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.IService;
import org.talend.designer.xmlmap.figures.IComboCell;
import org.talend.designer.xmlmap.figures.IExpressionBuilderCell;
import org.talend.designer.xmlmap.figures.ITextCell;
import org.talend.designer.xmlmap.model.emf.xmlmap.TreeNode;
import org.talend.designer.xmlmap.model.emf.xmlmap.VarNode;
import org.talend.expressionbuilder.IExpressionBuilderDialogService;

/**
 * DOC talend class global comment. Detailled comment
 */
public class XmlMapNodeDirectEditManager extends DirectEditManager {

    private TreeNode treeNodeModel;

    private VarNode varNodeModel;

    private GraphicalEditPart source;

    private CellEditorLocator locator;

    private String[] items;

    public XmlMapNodeDirectEditManager(GraphicalEditPart source, CellEditorLocator locator) {
        super(source, null, locator);
        this.source = source;
        this.locator = locator;
        Object model = source.getModel();
        if (model instanceof TreeNode) {
            this.treeNodeModel = (TreeNode) model;
        }
        if (model instanceof VarNode) {
            this.varNodeModel = (VarNode) model;
        }
    }

    public XmlMapNodeDirectEditManager(GraphicalEditPart source, Class editorType, CellEditorLocator locator, String[] items) {
        super(source, editorType, locator);
        this.source = source;
        this.locator = locator;
        Object model = source.getModel();
        if (model instanceof TreeNode) {
            this.treeNodeModel = (TreeNode) model;
        }
        if (model instanceof VarNode) {
            this.varNodeModel = (VarNode) model;
        }
        this.items = items;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.gef.tools.DirectEditManager#initCellEditor()
     */
    @Override
    protected void initCellEditor() {
        if (treeNodeModel != null) {
            getCellEditor().setValue(treeNodeModel.getExpression());
            Text text = ((ExtendedTextCellEditor) getCellEditor()).getTextControl();
            text.selectAll();
        } else if (varNodeModel != null) {
            Control control = getCellEditor().getControl();
            if (control instanceof CCombo) {
                CCombo combo = (CCombo) control;
                combo.setText(varNodeModel.getType());
            } else if (control instanceof Text) {
                String variable = varNodeModel.getVariable();
                if (variable == null) {
                    variable = "";
                }
                getCellEditor().setValue(variable);
                Text text = (Text) ((TextCellEditor) getCellEditor()).getControl();
                text.selectAll();
            } else {
                getCellEditor().setValue(varNodeModel.getExpression());
                Text text = ((ExtendedTextCellEditor) getCellEditor()).getTextControl();
                text.selectAll();
            }
            // getCellEditor().setValue(varNodeModel.getExpression());
        }
    }

    protected CellEditor createCellEditorOn(Composite composite) {
        Composite parent = (Composite) source.getViewer().getControl();
        CellEditor cellEditor = null;
        if (treeNodeModel != null) {

            IService expressionBuilderDialogService = GlobalServiceRegister.getDefault().getService(
                    IExpressionBuilderDialogService.class);
            CellEditorDialogBehavior behavior = new CellEditorDialogBehavior();
            cellEditor = new ExpressionCellEditor(composite, behavior);
            ((ExpressionCellEditor) cellEditor).setOwnerId(treeNodeModel.getXpath());
            IExpressionBuilderDialogController dialog = ((IExpressionBuilderDialogService) expressionBuilderDialogService)
                    .getExpressionBuilderInstance(parent, (ExpressionCellEditor) cellEditor, null);

            behavior.setCellEditorDialog(dialog);
        } else if (varNodeModel != null) {
            Figure figure = null;
            if (this.locator instanceof XmlMapNodeCellEditorLocator) {
                XmlMapNodeCellEditorLocator lo = (XmlMapNodeCellEditorLocator) locator;
                figure = lo.getFigure();
            }
            if (figure instanceof IComboCell) {
                try {
                    Constructor constructor = ComboBoxCellEditor.class.getConstructor(new Class[] { Composite.class,
                            String[].class });
                    cellEditor = (CellEditor) constructor.newInstance(new Object[] { composite, items });
                } catch (Exception e) {
                    return null;
                }
            } else if (figure instanceof ITextCell) {
                cellEditor = new TextCellEditor(composite);
            } else if (figure instanceof IExpressionBuilderCell && varNodeModel != null) {
                IService expressionBuilderDialogService = GlobalServiceRegister.getDefault().getService(
                        IExpressionBuilderDialogService.class);
                CellEditorDialogBehavior behavior = new CellEditorDialogBehavior();
                cellEditor = new ExpressionCellEditor(composite, behavior);
                ((ExpressionCellEditor) cellEditor).setOwnerId(varNodeModel.getExpression());
                IExpressionBuilderDialogController dialog = ((IExpressionBuilderDialogService) expressionBuilderDialogService)
                        .getExpressionBuilderInstance(parent, (ExpressionCellEditor) cellEditor, null);

                behavior.setCellEditorDialog(dialog);
            }

        }
        return cellEditor;
    }

    @Override
    protected boolean isDirty() {
        Object model = getEditPart().getModel();
        if (model instanceof VarNode) {
            Control control = getCellEditor().getControl();
            if (control instanceof CCombo) {

                return !((CCombo) control).getText().equals(((VarNode) model).getType());
            } else if (control instanceof Text) {
                return !((Text) control).getText().equals(((VarNode) model).getVariable());
            }
        }
        return false;
    }
}
