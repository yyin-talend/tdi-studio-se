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
public class RegisterUserBindingStub extends org.apache.axis.client.Stub implements
        org.talend.repository.registeruser.proxy.IRegisterUserPortType {

    private final java.util.Vector cachedSerClasses = new java.util.Vector();

    private final java.util.Vector cachedSerQNames = new java.util.Vector();

    private final java.util.Vector cachedSerFactories = new java.util.Vector();

    private final java.util.Vector cachedDeserFactories = new java.util.Vector();

    static org.apache.axis.description.OperationDesc[] operations;

    static {
        operations = new org.apache.axis.description.OperationDesc[4];
        initOperationDesc1();
    }

    /**
     * DOC mhirt Comment method "initOperationDesc1".
     */
    private static void initOperationDesc1() {
        org.apache.axis.description.OperationDesc oper;
        org.apache.axis.description.ParameterDesc param;
        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("RegisterUser");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "email"),
                org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName(
                        "http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "country"),
                org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName(
                        "http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "designerversion"),
                org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName(
                        "http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        oper.setReturnClass(boolean.class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "result"));
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        operations[0] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("RegisterUserWithProductName");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "email"),
                org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName(
                        "http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "country"),
                org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName(
                        "http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "designerversion"),
                org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName(
                        "http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "productname"),
                org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName(
                        "http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        oper.setReturnClass(boolean.class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "result"));
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        operations[1] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("RegisterUserWithAllUserInformations");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "email"),
                org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName(
                        "http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "country"),
                org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName(
                        "http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "designerversion"),
                org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName(
                        "http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "productname"),
                org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName(
                        "http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "projectLanguage"),
                org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName(
                        "http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "osName"),
                org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName(
                        "http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "osVersion"),
                org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName(
                        "http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "javaVersion"),
                org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName(
                        "http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "totalMemory"),
                org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName(
                        "http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "memRAM"),
                org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName(
                        "http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "nbProc"),
                org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName(
                        "http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        oper.setReturnClass(boolean.class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "result"));
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        operations[2] = oper;
    }

    /**
     * DOC mhirt RegisterUserBindingStub constructor comment.
     * 
     * @throws org.apache.axis.AxisFault
     */
    public RegisterUserBindingStub() throws org.apache.axis.AxisFault {
        this(null);
    }

    /**
     * DOC mhirt RegisterUserBindingStub constructor comment.
     * 
     * @param endpointURL
     * @param service
     * @throws org.apache.axis.AxisFault
     */
    public RegisterUserBindingStub(java.net.URL endpointURL, javax.xml.rpc.Service service)
            throws org.apache.axis.AxisFault {
        this(service);
        super.cachedEndpoint = endpointURL;
    }

    /**
     * DOC mhirt RegisterUserBindingStub constructor comment.
     * 
     * @param service
     * @throws org.apache.axis.AxisFault
     */
    public RegisterUserBindingStub(javax.xml.rpc.Service service) throws org.apache.axis.AxisFault {
        if (service == null) {
            super.service = new org.apache.axis.client.Service();
        } else {
            super.service = service;
        }
        ((org.apache.axis.client.Service) super.service).setTypeMappingVersion("1.2");
        java.lang.Class cls;
        javax.xml.namespace.QName qName;
        javax.xml.namespace.QName qName2;
        java.lang.Class beansf = org.apache.axis.encoding.ser.BeanSerializerFactory.class;
        java.lang.Class beandf = org.apache.axis.encoding.ser.BeanDeserializerFactory.class;
        java.lang.Class enumsf = org.apache.axis.encoding.ser.EnumSerializerFactory.class;
        java.lang.Class enumdf = org.apache.axis.encoding.ser.EnumDeserializerFactory.class;
        java.lang.Class arraysf = org.apache.axis.encoding.ser.ArraySerializerFactory.class;
        java.lang.Class arraydf = org.apache.axis.encoding.ser.ArrayDeserializerFactory.class;
        java.lang.Class simplesf = org.apache.axis.encoding.ser.SimpleSerializerFactory.class;
        java.lang.Class simpledf = org.apache.axis.encoding.ser.SimpleDeserializerFactory.class;
        java.lang.Class simplelistsf = org.apache.axis.encoding.ser.SimpleListSerializerFactory.class;
        java.lang.Class simplelistdf = org.apache.axis.encoding.ser.SimpleListDeserializerFactory.class;
        qName = new javax.xml.namespace.QName("http://www.talend.com/TalendRegisterWS/wsdl", "UserRegistration");
        cachedSerQNames.add(qName);
        cls = org.talend.repository.registeruser.proxy.UserRegistration.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);

        qName = new javax.xml.namespace.QName("http://www.talend.com/TalendRegisterWS/wsdl", "UserRegistrations");
        cachedSerQNames.add(qName);
        cls = org.talend.repository.registeruser.proxy.UserRegistration[].class;
        cachedSerClasses.add(cls);
        qName = new javax.xml.namespace.QName("http://www.talend.com/TalendRegisterWS/wsdl", "UserRegistration");
        qName2 = null;
        cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
        cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

    }

    /**
     * DOC mhirt Comment method "createCall".
     * 
     * @return
     * @throws java.rmi.RemoteException
     */
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
            // All the type mapping information is registered
            // when the first call is made.
            // The type mapping information is actually registered in
            // the TypeMappingRegistry of the service, which
            // is the reason why registration is only needed for the first call.
            synchronized (this) {
                if (firstCall()) {
                    // must set encoding style before registering serializers
                    call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
                    call.setEncodingStyle(org.apache.axis.Constants.URI_SOAP11_ENC);
                    for (int i = 0; i < cachedSerFactories.size(); ++i) {
                        java.lang.Class cls = (java.lang.Class) cachedSerClasses.get(i);
                        javax.xml.namespace.QName qName = (javax.xml.namespace.QName) cachedSerQNames.get(i);
                        java.lang.Object x = cachedSerFactories.get(i);
                        if (x instanceof Class) {
                            java.lang.Class sf = (java.lang.Class) cachedSerFactories.get(i);
                            java.lang.Class df = (java.lang.Class) cachedDeserFactories.get(i);
                            call.registerTypeMapping(cls, qName, sf, df, false);
                        } else if (x instanceof javax.xml.rpc.encoding.SerializerFactory) {
                            org.apache.axis.encoding.SerializerFactory sf = (org.apache.axis.encoding.SerializerFactory) cachedSerFactories
                                    .get(i);
                            org.apache.axis.encoding.DeserializerFactory df = (org.apache.axis.encoding.DeserializerFactory) cachedDeserFactories
                                    .get(i);
                            call.registerTypeMapping(cls, qName, sf, df, false);
                        }
                    }
                }
            }
            return call;
        } catch (java.lang.Throwable t) {
            throw new org.apache.axis.AxisFault("Failure trying to get the Call object", t);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.registeruser.proxy.IRegisterUserPortType#registerUser(java.lang.String,
     * java.lang.String, java.lang.String)
     */
    public boolean registerUser(java.lang.String email, java.lang.String country, java.lang.String designerversion)
            throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call call = createCall();
        call.setOperation(operations[0]);
        call.setUseSOAPAction(true);
        call.setSOAPActionURI("http://localhost/TalendRegisterWS/registerws.php/RegisterUser");
        call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        call.setOperationName(new javax.xml.namespace.QName("http://www.talend.com/TalendRegisterWS/wsdl",
                "RegisterUser"));

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

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.registeruser.proxy.IRegisterUserPortType#registerUserWithProductName(java.lang.String,
     * java.lang.String, java.lang.String, java.lang.String)
     */
    public boolean registerUserWithProductName(java.lang.String email, java.lang.String country,
            java.lang.String designerversion, java.lang.String productname) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call call = createCall();
        call.setOperation(operations[1]);
        call.setUseSOAPAction(true);
        call.setSOAPActionURI("http://localhost/TalendRegisterWS/registerws.php/RegisterUserWithProductName");
        call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        call.setOperationName(new javax.xml.namespace.QName("http://www.talend.com/TalendRegisterWS/wsdl",
                "RegisterUserWithProductName"));

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
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call call = createCall();
        call.setOperation(operations[2]);
        call.setUseSOAPAction(true);
        call.setSOAPActionURI("http://localhost/TalendRegisterWS/registerws.php/RegisterUserWithAllUserInformations");
        call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        call.setOperationName(new javax.xml.namespace.QName("http://www.talend.com/TalendRegisterWS?wsdl",
                "RegisterUserWithAllUserInformations"));

        setRequestHeaders(call);
        setAttachments(call);
        try {
            java.lang.Object resp = call.invoke(new java.lang.Object[] { email, country, designerversion, productname,
                    projectLanguage, osName, osVersion, javaVersion, totalMemory, memRAM, nbProc });

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
            axisFaultException.printStackTrace();
            throw axisFaultException;
        }
    }
}
