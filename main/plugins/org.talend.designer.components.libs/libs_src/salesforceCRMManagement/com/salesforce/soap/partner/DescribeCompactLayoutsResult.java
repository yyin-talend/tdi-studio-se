/**
 * DescribeCompactLayoutsResult.java
 *
 * This file was auto-generated from WSDL by the Apache Axis2 version: 1.6.2 Built on : Apr 17, 2012 (05:34:40 IST)
 */

package com.salesforce.soap.partner;

/**
 * DescribeCompactLayoutsResult bean class
 */
@SuppressWarnings({ "unchecked", "unused" })
public class DescribeCompactLayoutsResult implements org.apache.axis2.databinding.ADBBean {
    /*
     * This type was generated from the piece of schema that had name = DescribeCompactLayoutsResult Namespace URI =
     * urn:partner.soap.sforce.com Namespace Prefix = ns1
     */

    /**
     * field for CompactLayouts This was an Array!
     */

    protected com.salesforce.soap.partner.DescribeCompactLayout[] localCompactLayouts;

    /**
     * Auto generated getter method
     * 
     * @return com.salesforce.soap.partner.DescribeCompactLayout[]
     */
    public com.salesforce.soap.partner.DescribeCompactLayout[] getCompactLayouts() {
        return localCompactLayouts;
    }

    /**
     * validate the array for CompactLayouts
     */
    protected void validateCompactLayouts(com.salesforce.soap.partner.DescribeCompactLayout[] param) {

        if ((param != null) && (param.length < 1)) {
            throw new java.lang.RuntimeException();
        }

    }

    /**
     * Auto generated setter method
     * 
     * @param param CompactLayouts
     */
    public void setCompactLayouts(com.salesforce.soap.partner.DescribeCompactLayout[] param) {

        validateCompactLayouts(param);

        this.localCompactLayouts = param;
    }

    /**
     * Auto generated add method for the array for convenience
     * 
     * @param param com.salesforce.soap.partner.DescribeCompactLayout
     */
    public void addCompactLayouts(com.salesforce.soap.partner.DescribeCompactLayout param) {
        if (localCompactLayouts == null) {
            localCompactLayouts = new com.salesforce.soap.partner.DescribeCompactLayout[] {};
        }

        java.util.List list = org.apache.axis2.databinding.utils.ConverterUtil.toList(localCompactLayouts);
        list.add(param);
        this.localCompactLayouts = (com.salesforce.soap.partner.DescribeCompactLayout[]) list
                .toArray(new com.salesforce.soap.partner.DescribeCompactLayout[list.size()]);

    }

    /**
     * field for DefaultCompactLayoutId
     */

    protected com.salesforce.soap.partner.ID localDefaultCompactLayoutId;

    /**
     * Auto generated getter method
     * 
     * @return com.salesforce.soap.partner.ID
     */
    public com.salesforce.soap.partner.ID getDefaultCompactLayoutId() {
        return localDefaultCompactLayoutId;
    }

    /**
     * Auto generated setter method
     * 
     * @param param DefaultCompactLayoutId
     */
    public void setDefaultCompactLayoutId(com.salesforce.soap.partner.ID param) {

        this.localDefaultCompactLayoutId = param;

    }

    /**
     * field for RecordTypeCompactLayoutMappings This was an Array!
     */

    protected com.salesforce.soap.partner.RecordTypeCompactLayoutMapping[] localRecordTypeCompactLayoutMappings;

    /**
     * Auto generated getter method
     * 
     * @return com.salesforce.soap.partner.RecordTypeCompactLayoutMapping[]
     */
    public com.salesforce.soap.partner.RecordTypeCompactLayoutMapping[] getRecordTypeCompactLayoutMappings() {
        return localRecordTypeCompactLayoutMappings;
    }

    /**
     * validate the array for RecordTypeCompactLayoutMappings
     */
    protected void validateRecordTypeCompactLayoutMappings(com.salesforce.soap.partner.RecordTypeCompactLayoutMapping[] param) {

        if ((param != null) && (param.length < 1)) {
            throw new java.lang.RuntimeException();
        }

    }

    /**
     * Auto generated setter method
     * 
     * @param param RecordTypeCompactLayoutMappings
     */
    public void setRecordTypeCompactLayoutMappings(com.salesforce.soap.partner.RecordTypeCompactLayoutMapping[] param) {

        validateRecordTypeCompactLayoutMappings(param);

        this.localRecordTypeCompactLayoutMappings = param;
    }

