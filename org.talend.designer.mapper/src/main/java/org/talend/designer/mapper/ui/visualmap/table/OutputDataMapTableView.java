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

import org.eclipse.swt.widgets.Composite;
import org.talend.commons.ui.swt.tableviewer.TableViewerCreatorColumn;
import org.talend.commons.utils.data.bean.IBeanPropertyAccessors;
import org.talend.designer.mapper.managers.MapperManager;
import org.talend.designer.mapper.model.table.AbstractDataMapTable;
import org.talend.designer.mapper.model.table.OutputTable;
import org.talend.designer.mapper.model.tableentry.ConstraintTableEntry;
import org.talend.designer.mapper.model.tableentry.OutputColumnTableEntry;
import org.talend.designer.mapper.ui.proposal.expression.ExpressionProposal;
import org.talend.designer.mapper.ui.visualmap.zone.Zone;

/**
 * DOC amaumont class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 */
public class OutputDataMapTableView extends DataMapTableView {

    protected ExpressionProposal proposal;

    public OutputDataMapTableView(Composite parent, int style, AbstractDataMapTable abstractDataMapTable, MapperManager mapperManager) {
        super(parent, style, abstractDataMapTable, mapperManager);
    }

    @Override
    public void initColumns() {
        TableViewerCreatorColumn column = new TableViewerCreatorColumn(tableViewerCreatorForColumns);
        column.setTitle("Expression");
        column.setId(DataMapTableView.ID_EXPRESSION_COLUMN);
        column.setBeanPropertyAccessors(new IBeanPropertyAccessors<OutputColumnTableEntry, String>() {

            public String get(OutputColumnTableEntry bean) {
                return bean.getExpression();
            }

            public void set(OutputColumnTableEntry bean, String value) {
                bean.setExpression(value);
                tableViewerCreatorForColumns.getTableViewer().refresh(bean);
            }

        });
        column.setModifiable(true);
        column.setDefaultInternalValue("");
        createExpressionCellEditor(tableViewerCreatorForColumns, column, new Zone[] { Zone.INPUTS, Zone.VARS }, false);
        column.setWeight(COLUMN_EXPRESSION_SIZE_WEIGHT);

        column = new TableViewerCreatorColumn(tableViewerCreatorForColumns);
        column.setTitle(DataMapTableView.COLUMN_NAME);
        column.setId(DataMapTableView.ID_NAME_COLUMN);
        column.setBeanPropertyAccessors(new IBeanPropertyAccessors<OutputColumnTableEntry, String>() {

            public String get(OutputColumnTableEntry bean) {
                return bean.getMetadataColumn().getLabel();
            }

            public void set(OutputColumnTableEntry bean, String value) {
                bean.getMetadataColumn().setLabel(value);
            }

        });
        column.setWeight(COLUMN_NAME_SIZE_WEIGHT);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.mapper.ui.visualmap.table.DataMapTableView#initTableConstraints()
     */
    @Override
    protected void initTableConstraints() {
        super.createTableConstraints();
        TableViewerCreatorColumn column = new TableViewerCreatorColumn(tableViewerCreatorForConstraints);
        column.setTitle("Constraint conditions (AND)");
        column.setId(DataMapTableView.ID_EXPRESSION_COLUMN);
        column.setBeanPropertyAccessors(new IBeanPropertyAccessors<ConstraintTableEntry, String>() {

            public String get(ConstraintTableEntry bean) {
                return bean.getExpression();
            }

            public void set(ConstraintTableEntry bean, String value) {
                bean.setExpression(value);
            }

        });
        column.setModifiable(true);
        column.setDefaultInternalValue("");
        createExpressionCellEditor(tableViewerCreatorForConstraints, column, new Zone[] { Zone.INPUTS, Zone.VARS }, true);
        // final TextCellEditor cellEditor = new TextCellEditor(tableViewerCreatorForConstraints.getTable());
        // cellEditor.addListener(new ICellEditorListener() {
        //
        // Text text = (Text) cellEditor.getControl();
        //
        // public void applyEditorValue() {
        // ModifiedObjectInfo modifiedObjectInfo = tableViewerCreatorForEntries.getModifiedObjectInfo();
        // // System.out.println("applyEditorValue -> text.getText()=" + text.getText()
        // // + " modifiedObjectInfo.getCurrentModifiedBean() = " + modifiedObjectInfo.getCurrentModifiedBean()
        // // + " modifiedObjectInfo.getPreviousModifiedBean() = " + modifiedObjectInfo.getPreviousModifiedBean()
        // // + " modifiedObjectInfo.getPreviousModifiedBean() = " + modifiedObjectInfo.getPreviousModifiedBean()
        // // + " modifiedObjectInfo.getPreviousPropertyBeanValue()=" +
        // // modifiedObjectInfo.getPreviousPropertyBeanValue());
        // mapperManager.getUiManager().processNewExpression(text.getText(), dataMapTableView,
        // (ConstraintTableEntry) modifiedObjectInfo.getCurrentModifiedBean());
        // }
        //
        // public void cancelEditor() {
        // ModifiedObjectInfo modifiedObjectInfo = tableViewerCreatorForEntries.getModifiedObjectInfo();
        // text.setText((String) modifiedObjectInfo.getOriginalPropertyBeanValue());
        // // System.out.println("cancelEditor -> text.getText()=" + text.getText() + "
        // // modifiedObjectInfo.getCurrentModifiedBean() = "
        // // + modifiedObjectInfo.getCurrentModifiedBean() + " modifiedObjectInfo.getPreviousModifiedBean() = " +
        // // modifiedObjectInfo.getPreviousModifiedBean()
        // // + " modifiedObjectInfo.getPreviousPropertyBeanValue()="
        // // + modifiedObjectInfo.getPreviousPropertyBeanValue());
        // mapperManager.getUiManager().processNewExpression(originalExpression, dataMapTableView,
        // (ConstraintTableEntry) modifiedObjectInfo.getPreviousModifiedBean());
        // }
        //
        // public void editorValueChanged(boolean oldValidState, boolean newValidState) {
        //
        // text.setFocus();
        // ModifiedObjectInfo modifiedObjectInfo = tableViewerCreatorForEntries.getModifiedObjectInfo();
        // // System.out.println("editorValueChanged -> text.getText()=" + text.getText()
        // // + " modifiedObjectInfo.getCurrentModifiedBean() = " + modifiedObjectInfo.getCurrentModifiedBean()
        // // + " modifiedObjectInfo.getPreviousModifiedBean() = " + modifiedObjectInfo.getPreviousModifiedBean()
        // // + " modifiedObjectInfo.getPreviousPropertyBeanValue()=" +
        // // modifiedObjectInfo.getPreviousPropertyBeanValue());
        // ITableEntry tableEntry = (ITableEntry) (modifiedObjectInfo.getCurrentModifiedBean() != null ?
        // modifiedObjectInfo
        // .getCurrentModifiedBean()
        // : modifiedObjectInfo.getPreviousModifiedBean());
        // mapperManager.getUiManager().processNewExpression(text.getText(), dataMapTableView, tableEntry);
        // tableEntry.setExpression(text.getText());
        // tableViewerCreatorForEntries.getTableViewer().refresh(tableEntry);
        // }
        //
        // });
        // cellEditor.getControl().addFocusListener(new FocusListener() {
        //
        // private ExpressionProposal proposal;
        //
        // public void focusGained(FocusEvent e) {
        // initExpressionProposal((Text) cellEditor.getControl(), new Zone[] { Zone.INPUTS, Zone.VARS });
        // }
        //
        // public void focusLost(FocusEvent e) {
        // // TODO Auto-generated method stub
        //
        // }
        //
        // });
        // column.setCellEditor(cellEditor);
        column.setWeight(100);

        tableViewerCreatorForConstraints.init(((OutputTable) getDataMapTable()).getConstraintEntries());
        updateGridDataHeightForTableConstraints();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.mapper.ui.visualmap.table.DataMapTableView#addConstraintsActionsButtons()
     */
    @Override
    protected void addConstraintsActionsComponents() {
        createConstraintActionButtons();

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.mapper.ui.visualmap.table.DataMapTableView#addEntriesActionsComponents()
     */
    @Override
    protected void addEntriesActionsComponents() {
        // TODO Auto-generated method stub

    }

    @Override
    protected boolean hasConstraintsActions() {
        return true;
    }

    @Override
    protected boolean hasEntriesActions() {
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.mapper.ui.visualmap.table.DataMapTableView#getZone()
     */
    @Override
    public Zone getZone() {
        return Zone.OUTPUTS;
    }

    @Override
    public void unselectAllConstraintEntries() {
        tableViewerCreatorForConstraints.getSelectionHelper().deselectAll();
    }

    
    
}
