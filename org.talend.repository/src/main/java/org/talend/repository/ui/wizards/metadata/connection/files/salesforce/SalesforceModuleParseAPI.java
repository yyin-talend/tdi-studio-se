// ============================================================================
//
// Copyright (C) 2006-2010 Talend Inc. - www.talend.com
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.talend.core.model.metadata.IMetadataColumn;

/**
 * Maybe need a long connection ...
 * <p>
 * DOC YeXiaowei class global comment. Detailled comment <br/>
 * 
 */
public class SalesforceModuleParseAPI {

    public static final String SOCKS_PROXY_HOST = "socksProxyHost"; //$NON-NLS-1$

    public static final String SOCKS_PROXY_PORT = "socksProxyPort"; //$NON-NLS-1$

    public static final String SOCKS_PROXY_USERNAME = "java.net.socks.username"; //$NON-NLS-1$

    public static final String SOCKS_PROXY_PASSWORD = "java.net.socks.password"; //$NON-NLS-1$

    public static final String HTTP_PROXY_HOST = "http.proxyHost"; //$NON-NLS-1$

    public static final String HTTP_PROXY_PORT = "http.proxyPort"; //$NON-NLS-1$

    public static final String HTTP_PROXY_USER = "http.proxyUser";//$NON-NLS-1$

    public static final String HTTP_PROXY_PASSWORD = "http.proxyPassword";//$NON-NLS-1$

    public static final String HTTP_PROXY_SET = "http.proxySet";//$NON-NLS-1$

    public static final String USE_SOCKS_PROXY = "useProxyBtn";

    public static final String USE_HTTP_PROXY = "useHttpBtn";

    private String oldProxyHost;

    private String oldProxyPort;

    private String oldProxyUser;

    private String oldProxyPwd;

    private String oldHttpProxySet;

    private boolean socksProxy;

    private boolean httpProxy;

    private boolean login;

    private ISalesforceModuleParser currentAPI;

    /**
     * DOC YeXiaowei Comment method "login".
     */
    public ArrayList login(String endPoint, String username, String password) throws Exception {
        ArrayList returnValues;
        currentAPI = new SalesforceModuleParseEnterprise();
        currentAPI.setLogin(login);
        try {
            returnValues = currentAPI.login(endPoint, username, password);
        } catch (IOException e) {
            currentAPI = new SalesforceModuleParserPartner();
            currentAPI.setLogin(login);
            returnValues = currentAPI.login(endPoint, username, password);
        }
        return returnValues;
    }

    /**
     * Fetch a module from SF and transfor to Talend metadata data type. DOC YeXiaowei Comment method
     * "fetchMetaDataColumns".
     * 
     * @param module
     * @return
     */
    public List<IMetadataColumn> fetchMetaDataColumns(String module) {
        return currentAPI.fetchMetaDataColumns(module);
    }

    /**
     * Getter for login.
     * 
     * @return the login
     */
    public boolean isLogin() {
        return login;
    }

    /**
     * Sets the login.
     * 
     * @param login the login to set
     */
    public void setLogin(boolean login) {
        this.login = login;
    }

    /**
     * Getter for currentModuleName.
     * 
     * @return the currentModuleName
     */
    public String getCurrentModuleName() {
        if (currentAPI != null) {
            return currentAPI.getCurrentModuleName();
        }
        return null;
    }

    /**
     * Sets the currentModuleName.
     * 
     * @param currentModuleName the currentModuleName to set
     */
    public void setCurrentModuleName(String currentModuleName) {
        currentAPI.setCurrentModuleName(currentModuleName);
    }

    /**
     * Getter for currentMetadataColumns.
     * 
     * @return the currentMetadataColumns
     */
    public List<IMetadataColumn> getCurrentMetadataColumns() {
        return currentAPI.getCurrentMetadataColumns();
    }

    /**
     * Sets the currentMetadataColumns.
     * 
     * @param currentMetadataColumns the currentMetadataColumns to set
     */
    public void setCurrentMetadataColumns(List<IMetadataColumn> currentMetadataColumns) {
        currentAPI.setCurrentMetadataColumns(currentMetadataColumns);
    }

    public ISalesforceModuleParser getCurrentAPI() {
        return this.currentAPI;
    }

