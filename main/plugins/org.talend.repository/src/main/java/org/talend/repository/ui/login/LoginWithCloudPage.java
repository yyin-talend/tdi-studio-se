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
import java.util.List;

import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.PreferenceDialog;
import org.eclipse.jface.preference.PreferenceManager;
import org.eclipse.jface.preference.PreferenceNode;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.widgets.Hyperlink;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.utils.network.NetworkUtil;
import org.talend.core.model.general.ConnectionBean;
import org.talend.core.service.ICloudSignOnService;
import org.talend.repository.RepositoryPlugin;
import org.talend.repository.i18n.Messages;
import org.talend.repository.ui.login.connections.ConnectionUserPerReader;
import org.talend.repository.ui.login.connections.network.LoginNetworkPreferencePage;
import org.talend.repository.ui.login.connections.network.proxy.LoginProxyPreferencePage;
import org.talend.signon.util.SSOClientUtil;
import org.talend.signon.util.TokenMode;
import org.talend.signon.util.listener.LoginEventListener;

public class LoginWithCloudPage extends AbstractLoginActionPage implements LoginEventListener {

    protected Label title;

    private Link networkSettingsLabel;

    protected String initialLicenceString;

    protected String licenseString;

    private Button signCloudButton;

    private Button otherSignButton;
    
    private Button restartButton;
    
    private Color messagePanelColor = new Color(null, 205, 227, 242);
    
    private String codeVerifier = ICloudSignOnService.get().generateCodeVerifier();
    
    private boolean isRefreshToken = false;
    
    private boolean isFirstLogin = true;
    
    private Composite firstInfoComposite = null;
    
    private Hyperlink imageLink = null;

    public LoginWithCloudPage(Composite parent, LoginDialogV2 dialog, int style) {
        super(parent, dialog, style);
    }
    
    public LoginWithCloudPage(Composite parent, LoginDialogV2 dialog, int style, boolean isRefreshToken) {
        super(parent, dialog, style);
        this.isRefreshToken = isRefreshToken;
    }


    @Override
    public void preCreateControl() {
        // nothing need to do
    }

