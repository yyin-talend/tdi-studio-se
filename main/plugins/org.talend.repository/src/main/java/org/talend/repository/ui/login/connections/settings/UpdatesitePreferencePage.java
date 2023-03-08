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
package org.talend.repository.ui.login.connections.settings;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.program.Program;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.ui.runtime.exception.ExceptionMessageDialog;
import org.talend.commons.ui.runtime.image.EImage;
import org.talend.commons.ui.runtime.image.ImageProvider;
import org.talend.core.model.general.ConnectionBean;
import org.talend.core.service.IStudioLiteP2Service;
import org.talend.core.service.IStudioLiteP2Service.UpdateSiteConfig;
import org.talend.core.ui.branding.IBrandingService;
import org.talend.repository.ProjectManager;
import org.talend.repository.i18n.Messages;
import org.talend.repository.ui.login.LoginHelper;

/**
 * DOC cmeng class global comment. Detailled comment
 */
public class UpdatesitePreferencePage extends PreferencePage {

    IStudioLiteP2Service p2Service = IStudioLiteP2Service.get();

    private Text releaseUriText;

    private Text updateUriText;
    
    private Text baseUserText;

    private Text basePasswordText;

    private Text updateUserText;

    private Text updatePasswordText;
    
    private Button enableBaseAuth;
    
    private Button enableUpdateAuth;

    private Text remoteReleaseUriText;

    private Text remoteUpdateUriText;

    private Button overwriteRemoteUpdateSettingsBtn;

    private Composite panel;

    private Composite remotePanel;

    private Composite overwritePanel;

    private Composite localPanel;

    private Composite warningPanel;
    
    private Label warningDesc;

    private boolean enableTmcUpdateSettings;

    private boolean isCloudConnection = false;

    private boolean isWorkbenchRunning = false;
    
    private Button m2Delete = null;
    
    private static final boolean M2_DELETE_DEFAULT= false;
    
    private static final String LINK_MORE_URL = "https://document-link.us.cloud.talend.com/ts_mg_update-studio?version=80&lang=en&env=prd";
    
    private static final String PROPERTY_REMOVE_M2 = "talend.studio.m2.clean";

