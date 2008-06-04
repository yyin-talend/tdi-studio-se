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
package org.talend.repository.ui.wizards.metadata.connection.files.salesforce;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TableColumn;
import org.talend.commons.ui.swt.dialogs.ErrorDialogWidthDetailArea;
import org.talend.commons.ui.swt.formtools.Form;
import org.talend.commons.ui.swt.formtools.LabelledText;
import org.talend.commons.ui.swt.formtools.UtilsButton;
import org.talend.commons.ui.swt.thread.SWTUIThreadProcessor;
import org.talend.core.model.metadata.IMetadataColumn;
import org.talend.core.model.metadata.IMetadataContextModeManager;
import org.talend.core.model.metadata.IMetadataTable;
import org.talend.core.model.metadata.MetadataTable;
import org.talend.core.model.metadata.builder.connection.SalesforceSchemaConnection;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.core.model.utils.TalendTextUtils;
import org.talend.core.utils.CsvArray;
import org.talend.designer.core.model.utils.emf.talendfile.ContextType;
import org.talend.repository.i18n.Messages;
import org.talend.repository.preview.ProcessDescription;
import org.talend.repository.preview.SalesforceSchemaBean;
import org.talend.repository.ui.swt.preview.ShadowProcessPreview;
import org.talend.repository.ui.swt.utils.AbstractSalesforceStepForm;
import org.talend.repository.ui.utils.ConnectionContextHelper;
import org.talend.repository.ui.utils.OtherConnectionContextUtils;
import org.talend.repository.ui.utils.ShadowProcessHelper;

/**
 * DOC YeXiaowei class global comment. Detailled comment <br/>
 * 
 */
public class SalesforceStep2Form extends AbstractSalesforceStepForm {

    private static Logger log = Logger.getLogger(SalesforceStep2Form.class);

    private LabelledText queryConditionText = null;

    private String defaultQueryString = ""; // 'name == talend'

    private Button previewButton = null;

    private Label previewInformationLabel = null;

    private ShadowProcessPreview salesforcePreviewProcess = null;

    private boolean readOnly;

    private UtilsButton cancelButton;

    private TableViewer moduleViewer = null;

    private final static int COLUMN_WIDTH = 60;

    private SWTUIThreadProcessor processor = new PreviewProcessor();

