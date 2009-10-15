// ============================================================================
//
// Copyright (C) 2006-2009 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.repository.ui.wizards.metadata.table.database;

import java.lang.reflect.InvocationTargetException;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.SearchPattern;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.ui.swt.dialogs.ErrorDialogWidthDetailArea;
import org.talend.commons.ui.swt.formtools.Form;
import org.talend.commons.ui.swt.formtools.UtilsButton;
import org.talend.commons.ui.swt.tableviewer.TableViewerCreator;
import org.talend.commons.ui.swt.tableviewer.TableViewerCreator.LAYOUT_MODE;
import org.talend.commons.utils.data.text.IndiceHelper;
import org.talend.commons.utils.threading.TalendCustomThreadPoolExecutor;
import org.talend.core.model.metadata.IMetadataConnection;
import org.talend.core.model.metadata.builder.connection.ConnectionFactory;
import org.talend.core.model.metadata.builder.connection.DatabaseConnection;
import org.talend.core.model.metadata.builder.connection.MetadataColumn;
import org.talend.core.model.metadata.builder.connection.MetadataTable;
import org.talend.core.model.metadata.builder.connection.TableHelper;
import org.talend.core.model.metadata.builder.database.ExtractMetaDataFromDataBase;
import org.talend.core.model.metadata.builder.database.ExtractMetaDataUtils;
import org.talend.core.model.metadata.builder.database.TableInfoParameters;
import org.talend.core.model.metadata.editor.MetadataEmfTableEditor;
import org.talend.core.model.metadata.types.JavaTypesManager;
import org.talend.core.model.metadata.types.PerlTypesManager;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.core.model.utils.TalendTextUtils;
import org.talend.repository.i18n.Messages;
import org.talend.repository.model.IProxyRepositoryFactory;
import org.talend.repository.model.ProxyRepositoryFactory;
import org.talend.repository.ui.swt.utils.AbstractForm;
import org.talend.repository.ui.utils.ManagerConnection;

/**
 * @author cantoine
 * 
 */
public class SelectorTableForm extends AbstractForm {

    /**
     * FormTable Settings.
     */
    private static final int WIDTH_GRIDDATA_PIXEL = 700;

    /**
     * FormTable Var.
     */
    private final ManagerConnection managerConnection;

    private List<String> itemTableName;

    private IMetadataConnection iMetadataConnection = null;

    private MetadataTable metadataTable;

    private MetadataEmfTableEditor metadataEditor;

    private UtilsButton selectAllTablesButton;

    private UtilsButton selectNoneTablesButton;

    private UtilsButton checkConnectionButton;

    /**
     * Anothers Fields.
     */
    private final ConnectionItem connectionItem;

    private ConnectionItem templateConntion;

    // private DatabaseConnection connection;
    protected Table table;

    private int count = 0;

    private int countSuccess = 0;

    private int countPending = 0;

    private final WizardPage parentWizardPage;

    CustomThreadPoolExecutor threadExecutor;

    ScrolledComposite scrolledCompositeFileViewer;

    private Text nameFilter;

    private final TableInfoParameters tableInfoParameters;

    private boolean forTemplate = false;

    // store column number for each table name
    private Map<String, Integer> tableColumnNums = new HashMap<String, Integer>();

    /**
     * TableForm Constructor to use by RCP Wizard.
     * 
     * @param parent
     * @param page
     * @param connection
     * @param page
     * @param metadataTable
     */
    public SelectorTableForm(Composite parent, ConnectionItem connectionItem, SelectorTableWizardPage page) {
        super(parent, SWT.NONE);
        managerConnection = new ManagerConnection();
        this.connectionItem = connectionItem;
        this.parentWizardPage = page;
        this.tableInfoParameters = page.getTableInfoParameters();
        setupForm();
    }

    public SelectorTableForm(Composite parent, ConnectionItem connectionItem, SelectorTableWizardPage page, boolean forTemplate) {
        super(parent, SWT.NONE);
        managerConnection = new ManagerConnection();
        this.connectionItem = connectionItem;
        this.templateConntion = connectionItem;
        this.parentWizardPage = page;
        this.tableInfoParameters = page.getTableInfoParameters();
        this.forTemplate = forTemplate;
        if (forTemplate && getConnection().getTables().size() <= 0) {
            page.setPageComplete(false);
        }
        setupForm();
    }

    /**
     * 
     * Initialize value, forceFocus first field for right Click (new Table).
     * 
     */
    @Override
    public void initialize() {
    }

