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
package org.talend.repository.ui.wizards.metadata.connection.files.salesforce;

import java.lang.reflect.InvocationTargetException;

import org.apache.commons.lang.ArrayUtils;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.internal.dialogs.EventLoopProgressMonitor;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.exception.MessageBoxExceptionHandler;
import org.talend.commons.ui.swt.dialogs.ProgressDialog;
import org.talend.commons.ui.swt.formtools.Form;
import org.talend.commons.ui.swt.formtools.LabelledCombo;
import org.talend.commons.ui.swt.formtools.LabelledText;
import org.talend.commons.ui.swt.formtools.UtilsButton;
import org.talend.core.CorePlugin;
import org.talend.core.model.metadata.IMetadataContextModeManager;
import org.talend.core.model.process.IElementParameter;
import org.talend.core.model.process.INode;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.repository.i18n.Messages;
import org.talend.repository.ui.swt.utils.AbstractSalesforceStepForm;

import com.sforce.soap.enterprise.DescribeGlobalResult;

/**
 * DOC YeXiaowei class global comment. Detailled comment <br/>
 * 
 */
public class SalesforceStep1Form extends AbstractSalesforceStepForm {

    private String endPoint = null;

    private String username = null;

    private String pwd = null;

    private String batchSize = null;

    private LabelledText webServiceUrlText = null;

    private LabelledText userNameText = null;

    private LabelledText passwordText = null;

    private LabelledText batchSizeText = null;

    private LabelledCombo moduleNameCombo = null;

    private UtilsButton cancelButton = null;

    /*
     * 
     */
    private Button useProxyBtn = null;

    private LabelledText proxyHostText = null;

    private LabelledText proxyPortText = null;

    private LabelledText proxyUsernameText = null;

    private LabelledText proxyPasswordText = null;

    private Button checkButton = null;

    private boolean loginOk = false;

    private boolean readOnly;

    private final char pwdEhcoChar = '*';

    // private List<String> moduleNames = null;

    private LabelledCombo customModuleCombo = null;

    // private CCombo customModuleCombo;

    // private Button clearButton;

    private Button useCostomModuleButton;

