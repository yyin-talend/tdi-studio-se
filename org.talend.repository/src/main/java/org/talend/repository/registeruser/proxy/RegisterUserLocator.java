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
 * DOC mhirt  class global comment. Detailled comment
 * <br/>
 *
 * $Id$
 *
 */
public class RegisterUserLocator extends org.apache.axis.client.Service implements
        org.talend.repository.registeruser.proxy.IRegisterUser {

    public RegisterUserLocator() {
    }

    public RegisterUserLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public RegisterUserLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName)
            throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for RegisterUserPort
    private java.lang.String registerUserPortAddress = "http://www.talend.com/TalendRegisterWS/registerws.php"; //$NON-NLS-1$

    public java.lang.String getRegisterUserPortAddress() {
        return registerUserPortAddress;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String registerUserPortWSDDServiceName = "RegisterUserPort"; //$NON-NLS-1$

    public java.lang.String getRegisterUserPortWSDDServiceName() {
        return registerUserPortWSDDServiceName;
    }

    public void setRegisterUserPortWSDDServiceName(java.lang.String name) {
        registerUserPortWSDDServiceName = name;
    }

    public org.talend.repository.registeruser.proxy.IRegisterUserPortType getRegisterUserPort()
            throws javax.xml.rpc.ServiceException {
        java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(registerUserPortAddress);
        } catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getRegisterUserPort(endpoint);
    }

    public org.talend.repository.registeruser.proxy.IRegisterUserPortType getRegisterUserPort(java.net.URL portAddress)
            throws javax.xml.rpc.ServiceException {
        try {
            org.talend.repository.registeruser.proxy.RegisterUserBindingStub stub = new org.talend.repository.registeruser.proxy.RegisterUserBindingStub(
                    portAddress, this);
            stub.setPortName(getRegisterUserPortWSDDServiceName());
            return stub;
        } catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setRegisterUserPortEndpointAddress(java.lang.String address) {
        registerUserPortAddress = address;
    }

    /**
     * For the given interface, get the stub implementation. If this service has no port for the given interface, then
     * ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (org.talend.repository.registeruser.proxy.IRegisterUserPortType.class
                    .isAssignableFrom(serviceEndpointInterface)) {
                org.talend.repository.registeruser.proxy.RegisterUserBindingStub stub = new org.talend.repository.registeruser.proxy.RegisterUserBindingStub(
                        new java.net.URL(registerUserPortAddress), this);
                stub.setPortName(getRegisterUserPortWSDDServiceName());
                return stub;
            }
        } catch (java.lang.Throwable t) {
            throw new javax.xml.rpc.ServiceException(t);
        }
        throw new javax.xml.rpc.ServiceException(Messages.getString("RegisterUserLocator.serviceException.noStubImpl") //$NON-NLS-1$
                + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName())); //$NON-NLS-1$
    }

    /**
     * For the given interface, get the stub implementation. If this service has no port for the given interface, then
     * ServiceException is thrown.
     */
    public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface)
            throws javax.xml.rpc.ServiceException {
        if (portName == null) {
            return getPort(serviceEndpointInterface);
        }
        java.lang.String inputPortName = portName.getLocalPart();
        if ("RegisterUserPort".equals(inputPortName)) { //$NON-NLS-1$
            return getRegisterUserPort();
        } else {
            java.rmi.Remote stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) stub).setPortName(portName);
            return stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://www.talend.com/TalendRegisterWS/wsdl", "IRegisterUser"); //$NON-NLS-1$ //$NON-NLS-2$
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://www.talend.com/TalendRegisterWS/wsdl", "RegisterUserPort")); //$NON-NLS-1$ //$NON-NLS-2$
        }
        return ports.iterator();
    }

    /**
     * Set the endpoint address for the specified port name.
     */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address)
            throws javax.xml.rpc.ServiceException {

        if ("RegisterUserPort".equals(portName)) { //$NON-NLS-1$
            setRegisterUserPortEndpointAddress(address);
        } else { // Unknown Port Name
            throw new javax.xml.rpc.ServiceException(Messages.getString("RegisterUserLocator.serviceException.cannotSetEndpoint") + portName); //$NON-NLS-1$
        }
    }

    /**
     * Set the endpoint address for the specified port name.
     */
    public void setEndpointAddress(javax.xml.namespace.QName portName, java.lang.String address)
            throws javax.xml.rpc.ServiceException {
        setEndpointAddress(portName.getLocalPart(), address);
    }

}
