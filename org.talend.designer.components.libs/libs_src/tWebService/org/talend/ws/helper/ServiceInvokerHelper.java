/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
package org.talend.ws.helper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import javax.wsdl.Input;
import javax.wsdl.Message;
import javax.wsdl.Operation;
import javax.wsdl.Output;
import javax.wsdl.Port;
import javax.wsdl.Service;
import javax.wsdl.WSDLException;
import javax.xml.bind.annotation.XmlSchema;
import javax.xml.bind.annotation.XmlType;
import javax.xml.namespace.QName;

import org.apache.commons.lang.StringUtils;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.endpoint.dynamic.DynamicClientFactory;
import org.apache.cxf.jaxb.JAXBUtils;
import org.apache.cxf.jaxb.JAXBUtils.IdentifierType;
import org.apache.cxf.service.model.BindingOperationInfo;
import org.apache.cxf.transport.http.HTTPConduit;
import org.apache.ws.commons.schema.XmlSchemaCollection;
import org.apache.ws.commons.schema.XmlSchemaType;
import org.talend.ws.exception.LocalizedException;
import org.talend.ws.helper.conf.ServiceHelperConfiguration;
import org.talend.ws.helper.map.MapConverter;
import org.talend.ws.mapper.ClassMapper;
import org.talend.ws.mapper.EmptyMessageMapper;
import org.talend.ws.mapper.MapperFactory;
import org.talend.ws.mapper.MessageMapper;

/**
 * 
 * @author rlamarche
 */
public class ServiceInvokerHelper implements ClassMapper {

    private ServiceDiscoveryHelper serviceDiscoveryHelper;

    private DynamicClientFactory dynamicClientFactory;

    private final String packagePrefix;

    private Map<String, String> namespacePackageMap;

    private Map<String, String> packageNamespaceMap;

    private Map<QName, Map<QName, Client>> clients;

    private List<String> bindingFiles;

    private Map<Message, MessageMapper> mappers;

    private MapperFactory mapperFactory;

    private ServiceHelperConfiguration configuration;

    protected ServiceInvokerHelper() {
        packagePrefix = "tmp" + (String.valueOf((new Random(Calendar.getInstance().getTimeInMillis())).nextInt()).substring(1));
        dynamicClientFactory = DynamicClientFactory.newInstance();
        namespacePackageMap = new HashMap<String, String>();
        packageNamespaceMap = new HashMap<String, String>();
        clients = new HashMap<QName, Map<QName, Client>>();
        mappers = new HashMap<Message, MessageMapper>();
    }

    public ServiceInvokerHelper(String wsdlUri) throws WSDLException, IOException {
        this(new ServiceDiscoveryHelper(wsdlUri));
    }

    public ServiceInvokerHelper(String wsdlUri, ServiceHelperConfiguration configuration) throws WSDLException, IOException {
        this(new ServiceDiscoveryHelper(wsdlUri, configuration), configuration);
    }

    public ServiceInvokerHelper(ServiceDiscoveryHelper serviceDiscoveryHelper, ServiceHelperConfiguration configuration) {
        this(serviceDiscoveryHelper);
        this.configuration = configuration;
    }