    /**
     * Auto generated add method for the array for convenience
     * 
     * @param param com.salesforce.soap.partner.RecordTypeCompactLayoutMapping
     */
    public void addRecordTypeCompactLayoutMappings(com.salesforce.soap.partner.RecordTypeCompactLayoutMapping param) {
        if (localRecordTypeCompactLayoutMappings == null) {
            localRecordTypeCompactLayoutMappings = new com.salesforce.soap.partner.RecordTypeCompactLayoutMapping[] {};
        }

        java.util.List list = org.apache.axis2.databinding.utils.ConverterUtil.toList(localRecordTypeCompactLayoutMappings);
        list.add(param);
        this.localRecordTypeCompactLayoutMappings = (com.salesforce.soap.partner.RecordTypeCompactLayoutMapping[]) list
                .toArray(new com.salesforce.soap.partner.RecordTypeCompactLayoutMapping[list.size()]);

    }

    /**
     *
     * @param parentQName
     * @param factory
     * @return org.apache.axiom.om.OMElement
     */
    public org.apache.axiom.om.OMElement getOMElement(final javax.xml.namespace.QName parentQName,
            final org.apache.axiom.om.OMFactory factory) throws org.apache.axis2.databinding.ADBException {

        org.apache.axiom.om.OMDataSource dataSource = new org.apache.axis2.databinding.ADBDataSource(this, parentQName);
        return factory.createOMElement(dataSource, parentQName);

    }

    public void serialize(final javax.xml.namespace.QName parentQName, javax.xml.stream.XMLStreamWriter xmlWriter)
            throws javax.xml.stream.XMLStreamException, org.apache.axis2.databinding.ADBException {
        serialize(parentQName, xmlWriter, false);
    }