    @Override
    protected Control createContents(Composite parent) {
        this.setTitle(Messages.getString("UpdatesitePreferencePage.title"));

        panel = new Composite(parent, SWT.NONE);
        panel.setLayout(new FormLayout());

        FormData fd = null;
        remotePanel = new Composite(panel, SWT.NONE);
        FormLayout formLayout = new FormLayout();
        formLayout.marginBottom = 10;
        remotePanel.setLayout(formLayout);
        fd = new FormData();
        fd.top = new FormAttachment(0);
        fd.left = new FormAttachment(0);
        fd.right = new FormAttachment(100);
        remotePanel.setLayoutData(fd);

        Group remoteGroup = null;
        GridLayout panelLayout = null;
        GridData gd = null;
        ConnectionBean curConnection = LoginHelper.getInstance().getCurrentSelectedConnBean();
        isCloudConnection = LoginHelper.isCloudConnection(curConnection);
        isWorkbenchRunning = PlatformUI.isWorkbenchRunning();
        if (isWorkbenchRunning && isCloudConnection) {
            remoteGroup = new Group(remotePanel, SWT.NONE);
            String projectLabel = "";
            try {
                projectLabel = ProjectManager.getInstance().getCurrentProject().getLabel();
            } catch (Exception e) {
                ExceptionHandler.process(e);
            }
            remoteGroup.setText(Messages.getString("UpdatesitePreferencePage.group.remote", projectLabel));
            fd = new FormData();
            fd.top = new FormAttachment(0);
            fd.left = new FormAttachment(0);
            fd.right = new FormAttachment(100);
            remoteGroup.setLayoutData(fd);
            remoteGroup.setLayout(new FillLayout());

            Composite RemoteSettingsPanel = new Composite(remoteGroup, SWT.NONE);
            RemoteSettingsPanel.setLayoutData(new GridData(GridData.FILL_BOTH));

            panelLayout = new GridLayout(2, false);
            panelLayout.horizontalSpacing = 10;
            panelLayout.verticalSpacing = 5;
            RemoteSettingsPanel.setLayout(panelLayout);

            Label remoteReleaseLabel = new Label(RemoteSettingsPanel, SWT.NONE);
            remoteReleaseLabel.setText(Messages.getString("UpdatesitePreferencePage.base"));
            gd = new GridData();
            remoteReleaseLabel.setLayoutData(gd);

            remoteReleaseUriText = new Text(RemoteSettingsPanel, SWT.BORDER);
            remoteReleaseUriText.setEditable(false);
            gd = new GridData(GridData.FILL_HORIZONTAL);
            remoteReleaseUriText.setLayoutData(gd);

            Label remoteUpdateLabel = new Label(RemoteSettingsPanel, SWT.NONE);
            remoteUpdateLabel.setText(Messages.getString("UpdatesitePreferencePage.update"));
            gd = new GridData();
            remoteUpdateLabel.setLayoutData(gd);

            remoteUpdateUriText = new Text(RemoteSettingsPanel, SWT.BORDER);
            remoteUpdateUriText.setEditable(false);
            gd = new GridData(GridData.FILL_HORIZONTAL);
            remoteUpdateUriText.setLayoutData(gd);
        }

        boolean isCloudLicense = IBrandingService.get().isCloudLicense();
        boolean showOverwrite = false;
        if (isCloudLicense) {
            showOverwrite = true;
        } else {
            showOverwrite = isWorkbenchRunning && isCloudConnection;
        }
        overwritePanel = new Composite(panel, SWT.NONE);
        fd = new FormData();
        fd.top = new FormAttachment(remotePanel, 0, SWT.BOTTOM);
        fd.left = new FormAttachment(0);
        overwritePanel.setLayoutData(fd);
        if (showOverwrite) {
            formLayout = new FormLayout();
            formLayout.marginBottom = 7;
            overwritePanel.setLayout(formLayout);

            overwriteRemoteUpdateSettingsBtn = new Button(overwritePanel, SWT.CHECK);
            String overwriteLabel = null;
            if (isWorkbenchRunning) {
                overwriteLabel = Messages.getString("UpdatesitePreferencePage.btn.overwriteRemoteUpdateSettings");
            } else {
                overwriteLabel = Messages.getString("UpdatesitePreferencePage.btn.overwriteRemoteUpdateSettings.logon");
            }
            overwriteRemoteUpdateSettingsBtn.setText(overwriteLabel);
            fd = new FormData();
            fd.top = new FormAttachment(0);
            fd.left = new FormAttachment(0);
            overwriteRemoteUpdateSettingsBtn.setLayoutData(fd);
            if (!isWorkbenchRunning) {
                Label help = new Label(overwritePanel, SWT.PUSH);
                help.setImage(ImageProvider.getImage(EImage.QUESTION_ICON));
                help.setToolTipText(Messages.getString("UpdatesitePreferencePage.btn.overwriteRemoteUpdateSettings.help"));
                fd = new FormData();
                fd.top = new FormAttachment(overwriteRemoteUpdateSettingsBtn, 0, SWT.CENTER);
                fd.left = new FormAttachment(overwriteRemoteUpdateSettingsBtn, 2, SWT.RIGHT);
                help.setLayoutData(fd);
            }
        } else {
            fd.height = 0;
        }
        
        Composite m2Panel = new Composite(panel, SWT.None);
        
        localPanel = new Composite(panel, SWT.NONE);
        localPanel.setLayout(new FormLayout());
        fd = new FormData();
        fd.top = new FormAttachment(overwritePanel, 0, SWT.BOTTOM);
        fd.left = new FormAttachment(0);
        fd.right = new FormAttachment(100);
        fd.bottom = new FormAttachment(m2Panel, 0, SWT.TOP);
        localPanel.setLayoutData(fd);

        Group localGroup = new Group(localPanel, SWT.NONE);
        localGroup.setText(Messages.getString("UpdatesitePreferencePage.group.local"));
        fd = new FormData();
        fd.top = new FormAttachment(0);
        fd.left = new FormAttachment(0);
        fd.right = new FormAttachment(100);
        fd.bottom = new FormAttachment(100);
        localGroup.setLayoutData(fd);
        localGroup.setLayout(new FillLayout());
        
        Composite LocalSettinsContainer =  new Composite(localGroup, SWT.None);
        LocalSettinsContainer.setLayout(new GridLayout(1, false));
        LocalSettinsContainer.setLayoutData(new GridData(SWT.NONE, SWT.TOP, true, false));
        
        Composite localSettingsPanel = new Composite(LocalSettinsContainer, SWT.None);
        localSettingsPanel.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        panelLayout = new GridLayout(2, false);
        panelLayout.horizontalSpacing = 10;
        panelLayout.verticalSpacing = 5;
        localSettingsPanel.setLayout(panelLayout);

        Label releaseLabel = new Label(localSettingsPanel, SWT.NONE);
        releaseLabel.setText(Messages.getString("UpdatesitePreferencePage.base"));
        gd = new GridData();
        releaseLabel.setLayoutData(gd);

        releaseUriText = new Text(localSettingsPanel, SWT.BORDER);
        gd = new GridData(GridData.FILL_HORIZONTAL);
        releaseUriText.setLayoutData(gd);
        
        // add basic authentication settings for base uri
        enableBaseAuth = new Button(localSettingsPanel,SWT.CHECK);
        enableBaseAuth.setText(Messages.getString("UpdatesitePreferencePage.basicAuth.enable"));
        
        Composite baseAuthPanel =  new Composite(localSettingsPanel, SWT.None);
        baseAuthPanel.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        panelLayout = new GridLayout(4, false);
        baseAuthPanel.setLayout(panelLayout);
        
        Label unameLabel = new Label(baseAuthPanel, SWT.NONE);
        unameLabel.setText(Messages.getString("UpdatesitePreferencePage.basicAuth.user"));
        baseUserText = new Text(baseAuthPanel, SWT.BORDER);
        gd = new GridData(GridData.FILL_HORIZONTAL);
        baseUserText.setLayoutData(gd);
        
        Label pwdLabel = new Label(baseAuthPanel, SWT.NONE);
        pwdLabel.setText(Messages.getString("UpdatesitePreferencePage.basicAuth.password"));
        basePasswordText = new Text(baseAuthPanel, SWT.PASSWORD | SWT.BORDER);
        gd = new GridData(GridData.FILL_HORIZONTAL);
        basePasswordText.setLayoutData(gd);
        
        Label updateLabel = new Label(localSettingsPanel, SWT.NONE);
        updateLabel.setText(Messages.getString("UpdatesitePreferencePage.update"));
        gd = new GridData();
        updateLabel.setLayoutData(gd);

        updateUriText = new Text(localSettingsPanel, SWT.BORDER);
        gd = new GridData(GridData.FILL_HORIZONTAL);
        updateUriText.setLayoutData(gd);
        
        // add basic authentication settings for update uri
        enableUpdateAuth = new Button(localSettingsPanel,SWT.CHECK);
        enableUpdateAuth.setText(Messages.getString("UpdatesitePreferencePage.basicAuth.enable"));
        
        baseAuthPanel =  new Composite(localSettingsPanel, SWT.None);
        baseAuthPanel.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        panelLayout = new GridLayout(4, false);
        baseAuthPanel.setLayout(panelLayout);
        
        unameLabel = new Label(baseAuthPanel, SWT.NONE);
        unameLabel.setText(Messages.getString("UpdatesitePreferencePage.basicAuth.user"));
        updateUserText = new Text(baseAuthPanel, SWT.BORDER);
        gd = new GridData(GridData.FILL_HORIZONTAL);
        updateUserText.setLayoutData(gd);
        
        pwdLabel = new Label(baseAuthPanel, SWT.NONE);
        pwdLabel.setText(Messages.getString("UpdatesitePreferencePage.basicAuth.password"));
        updatePasswordText = new Text(baseAuthPanel, SWT.PASSWORD | SWT.BORDER);
        gd = new GridData(GridData.FILL_HORIZONTAL);
        updatePasswordText.setLayoutData(gd);
        
        Label placeHolder = new Label(localSettingsPanel, SWT.None);
        gd = new GridData();
        placeHolder.setLayoutData(gd);

        warningPanel = new Composite(localSettingsPanel, SWT.None);
        gd = new GridData(GridData.GRAB_HORIZONTAL);
        warningPanel.setLayoutData(gd);
        warningPanel.setLayout(new GridLayout(2, false));

        Label warningImg = new Label(warningPanel, SWT.None);
        gd = new GridData(GridData.VERTICAL_ALIGN_BEGINNING);
        warningImg.setLayoutData(gd);
        warningImg.setImage(ImageProvider.getImage(EImage.WARNING_ICON));
        warningDesc = new Label(warningPanel, SWT.WRAP);
        warningDesc.setText(Messages.getString("UpdatesitePreferencePage.warn.onPremUpdateSetup"));
        warningDesc.setFont(JFaceResources.getFontRegistry().getBold(JFaceResources.DEFAULT_FONT));
        gd = new GridData(GridData.VERTICAL_ALIGN_BEGINNING | GridData.FILL_HORIZONTAL);
        gd.widthHint = 600;
        warningDesc.setLayoutData(gd);
        
        // remove m2
        FormData m2PanelData = new FormData();
        m2PanelData.left = new FormAttachment(localPanel, 0, SWT.LEFT);
        m2PanelData.right = new FormAttachment(localPanel, 0, SWT.RIGHT);
        m2PanelData.bottom = new FormAttachment(100);
        m2Panel.setLayout(new GridLayout(1, false));
        m2Panel.setLayoutData(m2PanelData);
        
        m2Delete = new Button(m2Panel, SWT.CHECK | SWT.WRAP);
        m2Delete.setText(Messages.getString("UpdatesitePreferencePage.m2.delete"));
        try {
            m2Delete.setSelection(p2Service.removeM2());
        } catch (Exception e2) {
            ExceptionHandler.process(e2);
        }
        
        String linkMsg = Messages.getString("UpdatesitePreferencePage.m2.info", "<a>" + Messages.getString("UpdatesitePreferencePage.m2.more") + "</a>");

        Link m2Link = new Link(m2Panel, SWT.WRAP);
        GridData m2LinkData = new GridData(GridData.FILL_HORIZONTAL);
        m2LinkData.widthHint = 860;
        m2Link.setLayoutData(m2LinkData);
        m2Link.setText(linkMsg);
        m2Link.addListener(SWT.MouseUp, new Listener() {

            @Override
            public void handleEvent(Event event) {
                Program.launch(LINK_MORE_URL);
            }
        });
        
        if (Boolean.getBoolean(PROPERTY_REMOVE_M2)) {
            m2Delete.setSelection(true);
        }
        
        if (System.getProperty(PROPERTY_REMOVE_M2) != null) {
            m2Delete.setEnabled(false);
        }

        init();
        addListener();
        return panel;
    }