    public ServiceInvokerHelper(ServiceDiscoveryHelper serviceDiscoveryHelper) {
        this();
        this.serviceDiscoveryHelper = serviceDiscoveryHelper;
        Set<Map.Entry<String, String>> entrySet = serviceDiscoveryHelper.getDefinition().getNamespaces().entrySet();

        bindingFiles = new ArrayList<String>(entrySet.size());
        Set<String> namespaces = new HashSet<String>();
        for (Map.Entry<String, String> entry : entrySet) {
            if (namespaces.contains(entry.getValue())) {
                continue;
            }
            namespaces.add(entry.getValue());
            String packageName = packagePrefix + JAXBUtils.namespaceURIToPackage(entry.getValue());
            namespacePackageMap.put(entry.getValue(), packageName);
            packageNamespaceMap.put(packageName, entry.getValue());

            File f = org.apache.cxf.tools.util.JAXBUtils.getPackageMappingSchemaBindingFile(entry.getValue(), packageName);
            f.deleteOnExit();
            bindingFiles.add(f.getAbsolutePath());
        }
        // bchen , some targetnamespace in schema missing in definitions,code for bug 9900 (for bare webservice)
        XmlSchemaCollection xmlSchemaCollection = serviceDiscoveryHelper.getSchema();
        if (xmlSchemaCollection != null) {
            org.apache.ws.commons.schema.XmlSchema[] xs = xmlSchemaCollection.getXmlSchemas();
            if (xs != null) {
                for (org.apache.ws.commons.schema.XmlSchema x : xs) {
                    if (namespaces.contains(x.getTargetNamespace())) {
                        continue;
                    }
                    namespaces.add(x.getTargetNamespace());
                    String packageName = packagePrefix + JAXBUtils.namespaceURIToPackage(x.getTargetNamespace());
                    namespacePackageMap.put(x.getTargetNamespace(), packageName);
                    packageNamespaceMap.put(packageName, x.getTargetNamespace());

                    File f = org.apache.cxf.tools.util.JAXBUtils.getPackageMappingSchemaBindingFile(x.getTargetNamespace(),
                            packageName);
                    f.deleteOnExit();
                    bindingFiles.add(f.getAbsolutePath());
                }
            }
        }
        // end bchen ,code for bug 9900
        mapperFactory = new MapperFactory(this, serviceDiscoveryHelper.getSchema());
    }

    public Client getClient(QName service, QName port) {
        Map<QName, Client> serviceClients = clients.get(service);
        if (serviceClients == null) {
            serviceClients = new HashMap<QName, Client>();
            clients.put(service, serviceClients);
        }

        if (serviceClients.get(port) == null) {
            serviceClients.put(port, createClient(service, port));
        }

        return serviceClients.get(port);
    }

    protected Client createClient(QName service, QName port) {
        Client client = dynamicClientFactory.createClient(serviceDiscoveryHelper.getLocalWsdlUri(), service, Thread
                .currentThread().getContextClassLoader(), port, bindingFiles);
        HTTPConduit conduit = (HTTPConduit) client.getConduit();
        if (configuration != null) {
            configuration.configureHttpConduit(conduit);
        }

        return client;
    }

    private MessageMapper getMessageMapper(Message message) throws LocalizedException {

        MessageMapper messageMapper = mappers.get(message);
        if (messageMapper == null) {
            messageMapper = createMessageMapper(message);
            mappers.put(message, messageMapper);
        }

        return messageMapper;
    }

    private MessageMapper createMessageMapper(Message message) throws LocalizedException {
        return mapperFactory.createMessageMapper(message);
    }

    protected Map<String, Object> invoke(Client client, Operation operation, Object value) throws Exception, LocalizedException {

        Input input = operation.getInput();
        Output output = operation.getOutput();
        MessageMapper inMessageMapper = null;
        MessageMapper outMessageMapper = null;

        BindingOperationInfo bindingOperationInfo = client.getEndpoint().getEndpointInfo().getBinding().getOperation(
                new QName(client.getEndpoint().getService().getName().getNamespaceURI(), operation.getName()));
        if (input != null) {
            inMessageMapper = getMessageMapper(input.getMessage());
        } else {
            inMessageMapper = new EmptyMessageMapper();
        }
        if (output != null) {
            outMessageMapper = getMessageMapper(output.getMessage());
        } else {
            outMessageMapper = new EmptyMessageMapper();
        }
        if (bindingOperationInfo.isUnwrappedCapable()) {
            inMessageMapper.setUnwrapped(true);
            outMessageMapper.setUnwrapped(true);
        }

        Object[] retParams;
        if (value != null) {
            Object[] params = inMessageMapper.convertToParams(value);
            retParams = client.invoke(operation.getName(), params);
        } else {
            retParams = client.invoke(operation.getName());
        }

        Map<String, Object> retValues = outMessageMapper.convertToValue(retParams);

        return retValues;
    }

