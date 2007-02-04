package org.talend.designer.rowgenerator.ui.tabs;

import java.util.List;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.talend.commons.ui.swt.advanced.dataeditor.AbstractDataTableEditorView;
import org.talend.commons.ui.swt.advanced.dataeditor.ExtendedToolbarView;
import org.talend.commons.ui.swt.tableviewer.TableViewerCreator;
import org.talend.commons.ui.swt.tableviewer.TableViewerCreatorColumn;
import org.talend.commons.utils.data.bean.IBeanPropertyAccessors;
import org.talend.designer.rowgenerator.data.Function;
import org.talend.designer.rowgenerator.data.ListParameter;
import org.talend.designer.rowgenerator.data.Parameter;
import org.talend.designer.rowgenerator.ui.editor.RowGenTableEditor2;

public class FunParaTableView2 extends AbstractDataTableEditorView<Parameter> {

	private static final String NAME_PROPERTY = "Parameter";

	private static final String VALUE_PROPERTY = "Value";

	private static final String COMMENT_PROPERTY = "Comment";

	private RowGenTableEditor2 rowGenTableEditor2;

	public FunParaTableView2(Composite parentComposite, int mainCompositeStyle) {
		super(parentComposite, mainCompositeStyle);
	}

	public FunParaTableView2(Composite inEditorContainer, int border,
			RowGenTableEditor2 genTableEditor2) {
		this(inEditorContainer, border);
		this.rowGenTableEditor2 = genTableEditor2;
	}

	@Override
	protected void createColumns(
			TableViewerCreator<Parameter> tableViewerCreator, Table table) {
		TableViewerCreatorColumn column;
		column = new TableViewerCreatorColumn(tableViewerCreator);
		column.setTitle(NAME_PROPERTY);
		column
				.setBeanPropertyAccessors(new IBeanPropertyAccessors<Parameter, Object>() {

					public String get(Parameter bean) {
						return bean.getName();
					}

					public void set(Parameter bean, Object value) {
					}

				});
		column.setModifiable(false);
		column.setWidth(55);
		column.setMinimumWidth(45);
		column.setCellEditor(new TextCellEditor(tableViewerCreator.getTable()));
		// ////////////////////////////////////////////////////////
		column = new TableViewerCreatorColumn(tableViewerCreator);
		column.setTitle(VALUE_PROPERTY);
		column
				.setBeanPropertyAccessors(new IBeanPropertyAccessors<Parameter, Object>() {

					public String get(Parameter bean) {
						return bean.getValue();
					}

					public void set(Parameter bean, Object value) {
						if (value == null) {
							return;
						}
						String newValue = value.toString();
						bean.setValue(newValue);
						if (bean instanceof ListParameter) {

						}
						rowGenTableEditor2.getTableViewerCreator()
								.getTableViewer().refresh();
					}

				});
		column.setModifiable(true);
		column.setWidth(55);
		column.setCellEditor(new TextCellEditor(tableViewerCreator.getTable()));
		// ////////////////////////////////////////////////////////

		column = new TableViewerCreatorColumn(tableViewerCreator);
		column.setTitle(COMMENT_PROPERTY);
		column
				.setBeanPropertyAccessors(new IBeanPropertyAccessors<Parameter, Object>() {

					public String get(Parameter bean) {
						return bean.getComment();
					}

					public void set(Parameter bean, Object value) {
					}

				});
		column.setModifiable(false);
		column.setWidth(55);
		column.setCellEditor(new TextCellEditor(tableViewerCreator.getTable()));
		// ////////////////////////////////////////////////////////

	}

	@Override
	protected ExtendedToolbarView initToolBar() {
		return null;
	}

	@SuppressWarnings("unchecked")
	public void update(Function function) {
		setTitle(function.getDescription());
		updateData(function.getParameters());
	}

	private void updateData(List<Parameter> params) {
		Table table = this.getTable();
		TableViewer viewer = this.getTableViewerCreator().getTableViewer();
		
//		table.addSelectionListener(new SelectionAdapter() {
//
//            public void widgetSelected(SelectionEvent e) {
//                // Clean up any previous editor control
//                Control oldEditor = editor.getEditor();
//                if (oldEditor != null) {
//                    oldEditor.dispose();
//                }
//                // Identify the selected row
//                TableItem item = (TableItem) e.item;
//                if (item == null) {
//                    return;
//                }
//                Parameter param = (Parameter) item.getData();
//
//                if (param instanceof ListParameter) {
//                    createCombo((ListParameter) param, item);
//                }
//                viewer.refresh(param);
//            }
//
//            private void createCombo(final ListParameter list, TableItem item) {
//                // The control that will be the editor must be a child of the
//                // Table
//                final Combo combo = new Combo(table, SWT.PUSH);
//                combo.setItems(list.getValues());
//
//                combo.setFocus();
//                editor.setEditor(combo, item, eDITABLECOLUMN);
//                combo.setText(list.getValue());
//                combo.addSelectionListener(new SelectionAdapter() {
//
//                    public void widgetSelected(SelectionEvent e) {
//                        list.setValue(combo.getText());
//                        viewer.refresh(list);
//                    }
//
//                });
//
//            }
//
//        });
//		
		while (table.getItemCount() > params.size()) {
			table.getItem(table.getItemCount() - 1).dispose();
		}
		// resize all the columns but not the table
		for (int i = 0; i < table.getColumnCount(); i++) {
			table.getColumn(i).pack();
		}
		this.getTableViewerCreator().setInputList(params);
		viewer.refresh(params);

	}
}
