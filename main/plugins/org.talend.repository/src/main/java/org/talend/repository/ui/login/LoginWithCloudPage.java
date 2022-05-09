// ============================================================================
//
// Copyright (C) 2006-2021 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.repository.ui.login;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.configurator.common.connections.CloudSignOnLoginUtil;
import org.talend.configurator.common.connections.TokenMode;
import org.talend.configurator.common.connections.TokenServiceImpl;
import org.talend.configurator.common.utils.SignOnEventListener;
import org.talend.license.gui.util.ConnectionUtil;
import org.talend.repository.i18n.Messages;



public class LoginWithCloudPage extends AbstractLoginActionPage implements SignOnEventListener{

    private static final Color COLOR_BLACK = new Color(null, 0, 0, 0);

    private static final Color COLOR_RED = new Color(null, 240, 0, 0);

    private static final String ERROR = "error"; //$NON-NLS-1$

    protected Label title;

    private Link networkSettingsLabel;

    protected String initialLicenceString;

    protected String licenseString;

    private Button signCloudButton;

    private Button otherSignButton;

    private boolean isSignOnCloud = false;

    public LoginWithCloudPage(Composite parent, LoginDialogV2 dialog, int style) {
        super(parent, dialog, style);
    }

    @Override
    public void preCreateControl() {
        // nothing need to do
    }

    @Override
    public void instantiateControl(Composite container) {
        title = new Label(container, SWT.WRAP);
        title.setFont(LoginDialogV2.fixedFont);
        title.setText("Get start with your Studio integration software"); //$NON-NLS-1$

        signCloudButton = new Button(container, SWT.FLAT);
        signCloudButton.setFont(LoginDialogV2.fixedFont);
        signCloudButton.setText("Sign in with Talend Cloud");

        otherSignButton = new Button(container, SWT.FLAT);
        otherSignButton.setFont(LoginDialogV2.fixedFont);
        otherSignButton.setText("Other sign in mode");

        networkSettingsLabel = new Link(container, SWT.NONE);
        networkSettingsLabel.setFont(LoginDialogV2.fixedFont);
        networkSettingsLabel.setBackground(backgroundRadioColor);
        networkSettingsLabel.setText("<a>" //$NON-NLS-1$
                + Messages.getString("LoginProjectPage.networkSettings") //$NON-NLS-1$
                + "</a>");//$NON-NLS-1$

    }

    @Override
    protected void layoutControl() {
        int offset = (title.computeSize(SWT.DEFAULT, SWT.DEFAULT).x) / 2 * -1;
        FormData formData = new FormData();
        formData.top = new FormAttachment(15, 0);
        formData.left = new FormAttachment(50, offset);
        title.setLayoutData(formData);

        offset = signCloudButton.computeSize(SWT.DEFAULT, SWT.DEFAULT).x / 2 * -1;
        formData = new FormData();
        formData.top = new FormAttachment(title, 30, SWT.BOTTOM);
        formData.left = new FormAttachment(50, offset);
        signCloudButton.setLayoutData(formData);

        offset = otherSignButton.computeSize(SWT.DEFAULT, SWT.DEFAULT).x / 2 * -1;
        formData = new FormData();
        formData.top = new FormAttachment(signCloudButton, TAB_VERTICAL_PADDING_LEVEL_1, SWT.BOTTOM);
        formData.left = new FormAttachment(50, offset);
        otherSignButton.setLayoutData(formData);

        if (this.networkSettingsLabel != null) {
            offset = networkSettingsLabel.computeSize(SWT.DEFAULT, SWT.DEFAULT).x / 2 * -1;
            formData = new FormData();
            formData.top = new FormAttachment(100, -100);
            formData.left = new FormAttachment(50, offset);
            networkSettingsLabel.setLayoutData(formData);
        }
    }

    protected void layoutRemotely() {

    }

    @Override
    public void init() throws Throwable {
        super.init();
    }

    @Override
    public void afterCreateControl() {
    }

    @Override
    public void refreshUIData() {
        // nothing need to do
    }

    @Override
    public void check() {
        // nothing need to do
    }

    @Override
    public void addListeners() {
        signCloudButton.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                isSignOnCloud = true;
                try {
                    CloudSignOnLoginUtil.signonCloud(LoginWithCloudPage.this);
                } catch (Exception e1) {
                    showError(e1);
                }
            }
        });

        otherSignButton.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                isSignOnCloud = false;
                try {
                    gotoNextPage();
                } catch (Throwable e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
            }

        });

        networkSettingsLabel.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                ConnectionUtil.onNetworkSettingsClicked();
            }

        });
    }


    @Override
    public Object getCheckedErrors() {
        return null;
    }

    protected String getRecentWorkSpace() {
        String filePath = new Path(Platform.getInstanceLocation().getURL().getPath()).toFile().getPath();
        return filePath;
    }

    private void showError(Throwable e) {
        String message = null;
        if (e instanceof InvocationTargetException && ((InvocationTargetException) e).getTargetException() != null) {
            message = ((InvocationTargetException) e).getTargetException().getMessage();
        } else {
            message = e.getMessage();
        }
        MessageDialog.openError(Display.getDefault().getActiveShell(), getShell().getText(), message);
    }

    @Override
    public AbstractActionPage getNextPage() {
        AbstractActionPage iNextPage = new LoginProjectPage(getParent(), loginDialog, SWT.NONE);
        return iNextPage;
    }

    @Override
    public void loginStart(String clientID) {
        errorManager.setInfoMessage("Still working on first step..."); 
    }

    @Override
    public void loginStop(String clientID, String authCode) {
        errorManager.setInfoMessage("Still working on second step...");
        try {
            TokenMode token = CloudSignOnLoginUtil.getToken(clientID, authCode);
            TokenServiceImpl.getInstance().save(token);
            TokenServiceImpl.getInstance().start();          
        } catch (Exception e) {
            errorManager.setErrMessage(e.getLocalizedMessage());
            ExceptionHandler.process(e);
        }
        
    }

    @Override
    public void loginFailed(Exception ex) {
        errorManager.setErrMessage(ex.getMessage());
        
    }
}