    public void initializeForm() {
        initExistingNames();
        selectAllTablesButton.setEnabled(true);
        count = 0;
    }

    @Override
    protected void addFields() {
        int leftCompositeWidth = 80;
        int rightCompositeWidth = WIDTH_GRIDDATA_PIXEL - leftCompositeWidth;
        int headerCompositeHeight = 60;
        int tableSettingsCompositeHeight = 90;
        int tableCompositeHeight = 200;

        int height = headerCompositeHeight + tableSettingsCompositeHeight + tableCompositeHeight;

        // Main Composite : 2 columns
        Composite mainComposite = Form.startNewDimensionnedGridLayout(this, 1, leftCompositeWidth + rightCompositeWidth, height);
        mainComposite.setLayout(new GridLayout(1, false));
        GridData gridData = new GridData(GridData.FILL_BOTH);
        mainComposite.setLayoutData(gridData);

        Composite rightComposite = Form.startNewDimensionnedGridLayout(mainComposite, 1, rightCompositeWidth, height);

        // Group Table Settings
        Group groupTableSettings = Form.createGroup(rightComposite, 1,
                Messages.getString("SelectorTableForm.groupTableSettings"), tableSettingsCompositeHeight); //$NON-NLS-1$

        // Composite TableSettings
        Composite compositeTableSettings = Form.startNewDimensionnedGridLayout(groupTableSettings, 1, rightCompositeWidth,
                tableSettingsCompositeHeight);
        gridData = new GridData(GridData.FILL_BOTH);
        gridData.widthHint = rightCompositeWidth;
        gridData.horizontalSpan = 3;

        Composite filterComposite = new Composite(compositeTableSettings, SWT.NONE);
        GridLayout gridLayout = new GridLayout(2, false);
        filterComposite.setLayout(gridLayout);
        GridData gridData2 = new GridData(GridData.FILL_HORIZONTAL);
        filterComposite.setLayoutData(gridData2);
        Label label = new Label(filterComposite, SWT.NONE);
        label.setText(Messages.getString("SelectorTableForm.nameFilter")); //$NON-NLS-1$
        nameFilter = new Text(filterComposite, SWT.BORDER);
        nameFilter.setToolTipText(Messages.getString("SelectorTableForm.enterType")); //$NON-NLS-1$
        nameFilter.setEditable(true);
        gridData2 = new GridData(GridData.FILL_HORIZONTAL);
        nameFilter.setLayoutData(gridData2);
        scrolledCompositeFileViewer = new ScrolledComposite(compositeTableSettings, SWT.H_SCROLL | SWT.V_SCROLL | SWT.NONE);
        scrolledCompositeFileViewer.setExpandHorizontal(true);
        scrolledCompositeFileViewer.setExpandVertical(true);
        GridData gridData1 = new GridData(GridData.FILL_BOTH);
        int width = 700;
        int hight = 325;
        if (forTemplate) {
            width = 375;
            hight = 300;
        }
        gridData1.widthHint = width;
        gridData1.heightHint = hight;
        gridData1.horizontalSpan = 2;
        scrolledCompositeFileViewer.setLayoutData(gridData1);

        createTable();

        // Composite retreiveSchema
        Composite compositeRetreiveSchemaButton = Form.startNewGridLayout(compositeTableSettings, 3, false, SWT.CENTER,
                SWT.BOTTOM);

        GC gc = new GC(compositeRetreiveSchemaButton);
        // Button Create Table
        String displayStr = Messages.getString("SelectorTableForm.selectAllTables");
        Point buttonSize = gc.stringExtent(displayStr);
        selectAllTablesButton = new UtilsButton(compositeRetreiveSchemaButton, displayStr, buttonSize.x + 12, HEIGHT_BUTTON_PIXEL); //$NON-NLS-1$

        displayStr = Messages.getString("SelectorTableForm.selectNoneTables"); //$NON-NLS-1$
        buttonSize = gc.stringExtent(displayStr);
        selectNoneTablesButton = new UtilsButton(compositeRetreiveSchemaButton, displayStr, buttonSize.x + 12,
                HEIGHT_BUTTON_PIXEL);

        // Button Check Connection
        displayStr = Messages.getString("DatabaseTableForm.checkConnection"); //$NON-NLS-1$
        buttonSize = gc.stringExtent(displayStr);
        checkConnectionButton = new UtilsButton(compositeRetreiveSchemaButton, displayStr, buttonSize.x + 12, HEIGHT_BUTTON_PIXEL);
        gc.dispose();

        metadataEditor = new MetadataEmfTableEditor(""); //$NON-NLS-1$
        // addUtilsButtonListeners();
    }

