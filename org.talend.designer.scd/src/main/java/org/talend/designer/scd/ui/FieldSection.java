// ============================================================================
//
// Copyright (C) 2006-2007 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.designer.scd.ui;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.talend.designer.scd.util.DragDropManager;

/**
 * DOC hcw class global comment. Detailled comment
 */
public class FieldSection extends ScdSection implements IDragDropDelegate {

    private TableViewer tableViewer;

    private DragDropManager dragDropManager;

    private List tableModel;

    /**
     * DOC hcw FieldSection constructor comment.
     * 
     * @param parent
     */
    public FieldSection(Composite parent, int width, int height) {
        super(parent, width, height);
        dragDropManager = new DragDropManager();
        dragDropManager.addDragSupport(tableViewer.getTable(), this);
        dragDropManager.addDropSupport(tableViewer.getTable(), this);
        tableModel = new ArrayList();
    }

    @Override
    protected void createContents(Composite composite) {
        tableViewer = new TableViewer(composite, SWT.FULL_SELECTION | SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
        Table table = tableViewer.getTable();
        table.setLinesVisible(true);
        table.setHeaderVisible(false);

        GridData gd = new GridData(SWT.FILL, SWT.FILL, true, true);
        table.setLayoutData(gd);

        TableViewerColumn column = new TableViewerColumn(tableViewer, SWT.NONE);
        column.getColumn().setWidth(width);

        tableViewer.setLabelProvider(new TableLabelProvider());
        tableViewer.setContentProvider(new ContentProvider());
        // sorter
        tableViewer.setSorter(new ViewerSorter());

    }

    public void setTableInput(List input) {
        if (input == null) {
            return;
        }
        tableModel = input;
        tableViewer.setInput(input);
        tableViewer.refresh();
    }

    public TableViewer getTableViewer() {
        return tableViewer;
    }

    static class TableLabelProvider extends LabelProvider implements ITableLabelProvider {

        public String getColumnText(Object element, int columnIndex) {
            return element.toString();
        }

        public Image getColumnImage(Object element, int columnIndex) {
            return null;
        }
    }

    static class ContentProvider implements IStructuredContentProvider {

        public Object[] getElements(Object inputElement) {
            return ((List) inputElement).toArray();
        }

        public void dispose() {
        }

        public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
        }
    }

    public List<String> getTableData() {
        Table table = tableViewer.getTable();
        List<String> result = new ArrayList<String>(table.getItemCount());
        for (TableItem item : table.getItems()) {
            result.add(item.getText());
        }
        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.scd.ui.IDragDrop#getDragItemsAsText()
     */
    public String getDragItemsAsText() {
        Table table = tableViewer.getTable();
        TableItem[] selection = table.getSelection();

        StringBuffer buf = new StringBuffer();
        // number of selected elements
        buf.append(selection.length);
        for (int i = 0, n = selection.length; i < n; i++) {
            buf.append('|');
            buf.append(selection[i].getText());
        }

        return buf.toString();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.scd.ui.IDragDrop#onDropItems(java.lang.String, org.eclipse.swt.graphics.Point)
     */
    public void onDropItems(String data, Point position) {
        Table table = tableViewer.getTable();
        String[] items = data.split("\\|");
        // skip items[0], which is the number of selected elements
        table.setRedraw(false);
        for (int i = 1; i < items.length; i++) {
            TableItem item = new TableItem(table, SWT.NONE);
            item.setText(new String[] { items[i] });
            tableModel.add(items[i]);
        }

        table.setRedraw(true);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.scd.ui.IDragDrop#removeDragItems()
     */
    public void removeDragItems() {
        Table table = tableViewer.getTable();
        for (TableItem item : table.getSelection()) {
            tableModel.remove(item.getData());
        }
        table.remove(table.getSelectionIndices());
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.scd.ui.IDragDropDelegate#isDropAllowed(java.lang.String)
     */
    public boolean isDropAllowed(String data) {
        return true;
    }
}