    // need to be executed after loading uris of base and update
    private void initBasicAuth(UpdateSiteConfig config) throws Exception {
        this.enableBaseAuth.setSelection(false);
        this.enableUpdateAuth.setSelection(false);
        URI baseURI = config.getLocalRelease(new NullProgressMonitor());
        if (isHTTP(baseURI)) {
            if (config.isEnabledBasicAuth(baseURI.getHost())) {
                this.enableBaseAuth.setSelection(true);

                // load user and pwd
                String[] namePwd = UpdateSiteConfig.loadCredentialsFromSecureStore(baseURI);
                if (namePwd != null) {
                    this.baseUserText.setText(namePwd[0]);
                    this.basePasswordText.setText(namePwd[1]);
                }
            } else {
                this.baseUserText.setEnabled(false);
                this.basePasswordText.setEnabled(false);
            }
        }
        
        URI updateUri = null;
        Collection<URI> updateURIs = config.getLocalUpdates(new NullProgressMonitor());
        for (URI uri : updateURIs) {
            if (isHTTP(uri)) {
                updateUri = uri;
                // only support one http update site
                break;
            }
        }
        if (updateUri != null) {
            if (StringUtils.equals(baseURI.getHost(), updateUri.getHost())) {
                this.enableUpdateAuth.setSelection(enableBaseAuth.getSelection());
                this.enableUpdateAuth.setEnabled(false);
                this.updateUserText.setEnabled(false);
                this.updatePasswordText.setEnabled(false);
            } else {
                this.enableUpdateAuth.setEnabled(true);
                if (config.isEnabledBasicAuth(updateUri.getHost())) {
                    this.enableUpdateAuth.setSelection(true);

                    // load user and pwd
                    String[] namePwd = UpdateSiteConfig.loadCredentialsFromSecureStore(updateUri);
                    if (namePwd != null) {
                        this.updateUserText.setText(namePwd[0]);
                        this.updatePasswordText.setText(namePwd[1]);
                    }
                } else {
                    this.updateUserText.setEnabled(false);
                    this.updatePasswordText.setEnabled(false);
                }
            }
        } else {
            // no http update site
            this.enableUpdateAuth.setEnabled(false);
            this.updateUserText.setEnabled(false);
            this.updatePasswordText.setEnabled(false);
        }
    }
    