    /**
     * DOC qzhang Comment method "createTable".
     */
    private void createTable() {
        // List Table
        TableViewerCreator tableViewerCreator = new TableViewerCreator(scrolledCompositeFileViewer);
        tableViewerCreator.setColumnsResizableByDefault(true);
        tableViewerCreator.setBorderVisible(true);
        tableViewerCreator.setLayoutMode(LAYOUT_MODE.FILL_HORIZONTAL);
        tableViewerCreator.setCheckboxInFirstColumn(true);
        // tableViewerCreator.setAdjustWidthValue(-15);
        tableViewerCreator.setFirstColumnMasked(true);

        int columnWidth1 = 300;
        int columnWidth2 = 140;
        int columnWidth3 = 125;
        int columnWidth4 = 140;

        table = tableViewerCreator.createTable();
        if (forTemplate) {
            columnWidth1 = 150;
            columnWidth2 = 100;
            columnWidth3 = 100;
            columnWidth4 = 110;
        }
        table.setLayoutData(new GridData(GridData.FILL_BOTH));

        // table = new Table(scrolledCompositeFileViewer, SWT.CHECK | SWT.BORDER);
        // table.setBackground(table.getDisplay().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
        // table.setHeaderVisible(true);

        // table.setHeaderVisible(true);
        TableColumn tableName = new TableColumn(table, SWT.NONE);
        tableName.setText(Messages.getString("SelectorTableForm.TableName")); //$NON-NLS-1$
        tableName.setWidth(columnWidth1);

        tableName.addSelectionListener(getColumnSelectionListener());
        TableColumn tableType = new TableColumn(table, SWT.NONE);
        tableType.setText(Messages.getString("SelectorTableForm.TableType")); //$NON-NLS-1$
        tableType.setWidth(columnWidth2);

        tableType.addSelectionListener(getColumnSelectionListener());
        TableColumn nbColumns = new TableColumn(table, SWT.RIGHT);
        nbColumns.setText(Messages.getString("SelectorTableForm.ColumnNumber")); //$NON-NLS-1$
        nbColumns.setWidth(columnWidth3);

        TableColumn creationStatus = new TableColumn(table, SWT.RIGHT);
        creationStatus.setText(Messages.getString("SelectorTableForm.CreationStatus")); //$NON-NLS-1$
        creationStatus.setWidth(columnWidth4);

        scrolledCompositeFileViewer.setContent(table);
        scrolledCompositeFileViewer.setMinSize(table.computeSize(SWT.DEFAULT, SWT.DEFAULT));

    }

    private final Collator col = Collator.getInstance(Locale.getDefault());

    /**
     * DOC qzhang Comment method "getColumnSelectionListener".
     * 
     * @return
     */
    private SelectionAdapter getColumnSelectionListener() {
        return new SelectionAdapter() {

            int colIndex = 1;

            int updown = 1;

            private final Comparator strComparator = new Comparator() {

                public int compare(Object arg0, Object arg1) {

                    TableItem t1 = (TableItem) arg0;
                    TableItem t2 = (TableItem) arg1;

                    String v1 = (t1.getText(colIndex));
                    String v2 = (t2.getText(colIndex));

                    return (col.compare(v1, v2)) * updown;
                }
            };

            @Override
            public void widgetSelected(SelectionEvent e) {
                updown = updown * -1;

                TableColumn currentColumn = (TableColumn) e.widget;
                Table selectiontable = currentColumn.getParent();

                colIndex = searchColumnIndex(currentColumn);

                selectiontable.setRedraw(false);

                TableItem[] items = table.getItems();

                Arrays.sort(items, strComparator);

                selectiontable.setItemCount(items.length);

                for (int i = 0; i < items.length; i++) {
                    TableItem item = new TableItem(table, SWT.NONE, i);
                    item.setText(getData(items[i]));
                    if (items[i].getChecked()) {
                        clearTableItem(items[i]);
                        items[i].setChecked(false);
                        item.setChecked(true);
                        createTable(item);
                    }
                    items[i].dispose();
                }

                selectiontable.setRedraw(true);
                selectiontable.getParent().layout(true, true);
            }

            private String[] getData(TableItem t) {
                Table selectiontable = t.getParent();

                int colCount = selectiontable.getColumnCount();
                String[] s = new String[colCount];

                for (int i = 0; i < colCount; i++) {
                    s[i] = t.getText(i);
                }
                return s;

            }

            private int searchColumnIndex(TableColumn currentColumn) {
                Table selectiontable = currentColumn.getParent();

                int in = 0;

                for (int i = 0; i < selectiontable.getColumnCount(); i++) {
                    if (selectiontable.getColumn(i) == currentColumn) {
                        in = i;
                        break;
                    }
                }
                return in;
            }
        };
    }