    @Override
    public void instantiateControl(Composite container) {
        if (isFirstLogin) {
            firstInfoComposite = new Composite (container, SWT.None);
            GridLayout compositeLayout = new GridLayout(3, false);
            compositeLayout.marginWidth = 0;
            compositeLayout.marginHeight = 0;
            firstInfoComposite.setLayout(compositeLayout);
            firstInfoComposite.setBackground(messagePanelColor);
            
            Label imageLabel = new Label(firstInfoComposite, SWT.None);           
            imageLabel.setImage(JFaceResources.getImage(Dialog.DLG_IMG_MESSAGE_INFO));
            imageLabel.setBackground(messagePanelColor);
            
            Label infoLabel = new Label(firstInfoComposite, SWT.None);
            infoLabel.setText(Messages.getString("LoginWithCloudPage.titleFirstLbl"));
            infoLabel.setBackground(messagePanelColor);
            
            Composite moreInfoCom = new Composite(firstInfoComposite, SWT.None);
            compositeLayout = new GridLayout(3, false);
            compositeLayout.marginWidth = 0;
            compositeLayout.marginHeight = 0;
            moreInfoCom.setLayout(compositeLayout);
            moreInfoCom.setBackground(messagePanelColor);
            
            imageLink = new Hyperlink(moreInfoCom, SWT.NONE);
            imageLink.setText(Messages.getString("LoginWithCloudPage.titleFirstLinkLbl"));
            imageLink.setUnderlined(true); 
            imageLink.setBackground(messagePanelColor);
            
            Label moreImageLabel= new Label(moreInfoCom, SWT.RIGHT);           
            ImageDescriptor imageDescriptor = ImageDescriptor.createFromURL(RepositoryPlugin.class.getResource("/icons/moreInfo.png"));
            moreImageLabel.setImage(imageDescriptor.createImage());
            moreImageLabel.setBackground(messagePanelColor);
        }
        
        title = new Label(container, SWT.WRAP);
        title.setFont(LoginDialogV2.fixedFont);
        title.setText(Messages.getString("LoginWithCloudPage.titleLbl")); //$NON-NLS-1$

        signCloudButton = new Button(container, SWT.FLAT);
        signCloudButton.setFont(LoginDialogV2.fixedFont);
        signCloudButton.setText(Messages.getString("LoginWithCloudPage.signCloudBtn"));

        if (!isRefreshToken) {
            otherSignButton = new Button(container, SWT.FLAT);
            otherSignButton.setFont(LoginDialogV2.fixedFont);
            otherSignButton.setText(Messages.getString("LoginWithCloudPage.otherSignBtn")); 

            networkSettingsLabel = new Link(container, SWT.NONE);
            networkSettingsLabel.setFont(LoginDialogV2.fixedFont);
            networkSettingsLabel.setBackground(backgroundRadioColor);
            networkSettingsLabel.setText("<a>" //$NON-NLS-1$
                    + Messages.getString("LoginProjectPage.networkSettings") //$NON-NLS-1$
                    + "</a>");//$NON-NLS-1$
        } else {
            restartButton = new Button(container, SWT.FLAT);
            restartButton.setFont(LoginDialogV2.fixedFont);
            restartButton.setText("Exit Studio"); 
        }
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

        if (otherSignButton != null) {
            offset = otherSignButton.computeSize(SWT.DEFAULT, SWT.DEFAULT).x / 2 * -1;
            formData = new FormData();
            formData.top = new FormAttachment(signCloudButton, TAB_VERTICAL_PADDING_LEVEL_1, SWT.BOTTOM);
            formData.left = new FormAttachment(50, offset);
            otherSignButton.setLayoutData(formData); 
        }
        
        if (restartButton != null) {
            offset = restartButton.computeSize(SWT.DEFAULT, SWT.DEFAULT).x / 2 * -1;
            formData = new FormData();
            formData.top = new FormAttachment(signCloudButton, TAB_VERTICAL_PADDING_LEVEL_1, SWT.BOTTOM);
            formData.left = new FormAttachment(50, offset);
            restartButton.setLayoutData(formData); 
        }

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
                try {
                    ICloudSignOnService.get().signonCloud(LoginWithCloudPage.this);
                } catch (Exception e1) {
                    showError(e1);
                }
            }
        });
        if (restartButton != null) {
            restartButton.addSelectionListener(new SelectionAdapter() {

                @Override
                public void widgetSelected(SelectionEvent e) {
                    try {
                        PlatformUI.getWorkbench().close();
                    } catch (Exception e1) {
                        showError(e1);
                    }
                }
            });
        }

        if (otherSignButton != null) {
            otherSignButton.addSelectionListener(new SelectionAdapter() {

                @Override
                public void widgetSelected(SelectionEvent e) {
                    try {
                        gotoNextPage();
                    } catch (Throwable e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    }
                }

            });
        }

        if (networkSettingsLabel != null) {
            networkSettingsLabel.addSelectionListener(new SelectionAdapter() {

                @Override
                public void widgetSelected(SelectionEvent e) {
                    onNetworkSettingsClicked();
                }

            });
        }
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
    public void loginStart() {
        Display.getDefault().asyncExec(() -> {
            errorManager.setInfoMessage(Messages.getString("LoginWithCloudPage.signInTaskFirst"));
        });
    }

    @Override
    public void loginStop(String authCode, String dataCenter) {
        Display.getDefault().asyncExec(() -> {
            errorManager.setInfoMessage(Messages.getString("LoginWithCloudPage.signInTaskSec"));
            loginDialog.getShell().forceActive();
        });
        try {
            TokenMode token = ICloudSignOnService.get().getToken(authCode, this.codeVerifier, dataCenter);
            Display.getDefault().asyncExec(() -> {
                errorManager.setInfoMessage(Messages.getString("LoginWithCloudPage.signInTaskThi"));
            });
            saveConnection(token, SSOClientUtil.getCloudAdminURL(dataCenter), ICloudSignOnService.get().getTokenUser(SSOClientUtil.getCloudAdminURL(dataCenter), token));
            Display.getDefault().asyncExec(() -> {
                try {
                    errorManager.clearAllMessages();
                    this.gotoNextPage();
                } catch (Throwable e) {
                    ExceptionHandler.process(e);
                }
            });
        } catch (Exception e) {
            Display.getDefault().asyncExec(() -> {
                errorManager.setErrMessage(e.getLocalizedMessage());
            });
            ExceptionHandler.process(e);
        }

    }

    private void saveConnection(TokenMode token, String adminURL, String user) {
        ConnectionUserPerReader reader = ConnectionUserPerReader.getInstance();
        List<ConnectionBean> list = reader.readConnections();
        ConnectionBean connection = null;
        for (ConnectionBean bean : list) {
            if (adminURL.equals(bean.getUrl()) && user.equals(bean.getUser())) {
                connection = bean;
                break;
            }
        }
        if (connection == null) {
            connection = ConnectionBean.getDefaultCloudConnectionBean(token.getDataCenter());
            connection.setConnectionToken(token);
            connection.setUrl(adminURL);
            connection.setUser(user);
            list.add(connection);
        }
        reader.saveConnections(list);
        reader.saveLastConnectionBean(connection);
    }

    @Override
    public void loginFailed(Exception ex) {
        Display.getDefault().asyncExec(() -> {
            errorManager.setErrMessage(ex.getMessage());
        });
    }

    @Override
    public String getCodeChallenge() {
        String codeChallenge = null;
        try {
            if (ICloudSignOnService.get() != null) {
                codeChallenge = ICloudSignOnService.get().getCodeChallenge(codeVerifier);
            }
        } catch (Exception ex) {
            Display.getDefault().asyncExec(() -> {
                errorManager.setErrMessage(ex.getLocalizedMessage());
            });
            ExceptionHandler.process(ex);
        }
        return codeChallenge;
    }

    public static void onNetworkSettingsClicked() {
        PreferenceDialog pd = new PreferenceDialog(Display.getDefault().getActiveShell(), new PreferenceManager());

        LoginProxyPreferencePage prefPage = new LoginProxyPreferencePage();
        prefPage.setTitle(Messages.getString("LoginProjectPage.networkSettings.proxy.title"));
        pd.getPreferenceManager().addToRoot(new PreferenceNode("proxyPage", prefPage));

        LoginNetworkPreferencePage networkPage = new LoginNetworkPreferencePage();
        networkPage.setTitle(Messages.getString("LoginProjectPage.networkSettings.timeout.title"));
        pd.getPreferenceManager().addToRoot(new PreferenceNode("timeoutPage", networkPage));

        int open = pd.open();
        if (Window.OK == open) {
            NetworkUtil.loadAuthenticator();
        }
    }

    @Override
    public AbstractActionPage getNextPage() {
        AbstractActionPage iNextPage = null;
        if (!isRefreshToken) {
            iNextPage = super.getNextPage();
            if (iNextPage == null) {
                iNextPage = new LoginProjectPage(getParent(), loginDialog, SWT.NONE, true);
                setNextPage(iNextPage);
            }
        }

        return iNextPage;
    }
}
