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
package org.talend.repository.ui.wizards.metadata.connection.wsdl;

import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Plugin;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.talend.commons.ui.swt.dialogs.ErrorDialogWidthDetailArea;
import org.talend.commons.ui.swt.formtools.Form;
import org.talend.commons.ui.swt.thread.SWTUIThreadProcessor;
import org.talend.core.PluginChecker;
import org.talend.core.model.metadata.builder.connection.MetadataTable;
import org.talend.core.model.metadata.builder.connection.WSDLSchemaConnection;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.core.utils.CsvArray;
import org.talend.repository.i18n.Messages;
import org.talend.repository.preview.ProcessDescription;
import org.talend.repository.ui.swt.preview.ShadowProcessPreview;
import org.talend.repository.ui.swt.utils.AbstractForm;
import org.talend.repository.ui.utils.ShadowProcessHelper;
import org.talend.repository.ui.wizards.metadata.connection.ldap.BaseWidgetUtils;

/**
 * DOC qwei class global comment. Detailled comment
 */
public class WSDLSchemaStep1Form extends AbstractForm {

    private ConnectionItem connectionItem;

    private MetadataTable metadataTable;

    /** WSDL text. */
    private Text wsdlText;

    /** Method text. */
    private Text methodText;

    /** username text. */
    private Text userNameText;

    private Label useLabel;

    /** Password text. */
    private Text passWordText;

    private Label password;

    /** Need Auth */
    private Button needAuth;

    /** Parameters */
    private Table table;

    private Button buttonAdd, buttonRemove;

    private TableViewer valueTableViewer;

    private static final String VALUE_PROPERTY = "Value"; //$NON-NLS-1$

    private Group previewGroup;

    // private Button firstRowIsCaptionCheckbox;

    private Button previewButton;

    private Label previewInformationLabel;

    private ShadowProcessPreview wsdlPreview;

    /** http proxy */
    private Button useProxy;

    private Text proxyHost;

    private Label hostLabel;

    private Text proxyProt;

    private Label portLabel;

    private Text proxyUser;

    private Label userProLabel;

    private Text proxyPassword;

    private Label passwordProLabel;

    SWTUIThreadProcessor processor = new PreviewProcessor();

    private static Logger log = Logger.getLogger(WSDLSchemaStep1Form.class);

