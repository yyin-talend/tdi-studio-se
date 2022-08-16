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
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.preferences.ConfigurationScope;
import org.eclipse.equinox.app.IApplication;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.PreferenceDialog;
import org.eclipse.jface.preference.PreferenceManager;
import org.eclipse.jface.preference.PreferenceNode;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.widgets.Hyperlink;
import org.osgi.service.prefs.BackingStoreException;
import org.osgi.service.prefs.Preferences;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.utils.network.NetworkUtil;
import org.talend.core.model.general.ConnectionBean;
import org.talend.core.service.ICloudSignOnService;
import org.talend.core.ui.workspace.ChooseWorkspaceData;
import org.talend.repository.RepositoryPlugin;
import org.talend.repository.i18n.Messages;
import org.talend.repository.ui.login.connections.ConnectionUserPerReader;
import org.talend.repository.ui.login.connections.network.LoginNetworkPreferencePage;
import org.talend.repository.ui.login.connections.network.proxy.LoginProxyPreferencePage;
import org.talend.signon.util.TMCRepositoryUtil;
import org.talend.signon.util.TokenMode;
import org.talend.signon.util.listener.LoginEventListener;

public class LoginWithCloudPage extends AbstractLoginActionPage implements LoginEventListener {
    private static final Logger LOGGER = Logger.getLogger(LoginWithCloudPage.class);
    
    private static final String SHOW_WELCOME_INFO_KEY = "LoginWithCloudPage.showWelcomeInfo";

    protected Label title;

    private Button networkSettingsButton;

    protected String initialLicenceString;

    protected String licenseString;

    private Button signCloudButton;

    private Button otherSignButton;
    
    private Color messagePanelColor = new Color(null, 205, 227, 242);
    
    private String codeVerifier = ICloudSignOnService.get().generateCodeVerifier();
    
    private boolean isRefreshToken = false;
    
    private Composite firstInfoComposite = null;
    
    private Hyperlink imageLink = null;
    
    private boolean isSignOnCloud;

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
        if (isShowWelcomeInfo()) {
            firstInfoComposite = new Canvas(container, SWT.DOUBLE_BUFFERED);
            GridLayout compositeLayout = new GridLayout(3, false);
            firstInfoComposite.setLayout(compositeLayout);
            firstInfoComposite.addPaintListener(new PaintListener() {

                @Override
                public void paintControl(PaintEvent e) {
                    drawMessageBackground(e, firstInfoComposite);
                }
            });
            
            Label imageLabel = new Label(firstInfoComposite, SWT.None);           
            imageLabel.setImage(JFaceResources.getImage(Dialog.DLG_IMG_MESSAGE_INFO));
            imageLabel.setBackground(messagePanelColor);
            GridData gridData = new GridData(SWT.BEGINNING, SWT.CENTER, false, false);
            imageLabel.setLayoutData(gridData);
            
            Label infoLabel1 = new Label(firstInfoComposite, SWT.None);
            infoLabel1.setText(Messages.getString("LoginWithCloudPage.titleFirstLbl1"));
            infoLabel1.setBackground(messagePanelColor);
            gridData = new GridData(SWT.FILL, SWT.CENTER, false, false);
            gridData.horizontalSpan = 2;
            infoLabel1.setLayoutData(gridData);
            
            Label holderLabel = new Label(firstInfoComposite, SWT.None);
            gridData = new GridData(SWT.BEGINNING, SWT.CENTER, false, false);
            holderLabel.setLayoutData(gridData);
            
            Label infoLabel2 = new Label(firstInfoComposite, SWT.None);
            infoLabel2.setText(Messages.getString("LoginWithCloudPage.titleFirstLbl2"));
            infoLabel2.setBackground(messagePanelColor);
            gridData = new GridData(SWT.FILL, SWT.CENTER, false, false);
            infoLabel2.setLayoutData(gridData);
            
            Composite moreInfoCom = new Composite(firstInfoComposite, SWT.None);          
            compositeLayout = new GridLayout(2, false);
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
        
        otherSignButton = new Button(container, SWT.FLAT);
        otherSignButton.setFont(LoginDialogV2.fixedFont);
        otherSignButton.setText(Messages.getString("LoginWithCloudPage.otherSignBtn")); 

        networkSettingsButton = new Button(container, SWT.NONE);
        networkSettingsButton.setFont(LoginDialogV2.fixedFont);
        networkSettingsButton.setText(Messages.getString("LoginWithCloudPage.networkSettings"));//$NON-NLS-1$
        
        
        if (isRefreshToken) {
            title.setText(Messages.getString("LoginWithCloudPage.titleLblRelogin")); //$NON-NLS-1$
            otherSignButton.setText(Messages.getString("LoginWithCloudPage.exitBtn"));
        }
    }
    
    private void drawMessageBackground(PaintEvent e, Control ctrl) {
        e.gc.setBackground(messagePanelColor);
        e.gc.setAntialias(SWT.ON);
        e.gc.setInterpolation(SWT.HIGH);
        Point size = ctrl.getSize();
        e.gc.fillRoundRectangle(0, 0, size.x, size.y, 15, 15);
    }