    private void init() {
        try {
            ((GridData) warningPanel.getLayoutData()).exclude = true;
            warningPanel.setVisible(false);
            warningPanel.getParent().getParent().layout();
            
            IProgressMonitor monitor = new NullProgressMonitor();
            UpdateSiteConfig config = p2Service.getUpdateSiteConfig(new NullProgressMonitor());
            URI release = config.getLocalRelease(monitor);
            releaseUriText.setText(release == null ? "" : release.toString());
            releaseUriText.setEditable(config.isReleaseEditable());
            if (!config.isReleaseEditable()) {
                releaseUriText.setToolTipText(Messages.getString("UpdatesitePreferencePage.tooltip.cantEdit"));
            }

            enableTmcUpdateSettings = config.isEnableTmcUpdateSettings(monitor);
            FormData fd = (FormData) remotePanel.getLayoutData();
            if (enableTmcUpdateSettings) {
                fd.height = SWT.DEFAULT;

                if (remoteReleaseUriText != null) {
                    String tmcRelease = config.getTmcRelease(monitor);
                    remoteReleaseUriText.setText(tmcRelease);
                }
                if (remoteUpdateUriText != null) {
                    String tmcUpdate = config.getTmcUpdate(monitor);
                    remoteUpdateUriText.setText(tmcUpdate);
                }
            } else {
                fd.height = 0;
                if (isWorkbenchRunning) {
                    FormData owFd = (FormData) overwritePanel.getLayoutData();
                    owFd.height = 0;
                }
            }
            boolean overwriteTmcUpdateSettings = config.isOverwriteTmcUpdateSettings(monitor);
            if (overwriteRemoteUpdateSettingsBtn != null) {
                overwriteRemoteUpdateSettingsBtn.setSelection(overwriteTmcUpdateSettings);
                updateLocalPanelVisible(overwriteTmcUpdateSettings);
            }

            Collection<URI> updates = config.getLocalUpdates(monitor);
            StringBuilder updateStr = new StringBuilder();
            if (updates != null && !updates.isEmpty()) {
                for (String uri : updates.stream().map(uri -> uri.toString()).collect(Collectors.toList())) {
                    if (0 < updateStr.length()) {
                        updateStr.append(",");
                    }
                    updateStr.append(uri);
                }
            }
            updateUriText.setText(updateStr.toString());
            updateUriText.setEditable(config.isUpdateEditable());
            if (!config.isUpdateEditable()) {
                updateUriText.setToolTipText(Messages.getString("UpdatesitePreferencePage.tooltip.cantEdit"));
            }

            // init basic authentication settings
            initBasicAuth(config);
            
            panel.layout();
        } catch (Exception e) {
            ExceptionHandler.process(e);
        }
    }

