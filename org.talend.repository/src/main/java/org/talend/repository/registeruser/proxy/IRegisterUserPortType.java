// ============================================================================
//
// Copyright (C) 2006-2007 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the  agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//   
// ============================================================================
package org.talend.repository.registeruser.proxy;

/**
 * DOC mhirt class global comment. Detailled comment <br/>
 * 
 */
public interface IRegisterUserPortType extends java.rmi.Remote {

    /**
     * DOC mhirt Comment method "registerUser".
     * 
     * @param email
     * @param country
     * @param designerversion
     * @return
     * @throws java.rmi.RemoteException
     */
    public boolean registerUser(java.lang.String email, java.lang.String country, java.lang.String designerversion)
            throws java.rmi.RemoteException;

    /**
     * DOC mhirt Comment method "registerUserWithProductName".
     * 
     * @param email
     * @param country
     * @param designerversion
     * @param productname
     * @return
     * @throws java.rmi.RemoteException
     */
    public boolean registerUserWithProductName(java.lang.String email, java.lang.String country,
            java.lang.String designerversion, java.lang.String productname) throws java.rmi.RemoteException;

    /**
     * DOC mhirt Comment method "registerUserWithAllUserInformations".
     * 
     * @param email
     * @param country
     * @param designerversion
     * @param productname
     * @param projectLanguage
     * @param osName
     * @param osVersion
     * @param javaVersion
     * @param totalMemory
     * @param memRAM
     * @param nbProc
     * @return
     * @throws java.rmi.RemoteException
     */
    public boolean registerUserWithAllUserInformations(java.lang.String email, java.lang.String country,
            java.lang.String designerversion, java.lang.String productname, java.lang.String projectLanguage,
            java.lang.String osName, java.lang.String osVersion, java.lang.String javaVersion,
            java.lang.String totalMemory, java.lang.String memRAM, java.lang.String nbProc)
            throws java.rmi.RemoteException;
}
