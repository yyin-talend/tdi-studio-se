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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.talend.core.ui.proposal.TalendProposalUtils;
import org.talend.designer.scd.ScdPlugin;
import org.talend.designer.scd.model.SurrogateCreationType;
import org.talend.designer.scd.model.SurrogateKey;
import org.talend.designer.scd.util.DragDropManager;
import org.talend.designer.scd.util.IPropertySetter;
import org.talend.designer.scd.util.SWTResourceManager;
import org.talend.designer.scd.util.SurrogateKeyManager;
import org.talend.designer.scd.util.TableEditorManager;

/**
 * DOC hcw class global comment. Detailled comment
 */
public class SurrogateSection extends ScdSection {

    private static final int START_INDEX = 0;

    /**
     * 
     */
    private static final int COMPLEMENT_INDEX = START_INDEX + 2;

    /**
     * 
     */
    private static final int CREATION_INDEX = START_INDEX + 1;

    /**
     * 
     */
    private static final int COLUMN_INDEX = START_INDEX;

    private static final Color ERROR_COLOR = SWTResourceManager.getColor(IColorConstants.RED);

    private Table table;

    private SurrogateKeyManager surrogateManager;

    private TableEditorManager editorManager;

    private List<SurrogateKey> tableModel = new ArrayList<SurrogateKey>();

    private DragDropManager dragDropManager;

    /**
     * DOC hcw SurrogateSection constructor comment.
     * 
     * @param parent
     * @param width
     * @param height
     */
    public SurrogateSection(Composite parent, int width, int height) {
        super(parent, width, height);
        surrogateManager = new SurrogateKeyManager();
        editorManager = new TableEditorManager();
        dragDropManager = new DragDropManager();
    }

    @Override
    protected void createContents(Composite composite) {
        table = new Table(composite, SWT.BORDER | SWT.MULTI | SWT.FULL_SELECTION);
        GridData gd = new GridData(SWT.FILL, SWT.FILL, true, true);
        table.setLayoutData(gd);
        table.setHeaderVisible(true);
        table.setLinesVisible(true);

        // TableColumn checkColumn = new TableColumn(table, SWT.NONE);
        // checkColumn.setWidth(15);

        TableColumn column = new TableColumn(table, SWT.NONE);
        column.setWidth(100);
        column.setText("column");

        TableColumn creation = new TableColumn(table, SWT.NONE);
        creation.setWidth(100);
        creation.setText("creation");

        TableColumn complement = new TableColumn(table, SWT.NONE);
        complement.setWidth(150);
        complement.setText("complement");

        TableColumn empty = new TableColumn(table, SWT.NONE);
        empty.setWidth(100);

        // createToolbar(composite);
    }

