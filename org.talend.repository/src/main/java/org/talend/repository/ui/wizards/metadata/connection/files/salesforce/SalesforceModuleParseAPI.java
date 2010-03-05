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

    private boolean login;

    private ISalesforceModuleParser currentAPI;

    /**
     * DOC YeXiaowei Comment method "login".
     */
    public ArrayList login(String theProxy, String endPoint, String username, String password, String proxyHost,
            String proxyPort, String proxyUsername, String proxyPassword) throws Exception {
        ArrayList returnValues;
        currentAPI = new SalesforceModuleParseEnterprise();
        currentAPI.setLogin(login);
        try {
            returnValues = currentAPI.login(theProxy, endPoint, username, password, proxyHost, proxyPort, proxyUsername,
                    proxyPassword);
        } catch (IOException e) {
            currentAPI = new SalesforceModuleParserPartner();
            currentAPI.setLogin(login);
            returnValues = currentAPI.login(theProxy, endPoint, username, password, proxyHost, proxyPort, proxyUsername,
                    proxyPassword);
        }
        return returnValues;
    }

    public ArrayList login(String endPoint, String username, String password) throws Exception {
        return login(null, endPoint, username, password, null, null, null, null);
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

}
