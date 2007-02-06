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

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.talend.commons.ui.swt.advanced.dataeditor.AbstractDataTableEditorView;
import org.talend.commons.ui.swt.advanced.dataeditor.ExtendedToolbarView;
import org.talend.commons.ui.swt.tableviewer.TableViewerCreator;
import org.talend.commons.ui.swt.tableviewer.TableViewerCreatorColumn;
import org.talend.commons.utils.data.bean.IBeanPropertyAccessors;
import org.talend.designer.rowgenerator.data.Function;
import org.talend.designer.rowgenerator.data.ListParameter;
import org.talend.designer.rowgenerator.data.Parameter;
import org.talend.designer.rowgenerator.i18n.Messages;
import org.talend.designer.rowgenerator.ui.editor.RowGenTableEditor2;

/**
 * qzhang class global comment. Detailled comment <br/>
 * 
 * $Id: talend-code-templates.xml 1 2007-2-4 下午12:09:57 (星期五, 29 九月 2006) qzhang $
 * 
 */
public class FunParaTableView2 extends AbstractDataTableEditorView<Parameter> {

    private static final String VALUE_PROPERTY = "Value"; //$NON-NLS-1$

    private RowGenTableEditor2 rowGenTableEditor2;

    public FunParaTableView2(Composite parentComposite, int mainCompositeStyle) {
        super(parentComposite, mainCompositeStyle);
    }

    public FunParaTableView2(Composite inEditorContainer, int border, RowGenTableEditor2 genTableEditor2) {
        this(inEditorContainer, border);
        this.rowGenTableEditor2 = genTableEditor2;
    }

    @Override
    protected void createColumns(TableViewerCreator<Parameter> tableViewerCreator, Table table) {
        TableViewerCreatorColumn column;
        column = new TableViewerCreatorColumn(tableViewerCreator);
        column.setTitle(Messages.getString("FunParaTableView2.Parameter"));
        column.setBeanPropertyAccessors(new IBeanPropertyAccessors<Parameter, Object>() {

            public String get(Parameter bean) {
                return bean.getName();
            }

            public void set(Parameter bean, Object value) {
            }

        });
        column.setModifiable(false);
        column.setWidth(55);
        column.setCellEditor(new TextCellEditor(tableViewerCreator.getTable()));
        // ////////////////////////////////////////////////////////
        column = new TableViewerCreatorColumn(tableViewerCreator);
        column.setTitle(Messages.getString("FunParaTableView2.Value"));
        column.setId(VALUE_PROPERTY);
        column.setBeanPropertyAccessors(new IBeanPropertyAccessors<Parameter, Object>() {

            public String get(Parameter bean) {
                return bean.getValue();
            }

            public void set(Parameter bean, Object value) {
                if (value == null) {
                    return;
                }
                String newValue = value.toString();
                bean.setValue(newValue);
                rowGenTableEditor2.getTableViewerCreator().getTableViewer().refresh();
            }

        });
        column.setModifiable(true);
        column.setWidth(115);
        column.setCellEditor(new TextCellEditor(tableViewerCreator.getTable()));
        // ////////////////////////////////////////////////////////

        column = new TableViewerCreatorColumn(tableViewerCreator);
        column.setTitle(Messages.getString("FunParaTableView2.Comment"));
        column.setBeanPropertyAccessors(new IBeanPropertyAccessors<Parameter, Object>() {

            public String get(Parameter bean) {
                return bean.getComment();
            }

            public void set(Parameter bean, Object value) {
            }

        });
        column.setModifiable(false);
        column.setWidth(155);
        column.setCellEditor(new TextCellEditor(tableViewerCreator.getTable()));
        // ////////////////////////////////////////////////////////

    }

    @Override
    protected ExtendedToolbarView initToolBar() {
        return null;
    }

    @SuppressWarnings("unchecked")//$NON-NLS-1$
    public void update(Function function) {
        setTitle(function.getDescription());
        updateData(function.getParameters());
    }

    private void updateData(List<Parameter> params) {
        
        final Table table = this.getTable();
        final TableViewer viewer = this.getTableViewerCreator().getTableViewer();

        final TableEditor editor = new TableEditor(table);
        // The editor must have the same size as the cell and must
        // editing the Third column
        final int eDITABLECOLUMN = 2;
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
            }

            private void createCombo(final ListParameter list, TableItem item) {
                final Combo combo = new Combo(table, SWT.PUSH);

                combo.setItems(list.getValues());

                combo.setFocus();
                editor.setEditor(combo, item, eDITABLECOLUMN);
                combo.setText(list.getValue());
                Point size = combo.computeSize(SWT.DEFAULT, table.getItemHeight());
                // Set attributes of the editor
                editor.horizontalAlignment = SWT.LEFT;
                editor.minimumHeight = size.y;
                editor.minimumWidth = size.x;
                combo.addSelectionListener(new SelectionAdapter() {

                    public void widgetSelected(SelectionEvent e) {
                        list.setValue(combo.getText());
                        viewer.refresh(list);
                        rowGenTableEditor2.getTableViewerCreator().getTableViewer().refresh();
                    }

                });

            }

        });
        this.getTableViewerCreator().setInputList(params);
    }
}
