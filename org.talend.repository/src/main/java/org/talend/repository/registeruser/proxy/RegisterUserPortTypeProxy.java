// ============================================================================
//
// Talend Community Edition
//
// Copyright (C) 2006-2007 Talend - www.talend.com
//
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either
// version 2.1 of the License.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
// Lesser General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with this program; if not, write to the Free Software
// Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
//
// ============================================================================

package org.talend.repository.registeruser.proxy;

import org.talend.repository.i18n.Messages;

/**
 * DOC mhirt class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 */
public class RegisterUserPortTypeProxy implements org.talend.repository.registeruser.proxy.IRegisterUserPortType {

    private String endpoint = null;

    private org.talend.repository.registeruser.proxy.IRegisterUserPortType registerUserPortType = null;

    public RegisterUserPortTypeProxy() {
        initRegisterUserPortTypeProxy();
    }

    private void initRegisterUserPortTypeProxy() {
        try {
            registerUserPortType = (new org.talend.repository.registeruser.proxy.RegisterUserLocator())
                    .getRegisterUserPort();
            if (registerUserPortType != null) {
                if (endpoint != null) {
                    ((javax.xml.rpc.Stub) registerUserPortType)._setProperty("javax.xml.rpc.service.endpoint.address", //$NON-NLS-1$
                            endpoint);
                } else {
                    endpoint = (String) ((javax.xml.rpc.Stub) registerUserPortType)
                            ._getProperty("javax.xml.rpc.service.endpoint.address"); //$NON-NLS-1$
                }
            }

        } catch (javax.xml.rpc.ServiceException serviceException) {
            //
        }
    }

    public String getEndpoint() {
        return this.endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
        if (registerUserPortType != null) {
            ((javax.xml.rpc.Stub) registerUserPortType)
                    ._setProperty("javax.xml.rpc.service.endpoint.address", endpoint); //$NON-NLS-1$
        }

    }

    public org.talend.repository.registeruser.proxy.IRegisterUserPortType getRegisterUserPortType() {
        if (registerUserPortType == null) {
            initRegisterUserPortTypeProxy();
        }
        return registerUserPortType;
    }

    public boolean registerUser(java.lang.String email, java.lang.String country, java.lang.String designerversion)
            throws java.rmi.RemoteException {
        if (registerUserPortType == null) {
            initRegisterUserPortTypeProxy();
        }
        return registerUserPortType.registerUser(email, country, designerversion);
    }

    public boolean registerUserWithProductName(java.lang.String email, java.lang.String country,
            java.lang.String designerversion, java.lang.String productname) throws java.rmi.RemoteException {
        if (registerUserPortType == null) {
            initRegisterUserPortTypeProxy();
        }
        return registerUserPortType.registerUserWithProductName(email, country, designerversion, productname);
    }

}
