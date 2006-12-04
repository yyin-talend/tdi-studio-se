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

import java.util.List;

import org.eclipse.swt.widgets.Composite;
import org.talend.commons.ui.swt.tableviewer.TableViewerCreator;
import org.talend.commons.ui.swt.tableviewer.TableViewerCreatorColumn;
import org.talend.commons.ui.swt.tableviewer.tableeditor.ButtonPushImageTableEditorContent;
import org.talend.commons.ui.ws.WindowSystem;
import org.talend.commons.utils.data.bean.IBeanPropertyAccessors;
import org.talend.core.ui.images.EImage;
import org.talend.core.ui.images.ImageProvider;
import org.talend.designer.mapper.managers.MapperManager;
import org.talend.designer.mapper.model.table.AbstractDataMapTable;
import org.talend.designer.mapper.model.table.OutputTable;
import org.talend.designer.mapper.model.tableentry.ConstraintTableEntry;
import org.talend.designer.mapper.model.tableentry.ITableEntry;
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
    public void initColumns(final TableViewerCreator tableViewerCreatorForColumns) {
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
    protected void initTableFilters() {
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
        column.setWeight(99);
        column.setMoveable(false);
        column.setResizable(false);

        // Column with remove button
        column = new TableViewerCreatorColumn(tableViewerCreatorForConstraints);
        column.setTitle("");
        column.setDefaultDisplayedValue("");
        column.setWidth(16);
        column.setMoveable(false);
        column.setResizable(false);
        ButtonPushImageTableEditorContent buttonImage = new ButtonPushImageTableEditorContent() {

            /* (non-Javadoc)
             * @see org.talend.commons.ui.swt.tableviewer.tableeditor.ButtonImageTableEditorContent#selectionEvent(org.talend.commons.ui.swt.tableviewer.TableViewerCreatorColumn, java.lang.Object)
             */
            @Override
            protected void selectionEvent(TableViewerCreatorColumn column, Object bean) {
                mapperManager.removeTableEntry((ITableEntry) bean);
                tableViewerCreatorForConstraints.getTableViewer().refresh();
                List list = tableViewerCreatorForConstraints.getInputList();
                updateGridDataHeightForTableConstraints();
                if (list != null && list.size() == 0) {
                    showTableConstraints(false);
                } else {
                    showTableConstraints(true);
                }
                
            }
            
        };
        buttonImage.setImage(ImageProvider.getImage(EImage.MINUS_ICON));
        column.setTableEditorContent(buttonImage);
        
        

        List<ConstraintTableEntry> entries = ((OutputTable) getDataMapTable()).getConstraintEntries();

        // correct partially layout problem with GTK when cell editor value is applied
        tableViewerCreatorForConstraints.setAdjustWidthValue(WindowSystem.isGTK() ? -20 : ADJUST_WIDTH_VALUE);

        tableViewerCreatorForConstraints.init(entries);
        updateGridDataHeightForTableConstraints();

        if (WindowSystem.isGTK()) {
            tableViewerCreatorForConstraints.layout();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.mapper.ui.visualmap.table.DataMapTableView#addEntriesActionsComponents()
     */
    @Override
    protected boolean addToolItems() {
        createFiltersToolItems();
//        addToolItemSeparator();
//        createToolItems();
        return true;
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

    /* (non-Javadoc)
     * @see org.talend.designer.mapper.ui.visualmap.table.DataMapTableView#toolbarNeededToBeRightStyle()
     */
    @Override
    public boolean toolbarNeededToBeRightStyle() {
        return false;
    }

}