    /**
     * DOC hcw Comment method "createToolbar".
     * 
     * @param composite
     */
    private void createToolbar(Composite composite) {
        ToolBar toolBar = new ToolBar(composite, SWT.NONE);
        toolBar.setLayoutData(new GridData(157, SWT.DEFAULT));

        ToolItem addToolItem = new ToolItem(toolBar, SWT.PUSH);
        addToolItem.setImage(SWTResourceManager.getPluginImage(ScdPlugin.getDefault(), "icons/add.gif"));
        addToolItem.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                addButtonClick();
            }
        });

        ToolItem removeToolItem = new ToolItem(toolBar, SWT.PUSH);
        removeToolItem.setImage(SWTResourceManager.getPluginImage(ScdPlugin.getDefault(), "icons/delete.gif"));
        removeToolItem.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                removeButtonClick();
            }

        });

        ToolItem moveUpToolItem = new ToolItem(toolBar, SWT.PUSH);
        moveUpToolItem.setImage(SWTResourceManager.getPluginImage(ScdPlugin.getDefault(), "icons/up.gif"));
        moveUpToolItem.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                moveUpButtonClick();
            }

        });

        ToolItem moveDownToolItem = new ToolItem(toolBar, SWT.PUSH);
        moveDownToolItem.setImage(SWTResourceManager.getPluginImage(ScdPlugin.getDefault(), "icons/down.gif"));
        moveDownToolItem.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                moveDownButtonClick();
            }

        });

    }

    public Table getTable() {
        return table;
    }

    /**
     * DOC hcw Comment method "moveDownButtonClick".
     */
    protected void moveDownButtonClick() {
        table.setRedraw(false);
        int[] updateIndices = adjustIndicesDown(table.getSelectionIndices(), tableModel);

        // remove editors
        for (int i : updateIndices) {
            editorManager.removeEditors(table.getItem(i));
        }
        // rebuild table item
        for (int i : updateIndices) {
            initTableItem(tableModel.get(i), table.getItem(i));
        }

        table.setRedraw(true);

    }

    /**
     * DOC hcw Comment method "moveUpButtonClick".
     */
    protected void moveUpButtonClick() {
        table.setRedraw(false);
        int[] updateIndices = adjustIndicesUp(table.getSelectionIndices(), tableModel);

        // remove editors
        for (int i : updateIndices) {
            editorManager.removeEditors(table.getItem(i));
        }
        // rebuild table item
        for (int i : updateIndices) {
            initTableItem(tableModel.get(i), table.getItem(i));
        }

        table.setRedraw(true);
    }

    /**
     * DOC hcw Comment method "adjustIndicesDown".
     * 
     * @param selectionIndices
     * @param tableModel2
     * @return
     */
    private int[] adjustIndicesDown(int[] selectionIndices, List<SurrogateKey> model) {
        if (model.size() < 2) {
            return new int[0];
        }

        Map<Integer, Integer> selectionMap = new HashMap<Integer, Integer>(); // old selected item index, new selected
        // item index
        for (int i : selectionIndices) {
            selectionMap.put(i, i);
        }

        List<Integer> updateIndices = new ArrayList<Integer>(); // indices of item which will be updated
        Map<Integer, Integer> map = new HashMap<Integer, Integer>(); // new index , old index
        for (int i = selectionIndices.length - 1; i >= 0;) {
            int j = i - 1;
            // find continous block
            while (j >= 0 && selectionIndices[j] + 1 == selectionIndices[j + 1]) {
                j--;
            }

            if (selectionIndices[i] + 1 < tableModel.size()) {
                // from j + 1 to i, all index add 1
                for (int k = j + 1; k <= i; k++) {
                    map.put(selectionIndices[k] + 1, selectionIndices[k]);
                    selectionMap.put(selectionIndices[k], selectionIndices[k] + 1);
                }

                map.put(selectionIndices[j + 1], selectionIndices[i] + 1);
            }
            i = j;
        }
        List<SurrogateKey> temp = new ArrayList<SurrogateKey>(model);
        for (Integer newIndex : map.keySet()) {
            Integer oldIndex = map.get(newIndex);
            model.set(newIndex, temp.get(oldIndex));
            updateIndices.add(newIndex);
        }
        // move selection
        table.setSelection(convertToArray(new ArrayList<Integer>(selectionMap.values())));
        return convertToArray(updateIndices);
    }

    /**
     * DOC hcw Comment method "adjustIndicesUp".
     * 
     * @param selectionIndices
     * @param tableModel2
     * @return
     */
    private int[] adjustIndicesUp(int[] selectionIndices, List<SurrogateKey> model) {
        if (model.size() < 2) {
            return new int[0];
        }

        Map<Integer, Integer> selectionMap = new HashMap<Integer, Integer>(); // old selected item index, new selected
        // item index
        for (int i : selectionIndices) {
            selectionMap.put(i, i);
        }

        List<Integer> updateIndices = new ArrayList<Integer>(); // indices of item which will be updated
        Map<Integer, Integer> map = new HashMap<Integer, Integer>(); // new index , old index
        for (int i = selectionIndices.length - 1; i >= 0;) {
            int j = i - 1;
            // find continous block
            while (j >= 0 && selectionIndices[j] + 1 == selectionIndices[j + 1]) {
                j--;
            }

            if (selectionIndices[j + 1] - 1 >= 0) {
                // from j + 1 to i, all index minus 1
                for (int k = j + 1; k <= i; k++) {
                    map.put(selectionIndices[k] - 1, selectionIndices[k]);
                    selectionMap.put(selectionIndices[k], selectionIndices[k] - 1);
                }

                map.put(selectionIndices[i], selectionIndices[j + 1] - 1);
            }
            i = j;
        }
        List<SurrogateKey> temp = new ArrayList<SurrogateKey>(model);
        for (Integer newIndex : map.keySet()) {
            Integer oldIndex = map.get(newIndex);
            model.set(newIndex, temp.get(oldIndex));
            updateIndices.add(newIndex);
        }
        // move selection
        table.setSelection(convertToArray(new ArrayList<Integer>(selectionMap.values())));
        return convertToArray(updateIndices);

    }

    /**
     * DOC hcw Comment method "convertToArray".
     * 
     * @param collection
     * @return
     */
    private int[] convertToArray(List<Integer> collection) {
        // convert to int array
        int[] array = new int[collection.size()];
        for (int i = 0; i < array.length; i++) {
            array[i] = collection.get(i);
        }
        return array;
    }

    /**
     * DOC hcw Comment method "removeButtonClick".
     */
    private void removeButtonClick() {
        table.setRedraw(false);
        TableItem[] items = table.getSelection();
        for (TableItem item : items) {
            // SurrogateKey key = (SurrogateKey) item.getData();
            editorManager.removeEditors(item);
            tableModel.remove(item.getData());
        }

        table.remove(table.getSelectionIndices());
        table.setRedraw(true);
    }

    public List<SurrogateKey> getTableData() {
        return tableModel;
    }

    public void setTableInput(List<SurrogateKey> input) {
        tableModel = new ArrayList<SurrogateKey>();
        for (SurrogateKey key : input) {
            createNewItem(key);
            surrogateManager.addSurrogateKey(key);
        }
    }

    /**
     * DOC hcw Comment method "addButtonClick".
     */
    protected void addButtonClick() {
        SurrogateKey key = surrogateManager.createSurrogateKey();
        createNewItem(key);
    }

    public void createNewItem(final SurrogateKey key) {
        tableModel.add(key);
        table.setRedraw(false);
        final TableItem item = new TableItem(table, SWT.NONE);

        initTableItem(key, item);

        table.setRedraw(true);
    }

    /**
     * DOC hcw Comment method "initTableItem".
     * 
     * @param key
     * @param item
     */
    private void initTableItem(final SurrogateKey key, final TableItem item) {
        item.setData(key);
        // check
        // item.setText(new String[] { "", key.getColumn(), key.getCreation().getName(), key.getComplement() });
        //
        // boolean selected = isItemSelected(item);
        // TableEditor checkEditor = editorManager.createCheckboxEditor(table, selected, item, 0, new
        // IPropertySetter<Boolean>() {
        //
        // public void set(Boolean value) {
        // if (value.booleanValue()) {
        // setItemChecked(item, true);
        // }
        // }
        // });

        TableEditor editor = editorManager.createTextEditor(table, key.getColumn(), item, COLUMN_INDEX,
                new IPropertySetter<String>() {

                    public void set(String value) {
                        key.setColumn(value);
                    }
                });
        editor.layout();

        TableEditor comboEditor = editorManager.createComboEditor(table, SurrogateCreationType.getAllTypeNames(), item,
                CREATION_INDEX, key.getCreation().getIndex(), new IPropertySetter<Integer>() {

                    public void set(Integer value) {
                        key.setCreation(SurrogateCreationType.getTypeByIndex(value));
                        // remove value from model
                        key.setComplement("");
                        creationComplement(value, key, item);
                    }
                });

        creationComplement(key.getCreation().getIndex(), key, item);
    }

    /**
     * DOC hcw Comment method "markItemAsSelected".
     * 
     * @param item
     * @param checked
     */
    protected void setItemChecked(TableItem item, boolean checked) {
        for (int i = 0; i < table.getItemCount(); i++) {
            if (item == table.getItem(i)) {

                if (checked) {
                    table.setSelection(add(table.getSelectionIndices(), i));
                } else {
                    table.setSelection(remove(table.getSelectionIndices(), i));
                }

                break;
            }
        }
    }

    /**
     * DOC hcw Comment method "remove".
     * 
     * @param selectionIndices
     * @param i
     * @return
     */
    private int[] remove(int[] arr, int value) {
        int found = indexOf(arr, value);

        if (found > -1) {
            int[] newArr = copyOfArray(arr, arr.length - 1);
            if (found < newArr.length) {
                newArr[found] = arr[arr.length - 1]; // replace with the last element
            }
            return newArr;
        } else {
            return copyOfArray(arr, arr.length);
        }
    }

    public static int[] copyOfArray(int[] original, int newLength) {
        int[] copy = new int[newLength];
        System.arraycopy(original, 0, copy, 0, Math.min(original.length, newLength));
        return copy;
    }

    /**
     * DOC hcw Comment method "add".
     * 
     * @param selectionIndices
     * @param i
     * @return
     */
    private int[] add(int[] arr, int value) {
        int found = indexOf(arr, value);

        if (found > -1) {
            return copyOfArray(arr, arr.length);
        } else {
            int[] newArr = copyOfArray(arr, arr.length + 1);
            newArr[arr.length] = value;
            return newArr;
        }
    }

    /**
     * DOC hcw Comment method "find".
     * 
     * @param arr
     * @param value
     * @return
     */
    private int indexOf(int[] arr, int value) {
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == value) {
                return i;
            }
        }
        return -1;
    }

    /**
     * DOC hcw Comment method "isItemSelected".
     * 
     * @param item
     * @return
     */
    private boolean isItemSelected(TableItem item) {
        for (TableItem i : table.getSelection()) {
            if (i == item) {
                return true;
            }
        }
        return false;
    }

    /**
     * DOC hcw Comment method "onCreationChange".
     * 
     * @param value
     * @param key
     * @param item
     */
    protected void creationComplement(Integer value, final SurrogateKey key, TableItem item) {

        // dispose editor
        TableEditor editor = editorManager.getEditor(item, COMPLEMENT_INDEX);
        editorManager.disposeEditor(editor);

        if (value == SurrogateCreationType.ROUTINE.getIndex()) {
            // create routine editor
            editor = editorManager.createTextEditor(editor, table, key.getComplement(), item, COMPLEMENT_INDEX,
                    new IPropertySetter<String>() {

                        public void set(String value) {
                            key.setComplement(value);
                        }
                    });
            // add content proposal
            TalendProposalUtils.installOn(editor.getEditor(), null);
        } else if (value == SurrogateCreationType.INPUT_FIELD.getIndex()) {
            editor = editorManager.createLabelEditor(editor, table, key.getComplement(), item, COMPLEMENT_INDEX);
            final Label text = (Label) editor.getEditor();
            if (StringUtils.isEmpty(key.getComplement())) {
                // display as error status, this field must not be null
                text.setBackground(ERROR_COLOR);
            }
            // add drag and drop support
            IDragDropDelegate delegate = createDragDropDelegate(key, text);
            dragDropManager.addDragSupport(text, delegate);
            dragDropManager.addDropSupport(text, delegate);
        }
    }

    private IDragDropDelegate createDragDropDelegate(final SurrogateKey key, final Label text) {
        return new IDragDropDelegate() {

            public String getDragItemsAsText() {
                return "1|" + text.getText();
            }

            public void onDropItems(String data, Point position) {
                String[] items = data.split("\\|");
                text.setText(items[1]);
                key.setComplement(items[1]);
                text.setBackground(null);
            }

            public void removeDragItems() {
                text.setText("");
                key.setComplement("");
                // display as error status, this field must not be null
                text.setBackground(ERROR_COLOR);
                // don't remove table item here, it can be removed by the delete button
            }

            public boolean isDropAllowed(String data) {
                // only allow single selection
                return StringUtils.isEmpty(key.getComplement()) && data.startsWith("1|");
            }

        };

    }

}
