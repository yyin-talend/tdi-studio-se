// ============================================================================
//
// Talend Community Edition
//
// Copyright (C) 2006 Talend - www.talend.com
//
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either
// version 2.1 of the License, or (at your option) any later version.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
// Lesser General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with this program; if not, write to the Free Software
// Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
//
// ============================================================================
package org.talend.designer.rowgenerator.ui.tabs;

import java.util.List;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.talend.designer.rowgenerator.data.Function;
import org.talend.designer.rowgenerator.data.ListParameter;
import org.talend.designer.rowgenerator.data.Parameter;
import org.talend.designer.rowgenerator.i18n.Messages;
import org.talend.designer.rowgenerator.ui.editor.RowGenTableEditor2;

/**
 * qzhang class global comment. Detailled comment <br/>
 * 
 * $Id: FunParaTableView.java,v 1.13 2007/01/31 05:20:52 pub Exp $
 * 
 */
public class FunParaTableView {

    private static final String NAME_PROPERTY = "Parameter"; //$NON-NLS-1$

    private static final String VALUE_PROPERTY = "Value"; //$NON-NLS-1$

    private static final String COMMENT_PROPERTY = "Comment"; //$NON-NLS-1$

    private Table table;

    private Composite composite;

    private Label titleLabel;

    private TableViewer viewer;

    private RowGenTableEditor2 rowGenTableEditor2;

    private Function function;

    /**
     * qzhang FunParaTableView constructor comment.
     * 
     * @param parentComposite
     * @param mainCompositeStyle
     */
    public FunParaTableView(Composite parentComposite, int mainCompositeStyle, RowGenTableEditor2 genTableEditor2,
            Function function) {
        this.function = function;
        this.composite = new Composite(parentComposite, mainCompositeStyle);
        this.rowGenTableEditor2 = genTableEditor2;
        GridLayout gridLayout = new GridLayout(1, true);
        composite.setLayout(gridLayout);
        GridData gridData = new GridData(GridData.FILL_BOTH);
        composite.setLayoutData(gridData);
        newTitle();
        newTable();
        update();
    }

    /**
     * qzhang Comment method "newTitle".
     */
    private void newTitle() {
        if (titleLabel == null || titleLabel.isDisposed()) {
            titleLabel = new Label(composite, SWT.NONE);
            titleLabel.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
            titleLabel.setVisible(true);
        }
    }

    /**
     * qzhang Comment method "newTable".
     */
    private void newTable() {
        if (table != null) {
            table.dispose();
        }
        table = new Table(composite, SWT.BORDER | SWT.MULTI | SWT.FULL_SELECTION);
        viewer = buildAndLayoutTable(table);
        attachContentProvider();
        attachLabelProvider();
        attachCellEditors();
    }

    /**
     * qzhang Comment method "attachCellEditors".
     * 
     * @param viewer2
     * @param table2
     */
    @SuppressWarnings("unchecked")
    private void attachCellEditors() {
        viewer.setCellModifier(new ICellModifier() {

            public boolean canModify(Object element, String property) {
                if (NAME_PROPERTY.equals(property) || COMMENT_PROPERTY.equals(property)) {
                    return false;
                }
                return true;
            }

            public Object getValue(Object element, String property) {
                Parameter parameter = (Parameter) element;
                if (COMMENT_PROPERTY.equals(property)) {
                    return parameter.getComment();
                } else if (VALUE_PROPERTY.equals(property)) {
                    return parameter.getValue();
                } else {
                    return null;
                }
            }

            public void modify(Object element, String property, Object value) {
                TableItem tableItem = (TableItem) element;
                Parameter param = (Parameter) tableItem.getData();
                if (COMMENT_PROPERTY.equals(property)) {
                    param.setComment(value.toString());
                } else if (VALUE_PROPERTY.equals(property)) {
                    param.setValue(value.toString());
                    rowGenTableEditor2.getTableViewerCreator().getTableViewer().refresh();
                }
                viewer.refresh(param);
            }
        });

        // viewer.setCellEditors(new CellEditor[] { new TextCellEditor(table), new TextCellEditor(table),
        // new TextCellEditor(table) });

        viewer.setColumnProperties(new String[] { NAME_PROPERTY, VALUE_PROPERTY, COMMENT_PROPERTY });
        CellEditor valueCellEditor = new TextCellEditor(table);
        if (function != null) {
            for (Parameter para : (List<Parameter>) function.getParameters()) {
                if (para instanceof ListParameter) {
                    valueCellEditor = new ComboBoxCellEditor(table, ((ListParameter) para).getValues());
                }
            }
        }
        viewer.setCellEditors(new CellEditor[] { new TextCellEditor(table), valueCellEditor, new TextCellEditor(table) });

    }