    private void updateLocalPanelVisible(boolean visible) {
        if (!PlatformUI.isWorkbenchRunning() || !enableTmcUpdateSettings) {
            localPanel.setVisible(true);
        } else {
            localPanel.setVisible(visible);
        }
    }

    private void addListener() {
        releaseUriText.addModifyListener(new ModifyListener() {

            @Override
            public void modifyText(ModifyEvent e) {
                updateBasicAuthUI();
                refresh();
            }
        });
        updateUriText.addModifyListener(new ModifyListener() {

            @Override
            public void modifyText(ModifyEvent e) {
                updateBasicAuthUI();
                refresh();
            }
        });
        if (overwriteRemoteUpdateSettingsBtn != null) {
            overwriteRemoteUpdateSettingsBtn.addSelectionListener(new SelectionAdapter() {

                @Override
                public void widgetSelected(SelectionEvent e) {
                    onOverwriteRemoteUpdateSettingsBtn(e);
                }

            });
        }
        
        enableBaseAuth.addSelectionListener(new SelectionListener() {

            @Override
            public void widgetDefaultSelected(SelectionEvent arg0) {
                // TODO Auto-generated method stub
                
            }

            @Override
            public void widgetSelected(SelectionEvent arg0) {
                baseUserText.setEnabled(enableBaseAuth.getSelection());
                basePasswordText.setEnabled(enableBaseAuth.getSelection());
                
                URI baseURI = getBaseURI();
                URI updateURI = getUpdateURI();
                if (updateURI != null && baseURI != null && StringUtils.equals(baseURI.getHost(), updateURI.getHost())) {
                    enableUpdateAuth.setSelection(enableBaseAuth.getSelection());
                }
                
                refresh();
            }
            
        });
        
        enableUpdateAuth.addSelectionListener(new SelectionListener() {

            @Override
            public void widgetDefaultSelected(SelectionEvent arg0) {
                // TODO Auto-generated method stub
                
            }

            @Override
            public void widgetSelected(SelectionEvent arg0) {
                updateUserText.setEnabled(enableUpdateAuth.getSelection());
                updatePasswordText.setEnabled(enableUpdateAuth.getSelection());
                refresh();
            }
        });
        
        baseUserText.addModifyListener(new ModifyListener() {

            @Override
            public void modifyText(ModifyEvent e) {
                refresh();
            }
        });
        
        basePasswordText.addModifyListener(new ModifyListener() {

            @Override
            public void modifyText(ModifyEvent e) {
                refresh();
            }
        });
        
        updateUserText.addModifyListener(new ModifyListener() {

            @Override
            public void modifyText(ModifyEvent e) {
                refresh();
            }
        });
        
        updatePasswordText.addModifyListener(new ModifyListener() {

            @Override
            public void modifyText(ModifyEvent e) {
                refresh();
            }
        });
    }