    public Map<String, Object> invoke(QName serviceName, QName portName, String operationName, Object params) throws Exception,
            LocalizedException {
        if (serviceName == null) {
            throw new IllegalArgumentException("serviceName is mandatory.");
        }
        Service service = serviceDiscoveryHelper.getDefinition().getService(serviceName);
        if (service == null) {
            throw new IllegalArgumentException("Service " + serviceName.toString() + " does not exists.");
        }

        if (portName == null) {
            throw new IllegalArgumentException("portName is mandatory.");
        }
        Port port = service.getPort(portName.getLocalPart());
        if (port == null) {
            throw new IllegalArgumentException("Port " + portName + " does not exists for service " + serviceName.toString()
                    + ".");
        }
        if (operationName == null) {
            throw new IllegalArgumentException("operationName is mandatory.");
        }
        Operation operation = port.getBinding().getPortType().getOperation(operationName, null, null);
        if (operation == null) {
            throw new IllegalArgumentException("Operation " + operationName + " does not exists for service "
                    + serviceName.toString() + ".");
        }

        Client client = getClient(serviceName, portName);

        return invoke(client, operation, params);
    }

    /**
     * Invoke a service with a simple map of parametes (address.city=LYON, address.zipCode=69003, etc ...) Returned
     * results are also in this format
     * 
     * @param serviceName
     * @param portName
     * @param operationName
     * @param params
     * @return
     * @throws java.lang.Exception
     * @throws org.talend.ws.exception.LocalizedException
     */
    public Map<String, Object> invokeSimple(QName serviceName, QName portName, String operationName, Object params)
            throws Exception, LocalizedException {
        if (params instanceof Map) {
            params = MapConverter.mapToDeepMap((Map<String, Object>) params);
        }

        Map<String, Object> result = invoke(serviceName, portName, operationName, params);

        return MapConverter.deepMapToMap(result);
    }

    protected String getClassNameForType(XmlSchemaType schemaType) {
        StringBuilder sb = new StringBuilder();
        sb.append(getPackageForNamespaceURI(schemaType.getQName().getNamespaceURI()));
        sb.append(".");
        sb.append(getClassNameForTypeName(schemaType.getName()));
        String className = sb.toString();

        return className;
    }

    protected String getPackageForNamespaceURI(String ns) {
        return namespacePackageMap.get(ns);
    }

    protected String getNamespaceURIForPackage(String packageName) {
        return packageNamespaceMap.get(packageName);
    }

    protected String getClassNameForTypeName(String typeName) {
        return toCamelCase(org.apache.cxf.jaxb.JAXBUtils.nameToIdentifier(typeName, IdentifierType.CLASS), true);
    }

    public Class<?> getClassForType(XmlSchemaType xmlSchemaType) {
        String className = getClassNameForType(xmlSchemaType);
        try {
            return Thread.currentThread().getContextClassLoader().loadClass(className);
        } catch (ClassNotFoundException ex) {
            throw new RuntimeException(ex);
        }
    }

    public XmlSchemaType getTypeForClass(Class<?> clazz) {
        if (clazz.isAnnotationPresent(XmlType.class)) {
            XmlType type = clazz.getAnnotation(XmlType.class);
            XmlSchema schema = clazz.getPackage().getAnnotation(XmlSchema.class);
            QName qname = new QName(schema.namespace(), type.name());

            return serviceDiscoveryHelper.getSchema().getTypeByQName(qname);
        } else {
            QName type = MapperFactory.javaTypeToBuiltInType(clazz.getName());
            if (type != null) {
                return serviceDiscoveryHelper.getSchema().getTypeByQName(type);
            } else {
                throw new IllegalArgumentException("Unmapped class : " + clazz.getName());
            }
        }
    }

    public ServiceDiscoveryHelper getServiceDiscoveryHelper() {
        return serviceDiscoveryHelper;
    }

    private String toCamelCase(String value, boolean startWithLowerCase) {
        String[] strings = StringUtils.split(value, "_");
        for (int i = startWithLowerCase ? 1 : 0; i < strings.length; i++) {
            strings[i] = StringUtils.capitalize(strings[i]);
        }
        return StringUtils.join(strings);
    }
}