    public void serialize(final javax.xml.namespace.QName parentQName, javax.xml.stream.XMLStreamWriter xmlWriter,
            boolean serializeType) throws javax.xml.stream.XMLStreamException, org.apache.axis2.databinding.ADBException {

        java.lang.String prefix = null;
        java.lang.String namespace = null;

        prefix = parentQName.getPrefix();
        namespace = parentQName.getNamespaceURI();
        writeStartElement(prefix, namespace, parentQName.getLocalPart(), xmlWriter);

        if (serializeType) {

            java.lang.String namespacePrefix = registerPrefix(xmlWriter, "urn:partner.soap.sforce.com");
            if ((namespacePrefix != null) && (namespacePrefix.trim().length() > 0)) {
                writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "type", namespacePrefix
                        + ":DescribeCompactLayoutsResult", xmlWriter);
            } else {
                writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "type", "DescribeCompactLayoutsResult",
                        xmlWriter);
            }

        }

        if (localCompactLayouts != null) {
            for (int i = 0; i < localCompactLayouts.length; i++) {
                if (localCompactLayouts[i] != null) {
                    localCompactLayouts[i].serialize(new javax.xml.namespace.QName("urn:partner.soap.sforce.com",
                            "compactLayouts"), xmlWriter);
                } else {

                    throw new org.apache.axis2.databinding.ADBException("compactLayouts cannot be null!!");

                }

            }
        } else {

            throw new org.apache.axis2.databinding.ADBException("compactLayouts cannot be null!!");

        }

        if (localDefaultCompactLayoutId == null) {
            throw new org.apache.axis2.databinding.ADBException("defaultCompactLayoutId cannot be null!!");
        }
        localDefaultCompactLayoutId.serialize(new javax.xml.namespace.QName("urn:partner.soap.sforce.com",
                "defaultCompactLayoutId"), xmlWriter);

        if (localRecordTypeCompactLayoutMappings != null) {
            for (int i = 0; i < localRecordTypeCompactLayoutMappings.length; i++) {
                if (localRecordTypeCompactLayoutMappings[i] != null) {
                    localRecordTypeCompactLayoutMappings[i].serialize(new javax.xml.namespace.QName(
                            "urn:partner.soap.sforce.com", "recordTypeCompactLayoutMappings"), xmlWriter);
                } else {

                    throw new org.apache.axis2.databinding.ADBException("recordTypeCompactLayoutMappings cannot be null!!");

                }

            }
        } else {

            throw new org.apache.axis2.databinding.ADBException("recordTypeCompactLayoutMappings cannot be null!!");

        }

        xmlWriter.writeEndElement();

    }

    private static java.lang.String generatePrefix(java.lang.String namespace) {
        if (namespace.equals("urn:partner.soap.sforce.com")) {
            return "ns1";
        }
        return org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
    }

    /**
     * Utility method to write an element start tag.
     */
    private void writeStartElement(java.lang.String prefix, java.lang.String namespace, java.lang.String localPart,
            javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {
        java.lang.String writerPrefix = xmlWriter.getPrefix(namespace);
        if (writerPrefix != null) {
            xmlWriter.writeStartElement(namespace, localPart);
        } else {
            if (namespace.length() == 0) {
                prefix = "";
            } else if (prefix == null) {
                prefix = generatePrefix(namespace);
            }

            xmlWriter.writeStartElement(prefix, localPart, namespace);
            xmlWriter.writeNamespace(prefix, namespace);
            xmlWriter.setPrefix(prefix, namespace);
        }
    }

    /**
     * Util method to write an attribute with the ns prefix
     */
    private void writeAttribute(java.lang.String prefix, java.lang.String namespace, java.lang.String attName,
            java.lang.String attValue, javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {
        if (xmlWriter.getPrefix(namespace) == null) {
            xmlWriter.writeNamespace(prefix, namespace);
            xmlWriter.setPrefix(prefix, namespace);
        }
        xmlWriter.writeAttribute(namespace, attName, attValue);
    }

    /**
     * Util method to write an attribute without the ns prefix
     */
    private void writeAttribute(java.lang.String namespace, java.lang.String attName, java.lang.String attValue,
            javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {
        if (namespace.equals("")) {
            xmlWriter.writeAttribute(attName, attValue);
        } else {
            registerPrefix(xmlWriter, namespace);
            xmlWriter.writeAttribute(namespace, attName, attValue);
        }
    }

    /**
     * Util method to write an attribute without the ns prefix
     */
    private void writeQNameAttribute(java.lang.String namespace, java.lang.String attName, javax.xml.namespace.QName qname,
            javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {

        java.lang.String attributeNamespace = qname.getNamespaceURI();
        java.lang.String attributePrefix = xmlWriter.getPrefix(attributeNamespace);
        if (attributePrefix == null) {
            attributePrefix = registerPrefix(xmlWriter, attributeNamespace);
        }
        java.lang.String attributeValue;
        if (attributePrefix.trim().length() > 0) {
            attributeValue = attributePrefix + ":" + qname.getLocalPart();
        } else {
            attributeValue = qname.getLocalPart();
        }

        if (namespace.equals("")) {
            xmlWriter.writeAttribute(attName, attributeValue);
        } else {
            registerPrefix(xmlWriter, namespace);
            xmlWriter.writeAttribute(namespace, attName, attributeValue);
        }
    }

    /**
     * method to handle Qnames
     */

    private void writeQName(javax.xml.namespace.QName qname, javax.xml.stream.XMLStreamWriter xmlWriter)
            throws javax.xml.stream.XMLStreamException {
        java.lang.String namespaceURI = qname.getNamespaceURI();
        if (namespaceURI != null) {
            java.lang.String prefix = xmlWriter.getPrefix(namespaceURI);
            if (prefix == null) {
                prefix = generatePrefix(namespaceURI);
                xmlWriter.writeNamespace(prefix, namespaceURI);
                xmlWriter.setPrefix(prefix, namespaceURI);
            }

            if (prefix.trim().length() > 0) {
                xmlWriter.writeCharacters(prefix + ":" + org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
            } else {
                // i.e this is the default namespace
                xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
            }

        } else {
            xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
        }
    }

    private void writeQNames(javax.xml.namespace.QName[] qnames, javax.xml.stream.XMLStreamWriter xmlWriter)
            throws javax.xml.stream.XMLStreamException {

        if (qnames != null) {
            // we have to store this data until last moment since it is not possible to write any
            // namespace data after writing the charactor data
            java.lang.StringBuffer stringToWrite = new java.lang.StringBuffer();
            java.lang.String namespaceURI = null;
            java.lang.String prefix = null;

            for (int i = 0; i < qnames.length; i++) {
                if (i > 0) {
                    stringToWrite.append(" ");
                }
                namespaceURI = qnames[i].getNamespaceURI();
                if (namespaceURI != null) {
                    prefix = xmlWriter.getPrefix(namespaceURI);
                    if ((prefix == null) || (prefix.length() == 0)) {
                        prefix = generatePrefix(namespaceURI);
                        xmlWriter.writeNamespace(prefix, namespaceURI);
                        xmlWriter.setPrefix(prefix, namespaceURI);
                    }

                    if (prefix.trim().length() > 0) {
                        stringToWrite.append(prefix).append(":")
                                .append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
                    } else {
                        stringToWrite.append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
                    }
                } else {
                    stringToWrite.append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
                }
            }
            xmlWriter.writeCharacters(stringToWrite.toString());
        }

    }

    /**
     * Register a namespace prefix
     */
    private java.lang.String registerPrefix(javax.xml.stream.XMLStreamWriter xmlWriter, java.lang.String namespace)
            throws javax.xml.stream.XMLStreamException {
        java.lang.String prefix = xmlWriter.getPrefix(namespace);
        if (prefix == null) {
            prefix = generatePrefix(namespace);
            javax.xml.namespace.NamespaceContext nsContext = xmlWriter.getNamespaceContext();
            while (true) {
                java.lang.String uri = nsContext.getNamespaceURI(prefix);
                if (uri == null || uri.length() == 0) {
                    break;
                }
                prefix = org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
            }
            xmlWriter.writeNamespace(prefix, namespace);
            xmlWriter.setPrefix(prefix, namespace);
        }
        return prefix;
    }

    /**
     * databinding method to get an XML representation of this object
     *
     */
    public javax.xml.stream.XMLStreamReader getPullParser(javax.xml.namespace.QName qName)
            throws org.apache.axis2.databinding.ADBException {

        java.util.ArrayList elementList = new java.util.ArrayList();
        java.util.ArrayList attribList = new java.util.ArrayList();

        if (localCompactLayouts != null) {
            for (int i = 0; i < localCompactLayouts.length; i++) {

                if (localCompactLayouts[i] != null) {
                    elementList.add(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "compactLayouts"));
                    elementList.add(localCompactLayouts[i]);
                } else {

                    throw new org.apache.axis2.databinding.ADBException("compactLayouts cannot be null !!");

                }

            }
        } else {

            throw new org.apache.axis2.databinding.ADBException("compactLayouts cannot be null!!");

        }

        elementList.add(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "defaultCompactLayoutId"));

        if (localDefaultCompactLayoutId == null) {
            throw new org.apache.axis2.databinding.ADBException("defaultCompactLayoutId cannot be null!!");
        }
        elementList.add(localDefaultCompactLayoutId);

        if (localRecordTypeCompactLayoutMappings != null) {
            for (int i = 0; i < localRecordTypeCompactLayoutMappings.length; i++) {

                if (localRecordTypeCompactLayoutMappings[i] != null) {
                    elementList.add(new javax.xml.namespace.QName("urn:partner.soap.sforce.com",
                            "recordTypeCompactLayoutMappings"));
                    elementList.add(localRecordTypeCompactLayoutMappings[i]);
                } else {

                    throw new org.apache.axis2.databinding.ADBException("recordTypeCompactLayoutMappings cannot be null !!");

                }

            }
        } else {

            throw new org.apache.axis2.databinding.ADBException("recordTypeCompactLayoutMappings cannot be null!!");

        }

        return new org.apache.axis2.databinding.utils.reader.ADBXMLStreamReaderImpl(qName, elementList.toArray(),
                attribList.toArray());

    }

    /**
     * Factory class that keeps the parse method
     */
    public static class Factory {

        /**
         * static method to create the object Precondition: If this object is an element, the current or next start element starts this
         * object and any intervening reader events are ignorable If this object is not an element, it is a complex type and the reader is
         * at the event just after the outer start element Postcondition: If this object is an element, the reader is positioned at its end
         * element If this object is a complex type, the reader is positioned at the end element of its outer element
         */
        public static DescribeCompactLayoutsResult parse(javax.xml.stream.XMLStreamReader reader) throws java.lang.Exception {
            DescribeCompactLayoutsResult object = new DescribeCompactLayoutsResult();

            int event;
            java.lang.String nillableValue = null;
            java.lang.String prefix = "";
            java.lang.String namespaceuri = "";
            try {

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance", "type") != null) {
                    java.lang.String fullTypeName = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance", "type");
                    if (fullTypeName != null) {
                        java.lang.String nsPrefix = null;
                        if (fullTypeName.indexOf(":") > -1) {
                            nsPrefix = fullTypeName.substring(0, fullTypeName.indexOf(":"));
                        }
                        nsPrefix = nsPrefix == null ? "" : nsPrefix;

                        java.lang.String type = fullTypeName.substring(fullTypeName.indexOf(":") + 1);

                        if (!"DescribeCompactLayoutsResult".equals(type)) {
                            // find namespace for the prefix
                            java.lang.String nsUri = reader.getNamespaceContext().getNamespaceURI(nsPrefix);
                            return (DescribeCompactLayoutsResult) com.salesforce.soap.partner.sobject.ExtensionMapper
                                    .getTypeObject(nsUri, type, reader);
                        }

                    }

                }

                // Note all attributes that were handled. Used to differ normal attributes
                // from anyAttributes.
                java.util.Vector handledAttributes = new java.util.Vector();

                reader.next();

                java.util.ArrayList list1 = new java.util.ArrayList();

                java.util.ArrayList list3 = new java.util.ArrayList();

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement()
                        && new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "compactLayouts")
                                .equals(reader.getName())) {

                    // Process the array and step past its final element's end.
                    list1.add(com.salesforce.soap.partner.DescribeCompactLayout.Factory.parse(reader));

                    // loop until we find a start element that is not part of this array
                    boolean loopDone1 = false;
                    while (!loopDone1) {
                        // We should be at the end element, but make sure
                        while (!reader.isEndElement())
                            reader.next();
                        // Step out of this element
                        reader.next();
                        // Step to next element event.
                        while (!reader.isStartElement() && !reader.isEndElement())
                            reader.next();
                        if (reader.isEndElement()) {
                            // two continuous end elements means we are exiting the xml structure
                            loopDone1 = true;
                        } else {
                            if (new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "compactLayouts").equals(reader
                                    .getName())) {
                                list1.add(com.salesforce.soap.partner.DescribeCompactLayout.Factory.parse(reader));

                            } else {
                                loopDone1 = true;
                            }
                        }
                    }
                    // call the converter utility to convert and set the array

                    object.setCompactLayouts((com.salesforce.soap.partner.DescribeCompactLayout[]) org.apache.axis2.databinding.utils.ConverterUtil
                            .convertToArray(com.salesforce.soap.partner.DescribeCompactLayout.class, list1));

                }  // End of if for expected property start element

                else {
                    // A start element we are not expecting indicates an invalid parameter was passed
                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getName());
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement()
                        && new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "defaultCompactLayoutId").equals(reader
                                .getName())) {

                    object.setDefaultCompactLayoutId(com.salesforce.soap.partner.ID.Factory.parse(reader));

                    reader.next();

                }  // End of if for expected property start element

                else {
                    // A start element we are not expecting indicates an invalid parameter was passed
                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getName());
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement()
                        && new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "recordTypeCompactLayoutMappings")
                                .equals(reader.getName())) {

                    // Process the array and step past its final element's end.
                    list3.add(com.salesforce.soap.partner.RecordTypeCompactLayoutMapping.Factory.parse(reader));

                    // loop until we find a start element that is not part of this array
                    boolean loopDone3 = false;
                    while (!loopDone3) {
                        // We should be at the end element, but make sure
                        while (!reader.isEndElement())
                            reader.next();
                        // Step out of this element
                        reader.next();
                        // Step to next element event.
                        while (!reader.isStartElement() && !reader.isEndElement())
                            reader.next();
                        if (reader.isEndElement()) {
                            // two continuous end elements means we are exiting the xml structure
                            loopDone3 = true;
                        } else {
                            if (new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "recordTypeCompactLayoutMappings")
                                    .equals(reader.getName())) {
                                list3.add(com.salesforce.soap.partner.RecordTypeCompactLayoutMapping.Factory.parse(reader));

                            } else {
                                loopDone3 = true;
                            }
                        }
                    }
                    // call the converter utility to convert and set the array

                    object.setRecordTypeCompactLayoutMappings((com.salesforce.soap.partner.RecordTypeCompactLayoutMapping[]) org.apache.axis2.databinding.utils.ConverterUtil
                            .convertToArray(com.salesforce.soap.partner.RecordTypeCompactLayoutMapping.class, list3));

                }  // End of if for expected property start element

                else {
                    // A start element we are not expecting indicates an invalid parameter was passed
                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getName());
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement())
                    // A start element we are not expecting indicates a trailing invalid property
                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getName());

            } catch (javax.xml.stream.XMLStreamException e) {
                throw new java.lang.Exception(e);
            }

            return object;
        }

    }// end of factory class

}