    private void onOverwriteRemoteUpdateSettingsBtn(SelectionEvent e) {
        updateLocalPanelVisible(overwriteRemoteUpdateSettingsBtn.getSelection());
    }
    
    private void updateBasicAuthUI() {
        URI baseURI = getBaseURI();
        URI updateURI = getUpdateURI();
        if (baseURI == null) {
            enableBaseAuth.setEnabled(false);
            enableBaseAuth.setSelection(false);
        } else {
            enableBaseAuth.setEnabled(true);
        }
        
        if (updateURI == null) {
            enableUpdateAuth.setEnabled(true);
            enableUpdateAuth.setSelection(false);
        } else {
            if (baseURI != null && StringUtils.equals(baseURI.getHost(), updateURI.getHost())) {
                enableUpdateAuth.setSelection(enableBaseAuth.getSelection());
                enableUpdateAuth.setEnabled(false);
            } else {
                enableUpdateAuth.setEnabled(true);
                enableUpdateAuth.setSelection(false);
            }
        }
    }
    
    private URI getBaseURI() {
        if (StringUtils.isBlank(this.releaseUriText.getText().trim())) {
            return null;
        }
        try {
            URI uri = p2Service.toURI(this.releaseUriText.getText().trim());
            if (isHTTP(uri)) {
                return uri;
            }
        } catch (Exception e) {
            ExceptionHandler.process(e);
        }
        return null;
    }
    
    private URI getUpdateURI() {
        if (StringUtils.isBlank(this.updateUriText.getText().trim())) {
            return null;
        }
        try {
            String[] uriStrs = this.updateUriText.getText().trim().split(",");
            for (String uriStr : uriStrs) {
                URI uri = p2Service.toURI(uriStr);
                if (isHTTP(uri)) {
                    return uri;
                }
            }
        } catch (Exception e) {
            ExceptionHandler.process(e);
        }
        return null;
    }
    
    private static boolean isHTTP(URI uri) {
        if (StringUtils.equals(uri.getScheme(), UpdateSiteConfig.PROTOCOL_HTTP) || StringUtils.equals(uri.getScheme(), UpdateSiteConfig.PROTOCOL_HTTPS)) {
            return true;
        }
        return false;
    }
    
    private void saveBasicAuth(UpdateSiteConfig config) throws Exception {
        URI baseUri = p2Service.toURI(this.releaseUriText.getText().trim());
        if (isHTTP(baseUri)) {
            if (this.enableBaseAuth.getSelection()) {
                UpdateSiteConfig.saveCredentialsIntoSecureStore(baseUri, this.baseUserText.getText().trim(), this.basePasswordText.getText().trim());
                config.enableBasicAuth(baseUri.getHost(), true);
            } else {
                UpdateSiteConfig.deleteCredentialsFromSecureStore(baseUri);
                config.enableBasicAuth(baseUri.getHost(), false);
            }
        }
        if (this.enableUpdateAuth.isEnabled()) {
            URI updateUri = null;
            String[] uriStrs = this.updateUriText.getText().trim().split(",");
            for (String uriStr : uriStrs) {
                URI uri = p2Service.toURI(uriStr);
                if (isHTTP(uri)) {
                    updateUri = uri;
                    // support one http update site
                    break;
                }
            }

            if (updateUri != null) {
                if (this.enableUpdateAuth.getSelection()) {
                    UpdateSiteConfig.saveCredentialsIntoSecureStore(updateUri, this.updateUserText.getText().trim(), this.updatePasswordText.getText().trim());
                    config.enableBasicAuth(updateUri.getHost(), true);
                } else {
                    UpdateSiteConfig.deleteCredentialsFromSecureStore(updateUri);
                    config.enableBasicAuth(updateUri.getHost(), false);
                }
            }
        }
    }