    public void setProxy(String proxyHost, String proxyPort, String proxyUsername, String proxyPassword, boolean httpProxy,
            boolean socksProxy) {
        Properties properties = System.getProperties();
        this.socksProxy = false;
        this.httpProxy = false;
        if (socksProxy && proxyHost != null && proxyPort != null) { //$NON-NLS-1$ 
            this.socksProxy = true;
            oldProxyHost = (String) properties.get(SalesforceModuleParseAPI.SOCKS_PROXY_HOST);
            properties.put(SalesforceModuleParseAPI.SOCKS_PROXY_HOST, proxyHost);
            oldProxyPort = (String) properties.get(SalesforceModuleParseAPI.SOCKS_PROXY_PORT);
            properties.put(SalesforceModuleParseAPI.SOCKS_PROXY_PORT, proxyPort);
            oldProxyUser = (String) properties.get(SalesforceModuleParseAPI.SOCKS_PROXY_USERNAME);
            properties.put(SalesforceModuleParseAPI.SOCKS_PROXY_USERNAME, proxyUsername == null ? "" : proxyUsername); //$NON-NLS-1$
            oldProxyPwd = (String) properties.get(SalesforceModuleParseAPI.SOCKS_PROXY_PASSWORD);
            properties.put(SalesforceModuleParseAPI.SOCKS_PROXY_PASSWORD, proxyPassword == null ? "" : proxyPassword); //$NON-NLS-1$
        } else if (httpProxy && proxyHost != null && proxyPort != null) { //$NON-NLS-1$ 
            this.httpProxy = true;
            oldHttpProxySet = (String) properties.get(SalesforceModuleParseAPI.HTTP_PROXY_SET);
            oldProxyHost = (String) properties.get(SalesforceModuleParseAPI.HTTP_PROXY_HOST);
            oldProxyPort = (String) properties.get(SalesforceModuleParseAPI.HTTP_PROXY_PORT);
            oldProxyUser = (String) properties.get(SalesforceModuleParseAPI.HTTP_PROXY_USER);
            oldProxyPwd = (String) properties.get(SalesforceModuleParseAPI.HTTP_PROXY_PASSWORD);

            properties.put(SalesforceModuleParseAPI.HTTP_PROXY_SET, "true"); //$NON-NLS-1$
            properties.put(SalesforceModuleParseAPI.HTTP_PROXY_HOST, proxyHost);
            properties.put(SalesforceModuleParseAPI.HTTP_PROXY_PORT, proxyPort);
            properties.put(SalesforceModuleParseAPI.HTTP_PROXY_USER, proxyUsername == null ? "" : proxyUsername); //$NON-NLS-1$
            properties.put(SalesforceModuleParseAPI.HTTP_PROXY_PASSWORD, proxyPassword == null ? "" : proxyPassword); //$NON-NLS-1$
        }
    }

    public void resetProxy() {
        Properties properties = System.getProperties();
        if (socksProxy) {
            properties.put(SalesforceModuleParseAPI.SOCKS_PROXY_HOST, oldProxyHost == null ? "" : oldProxyHost); //$NON-NLS-1$
            properties.put(SalesforceModuleParseAPI.SOCKS_PROXY_PORT, oldProxyPort == null ? "" : oldProxyPort); //$NON-NLS-1$
            properties.put(SalesforceModuleParseAPI.SOCKS_PROXY_USERNAME, oldProxyUser == null ? "" : oldProxyUser); //$NON-NLS-1$
            properties.put(SalesforceModuleParseAPI.SOCKS_PROXY_PASSWORD, oldProxyPwd == null ? "" : oldProxyPwd); //$NON-NLS-1$
        }

        if (httpProxy) {
            properties.put(SalesforceModuleParseAPI.HTTP_PROXY_SET, oldHttpProxySet == null ? "" : oldHttpProxySet); //$NON-NLS-1$
            properties.put(SalesforceModuleParseAPI.HTTP_PROXY_HOST, oldProxyHost == null ? "" : oldProxyHost); //$NON-NLS-1$
            properties.put(SalesforceModuleParseAPI.HTTP_PROXY_PORT, oldProxyPort == null ? "" : oldProxyPort); //$NON-NLS-1$
            properties.put(SalesforceModuleParseAPI.HTTP_PROXY_USER, oldProxyUser == null ? "" : oldProxyUser); //$NON-NLS-1$
            properties.put(SalesforceModuleParseAPI.HTTP_PROXY_PASSWORD, oldProxyPwd == null ? "" : oldProxyPwd); //$NON-NLS-1$
        }

    }

}
