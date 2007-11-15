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
package org.talend.repository.registeruser.proxy;

/**
 * DOC mhirt class global comment. Detailled comment <br/>
 * 
 */
public class RegisterUserPortTypeProxy implements org.talend.repository.registeruser.proxy.IRegisterUserPortType {

    private String endpoint = null;

    private org.talend.repository.registeruser.proxy.IRegisterUserPortType registerUserPortType = null;

    public RegisterUserPortTypeProxy() {
        initRegisterUserPortTypeProxy();
    }

    public RegisterUserPortTypeProxy(String endpoint) {
        this.endpoint = endpoint;
        initRegisterUserPortTypeProxy();
    }

    /**
     * DOC mhirt Comment method "initRegisterUserPortTypeProxy".
     */
    private void initRegisterUserPortTypeProxy() {
        try {
            registerUserPortType = (new org.talend.repository.registeruser.proxy.RegisterUserLocator())
                    .getRegisterUserPort();
            if (registerUserPortType != null) {
                if (endpoint != null) {
                    ((javax.xml.rpc.Stub) registerUserPortType)._setProperty("javax.xml.rpc.service.endpoint.address",
                            endpoint);
                } else {
                    endpoint = (String) ((javax.xml.rpc.Stub) registerUserPortType)
                            ._getProperty("javax.xml.rpc.service.endpoint.address");
                }
            }
        } catch (javax.xml.rpc.ServiceException serviceException) {
            // Do nothing
        }
    }

    /**
     * DOC mhirt Comment method "getEndpoint".
     * 
     * @return
     */
    public String getEndpoint() {
        return endpoint;
    }

    /**
     * DOC mhirt Comment method "setEndpoint".
     * 
     * @param endpoint
     */
    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
        if (registerUserPortType != null) {
            ((javax.xml.rpc.Stub) registerUserPortType)
                    ._setProperty("javax.xml.rpc.service.endpoint.address", endpoint);
        }
    }

    /**
     * DOC mhirt Comment method "getRegisterUserPortType".
     * 
     * @return
     */
    public org.talend.repository.registeruser.proxy.IRegisterUserPortType getRegisterUserPortType() {
        if (registerUserPortType == null) {
            initRegisterUserPortTypeProxy();
        }
        return registerUserPortType;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.registeruser.proxy.IRegisterUserPortType#registerUser(java.lang.String,
     * java.lang.String, java.lang.String)
     */
    public boolean registerUser(java.lang.String email, java.lang.String country, java.lang.String designerversion)
            throws java.rmi.RemoteException {
        if (registerUserPortType == null) {
            initRegisterUserPortTypeProxy();
        }
        return registerUserPortType.registerUser(email, country, designerversion);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.registeruser.proxy.IRegisterUserPortType#registerUserWithProductName(java.lang.String,
     * java.lang.String, java.lang.String, java.lang.String)
     */
    public boolean registerUserWithProductName(java.lang.String email, java.lang.String country,
            java.lang.String designerversion, java.lang.String productname) throws java.rmi.RemoteException {
        if (registerUserPortType == null) {
            initRegisterUserPortTypeProxy();
        }
        return registerUserPortType.registerUserWithProductName(email, country, designerversion, productname);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.registeruser.proxy.IRegisterUserPortType#registerUserWithAllUserInformations(java.lang.String,
     * java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String,
     * java.lang.String, java.lang.String, java.lang.String, java.lang.String)
     */
    public boolean registerUserWithAllUserInformations(java.lang.String email, java.lang.String country,
            java.lang.String designerversion, java.lang.String productname, java.lang.String projectLanguage,
            java.lang.String osName, java.lang.String osVersion, java.lang.String javaVersion,
            java.lang.String totalMemory, java.lang.String memRAM, java.lang.String nbProc)
            throws java.rmi.RemoteException {
        if (registerUserPortType == null) {
            initRegisterUserPortTypeProxy();
        }
        return registerUserPortType.registerUserWithAllUserInformations(email, country, designerversion, productname,
                projectLanguage, osName, osVersion, javaVersion, totalMemory, memRAM, nbProc);
    }
}
