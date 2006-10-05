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
package org.talend.designer.mapper.ui.visualmap.table;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ICellEditorListener;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.talend.commons.ui.swt.tableviewer.TableViewerCreatorColumn;
import org.talend.commons.ui.swt.tableviewer.data.ModifiedObjectInfo;
import org.talend.commons.utils.data.bean.IBeanPropertyAccessors;
import org.talend.commons.utils.threading.AsynchronousThreading;
import org.talend.designer.mapper.managers.MapperManager;
import org.talend.designer.mapper.model.table.AbstractDataMapTable;
import org.talend.designer.mapper.model.table.VarsTable;
import org.talend.designer.mapper.model.tableentry.ITableEntry;
import org.talend.designer.mapper.model.tableentry.VarTableEntry;
import org.talend.designer.mapper.ui.visualmap.zone.Zone;

/**
 * DOC amaumont class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 */
public class VarsDataMapTableView extends DataMapTableView {

    private Text expressionEditorText;

    public VarsDataMapTableView(Composite parent, int style, AbstractDataMapTable abstractDataMapTable, MapperManager mapperManager) {
        super(parent, style, abstractDataMapTable, mapperManager);
    }

    
    
    @Override
    protected void addListeners() {
        super.addListeners();
        tableViewerCreatorForColumns.getTable().addSelectionListener(new SelectionListener() {

            public void widgetDefaultSelected(SelectionEvent e) {}

            public void widgetSelected(SelectionEvent e) {
                removeEntryItem.setEnabled(tableViewerCreatorForColumns.getTable().getSelectionCount() > 0);
            }
            
        });
    }



    @Override
    public void initColumns() {
        TableViewerCreatorColumn column = new TableViewerCreatorColumn(tableViewerCreatorForColumns);
        column.setTitle("Expression");
        column.setId(DataMapTableView.ID_EXPRESSION_COLUMN);
        column.setBeanPropertyAccessors(new IBeanPropertyAccessors<VarTableEntry, String>() {

            public String get(VarTableEntry bean) {
                return bean.getExpression();
            }

            public void set(VarTableEntry bean, String value) {
                bean.setExpression(value);
            }

        });
        column.setModifiable(true);
        column.setDefaultInternalValue("");
        createExpressionCellEditor(tableViewerCreatorForColumns, column, new Zone[] { Zone.INPUTS, Zone.VARS }, false);
        column.setWeight(COLUMN_EXPRESSION_SIZE_WEIGHT);

        column = new TableViewerCreatorColumn(tableViewerCreatorForColumns);
        column.setTitle("Variable");
        column.setId(DataMapTableView.ID_NAME_COLUMN);
        column.setBeanPropertyAccessors(new IBeanPropertyAccessors<VarTableEntry, String>() {

            public String get(VarTableEntry bean) {
                return bean.getName();
            }

            public void set(VarTableEntry bean, String value) {
                bean.setName(value);
            }

        });
        column.setModifiable(true);
        column.setWeight(COLUMN_NAME_SIZE_WEIGHT);
        final CellEditor cellEditor = new TextCellEditor(tableViewerCreatorForColumns.getTable());
        final TableViewerCreatorColumn nameColumn = column;
        cellEditor.addListener(new ICellEditorListener() {

            Text text = (Text) cellEditor.getControl();

            String lastValidValue = null;

            public void applyEditorValue() {
                ModifiedObjectInfo<ITableEntry> modifiedObjectInfo = tableViewerCreatorForColumns.getModifiedObjectInfo();
                // System.out.println("------- applyEditorValue=" + text.getText());
                Object bean = modifiedObjectInfo.getCurrentModifiedBean() != null ? modifiedObjectInfo.getCurrentModifiedBean()
                        : modifiedObjectInfo.getPreviousModifiedBean();
                fireEventIfValidColumnName(text.getText(), true, bean);
                lastValidValue = null;
            }

            public void cancelEditor() {
                ModifiedObjectInfo<ITableEntry> modifiedObjectInfo = tableViewerCreatorForColumns.getModifiedObjectInfo();
                String originalName = (String) modifiedObjectInfo.getOriginalPropertyBeanValue();
                text.setText(originalName);
                fireEventIfValidColumnName(originalName, false, modifiedObjectInfo.getCurrentModifiedBean());
                lastValidValue = null;
            }

            public void editorValueChanged(boolean oldValidState, boolean newValidState) {
                ModifiedObjectInfo<ITableEntry> modifiedObjectInfo = tableViewerCreatorForColumns.getModifiedObjectInfo();
                if (!newValidState) {
                    // MessageDialog.openError(composite.getShell(), "Error", cellEditor.getErrorMessage());
                } else {
                }
                String newValue = text.getText();
                fireEventIfValidColumnName(newValue, false, modifiedObjectInfo.getCurrentModifiedBean());
            }

            private void fireEventIfValidColumnName(final String newValue, boolean showAlertIfError, final Object currentModifiedBean) {
                final ModifiedObjectInfo<ITableEntry> modifiedObjectInfo = tableViewerCreatorForColumns.getModifiedObjectInfo();
                String originalValue = (String) modifiedObjectInfo.getOriginalPropertyBeanValue();
                lastValidValue = lastValidValue != null ? lastValidValue : originalValue;

                int beanPosition = tableViewerCreatorForColumns.getInputList().indexOf(currentModifiedBean);
                final String errorMessage = ((VarsTable) getDataMapTable()).validateColumnName(newValue, beanPosition);
                // System.out.println(errorMessage);
                if (errorMessage == null) {
                    mapperManager.getUiManager().processColumnNameChanged(newValue, dataMapTableView, (ITableEntry) currentModifiedBean);
                    text.setBackground(text.getDisplay().getSystemColor(SWT.COLOR_WHITE));
                    lastValidValue = newValue;
                } else {
                    text.setBackground(text.getDisplay().getSystemColor(SWT.COLOR_RED));
                    if (showAlertIfError) {
                        final Point selection = text.getSelection();
                        text.setText(lastValidValue);

                        new AsynchronousThreading(50, true, text.getDisplay(), new Runnable() {
                            public void run() {

                                MessageDialog.openError(dataMapTableView.getShell(), "Error", errorMessage);
                                // System.out.println("setText:" + newValue);
                                final int columnPosition = tableViewerCreatorForColumns.getColumns().indexOf(nameColumn);
                                tableViewerCreatorForColumns.getTableViewer().editElement(currentModifiedBean, columnPosition);
                                text.setText(newValue);
                                text.setSelection(selection.x, selection.y);
                                
                            }
                        }).start();
                        
                    }
                }
            }

        });

        column.setCellEditor(cellEditor);

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.mapper.ui.visualmap.table.DataMapTableView#initTableConstraints()
     */
    @Override
    protected void initTableConstraints() {
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.mapper.ui.visualmap.table.DataMapTableView#addConstraintsActionsButtons()
     */
    @Override
    protected void addConstraintsActionsComponents() {
        // createConstraintActionButtons();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.mapper.ui.visualmap.table.DataMapTableView#addEntriesActionsComponents()
     */
    @Override
    protected void addEntriesActionsComponents() {
        super.createEntriesActionButtons();
    }

    @Override
    protected boolean hasConstraintsActions() {
        return false;
    }

    @Override
    protected boolean hasEntriesActions() {
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.mapper.ui.visualmap.table.DataMapTableView#getZone()
     */
    @Override
    public Zone getZone() {
        return Zone.VARS;
    }



    @Override
    public void unselectAllColumnEntries() {
        super.unselectAllColumnEntries();
        removeEntryItem.setEnabled(false);
    }

    
    
}
