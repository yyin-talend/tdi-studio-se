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
    private java.lang.String registerUserPortAddress = "http://localhost/TalendRegisterWS/registerws.php";

    public java.lang.String getRegisterUserPortAddress() {
        return registerUserPortAddress;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String registerUserPortWSDDServiceName = "RegisterUserPort";

    /**
     * DOC mhirt Comment method "getRegisterUserPortWSDDServiceName".
     * 
     * @return
     */
    public java.lang.String getRegisterUserPortWSDDServiceName() {
        return registerUserPortWSDDServiceName;
    }

    /**
     * DOC mhirt Comment method "setRegisterUserPortWSDDServiceName".
     * 
     * @param name
     */
    public void setRegisterUserPortWSDDServiceName(java.lang.String name) {
        registerUserPortWSDDServiceName = name;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.registeruser.proxy.IRegisterUser#getRegisterUserPort()
     */
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

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.registeruser.proxy.IRegisterUser#getRegisterUserPort(java.net.URL)
     */
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

    /**
     * DOC mhirt Comment method "setRegisterUserPortEndpointAddress".
     * 
     * @param address
     */
    public void setRegisterUserPortEndpointAddress(java.lang.String address) {
        registerUserPortAddress = address;
    }

    /**
     * For the given interface, get the stub implementation. If this service has no port for the given interface, then
     * ServiceException is thrown.
     */
    @Override
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
        throw new javax.xml.rpc.ServiceException("There is no stub implementation for the interface:  "
                + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
    }

    /**
     * For the given interface, get the stub implementation. If this service has no port for the given interface, then
     * ServiceException is thrown.
     */
    @Override
    public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface)
            throws javax.xml.rpc.ServiceException {
        if (portName == null) {
            return getPort(serviceEndpointInterface);
        }
        java.lang.String inputPortName = portName.getLocalPart();
        if ("RegisterUserPort".equals(inputPortName)) {
            return getRegisterUserPort();
        } else {
            java.rmi.Remote stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) stub).setPortName(portName);
            return stub;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.axis.client.Service#getServiceName()
     */
    @Override
    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://www.talend.com/TalendRegisterWS/wsdl", "RegisterUser");
    }

    private java.util.HashSet ports = null;

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.axis.client.Service#getPorts()
     */
    @Override
    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://www.talend.com/TalendRegisterWS/wsdl", "RegisterUserPort"));
        }
        return ports.iterator();
    }

    /**
     * Set the endpoint address for the specified port name.
     */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address)
            throws javax.xml.rpc.ServiceException {

        if ("RegisterUserPort".equals(portName)) {
            setRegisterUserPortEndpointAddress(address);
        } else { // Unknown Port Name
            throw new javax.xml.rpc.ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
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