    /**
     * WSDLSchemaStep2Form constructor comment.
     * 
     * @param parent
     * @param connectionItem
     * @param metadataTable
     * @param tableNames
     */
    public WSDLSchemaStep1Form(Composite parent, ConnectionItem connectionItem, MetadataTable metadataTable, String[] tableNames) {
        super(parent, SWT.NONE, tableNames);
        this.connectionItem = connectionItem;
        this.metadataTable = metadataTable;
        setupForm();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.ui.swt.utils.AbstractForm#addFields()
     */
    @Override
    protected void addFields() {

        Composite composite = BaseWidgetUtils.createColumnContainer(this, 1, 1);
        BaseWidgetUtils.createSpacer(composite, 1);
        Group group = BaseWidgetUtils.createGroup(composite, Messages.getString("WSDLSchemaStep1Form.WSDLGroupParameter"), 1); //$NON-NLS-1$
        Composite groupComposite = BaseWidgetUtils.createColumnContainer(group, 4, 1);
        // ((GridData) group.getLayoutData()).heightHint = 260;
        BaseWidgetUtils.createLabel(groupComposite, Messages.getString("WSDLSchemaStep1Form.WSDLName"), 1); //$NON-NLS-1$
        wsdlText = BaseWidgetUtils.createText(groupComposite, Messages.getString("WSDLSchemaStep1Form.WSDLURL"), 3);
        needAuth = BaseWidgetUtils.createCheckbox(groupComposite, Messages.getString("WSDLSchemaStep1Form.NeedAuth"), 4);
        useLabel = BaseWidgetUtils.createLabel(groupComposite, Messages.getString("WSDLSchemaStep1Form.UserName"), 1);
        userNameText = BaseWidgetUtils.createText(groupComposite, "", 1);
        password = BaseWidgetUtils.createLabel(groupComposite, Messages.getString("WSDLSchemaStep1Form.Password"), 1);
        passWordText = BaseWidgetUtils.createText(groupComposite, "", 1);
        useProxy = BaseWidgetUtils.createCheckbox(groupComposite, Messages.getString("WSDLSchemaStep1Form.UseProxy"), 4);
        hostLabel = BaseWidgetUtils.createLabel(groupComposite, Messages.getString("WSDLSchemaStep1Form.ProxyHost"), 1);
        proxyHost = BaseWidgetUtils.createText(groupComposite, "", 1);
        portLabel = BaseWidgetUtils.createLabel(groupComposite, Messages.getString("WSDLSchemaStep1Form.ProxyPort"), 1);
        proxyProt = BaseWidgetUtils.createText(groupComposite, "", 1);
        userProLabel = BaseWidgetUtils.createLabel(groupComposite, Messages.getString("WSDLSchemaStep1Form.ProxyUser"), 1);
        proxyUser = BaseWidgetUtils.createText(groupComposite, "", 1);
        passwordProLabel = BaseWidgetUtils
                .createLabel(groupComposite, Messages.getString("WSDLSchemaStep1Form.ProxyPassword"), 1);
        proxyPassword = BaseWidgetUtils.createText(groupComposite, "", 1);
        BaseWidgetUtils.createLabel(groupComposite, Messages.getString("WSDLSchemaStep1Form.MethodName"), 1);
        methodText = BaseWidgetUtils.createText(groupComposite, "", 3);
        BaseWidgetUtils.createLabel(groupComposite, Messages.getString("WSDLSchemaStep1Form.Parameters"), 4);
        valueTableViewer = new TableViewer(groupComposite, SWT.BORDER | SWT.FULL_SELECTION | SWT.MULTI);
        TableViewerContentProvider provider = new TableViewerContentProvider();
        valueTableViewer.setContentProvider(provider);
        valueTableViewer.setLabelProvider(provider);
        valueTableViewer.setCellModifier(new ICellModifier() {

            /*
             * (non-Javadoc)
             * 
             * @see org.eclipse.jface.viewers.ICellModifier#canModify(java.lang.Object, java.lang.String)
             */
            public boolean canModify(Object element, String property) {
                return true;
            }

            /*
             * (non-Javadoc)
             * 
             * @see org.eclipse.jface.viewers.ICellModifier#getValue(java.lang.Object, java.lang.String)
             */
            public Object getValue(Object element, String property) {
                if (VALUE_PROPERTY.equals(property)) {
                    return element;
                }
                return ""; //$NON-NLS-1$
            }

            /*
             * (non-Javadoc)
             * 
             * @see org.eclipse.jface.viewers.ICellModifier#modify(java.lang.Object, java.lang.String, java.lang.Object)
             */
            public void modify(Object element, String property, Object value) {
                if (VALUE_PROPERTY.equals(property)) {
                    ArrayList list = (ArrayList) valueTableViewer.getInput();
                    int index = valueTableViewer.getTable().getSelectionIndex();
                    if (index > -1) {
                        list.set(index, value);
                    }
                    // var.setValue((String) value);
                }
                valueTableViewer.refresh();
            }

        });
        table = valueTableViewer.getTable();
        valueTableViewer.setCellEditors(new CellEditor[] { new TextCellEditor(table), new TextCellEditor(table) });
        valueTableViewer.setColumnProperties(new String[] { VALUE_PROPERTY });
        valueTableViewer.setInput(getConnection().getParameters());
        // valueTableViewer.setInput(new HashMap<ColumnValue,ColumnValue>());
        table.setHeaderVisible(true);

        GridData gd = new GridData(GridData.FILL_BOTH);
        gd.horizontalSpan = 4;
        gd.heightHint = 50;
        table.setLayoutData(gd);
        final TableColumn valueColumn = new TableColumn(table, SWT.NONE);
        valueColumn.setWidth(200);
        valueColumn.setText(Messages.getString("WSDLSchemaStep1Form.ParColumnValue")); //$NON-NLS-1$
        Composite buttonPart = new Composite(group, SWT.NONE);
        buttonPart.setLayout(new GridLayout(2, false));
        buttonPart.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING));
        buttonAdd = new Button(buttonPart, SWT.NONE);
        buttonAdd.setText(Messages.getString("WSDLSchemaStep1Form.ParameterAdd")); //$NON-NLS-1$
        buttonAdd.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING));
        buttonRemove = new Button(buttonPart, SWT.NONE);
        buttonRemove.setText(Messages.getString("WSDLSchemaStep1Form.ParameterRemove")); //$NON-NLS-1$
        buttonRemove.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING));
        addGroupFileViewer(this, 700, 135);

    }

    /**
     * DOC qwei Comment method "addadvancedWSDLExplorer".
     */
    private void addadvancedWSDLExplorer(final Composite parent) {

        if (!PluginChecker.isWSDLPluginLoaded()) {
            return;
        }

        Button wsdlButton = new Button(parent, SWT.NONE);
        wsdlButton.setText("Open Web Services Explorer");
        wsdlButton.setAlignment(SWT.RIGHT);
        wsdlButton.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                Plugin plugin = PluginChecker.getWSDLPlugin();
                if (plugin instanceof IWSDLExplorerAccessor) {
                    IWSDLExplorerAccessor accessor = (IWSDLExplorerAccessor) plugin;
//                    Shell shell = new Shell(getParent().getShell().getDisplay());
                    accessor.openWSExplorer(getShell());
                }
            }
        });
    }

    /**
     * add Field to Group File Viewer.
     * 
     * @param parent
     * @param form
     * @param width
     * @param height
     */
    private void addGroupFileViewer(final Composite parent, final int width, int height) {
        // composite Delimited File Preview
        previewGroup = Form.createGroup(parent, 1, Messages.getString("FileStep2.groupPreview"), height); //$NON-NLS-1$
        Composite compositeDelimitedFilePreviewButton = Form.startNewDimensionnedGridLayout(previewGroup, 4, width,
                HEIGHT_BUTTON_PIXEL);
        height = height - HEIGHT_BUTTON_PIXEL - 15;

        // Delimited File Preview Info
        // firstRowIsCaptionCheckbox = new Button(compositeDelimitedFilePreviewButton, SWT.CHECK);
        // firstRowIsCaptionCheckbox.setText(Messages.getString("FileStep2.firstRowsIsCaption")); //$NON-NLS-1$
        // firstRowIsCaptionCheckbox.setAlignment(SWT.LEFT);
        previewButton = new Button(compositeDelimitedFilePreviewButton, SWT.NONE);
        previewButton.setText(Messages.getString("FileStep2.refreshPreview")); //$NON-NLS-1$
        previewButton.setSize(WIDTH_BUTTON_PIXEL, HEIGHT_BUTTON_PIXEL);
        addadvancedWSDLExplorer(compositeDelimitedFilePreviewButton);

        // simple space
        new Label(compositeDelimitedFilePreviewButton, SWT.NONE);
        // Information Label
        previewInformationLabel = new Label(compositeDelimitedFilePreviewButton, SWT.NONE);
        previewInformationLabel
                .setText("                                                                                                                        "); //$NON-NLS-1$
        previewInformationLabel.setForeground(getDisplay().getSystemColor(SWT.COLOR_BLUE));

        Composite compositeDelimitedFilePreview = Form.startNewDimensionnedGridLayout(previewGroup, 1, width, height);

        // Delimited File Preview
        wsdlPreview = new ShadowProcessPreview(compositeDelimitedFilePreview, null, width, height - 10);
        wsdlPreview.newTablePreview();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.ui.swt.utils.AbstractForm#addFieldsListeners()
     */
    @Override
    protected void addFieldsListeners() {

        needAuth.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                checkFieldsValue();
                setNeedAuthEnable(needAuth.getSelection());
                getConnection().setNeedAuth(needAuth.getSelection());
            }

        });
        useProxy.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                checkFieldsValue();
                setUseProxyEnable(useProxy.getSelection());
                getConnection().setUseProxy(useProxy.getSelection());
            }

        });
        wsdlText.addModifyListener(new ModifyListener() {

            public void modifyText(ModifyEvent e) {
                checkFieldsValue();
                getConnection().setWSDL(wsdlText.getText());
            }

        });

        proxyHost.addModifyListener(new ModifyListener() {

            public void modifyText(ModifyEvent e) {
                checkFieldsValue();
                getConnection().setProxyHost(proxyHost.getText());

            }

        });
        proxyProt.addModifyListener(new ModifyListener() {

            public void modifyText(ModifyEvent e) {
                checkFieldsValue();
                getConnection().setProxyPort(proxyProt.getText());

            }

        });
        proxyUser.addModifyListener(new ModifyListener() {

            public void modifyText(ModifyEvent e) {
                checkFieldsValue();
                getConnection().setProxyUser(proxyUser.getText());
            }

        });
        proxyPassword.addModifyListener(new ModifyListener() {

            public void modifyText(ModifyEvent e) {
                checkFieldsValue();
                getConnection().setProxyPassword(proxyPassword.getText());
            }

        });
        methodText.addModifyListener(new ModifyListener() {

            public void modifyText(ModifyEvent e) {
                checkFieldsValue();
                getConnection().setMethodName(methodText.getText());
            }

        });
        userNameText.addModifyListener(new ModifyListener() {

            public void modifyText(ModifyEvent e) {
                checkFieldsValue();
                getConnection().setUserName(userNameText.getText());
            }

        });
        passWordText.addModifyListener(new ModifyListener() {

            public void modifyText(ModifyEvent e) {
                checkFieldsValue();
                getConnection().setPassword(passWordText.getText());
            }

        });
        buttonAdd.addMouseListener(new MouseAdapter() {

            /*
             * (non-Javadoc)
             * 
             * @see org.eclipse.swt.events.MouseAdapter#mouseUp(org.eclipse.swt.events.MouseEvent)
             */
            @Override
            public void mouseUp(MouseEvent e) {
                String unName = "newLine_";
                ArrayList hashmap = getConnection().getParameters();
                hashmap.add(unName + hashmap.size());
                // for (ColumnValue columnValue : input) {
                // hashmap.put(columnValue, columnValue.getValue());
                // }
                getConnection().setParameters(hashmap);
                valueTableViewer.setInput(hashmap);
                valueTableViewer.refresh();
            }
        });
        buttonRemove.addMouseListener(new MouseAdapter() {

            /*
             * (non-Javadoc)
             * 
             * @see org.eclipse.swt.events.MouseAdapter#mouseUp(org.eclipse.swt.events.MouseEvent)
             */
            @Override
            public void mouseUp(MouseEvent e) {
                ISelection selection = valueTableViewer.getSelection();
                ArrayList hashmap = getConnection().getParameters();
                if (!selection.isEmpty() && selection instanceof StructuredSelection) {
                    Object[] vars = ((StructuredSelection) selection).toArray();
                    for (Object var : vars) {
                        hashmap.remove(var);
                        valueTableViewer.refresh();
                    }
                } else if (!hashmap.isEmpty()) {
                    hashmap.remove(hashmap.size() - 1);
                    valueTableViewer.refresh();
                }
                // HashMap<ColumnValue, String> hashmap = new HashMap<ColumnValue, String>();
                // for (ColumnValue columnValue : list) {
                // hashmap.put(columnValue, columnValue.getValue());
                // }
                getConnection().setParameters(hashmap);
            }
        });

        previewButton.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(final SelectionEvent e) {
                refreshPreview();
                updateStatus(IStatus.OK, null);
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
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.ui.swt.utils.AbstractForm#initialize()
     */
    @Override
    protected void initialize() {
        String wsdlUrl = getConnection().getWSDL();
        this.wsdlText.setText(wsdlUrl == null ? "" : wsdlUrl);
        boolean needAuth2 = getConnection().isNeedAuth();
        String userName = getConnection().getUserName();
        this.userNameText.setText(userName == null ? "" : userName);
        String password2 = getConnection().getPassword();
        this.passWordText.setText(password2 == null ? "" : password2);
        setNeedAuthEnable(needAuth2);
        boolean useproxy = getConnection().isUseProxy();
        setUseProxyEnable(useproxy);
        String method = getConnection().getMethodName();
        this.methodText.setText(method == null ? "" : method);
        ArrayList hashparameter = getConnection().getParameters();
        // Object[] objs = hashparameter.values().toArray();
        // LinkedList<ColumnValue> list = new LinkedList<ColumnValue>();
        // for (Object object : objs) {
        // list.add((ColumnValue) object);
        // }
        this.valueTableViewer.setInput(hashparameter);
        checkFieldsValue();

    }

    private void setNeedAuthEnable(boolean b) {
        this.needAuth.setSelection(b);
        this.userNameText.setEnabled(b);
        this.useLabel.setEnabled(b);
        this.passWordText.setEnabled(b);
        this.password.setEnabled(b);
    }

    private void setUseProxyEnable(boolean b) {
        this.useProxy.setSelection(b);
        this.useProxy.setSelection(b);
        this.hostLabel.setEnabled(b);
        this.proxyHost.setEnabled(b);
        this.portLabel.setEnabled(b);
        this.proxyProt.setEnabled(b);
        this.proxyUser.setEnabled(b);
        this.userProLabel.setEnabled(b);
        this.proxyPassword.setEnabled(b);
        this.passwordProLabel.setEnabled(b);
    }

    /**
     * refreshPreview use ShadowProcess to refresh the preview.
     */
    void refreshPreview() {
        processor.execute();

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.ui.swt.utils.AbstractForm#adaptFormToReadOnly()
     */
    @Override
    protected void adaptFormToReadOnly() {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.ui.swt.utils.AbstractForm#checkFieldsValue()
     */
    @Override
    protected boolean checkFieldsValue() {
        // TODO Auto-generated method stub
        if (wsdlText.getText() == null || wsdlText.getText().equals("")) {
            updateStatus(IStatus.ERROR, "WSDL URL must be specified."); //$NON-NLS-1$
            return false;
        } else if (methodText.getText() == null || methodText.getText().equals("")) {
            updateStatus(IStatus.ERROR, "Method must be specified."); //$NON-NLS-1$
            return false;
        } else if (needAuth.getSelection() && (userNameText.getText() == null || userNameText.getText().equals(""))) {
            updateStatus(IStatus.ERROR, "User Name must be specified."); //$NON-NLS-1$
            return false;
        } else if (needAuth.getSelection() && (passWordText.getText() == null || passWordText.getText().equals(""))) {
            updateStatus(IStatus.ERROR, "Password must be specified."); //$NON-NLS-1$
            return false;
        } else if (useProxy.getSelection() && (proxyHost.getText() == null || proxyHost.getText().equals(""))) {
            updateStatus(IStatus.ERROR, "Proxy host must be specified."); //$NON-NLS-1$
            return false;
        } else if (useProxy.getSelection() && (proxyProt.getText() == null || proxyProt.getText().equals(""))) {
            updateStatus(IStatus.ERROR, "Proxy port must be specified."); //$NON-NLS-1$
            return false;
        } else if (useProxy.getSelection() && (proxyUser.getText() == null || proxyUser.getText().equals(""))) {
            updateStatus(IStatus.ERROR, "Proxy user must be specified."); //$NON-NLS-1$
            return false;
        } else if (useProxy.getSelection() && (proxyPassword.getText() == null || proxyPassword.getText().equals(""))) {
            updateStatus(IStatus.ERROR, "Proxy password must be specified."); //$NON-NLS-1$
            return false;
        } else {
            updateStatus(IStatus.ERROR, null);
            return true;
        }

    }

    /**
     * Subclass of SWTUIThreadProcessor to process the preview event. <br/>
     * 
     * $Id: DelimitedFileStep2Form.java 4837 2007-07-27 05:40:31Z bqian $
     * 
     */
    class PreviewProcessor extends SWTUIThreadProcessor {

        CsvArray csvArray = null;

        ProcessDescription processDescription = null;

        public boolean preProcessStart() {
            previewButton.setText(Messages.getString("FileStep2.stop"));

            clearPreview();

            // if incomplete settings, , the process don't be executed
            if (!checkFieldsValue()) {
                previewInformationLabel.setText(" " + Messages.getString("FileStep2.settingsIncomplete")); //$NON-NLS-1$
                //$NON-NLS-2$
                if (!previewButton.isDisposed()) {
                    previewButton.setText(Messages.getString("FileStep2.refreshPreview"));
                    previewButton.setEnabled(true);
                }
                return false;
            }

            previewInformationLabel.setText(" " + Messages.getString("FileStep2.previewProgress")); //$NON-NLS-1$
            //$NON-NLS-2$
            processDescription = getProcessDescription();
            return true;
        }

        public void nonUIProcessInThread() {
            // get the CsvArray width an adapt ProcessDescription
            try {
                csvArray = ShadowProcessHelper.getCsvArray(processDescription, "WSDL_SCHEMA"); //$NON-NLS-1$

            } catch (CoreException e) {
                setException(e);
                log.error(Messages.getString("FileStep2.previewFailure") + " " + e.getMessage()); //$NON-NLS-1$ //$NON-NLS-2$
            }
        }

        public void updateUIInThreadIfThreadIsNotCanceled() {
            if (previewInformationLabel.isDisposed()) {
                return;
            }
            if (getException() != null) {
                previewInformationLabel.setText(" " + Messages.getString("FileStep2.previewFailure")); //$NON-NLS-1$
                //$NON-NLS-2$
                new ErrorDialogWidthDetailArea(getShell(), PID,
                        Messages.getString("FileStep2.previewFailure"), getException().getMessage()); //$NON-NLS-1$
                return;
            }

            if (csvArray == null || csvArray.getRows() == null || csvArray.getRows().size() == 0) {
                previewInformationLabel.setText(" " + Messages.getString("FileStep2.previewFailure")); //$NON-NLS-1$
                //$NON-NLS-2$
                MessageDialog.openError(getShell(), "Error", "Preview refresh failed, please check attributes and filter.");
            } else {
                previewInformationLabel.setText(" " + Messages.getString("FileStep2.previewIsDone")); //$NON-NLS-1$
                //$NON-NLS-2$

                // refresh TablePreview on this step
                try {
                    wsdlPreview.refreshTablePreview(csvArray, false, processDescription);
                } catch (Exception e) {
                    MessageDialog.openError(getShell(), "Error", "Preview refresh failed, please check attributes and filter.");
                }
                previewInformationLabel.setText(""); //$NON-NLS-1$
            }
        }

        public void updateUIInThreadIfThreadIsCanceled() {
            if (!previewInformationLabel.isDisposed()) {
                previewInformationLabel.setText("");
            }
        }

        public void updateUIInThreadIfThreadFinally() {
            if (!previewButton.isDisposed()) {
                previewButton.setText(Messages.getString("FileStep2.refreshPreview"));
                previewButton.setEnabled(true);
            }
        }

        public void postProcessCancle() {
            previewButton.setEnabled(false);
        }
    }

    /**
     * clear the table preview.
     */
    void clearPreview() {
        wsdlPreview.clearTablePreview();
    }

    private ProcessDescription getProcessDescription() {

        ProcessDescription processDescription = ShadowProcessHelper.getProcessDescription(getConnection());
        return processDescription;
    }

    /**
     * Administrator Comment method "getConnection".
     * 
     * @return
     */
    private WSDLSchemaConnection getConnection() {
        return (WSDLSchemaConnection) this.connectionItem.getConnection();
    }

}
