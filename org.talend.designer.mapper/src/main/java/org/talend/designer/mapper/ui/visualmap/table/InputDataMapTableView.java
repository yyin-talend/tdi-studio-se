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

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.ToolItem;
import org.talend.commons.ui.swt.tableviewer.TableViewerCreator;
import org.talend.commons.ui.swt.tableviewer.TableViewerCreatorColumn;
import org.talend.commons.ui.ws.WindowSystem;
import org.talend.commons.utils.data.bean.IBeanPropertyAccessors;
import org.talend.designer.mapper.managers.MapperManager;
import org.talend.designer.mapper.model.table.InputTable;
import org.talend.designer.mapper.model.tableentry.InputColumnTableEntry;
import org.talend.designer.mapper.ui.image.ImageInfo;
import org.talend.designer.mapper.ui.image.ImageProviderMapper;
import org.talend.designer.mapper.ui.visualmap.zone.Zone;

/**
 * DOC amaumont class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 */
public class InputDataMapTableView extends DataMapTableView {

    public InputDataMapTableView(Composite parent, int style, InputTable inputTable, MapperManager mapperManager) {
        super(parent, style, inputTable, mapperManager);
    }

    @Override
    public void initColumnsOfTableColumns(TableViewerCreator tableViewerCreatorForColumns) {
        boolean isMainConnection = ((InputTable) getDataMapTable()).isMainConnection();
        TableViewerCreatorColumn column = null;
        if (!isMainConnection) {

            column = new TableViewerCreatorColumn(tableViewerCreatorForColumns);
            column.setTitle("Expr. key");
            column.setId(DataMapTableView.ID_EXPRESSION_COLUMN);
            column.setBeanPropertyAccessors(new IBeanPropertyAccessors<InputColumnTableEntry, String>() {

                public String get(InputColumnTableEntry bean) {
                    return bean.getExpression();
                }

                public void set(InputColumnTableEntry bean, String value) {
                    bean.setExpression(value);
                }

            });
            column.setModifiable(true);
            column.setDefaultInternalValue("");
            createExpressionCellEditor(tableViewerCreatorForColumns, column, new Zone[] { Zone.INPUTS }, false);
            column.setWeight(COLUMN_EXPRESSION_SIZE_WEIGHT);
        }

        column = new TableViewerCreatorColumn(tableViewerCreatorForColumns);
        column.setTitle(DataMapTableView.COLUMN_NAME);
        column.setId(DataMapTableView.ID_NAME_COLUMN);
        column.setBeanPropertyAccessors(new IBeanPropertyAccessors<InputColumnTableEntry, String>() {

            public String get(InputColumnTableEntry bean) {
                return bean.getMetadataColumn().getLabel();
            }

            public void set(InputColumnTableEntry bean, String value) {
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
        // nothing
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.mapper.ui.visualmap.table.DataMapTableView#addEntriesActionsComponents()
     */
    @Override
    protected boolean addToolItems() {
        if (!getInputTable().isMainConnection()) {

            final ToolItem rejectConstraintCheck = new ToolItem(toolBarActions, SWT.CHECK);
            rejectConstraintCheck.setToolTipText("Reject main row if this lookup row doesn't exist");
            boolean isInnerJoin = getInputTable().isInnerJoin();
            // Image image = ImageProviderMapper.getImage(isInnerJoin ? ImageInfo.CHECKED_ICON :
            // ImageInfo.UNCHECKED_ICON);
            Image image = ImageProviderMapper.getImage(isInnerJoin ? ImageInfo.CHECKED_ICON : ImageInfo.UNCHECKED_ICON);
            if (WindowSystem.isGTK()) {
                rejectConstraintCheck.setImage(image);
                rejectConstraintCheck.setHotImage(image);
            } else {
                rejectConstraintCheck.setImage(ImageProviderMapper.getImage(ImageInfo.UNCHECKED_ICON));
                rejectConstraintCheck.setHotImage(image);
            }
            rejectConstraintCheck.setSelection(isInnerJoin);
            rejectConstraintCheck.setText("Inner join");

            rejectConstraintCheck.addSelectionListener(new SelectionListener() {

                public void widgetDefaultSelected(SelectionEvent e) {
                }

                public void widgetSelected(SelectionEvent e) {
                    Image image = null;
                    if (rejectConstraintCheck.getSelection()) {
                        getInputTable().setInnerJoin(true);
                        image = ImageProviderMapper.getImage(ImageInfo.CHECKED_ICON);
                    } else {
                        getInputTable().setInnerJoin(false);
                        image = ImageProviderMapper.getImage(ImageInfo.UNCHECKED_ICON);
                    }
                    if (WindowSystem.isGTK()) {
                        rejectConstraintCheck.setImage(image);
                        rejectConstraintCheck.setHotImage(image);
                    } else {
                        rejectConstraintCheck.setHotImage(image);
                    }
                }

            });
            return true;
        }
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.mapper.ui.visualmap.table.DataMapTableView#getZone()
     */
    @Override
    public Zone getZone() {
        return Zone.INPUTS;
    }

    public InputTable getInputTable() {
        return (InputTable) abstractDataMapTable;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.mapper.ui.visualmap.table.DataMapTableView#toolbarNeededToBeRightStyle()
     */
    @Override
    public boolean toolbarNeededToBeRightStyle() {
        return !getInputTable().isMainConnection();
    }

}
