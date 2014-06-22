/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
package org.talend.designer.webservice.ws.wsdlutil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.wsdl.Definition;
import javax.wsdl.WSDLException;
import javax.wsdl.factory.WSDLFactory;
import javax.wsdl.xml.WSDLReader;

import com.ibm.wsdl.ImportImpl;

/**
 * This helper allow easy discovery of services and types
 * 
 * @author rlamarche
 */
public class ServiceDiscoveryHelper {

    private String wsdlUri;

    private WSDLFactory wsdlFactory;

    private Definition definition;

    private List<Definition> definitions;

    private ServiceHelperConfiguration configuration;

    public ServiceDiscoveryHelper(String wsdlUri) throws WSDLException, IOException {
        this(wsdlUri, null);
    }

    public ServiceDiscoveryHelper(String wsdlUri, ServiceHelperConfiguration configuration) throws WSDLException, IOException {
        this.wsdlUri = wsdlUri;
        this.configuration = configuration;
        init();
    }

    /**
     * Read the wsdl and schema
     * 
     * @throws javax.wsdl.WSDLException
     */
    private void init() throws WSDLException, IOException {
        wsdlFactory = WSDLFactory.newInstance();
        // wsdlFactory.
        WSDLReader newWSDLReader = wsdlFactory.newWSDLReader();

        newWSDLReader.setExtensionRegistry(wsdlFactory.newPopulatedExtensionRegistry());
        // newWSDLReader.getExtensionRegistry()
        newWSDLReader.setFeature(com.ibm.wsdl.Constants.FEATURE_VERBOSE, false);
        if (configuration == null) {
            definition = newWSDLReader.readWSDL(wsdlUri);
        } else {
            definition = newWSDLReader.readWSDL(configuration.createWSDLLocator(wsdlUri));
        }
        definitions = findWsdlImport(definition, null, null, null);
    }

    private List<Definition> findWsdlImport(Definition definition, List<Definition> definitions, List<String> locationURIs,
            List<String> importNSs) {
        if (definitions == null) {
            definitions = new ArrayList<Definition>();
            definitions.add(definition);
        }

        if (locationURIs == null) {
            locationURIs = new ArrayList<String>();
        }
        if (importNSs == null) {
            importNSs = new ArrayList<String>();
        }

        if (definition.getImports() != null && !definition.getImports().isEmpty()) {

            Map imports = definition.getImports();
            for (Object key : imports.keySet()) {

                Vector importsImpl = (Vector) imports.get(key);
                for (int i = 0; i < importsImpl.size(); i++) {

                    ImportImpl importImpl = (ImportImpl) importsImpl.get(i);
                    if (!locationURIs.contains(importImpl.getLocationURI()) || !importNSs.contains(importImpl.getNamespaceURI())) {
                        locationURIs.add(importImpl.getLocationURI());
                        importNSs.add(importImpl.getNamespaceURI());

                        String importWsdlFileName = "importWsdl" + definitions.size() + ".wsdl";
                        importImpl.setLocationURI(importWsdlFileName);
                        Definition importDef = importImpl.getDefinition();

                        if (importDef != null) {
                            definitions.add(importDef);
                            findWsdlImport(importDef, definitions, locationURIs, importNSs);
                        }
                    }
                }
            }
        }

        return definitions;
    }

    /**
     * Return the parsed wsdl, it contains all services
     * 
     * @return
     */

    public List<Definition> getDefinitions() {
        return definitions;
    }

}
