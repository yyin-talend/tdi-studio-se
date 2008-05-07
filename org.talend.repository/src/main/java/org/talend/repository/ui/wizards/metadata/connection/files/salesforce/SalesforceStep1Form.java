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

import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.talend.commons.ui.swt.formtools.Form;
import org.talend.commons.ui.swt.formtools.LabelledCombo;
import org.talend.commons.ui.swt.formtools.LabelledText;
import org.talend.commons.ui.swt.formtools.UtilsButton;
import org.talend.core.model.metadata.IMetadataContextModeManager;
import org.talend.core.model.process.IElementParameter;
import org.talend.core.model.process.INode;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.repository.i18n.Messages;
import org.talend.repository.ui.swt.utils.AbstractSalesforceStepForm;

/**
 * DOC YeXiaowei class global comment. Detailled comment <br/>
 * 
 */
public class SalesforceStep1Form extends AbstractSalesforceStepForm {

    private LabelledText webServiceUrlText = null;

    private LabelledText userNameText = null;

    private LabelledText passwordText = null;

    private LabelledCombo moduleNameCombo = null;

    private UtilsButton cancelButton = null;

    private Button checkButton = null;

    private boolean loginOk = false;

    private boolean readOnly;

    private final String defaultWebServiceURL = "https://www.salesforce.com/services/Soap/u/8.0";

    private final char pwdEhcoChar = '*';

    private List<String> moduleNames = null;

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
        updateStatus(IStatus.OK, "");
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.ui.swt.utils.AbstractForm#addFields()
     */
    @Override
    protected void addFields() {

        Group group = Form.createGroup(this, 2, "Salesforce paramters");

        GridData data = new GridData(GridData.FILL_HORIZONTAL);
        group.setLayoutData(data);

        webServiceUrlText = new LabelledText(group, "Web service URL", true);

        userNameText = new LabelledText(group, "User name");

        passwordText = new LabelledText(group, "Password ");
        passwordText.getTextControl().setEchoChar(pwdEhcoChar);

        moduleNameCombo = new LabelledCombo(group, "Module name", "Please select a module name", moduleNames);

        initModuleNames();

        checkButton = new Button(group, SWT.NONE);
        checkButton.setText("Check login");
        checkButton.setEnabled(false);

        GridData wd = new GridData();
        wd.horizontalSpan = 2;
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
                checkFieldsValue();
            }
        });

    }

    private void testSalesforceLogin() {
        String webUrl = webServiceUrlText.getText();
        String username = userNameText.getText();
        String password = passwordText.getText();
        if (isContextMode() && getContextModeManager() != null) {
            webUrl = getContextModeManager().getOriginalValue(webUrl);
            username = getContextModeManager().getOriginalValue(username);
            password = getContextModeManager().getOriginalValue(password);
        }
        loginOk = checkSalesfoceLogin(webUrl, username, password);
    }

    public void setVisible(boolean visible) {
        super.setVisible(visible);

        if (isReadOnly() != readOnly) {
            adaptFormToReadOnly();
        }
        if (!isContextMode()) {
            checkFieldsValue();
            setCheckEnable();
        }
        if (visible && isContextMode()) {
            initialize();
            adaptFormToEditable();
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

        if (!isValueValid(moduleNameCombo.getText())) {
            updateStatus(IStatus.ERROR, "Your must give module for using Salesforce service"); //$NON-NLS-1$
            return false;
        }

        if (!loginOk) {
            updateStatus(IStatus.ERROR, "You haven't check login or URL, username, password is not correct."); //$NON-NLS-1$
            return false;
        }

        updateStatus(IStatus.OK, null);
        return true;
    }

    private boolean isValueValid(String value) {
        return value != null && !value.equals("");
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

        if (getConnection().getWebServiceUrl() != null && !getConnection().getWebServiceUrl().equals("")) {
            webServiceUrlText.setText(getConnection().getWebServiceUrl());
        }

        setTextValue(getConnection().getWebServiceUrl(), webServiceUrlText);

        if (getConnection().getWebServiceUrl() == null || getConnection().getWebServiceUrl().equals("")) {
            getConnection().setWebServiceUrl(defaultWebServiceURL);
        } // Give a default value

        if (webServiceUrlText.getText().equals("") || webServiceUrlText.getText() == null) {
            webServiceUrlText.setText(defaultWebServiceURL); // Default
        }

        setTextValue(getConnection().getUserName(), userNameText);

        setTextValue(getConnection().getPassword(), passwordText);

        if (getConnection().getModuleName() != null && !getConnection().getModuleName().equals("")) {
            moduleNameCombo.setText(getConnection().getModuleName());
        } else {
            getConnection().setModuleName(moduleNameCombo.getText()); // Set defult value
        }

    }

    private void initModuleNames() {

        INode node = getSalesforceNode();

        if (node == null) {
            moduleNameCombo.add("Account");
        } else {
            IElementParameter modulesNameParam = node.getElementParameter("MODULENAME");
            Object[] modules = modulesNameParam.getListItemsValue();

            if (modules == null || modules.length <= 0) {
                return;
            }

            moduleNameCombo.removeAll(); // First clear

            for (Object module : modules) {
                moduleNameCombo.add(module.toString());
            }

            moduleNameCombo.select(0);
        }
    }

    private void setTextValue(String value, LabelledText control) {
        if (value != null && !value.equals("")) {
            control.setText(value);
        }
    }

    @Override
    protected void adaptFormToEditable() {
        super.adaptFormToEditable();
        webServiceUrlText.setEditable(!isContextMode());
        userNameText.setEditable(!isContextMode());
        passwordText.setEditable(!isContextMode());
        if (isContextMode()) {
            passwordText.getTextControl().setEchoChar('\0');
        }
        checkButton.setEnabled(isContextMode());
    }
}