    /**
     * DOC YeXiaowei SalesforceStep2Form constructor comment.
     * 
     * @param parent
     * @param connectionItem
     */
    public SalesforceStep2Form(Composite parent, ConnectionItem connectionItem, SalesforceModuleParseAPI salesforceAPI,
            IMetadataContextModeManager contextModeManager) {
        super(parent, connectionItem, salesforceAPI);
        setConnectionItem(connectionItem);
        setContextModeManager(contextModeManager);
        setupForm(true);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.ui.swt.utils.AbstractForm#adaptFormToReadOnly()
     */
    @Override
    protected void adaptFormToReadOnly() {
        readOnly = isReadOnly();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.ui.swt.utils.AbstractForm#addFields()
     */
    @Override
    protected void addFields() {
        addQueryConditionGroup();
        addSalesforcePreviewGroup();
        if (!isInWizard()) {
            Composite compositeBottomButton = Form.startNewGridLayout(this, 2, false, SWT.CENTER, SWT.CENTER);
            cancelButton = new UtilsButton(compositeBottomButton, Messages.getString("CommonWizard.cancel"), WIDTH_BUTTON_PIXEL, //$NON-NLS-1$
                    HEIGHT_BUTTON_PIXEL);
        }
    }

    /**
     * DOC YeXiaowei Comment method "addSalesforcePreviewGroup".
     */
    private void addSalesforcePreviewGroup() {
        Group previewGroup = Form.createGroup(this, 2, "Salesforce Preview");

        previewButton = new Button(previewGroup, SWT.NONE);
        previewButton.setText(Messages.getString("FileStep2.refreshPreview")); //$NON-NLS-1$
        previewButton.setSize(WIDTH_BUTTON_PIXEL, HEIGHT_BUTTON_PIXEL);

        previewInformationLabel = new Label(previewGroup, SWT.NONE);
        previewInformationLabel
                .setText("                                                                                                                        "); //$NON-NLS-1$
        previewInformationLabel.setForeground(getDisplay().getSystemColor(SWT.COLOR_BLUE));

        salesforcePreviewProcess = new ShadowProcessPreview(previewGroup, null, 600, 200);
        salesforcePreviewProcess.newTablePreview();
    }

    /**
     * DOC YeXiaowei Comment method "addQueryConditionGroup".
     */
    private void addQueryConditionGroup() {
        Group queryConditionGroup = Form.createGroup(this, 2, "Browse data column and set query condition");

        queryConditionText = new LabelledText(queryConditionGroup, "Query Condition", true);
        queryConditionText.setText(defaultQueryString);

        Composite moduleViewerComposite = new Composite(queryConditionGroup, SWT.NONE);

        GridData data = new GridData(GridData.FILL_BOTH);
        data.horizontalSpan = 2;
        moduleViewerComposite.setLayoutData(data);

        moduleViewerComposite.setLayout(new GridLayout());

        Label label = new Label(moduleViewerComposite, SWT.NONE);
        label.setText("Salesforce Module field detail");
        label.setLayoutData(new GridData(GridData.FILL | GridData.CENTER));

        createModuleDetailViewer(moduleViewerComposite);
    }

    /**
     * DOC YeXiaowei Comment method "readAndSetModuleDetailContent".
     */
    private void readAndSetModuleDetailContent() {

        String moduleName = getConnection().getModuleName();

        if (moduleName == null || moduleName.equals("")) {
            return;
        }

        String webServiceUrl = getConnection().getWebServiceUrl();
        String userName = getConnection().getUserName();
        String password = getConnection().getPassword();
        if (isContextMode() && getContextModeManager() != null) {
            webServiceUrl = getContextModeManager().getOriginalValue(webServiceUrl);
            userName = getContextModeManager().getOriginalValue(userName);
            password = getContextModeManager().getOriginalValue(password);
        }

        IMetadataTable metadataTable = getMetadatasForSalesforce(webServiceUrl, userName, password, moduleName, true);

        List<IMetadataColumn> columns = metadataTable.getListColumns();

        moduleViewer.setInput(columns);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.ui.swt.utils.AbstractForm#addFieldsListeners()
     */
    @Override
    protected void addFieldsListeners() {
        queryConditionText.addModifyListener(new ModifyListener() {

            public void modifyText(ModifyEvent e) {
                if (!isContextMode()) {
                    if (checkFieldsValue()) {
                        getConnection().setQueryCondition(queryConditionText.getText());
                    }
                }
            }

        });

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.ui.swt.utils.AbstractForm#addUtilsButtonListeners()
     */
    @Override
    protected void addUtilsButtonListeners() {
        // Event PreviewButton
        previewButton.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(final SelectionEvent e) {
                processor.execute();
            }
        });

        if (cancelButton != null) {
            // Event CancelButton
            cancelButton.addSelectionListener(new SelectionAdapter() {

                @Override
                public void widgetSelected(final SelectionEvent e) {
                    getShell().close();
                }
            });
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.ui.swt.utils.AbstractForm#checkFieldsValue()
     */
    @Override
    protected boolean checkFieldsValue() {
        previewInformationLabel.setText("   " + Messages.getString("FileStep2.settingsIncomplete")); //$NON-NLS-1$ //$NON-NLS-2$
        updateStatus(IStatus.OK, null);
        previewButton.setEnabled(false);
        previewInformationLabel.setText(""); //$NON-NLS-1$
        previewButton.setEnabled(true);

        updateStatus(IStatus.OK, null);
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.ui.swt.utils.AbstractForm#initialize()
     */
    @Override
    protected void initialize() {

        String queryCondition = getConnection().getQueryCondition();
        if (queryCondition != null && !queryCondition.equals("")) {
            queryConditionText.setText(queryCondition);
        } else {
            queryConditionText.setText("");
        }

        checkFieldsValue();
    }

    @Override
    public void setVisible(boolean visible) {

        super.setVisible(visible);

        if (super.isVisible()) {
            if (!isContextMode()) {
                if ((!"".equals(getConnection().getWebServiceUrl())) && (getConnection().getModuleName() != null)) { //$NON-NLS-1$
                    readAndSetModuleDetailContent();
                    refreshPreview();
                }
            }

            if (isReadOnly() != readOnly) {
                adaptFormToReadOnly();
            }
            if (isContextMode()) {
                adaptFormToEditable();
            }
        }
    }

    /**
     * DOC YeXiaowei Comment method "refreshPreview".
     */
    private void refreshPreview() {
        processor.execute();
    }

    /**
     * 
     * DOC YeXiaowei SalesforceStep2Form class global comment. Detailled comment <br/>
     * 
     */
    class PreviewProcessor extends SWTUIThreadProcessor {

        String previewInformationLabelMsg = null;

        CsvArray csvArray = null;

        ProcessDescription processDescription = null;

        boolean firstRowIsCatption = false;

        @Override
        public boolean preProcessStart() {
            previewButton.setText(Messages.getString("FileStep2.stop"));

            clearPreview();
            String webServiceUrl = getConnection().getWebServiceUrl();
            SalesforceSchemaConnection originalValueConnection = null;
            if (isContextMode()) {
                boolean found = false;
                ContextType contextType = ConnectionContextHelper.getContextTypeForContextMode(getShell(), getConnection());
                if (contextType != null) {
                    if (getContextModeManager() != null) {
                        getContextModeManager().setSelectedContextType(contextType);
                        webServiceUrl = getContextModeManager().getOriginalValue(getConnection().getWebServiceUrl());
                        found = true;
                    }
                    originalValueConnection = OtherConnectionContextUtils.cloneOriginalValueSalesforceConnection(getConnection(),
                            contextType);
                }
                if (!found) {
                    webServiceUrl = null;
                }
            }

            if (webServiceUrl == null || webServiceUrl.equals("")) { //$NON-NLS-1$
                previewInformationLabel.setText(" Please reset Salesforce URL"); //$NON-NLS-1$ 
                return false;
            }

            if (!checkFieldsValue()) {
                previewInformationLabel.setText("   " + Messages.getString("FileStep2.settingsIncomplete")); //$NON-NLS-1$ //$NON-NLS-2$
                return false;
            }

            previewInformationLabel.setText("   " + Messages.getString("FileStep2.previewProgress")); //$NON-NLS-1$ //$NON-NLS-2$
            firstRowIsCatption = false;
            if (originalValueConnection == null) {
                originalValueConnection = getConnection();
            }
            processDescription = getProcessDescription(originalValueConnection);
            return true;
        }

        @Override
        public void nonUIProcessInThread() {
            // get the XmlArray width an adapt ProcessDescription
            try {
                // the web service url is used by tSalesforceInput, see 0004027: Studio crashes when clicking Next on
                // Step 3 of SF wizard
                processDescription.getSalesforceSchemaBean().setWebServerUrl(TSALESFORCE_INPUT_URL);
                csvArray = ShadowProcessHelper.getCsvArray(processDescription, "SALESFORCE_SCHEMA", true); //$NON-NLS-1$
                if (csvArray == null) {
                    previewInformationLabelMsg = "   " + Messages.getString("FileStep2.previewFailure"); //$NON-NLS-1$ //$NON-NLS-2$
                } else {
                    previewInformationLabelMsg = "   " + Messages.getString("FileStep2.previewIsDone"); //$NON-NLS-1$ //$NON-NLS-2$

                    // refresh TablePreview on this step
                    previewInformationLabelMsg = ""; //$NON-NLS-1$
                }
            } catch (CoreException ex) {
                setException(ex);
                previewInformationLabelMsg = "   " + Messages.getString("FileStep2.previewFailure"); //$NON-NLS-1$ //$NON-NLS-2$
            }
        }

        @Override
        public void updateUIInThreadIfThreadIsCanceled() {
            if (!previewInformationLabel.isDisposed()) {
                previewInformationLabel.setText("");
            }
        }

        @Override
        public void updateUIInThreadIfThreadIsNotCanceled() {
            if (previewInformationLabel.isDisposed()) {
                return;
            }
            previewInformationLabel.setText(previewInformationLabelMsg);
            if (getException() != null) {
                new ErrorDialogWidthDetailArea(getShell(), PID,
                        Messages.getString("FileStep2.previewFailure"), getException().getMessage()); //$NON-NLS-1$
            }
            if (csvArray != null) {
                salesforcePreviewProcess.refreshTablePreview(csvArray, firstRowIsCatption, processDescription);
            }
        }

        @Override
        public void updateUIInThreadIfThreadFinally() {
            if (!previewButton.isDisposed()) {
                previewButton.setText(Messages.getString("FileStep2.refreshPreview"));
                previewButton.setEnabled(true);

            }
        }

        @Override
        public void postProcessCancle() {
            previewButton.setEnabled(false);
        }
    }

    void clearPreview() {
        salesforcePreviewProcess.clearTablePreview();
    }

    /**
     * DOC YeXiaowei Comment method "getProcessDescription".
     * 
     * @return
     */
    private ProcessDescription getProcessDescription(SalesforceSchemaConnection originalValueConnection) {

        ProcessDescription processDescription = ShadowProcessHelper.getProcessDescription(originalValueConnection);

        SalesforceSchemaBean bean = new SalesforceSchemaBean();
        bean.setWebServerUrl(originalValueConnection.getWebServiceUrl());
        bean.setUserName(originalValueConnection.getUserName());
        bean.setPassword(originalValueConnection.getPassword());
        bean.setModuleName(originalValueConnection.getModuleName());
        bean.setQueryCondition(originalValueConnection.getQueryCondition());

        processDescription.setSalesforceSchemaBean(bean);

        IMetadataTable tableGet = getMetadatasForSalesforce(bean.getWebServerUrl(), bean.getUserName(), bean.getPassword(), bean
                .getModuleName(), false);

        List<IMetadataTable> tableSchema = new ArrayList<IMetadataTable>();
        IMetadataTable table = new MetadataTable();
        List<IMetadataColumn> schema = new ArrayList<IMetadataColumn>();

        for (IMetadataColumn column : tableGet.getListColumns()) {
            schema.add(column.clone());
        }

        table.setTableName("tSalesforceInput");
        table.setListColumns(schema);
        tableSchema.add(table);

        processDescription.setSchema(tableSchema);

        processDescription.setEncoding(TalendTextUtils.addQuotes("ISO-8859-15"));
        if (tableGet != null) {
            moduleViewer.getTable().clearAll();
            moduleViewer.setInput(tableGet.getListColumns());
            moduleViewer.refresh();
        }

        return processDescription;
    }

    /**
     * DOC YeXiaowei Comment method "createModuleDetailViewer".
     * 
     * @param moduleGroup
     */
    private void createModuleDetailViewer(Composite moduleGroup) {
        moduleViewer = new TableViewer(moduleGroup, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL | SWT.FULL_SELECTION);

        moduleViewer.getTable().setHeaderVisible(true);
        moduleViewer.getTable().setLinesVisible(true);

        moduleViewer.getTable().setLayoutData(new GridData(GridData.FILL_BOTH));

        moduleViewer.setContentProvider(new IStructuredContentProvider() {

            public void dispose() {

            }

            public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {

            }

            public Object[] getElements(Object inputElement) {
                if (inputElement instanceof List) {
                    return ((List) inputElement).toArray();
                }
                return null;
            }

        });

        moduleViewer.setLabelProvider(new ITableLabelProvider() {

            public Image getColumnImage(Object element, int columnIndex) {
                return null;
            }

            public String getColumnText(Object element, int columnIndex) {
                if (element instanceof IMetadataColumn) {
                    IMetadataColumn metadataColumn = (IMetadataColumn) element;
                    String title = null;
                    switch (columnIndex) {
                    case 0:
                        title = metadataColumn.getLabel();
                        break;
                    case 1:
                        title = metadataColumn.isKey() ? "true" : "false";
                        break;
                    case 2:
                        title = metadataColumn.getType();
                        break;
                    case 3:
                        title = metadataColumn.isNullable() ? "true" : "false";
                        break;
                    case 4:
                        title = metadataColumn.getPattern();
                        break;
                    case 5:
                        title = getStringFromInt(metadataColumn.getLength());
                        break;
                    case 6:
                        title = getStringFromInt(metadataColumn.getPrecision());
                        break;
                    case 7:
                        title = metadataColumn.getDefault();
                        break;
                    case 8:
                        title = metadataColumn.getComment();
                        break;
                    default:
                        title = "Other column title";
                    }

                    return title;
                }
                return null;
            }

            private String getStringFromInt(int x) {
                try {
                    return Integer.toString(x);
                } catch (Error e) {
                    return "";
                }
            }

            public void addListener(ILabelProviderListener listener) {

            }

            public void dispose() {

            }

            public boolean isLabelProperty(Object element, String property) {
                return false;
            }

            public void removeListener(ILabelProviderListener listener) {

            }

        });

        String[] titles = new String[] { "Column", "Key", "Type", "Nullable", "Data Pattern", "Length", "Precision", "Default",
                "Comment" };

        for (String title : titles) {
            int width = COLUMN_WIDTH;
            if (title.equals("Column") || title.equals("Data Pattern") || title.equals("Comment")) {
                width = COLUMN_WIDTH * 2;
            }
            createTableColumn(title, width);
        }

        // readAndSetModuleDetailContent();
    }

    private void createTableColumn(String title, int width) {
        TableColumn column = new TableColumn(moduleViewer.getTable(), SWT.NONE);
        column.setText(title);
        if (width < COLUMN_WIDTH || width > 400) {
            column.setWidth(COLUMN_WIDTH);
        } else {
            column.setWidth(width);
        }
    }

    @Override
    protected void adaptFormToEditable() {
        super.adaptFormToEditable();
        queryConditionText.setEditable(!isContextMode());
    }

    @Override
    protected void exportAsContext() {
        super.exportAsContext();
        if (getContextModeManager() != null) {
            getContextModeManager().setDefaultContextType(getConnection());
        }
    }

}