    @Override
    public boolean performOk() {
        if (this.isControlCreated()) {
            try {
                if (m2Delete.getSelection() != p2Service.removeM2()) {
                    p2Service.saveRemoveM2(m2Delete.getSelection());
                }
            } catch (Exception e1) {
                ExceptionHandler.process(e1);
            }
            
            try {
                IProgressMonitor monitor = new NullProgressMonitor();
                UpdateSiteConfig config = p2Service.getUpdateSiteConfig(new NullProgressMonitor());
                if (config.isReleaseEditable()) {
                    String release = releaseUriText.getText();
                    config.setLocalRelease(monitor, StringUtils.isBlank(release) ? null : p2Service.toURI(release.trim()));
                }
                if (config.isUpdateEditable()) {
                    String update = updateUriText.getText();
                    if (StringUtils.isBlank(update)) {
                        config.setLocalUpdates(monitor, null);
                    } else {
                        Collection<URI> updates = new ArrayList<>();
                        String[] splits = update.split(",");
                        for (String split : splits) {
                            updates.add(p2Service.toURI(split.trim()));
                        }
                        config.setLocalUpdates(monitor, updates);
                    }
                }
                if (overwriteRemoteUpdateSettingsBtn != null) {
                    config.overwriteTmcUpdateSettings(monitor, overwriteRemoteUpdateSettingsBtn.getSelection());
                }
                // save basic authentication credentials
                // validate basic auth
                boolean isValid = this.validateBasicAuth();
                if (!isValid) {
                    return false;
                }
                saveBasicAuth(config);
            } catch (Exception e) {
                ExceptionMessageDialog.openError(null, Messages.getString("UpdatesitePreferencePage.err.title"),
                        e.getLocalizedMessage(), e);
            }
        }
        return super.performOk();
    }
    
    private void resetBasicAuth() {
        URI baseUri = getBaseURI();
        URI updateUri = getUpdateURI();
        if (baseUri == null) {
            enableBaseAuth.setEnabled(false);
            this.enableBaseAuth.setSelection(false);
        } else {
            enableBaseAuth.setEnabled(true);
            this.enableBaseAuth.setSelection(false);
        }

        this.baseUserText.setText("");
        this.basePasswordText.setText("");
        this.baseUserText.setEnabled(this.enableBaseAuth.getSelection());
        this.basePasswordText.setEnabled(this.enableBaseAuth.getSelection());

        if (updateUri == null) {
            enableUpdateAuth.setEnabled(false);
            this.enableUpdateAuth.setSelection(false);
        } else {
            if (baseUri != null && StringUtils.equals(baseUri.getHost(), updateUri.getHost())) {
                enableUpdateAuth.setEnabled(false);
                this.enableUpdateAuth.setSelection(false);
            } else {
                enableUpdateAuth.setEnabled(true);
                this.enableUpdateAuth.setSelection(false);
            }
        }
        this.updateUserText.setText("");
        this.updatePasswordText.setText("");
        this.updateUserText.setEnabled(enableUpdateAuth.getSelection());
        this.updatePasswordText.setEnabled(enableUpdateAuth.getSelection());
    }

    @Override
    protected void performDefaults() {
        if (this.isControlCreated()) {
            try {
                UpdateSiteConfig config = p2Service.getUpdateSiteConfig(new NullProgressMonitor());
                if (config.isReleaseEditable() && config.isUpdateEditable()) {
                    NullProgressMonitor monitor = new NullProgressMonitor();
                    config.resetToDefault(monitor);
                    URI release = config.getLocalRelease(monitor);
                    releaseUriText.setText(release == null ? "" : release.toString());
                    Collection<URI> updates = config.getLocalUpdates(monitor);
                    StringBuilder updateStr = new StringBuilder();
                    if (updates != null && !updates.isEmpty()) {
                        updateStr.append(
                                String.join(",", updates.stream().map(uri -> uri.toString()).collect(Collectors.toList())));
                    }
                    updateUriText.setText(updateStr.toString());
//                    if (this.overwriteRemoteUpdateSettingsBtn != null) {
//                        this.overwriteRemoteUpdateSettingsBtn.setSelection(config.isOverwriteTmcUpdateSettings(monitor));
//                        onOverwriteRemoteUpdateSettingsBtn(null);
//                    }
                    
                    // reset basic authentication
                    resetBasicAuth();
                    
                    // set default for m2delete
                    if (System.getProperty(PROPERTY_REMOVE_M2) == null) {
                        m2Delete.setSelection(M2_DELETE_DEFAULT);
                    }
                } else {
                    // normally it should be a dead code
                    throw new Exception(Messages.getString("UpdatesitePreferencePage.err.reset.readonly"));
                }
            } catch (Exception e) {
                ExceptionMessageDialog.openError(null, Messages.getString("UpdatesitePreferencePage.err.title"),
                        e.getLocalizedMessage(), e);
            }
        }
        super.performDefaults();
    }