    /**
     * DOC YeXiaowei SalesforceStep1Form constructor comment.
     * 
     * @param parent
     * @param connectionItem
     * @param existingNames
     */
    public SalesforceStep1Form(Composite parent, ConnectionItem connectionItem, String[] existingNames,
            SalesforceModuleParseAPI salesforceAPI, IMetadataContextModeManager contextModeManager) {
        super(parent, connectionItem, existingNames, salesforceAPI);
        setConnectionItem(connectionItem);
        setContextModeManager(contextModeManager);
        setupForm();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.ui.swt.utils.AbstractForm#adaptFormToReadOnly()
     */
    @Override
    protected void adaptFormToReadOnly() {
        readOnly = isReadOnly();
        updateStatus(IStatus.OK, ""); //$NON-NLS-1$
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.ui.swt.utils.AbstractForm#addFields()
     */
    @Override
    protected void addFields() {

        Group group = Form.createGroup(this, 3, Messages.getString("SalesforceStep1Form.SalesforceParam")); //$NON-NLS-1$

        GridData data = new GridData(GridData.FILL_HORIZONTAL);
        group.setLayoutData(data);

        webServiceUrlText = new LabelledText(group, Messages.getString("SalesforceStep1Form.webURL"), 2, true); //$NON-NLS-1$

        userNameText = new LabelledText(group, Messages.getString("SalesforceStep1Form.Username"), 2); //$NON-NLS-1$

        passwordText = new LabelledText(group, Messages.getString("SalesforceStep1Form.Password"), 2); //$NON-NLS-1$
        passwordText.getTextControl().setEchoChar(pwdEhcoChar);

        batchSizeText = new LabelledText(group, Messages.getString("SalesforceStep1Form.BatchSize"), 2); //$NON-NLS-1$

        Group proxyGroup = Form.createGroup(group, 4, Messages.getString("SalesforceStep1Form.SocksProxyParam")); //$NON-NLS-1$
        GridData layoutData = new GridData(GridData.FILL_HORIZONTAL);
        layoutData.horizontalSpan = 3;
        proxyGroup.setLayoutData(layoutData);
        useProxyBtn = new Button(proxyGroup, SWT.CHECK);
        layoutData = new GridData(GridData.FILL_HORIZONTAL);
        layoutData.horizontalSpan = 4;
        useProxyBtn.setLayoutData(layoutData);
        useProxyBtn.setText(Messages.getString("SalesforceStep1Form.EnabledProxy")); //$NON-NLS-1$
        proxyHostText = new LabelledText(proxyGroup, Messages.getString("SalesforceStep1Form.ProxyHost")); //$NON-NLS-1$
        proxyPortText = new LabelledText(proxyGroup, Messages.getString("SalesforceStep1Form.ProxyPort")); //$NON-NLS-1$
        proxyUsernameText = new LabelledText(proxyGroup, Messages.getString("SalesforceStep1Form.ProxyUsername")); //$NON-NLS-1$
        proxyPasswordText = new LabelledText(proxyGroup, Messages.getString("SalesforceStep1Form.ProxyPassword")); //$NON-NLS-1$
        enableProxyParameters(false);

        moduleNameCombo = new LabelledCombo(
                group,
                Messages.getString("SalesforceStep1Form.standardObjects"), Messages.getString("SalesforceStep1Form.selectModuleName"), //$NON-NLS-1$ //$NON-NLS-2$
                null, 2, false);

        initModuleNames();

        new Label(group, SWT.NONE); // Pachlaer
        useCostomModuleButton = new Button(group, SWT.CHECK);
        useCostomModuleButton.setText(Messages.getString("SalesforceStep1Form.fetchModuleList")); //$NON-NLS-1$
        new Label(group, SWT.NONE); // Pachlaer

        customModuleCombo = new LabelledCombo(
                group,
                Messages.getString("SalesforceStep1Form.customModule"), Messages.getString("SalesforceStep1Form.selectCustomModule"), null, 1, false); //$NON-NLS-1$ //$NON-NLS-2$

        GridData gd = new GridData();
        gd.widthHint = 140;
        customModuleCombo.getCombo().setLayoutData(gd);

        GridData cdata = new GridData(GridData.FILL);
        cdata.horizontalAlignment = SWT.LEFT;

        checkButton = new Button(group, SWT.NONE);
        checkButton.setText(Messages.getString("SalesforceStep1Form.checkLogin")); //$NON-NLS-1$
        checkButton.setEnabled(false);

        GridData wd = new GridData();
        wd.horizontalSpan = 3;
        wd.horizontalAlignment = GridData.CENTER;

        checkButton.setLayoutData(wd);

        if (!isInWizard()) {
            Composite compositeBottomButton = Form.startNewGridLayout(this, 2, false, SWT.CENTER, SWT.CENTER);
            cancelButton = new UtilsButton(compositeBottomButton, Messages.getString("CommonWizard.cancel"), WIDTH_BUTTON_PIXEL, //$NON-NLS-1$
                    HEIGHT_BUTTON_PIXEL);
        }

        addUtilsButtonListeners();

    }

    /**
     * DOC YeXiaowei Comment method "setCheckEnable".
     */
    private void setCheckEnable() {
        checkButton.setEnabled(isValueValid(webServiceUrlText.getText()) && isValueValid(userNameText.getText())
                && isValueValid(passwordText.getText()));
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.ui.swt.utils.AbstractForm#addFieldsListeners()
     */
    @Override
    protected void addFieldsListeners() {

        webServiceUrlText.addModifyListener(new ModifyListener() {

            public void modifyText(ModifyEvent e) {
                if (!isContextMode()) {
                    loginOk = false;
                    checkFieldsValue();
                    getConnection().setWebServiceUrl(webServiceUrlText.getText());
                    setCheckEnable();
                }
            }
        });

        userNameText.addModifyListener(new ModifyListener() {

            public void modifyText(ModifyEvent e) {
                if (!isContextMode()) {
                    loginOk = false;
                    checkFieldsValue();
                    getConnection().setUserName(userNameText.getText());
                    setCheckEnable();
                }
            }
        });

        passwordText.addModifyListener(new ModifyListener() {

            public void modifyText(ModifyEvent e) {
                if (!isContextMode()) {
                    loginOk = false;
                    checkFieldsValue();
                    getConnection().setPassword(passwordText.getText());
                    setCheckEnable();
                }
            }
        });
        batchSizeText.addModifyListener(new ModifyListener() {

            public void modifyText(ModifyEvent e) {
                if (!isContextMode()) {
                    loginOk = false;
                    checkFieldsValue();
                    getConnection().setBatchSize(batchSizeText.getText());
                    setCheckEnable();
                }
            }
        });
        useProxyBtn.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {

                checkFieldsValue();
                enableProxyParameters(useProxyBtn.getSelection());
                getConnection().setUseProxy(useProxyBtn.getSelection());
            }

        });
        proxyHostText.addModifyListener(new ModifyListener() {

            public void modifyText(ModifyEvent e) {
                if (!isContextMode()) {
                    checkFieldsValue();
                    getConnection().setProxyHost(proxyHostText.getText());
                }

            }

        });
        proxyPortText.addModifyListener(new ModifyListener() {

            public void modifyText(ModifyEvent e) {
                if (!isContextMode()) {
                    checkFieldsValue();
                    getConnection().setProxyPort(proxyPortText.getText());
                }

            }

        });
        proxyUsernameText.addModifyListener(new ModifyListener() {

            public void modifyText(ModifyEvent e) {
                if (!isContextMode()) {
                    checkFieldsValue();
                    getConnection().setProxyUsername(proxyUsernameText.getText());
                }
            }

        });
        proxyPasswordText.addModifyListener(new ModifyListener() {

            public void modifyText(ModifyEvent e) {
                if (!isContextMode()) {
                    checkFieldsValue();
                    getConnection().setProxyPassword(proxyPasswordText.getText());
                }
            }

        });
        moduleNameCombo.addModifyListener(new ModifyListener() {

            public void modifyText(ModifyEvent e) {
                checkFieldsValue();
                getConnection().setModuleName(moduleNameCombo.getText());
            }
        });

        checkButton.addSelectionListener(new SelectionAdapter() {

            /*
             * (non-Javadoc)
             * 
             * @see org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse.swt.events.SelectionEvent)
             */
            @Override
            public void widgetSelected(SelectionEvent e) {
                if (!isContextMode()) {
                    checkFieldsValue();
                }
                testSalesforceLogin();
                if (useProxyBtn.getSelection()) {
                    loginOk = checkSalesfoceLogin(endPoint, username, pwd, proxyHostText.getText(), proxyPortText.getText(),
                            proxyUsernameText.getText(), proxyPasswordText.getText());
                } else {
                    loginOk = checkSalesfoceLogin(endPoint, username, pwd, null, null, null, null);
                }

                if (useCostomModuleButton.getSelection()) {
                    getConnection().setModuleName(customModuleCombo.getText().trim());
                    // appendCustomModule(customModuleCombo.getText().trim());
                    // qli modified to fix the bug 6627.
                    // setCustomModuleCombo(getConnection().getModuleName());
                }
                checkFieldsValue();
            }
        });

        customModuleCombo.addModifyListener(new ModifyListener() {

            public void modifyText(ModifyEvent e) {
                checkFieldsValue();
                getConnection().setModuleName(customModuleCombo.getText());
            }
        });

        useCostomModuleButton.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(final SelectionEvent e) {
                connectFromCustomModuleName();
                moduleNameCombo.setEnabled(!useCostomModuleButton.getSelection());
                customModuleCombo.setEnabled(useCostomModuleButton.getSelection());
                getConnection().setUseCustomModuleName(useCostomModuleButton.getSelection());
                if (useCostomModuleButton.getSelection()) {
                    getConnection().setModuleName(customModuleCombo.getText());
                }
                checkFieldsValue();
            }
        });

    }

    private void enableProxyParameters(boolean enable) {
        proxyHostText.setEnabled(enable);
        proxyPortText.setEnabled(enable);
        proxyUsernameText.setEnabled(enable);
        proxyPasswordText.setEnabled(enable);
    }

    Object[] modulename = null;

    private void initModuleNames() {

        INode node = getSalesforceNode();

        if (node == null) {
            moduleNameCombo.add(Messages.getString("SalesforceStep1Form.account")); //$NON-NLS-1$
        } else {
            IElementParameter modulesNameParam = node.getElementParameter("MODULENAME"); //$NON-NLS-1$
            modulename = modulesNameParam.getListItemsValue();

            if (modulename == null || modulename.length <= 0) {
                return;
            }
            moduleNameCombo.removeAll(); // First clear

            for (Object module : modulename) {
                moduleNameCombo.add(module.toString());
            }
            moduleNameCombo.select(0);
        }
    }

    private void preparModuleInit() {
        /*
         * prepare to ininCustomModule
         */
        endPoint = webServiceUrlText.getText();
        username = userNameText.getText();
        pwd = passwordText.getText();
        batchSize = batchSizeText.getText();

        if (isContextMode() && getContextModeManager() != null) {
            endPoint = getContextModeManager().getOriginalValue(endPoint);
            username = getContextModeManager().getOriginalValue(username);
            pwd = getContextModeManager().getOriginalValue(pwd);
        }
        // TSALESFORCE_INPUT_URL is only used by tSalesForceInput, the logic doesn't work with this url
        if (endPoint.equals(TSALESFORCE_INPUT_URL)) {
            endPoint = DEFAULT_WEB_SERVICE_URL;
        }
    }

    private void connectFromCustomModuleName() {
        ProgressDialog progressDialog = new ProgressDialog(Display.getCurrent().getActiveShell(), 0) {

            private IProgressMonitor monitorWrap;

            @SuppressWarnings("restriction")
            @Override
            public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
                monitorWrap = new EventLoopProgressMonitor(monitor);
                monitorWrap.beginTask(Messages.getString("SalesforceStep1Form.connection"), IProgressMonitor.UNKNOWN); //$NON-NLS-1$
                testSalesforceLogin();
                loginOk = toCheckSalesfoceLogin(endPoint, username, pwd);
                preparModuleInit();
                String[] types = null;
                DescribeGlobalResult describeGlobalResult = null;
                monitorWrap.worked(50);

                try {
                    describeGlobalResult = describeGlobal();
                    types = describeGlobalResult.getTypes();
                    customModuleCombo.removeAll();
                    for (int i = 0; i < types.length; i++) {
                        if (!ArrayUtils.contains(modulename, types[i])) {
                            customModuleCombo.add(types[i]);
                        }
                    }
                    monitorWrap.done();
                } catch (Exception ex) {
                    ExceptionHandler.process(ex);
                }
                customModuleCombo.select(0);

            }

        };

        try {
            progressDialog.executeProcess();
        } catch (InvocationTargetException e) {
            ExceptionHandler.process(e);
            return;
        } catch (Exception e) {
            MessageBoxExceptionHandler.process(e);
            return;
        }
    }

    private void testSalesforceLogin() {
        endPoint = webServiceUrlText.getText();
        username = userNameText.getText();
        pwd = passwordText.getText();
        if (isContextMode() && getContextModeManager() != null) {
            endPoint = getContextModeManager().getOriginalValue(endPoint);
            username = getContextModeManager().getOriginalValue(username);
            pwd = getContextModeManager().getOriginalValue(pwd);
        }
        // TSALESFORCE_INPUT_URL is only used by tSalesForceInput, the logic doesn't work with this url
        if (endPoint.equals(TSALESFORCE_INPUT_URL)) {
            endPoint = DEFAULT_WEB_SERVICE_URL;
        }
    }

    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);

        if (isReadOnly() != readOnly) {
            adaptFormToReadOnly();
        }
        if (visible) {
            initialize();
            adaptFormToEditable();
        }
        if (!isContextMode()) {
            checkFieldsValue();
            setCheckEnable();
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.ui.swt.utils.AbstractForm#addUtilsButtonListeners()
     */
    @Override
    protected void addUtilsButtonListeners() {
        if (!isInWizard()) {
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

        if (!isValueValid(webServiceUrlText.getText())) {
            updateStatus(IStatus.ERROR, "Your must give Webserver url for using Salesforce service"); //$NON-NLS-1$
            return false;
        }

        if (!isValueValid(userNameText.getText())) {
            updateStatus(IStatus.ERROR, "Your must give user name for using Salesforce service"); //$NON-NLS-1$
            return false;
        }

        if (!isValueValid(passwordText.getText())) {
            updateStatus(IStatus.ERROR, "Your must give password for using Salesforce service"); //$NON-NLS-1$
            return false;
        }

        if (!useCostomModuleButton.getSelection()) {
            if (!isValueValid(moduleNameCombo.getText())) {
                updateStatus(IStatus.ERROR, "Your must give module for using Salesforce service"); //$NON-NLS-1$
                return false;
            }
        } else {
            if (!isValueValid(customModuleCombo.getText())) {
                updateStatus(IStatus.ERROR, "Your must give module for using Salesforce service"); //$NON-NLS-1$
                return false;
            }
        }

        if (!loginOk) {
            updateStatus(IStatus.ERROR, "Click Check Login to make sure that URL, username, password are correct."); //$NON-NLS-1$
            return false;
        }

        updateStatus(IStatus.OK, null);
        return true;
    }

    private boolean isValueValid(String value) {
        return value != null && !value.equals(""); //$NON-NLS-1$
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.ui.swt.utils.AbstractForm#initialize()
     */
    @Override
    protected void initialize() {

        if (getConnection() == null) {
            return;
        }

        if (getConnection().getWebServiceUrl() != null && !getConnection().getWebServiceUrl().equals("")) { //$NON-NLS-1$
            webServiceUrlText.setText(getConnection().getWebServiceUrl());
        }

        if (getConnection().getWebServiceUrl() == null || getConnection().getWebServiceUrl().equals("")) { //$NON-NLS-1$
            getConnection().setWebServiceUrl(TSALESFORCE_INPUT_URL);
        } // Give a default value

        if (webServiceUrlText.getText() == null || webServiceUrlText.getText().equals("")) { //$NON-NLS-1$
            webServiceUrlText.setText(TSALESFORCE_INPUT_URL);
        }

        setTextValue(getConnection().getUserName(), userNameText);
        setTextValue(getConnection().getPassword(), passwordText);
        setTextValue(getConnection().getBatchSize(), batchSizeText);
        useProxyBtn.setSelection(getConnection().isUseProxy());
        setTextValue(getConnection().getProxyHost(), proxyHostText);
        setTextValue(getConnection().getProxyPort(), proxyPortText);
        setTextValue(getConnection().getProxyUsername(), proxyUsernameText);
        setTextValue(getConnection().getProxyPassword(), proxyPasswordText);

        boolean useCustom = false;
        useCustom = getConnection().isUseCustomModuleName();
        if (!useCustom) {
            if (getConnection().getModuleName() != null && !getConnection().getModuleName().equals("")) { //$NON-NLS-1$
                moduleNameCombo.setText(getConnection().getModuleName());
            } else {
                getConnection().setModuleName(moduleNameCombo.getText()); // Set defult value
            }
        } else {
            if (getConnection().getModuleName() != null && !getConnection().getModuleName().equals("")) { //$NON-NLS-1$
                setCustomModuleCombo(getConnection().getModuleName());
            } else {
                getConnection().setModuleName(customModuleCombo.getText()); // Set defult value
            }
        }
        useCostomModuleButton.setSelection(useCustom);
        customModuleCombo.setEnabled(useCustom);
        moduleNameCombo.setEnabled(!useCustom);
    }

    private void setCustomModuleCombo(String moduleName) {
        customModuleCombo.removeAll();
        customModuleCombo.add(moduleName);
        customModuleCombo.select(0);
    }

    private void setTextValue(String value, LabelledText control) {
        if (value != null && !value.equals("")) { //$NON-NLS-1$
            control.setText(value);
        }
    }

    @Override
    protected void adaptFormToEditable() {
        super.adaptFormToEditable();
        webServiceUrlText.setEditable(!isContextMode());
        userNameText.setEditable(!isContextMode());
        passwordText.setEditable(!isContextMode());
        proxyHostText.setEditable(!isContextMode());
        proxyPortText.setEditable(!isContextMode());
        proxyUsernameText.setEditable(!isContextMode());
        proxyPasswordText.setEditable(!isContextMode());
        if (isContextMode()) {
            passwordText.getTextControl().setEchoChar('\0');
            proxyPasswordText.getTextControl().setEchoChar('\0');
            checkButton.setEnabled(isContextMode());
        } else {
            passwordText.getTextControl().setEchoChar(pwdEhcoChar);
            proxyPasswordText.getTextControl().setEchoChar(pwdEhcoChar);
        }
    }

    public IPreferenceStore getPreferenceStore() {
        return CorePlugin.getDefault().getPreferenceStore();
    }
}
