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
package org.talend.repository.ui.wizards.metadata.table.database;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.talend.commons.ui.swt.dialogs.ErrorDialogWidthDetailArea;
import org.talend.commons.ui.swt.formtools.Form;
import org.talend.commons.ui.swt.formtools.UtilsButton;
import org.talend.commons.ui.swt.tableviewer.TableViewerCreator;
import org.talend.commons.ui.swt.tableviewer.TableViewerCreator.LAYOUT_MODE;
import org.talend.commons.utils.data.text.IndiceHelper;
import org.talend.core.model.metadata.IMetadataConnection;
import org.talend.core.model.metadata.builder.ConvertionHelper;
import org.talend.core.model.metadata.builder.connection.ConnectionFactory;
import org.talend.core.model.metadata.builder.connection.DatabaseConnection;
import org.talend.core.model.metadata.builder.connection.MetadataColumn;
import org.talend.core.model.metadata.builder.connection.MetadataTable;
import org.talend.core.model.metadata.builder.connection.TableHelper;
import org.talend.core.model.metadata.builder.database.ExtractMetaDataFromDataBase;
import org.talend.core.model.metadata.editor.MetadataEditor2;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.repository.i18n.Messages;
import org.talend.repository.model.IRepositoryFactory;
import org.talend.repository.model.RepositoryFactoryProvider;
import org.talend.repository.ui.swt.utils.AbstractForm;
import org.talend.repository.ui.utils.ManagerConnection;
import org.talend.repository.ui.views.RepositoryView;

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
    private ManagerConnection managerConnection;

    private List<String> itemTableName;

    private IMetadataConnection iMetadataConnection = null;

    private MetadataTable metadataTable;

    private MetadataEditor2 metadataEditor;

    private UtilsButton selectAllTablesButton;

    private UtilsButton selectNoneTablesButton;

    private UtilsButton checkConnectionButton;

    /**
     * Anothers Fields.
     */
    private ConnectionItem connectionItem;

    // private DatabaseConnection connection;

    protected Table table;

    private Collection<TableItem> tableItems;

    private int count = 0;

    /**
     * TableForm Constructor to use by RCP Wizard.
     * 
     * @param parent
     * @param connection
     * @param page
     * @param metadataTable
     */
    public SelectorTableForm(Composite parent, ConnectionItem connectionItem) {
        super(parent, SWT.NONE);
        managerConnection = new ManagerConnection();
        this.connectionItem = connectionItem;
        setupForm();
    }

    /**
     * 
     * Initialize value, forceFocus first field for right Click (new Table).
     * 
     */
    public void initialize() {
    }

    public void initializeForm() {
        initExistingNames();
        selectAllTablesButton.setEnabled(true);
        count = 0;
    }

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
                Messages.getString("SelectorTableForm.groupTableSettings"), tableSettingsCompositeHeight);

        // Composite TableSettings
        Composite compositeTableSettings = Form.startNewDimensionnedGridLayout(groupTableSettings, 1, rightCompositeWidth,
                tableSettingsCompositeHeight);
        gridData = new GridData(GridData.FILL_BOTH);
        gridData.widthHint = rightCompositeWidth;
        gridData.horizontalSpan = 3;

        ScrolledComposite scrolledCompositeFileViewer = new ScrolledComposite(compositeTableSettings, SWT.H_SCROLL | SWT.V_SCROLL
                | SWT.NONE);
        scrolledCompositeFileViewer.setExpandHorizontal(true);
        scrolledCompositeFileViewer.setExpandVertical(true);
        GridData gridData1 = new GridData(GridData.FILL_BOTH);
        gridData1.widthHint = WIDTH_GRIDDATA_PIXEL;
        gridData1.heightHint = 325;
        gridData1.horizontalSpan = 2;
        scrolledCompositeFileViewer.setLayoutData(gridData1);

        // List Table
        TableViewerCreator tableViewerCreator = new TableViewerCreator(scrolledCompositeFileViewer);
        tableViewerCreator.setHeaderVisible(true);
        tableViewerCreator.setAllColumnsResizable(true);
        tableViewerCreator.setBorderVisible(true);
        tableViewerCreator.setLinesVisible(true);
        tableViewerCreator.setHorizontalScroll(false);
        tableViewerCreator.setLayoutMode(LAYOUT_MODE.SHOW_ALWAYS_ALL_COLUMNS);
        tableViewerCreator.setCheckboxInFirstColumn(true);
        // tableViewerCreator.setAdjustWidthValue(-15);
        tableViewerCreator.setFirstColumnMasked(true);

        table = tableViewerCreator.createTable();
        table.setLayoutData(new GridData(GridData.FILL_BOTH));

        // table = new Table(scrolledCompositeFileViewer, SWT.CHECK | SWT.BORDER);
        // table.setBackground(table.getDisplay().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
        // table.setHeaderVisible(true);

        // table.setHeaderVisible(true);
        TableColumn tableName = new TableColumn(table, SWT.NONE);
        tableName.setText(Messages.getString("SelectorTableForm.TableName"));
        tableName.setWidth(300);

        TableColumn nbColumns = new TableColumn(table, SWT.RIGHT);
        nbColumns.setText(Messages.getString("SelectorTableForm.ColumnNumber"));
        nbColumns.setWidth(200);

        TableColumn creationStatus = new TableColumn(table, SWT.RIGHT);
        creationStatus.setText(Messages.getString("SelectorTableForm.CreationStatus"));
        creationStatus.setWidth(200);

        scrolledCompositeFileViewer.setContent(table);
        scrolledCompositeFileViewer.setMinSize(table.computeSize(SWT.DEFAULT, SWT.DEFAULT));

        // Composite retreiveSchema
        Composite compositeRetreiveSchemaButton = Form.startNewGridLayout(compositeTableSettings, 3, false, SWT.CENTER,
                SWT.BOTTOM);

        // Button Create Table
        selectAllTablesButton = new UtilsButton(compositeRetreiveSchemaButton, Messages
                .getString("SelectorTableForm.selectAllTables"), WIDTH_BUTTON_PIXEL, HEIGHT_BUTTON_PIXEL);

        selectNoneTablesButton = new UtilsButton(compositeRetreiveSchemaButton, Messages
                .getString("SelectorTableForm.selectNoneTables"), WIDTH_BUTTON_PIXEL, HEIGHT_BUTTON_PIXEL);

        // Button Check Connection
        checkConnectionButton = new UtilsButton(compositeRetreiveSchemaButton, Messages
                .getString("DatabaseTableForm.checkConnection"), WIDTH_BUTTON_PIXEL, HEIGHT_BUTTON_PIXEL);

        metadataEditor = new MetadataEditor2(Messages.getString("DatabaseTableForm.metadataDescription"));
        addUtilsButtonListeners();
    }

    /**
     * addButtonControls.
     * 
     */
    protected void addUtilsButtonListeners() {

        // Event CheckConnection Button
        checkConnectionButton.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(final SelectionEvent e) {
                selectAllTablesButton.setEnabled(true);
                count = 0;
                if (!checkConnectionButton.getEnabled()) {
                    checkConnectionButton.setEnabled(true);
                    checkConnection(true);
                } else {
                    checkConnectionButton.setEnabled(false);
                }
            }
        });

        selectAllTablesButton.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(final SelectionEvent e) {
                updateStatus(IStatus.ERROR, null);
                selectAllTablesButton.setEnabled(false);
                selectNoneTablesButton.setEnabled(false);
                checkConnectionButton.setEnabled(false);
                if (!table.getEnabled()) {
                    TableItem[] tableItems = table.getItems();
                    int size = tableItems.length;
                    for (int i = 0; i < tableItems.length; i++) {
                        TableItem tableItem = tableItems[i];
                        table.setEnabled(true);
                        if (!tableItem.getChecked()) {
                            tableItem.setText(2, Messages.getString("SelectorTableForm.Pending"));
                            refreshTable(tableItem, size);
                        }
                        tableItem.setChecked(true);
                    }
                } else {
                    table.setEnabled(false);
                }
            }
        });

        selectNoneTablesButton.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(final SelectionEvent e) {
                selectAllTablesButton.setEnabled(true);
                count = 0;
                if (!table.getEnabled()) {
                    TableItem[] tableItems = table.getItems();
                    for (int i = 0; i < tableItems.length; i++) {
                        TableItem tableItem = tableItems[i];
                        table.setEnabled(true);
                        if (tableItem.getChecked()) {
                            deleteTable(tableItem);
                            tableItem.setText(1, "");
                            tableItem.setText(2, "");
                        }
                        tableItem.setChecked(false);
                    }
                } else {
                    table.setEnabled(false);
                }
            }
        });

        // Event checkBox action
        table.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(final SelectionEvent e) {
                if (!table.getEnabled()) {
                    table.setEnabled(true);
                    if (e.detail == SWT.CHECK) {
                        TableItem tableItem = (TableItem) e.item;
                        boolean promptNeeded = tableItem.getChecked();
                        if (promptNeeded) {
                            tableItems.remove(tableItem);
                            tableItems.add(tableItem);
                            refreshTable(tableItem, -1);
                        } else {
                            tableItems.remove(tableItem);
                            deleteTable(tableItem);
                            tableItem.setText(1, "");
                            tableItem.setText(2, "");
                        }
                    }
                } else {
                    table.setEnabled(false);
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

        tableItems = new ArrayList<TableItem>();

        iMetadataConnection = ConvertionHelper.convert(getConnection());
        managerConnection.check(iMetadataConnection);

        if (table.getItemCount() > 0) {
            table.removeAll();
        }

        if (managerConnection.getIsValide()) {
            itemTableName = ExtractMetaDataFromDataBase.returnTablesFormConnection(iMetadataConnection);
            if (itemTableName.size() <= 0) {
                // connection is done but any table exist

                if (displayMessageBox) {
                    MessageDialog.openInformation(getShell(), Messages.getString("DatabaseTableForm.checkConnection"), Messages
                            .getString("DatabaseTableForm.tableNoExist"));
                }
            } else {
                // connection is done and tables exist
                if (itemTableName != null && !itemTableName.isEmpty()) {
                    // fill the combo
                    Iterator<String> iterate = itemTableName.iterator();
                    while (iterate.hasNext()) {
                        String nameTable = iterate.next();
                        TableItem item = new TableItem(table, SWT.NONE);
                        item.setText(nameTable);
                    }
                }
                if (displayMessageBox) {
                    String msg = Messages.getString("DatabaseTableForm.connectionIsDone");
                    MessageDialog.openInformation(getShell(), Messages.getString("DatabaseTableForm.checkConnection"), msg);
                }
            }
        } else if (displayMessageBox) {
            // connection failure
            new ErrorDialogWidthDetailArea(getShell(), PID, Messages.getString("DatabaseTableForm.connectionFailureTip"),
                    managerConnection.getMessageException());
        }
    }

    /**
     * createTable.
     * 
     * @param tableItem
     */
    protected void createTable(TableItem tableItem) {

        String tableString = tableItem.getText().replaceAll("_", "-");

        IMetadataConnection iMetadataConnection = ConvertionHelper.convert(getConnection());
        boolean checkConnectionIsDone = managerConnection.check(iMetadataConnection);

        if (!checkConnectionIsDone) {
            updateStatus(IStatus.WARNING, Messages.getString("DatabaseTableForm.connectionFailure"));
            new ErrorDialogWidthDetailArea(getShell(), PID, Messages.getString("DatabaseTableForm.connectionFailure"),
                    managerConnection.getMessageException());
        } else {
            List<MetadataColumn> metadataColumns = new ArrayList<MetadataColumn>();
            metadataColumns = ExtractMetaDataFromDataBase
                    .returnMetadataColumnsFormTable(iMetadataConnection, tableItem.getText());

            tableItem.setText(1, "" + metadataColumns.size());
            tableItem.setText(2, Messages.getString("SelectorTableForm.Success"));

            IWorkbenchPage activePage = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
            IViewPart viewPart = (IViewPart) activePage.findView(RepositoryView.VIEW_ID);
            IRepositoryFactory factory = RepositoryFactoryProvider
                    .getInstance(((RepositoryView) viewPart).getRepositoryContext());

            metadataTable = ConnectionFactory.eINSTANCE.createMetadataTable();
            initExistingNames();
            metadataTable.setLabel(IndiceHelper.getIndexedLabel(tableString, existingNames));
            metadataTable.setSourceName(tableItem.getText());
            metadataTable.setId(factory.getNextId());
            List<MetadataColumn> metadataColumnsValid = new ArrayList<MetadataColumn>();
            Iterator iterate = metadataColumns.iterator();

            while (iterate.hasNext()) {
                MetadataColumn metadataColumn = (MetadataColumn) iterate.next();

                // Check the label and add it to the table
                metadataColumnsValid.add(metadataColumn);
                metadataTable.getColumns().add(metadataColumn);
            }
            getConnection().getTables().add(metadataTable);
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
                if (metadata.getLabel().equals(tableItem.getText().replaceAll("_", "-"))) {
                    tables.add(metadata);
                }
            }
            getConnection().getTables().removeAll(tables);
        }
    }

    /**
     * refreshTable. This Methos execute the CreateTable in a Thread task.
     * 
     * @param tableItem
     * @param size
     */
    private void refreshTable(final TableItem tableItem, final int size) {
        getDisplay().asyncExec(new Runnable() {

            public void run() {
                createTable(tableItem);
                count++;
                if (count == size) {
                    updateStatus(IStatus.OK, null);
                    selectNoneTablesButton.setEnabled(true);
                    checkConnectionButton.setEnabled(true);
                }
            }
        });
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
    protected void addFieldsListeners() {

    }

    /**
     * Ensures that fields are set. Update checkEnable / use to checkTableSetting().
     */
    protected boolean checkFieldsValue() {
        updateStatus(IStatus.OK, null);
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.ui.swt.AbstractForm#adaptFormToReadOnly()
     */
    protected void adaptFormToReadOnly() {
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.swt.widgets.Control#setVisible(boolean)
     * 
     */
    public void setVisible(boolean visible) {
        super.setVisible(visible);
        if (visible) {
            initializeForm();
        }
        checkConnection(false);
    }

    protected DatabaseConnection getConnection() {
        return (DatabaseConnection) connectionItem.getConnection();
    }
}