    private void refresh() {
        this.updateApplyButton();
        this.getContainer().updateButtons();
    }

    private boolean validate() {
        this.setErrorMessage(null);
        checkUpdateUriSettings();
        String releaseUriStr = releaseUriText.getText().trim();
        if (StringUtils.isBlank(releaseUriStr)) {
            this.setErrorMessage(Messages.getString("UpdatesitePreferencePage.err.baseCantEmpty"));
            return false;
        } else if (StringUtils.equals(releaseUriStr, updateUriText.getText().trim())) {
            this.setErrorMessage(Messages.getString("UpdatesitePreferencePage.err.baseAndUpdateShouldBeDiff"));
            return false;
        }
        
        return this.validateBasicAuthInput();
    }
    
    private boolean validateBasicAuthInput() {
        if (enableBaseAuth.getSelection()) {
            try {
                URI baseUri = p2Service.toURI(this.releaseUriText.getText().trim());
                if (isHTTP(baseUri)) {
                    if (StringUtils.isBlank(baseUserText.getText())) {
                        this.setErrorMessage(Messages.getString("UpdatesitePreferencePage.basicAuth.user.empty", baseUri.getHost()));
                        return false;
                    }
                    if (StringUtils.isBlank(basePasswordText.getText())) {
                        this.setErrorMessage(Messages.getString("UpdatesitePreferencePage.basicAuth.password.empty", baseUri.getHost()));
                        return false;
                    }
                }
            } catch (Exception e) {

            }
        }

        if (enableUpdateAuth.isEnabled() && enableUpdateAuth.getSelection()) {
            String[] uriStrs = updateUriText.getText().trim().split(",");
            URI updateUri = null;
            for (int i = 0; i < uriStrs.length; i++) {
                try {
                    URI uri = p2Service.toURI(uriStrs[i]);
                    if (isHTTP(uri)) {
                        updateUri = uri;
                    }
                } catch (Exception e) {

                }
            }

            if (updateUri != null) {
                if (StringUtils.isBlank(updateUserText.getText())) {
                    this.setErrorMessage(Messages.getString("UpdatesitePreferencePage.basicAuth.user.empty", updateUri.getHost()));
                    return false;
                }
                if (StringUtils.isBlank(updatePasswordText.getText())) {
                    this.setErrorMessage(Messages.getString("UpdatesitePreferencePage.basicAuth.password.empty", updateUri.getHost()));
                    return false;
                }
            }
        }
        return true;
    }
    
    private boolean validateBasicAuth() throws Exception {
        if (enableBaseAuth.getSelection()) {
            URI uri = p2Service.toURI(releaseUriText.getText().trim());
            String nameAndPwd = baseUserText.getText().trim() + ":" + basePasswordText.getText().trim();
            boolean requireAuth = UpdateSiteConfig.requireCredentials(uri, nameAndPwd);
            if (requireAuth) {
                warningDesc.setText(Messages.getString("UpdatesitePreferencePage.basicAuth.wrongUserOrPassword", uri.getHost()));
                showWarning(true);
                return false;
            }
        }

        if (enableUpdateAuth.isEnabled() && enableUpdateAuth.getSelection()) {
            String[] uriStrs = updateUriText.getText().trim().split(",");
            for (int i = 0; i < uriStrs.length; i++) {
                URI uri = p2Service.toURI(uriStrs[i]);
                String nameAndPwd = updateUserText.getText().trim() + ":" + updatePasswordText.getText().trim();
                boolean requireAuth = UpdateSiteConfig.requireCredentials(uri, nameAndPwd);
                if (requireAuth) {
                    warningDesc.setText(Messages.getString("UpdatesitePreferencePage.basicAuth.wrongUserOrPassword", uri.getHost()));
                    showWarning(true);
                    return false;
                }
            }
        }
        return true;
    }

    private void checkUpdateUriSettings() {
        String updateUriStr = updateUriText.getText().trim();
        warningDesc.setText(Messages.getString("UpdatesitePreferencePage.warn.onPremUpdateSetup"));
        showWarning(StringUtils.isBlank(updateUriStr));
    }
    
    private void showWarning(boolean show) {
        if (show) {
            ((GridData) warningPanel.getLayoutData()).exclude = false;
            warningPanel.setVisible(true);
        } else {
            ((GridData) warningPanel.getLayoutData()).exclude = true;
            warningPanel.setVisible(false);
        }
        warningPanel.getParent().getParent().layout();
    }

    @Override
    public boolean isValid() {
        return super.isValid() && (this.isControlCreated() && validate());
    }

}