    @Override
    protected void layoutControl() {
        if (firstInfoComposite != null) {            
            int offset = (firstInfoComposite.computeSize(SWT.DEFAULT, SWT.DEFAULT).x) / 2 * -1;
            FormData formData = new FormData();
            formData.top = new FormAttachment(0, 0);
            formData.left = new FormAttachment(50, offset);
            firstInfoComposite.setLayoutData(formData);
        }
        
        int offset = (title.computeSize(SWT.DEFAULT, SWT.DEFAULT).x) / 2 * -1;
        FormData formData = new FormData();
        if (firstInfoComposite != null) {
            formData.top = new FormAttachment(firstInfoComposite, 20);
        } else {
            formData.top = new FormAttachment(20, 0);
        }
        formData.left = new FormAttachment(50, offset);
        title.setLayoutData(formData);

        offset = signCloudButton.computeSize(SWT.DEFAULT, SWT.DEFAULT).x / 2 * -1;
        formData = new FormData();
        formData.top = new FormAttachment(title, 30, SWT.BOTTOM);
        formData.left = new FormAttachment(50, offset);
        signCloudButton.setLayoutData(formData);

        offset = otherSignButton.computeSize(SWT.DEFAULT, SWT.DEFAULT).x / 2 * -1;
        formData = new FormData();
        formData.top = new FormAttachment(signCloudButton, 20, SWT.BOTTOM);
        formData.left = new FormAttachment(50, offset);
        otherSignButton.setLayoutData(formData);

        if (this.networkSettingsButton != null) {
            offset = networkSettingsButton.computeSize(SWT.DEFAULT, SWT.DEFAULT).x / 2 * -1;
            formData = new FormData();
            formData.top = new FormAttachment(100, -50);
            formData.left = new FormAttachment(50, offset);
            networkSettingsButton.setLayoutData(formData);
        }
    }
    
    private boolean isShowWelcomeInfo() {
        Preferences node = new ConfigurationScope().getNode(ChooseWorkspaceData.ORG_TALEND_WORKSPACE_PREF_NODE);
        boolean showWelcomeInfo = node.getBoolean(SHOW_WELCOME_INFO_KEY, true);
        return showWelcomeInfo;
    }
    
    private void saveShowWelcomeInfo() {
        Preferences node = new ConfigurationScope().getNode(ChooseWorkspaceData.ORG_TALEND_WORKSPACE_PREF_NODE);
        node.putBoolean(SHOW_WELCOME_INFO_KEY, false);
        try {
            node.flush();
        } catch (BackingStoreException e) {
            LOGGER.error("failed to store workspace location in preferences :", e); //$NON-NLS-1$
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
                    ICloudSignOnService.get().signonCloud(LoginWithCloudPage.this);
                } catch (Exception e1) {
                    showError(e1);
                }
            }
        });

        if (otherSignButton != null) {
            otherSignButton.addSelectionListener(new SelectionAdapter() {

                @Override
                public void widgetSelected(SelectionEvent e) {
                    if (!isRefreshToken) {
                        isSignOnCloud = false;
                        removeSSOConnection();
                        try {
                            gotoNextPage();
                        } catch (Throwable e1) {
                            showError(e1);
                        }
                    } else {
                        try {
                            if (PlatformUI.isWorkbenchRunning()) {
                                PlatformUI.getWorkbench().close();
                            }
                            System.exit(IApplication.EXIT_OK);
                        } catch (Exception e1) {
                            showError(e1);
                        } 
                    }
                }
            });
        }

        if (networkSettingsButton != null) {
            networkSettingsButton.addSelectionListener(new SelectionAdapter() {

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
            updateSignButtonStatus(false);
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
            saveConnection(token, TMCRepositoryUtil.getCloudAdminURL(dataCenter),
                    ICloudSignOnService.get().getTokenUser(TMCRepositoryUtil.getCloudAdminURL(dataCenter), token));
            if (isRefreshToken) {
                Display.getDefault().asyncExec(() -> {
                    loginDialog.okPressed();
                });
            } else {
                Display.getDefault().asyncExec(() -> {
                    try {
                        errorManager.clearAllMessages();
                        updateSignButtonStatus(true);
                        this.gotoNextPage();
                    } catch (Throwable e) {
                        Display.getDefault().asyncExec(() -> {
                            errorManager.setErrMessage(e.getLocalizedMessage());
                        });
                        ExceptionHandler.process(e);
                    }
                });
            }
        } catch (Exception e) {
            Display.getDefault().asyncExec(() -> {
                updateSignButtonStatus(true);
                errorManager.setErrMessage(e.getLocalizedMessage());
            });
            ExceptionHandler.process(e);
        }
    }
    

    @Override
    public void loginFailed(Exception ex) {
        Display.getDefault().asyncExec(() -> {
            updateSignButtonStatus(true);
            errorManager.setErrMessage(ex.getMessage());
        });
    }
    
    private void updateSignButtonStatus(boolean isEnable) {
        if (signCloudButton != null) {
            signCloudButton.setEnabled(isEnable);
        }
        if (otherSignButton != null && !isRefreshToken) {
            otherSignButton.setEnabled(isEnable);
        }
    }
    
    private void removeSSOConnection() {
        ConnectionUserPerReader reader = ConnectionUserPerReader.getInstance();
        List<ConnectionBean> list = reader.readConnections();
        Iterator<ConnectionBean> connectionBeanIter = list.iterator();
        while (connectionBeanIter.hasNext()) {
            if (connectionBeanIter.next().isLoginViaCloud()) {
                connectionBeanIter.remove();
            }
        }
        reader.saveConnections(list);
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
            saveShowWelcomeInfo();
            iNextPage = super.getNextPage();
            if (iNextPage == null) {
                iNextPage = new LoginProjectPage(getParent(), loginDialog, SWT.NONE, isSignOnCloud);
                setNextPage(iNextPage);
            }
        }

        return iNextPage;
    }
}
