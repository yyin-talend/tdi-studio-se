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
public interface IRegisterUser extends javax.xml.rpc.Service {

    /**
     * DOC mhirt Comment method "getRegisterUserPortAddress".
     * 
     * @return
     */
    public java.lang.String getRegisterUserPortAddress();

    /**
     * DOC mhirt Comment method "getRegisterUserPort".
     * 
     * @return
     * @throws javax.xml.rpc.ServiceException
     */
    public org.talend.repository.registeruser.proxy.IRegisterUserPortType getRegisterUserPort()
            throws javax.xml.rpc.ServiceException;

    /**
     * DOC mhirt Comment method "getRegisterUserPort".
     * 
     * @param portAddress
     * @return
     * @throws javax.xml.rpc.ServiceException
     */
    public org.talend.repository.registeruser.proxy.IRegisterUserPortType getRegisterUserPort(java.net.URL portAddress)
            throws javax.xml.rpc.ServiceException;
}
