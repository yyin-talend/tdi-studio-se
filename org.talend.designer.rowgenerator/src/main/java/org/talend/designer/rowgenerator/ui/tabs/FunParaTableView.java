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
import org.talend.designer.rowgenerator.ui.editor.RowGenTableEditor2;

/**
 *  qzhang class global comment. Detailled comment <br/>
 * 
 * $Id: FunParaTableView.java,v 1.13 2007/01/31 05:20:52 pub Exp $
 * 
 */
public class FunParaTableView {

    private static final String NAME_PROPERTY = "Parameter";

    private static final String VALUE_PROPERTY = "Value";

    private static final String COMMENT_PROPERTY = "Comment";

    private Table table;

    private Composite composite;

    private Label titleLabel;

    private TableViewer viewer;

    private RowGenTableEditor2 rowGenTableEditor2;
    /**
     *  qzhang FunParaTableView constructor comment.
     * 
     * @param parentComposite
     * @param mainCompositeStyle
     */
    public FunParaTableView(Composite parentComposite, int mainCompositeStyle, RowGenTableEditor2 genTableEditor2) {
        this.composite = new Composite(parentComposite, mainCompositeStyle);
        this.rowGenTableEditor2 = genTableEditor2;
        GridLayout gridLayout = new GridLayout(1, true);
        composite.setLayout(gridLayout);
        GridData gridData = new GridData(GridData.FILL_BOTH);
        composite.setLayoutData(gridData);
        newTitle();
        newTable();

    }

    /**
     *  qzhang Comment method "newTitle".
     */
    private void newTitle() {
        titleLabel = new Label(composite, SWT.NONE);
        titleLabel.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        titleLabel.setVisible(true);
    }

    /**
     *  qzhang Comment method "newTable".
     */
    private void newTable() {
        table = new Table(composite, SWT.BORDER | SWT.MULTI | SWT.FULL_SELECTION);
        viewer = buildAndLayoutTable(table);
        attachContentProvider();
        attachLabelProvider();
        attachCellEditors();
    }

    /**
     *  qzhang Comment method "attachCellEditors".
     * 
     * @param viewer2
     * @param table2
     */
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

        final TableEditor editor = new TableEditor(table);
        // The editor must have the same size as the cell and must
        // not be any smaller than 50 pixels.
        editor.horizontalAlignment = SWT.LEFT;
        editor.grabHorizontal = true;
        editor.minimumWidth = 50;
        // editing the second column
        final int eDITABLECOLUMN = 1;
        table.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                // Clean up any previous editor control
                Control oldEditor = editor.getEditor();
                if (oldEditor != null) {
                    oldEditor.dispose();
                }
                // Identify the selected row
                TableItem item = (TableItem) e.item;
                if (item == null) {
                    return;
                }
                Parameter param = (Parameter) item.getData();

                if (param instanceof ListParameter) {
                    createCombo((ListParameter) param, item);
                }
                viewer.refresh(param);
            }

            private void createCombo(final ListParameter list, TableItem item) {
                // The control that will be the editor must be a child of the
                // Table
                final Combo combo = new Combo(table, SWT.PUSH);
                combo.setItems(list.getValues());

                combo.setFocus();
                editor.setEditor(combo, item, eDITABLECOLUMN);
                combo.setText(list.getValue());
                combo.addSelectionListener(new SelectionAdapter() {

                    public void widgetSelected(SelectionEvent e) {
                        list.setValue(combo.getText());
                        viewer.refresh(list);
                        // editor.getItem().setText(EDITABLECOLUMN, text.getText());
                    }

                });

            }

        });
    }

    /**
     *  qzhang Comment method "attachLabelProvider".
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
     *  qzhang Comment method "attachContentProvider".
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
        column.setText("Parameter");
        column = new TableColumn(table, SWT.NONE);
        column.setWidth(100);
        column.setText("Value");
        column = new TableColumn(table, SWT.NONE);
        column.setText("Comment");
        column.setWidth(200);

        table.setHeaderVisible(true);
        table.setLinesVisible(true);
    }

    // private ComboBoxCellEditor valueCombox;

    @SuppressWarnings("unchecked")
    public void update(Function function) {
        setTitle(function.getDescription());
        updateData(function.getParameters());
    }

    public void updateData(List<Parameter> params) {
        Parameter[] newParam = new Parameter[params.size()];
        for (int i = 0; i < newParam.length; i++) {
            newParam[i] = params.get(i);
        }
        viewer
                .setCellEditors(new CellEditor[] { new TextCellEditor(table), new TextCellEditor(table),
                        new TextCellEditor(table) });
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