    /**
     * qzhang Comment method "attachLabelProvider".
     * 
     * @param viewer2
     */
    private void attachLabelProvider() {
        viewer.setLabelProvider(new ITableLabelProvider() {

            public Image getColumnImage(Object element, int columnIndex) {
                return null;
            }

            public String getColumnText(Object element, int columnIndex) {
                Parameter parameter = (Parameter) element;
                switch (columnIndex) {
                case 0:
                    return parameter.getName();
                case 1:
                    return parameter.getValue();
                case 2:
                    return parameter.getComment();
                default:
                    return null;
                }

            }

            public void addListener(ILabelProviderListener listener) {
            }

            public void dispose() {
            }

            public boolean isLabelProperty(Object element, String property) {
                return false;
            }

            public void removeListener(ILabelProviderListener lpl) {
            }
        });
    }

    /**
     * qzhang Comment method "attachContentProvider".
     * 
     * @param viewer2
     */
    private void attachContentProvider() {
        viewer.setContentProvider(new IStructuredContentProvider() {

            public Object[] getElements(Object inputElement) {
                return (Object[]) inputElement;
            }

            public void dispose() {

            }

            public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
            }
        });
    }

    /**
     * qzhang Comment method "buildAndLayoutTable".
     * 
     * @param table2
     * @return
     */
    private TableViewer buildAndLayoutTable(Table table2) {
        TableViewer tableViewer = new TableViewer(table);

        GridData gridData = new GridData(GridData.FILL_BOTH);
        table.setLayoutData(gridData);

        createColumns();
        return tableViewer;
    }

    protected void createColumns() {
        TableColumn column = new TableColumn(table, SWT.NONE);
        column.setWidth(100);
        column.setResizable(true);
        column.setText(Messages.getString("FunParaTableView.Parameter.TitleText")); //$NON-NLS-1$
        column = new TableColumn(table, SWT.NONE);
        column.setWidth(100);
        column.setText(Messages.getString("FunParaTableView.Value.TitleText")); //$NON-NLS-1$
        column = new TableColumn(table, SWT.NONE);
        column.setText(Messages.getString("FunParaTableView.Comment.TitleText")); //$NON-NLS-1$
        column.setWidth(200);

        table.setHeaderVisible(true);
        table.setLinesVisible(true);
    }

    // private ComboBoxCellEditor valueCombox;

    @SuppressWarnings("unchecked")//$NON-NLS-1$
    private void update() {
        if (function!= null)
        setTitle(function.getDescription());
        // updateData(function.getParameters());
    }

    private void updateData(List<Parameter> params) {
        Parameter[] newParam = new Parameter[params.size()];
        for (int i = 0; i < newParam.length; i++) {
            newParam[i] = params.get(i);
        }

        // for (Parameter parameter : newParam) {
        // if (parameter.getValue().indexOf(",") != -1) {
        // String[] items = parameter.getValue().split(",");
        // valueCombox = new ComboBoxCellEditor(table, items);
        // viewer.setCellEditors(new CellEditor[] { new TextCellEditor(table), valueCombox,
        // new TextCellEditor(table) });
        // }
        // }
        viewer.setInput(newParam);
        while (table.getItemCount() > params.size()) {
            table.getItem(table.getItemCount() - 1).dispose();
        }
        viewer.refresh(true);
        viewer.refresh(newParam);
        viewer.refresh();
    }

    /**
     * qzhang Comment method "setTitle".
     * 
     * @param string
     */
    public void setTitle(String string) {
        this.titleLabel.setText(string);
    }

    public Table getTable() {
        return this.table;
    }

    public TableViewer getViewer() {
        return this.viewer;
    }

}
