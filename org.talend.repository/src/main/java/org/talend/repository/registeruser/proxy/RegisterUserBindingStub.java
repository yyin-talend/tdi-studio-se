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
public class RegisterUserBindingStub extends org.apache.axis.client.Stub implements
        org.talend.repository.registeruser.proxy.IRegisterUserPortType {

    private java.util.Vector cachedSerClasses = new java.util.Vector();

    private java.util.Vector cachedSerQNames = new java.util.Vector();

    private java.util.Vector cachedSerFactories = new java.util.Vector();

    private java.util.Vector cachedDeserFactories = new java.util.Vector();

    static org.apache.axis.description.OperationDesc[] operations;

    static {
        operations = new org.apache.axis.description.OperationDesc[2];
        initOperationDesc1();
    }

    /**
     * DOC mhirt Comment method "_initOperationDesc1".
     */
    private static void initOperationDesc1() {
        org.apache.axis.description.OperationDesc oper;
        org.apache.axis.description.ParameterDesc param;
        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("IRegisterUser"); //$NON-NLS-1$
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "email"), //$NON-NLS-1$ //$NON-NLS-2$
                org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName(
                        "http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false); //$NON-NLS-1$ //$NON-NLS-2$
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "country"), //$NON-NLS-1$ //$NON-NLS-2$
                org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName(
                        "http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false); //$NON-NLS-1$ //$NON-NLS-2$
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "designerversion"), //$NON-NLS-1$ //$NON-NLS-2$
                org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName(
                        "http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false); //$NON-NLS-1$ //$NON-NLS-2$
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean")); //$NON-NLS-1$ //$NON-NLS-2$
        oper.setReturnClass(boolean.class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "result")); //$NON-NLS-1$ //$NON-NLS-2$
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        operations[0] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("RegisterUserWithProductName"); //$NON-NLS-1$
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "email"), //$NON-NLS-1$ //$NON-NLS-2$
                org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName(
                        "http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false); //$NON-NLS-1$ //$NON-NLS-2$
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "country"), //$NON-NLS-1$ //$NON-NLS-2$
                org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName(
                        "http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false); //$NON-NLS-1$ //$NON-NLS-2$
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "designerversion"), //$NON-NLS-1$ //$NON-NLS-2$
                org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName(
                        "http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false); //$NON-NLS-1$ //$NON-NLS-2$
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "productname"), //$NON-NLS-1$ //$NON-NLS-2$
                org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName(
                        "http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false); //$NON-NLS-1$ //$NON-NLS-2$
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean")); //$NON-NLS-1$ //$NON-NLS-2$
        oper.setReturnClass(boolean.class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "result")); //$NON-NLS-1$ //$NON-NLS-2$
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        operations[1] = oper;

    }

    public RegisterUserBindingStub() throws org.apache.axis.AxisFault {
        this(null);
    }

    public RegisterUserBindingStub(java.net.URL endpointURL, javax.xml.rpc.Service service)
            throws org.apache.axis.AxisFault {
        this(service);
        super.cachedEndpoint = endpointURL;
    }

    public RegisterUserBindingStub(javax.xml.rpc.Service service) throws org.apache.axis.AxisFault {
        if (service == null) {
            super.service = new org.apache.axis.client.Service();
        } else {
            super.service = service;
        }
        ((org.apache.axis.client.Service) super.service).setTypeMappingVersion("1.2"); //$NON-NLS-1$
    }

    protected org.apache.axis.client.Call createCall() throws java.rmi.RemoteException {
        try {
            org.apache.axis.client.Call call = super._createCall();
            if (super.maintainSessionSet) {
                call.setMaintainSession(super.maintainSession);
            }
            if (super.cachedUsername != null) {
                call.setUsername(super.cachedUsername);
            }
            if (super.cachedPassword != null) {
                call.setPassword(super.cachedPassword);
            }
            if (super.cachedEndpoint != null) {
                call.setTargetEndpointAddress(super.cachedEndpoint);
            }
            if (super.cachedTimeout != null) {
                call.setTimeout(super.cachedTimeout);
            }
            if (super.cachedPortName != null) {
                call.setPortName(super.cachedPortName);
            }
            java.util.Enumeration keys = super.cachedProperties.keys();
            while (keys.hasMoreElements()) {
                java.lang.String key = (java.lang.String) keys.nextElement();
                call.setProperty(key, super.cachedProperties.get(key));
            }
            return call;
        } catch (java.lang.Throwable t) {
            throw new org.apache.axis.AxisFault("Failure trying to get the Call object", t); //$NON-NLS-1$
        }
    }

    public boolean registerUser(java.lang.String email, java.lang.String country, java.lang.String designerversion)
            throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call call = createCall();
        call.setOperation(operations[0]);
        call.setUseSOAPAction(true);
        call.setSOAPActionURI("http://www.talend.com/TalendRegisterWS/registerws.php/IRegisterUser"); //$NON-NLS-1$
        call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        call.setOperationName(new javax.xml.namespace.QName("http://www.talend.com/TalendRegisterWS/wsdl", //$NON-NLS-1$
                "IRegisterUser")); //$NON-NLS-1$

        setRequestHeaders(call);
        setAttachments(call);
        try {
            java.lang.Object resp = call.invoke(new java.lang.Object[] { email, country, designerversion });

            if (resp instanceof java.rmi.RemoteException) {
                throw (java.rmi.RemoteException) resp;
            } else {
                extractAttachments(call);
                try {
                    return ((java.lang.Boolean) resp).booleanValue();
                } catch (java.lang.Exception exception) {
                    return ((java.lang.Boolean) org.apache.axis.utils.JavaUtils.convert(resp, boolean.class))
                            .booleanValue();
                }
            }
        } catch (org.apache.axis.AxisFault axisFaultException) {
            throw axisFaultException;
        }
    }

    public boolean registerUserWithProductName(java.lang.String email, java.lang.String country,
            java.lang.String designerversion, java.lang.String productname) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call call = createCall();
        call.setOperation(operations[1]);
        call.setUseSOAPAction(true);
        call.setSOAPActionURI("http://www.talend.com/TalendRegisterWS/registerws.php/RegisterUserWithProductName"); //$NON-NLS-1$
        call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        call.setOperationName(new javax.xml.namespace.QName("http://www.talend.com/TalendRegisterWS/wsdl", //$NON-NLS-1$
                "RegisterUserWithProductName")); //$NON-NLS-1$

        setRequestHeaders(call);
        setAttachments(call);
        try {
            java.lang.Object resp = call
                    .invoke(new java.lang.Object[] { email, country, designerversion, productname });

            if (resp instanceof java.rmi.RemoteException) {
                throw (java.rmi.RemoteException) resp;
            } else {
                extractAttachments(call);
                try {
                    return ((java.lang.Boolean) resp).booleanValue();
                } catch (java.lang.Exception exception) {
                    return ((java.lang.Boolean) org.apache.axis.utils.JavaUtils.convert(resp, boolean.class))
                            .booleanValue();
                }
            }
        } catch (org.apache.axis.AxisFault axisFaultException) {
            throw axisFaultException;
        }
    }

}