    /**
     * addButtonControls.
     * 
     */
    @Override
    protected void addUtilsButtonListeners() {
        // Event CheckConnection Button
        checkConnectionButton.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(final SelectionEvent e) {
                count = 0;
                checkConnection(true);
            }
        });

        selectAllTablesButton.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(final SelectionEvent e) {
                updateStatus(IStatus.ERROR, null);
                TableItem[] tableItems = table.getItems();
                int size = tableItems.length;
                for (int i = 0; i < tableItems.length; i++) {
                    TableItem tableItem = tableItems[i];
                    if (!tableItem.getChecked()) {
                        tableItem.setText(3, Messages.getString("SelectorTableForm.Pending")); //$NON-NLS-1$
                        countPending++;
                        parentWizardPage.setPageComplete(false);
                        refreshTable(tableItem, size);
                    } else {
                        updateStatus(IStatus.OK, null);
                    }
                    tableItem.setChecked(true);
                }
                if (forTemplate) {
                    parentWizardPage.setPageComplete(true);
                }
            }
        });

        selectNoneTablesButton.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(final SelectionEvent e) {
                count = 0;
                countSuccess = 0;
                countPending = 0;
                TableItem[] tableItems = table.getItems();
                for (int i = 0; i < tableItems.length; i++) {
                    TableItem tableItem = tableItems[i];
                    if (tableItem.getChecked()) {
                        clearTableItem(tableItem);
                        tableItem.setChecked(false);
                    }
                }
                if (forTemplate) {
                    parentWizardPage.setPageComplete(false);
                }
            }

        });

        addTableListener();
    }

    /**
     * DOC qzhang Comment method "addTableListener".
     */
    private void addTableListener() {
        // Event checkBox action
        table.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(final SelectionEvent e) {
                if (e.detail == SWT.CHECK) {
                    TableItem tableItem = (TableItem) e.item;
                    boolean promptNeeded = tableItem.getChecked();
                    if (promptNeeded) {
                        tableItem.setText(2, ""); //$NON-NLS-1$
                        tableItem.setText(3, Messages.getString("SelectorTableForm.Pending")); //$NON-NLS-1$
                        countPending++;
                        parentWizardPage.setPageComplete(false);
                        refreshTable(tableItem, -1);
                    } else {
                        clearTableItem(tableItem);
                        if (tableItem.getText() != null
                                && tableItem.getText().equals(Messages.getString("SelectorTableForm.Pending"))) {
                            countPending--;
                        }
                    }
                    if (forTemplate && (getConnection().getTables().size() <= 0)) {
                        parentWizardPage.setPageComplete(false);
                    }
                }
            }
        });
    }

    /**
     * checkConnection.
     * 
     * @param displayMessageBox
     */
    protected void checkConnection(final boolean displayMessageBox) {
        try {
            if (table.getItemCount() > 0) {
                table.removeAll();
            }
            parentWizardPage.getWizard().getContainer().run(true, true, new IRunnableWithProgress() {

                public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
                    monitor.beginTask(Messages.getString("CreateTableAction.action.createTitle"), IProgressMonitor.UNKNOWN); //$NON-NLS-1$

                    managerConnection.check(getIMetadataConnection());

                    if (managerConnection.getIsValide()) {
                        itemTableName = ExtractMetaDataFromDataBase.returnTablesFormConnection(iMetadataConnection,
                                getTableInfoParameters());
                        if (itemTableName.size() <= 0) {
                            // connection is done but any table exist
                            if (displayMessageBox) {
                                openInfoDialogInUIThread(getShell(),
                                        Messages.getString("DatabaseTableForm.checkConnection"), Messages //$NON-NLS-1$
                                                .getString("DatabaseTableForm.tableNoExist"), true);//$NON-NLS-1$
                            }
                        } else {
                            createAllItems(displayMessageBox, null);
                        }
                    } else if (displayMessageBox) {
                        // connection failure
                        getShell().getDisplay().asyncExec(new Runnable() {

                            public void run() {
                                new ErrorDialogWidthDetailArea(getShell(), PID, Messages
                                        .getString("DatabaseTableForm.connectionFailureTip"), //$NON-NLS-1$
                                        managerConnection.getMessageException());
                            }
                        });
                    }
                    monitor.done();
                }
            });
        } catch (Exception e) {
            ExceptionHandler.process(e);
        }

    }

    /**
     * DOC qzhang Comment method "createAllItems".
     * 
     * @param displayMessageBox
     * @param newList
     */
    private void createAllItems(final boolean displayMessageBox, final List<String> newList) {
        Display.getDefault().asyncExec(new Runnable() {

            public void run() {
                List<String> list = new ArrayList<String>();
                if (newList != null) {
                    list.addAll(newList);
                } else {
                    list = itemTableName;
                }
                // connection is done and tables exist
                if (list != null && !list.isEmpty()) {
                    // fill the combo
                    Iterator<String> iterate = list.iterator();
                    while (iterate.hasNext()) {
                        String nameTable = iterate.next();
                        String name = ExtractMetaDataFromDataBase.getTableTypeByTableName(nameTable);
                        if (nameTable != null && name != null) {
                            TableItem item = new TableItem(table, SWT.NONE);
                            item.setText(0, nameTable);
                            item.setText(1, name);
                        }
                    }
                }
                restoreCheckItems();
                // if (forTemplate) {
                // if (displayMessageBox) {
                // MessageBox box = new MessageBox(getShell(), SWT.ICON_ERROR | SWT.OK | SWT.CANCEL);
                //                        box.setText(Messages.getString("DatabaseTableForm.checkConnection")); //$NON-NLS-1$
                //                        box.setMessage("Connection unsuccessfully!"); //$NON-NLS-1$
                // box.open();
                // }
                // } else {
                if (displayMessageBox) {
                    String msg = Messages.getString("DatabaseTableForm.connectionIsDone"); //$NON-NLS-1$
                    openInfoDialogInUIThread(getShell(), Messages.getString("DatabaseTableForm.checkConnection"), msg, false); //$NON-NLS-1$
                }
                // }
            }
        });
    }

    public static void openInfoDialogInUIThread(final Shell shell, final String title, final String msg, boolean ifUseRunnable) {
        if (ifUseRunnable) {
            shell.getDisplay().asyncExec(new Runnable() {

                public void run() {
                    MessageDialog.openInformation(shell, title, msg);
                }
            });
        } else {
            MessageDialog.openInformation(shell, title, msg);
        }
    }

    /**
     * createTable.
     * 
     * @param tableItem
     */
    protected void createTable(TableItem tableItem) {
        String tableString = tableItem.getText(0);

        boolean checkConnectionIsDone = managerConnection.check(getIMetadataConnection(), true);
        if (!checkConnectionIsDone) {
            updateStatus(IStatus.WARNING, Messages.getString("DatabaseTableForm.connectionFailure")); //$NON-NLS-1$
            new ErrorDialogWidthDetailArea(getShell(), PID, Messages.getString("DatabaseTableForm.connectionFailure"), //$NON-NLS-1$
                    managerConnection.getMessageException());
        } else {
            List<MetadataColumn> metadataColumns = new ArrayList<MetadataColumn>();
            metadataColumns = ExtractMetaDataFromDataBase.returnMetadataColumnsFormTable(iMetadataConnection, tableItem
                    .getText(0));

            tableItem.setText(2, "" + metadataColumns.size()); //$NON-NLS-1$
            tableItem.setText(3, Messages.getString("SelectorTableForm.Success")); //$NON-NLS-1$
            countSuccess++;

            IProxyRepositoryFactory factory = ProxyRepositoryFactory.getInstance();

            metadataTable = ConnectionFactory.eINSTANCE.createMetadataTable();

            initExistingNames();
            metadataTable.setLabel(IndiceHelper.getIndexedLabel(tableString, existingNames));
            metadataTable.setSourceName(tableItem.getText(0));
            metadataTable.setId(factory.getNextId());
            metadataTable.setTableType(ExtractMetaDataFromDataBase.getTableTypeByTableName(tableString));

            List<MetadataColumn> metadataColumnsValid = new ArrayList<MetadataColumn>();
            Iterator iterate = metadataColumns.iterator();

            while (iterate.hasNext()) {
                MetadataColumn metadataColumn = (MetadataColumn) iterate.next();

                // Check the label and add it to the table
                metadataColumnsValid.add(metadataColumn);
                metadataTable.getColumns().add(metadataColumn);
            }
            if (!getConnection().getTables().contains(metadataTable) && !limitTemplateTable(metadataTable)) {
                getConnection().getTables().add(metadataTable);
            }

        }
    }

    /**
     * deleteTable.
     * 
     * @param tableItem
     */
    protected void deleteTable(TableItem tableItem) {

        if (itemTableName != null && !itemTableName.isEmpty()) {
            // fill the combo
            Collection tables = new ArrayList();
            Iterator<MetadataTable> iterate = getConnection().getTables().iterator();
            while (iterate.hasNext()) {
                MetadataTable metadata = iterate.next();
                if (metadata != null && metadata.getLabel().equals(tableItem.getText(0))) {
                    tables.add(metadata);
                }
            }
            getConnection().getTables().removeAll(tables);
        }
    }

    /**
     * A subclass of ThreadPoolExecutor that executes each submitted RetrieveColumnRunnable using one of possibly
     * several pooled threads.
     * 
     * 
     */
    class CustomThreadPoolExecutor extends TalendCustomThreadPoolExecutor {

        // This map is used to store the tableItems that are selected or unselected by the user.
        // see afterExecute() and beforeExecute(). If an item is in the map, it means that the item's
        // related thread is running.
        Map<TableItem, RetrieveColumnRunnable> map = Collections
                .synchronizedMap(new HashMap<TableItem, RetrieveColumnRunnable>());

        public CustomThreadPoolExecutor(int queueCapacity) {
            super(queueCapacity);
        }

        /*
         * (non-Javadoc)
         * 
         * @see java.util.concurrent.ThreadPoolExecutor#afterExecute(java.lang.Runnable, java.lang.Throwable)
         */
        @Override
        protected void afterExecute(Runnable r, Throwable t) {
            RetrieveColumnRunnable runnable = (RetrieveColumnRunnable) r;
            map.remove(runnable.getTableItem());
        }

        /*
         * (non-Javadoc)
         * 
         * @see java.util.concurrent.ThreadPoolExecutor#beforeExecute(java.lang.Thread, java.lang.Runnable)
         */
        @Override
        protected void beforeExecute(Thread t, Runnable r) {
            RetrieveColumnRunnable runnable = (RetrieveColumnRunnable) r;
            map.put(runnable.getTableItem(), runnable);
        }

        /**
         * If an item is in the List runningThreads, it means that the item's related thread is running.
         * 
         * @param item
         * @return
         */
        public boolean isThreadRunning(TableItem item) {
            return map.containsKey(item);
        }

        /**
         * Find the RetrieveColumnRunnable from map and waiting queue. Map stores running runnables
         * 
         * @param key
         * @return
         */
        public synchronized RetrieveColumnRunnable getRunnable(TableItem key) {
            // Get the runnable from map first, else then find it in the waiting queue.
            RetrieveColumnRunnable runnable = map.get(key);
            if (runnable != null) {
                return runnable;
            }
            for (Iterator iter = getQueue().iterator(); iter.hasNext();) {
                RetrieveColumnRunnable element = (RetrieveColumnRunnable) iter.next();
                if (element.getTableItem() == key) {
                    return element;
                }
            }
            return null;
        }
    }

    /**
     * Subclass of SWTUIThreadProcessor to process the Retrieving Columns job. <br/>
     * 
     */
    class RetrieveColumnRunnable implements Runnable {

        TableItem tableItem;

        String tableString = null;

        boolean checkConnectionIsDone = false;

        List<MetadataColumn> metadataColumns = null;

        volatile boolean isCanceled = false;

        /**
         * Getter for tableItem.
         * 
         * @return the tableItem
         */
        public TableItem getTableItem() {
            return this.tableItem;
        }

        RetrieveColumnRunnable(TableItem tableItem) {
            this.tableItem = tableItem;
            setup();
        }

        public void setCanceled(boolean cancel) {
            this.isCanceled = cancel;
        }

        /**
         * Getter for isCanceled.
         * 
         * @return the isCanceled
         */
        public boolean isCanceled() {
            return this.isCanceled;
        }

        /**
         * Get all the parameters from UI for the non-UI job to use.
         */
        private void setup() {
            tableString = tableItem.getText(0);
        }

        public void run() {
            if (isCanceled()) {
                return;
            }
            metadataColumns = ExtractMetaDataFromDataBase.returnMetadataColumnsFormTable(iMetadataConnection, tableString, true);
            if (isCanceled()) {
                return;
            }
            IProxyRepositoryFactory factory = ProxyRepositoryFactory.getInstance();

            metadataTable = ConnectionFactory.eINSTANCE.createMetadataTable();
            initExistingNames();
            metadataTable.setLabel(IndiceHelper.getIndexedLabel(tableString, existingNames));
            metadataTable.setSourceName(tableString);
            metadataTable.setId(factory.getNextId());
            metadataTable.setTableType(ExtractMetaDataFromDataBase.getTableTypeByTableName(tableString));
            List<MetadataColumn> metadataColumnsValid = new ArrayList<MetadataColumn>();
            Iterator iterate = metadataColumns.iterator();
            while (iterate.hasNext()) {
                MetadataColumn metadataColumn = (MetadataColumn) iterate.next();
                if (metadataColumn.getTalendType().equals(JavaTypesManager.DATE.getId())
                        || metadataColumn.getTalendType().equals(PerlTypesManager.DATE)) {
                    if ("".equals(metadataColumn.getPattern())) {
                        metadataColumn.setPattern(TalendTextUtils.addQuotes("dd-MM-yyyy"));
                    }
                }
                // Check the label and add it to the table
                metadataColumnsValid.add(metadataColumn);
                metadataTable.getColumns().add(metadataColumn);
            }
            if (!getConnection().getTables().contains(metadataTable) && !limitTemplateTable(metadataTable)) {
                getConnection().getTables().add(metadataTable);
            }

            checkConnectionIsDone = true;

            Display.getDefault().asyncExec(new Runnable() {

                public void run() {
                    if (isCanceled()) {
                        return;
                    }
                    updateUIInThreadIfThread();
                }
            });

        }

        public void updateUIInThreadIfThread() {
            if (tableItem.isDisposed()) {
                return;
            }

            if (checkConnectionIsDone) {
                tableItem.setText(2, "" + metadataColumns.size()); //$NON-NLS-1$
                tableItem.setText(3, Messages.getString("SelectorTableForm.Success")); //$NON-NLS-1$
                countSuccess++;
                tableColumnNums.put(tableItem.getText(0), metadataColumns.size());
            } else {
                updateStatus(IStatus.WARNING, Messages.getString("DatabaseTableForm.connectionFailure")); //$NON-NLS-1$
                new ErrorDialogWidthDetailArea(getShell(), PID, Messages.getString("DatabaseTableForm.connectionFailure"), //$NON-NLS-1$
                        managerConnection.getMessageException());

            }
            count++;

            updateStatus(IStatus.OK, null);
            // selectNoneTablesButton.setEnabled(true);
            // checkConnectionButton.setEnabled(true);

            parentWizardPage.setPageComplete(threadExecutor.getQueue().isEmpty()
                    && (threadExecutor.getActiveCount() == 0 || countSuccess == countPending));
        }
    }

    /**
     * refreshTable. This Methos execute the CreateTable in a Thread task.
     * 
     * @param tableItem
     * @param size
     */
    private void refreshTable(final TableItem tableItem, final int size) {
        if (threadExecutor == null) {
            return;
        }
        if (!threadExecutor.isThreadRunning(tableItem)) {
            if (managerConnection.check(getIMetadataConnection(), true)) {
                RetrieveColumnRunnable runnable = new RetrieveColumnRunnable(tableItem);
                threadExecutor.execute(runnable);
            }
        } else {
            RetrieveColumnRunnable runnable = threadExecutor.getRunnable(tableItem);
            runnable.setCanceled(false);
        }
    }

    private void clearTableItem(TableItem item) {
        clearTableItem(item, true);
    }

    private void clearTableItem(TableItem item, boolean deleteFromConnection) {
        if (deleteFromConnection) {
            deleteTable(item);
        }
        item.setText(2, ""); //$NON-NLS-1$
        item.setText(3, ""); //$NON-NLS-1$
        RetrieveColumnRunnable runnable = threadExecutor.getRunnable(item);
        if (runnable != null) {
            runnable.setCanceled(true);
        }
    }

    /**
     * DOC ocarbone Comment method "initExistingNames".
     * 
     * @param connection
     * @param metadataTable
     */
    private void initExistingNames() {
        String[] exisNames;
        if (metadataTable != null) {
            exisNames = TableHelper.getTableNames(getConnection(), metadataTable.getLabel());
        } else {
            exisNames = TableHelper.getTableNames(getConnection());
        }
        this.existingNames = existingNames == null ? Collections.EMPTY_LIST : Arrays.asList(exisNames);
    }

    /**
     * Main Fields addControls.
     */
    @Override
    protected void addFieldsListeners() {
        nameFilter.addModifyListener(new ModifyListener() {

            public void modifyText(ModifyEvent e) {
                List<String> newList = new ArrayList<String>();

                String pattern = nameFilter.getText();
                SearchPattern matcher = new SearchPattern();
                matcher.setPattern(pattern);
                for (int i = 0; i < itemTableName.size(); i++) {
                    String item = itemTableName.get(i);
                    if (matcher.matches(item)) {
                        newList.add(item);
                    }
                }
                for (int j = 0; j < table.getItemCount(); j++) {
                    TableItem item = table.getItem(j);
                    if (item.getChecked()) {
                        clearTableItem(item, false);
                        item.setChecked(false);
                    }
                }
                table.clearAll();
                if (!table.isDisposed()) {
                    table.dispose();
                    table = null;
                    createTable();
                    addTableListener();
                }
                createAllItems(false, newList);
            }

        });
    }

    /**
     * DOC hcw Comment method "restoreCheckItems".
     */
    protected void restoreCheckItems() {
        Set<String> checkedItems = new HashSet<String>();
        for (Object obj : getConnection().getTables()) {
            if (obj == null) {
                continue;
            }
            MetadataTable table = (MetadataTable) obj;
            checkedItems.add(table.getLabel());
        }
        for (TableItem tableItem : table.getItems()) {
            if (checkedItems.contains(tableItem.getText(0))) {
                tableItem.setChecked(true);
                Integer num = tableColumnNums.get(tableItem.getText(0));
                if (num != null) {
                    // get column num from previous result
                    tableItem.setText(2, num.toString());
                    tableItem.setText(3, Messages.getString("SelectorTableForm.Success")); //$NON-NLS-1$   
                } else {
                    // retrieve column num again
                    refreshTable(tableItem, -1);
                }
            }
        }
    }

    /**
     * Ensures that fields are set. Update checkEnable / use to checkTableSetting().
     */
    @Override
    protected boolean checkFieldsValue() {
        updateStatus(IStatus.OK, null);
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.ui.swt.AbstractForm#adaptFormToReadOnly()
     */
    @Override
    protected void adaptFormToReadOnly() {
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.swt.widgets.Control#setVisible(boolean)
     */
    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);
        if (visible) {
            initializeForm();
        }
        // initControlData();
    }

    /**
     * DOC qzhang Comment method "initControlData".
     */
    public void initControlData() {
        checkConnection(false);
        if (itemTableName.size() > 0) {
            threadExecutor = new CustomThreadPoolExecutor(itemTableName.size());
        }
    }

    public void initControlData(boolean flag) {
        checkConnection(flag);
        if (itemTableName != null && itemTableName.size() > 0) {
            threadExecutor = new CustomThreadPoolExecutor(itemTableName.size());
        }
    }

    protected DatabaseConnection getConnection() {
        if (forTemplate) {
            return (DatabaseConnection) templateConntion.getConnection();
        } else {
            return (DatabaseConnection) connectionItem.getConnection();
        }

    }

    public Table getTable() {
        return this.table;
    }

    /**
     * DOC nrousseau Comment method "performCancel".
     */
    public void performCancel() {
        processWhenDispose();
    }

    /**
     * Getter for tableInfoParameters.
     * 
     * @return the tableInfoParameters
     */
    public TableInfoParameters getTableInfoParameters() {
        return this.tableInfoParameters;
    }

    public IMetadataConnection getIMetadataConnection() {
        return this.iMetadataConnection;
    }

    public void setIMetadataConnection(IMetadataConnection metadataConnection) {
        this.iMetadataConnection = metadataConnection;
    }

    @Override
    protected void processWhenDispose() {
        if (threadExecutor != null) {
            threadExecutor.clearThreads();
            ExtractMetaDataUtils.closeConnection();
        }
    }

    public ConnectionItem getTemplateConntion() {
        return this.templateConntion;
    }

    public void setTemplateConntion(ConnectionItem templateConntion) {
        this.templateConntion = templateConntion;
    }

    private boolean limitTemplateTable(MetadataTable tabel) {
        boolean exist = false;
        if (!forTemplate) {
            return exist;
        }
        for (int i = 0; i < getConnection().getTables().size(); i++) {
            String sourceName = tabel.getSourceName();
            if (((MetadataTable) getConnection().getTables().get(i)).getSourceName().equals(sourceName)) {
                exist = true;
                break;
            }
        }
        return exist;
    }

}
