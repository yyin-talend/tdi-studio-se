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

import java.util.HashMap;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.talend.designer.scd.util.DragDropManager;
import org.talend.designer.scd.util.IPropertySetter;
import org.talend.designer.scd.util.TableEditorManager;

/**
 * DOC hcw class global comment. Detailled comment
 */
public class Type3Section extends ScdSection implements IDragDropDelegate {

    private static final String PREVIOUS_HEADER = "previous value";

    private static final String CURRENT_HEADER = "current value";

    private static final int PREVIOUS_COLUMN_INDEX = 1;

    private static final String PREVIOUS_NAME_PREFIX = "previous_";

    private Table table;

    private TableEditorManager editorManager;

    private DragDropManager dragDropManager;

    private Map<String, String> tableModel;

    /**
     * DOC hcw Type3Section constructor comment.
     * 
     * @param parent
     * @param width
     * @param height
     */
    public Type3Section(Composite parent, int width, int height) {
        super(parent, width, height);
        editorManager = new TableEditorManager();
        dragDropManager = new DragDropManager();
        dragDropManager.addDragSupport(table, this);
        dragDropManager.addDropSupport(table, this);
        tableModel = new HashMap<String, String>();
    }

    @Override
    protected void createContents(Composite composite) {
        // createLabelTitle(composite, new String[] { "current value", "previous value" }, new int[] { 100, 100 },
        // SWTResourceManager.getColor(IColorConstants.GRAY));
        table = new Table(composite, SWT.BORDER);

        GridData gd = new GridData(SWT.FILL, SWT.FILL, true, true);
        table.setLayoutData(gd);
        table.setHeaderVisible(true);
        table.setLinesVisible(true);

        TableColumn currentColumn = new TableColumn(table, SWT.NONE);
        currentColumn.setWidth(200);
        currentColumn.setText(CURRENT_HEADER);

        TableColumn previousColumn = new TableColumn(table, SWT.NONE);
        previousColumn.setWidth(200);
        previousColumn.setText(PREVIOUS_HEADER);

    }

    // protected Label[] createLabelTitle(Composite parent, String[] text, int[] width, Color background) {
    // Composite composite = new Composite(parent, SWT.NONE);
    // GridDataFactory.swtDefaults().align(SWT.FILL, SWT.CENTER).applyTo(composite);
    // GridLayoutFactory.swtDefaults().numColumns(text.length).margins(0, 0).spacing(2, 0).applyTo(composite);
    //
    // Label[] headers = new Label[text.length];
    // for (int i = 0; i < text.length; i++) {
    // headers[i] = new Label(composite, SWT.NONE);
    // headers[i].setBackground(background);
    // headers[i].setText(text[i]);
    // GridDataFactory.swtDefaults().hint(width[i], SWT.DEFAULT).applyTo(headers[i]);
    // }
    // return headers;
    //
    // }

    public Table getTable() {
        return table;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.scd.ui.IDragDropDelegate#getDragItemsAsText()
     */
    public String getDragItemsAsText() {
        TableItem[] selection = table.getSelection();
        StringBuffer buf = new StringBuffer();
        // number of selected elements
        buf.append(selection.length);
        for (int i = 0, n = selection.length; i < n; i++) {
            buf.append('|');
            buf.append(selection[i].getText(0));
        }

        return buf.toString();

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.scd.ui.IDragDropDelegate#onDropItems(java.lang.String, org.eclipse.swt.graphics.Point)
     */
    public void onDropItems(String data, Point position) {
        table.setRedraw(false);
        String[] items = data.split("\\|");
        // skip items[0], which is the number of selected elements
        for (int i = 1; i < items.length; i++) {
            createNewItem(items[i], PREVIOUS_NAME_PREFIX + items[i]);
        }

        table.setRedraw(true);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.scd.ui.IDragDropDelegate#isDropAllowed(java.lang.String)
     */
    public boolean isDropAllowed(String data) {
        return true;
    }

    public void setTableInput(Map<String, String> tableModel) {
        this.tableModel = new HashMap<String, String>();
        if (tableModel != null) {
            table.setRedraw(false);
            for (String current : tableModel.keySet()) {
                String previous = tableModel.get(current);
                createNewItem(current, previous);
            }
            table.setRedraw(true);
        }
    }

    public Map<String, String> getTableData() {
        return tableModel;
    }

    public void createNewItem(final String currentName, String previousName) {
        tableModel.put(currentName, previousName);
        TableItem item = new TableItem(table, SWT.NONE);
        item.setText(new String[] { currentName, previousName });
        item.setData(currentName);
        editorManager.createTextEditor(table, previousName, item, PREVIOUS_COLUMN_INDEX, new IPropertySetter<String>() {

            public void set(String value) {
                tableModel.put(currentName, value);
            }
        });
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.scd.ui.IDragDropDelegate#removeDragItems()
     */
    public void removeDragItems() {
        TableItem[] selection = table.getSelection();
        for (TableItem item : selection) {
            String currentName = (String) item.getData();
            tableModel.remove(currentName);
            editorManager.removeEditors(item);
        }
        table.remove(table.getSelectionIndices());
    }
}
